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
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

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
			Colors.RED.getColor(),		//PINK
			Colors.CYAN.getColor(),			//MAGENTA
			Colors.PINK.getColor()
			
			
	};
	private String mode = "useRandom";
	private int runCount;
	
	static LinkedHashMap<Integer,List<Triangle>> trianglesListMap
	= new LinkedHashMap<Integer,List<Triangle>>();

	public ApollonianTriangleNetwork() {
		super();
		runCount = 1;
	}

	public ApollonianTriangleNetwork(String m) {
		super();
		runCount=1;
		this.mode=m;
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
//		/*if(runCount>4)
//			return this.bufferedImage;*/
//		g.setColor(TriangleColorPalette[runCount]);
		g.setColor(Color.white);

		g.fillRect(0, 0, getWidth(), getHeight());
		
		Point p1, p2, p3 = null;
		// Initialize p1, p2, p3 based on frame size
		p1 = new Point(getWidth() / 2, OFFSET);
		p2 = new Point(OFFSET, getHeight() - OFFSET);
		p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);

		Triangle tBase = new Triangle(p1, p2, p3);
		
		add2TrianglesListSet(tBase,d);
		
		// Draw ApollonianTriangleNetwork
		this.drawApollonianTriangleNetwork(g, d, tBase);	//Uses Triangle
//		this.drawApollonianTriangleNetwork(g, depth,  p1, p2, p3);	//Uses Point

		return this.bufferedImage;
	}

	private void drawApollonianTriangleNetwork(Graphics2D g, int d, Triangle tri) {
//		g.setColor(TriangleColorPalette[runCount+1]);//
		g.setColor(Color.BLACK);
		if (d == 0) { // depth is 0, draw the triangle
			g.setStroke(new BasicStroke(1));
//			tri.draw(g);
			
			this.drawTrianglesListMap(g);
//			((List<Triangle>)trianglesListMap.get(d+1)).get(0).draw(g);
			return;
		}
		Point innerPt;
		if (this.mode.equalsIgnoreCase("useCentroid")) {
			innerPt = tri.centroid();
		} else if (this.mode.equalsIgnoreCase("useRandom")) {
			innerPt = this.randomInnerPoint(g, tri);
		} else {
			innerPt = this.randomInnerPoint(g, tri);
		}

		Triangle t1 = new Triangle(tri.p1, tri.p2, innerPt);
		Triangle t2 = new Triangle(tri.p2, tri.p3, innerPt);
		Triangle t3 = new Triangle(tri.p1, tri.p3, innerPt);
		

		this.add2TrianglesListSet(t1,d);
		this.add2TrianglesListSet(t2,d);
		this.add2TrianglesListSet(t3,d);
		
		this.drawApollonianTriangleNetwork(g, d - 1, t1);
		this.drawApollonianTriangleNetwork(g, d - 1, t2);
		this.drawApollonianTriangleNetwork(g, d - 1, t3);
	}
	
	private void drawTrianglesListMap(Graphics2D g) {
		for(Integer dep : trianglesListMap.keySet()){
//			System.out.println("Dep___ "+dep);
			for(Triangle tri : trianglesListMap.get(dep)){
				tri.draw(g);
			}
		}
		
	}

	private void add2TrianglesListSet(Triangle tri, int dpth) {
		// TODO Auto-generated method stub
//		System.out.println("depth+" + dpth);
		if (trianglesListMap.get(dpth) == null) {
			List<Triangle> trianglesSet = new ArrayList<Triangle>();
			trianglesSet.add(tri);

			trianglesListMap.put(dpth, trianglesSet);

		} else {
			List<Triangle> trianglesSet = trianglesListMap.get(dpth);
			trianglesSet.add(tri);

			trianglesListMap.put(dpth, trianglesSet);
		}

	}
	
	

	private Point randomInnerPoint(Graphics2D g, Triangle t) {
		Point randInnrPt = null;
		final int minX = Math.min(t.p1.x,Math.min(t.p2.x,t.p3.x));		
		final int minY = Math.min(t.p1.y,Math.min(t.p2.y,t.p3.y));
		final int maxX = Math.max(t.p1.x,Math.max(t.p2.x,t.p3.x));		
		final int maxY = Math.max(t.p1.y,Math.max(t.p2.y,t.p3.y));

		boolean found = false;
		int randXPt = (int) (Math.random() * (maxX-minX)+minX);
		int randYPt = (int) (Math.random() * (maxY-minY)+minY);
		
		randInnrPt = new Point(randXPt,randYPt);

		while (!found) {
			if (t.isPointInTriangle(randInnrPt)) {
				found = true;
				break;
				
			}
			randXPt = (int) (Math.random() * (maxX-minX)+minX);
			randYPt = (int) (Math.random() * (maxY-minY)+minY);
			
			randInnrPt = new Point(randXPt,randYPt);
		}
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
				apollonianTriangleNetwork.mode="useRandom";//"useCentroid";//
				final FractalBase frame = apollonianTriangleNetwork;
//				System.out.println("In__run__depth=="+frame.depth);==0
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(WIDTH-200, HEIGHT-200);
				frame.setRunning(true);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
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
	
	public Point centroid() {
		return new Point(
				(this.p1.x + this.p2.x + this.p3.x) / 3, 
				(this.p1.y + this.p2.y + this.p3.y) / 3);
	}
	
	public void draw(Graphics2D g) {
		g.drawLine(this.p1.x, this.p1.y, this.p2.x, this.p2.y);
		g.drawLine(this.p2.x, this.p2.y, this.p3.x, this.p3.y);
		g.drawLine(this.p1.x, this.p1.y, this.p3.x, this.p3.y);
	}
	
	
	protected void fill(Graphics2D g) {
		Path2D myPath = this.getTrianglePath();
		g.fill(myPath);
	}
	
	private Path2D getTrianglePath() {
		Path2D myPath = new Path2D.Double();
		myPath.moveTo(this.p1.x, this.p1.y);
		myPath.lineTo(this.p2.x, this.p2.y);
		myPath.lineTo(this.p3.x, this.p3.y);
		myPath.closePath();
		return myPath;
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