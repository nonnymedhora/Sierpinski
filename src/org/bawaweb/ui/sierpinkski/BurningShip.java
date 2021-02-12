/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *	@see https://en.wikipedia.org/wiki/Burning_Ship_fractal
 *	@see http://paulbourke.net/fractals/burnship/
 *	@see http://paulbourke.net/fractals/burnship/burningship.c
 */
public class BurningShip extends FractalBase {
	private static final long serialVersionUID = 189556L;
	
	private boolean useDiff = false;

	private double rangeWidth;
	
	public BurningShip() {
		super();

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public BurningShip(double range, double xc, double yc) {
		this();
		
		this.rangeWidth = range;
		this.setxC(xc);
		this.setyC(yc);
	}

	/**
	 * @param p
	 */
	public BurningShip(Properties p) {
		super(p);
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		createBurningShip(g, this.useDiff);
	}
	
	private void createBurningShip(Graphics2D g, boolean diff) {
		double xc = getxC();
		double yc = getyC();
		
		double size =	getScaleSize();
		int n = getAreaSize();//4000;
		int max = getMaxIter();
		double bd = this.getBound();		
		this.setRangeSpace(xc, yc, size, n);

		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;

				double cX = xc + 2 * this.rangeWidth * (row / (double) n - 0.5);
				double cY = yc + 2 * this.rangeWidth * (col / (double) n - 0.5);
				
				int colorRGB;
				if (diff) {
					colorRGB = this.burnShip(max, bd, x0, y0, cX, cY);
				} else {
					colorRGB = max - this.burnShip(max, bd, x0, y0, cX, cY);
				}

				Color color = getPixelDisplayColor(row, col, colorRGB, diff);
				
				setPixel(row, n - 1 - col, color.getRGB());			
			}	//	ends	for-col
		}		//	ends	for-row
	}

	private int burnShip(final int maxIterations, final double boundary, final double pixelX, final double pixelY,final double cX,final double cY) {
		ComplexNumber z;
		
		if (this.isReversePixelCalculation()) {
			z = this.getPixelComplexValue(pixelY, pixelX);
		} else {
			z = this.getPixelComplexValue(pixelX, pixelY);
		}
		
		int iter = 0;
		double xtemp;
		double ytemp;
		
		
		while (z.real * z.real + z.imaginary * z.imaginary < boundary && iter < maxIterations) {
			xtemp = z.real * z.real - z.imaginary * z.imaginary - cX;

			ytemp = Math.abs(2 * z.real * z.imaginary) - cY;

			z = new ComplexNumber(xtemp, ytemp);
			
			iter += 1;
		}
		
		return iter;
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

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#getFractalShapeTitle()
	 */
	@Override
	protected String getFractalShapeTitle() {
		return "BaWaZ BurningShip";
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
	Create the burning ship fractal
	Whole ship        -w 1.7 -c 0.45 0.5
	First small ship  -w 0.04 -c 1.755 0.03
	Second small ship -w .04 -c 1.625 0.035
	Tiny ship in tail -w 0.005 -c 1.941 0.004
	Another small one -w 0.008 -c 1.861 0.005
*/

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {		
//				final BurningShip bS = new BurningShip(0.02,-1.66,0.0055);	
//				final BurningShip bS = new BurningShip(5.7,0.45,0.5);			//wholeShip
				final BurningShip bS = new BurningShip(1.7,0.45,0.5);			//wholeShip
//				final BurningShip bS = new BurningShip(0.04, 1.755, 0.03);		//firstSmallShip
//				final BurningShip bS = new BurningShip(0.04, 1.625, 0.035);	//secondSmallShip
//				final BurningShip bS = new BurningShip(0.005,1.941,0.04);		//TinyShipInTail
//				final BurningShip bS = new BurningShip(0.008, 1.861, 0.005);	//AnotherSmallOne
				bS.setReversePixelCalculation(false);//setReversePixelCalculation(true);//
				bS.setUseDiff(false);//setUseDiff(true);//
				
				final FractalBase frame = bS;		
				frame.setColorChoice("ColorGradient6");//("ComputeColor");/*("ColorPalette");/*("BlackWhite");*/
				frame.setScaleSize(2);
				frame.setMaxIter(10000);//(100);//
				frame.setBound(10);//(40);//
				/*frame.setUseColorPalette(false);
				frame.setUseBlackWhite(true);*/
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH-200, FractalBase.HEIGHT-200);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

			}
		});
	
	}

	public void createFocalFractalShape(FractalBase fBase, ComplexNumber cStart, ComplexNumber cEnd) {
		BurningShip bS = (BurningShip) fBase;
		double xc = (cStart.real + cEnd.real) / 2.0;
		double yc = (cStart.imaginary + cEnd.imaginary) / 2.0;
		double size = getScaleSize();
		double bd = bS.getBound();
		int max = bS.getMaxIter();
		int n = getAreaSize();

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

				double cX = xc;// + 2 * this.rangeWidth * (row / (double) n - 0.5);
				double cY = yc;// + 2 * this.rangeWidth * (col / (double) n - 0.5);
				

				int colorRGB;
				if (this.useDiff) {
					colorRGB = this.burnShip(max, bd, x0, y0, cX, cY);
				} else {
					colorRGB = max - this.burnShip(max, bd, x0, y0, cX, cY);
				}

				Color color = getPixelDisplayColor(row, col, colorRGB, this.useDiff);
				
				setPixel(row, n - 1 - col, color.getRGB());	
				
			}
		}
		
	}

}
