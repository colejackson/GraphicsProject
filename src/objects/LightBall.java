package objects;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;

import driver.OGL;
import driver.Utilities;
import maze.Side;
import maze.Wall;

public class LightBall extends Double 
{
	private static final long serialVersionUID = 2011551764811644298L;

	private double angle;
	private double speed;
	private double radius;
	
	private ArrayList<Wall> buffer = null;
	
	public LightBall(double x, double y)
	{
		super(x,y);
		
		angle = Math.random() * 360;
		speed = .0009;
		radius = .01;
	}
	
	public void changeSpeed(double d)
	{
		speed *= d;
	}
	
	public void setAngle(double d)
	{
		angle = d % 360;
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void setBuffer(ArrayList<Wall> buffer)
	{
		this.buffer = buffer;
	}
	
	public void draw()
	{
		updateLoc();
		
		OGL.gl.glColor3f(0.3f, 0.6f, 0.3f);
		
		OGL.gl.glPushMatrix();

		GLUquadric quad = OGL.glu.gluNewQuadric();
		OGL.glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);
		OGL.glu.gluQuadricTexture(quad, true); 

		OGL.gl.glTranslated(this.getX(), this.getY(), 0.1);
		OGL.gl.glScaled(1.0, 1.0, 2.0);
		
		OGL.glu.gluSphere(quad, radius, 32, 32);
		
		OGL.gl.glPopMatrix();		
	}
	
	private void updateLoc()
	{
		double twoPi = Math.PI * 2;
		double newX = Math.cos(twoPi*(angle/360))*speed + getX();
		double newY = Math.sin(twoPi*(angle/360))*speed + getY();
		
		if(willCollide(newX, newY))
		{
			return;
		}
		
		this.setLocation(newX, newY);
	}
	
	private boolean willCollide(double x, double y)
	{
		if(buffer == null)
			return false;
		
		for(Wall w : buffer)
		{
			for(Side s : w)
			{
				System.out.println(getX() + " : " + getY() + " : " + x + " : " + y + " : " + s.x1 + " : " + s.y1 + " : " + s.x2 + " : " + s.y2);
				
				if(!s.isTop && new Line2D.Double(getX(), getY(), x, y).intersectsLine(new Line2D.Double(s.x1, s.y1, s.x2, s.y2)));
				{
					double reverse = ((Utilities.getAngle(this, new Point2D.Double(x, y)) + 180) % 360) + 360;
					double normal = ((Utilities.getAngle(new Point2D.Double(s.x1, s.y1), new Point2D.Double(s.x2, s.y2)) + 90) % 360) + 360;
					
					System.out.println("\n-------------------------");
					
					System.out.println("Angle: " + angle);
					System.out.println("Reverse: " + reverse);
					System.out.println("Normal: " + normal + "\n");
					
					if(Math.max(reverse, normal) - Math.min(reverse, normal) > 180.0)
						normal = ((normal + 180.0) % 360.0) + 360.0;
					
					System.out.println("Angle: " + angle);
					System.out.println("Reverse: " + reverse);
					System.out.println("Normal: " + normal + "\n");
					
					if(normal > reverse)
						setAngle(angle + (2.0 * (normal - reverse)));
					else
						setAngle(angle - (2.0 * (reverse - normal)));
					
					System.out.println("Angle: " + angle);
					System.out.println("Reverse: " + reverse);
					System.out.println("Normal: " + normal);
					
					System.out.println("-------------------------\n");
					
					return true;
				}
			}
		}
		
		return false;
	}
}
