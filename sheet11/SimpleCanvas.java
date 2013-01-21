/*
 * Author: Frido Koch
 * Email: frido@vresh.net
 * License: CC BY-NC-SA 3.0 DE 
 * http://creativecommons.org/licenses/by-nc-sa/3.0/de/
 */
package sheet11;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class SimpleCanvas extends JComponent {

	private BufferedImage image;
	
	public SimpleCanvas()	{
		
		this.image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		this.setPreferredSize(new Dimension(640, 480));
		this.addMouseMotionListener( new MouseMotionAdapter()	{
			
			@Override
			public void mouseDragged(MouseEvent me)	{
				Graphics g = image.getGraphics();
				g.setColor(Color.BLACK);
				g.fillOval(me.getX() - 4, me.getY() - 4, 8, 8);
				repaint(me.getX() - 4, me.getY() - 4, 8, 8);
			}
	
		});
		
		this.addComponentListener( new ComponentAdapter()	{
			
			public void componentResized(ComponentEvent e)	{
				BufferedImage img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
				Graphics g = img.getGraphics();
				g.drawImage(image, 0, 0, null);
				image = img;
			}
			
		});
		
	}

	public void paint(Graphics g)	{
		g.drawImage(this.image, 0, 0, null);
	}
	
	public static void main(String args[]) {
		
		JFrame main = new JFrame("SimpleCanvas");
		main.add(new SimpleCanvas());
		main.pack();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
		


	}
}
