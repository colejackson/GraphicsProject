package graphics;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.imageio.ImageIO;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import async.Director;
import driver.Utilities;
import maze.Maze;
import maze.WallPreprocessor;
import z_deprecated.ColorPreprocessor;

public class Stage implements GLEventListener
{
	private boolean newBuffer = false;
	private boolean bufferConsumed = false;
	
	// GL Global Objects
	private GLProfile profile;
	private GLCapabilities capabilities;;
	private GLCanvas canvas;
	
	private ArrayList<ArrayList<double[]>> worker = new ArrayList<ArrayList<double[]>>(6);
	private ArrayList<ArrayList<double[]>> buffer = new ArrayList<ArrayList<double[]>>(6);
	
	private ArrayList<double[]> worker_collide = new ArrayList<double[]>();
	
	private final boolean running = true;
	
	// GL Function Objects
	GL2 gl;
	GLU glu = new GLU();
	GLUT glut = new GLUT();
	
	// Maze Related Objects
	private Camera camera;
	private Director director;
	private ColorPreprocessor cpp;
	private WallPreprocessor mpp;
	
	// Texture objects
	private Texture groundTexture;
	private Texture wallTexture;
	private Texture wallTexture1;
	private Texture wallTexture2;	

	// Make a Stage object containing a Maze
	public Stage(Maze maze)
	{
		// Initialize variables except camera which requires the GL2 object be initialized first.
		this.profile = GLProfile.getDefault();
		this.capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.mpp = new WallPreprocessor(maze);
		this.cpp = new ColorPreprocessor(mpp);
		this.director = new Director();
						
		// Configure the canvas, add KeyListener and Request Focus
		canvas.addGLEventListener(this);
		canvas.setFocusable(true);
		canvas.addKeyListener(director);
		canvas.requestFocus();
		
		// Animator stared up, runs display 60x per second.
		FPSAnimator	animator = new FPSAnimator(canvas, 60);
		animator.start();
	}

	@Override
	public void display(GLAutoDrawable glad) 
	{
		// Clear the color and depth buffers.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		
		// Render the Maze.
		render(glad);
	}

	@Override
	public void init(GLAutoDrawable glad) 
	{		
		// Set the GL Object
		gl = glad.getGL().getGL2();
		
		// Enable the depth test code.
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LESS);
		
		// Enable Antialiasing
		gl.glEnable(GL.GL_LINE_SMOOTH);
		
		// Create a Camera and pass in the gl objects.
		this.camera = new Camera(glu, gl);
		
		// Create the texture objects
		groundTexture = createTexture("grassystone.jpg");
		wallTexture1 = createTexture("concrete.jpg");
		wallTexture2 = createTexture("metal1light.jpg");
		
		
		// Worker
		ExecutorService es = Executors.newFixedThreadPool(2);
		
		@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
		final Future f = es.submit(
			new Callable() 
			{
				@Override
				public Object call() throws Exception 
				{
					while(running)
					{
						setBuffer();
						
						if(!bufferConsumed)
							Thread.sleep(20);
						
						Thread.sleep(50);
					}
				}
			}
		);
		
		buffer = mpp;
		// End Worker
	}

	@Override
	public void dispose(GLAutoDrawable glad) {}
	
	@Override
	public void reshape(GLAutoDrawable glad, int x, int y, int w, int h) {}
	
	// Accessor for the canvas.
	public GLCanvas canvas()
	{
		return canvas;
	}
	
	//Render the scene on the OpenGL Canvas
	private void render(GLAutoDrawable glad)
	{
		// Check for commands from the director and pass them to the camera.
		for(String s : director)
			camera.command(s);
		
		// Reset the Color Preprocessor;
		cpp.reset();
	
		// Render the maze.
		render(gl);
	}

	//Method to draw something on the canvas
	private void render(GL2 gl)
	{	
		// Switch out the buffer if you need to.
		if(newBuffer)
		{
			buffer = worker;
			bufferConsumed = true;
			newBuffer = false;		
		}
		Scenery.drawGround(gl, groundTexture);		
		Scenery.drawWalls(gl, buffer, camera, wallTexture1, wallTexture1);

	}
	
	private Texture createTexture(String imagePath)
	{
		Texture texture = null;
		try
		{
			URL textureURL;
			textureURL = getClass().getResource(imagePath);
			
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
	
	protected void setBuffer() 
	{		
		worker = new ArrayList<ArrayList<double[]>>();
		for(int i = 0; i < 6; i++)
			worker.add(new ArrayList<double[]>());
		
		worker_collide = new ArrayList<double[]>();
		
		double posx = camera.getPosition()[0];
		double posy = camera.getPosition()[1];
		double lookx = camera.getLook()[0];
		double looky = camera.getLook()[1];
		
		double neglookx = posx - lookx;
		double neglooky = posy - looky;
		
		Line2D line1 = new Line2D.Double(posx, posy, lookx, looky);
		Line2D line2 = new Line2D.Double(posx, posy, neglookx, neglooky);
						
		for(int i = 0; i < 3; i++)
		{			
			for(int j = 0; j < mpp.get(i).size(); j++)
			{				
				double avgx = (mpp.get(i).get(j)[0] + mpp.get(i).get(j)[2])/2.0;
				double avgy = (mpp.get(i).get(j)[1] + mpp.get(i).get(j)[3])/2.0;

				double distance = Utilities.distance(avgx, avgy, camera.getPosition()[0], camera.getPosition()[1]);
				
				if((.5 >= line1.ptLineDist(avgx, avgy) &&
				   1.4 >= Point2D.distance(posx, posy, avgx, avgy) &&
				   line1.ptSegDist(avgx, avgy) <= line2.ptSegDist(avgx, avgy)) ||
				   .1 > Point2D.distance(posx, posy, avgx, avgy))
				{
					worker.get(i).add(mpp.get(i).get(j));
				}
				if(distance < .05)
				{
					worker_collide.add(mpp.get(i).get(j));
				}
			}
		}
		newBuffer = true;
		bufferConsumed = false;
		camera.setBuffer(worker_collide);
	}
}