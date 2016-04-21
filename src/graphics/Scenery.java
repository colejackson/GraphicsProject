package graphics;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public abstract class Scenery 
{
	private final static int CIRCLE_SIDES = 32;
	
	private final static double TREE_HEIGHT = 0.8;
	private final static double TREE_RADIUS = 0.2;
	
	private static Texture groundTexture;
	private static Texture skyTexture;
	private static Texture wallTexture;
	private static Texture wallTexture1;
	private static Texture wallTexture2;
	private static Texture fireTexture;
	
	private static Camera myCam;
	
	private static GLUquadric quad;
	private static GLUquadric quad2;
	
	private final static int NUM_CANDLES = 1000;
	private static double[][] candleCoords = new double[NUM_CANDLES][2];
	private static int candleCount = 0;
	

	public static void initCamera(Camera cam)
	{
		myCam = cam;
	}
	
	public static void initQuadrics(GLU glu)
	{
		quad = glu.gluNewQuadric();
		glu.gluQuadricTexture(quad, false);
		
		quad2 = glu.gluNewQuadric();
		//glu.gluQuadricNormals(quad2, glu.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
		glu.gluQuadricTexture(quad2, true); 
	}
	
	public static void initTextures(GL2 gl)
	{
		groundTexture = createTexture(gl, "ImagesGround/grassystone.jpg");
		skyTexture = createTexture(gl, "ImagesSky/skyNight2.jpg");
		wallTexture = createTexture(gl, "ImagesWall/concrete.jpg");
		wallTexture1 = createTexture(gl, "ImagesWall/concrete.jpg"); //concrete wall texture
		wallTexture2 = createTexture(gl, "ImagesWall/concrete.jpg"); //metal wall texture
		fireTexture = createTexture(gl, "ImagesOther/fire.jpg"); //fire texture
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
		candleCoords[candleCount] = new double[] {myCam.getPosition()[0], myCam.getPosition()[1]};
		++candleCount;
	}
	
	public static void removeCandle()
	{
		//If the coordinate of a candle falls within a circle around the current position of the camera
		for (int i = 0; i < candleCount; ++i)
		{
			double candleX = candleCoords[i][0];
			double candleY = candleCoords[i][1];
			double centerX = myCam.getPosition()[0];
			double centerY = myCam.getPosition()[1];
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
	
//	public static void drawTree(double x, double y, GL2 gl)
//	{
//		gl.glColor3d(1.0, .55, .55);
//				
//		drawCylinder(x, y, TREE_HEIGHT, TREE_RADIUS, gl);
//	}
//	
//	public static void drawCylinder(double x, double y, double h, double r, GL2 gl)
//	{
//		double twoPi = Math.PI * 2;
//		
//		for(int i = 0; i < CIRCLE_SIDES; i++)
//		{
//			double x1 = (r * Math.sin((i/CIRCLE_SIDES) * twoPi)) + x;
//			double y1 = (r * Math.cos((i/CIRCLE_SIDES) * twoPi)) + y;
//			
//			double x2 = (r * Math.sin(((i+1)/CIRCLE_SIDES) * twoPi)) + x;
//			double y2 = (r * Math.cos(((i+1)/CIRCLE_SIDES) * twoPi)) + y;
//						
//			gl.glBegin(GL.GL_TRIANGLES);
//			
//			gl.glVertex3d(x, y, 0.0);
//			gl.glVertex3d(x1, y1, 0.0);
//			gl.glVertex3d(x2, y2, 0.0);
//
//			gl.glVertex3d(x, y, h);
//			gl.glVertex3d(x1, y1, h);
//			gl.glVertex3d(x2, y2, h);
//
//			gl.glEnd();
//			
//			gl.glBegin(GL2.GL_POLYGON);
//			
//			gl.glVertex3d(x1, y1, 0.0);
//			gl.glVertex3d(x2, y2, 0.0);
//			gl.glVertex3d(x2, y2, h);
//			gl.glVertex3d(x1, y1, h);
//
//			gl.glEnd();
//		}
//	}
//	
//	public static void drawZRect(double x1, double y1, double x2, double y2, double h, GL2 gl)
//	{
//		gl.glBegin(GL2.GL_POLYGON);
//		
//		gl.glVertex3d(x1, y1, 0.0);
//		gl.glVertex3d(x2, y2, 0.0);
//		gl.glVertex3d(x2, y2, h);
//		gl.glVertex3d(x1, y1, h);
//		
//		gl.glEnd();
//	}
	
	public static void drawGround(GL2 gl)
	{
		//Enable the ground texture
		gl.glColor3f(.6f, .6f, .6f);		//will darken the image being drawn
		groundTexture.enable(gl);
		groundTexture.bind(gl);
		
		// Draw the ground for the maze to sit on. 10x10.
		gl.glBegin(GL2.GL_POLYGON);
		
		gl.glNormal3f(0,0,1);
		gl.glTexCoord2d(0,0);
		gl.glVertex2d(-10, -10);
		gl.glTexCoord2d(225, 0);
		gl.glVertex2d(10, -10);
		gl.glTexCoord2d(225, 225);
		gl.glVertex2d(10, 10);
		gl.glTexCoord2d(0, 225);
		gl.glVertex2d(-10, 10);

		gl.glEnd();
		groundTexture.disable(gl);
	}
	
	public static void drawSky(GL2 gl, Camera camera)
	{				
		//Enable the sky texture
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		skyTexture.enable(gl);
		skyTexture.bind(gl);
		
		double[] pos = camera.getPosition();

		gl.glBegin(GL2.GL_TRIANGLES);
		
		//front side
		gl.glTexCoord3d(0,4,0);
		gl.glVertex3d(-5+pos[0],5+pos[1],0);
		gl.glTexCoord3d(0,0,4);
		gl.glVertex3d(0+pos[0],0+pos[1],3);
		gl.glTexCoord3d(4,0,0);
		gl.glVertex3d(5+pos[0],5+pos[1],0);
		
		//left side
		gl.glTexCoord3d(0,4,0);
		gl.glVertex3d(-5+pos[0],-5+pos[1],0);
		gl.glTexCoord3d(0,0,4);
		gl.glVertex3d(0+pos[0],0+pos[1],3);
		gl.glTexCoord3d(4,0,0);
		gl.glVertex3d(-5+pos[0],5+pos[1],0);
		
		//right side
		gl.glTexCoord3d(0,4,0);
		gl.glVertex3d(5+pos[0],-5+pos[1],0);
		gl.glTexCoord3d(0,0,4);
		gl.glVertex3d(0+pos[0],0+pos[1],3);
		gl.glTexCoord3d(4,0,0);
		gl.glVertex3d(5+pos[0],5+pos[1],0);
		
		//back side
		gl.glTexCoord3d(0,4,0);
		gl.glVertex3d(-5+pos[0],-5+pos[1],0);
		gl.glTexCoord3d(0,0,4);
		gl.glVertex3d(0+pos[0],0+pos[1],3);
		gl.glTexCoord3d(4,0,0);
		gl.glVertex3d(5+pos[0],-5+pos[1],0);
		
		gl.glEnd();
		
		skyTexture.disable(gl);
	}
	
	
	public static void drawWalls(GL2 gl, ArrayList<ArrayList<double[]>> buffer, Camera camera)
	{
		int counter = 0;

		// "All walls are half the walls", so only draw the first three walls.
		for(int i = 0; i < 3; i++)
		{	
			// Draw all the values in the preprocessed array.
			for(double[] da : buffer.get(i))
			{	
				if (counter % 5 == 0)
				{
					if (counter % 30 == 0)
						wallTexture = wallTexture2;
					else
						wallTexture = wallTexture1;
				}
				
				wallTexture.enable(gl);
				wallTexture.bind(gl);
				
				// Draw a single wall.
				gl.glBegin(GL2.GL_POLYGON);
					
				//Sides of wall
				if(da.length < 7)
				{	
					gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
					gl.glTexCoord2d(0,0);
					gl.glVertex3d(da[2], da[3], da[5]);
					
					gl.glColor3f(1.0f, 1.0f, 1.0f);			//lighten the image a little
					gl.glTexCoord2d(0,1);
					gl.glVertex3d(da[2], da[3], da[4]);
					
					gl.glTexCoord2d(1,1);
					gl.glVertex3d(da[0], da[1], da[4]);
					
					gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
					gl.glTexCoord2d(1,0);
					gl.glVertex3d(da[0], da[1], da[5]);
				}
				//Top of wall
				else
				{
					gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
					gl.glTexCoord2d(0,0);
					gl.glVertex3d(da[0], da[1], da[8]);
					
					gl.glColor3f(1.0f, 1.0f, 1.0f);			//lighten the image a little
					gl.glTexCoord2d(0,1);
					gl.glVertex3d(da[2], da[3], da[8]);
						
					gl.glTexCoord2d(1,1);
					gl.glVertex3d(da[4], da[5], da[8]);
					
					gl.glColor3f(.7f, .7f, .7f);			//darken the image a little
					gl.glTexCoord2d(1,0);
					gl.glVertex3d(da[6], da[7], da[8]);
				}
				
				gl.glEnd();
					
				++counter;
			}
		}
		wallTexture.disable(gl);
	}

	
	public static void drawCandle(GL2 gl, GLU glu, int i, int option)
	{
		double x = candleCoords[i][0];
		double y = candleCoords[i][1];
		
		//Draw base of candle
		gl.glColor3d(0.965, 0.946, 0.883);
		gl.glPushMatrix();
		gl.glTranslated(x, y, 0.0);
		glu.gluCylinder(quad, 0.0013f, 0.0013f, 0.025f, 5, 5);
		gl.glPopMatrix();
		
		//Reset color
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		//
		double wind = 0.0;
		if(option == 0)
			wind = 0.0;
		else if(option == 1)
			wind = -0.0002;
		else
			wind = 0.0002;
		
		//Enable fire texture
		fireTexture.enable(gl);
		fireTexture.bind(gl);
		
		//Draw flame of candle (pyramid)
		gl.glColor3d(0.996, 0.663, 0.235);
		gl.glBegin(GL2.GL_TRIANGLES);
		
		//front side
		gl.glTexCoord3d(0,1,0);
		gl.glVertex3d(-0.0007+x,0.0007+y,0.026);
		gl.glTexCoord3d(0,0,1);
		gl.glVertex3d(wind+x,y,0.031);
		gl.glTexCoord3d(1,0,0);
		gl.glVertex3d(0.0007+x,0.0007+y,0.026);
		
		//left side
		gl.glTexCoord3d(0,1,0);
		gl.glVertex3d(-0.0007+x,-0.0007+y,0.026);
		gl.glTexCoord3d(0,0,1);
		gl.glVertex3d(wind+x,y,0.031);
		gl.glTexCoord3d(1,0,0);
		gl.glVertex3d(-0.0007+x,0.0007+y,0.026);
		
		//right side
		gl.glTexCoord3d(0,1,0);
		gl.glVertex3d(0.0007+x,-0.0007+y,0.026);
		gl.glTexCoord3d(0,0,1);
		gl.glVertex3d(wind+x,y,0.031);
		gl.glTexCoord3d(1,0,0);
		gl.glVertex3d(0.0007+x,0.0007+y,0.026);
		
		//back side
		gl.glTexCoord3d(0,1,0);
		gl.glVertex3d(-0.0007+x,-0.0007+y,0.026);
		gl.glTexCoord3d(0,0,1);
		gl.glVertex3d(wind+x,y,0.031);
		gl.glTexCoord3d(1,0,0);
		gl.glVertex3d(0.0007+x,-0.0007+y,0.026);
		
		gl.glEnd();

		//Draw flame of candle (sphere)
		gl.glPushMatrix();
		gl.glTranslatef((float)x, (float)y, 0.026f);
		gl.glScalef(1.0f, 1.0f, 2.0f);
		glu.gluSphere(quad2, 0.0009, 5, 5);		
		gl.glPopMatrix();
		
		//Disable the fire texture
		fireTexture.disable(gl);
		
		
		//Enable transparency
		gl.glEnable(GL2.GL_BLEND);
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		
		
//		float[] alphas = new float[] {0.0f, 0.0f, 0.0f};
//		
//		if(option == 0)
//		{
//			alphas[0] = 0.17f;
//			alphas[1] = 0.13f;
//			alphas[2] = 0.09f;
//		}
//		else if(option == 1)
//		{
//			alphas[0] = 0.09f;
//			alphas[1] = 0.17f;
//			alphas[2] = 0.13f;
//		}
//		else
//		{
//			alphas[0] = 0.13f;
//			alphas[1] = 0.09f;
//			alphas[2] = 0.17f;
//		}

//		Circle		
//		gl.glColor4f(1.0f, 0.49f, 0.176f, alphas[0]);
//
//		double z = 0.029;
//		gl.glBegin(GL2.GL_TRIANGLE_FAN);						//begin the triangle fan
//		gl.glVertex3f((float)x, (float)y, (float)z);			//start from the center
//		for(int s = 0; s < 20; ++s)
//		{ 
//			float x1 = (float)x + (0.0040f * (float)Math.cos(s*2.0f*Math.PI / 19));
//			float z1 = (float)z + (0.0060f * (float)Math.sin(s*2.0f*Math.PI / 19));
//			gl.glVertex3f(x1, (float)y, z1);
//		}
//		gl.glEnd();		
		
		
		if (option == 0)
		{
			gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0.029f);
			gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0030, 5, 5);		
			gl.glPopMatrix();
			
			gl.glColor4f(1.0f, 0.49f, 0.176f, 0.08f);
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0.029f);
			gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0060, 5, 5);		
			gl.glPopMatrix();
			
			gl.glColor4f(1.0f, 0.49f, 0.176f, 0.04f);
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0.029f);
			gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0080, 5, 5);
			gl.glPopMatrix();
		}
		else if (option == 1)
		{	
			gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0.029f);
			gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0060, 5, 5);		
			gl.glPopMatrix();
			
			gl.glColor4f(1.0f, 0.49f, 0.176f, 0.08f);
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0.029f);
			gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0080, 5, 5);
			gl.glPopMatrix();
		}
		else
		{
			gl.glColor4f(1.0f, 0.49f, 0.176f, 0.12f);
			gl.glPushMatrix();
			gl.glTranslatef((float)x, (float)y, 0.029f);
			gl.glScalef(1.0f, 1.0f, 2.0f);
			glu.gluSphere(quad, 0.0080, 5, 5);
			gl.glPopMatrix();
		}
	}
	
	public static void drawDimmer(GL2 gl, GLU glu, Camera camera){
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glColor4f(0.0f, 0.0f, 0.0f, camera.getAlpha());
	
		double[] where = camera.getLook();
		double[] pos = camera.getPosition();
		
		gl.glColor4f(0.0f, 0.0f, 0.0f, camera.getAlpha());
	
		gl.glPushMatrix();

		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricNormals(quad, glu.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
		glu.gluQuadricTexture(quad, true); 

		gl.glTranslatef((float)pos[0], (float)pos[1], 0.0f);

		
		glu.gluCylinder(quad, 0.002f, 0.002f, 0.3f, 32, 1);
		
		gl.glPopMatrix();

	
		gl.glDisable(GL.GL_BLEND);
	
	}
	
	public static float drawOrbs(GL2 gl, GLU glu, float start){
		
		// Ball o light
		
		//gl.glColor3f(0.3f, 0.6f, 0.3f);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		fireTexture.enable(gl);
		fireTexture.bind(gl);
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		
		gl.glPushMatrix();

		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricNormals(quad, glu.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
		glu.gluQuadricTexture(quad, true); 

		gl.glTranslatef(0.02f, 0.02f, 0.0f + start);
		gl.glScalef(1.0f, 1.0f, 2.0f);
		
		glu.gluSphere(quad, 0.04, 32, 32);		
		
		gl.glPopMatrix();
		
		start *= 1.001f;
		fireTexture.disable(gl);
		return start;
		// end ball o light
	}
	

	
	private static Texture createTexture(GL2 gl, String imagePath)
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
				texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);
				texture.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);
			}
		}	
		catch(Exception e){
			e.printStackTrace();
		}
		return texture;
	}
}
