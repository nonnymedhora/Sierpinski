/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class FractalBaseSample extends FractalBase {

	public FractalBaseSample() {
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void createFractalShape(Graphics2D g) {
		final Point center = new Point((WIDTH - OFFSET * 2) / 2, (HEIGHT - OFFSET * 2) / 2);
		System.out.println("Center is P(" + center.x + "," + center.y + ")");
		final int length = 50 * depth - 1;
		System.out.println("length is " + length);

		drawSquare(g, center, length * 2, Color.blue);

		g.setColor(Color.green);
		drawSquare(g, center, length);

		g.setColor(Color.red);
		drawCircle(g, center, length);

		drawCircle(g, center, length / 2, Color.pink);
		fillSquare(g, center, length / 4, Color.orange);

		g.setStroke(new BasicStroke(2));
		g.setColor(Color.red);
		drawEquilateralTriangle(g, center, length);

	}

}

/**
 * @author Navroz Superclass for SierpinskiTriangle, SiepinskiSquare,
 *         KochSnowflake
 */
public abstract class FractalBase extends JFrame implements Runnable {

	public static void main(String[] arf) {
		final FractalBase ff = new FractalBaseSample();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = ff;
				frame.setTitle("Bawaz _ FractalBaseSample");
				frame.setSize(frame.WIDTH, frame.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				new Thread(frame).start();

			}
		});
	}

	private static final long serialVersionUID = 123456543L;
	static final int HEIGHT = 600;
	static final int WIDTH = 600;

	boolean isOriginUpperLeft = true; // location of origin
	static int OFFSET = 25; // pixel offset from edge

	static int depth; // recursion depth
	static final int MAX_DEPTH = 10;

	final static int maxIter = 255; // maximum iterations to check for
									// Mandelbrot

	BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

	/** Constructor: an instance */
	public FractalBase() {
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	}

	public void drawEquilateralTriangle(Graphics2D g, Point center, int length) {
		System.out.println("In createET");
		System.out.println("Cenetr---P(" + center.x + "," + center.y + ")");
		System.out.println("L is " + length);
		// -- go up
		Line up = new Line(center.x, center.y, length * 2 / 3, 90.0);
		Point vertex = new Point((int) up.getX2(), (int) up.getY2());
		System.out.println("Vertex is P(" + vertex.x + "," + vertex.y + ")");

		// -- go down
		Line down = new Line(center.x, center.y, length / 3, 270.0);
		Point midBase = new Point((int) down.getX2(), (int) down.getY2());

		// --go-left
		Line leftB = new Line(midBase.x, midBase.y, length / 2, 180.0);
		Line rightB = new Line(midBase.x, midBase.y, length / 2, 0.0);

		Point lftB = new Point((int) leftB.getX2(), (int) leftB.getY2());
		Point rtB = new Point((int) rightB.getX2(), (int) rightB.getY2());
		System.out.println("lftB is P(" + lftB.x + "," + lftB.y + ")");
		System.out.println("rtB is P(" + rtB.x + "," + rtB.y + ")");

		Path2D myPath = new Path2D.Double();
		myPath.moveTo(vertex.x, vertex.y);
		myPath.lineTo(lftB.x, lftB.y);
		myPath.lineTo(rtB.x, rtB.y);
		myPath.closePath();

		g.draw(myPath);

		// /*g.drawLine(vertex.x,vertex.y,lftB.x,lftB.y);
		// g.drawLine(vertex.x,vertex.y,rtB.x,rtB.y);
		// g.drawLine(lftB.x,lftB.y,rtB.x,rtB.y);*/

		/*
		 * Line l1 = new Line(vertex, lftB); Line l2 = new Line(vertex, rtB);
		 * Line l3 = new Line(lftB, rtB);
		 * 
		 * l1.draw(g); l2.draw(g); l3.draw(g);
		 */

	}

	public void fillSquare(Graphics2D g, Point center, int length, Color color) {
		g.setColor(color);
		this.fillSquare(g, center, length);
	}

	public void fillSquare(Graphics2D g, Point center, int length) {
		Point pu1 = setupSquare(center, length);
		g.fillRect(pu1.x, pu1.y, length, length);
	}

	private Point setupSquare(Point center, int length) {
		// go-left
		Line left = new Line(center.x, center.y, length / 2, 180.0);
		Point p1l = new Point((int) left.getX2(), (int) left.getY2());

		// go-up
		Line up = new Line(p1l.x, p1l.y, length / 2, 90.0);
		Point pu1 = new Point((int) up.getX2(), (int) up.getY2());
		return pu1;
	}

	public void drawSquare(Graphics2D g, Point center, int length, Color color) {
		g.setColor(color);
		this.drawSquare(g, center, length);
	}

	public void drawSquare(Graphics2D g, Point center, int length) {
		/*
		 * // go-left Line left = new Line(center.x, center.y, length / 2,
		 * 180.0); Point p1l = new Point((int) left.x, (int) left.y);
		 * 
		 * // go-up Line up = new Line(left.x, left.y, length / 2, 90.0); Point
		 * pu1 = new Point((int) up.x, (int) up.y);
		 */
		Point pu1 = setupSquare(center, length);
		g.drawRect(pu1.x, pu1.y, length, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (depth < MAX_DEPTH) {
				// System.out.println("depth is " + depth);
				depth += 1;
				Thread.sleep(3000);
				generate(depth);
				run();
			}
		} catch (InterruptedException ex) {
		}
	}

	private void generate(int d) {
		depth = d;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g1) {
		// TODO Auto-generated method stub
		super.paint(g1);
		Image img = createFractalImage();
		Graphics2D g2 = (Graphics2D) g1;
		g2.drawImage(img, null, null);
		g2.dispose();
	}

	private Image createFractalImage() {
		Graphics2D g = bufferedImage.createGraphics();
		// Clear the frame
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);

		createFractalShape(g);
		return bufferedImage;
	}

	public abstract void createFractalShape(Graphics2D g);

	protected void setPixel(int col, int row, int rgbColor) {
		// bufferedImage.setRGB(col, row, rgbColor);
		validateColumnIndex(col);
		validateRowIndex(row);
		if (isOriginUpperLeft)
			bufferedImage.setRGB(col, row, rgbColor);
		else
			bufferedImage.setRGB(col, HEIGHT - row - 1, rgbColor);
	}

	private void validateRowIndex(int row) {
		if (row < 0 || row >= HEIGHT)
			throw new IllegalArgumentException("row index must be between 0 and " + (HEIGHT - 1) + ": " + row);
	}

	private void validateColumnIndex(int col) {
		if (col < 0 || col >= WIDTH)
			throw new IllegalArgumentException("column index must be between 0 and " + (WIDTH - 1) + ": " + col);
	}

	protected void fillCircle(Graphics2D g, Point center, int radius, Color color) {
		g.setColor(color);
		this.fillCircle(g, center, radius);
	}

	protected void fillCircle(Graphics2D g, Point center, int radius) {
		/*
		 * // go-left Line left = new Line(center.x, center.y, radius / 2,
		 * 180.0); Point p1l = new Point((int) left.x, (int) left.y);
		 * 
		 * // go-up Line up = new Line(left.x, left.y, radius / 2, 90.0); Point
		 * pu1 = new Point((int) up.x, (int) up.y);
		 */

		Point pu1 = setupSquare(center, radius * 2);
		g.fillOval(pu1.x, pu1.y, radius * 2, radius * 2);

	}

	protected void drawCircle(Graphics2D g, Point center, int radius, Color color) {
		g.setColor(color);
		this.drawCircle(g, center, radius);
	}

	protected void drawCircle(Graphics2D g, Point center, int radius) {
		/*
		 * // go-left Line left = new Line(center.x, center.y, radius / 2,
		 * 180.0); Point p1l = new Point((int) left.getX2(), (int)
		 * left.getY2());
		 * 
		 * // go-up Line up = new Line(p1l.x, p1l.y, radius / 2, 90.0); Point
		 * pu1 = new Point((int) up.getX2(), (int) up.getY2());
		 */

		Point pu1 = setupSquare(center, radius * 2);
		g.drawOval(pu1.x, pu1.y, radius * 2, radius * 2);

	}

	protected double distance(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	/** Draw a line between p1 and p2 on g. */
	protected void drawLine(Graphics2D g, Point p1, Point p2) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	protected Point midpoint(Line l) {
		return midpoint(new Point((int) l.x, (int) l.x), new Point((int) l.getX2(), (int) l.getY2()));
	}

	/** = the midpoint between p1 and p2 */
	protected Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}

	protected Point thirdPtH(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 3), pLeft.y);
	}

	protected Point thirdPtV(Point pTop, Point pBottom) {
		return new Point(pTop.x, (int) (pTop.y + (pBottom.y - pTop.y) / 3));
	}

	protected Point twoThirdPtH(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) * 2 / 3), pLeft.y);
	}

	protected Point twoThirdPtV(Point pTop, Point pBottom) {
		return new Point(pTop.x, (int) (pTop.y + (pBottom.y - pTop.y) * 2 / 3));
	}

	protected Point mergeXYPt(Point p1, Point p2) {
		return new Point(p1.x, p2.y);
	}

	protected void fillTriangle(Graphics2D g, Point p1, Point p2, Point p3, Color color) {
		Path2D myPath = new Path2D.Double();
		myPath.moveTo(p1.x, p1.y);
		myPath.lineTo(p2.x, p2.y);
		myPath.lineTo(p3.x, p3.y);
		myPath.closePath();

		g.setPaint(color);
		g.fill(myPath);
	}

	class Line {
		private double x, y, length, angle;

		public Line(Point p1, Point p2) {
			super();
			this.x = p1.x;
			this.y = p1.y;

			this.length = this.length(p1, p2);
			this.angle = this.angle(p1, p2);
		}

		/**
		 * @param x
		 *            - x-coordinate of start point
		 * @param y
		 *            - y-coordinate of start point
		 * @param length
		 *            - length of line
		 * @param angle
		 *            - line angle
		 */
		public Line(double x, double y, double length, double angle) {
			super();
			this.x = x;
			this.y = y;
			this.length = length;
			this.angle = angle;
		}

		// Getting the second x coordinate based on the angle and length
		public double getX2() {
			return x + Math.cos(angle * (Math.PI / 180.0)) * length;
		}

		// Getting the second y coordinate based on the angle and length
		public double getY2() {
			return y + Math.sin(angle * (Math.PI / 180.0)) * length;
		}

		public void draw(Graphics g) {
			g.drawLine((int) x, (int) y, (int) getX2(), (int) getY2());
		}

		public void drawLine(Graphics2D g, Point p1, Point p2) {
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}

		public Point midPt(Point pLeft, Point pRight) {
			return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 2), (int) (pLeft.y + (pRight.y - pLeft.y) / 2));
		}

		public Point thirdPt(Point pLeft, Point pRight) {
			return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 3), (int) (pLeft.y + (pRight.y - pLeft.y) / 3));
		}

		public Point twoThirdPt(Point pLeft, Point pRight) {
			return new Point((int) (pLeft.x + (pRight.x - pLeft.x) * 2 / 3),
					(int) (pLeft.y + (pRight.y - pLeft.y) * 2 / 3));
		}

		public double length(Point p1, Point p2) {
			return this.length = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
		}

		// https://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees

		/**
		 * Calculates the angle from centerPt to targetPt in degrees. The return
		 * should range from [0,360), rotating CLOCKWISE, 0 and 360 degrees
		 * represents NORTH, 90 degrees represents EAST, etc...
		 *
		 * Assumes all points are in the same coordinate space. If they are not,
		 * you will need to call SwingUtilities.convertPointToScreen or
		 * equivalent on all arguments before passing them to this function.
		 *
		 * @param centerPt
		 *            Point we are rotating around.
		 * @param targetPt
		 *            Point we want to calcuate the angle to.
		 * @return angle in degrees. This is the angle from centerPt to
		 *         targetPt.
		 */
		protected double angle(Point centerPt, Point targetPt) {
			// calculate the angle theta from the deltaY and deltaX values
			// (atan2 returns radians values from [-PI,PI])
			// 0 currently points EAST.
			// NOTE: By preserving Y and X param order to atan2, we are
			// expecting
			// a CLOCKWISE angle direction.
			double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

			// rotate the theta angle clockwise by 90 degrees
			// (this makes 0 point NORTH)
			// NOTE: adding to an angle rotates it clockwise.
			// subtracting would rotate it counter-clockwise
			theta += Math.PI / 2.0;

			// convert from radians to degrees
			// this will give you an angle from [0->270],[-180,0]
			double angle = Math.toDegrees(theta);

			// convert to positive range [0-360)
			// since we want to prevent negative angles, adjust them now.
			// we can assume that atan2 will not return a negative value
			// greater than one partial rotation
			if (angle < 0) {
				angle += 360;
			}

			return angle;
		}

	}

	class ComplexNumber {
		final double real; // the real part
		final double imaginary; // the imaginary part

		// return the real or imaginary part
		public double real() {
			return real;
		}

		public double imaginary() {
			return imaginary;
		}

		/**
		 * @param r
		 *            - real part
		 * @param i
		 *            - imaginary part
		 */
		public ComplexNumber(double r, double i) {
			super();
			this.real = r;
			this.imaginary = i;
		}

		// return a new Complex object whose value is (this * b)
		public ComplexNumber times(ComplexNumber b) {
			ComplexNumber a = this;
			double real = a.real * b.real - a.imaginary * b.imaginary;
			double imag = a.real * b.imaginary + a.imaginary * b.real;
			return new ComplexNumber(real, imag);
		}

		// return a new Complex object whose value is (this + b)
		public ComplexNumber plus(ComplexNumber b) {
			ComplexNumber a = this; // invoking object
			double real = a.real + b.real;
			double imag = a.imaginary + b.imaginary;
			return new ComplexNumber(real, imag);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			if (imaginary == 0)
				return real + "";
			if (real == 0)
				return imaginary + "i";
			if (imaginary < 0)
				return real + " - " + (-imaginary) + "i";
			return real + " + " + imaginary + "i";
			// return "ComplexNumber [real=" + real + ", imaginary=" + imaginary
			// + "]";
		}

		// See Section 3.3.
		public boolean equals(Object x) {
			if (x == null)
				return false;
			if (this.getClass() != x.getClass())
				return false;
			ComplexNumber that = (ComplexNumber) x;
			return (this.real == that.real) && (this.imaginary == that.imaginary);
		}

		// See Section 3.3.
		public int hashCode() {
			return Objects.hash(real, imaginary);
		}

		public double abs() {
			return Math.hypot(real, imaginary);
		}

		// return a new Complex object whose value is the complex exponential of
		// this
		public ComplexNumber exp() {
			return new ComplexNumber(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
		}

		// return a new Complex object whose value is the complex sine of this
		public ComplexNumber sin() {
			return new ComplexNumber(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
		}

		// return a new Complex object whose value is the complex cosine of this
		public ComplexNumber cos() {
			return new ComplexNumber(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
		}

		// return a new Complex object whose value is the complex tangent of
		// this
		public ComplexNumber tan() {
			return sin().divides(cos());
		}

		// return a / b
		public ComplexNumber divides(ComplexNumber b) {
			ComplexNumber a = this;
			return a.times(b.reciprocal());
		}

		// return a new Complex object whose value is the reciprocal of this
		public ComplexNumber reciprocal() {
			double scale = real * real + imaginary * imaginary;
			return new ComplexNumber(real / scale, -imaginary / scale);
		}

	}

}
