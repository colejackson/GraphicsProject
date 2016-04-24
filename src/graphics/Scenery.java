package graphics;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

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
import driver.Utilities;

public abstract class Scenery 
{	
	private static Camera camera;
	
	//Textures, so they don't have to be created every time drawn
	private static Texture groundTexture;
	private static Texture skyTexture;
	private static Texture fireTexture;
	private static Texture goldTexture;
	
	//Candle info
	private final static int MAX_NUM_CANDLES = 500;
	private static Candle[] candles = new Candle[MAX_NUM_CANDLES];
	private static int candleCount = 0;
	
	//Used by candle methods
	private static Random rand = new Random();
	private static int drawCounter = 0;
	private static double[] wind = new double[MAX_NUM_CANDLES];
	private static int[] flicker = new int[MAX_NUM_CANDLES];
	

	public static void initCamera(Camera cam)
	{
		camera = cam;
	}
	
	public static void initTextures()
	{
		groundTexture = createTexture("ImagesGround/grassystone.jpg");
		skyTexture = createTexture("ImagesSky/skyNight2.jpg");
		fireTexture = createTexture("ImagesOther/fire.jpg");
		goldTexture = createTexture("ImagesOther/gold.png");
	}
	
	public static int getCandleCount()
	{
		return candleCount;
	}
	
	public static Candle getCandles(int index)
	{
		return candles[index];
	}
	
	public static void addCandle()
	{
		if(candleCount == MAX_NUM_CANDLES)
			return;
		
		//Candle candle = new Candle((.90*camera.getPosition()[0] + .10*camera.getLook()[0]), (.90*camera.getPosition()[1] + .10*camera.getLook()[1]));
		Candle candle = new Candle(camera.getPosition()[0], camera.getPosition()[1]);
		candles[candleCount] = candle;
		++candleCount;
	}
	
	public static void removeCandle()
	{
		if(candleCount == 0)
			return;
		
		//If the coordinate of a candle falls within a circle around the current position of the camera
		for (int i = 0; i < candleCount; ++i)
		{
			double candleX = candles[i].getX();
			double candleY = candles[i].getY();
			double centerX = camera.getPosition()[0];
			double centerY = camera.getPosition()[1];
			double radius = 0.1;
			
			double lhs = ((candleX-centerX)*(candleX-centerX)) + ((candleY-centerY)*(candleY-centerY));
			double rhs = radius*radius;
			
			//If the candle coordinate lies inside the circle around the camera
			if (lhs < rhs)
			{
				//Pick it up
				for (int j = i; j < candleCount-1; ++j)
					candles[j] = candles[j+1];
				candles[candleCount-1] = null;
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

	
	public static void drawCandles(GLU glu)
	{	
		if(drawCounter == 12)
		{
			for(int i=0; i<candleCount; ++i)
			{
				wind[i] = -0.0005 + (0.0005 - (-0.0005)) * rand.nextDouble();
				flicker[i] = rand.nextInt(3);
			}
			drawCounter = 0;
		}
		
		double[] angles = new double[2];
		
		if(candleCount > 0)
			angles = findBounds();
		
		for(int i = 0; i < candleCount; ++i)
		{
			if(inFront(candles[i], angles[0], angles[1]))
			{
				System.out.println("Printing candle " + i);
				candles[i].drawBase(glu);
				candles[i].drawFlame(glu, fireTexture, wind[i]);
				candles[i].drawFlicker(glu, flicker[i]);
				candles[i].drawStand(glu, goldTexture);
			}
		}
		
		++drawCounter;	
	}
	
	public static boolean shouldDraw(Point2D.Double position, Point2D.Double look, Point2D.Double candle)
	{
		return false;
	}
	
	public static double[] findBounds()
	{
		double[] angles = new double[2];
		
		Point2D.Double origin = new Point2D.Double(camera.getPosition()[0], camera.getPosition()[1]);
		Point2D.Double look = new Point2D.Double(camera.getLook()[0], camera.getLook()[1]);
		double checkAngle = Utilities.getAngle(origin, look) + 360.0;
		
		double angleRightBound = checkAngle - 35.0;
		double angleLeftBound = checkAngle + 35.0;
		
		angles[0] = angleRightBound;
		angles[1] = angleLeftBound;
		
		return angles;
	}
	
	public  static boolean inFront(Candle candle, double angleRight, double angleLeft)
	{
		Point2D.Double origin = new Point2D.Double(camera.getPosition()[0], camera.getPosition()[1]);
		Point2D.Double location = new Point2D.Double(candle.getX(), candle.getY());
		double angle = Utilities.getAngle(origin, location) + 360.0;
		
		if (angle >= angleRight && angle <= angleLeft)
			return true;
		else
			return false;
	}
	
	public static void drawDimmer(GLU glu, Camera camera)
	{
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
	}

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
