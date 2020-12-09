/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class CliffordAttractor extends /*Custom*/Attractor {
	/*	xn+1 = sin(a yn) + c cos(a xn)
     *  yn+1 = sin(b xn) + d cos(b yn)
	 * 
	 */
	
	private int width = 1600;
    private int height = 1200;
    private int frames = 10000;
    private int iters = 10000;
    private int skipIters = 10;

    private double sensitivity = 0.02;
    
    private double minX = -4.0;
    private double minY = minX * height / width;

    private double maxX = 4.0;
    private double maxY = maxX * height / width;
    
    private double minA = Math.acos( 1.6 / 2.0 );
    private double maxA = Math.acos( 1.3 / 2.0 );
    
    private double minB = Math.acos( -0.6 / 2.0 );
    private double maxB = Math.acos( 1.7 / 2.0 );
    
    private double minC = Math.acos( -1.2 / 2.0 );
    private double maxC = Math.acos( 0.5 / 2.0 );
    
    private double minD = Math.acos( 1.6 / 2.0 );
    private double maxD = Math.acos( 1.4 / 2.0 );
	

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param c
	 */
	public CliffordAttractor(double x, double y, double z, Color c) {
		super(x, y, z, c);
		this.setName("Clifford");
		this.setIs3D(false);
		this.setTimeInvariant(true);
		this.setTimeIterDependant(true);
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param c
	 * @param dspace
	 */
	public CliffordAttractor(double x, double y, double z, Color c, String dspace) {
		super(x, y, z, c, dspace);
		this.setName("Clifford");
		this.setIs3D(false);
		this.setTimeInvariant(true);
		this.setTimeIterDependant(true);
	}

	/**
	 * @param tuple
	 * @param col
	 */
	public CliffordAttractor(Tuple3d tuple, Color col) {
		super(tuple, col);
		this.setName("Clifford");
		this.setIs3D(false);
		this.setTimeInvariant(true);
		this.setTimeIterDependant(true);
	}
	
	

	private volatile double a;
	private volatile double b;
	private volatile double c;
	private volatile double d;

	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}
	
	Color createHue( double h ) {
		if(h<0)		h *= -1;
        h *= 6.0;
        int hi = (int) h;
        double hf = h - hi;
        if(hf<0)	hf	*=	-1;
        switch( hi % 6 ) {
            case 0:
            return new Color( 1 , (int) hf, 0 );
            case 1:
            return new Color((int) (1.0 - hf), 1, 0 );
            case 2:
            return new Color( 0 , 1, (int) hf );
            case 3:
            return new Color( 0, (int)(1.0 - hf), 1 );
            case 4:
            return new Color( (int) hf, 0, 1 );
            case 5:
            return new Color( 1, 0, (int)(1.0 - hf) );
        }
        
        return null;
    }

	
	public Color add(Color c) {
		Color clr = this.color;
		int r = clr.getRed() + c.getRed();
		int g = clr.getGreen() + c.getGreen();
		int b = clr.getBlue() + c.getBlue();
		Color ch = this.correctColor(r, g, b);
		this.setColor(ch);
		return ch;
	}


	private Color correctColor(int red, int green, int blue) {
		int correctedR = red > 0xFF ? 0xFF : red;
		correctedR = correctedR < 0 ? 0 : correctedR;
		int correctedG = green > 0xFF ? 0xFF : green;
		correctedG = correctedG < 0 ? 0 : correctedG;
		int correctedB = blue > 0xFF ? 0xFF : blue;
		correctedB = correctedB < 0 ? 0 : correctedB;
		
		return new Color(correctedR,correctedG,correctedB);
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.Attractor#dx(org.bawaweb.ui.sierpinkski.Tuple3d, double)
	 */
	@Override
	protected double dx(Tuple3d tuple, double dt) {
		double aX = this.getA();
		double cX = this.getC();
//      double xn = sin(a * y) + c * cos(a * x);
		return Math.sin(aX*tuple.y)+cX*Math.cos(aX*tuple.x);
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.Attractor#dy(org.bawaweb.ui.sierpinkski.Tuple3d, double)
	 */
	@Override
	protected double dy(Tuple3d tuple, double dt) {
		double bY = this.getB();
		double dY = this.getD();
//      double yn = sin(b * x) + d * cos(b * y);
		return Math.sin(bY*tuple.x)+d*Math.cos(b*tuple.y);
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.Attractor#dz(org.bawaweb.ui.sierpinkski.Tuple3d, double)
	 */
	@Override
	protected double dz(Tuple3d tuple, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Tuple3d update(final Tuple3d existingTuple, final double dt, final int iterTime) {
		double p = (double) iterTime / frames;
		Color curCol = this.createHue(p * this.color.getRGB());
		
        double aTmp = Math.cos( minA + p * (maxA - minA) ) * 2.0;	this.setA(aTmp);
        double bTemp = Math.cos( minB + p * (maxB - minB) ) * 2.0;	this.setB(bTemp);
        double cTemp = Math.cos( minC + p * (maxC - minC) ) * 2.0;	this.setC(cTemp);
        double dTemp = Math.cos( minD + p * (maxD - minD) ) * 2.0;	this.setD(dTemp);

//        double xn = sin(a * y) + c * cos(a * x);
//        double yn = sin(b * x) + d * cos(b * y);

		// do the update
		Tuple3d updatedTuple = this.update(existingTuple, dt);
		
		int xi = (int)( (updatedTuple.x - minX) * width / (maxX - minX) );
		int yi = (int)( (updatedTuple.y - minY) * height / (maxY - minY) );

		 if ( xi >= 0 && xi < width &&
                 yi >= 0 && yi < height ) {
                 this.add(curCol);
//                this.setColor( xi + yi * width ] += curCol;
                 
            }

		return updatedTuple;		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final AttractorsGenerator cliffordAttractor = new AttractorsGenerator("clifford");
				cliffordAttractor.setAttractors(new Attractor[] {
					new CliffordAttractor(0.0,20.0,0,Color.black)
				});
				cliffordAttractor.setMaxIter(10000);
				cliffordAttractor.setSpace2d("x-y");
				final JFrame frame = cliffordAttractor;
				
				frame.setTitle("BawazCliffordAttractor");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
	}

}
