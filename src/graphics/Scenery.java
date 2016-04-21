package graphics;

import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import driver.OGL;

public abstract class Scenery 
{	
	private static Camera camera;
	
	private static GLUquadric quad;
	private static GLUquadric quad2;
	
	private static Texture fireTexture;
	
	private final static int NUM_CANDLES = 1000;
	private static double[][] candleCoords = new double[NUM_CANDLES][2];
	private static int candleCount = 0;
	

	public static void initCamera(Camera cam)
	{
		camera = cam;
	}
	
	public static void initTexture()
	{
		fireTexture = createTexture("ImagesOther/fire.jpg");
	}
	
	public static void initQuadrics(GLU glu)
	{
		quad = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad, false);
		
		quad2 = glu.gluNewQuadric();
		//glu.gluQuadricNormals(quad2, glu.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
		glu.gluQuadricTexture(quad2, true); 
	}
	
	public static int getTotalNumCandles()
	{
		return NUM_CANDLES;
	}
	
	public static int getCandleCount()
	{
		return candleCount;
	}
	
	public static void addCandle()
	{
		candleCoords[candleCount] = new double[] {camera.getPosition()[0], camera.getPosition()[1]};
		++candleCount;
	}
	
	public static void removeCandle()
	{
		//If the coordinate of a candle falls within a circle around the current position of the camera
		for (int i = 0; i < candleCount; ++i)
		{
			double candleX = candleCoords[i][0];
			double candleY = candleCoords[i][1];
			double centerX = camera.getPosition()[0];
			double centerY = camera.getPosition()[1];
			double radius = 0.1;
			
			double lhs = ((candleX-centerX)*(candleX-centerX)) + ((candleY-centerY)*(candleY-centerY));
			double rhs = radius*radius;
			
			//If the candle coordinate lies inside the circle around the camera
			if (lhs < rhs)
			{
				//Pick it up
				//I.e. remove its coordinate from the array of candle coordinates
				for (int j = i; j < candleCount-1; ++j)
				{
					candleCoords[j] = candleCoords[j+1];
				}
				candleCoords[candleCount-1] = null;
				--candleCount;
			}	
		}
	}
	
	public static void drawZRect(double x1, double y1, double x2, double y2, double h, GL2 gl)
	{
		OGL.gl.glBegin(GL2.GL_POLYGON);
		
		OGL.gl.glVertex3d(x1, y1, 0.0);
		OGL.gl.glVertex3d(x2, y2, 0.0);
		OGL.gl.glVertex3d(x2, y2, h);
		OGL.gl.glVertex3d(x1, y1, h);
		
		gl.glEnd();
	}
	
	public static void drawGround()
	{
		Texture groundTexture = createTexture("ImagesGround/grassystone.jpg");
		
		//Enable the ground texture
		OGL.gl.glColor3f(.6f, .6f, .6f);		//will darken the image being drawn
		groundTexture.enable(OGL.gl);
		groundTexture.bind(OGL.gl);
		
		// Draw the ground for the maze to sit on. 10x10.
		OGL.gl.glBegin(GL2.GL_POLYGON);
		
		OGL.gl.glNormal3f(0,0,1);
		OGL.gl.glTexCoord2d(0,0);
		OGL.gl.glVertex2d(-10, -10);
		OGL.gl.glTexCoord2d(225, 0);
		OGL.gl.glVertex2d(10, -10);
		OGL.gl.glTexCoord2d(225, 225);
		OGL.gl.glVertex2d(10, 10);
		OGL.gl.glTexCoord2d(0, 225);
		OGL.gl.glVertex2d(-10, 10);

		OGL.gl.glEnd();
		groundTexture.disable(OGL.gl);
	}
	
	public static void drawSky()
	{				
		Texture skyTexture = createTexture("ImagesSky/skyNight2.jpg");
		
		//Enable the sky texture
		OGL.gl.glColor3f(1.0f, 1.0f, 1.0f);
		skyTexture.enable(OGL.gl);
		skyTexture.bind(OGL.gl);
		
		double[] pos = camera.getPosition();

		OGL.gl.glBegin(GL2.GL_TRIANGLES);
		
		//front side
		OGL.gl.glTexCoord3d(0,4,0);
		OGL.gl.glVertex3d(-5+pos[0],5+pos[1],0);
		OGL.gl.glTexCoord3d(0,0,4);
		OGL.gl.glVertex3d(0+pos[0],0+pos[1],3);
		OGL.gl.glTexCoord3d(4,0,0);
		OGL.gl.glVertex3d(5+pos[0],5+pos[1],0);
		
		//left side
		OGL.gl.glTexCoord3d(0,4,0);
		OGL.gl.glVertex3d(-5+pos[0],-5+pos[1],0);
		OGL.gl.glTexCoord3d(0,0,4);
		OGL.gl.glVertex3d(0+pos[0],0+pos[1],3);
		OGL.gl.glTexCoord3d(4,0,0);
		OGL.gl.glVertex3d(-5+pos[0],5+pos[1],0);
		
		//right side
		OGL.gl.glTexCoord3d(0,4,0);
		OGL.gl.glVertex3d(5+pos[0],-5+pos[1],0);
		OGL.gl.glTexCoord3d(0,0,4);
		OGL.gl.glVertex3d(0+pos[0],0+pos[1],3);
		OGL.gl.glTexCoord3d(4,0,0);
		OGL.gl.glVertex3d(5+pos[0],5+pos[1],0);
		
		//back side
		OGL.gl.glTexCoord3d(0,4,0);
		OGL.gl.glVertex3d(-5+pos[0],-5+pos[1],0);
		OGL.gl.glTexCoord3d(0,0,4);
		OGL.gl.glVertex3d(0+pos[0],0+pos[1],3);
		OGL.gl.glTexCoord3d(4,0,0);
		OGL.gl.glVertex3d(5+pos[0],-5+pos[1],0);
		
		OGL.gl.glEnd();
		
		skyTexture.disable(OGL.gl);
	}
	
//	public static void drawCylinder(double x, double y)
//	{
//
//		// "All walls are half the walls", so only draw the first three walls.
//		for(int i = 0; i < 3; i++)
//		{	
//			// Draw all the values in the preprocessed array.
//			for(double[] da : buffer.get(i))
//			{	
//				if (counter % 5 == 0)
//				{
//					if (counter % 30 == 0)
//						wallTexture = wallTexture2;
//					else
//						wallTexture = wallTexture1;
//				}
//				
//				wallTexture.enable(gl);
//				wallTexture.bind(gl);
//				
//				// Draw a single wall.
//				gl.glBegin(GL2.GL_POLYGON);
//					
//				//Sides of wall
//				if(da.length < 7)
//				{	
//					gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
//					gl.glTexCoord2d(0,0);
//					gl.glVertex3d(da[2], da[3], da[5]);
//					
//					gl.glColor3f(1.0f, 1.0f, 1.0f);			//lighten the image a little
//					gl.glTexCoord2d(0,1);
//					gl.glVertex3d(da[2], da[3], da[4]);
//					
//					gl.glTexCoord2d(1,1);
//					gl.glVertex3d(da[0], da[1], da[4]);
//					
//					gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
//					gl.glTexCoord2d(1,0);
//					gl.glVertex3d(da[0], da[1], da[5]);
//				}
//				//Top of wall
//				else
//				{
//					gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
//					gl.glTexCoord2d(0,0);
//					gl.glVertex3d(da[0], da[1], da[8]);
//					
//					gl.glColor3f(1.0f, 1.0f, 1.0f);			//lighten the image a little
//					gl.glTexCoord2d(0,1);
//					gl.glVertex3d(da[2], da[3], da[8]);
//						
//					gl.glTexCoord2d(1,1);
//					gl.glVertex3d(da[4], da[5], da[8]);
//					
//					gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
//					gl.glTexCoord2d(1,0);
//					gl.glVertex3d(da[6], da[7], da[8]);
//				}
//				
//				gl.glEnd();
//					
//				++counter;
//			}
//		}
//		wallTexture.disable(gl);
//	}

	
	public static void drawCandle(GLU glu, int i, int option)
	{
		double x = candleCoords[i][0];
		double y = candleCoords[i][1];
		
		//Draw base of candle
		OGL.gl.glColor3d(0.965, 0.946, 0.883);
		OGL.gl.glPushMatrix();
		OGL.gl.glTranslated(x, y, 0.0);
		glu.gluCylinder(quad, 0.0013f, 0.0013f, 0.025f, 5, 5);
		OGL.gl.glPopMatrix();
		
		//Reset color
		OGL.gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		//
		double wind = 0.0;
		if(option == 0)
			wind = 0.0;
		else if(option == 1)
			wind = -0.0003;
		else
			wind = 0.0003;
		
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
		OGL.gl.glPushMatrix();
		OGL.gl.glTranslatef((float)x, (float)y, 0.026f);
		OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
		glu.gluSphere(quad2, 0.0009, 5, 5);		
		OGL.gl.glPopMatrix();
		
		//Disable the fire texture
		fireTexture.disable(OGL.gl);
				
		
		if (option == 0)
		{
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslatef((float)x, (float)y, 0.029f);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0030, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.08f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslatef((float)x, (float)y, 0.029f);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0060, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.04f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslatef((float)x, (float)y, 0.029f);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
		else if (option == 1)
		{	
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslatef((float)x, (float)y, 0.029f);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0060, 5, 5);		
			OGL.gl.glPopMatrix();
			
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.08f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslatef((float)x, (float)y, 0.029f);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
		else
		{
			OGL.gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			OGL.gl.glPushMatrix();
			OGL.gl.glTranslatef((float)x, (float)y, 0.029f);
			OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0080, 5, 5);
			OGL.gl.glPopMatrix();
		}
	}
	
	public static void drawDimmer(GLU glu, Camera camera){
		OGL.gl.glEnable(GL.GL_BLEND);
		OGL.gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		OGL.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		OGL.gl.glColor4f(0.0f, 0.0f, 0.0f, camera.getAlpha());
	
		double[] pos = camera.getPosition();
		
		OGL.gl.glColor4f(0.0f, 0.0f, 0.0f, camera.getAlpha());
	
		OGL.gl.glPushMatrix();

		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
		glu.gluQuadricTexture(quad, true); 

		OGL.gl.glTranslatef((float)pos[0], (float)pos[1], 0.0f);

		
		glu.gluCylinder(quad, 0.002f, 0.002f, 0.3f, 32, 1);
		
		OGL.gl.glPopMatrix();

	
		OGL.gl.glDisable(GL.GL_BLEND);
	
	}
	
//	public static float drawOrbs(GLU glu, float start){
//		
//		// Ball o light
//		
//		//gl.glColor3f(0.3f, 0.6f, 0.3f);
//		OGL.gl.glColor3f(1.0f, 1.0f, 1.0f);
//		
//		OGL.gl.glPushMatrix();
//
//		GLUquadric quad = glu.gluNewQuadric();
//		glu.gluQuadricNormals(quad, GLU.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
//		glu.gluQuadricTexture(quad, false); 
//
//		OGL.gl.glTranslatef(0.02f, 0.02f, 0.0f + start);
//		OGL.gl.glScalef(1.0f, 1.0f, 2.0f);
//		
//		glu.gluSphere(quad, 0.04, 15, 15);		
//		
//		OGL.gl.glPopMatrix();
//		
//		start *= 1.001f;
//		
//		return start;
//		// end ball o light
//	}
	

	public static Texture createTexture(String imagePath)
	{	
		Texture texture = null;
		try
		{
			URL textureURL;
			textureURL = Scenery.class.getResource(imagePath);
			
			if (textureURL != null)
			{
				BufferedImage img = ImageIO.read(textureURL);
				ImageUtil.flipImageVertically(img);
				texture = AWTTextureIO.newTexture(GLProfile.getDefault(), img, true);
				texture.setTexParameteri(OGL.gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
				texture.setTexParameteri(OGL.gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
			}
		}	
		catch(Exception e){
			e.printStackTrace();
		}
		return texture;
	}
}
