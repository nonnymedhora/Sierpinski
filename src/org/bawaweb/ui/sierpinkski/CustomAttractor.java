/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * @author Navroz
 *
 */
public class CustomAttractor extends Attractor {

	private String deltaXFormula;
	private String deltaYFormula;
	private String deltaZFormula;

	FunctionEvaluator funcEval = new FunctionEvaluator();

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param c
	 */
	public CustomAttractor(double x, double y, double z, Color c) {
		super(x, y, z, c);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param x
	 * @param y
	 * @param z
	 * @param c
	 * @param dspace
	 */
	public CustomAttractor(double x, double y, double z, Color c, String dspace) {
		super(x, y, z, c, dspace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param tuple
	 * @param col
	 */
	public CustomAttractor(Tuple3d tuple, Color col) {
		super(tuple, col);
		// TODO Auto-generated constructor stub
	}

	public String getDeltaXFormula() {
		return this.deltaXFormula;
	}

	public void setDeltaXFormula(String deltaXFormula) {
		this.deltaXFormula = deltaXFormula;
	}

	public String getDeltaYFormula() {
		return this.deltaYFormula;
	}

	public void setDeltaYFormula(String dY) {
		this.deltaYFormula = dY;
	}

	public String getDeltaZFormula() {
		return this.deltaZFormula;
	}

	public void setDeltaZFormula(String dZ) {
		this.deltaZFormula = dZ;
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.Attractor#dx(org.bawaweb.ui.sierpinkski.Tuple3d, double)
	 */
	@Override//z0 = new FunctionEvaluator().evaluate(fun, z0);
	protected double dx(Tuple3d tuple, double dt) {
		if (!this.isTimeInvariant) {
			return tuple.x + (new FunctionEvaluator().evaluate(this.deltaXFormula, tuple) * dt);
		} else {
			return new FunctionEvaluator().evaluate(this.deltaXFormula, tuple);
		}
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.Attractor#dy(org.bawaweb.ui.sierpinkski.Tuple3d, double)
	 */
	@Override
	protected double dy(Tuple3d tuple, double dt) {
		if (!this.isTimeInvariant) {
			return tuple.y + (new FunctionEvaluator().evaluate(this.deltaYFormula, tuple) * dt);
		} else {
			return new FunctionEvaluator().evaluate(this.deltaYFormula, tuple);
		}
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.Attractor#dz(org.bawaweb.ui.sierpinkski.Tuple3d, double)
	 */
	@Override
	protected double dz(Tuple3d tuple, double dt) {
		if (!this.isTimeInvariant) {
			return tuple.z + (new FunctionEvaluator().evaluate(this.deltaZFormula, tuple) * dt);
		} else {
			return new FunctionEvaluator().evaluate(this.deltaZFormula, tuple);
		}
	}

}
