package frame;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import driver.Utilities;
import graphics.Stage;
import maze.Maze;
import menu.MazeBar;
import menu.MenuContent;

// The JFrame object that will hold our project.
public final class MazeFrame extends JFrame 
{
	// Serial ID
	private static final long serialVersionUID = -9164525459699000796L;
	// Mode of Display in the opening Menu
	public static final int MENU_MODE = 1;
	// Mode of Display while in the Maze
	public static final int MAZE_MODE = 2;
	// Dimensions of the home screen on Load
	private final Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	// The name of our project as displayed by the window.
	private final String title = "SolarMaze";
	// Icon for the Program.
	private final ImageIcon icon = new ImageIcon("resource/icons/icon.png");
	// Size of the maze in the frame. Number in rows.
	private final int NUM_OF_ROWS = 15;
	
	//Creates a basic, empty frame
	public MazeFrame()
	{
		super();
		
		this.setTitle(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Utilities.resizeDimension(screenSize, .7));
		this.setIconImage(icon.getImage());
		this.setMinimumSize(Utilities.resizeDimension(screenSize, .4));
		this.setLocation(Utilities.relativePosition(screenSize, .15));
	}
	
	//Creates a basic frame that displays either a menu or the maze
	public MazeFrame(int mode)
	{
		this();
		
		setMode(mode);
	}
	
	// Set the mode of the MazeFrame, modes are static final variables of the frame.
	public void setMode(int mode)
	{
		// Remove whatever is in the frame beforehand.
		this.setJMenuBar(null);
		this.getContentPane().removeAll();
		
		// Switch based on the mode selected.
		switch (mode)
		{
			case MENU_MODE:
				//Set the menu bar
				this.setJMenuBar(new MazeBar(mode));
				this.getContentPane().add(new MenuContent());
				break;
			case MAZE_MODE:
				//Create a maze with specified number of rows
				Maze maze = new Maze(NUM_OF_ROWS);
				//Create canvas for the maze to be drawn on
				Stage stage = new Stage(maze);
				//Set the menu bar
				this.setJMenuBar(new MazeBar(mode));
				this.getContentPane().add(stage.canvas());
				break;
			default:
				break;
		}
	}
}
