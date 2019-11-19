package org.bawaweb.ui.sierpinkski;

import java.awt.Color;

class LorenzAttractor extends Attractor {
		
		private final double a_prandt 	= 10.0;				//sigma=====10
	    private final double b_rayleigh = 8.0/3;//28.0;		//beta======8/3	
	    private final double c_rho 		= 28.0;//8.0/3;		//rho=======28		0<c_rho < Double.infinity

		public LorenzAttractor(double x, double y, double z, Color c) {
			super( x, y, z, c);
			this.setName("lorenz");
		}
		
		public LorenzAttractor(double x, double y, double z, Color c, String space) {
			super(x, y, z, c, space);
			this.setName("lorenz");
		}
		
		public LorenzAttractor(Tuple3d td, Color c, String space) {
			this(td.x, td.y, td.z, c, space);
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
		
	}