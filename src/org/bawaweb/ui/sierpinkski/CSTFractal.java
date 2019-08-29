/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class CSTFractal extends FractalBase {

	/**
	 * 
	 */
	public CSTFractal() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		/*Point p1 = new Point(OFFSET, OFFSET);
		Point p2 = new Point(getWidth() - OFFSET, OFFSET);*/
		
		Point p1 = new Point(getWidth() / 2, OFFSET);
		Point p2 = new Point(OFFSET, getHeight() - OFFSET);
		Point p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);
//p1(300,25), p2(25,575), p3(575,575)
		/*
		
		int length = getWidth() - (2 * OFFSET);
		Line l1 = new Line(p1.x, p1.y, length, 0.0);
		Line l2 = new Line(p1.x, p1.y, length, 300.0);
		Line l3 = new Line(p2.x, p2.y, length, 240.0);
		Point p3 = new Point((int) l3.getX2(), (int) l3.getY2());*/

		drawCSTFractal(g, depth, p1, p2, p3);

	}

	private void drawCSTFractal(Graphics2D g, int d, Point p1, Point p2, Point p3) {
		g.setColor(ColorPalette[depth+1]);
//		System.out.println("depth=== "+depth+" d== "+d+" p1("+p1.x+","+p1.y+"), p2("+p2.x+","+p2.y+"), p3("+p3.x+","+p3.y+")");
		if (d == 0) {
			drawLine(g, p1, p2);
			drawLine(g, p1, p3);
			drawLine(g, p2, p3);
			
			drawCircle(g, midpoint(p2, midpoint(p1,p3)), 5);
			
			drawSquare(g,midpoint(p1,midpoint(p2,p3)),5);
			
			return;
		}

		if (d % 2 == 0)
			g.setColor(Color.green);
		
		/*
		Line l1=new Line(p1,p2);		
		Line l2=new Line(p1,p3);		
		Line l3=new Line(p2,p3);
		l1.draw(g);
		l2.draw(g);
		l3.draw(g);
		
		g.setColor(Color.pink);
		
		Point m1 = midpoint(l1);
		Point m2 = midpoint(l2);
		Point m3 = midpoint(l3);
		
		Line lm12= new Line(m1,m2);
		Line lm13=new Line (m1,m3);
		Line lm23=new Line (m2,m3);
		
		lm12.draw(g);
		lm13.draw(g);
		lm23.draw(g);
		
		
		drawCSTFractal(g,d-1,m1,m2,m3);		
		*/
		
		Point m12 = midpoint(p1, p2);
		Point m23 = midpoint(p2, p3);
		Point m31 = midpoint(p3, p1);

		drawCSTFractal(g, d - 1, p1, m12, m31);
		drawCSTFractal(g, d - 1, m12, p2, m23);
		drawCSTFractal(g, d - 1, m31, m23, p3);

//		drawCSTFractal(g, d - 1, m12, m23, m31);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		@Override
		public void run() {
			final FractalBase frame = new CSTFractal();
			frame.setTitle(frame.getFractalShapeTitle());
			frame.setSize(frame.WIDTH, frame.HEIGHT);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setResizable(false);
			frame.setVisible(true);

			new Thread(frame).start();

		}
	});

	}

	@Override
	protected String getFractalShapeTitle() {		
		return "Bawaz _ CircleSquareTriangle";
	}

}
