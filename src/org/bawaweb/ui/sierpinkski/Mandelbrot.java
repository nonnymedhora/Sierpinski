/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;

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
	private boolean useDiff = false;
//	private int size;		//0-599 4 ltr
	
	private boolean isComplexNumConst;
	private ComplexNumber compConst;// = new ComplexNumber(-0.75, 0.11);
	
	private double bound=2.0;

	public Mandelbrot() {
		super();
		this.mag = 2;
		this.exp = 2;
	}

	/**
	 * @param mg	--	magnification
	 * mg=1 is high, mg=10 = low
	 */
	public Mandelbrot(int mg) {
		super();
		this.mag = mg;
		this.exp = 2;
		this.isComplexNumConst=true;
		this.compConst=null;
	}

	/**
	 * @param mg
	 * @param ep
	 */
	public Mandelbrot(int mg, int ep) {
		this(mg);
		this.exp = ep;
	}

	/**
	 * @param mg
	 * @param ep
	 * @param dif
	 */
	public Mandelbrot(int mg, int ep, boolean dif) {
		this(mg, ep);
		this.useDiff = dif;
	}

	/**
	 * @param mag
	 * @param exp
	 * @param useDiff
	 * @param complexConst
	 */
	public Mandelbrot(int mg, int ep, boolean useD, ComplexNumber complexConst) {
		this(mg, ep, useD);
		this.compConst = complexConst;
		this.isComplexNumConst = false;
	}
	
	public Mandelbrot(int mg, int ep, boolean useD, double real, double img) {
		this(mg,ep,useD);
//		System.out.println(" Mandelbrot(int mg("+mg+"), int ep("+ep+"), boolean useD, double real("+real+"), double img("+img+"))");
		this.compConst = new ComplexNumber(real, img);
		this.isComplexNumConst = false;
	}

	public Mandelbrot(int mg, int ep, boolean useD, boolean complexNumIsConst) {
		this(mg, ep, useD);
		this.isComplexNumConst = complexNumIsConst;
	}

	public Mandelbrot(int mg, int ep, boolean useD, double bd, boolean complexNumIsConst) {
		this(mg,ep,useD,complexNumIsConst);
		this.setBound(bd);
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
//		System.out.println("getXC--- "+getxC());		
//		System.out.println("getYC--- "+getyC());
		
		double xc = getxC();//-0.5;//getxC();//-0.5;
		double yc = getyC();//0;//getyC();//0;
		double size = this.mag;//getScaleSize();//this.mag;	//10;//4;//2;
		double bd = this.getBound();
		int max = getMaxIter();

		int n = getAreaSize();//599;//512;	(0-599)
		//System.out.println("here with depth " + depth);
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				ComplexNumber z0 = new ComplexNumber(x0, y0);
				if (isComplexNumConst || this.compConst == null) {
					this.compConst = z0;
				}
				int colorRGB;
				// int gray = /*maxIter - */mand(z0, maxIter);
				if (diff) {
					colorRGB = mand(z0, max, exp, this.compConst, bd);
				} else {
					colorRGB = max - mand(z0, max, exp, this.compConst,bd);
				}
				Color color = this.getPixelDisplayColor(row, col, colorRGB, diff);
				setPixel(row, n - 1 - col, color.getRGB());

			}
		}
	}

	
	private int mand(ComplexNumber z0, int maxIterations, int power, ComplexNumber constant, double bd) {
		ComplexNumber z = z0;
		for (int t = 0; t < maxIterations; t++) {
			if (z.abs() > bd)
				return t;
			z = z.power(power).plus(constant);
		}
		return maxIterations;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new Mandelbrot(2,2);//96);//,2);//,true);//(2,3,false);
//				frame.depth = 5;
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
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

	public double getBound() {
		return this.bound;
	}

	public void setBound(double bod) {
		this.bound = bod;
	}

}
