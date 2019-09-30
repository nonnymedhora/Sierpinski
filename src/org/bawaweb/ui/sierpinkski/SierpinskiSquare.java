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
public class SierpinskiSquare extends FractalBase {

	private static final long 	serialVersionUID 	= 123456543L;

	public SierpinskiSquare() {
		super();
	}

	private Image createSierpinskiSquares(Graphics2D g, int d) {
		// Clear the frame
		g.setColor(Color.black);
		if (depth==0) {
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		// Initialize p1, p2, p3, p4 based on frame size */
		Point p1 = new Point(OFFSET, OFFSET);
		Point p2 = new Point(getWidth() - OFFSET, OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);
		Point p4 = new Point(OFFSET, getHeight() - OFFSET);
		
//		System.out.println("depth== "+depth);
		
		// Draw Sierpinski's squares
		fillSquares(g, d, p1, p2, p3, p4);
		
		return bufferedImage;
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
		fillSquares(g, dep - 1, p122, p2, p233, p22); 		// top-right

		fillSquares(g, dep - 1, p134, p11, p44, p124); 		// mid-left
		fillSquares(g, dep - 1, p22, p233, p223, p33); 		// mid-right

		fillSquares(g, dep - 1, p124, p44, p334, p4); 		// bot-left
		fillSquares(g, dep - 1, p44, p33, p324, p334); 		// bot-mid
		fillSquares(g, dep - 1, p33, p223, p3, p324); 		// bot-right
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new SierpinskiSquare();
				frame.setTitle(frame.getFractalShapeTitle());
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
		createSierpinskiSquares(g, depth);		
	}

	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz _ Sierpinski'Square";
	}


	/*@Override
	public String getFractalDetails() {		
		return this.getFractalShapeTitle();
	}*/

}
