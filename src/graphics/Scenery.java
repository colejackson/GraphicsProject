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
	private static double[][] candleCoords = new double[50][2];
	private static int candleCount = 0;
	
	public static void setCamera(Camera cam)
	{
		myCam = cam;
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
				
//				//OUTLINING
//				//If a side of a wall (AKA not the top)
//				if(da.length < 7)
//				{
//					//Outline the walls for more texture
//					if (wallTexture == wallTexture2)
//						gl.glColor3f(.4f, .4f, .4f);
//					else
//						gl.glColor3f(.6f, .6f, .6f);
//					
//					gl.glLineWidth(2.0f);
//					gl.glBegin(GL.GL_LINES);
//					
//					//bottom
//					gl.glVertex3d(da[0], da[1], da[4]);
//					gl.glVertex3d(da[2], da[3], da[4]);
//					
//					//top
//					gl.glVertex3d(da[0], da[1], da[5]);
//					gl.glVertex3d(da[2], da[3], da[5]);
//					
//					//Side
//					//gl.glVertex3d(da[0], da[1], da[4]);
//					//gl.glVertex3d(da[0], da[1], da[5]);
//					
//					//Side
//					//gl.glVertex3d(da[2], da[3], da[4]);
//					//gl.glVertex3d(da[2], da[3], da[5]);
//					
//					gl.glEnd();	
//				}
					
				++counter;
			}
		}
		wallTexture.disable(gl);
		
	}
	
	public static void drawCandle(GL2 gl, GLU glu, int i)
	{
		double x = candleCoords[i][0];
		double y = candleCoords[i][1];
		
		//Draw base of candle
		gl.glColor3d(0.996, 1.0, 0.941);
		gl.glPushMatrix();
		GLUquadric quad = glu.gluNewQuadric();
		gl.glTranslated(x, y, 0.0);
		glu.gluCylinder(quad, 0.0013f, 0.0013f, 0.025f, 32, 3);
		gl.glPopMatrix();
		
		fireTexture.enable(gl);
		fireTexture.bind(gl);
		
		//Draw flame of candle
		gl.glColor3d(0.996, 0.663, 0.235);
		gl.glBegin(GL2.GL_TRIANGLES);
		
		//front side
		gl.glTexCoord3d(0,1,0);
		gl.glVertex3d(-0.0007+x,0.0007+y,0.025);
		gl.glTexCoord3d(0,0,1);
		gl.glVertex3d(0.0+x,y,0.030);
		gl.glTexCoord3d(1,0,0);
		gl.glVertex3d(0.0007+x,0.0007+y,0.025);
		
		//left side
		gl.glTexCoord3d(0,1,0);
		gl.glVertex3d(-0.0007+x,-0.0007+y,0.025);
		gl.glTexCoord3d(0,0,1);
		gl.glVertex3d(0.0+x,y,0.030);
		gl.glTexCoord3d(1,0,0);
		gl.glVertex3d(-0.0007+x,0.0007+y,0.025);
		
		//right side
		gl.glTexCoord3d(0,1,0);
		gl.glVertex3d(0.0007+x,-0.0007+y,0.025);
		gl.glTexCoord3d(0,0,1);
		gl.glVertex3d(0.0+x,y,0.030);
		gl.glTexCoord3d(1,0,0);
		gl.glVertex3d(0.0007+x,0.0007+y,0.025);
		
		//back side
		gl.glTexCoord3d(0,1,0);
		gl.glVertex3d(-0.0007+x,-0.0007+y,0.025);
		gl.glTexCoord3d(0,0,1);
		gl.glVertex3d(0.0+x,y,0.030);
		gl.glTexCoord3d(1,0,0);
		gl.glVertex3d(0.0007+x,-0.0007+y,0.025);
		
		gl.glEnd();
		
		fireTexture.disable(gl);
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
		
		gl.glColor3f(0.3f, 0.6f, 0.3f);
		
		gl.glPushMatrix();

		GLUquadric quad = glu.gluNewQuadric();
		glu.gluQuadricNormals(quad, glu.GLU_SMOOTH);   // Create Smooth Normals ( NEW )
		glu.gluQuadricTexture(quad, true); 

		gl.glTranslatef(0.02f, 0.02f, 0.0f + start);
		
		glu.gluSphere(quad, 0.04, 32, 32);
		
		gl.glPopMatrix();
		
		start *= 1.001f;
		 
		return start;
		// end ball o light
	}
	
	public static void initTextures(GL2 gl)
	{
		groundTexture = createTexture(gl, "ImagesGround/grassystone.jpg");
		skyTexture = createTexture(gl, "ImagesSky/skyNight2.jpg");
		
		//This texture will be set to either wallTexture1 or 2 depending on which should be drawn
		//Just need to initialize it to some texture here though to avoid null pointer exception
		wallTexture = createTexture(gl, "ImagesWall/concrete.jpg");
		
		wallTexture1 = createTexture(gl, "ImagesWall/concrete.jpg"); //concrete wall texture
		wallTexture2 = createTexture(gl, "ImagesWall/concrete.jpg"); //metal wall texture
		
		fireTexture = createTexture(gl, "ImagesOther/fire.jpg"); //fire texture
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
