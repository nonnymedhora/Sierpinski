package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JFrame;

public abstract class Attractor /*extends JFrame */{
/**
		 * 
		 */
		/*private final Lorenz_Attractor lorenz_Attractor;*/
		//		public static final int MAX_ITER = 50000;
	private Tuple3d t3d;
	private Color color;
	private int maxIter;
	private double timeJump;
	private double cumulativeTime;
	private String name;

	protected double xmin;// = -100d;
	protected double xmax;// = 100d;
	protected double ymin;// = xmin;
	protected double ymax;// = xmax;
	protected double zmin;// = xmin;
	protected double zmax;// = xmax;

	private String space2d = "x-z";
	private Map<Integer,Tuple3d> updatedTuples = new LinkedHashMap<Integer,Tuple3d>();

	public Attractor(double x, double y, double z, Color c) {
		this.setT3d(new Tuple3d(x, y, z));
		this.setTimeJump(0.01); // TODO externalize
		this.color = c;
		/* this.setSpace(); */
		this.space2d = "x-z";
	}

	public Attractor(double x, double y, double z, Color c, String dspace) {
		this(x, y, z, c);
		this.space2d = dspace;
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

	public void setSpace2d(String spc2d) {
		this.space2d = spc2d;
	}

	public Tuple3d update(final Tuple3d tuple, final double dt) {
		double newX = this.dx(tuple, dt);
		double newY = this.dy(tuple, dt);
		double newZ = this.dz(tuple, dt);

		this.setCumulativeTime(this.getCumulativeTime() + dt);

		return new Tuple3d(newX, newY, newZ);
	}

	public int getMaxIter() {
		return this.maxIter;
	}

	public void setMaxIter(int m) {
		this.maxIter = m;
	}

	protected abstract double dx(final Tuple3d tuple, final double dt);

	protected abstract double dy(final Tuple3d tuple, final double dt);

	protected abstract double dz(final Tuple3d tuple, final double dt);
	
	public void draw(int index, Graphics2D g2) {
		Tuple3d existingTuple = null, scaledExistingTuple = null, 
				updatedTuple = null, scaledUpdatedTuple = null;
		if (index != 0) {
			existingTuple = this.updatedTuples.get(index - 1);
			updatedTuple = this.updatedTuples.get(index);

			if (existingTuple != null && updatedTuple != null) {
				scaledExistingTuple = this.scale(existingTuple);
				this.doScaleCheck(scaledExistingTuple);
				scaledUpdatedTuple = this.scale(updatedTuple);
				this.doScaleCheck(scaledUpdatedTuple);
			}

		} else {
			existingTuple = this.t3d;
			updatedTuple = this.updatedTuples.get(0);

			scaledExistingTuple = this.scale(existingTuple);
			this.doScaleCheck(scaledExistingTuple);

			scaledUpdatedTuple = this.scale(updatedTuple);
			this.doScaleCheck(scaledUpdatedTuple);
		}
		
		if (scaledExistingTuple != null && scaledUpdatedTuple != null) {
			g2.setColor(this.color);
			drawLine(g2, scaledExistingTuple, scaledUpdatedTuple);
		}
	}
	
//
//	//////////////////////removing--paint././//////////////////////
//	public void draw(Graphics2D g2) {
//		/*this.setSpace();
//		this.drawAxes(g2);*/
//		/*System.out.println("xmax= "+xmax+" xmin="+xmin+" xRange="+(xmax-xmin)+" xC="+(xmin+(xmax-xmin)/2));
//		System.out.println("ymax= "+ymax+" ymin="+ymin+" yRange="+(ymax-ymin)+" yC="+(ymin+(ymax-ymin)/2));
//		System.out.println("zmax= "+zmax+" zmin="+zmin+" zRange="+(zmax-zmin)+" zC="+(zmin+(zmax-zmin)/2));*/
//		double dt = this.getTimeJump();//0.001;
//		/*for (int i = 0; i < MAX_ITER ; i++) {*/
//			Tuple3d existingTuple = this.getT3d();
////				System.out.println(/*i+*/" existing x["+existingTuple.x+"], y["+existingTuple.y+"], z["+existingTuple.z+"]");
//			Tuple3d scaledExistingTuple = this.scale(existingTuple);
//			Tuple3d updatedTuple = this.update(existingTuple/*this.t3d*/, dt);
////				System.out.println(/*i+*/" updated x["+updatedTuple.x+"], y["+updatedTuple.y+"], z["+updatedTuple.z+"]");
//			
//			Tuple3d scaledUpdatedTuple = this.scale(updatedTuple);
//			this.doScaleCheck(scaledExistingTuple);
//			this.doScaleCheck(scaledUpdatedTuple);
//			this.setT3d(updatedTuple);
//			
////				if(i<50){System.out.println(this.t3d);}
//
//			g2.setColor(this.color);
////				System.out.println(/*i+*/" scaledExisting x["+scaledExistingTuple.x+"], y["+scaledExistingTuple.y+"], z["+scaledExistingTuple.z+"]");
////				System.out.println(/*i+*/" scaledUpdated x["+scaledUpdatedTuple.x+"], y["+scaledUpdatedTuple.y+"], z["+scaledUpdatedTuple.z+"]");
//			
//		drawLine(g2, scaledExistingTuple, scaledUpdatedTuple);
//	}
//	//////////////////ends-removing-paint-4now////////////////////
//	
	
	private void drawLine(Graphics2D g2, Tuple3d scaledExistingTuple, Tuple3d scaledUpdatedTuple) {
		if (this.space2d.equals("x-z")) {
			g2.drawLine(
					(int) scaledExistingTuple.x, (int) scaledExistingTuple.z, 
					(int) scaledUpdatedTuple.x, (int) scaledUpdatedTuple.z);
		} else if (this.space2d.equals("x-y")) {
			g2.drawLine(
					(int) scaledExistingTuple.x, (int) scaledExistingTuple.y, 
					(int) scaledUpdatedTuple.x, (int) scaledUpdatedTuple.y);
		} else if (this.space2d.equals("y-z")) {
			g2.drawLine(
					(int) scaledExistingTuple.y, (int) scaledExistingTuple.z, 
					(int) scaledUpdatedTuple.y, (int) scaledUpdatedTuple.z);
		} else if (this.space2d.equals("z-x")) {
			g2.drawLine(
					(int) scaledExistingTuple.z, (int) scaledExistingTuple.x, 
					(int) scaledUpdatedTuple.z, (int) scaledUpdatedTuple.x);
		} else if (this.space2d.equals("y-x")) {
			g2.drawLine(
					(int) scaledExistingTuple.y, (int) scaledExistingTuple.x, 
					(int) scaledUpdatedTuple.y, (int) scaledUpdatedTuple.x);
		} else if (this.space2d.equals("z-y")) {
			g2.drawLine(
					(int) scaledExistingTuple.z, (int) scaledExistingTuple.y, 
					(int) scaledUpdatedTuple.z, (int) scaledUpdatedTuple.y);
		}
	}
	
	private void doScaleCheck(Tuple3d t) {
		boolean xOk = t.x <= AttractorsGenerator.WIDTH || t.x >= 0;
		boolean yOk = t.y <= AttractorsGenerator.HEIGHT || t.y >= 0;
		boolean zOk = t.z <= AttractorsGenerator.DEPTH || t.z >= 0;
		
		
		if(!(xOk||yOk||zOk))
			System.out.println("UhOhOttaScale  x["+t.x+"], y["+t.y+"], z["+t.z+"]");

	}

	void setSpace() {
		this.determineRange();
	}

	void drawAxes(Graphics2D g2) {
		// 4now doing x-z axis only
		int xCentr = AttractorsGenerator.WIDTH / 2;
		int yCentr = AttractorsGenerator.HEIGHT / 2;
		int zCentr = AttractorsGenerator.DEPTH / 2;
		g2.setColor(Color.black);
		
		if (this.space2d.equals("x-z")) {
			g2.drawLine(xCentr, 0, xCentr, AttractorsGenerator.WIDTH); //horizontal
			g2.drawLine(0, zCentr, AttractorsGenerator.DEPTH, zCentr); //vertical(y-for-graph)
		} else if (this.space2d.equals("x-y")) {
			g2.drawLine(xCentr, 0, xCentr, AttractorsGenerator.WIDTH); //horizontal
			g2.drawLine(0, yCentr, AttractorsGenerator.HEIGHT, yCentr); //vertical(y-for-graph)
		} else if (this.space2d.equals("y-z")) {
			g2.drawLine(yCentr, 0, yCentr, AttractorsGenerator.HEIGHT); //horizontal
			g2.drawLine(0, zCentr, AttractorsGenerator.DEPTH, zCentr); //vertical(y-for-graph)
		} else if (this.space2d.equals("z-x")) {
			g2.drawLine(zCentr, 0, zCentr, AttractorsGenerator.DEPTH); //horizontal
			g2.drawLine(0, xCentr, AttractorsGenerator.WIDTH, xCentr); //vertical(y-for-graph)
		} else if (this.space2d.equals("y-x")) {
			g2.drawLine(yCentr, 0, yCentr, AttractorsGenerator.HEIGHT); //horizontal
			g2.drawLine(0, xCentr, AttractorsGenerator.WIDTH, xCentr); //vertical(y-for-graph)
		} else if (this.space2d.equals("z-y")) {
			g2.drawLine(zCentr, 0, zCentr, AttractorsGenerator.DEPTH); //horizontal
			g2.drawLine(0, yCentr, AttractorsGenerator.HEIGHT, yCentr); //vertical(y-for-graph)
		}
	}	

	protected Tuple3d scale(Tuple3d tuple) {
		double xVal = tuple.x;
		double yVal = tuple.y;
		double zVal = tuple.z;

		/*final double xCentr = WIDTH / 2;
		final double yCentr = HEIGHT / 2;
		final double zCentr = DEPTH / 2;*/
		
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
		
		
		double x0 = /*Math.round(*/(xVal-xmin) * (AttractorsGenerator.WIDTH / xRange)/*)*/;
		double y0 = (yVal-ymin) * (AttractorsGenerator.HEIGHT / yRange);
		double z0 = (zVal-zmin) * (AttractorsGenerator.DEPTH / zRange);
		
		
		
		return new Tuple3d(x0, y0, z0);
	}

	public void setName(String nme) {
		this.name = nme;
	}

/////////////////////ends---removed---xscale,yscale/////////////////
//	/**
//     * Sets the <em>x</em>-scale to the specified range.
//     *
//     * @param  min the minimum value of the <em>x</em>-scale
//     * @param  max the maximum value of the <em>x</em>-scale
//     * @throws IllegalArgumentException if {@code (max == min)}
//     */
//    public void setXscale(double min, double max) {
//        double size = max - min;
//        if (size == 0.0) throw new IllegalArgumentException("the min and max are the same");
////	        synchronized (mouseLock) {
//            xmin = min - 0.0/*BORDER*/ * size;
//            xmax = max + 0.0/*BORDER*/ * size;
////	        }
//    }
//
//    /**
//     * Sets the <em>y</em>-scale to the specified range.
//     *
//     * @param  min the minimum value of the <em>y</em>-scale
//     * @param  max the maximum value of the <em>y</em>-scale
//     * @throws IllegalArgumentException if {@code (max == min)}
//     */
//    public void setYscale(double min, double max) {
//        double size = max - min;
//        if (size == 0.0) throw new IllegalArgumentException("the min and max are the same");
////	        synchronized (mouseLock) {
//            ymin = min - 0.0/*BORDER*/ * size;
//            ymax = max + 0.0/*BORDER*/ * size;
////	        }
//    }
/////////////////////ends---removed---xscale,yscale/////////////////
	
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
		
		for (int i = 0; i < this.maxIter; i++) {
			Tuple3d existingTuple = tempDummyTuple;
			Tuple3d updatedTuple = this.update(existingTuple, dt);

			this.addUpdatedTuples(i, existingTuple, updatedTuple);
	
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
	
	private void addUpdatedTuples(int index, Tuple3d existingTuple, Tuple3d updatedTuple) {
		this.updatedTuples.put(index,updatedTuple);
		
		
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