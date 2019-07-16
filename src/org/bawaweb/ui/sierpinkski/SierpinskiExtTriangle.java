package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SierpinskiExtTriangle extends JFrame implements Runnable {

	private static final int HEIGHT = 500;
	private static final int WIDTH = 500;

	public static int OFFSET = 25; // pixel offset from edge

	private static int depth; // recursion depth

	/** Constructor: an instance */
	public SierpinskiExtTriangle() {
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	}

	/** Draw a line between p1 and p2 on g. */
	private static void drawLine(Graphics g, Point p1, Point p2) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	public @Override void paint(Graphics g) {
		// Clear the frame
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);

		// Initialize p1, p2, p3 based on frame size */
		Point p1 = new Point(getWidth() / 2, OFFSET);
		Point p2 = new Point(OFFSET, getHeight() - OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);

		// Draw Sierpinski's triangle
		drawTriangles(g, depth, p1, p2, p3);
	}

	private static void drawTriangles(Graphics g, int d, Point p1, Point p2, Point p3) {
		if (d == 0) { // depth is 0, draw the triangle
			drawLine(g, p1, p2);
			drawLine(g, p2, p3);
			drawLine(g, p3, p1);
			return;
		}
		
		Point p132 = thirdPt(p1,p2);
		Point p122 = twoThirdPt(p1,p2);
		Point v1 = midpoint(p1,p2);
		
		Point p233 = thirdPt(p2,p3);
		Point p223 = twoThirdPt(p2,p3);
		
		Point p331 = thirdPt(p3,p1);
		Point p321 = twoThirdPt(p3,p1);
		
		
		
		
	}

	/** = the midpoint between p1 and p2 */
	private static Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	private static Point thirdPt(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 3, (p1.y + p2.y) / 3);
	}

	private static Point twoThirdPt(Point p1, Point p2) {
		return new Point((p1.x + p2.x) * 2 / 3, (p1.y + p2.y) * 2 / 3);
	}
	
	public void Sierpinski(int d) {  
		assert(d>=0);
        depth= d; repaint();  
    }

	@Override
	public void run() {
		try {
			while (depth<10) {System.out.println("depth is "+depth);
				depth += 1;
				Thread.sleep(3000);
				Sierpinski(depth);
				run();
			}
		} catch (InterruptedException ex) {
		} 
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		      @Override
		      public void run() {
		        final SierpinskiExtTriangle frame = new SierpinskiExtTriangle();
		        frame.setTitle("Bawaz _Ext Sierpinski'Triangle");
		        frame.setSize(WIDTH, HEIGHT);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setResizable(false);
		        frame.setVisible(true);
		        
		        new Thread(frame).start();
		        
		       
		      }
		    });
	}

}
