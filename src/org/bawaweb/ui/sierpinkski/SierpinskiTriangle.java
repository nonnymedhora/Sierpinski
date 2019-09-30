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
	private boolean filledInnerTriangles 	= false;
	private String direction	=	"DOWN";

	/** Constructor: an instance */
	public SierpinskiTriangle() {
		super();
	}
	
	
	/**
	 * Constructor
	 * @param innerFilled	==	inner triangles filled
	 */
	public SierpinskiTriangle(boolean innerFilled) {
		this();
		this.setFilledInnerTriangles(innerFilled);
	}
	
	/**
	 * Constructor
	 * @param dir	==	the direction is either "UP" or "DOWN"
	 * @param allFilled	==	external circum-triangle filled
	 * @param innerFilled	==	inner triangles filled
	 */
	public SierpinskiTriangle(String dir, boolean innerFilled) {
		this(innerFilled);
		this.setDirection(dir);
	}
	
	private Image createSierpinskiT(Graphics2D g, int d) {
		// Clear the frame
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);
		
		Point p1, p2, p3 = null;

		if (this.getDirection().equals("UP")) {
			// Initialize p1, p2, p3 based on frame size
			p1 = new Point(getWidth() / 2, OFFSET);
			p2 = new Point(OFFSET, getHeight() - OFFSET);
			p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);
		} else {
			// upsideDown
			p1 = new Point(OFFSET, OFFSET);
			p2 = new Point(getWidth() - OFFSET, OFFSET);
			p3 = new Point(getWidth() / 2, getHeight() - OFFSET);
		}
		// Draw Sierpinski's triangle
		drawTriangles(g, depth, p1, p2, p3);
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
		
		if (d/*epth*/ % 2 == 0) {
			fillColor = Color.yellow;
		}

		if (this.isFilledInnerTriangles()) {
			//		extra
			fillTriangle(g, p1, m12, m31, fillColor);
			fillTriangle(g, m12, p2, m23, fillColor);
			fillTriangle(g, p3, m23, m31, fillColor);
			//		ends-extra
		}
		
		fillTriangle(g, m12, m23, m31, emptyColor);		
		
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
				final FractalBase frame = new SierpinskiTriangle("UP", false);
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(WIDTH, HEIGHT);
				frame.setRunning(true);
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
	

	/*@Override
	public String getFractalDetails() {		
		return this.getFractalShapeTitle();
	}*/

	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz _ Sierpinski'Triangle";
	}	

	public boolean isFilledInnerTriangles() {
		return this.filledInnerTriangles;
	}

	public void setFilledInnerTriangles(boolean filledITriangles) {
		this.filledInnerTriangles = filledITriangles;
	}

	public String getDirection() {
		return this.direction;
	}

	public void setDirection(String dir) {
		this.direction = dir;
	}
}
