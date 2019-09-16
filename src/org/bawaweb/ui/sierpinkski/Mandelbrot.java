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
	private boolean useDiff = false;
//	private int size;		//0-599 4 ltr
	
	private boolean isComplexNumConst;
	private ComplexNumber complex;// = new ComplexNumber(-0.75, 0.11);
	
	public Mandelbrot() {
		super();
		this.mag = 2;
		this.power = 2;
	}

	/**
	 * @param mg	--	magnification
	 * mg=1 is high, mg=10 = low
	 */
	public Mandelbrot(int mg) {
		super();
		this.mag = mg;
		this.power = 2;
		this.isComplexNumConst=true;
		this.complex=null;
	}

	/**
	 * @param mg
	 * @param ep
	 */
	public Mandelbrot(int mg, int ep) {
		this(mg);
		this.power = ep;
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
		this.complex = complexConst;
		this.isComplexNumConst = false;
	}
	
	public Mandelbrot(int mg, int ep, boolean useD, double real, double img) {
		this(mg,ep,useD);
//		System.out.println(" Mandelbrot(int mg("+mg+"), int ep("+ep+"), boolean useD, double real("+real+"), double img("+img+"))");
		this.complex = new ComplexNumber(real, img);
		this.isComplexNumConst = false;
	}
	
	public Mandelbrot(int mg, int ep, boolean useD, double bd, double real, double img) {
		this(mg,ep,useD,real,img);
//		System.out.println(" Mandelbrot(int mg("+mg+"), int ep("+ep+"), boolean useD, double real("+real+"), double img("+img+"))");
		this.setBound(bd);
	}

	public Mandelbrot(int mg, int ep, boolean useD, boolean complexNumIsConst) {
		this(mg, ep, useD);
		this.isComplexNumConst = complexNumIsConst;
	}

	public Mandelbrot(int mg, int ep, boolean useD, double bd, boolean complexNumIsConst) {
		this(mg,ep,useD,complexNumIsConst);
		this.setBound(bd);
	}
/*
	public Mandelbrot(int mg, int ep, double bd, boolean diyMandUseD, boolean diyMKConst) {
		// TODO Auto-generated constructor stub
	}*/

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

		String func2Apply = this.useFuncConst;

		int n = getAreaSize();//599;//512;	(0-599)
		//System.out.println("here with depth " + depth);
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				ComplexNumber z0 = new ComplexNumber(x0, y0);
				z0 = this.computeComplexConstant(func2Apply, z0);

				int colorRGB;

				if (diff) {
					colorRGB = mand(z0, max, this.power, this.complex, bd);
				} else {
					colorRGB = max - mand(z0, max, this.power, this.complex,bd);
				}
				Color color = this.getPixelDisplayColor(row, col, colorRGB, diff);

				setPixel(row, n - 1 - col, color.getRGB());

			}
		}
	}

	private ComplexNumber computeComplexConstant(String func2Apply, ComplexNumber z0) {
		if (isComplexNumConst || this.complex == null) {
			this.complex = z0;

			switch (func2Apply) {
				case "Sine"	:
						this.complex = z0.sine();	//z0.sin();
						break;
				case "Cosine" :
						this.complex = z0.cosine();	//z0.cos();
						break;
				case "Tan" :
						this.complex = z0.tangent();	//z0.tan();
						break;
				case "ArcSine"	:
					this.complex = z0.inverseSine();	//z0.sin();
					break;
				case "ArcCosine" :
						this.complex = z0.inverseCosine();	//z0.cos();
						break;
				case "ArcTan" :
						this.complex = z0.inverseTangent();	//z0.tan();
						break;

						
				case "Square"	:
					this.complex = z0.power(2);	//z0.sin();
						break;
				case "Cube" :
					this.complex = z0.power(3);	//z0.cos();
						break;
				case "Exponent" :
					this.complex = z0.exp();	//z0.tan();
						break;
				case "Root"	:
					this.complex = z0.sqroot();	//z0.sin();
						break;
				case "CubeRoot" :
					this.complex = z0.curoot();	//z0.cos();
						break;
				case "Log" :
					this.complex = z0.ln();	//z0.tan();
						break;
						
						
				case "None" :
						this.complex = z0;
						break;
				default:
					this.complex = z0;
					break;
			}
		}

		return this.complex;
	}

	
	private int mand(ComplexNumber z0, int maxIterations, int pwr, ComplexNumber constant, double bd) {
		ComplexNumber z = z0;
		for (int t = 0; t < maxIterations; t++) {
			if (z.abs() > bd)
				return t;
			z = z.power(pwr).plus(constant);
		}
		return maxIterations;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new Mandelbrot(2,2,true);//96);//,2);//,true);//(2,3,false);
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

	public ComplexNumber getComplex() {
		return this.complex;
	}

	public void setComplex(ComplexNumber comp) {
		this.complex = comp;
	}

}
