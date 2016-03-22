package maze;

import java.util.ArrayList;

public class MazePoint extends ArrayList<MazePoint>
{
	private static final long serialVersionUID = -5026696560676903634L;

	//The 6 surrounding nodes of a Maze Point
	public static final int UPPER_RIGHT = 0;
	public static final int RIGHT = 1;
	public static final int LOWER_RIGHT = 2;
	public static final int LOWER_LEFT = 3;
	public static final int LEFT = 4;
	public static final int UPPER_LEFT = 5;
	
	//x,y coordinates
	private int x;
	private int y;
	//The 6 surrounding walls of a Maze Point
	public boolean[] walls = new boolean[6];
	private boolean isSet;
	
	MazePoint(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.isSet = false;
		
		//Initialize the walls array to false
		for(@SuppressWarnings("unused") boolean b : this.walls)
		{
			b = false;
		}
	}
	
	//Initialize inner Maze Point (6 surrounding nodes)
	public void init(Maze nodes)
	{
		// So this is farily complicated to understand, for help try reading the Maze.nodes comments.
		this.add(nodes.get(x+1,y-1));
		this.add(nodes.get(x+2,y));
		this.add(nodes.get(x+1,y+1));
		this.add(nodes.get(x-1,y+1));
		this.add(nodes.get(x-2,y));
		this.add(nodes.get(x-1,y-1));
	}
	
	//Initialize Maze Point that is on an outer boundary
	public void init(int side, Maze nodes)
	{
		this.init(nodes);
		
		nullSide(side);
	}
	
	//Initialize Maze Point that is an outer vertex
	public void init(int side1, int side2, Maze nodes)
	{
		this.init(side1, nodes);
		
		nullSide(side2);
	}
	
	// X accessor
	public int getX()
	{
		return x;
	}
	
	// Y accessor
	public int getY()
	{
		return y;
	}
	
	//Check if the Maze Point is an outer boundary point
	//If so, return what side it is part of
	public int isBoundary(int rows)
	{
		if(Math.abs(x) == Math.abs(y) || y+1 == rows)
		{
			if(y+1 == rows)
				return Maze.BOTTOM_SIDE;
			else if(x < 0)
				return Maze.LEFT_SIDE;
			else if(x > 0)
				return Maze.RIGHT_SIDE;
		}
		
		return -1;	
	}
	
	//Check if the Maze Point is one of the 3 outer vertices
	//If so, return which vertex it is
	public int isVertex(int rows)
	{
		if((Math.abs(x) == Math.abs(y) && y+1 == rows) || y == 0)
		{
			if(x == 0)
				return Maze.TOP_VERTEX;
			else if(x > 0)
				return Maze.RIGHT_VERTEX;
			else if(x < 0)
				return Maze.LEFT_VERTEX;
		}

		return -1;
	}
	
	// Set the node as added.
	public void set()
	{
		this.isSet = true;
	}
	
	// Null out a side of the point, this means it is a boundary.
	private void nullSide(int side)
	{
		int nulled1 = 0, nulled2 = 0;
		
		switch(side)
		{
			case Maze.LEFT_SIDE: nulled1 = UPPER_LEFT; nulled2 = LEFT; break;
			case Maze.RIGHT_SIDE: nulled1 = UPPER_RIGHT; nulled2 = RIGHT; break;
			case Maze.BOTTOM_SIDE: nulled1 = LOWER_LEFT; nulled2 = LOWER_RIGHT; break;
		}
		
		this.set(nulled1, null);
		this.set(nulled2, null);
	}
	
	// accessor for isSet
	public boolean isSet()
	{
		return this.isSet;
	}
	
	// Is the node array full.
	public boolean isFull()
	{
		for(MazePoint mp : this)
		{
			if(mp == null)
				continue;
			
			if(mp.isSet())
				continue;
			
			return false;
		}
		
		return true;
	}
	
}
