package graphics;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

public abstract class Scenery 
{
	private final static int CIRCLE_SIDES = 32;
	
	private final static double TREE_HEIGHT = 0.8;
	private final static double TREE_RADIUS = 0.2;
	
	public static void drawTree(double x, double y, GL2 gl)
	{
		gl.glColor3d(1.0, .55, .55);
				
		drawCylinder(x, y, TREE_HEIGHT, TREE_RADIUS, gl);
	}
	
	public static void drawCylinder(double x, double y, double h, double r, GL2 gl)
	{
		double twoPi = Math.PI * 2;
		
		for(int i = 0; i < CIRCLE_SIDES; i++)
		{
			double x1 = (r * Math.sin((i/CIRCLE_SIDES) * twoPi)) + x;
			double y1 = (r * Math.cos((i/CIRCLE_SIDES) * twoPi)) + y;
			
			double x2 = (r * Math.sin(((i+1)/CIRCLE_SIDES) * twoPi)) + x;
			double y2 = (r * Math.cos(((i+1)/CIRCLE_SIDES) * twoPi)) + y;
						
			gl.glBegin(GL.GL_TRIANGLES);
			
			gl.glVertex3d(x, y, 0.0);
			gl.glVertex3d(x1, y1, 0.0);
			gl.glVertex3d(x2, y2, 0.0);

			gl.glVertex3d(x, y, h);
			gl.glVertex3d(x1, y1, h);
			gl.glVertex3d(x2, y2, h);

			gl.glEnd();
			
			gl.glBegin(GL2.GL_POLYGON);
			
			gl.glVertex3d(x1, y1, 0.0);
			gl.glVertex3d(x2, y2, 0.0);
			gl.glVertex3d(x2, y2, h);
			gl.glVertex3d(x1, y1, h);

			gl.glEnd();
		}
	}
	
	public static void drawZRect(double x1, double y1, double x2, double y2, double h, GL2 gl)
	{
		gl.glBegin(GL2.GL_POLYGON);
		
		gl.glVertex3d(x1, y1, 0.0);
		gl.glVertex3d(x2, y2, 0.0);
		gl.glVertex3d(x2, y2, h);
		gl.glVertex3d(x1, y1, h);
		
		gl.glEnd();
	}
}
