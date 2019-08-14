/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


/**
 * @author Navroz
 *	Displays the popular Mandelbrot Set
 *	see
 *	https://en.wikipedia.org/wiki/Mandelbrot_set
 */
public class Mandelbrot extends FractalBase {

	public Mandelbrot() {
		super();
	}

	private static final long serialVersionUID = 13456L;

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		createMandelbrot(g, depth);
	}

	private void createMandelbrot(Graphics2D g, int d) {		
		double xc = -0.5;
		double yc = 0;
		double size = 2;

		int n = 512;
		//System.out.println("here with depth " + depth);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double x0 = xc - size / 2 + size * i / n;
				double y0 = yc - size / 2 + size * j / n;
				ComplexNumber z0 = new ComplexNumber(x0, y0);
				int gray = maxIter - mand(z0, maxIter);
				Color color;
				/* Simple ===	renders in black-white-gray*/
				/* color = new Color(gray, gray, gray); */
				/* Complex === renders in Color */ 
				if (gray % 2 == 0) {
					color = new Color(gray, gray, gray);
				} else if (gray % 7 == 0) {
					final int start = 128;
					int num1 = correctColor(start, gray, i, 7);
					int num2 = correctColor(start, gray, j, 7);
					color = new Color(num1, num2, start);
				} else {
					final int start = 255;
					int num3 = correctColor(start, gray,i,1);
					int num4 = correctColor(start, gray,j,1);
					color = new Color(num3, num4, start);
				}
				setPixel(i, n - 1 - j, color.getRGB());
				
			}
		}
	}
	
	private int mand(ComplexNumber z0, int maxIterations) {
		ComplexNumber z = z0;
		for (int t = 0; t < maxIterations; t++) {
			if (z.abs() > 2.0)
				return t;
			z = z.power(2).plus(z0);
		}
		return maxIterations;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new Mandelbrot();
				frame.depth = 5;
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(frame.WIDTH, frame.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

			}
		});
	}

	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz _ Mandelbrot";
	}

}
