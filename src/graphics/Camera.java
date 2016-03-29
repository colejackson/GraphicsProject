package graphics;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import async.WorkerReady;

public class Camera implements WorkerReady
{
	private GL2 gl;
	private GLU glu;
	private ArrayList<double[]> buffer;
	private ArrayList<double[]> collision;
	
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
	
	//Creates a camera object
	public Camera(GLU glu, GL2 gl)
	{	
		// Set the GL variables and initiate the view.
		this.glu = glu;
		this.gl = gl;
		
		init();
		
		// Set initial position
		posX = 0.0;
		posY = 0.98;
		posZ = 0.1;
		
		// Set initial look
		lookX = 0.0;
		lookY = -0.02;
		lookZ = 0.1;
		
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
	
	// Call this method every time lookat should be called.
	private void set()
	{
		gl.glLoadIdentity();
		glu.gluLookAt(posX, posY, posZ, lookX, lookY, lookZ, upX, upY, upZ);
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
	
	public double[] getPosition()
	{
		return new double[] {posX, posY, posZ};
	}
	
	public double[] getLook()
	{
		return new double[] {lookX, lookY, lookZ};
	}
	
	private boolean willCollide(double x1, double y1, double x2, double y2) 
	{
		if(!bufferSet)
			return false;
		
		collision = buffer;
		
		for(double[] da : collision)
		{
			if(da.length < 7)
			{
				if(Line2D.linesIntersect(x1, y1, x2, y2, da[0], da[1], da[2], da[3]))
				{					
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public void setBuffer(Collection<?> c)
	{
		if(c instanceof ArrayList<?>)
		{
			this.buffer = (ArrayList<double[]>)c;
			bufferSet = true;
		}
		else
		{
			System.err.println("The Buffer Passed to " + this.getClass().toGenericString() + " was not a compatible type.");
		}
	}
}
