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
	private boolean isComplexNumConst = false;
	
	public final ComplexNumber C1 = new ComplexNumber(-0.74543,0.11301);	//c=-0.74543+0.11301*i
	public final ComplexNumber C2 = new ComplexNumber(-0.75,0.11);			//c= -0.75+0.11*i
	public final ComplexNumber C3 = new ComplexNumber(-0.1,0.651);			//c=-0.1+0.651*i

	//for dealing with C1-C3
	private boolean preStringComplexConstConstruct = false;
	//wiil remove above l8r - and make all uniform
	
	private boolean isConstFuncApplied = false;

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
		this(mul,comp);
		this.useDiff = uDiff;
	}
	

	public Julia(int mul, String comp, boolean uDiff) {
		this(mul,comp);
		this.useDiff = uDiff;
	}
	public Julia(int mul, String comp, double bd, boolean uDiff) {
		this(mul,comp,uDiff);
		this.setComplexNumConst(true);
		this.setBound(bd);
	}

	public Julia(int m, boolean uDiff, double realVal, double imagVal) {
		this.power=m;
		this.useDiff=uDiff;
		this.complex=new ComplexNumber(realVal,imagVal);
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
		this.setComplexNumConst(true);
		this.setPreStringComplexConstConstruct(true);
	}

	public Julia(int m, double con, double bd, boolean uDiff) {
		this(m,con,uDiff);
		this.setComplexNumConst(true);
		this.setBound(bd);
	}

	public Julia(int m, boolean uDiff, boolean keepConst) {
		this();
		this.power = m;
		this.useDiff = uDiff;
		this.setComplexNumConst(keepConst);
		if (keepConst) {
			this.complex = null;
		}
	}

	public Julia(int m, boolean uDiff, double bd, boolean keepConst) {
		this(m, uDiff, keepConst);
		this.setComplexNumConst(keepConst);
		if (keepConst) {
			this.complex = null;
		}
		this.setBound(bd);
	}

	public Julia(int m, boolean uDiff, double bd, double realVal, double imgVal) {
		this(m,uDiff,realVal,imgVal);
		this.setBound(bd);
		this.setComplexNumConst(false);
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

	public boolean isComplexNumConst() {
		return this.isComplexNumConst;
	}

	public void setComplexNumConst(boolean isComplexNumConst) {
		this.isComplexNumConst = isComplexNumConst;
	}

	public boolean isPreStringComplexConstConstruct() {
		return this.preStringComplexConstConstruct;
	}

	public void setPreStringComplexConstConstruct(boolean preStringCC) {
		this.preStringComplexConstConstruct = preStringCC;
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
		
		String func2Apply = this.useFuncConst;
//		System.out.println("this.complexConst===" + this.complexConst);
//		System.out.println("this.complex==null  " + (this.complex == null));
//		System.out.println("this.isComplexNumConst  --  " + this.isComplexNumConst);
//		System.out.println("this.isComplexNumConst || this.complex == null  is  "
//				+ (this.isComplexNumConst || this.complex == null));
//		System.out.println("this.complex===" + this.complex);	
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				ComplexNumber z0 = new ComplexNumber(x0, y0);

				/*if (isComplexNumConst || this.complex == null) {
					if (!this.preStringComplexConstConstruct) {
						this.complex = z0;
					}
				}*/				
				int colorRGB;
				if (diff) {
					colorRGB = this.julia(z0, max, bd);
				} else {
					colorRGB = max - this.julia(z0, max, bd);
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

		ComplexNumber complexConstant = computeComplexConstant();

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
	
	
	/*private int julia(ComplexNumber z0, int maxIterations, int pwr, ComplexNumber constant, double bd) {
		ComplexNumber z = z0;
		for (int t = 0; t < maxIterations; t++) {
			if (z.abs() > bd)
				return t;
			z = z.power(pwr).plus(constant);
		}
		return maxIterations;
	}*/

	private ComplexNumber computeComplexConstant() {
		ComplexNumber cConst;
		
		if (isComplexNumConst || this.complex == null) {
			if (!preStringComplexConstConstruct) {
				cConst = new ComplexNumber(this.complexConst, 0);
			}else{
				cConst=this.complex;
			}
		} else {
			cConst = this.complex;
		}

		String func2Apply = this.useFuncConst;
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

			if (!this.isComplexNumConst()) {
				this.complex = cConst;
				this.isConstFuncApplied = true;
			}
		}
		return cConst;
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
//				final FractalBase frame = new Julia(2,true,-0.4,0.59); //new Julia(2,false,-1.29904,-0.75); //new Julia(2,0.279,true/*false*/);
				//
				final FractalBase frame = new Julia(2,"C3",true);//
//				frame.setUseFuncConst("Log");
				/*frame.setPower(2);
				frame.setComplex(frame.c1);*/
//				frame.depth = 5;
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
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
/
*/