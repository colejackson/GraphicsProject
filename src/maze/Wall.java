package maze;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;

import driver.OGL;
import graphics.Scenery;

public class Wall extends ArrayList<Side> 
{
	private static final long serialVersionUID = -8790194530751762795L;
	
	private static int lastTexture = -1;

	private static Material[] materials = null;
	private int texIndx;
	
	static
	{
		materials = new Material[1];
		
		materials[0] = new Material(Scenery.createTexture("ImagesWall/concrete.jpg"), null);
		//materials[1] = new Material(Scenery.createTexture("ImagesWall/metal1dark.jpg"), null);
		//materials[2] = new Material(Scenery.createTexture("ImagesWall/hedge.png"), null);
	}
	
	{
		this.texIndx = (int)(Math.random()* materials.length);
	}
	
	public Wall(Side... sides)
	{
		super();
		
		Arrays.stream(sides).forEach(c -> this.add(c));
	}
	
	public void glDraw()
	{	
		if(lastTexture == -1)
		{
			materials[texIndx].getTexture().enable(OGL.gl);
			materials[texIndx].getTexture().bind(OGL.gl);
		}
		else if(texIndx != lastTexture)
		{	
			materials[texIndx-1].getTexture().disable(OGL.gl);
			materials[texIndx].getTexture().enable(OGL.gl);
			materials[texIndx].getTexture().bind(OGL.gl);
			lastTexture = texIndx;
		}
				
		this.stream().forEach(c -> c.glDraw());
		
		//Done, so disable these textures
		materials[0].getTexture().disable(OGL.gl);
		//materials[1].getTexture().disable(OGL.gl);
	}
	
	public Point2D.Double center()
	{
		return this.stream().filter(p -> p.isTop).findFirst().get().center();
	}

	public int getTexIndx() {
		return texIndx;
	}
	
}
