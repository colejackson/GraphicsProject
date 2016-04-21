package objects;

import com.jogamp.opengl.GL2;

import driver.OGL;

public class TractorBeam 
{
	private double x;
	private double y;
	
	private double color = .55;
	private double factor = .02;
	
	private double rotate = 60000;
	
	public TractorBeam(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void draw()
	{
		for(int i = 0; i < 64; i++)
		{			
			OGL.gl.glBegin(GL2.GL_POLYGON);
			
			double angle1 = (2*Math.PI*i)/64 + ((2*Math.PI*rotate)/60000);
			double angle2 = (2*Math.PI*((i+1)%64)/64) + ((2*Math.PI*rotate)/60000);
			
			for(double d = 0; d < 60.0; d++)
			{			
				OGL.gl.glColor4d(1.0, color, color, .5);
								
				OGL.gl.glVertex3d(((Math.cos(angle1)*0.03 + x)),((Math.sin(angle1)*0.03) + y), d/60.0);
				
				OGL.gl.glColor4d(color, 1.0, color, .5);
				
				OGL.gl.glVertex3d(((Math.cos(angle1)*0.03 + x)),((Math.sin(angle1)*0.03) + y), (d + 1.0)/60.0);
				
				OGL.gl.glColor4d(color, color, 1.0, .5);
				
				OGL.gl.glVertex3d(((Math.cos(angle2)*0.03 + x)),((Math.sin(angle2)*0.03) + y), (d + 1.0)/60.0);
				
				OGL.gl.glColor4d(color, color, color, .5);
				
				OGL.gl.glVertex3d(((Math.cos(angle2)*0.03 + x)),((Math.sin(angle2)*0.03) + y), d/60.0);
				
				if(color >= .88 || color <= .55)
					factor *= -1;
				
				color += factor;
			}
			
			if(color >= .88 || color <= .55)
				factor *= -1;
			
			color += factor*10000;
			
			rotate = (rotate + 1) % 60000;
			
			OGL.gl.glEnd();
			
			this.x += (.0002/60);
			this.y += (.0002/60);
		}	
	}
	
}
