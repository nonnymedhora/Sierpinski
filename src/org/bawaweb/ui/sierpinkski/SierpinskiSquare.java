/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class SierpinskiSquare extends JFrame implements Runnable {

	private static final long 	serialVersionUID 	= 123456543L;
	private static final int 	HEIGHT 				= 600;
	private static final int 	WIDTH 				= 600;

	public static int OFFSET = 25; 		// pixel offset from edge

	private static int depth; 			// recursion depth
	private static final int MAX_DEPTH = 10;

	public SierpinskiSquare() {
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	}
	
	@Override 
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		// Clear the frame
		g.setColor(Color.black);
		if (depth==0) {
			g.fillRect(0, 0, getWidth(), getHeight());
			//		g.fillRect(OFFSET,OFFSET,getWidth() - OFFSET, getHeight() - OFFSET);
			//		g.setColor(Color.blue);
		}
		// Initialize p1, p2, p3, p4 based on frame size */
		Point p1 = new Point(OFFSET, OFFSET);
		Point p2 = new Point(getWidth() - OFFSET, OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);
		Point p4 = new Point(OFFSET, getHeight() - OFFSET);
		
		System.out.println("depth== "+depth);
		/*System.out.println("p1	(" + p1.x+", "+p1.y+")");
		System.out.println("p2	(" + p2.x+", "+p2.y+")");
		System.out.println("p3	(" + p3.x+", "+p3.y+")");
		System.out.println("p4	(" + p4.x+", "+p4.y+")");
		*/
//		p1	(25, 25)
//		p2	(575, 25)
//		p3	(575, 575)
//		p4	(25, 575)

		// Draw Sierpinski's triangle
		fillSquares(g, depth, p1, p2, p3, p4);
	}
	
	
	/*

	 p1 ------------- p2
    |		          |
    |		          |
    |		          |
    |		          |
    |		          |
	p4 --------------- p3
	
	 */

	private void fillSquares(Graphics2D g, int dep, Point p1, Point p2, Point p3, Point p4) {
//		assert(dep<10);
//		System.out.println("dep is -- " + dep);
		
		if (dep % 2 == 0) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.blue);
		}
		if (dep == 0) {
			g.setColor(Color.yellow);
			g.fillRect(p1.x, p1.y, (p3.x - p1.x), (p3.y - p1.y));
			return;
		}

		Point p132 = thirdPtH(p1, p2);				//	one-third horizontally b/w p1 & p2
		Point p122 = twoThirdPtH(p1, p2);			// 	two-thirds horizontally b/w p1 & p2
		Point p334 = thirdPtH(p4, p3);				//	one-third horizontally b/w p4 & p3	
		Point p324 = twoThirdPtH(p4, p3);			//	two-thirds horizontally b/w p4 & p3

		Point p233 = thirdPtV(p2, p3);				//	one-third vertically b/w p2 & p3
		Point p223 = twoThirdPtV(p2, p3);			// 	two-thirds vertically b/w p2 & p3

		Point p134 = thirdPtV(p1, p4);				//	one-third vertically b/w p1 & p4
		Point p124 = twoThirdPtV(p1, p4);			//	two-thirds vertically b/w p1 & p4

		Point p11 = mergeXYPt(p132, p233);			// 	merged pt (p132.X, p233.Y)
		Point p22 = mergeXYPt(p122, p233);			//	merged pt (p122.X, p233.Y)
		Point p33 = mergeXYPt(p122, p223);			//	merged pt (p122.X, p223.Y)		
		Point p44 = mergeXYPt(p132, p223);			// 	merged pt (p132.X, p223.Y)

		g.setStroke(new BasicStroke((int)(3/(dep+1))));
		g.fillRect(p132.x, p233.y, (p122.x - p132.x), (p223.y - p233.y));

		fillSquares(g, dep - 1, p1, p132, p11, p134); 		// top-left
		fillSquares(g, dep - 1, p132, p122, p22, p11); 		// top-mid
		fillSquares(g, dep - 1, p122, p2, p233, p11); 		// top-right

		fillSquares(g, dep - 1, p134, p11, p44, p124); 		// mid-left
		fillSquares(g, dep - 1, p22, p233, p223, p33); 		// mid-right

		fillSquares(g, dep - 1, p124, p44, p334, p4); 		// bot-left
		fillSquares(g, dep - 1, p44, p33, p324, p334); 		// bot-mid
		fillSquares(g, dep - 1, p33, p223, p3, p324); 		// bot-right	
		
		
//		fillSquares(g, dep - 1, p11, p22, p33, p44);		//	//mid-big
		
		
		/*
		
		
		
		
//		Path2D myPath = new Path2D.Double();
//		myPath.moveTo(p1.x, p1.y);
//		myPath.lineTo(p2.x, p2.y);
//		myPath.lineTo(p3.x, p3.y);
//		myPath.lineTo(p4.x, p4.y);
//		myPath.closePath();
//		
////		g.setPaint(Color.red);
//		/*==Paint GRADIENT_PAINT = new GradientPaint(0, 0, Color.BLUE, 20, 20, Color.RED, true);
//		
//		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g.setPaint(GRADIENT_PAINT);===/
//		g.fill(myPath);
		
		
		
		
		
		
		*/
		
		
	}

	private static void drawSquares(Graphics g, int d, Point p1, Point p2, Point p3, Point p4) {
		if (d == 0) { // depth is 0, draw the square
			drawLine(g, p1, p2);
			drawLine(g, p2, p3);
			drawLine(g, p3, p4);
			drawLine(g, p4, p1);
			return;
		}
		// Draw three Sierpinski squares of depth d-1
		Point m12 = midpoint(p1, p2);
		Point m23 = midpoint(p2, p3);
		Point m34 = midpoint(p3, p4);
		Point m41 = midpoint(p4, p1);

		Point p132 = thirdPt(p1, p2);
		Point p122 = twoThirdPt(p1, p2);
		Point p13d2 = new Point(p132.x,p132.y+(p132.x-p1.x));
		Point p12d2 = new Point(p122.x,p122.y+(p122.x-p1.x));
		
		drawSquares(g,d-1,p132,p122,p12d2,p13d2);
		
		Point p233 = thirdPt(p2,p3);
		Point p223 = twoThirdPt(p2,p3);
		Point p23l3 = new Point(p233.x,p233.y-(p233.x-p2.x));
		Point p22l3 = new Point(p223.x,p223.y-(p223.x-p2.x));
		
		drawSquares(g,d-1,p233,p223,p22l3,p23l3);
		
		
		
		

		// drawSquares(g, d - 1, p1, m12, m34);
		// drawSquares(g, d - 1, m12, p2,m23);
		// drawSquares(g, d - 1, m34, m23, p3);
	}

	private static void drawLine(Graphics g, Point p1, Point p2) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	/** = the midpoint between p1 and p2 */
	private static Point midpoint(Point p1, Point p2) {
		return new Point((int)(p1.x + p2.x) / 2, (int)(p1.y + p2.y) / 2);
	}

	private static Point thirdPt(Point p1, Point p2) {
		return new Point((int)(p1.x + p2.x) / 3, (int)(p1.y + p2.y) / 3);
	}
	
	private static Point thirdPtH(Point pLeft, Point pRight){
		return new Point((int)(pLeft.x+(pRight.x-pLeft.x)/3),pLeft.y);
	}
	
	private static Point thirdPtV(Point pTop, Point pBottom){
		return new Point(pTop.x,(int)(pTop.y+(pBottom.y-pTop.y)/3));
	}

	private static Point twoThirdPt(Point p1, Point p2) {
		return new Point((int)(p1.x + p2.x) * 2 / 3, (int)(p1.y + p2.y) * 2 / 3);
	}
	
	private static Point twoThirdPtH(Point pLeft, Point pRight){
		return new Point((int)(pLeft.x+(pRight.x-pLeft.x)*2/3),pLeft.y);
	}
	
	private static Point twoThirdPtV(Point pTop, Point pBottom){
		return new Point(pTop.x,(int)(pTop.y+(pBottom.y-pTop.y)*2/3));
	}
	
	
	private static Point mergeXYPt(Point p1, Point p2){
		return new Point(p1.x,p2.y);
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
				depth += 1;
				Thread.sleep(3000);
				Sierpinski(depth);
				run();
			}
		} catch (InterruptedException ex) {
		}
	}

	public void Sierpinski(int d) {
		depth = d;
		repaint();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final SierpinskiSquare frame = new SierpinskiSquare();
				frame.setTitle("Bawaz _ Sierpinski'Square");
				frame.setSize(WIDTH, HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				new Thread(frame).start();

			}
		});
	}

}
