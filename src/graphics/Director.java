package graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

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
	private final String PIVOT_LFT = "r:-.5";
	private final String PIVOT_RGT = "r:.5";
	private final String ZOOM_OUT = "z:.02";
	private final String ZOOM_IN = "z:-.02";
	
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
	}

	@Override
	public void keyReleased(KeyEvent k) 
	{
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
	}
	
	@Override
	public void keyTyped(KeyEvent k) {}
}
