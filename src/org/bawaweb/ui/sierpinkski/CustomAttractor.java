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

	public Tuple3d update(final Tuple3d existingTuple, final double dt, final int iterTime) {
		// store original formula to temp
		final String tempXFormula = this.deltaXFormula;
		final String tempYFormula = this.is1D ? this.deltaYFormula : null;
		final String tempZFormula = this.is3D ? this.deltaZFormula : null;
		
		if (this.isTimeIterDependant) {
			// replace all t or T with iterTime value
			if (this.deltaXFormula.contains("t") || this.deltaXFormula.contains("T")) {
				this.deltaXFormula = this.deltaXFormula.replaceAll("t|T", String.valueOf(iterTime));
			}
			if (!this.is1D) {
				if (this.deltaYFormula.contains("t") || this.deltaYFormula.contains("T")) {
					this.deltaYFormula = this.deltaYFormula.replaceAll("t|T", String.valueOf(iterTime));
				}
			}
			if (this.is3D) {
				if (this.deltaZFormula.contains("t") || this.deltaZFormula.contains("T")) {
					this.deltaZFormula = this.deltaZFormula.replaceAll("t|T", String.valueOf(iterTime));
				}
			} 
		}
		// do the update
		Tuple3d updatedTuple = this.update(existingTuple, dt);
		
		// revert the original formulas from temp	iterTime value replaced with t|T
		this.deltaXFormula = tempXFormula;
		if (!this.is1D) {
			this.deltaYFormula = tempYFormula;
		}
		if(this.is3D){
			this.deltaZFormula = tempZFormula;			
		}
		
		return updatedTuple;
		
	}

}
