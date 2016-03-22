package maze;

import java.util.ArrayList;

import driver.Utilities;

public class Maze extends ArrayList<MazePoint> 
{
	private static final long serialVersionUID = 5159392545871313772L;
	
	//The 3 sides of the outer triangle
	public static final int LEFT_SIDE = 9849;
	public static final int RIGHT_SIDE = 3859;
	public static final int BOTTOM_SIDE = 4596;
	
	//The 3 vertices of the outer triangle
	public static final int TOP_VERTEX = 10;
	public static final int RIGHT_VERTEX = 20;
	public static final int LEFT_VERTEX = 30;
	
	// An arraylist of points that have been added to the maze, from this we randomly select the next branch point.
	private ArrayList<MazePoint> added;
	
	// This int will increase when we initiate node, and deincrement as we add nodes to the maze, starts at zero ends at zero.
	private int isSet;
	private int rows;
	
	//Creates a triangular maze with a given number of rows
	public Maze(int rows)
	{
		// set the instance variables.
		this.rows = rows;
		isSet = 0;
		
		//Create the nodes
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j <= i; j++)
			{
				//Create new Maze Point at specified x,y location and add to the maze
				this.add(new MazePoint(((j*2) - i), i));
				// Increment isSet for every node.
				isSet++;
			}
		}
				
		//Connect the nodes to each other
		//Initializing the MP depends on how many surrounding nodes the MP has
		for(MazePoint mp : this)
		{
			//Check if the Maze Point is on an outer boundary or is an outer vertex
			int boundary = mp.isBoundary(rows);
			int vertex = mp.isVertex(rows);
			
			//If it is an outer vertex, initialize it corresponding to which vertex it is
			if(vertex > 0)
			{
				if(vertex == TOP_VERTEX)
					mp.init(LEFT_SIDE,RIGHT_SIDE,this);
				else if(vertex == RIGHT_VERTEX)
					mp.init(RIGHT_SIDE,BOTTOM_SIDE,this);
				else if(vertex == LEFT_VERTEX)
					mp.init(BOTTOM_SIDE,LEFT_SIDE,this);
			}
			//If it's on an outer boundary, initialize it corresponding to which boundary it's on
			else if(boundary > 0)
			{
				if(boundary == BOTTOM_SIDE)
					mp.init(BOTTOM_SIDE,this);
				else if(boundary == LEFT_SIDE)
					mp.init(LEFT_SIDE,this);
				else if(boundary == RIGHT_SIDE)
					mp.init(RIGHT_SIDE,this);
			}
			//If it's neither, initialize it with 6 surrounding points
			else
			{
				mp.init(this);
			}
		}
		
		// Create an Array to store maze points added to the maze algorithm
		added = new ArrayList<MazePoint>();
		
		// Create the boundaries.
		createBoundary(rows);
		
		// Run the maze creation algorithm.
		createMaze();

	}

	// Return a specific point in the maze at row y, number x
	//Gets a Maze Point's relative location
	public MazePoint get(int x, int y)
	{
		try
		{
			/* 
			 * -y is the x coordinate of the first node in every row, so add the x offset to that and divide by
			 * two because nodes are seperated by two units from the nodes on their left and right.  and finally
			 * add this to the count of nodes before that row, this gets a node based on the virtual coordinates.
			 */
			return this.get(Utilities.fooCount(y) + ((x+y)/2));
		}
		catch(Exception e)
		{
			return null;
		}		
	}
	
	//Builds the actual maze
	private void createMaze() 
	{
		//Go through each Maze Point
		while(isSet > 0)
		{		
			// Choose the next node to add at random.
			int rand = (int)(Math.random()*added.size());
			MazePoint target = added.get(rand);
			
			// If the target node has no more nodes that need adding then remove it from the list.
			if(target.isFull())
			{
				added.remove(rand);
				continue;
			}
			
			// Build the maze from zero to six, this is important because it makes the maze semi predictable
			// Without this there would be a lot of sharp turns in the maze.
			for(int i = 0; i<6; i++)
			{
				// get the other maze point.
				MazePoint mp = target.get(i);
				
				// If the point hasnt already been set.
				if(mp != null && !mp.isSet())
				{	
					// Set the true flaggs for both nodes idependently.
					mp.walls[(i+3)%6] = true;
					target.walls[i] = true;
					
					// The node has been set.
					mp.set();
					isSet--;
					
					// Add the node to the added array so it may be chosen
					added.add(mp);
					
					// If it is full then remove it from the added array and continue.
					if(target.isFull())
					{
						added.remove(rand);
					}
					
					// Break, cause we found a node to add.
					break;
				}
			}
		}		
	}

	// Add a node as a boundary node, unless it is a vertice then add is as a vertice.
	private void createBoundary(int rows) 
	{
		// For each node.
		for(MazePoint mp : this)
		{			
			// Handle the vertices.
			if(mp.isVertex(rows) > 0)
			{
				createVertex(mp, rows);
				continue;
			}
			
			// Which boundary is it if it is such.
			int boundary = mp.isBoundary(rows);
			
			// Slightly different cases based on which boundary it is.  Little difficult to read, pretty stable though, didn't want a lot of lines here.
			switch(boundary)
			{
				// Basic procedure, specify which sides will be walls, set the point and deincrement the isSet counter, add the node to the current maze array.
				case RIGHT_SIDE: mp.walls[MazePoint.UPPER_LEFT] = true; mp.walls[MazePoint.LOWER_RIGHT] = true; mp.set(); isSet--; added.add(mp); break;
				case LEFT_SIDE: mp.walls[MazePoint.UPPER_RIGHT] = true; mp.walls[MazePoint.LOWER_LEFT] = true; mp.set(); isSet--; added.add(mp); break;
				case BOTTOM_SIDE: mp.walls[MazePoint.LEFT] = true; mp.walls[MazePoint.RIGHT] = true; isSet--; mp.set(); added.add(mp); break;
				default: break;
			}				
		}
	}

	// Method for creating a vertice.
	private void createVertex(MazePoint mp, int rows) 
	{
		// Decide which vertex is it.
		int vertex = mp.isVertex(rows);
		
		// Same as the switch for boundaries above, hard to read, but everything is there and it is compact.
		switch(vertex)
		{
			case TOP_VERTEX: mp.walls[MazePoint.LOWER_LEFT] = true; mp.walls[MazePoint.LOWER_RIGHT] = true; mp.set(); isSet--; added.add(mp); break;
			case RIGHT_VERTEX: mp.walls[MazePoint.UPPER_LEFT] = true; mp.walls[MazePoint.LEFT] = true; mp.set(); isSet--; added.add(mp); break;
			case LEFT_VERTEX: mp.walls[MazePoint.UPPER_RIGHT] = true; mp.walls[MazePoint.RIGHT] = true; mp.set(); isSet--; added.add(mp); break;
			default: break;
		}	
	}
	
	// Getter for the number of rows.
	public int getRows()
	{
		return rows;
	}
}
