/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class ApollonianGasket extends JFrame implements Runnable{
	
	private static final long 	serialVersionUID 	= 126543L;
	private static final int 	HEIGHT 				= 600;
	private static final int 	WIDTH 				= 600;

	public static int OFFSET = 25; 		// pixel offset from edge

	private static int depth; 			// recursion depth
	private static final int MAX_DEPTH = 10;
	
	public ApollonianGasket (){
		setSize(WIDTH,HEIGHT);
		setVisible(true);
	}
	
	@Override 
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D)g1;
		// Clear the frame
		g.setColor(Color.black);
		if (depth==0) {
			g.fillOval(OFFSET, OFFSET, getWidth()-OFFSET, getHeight()-OFFSET);
			return;
		}
		
		Point p1 = new Point(OFFSET, OFFSET);
		Point p2 = new Point(getWidth() - OFFSET, OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);
		Point p4 = new Point(OFFSET, getHeight() - OFFSET);
		
		fillCircles(g,depth,p1,p2,p3,p4);
//		g.drawOval(OFFSET+10, OFFSET+10, getWidth()-OFFSET-40, getHeight()-OFFSET-40);
		
		
	}

	private void fillCircles(Graphics2D g, int dep, Point p1, Point p2, Point p3, Point p4) {
		if (dep == 0) {
			g.setColor(Color.blue);

			g.drawOval(p1.x, p1.y, p3.x-p1.x, p3.y-p1.y);
			return;
		}
		if (dep % 2 == 0) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.yellow);
		}
		
		
		Point m12 = midpoint(p1, p2);
		Point m23 = midpoint(p2, p3);
		Point m34 = midpoint(p3, p4);
		Point m41 = midpoint(p4, p1);
		
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
		
		g.fillOval(p132.x,p132.y,(int)((p3.x-p1.x)/3),(int)((p3.y-p1.y)/3));
		
		
		
		fillCircles(g, dep - 1, p1, p132, p11, p134); 		// top-left
		fillCircles(g, dep - 1, p132, p122, p22, p11); 		// top-mid
		fillCircles(g, dep - 1, p122, p2, p233, p11); 		// top-right

		fillCircles(g, dep - 1, p134, p11, p44, p124); 		// mid-left
		fillCircles(g, dep - 1, p22, p233, p223, p33); 		// mid-right

		fillCircles(g, dep - 1, p124, p44, p334, p4); 		// bot-left
		fillCircles(g, dep - 1, p44, p33, p324, p334); 		// bot-mid
		fillCircles(g, dep - 1, p33, p223, p3, p324); 		// bot-right
		
		fillCircles(g, dep - 1, p11, p22, p33, p44);		//	//mid-big
		
		
		
	}
	
	/** = the midpoint between p1 and p2 */
	private static Point midpoint(Point p1, Point p2) {
		return new Point((int)(p1.x + p2.x) / 2, (int)(p1.y + p2.y) / 2);
	}
	private static Point thirdPtH(Point pLeft, Point pRight){
		return new Point((int)(pLeft.x+(pRight.x-pLeft.x)/3),pLeft.y);
	}
	
	private static Point thirdPtV(Point pTop, Point pBottom){
		return new Point(pTop.x,(int)(pTop.y+(pBottom.y-pTop.y)/3));
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final ApollonianGasket frame = new ApollonianGasket();
				frame.setTitle("Bawaz _ ApollonianGasket");
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
			while (depth < MAX_DEPTH-4) {
				depth += 1;
				Thread.sleep(3000);
				apollonianGasket(depth);
				run();
			}
		} catch (InterruptedException ex) {
		}
	}
	
	public void apollonianGasket(int d) {
		depth = d;
		repaint();
	}

}
