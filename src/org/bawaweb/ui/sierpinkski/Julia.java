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
	
	private int power;
	private double complexConst;
	
	private ComplexNumber complex;
	
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
		super();
		this.power = mul;
		this.complexConst = con;
	}
	
	

	/**
	 * @param mul
	 * @param comp
	 */
	public Julia(int mul, ComplexNumber comp) {
		super();
		this.power = mul;
		this.complex = comp;
	}

	public Julia(int mul, String comp) {
		super();
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

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		createJulia(g,depth);
	}

	private void createJulia(Graphics2D g, int d) {
		double xc = 0;
		double yc = 0;
		double size = 2;

		int n = 512;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double x0 = xc - size / 2 + size * i / n;
				double y0 = yc - size / 2 + size * j / n;
				ComplexNumber z0 = new ComplexNumber(x0, y0);
				int gray = maxIter - julia(z0, maxIter);
				Color color;
				/* Simple */
				/* color = new Color(gray, gray, gray); */
				/* Complex */ 
				if (gray % 2 == 0) {
					color = new Color(gray, gray, gray);
				} else if (gray % 7 == 0) {
					final int start = 128;
					int num1 = correctColor(start, gray, i, 7);
					int num2 = correctColor(start, gray, j, 7);
					color = new Color(num1, num2, start);
//					color = new Color(start + gray / 7 - i, start + gray / 7 - j, start + gray / 7);
				} else {
					color = new Color(255 - gray, 255 - gray, 255 - gray);
				}
				setPixel(i, n - 1 - j, color.getRGB());
			}
		}
	}

	// correction for color range  0--255
	private int correctColor(int start, int num, int i, int div) {
		int corrected = start + num / div - i;	//random transformation
		if (corrected > 255) {
			corrected = 255;
		}
		if (corrected < 0) {
			corrected = 0;
		}
		return corrected;
	}

	private int julia(ComplexNumber zz, int max) {
		// f(z)=z^n+c
		ComplexNumber z = zz;

		final ComplexNumber complexConstant;
		if (this.complex == null) {
			complexConstant = new ComplexNumber(this.complexConst, 0);
		} else {
			complexConstant = this.complex;
		}

		for (int t = 0; t < max; t++) {
			if (z.abs() > 2.0) {
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
				final Julia frame = new Julia(2,"C2");//2,0.279);
				/*frame.setPower(2);
				frame.setComplex(frame.c1);*/
				frame.depth = 5;
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
