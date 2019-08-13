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
 *
 */
public class Julia extends FractalBase {

	private static final long serialVersionUID = 1987L;
	
	private int power;
	private double complexConst;

	public Julia() {
		super();
		power=		5;//2;
		complexConst=0.544;//0.279;
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

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		createJulia(g,depth);
	}

	private void createJulia(Graphics2D g, int d) {
		double xc = 0;//-0.5;
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
				} else {
					color = new Color(255 - gray, 255 - gray, 255 - gray);
				}
				setPixel(i, n - 1 - j, color.getRGB());
			}
		}
	}

	private int julia(ComplexNumber zz, int max) {
		ComplexNumber z = zz;
		final ComplexNumber complexConstant = new ComplexNumber(this.complexConst,0);
		for (int t = 0; t < max; t++) {
			if (z.abs() > 2.0)
				return t;
//			z = z.times(z).plus(zz);
//			z = z.times(z).plus(new ComplexNumber(0.279,0));					//f(z) = z^2 + 0.279
//			z = z.times(z).times(z).plus(new ComplexNumber(0.4,0));				//f(z) = z^3 + 0.400
//			z= z.times(z).times(z).times(z).plus(new  ComplexNumber(0.484,0));	//f(z) = z^4 + 0.484
//			z= z.times(z).times(z).times(z).times(z).plus(new ComplexNumber(0.544,0));	//f(z) = z^5 + 0.544
			z = z.power(power).plus(complexConstant);
		}
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
//		new Julia().doComplexTest();
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = new Julia(2,0.279);	//(3,0.4);//(2,0.279);	//f(z) = z2 + 0.279
				frame.depth = 5;
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(frame.WIDTH, frame.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

			}
		});
	}

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

}
