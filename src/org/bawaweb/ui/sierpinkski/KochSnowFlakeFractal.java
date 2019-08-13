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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class KochSnowFlakeFractal extends FractalBase {
	private static final long serialVersionUID = 123326543L;

	public KochSnowFlakeFractal() {
		super();
	}

	private Image createKochSnowFractal(Graphics2D g) {
		Line l1 = new Line(getWidth() - 100, 150, getWidth() - 200, 180.0);
		Line l2 = new Line(100, 150, getWidth() - 200, 60.0);
		Line l3 = new Line(getWidth() - 100, 150, getWidth() - 200, 120.0);
			//	for angle re-orientation
		l3.x += Math.cos(l3.angle * (Math.PI / 180.0)) * l3.length;
		l3.y += Math.sin(l3.angle * (Math.PI / 180.0)) * l3.length;
		l3.angle -= 180.0;

		List<Line> theLines = new ArrayList<Line>();
		theLines.add(l1);
		theLines.add(l2);
		theLines.add(l3);

		drawKochSnowFractal(g, depth, theLines);
		return bufferedImage;
	}


	private void drawKochSnowFractal(Graphics2D g, int d, List<Line> lines) {
		g.setColor(Color.red);
		if (d == 0) {
			g.setStroke(new BasicStroke(1));
			for (int i = 0; i < lines.size(); i++) {
				lines.get(i).draw(g);
			}
			return;
		}

		// deleted lines
		List<Line> delLines = new ArrayList<Line>();
		// new added lines
		List<Line> addedLines = new ArrayList<Line>();

		for (int i = 0; i < lines.size(); i++) {
			Line aLine = lines.get(i);
			List<Line> newLines = create4NewLines(aLine);
			addedLines.addAll(newLines);
			delLines.add(aLine);		//	delete itself
		}

		lines.addAll(addedLines);
		lines.removeAll(delLines);

		drawKochSnowFractal(g, d - 1, lines);
	}
	
	/**
	 * 
	 * @param aLine	--	the line to create 4 lines from
	 * @return the list of lines created
	 * 
	 * input-line		________________________
	 * will
	 * then						/\
	 * become 				   /  \
	 * 4 lines			______/    \______	
	 */
	private List<Line> create4NewLines(Line aLine) {
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
			
			// for angle re-orientation
		x_l4 = x_l4 + Math.cos(ang_l4 * (Math.PI / 180.0)) * len_l4;
		y_l4 = y_l4 + Math.sin(ang_l4 * (Math.PI / 180.0)) * len_l4;
		ang_l4 -= 180.0;
		
		Line ll1 = new Line(x_l1,y_l1,len_l1,ang_l1);
		Line ll2 = new Line(x_l2,y_l2,len_l2,ang_l2);
		Line ll3 = new Line(x_l3,y_l3,len_l3,ang_l3);
		Line ll4 = new Line(x_l4,y_l4,len_l4,ang_l4);
		
		List<Line> newLines=new ArrayList<Line>();
		newLines.add(ll1);				
		newLines.add(ll2);				
		newLines.add(ll3);				
		newLines.add(ll4);
		
		return newLines;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final KochSnowFlakeFractal frame = new KochSnowFlakeFractal();
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(WIDTH, HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				new Thread(frame).start();

			}
		});
	}
	
	
	//http://www.cplusplus.com/articles/iE86b7Xj/
		class Line {
			private double x, y, length, angle;

			/**
			 * @param x	-	x-coordinate of	start point
			 * @param y	-	y-coordinate of start point
			 * @param length	-	length of line
			 * @param angle	-	line angle
			 */
			public Line(double x, double y, double length, double angle) {
				super();
				this.x = x;
				this.y = y;
				this.length = length;
				this.angle = angle;
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


	@Override
	public void createFractalShape(Graphics2D g) {
		createKochSnowFractal(g);
	}


	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz _ Koch_Snow_Flake";
	}


}
