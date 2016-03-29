package swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public final class MazeBar extends JMenuBar 
{
	// Serial ID
	private static final long serialVersionUID = 2468356696386354148L;
	// Mode of the menu bar in the main menu.
	public static final int MENU_MODE = 1;
	// Mode of the menu bar in maze.
	public static final int MAZE_MODE = 2;
	
	public MazeBar()
	{
		super();
	}
	
	//Create a menu bar for either the menu or maze display
	public MazeBar(int mode)
	{
		this();
		
		switch(mode)
		{
			case MENU_MODE:
				menuModeInit();
				break;
			case MAZE_MODE:
				mazeModeInit();
				break;
			default:
				break;
		}
	}
	
	//Creates a menu for when in menu display mode
	private void menuModeInit()
	{
		// TODO: Add Substantial Menu Items.
		
		final JMenu exampleMenu = new JMenu("Example Menu");
		this.add(exampleMenu);
		
		final JMenuItem exampleItemMenu = new JMenuItem("Example Item Menu");
		exampleMenu.add(exampleItemMenu);
	}
	
	//Creates a menu for when in maze display mode
	private void mazeModeInit()
	{
		// TODO: Add Substatial Menu Items.
		
		final JMenu exampleMenu = new JMenu("Example Maze");
		this.add(exampleMenu);
		
		final JMenuItem exampleItemMenu = new JMenuItem("Example Item Maze");
		exampleMenu.add(exampleItemMenu);
	}
}
