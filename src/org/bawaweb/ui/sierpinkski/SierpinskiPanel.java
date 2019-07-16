package org.bawaweb.ui.sierpinkski;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 */

/**
 * @author Navroz
 *         https://stackoverflow.com/questions/29447994/how-do-i-draw-a-triangle-in-java
 */
public class SierpinskiPanel extends JPanel implements Runnable {

	private static final int PREF_W = 600;
	private static final int PREF_H = 600;
	private static final Color COLOR_1 = Color.blue;
	private static final Color COLOR_2 = Color.red;
	private static final Paint GRADIENT_PAINT = new GradientPaint(0, 0, COLOR_1, 20, 20, COLOR_2, true);
	private Path2D myPath = new Path2D.Double();
	
	boolean started=false;
	boolean stopped = false;
	
	int numGens = 0;

	public SierpinskiPanel() {
		/*started=true;*/
//		new Thread(this).start();
		
		/*double firstX = (PREF_W / 2.0) * (1 - 1 / Math.sqrt(3));
		double firstY = 3.0 * PREF_H / 4.0;

		myPath.moveTo(firstX, firstY);
		myPath.lineTo(PREF_W - firstX, firstY);
		myPath.lineTo(PREF_W / 2.0, PREF_H / 4.0);
		myPath.closePath();*/
	}

//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//	         public void run() {
//	            createAndShowGui();
//	         }
//
//			private void createAndShowGui() {
//				JFrame frame = new JFrame("Path2DExample");
//			      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//			      SierpinskiPanel mainPanel = new SierpinskiPanel();
//				frame.getContentPane().add(mainPanel );
//			      frame.pack();
//			      frame.setLocationByPlatform(true);
//			      frame.setVisible(true);
//				
//			}
//	      });
//	}

	@Override
	public Dimension getPreferredSize() {
		if (isPreferredSizeSet()) {
			return super.getPreferredSize();
		}
		return new Dimension(PREF_W, PREF_H);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// to smooth out the jaggies
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setPaint(GRADIENT_PAINT); // just for fun!
		g2.fill(myPath); // fill my triangle
	}

	@Override
	public void run() {
		System.out.println("Started is "+started);
		System.out.println("Stopped is "+stopped);
		while (!stopped) {
			numGens+=1;
			drawTriangle(numGens);
			try{
				Thread.sleep(3000);
			} catch (InterruptedException ex) {
			}
		}
	}

	private void drawTriangle(int num) {
		double width = PREF_W * 1/num;
		final int height = PREF_H * 1/num;
		double firstX = (width / 2.0) * (1 - 1 / Math.sqrt(3));
		double firstY = 3.0 * height / 4.0;
		myPath.moveTo(firstX, firstY);
		myPath.lineTo(width - firstX, firstY);
		myPath.lineTo(width / 2.0, height / 4.0);
		myPath.closePath();
	}
	
	
	
//	
//	private class MyMouseAdapter extends MouseAdapter {
//	      private Point pPressed = null;
//
//	      @Override
//	      public void mousePressed(MouseEvent e) {
//	         if (e.getButton() != MouseEvent.BUTTON1) {
//	            return;
//	         }
//	         if (myPath.contains(e.getPoint())) {
//	            pPressed = e.getPoint();
//	         }
//	      }
//
//	      public void mouseDragged(MouseEvent e) {
//	         drag(e);
//	      }
//
//	      @Override
//	      public void mouseReleased(MouseEvent e) {
//	         drag(e);
//	         pPressed = null;
//	      }
//
//	      private void drag(MouseEvent e) {
//	         if (pPressed == null) {
//	            return;
//	         }
//	         Point p = e.getPoint();
//	         int tx = p.x - pPressed.x;
//	         int ty = p.y - pPressed.y;
//	         AffineTransform at = AffineTransform.getTranslateInstance(tx, ty);
//	         myPath.transform(at);
//	         pPressed = p;
//	         repaint();
//	      };
//
//	   }

}
