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
 */
public class PolyFract extends FractalBase {

	private static final long serialVersionUID = 14565L;
	private boolean useDiff = false;
	private boolean isComplexNumConst = false;
	private ComplexNumber compConst;

	private boolean isConstFuncApplied = false;
	
	public PolyFract() {
		super();
	}

	public PolyFract(int pow, boolean uD) {
		this();
		this.power = pow;
		this.useDiff = uD;
	}

	public PolyFract(int pow, boolean uD, double bd) {
		this(pow, uD);
		this.setBound(bd);
	}
	
	public PolyFract(int pow, boolean uD, double bd, boolean complexNumIsConst) {
		this(pow, uD,bd);
		this.setComplexNumConst(complexNumIsConst);
	}

	public PolyFract(int pow, boolean uD, double bd,/* boolean complexNumIsConst,*/ double realVal,
			double imgVal) {
		this(pow, uD, bd);//, complexNumIsConst);
		/*if (!complexNumIsConst) {*/
			this.compConst = new ComplexNumber(realVal, imgVal);
		/*} else {
			this.compConst = null;
		}*/
			this.setComplexNumConst(false);
	}

	public PolyFract(Properties p) {
		super(p);
		if (p.getProperty("useDiff") != null)
			this.setUseDiff(Boolean.parseBoolean(p.getProperty("useDiff").replaceAll(WHITESPACE, EMPTY)));
		if (p.getProperty("isComplexNumConst") != null) {
			this.setComplexNumConst(
					Boolean.parseBoolean(p.getProperty("isComplexNumConst").replaceAll(WHITESPACE, EMPTY)));
		}
		if (p.getProperty("constReal") != null && p.getProperty(
				"constImag") != null) {
			this.setCompConst(new ComplexNumber(Double.parseDouble(p.getProperty("constReal").replaceAll(WHITESPACE, EMPTY)),
					Double.parseDouble(p.getProperty("constImag").replaceAll(WHITESPACE, EMPTY))));

			this.setComplexNumConst(false); // redundant - But!
		}
		if (p.getProperty("rowColumnMixType") != null) {
			String fieldType = p.getProperty("rowColumnMixType").replaceAll(WHITESPACE, EMPTY);
			this.setRowColMixType(fieldType);
		}
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		createPolyFractals(g,this.useDiff);

	}

	private void createPolyFractals(Graphics2D g, boolean diff) {
		double xc = getxC();// 0;	//0.0
		double yc = getyC();// 0;	//0.0
		double size = getScaleSize();//4.5;// this.power;//this.mag;//2;	//1.0

		int n = getAreaSize();// 599;

		int max = getMaxIter();	//255
		double bd = this.getBound();
		double pow = this.getPower();	
		String type = this.getRowColMixType();
		
		if (this.isSavePixelInfo2File()) {
			if (!this.isComplexNumConst) {
				if (this.useFuncConst.equals("None")) {
					this.appendConstantInfo2File("Dynamic Pixel Constant   z=C");
				} else {
					this.appendConstantInfo2File("Dynamic Pixel Constant  " + this.useFuncConst + " (z)=C");
				}
			} else {
				if (this.useFuncConst.equals("None")) {
					this.appendConstantInfo2File("Constant z=C");
				} else {
					this.appendConstantInfo2File("Constant  " + this.useFuncConst + " (z)=C");
				}
			}
		}
/*System.out.println("isComplexNumConst---->"+(isComplexNumConst));	
System.out.println("this.compConst == null---->"+(this.compConst == null));	
System.out.println("(BEFOR)this.compConst == "+this.compConst);
System.out.println("type---->"+(type));	*/
		
		
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0;
				double y0;
				if (!this.isReversePixelCalculation()) {
					x0 = xc - size / 2 + size * row / n;
					y0 = yc - size / 2 + size * col / n;
				} else {
					x0 = xc - size / 2 + size * col / n;
					y0 = yc - size / 2 + size * row / n;
				}
				
				ComplexNumber zx = null;
				ComplexNumber zy = null;
				
				switch (type) {
					case "Reverse":
						zx = this.getPixelComplexValue(x0, y0);
						zy = this.getPixelComplexValue(y0, x0);
						break;
					case "Exchange":
						zx = this.getPixelComplexValue(x0, 0.0);
						zy = this.getPixelComplexValue(0.0, y0);
						break;
					case "Single":
						zx = this.getPixelComplexValue(x0, y0);
						zy = this.getPixelComplexValue(0.0, 0.0);
						break;
					case "Duplicate":
						zx = zy = this.getPixelComplexValue(x0, y0);
						break;
					case "Exponent"	:
						zx = this.getPixelComplexValue(x0, 0.0).exp();
						zy = this.getPixelComplexValue(y0, 0.0).exp();
						break;
					case	"Power"	:
						zx = this.getPixelComplexValue(x0, y0).power((int)x0);
						zy = this.getPixelComplexValue(y0, x0).power((int)y0);
						break;
					case	"Default"	:
						zx = this.getPixelComplexValue(x0, 0.0);
						zy = this.getPixelComplexValue(y0, 0.0);
						break;
					default:
						zx = this.getPixelComplexValue(x0, 0.0);
						zy = this.getPixelComplexValue(y0, 0.0);
						break;
				}
				
				String pxFunc2Apply = this.useFuncPixel;

				boolean pxFuncHasConst = this.applyCustomFormula
						&& (pxFunc2Apply.indexOf('c') > -1 || pxFunc2Apply.indexOf('C') > -1);
				if (!pxFuncHasConst) {
					zx = this.computePixel(pxFunc2Apply, zx);
					zy = this.computePixel(pxFunc2Apply, zy);
				} else {

					ComplexNumber cConst = null;

					if (this.isComplexNumConst || this.compConst == null) {
						cConst = zx.plus(zy);
						this.compConst = cConst;
					} else if (!this.isComplexNumConst) {
						if(this.compConst==null){System.out.println("uh--oh[poly]  this.compConst="+this.compConst+" avd this.isComplexNumConst="+this.isComplexNumConst);}
						cConst = this.compConst;
					}

					zx = this.computePixel(pxFunc2Apply, zx, cConst);
					zy = this.computePixel(pxFunc2Apply, zy, cConst);
				
				}
//				
//				switch (pxFunc2Apply) {
//					case "sine"	:
//						zx = zx.sine();
//						zy = zy.sine();
//						break;
//					case "cosine" :
//						zx = zx.cosine();
//						zy = zy.cosine();
//						break;
//					case "tan" :
//						zx = zx.tan();
//						zy = zy.tan();
//						break;
//					case "arcsine"	:
//						zx = zx.inverseSine();
//						zy = zy.inverseSine();
//						break;
//					case "arccosine" :
//						zx = zx.inverseCosine();
//						zy = zy.inverseCosine();
//						break;
//					case "arctan" :
//						zx = zx.inverseTangent();
//						zy = zy.inverseTangent();
//						break;									
//					case "square"	:
//						zx = zx.power(2);
//						zy = zy.power(2);
//						break;
//					case "cube" :
//						zx = zx.power(3);
//						zy = zy.power(3);
//						break;
//					case "exponent(e)" :
//						zx = zx.exp();
//						zy = zy.exp();
//						break;
//					case "root"	:
//						zx = zx.sqroot();
//						zy = zy.sqroot();
//						break;
//					case "cube-root" :
//						zx = zx.curoot();
//						zy = zy.curoot();
//						break;
//					case "log(e)" :
//						zx = zx.ln();
//						zy = zy.ln();
//						break;	
//					case "None" :
//						zx = zx;
//						zy = zy;
//						break;
//					default:
//						zx = zx;
//						zy = zy;
//						break;
//			}// ends switch
//				

			if (zx.isNaN()||zy.isNaN()) {
				continue;
			}
				
			if (this.isComplexNumConst || this.compConst == null) {
				this.compConst = zx.plus(zy);
			}
			
			String func2Apply = this.useFuncConst;
			if (!this.isConstFuncApplied) {
				switch (func2Apply) {
					case "sine"	:
						this.compConst = this.compConst.sine();	//z0.sin();
						break;
					case "cosine" :
						this.compConst = this.compConst.cosine();	//z0.cos();
						break;
					case "tan" :
						this.compConst = this.compConst.tangent();	//z0.tan();
						break;
					case "arcsine"	:
						this.compConst = this.compConst.inverseSine();	//z0.sin();
						break;
					case "arccosine" :
						this.compConst = this.compConst.inverseCosine();	//z0.cos();
						break;
					case "arctan" :
						this.compConst = this.compConst.inverseTangent();	//z0.tan();
						break;									
					case "square"	:
						this.compConst = this.compConst.power(2);	//z0.sin();
						break;
					case "cube" :
						this.compConst = this.compConst.power(3);	//z0.cos();
						break;
					case "exponent(e)" :
						this.compConst = this.compConst.exp();	//z0.tan();
						break;
					case "root"	:
						this.compConst = this.compConst.sqroot();	//z0.sin();
						break;
					case "cube-root" :
						this.compConst = this.compConst.curoot();	//z0.cos();
						break;
					case "log(e)" :
						this.compConst = this.compConst.ln();	//z0.tan();
						break;	
					case "None" :
						this.compConst = zx.plus(zy);
						break;
					default:
						this.compConst = zx.plus(zy);
						break;
				}// ends switch
				
				if (!this.isComplexNumConst()) {
					this.isConstFuncApplied = true;
				}
			}
			

			if (this.compConst.isNaN()) {
				continue;
			}
			
			
			if (this.colorChoice.equals(BlackWhite)) {
				int bOrW;
				if (diff) {
					bOrW = this.polyFract(zx,zy, max, this.power, this.compConst, bd);
					if (bOrW != max) {
						bOrW = 0;
					} else {
						bOrW = COLORMAXRGB;
					}
				} else {
					int res = this.polyFract(zx,zy, max, this.power, this.compConst, bd);
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
					colorRGB = polyFract(zx, zy, max, pow, this.compConst, bd);
				} else {
					colorRGB = max - polyFract(zx, zy,max, pow, this.compConst,bd);
				}
				
				Color color = this.getPixelDisplayColor(row, col, colorRGB, diff);

				setPixel(row, n - 1 - col, color.getRGB());

				if (this.isSavePixelInfo2File()) {
					this.appendPixelInfo2File(row, n - 1 - col, color.getRGB());
				}
			}

			}
		}
		
	}

	private int polyFract(ComplexNumber z1, ComplexNumber z2, 
							int maxItr, double powr, ComplexNumber constant,
							double boundary) {
		if (z1.isNaN() || z2.isNaN()) {
			return 0;
		}
		
		ComplexNumber z11 = z1.power(this.power).plus(constant);
		ComplexNumber z22 = z2.power(this.power).plus(constant);
		
		ComplexNumber zz;
		switch (this.pxConstOperation) {
			case "Plus":
				zz = z11.plus(z22).plus(constant);
				break;
			case "Minus":
				zz = z11.plus(z22).minus(constant);
				break;
			case "Multiply":
				zz = z11.plus(z22).times(constant);
				break;
			case "Divide":
				zz = z11.plus(z22).divides(constant);
				break;
			case "Power":
				zz = z11.plus(z22).power(constant);
				break;
			default:
				zz = z11.plus(z22).plus(constant);
				break;
		}

		for (int t = 0; t < maxIter; t++) {

			if (zz.isNaN()) {
				return 0;
			}
			
			if (zz.abs() > boundary){
				return t;
			}
			
			z11 = z11.power(this.power).plus(constant);
			z22 = z22.power(this.power).plus(constant);
			
			switch (this.pxConstOperation) {
				case "Plus":
					zz = z11.plus(z22).plus(constant);
					break;
				case "Minus":
					zz = z11.plus(z22).minus(constant);
					break;
				case "Multiply":
					zz = z11.plus(z22).times(constant);
					break;
				case "Divide":
					zz = z11.plus(z22).divides(constant);
					break;
				case "Power":
					zz = z11.plus(z22).power(constant);
					break;
				default:
					zz = z11.plus(z22).plus(constant);
					break;
			}
			
			
//			if (this.pxConstOperation.equals("Plus")) {
//				z11 = z11.power(this.power).plus(constant);
//				z22 = z22.power(this.power).plus(constant);
//			} else if (this.pxConstOperation.equals("Minus")) {
//				z11 = z11.power(this.power).minus(constant);
//				z22 = z22.power(this.power).minus(constant);
//			} else if (this.pxConstOperation.equals("Multiply")) {
//				z11 = z11.power(this.power).times(constant);
//				z22 = z22.power(this.power).times(constant);
//			} else if (this.pxConstOperation.equals("Divide")) {
//				z11 = z11.power(this.power).divides(constant);
//				z22 = z22.power(this.power).divides(constant);
//			}/* else if (this.pxConstOperation.equals("Power")) {
//				z = z.power(pwr).power(constant);
//			}*/
			
		}
		return maxIter;
	}
	
	

	/*@Override
	public String getFractalDetails() {		
		return this.getFractalShapeTitle();
	}*/

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#getFractalShapeTitle()
	 */
	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz_PolyFractals";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new PolyFract(3/*10*//*2*/,true/* false*/, 2.5,true);//(2,2,true);//96);//,2);//,true);//(2,3,false);
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});

	}

	public ComplexNumber getCompConst() {
		return this.compConst;
	}

	public void setCompConst(ComplexNumber cConst) {
		this.compConst = cConst;
	}

	public boolean isComplexNumConst() {
		return this.isComplexNumConst;
	}

	public void setComplexNumConst(boolean isConstant) {
		this.isComplexNumConst = isConstant;
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

}
