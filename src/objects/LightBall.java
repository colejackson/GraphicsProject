package objects;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

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
	private ArrayList<Wall> oldBuffer = null;
	
	public LightBall(double x, double y)
	{
		super(x,y);
		
		angle = Math.random() * 360.0;
		speed = .00054;
		radius = .01;
	}
	
	public void changeSpeed(double d)
	{
		speed *= d;
	}
	
	public void setAngle(double d)
	{
		angle = d % 360.0;
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void setBuffer(ArrayList<Wall> buffer)
	{
		this.oldBuffer = this.buffer;
		this.buffer = buffer;
	}
	
	public ArrayList<Wall> getBuffer()
	{
		ArrayList<Wall> temp = new ArrayList<Wall>();
		
		if(oldBuffer != null)
		{
			return oldBuffer;
		}
		
		return temp;
	}
	
	public void draw(Texture orbTexture)
	{
		updateLoc();
		
		OGL.gl.glColor3f(1.0f, 1.0f, 1.0f);
		orbTexture.enable(OGL.gl);
		orbTexture.bind(OGL.gl);
		
		OGL.gl.glPushMatrix();

		GLUquadric quad = OGL.glu.gluNewQuadric();
		OGL.glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);
		OGL.glu.gluQuadricTexture(quad, true); 

		OGL.gl.glTranslated(this.getX(), this.getY(), 0.1);
		OGL.gl.glScaled(1.0, 1.0, 2.0);
		
		OGL.glu.gluSphere(quad, radius, 32, 32);
		
		OGL.gl.glPopMatrix();

		OGL.glu.gluDeleteQuadric(quad);
		orbTexture.disable(OGL.gl);
	}
	
	private void updateLoc()
	{
		double twoPi = Math.PI * 2.0;
		double newX = Math.cos(twoPi*(angle/360.0))*speed + getX();
		double newY = Math.sin(twoPi*(angle/360.0))*speed + getY();
		
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
				
				if(!s.isTop && new Line2D.Double(getX(), getY(), x, y).intersectsLine(new Line2D.Double(s.x1, s.y1, s.x2, s.y2)))
				{
					double reverse = ((angle + 180.0) % 360.0) + 360.0;
					double normal = ((Utilities.getAngle(new Point2D.Double(s.x1, s.y1), new Point2D.Double(s.x2, s.y2)) + 90.0) % 360.0) + 360.0;
					
					if(Math.max(reverse, normal) - Math.min(reverse, normal) > 180.0)
						normal = ((normal + 180.0) % 360.0) + 360.0;
					
					if(normal > reverse)
						setAngle(reverse + (2.0 * (normal - reverse)));
					else
						setAngle(reverse - (2.0 * (reverse - normal)));
					
					return true;
				}
			}
		}
		
		return false;
	}
}
