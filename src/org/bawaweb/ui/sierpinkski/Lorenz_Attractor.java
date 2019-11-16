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
		private Tuple3d t3d;
		private Color color;
		private double time;
		private String name;

		public Attractor(double x, double y, double z, Color c) {
			this.t3d = new Tuple3d(x, y, z);
			this.color = c;
		}

		public Attractor(Tuple3d tuple, Color col) {
			this(tuple.x, tuple.y, tuple.z, col);
		}

		public Tuple3d update(Tuple3d tuple, double dt) {
			double newX = this.dx(tuple, dt);
			double newY = this.dy(tuple, dt);
			double newZ = this.dz(tuple, dt);

			this.setTime(this.getTime() + dt);

			return new Tuple3d(newX, newY, newZ);
		}

		protected abstract double dx(final Tuple3d tuple, final double dt);

		protected abstract double dy(final Tuple3d tuple, final double dt);

		protected abstract double dz(final Tuple3d tuple, final double dt);

		public double getTime() {
			return this.time;
		}

		public void setTime(double tm) {
			this.time = tm;
		}

		public void draw(Graphics2D g2) {
			this.drawAxes(g2);
			double dt = 0.001;
			for (int i = 0; i < 50000 ; i++) {
				Tuple3d existingTuple = this.t3d;
				Tuple3d scaledExistingTuple = this.scale(existingTuple);
				Tuple3d updatedTuple = this.update(this.t3d, dt);
				Tuple3d scaledUpdatedTuple = this.scale(updatedTuple);
				
				this.t3d = updatedTuple;
				
//				if(i<50){System.out.println(this.t3d);}

				g2.setColor(this.color);
				g2.drawLine((int) scaledExistingTuple.x, (int) scaledExistingTuple.y, (int) scaledUpdatedTuple.x, (int) scaledUpdatedTuple.y);
			}

		}

		private void drawAxes(Graphics2D g2) {
			int xCentr = WIDTH / 2;
			int yCentr = HEIGHT / 2;
			int zCentr = DEPTH / 2;
			g2.setColor(Color.black);
			g2.drawLine(xCentr, 0, xCentr, HEIGHT);
			g2.drawLine(0, yCentr, WIDTH, yCentr);
		}

		private Tuple3d scale(Tuple3d tuple) {
			/*
			 * private double scaleX(double x) { return WIDTH * (x - xmin) / (xmax - xmin); } 
			 * private double scaleY(double y) { return HEIGHT (ymax - y) / (ymax - ymin); }
			 */
			double x = tuple.x;
			double y = tuple.y;
			double z = tuple.z;

			double xCentr = WIDTH / 2;
			double yCentr = HEIGHT / 2;
			double zCentr = DEPTH / 2;

			final double xmin = -100d;
			final double xmax = 100d;
			final double ymin = xmin;
			final double ymax = xmax;
			final double zmin = xmin;
			final double zmax = xmax;

			double x0 = WIDTH * (x - xmin) / (xmax - xmin);//	x + (xCentr /*- WIDTH / 2*/);// + (WIDTH / 2 * x);
			double y0 = HEIGHT * (y - ymin) / (ymax - ymin);//	y + (yCentr/* - HEIGHT / 2 */);// + (HEIGHT / 2 * y);
			double z0 = DEPTH * (z - zmin) / (zmax - zmin);//	z + (zCentr/* - DEPTH / 2 */);// + (DEPTH / 2 * z);

			/* return new Tuple3d(x + xCentr, y + yCentr, z + zCentr); */
			return new Tuple3d(x0, y0, z0);
		}

		public void setName(String nme) {
			this.name = nme;
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

	}
	
	
	class LorenzAttractor extends Attractor {
		
		private final double a_prandt 	= 10.0;			//sigma=====10
	    private final double b_rayleigh = 28.0;			//beta======28	
	    private final double c_rho 		= 8.0/3;		//rho=======8/3

		public LorenzAttractor(double x, double y, double z, Color c) {
			super(x, y, z, c);
			this.setName("lorenz");
		}

		@Override
		protected double dx(final Tuple3d tuple, final double dt) {
			return tuple.x + (((-10 * this.a_prandt) * (tuple.x - tuple.y)) * dt);
		}

		@Override
		protected double dy(final Tuple3d tuple, final double dt) {
			return tuple.y + ((((-1 * tuple.x * tuple.z) + ((this.b_rayleigh * tuple.x) - tuple.y))) * dt);
		}

		@Override
		protected double dz(final Tuple3d tuple, final double dt) {
			return tuple.z + ((tuple.x * tuple.y - (tuple.z * this.c_rho)) * dt);
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

	private void draw(Graphics2D g2) {
		if (this.attractor.equals("lorenz")) {
			Attractor l1 = new LorenzAttractor(0.0, 20.00, 25.0, Color.blue);
			Attractor l2 = new LorenzAttractor(0.0, 20.01, 25.0, Color.green);
			
			l1.draw(g2);
			l2.draw(g2);
		} else if (this.attractor.equals("aizawa")){
			Attractor a = new AizawaAttractor(0.1, 0, 0, Color.red);
			a.draw(g2);
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
