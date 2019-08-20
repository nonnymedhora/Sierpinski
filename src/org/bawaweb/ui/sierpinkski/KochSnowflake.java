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
public class KochSnowflake extends JFrame implements Runnable {

	private static final long serialVersionUID = 12876543L;
	private static final int HEIGHT = 600;
	private static final int WIDTH = 600;

	public static int OFFSET = 25; // pixel offset from edge

	private static int depth; // recursion depth
	private static final int MAX_DEPTH = 10;
	
	public KochSnowflake(){
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	}
	
	@Override
	public void paint(Graphics g1) {
		Image img = createKochSnowflake();
		Graphics2D g = (Graphics2D) g1;
		g.drawImage(img, null, null);
		g.dispose();
	}

	/* triangle of depth d with perimeter given by p1, p2, p3.
	 *  //p1-p2 is the base line, p3 the top point
	 *  /////nope
	 *  //p2-3 is the base line, p1 the top point
	 *  
	 * 				   p1
	 * 				   / \
	 * 				  /	  \
	 * 				 /	   \
	 * 				/		\
	 * 			   /		 \
	 * 			   p2________p3
	 * 
	 * 	P1(300,25)
		P2(25,575)
		P3(575,575)
	 * */
	private Image createKochSnowflake() {
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
		
		//TOREMOVELTR
		/*System.out.println("P1("+p1.x+","+p1.y+")");
		System.out.println("P2("+p2.x+","+p2.y+")");
		System.out.println("P3("+p3.x+","+p3.y+")");
		
		System.out.println("Angle p1-p2  "+angle(p1,p2));		
		System.out.println("Angle p2-p3  "+angle(p2,p3));		
		System.out.println("Angle p1-p3  "+angle(p1,p3));*/
		
		drawKochSnowflakes(g, depth, p1, p2, p3);
		return bufferedImage;
	}
	


	private static void drawKochSnowflakes(Graphics2D g, int d, Point p1, Point p2, Point p3) {
		g.setColor(Color.red);
		if (d == 0) { // depth is 0, draw the triangle
			g.setStroke(new BasicStroke(1));// ((int)3/(d+1)));
			drawLine(g, p1, p2);
			drawLine(g, p2, p3);
			drawLine(g, p3, p1);
			return;
		}

		Point p132 = getThirdPt/*thirdPt*/(p1, p2);
		Point p122 = getTwoThirdPt/*twoThirdPt*/(p1, p2);

		Point p233 = thirdPt(p2, p3);
		Point p223 = twoThirdPt(p2, p3);

		Point p133 = thirdPt(p1, p3);
		Point p123 = twoThirdPt(p1, p3);

		g.setStroke(new BasicStroke(2));
		g.setPaint(Color.yellow);

		drawLine(g, p132, p122);
		drawLine(g, p233, p223);
		drawLine(g, p133, p123);

		drawKochSnowflakes(g, d - 1, p132, p233, p133);
		drawKochSnowflakes(g, d - 1, p122, p223, p123);
	}
	
	
	private static Point getVertex(Point p1, Point p2){
		Point vertex = null;
		Point midpoint = midpoint(p1,p2);
		String slopeDir = getSlopeDir(p1,p2);
		return vertex ;
	}
	
	
	private static String getSlopeDir(Point p1, Point p2) {
		String slope = null;

		if (p1.x == p2.x) {
			slope = "VER";
		} else if (p1.y == p2.y) {
			slope = "HOR";
		} else if (p1.x > p2.x) {
			if (p1.y > p2.y) {
				slope = "RL_DU";
			} else {
				slope = "RL_UD";
			}
		} else {
			if (p1.y > p2.y) {
				slope = "LR_DU";
			} else {
				slope = "LR_UD";
			}
		}
		return slope;
	}
	
	private static double distance(Point p1, Point p2) {
		return Math.sqrt( Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) );
	}

	/** Draw a line between p1 and p2 on g. */
	private static void drawLine(Graphics2D g, Point p1, Point p2) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}

	/** = the midpoint between p1 and p2 */
	private static Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
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
	
	//https://people.cs.clemson.edu/~goddard/handouts/math360/notes11.pdf

	private static Point thirdPt(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 3), (int) (pLeft.y + (pRight.y - pLeft.y) / 3));
	}

	private static Point thirdPtH(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 3), pLeft.y);
	}

	private static Point thirdPtV(Point pTop, Point pBottom) {
		return new Point(pTop.x, (int) (pTop.y + (pBottom.y - pTop.y) / 3));
	}

	private static Point twoThirdPt(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) * 2 / 3), (int) (pLeft.y + (pRight.y - pLeft.y) * 2 / 3));
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

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (depth < 4/*MAX_DEPTH*/) {
				depth += 1;
				Thread.sleep(8000);
				kochSnowflake(depth);
				run();
			}
		} catch (InterruptedException ex) {
		}
	}
	
	private void kochSnowflake(int d) {
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
				final KochSnowflake frame = new KochSnowflake();
				frame.setTitle("Bawaz _ KochSnowFlake");
				frame.setSize(WIDTH, HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				new Thread(frame).start();

			}
		});
	}

}
