/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.bawaweb.ui.sierpinkski.FractalBase.ComplexNumber;


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
	
	private boolean isComplexNumConst = false;
	private ComplexNumber complex;// = new ComplexNumber(-0.75, 0.11);
	private boolean isConstFuncApplied=false;
	
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
//		System.out.println("in__Mandelbrot__createMandelbrot (isComplexNumConst || this.complex == null) = "+(isComplexNumConst || this.complex == null));		

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
		ComplexNumber cConst;

		if (this.isComplexNumConst || this.complex == null) {
			cConst = z0;
			this.complex = z0;
		} else {
			cConst = this.complex;
		}

		if (!this.isConstFuncApplied) {
			switch (func2Apply) {
			case "Sine":
				cConst = cConst.sine(); //z0.sin();
				break;
			case "Cosine":
				cConst = cConst.cosine(); //z0.cos();
				break;
			case "Tan":
				cConst = cConst.tangent(); //z0.tan();
				break;
			case "ArcSine":
				cConst = cConst.inverseSine(); //z0.sin();
				break;
			case "ArcCosine":
				cConst = cConst.inverseCosine(); //z0.cos();
				break;
			case "ArcTan":
				cConst = cConst.inverseTangent(); //z0.tan();
				break;
			case "Square":
				cConst = cConst.power(2); //z0.sin();
				break;
			case "Cube":
				cConst = cConst.power(3); //z0.cos();
				break;
			case "Exponent":
				cConst = cConst.exp(); //z0.tan();
				break;
			case "Root":
				cConst = cConst.sqroot(); //z0.sin();
				break;
			case "CubeRoot":
				cConst = cConst.curoot(); //z0.cos();
				break;
			case "Log":
				cConst = cConst.ln(); //z0.tan();
				break;

			case "None":
				cConst = cConst;
				break;
			default:
				this.complex = cConst;
				break;
			}//ends-switch

			if (!this.isComplexNumConst) {
				this.complex = cConst;
				this.isConstFuncApplied = true;
			}
		}
		
		return cConst;
		////////////////////////////
//		
//		if (isComplexNumConst && this.complex == null) {
//			this.complex = z0;
//		}
//		
//		
//		if (!this.isConstFuncApplied) {
//			switch (func2Apply) {
//				case "Sine":
//					this.complex = /*z0*/this.complex.sine(); //z0.sin();
//					break;
//				case "Cosine":
//					this.complex = /*z0*/this.complex.cosine(); //z0.cos();
//					break;
//				case "Tan":
//					this.complex = /*z0*/this.complex.tangent(); //z0.tan();
//					break;
//				case "ArcSine":
//					this.complex = /*z0*/this.complex.inverseSine(); //z0.sin();
//					break;
//				case "ArcCosine":
//					this.complex = /*z0*/this.complex.inverseCosine(); //z0.cos();
//					break;
//				case "ArcTan":
//					this.complex = /*z0*/this.complex.inverseTangent(); //z0.tan();
//					break;
//				case "Square":
//					this.complex = /*z0*/this.complex.power(2); //z0.sin();
//					break;
//				case "Cube":
//					this.complex = /*z0*/this.complex.power(3); //z0.cos();
//					break;
//				case "Exponent":
//					this.complex = /*z0*/this.complex.exp(); //z0.tan();
//					break;
//				case "Root":
//					this.complex = /*z0*/this.complex.sqroot(); //z0.sin();
//					break;
//				case "CubeRoot":
//					this.complex = /*z0*/this.complex.curoot(); //z0.cos();
//					break;
//				case "Log":
//					this.complex = /*z0*/this.complex.ln(); //z0.tan();
//					break;
//				case "None":
//					this.complex = /*z0*/this.complex;
//					break;
//				default:
//					this.complex = /*z0*/this.complex;
//					break;
//			}
//			
//			if(!this.isComplexNumConst){
//				this.complex=cConst;
//				this.isConstFuncApplied=true;
//			}
//		}
//		return this.complex;
		/////////////////////////////////////////
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
