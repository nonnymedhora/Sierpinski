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
 *
 */
public class PolyFract extends FractalBase {

	private static final long serialVersionUID = 14565L;
	private boolean useDiff = false;
	private double bound = 2.0;
	private boolean isComplexNumConst;
	private ComplexNumber compConst;
	
	private double power;

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
		this.isComplexNumConst = complexNumIsConst;
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
		
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				ComplexNumber zx = new ComplexNumber(x0, 0.0);
				ComplexNumber zy = new ComplexNumber(0.0, y0);

				if (isComplexNumConst || this.compConst == null) {
					this.compConst = zx.plus(zy);
				}
				
				int colorRGB;
				// int gray = /*maxIter - */mand(z0, maxIter);
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
		ComplexNumber z11 = z1.power((int) this.power).plus(constant);
		ComplexNumber z22 = z2.power((int) this.power).plus(constant);
		ComplexNumber zz = z11.plus(z22).plus(constant);
		for (int t = 0; t < maxIter; t++) {
			if (zz.abs() > boundary)
				return t;
			z11 = z11.power((int) this.power).plus(constant);
			z22 = z22.power((int) this.power).plus(constant);
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

	public double getBound() {
		return this.bound;
	}

	public void setBound(double bd) {
		this.bound = bd;
	}

	public ComplexNumber getCompConst() {
		return this.compConst;
	}

	public void setCompConst(ComplexNumber cConst) {
		this.compConst = cConst;
	}

	public double getPower() {
		return this.power;
	}

	public void setPower(double pow) {
		this.power = pow;
	}

	/*public boolean isComplexNumConst() {
		return this.isComplexNumConst;
	}

	public void setComplexNumConst(boolean isConstant) {
		this.isComplexNumConst = isConstant;
	}*/

}
