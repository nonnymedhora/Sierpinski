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
 */
public class PolyFract extends FractalBase {

	private static final long serialVersionUID = 14565L;
	private boolean useDiff = false;
	private boolean isComplexNumConst;
	private ComplexNumber compConst;
	
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

	public PolyFract(int pow, boolean uD, double bd, boolean complexNumIsConst, double realVal,
			double imgVal) {
		this(pow, uD, bd, complexNumIsConst);

		if (!complexNumIsConst) {
			this.compConst = new ComplexNumber(realVal, imgVal);
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
/*System.out.println("isComplexNumConst---->"+(isComplexNumConst));	
System.out.println("this.compConst == null---->"+(this.compConst == null));	
System.out.println("(BEFOR)this.compConst == "+this.compConst);
System.out.println("type---->"+(type));	*/
		
		String func2Apply = this.useFuncConst;
		/*boolean applyFun = this.applyFuncConst;
		String func2Apply = "None";
		if (applyFun) {
			func2Apply = this.useFuncConst;
		}*/
		
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				
				ComplexNumber zx = null;
				ComplexNumber zy = null;
				
				switch (type) {
					case "Reverse":
						zx = new ComplexNumber(x0, y0);
						zy = new ComplexNumber(y0, x0);
						break;
					case "Exchange":
						zx = new ComplexNumber(x0, 0.0);
						zy = new ComplexNumber(0.0, y0);
						break;
					case "Single":
						zx = new ComplexNumber(x0, y0);
						zy = new ComplexNumber(0.0, 0.0);
					case "Duplicate":
						zx = zy = new ComplexNumber(x0, y0);
						break;
					case	"Exponent"	:
						zx = new ComplexNumber(x0, y0).power((int)x0);
						zy = new ComplexNumber(y0, x0).power((int)y0);
					case	"Default"	:
						zx = new ComplexNumber(x0, 0.0);
						zy = new ComplexNumber(y0, 0.0);
					default:
						zx = new ComplexNumber(x0, 0.0);
						zy = new ComplexNumber(y0, 0.0);
						break;

				}/*
System.out.println("zx_is_nul____"+(zx==null));	
System.out.println("zy_is_nul____"+(zy==null));	*/			
				
///////////////////////////////////////////////////////////////////////////				
//				/////////////////////////////////////////
//				//	reverse complex numbers
//				ComplexNumber zx = new ComplexNumber(x0, y0);
//				ComplexNumber zy = new ComplexNumber(y0, x0);
//
//				//	exchange
//				/*ComplexNumber zx = new ComplexNumber(x0, 0.0);
//				ComplexNumber zy = 				new ComplexNumber(0.0, y0);
////////////////////////////////////////////////////////////////////////*/
				
				if (this.isComplexNumConst || this.compConst == null) {
						this.compConst = zx.plus(zy);
				}
					/*if (!applyFun) {
						this.compConst = zx.plus(zy);
					} else {*/
						
						switch (func2Apply) {
						case "Sine"	:
								this.compConst = this.compConst.sine();	//z0.sin();
								break;
						case "Cosine" :
								this.compConst = this.compConst.cosine();	//z0.cos();
								break;
						case "Tan" :
								this.compConst = this.compConst.tangent();	//z0.tan();
								break;
						case "None" :
								this.compConst = zx.plus(zy);
								break;
						default:
							this.compConst = zx.plus(zy);
							break;
						}
					
					/*this.compConst =	zx.plus(zy);*/
/*if (row<5&&col<5) {
	System.out.println("this.compConst is " + this.compConst);
	//this.compConst =	 zx.power((int)pow).plus(zy.power((int)pow));
}*/
					
					//c=-0.74543+0.11301*i
					//this.compConst = new ComplexNumber(-0.74543, 0.11301);
					
					// new ComplexNumber(-0.75,0.11);
					//this.compConst = new ComplexNumber(-0.75, 0.11);
				/*}*/
/*System.out.println("(AFTER)this.compConst == "+this.compConst);*/
				int colorRGB;
				// int gray = /*maxIter - */mand(z0, maxIter);
				if (diff) {
					colorRGB = polyFract(zx, zy, max, pow, this.compConst, bd);
				} else {
					colorRGB = max - polyFract(zx, zy,max, pow, this.compConst,bd);
				}
				Color color = this.getPixelDisplayColor(row, col, colorRGB, diff);

				setPixel(row, n - 1 - col, color.getRGB());

				/*}*/
			}
		}
		
	}

	private int polyFract(ComplexNumber z1, ComplexNumber z2, 
							int maxItr, double powr, ComplexNumber constant,
							double boundary) {
		ComplexNumber z11 = z1.power(this.power).plus(constant);
		ComplexNumber z22 = z2.power(this.power).plus(constant);
		ComplexNumber zz = z11.plus(z22).plus(constant);
		for (int t = 0; t < maxIter; t++) {
			if (zz.abs() > boundary)
				return t;
			z11 = z11.power(this.power).plus(constant);
			z22 = z22.power(this.power).plus(constant);
			zz = z11.plus(z22).plus(constant);//zz.power((int) power).plus(constant);
		}
		return maxIter;
	}

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

}
