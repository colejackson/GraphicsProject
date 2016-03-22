package menu;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

// A three way button that can be loaded with images for hover, click, and normal appearance.
public final class MenuButton extends JLabel 
{

	// Serial ID
	private static final long serialVersionUID = -6053935672921515907L;
	
	public MenuButton()
	{
		super();
	}
	
	public MenuButton(String imgPath)
	{
		super(new ImageIcon(imgPath));
	}
	
	//Creates a three-way menu button
	//Different images for no mouse contact, hovered mouse, and pressed mouse
	public MenuButton(String imgPath, String pressedImagePath, String hoverImagePath)
	{
		super(new ImageIcon(imgPath));
		
		JLabel label = this;
		
		ImageIcon icon = new ImageIcon(imgPath);
		ImageIcon hover = new ImageIcon(hoverImagePath);
		ImageIcon pressed = new ImageIcon(pressedImagePath);
		
		label.addMouseListener
		(
			new MouseListener()
			{

				@Override
				public void mouseClicked(MouseEvent e) 
				{}

				@Override
				public void mousePressed(MouseEvent e) 
				{
					// Set the icon to the pressed version.
					label.setIcon(pressed);
				}

				@Override
				public void mouseReleased(MouseEvent e) 
				{
					// Icon back to the standard.
					label.setIcon(icon);
				}

				@Override
	            public void mouseEntered(MouseEvent e) 
				{
					// Icon set to hover.
	                label.setIcon(hover);
	            }

	            @Override
	            public void mouseExited(MouseEvent e) 
	            {
	            	// Icon set back to standard.
	                label.setIcon(icon);
	            }
				
			}
		);
	}

}
