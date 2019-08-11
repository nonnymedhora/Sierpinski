package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SierpinskiTriangle extends FractalBase {
	
	private static final long serialVersionUID = 123456543L;

	/** Constructor: an instance */
	public SierpinskiTriangle() {
		super();
	}
	
	private Image createSierpinskiT(Graphics2D g, int d) {
		// Clear the frame
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);

		/*// Initialize p1, p2, p3 based on frame size
		Point p1 = new Point(getWidth() / 2, OFFSET);
		Point p2 = new Point(OFFSET, getHeight() - OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);

		 */
		
		//upsideDown
		Point p1d = new Point(OFFSET, OFFSET);
		Point p2d = new Point(getWidth()-OFFSET, OFFSET);
		Point p3d = new Point(getWidth() /2, getHeight()-OFFSET);
		
		// Draw Sierpinski's triangle
		drawTriangles(g,depth,p1d,p2d,p3d);
		return bufferedImage;
	}
	/**
	 * Draw a Sierpinski triangle of depth d with perimeter given by p1, p2, p3.
	 * p1-p2 is the base line, p3 the top point
	 */
	private /*static*/ void drawTriangles(Graphics2D g, int d, Point p1, Point p2, Point p3) {
		g.setColor(Color.red);
		if (d == 0) { // depth is 0, draw the triangle
			g.setStroke(new BasicStroke(1));
			drawLine(g, p1, p2);
			drawLine(g, p2, p3);
			drawLine(g, p3, p1);
			return;
		}
		// Draw three Sierpinski triangles of depth d-1
		Point m12 = midpoint(p1, p2);
		Point m23 = midpoint(p2, p3);
		Point m31 = midpoint(p3, p1);
		
		Color fillColor = Color.green;
		Color emptyColor = Color.blue;
		
		if (depth % 2 == 0) {
			fillColor = Color.yellow;
		}
		
		//		extra
		fillTriangle(g,p1,m12,m31,fillColor);
		fillTriangle(g,m12,p2,m23,fillColor);
		fillTriangle(g,p3,m23,m31,fillColor);
		//		ends-extra
		
		fillTriangle(g, m12, m23, m31,emptyColor);
		
		/*drawTriangle(g,p1,m12,m31,g.getColor());
		drawTriangle(g,m12,p2,m23,g.getColor());
		drawTriangle(g,m31,m23,p3,g.getColor());*/

		drawTriangles(g, d - 1, p1, m12, m31);
		drawTriangles(g, d - 1, m12, p2, m23);
		drawTriangles(g, d - 1, m31, m23, p3);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new SierpinskiTriangle();
				frame.setTitle("Bawaz _ Sierpinski'Triangle");
				frame.setSize(WIDTH, HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				new Thread(frame).start();

			}
		});

	}

	@Override
	public void createFractalShape(Graphics2D g) {
		createSierpinskiT(g,depth);
		
	}
}
