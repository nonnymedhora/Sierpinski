
package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;



/**
 * @author Navroz
 * @see CirclesFX
 */
public class ApollonianCircles extends FractalBase {
	
	public static final Color TRANSPARENT = new Color(0f, 0f, 0f, 0f);
	
	private double[] curvatures;
	private double multiplier;

	public ApollonianCircles() {
		super();
	}
	
	public ApollonianCircles(double[] curves, double mult) {
		super();
//		System.out.println(" ApollonianCircles(double[] curves, double mult)");
//		System.out.println("C1["+curves[0]+"] C2["+curves[1]+"] C3["+curves[2]+"] mult["+mult+"]");
		this.setCurvatures(curves);
		this.setMultiplier(mult);
	}
	
	@Override
	public Color getBGColor(){
		return Color.white;
		
	}
	
	private void createApollonianCircles(Graphics2D g, Color[] colors, double[] c, double m, int dpth) {
		final int windowSize = WIDTH;
		final double scaleMin = -2.5;
		final double scaleMax = 2.5;

		final double scaleMult = windowSize / (scaleMax - scaleMin);
		final double windowMid = windowSize / 2.0;

		final int dims = 2;
		
		final double r1 = m * 1.0 / c[0];
		final double r2 = m * 1.0 / c[1];
		final double r3 = m * 1.0 / c[2];

		final double x1 = 0.0;
		final double y1 = 0.0;
		final double x2 = r1 + r2;
		final double y2 = 0.0;

		final double[] xy = getXY(r1, r2, r3);
		final double[] o = getOffset(x1, y1, x2, y2, xy[0], xy[1]);
		if(dpth==0){
			Element e = new Element(dims, 0, r1, o[0] + x1, o[1] + y1);
			drawCircle(g,
					new Point((int) (windowMid + e.getX(0) * scaleMult), (int) (windowMid - e.getX(1) * scaleMult)),
					(int) (e.getRadius() * scaleMult), colors[e.getIteration()]);

			fillCircle(g,
					new Point((int) (windowMid + e.getX(0) * scaleMult), (int) (windowMid - e.getX(1) * scaleMult)),
					(int) (e.getRadius() * scaleMult), TRANSPARENT);

			return;
		}

		final ArrayList<Element> elements = new ArrayList<>();	
		
		elements.add(new Element(dims, 0, r1, o[0] + x1, o[1] + y1));
		elements.add(new Element(dims, 0, r2, o[0] + x2, o[1] + y2));
		elements.add(new Element(dims, 0, r3, o[0] + xy[0], o[1] + xy[1]));
			
		Gasket.generate(dims, elements, dpth - 1);
			
		
		for (int i = 0; i < elements.size(); i++) {
			final Element e = elements.get(i);
			g.setStroke(new BasicStroke(2));
			drawCircle(g,
					new Point((int) (windowMid + e.getX(0) * scaleMult), (int) (windowMid - e.getX(1) * scaleMult)),
					(int) (e.getRadius() * scaleMult), colors[e.getIteration()]);

			fillCircle(g,
					new Point((int) (windowMid + e.getX(0) * scaleMult), (int) (windowMid - e.getX(1) * scaleMult)),
					(int) (e.getRadius() * scaleMult), TRANSPARENT);
	
		}

		createApollonianCircles(g, colors, c, m, dpth - 1);

	}
	
	private static double[] getXY(double r1, double r2, double r3) {
		final double a = r2 + r3;
		final double b = r1 + r3;
		final double c = r1 + r2;

		final double x = (b * b + c * c - a * a) / (2 * c);
		final double y = Math.sqrt(b * b - x * x);

		return new double[] { x, y };
	}

	private static double[] getOffset(double x1, double y1, double x2, double y2, double x3, double y3) {
		final double cx = 1. / 3. * (x1 + x2 + x3);
		final double cy = 1. / 3. * (y1 + y2 + y3);

		return new double[] { -cx, -cy };
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		g.setBackground(Color.white);
		////////////////////////////////////////////////////
		// Step 1: Choose seed circle sizes
		////////////////////////////////////////////////////
		
		final double[] c=this.getCurvatures(); // initial curvatures
		final double m=this.getMultiplier(); // visual multiplier
		
		// some initial configuration options
		// leave only one uncommented
		/*c = new double[] { 1.0, 1.0, 1.0 };
		m = 1.0;*/
		// c = new double[] {25., 25.0, 28.0}; m = 20.0;
		// c = new double[] {5.0, 8.0, 8.0}; m = 6.0;
		// c = new double[] {10.0, 15.0, 19.0}; m = 6.0;
		// c = new double[] {23.0, 27.0, 18.0}; m = 16.0;
		// c = new double[] {2.0, 2.0, 3.0}; m = 1.0;
		
		////////////////////////////////////////////////////
		// Step 2: Set max iterations via colors
		////////////////////////////////////////////////////
		
		// first = seed color
		// iterations = colors.length - 1
		final Color[] colors = { 
				Color.BLACK, Color.RED, Color.BLUE, Color.YELLOW, 
				Color.ORANGE, Color.GREEN, Color.MAGENTA, Color.DARK_GRAY,
				Color.CYAN, Color.PINK, Color.LIGHT_GRAY};
		
		////////////////////////////////////////////////////
		////////////////////////////////////////////////////

		createApollonianCircles(g, colors, c, m, depth);

	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#getFractalShapeTitle()
	 */
	@Override
	protected String getFractalShapeTitle() {		
		return "BaswaZ Apollonion Circles";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final double[] initCurves = new double[] { 1.0, 1.0, 1.0 };
		double mXr=1.0;
		final FractalBase ff = new ApollonianCircles(initCurves,mXr);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = ff;
				frame.setTitle(ff.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
				frame.depth = 2;
				new Thread(frame).start();

			}
		});
	}

	/**
	 * @return the curvatures
	 */
	public double[] getCurvatures() {
		return curvatures;
	}

	/**
	 * @param curvatures the curvatures to set
	 */
	public void setCurvatures(double[] curvatures) {
		this.curvatures = curvatures;
	}

	/**
	 * @return the multiplier
	 */
	public double getMultiplier() {
		return multiplier;
	}

	/**
	 * @param mult the multiplier to set
	 */
	public void setMultiplier(double mult) {
		this.multiplier = mult;
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
	 * @param d - dimensions
	 * @param iteration - iteration of creation
	 * @param b - curvature
	 */
	Element(int d, int iteration, double b) {
		this(d, iteration);

		this.b = b;
		this.r = 1. / b;
	}

	/**
	 * Creates a new element
	 * 
	 * @param d - number of dimensions
	 * @param iteration - iteration of creation
	 * @param r - radius
	 * @param x - coordinates of element
	 */
	public Element(int d, int iteration, double r, double... x) {
		this(d, iteration);

		this.r = r;
		this.b = (1.0 / r);

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
	 * @param d - dimension
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
	 * @param dim - dimensions
	 * @param elements - destination list (includes seed elements)
	 * @param iterations - number of iterations for which to generate
	 */
	public static void generate(int dim, List<Element> elements, int iterations) {
		if (iterations > 0) {
			final Stack<int[]> indexPool = new Stack<>();
			final ArrayList<int[]> last = new ArrayList<>();
			final ArrayList<int[]> newbies = new ArrayList<>();

			final ArrayList<Integer> added = new ArrayList<>();
			final double[][] qr = new double[dim][2];

			final double A = ((double) dim - 1) / ((double) dim);
			final double B_c = -2.0 / dim;
			final double C_c = 1.0 / dim;

			final double[] exp = new double[dim + 1];
			final double[] combo = new double[dim];
			final double[] combo2 = new double[dim];
			final int[][] combos = new int[(int) Math.pow(2, dim)][dim];
			for (int i = 0; i < combos.length; i++) {
				final String s = String.format("%0" + dim + "d", Integer.valueOf(Integer.toString(i, 2)));
				for (int j = 0; j < dim; j++) {
					combos[i][j] = s.charAt(j) - '0';
				}
			}

			{
				final int[] indexes = getFromPool(dim, indexPool);
				for (int i = 0; i <= dim; i++) {
					indexes[i] = i;
				}

				last.add(indexes);
			}

			boolean first = true;
			for (int iteration = 1; iteration <= iterations; iteration++) {
				for (int[] l : last) {
					added.clear();
					computeCurvature(dim, iteration, elements, added, l, A, B_c, C_c, first);

					for (int a : added) {
						l[dim + 1] = a;
						computePosition(dim, elements, l, qr, combos, combo, combo2, exp);

						for (int i = 0; i <= dim; i++) {
							final int[] indexes = getFromPool(dim, indexPool);
							for (int j = 0; j <= dim; j++) {
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