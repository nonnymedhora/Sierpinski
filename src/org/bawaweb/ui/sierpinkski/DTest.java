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
public class DTest extends FractalBase {

	private static final long serialVersionUID = -6849330178681033345L;

	public DTest() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		Point p1d = new Point(OFFSET, OFFSET);
		Point p2d = new Point(getWidth()-OFFSET, OFFSET);
		Point p3d = new Point(getWidth() /2, getHeight()-OFFSET);
		drawTriangles(g,depth,p1d,p2d,p3d);
	}

	private void drawTriangles(Graphics2D g, int d, Point p1, Point p2, Point p3) {
		g.setColor(Color.red);
		if (d == 0) { // depth is 0, draw the triangle
			g.setStroke(new BasicStroke(1));//((int)3/(d+1)));
			drawLine(g, p1, p2);
			drawLine(g, p2, p3);
			drawLine(g, p3, p1);
			return;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				FractalBase frame = new DTest();
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(frame.WIDTH, frame.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
	}
	

	/*@Override
	public String getFractalDetails() {		
		return this.getFractalShapeTitle();
	}*/

	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz _ DTest";
	}

}
