package maze;

import java.awt.geom.Point2D;
import java.util.Arrays;

import com.jogamp.opengl.GL2;

import driver.OGL;

public class Side 
{	
	private static GL2 gl = null;
	
	static
	{
		gl = OGL.gl;
	}
	
	public double x1;
	public double y1;
	
	public double x2;
	public double y2;
	
	public boolean isTop;
	
	public double x3;
	public double y3;
	
	public double x4;
	public double y4;
	
	public double z1;
	public double z2;
	
	public Side(double... arr)
	{
		if(arr.length > 6)
		{
			isTop = true;
			
			x1 = arr[0];
			y1 = arr[1];
			x2 = arr[2];
			y2 = arr[3];
			x3 = arr[4];
			y3 = arr[5];
			x4 = arr[6];
			y4 = arr[7];
			z1 = arr[8];
			z2 = arr[8];
		}
		else if(arr.length <= 6)
		{
			isTop = false;
			
			x1 = arr[0];
			y1 = arr[1];
			x2 = arr[2];
			y2 = arr[3];
			z1 = arr[4];
			z2 = arr[5];
		}
	}
	
	public void glDraw()
	{
		gl.glBegin(GL2.GL_POLYGON);
		
		if(isTop)
		{			
			gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
			gl.glTexCoord2d(0,0);
			gl.glVertex3d(x1, y1, z1);
			
			gl.glColor3f(1.0f, 1.0f, 1.0f);			//darken the image a little
			gl.glTexCoord2d(0,1);
			gl.glVertex3d(x2, y2, z1);
			
			gl.glTexCoord2d(1,1);
			gl.glVertex3d(x3, y3, z1);
			
			gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
			gl.glTexCoord2d(1,0);
			gl.glVertex3d(x4, y4, z1);
		}
		else
		{
			gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
			gl.glTexCoord2d(0,0);
			gl.glVertex3d(x1, y1, z1);
			
			gl.glColor3f(1.0f, 1.0f, 1.0f);			//darken the image a little
			gl.glTexCoord2d(0,1);
			gl.glVertex3d(x1, y1, z2);
			
			gl.glTexCoord2d(1,1);
			gl.glVertex3d(x2, y2, z2);
			
			gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
			gl.glTexCoord2d(1,0);
			gl.glVertex3d(x2, y2, z1);
		}
		
		gl.glEnd();
	}
	
	protected Point2D.Double center()
	{
		if(this.isTop)
		{
			double avgx = Arrays.asList(x1, x2, x3, x4).stream().mapToDouble(Double::doubleValue).average().getAsDouble();
			double avgy = Arrays.asList(y1, y2, y3, y4).stream().mapToDouble(Double::doubleValue).average().getAsDouble();
			
			return new Point2D.Double(avgx, avgy);
		}
		else
			return null;
	}

	public double getX1() {
		return x1;
	}

	public double getY1() {
		return y1;
	}

	public double getX2() {
		return x2;
	}

	public double getY2() {
		return y2;
	}

	public double getX3() {
		return x3;
	}

	public double getY3() {
		return y3;
	}

	public double getX4() {
		return x4;
	}

	public double getY4() {
		return y4;
	}

	public double getZ1() {
		return z1;
	}

	public double getZ2() {
		return z2;
	}
}
