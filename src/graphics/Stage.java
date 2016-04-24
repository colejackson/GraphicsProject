package graphics;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;

import async.Director;
import async.Worker;
import driver.OGL;
import maze.Maze;
import maze.Wall;
import maze.WallPreprocessor;
import objects.LightBall;
import objects.TractorBeam;
import graphics.Orb;
import graphics.ParticleEngine;

public class Stage implements GLEventListener
{
	private Maze maze = null;
	
	private boolean newBuffer = false;
	
	// GL Global Objects
	private GLProfile profile;
	private GLCapabilities capabilities;;
	private GLCanvas canvas;
	
	private ArrayList<Wall> buffer = null;
	private ArrayList<Wall> draw = null;
			
	// GL Function Objects
	GL2 gl = null;
	GLU glu = new GLU();
	GLUT glut = new GLUT();
	
	// Maze Related Objects
	private Camera camera;
	private Director director;
	private WallPreprocessor wpp;
	private Stage stage;
	
	// Objects in the Maze
	private TractorBeam tractor;
	private ArrayList<LightBall> balls;
	private ParticleEngine orbPartEng;
	private Orb orb;
	
	float start = 0.2f;

	// Make a Stage object containing a Maze
	public Stage(Maze maze)
	{	
		// Hey its the maze;
		this.maze = maze;
		this.stage = this;
		
		// Initialize the varibales to set the frame.
		this.director = new Director();
		this.profile = GLProfile.getDefault();
		this.capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		
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
		
		try 
		{
			Field gl_object = OGL.class.getDeclaredField("gl");
			gl_object.setAccessible(true);
			
			Field modifiersField = Field.class.getDeclaredField( "modifiers" );
            modifiersField.setAccessible( true );
            modifiersField.setInt(gl_object, gl_object.getModifiers() & ~Modifier.FINAL );
			
			gl_object.set(null, gl);
			
			gl_object = OGL.class.getDeclaredField("glu");
			gl_object.setAccessible(true);
			
			modifiersField = Field.class.getDeclaredField( "modifiers" );
            modifiersField.setAccessible( true );
            modifiersField.setInt(gl_object, gl_object.getModifiers() & ~Modifier.FINAL );
			
			gl_object.set(null, glu);
		} 
		catch (NoSuchFieldException | SecurityException | IllegalAccessException e) 
		{
			e.printStackTrace();
		}
		
		tractor = new TractorBeam(0.0, 0.0);
		balls = new ArrayList<>();
		for(int i = 0; i < 5; i++)
			balls.add(new LightBall(0.0,0.0));
		
		// Initialize variables except camera which requires the GL2 object be initialized first.
		this.wpp = new WallPreprocessor(maze);
		this.camera = new Camera();
		new Worker(stage, wpp, camera, balls);
		
		// Enable the depth test code.
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LESS);
		
		// Enable Antialiasing
		gl.glEnable(GL.GL_LINE_SMOOTH);
		
		//Enable transparency
		gl.glEnable(GL.GL_BLEND);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
		
		orb = new Orb();
		orbPartEng = new ParticleEngine(75, 0.008f);
		
		// Initialize the scenery
		Scenery.initTextures();
		Scenery.initCamera(camera);
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
	
		while(buffer == null)
		{
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// Switch out the buffer if you need to.
		if(newBuffer)
		{
			newBuffer = false;	
			draw = buffer;
		}	

		Scenery.drawGround();
		Scenery.drawSky();
		
		//draw walls
		draw.stream().sorted((x,y) -> Integer.compare(x.getTexIndx(), y.getTexIndx())).forEach(c -> c.glDraw());
		
		Scenery.drawCandles(glu);
		
		//draw orbs
		orb.drawOrb(glu);
		orbPartEng.update(orb, glu, camera);
		
		//for(LightBall lb : balls)
			//lb.draw();
		
		//draw light beam
		tractor.draw();
		
		//Scenery.drawDimmer(glu, camera);
	}
	
	public void setBuffer(ArrayList<Wall> al)
	{
		this.buffer = al;
		newBuffer = true;
	}
}