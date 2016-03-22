package menu;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

// This panel will contain the menu content that appears in the JFrame on program start.
public final class MenuContent extends JPanel 
{
	// Serial ID
	private static final long serialVersionUID = -1035737370985582720L;

	public MenuContent()
	{
		super();
		
		//this.setBackground(Color.WHITE);
		//this.repaint();
		
		//Create a three-way menu button
		MenuButton button = new MenuButton("resources/images/buttons/button.png","resources/image/buttons/pressed.png","resource/image/buttons/hover.png");
		
		//Add the button to the menu
		this.add(button);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		int width = this.getWidth();
		int height = this.getHeight();
		@SuppressWarnings("unused")
		Dimension dim = new Dimension(width, height);
		
		// Paint a background.
		g.drawImage(new ImageIcon("resources/images/background/null.jpg").getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	}
}
