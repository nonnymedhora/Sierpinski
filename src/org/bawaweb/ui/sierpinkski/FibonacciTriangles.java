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
public class FibonacciTriangles extends FractalBase {

	private static final long serialVersionUID = 186567L;
	
	public FibonacciTriangles() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		drawFibonacciTriangles(g, depth, center);
	}
	
	private void drawFibonacciTriangles(Graphics2D g, int d, Point center) {
		int side = getNthFibonacci(d + 2);
		System.out.println("depth is == " + depth + " and 'd' is " + d + " and side is " + side);
		if (d <= 2) {
			side = getNthFibonacci(d + 2);
			fillEquilateralTriangle(g, center, side, "up", Color.green);
			// for border / outline
			g.setStroke(new BasicStroke(2));
			drawEquilateralTriangle(g, center, side, "up", Color.blue);
			return;
		}

		Point pc = new Point(center.x + side, center.y);
		Point qc = new Point(center.x, center.y + side);

		fillEquilateralTriangle(g, pc, side, "up", Color.yellow);
		g.setStroke(new BasicStroke(2));
		drawEquilateralTriangle(g, pc, side, "up", Color.red);

		fillEquilateralTriangle(g, qc, side, "down", Color.pink);
		g.setStroke(new BasicStroke(2));
		drawEquilateralTriangle(g, qc, side, "down", Color.red);

		drawFibonacciTriangles(g, d - 1, pc);
		drawFibonacciTriangles(g, d - 1, qc);
	}

	private int n1 = 0, n2 = 1, n3 = 0;
	
	private int getNthFibonacci(int n) {System.out.println("in getFibo - depth is "+depth+" and input n is "+n);
		if (n == 0 || n == 1 || n == 2) {
			return 1;
		} else {
			int count = 2;

			while (count < n) {
				n3 = n1 + n2;
				n1 = n2;
				n2 = n3;

				count += 1;
			}
			System.out.println("Returning n3 == "+n3+" count is "+count+" and input n is "+n);
			return n3;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*FibonacciTriangles ff = new FibonacciTriangles();
		System.out.println(ff.getNthFibonacci(10));*/
		
		final FractalBase ff = new FibonacciTriangles();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = ff;
				frame.setTitle("Bawaz _ FibonacciTriangles");
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
	
				new Thread(frame).start();
	
			}
		});
	}

}
