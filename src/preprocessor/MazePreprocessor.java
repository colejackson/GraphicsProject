package preprocessor;

import java.util.ArrayList;

import maze.Maze;
import maze.MazePoint;

// This arraylist processes the positions of maze walls.
public class MazePreprocessor extends ArrayList<ArrayList<double[]>> 
{
	private static final long serialVersionUID = 4266007871842632293L;
	
	private final double WALL_BOTTOM = 0;
	private final double WALL_TOP = .2;

	// Store the maze that we are processing.
	private Maze maze;
	
	// Start the processor.
	public MazePreprocessor(Maze maze)
	{
		this.maze = maze;
		processMaze();		
	}
	
	// store all the walls in an arraylist.
	private void processMaze()
	{	
		// For all the sides of each node.
		for(int i = 0; i < 6; i++)
		{
			// Add an array to store walls in.
			this.add(i, new ArrayList<double[]>());
		}
		
		// For each point in the maze.
		for(MazePoint mp : maze)
		{		
			// For all the walls that could be around that point.
			for(int i = 0; i < 6; i++)
			{
				// If the wall exists.
				if(mp.walls[i])
				{	
					// Then build the wall with width and for all the sides/
					for(double[] da : buildWall(mp.getX(), mp.getY(), mp.get(i).getX(), mp.get(i).getY()))
					{
						// add them to the array.
						this.get(i).add(da);
					}
				}
			}
		}
	}
	
	// This farily complex method builds thickness into the walls that go into the array.
	private ArrayList<double[]> buildWall(int x_1, int y_1, int x_2, int y_2)
	{
		// Find the values in opengl coordinate for the position of walls.
		double x1 = x_1/((double)maze.getRows());
		double y1 = -(y_1-((double)maze.getRows())/2)/((double)maze.getRows()/2);
		double x2 = x_2/((double)maze.getRows());
		double y2 = -(y_2-((double)maze.getRows())/2)/((double)maze.getRows()/2);
		
		ArrayList<double[]> arr = new ArrayList<double[]>();
		double distance = Math.sqrt(Math.pow(y1-y2, 2) + Math.pow(x1-x2, 2));
		double wallWidth = .006;
			
		// Find an x and y offset for all the walls.
		double xo = (Math.abs(y1-y2)/distance)*(wallWidth/2);
		double yo = (Math.abs(x1-x2)/distance)*(wallWidth/2);
		
		// Add the offset for walls shaped like \ and those like /
		if((x1<x2 && y1<y2) || (x1>x2 && y1>y2))
		{
			arr.add(new double[] {x1-xo, y1+yo, x2-xo, y2+yo, WALL_BOTTOM, WALL_TOP});
			arr.add(new double[] {x1+xo, y1-yo, x2+xo, y2-yo, WALL_BOTTOM, WALL_TOP});
			arr.add(new double[] {x1+xo, y1-yo, x1-xo, y1+yo, WALL_BOTTOM, WALL_TOP});
			arr.add(new double[] {x2+xo, y2-yo, x2-xo, y2+yo, WALL_BOTTOM, WALL_TOP});
			arr.add(new double[] {x1-xo, y1+yo, x2-xo, y2+yo, x2+xo, y2-yo, x1+xo, y1-yo, WALL_TOP});
		}
		else
		{
			arr.add(new double[] {x1+xo, y1+yo, x2+xo, y2+yo, WALL_BOTTOM, WALL_TOP});
			arr.add(new double[] {x1-xo, y1-yo, x2-xo, y2-yo, WALL_BOTTOM, WALL_TOP});
			arr.add(new double[] {x1-xo, y1-yo, x1+xo, y1+yo, WALL_BOTTOM, WALL_TOP});
			arr.add(new double[] {x2+xo, y2+yo, x2-xo, y2-yo, WALL_BOTTOM, WALL_TOP});
			arr.add(new double[] {x1+xo, y1+yo, x2+xo, y2+yo, x2-xo, y2-yo, x1-xo, y1-yo, WALL_TOP});
		}
		
		// Return the array you just built.
		return arr;
	}
}
