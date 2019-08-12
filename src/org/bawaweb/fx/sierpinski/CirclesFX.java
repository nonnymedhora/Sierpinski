/**
 * 
 */
package org.bawaweb.fx.sierpinski;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Navroz
 *https://github.com/natederbinsky/apollonian/blob/java/src/edu/wit/epic/CirclesFX.java
 */

class Gasket {
	private static final double THRESH = 1.0E-10;
	private static final int[] MULT = { -1, 1 };

	private static void addElement(int iteration, List<Element> elements, List<Integer> added, int d,
			double... curvatures) {
		for (int i = 0; i < curvatures.length; i++) {
			added.add(elements.size() + i);
		}

		for (double b : curvatures) {
			elements.add(new Element(d, iteration, b));
		}
	}

	private static void computeCurvature(int d, int iteration, List<Element> elements, List<Integer> added,
			int[] indexes, double A, double B_c, double C_c, boolean includeNeg) {
		final double B;
		final double C;
		{
			double sum_b = 0;
			double sum_b2 = 0;
			for (int i = 0; i <= d; i++) {
				final double b = elements.get(indexes[i]).b;
				sum_b += b;
				sum_b2 += b * b;
			}

			B = B_c * sum_b;
			C = sum_b2 - C_c * (sum_b * sum_b);
		}

		final double disc = B * B - 4 * A * C;
		final double Ax2 = 2 * A;

		if (disc < THRESH) {
			addElement(iteration, elements, added, d, -B / Ax2);
		} else {
			final double disc_sqrt = Math.sqrt(disc);
			final double pos = ((-B + disc_sqrt) / Ax2);
			final double neg = ((-B - disc_sqrt) / Ax2);

			if (includeNeg) {
				addElement(iteration, elements, added, d, pos, neg);
			} else {
				addElement(iteration, elements, added, d, pos);
			}
		}
	}

	private static void _computeCoord(int[] c, double[] combo, double[][] qr) {
		for (int i = 0; i < c.length; i++) {
			combo[i] = qr[i][0] + MULT[c[i]] * qr[i][1];
		}
	}

	private static void computePosition(int d, List<Element> elements, int[] indexes, double[][] qr, int[][] combos,
			double[] combo, double[] combo2, double[] exp) {
		final Element nE = elements.get(indexes[d + 1]);
		final double newB = nE.b;
		final double A = (1 - 1. / d) * newB * newB;
		final double A2 = 2. * A;

		boolean singlePoint = true;
		for (int i = 1; i <= d; i++) {
			double bSum = 0;
			double cSum1 = 0;
			double cSum2 = 0;
			for (int j = 0; j <= d; j++) {
				final Element e_j = elements.get(indexes[j]);
				final double b_j = e_j.b;
				final double y_j_i = e_j.x[i];

				bSum += (newB * b_j * y_j_i);
				cSum1 += b_j * b_j * y_j_i * y_j_i;

				for (int k = 0; k <= d; k++) {
					final Element e_k = elements.get(indexes[k]);
					final double b_k = e_k.b;
					final double y_k_i = e_k.x[i];

					cSum2 += b_j * b_k * y_j_i * y_k_i;
				}
			}
			final double B = -(2. / d) * bSum;
			final double C = (cSum1 - (1. / d) * cSum2) - 2;

			qr[i - 1][0] = -B / A2;

			final double disc = B * B - 4 * A * C;
			if (disc < THRESH) {
				qr[i - 1][1] = 0.;
			} else {
				qr[i - 1][1] = Math.sqrt(disc) / A2;
				singlePoint = false;
			}
		}

		if (singlePoint) {
			for (int i = 1; i <= d; i++) {
				nE.x[i] = qr[i - 1][0];
			}
		} else {
			Double bestDiff = null;
			int[] bestC = null;

			double expSum = 0.;
			for (int i = 0; i < exp.length; i++) {
				final Element e = elements.get(indexes[i]);

				final double expV;
				if (e.r < 0) {
					expV = Math.abs(e.r) - nE.r;
				} else if (nE.r < 0) {
					expV = Math.abs(nE.r) - e.r;
				} else {
					expV = e.r + nE.r;
				}
				exp[i] = expV;
				expSum += expV;
			}

			for (int[] c : combos) {
				_computeCoord(c, combo, qr);

				double diffSum = 0.;
				for (int i = 0; i < exp.length; i++) {
					final Element e = elements.get(indexes[i]);
					double sum2 = 0.;
					for (int j = 0; j < c.length; j++) {
						final double cDiff = e.x[1 + j] - combo[j];
						sum2 += cDiff * cDiff;
					}
					diffSum += Math.abs(Math.sqrt(sum2) - exp[i]);
				}

				if (bestDiff == null || diffSum < bestDiff) {

					// rare edge case to break symmetries
					boolean go = true;
					if ((bestDiff != null) && (diffSum / expSum < THRESH && bestDiff / expSum < THRESH)) {
						_computeCoord(bestC, combo2, qr);

						final double pDistance;
						{
							double pSum = 0.;
							for (int i = 0; i < c.length; i++) {
								final double pDiff = combo[i] - combo2[i];
								pSum += pDiff * pDiff;
							}
							pDistance = Math.sqrt(pSum);
						}

						if (pDistance > THRESH) {
							for (Element e : elements) {
								double eSum = 0.;
								for (int i = 0; i < c.length; i++) {
									final double eDiff = e.x[1 + i] - combo[i];
									eSum += eDiff * eDiff;
								}
								final double eDistance = Math.sqrt(eSum);
								if (eDistance < THRESH) {
									go = false;
								}
							}
						} else {
							go = false;
						}
					}

					if (go) {
						bestDiff = diffSum;
						bestC = c;
					}
				}
			}

			_computeCoord(bestC, combo, qr);
			for (int i = 0; i < combo.length; i++) {
				nE.x[i + 1] = combo[i];
			}
		}
	}

	private static int[] getFromPool(int d, Stack<int[]> pool) {
		if (pool.isEmpty()) {
			return new int[d + 2];
		} else {
			return pool.pop();
		}
	}

	private static void returnToPool(Stack<int[]> pool, int[] a) {
		pool.push(a);
	}

	/**
	 * Generates an Apollonian sphere in a specified dimension for a specified
	 * number of iterations
	 * 
	 * @param d
	 *            dimensions
	 * @param elements
	 *            destination list (includes seed elements)
	 * @param iterations
	 *            number of iterations for which to generate
	 */
	public static void generate(int d, List<Element> elements, int iterations) {
		if (iterations > 0) {
			final Stack<int[]> indexPool = new Stack<>();
			final ArrayList<int[]> last = new ArrayList<>();
			final ArrayList<int[]> newbies = new ArrayList<>();

			final ArrayList<Integer> added = new ArrayList<>();
			final double[][] qr = new double[d][2];

			final double A = ((double) d - 1) / ((double) d);
			final double B_c = -2.0 / d;
			final double C_c = 1.0 / d;

			final double[] exp = new double[d + 1];
			final double[] combo = new double[d];
			final double[] combo2 = new double[d];
			final int[][] combos = new int[(int) Math.pow(2, d)][d];
			for (int i = 0; i < combos.length; i++) {
				final String s = String.format("%0" + d + "d", Integer.valueOf(Integer.toString(i, 2)));
				for (int j = 0; j < d; j++) {
					combos[i][j] = s.charAt(j) - '0';
				}
			}

			{
				final int[] indexes = getFromPool(d, indexPool);
				for (int i = 0; i <= d; i++) {
					indexes[i] = i;
				}

				last.add(indexes);
			}

			boolean first = true;
			for (int iteration = 1; iteration <= iterations; iteration++) {
				for (int[] l : last) {
					added.clear();
					computeCurvature(d, iteration, elements, added, l, A, B_c, C_c, first);

					for (int a : added) {
						l[d + 1] = a;
						computePosition(d, elements, l, qr, combos, combo, combo2, exp);

						for (int i = 0; i <= d; i++) {
							final int[] indexes = getFromPool(d, indexPool);
							for (int j = 0; j <= d; j++) {
								if (i == j) {
									indexes[j] = a;
								} else {
									indexes[j] = l[j];
								}
							}

							newbies.add(indexes);
						}
					}
				}
				first = false;

				for (int[] l : last) {
					returnToPool(indexPool, l);
				}
				last.clear();
				last.addAll(newbies);
				newbies.clear();
			}
		}
	}
}

class Element {

	// intentional direct
	// accessibility only
	// within the package
	final int iteration;
	final double[] x;
	double r;
	double b;

	// common constructor code
	private Element(int d, int iteration) {
		this.iteration = iteration;
		x = new double[d + 1];
		x[0] = 1.;
	}

	/**
	 * Convenience package method to generate an element via curvature without
	 * knowing coordinates
	 * 
	 * @param d
	 *            dimensions
	 * @param iteration
	 *            iteration of creation
	 * @param b
	 *            curvature
	 */
	Element(int d, int iteration, double b) {
		this(d, iteration);

		this.b = b;
		this.r = 1. / b;
	}

	/**
	 * Creates a new element
	 * 
	 * @param d
	 *            number of dimensions
	 * @param iteration
	 *            iteration of creation
	 * @param r
	 *            radius
	 * @param x
	 *            coordinates of element
	 */
	public Element(int d, int iteration, double r, double... x) {
		this(d, iteration);

		this.r = r;
		this.b = (1. / r);

		for (int i = 1; i < (x.length + 1); i++) {
			this.x[i] = x[i - 1];
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(String.format("%d: %.3f/%.3f @ (%.3f", iteration, r, b, x[0]));

		for (int i = 1; i < x.length; i++) {
			sb.append(String.format(", %.3f", x[i]));
		}
		sb.append(")");

		return sb.toString();
	}

	/**
	 * Get coordinate via dimension
	 * 
	 * @param d
	 *            dimension
	 * @return coordinate
	 */
	public double getX(int d) {
		return x[d + 1];
	}

	/**
	 * Get creation iteration
	 * 
	 * @return iteration of creation
	 */
	public int getIteration() {
		return iteration;
	}

	/**
	 * Get element radius
	 * 
	 * @return radius
	 */
	public double getRadius() {
		return (r < 0) ? (-r) : (r);
	}
}



public class CirclesFX extends Application {

	public CirclesFX() {
		super();
	}
	
	private static double[] getXY(double r1, double r2, double r3) {
		final double a = r2 + r3;
		final double b = r1 + r3;
		final double c = r1 + r2;
		
		final double x = (b*b + c*c - a*a) / (2 * c);
		final double y = Math.sqrt(b*b - x*x);
		
		return new double[] {x, y};
	}
	
	private static double[] getOffset(double x1, double y1, double x2, double y2, double x3, double y3) {
		final double cx = 1./3.*(x1 + x2 + x3);
		final double cy = 1./3.*(y1 + y2 + y3);
		
		return new double[] {-cx, -cy};
	}
	
	private void _start(Stage ps, Color[] colors, double[] c, double m) {
		final int windowSize = 600;
		final double scaleMin = -2.5;
		final double scaleMax = 2.5;
		
		final double scaleMult = windowSize / (scaleMax - scaleMin);
		final double windowMid = windowSize / 2.;
		
		//
		
		final Group root = new Group();
		final Scene scene = new Scene(root, windowSize, windowSize, Color.WHITE);
		
		//
		
		final int d = 2;
		
		//
		
		final double r1 = m*1./c[0];
		final double r2 = m*1./c[1];
		final double r3 = m*1./c[2];
		
		final double x1 = 0.;
		final double y1 = 0.;
		final double x2 = r1 + r2;
		final double y2 = 0.;
		
		final double[] xy = getXY(r1, r2, r3);
		final double[] o = getOffset(x1, y1, x2, y2, xy[0], xy[1]);

		final ArrayList<Element> elements = new ArrayList<>();
		elements.add(new Element(d, 0, r1, o[0] + x1, o[1] + y1));
		elements.add(new Element(d, 0, r2, o[0] + x2, o[1] + y2));
		elements.add(new Element(d, 0, r3, o[0] + xy[0], o[1] + xy[1]));
		
		Gasket.generate(d, elements, colors.length-1);
		
		//

		for (int i=0; i<elements.size(); i++) {
			final Element e = elements.get(i);
			
			final Circle circ = new Circle(windowMid + e.getX(0)*scaleMult, windowMid - e.getX(1)*scaleMult, e.getRadius()*scaleMult);
			circ.setStroke(colors[e.getIteration()]);
			circ.setStrokeWidth(0.9);
			circ.setFill(Color.TRANSPARENT);
			root.getChildren().add(circ);
		}
		
		root.getChildren().add(new Text(10, windowSize-10, String.format("Iterations: %d, Circles: %,d", colors.length-1, elements.size())));
		
		//
		
		ps.setScene(scene);
		ps.setResizable(false);
		ps.setTitle("Apollonia!");
		ps.show();
	}

	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage ps) throws Exception {
		////////////////////////////////////////////////////
		// Step 1: Choose seed circle sizes
		////////////////////////////////////////////////////

		final double[] c; // initial curvatures
		final double m; // visual multiplier

		// some initial configuration options
		// leave only one uncommented
		c = new double[] { 1., 1., 1. };
		m = 1.;
		// c = new double[] {25., 25., 28.}; m = 20.;
		// c = new double[] {5., 8., 8.}; m = 6.;
		// c = new double[] {10., 15., 19.}; m = 6.;
		// c = new double[] {23., 27., 18.}; m = 16.;
		// c = new double[] {2., 2., 3.}; m = 1.;

		////////////////////////////////////////////////////
		// Step 2: Set max iterations via colors
		////////////////////////////////////////////////////

		// first = seed color
		// iterations = colors.length - 1
		final Color[] colors = { Color.BLACK, Color.ORANGE, Color.BLUE, Color.GRAY, Color.RED, Color.GREEN,
				Color.MAGENTA, };

		////////////////////////////////////////////////////
		////////////////////////////////////////////////////

		_start(ps, colors, c, m);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

}
