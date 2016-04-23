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
		double z = 0.000;
		
		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad, false);
		
		OGL.gl.glColor4f(0.965f, 0.946f, 0.883f, 1.0f);
		OGL.gl.glPushMatrix();
		OGL.gl.glTranslated(x, y, z);
		glu.gluCylinder(quad, 0.0012f, 0.0012f, 0.025f, 5, 5);
		OGL.gl.glPopMatrix();
		OGL.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		glu.gluDeleteQuadric(quad);
	}
	
	public void drawFlame(GLU glu, Texture fireTexture, double wind)
	{
		double z = 0.026;
		
		//Enable fire texture
		fireTexture.enable(OGL.gl);
		fireTexture.bind(OGL.gl);
		
		//Draw flame of candle (pyramid)
		OGL.gl.glColor4f(0.996f, 0.663f, 0.235f, 1.0f);
		OGL.gl.glBegin(GL2.GL_TRIANGLES);
		
		//front side
		OGL.gl.glTexCoord3d(0,1,0);
		OGL.gl.glVertex3d(-0.0007+x,0.0007+y,z);
		OGL.gl.glTexCoord3d(0,0,1);
		OGL.gl.glVertex3d(wind+x,y,z+0.005);
		OGL.gl.glTexCoord3d(1,0,0);
		OGL.gl.glVertex3d(0.0007+x,0.0007+y,z);
		
		//left side
		OGL.gl.glTexCoord3d(0,1,0);
		OGL.gl.glVertex3d(-0.0007+x,-0.0007+y,z);
		OGL.gl.glTexCoord3d(0,0,1);
		OGL.gl.glVertex3d(wind+x,y,z+0.005);
		OGL.gl.glTexCoord3d(1,0,0);
		OGL.gl.glVertex3d(-0.0007+x,0.0007+y,z);
		
		//right side
		OGL.gl.glTexCoord3d(0,1,0);
		OGL.gl.glVertex3d(0.0007+x,-0.0007+y,z);
		OGL.gl.glTexCoord3d(0,0,1);
		OGL.gl.glVertex3d(wind+x,y,z+0.005);
		OGL.gl.glTexCoord3d(1,0,0);
		OGL.gl.glVertex3d(0.0007+x,0.0007+y,z);
		
		//back side
		OGL.gl.glTexCoord3d(0,1,0);
		OGL.gl.glVertex3d(-0.0007+x,-0.0007+y,z);
		OGL.gl.glTexCoord3d(0,0,1);
		OGL.gl.glVertex3d(wind+x,y,z+0.005);
		OGL.gl.glTexCoord3d(1,0,0);
		OGL.gl.glVertex3d(0.0007+x,-0.0007+y,z);
		
		OGL.gl.glEnd();
		
		
		//Draw flame of candle (sphere)
		GLUquadric quad2 = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad2, true);

		OGL.gl.glPushMatrix();
		OGL.gl.glTranslated(x, y, z);
		OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
		glu.gluSphere(quad2, 0.00095, 4, 4);		
		OGL.gl.glPopMatrix();
		
		glu.gluDeleteQuadric(quad2);
		
		//Disable the fire texture
		fireTexture.disable(OGL.gl);
		OGL.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public void drawFlicker(GLU glu, int flicker)
	{
		double z = 0.029;
		
		GLUquadric quad3 = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad3, false);
		
		//Draw 3 spheres, center being darkest
		if (flicker == 0)
		{
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, z);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0030, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.08f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, z);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0060, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.04f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, z);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
		//Draw 2 spheres center being darkest
		else if (flicker == 1)
		{	
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, z);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0060, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.08f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, z);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
		//Draw 1 sphere
		else
		{
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslated(x, y, z);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad3, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
		
		glu.gluDeleteQuadric(quad3);
		OGL.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public void drawStand(GLU glu, Texture goldTexture)
	{
		goldTexture.enable(OGL.gl);
		goldTexture.bind(OGL.gl);
		
		GLUquadric quad4 = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad4, true);
		
		//OGL.gl.glColor4f(0.827f, 0.776f, 0.302f, 1.0f);
		OGL.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		//Stand design 1
		OGL.gl.glPushMatrix();
		OGL.gl.glTranslated(x, y, 0.0);
		glu.gluCylinder(quad4, 0.002f, 0.0035f, 0.0035f, 7, 7);
		OGL.gl.glPopMatrix();
		
		OGL.gl.glPushMatrix();
		OGL.gl.glTranslated(x, y, 0.001);
		glu.gluDisk(quad4, 0.0, 0.0022, 7, 7);
		OGL.gl.glPopMatrix();
		
		//Stand design 2
//		OGL.gl.glPushMatrix();
//		OGL.gl.glTranslated(x, y, 0.0);
//		glu.gluCylinder(quad4, 0.0005f, 0.0035f, 0.003f, 7, 7);
//		OGL.gl.glPopMatrix();
//		
//		OGL.gl.glPushMatrix();
//		OGL.gl.glTranslated(x, y, 0.0);
//		glu.gluCylinder(quad4, 0.0005f, 0.0005f, 0.009f, 7, 7);
//		OGL.gl.glPopMatrix();
//		
//		OGL.gl.glPushMatrix();
//		OGL.gl.glTranslated(x, y, 0.009);
//		glu.gluDisk(quad4, 0.0, 0.002, 7, 7);
//		OGL.gl.glPopMatrix();
		
		//Stand design 3
//		OGL.gl.glPushMatrix();
//		OGL.gl.glTranslated(x, y, 0.0);
//		glu.gluCylinder(quad4, 0.002f, 0.0005f, 0.002f, 7, 7);
//		OGL.gl.glPopMatrix();
//		
//		OGL.gl.glPushMatrix();
//		OGL.gl.glTranslated(x, y, 0.002);
//		glu.gluCylinder(quad4, 0.0005f, 0.0005f, 0.009f, 7, 7);
//		OGL.gl.glPopMatrix();
//		
//		OGL.gl.glPushMatrix();
//		OGL.gl.glTranslated(x, y, 0.009);
//		glu.gluDisk(quad4, 0.0, 0.0025, 7, 7);
//		OGL.gl.glPopMatrix();

		glu.gluDeleteQuadric(quad4);
		goldTexture.disable(OGL.gl);
		OGL.gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
