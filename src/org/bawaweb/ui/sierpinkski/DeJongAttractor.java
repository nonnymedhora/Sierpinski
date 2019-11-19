package org.bawaweb.ui.sierpinkski;

import java.awt.Color;

class DeJongAttractor extends Attractor {
	/*
	 * https://www.algosome.com/articles/strange-attractors-de-jong.html The
	 * equations underlying the De Jong attractor follow:
	 * 
	 * xt+1 = sin(a * yt) - cos(b * xt)		 * 
	 * yt+1 = sin(c * xt) - cos(d * yt)
	 * 
	 * Iterative calculation of the coordinates yields a set of coordinates
	 * which can be plotted in two dimensions. Values of the constants a, b,
	 * c, and d can be altered to generate different patterns from the
	 * attractor.
	 * 
	 * 
	 * a = -2.24, b = 0.43, c = -0.65, d = -2.43 
	 * a = 2.01, b = -2.53, c = 1.61, d = -0.33 
	 * a = -2, b = -2, c = -1.2, d = 2
	 */
	
	private final double a = -2.24;
	private final double b = 0.43;
	private final double c = -0.65;
	private final double d = -2.43;

	public DeJongAttractor(double x, double y, double z, Color c) {
		super(x, y, z, c);
		this.setName("dejong");
	}
	
	public DeJongAttractor(double x, double y, double z, Color c, String dspace) {
		super(x, y, z, c, dspace);
		this.setName("dejong");
	}
	//https://www.algosome.com/articles/strange-attractors-de-jong.html

	@Override
	protected double dx(Tuple3d tuple, double dt) {
		// xt+1 = sin(a * yt) - cos(b * xt)
		return tuple.x + (dt * ( Math.sin(a * tuple.y) - Math.cos(b * tuple.x) ));
	}

	@Override
	protected double dy(Tuple3d tuple, double dt) {
		// yt+1 = sin(c * xt) - cos(d * yt)
		return tuple.y + (dt * ( Math.sin(c * tuple.x) - Math.cos(d * tuple.y) ));
	}

	@Override
	protected double dz(Tuple3d tuple, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}
}