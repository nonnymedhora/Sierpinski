package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SierpinskiTriangle extends FractalBase {
	

	private static final int NUM_SQUARES = 3;

	private static final long serialVersionUID = 123456543L;
	
	private static final Color COLOR_BLACK = Color.black;
	private static final Color COLOR_1 = Color.red;
	private static final Color COLOR_2 = Color.green;
	private static final Color COLOR_3 = Color.blue;
	private static final Color COLOR_4 = Color.yellow;

	private boolean filledInnerTriangles = false;
	private String direction = "DOWN";

	private boolean isCreateGasket = false;

	/** Constructor: an instance */
	public SierpinskiTriangle() {
		super();
	}
	
	
	/**
	 * Constructor
	 * @param innerFilled	==	inner triangles filled
	 */
	public SierpinskiTriangle(final boolean innerFilled) {
		this();
		this.setFilledInnerTriangles(innerFilled);
	}
	
	/**
	 * Constructor
	 * @param dir	==	the direction is either "UP" or "DOWN"
	 * @param allFilled	==	external circum-triangle filled
	 * @param innerFilled	==	inner triangles filled
	 */
	public SierpinskiTriangle(final String dir, final boolean innerFilled) {
		this(innerFilled);
		this.setDirection(dir);
	}
	
	private Image createSierpinskiT(final Graphics2D g, final int d) {
		// Clear the frame
		g.setColor(COLOR_BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(COLOR_3);
		
		Point p1, p2, p3 = null;

		if (this.getDirection().equals("UP")) {
			// Initialize p1, p2, p3 based on frame size
			p1 = new Point(getWidth() / 2, OFFSET);
			p2 = new Point(OFFSET, getHeight() - OFFSET);
			p3 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);
		} else {
			// upsideDown
			p1 = new Point(OFFSET, OFFSET);
			p2 = new Point(getWidth() - OFFSET, OFFSET);
			p3 = new Point(getWidth() / 2, getHeight() - OFFSET);
		}
		// Draw Sierpinski's triangle
		this.drawTriangles(g, depth, p1, p2, p3);
		return this.bufferedImage;
	}
	
	
	/**
	 * Draw a Sierpinski triangle of depth d with perimeter given by p1, p2, p3.
	 * p1-p2 is the base line, p3 the top point
	 */
	private void drawTriangles(final Graphics2D g, final int d, final Point p1, final Point p2, final Point p3) {
		g.setColor(COLOR_3);
		if (d == 0) { // depth is 0, draw the triangle
			g.setStroke(new BasicStroke(1));
			this.drawLine(g, p1, p2);
			this.drawLine(g, p2, p3);
			this.drawLine(g, p3, p1);
			return;
		}
		// Draw three Sierpinski triangles of depth d-1
		Point m12 = this.midpoint(p1, p2);
		Point m23 = this.midpoint(p2, p3);
		Point m31 = this.midpoint(p3, p1);
		
		Color fillColor = COLOR_1;
		Color emptyColor = COLOR_2;
		
		if (d % 2 == 0) {
			fillColor = COLOR_4;	//yellow
		}

		if (this.isFilledInnerTriangles()) {
			//		extra
			fillTriangle(g, p1, m12, m31, fillColor);
			fillTriangle(g, m12, p2, m23, fillColor);
			fillTriangle(g, p3, m23, m31, fillColor);
			//		ends-extra
		}
		
		this.fillTriangle(g, m12, m23, m31, emptyColor);		
		
		/*drawTriangle(g,p1,m12,m31,g.getColor());
		drawTriangle(g,m12,p2,m23,g.getColor());
		drawTriangle(g,m31,m23,p3,g.getColor());*/

		this.drawTriangles(g, d - 1, p1, m12, m31);
		this.drawTriangles(g, d - 1, m12, p2, m23);
		this.drawTriangles(g, d - 1, m31, m23, p3);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				/*final FractalBase frame = new SierpinskiTriangle("UP", true);//false);*/				SierpinskiTriangle st = new SierpinskiTriangle();
				st.setDirection("DOWN");//("UP");
				st.createGasket();
				final FractalBase frame = st;
				
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

	@Override
	public void createFractalShape(final Graphics2D g) {
		if (!isCreateGasket) {
			createSierpinskiT(g, depth);
		} else {
			createSierpinskiGasket(g, depth);
		}
	}
	

	private Image createSierpinskiGasket(final Graphics2D g, final int d) {
		// Clear the frame
		g.setColor(COLOR_BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		Square startSquare = new Square(OFFSET, OFFSET, getWidth() - (2 * OFFSET), Color.PINK);

		Square[] ss = this.breakSquares(startSquare);

		// Draw Sierpinski's Gasket
		this.drawSierpinskiGasket(g, depth, ss);// s1, s2, s3);

		return this.bufferedImage;

	}


	private void drawSierpinskiGasket(final Graphics2D g, final int d, final Square[] squares){//Square sq1, Square sq2, Square sq3) {
		if (d == 0) {
			this.fillSquares(g, squares);
			return;
		}
		
		this.removeSquares(g, squares);
		
		for(Square aSquare : squares) {
			Square[] bSquares = this.breakSquares(aSquare);

			this.drawSierpinskiGasket(g, d - 1, bSquares);
		}
	}


	private void fillSquares(final Graphics2D g, final Square[] squares) {
		for (int i = 0; i < squares.length; i++) {
			g.setColor(squares[i].color);
			fillSquare(g, squares[i].xpos, squares[i].ypos, squares[i].length);
		}
	}
	
	private void removeSquares(Graphics2D g, Square[] squares) {
		for (int i = 0; i < squares.length; i++) {
			g.setColor(COLOR_BLACK);
			fillSquare(g, squares[i].xpos, squares[i].ypos, squares[i].length);
		}
	}


	private Square[] breakSquares(final Square sq) {
		Square[] br = new Square[NUM_SQUARES];
		final int halfSide = sq.length / 2;
		final int qrtrSide = sq.length / 4;
		
		if (this.getDirection().equals("UP")) {
//			//	 ___
//			//	|   |			 []
//			//	|___|	==>		[][]
//			//
			br[0] = new Square(sq.xpos + qrtrSide, sq.ypos, halfSide, COLOR_1);
			br[1] = new Square(sq.xpos, sq.ypos + halfSide, halfSide, COLOR_2);
			br[2] = new Square(sq.xpos + halfSide, sq.ypos + halfSide, halfSide, COLOR_3);
		} else {
//			//	 ___
//			//	|   |			[][]
//			//	|___|	==>		 []
//			//
			br[0] = new Square(sq.xpos, sq.ypos, halfSide, COLOR_1);
			br[1] = new Square(sq.xpos + halfSide, sq.ypos, halfSide, COLOR_2);
			br[2] = new Square(sq.xpos + qrtrSide, sq.ypos + halfSide, halfSide, COLOR_3);
		}
		return br;
	}

	@Override
	protected String getFractalShapeTitle() {		
		return isCreateGasket?"Bawaz _ Sierpinski'Gasket":"Bawaz _ Sierpinski'Triangle";
	}	

	public boolean isFilledInnerTriangles() {
		return this.filledInnerTriangles;
	}

	public void setFilledInnerTriangles(boolean filledITriangles) {
		this.filledInnerTriangles = filledITriangles;
	}

	public String getDirection() {
		return this.direction;
	}

	public void setDirection(String dir) {
		this.direction = dir;
	}


	public void createGasket() {
		this.setCreateGasket(true);		
	}


	public boolean isCreateGasket() {
		return isCreateGasket;
	}


	public void setCreateGasket(boolean isCreateGasket) {
		this.isCreateGasket = isCreateGasket;
	}
	
	private class Square {
		private int xpos;
		private int ypos;
		private int length;
		private Color color;

		public Square(int x, int y, int size, Color clr) {
			this.xpos = x;
			this.ypos = y;
			this.length = size;
			this.color = clr;
		}

	}
}
