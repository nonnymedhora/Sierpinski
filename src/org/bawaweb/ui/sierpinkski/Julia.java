/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Properties;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

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

	public final ComplexNumber M1 = new ComplexNumber((Math.PI/2),(Math.PI/2)*(0.6));			//[(π/2)*(1.0 + 0.6i)]";	//f(z) = z^2 + ...
	public final ComplexNumber M2 = new ComplexNumber((Math.PI/2),(Math.PI/2)*(0.4));			//[(π/2)*(1.0 + 0.4i)]";	//f(z) = z^2 + ...
	
	

	//for dealing with C1-C3
	private boolean preStringComplexConstConstruct = false;
	//wiil remove above l8r - and make all uniform
	
	private boolean isConstFuncApplied = false;
	
	public Julia(Properties p) {
		super(p);
		if (p.getProperty("useDiff") != null)
			this.setUseDiff(Boolean.parseBoolean(p.getProperty("useDiff").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("isComplexNumConst") != null) {
			this.setComplexNumConst(Boolean.parseBoolean(p.getProperty("isComplexNumConst").replaceAll(WHITESPACE,EMPTY)));
		} if (p.getProperty("constReal") != null && p.getProperty("constImag") != null/* && !(p.getProperty("constReal").replaceAll(WHITESPACE,EMPTY).equals("DynamicConst") || p.getProperty("constImag").replaceAll(WHITESPACE,EMPTY).equals("DynamicConst"))*/) {
			this.setComplex(Double.parseDouble(p.getProperty("constReal").replaceAll(WHITESPACE, EMPTY)),
					Double.parseDouble(p.getProperty("constImag").replaceAll(WHITESPACE, EMPTY)));
			
			this.setComplexNumConst(false);		//redundant	-	But!
		}
		if (p.getProperty("fieldType") != null) {
			String fieldType = p.getProperty("fieldType").replaceAll(WHITESPACE, EMPTY);
			this.setFieldType(fieldType);
		}
	}
	
	
	public Julia() {
		super();
		this.setVisible(false);
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
			case "M1":
				this.complex = M1;
				break;
			case "M2":
				this.complex = M2;
				break;
			default:
				this.complex = C1;
				break;
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
	 * @param complex the complex to set
	 */
	public void setComplex(double realVal,double imagVal) {
		this.complex = new ComplexNumber(realVal,imagVal);
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
	
	/////////////////////////
	// z=z^2+c
	////////////////////////
	private boolean isZSq = false; // todo - rename to getFieldLines??

	public boolean isZSq() {
		return isZSq;
	}

	public void setZSq(boolean iszsq) {
		isZSq = iszsq;
	}

	protected ComplexNumber getZSqValue(ComplexNumber z) {
		double x = z.real();
		double y = z.imaginary();
		return this.getZSqValue(x, y);
	}

	protected ComplexNumber getZSqValue(double x, double y) {
		return new ComplexNumber(x, y).power(2);
	}
	///////////
	// endshttps://en.wikipedia.org/wiki/Julia_set#Field_lines

	// http://paulbourke.net/fractals/juliaset/
	/*
	 * Julia was interested in the iterative properties of a more general
	 * expression, * namely
	 * 
	 * z^4 + z^3/(z-1) + z^2/(z^3 + 4*z^2 + 5) + c
	 */
	private boolean isClassicJulia = false;

	public boolean isClassicJulia() {
		return isClassicJulia;
	}

	public void setClassicJulia(boolean isClassicJulia) {
		this.isClassicJulia = isClassicJulia;
	}

	protected ComplexNumber getClassicJulia(ComplexNumber z) {
		double x = z.real();
		double y = z.imaginary();
		return this.getClassicJulia(x, y);
	}

	protected ComplexNumber getClassicJulia(double x, double y) {
		ComplexNumber z = new ComplexNumber(x, y);
		final ComplexNumber one = new ComplexNumber(1.0, 0.0);
		final ComplexNumber four = new ComplexNumber(4.0, 0.0);
		final ComplexNumber five = new ComplexNumber(5.0, 0.0);

		final ComplexNumber first = z.power(4);
		final ComplexNumber second = (z.power(3)).divides(z.minus(one));
		final ComplexNumber third = (z.power(2)).divides((z.power(3)).plus(four.times(z.power(2))).plus(five));

		z = first.plus(second).plus(third);
		return z;
	}

	// https://en.wikipedia.org/wiki/Julia_set#Field_lines
	private boolean isFatou = false; // todo - rename to getFieldLines??

	public boolean isFatou() {
		return isFatou;
	}

	public void setFatou(boolean isFat) {
		isFatou = isFat;
	}

	private String fieldType;
	
	public String getFieldType() {
		return fieldType;
	}


	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;

		if (this.fieldType.equals("None")) {
			this.isFatou = false;
			this.isZSq = false;
			this.isClassicJulia = false;
		} else if (this.fieldType.equals("Fatou")) {
			this.isFatou = true;
			this.isZSq = false;
			this.isClassicJulia = false;
		} else if (this.fieldType.equals("Z-Sq")) {
			this.isFatou = false;
			this.isZSq = true;
			this.isClassicJulia = false;
		} else if (this.fieldType.equals("ClassicJ")) {
			this.isFatou = false;
			this.isZSq = false;
			this.isClassicJulia = true;
		}
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
		String pxFunc2Apply = this.useFuncPixel;
		
		boolean pxFuncHasConst = (pxFunc2Apply.indexOf('c') > -1 || pxFunc2Apply.indexOf('C') > -1);
		
		if (this.isSavePixelInfo2File()) {
			if (!this.isComplexNumConst) {
				if (func2Apply.equals("None")) {
					this.appendConstantInfo2File("Dynamic Pixel Constant   z=C");
				} else {
					this.appendConstantInfo2File("Dynamic Pixel Constant  " + func2Apply + " (z)=C");
				}
			} else {
				if (this.useFuncConst.equals("None")) {
					this.appendConstantInfo2File("Constant z=C");
				} else {
					this.appendConstantInfo2File("Constant  " + this.useFuncConst + " (z)=C");
				}
			}
		}
		
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
				
				if (this.isReversePixelCalculation()) {
					double tmp = x0;
					x0 = y0;
					y0 = tmp;
				}
				
				//ComplexNumber z0 = new ComplexNumber(x0,y0);
				ComplexNumber z0;
				if (!this.isReversePixelCalculation()) {
					z0 = new ComplexNumber(x0, y0);
					z0 = this.getPixelComplexValue(x0, y0);					
				} else {
					z0 = new ComplexNumber(y0, x0);
					z0 = this.getPixelComplexValue(y0, x0);
				}
				
				if (!pxFuncHasConst) {
					z0 = this.computePixel(pxFunc2Apply, z0);
				} else {
					ComplexNumber cConst;

					if (isComplexNumConst || this.complex == null) {
						if (!preStringComplexConstConstruct) {
							cConst = new ComplexNumber(this.complexConst, 0);
						} else {
							cConst = this.complex;
						}
					} else {
						cConst = this.complex;
					}

					z0 = this.computePixel(pxFunc2Apply, z0, cConst);
				}
				
				/*if (z0.isNaN()) {
					continue;
				}*/

				if (this.isFatou()) {
					z0 = this.getFatouValue(z0);
				} else if (this.isZSq()) {
					z0 = this.getZSqValue(z0);
				} else if (this.isClassicJulia()) {
					z0 = this.getClassicJulia(z0);
				} else {
					z0 = z0;//new ComplexNumber(x0, y0);
				}

				/*if (isComplexNumConst || this.complex == null) {
					if (!this.preStringComplexConstConstruct) {
						this.complex = z0;
					}
				}*/			
				
				if (this.colorChoice.equals(BlackWhite)) {
					int bOrW;
					if (diff) {
						bOrW = this.julia(z0, max, bd);
						if (bOrW != max) {
							bOrW = 0;
						} else {
							bOrW = COLORMAXRGB;
						}
					} else {
						int res = this.julia(z0, max, bd);
						bOrW = max - res;
						if (bOrW != res) {
							bOrW = 0;
						} else {
							bOrW = COLORMAXRGB;
						}

					}
					
					setPixel(row, n - 1 - col, bOrW);
					if (this.isSavePixelInfo2File()) {
						this.appendPixelInfo2File(row, n - 1 - col, bOrW);
					}

				} else {
				
					int colorRGB;
					if (diff) {
						colorRGB = this.julia(z0, max, bd);
					} else {
						colorRGB = max - this.julia(z0, max, bd);
					}
					Color color;
					color = getPixelDisplayColor(row, col, colorRGB, diff);
					setPixel(row, n - 1 - col, color.getRGB());
					
					if (this.isSavePixelInfo2File()) {
						this.appendPixelInfo2File(row, n - 1 - col, color.getRGB());
					}
				}
			}
		}
		
		if (this.isSavePixelInfo2File()) {
			this.closePixelFile();
		}
	}
	
	
	private int julia(ComplexNumber zz, int max, double bd) {
		// f(z)=z^n+c
		ComplexNumber z = zz;

		ComplexNumber complexConstant = this.computeComplexConstant();
		
		if (complexConstant.isNaN()) {
			return 0;		//	for now
		}

		for (int t = 0; t < max; t++) {
			if (z.abs() > bd) {
				return t;
			}
			
			if (this.pxConstOperation.equals("Plus")) {
				z = z.power(this.power).plus(complexConstant);
			} else if (this.pxConstOperation.equals("Minus")) {
				z = z.power(this.power).minus(complexConstant);
			} else if (this.pxConstOperation.equals("Multiply")) {
				z = z.power(this.power).times(complexConstant);
			} else if (this.pxConstOperation.equals("Divide")) {
				z = z.power(this.power).divides(complexConstant);
			} else if (this.pxConstOperation.equals("Power")) {
				z = z.power(this.power).power(complexConstant);
			}
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
			} else {
				cConst = this.complex;
			}
		} else {
			cConst = this.complex;
		}

		String func2Apply = this.useFuncConst;
		if (!this.isConstFuncApplied) {
			switch (func2Apply) {
			case "sine":
				cConst = cConst.sine(); //z0.sin();
				break;
			case "cosine":
				cConst = cConst.cosine(); //z0.cos();
				break;
			case "tan":
				cConst = cConst.tangent(); //z0.tan();
				break;
			case "arcsine":
				cConst = cConst.inverseSine(); //z0.sin();
				break;
			case "arccosine":
				cConst = cConst.inverseCosine(); //z0.cos();
				break;
			case "arctan":
				cConst = cConst.inverseTangent(); //z0.tan();
				break;
			case "reciprocal":
				cConst = cConst.reciprocal(); //z0.tan();
				break;
			case "reciprocalSquare":
				cConst = (cConst.reciprocal()).power(2); //z0.tan();
				break;
			case "square":
				cConst = cConst.power(2); //z0.sin();
				break;
			case "cube":
				cConst = cConst.power(3); //z0.cos();
				break;
			case "exponent(e)":
				cConst = cConst.exp(); //z0.tan();
				break;
			case "root":
				cConst = cConst.sqroot(); //z0.sin();
				break;
			case "cube-root":
				cConst = cConst.curoot(); //z0.cos();
				break;
			case "log(e)":
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
	
	private ComplexNumber computePixel(String fun, ComplexNumber z0, ComplexNumber compConst) {
		if (this.applyCustomFormula) {
			fun = fun.replaceAll("c", "(" + compConst.toString().replaceAll("i", "*i") + ")")
					.replaceAll("C", "(" + compConst.toString().replaceAll("i", "*i") + ")");

			return new FunctionEvaluator().evaluate(fun, z0);//, compConst);
		}
		return null;
	}

	private ComplexNumber computePixel(String fun, ComplexNumber z0) {
		if (!this.applyCustomFormula) {
			switch (fun) {
			case "sine":
				z0 = z0.sine(); // z0.sin();
				break;
			case "cosine":
				z0 = z0.cosine(); // z0.cos();
				break;
			case "tan":
				z0 = z0.tangent(); // z0.tan();
				break;
			case "arcsine":
				z0 = z0.inverseSine(); // z0.sin();
				break;
			case "arccosine":
				z0 = z0.inverseCosine(); // z0.cos();
				break;
			case "arctan":
				z0 = z0.inverseTangent(); // z0.tan();
				break;
			case "reciprocal":
				z0 = z0.reciprocal(); // z0.sin();
				break;
			case "reciprocalSquare":
				z0 = (z0.reciprocal()).power(2); // z0.sin();
				break;
			case "square":
				z0 = z0.power(2); // z0.sin();
				break;
			case "cube":
				z0 = z0.power(3); // z0.cos();
				break;
			case "exponent(e)":
				z0 = z0.exp(); // z0.tan();
				break;
			case "root":
				z0 = z0.sqroot(); // z0.sin();
				break;
			case "cube-root":
				z0 = z0.curoot(); // z0.cos();
				break;
			case "log(e)":
				z0 = z0.ln(); // z0.tan();
				break;

			case "None":
				z0 = z0;
				break;
			default:
				z0 = z0;
				break;
			}// ends-switch
		} else {
			z0 = new FunctionEvaluator().evaluate(fun, z0);
		}
		return z0;
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
				frame.setColorChoice("ComputeColor");
				/*frame.setUseColorPalette(false);
				frame.setUseBlackWhite(true);*/
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
/
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
x=-0.202420806884766,	y=0.3957333577474
/
/*

*https://en.wikipedia.org/wiki/Julia_set
*
*
Julia set for fc, c = 0.285 + 0i
Julia set for fc, c = 0.285 + 0.01i
Julia set for fc, c = 0.45 + 0.1428i
Julia set for fc, c = −0.70176 − 0.3842i
Julia set for fc, c = −0.835 − 0.2321i
Julia set for fc, c = −0.8 + 0.156i
Julia set for fc, c = −0.7269 + 0.1889i
Julia set for fc, c = −0.8i
*
*
*
*
==================================================
Using DEM/J
Images of Julia sets for fc(z) = z*z + c

c=-0.74543+0.11301*i
c= -0.75+0.11*i
c=-0.1+0.651*i

Julia set drawn by distance estimation, 
the iteration is of the form 
1-z^2+(z^5/(2 + 4z))+c
*
*
*
*
*
*
*/