package graphics;

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

import maze.Maze;
import preprocessor.ColorPreprocessor;
import preprocessor.MazePreprocessor;

public class Stage implements GLEventListener
{
	// GL Global Object
	private GLProfile profile;
	private GLCapabilities capabilities;;
	private GLCanvas canvas;
	
	// GL Function Objects
	GL2 gl;
	GLU glu = new GLU();
	GLUT glut = new GLUT();
	
	// Maze Related Objects
	private Camera camera;
	private Director director;
	private ColorPreprocessor cpp;
	private MazePreprocessor mpp;
		
	// Make a Stage object containing a Maze
	public Stage(Maze maze)
	{
		// Initialize varuables except camera which requires the GL2 object be initialized first.
		this.profile = GLProfile.getDefault();
		this.capabilities = new GLCapabilities(profile);
		this.canvas = new GLCanvas(capabilities);
		this.director = new Director();
		this.mpp = new MazePreprocessor(maze);
		this.cpp = new ColorPreprocessor(mpp);
				
		// Configure the canvas, add KeyListener and Request Focus
		canvas.addGLEventListener(this);
		canvas.addKeyListener(director);
		canvas.setFocusable(true);
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
		camera = new Camera(glu, gl);
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
		
		// Reset the Color Presprocessor;
		cpp.reset();
	
		// Render the maze.
		render(gl);
	}

	//Method to draw something on the canvas
	private void render(GL2 gl)
	{	
		// Draw the ground for the maze to sit on.
		gl.glBegin(GL2.GL_POLYGON);
		
		// Color light green.
		gl.glColor3f(.66f, 1.0f, .66f);
		
		// Draw a nice big platform 10x10.
		gl.glVertex2d(-10, 10);
		gl.glVertex2d(10, 10);
		gl.glVertex2d(10, -10);
		gl.glVertex2d(-10, -10);
		
		gl.glEnd();
		
		// "All walls are half the walls", so only draw the first three walls.
		for(int i = 0; i < 3; i++)
		{
			// Draw all the values in the preprocessed array.
			for(double[] da : mpp.get(i))
			{
				// Draw a single wall.
				gl.glBegin(GL2.GL_POLYGON);
				
				// Set the color in the processor.
				gl.glColor3d(cpp.getR(), cpp.getG(), cpp.getB());
				cpp.next();
				
				// Draw the first side of the wall.
				gl.glVertex3d(da[2], da[3], da[5]);
				
				gl.glColor3d(cpp.getR() * 1.12, cpp.getG() * 1.12, cpp.getB() * 1.12);
				
				gl.glVertex3d(da[2], da[3], da[4]);
				
				// Change the color.
				gl.glColor3d(cpp.getR() * 1.12, cpp.getG() * 1.12, cpp.getB() * 1.12);
				cpp.next();
				
				// Draw the second half of the wall.
				gl.glVertex3d(da[0], da[1], da[4]);
				
				gl.glColor3d(cpp.getR(), cpp.getG(), cpp.getB());
				
				gl.glVertex3d(da[0], da[1], da[5]);
				
				gl.glEnd();
				
				gl.glBegin(GL.GL_LINES);
				
				gl.glColor3d(cpp.getR() * .9 , cpp.getG() * .9, cpp.getB() * .9);
				gl.glLineWidth(3.0f);
				
				gl.glVertex3d(da[0], da[1], da[4]);
				gl.glVertex3d(da[2], da[3], da[4]);
				
				gl.glVertex3d(da[0], da[1], da[5]);
				gl.glVertex3d(da[2], da[3], da[5]);
				
				gl.glVertex3d(da[0], da[1], da[4]);
				gl.glVertex3d(da[0], da[1], da[5]);
				
				gl.glVertex3d(da[2], da[3], da[4]);
				gl.glVertex3d(da[2], da[3], da[5]);
				
				gl.glEnd();
			}
		}	
	}
}