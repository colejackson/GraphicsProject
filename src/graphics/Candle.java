package graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.texture.Texture;

import driver.OGL;

public class Candle
{
	private double x;
	private double y;
	
	public Candle(double xLoc, double yLoc)
	{
		x = xLoc;
		y = yLoc;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}

	public void drawBase(GLU glu)
	{
		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad, false);
		
		OGL.gl.glColor3d(0.965, 0.946, 0.883);
		OGL.gl.glPushMatrix();
		OGL.gl.glTranslated(x, y, 0.0);
		glu.gluCylinder(quad, 0.0013f, 0.0013f, 0.025f, 5, 5);
		OGL.gl.glPopMatrix();
		OGL.gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		glu.gluDeleteQuadric(quad);
	}
	
	public void drawFlame(GLU glu, Texture fireTexture, double wind)
	{
		//Enable fire texture
		fireTexture.enable(OGL.gl);
		fireTexture.bind(OGL.gl);
		
		//Draw flame of candle (pyramid)
		OGL.gl.glColor3d(0.996, 0.663, 0.235);
		OGL.gl.glBegin(GL2.GL_TRIANGLES);
		
		//front side
		OGL.gl.glTexCoord3d(0,1,0);
		OGL.gl.glVertex3d(-0.0007+x,0.0007+y,0.026);
		OGL.gl.glTexCoord3d(0,0,1);
		OGL.gl.glVertex3d(wind+x,y,0.031);
		OGL.gl.glTexCoord3d(1,0,0);
		OGL.gl.glVertex3d(0.0007+x,0.0007+y,0.026);
		
		//left side
		OGL.gl.glTexCoord3d(0,1,0);
		OGL.gl.glVertex3d(-0.0007+x,-0.0007+y,0.026);
		OGL.gl.glTexCoord3d(0,0,1);
		OGL.gl.glVertex3d(wind+x,y,0.031);
		OGL.gl.glTexCoord3d(1,0,0);
		OGL.gl.glVertex3d(-0.0007+x,0.0007+y,0.026);
		
		//right side
		OGL.gl.glTexCoord3d(0,1,0);
		OGL.gl.glVertex3d(0.0007+x,-0.0007+y,0.026);
		OGL.gl.glTexCoord3d(0,0,1);
		OGL.gl.glVertex3d(wind+x,y,0.031);
		OGL.gl.glTexCoord3d(1,0,0);
		OGL.gl.glVertex3d(0.0007+x,0.0007+y,0.026);
		
		//back side
		OGL.gl.glTexCoord3d(0,1,0);
		OGL.gl.glVertex3d(-0.0007+x,-0.0007+y,0.026);
		OGL.gl.glTexCoord3d(0,0,1);
		OGL.gl.glVertex3d(wind+x,y,0.031);
		OGL.gl.glTexCoord3d(1,0,0);
		OGL.gl.glVertex3d(0.0007+x,-0.0007+y,0.026);
		
		OGL.gl.glEnd();
		
		
		//Draw flame of candle (sphere)
		GLUquadric quad2 = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad2, true);

		OGL.gl.glPushMatrix();
		OGL.gl.glTranslated(x, y, 0.026);
		OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
		glu.gluSphere(quad2, 0.00095, 4, 4);		
		OGL.gl.glPopMatrix();
		
		glu.gluDeleteQuadric(quad2);
		
		//Disable the fire texture
		fireTexture.disable(OGL.gl);
	}
	
	public void drawFlicker(GLU glu, int flicker)
	{
		GLUquadric quad3 = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad3, false);
		
		//Draw 3 spheres, center being darkest
		if (flicker == 0)
		{
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, 0.029);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0030, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.08f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, 0.029);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0060, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.04f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, 0.029);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
		//Draw 2 spheres center being darkest
		else if (flicker == 1)
		{	
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, 0.029);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0060, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.08f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, 0.029);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
		//Draw 1 sphere
		else
		{
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, 0.029);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
		
		glu.gluDeleteQuadric(quad3);
	}
}
