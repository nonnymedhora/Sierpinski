/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 * 
 *         Apollonian network is an undirected graph formed by a process of
 *         recursively subdividing a triangle into three smaller triangles.
 */
public class ApollonianNetwork extends JFrame implements Runnable {

	private static final long serialVersionUID = 122343L;
	private static final int HEIGHT = 600;
	private static final int WIDTH = 600;

	public static int OFFSET = 25; // pixel offset from edge

	private static int depth; // recursion depth
	private static final int MAX_DEPTH = 10;

	public ApollonianNetwork() {
		setSize(WIDTH, HEIGHT);
		setVisible(true);

	}

	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		Image img = createApollonianNetwork();
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(img, null, null);
		g2.dispose();
	}

	private Image createApollonianNetwork() {
		BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bufferedImage.createGraphics();
		// Clear the frame
		/*g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);*/
		
		Point p1 = new Point(OFFSET, OFFSET);
		Point p2 = new Point(getWidth() - OFFSET, getHeight() - OFFSET);
		/*
		 * 	p1 == (25, 25)
		 *	p2 == (575, 575)
		 */
		drawNetwork(g,depth,p1,p2);
		return bufferedImage;
	}
	

	private void drawNetwork(Graphics2D g, int dep, Point p1,Point p2) {
		System.out.println("p1 == ("+p1.x+", "+p1.y+")");
		System.out.println("p2 == ("+p2.x+", "+p2.y+")");
		System.out.println("depth -- "+dep);
		if (dep % 2 == 0) {
			g.setColor(Color.red);
		} else {
			g.setColor(Color.blue);
		}
		if (dep == 0) {
			g.setColor(Color.yellow);
			g.drawOval(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y);
			return;
		}

		Point m1 = midpoint(p1,p2);
		Point m2 = midpoint(p1,m1);
		Point m3 = midpoint(m1,p2);
		
		g.setColor(Color.green);
		g.drawOval(p1.x, p1.y, m1.x-p1.x, m1.y-p1.y);
		
		g.setColor(Color.blue);
		g.drawOval(p1.x,p1.y,m2.x-p1.x,m2.y-p1.y);
		
		g.setColor(Color.white);
		g.drawOval(m1.x,m1.y,m3.x-m2.x,m3.y-m2.y);
		
		drawNetwork(g,dep-1,m1,m2);
		drawNetwork(g,dep-1,m1,m3);
		drawNetwork(g,dep-1,m2,m3);
		
	}

	/** = the midpoint between p1 and p2 */
	private static Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}
	
	private void ApollonianNetwork(int dep){
		depth = dep;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (depth < 4/*MAX_DEPTH*/) {
				depth += 1;
				Thread.sleep(6000);
				ApollonianNetwork(depth);
				run();
			}
		} catch (InterruptedException ex) {
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final ApollonianNetwork frame = new ApollonianNetwork();
				frame.setTitle("Bawaz _ ApollonianNetwork");
				frame.setSize(WIDTH, HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				new Thread(frame).start();

			}
		});
	}

}
