/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.Properties;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 *<h>Apollonian network</h>
	In  combinatorial  mathematics,  an  Apollonian  network  is  an
	undirected graph formed by a process of recursively subdividing a
	triangle  into  three  smaller  triangles.  Apollonian  networks  may
	equivalently be defined  as  the  planar  3-trees,  the maximal  planar
	chordal  graphs,  the  uniquely  4-colorable  planar  graphs,  and  the
	graphs of stacked polytopes. They are named after Apollonius  of
	Perga, who studied a related circle-packing construction.
	
	
	<b>Definition</b>
	An Apollonian network may be formed, starting from a single  triangle 
	embedded in the Euclidean plane,  by repeatedly selecting a  
	triangular face of  the embedding, adding a new vertex  
	inside  the face, and connecting the new vertex  
	to each vertex of  the face containing  it. 
	
	In  this way,  the  triangle containing  the new vertex  is
	subdivided into three smaller triangles, 
	which may in turn be subdivided in the same way.
 */
public class ApollonianTriangleNetwork extends FractalBase {

	private static final long serialVersionUID = 1654L;

	private static final Color[] TriangleColorPalette =new Color[] {
			
			Colors.BLACK.getColor(),
			Colors.RED.getColor(),
			Colors.BLUE.getColor(),
			Colors.GREEN.getColor(),
			Colors.YELLOW.getColor(),		//ORANGE
			Colors.MAGENTA.getColor(),	//YELLOW
			Colors.ORANGE.getColor(),		//PINK
			Colors.CYAN.getColor(),			//MAGENTA
			Colors.PINK.getColor()
			
			
	};
	
	private int runCount;

	public ApollonianTriangleNetwork() {
		super();
		runCount = 1;
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		runCount += 1;
		if (runCount<5) {
			this.createApollonianTriangleNetwork(g, depth);
		}
	}
	
	private Image createApollonianTriangleNetwork(final Graphics2D g, final int d) {
		// Clear the frame
//		g.setColor(Color.BLUE);
		/*if(runCount>4)
			return this.bufferedImage;*/
		g.setColor(TriangleColorPalette[runCount]);
//		g.setColor(new Color((int)Math.random()*256,(int)Math.random()*256,(int)Math.random()*256));
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Point p1, p2, p3 = null;
		// Initialize p1, p2, p3 based on frame size
		p1 = new Point(getWidth() / 2, OFFSET);
		p2 = new Point(OFFSET, getHeight() - OFFSET);
		p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);

		Triangle tBase = new Triangle(p1, p2, p3);
		// Draw ApollonianTriangleNetwork
//		this.drawApollonianTriangleNetwork(g, depth, tBase);// p1, p2, p3);
		this.drawApollonianTriangleNetwork(g, depth,  p1, p2, p3);

		return this.bufferedImage;

	}

	private void drawApollonianTriangleNetwork(Graphics2D g, int d, Triangle tri) {
		g.setColor(Color.BLACK);
		if (d == 0) { // depth is 0, draw the triangle
			g.setStroke(new BasicStroke(1));
			tri.draw(g);
			return;
		}
		Point p1 = tri.p1;
		Point p2 = tri.p1;
		Point p3 = tri.p3;
		
		Point randomPt = this.randomInnerPoint(g, p1, p2, p3);


		Triangle t1 = new Triangle(p1, p2, randomPt);
		Triangle t2 = new Triangle(p2, p3, randomPt);
		Triangle t3 = new Triangle(p1, p3, randomPt);
		
		this.drawApollonianTriangleNetwork(g, d - 1, t1);
		this.drawApollonianTriangleNetwork(g, d - 1, t2);
		this.drawApollonianTriangleNetwork(g, d - 1, t3);
	}

	private void drawApollonianTriangleNetwork(final Graphics2D g, final int d, final Point p1, final Point p2, final Point p3) {
		g.setColor(Color.BLACK);
		if (d == 0) { // depth is 0, draw the triangle
			g.setStroke(new BasicStroke(1));
			this.drawLine(g, p1, p2);
			this.drawLine(g, p2, p3);
			this.drawLine(g, p3, p1);
			return;
		}

		Point randomPt = this.randomInnerPoint(g, p1, p2, p3);


		Triangle t1 = new Triangle(p1, p2, randomPt);
		Triangle t2 = new Triangle(p2, p3, randomPt);
		Triangle t3 = new Triangle(p1, p3, randomPt);

		/*g.setColor(Color.RED);
		t1.draw(g);
		t2.draw(g);
		t3.draw(g);*/

		this.drawApollonianTriangleNetwork(g, d - 1, t1.p1, t1.p2, t1.p3);
		this.drawApollonianTriangleNetwork(g, d - 1, t2.p1, t2.p2, t2.p3);
		this.drawApollonianTriangleNetwork(g, d - 1, t3.p1, t3.p2, t3.p3);

	}

	private Point randomInnerPoint(Graphics2D g, Point p1, Point p2, Point p3) {		
		Point randInnrPt = null;
		Triangle testTriangle = new Triangle(p1,p2,p3);

		final int minX = Math.min(p1.x,Math.min(p2.x,p3.x));		
		final int minY = Math.min(p1.y,Math.min(p2.y,p3.y));
		final int maxX = Math.max(p1.x,Math.max(p2.x,p3.x));		
		final int maxY = Math.max(p1.y,Math.max(p2.y,p3.y));

		boolean found = false;
		int randXPt = (int) (Math.random() * (maxX-minX)+minX);
		int randYPt = (int) (Math.random() * (maxY-minY)+minY);
		
		randInnrPt = new Point(randXPt,randYPt);

		while (!found) {
			if (testTriangle.isPointInTriangle(randInnrPt)) {
				found = true;
				break;
				
			}
			randXPt = (int) (Math.random() * (maxX-minX)+minX);
			randYPt = (int) (Math.random() * (maxY-minY)+minY);
			
			randInnrPt = new Point(randXPt,randYPt);
//			System.out.println("randInnrPt = "+randInnrPt);
		}
		
		/*
		final Random randX = new Random((p1.x + p2.x + p3.x) / 3);
		final Random randY = new Random((p1.y + p2.y + p3.y) / 3);
		
		while (!found) {
			randInnrPt = new Point(randX.nextInt(), 
					 randY.nextInt());
System.out.println("randInnrPt = "+randInnrPt);
			if (testTriangle.isPointInTriangle(randInnrPt)) {
				found = true;
			}
		}*/
//		System.out.println("found==="+found);
		return randInnrPt ;
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#getFractalShapeTitle()
	 */
	@Override
	protected String getFractalShapeTitle() {
		return "BaWaZ ApollonianTriangkeNetwork";
	}
	
	
	


	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final ApollonianTriangleNetwork apollonianTriangleNetwork = new ApollonianTriangleNetwork();
				/*frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
				*/
				
				final FractalBase frame = apollonianTriangleNetwork;
				
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(WIDTH-200, HEIGHT-200);
				frame.setRunning(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
//				frame.depth = 3;
				new Thread(frame).start();
				

			}
		});
	}

}






class Triangle {
	
	Point p1;
	Point p2;
	Point p3;
	
	/**
	 * @param p11
	 * @param p22
	 * @param p33
	 */
	public Triangle(Point p11, Point p22, Point p33) {
		super();
		this.p1 = p11;
		this.p2 = p22;
		this.p3 = p33;
	}
	
	public void draw(Graphics2D g) {
		g.drawLine(this.p1.x, this.p1.y, this.p2.x, this.p2.y);
		g.drawLine(this.p2.x, this.p2.y, this.p3.x, this.p3.y);
		g.drawLine(this.p1.x, this.p1.y, this.p3.x, this.p3.y);
	}
	
	
	public double area() {
		//Area A = [ x1(y2 – y3) + x2(y3 – y1) + x3(y1-y2)]/2
		return Math.abs(
				(this.p1.x*(this.p2.y-this.p3.y)+
				this.p2.x*(this.p3.y-this.p1.y)+
				this.p3.x*(this.p1.y-this.p2.y)
				)/2.0);
	}

	public boolean isPointInTriangle(Point p) {
		
		final double area = this.area();
		
		Triangle t1 = new Triangle(p1,p2,p);		
		double a1 = t1.area();
		
		Triangle t2 = new Triangle(p2,p3,p);
		double a2 = t2.area();
		
		Triangle t3 = new Triangle(p1,p3,p);
		double a3 = t3.area();
		
		return area == (a1+a2+a3);
	}
	
	
	
}