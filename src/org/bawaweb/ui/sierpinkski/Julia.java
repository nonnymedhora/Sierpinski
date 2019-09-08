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
 * 
 * 			f(z) = z^2 + 0.279
			f(z) = z^3 + 0.400
 			f(z) = z^4 + 0.484
 			f(z) = z^5 + 0.544
			f(z) = z^6 + 0.590
			f(z) = z^7 + 0.626
			
			Field lines for an iteration of the form 
			((1-z^3)/6)/(z-z^2/2)^2)+c}
			
			c=-0.74543+0.11301*i
			c= -0.75+0.11*i
			c=-0.1+0.651*i
 *
 */
public class Julia extends FractalBase {

	private static final long serialVersionUID = 1987L;
	
	private double complexConst;		//	either this	
	private ComplexNumber complex; 		// or this
	private boolean useDiff = false;
	private boolean isComplexNumConst=false;
	private boolean useSineCalc = false;
	
	public final ComplexNumber C1 = new ComplexNumber(-0.74543,0.11301);	//c=-0.74543+0.11301*i
	public final ComplexNumber C2 = new ComplexNumber(-0.75,0.11);			//c= -0.75+0.11*i
	public final ComplexNumber C3 = new ComplexNumber(-0.1,0.651);			//c=-0.1+0.651*i
	
	
	public Julia() {
		super();
	}

	/**
	 * @param mul
	 * @param con
	 */
	public Julia(int mul, double con) {
		this();
		this.power = mul;
		this.complexConst = con;
	}
	
	

	/**
	 * @param mul
	 * @param con
	 * @param uDiff
	 */
	public Julia(int mul, double con, boolean uDiff) {
		this(mul,con);
		this.useDiff = uDiff;
	}

	/**
	 * @param mul
	 * @param comp
	 */
	public Julia(int mul, ComplexNumber comp) {
		this();
		this.power = mul;
		this.complex = comp;
	}
	
	

	/**
	 * @param mul
	 * @param comp
	 * @param uDiff
	 */
	public Julia(int mul, ComplexNumber comp, boolean uDiff) {
		this(mul, comp);
		this.useDiff = uDiff;
	}

	public Julia(int mul, String comp, boolean uDiff) {
		this(mul, comp);
		this.useDiff = uDiff;
	}

	public Julia(int mul, String comp, double bd, boolean uDiff) {
		this(mul, comp, uDiff);
		this.setBound(bd);
	}

	public Julia(int m, boolean uDiff, double realVal, double imagVal) {
		this.power = m;
		this.useDiff = uDiff;
		this.complex = new ComplexNumber(realVal, imagVal);
	}

	public Julia(int mul, String comp) {
		this();
		this.power = mul;
		switch (comp) {
		case "C1":
			this.complex = C1;
			break;
		case "C2":
			this.complex = C2;
			break;
		case "C3":
			this.complex = C3;
			break;
		default:
			this.complex = C1;
		}
	}

	public Julia(int m, double con, double bd, boolean uDiff) {
		this(m, con, uDiff);
		this.setBound(bd);
	}

	public Julia(int m, boolean uDiff, boolean keepConst) {
		this();
		this.power = m;
		this.useDiff = uDiff;
		this.isComplexNumConst = keepConst;
		if (keepConst) {
			this.complex = null;
		}
	}

	public Julia(int m, boolean uDiff, double bd, boolean keepConst) {
		this(m, uDiff, keepConst);
		this.setBound(bd);
	}
	
	/*public Julia(int m, boolean uDiff, double bd, boolean keepConst,boolean useSine) {
		this(m, uDiff, bd,keepConst);
		this.setUseSineCalc(useSine);
	}*/

	public Julia(int m, boolean uDiff, double bd, double realVal, double imgVal) {
		this(m, uDiff, realVal, imgVal);
		this.setBound(bd);
	}
	
	/*public Julia(int m, boolean uDiff, double bd, double realVal, double imgVal, boolean useSine) {
		this(m, uDiff, bd, realVal, imgVal);
		this.setUseSineCalc(useSine);
		if (useSine && this.complex != null) {
			this.complex = this.complex.sin();
		}
	}*/

	/**
	 * @return the power
	 */
	public int getPower() {
		return power;
	}

	/**
	 * @param power the power to set
	 */
	public void setPower(int power) {
		this.power = power;
	}

	/**
	 * @return the complexConst
	 */
	public double getComplexConst() {
		return complexConst;
	}

	/**
	 * @param complexConst the complexConst to set
	 */
	public void setComplexConst(double complexConst) {
		this.complexConst = complexConst;
	}

	/**
	 * @return the complex
	 */
	public ComplexNumber getComplex() {
		return complex;
	}

	/**
	 * @param complex the complex to set
	 */
	public void setComplex(ComplexNumber complex) {
		this.complex = complex;
	}

	/**
	 * @return the useDiff
	 */
	public boolean isUseDiff() {
		return useDiff;
	}

	/**
	 * @param useDiff the useDiff to set
	 */
	public void setUseDiff(boolean useDiff) {
		this.useDiff = useDiff;
	}

	public boolean isUseSineCalc() {
		return this.useSineCalc;
	}

	public void setUseSineCalc(boolean useSine) {
		this.useSineCalc = useSine;
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		createJulia(g, this.useDiff);
	}

	private void createJulia(Graphics2D g, boolean diff) {
		double xc = getxC();//0;
		double yc = getyC();//0;
		double size = getScaleSize();//this.power;//this.mag;//2;

		int n = getAreaSize();//512;
		
		int max = getMaxIter();
		double bd = this.getBound();
		boolean applyFun = this.applyFuncConst;
		String func2Apply = this.useFuncConst;
/*		String func2Apply = "None";
		if (applyFun) {
			func2Apply = this.useFuncConst;
		}
*/		
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				ComplexNumber z0 = new ComplexNumber(x0, y0);
				if (this.isComplexNumConst || this.complex == null) {this.complex = z0;}
					/*if (!applyFun) {
						this.complex = z0;
					} else {*/
						
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
						case "None" :
								this.complex = z0;
								break;
						default:
							this.complex = z0;
							break;
						}
					/*}*/

					/*if (!this.useSineCalc) {
						this.complex = z0;
					} else {
						this.complex = z0.sin();
					}*/
				
				int colorRGB;
				if (diff) {
					colorRGB = julia(z0, max, bd);
				} else {
					colorRGB = max - julia(z0, max, bd);
				}
				Color color;
				color = getPixelDisplayColor(row, col, colorRGB, diff);
				setPixel(row, n - 1 - col, color.getRGB());
			}
		}
	}

	private int julia(ComplexNumber zz, int max, double bd) {
		// f(z)=z^n+c
		ComplexNumber z = zz;

		final ComplexNumber complexConstant;
		if (this.complex == null) {
			complexConstant = new ComplexNumber(this.complexConst, 0);
		} else {
			complexConstant = this.complex;
		}

		for (int t = 0; t < max; t++) {
			if (z.abs() > bd) {
				return t;
			}
			z = z.power(this.power).plus(complexConstant);
		}
			
		/*	============	Field lines - Fatou Domain	======================
		 //((1-z^3)/6)/(z-z^2/2)^2)+c}
		
		ComplexNumber z = zz;
		final ComplexNumber complexConstant = new ComplexNumber(this.complexConst, 0);
		final ComplexNumber one = new ComplexNumber(1.0, 0.0);
		final ComplexNumber six = new ComplexNumber(6.0, 0.0);
		final ComplexNumber two = new ComplexNumber(2.0, 0.0);
		for (int t = 0; t < max; t++) {
			if (z.abs() > 2.0) {
				return t;
			}
			z = (((one.minus(z.power(3))).divides(six)).divides(((z.minus(z.power(2))).divides(two)).power(2))).plus(complexConstant);
		}
		=========================================================================*/
		
		return max;
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#getFractalShapeTitle()
	 */
	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz JuliaSet";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
//				final FractalBase frame = new Julia(2,0.279);	//(3,0.4);//(2,0.279);	//f(z) = z2 + 0.279
				final Julia frame = new Julia(2,true,-0.4,0.59); //new Julia(2,false,-1.29904,-0.75); //new Julia(2,0.279,true/*false*/);//Julia(2,"C3",true);//
				/*frame.setPower(2);
				frame.setComplex(frame.c1);*/
//				frame.depth = 5;
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(frame.WIDTH, frame.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

			}
		});
	}
/*
	private void doComplexTest() {
		final ComplexNumber a1 = new ComplexNumber(10.1,0.54);
		final ComplexNumber a11 = new ComplexNumber(10.1,0.54);
		ComplexNumber b1 = new ComplexNumber(7.9,0.384);
		System.out.println("a1 == "+a1);
		
		
		ComplexNumber a2 = a1.times(a1);//.plus(b1);
		System.out.println("a2 == "+a2);
		
		ComplexNumber myA2 = a11.power(2);//.plus(b1);
		System.out.println("myA2 == "+myA2);
		
		
		
		
	}
*/
}


/*//http://paulbourke.net/fractals/juliaset/

c = 0 + 0.8i
c = 0.37 + 0.1i
c = 0.355 + 0.355i
c = -0.54 + 0.54i
c = -0.4 + -0.59i
c = 0.34 + -0.05i
c = 0 + 0.8i
c = 0.37 + 0.1i
c = 0.355 + 0.355i
c = -0.54 + 0.54i
c = -0.4 + -0.59i
c = 0.355534 - 0.337292i
/////////////////////////////////////////////////
http://paulbourke.net/fractals/sinjulia/
If you are wondering how to compute the sine of a complex number, 
you can use the following relationships:

xk+1 = sin(xk) cosh(yk)
yk+1 = cos(xk) sinh(yk)

where zk = xk + i yk

 	
c = 1 + 0i
c = 1 + 0.1i
c = 1 + 0.2i
c = 1 + 0.3i
c = 1 + 0.4i
c = 1 + 0.5i
c = 1 + i
c = 3i/2
c = 0.984808 + 0.173648i
c = -1.29904 + -0.75i
c = 1.17462 + 0.427525i
c = 1.87939 + 0.68404i
c = 1 + i
c = -0.2 + i

Contributions by Klaus Messner

c = 1 + 0i
x = -1.201171875, y = -0.963541666666666

c = 0 + i
x = -0.5390625, y = -1.4296875 
 */
