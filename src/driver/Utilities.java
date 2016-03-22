package driver;

import java.awt.Dimension;
import java.awt.Point;

// A Utilities class for the SolarMaze Project, abstract class with static helper methods.
public abstract class Utilities 
{
	// Method to resize a dimension along a single resize factor.
	public static Dimension resizeDimension(Dimension dim, double factor)
	{
		// Resize each dimension.
		int _scaled_width = (int)(dim.width * factor);
		int _scaled_height = (int)(dim.height * factor);
		
		// Return the newly created dimension object.
		return new Dimension(_scaled_width, _scaled_height);
	}
	
	// Returns the point representing a relative position withon a dimension.
	public static Point relativePosition(Dimension dim, double factorX, double factorY)
	{
		if (factorX < 0 || factorY < 0)
		{
			System.err.println("Cannot Find Dimension Position to Negative Factor! -CJ");
			return new Point(0,0);
		}
		
		int _x_factor = (int)(dim.width * factorX);
		int _y_factor = (int)(dim.height * factorY);
		
		return new Point(_x_factor, _y_factor);
	}
	
	// Returns the position of a relative position within a dimension, with a single factor for x and y.
	public static Point relativePosition(Dimension dim, double factor)
	{
		if (factor < 0)
		{
			System.err.println("Cannot Find Dimension Position to Negative Factor! -CJ");
			return new Point(0,0);
		}
		
		int _x_factor = (int)(dim.width * factor);
		int _y_factor = (int)(dim.height * factor);
		
		return new Point(_x_factor, _y_factor);
	}

	//Counter function used to find total number of nodes based on number of rows
	//Sums numbers like 0+1+2+3+...+rows
	public static int fooCount(int rows)
	{
		int count = 0;
		
		for(int i = 1; i <= rows; i++)
			count += i;
		
		return count;
	}
	
	// Helper mehtod, distance between two points without creating a Point object pair.
	public static double distance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt((Math.pow((x1-x2), 2)) + (Math.pow((y1-y2), 2)));
	}
	
}
