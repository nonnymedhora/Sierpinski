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

	private int mag;
	private int exp;
	private boolean useDiff=false;
	private int size;		//0-599 4 ltr

	public Mandelbrot() {
		super();
		this.mag = 2;
		this.exp=2;
	}

	/**
	 * @param mg	--	magnification
	 * mg=1 is high, mg=10 = low
	 */
	public Mandelbrot(int mg) {
		super();
		this.mag = mg;
		this.exp=2;
	}

	/**
	 * @param mg
	 * @param ep
	 */
	public Mandelbrot(int mg, int ep) {
		super();
		this.mag = mg;
		this.exp = ep;
	}

	/**
	 * @param mg
	 * @param ep
	 * @param dif
	 */
	public Mandelbrot(int mg, int ep, boolean dif) {
		super();
		this.mag = mg;
		this.exp = ep;
		this.useDiff = dif;
	}

	private static final long serialVersionUID = 13456L;

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		createMandelbrot(g, this.useDiff);
	}

	private void createMandelbrot(Graphics2D g, boolean diff) {		
		double xc = -0.5;
		double yc = 0;
		double size = this.mag;//10;//4;//2;

		int n = 599;//512;	(0-599)
		//System.out.println("here with depth " + depth);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double x0 = xc - size / 2 + size * i / n;
				double y0 = yc - size / 2 + size * j / n;
				ComplexNumber z0 = new ComplexNumber(x0, y0);
				int gray;
				// int gray = /*maxIter - */mand(z0, maxIter);
				if (diff) {
					gray = mand(z0, maxIter, exp);
				} else {
					gray = maxIter - mand(z0, maxIter, exp);
				}
				Color color;
				/* Simple === renders in black-white-gray */
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
	
	private int mand(ComplexNumber z0, int maxIterations, int power) {
		ComplexNumber z = z0;
		for (int t = 0; t < maxIterations; t++) {
			if (z.abs() > 2.0)
				return t;
			z = z.power(power).plus(z0);
//			z = z.power(3).plus(z0);
//			z = z.power(2).plus(z0);
		}
		return maxIterations;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new Mandelbrot(2,3,false);
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
