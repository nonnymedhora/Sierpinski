package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SierpinskiTriangle extends JFrame implements Runnable {

	private static final long serialVersionUID = 123456543L;
	private static final int HEIGHT = 600;
	private static final int WIDTH = 600;

	public static int OFFSET = 25; // pixel offset from edge

	private static int depth; // recursion depth
	private static final int MAX_DEPTH = 10;

	/** Constructor: an instance */
	public SierpinskiTriangle() {
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g1) {
		Image img = createSierpinskiT();
		Graphics2D g = (Graphics2D) g1;
		g.drawImage(img, null, null);
		g.dispose();
	}

	private Image createSierpinskiT() {
		BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		// Clear the frame
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);

		// Initialize p1, p2, p3 based on frame size
		Point p1 = new Point(getWidth() / 2, OFFSET);
		Point p2 = new Point(OFFSET, getHeight() - OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);

		// Draw Sierpinski's triangle
		drawTriangles(g, depth, p1, p2, p3);
		return bufferedImage;
	}

	/** Draw a line between p1 and p2 on g. */
	private static void drawLine(Graphics2D g, Point p1, Point p2) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	/** = the midpoint between p1 and p2 */
	private static Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	/**
	 * Draw a Sierpinski triangle of depth d with perimeter given by p1, p2, p3.
	 * p1-p2 is the base line, p3 the top point
	 */
	private static void drawTriangles(Graphics2D g, int d, Point p1, Point p2, Point p3) {
		g.setColor(Color.red);
		if (d == 0) { // depth is 0, draw the triangle
			g.setStroke(new BasicStroke(1));//((int)3/(d+1)));
			drawLine(g, p1, p2);
			drawLine(g, p2, p3);
			drawLine(g, p3, p1);
			return;
		}
		// Draw three Sierpinski triangles of depth d-1
		Point m12 = midpoint(p1, p2);
		Point m23 = midpoint(p2, p3);
		Point m31 = midpoint(p3, p1);

		fillTriangle(g, m12, m23, m31);

		drawTriangles(g, d - 1, p1, m12, m31);
		drawTriangles(g, d - 1, m12, p2, m23);
		drawTriangles(g, d - 1, m31, m23, p3);
	}

	private static void fillTriangle(Graphics2D g, Point m12, Point m23, Point m31) {
		Path2D myPath = new Path2D.Double();
		myPath.moveTo(m12.x, m12.y);
		myPath.lineTo(m23.x, m23.y);
		myPath.lineTo(m31.x, m31.y);
		myPath.closePath();

		g.setPaint(Color.blue);
		g.fill(myPath);

	}

	/**
	 * Draw a Sierpinski triangle of depth d. Precondition: d >= 0.
	 */
	public void Sierpinski(int d) {
		depth = d;
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final SierpinskiTriangle frame = new SierpinskiTriangle();
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
	public void run() {
		try {
			while (depth < MAX_DEPTH) {
				System.out.println("depth is " + depth);
				depth += 1;
				Thread.sleep(6000);
				Sierpinski(depth);
				run();
			}
		} catch (InterruptedException ex) {
		}
	}
}
