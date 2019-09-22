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
						break;
					case "Duplicate":
						zx = zy = new ComplexNumber(x0, y0);
						break;
					case "Exponent"	:
						zx = new ComplexNumber(x0, 0.0).exp();
						zy = new ComplexNumber(y0, 0.0).exp();
						break;
					case	"Power"	:
						zx = new ComplexNumber(x0, y0).power((int)x0);
						zy = new ComplexNumber(y0, x0).power((int)y0);
						break;
					case	"Default"	:
						zx = new ComplexNumber(x0, 0.0);
						zy = new ComplexNumber(y0, 0.0);
						break;
					default:
						zx = new ComplexNumber(x0, 0.0);
						zy = new ComplexNumber(y0, 0.0);
						break;
				}
				
				if (this.isComplexNumConst || this.compConst == null) {
					this.compConst = zx.plus(zy);
				}
				
				String func2Apply = this.useFuncConst;
				if (!this.isConstFuncApplied) {
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
						case "ArcSine"	:
							this.compConst = this.compConst.inverseSine();	//z0.sin();
							break;
						case "ArcCosine" :
								this.compConst = this.compConst.inverseCosine();	//z0.cos();
								break;
						case "ArcTan" :
								this.compConst = this.compConst.inverseTangent();	//z0.tan();
								break;									
						case "Square"	:
							this.compConst = this.compConst.power(2);	//z0.sin();
								break;
						case "Cube" :
							this.compConst = this.compConst.power(3);	//z0.cos();
								break;
						case "Exponent" :
							this.compConst = this.compConst.exp();	//z0.tan();
								break;
						case "Root"	:
							this.compConst = this.compConst.sqroot();	//z0.sin();
								break;
						case "CubeRoot" :
							this.compConst = this.compConst.curoot();	//z0.cos();
								break;
						case "Log" :
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
				
				

				int colorRGB;

				if (diff) {
					colorRGB = polyFract(zx, zy, max, pow, this.compConst, bd);
				} else {
					colorRGB = max - polyFract(zx, zy,max, pow, this.compConst,bd);
				}
				
				Color color = this.getPixelDisplayColor(row, col, colorRGB, diff);

				setPixel(row, n - 1 - col, color.getRGB());

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
			zz = z11.plus(z22).plus(constant);
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