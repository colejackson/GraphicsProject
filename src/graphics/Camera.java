package graphics;

import java.awt.geom.Line2D;
import java.util.ArrayList;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import driver.GlobalPosition;
import driver.OGL;
import maze.Side;
import maze.Wall;

public class Camera
{
	private GL2 gl;
	private GLU glu;
	private ArrayList<Wall> buffer;
	private ArrayList<Wall> collision;
	
	private boolean bufferSet = false;

	//Position the camera is at
	private double posX;
	private double posY;
	private double posZ;
	
	//Location the camera is looking at
	private double lookX;
	private double lookY;
	private double lookZ;
	
	//Up vector
	private double upX;
	private double upY;
	private double upZ;
	
	// Radius and Degree of the view
	private double radius = 0;
	private double degree = 30;
	
	private float alpha = 0f;
	
	//Creates a camera object
	public Camera()
	{	
		// Set the GL variables and initiate the view.
		this.glu = OGL.glu;
		this.gl = OGL.gl;
		
		init();
		
		// Set initial position
		posX = 0.0;
		posY = 0.98;
		posZ = 0.1;
		
		// Set initial look
		lookX = 0.0;
		lookY = -0.02;
		lookZ = 0.14;
		
		// Set initial up vector
		upX = 0.0;
		upY = 0.0;
		upZ = 1.0;
		
		// Set the radus of the view.
		radius = Math.sqrt((Math.pow((lookX-posX), 2) + Math.pow((lookY-posY), 2)));;
		
		// Set the camera.
		set();

	}
	
	//When the director has a request this  processes it
	public void command(String s)
	{
		// Split the command string
		String[] commands = s.split(":");
		
		// Process the string to match the correct private method, one for each command we want to accept.
		if(commands[0].equals("z"))
		{
			moveZ(Double.parseDouble(commands[1]));
		}
		else if(commands[0].equals("f"))
		{
			moveForward(Double.parseDouble(commands[1]));
		}
		else if(commands[0].equals("r"))
		{
			rotate(Double.parseDouble(commands[1]));
		}
		else if(commands[0].equals("a"))
		{
			adjustAlpha(Double.parseDouble(commands[1]));
		}
		else if(commands[0].equals("u"))
		{
			up(Double.parseDouble(commands[1]));
		}
	}
	
	public float getAlpha()
	{
		return alpha;
	}

	public double[] getLook()
	{
		return new double[] {lookX, lookY, lookZ};
	}
	
	public double[] getPosition()
	{
		return new double[] {posX, posY, posZ};
	}
	
	public void setBuffer(ArrayList<Wall> al)
	{
		this.buffer = al;
		bufferSet = true;
	}
	
	// Set the initial view factors.
	private void init() 
	{		

		// Set the view with frustrum
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(100.0, .5, 0.001, 10.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glClearColor(.66f, .88f, 1.0f, 0.0f);

	}
	
	private void up(double degree)
	{
		posZ += degree;
		lookZ += degree;
		
		if(posZ <= 0 || lookZ <= 0)
		{
			posZ = .01;
			lookZ = .01;
		}
				
		set();
	}
	
	private void adjustAlpha(double degree)
	{		
		if((double)alpha + degree > 1)
			alpha = 1.0f;
		else if((double)alpha + degree < 0)
			alpha = 0.0f;
		else
			alpha += (float)degree;
	}
	
	// Move the camera forward based on the current look vector.
	private void moveForward(double degree)
	{
		// Build the look at vector
		double vecX = lookX-posX;
		double vecY = lookY-posY;
		
		// Make this vector fit the degree we are moving forward (% of the total vector)
		vecX *= (degree);
		vecY *= (degree);
		
		if(willCollide(posX, posY, posX + (5 * vecX), posY + (5 * vecY)) && posZ < 0.3)
		{
			return;
		}
			
		// Adjust the x values
		lookX += vecX;
		posX += vecX;
		
		// Adjust the y values
		lookY += vecY;
		posY += vecY;
		
		set();
	}
	
	// Move the z value around and set the camera.
	private void moveZ(double z)
	{	
		posZ += z;
		
		if(posZ <= 0)
		{
			posZ = .01;
		}
				
		set();
	}
	
	// Rotate (Pivot) the camera
	private void rotate(double d)
	{
		// Set the degree of rotation and 2*pi
		double twoPi = 2 * Math.PI;	
		double deg = Math.toRadians(degree = (degree += d)%360);

		// The x and y move in a circle based on the radius.
		lookX = radius * Math.sin(deg * twoPi) + posX;
		lookY = radius * Math.cos(deg * twoPi) + posY;
		
		// Set the camera.
		set();
	}
	
	// Call this method every time lookat should be called.
	private void set()
	{
		gl.glLoadIdentity();
		glu.gluLookAt(posX, posY, posZ, lookX, lookY, lookZ, upX, upY, upZ);
		
		GlobalPosition.pos.setLocation(posX, posY);
	}		
	
	private boolean willCollide(double x1, double y1, double x2, double y2) 
	{
		if(!bufferSet)
			return false;
		
		collision = buffer;
		
		for(Wall w : collision)
		{
			for(Side s : w)
			{
				if(!s.isTop)
				{
					if(Line2D.linesIntersect(x1, y1, x2, y2, s.x1, s.y1, s.x2, s.y2))
					{					
						return true;
					}
				}	
			}
		}
		
		return false;
	}
}
