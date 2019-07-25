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
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class KochFractal extends JFrame implements Runnable {
	private static final long serialVersionUID = 124986543L;
	private static final int HEIGHT = 600;
	private static final int WIDTH = 600;

	public static int OFFSET = 25; // pixel offset from edge

	private static int depth; // recursion depth
	private static final int MAX_DEPTH = 10;

	public KochFractal(){
		setSize(WIDTH, HEIGHT);
		setVisible(true);
	}

	@Override
	public void paint(Graphics g1) {
		Image img = createKochFractal();
		Graphics2D g = (Graphics2D) g1;
		g.drawImage(img, null, null);
		g.dispose();
	}
	
	private Image createKochFractal() {
		BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		// Clear the frame
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.green);

		// Initialize p1, p2, p3 based on frame size
		/*Point p1 = new Point(getWidth() / 2, OFFSET);
		Point p2 = new Point(OFFSET, getHeight() - OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);*/
		//TOREMOVELTR
		/*System.out.println("depth==="+depth);
		System.out.println("P1("+p1.x+","+p1.y+")");
		System.out.println("P2("+p2.x+","+p2.y+")");
		System.out.println("P3("+p3.x+","+p3.y+")");*/
		/*
		 * 	P1(300,25)
			P2(25,575)
			P3(575,575)
		*/
//		theLines.add(new Line(p2.x,p2.y,distance(p2,p3),180.0));
//		theLines.add(new Line(p1.x,p1.y,distance(p1,p2),60.0));
//		
//		Line aLine = new Line(p3.x,p3.y,distance(p1,p3),120.0);
////		aLine.x += Math.cos(aLine.angle*(Math.PI/180.0))*aLine.length;
////		aLine.y += Math.sin(aLine.angle*(Math.PI/180.0))*aLine.length;
////		aLine.angle-=180.0;
//		theLines.add(aLine);
		
		theLines.add(new Line(getWidth()-100,150,getWidth()-200,180.0));
		theLines.add(new Line(100,150,getWidth()-200,60.0));
		Line aLine = new Line(getWidth()-100,150,getWidth()-200,120.0);
		aLine.x += Math.cos(aLine.angle*(Math.PI/180.0))*aLine.length;
		aLine.y += Math.sin(aLine.angle*(Math.PI/180.0))*aLine.length;
		aLine.angle-=180.0;
		theLines.add(aLine);
		for(int i = 0; i < theLines.size();i++){
			theLines.get(i).draw(g);
		}
		drawKochFractal(theLines,g);
//		for(int i = 0; i < theLines.size();i++){
//			theLines.remove(i);
//		}
		return bufferedImage;
	}



	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (depth < 7/*MAX_DEPTH*/) {
				Thread.sleep(3000);
				depth += 1;
				Thread.sleep(3000);
				kochFractal(depth);
				run();
			}
		} catch (InterruptedException ex) {
		}
	}

	private void kochFractal(int d) {
		depth = d;
		repaint();		
	}


	
	private static double distance(Point p1, Point p2) {
		return Math.sqrt( Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) );
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final KochFractal frame = new KochFractal();
				frame.setTitle("Bawaz _ KochFractal");
				frame.setSize(WIDTH, HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				new Thread(frame).start();

			}
		});
	}
	
	
	private List<Line> theLines = new ArrayList<Line>();
	
	private void drawKochFractal(List<Line> lines, Graphics2D g) {
		// new lines
		List<Line> newLines = new ArrayList<Line>();
		// deleted lines
		List<Line> delLines = new ArrayList<Line>();

		for (int i = 0; i < lines.size(); i++) {
			Line aLine = lines.get(i);
			create4NewLines(newLines, aLine);
			delLines.add(aLine);
		}
		
		for(int j=0;j<newLines.size();j++){
			lines.add(newLines.get(j));
		}
		
		for(int k = 0; k < delLines.size();k++){
			Line line2Del = delLines.get(k);
			lines.remove(line2Del);
//			delLines.remove(k);
			line2Del=null;
			
//			line2Del.removeMidThrd(g);
			
//			line2Del.undraw(g);
			/*
			line2Del=null;*/
			/*Line aLine = line2Del;
			aLine=null;*/
		}
		
//		drawKochFractal(lines,g);
	}

	private void create4NewLines(List<Line> newLines, Line aLine) {
		double x_l1 = aLine.x;
		double y_l1 = aLine.y;
		double len_l1 = aLine.length / 3.0;
		double ang_l1 = aLine.angle;

		double x_l2 = aLine.x + (Math.cos(aLine.angle * (Math.PI / 180.0)) * aLine.length / 1.5);
		double y_l2 = aLine.y + (Math.sin(aLine.angle * (Math.PI / 180.0)) * aLine.length / 1.5);
		double len_l2 = aLine.length / 3.0;
		double ang_l2 = aLine.angle;

		double x_l3 = aLine.x + (Math.cos(aLine.angle * (Math.PI / 180.0)) * aLine.length / 3.0);
		double y_l3 = aLine.y + (Math.sin(aLine.angle * (Math.PI / 180.0)) * aLine.length / 3.0);
		double len_l3 = aLine.length / 3.0;
		double ang_l3 = aLine.angle - 300.0;

		double x_l4 = aLine.x + (Math.cos(aLine.angle * (Math.PI / 180.0)) * aLine.length / 1.5);
		double y_l4 = aLine.y + (Math.sin(aLine.angle * (Math.PI / 180.0)) * aLine.length / 1.5);
		double len_l4 = aLine.length / 3.0;
		double ang_l4 = aLine.angle - 240.0;
		
		x_l4 = x_l4 + Math.cos(ang_l4 * (Math.PI / 180.0)) * len_l4;
		y_l4 = y_l4 + Math.sin(ang_l4 * (Math.PI / 180.0)) * len_l4;
		ang_l4 -= 180.0;
		
		newLines.add(new Line(x_l1,y_l1,len_l1,ang_l1));				
		newLines.add(new Line(x_l2,y_l2,len_l2,ang_l2));				
		newLines.add(new Line(x_l3,y_l3,len_l3,ang_l3));				
		newLines.add(new Line(x_l4,y_l4,len_l4,ang_l4));
	}
	
	//http://www.cplusplus.com/articles/iE86b7Xj/
	class Line /*extends Line2D*/{
		private double x, y, length, angle;

		/**
		 * @param x
		 * @param y
		 * @param length
		 * @param angle
		 */
		public Line(double x, double y, double length, double angle) {
			super();
			this.x = x;
			this.y = y;
			this.length = length;
			this.angle = angle;
		}

		public void removeMidThrd(Graphics2D g) {
			Point pa = new Point((int)x,(int) y);
			Point pb = new Point((int)getX2(),(int)getY2());
			
			Point thirdPt = thirdPt(pa,pb);
			Point twoThirdPt = twoThirdPt(pa,pb);
			
			/*
			g.setColor(Color.green);
			g.drawLine(thirdPt.x,thirdPt.y,twoThirdPt.x,twoThirdPt.y);*/
			g.setColor(Color.blue);
			g.setStroke(new BasicStroke(2));
			g.drawLine(thirdPt.x,thirdPt.y,twoThirdPt.x,twoThirdPt.y);
			
			
		}

		// Getting the second x coordinate based on the angle and length
		public double getX2() {
			return x + Math.cos(angle * (Math.PI / 180.0)) * length;
		}

		// Getting the second y coordinate based on the angle and length
		public double getY2() {
			return y + Math.sin(angle * (Math.PI / 180.0)) * length;
		}

		public void draw(Graphics g) {
			g.drawLine((int) x, (int) y, (int) getX2(), (int) getY2());
		}
		
		public void undraw(Graphics2D g){
			g.setPaint(Color.yellow);
			g.drawLine((int) x, (int) y, (int) getX2(), (int) getY2());
			
			g.setColor(Color.black);
			Point thPt = thirdPt(new Point((int)x,(int)y), new Point((int)getX2(),(int)getY2()));
			Point twoThPt=twoThirdPt( new Point( (int)x,(int)y) , new Point((int)getX2(),(int)getY2()));
			drawLine(g,thPt,twoThPt);
		}
		
		public void drawLine(Graphics2D g, Point p1, Point p2) {
			g.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
		
		
		public Point thirdPt(Point pLeft, Point pRight) {
			return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 3), (int) (pLeft.y + (pRight.y - pLeft.y) / 3));
		}
		
		public Point twoThirdPt(Point pLeft, Point pRight) {
			return new Point((int) (pLeft.x + (pRight.x - pLeft.x) * 2 / 3), (int) (pLeft.y + (pRight.y - pLeft.y) * 2 / 3));
		}

		

	}

}
