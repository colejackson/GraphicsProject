package preprocessor;

import java.util.ArrayList;

/*
 * This class represents the array of precomputed colors that we
 * can give to our array, this save processing power at since we
 * don't have to compute what color to use every time there is a
 * refresh.
 */
public class ColorPreprocessor extends ArrayList<double[]> 
{
	private static final long serialVersionUID = 7539254017166317833L;
	
	private static final double HUE_R = 255.0/255.0;
	private static final double HUE_G = 190.0/255.0;
	private static final double HUE_B = 64.0/255.0;
	
	private static final double DEGREE_OF_VARIATION = .06;
	
	int currentIndex;
	int max;

	// Begin the preprocessor.
	public ColorPreprocessor(ArrayList<ArrayList<double[]>> arrTop)
	{	
		for(ArrayList<double[]> arrMiddle : arrTop)
		{
			for(double[] arr : arrMiddle)
			{
				if(arr.length < 7)
				{
					GenerateValue(arr[0], arr[1]);
					GenerateValue(arr[2], arr[3]);
				}
				else if(arr.length >= 7)
				{
					GenerateValue(arr[0], arr[1]);
					GenerateValue(arr[2], arr[3]);
					GenerateValue(arr[4], arr[5]);
					GenerateValue(arr[6], arr[7]);
				}
			}
		}
	}
	
	public void GenerateValue(double x, double y)
	{
		double factor = (DEGREE_OF_VARIATION*Math.sin(x*100)*Math.sin(y*100)) + 1;
		
		System.out.println(factor);
		
		this.add(new double[] {HUE_R*factor, HUE_G*factor, HUE_B*factor});
		
		System.out.println();
	}
	
	// Return the next color values.
	public void next()
	{
		currentIndex++;	
	}
	
	// Return the current red value
	public double getR()
	{
		return this.get(currentIndex)[0];
	}
	
	// Return the current green value
	public double getG()
	{
		return this.get(currentIndex)[1];
	}
	
	// Return the current blue value
	public double getB()
	{
		return this.get(currentIndex)[2];
	}
	
	// Reset the preprocessor to start again.
	public void reset()
	{
		currentIndex = 0;
	}
}
