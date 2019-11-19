package org.bawaweb.ui.sierpinkski;

import java.awt.Color;

class AizawaAttractor extends Attractor {
//		H:\temp\e2\www_algosome_com_articles_aizawa_attractor_chaos_html.pdf
		private final double a = 0.95;
		private final double b = 0.7;
		private final double c = 0.65;//0.9;
		private final double d = 3.5;
		private final double e = 0.25;
		private final double f = 0.1;
		
//		H:\temp\e2\alpaca_17 The Aizawa Attractor.pdf
		private final double alpha = 0.095;
		private final double beta = 0.7;
		private final double gamma = 0.65;
		private final double delta = 3.5;
		private final double eta = 0.1;

		public AizawaAttractor(double x, double y, double z, Color c) {
			super(x, y, z, c);
			this.setName("aizawa");
		}

		public AizawaAttractor(double x, double y, double z, Color c, String dspace) {
			super(x, y, z, c, dspace);
			this.setName("aizawa");
		}
//
//		H:\temp\e2\www_algosome_com_articles_aizawa_attractor_chaos_html.pdf
		@Override
		protected double dx(Tuple3d tuple, double dt) {
			return tuple.x + (dt * (((tuple.z - b) * tuple.x) - (/*tuple.x - */(d * tuple.y))));
		}

		@Override
		protected double dy(Tuple3d tuple, double dt) {
			return tuple.y + (dt * ((d * tuple.x) + ((tuple.z - b) * tuple.y)));
		}

		@Override
		protected double dz(Tuple3d tuple, double dt) {
			return tuple.z + (dt * (
					c + (tuple.z * a) - (Math.pow(tuple.z, 3) / (3 * d))
					- (Math.pow(tuple.x, 2) + Math.pow(tuple.y, 2))
					* (1 + (e * tuple.z))
					+ (f * tuple.z * Math.pow(tuple.x, 3))
				 )
				);
		}
//		H:\temp\e2\www_algosome_com_articles_aizawa_attractor_chaos_html.pdf
		
		

//		H:\temp\e2\alpaca_17 The Aizawa Attractor.pdf
//		@Override
//		protected double dx(Tuple3d tuple, double dt) {
//			return tuple.x + (dt * ((tuple.x * (tuple.z - beta) - (delta * tuple.y))));
//		}
//
//		@Override
//		protected double dy(Tuple3d tuple, double dt) {
//			return tuple.y + (dt * ((delta * tuple.x) + (tuple.y * (tuple.z - beta))));
//		}
//
//		@Override
//		protected double dz(Tuple3d tuple, double dt) {
//			return tuple.z + 
//					(dt * 
//							(gamma + (alpha * tuple.z) - (Math.pow(tuple.z, 3) / 3)
//									+ (eta * tuple.z * Math.pow(tuple.x, 3))));
//		}

//		ends---H:\temp\e2\alpaca_17 The Aizawa Attractor.pdf
		
		/*@Override
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
			
			
			this.xmin = x_min_r;
			this.xmax = x_max_r;

			this.ymin = y_min_r;
			this.ymax = y_max_r;

			this.zmin = z_min_r;
			this.zmax = z_max_r;
			
			//revert
			this.setT3d(extistingDummyTuple);
			this.setCumulativeTime(0.0);

		}
*/
	}