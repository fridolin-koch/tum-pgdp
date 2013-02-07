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

/**
 * @author Frido Koch
 */
public class SimpleCanvas extends JComponent {

    private static final long serialVersionUID = 4806715466634351506L;

    private BufferedImage image;

	public SimpleCanvas()	{

	    //create image for the drawing
		this.image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		//set initial window size
		this.setPreferredSize(new Dimension(640, 480));
		//listen to mouse events
		this.addMouseMotionListener( new MouseMotionAdapter()	{
			public void mouseDragged(MouseEvent me)	{
				//draw on mouse drag
			    Graphics g = image.getGraphics();
				g.setColor(Color.BLACK);
				g.fillOval(me.getX() - 4, me.getY() - 4, 8, 8);
				//repaint the area where the mouse was dragged
				repaint(me.getX() - 4, me.getY() - 4, 8, 8);
			}
		});
		//listen to window-resize event
		this.addComponentListener( new ComponentAdapter()	{
			public void componentResized(ComponentEvent e)	{
			    //create fitting image for new window size
				BufferedImage img = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
				Graphics g = img.getGraphics();
				//copy old image
				g.drawImage(image, 0, 0, null);
				image = img;
			}
		});

	}

	public void paint(Graphics g)	{
		g.drawImage(this.image, 0, 0, null);
	}

	public static void main(String args[]) {
	    //create canvas frame
		JFrame main = new JFrame("SimpleCanvas");
		main.add(new SimpleCanvas());
		main.pack();
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setVisible(true);
	}
}
