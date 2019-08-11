/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class FannyTriangles extends FractalBase {

	private static final long serialVersionUID = 1343L;

	public FannyTriangles() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		final Point center = new Point(WIDTH / 2, HEIGHT / 2);
		final int length = 150 * depth + 1;		//	manipulate variable 50-350
		drawFannyTriangles(g, depth, center, length);
	}

	private void drawFannyTriangles(Graphics2D g, int d, Point center, int side) {
		if (d == 0) {
			double randomBit = (int) (Math.random() * 2); // equally likely 0 or 1
			if (randomBit == 0)
				g.setColor(Color.green);
			else
				g.setColor(Color.yellow);
			
			// for fill
			fillEquilateralTriangle(g, center, side * 2, "up");
			fillEquilateralTriangle(g, center, side * 2, "down");

			// for border / outline
			g.setStroke(new BasicStroke(2));
			
			drawEquilateralTriangle(g, center, side * 2, "up",Color.blue);
			drawEquilateralTriangle(g, center, side * 2, "down",Color.blue);
			
			return;
		}
		
		
		int cutSide = side / 2;			//	manipulate variable 2-5
		
		drawFannyTriangles(g, d - 1, new Point(center.x + cutSide, center.y), cutSide);
		drawFannyTriangles(g, d - 1, new Point(center.x, center.y + cutSide), cutSide);

		drawFannyTriangles(g, d - 1, new Point(center.x - cutSide, center.y), cutSide);
		drawFannyTriangles(g, d - 1, new Point(center.x, center.y - cutSide), cutSide);
		
		drawFannyTriangles(g, d - 1, center, cutSide);		//	center
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final FractalBase ff = new FannyTriangles();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = ff;
				frame.setTitle("Bawaz _ FannyTriangles");
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
	
				new Thread(frame).start();
	
			}
		});
	}

}
