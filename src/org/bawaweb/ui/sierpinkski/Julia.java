/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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
		/*this.npixel = 0;
		this.pixel = new int[WIDTH*HEIGHT];*/
		createJulia(g, this.useDiff);
		
		if(this.isCaptureOrbit())
			this.printOrbitMap();
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
		
		boolean pxFuncHasConst = this.applyCustomFormula
				&& this.checkForConstInAppliedFormula(pxFunc2Apply);//(pxFunc2Apply.indexOf('c') > -1 || pxFunc2Apply.indexOf('C') > -1);

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

		this.setRangeSpace(xc, yc, size, n);
		
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
				
				/*///////////////////////////////////////////////
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

				} else {/////////////////////////////////////////////*/

				int colorRGB;
				Color color = null;
					
//				if (! this.colorChoice.equals(Color_Palette_Blowout)) {
					if (diff) {
						colorRGB = this.julia(z0, max, bd);
					} else {
						colorRGB = max - this.julia(z0, max, bd);
					}
					color = getPixelDisplayColor(row, col, colorRGB, diff);
					/*this.pixel[npixel++] = colorRGB;*/
					
					setPixel(row, n - 1 - col, color.getRGB());
//				} else {	//colorChoice=="ColorBlowout"
//					if (diff) {
//						colorRGB = this.julia(z0, max, bd);
//					} else {
//						colorRGB = max - this.julia(z0, max, bd);
//					}
//
//					colorRGB = this.time2Color(colorRGB);
//					/*this.pixel[npixel++] = colorRGB;*/
//
//					setPixel(row, n - 1 - col, colorRGB);
//				}
				
				if (this.isSavePixelInfo2File()) {
					this.appendPixelInfo2File(row, n - 1 - col, color.getRGB());
				}
				/*}//////////////////////////*/
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
		
		List<Double> zList = null;
		List<ComplexNumber> zPxList = null;
		boolean trapped = false;
		double trapDist = 0.0;
		
		if (this.isCaptureOrbit()) {
			zList = new ArrayList<Double>();
			zPxList = new ArrayList<ComplexNumber>();
		}
		
		int t = 0;
		for (; t < max; t++) {
			if (this.isCaptureOrbit()) {
				if (zList.size() < 20) {
					zList.add(z.abs());
					zPxList.add(z);
				}

				switch (this.trapShape) {
					case "Circle":
						trapped = isInOrbitCircle(z);
						break;
					case "Square":
						trapped = isInOrbitSquare(z);
						break;
					case "Cross":
						trapped = isInOrbitCross(z);
						break;
					case "Diamond":
						trapped = isInOrbitDiamond(z);
						break;
					case "TrianglUP":
						trapped = isInOrbitTriangle(z,"UP");
						break;
					case "TrianglDWN":
						trapped = isInOrbitTriangle(z,"DOWN");
						break;
					case "LineLR":
						trapped = isInOrbitLine(z,"LR");
						break;
					case "LineUD":
						trapped = isInOrbitLine(z,"UD");
						break;
					default:
						trapped = trapDist < trapSize;
						break;
				}
				
				if (trapped)
					break;	//exits for loop
			}
			
			if (z.abs() > bd) {
				return t;
			}

			z = this.processJuliaZValue(z, complexConstant);
		}

		if (this.isCaptureOrbit()) {
			this.orbitPixelMap.put(zz, zPxList);
			this.orbitDistanceMap.put(zz, zList);
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

		
		if (this.isCaptureOrbit() && trapped) {
			return (int) (this.getDistance(z, this.orbitTrapPoint) * getWidth() / this.trapSize);
		}
		if (!this.isCaptureOrbit()) {
			return max;
		} else {
			return (int) (t + 1 - ((Math.log(Math.log(Math.sqrt(trapDist))) / Math.log(2.0))));
		}
	}


	/*private*/ ComplexNumber processJuliaZValue(ComplexNumber zz, ComplexNumber aComplexConstant) {
		if (this.pxConstOperation.equals("Plus")) {
			zz = zz.power(this.power).plus(aComplexConstant);
		} else if (this.pxConstOperation.equals("Minus")) {
			zz = zz.power(this.power).minus(aComplexConstant);
		} else if (this.pxConstOperation.equals("Multiply")) {
			zz = zz.power(this.power).times(aComplexConstant);
		} else if (this.pxConstOperation.equals("Divide")) {
			zz = zz.power(this.power).divides(aComplexConstant);
		} else if (this.pxConstOperation.equals("PowerZ^C")) {
			zz = zz.power(this.power).power(aComplexConstant);
		} else if (this.pxConstOperation.equals("PowerC^Z")) {
			zz = aComplexConstant.power(this.power).power(zz);
		}
		return zz;
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

	/*private*/ ComplexNumber computeComplexConstant() {
		ComplexNumber cConst = null;
		
		if (isComplexNumConst || this.complex == null) {
			if (!preStringComplexConstConstruct) {
				cConst = new ComplexNumber(this.complexConst, 0);
			} else {
				cConst = this.complex;
			}
		} else if (!this.isComplexNumConst) {
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
	
//	private ComplexNumber computePixel(String fun, ComplexNumber z0, ComplexNumber compConst) {
//		if (this.applyCustomFormula) {
//			fun = fun.replaceAll("c", "(" + compConst.toString().replaceAll("i", "*i") + ")")
//					.replaceAll("C", "(" + compConst.toString().replaceAll("i", "*i") + ")");
//
//			return new FunctionEvaluator().evaluate(fun, z0);//, compConst);
//		}
//		return null;
//	}
//
//	private ComplexNumber computePixel(String fun, ComplexNumber z0) {
//		if (!this.applyCustomFormula) {
//			switch (fun) {
//			case "sine":
//				z0 = z0.sine(); // z0.sin();
//				break;
//			case "cosine":
//				z0 = z0.cosine(); // z0.cos();
//				break;
//			case "tan":
//				z0 = z0.tangent(); // z0.tan();
//				break;
//			case "arcsine":
//				z0 = z0.inverseSine(); // z0.sin();
//				break;
//			case "arccosine":
//				z0 = z0.inverseCosine(); // z0.cos();
//				break;
//			case "arctan":
//				z0 = z0.inverseTangent(); // z0.tan();
//				break;
//			case "reciprocal":
//				z0 = z0.reciprocal(); // z0.sin();
//				break;
//			case "reciprocalSquare":
//				z0 = (z0.reciprocal()).power(2); // z0.sin();
//				break;
//			case "square":
//				z0 = z0.power(2); // z0.sin();
//				break;
//			case "cube":
//				z0 = z0.power(3); // z0.cos();
//				break;
//			case "exponent(e)":
//				z0 = z0.exp(); // z0.tan();
//				break;
//			case "root":
//				z0 = z0.sqroot(); // z0.sin();
//				break;
//			case "cube-root":
//				z0 = z0.curoot(); // z0.cos();
//				break;
//			case "log(e)":
//				z0 = z0.ln(); // z0.tan();
//				break;
//
//			case "None":
//				z0 = z0;
//				break;
//			default:
//				z0 = z0;
//				break;
//			}// ends-switch
//		} else {
//			z0 = new FunctionEvaluator().evaluate(fun, z0);
//		}
//		return z0;
//	}
	
	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#getFractalShapeTitle()
	 */
	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz JuliaSet";
	}
	
	public void draw_inverse_iteration(int power, ComplexNumber c) {
		int bailout = 10;
		ComplexNumber[][] plot_records = new ComplexNumber[getAreaSize()][getAreaSize()];
		
		for(int cnt=0; cnt<HEIGHT; cnt++) {
			plot_records[cnt] = new ComplexNumber[WIDTH];
		}
		for(int row = 0; row < HEIGHT; row++) {
			for(int col = 0; col < WIDTH; col++) { 
				plot_records[row][col] = new ComplexNumber(0);
			}
		}
		/*
		//z0	is regular Mandlbrot set's origin	???
		ComplexNumber z0 = new ComplexNumber(0.12,0.34);
		for(int cnt = 0; cnt<5; cnt++) {
				c.times()
			z0 = Math.sqrt(Math.add(z0,Math.multiply(c,-1)));
			z0 = math.multiply(math.sqrt(math.add(z0,math.multiply(c,-1))),-1);
		}
		var zNode = new JuliaTreeNode(z0);
		*/
		
		
	}
	
	//	to be used by the inverse_iteration algorithm
	// which sets up the list of JuliaNodes
	class JuliaNode {
		private ComplexNumber nodeValCmplx;
		private JuliaNode left;
		private JuliaNode right;
		
		public JuliaNode(ComplexNumber cNum) {
			this.nodeValCmplx = cNum != null ? cNum : new ComplexNumber(0);
			this.left=null;
			this.right=null;
		}
		
		public String toString() {
			String lftVal = this.left!=null?this.left.toString():"empty";
			String rhtVal = this.right!=null?this.right.toString():"empty";
			return this.left.toString() + "<--[" + this.nodeValCmplx.toString() + "-->" + this.right.toString();
		}

		public JuliaNode getLeft() {
			return this.left;
		}

		public void setLeft(JuliaNode lft) {
			this.left = lft;
		}

		public JuliaNode getRight() {
			return this.right;
		}

		public void setRight(JuliaNode rht) {
			this.right = rht;
		}
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
				final FractalBase frame = new Julia(2, new ComplexNumber(0.12,0.34) );//new ComplexNumber(-0.4,0.59));//new Julia(2,"C3",true);//
				frame.setColorChoice/*("BlackWhite");/*/("ComputeColor");
				frame.setScaleSize(5);
				frame.setMaxIter(50);//1000
				frame.setCaptureOrbit(true);
				/*frame.setUseColorPalette(false);
				frame.setUseBlackWhite(true);*/
//				frame.setUseFuncConst("Log");
				/*frame.setPower(2);
				frame.setComplex(frame.c1);*/
//				frame.depth = 5;
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(300,300);//(FractalBase.WIDTH-300, FractalBase.HEIGHT-300);
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
	
	/*@Override*/
	public void createFractalDtlInfoObject() {
		FractalDtlInfo fd = new FractalDtlInfo("Julia");

		fd.setScaleSize(scaleSize);
		fd.setPower(this.power);
		fd.setBound(this.bound);
		fd.setColorChoice(this.colorChoice);
		fd.setxC(this.getxC());
		fd.setyC(this.getyC());
		fd.setX_min(this.x_min);
		fd.setX_max(this.x_max);
		fd.setY_min(this.y_min);
		fd.setY_max(this.y_max);
		fd.setMaxIter(maxIter);
		fd.setUseDiff(this.useDiff);
		fd.setPxConstOperation(this.pxConstOperation);
		fd.setUseFuncPixel(this.useFuncPixel);
		fd.setUseFuncConst(this.useFuncConst);
		fd.setPxXTransformation(this.pxXTransformation);
		fd.setPxYTransformation(this.pxYTransformation);
		fd.setPixXYOperation(this.pixXYOperation);
		fd.setApplyCustomFormula(this.applyCustomFormula);
		
		fd.setfBase(this);

		fbDTlsList.add(fd);

	}
	
	@Override
	public void setFractalDtlInfo() {
//		this.createFractalDtlInfoObject();
		
		String info = "<html><font color='black'>";
		final String eol = "<br/>";
		info += "Class: " + this.getClass().getName() + eol;
		info += "ScaleSize = " + scaleSize + eol;
		info += "Power (Exponent): " + this.power + ", Boundary = " + this.bound + eol;
		info += "ColorScheme: " + this.colorChoice + eol;
		info += "[XC,YC]:	[" + this.getxC() +", "+ this.getyC() +"]" + eol;
		info += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.x_min + ", "+ this.y_min + "] to [" + this.x_max + ", " + this.y_max +"]" + eol;
		info += "Center [x,y] is  (" + ((this.x_min + this.x_max) / 2) + ", " + ((this.y_min + this.y_max) / 2) + ")" + eol;
		if(this.isCaptureOrbit())
			info += "Orbit Trap Captured at "+ this.orbitTrapPoint + ", TrapShape is " + this.trapShape + ", TrapSize is " + this.trapSize + eol;
		info += "Constant: (cr,ci)  is (" + this.complex.real + ", " + this.complex.imaginary + ")" + eol;
		info += "MaxIterations: " + maxIter + "  - Ud = " + this.useDiff + eol;
		info += "Pixel_Constant Operation Z --> C is " + this.pxConstOperation + eol;
		info += "Pixel Function F(Z) is " + this.useFuncPixel + eol;
		info += "Constant Function F(C) is " + this.useFuncConst + eol;
		info += "X Transform f(X) = " + this.pxXTransformation + ", Y Transform f(Y) = " + this.pxYTransformation + eol;
		info += "X_Y Operation f(X,Y) " + this.pixXYOperation + eol;
		info += "</font></html>";
		
		this.fractalDtlInfo = info;
	}


	public void createFocalFractalShape(FractalBase fbase, ComplexNumber cStart, ComplexNumber cEnd) {
		Julia julie = (Julia) fbase;
		double xc = (cStart.real + cEnd.real) / 2.0;
		double yc = (cStart.imaginary + cEnd.imaginary) / 2.0;
		double size = getScaleSize();
		double bd = julie.getBound();
		int max = julie.getMaxIter();
//		System.out.println("in__Julia__createFocalFractalShape");		

		String func2Apply = julie.useFuncConst;
		String pxFunc2Apply = julie.useFuncPixel;

		int n = getAreaSize();
		int pow = julie.power;
		
		boolean pxFuncHasConst = this.applyCustomFormula
				&& this.checkForConstInAppliedFormula(pxFunc2Apply);//(pxFunc2Apply.indexOf('c') > -1 || pxFunc2Apply.indexOf('C') > -1);

		
		this.setRangeSpace(xc, yc, size, n);

		double xStart = cStart.real < cEnd.real ? cStart.real : cEnd.real;
		double yStart = cStart.imaginary < cEnd.imaginary ? cStart.imaginary : cEnd.imaginary;

		double xEnd = cStart.real > cEnd.real ? cStart.real : cEnd.real;
		double yEnd = cStart.imaginary > cEnd.imaginary ? cStart.imaginary : cEnd.imaginary;

		x_min = xStart;
		x_max = xEnd;
		y_min = yStart;
		y_max = yEnd;

		if (xStart == xEnd || yStart == yEnd)
			return;
		
		double xColJump = 1.0 * (xEnd - xStart) / n;
		double yRowJump = 1.0 * (yEnd - yStart) / n;

		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xStart + row * xColJump;
				double y0 = yStart + col * yRowJump;			

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
				
				if (this.isFatou()) {
					z0 = this.getFatouValue(z0);
				} else if (this.isZSq()) {
					z0 = this.getZSqValue(z0);
				} else if (this.isClassicJulia()) {
					z0 = this.getClassicJulia(z0);
				} else {
					z0 = z0;//new ComplexNumber(x0, y0);
				}
				
				int colorRGB;
				Color color = null;
					
				if (! this.colorChoice.equals(Color_Palette_Blowout)) {
					if (julie.useDiff) {
						colorRGB = this.julia(z0, max, bd);
					} else {
						colorRGB = max - this.julia(z0, max, bd);
					}
					color = getPixelDisplayColor(row, col, colorRGB, julie.useDiff);
					/*this.pixel[npixel++] = colorRGB;*/
					setPixel(row, n - 1 - col, color.getRGB());
				} else {	//colorChoice=="ColorBlowout"
					if (julie.useDiff) {
						colorRGB = this.julia(z0, max, bd);
					} else {
						colorRGB = max - this.julia(z0, max, bd);
					}

					colorRGB = this.time2Color(colorRGB);
					/*this.pixel[npixel++] = colorRGB;*/

					setPixel(row, n - 1 - col, colorRGB);
				}
			}
		}
	}// ends__createFocalFractalShape

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