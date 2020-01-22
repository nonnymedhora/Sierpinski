package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Attractor {
	
	private Tuple3d t3d;
	private Color color;
	private int maxIter;
	protected boolean isTimeInvariant;
	private double timeJump = 0.01;
	private double cumulativeTime;
	private String name;
	private boolean is3D = true;
	String space2dAxes;
	private Map<Integer,Tuple3d> updatedTuples = new LinkedHashMap<Integer,Tuple3d>();
	private boolean isPixellated = false;		//	Fractal rendered by lines, or pixels

	public Attractor(double x, double y, double z, Color c) {
		this.setT3d(new Tuple3d(x, y, z));
		this.setTimeJump(this.getTimeJump());
		this.color = c;
		/*this.space2dAxes = "x-z";*/
	}

	public Attractor(double x, double y, double z, Color c, String dspace) {
		this(x, y, z, c);
		this.space2dAxes = dspace;
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

	public void setSpace2dAxes(String spc2d) {
		this.space2dAxes = spc2d;
	}

	public boolean is3D() {
		return this.is3D;
	}

	public void setIs3D(boolean is3d) {
		this.is3D = is3d;
	}

	public String getName() {
		return name;
	}

	public boolean isPixellated() {
		return this.isPixellated;
	}

	public void setPixellated(boolean toPixellate) {
		this.isPixellated = toPixellate;
	}

	public boolean isTimeInvariant() {
		return this.isTimeInvariant;
	}

	public void setTimeInvariant(boolean isInvariant) {
		this.isTimeInvariant = isInvariant;
	}

	public int getMaxIter() {
		return this.maxIter;
	}

	public void setMaxIter(int m) {
		this.maxIter = m;
	}

	public Tuple3d update(final Tuple3d tuple, final double dt) {
		double newX = this.dx(tuple, dt);
		double newY = this.dy(tuple, dt);
		double newZ = Double.NaN;
		if (this.is3D) {
			newZ = this.dz(tuple, dt);
		}

		if (!this.isTimeInvariant) {
			this.setCumulativeTime(this.getCumulativeTime() + dt);
		}
		
		if (this.is3D) {
			return new Tuple3d(newX, newY, newZ);
		} else {
			return new Tuple3d(newX, newY, 0);
		}
	}

	protected abstract double dx(final Tuple3d tuple, final double dt);

	protected abstract double dy(final Tuple3d tuple, final double dt);

	protected abstract double dz(final Tuple3d tuple, final double dt);
	
	public void draw(int index, Graphics2D g2, final Map<String, Double> spaceMap) {
		Tuple3d existingTuple = null, scaledExistingTuple = null, 
				updatedTuple = null, scaledUpdatedTuple = null;
		if (index != 0) {
			existingTuple = this.updatedTuples.get(index - 1);
			updatedTuple = this.updatedTuples.get(index);

			if (existingTuple != null && updatedTuple != null) {
				scaledExistingTuple = this.scale(existingTuple,spaceMap);
				this.doScaleCheck(scaledExistingTuple);
				scaledUpdatedTuple = this.scale(updatedTuple,spaceMap);
				this.doScaleCheck(scaledUpdatedTuple);
			}

		} else {
			existingTuple = this.t3d;
			updatedTuple = this.updatedTuples.get(0);

			scaledExistingTuple = this.scale(existingTuple,spaceMap);
			this.doScaleCheck(scaledExistingTuple);

			scaledUpdatedTuple = this.scale(updatedTuple,spaceMap);
			this.doScaleCheck(scaledUpdatedTuple);
		}
		
		if (scaledExistingTuple != null && scaledUpdatedTuple != null) {
			g2.setColor(this.color);
//			System.out.println("Attractor[draw] space2d is = "+this.space2dAxes);
			
			if (this.isPixellated) {
				drawRect(g2, scaledExistingTuple, scaledUpdatedTuple);
			} else {
				drawLine(g2, scaledExistingTuple, scaledUpdatedTuple);
			}
		}
		
/*
		if (!this.isInstantDraw) {
			pause(10);
		}*/
	}
	
	private void drawRect(Graphics2D g, Tuple3d existing, Tuple3d updated) {
		if (this.space2dAxes.equals("x-z")) {
			g.drawRect((int) existing.x, (int) existing.z, 0, 0);
			g.drawRect((int) updated.x, (int) updated.z, 0, 0);
		} else if (this.space2dAxes.equals("x-y")) {
			g.drawRect((int) existing.x, (int) existing.y, 0, 0);
			g.drawRect((int) updated.x, (int) updated.y, 0, 0);
		} else if (this.space2dAxes.equals("y-z")) {
			g.drawRect((int) existing.y, (int) existing.z, 0, 0);
			g.drawRect((int) updated.y, (int) updated.z, 0, 0);
		} else if (this.space2dAxes.equals("z-x")) {
			g.drawRect((int) existing.z, (int) existing.x, 0, 0);
			g.drawRect((int) updated.z, (int) updated.x, 0, 0);
		} else if (this.space2dAxes.equals("y-x")) {
			g.drawRect((int) existing.y, (int) existing.x, 0, 0);
			g.drawRect((int) updated.y, (int) updated.x, 0, 0);
		} else if (this.space2dAxes.equals("z-y")) {
			g.drawRect((int) existing.z, (int) existing.y, 0, 0);
			g.drawRect((int) updated.z, (int) updated.y, 0, 0);
		}
	}

	private void drawLine(Graphics2D g2, Tuple3d scaledExistingTuple, Tuple3d scaledUpdatedTuple) {
		if (this.space2dAxes.equals("x-z")) {
			g2.drawLine(
					(int) scaledExistingTuple.x, (int) scaledExistingTuple.z, 
					(int) scaledUpdatedTuple.x, (int) scaledUpdatedTuple.z);
		} else if (this.space2dAxes.equals("x-y")) {
			g2.drawLine(
					(int) scaledExistingTuple.x, (int) scaledExistingTuple.y, 
					(int) scaledUpdatedTuple.x, (int) scaledUpdatedTuple.y);
		} else if (this.space2dAxes.equals("y-z")) {
			g2.drawLine(
					(int) scaledExistingTuple.y, (int) scaledExistingTuple.z, 
					(int) scaledUpdatedTuple.y, (int) scaledUpdatedTuple.z);
		} else if (this.space2dAxes.equals("z-x")) {
			g2.drawLine(
					(int) scaledExistingTuple.z, (int) scaledExistingTuple.x, 
					(int) scaledUpdatedTuple.z, (int) scaledUpdatedTuple.x);
		} else if (this.space2dAxes.equals("y-x")) {
			g2.drawLine(
					(int) scaledExistingTuple.y, (int) scaledExistingTuple.x, 
					(int) scaledUpdatedTuple.y, (int) scaledUpdatedTuple.x);
		} else if (this.space2dAxes.equals("z-y")) {
			g2.drawLine(
					(int) scaledExistingTuple.z, (int) scaledExistingTuple.y, 
					(int) scaledUpdatedTuple.z, (int) scaledUpdatedTuple.y);
		}
	}
	
	private void doScaleCheck(Tuple3d t) {
		boolean xOk = t.x <= AttractorsGenerator.WIDTH || t.x >= 0;
		boolean yOk = t.y <= AttractorsGenerator.HEIGHT || t.y >= 0;
		if (this.is3D) {
			boolean zOk = t.z <= AttractorsGenerator.DEPTH || t.z >= 0;
			if (!(xOk || yOk || zOk))
				System.out.println("UhOhOttaScale  x[" + t.x + "], y[" + t.y + "], z[" + t.z + "]");
		} else {
			if (!(xOk || yOk))
				System.out.println("UhOhOttaScale  x[" + t.x + "], y[" + t.y + "]");
		}

	}

	Map<String, Double> setSpace() {
		return this.determineRange();
	}

	void drawAxes(Graphics2D g2) {
		int xCentr = AttractorsGenerator.WIDTH / 2;
		int yCentr = AttractorsGenerator.HEIGHT / 2;
		int zCentr=Integer.MIN_VALUE;
		if (this.is3D) {
			zCentr = AttractorsGenerator.DEPTH / 2;
		}
		g2.setColor(Color.black);
		
		if (this.space2dAxes.equals("x-z")) {
			g2.drawLine(xCentr, 0, xCentr, AttractorsGenerator.WIDTH); //horizontal
			g2.drawLine(0, zCentr, AttractorsGenerator.DEPTH, zCentr); //vertical(y-for-graph)
		} else if (this.space2dAxes.equals("x-y")) {
			g2.drawLine(xCentr, 0, xCentr, AttractorsGenerator.WIDTH); //horizontal
			g2.drawLine(0, yCentr, AttractorsGenerator.HEIGHT, yCentr); //vertical(y-for-graph)
		} else if (this.space2dAxes.equals("y-z")) {
			g2.drawLine(yCentr, 0, yCentr, AttractorsGenerator.HEIGHT); //horizontal
			g2.drawLine(0, zCentr, AttractorsGenerator.DEPTH, zCentr); //vertical(y-for-graph)
		} else if (this.space2dAxes.equals("z-x")) {
			g2.drawLine(zCentr, 0, zCentr, AttractorsGenerator.DEPTH); //horizontal
			g2.drawLine(0, xCentr, AttractorsGenerator.WIDTH, xCentr); //vertical(y-for-graph)
		} else if (this.space2dAxes.equals("y-x")) {
			g2.drawLine(yCentr, 0, yCentr, AttractorsGenerator.HEIGHT); //horizontal
			g2.drawLine(0, xCentr, AttractorsGenerator.WIDTH, xCentr); //vertical(y-for-graph)
		} else if (this.space2dAxes.equals("z-y")) {
			g2.drawLine(zCentr, 0, zCentr, AttractorsGenerator.DEPTH); //horizontal
			g2.drawLine(0, yCentr, AttractorsGenerator.HEIGHT, yCentr); //vertical(y-for-graph)
		}
	}	

	protected Tuple3d scale(Tuple3d tuple, final Map<String, Double> spaceMap) {
		double xVal = tuple.x;
		double yVal = tuple.y;
		double zVal = Double.NaN;
		
		if (this.is3D) {
			zVal = tuple.z;
		}

		Double xminVal = spaceMap.get("xmin");
		Double yminVal = spaceMap.get("ymin");
		Double zminVal = Double.NaN;
		
		if (this.is3D) {
			zminVal = spaceMap.get("zmin");
		}
		
		final double xRange = (spaceMap.get("xmax") - xminVal);// (this.xmax -  this.xmin);
		final double yRange = (spaceMap.get("ymax") - yminVal);
		double zRange = Double.NaN;
		
		if (this.is3D) {
			zRange = (spaceMap.get("zmax") - zminVal);
		}
		
		double x0 = /*Math.round(*/(xVal-xminVal) * (AttractorsGenerator.WIDTH / xRange)/*)*/;
		double y0 = (yVal-yminVal) * (AttractorsGenerator.HEIGHT / yRange);
		double z0 = Double.NaN;
		
		if (this.is3D) {
			z0 = (zVal - zminVal) * (AttractorsGenerator.DEPTH / zRange);
		}
		
		if (this.is3D) {
			return new Tuple3d(x0, y0, z0);
		} else {
			return new Tuple3d(x0, y0, 0);
		}
	}

	public void setName(String nme) {
		this.name = nme;
	}

	
	protected Map<String, Double> determineRange() {
		// determines range
		// and puts tuples into map
		// for future display
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
	
		if (this.is3D) {
			z_min_r = tempDummyTuple.z < z_min_r ? tempDummyTuple.z : z_min_r;
			z_max_r = tempDummyTuple.z > z_max_r ? tempDummyTuple.z : z_max_r;
		}

		final double dt = this.isTimeInvariant ? 0 : this.getTimeJump();
		
		Map<String, Double> spaceMap = new HashMap<String, Double>();
		
		for (int i = 0; i < this.maxIter; i++) {
			Tuple3d existingTuple = tempDummyTuple;

			if (! (Double.isNaN(existingTuple.x) || Double.isNaN(existingTuple.y) || Double.isNaN(existingTuple.z) )) {
				Tuple3d updatedTuple = this.update(existingTuple, dt);
				
				if (Double.isNaN(updatedTuple.x) || Double.isNaN(updatedTuple.y) || Double.isNaN(updatedTuple.z)) {
					return spaceMap;
				}

				this.addUpdatedTuples(i, existingTuple, updatedTuple);
				
				tempDummyTuple = updatedTuple;
				
				x_min_r = tempDummyTuple.x < x_min_r ? tempDummyTuple.x : x_min_r;
				x_max_r = tempDummyTuple.x > x_max_r ? tempDummyTuple.x : x_max_r;
				y_min_r = tempDummyTuple.y < y_min_r ? tempDummyTuple.y : y_min_r;
				y_max_r = tempDummyTuple.y > y_max_r ? tempDummyTuple.y : y_max_r;
				
				if (this.is3D) {
					z_min_r = tempDummyTuple.z < z_min_r ? tempDummyTuple.z : z_min_r;
					z_max_r = tempDummyTuple.z > z_max_r ? tempDummyTuple.z : z_max_r;
				}
				
			} else {
				return spaceMap;
			}
		}
		
		spaceMap.put("xmin", x_min_r);//-20.0);
		spaceMap.put("xmax", x_max_r);//20.0);
	
		spaceMap.put("ymin", y_min_r);//-40.0);
		spaceMap.put("ymax", y_max_r);//40.0);
	
		if (this.is3D) {
			spaceMap.put("zmin", z_min_r);//00);
			spaceMap.put("zmax", z_max_r);//50.0);
		}
		
		/*//revert		//todo	-	do we need this
		this.setT3d(extistingDummyTuple);
		this.setCumulativeTime(0.0);*/
		return spaceMap;		
	
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
		} catch (InterruptedException e) {
			System.out.println("Error sleeping");
		}
	}

}