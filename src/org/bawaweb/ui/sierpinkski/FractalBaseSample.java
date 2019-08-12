package org.bawaweb.ui.sierpinkski;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class FractalBaseSample extends FractalBase {
	
	public FractalBaseSample(){
		super();
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void createFractalShape(Graphics2D g) {
		final Point center = new Point(WIDTH / 2, HEIGHT / 2);
		// System.out.println("Center is P(" + center.x + "," + center.y + ")");

		g.setStroke(new BasicStroke(4));
		setPixel(center.y, center.x, Color.cyan.getRGB());

		final int length = 50 * depth + 1;
//		System.out.println("length is " + length);
		
		g.setStroke(new BasicStroke(1));
		drawSquare(g, center, length * 2, Color.cyan);

		g.setColor(Color.green);
		drawSquare(g, center, length);

		g.setColor(Color.red);
		drawCircle(g, center, length);

		drawCircle(g, center, length / 2, Color.yellow);
		fillCircle(g, center, length / 3, Color.green);
		fillSquare(g, center, length / 4, Color.blue);

//		g.setStroke(new BasicStroke(2));
		g.setColor(Color.white);
		drawEquilateralTriangle(g, center, length * 2, "up");
		drawEquilateralTriangle(g, center, length * 2, "down");

		g.setColor(Color.red);
		drawEquilateralTriangle(g, center, length, "up");
		drawEquilateralTriangle(g, center, length, "down");

		g.setColor(Color.red);
		fillEquilateralTriangle(g, center, length / 6, "up");
		fillEquilateralTriangle(g, center, length / 6, "down");
		
	}

	public static void main(String[] arf) {
		final FractalBase ff = new FractalBaseSample();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final FractalBase frame = ff;
				frame.setTitle(ff.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
	
				new Thread(frame).start();
	
			}
		});
	}

	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz _ FractalBaseSample";
	}
	
}