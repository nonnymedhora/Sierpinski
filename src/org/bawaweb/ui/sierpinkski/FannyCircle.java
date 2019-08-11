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
public class FannyCircle extends FractalBase {
	//	NOTE		--	change variables for different designs
	//	length 		--	(50 -to- 350)	* depth + 1
	//	cutRadius	--	radius / (2 to 5)
	// 	inverse relationship	for visibility
	private static final long serialVersionUID = 1657483L;

	public FannyCircle() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		final int length = 50 * depth + 1;	// ranging 50-350
//System.out.println("in createFractalShape -  depth is "+depth+" and length is "+length);
		drawFannyCircle(g, depth, center.x, center.y, length);

	}

	private void drawFannyCircle(Graphics2D g, int d, int px, int py, int radius) {		
//		System.out.println("in drawFanny - depth is "+depth+" and 'd' is "+d+" and radius is "+radius);
		if (d == 0) {
			double randomBit = (int) (Math.random() * 2); // equally likely 0 or 1
			if (randomBit == 0)
				g.setColor(Color.green);
			else
				g.setColor(Color.yellow);

			fillCircle(g, new Point(px, py), radius * 2);

			g.setStroke(new BasicStroke(2));
			g.setColor(Color.blue);
			drawCircle(g, new Point(px, py), radius * 2);
			return;
		}

		int cutRadius = radius / 2;		// diff designs for 2-5, but needs higher radii

		drawFannyCircle(g, d - 1, px - cutRadius, py, cutRadius);
		drawFannyCircle(g, d - 1, px, py - cutRadius, cutRadius);	

		drawFannyCircle(g, d - 1, px + cutRadius, py, cutRadius);
		drawFannyCircle(g, d - 1, px, py + cutRadius, cutRadius);

		drawFannyCircle(g, d - 1, px, py, cutRadius);	//extraCenter
	}

	public static void main(String[] args) {
		final FractalBase ff = new FannyCircle();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = ff;
				frame.setTitle("Bawaz _ FannyCircle");
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
	
				new Thread(frame).start();
	
			}
		});
	}

}
