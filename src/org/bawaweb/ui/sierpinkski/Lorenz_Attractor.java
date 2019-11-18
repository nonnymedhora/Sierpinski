/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class Lorenz_Attractor extends JFrame {

	private static final long serialVersionUID = 12345543L;

	/** Class to store 3-dimensional values. */

	class Tuple3d {

		public double x;
		public double y;
		public double z;

		/**
		 * Constructs a new Tuple class. *
		 * 
		 * @param x
		 * @param y
		 * @param z
		 **/
		public Tuple3d(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		/*@Override
		public String toString() {
			return "[" + x + ":" + y + ":" + z + "]";
		}*/
		@Override
		public String toString() {
			return "[" + (int)x + ":" + (int)y + ":" + (int)z + "]";
		}

	}

	abstract class Attractor {
		public static final int MAX_ITER = 50000;
		private Tuple3d t3d;
		private Color color;
		private double timeJump;
		private double cumulativeTime;
		private String name;

		protected double xmin;// = -100d;
		protected double xmax;// = 100d;
		protected double ymin;// = xmin;
		protected double ymax;// = xmax;
		protected double zmin;// = xmin;
		protected double zmax;// = xmax;

		public Attractor(double x, double y, double z, Color c) {
			this.setT3d(new Tuple3d(x, y, z));
			this.setTimeJump(0.001); // TODO externalize
			this.color = c;
		}

		public Attractor(Tuple3d tuple, Color col) {
			this(tuple.x, tuple.y, tuple.z, col);
		}

		public Tuple3d getT3d() {
			return t3d;
		}

		public void setT3d(Tuple3d t3d) {
			this.t3d = t3d;
		}

		public double getTimeJump() {
			return this.timeJump;
		}

		public void setTimeJump(double tm) {
			this.timeJump = tm;
		}

		public double getCumulativeTime() {
			return this.cumulativeTime;
		}

		public void setCumulativeTime(double ct) {
			this.cumulativeTime = ct;
		}

		public Tuple3d update(final Tuple3d tuple, final double dt) {
			double newX = this.dx(tuple, dt);
			double newY = this.dy(tuple, dt);
			double newZ = this.dz(tuple, dt);

			this.setCumulativeTime(this.getCumulativeTime() + dt);

			return new Tuple3d(newX, newY, newZ);
		}

		protected abstract double dx(final Tuple3d tuple, final double dt);

		protected abstract double dy(final Tuple3d tuple, final double dt);

		protected abstract double dz(final Tuple3d tuple, final double dt);

		public void draw(Graphics2D g2) {
			this.setSpace();
			this.drawAxes(g2);
			System.out.println("xmax= "+xmax+" xmin="+xmin+" xRange="+(xmax-xmin)+" xC="+(xmin+(xmax-xmin)/2));
			System.out.println("ymax= "+ymax+" ymin="+ymin+" yRange="+(ymax-ymin)+" yC="+(ymin+(ymax-ymin)/2));
			System.out.println("zmax= "+zmax+" zmin="+zmin+" zRange="+(zmax-zmin)+" zC="+(zmin+(zmax-zmin)/2));
			double dt = this.getTimeJump();//0.001;
			for (int i = 0; i < MAX_ITER ; i++) {
				Tuple3d existingTuple = this.getT3d();
//				System.out.println(i+" existing x["+existingTuple.x+"], y["+existingTuple.y+"], z["+existingTuple.z+"]");
				Tuple3d scaledExistingTuple = this.scale(existingTuple);
				Tuple3d updatedTuple = this.update(existingTuple/*this.t3d*/, dt);
//				System.out.println(i+" updated x["+updatedTuple.x+"], y["+updatedTuple.y+"], z["+updatedTuple.z+"]");
				
				Tuple3d scaledUpdatedTuple = this.scale(updatedTuple);
				
				this.setT3d(updatedTuple);
				
//				if(i<50){System.out.println(this.t3d);}

				g2.setColor(this.color);
//				System.out.println(i+" scaledExisting x["+scaledExistingTuple.x+"], y["+scaledExistingTuple.y+"], z["+scaledExistingTuple.z+"]");
//				System.out.println(i+" scaledUpdated x["+scaledUpdatedTuple.x+"], y["+scaledUpdatedTuple.y+"], z["+scaledUpdatedTuple.z+"]");
				
//				g2.drawLine((int) scaledExistingTuple.x, (int) scaledExistingTuple.y, (int) scaledUpdatedTuple.x, (int) scaledUpdatedTuple.y);
				g2.drawLine((int) scaledExistingTuple.x, (int) scaledExistingTuple.z, (int) scaledUpdatedTuple.x, (int) scaledUpdatedTuple.z);

				pause(2);
			}
System.out.println("Done"+System.currentTimeMillis());
		}
		
		private void setSpace() {
			this.determineRange();
		}

		protected abstract void determineRange();

		/*protected abstract void setSpace();*/

		private void drawAxes(Graphics2D g2) {
			// 4now doing x-z axis only
			int xCentr = WIDTH / 2;
//			int yCentr = HEIGHT / 2;
			int zCentr = DEPTH / 2;
			g2.setColor(Color.black);
			g2.drawLine(xCentr, 0, xCentr, HEIGHT);		//horizontal
			g2.drawLine(0, zCentr, DEPTH, zCentr);		//vertical(y-for-graph)
		}	

		protected Tuple3d scale(Tuple3d tuple) {
			double xVal = tuple.x;
			double yVal = tuple.y;
			double zVal = tuple.z;

			final double xCentr = WIDTH / 2;
			final double yCentr = HEIGHT / 2;
			final double zCentr = DEPTH / 2;
			
			final double xRange = (this.xmax - this.xmin);
			final double yRange = (this.ymax - this.ymin);
			final double zRange = (this.zmax - this.zmin);

//			...............x[5.958136900535692], y[259.7447851220958], z[-5.185551290427554]4blow
//			double x0 = ((xCentr + xVal) / WIDTH) + /*(xCentr * */(WIDTH*(xVal - this.xmin) / (this.xmax - this.xmin))/*)*/ - xCentr;//	x + (xCentr /*- WIDTH / 2*/);// + (WIDTH / 2 * x);
//			double y0 = ((yCentr + yVal) / HEIGHT) + /*(yCentr * */(HEIGHT*(yVal - this.ymin) / (this.ymax - this.ymin))/*)*/ - yCentr;//	y + (yCentr/* - HEIGHT / 2 */);// + (HEIGHT / 2 * y);
//			double z0 = ((zCentr + zVal) / DEPTH) + /*(zCentr * */(DEPTH*(zVal - this.zmin) / (this.zmax - this.zmin))/*)*/ - zCentr;//	z + (zCentr/* - DEPTH / 2 */);// + (DEPTH / 2 * z);
//			...............x[300.0], y[320.0], z[325.0]4blow
//			double x0 = xVal + (xCentr /*- WIDTH / 2*/);// + (WIDTH / 2 * x);
//			double y0 = yVal + (yCentr/* - HEIGHT / 2 */);// + (HEIGHT / 2 * y);
//			double z0 = zVal + (zCentr/* - DEPTH / 2 */);// + (DEPTH / 2 * z);
			
			/*JULIA
			double x0 = xc - size / 2 + size * row / n;
			double y0 = yc - size / 2 + size * col / n;*/
//			..........x[282.127220733809], y[527.782251511457], z[657.6217390140539]4blow.................
//			double x0 = xCentr - ((this.xmax - this.xmin)/2) + (xVal * (WIDTH / (this.xmax - this.xmin)));
//			double y0 = xCentr - ((this.ymax - this.ymin)/2) + (yVal * (HEIGHT / (this.ymax - this.ymin)));
//			double z0 = xCentr - ((this.zmax - this.zmin)/2) + (zVal * (DEPTH / (this.zmax - this.zmin)));
//				................x[300.0], y[551.6270634259937], z[677.4898242401819]4blow.........................	
//			double x0 = xCentr + (xVal * (WIDTH / xRange));
//			double y0 = yCentr + (yVal * (HEIGHT / yRange));
//			double z0 = zCentr + (zVal * (DEPTH / zRange));
			
			
			double x0 = (xVal-xmin) * (WIDTH / xRange);
			double y0 = (yVal-ymin) * (HEIGHT / yRange);
			double z0 = (zVal-zmin) * (DEPTH / zRange);
			
			
			
			return new Tuple3d(x0, y0, z0);
		}

		public void setName(String nme) {
			this.name = nme;
		}
		
		/**
	     * Sets the <em>x</em>-scale to the specified range.
	     *
	     * @param  min the minimum value of the <em>x</em>-scale
	     * @param  max the maximum value of the <em>x</em>-scale
	     * @throws IllegalArgumentException if {@code (max == min)}
	     */
	    public void setXscale(double min, double max) {
	        double size = max - min;
	        if (size == 0.0) throw new IllegalArgumentException("the min and max are the same");
//	        synchronized (mouseLock) {
	            xmin = min - 0.0/*BORDER*/ * size;
	            xmax = max + 0.0/*BORDER*/ * size;
//	        }
	    }

	    /**
	     * Sets the <em>y</em>-scale to the specified range.
	     *
	     * @param  min the minimum value of the <em>y</em>-scale
	     * @param  max the maximum value of the <em>y</em>-scale
	     * @throws IllegalArgumentException if {@code (max == min)}
	     */
	    public void setYscale(double min, double max) {
	        double size = max - min;
	        if (size == 0.0) throw new IllegalArgumentException("the min and max are the same");
//	        synchronized (mouseLock) {
	            ymin = min - 0.0/*BORDER*/ * size;
	            ymax = max + 0.0/*BORDER*/ * size;
//	        }
	    }
		
//		
//		
//		/**
//	     * Sets the <em>x</em>-scale and <em>y</em>-scale to be the default
//	     * (between 0.0 and 1.0).
//	     */
//	    public static void setScale() {
//	        setXscale();
//	        setYscale();
//	    }
//
//	    /**
//	     * Sets the <em>x</em>-scale to the specified range.
//	     *
//	     * @param  min the minimum value of the <em>x</em>-scale
//	     * @param  max the maximum value of the <em>x</em>-scale
//	     * @throws IllegalArgumentException if {@code (max == min)}
//	     */
//	    public static void setXscale(double min, double max) {
//	        double size = max - min;
//	        if (size == 0.0) throw new IllegalArgumentException("the min and max are the same");
//	        synchronized (mouseLock) {
//	            xmin = min - BORDER * size;
//	            xmax = max + BORDER * size;
//	        }
//	    }
//
//	    /**
//	     * Sets the <em>y</em>-scale to the specified range.
//	     *
//	     * @param  min the minimum value of the <em>y</em>-scale
//	     * @param  max the maximum value of the <em>y</em>-scale
//	     * @throws IllegalArgumentException if {@code (max == min)}
//	     */
//	    public static void setYscale(double min, double max) {
//	        double size = max - min;
//	        if (size == 0.0) throw new IllegalArgumentException("the min and max are the same");
//	        synchronized (mouseLock) {
//	            ymin = min - BORDER * size;
//	            ymax = max + BORDER * size;
//	        }
//	    }
//
//	    /**
//	     * Sets both the <em>x</em>-scale and <em>y</em>-scale to the (same) specified range.
//	     *
//	     * @param  min the minimum value of the <em>x</em>- and <em>y</em>-scales
//	     * @param  max the maximum value of the <em>x</em>- and <em>y</em>-scales
//	     * @throws IllegalArgumentException if {@code (max == min)}
//	     */
//	    public static void setScale(double min, double max) {
//	        double size = max - min;
//	        if (size == 0.0) throw new IllegalArgumentException("the min and max are the same");
//	        synchronized (mouseLock) {
//	            xmin = min - BORDER * size;
//	            xmax = max + BORDER * size;
//	            ymin = min - BORDER * size;
//	            ymax = max + BORDER * size;
//	        }
//	    }
//
	    // helper functions that scale from user coordinates to screen coordinates and back
	   /* private double  scaleX(double x) { return WIDTH  * (x - xmin) / (xmax - xmin); }
	    private double  scaleY(double y) { return HEIGHT * (ymax - y) / (ymax - ymin); }*/
//	    private static double factorX(double w) { return w * width  / Math.abs(xmax - xmin);  }
//	    private static double factorY(double h) { return h * height / Math.abs(ymax - ymin);  }
//	    private static double   userX(double x) { return xmin + x * (xmax - xmin) / width;    }
//	    private static double   userY(double y) { return ymax - y * (ymax - ymin) / height;   }
//

	}
	
	
	class AizawaAttractor extends Attractor {
		private final double a = 0.95;
		private final double b = 0.7;
		private final double c = 0.9;
		private final double d = 3.5;
		private final double e = 0.25;
		private final double f = 0.1;

		public AizawaAttractor(double x, double y, double z, Color c) {
			super(x, y, z, c);
			this.setName("aizawa");
		}

		@Override
		protected double dx(Tuple3d tuple, double dt) {
			return tuple.x + (dt * (((tuple.z - b) * tuple.x) - (tuple.x - (d * tuple.y))));
		}

		@Override
		protected double dy(Tuple3d tuple, double dt) {
			return tuple.y + (dt * ((d * tuple.x) + ((tuple.z - b) * tuple.y)));
		}

		@Override
		protected double dz(Tuple3d tuple, double dt) {
			return tuple.z + (dt * (
					c + (tuple.z * a) - (Math.pow(tuple.z, 3) / (3 * d))
					- (Math.pow(tuple.x, 2) + Math.pow(tuple.y, 2)) * (1 + (e * tuple.z))
					+ (f * tuple.z * Math.pow(tuple.x, 3))
					)
				);
		}

		@Override
		protected void determineRange() {
		}

	}
	
	
	class LorenzAttractor extends Attractor {
		
		private final double a_prandt 	= 10.0;					//sigma=====10
	    private final double b_rayleigh = 8.0/3;//28.0;			//beta======8/3	
	    private final double c_rho 		= 28.0;//8.0/3;		//rho=======28		0<c_rho < Double.infinity

		public LorenzAttractor(double x, double y, double z, Color c) {
			super(x, y, z, c);
			this.setName("lorenz");
		}

		@Override
		protected double dx(final Tuple3d tuple, final double dt) {
			//princeton		dx= -10*(x - y);
			return tuple.x + (((-1/*0*/ * this.a_prandt) * (tuple.x - tuple.y)) * dt);
		}

		@Override
		protected double dy(final Tuple3d tuple, final double dt) {
			//princeton		dy=		-x*z + 28*x - y;
//			return tuple.y + ((((-1 * tuple.x * tuple.z) + ((this.b_rayleigh * tuple.x) - tuple.y))) * dt);
			return tuple.y + ((((-1 * tuple.x * tuple.z) + ((this.c_rho * tuple.x) - tuple.y))) * dt);
		}

		@Override
		protected double dz(final Tuple3d tuple, final double dt) {
			//princeton		dz=		x*y - 8*z/3;
//			return tuple.z + ((tuple.x * tuple.y - (tuple.z * this.c_rho)) * dt);
			return tuple.z + (((tuple.x * tuple.y) - (tuple.z * this.b_rayleigh)) * dt);
		}

		@Override
		protected void determineRange() {
			// do-the-calculations (it-does-get-repeated-while-displaying
			// determine min-max values
			double x_min_r = Double.POSITIVE_INFINITY;
			double x_max_r = Double.NEGATIVE_INFINITY;
			double y_min_r = Double.POSITIVE_INFINITY;
			double y_max_r = Double.NEGATIVE_INFINITY;
			double z_min_r = Double.POSITIVE_INFINITY;
			double z_max_r = Double.NEGATIVE_INFINITY;
			
			final Tuple3d extistingDummyTuple = this.getT3d();
			
			Tuple3d tempDummyTuple = this.getT3d();
			x_min_r = tempDummyTuple.x < x_min_r ? tempDummyTuple.x : x_min_r;
			x_max_r = tempDummyTuple.x > x_max_r ? tempDummyTuple.x : x_max_r;			

			y_min_r = tempDummyTuple.y < y_min_r ? tempDummyTuple.y : y_min_r;
			y_max_r = tempDummyTuple.y > y_max_r ? tempDummyTuple.y : y_max_r;			

			z_min_r = tempDummyTuple.z < z_min_r ? tempDummyTuple.z : z_min_r;
			z_max_r = tempDummyTuple.z > z_max_r ? tempDummyTuple.z : z_max_r;
			
			final double dt = this.getTimeJump();
			
			for (int i = 0; i < MAX_ITER; i++) {
				Tuple3d existingTuple = tempDummyTuple;
				Tuple3d updatedTuple = this.update(existingTuple, dt);

				tempDummyTuple = updatedTuple;

				x_min_r = tempDummyTuple.x < x_min_r ? tempDummyTuple.x : x_min_r;
				x_max_r = tempDummyTuple.x > x_max_r ? tempDummyTuple.x : x_max_r;
				y_min_r = tempDummyTuple.y < y_min_r ? tempDummyTuple.y : y_min_r;
				y_max_r = tempDummyTuple.y > y_max_r ? tempDummyTuple.y : y_max_r;
				z_min_r = tempDummyTuple.z < z_min_r ? tempDummyTuple.z : z_min_r;
				z_max_r = tempDummyTuple.z > z_max_r ? tempDummyTuple.z : z_max_r;
			}
			
			
			this.xmin = x_min_r;//-20.0;
			this.xmax = x_max_r;//20.0;

			this.ymin = y_min_r;//-40.0;
			this.ymax = y_max_r;//40.0;

			this.zmin = z_min_r;//00;
			this.zmax = z_max_r;//50.0;
			
			//revert
			this.setT3d(extistingDummyTuple);
			this.setCumulativeTime(0.0);

		}
		
	}
	
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public static final int DEPTH = 600;

	private String attractor;

	public Lorenz_Attractor() {

	}

	public Lorenz_Attractor(String p) {
		this.setSize(WIDTH, HEIGHT);
		this.attractor = p;
	}
	


	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g1) {
		super.paint(g1);
		Graphics2D g2 = (Graphics2D) g1;
		this.draw(g2);
	}

	private void draw(Graphics2D g) {
		if (this.attractor.equals("lorenz")) {	// seed l1 & l2
			Attractor l1 = new LorenzAttractor(0.0, 20.00, 25.0, Color.blue);
			Attractor l2 = new LorenzAttractor(0.0, 20.01, 25.0, Color.red);
			
			l1.draw(g);
			l2.draw(g);
		} else if (this.attractor.equals("aizawa")){
			Attractor a = new AizawaAttractor(0.1, 0, 0, Color.red);
			a.draw(g);
		}
	}
	
	
	/**
     * Pause for t milliseconds. This method is intended to support computer animations.
     * @param t number of milliseconds
     */
    public static void pause(int t) {
        try {
            Thread.sleep(t);
        }
        catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new Lorenz_Attractor("lorenz");//("aizawa");//
				frame.setTitle("BawazAttractor");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});

	}

}
