/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

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
	
	// Buddhabrot
	private boolean isBuddha = true;
	private Map<Pixel,Integer> buddhaMap=new HashMap<Pixel,Integer>();
	
	
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
		String pxFunc2Apply = this.useFuncPixel;
		

		
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

		int n = getAreaSize();//599;//512;	(0-599)
		//System.out.println("here with depth " + depth);
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				
				ComplexNumber z0;
				if (!this.isReversePixelCalculation()) {
					z0 = new ComplexNumber(x0, y0);
					z0 = this.getPixelComplexValue(x0, y0);					
				} else {
					z0 = new ComplexNumber(y0, x0);
					z0 = this.getPixelComplexValue(y0, x0);
				}
				
				
				z0 = this.computePixel(pxFunc2Apply, z0);
				z0 = this.computeComplexConstant(func2Apply, z0);	
				

				if (this.isUseBlackWhite()) {
					int bOrW = 0;
					
					if (diff) {
						bOrW = this.mand(z0, max, this.power, this.complex, bd, row, col);
						if (!this.isBuddha) {
							if (bOrW != max) {
								bOrW = 0;
							} else {
								bOrW = COLORMAXRGB;
							} 
						}
					} else {
						int res = this.mand(z0, max, this.power, this.complex, bd, row, col);
						if (!this.isBuddha) {
							bOrW = max - res;
							if (bOrW != res) {
								bOrW = 0;
							} else {
								bOrW = COLORMAXRGB;
							} 
						}

					}
					
					if (!this.isBuddha) {
						setPixel(row, n - 1 - col, bOrW);
					}
					if (this.isSavePixelInfo2File()) {
						this.appendPixelInfo2File(row, n - 1 - col, bOrW);
					}

				} else {

					int colorRGB;
	
					if (diff) {
						colorRGB = mand(z0, max, this.power, this.complex, bd, row, col);
					} else {
						colorRGB = max - mand(z0, max, this.power, this.complex,bd, row, col);
					}
					
					Color color = null;
	
					if (!this.isBuddha) {
						color = this.getPixelDisplayColor(row, col, colorRGB, diff);
						setPixel(row, n - 1 - col, color.getRGB());
					}
					if (this.isSavePixelInfo2File()) {
						this.appendPixelInfo2File(row, n - 1 - col, color.getRGB());
					}
				}

			}
		}
		
		if (this.isSavePixelInfo2File()) {
			this.closePixelFile();
		}
		
		if(this.isBuddha){System.out.println("BuddhaSize==="+this.buddhaMap.size());}
	}
	
	
	private ComplexNumber computePixel(String fun, ComplexNumber z0) {
		switch (fun) {
			case "sine":
				z0 = z0.sine(); // z0.sin();
				break;
			case "coosine":
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
			case "reiprocal":
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

		return z0;
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
				cConst = cConst.reciprocal(); //1/z0;
				break;
			case "reciprocalSquare":
				cConst = (cConst.reciprocal()).power(2); //(1/z)^2;
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

			if (!this.isComplexNumConst) {
				this.complex = cConst;
				this.isConstFuncApplied = true;
			}
		}
		
		return cConst;
	}

	
	private int mand(ComplexNumber z0, int maxIterations, int pwr, ComplexNumber constant, double bd, int r, int c) {
		ComplexNumber z = z0;
		for (int t = 0; t < maxIterations; t++) {
			if (z.abs() > bd)
				return t;

			if (this.pxConstOperation.equals("Plus")) {
				z = z.power(pwr).plus(constant);
			} else if (this.pxConstOperation.equals("Minus")) {
				z = z.power(pwr).minus(constant);
			} else if (this.pxConstOperation.equals("Multiply")) {
				z = z.power(pwr).times(constant);
			} else if (this.pxConstOperation.equals("Divide")) {
				z = z.power(pwr).divides(constant);
			}/* else if (this.pxConstOperation.equals("Power")) {
				z = z.power(pwr).power(constant);
			}*/
		}
		if (!this.isBuddha) {
			return maxIterations;
		} else {
			return this.populateBuddha(r,c);
		}
	}

	private int populateBuddha(int row, int col) {
		final Pixel key = new Pixel(row, col);
		Integer value = this.buddhaMap.get(key);
		if (value == null) {
			this.buddhaMap.put(key, 1);
		} else {
			value += 1;
			this.buddhaMap.put(key, value);
		}
		return 123;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new Mandelbrot(2,2,true);//96);//,2);//,true);//(2,3,false);
//				frame.depth = 5;
/*				frame.setUseColorPalette(false);
				frame.setUseBlackWhite(true);*/
				frame.setPxXTransformation("absolute");
				frame.setPxYTransformation("absolute");

				frame.setPixXYOperation("Plus");
				
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

			}
		});
	}
	

	/*@Override
	public String getFractalDetails() {		
		return this.getFractalShapeTitle();
	}*/

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
