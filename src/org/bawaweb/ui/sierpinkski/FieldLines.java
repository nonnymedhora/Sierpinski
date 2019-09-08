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
public class FieldLines extends FractalBase {
	private static final long serialVersionUID = 166L;

	private double complexConst; // either this
	private ComplexNumber complex; // or this
	private boolean useDiff = false;
	private boolean isComplexNumConst = false;

	public final ComplexNumber one = new ComplexNumber(1.0, 0.0);
	public final ComplexNumber two = new ComplexNumber(2.0, 0.0);
	public final ComplexNumber three = new ComplexNumber(3.0, 0.0);
	public final ComplexNumber four = new ComplexNumber(4.0, 0.0);
	public final ComplexNumber five = new ComplexNumber(5.0, 0.0);
	public final ComplexNumber six = new ComplexNumber(6.0, 0.0);
	public final ComplexNumber seven = new ComplexNumber(7.0, 0.0);
	public final ComplexNumber eight = new ComplexNumber(8.0, 0.0);
	public final ComplexNumber nine = new ComplexNumber(9.0, 0.0);

	public FieldLines() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		createFieldLines(g, depth, this.useDiff);

	}
	


	/*	Field lines for an iteration of the form 
		((1-z^3)/6)/(z-z^2/2)^2)+c}
		
		z = (((one.minus(z.power(3))).divides(six)).divides(((z.minus(z.power(2))).divides(two)).power(2))).plus(complexConstant);
	*/

	private void createFieldLines(Graphics2D g, int d, boolean diff) {		
		double xc = getxC();
//System.out.println("xc is == "+xc);
		double yc = getyC();
//System.out.println("yc is == "+yc);
		int n = getAreaSize();
//System.out.println("n is == "+n);
		int max = getMaxIter();
//System.out.println("max is == "+max);
		int size=2;
//System.out.println("size is == "+size);
//System.out.println("isComplexNumConst "+ isComplexNumConst);
//System.out.println("this.complex==null "+ (this.complex==null));
//System.out.println("isComplexNumConst || this.complex == null  - "+(isComplexNumConst || this.complex == null));
		
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / size;
				double y0 = yc - size / 2 + size * col / size;
				ComplexNumber z0 = new ComplexNumber(x0, y0);
				
				
				if (isComplexNumConst || this.complex == null) {
					this.complex = z0;
				}
//System.out.println("row is "+row+" and col is "+col+" and this.complex is => "+this.complex.toString());
				int colorRGB=0;
				if (diff) {
					colorRGB = fieldLines(z0, max, this.bound);// ? 0 : 255;
				} else {
					colorRGB = max - fieldLines(z0, max, this.bound);// ? 255 : 0);
				}
				Color color;
				color = getPixelDisplayColor(row, col, colorRGB, diff);
				setPixel(row, n - 1 - col, color.getRGB());
			}
		}
	}
	
	/*private boolean fieldLines(ComplexNumber z, int max, double bd) {
		// f(z)=((1-z^3)/6)/(z-z^2/2)^2)+c}
				ComplexNumber z0 = z;

				final ComplexNumber complexConstant;
				if (this.complex == null) {
					complexConstant = new ComplexNumber(this.complexConst, 0);
				} else {
					complexConstant = this.complex;
				}

				for (int t = 0; t < max; t++) {
					if (z0.abs() > bd) {
						return true;
					}
					z0 = (((one.minus(z0.power(3))).divides(six)).divides(((z0.minus(z0.power(2))).divides(two)).power(2))).plus(complexConstant);
					System.out.println(z0.toString());
				}
				
				return false;
	}*/

	private int fieldLines(ComplexNumber z, int max, double bd) {
		// f(z)=((1-z^3)/6)/(z-z^2/2)^2)+c}
				ComplexNumber z0 = z;

				@SuppressWarnings("unused")
				final ComplexNumber complexConstant;
				if (this.complex == null) {
					complexConstant = new ComplexNumber(this.complexConst, 0);
				} else {
					complexConstant = this.complex;
				}

				for (int t = 0; t < max; t++) {
					if (z0.abs() > bd) {
						return t;
					}
					ComplexNumber zNumer = (one.minus(z0.power(3).divides(six)));
					ComplexNumber zDenom = ((z0.minus(z0.power(2).divides(two)))).power(2);
//					z0 = (((one.minus(z0.power(3))).divides(six)).divides(((z0.minus(z0.power(2))).divides(two)).power(2))).plus(complex);
					
					z0 = zNumer.divides(zDenom).plus(complexConstant);

				}
				
				return max;
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#getFractalShapeTitle()
	 */
	@Override
	protected String getFractalShapeTitle() {		
		return "FieldLines";
	}

	public double getComplexConst() {
		return complexConst;
	}

	public void setComplexConst(double complexConst) {
		this.complexConst = complexConst;
	}

	public ComplexNumber getComplex() {
		return complex;
	}

	public void setComplex(ComplexNumber complex) {
		this.complex = complex;
	}

	public boolean isUseDiff() {
		return useDiff;
	}

	public void setUseDiff(boolean useDiff) {
		this.useDiff = useDiff;
	}

	public boolean isComplexNumConst() {
		return isComplexNumConst;
	}

	public void setComplexNumConst(boolean isComplexNumConst) {
		this.isComplexNumConst = isComplexNumConst;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				final FieldLines frame = new FieldLines();
				frame.setComplexNumConst(true);
				FractalBase.depth = 5;
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

			}
		});
	}

}
