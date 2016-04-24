package async;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import graphics.Scenery;

/* 
 * The Director is a special arraylist that will contain directions for the camera async.  
 * The class consistes of event listeners that add commands to the array and remove them 
 * when a button is released.  The result is a sort of bulletin board that tells the camera
 * how to behave at every refresh.
 */
public class Director extends ArrayList<String>implements KeyListener 
{
	private static final long serialVersionUID = 4866745232941706046L;

	/*
	 * General procedure: when a key is pressed, make sure the command
	 * isn't already in the array by removing it, this will happen when a
	 * button is held or something werid happens with multiple keyboards.
	 * 
	 * Next add the command to the array when the key is pressed, make sure
	 * the command change the camera the correct amount for a SINGLE FRAME
	 * of refresh, very small values or thing look jerky.
	 * 
	 * When the matching key is depressed, the command should be removed from
	 * command array.
	 */
	
	// LIST OF FINAL COMMANDS
	private final String MOVE_BACK = "f:-.003";
	private final String MOVE_FORW = "f:.003";
	private final String PIVOT_LFT = "r:-.4";
	private final String PIVOT_RGT = "r:.4";
	private final String ZOOM_OUT = "z:.02";
	private final String ZOOM_IN = "z:-.02";
	private final String ALPHA_UP = "a:-.005";
	private final String ALPHA_DOWN = "a:.005";
	private final String UP = "u:.02";
	private final String DOWN = "u:-.02";
		
	@Override
	public void keyPressed(KeyEvent k) 
	{	
		if(k.getKeyCode() == KeyEvent.VK_DOWN)
		{
			this.remove(MOVE_BACK);
			this.add(MOVE_BACK);
		}
		if(k.getKeyCode() == KeyEvent.VK_UP)
		{
			this.remove(MOVE_FORW);
			this.add(MOVE_FORW);
		}
		if(k.getKeyCode() == KeyEvent.VK_LEFT)
		{
			this.remove(PIVOT_LFT);
			this.add(PIVOT_LFT);
		}
		if(k.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			this.remove(PIVOT_RGT);
			this.add(PIVOT_RGT);
		}
		if(k.getKeyCode() == KeyEvent.VK_W)
		{
			this.remove(ZOOM_OUT);
			this.add(ZOOM_OUT);
		}
		if(k.getKeyCode() == KeyEvent.VK_S)
		{
			this.remove(ZOOM_IN);
			this.add(ZOOM_IN);
		}
		if(k.getKeyCode() == KeyEvent.VK_2)
		{
			this.remove(ALPHA_UP);
			this.add(ALPHA_UP);
		}
		if(k.getKeyCode() == KeyEvent.VK_1)
		{
			this.remove(ALPHA_DOWN);
			this.add(ALPHA_DOWN);
		}
		if(k.getKeyCode() == KeyEvent.VK_C)
		{
			Scenery.addCandle();
		}
		if(k.getKeyCode() == KeyEvent.VK_D)
		{
			Scenery.removeCandle();
		}
		if(k.getKeyCode() == KeyEvent.VK_X)
		{
			this.remove(UP);
			this.add(UP);
		}
		if(k.getKeyCode() == KeyEvent.VK_Z)
		{
			this.remove(DOWN);
			this.add(DOWN);
		}
	}

	@Override
	public void keyReleased(KeyEvent k) 
	{
		if(k.getKeyCode() == KeyEvent.VK_2)
		{
			this.remove(ALPHA_UP);
		}
		if(k.getKeyCode() == KeyEvent.VK_1)
		{
			this.remove(ALPHA_DOWN);
		}
		if(k.getKeyCode() == KeyEvent.VK_DOWN)
		{
			this.remove(MOVE_BACK);
		}
		if(k.getKeyCode() == KeyEvent.VK_UP)
		{
			this.remove(MOVE_FORW);
		}
		if(k.getKeyCode() == KeyEvent.VK_LEFT)
		{
			this.remove(PIVOT_LFT);
		}
		if(k.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			this.remove(PIVOT_RGT);
		}
		if(k.getKeyCode() == KeyEvent.VK_W)
		{
			this.remove(ZOOM_OUT);
		}
		if(k.getKeyCode() == KeyEvent.VK_S)
		{
			this.remove(ZOOM_IN);
		}
		if(k.getKeyCode() == KeyEvent.VK_X)
		{
			this.remove(UP);
		}
		if(k.getKeyCode() == KeyEvent.VK_Z)
		{
			this.remove(DOWN);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent k) {}
}
