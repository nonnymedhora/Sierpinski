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
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class ApollonionFractal extends JFrame implements Runnable {

	private static final long serialVersionUID = 1232366543L;
	private static final int HEIGHT = 700;
	private static final int WIDTH = 700;

	public static int OFFSET = 25; // pixel offset from edge

	private static int depth; // recursion depth
	private static final int MAX_DEPTH = 10;
	private boolean debug = false;

	public ApollonionFractal() {
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	}
	

	@Override
	public void paint(Graphics g1) {
		if (debug)
			System.out.println("paint---depth " + depth);
		Image img = createApollonionFractal();
		Graphics2D g = (Graphics2D) g1;
		g.drawImage(img, null, null);
		g.dispose();
	}

	private Image createApollonionFractal() {
		BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		// Clear the frame
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);
		// Initialize p1, p2, p3, p4 based on frame size */
		Point p1 = new Point(OFFSET, OFFSET);
		Point p2 = new Point(getWidth() - OFFSET, OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);
		if (debug)
			System.out.println("in createApollonionFractal depth == " + depth);
		drawApollonionFractal(g, depth, p1, p2, p3);
		return bufferedImage;

	}
	
	private void drawApollonionFractal(Graphics2D g, int dep, Point p1, Point p2, Point p3) {
		g.setColor(Color.white);
		if (debug) {
			System.out.println("[drawApollonionFractal] depth===" + depth+" and d== "+dep);
			System.out.println("P1(" + p1.x + "," + p1.y + ")");
			System.out.println("P2(" + p2.x + "," + p2.y + ")");
			System.out.println("P3(" + p3.x + "," + p3.y + ")");
		}
		if (dep == 0) {
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setStroke(new BasicStroke(2));
			Apollo app = new Apollo(p1, (int) distance(p1, p3) / 2);
			app.draw(g, true, Color.black, true);

			return;
		}

		g.setColor(Color.white);
		

		Point mp12 = midpoint(p1, p2);
		Point p132 = getThirdPt(p1, p2);
		Point p122 = getTwoThirdPt(p1, p2);

		Point mp23 = midpoint(p2, p3);
		Point p233 = getThirdPt(p2, p3);
		Point p223 = getTwoThirdPt(p2, p3);

		Point mp13 = midpoint(p1, p3);
		Point p133 = getThirdPt(p1, p3);
		Point p123 = getTwoThirdPt(p1, p3);
		
		Point mrg132223 = mergeXYPt(p132, p223);
		Point mrg133122 = mergeXYPt(p133, p122);

		int size1 = (int) distance(p132, p122);

		Apollo ap1 = new Apollo(p132, size1/3);
		Apollo ap2 = new Apollo(mrg132223, size1/2);
		Apollo ap3 = new Apollo(mrg133122, size1/5);

		
		drawApollonionFractal(g, dep - 1, p1, 			p132, 		mrg133122);
		drawApollonionFractal(g, dep - 1, p132, 		p2, 		mrg132223);
		drawApollonionFractal(g, dep - 1, mrg133122,	mrg132223, 	p3);
		
		ap1.draw(g, false, Color.blue,true);
		ap2.draw(g, false, Color.red,false);
		ap3.draw(g, false, Color.green,true);


	}


	private void ApollonionFractal(int d){
		depth=d;
		if (debug) {
			System.out.println("in generate-ApollonionFractal--- depth==" + depth);
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (depth < MAX_DEPTH) {
				if (debug) {
					System.out.println("depth is " + depth);
				}
				Thread.sleep(2000);
				depth += 1;
				Thread.sleep(2000);
				ApollonionFractal(depth);
				run();
			}
		} catch (InterruptedException ex) {
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final ApollonionFractal frame = new ApollonionFractal();
				frame.setTitle("Bawaz _  ApollonionFractal");
				frame.setSize(WIDTH, HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				new Thread(frame).start();

			}
		});
	}
	
	


	/** Draw a line between p1 and p2 on g. */
	private static void drawLine(Graphics2D g, Point p1, Point p2) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	/** = the midpoint between p1 and p2 */
	private static Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}
	

	private static Point thirdPtH(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 3), pLeft.y);
	}

	private static Point thirdPtV(Point pTop, Point pBottom) {
		return new Point(pTop.x, (int) (pTop.y + (pBottom.y - pTop.y) / 3));
	}

	private static Point twoThirdPtH(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) * 2 / 3), pLeft.y);
	}

	private static Point twoThirdPtV(Point pTop, Point pBottom) {
		return new Point(pTop.x, (int) (pTop.y + (pBottom.y - pTop.y) * 2 / 3));
	}

	private static Point mergeXYPt(Point p1, Point p2) {
		return new Point(p1.x, p2.y);
	}
	
	private static double distance(Point p1, Point p2) {
		return Math.sqrt( Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) );
	}

	//https://people.cs.clemson.edu/~goddard/handouts/math360/notes11.pdf
	private static Point getThirdPt(Point p1,Point p2){
		Point p3 = null;
		return p3 = new Point((int)((2*p1.x+p2.x)/3),(int)((2*p1.y+p2.y)/3)) ;
		
	}
	
	private static Point getTwoThirdPt(Point p1,Point p2){
		Point p3=null;
		return p3=new Point((int)((p1.x+2*p2.x)/3),(int)((p1.y+2*p2.y)/3));		
	}
	
	private static double angle(Point p1, Point p2){
		double angle = 0;
		return angle = Math.atan2(p2.y-p1.y,p2.x-p1.x);
		
	}
	
	
	
	// to represent a square circumscribing a circle
	class Apollo {
		Point start;
		int distance;

		public Apollo(Point p, int d) {
			start = p;
			distance = d;
		}

		public void draw(Graphics g, boolean drawSquare, Color color, boolean fillOval) {
			if (drawSquare) {
				g.setColor(Color.cyan);
				g.drawRect(start.x, start.y, distance, distance);
			}
			g.setColor(color);
			if (fillOval) {
				g.fillOval(start.x, start.y, distance, distance);
			}else{
				g.drawOval(start.x, start.y, distance, distance);
			}
		}

	}
}
