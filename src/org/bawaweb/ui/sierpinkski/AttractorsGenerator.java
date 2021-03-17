/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */
public class AttractorsGenerator extends JFrame {

	private static final long serialVersionUID = 12345543L;

	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	public static final int DEPTH = 600;
	private int maxIter = 1500000;
	private String space2d/* = "z-x"*/;

	private String attractorName;

	private Attractor[] attractors;
	private boolean isInstantDraw;
	private int pauseTime = 10;
	BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

	private BufferedImage[] gifImages;
	private boolean isOriginUpperLeft = false;
	
	private boolean isDebug = false;//true;//

	public AttractorsGenerator() {

	}

	public AttractorsGenerator(String p) {
		this.setLocation(100,100);
		this.setSize(WIDTH, HEIGHT);
		this.attractorName = p;
	}

	public int getMaxIter() {
		return this.maxIter;
	}

	public void setMaxIter(int m) {
		this.maxIter = m;

		if (this.attractors != null && this.attractors.length > 0) {
			for(Attractor a: this.attractors){
				a.setMaxIter(this.maxIter);
			}
		}

	}

	public String getSpace2d() {
		return this.space2d;
	}

	public void setSpace2d(String sp2d) {
		this.space2d = sp2d;
		

	}

	private void setAttractorsSpace2D(String space) {
		if (this.attractors != null && this.attractors.length > 0) {
			for(Attractor a: this.attractors){
				a.setSpace2dAxes(space);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g1) {
		long cur = System.currentTimeMillis();
		super.paint(g1);
		/*System.out.println("AG[paint-1] space2d is = "+this.getSpace2d());
		System.out.println("this.attractors[0].space2dAxes is = "+this.attractors[0].space2dAxes);
		System.out.println("AG-space2d==Attr-space2D  "+(this.getSpace2d()).equals(this.attractors[0].space2dAxes));
		*/
		this.setAttractorsSpace2D(this.space2d);
		Graphics2D g2 = (Graphics2D) g1;
		Graphics2D offScreenGraphics = this.getBufferedImage() != null ? this.getBufferedImage().createGraphics()
				: null;
		if (this.isDebug) {
			System.out.println("offScreenGraphicsIs==NUll???" + (offScreenGraphics == null));
		}
		this.setSpace2d(this.space2d);
		if (this.isInstantDraw) {
			final BufferedImage attractorsImage = this.drawAttractors(offScreenGraphics);
			this.setImage(attractorsImage);
			g2.drawImage(attractorsImage, 0, 0, null);
		} else {
			this.gifImages = new BufferedImage[this.maxIter];
			this.drawAttractors(g2, offScreenGraphics, this.pauseTime);
			
			/*this.setImage(this.drawAttractors(offScreenGraphics));
			g2.drawImage(this.getBufferedImage(), 0, 0, null);*/
		}
		/*System.out.println("AG[paint-2] space2d is = "+this.getSpace2d());*/
		if (this.isDebug) {
			System.out.println("Done with AttractorsGenerator - paint ^__^");
			System.out.println("Done in " + (System.currentTimeMillis() - cur) / 1000 + " seconds");
			
		}
		g2.dispose();
		offScreenGraphics.dispose();
	}
	

	private void setImage(BufferedImage img) {
		this.bufferedImage = img;

	}

	public synchronized BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}
	
	private void drawAttractors(Graphics2D g, Graphics2D offScrnG, int pauseInterval) {
		Attractor[] attrs = this.attractors;
		if (attrs != null && attrs.length > 0) {
			Map<String, Double> rangeMap = new HashMap<String, Double>();

			for (int j = 0; j < attrs.length; j++) {
				Map<String, Double> attrSpaceMap = attrs[j].setSpace();
				attrs[j].drawAxes(offScrnG);
				rangeMap = this.setRangeMapVals(rangeMap, attrSpaceMap);
			}
			/*Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			Robot r  = null;
			try {
				r  = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}*/

			for (int i = 0; i < this.maxIter; i++) {
				for (int j = 0; j < attrs.length; j++) {
					attrs[j].draw(i, g, rangeMap);
					
					/*this.gifImages[i] = r.createScreenCapture(new Rectangle(100,100,WIDTH,HEIGHT));//(new Rectangle(dim));*/					
					/*this.gifImages[i] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
					this.gifImages[i] = this.takeScreenShot(this.getContentPane(),this.gifImages[i]);*/
					this.pause(offScrnG, pauseInterval);
					/*g.drawImage(this.getBufferedImage(), 0, 0, null);*/
				}
				
				/*System.out.println("Here i====="+i);
				
				this.gifImages[i] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				boolean drwn = g.drawImage(this.gifImages[i],0,0,null);
				System.out.println("For [i] ("+i+")   drwn=="+drwn);*/
/*				
//				this.gifImages[i] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
				g.drawImage(this.getBufferedImage(),0,0,this);
				this.gifImages[i] = this.getBufferedImage();*/
			}
			/*System.out.println("NumGifs==="+this.gifImages.length);
			this.createGif(this.gifImages);
			System.out.println("Done " + System.currentTimeMillis());*/
		}
	}

	private BufferedImage takeScreenShot(Component comp, BufferedImage image) {
		//comp.paint( image.getGraphics());
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		Robot r;
			try {
				r = new Robot();
				image = r.createScreenCapture(new Rectangle(dim));
			} catch (AWTException e) {
				e.printStackTrace();
			}
		
		return image;
	}

	private void createGif(BufferedImage[] images) {
		if (isDebug) {
			System.out.println("in_createGif");
		}
		BufferedImage first = images[0];
	     try {
			File outputfile = new File("C:\\Users\\Navroz\\Desktop\\" + System.currentTimeMillis() + "gif.mp4");
			ImageOutputStream output = new FileImageOutputStream(outputfile);//new File("/tmp/example.gif"));
			GifSequenceWriter writer = new GifSequenceWriter(output, first.getType(), this.pauseTime/*250*/, true);
	        writer.writeToSequence(first);
	        
	        for(int i = 1; i <images.length;i++){
	        	writer.writeToSequence(images[i]);
	        }

	        writer.close();
	        output.close();
	        System.out.println("Created gif at "+ outputfile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private BufferedImage drawAttractors(Graphics2D g) {
		Attractor[] attrs = this.attractors;
		if (attrs != null && attrs.length > 0) {
			Map<String, Double> rangeMap = new HashMap<String, Double>();

			for (int j = 0; j < attrs.length; j++) {
				Map<String, Double> attrSpaceMap = attrs[j].setSpace();
				attrs[j].drawAxes(g);
				rangeMap = this.setRangeMapVals(rangeMap, attrSpaceMap);
			}
			
			
			if (this.isDebug) {
				this.printRangeMap(rangeMap);
				System.out.println(this.checkRangeMap(rangeMap, attrs[0].is3D()));
				String isNotOk = "rangeMap is NOT ok    Gr@#$^$";
				if (isNotOk.equals(this.checkRangeMap(rangeMap, attrs[0].is3D()))) {
					System.out.println("Uh-Oh  spaceMap ranges undefined");
					return null;
				} 
			}
			
			for (int i = 0; i < this.maxIter; i++) {
				for (int j = 0; j < attrs.length; j++) {
					attrs[j].draw(i, g, rangeMap);
				}
			}
			System.out.println("Done instantDraw  " + System.currentTimeMillis());
		}
		return this.getBufferedImage();

//		
//		
//		
//		
//		
//			///////////////////////////endsRemove4now////////////////////////////////	
//		
//		Attractor attr1 = null, attr2 = null, attr3 = null;
//		
//		if (this.attractorName.equals("lorenz")) { // seed l1 & l2
//			/*attr1 = new LorenzAttractor(0.0, 20.00, 25.0, Color.blue, "z-y");
//			attr2 = new LorenzAttractor(0.0, 20.01, 25.0, Color.red, "z-y");
//			attr3 = new LorenzAttractor(0.0, 20.00, 25.1, Color.green.darker().darker(), "z-y");*/
////			this.space2d
//			//	all with same seeds & different 2dspace
//			attr1 = new LorenzAttractor(0.0, 20.00, 25.0, Color.blue, "z-y");
//			attr2 = new LorenzAttractor(0.0, 20.00, 25.0, Color.red, "z-x");
//			attr3 = new LorenzAttractor(0.0, 20.00, 25.0, Color.green.darker().darker(), "x-y");
//
//		} else if (this.attractorName.equals("aizawa")) {
//			attr1 = new AizawaAttractor(0.1000, 0.0000, 0.0000, Color.red, "y-x");
//			attr2 = new AizawaAttractor(0.1000, 0.5000, 0.0000, Color.blue, "y-x");
//			attr3 = new AizawaAttractor(0.1000, 0, 0.5000, Color.green.darker().darker(), "y-x");
//
//		} else if (this.attractorName.equals("dejong")) {
//			attr1 = new DeJongAttractor(1, 1, 1, Color.red, "x-y");
//			attr2 = new DeJongAttractor(1, 1.5, 1, Color.blue, "x-y");
//			/*attr1 = new DeJongAttractor(0.1, 0, 0, Color.red,"x-y");
//			attr2 = new AizawaAttractor(0.1, 0.5, 0, Color.blue,"x-y");*/
//			attr3 = new DeJongAttractor(1, 1, 1.5, Color.green.darker().darker(), "x-y");
//
//		}
//
//		attr1.drawAxes(g); // only doing once
////		System.out.println("xmax= "+attr1.xmax+" xmin="+attr1.xmin+" xRange="+(attr1.xmax-attr1.xmin)+" xC="+(attr1.xmin+(attr1.xmax-attr1.xmin)/2));
////		System.out.println("ymax= "+attr1.ymax+" ymin="+attr1.ymin+" yRange="+(attr1.ymax-attr1.ymin)+" yC="+(attr1.ymin+(attr1.ymax-attr1.ymin)/2));
////		System.out.println("zmax= "+attr1.zmax+" zmin="+attr1.zmin+" zRange="+(attr1.zmax-attr1.zmin)+" zC="+(attr1.zmin+(attr1.zmax-attr1.zmin)/2));
//		for (int i = 0; i < maxIter; i++) {
//			attr1.draw(g);
//			attr2.draw(g);
//			attr3.draw(g);
//			pause(10);
//		}
//
//		System.out.println("Done" + System.currentTimeMillis());
		///////////////////////////endsRemove4now////////////////////////////////

	}
	
	
	private void printRangeMap(Map<String, Double> rMap) {
		Set<String> kSet = rMap.keySet();
		Iterator<String> itr = kSet.iterator();
		while(itr.hasNext()) {
			String key = itr.next();
			Double value = rMap.get(key);
			
			System.out.println("["+key+"] ===>  ["+value+"]");
		}
		
	}

	private String checkRangeMap(Map<String, Double> rngMap, boolean is3d) {
		String isOk = "rangeMap is OK   ^__^";
		String isNotOk = "rangeMap is NOT ok    Gr@#$^$";
		try {
			Set<String> kSet = rngMap.keySet();
			Iterator<String> itr = kSet.iterator();
			while (itr.hasNext()) {
				String key = itr.next();
				Double value = rngMap.get(key);

				if (value == null) {
					if (is3d) {
						return isNotOk;
					} else {
						if (!key.startsWith("z")) {
							return isNotOk;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return isNotOk;
		}
		
		return isOk;
	}

	private Map<String, Double> setRangeMapVals(Map<String, Double> rangeMap, Map<String, Double> spaceMap) {
		if (rangeMap.get("xmin") != null) {
			if (rangeMap.get("xmin") > spaceMap.get("xmin")) {
				rangeMap.put("xmin", spaceMap.get("xmin"));
			}
		} else {
			rangeMap.put("xmin", spaceMap.get("xmin"));
		}
		
		if (rangeMap.get("xmax") != null) {
			if (rangeMap.get("xmax") < spaceMap.get("xmax")) {
				rangeMap.put("xmax", spaceMap.get("xmax"));
			}
		} else {
			rangeMap.put("xmax", spaceMap.get("xmax"));
		}
		
		if (rangeMap.get("ymin") != null) {
			if (rangeMap.get("ymin") > spaceMap.get("ymin")) {
				rangeMap.put("ymin", spaceMap.get("ymin"));
			}
		} else {
			rangeMap.put("ymin", spaceMap.get("ymin"));
		}
		
		if (rangeMap.get("ymax") != null) {
			if (rangeMap.get("ymax") < spaceMap.get("ymax")) {
				rangeMap.put("ymax", spaceMap.get("ymax"));
			}
		} else {
			rangeMap.put("ymax", spaceMap.get("ymax"));
		}
		
		if (rangeMap.get("zmin") != null) {
			if (rangeMap.get("zmin") > spaceMap.get("zmin")) {
				rangeMap.put("zmin", spaceMap.get("zmin"));
			}
		} else {
			rangeMap.put("zmin", spaceMap.get("zmin"));
		}
		
		if (rangeMap.get("zmax") != null) {
			if (rangeMap.get("zmax") < spaceMap.get("zmax")) {
				rangeMap.put("zmax", spaceMap.get("zmax"));
			}
		} else {
			rangeMap.put("zmax", spaceMap.get("zmax"));
		}
		return rangeMap;
		
		
	}

	/**
     * Pause for t milliseconds. This method is intended to support computer animations.
	 * @param g2 
     * @param t number of milliseconds
     */
    public /*static */void pause(Graphics2D g2, int t) {
        try {

			g2.drawImage(this.getBufferedImage(), 0, 0, null);
            Thread.sleep(t);
        }
        catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final AttractorsGenerator lorenz_Attractor = new AttractorsGenerator("lorenz");//("dejong");//("aizawa");//
				
				final LorenzAttractor l1 = new LorenzAttractor(0.0, 20.00, 25.0, Color.blue);
				final LorenzAttractor l2 = new LorenzAttractor(0.0, 20.01, 25.0, Color.red);
				final LorenzAttractor l3 = new LorenzAttractor(0.0, 20.00, 25.01, Color.green);
				l1.setPixellated(false);	//l1.setPixellated(true);
				l2.setPixellated(false);	//l2.setPixellated(true);
				l3.setPixellated(false);	//l3.setPixellated(true);
				lorenz_Attractor.setAttractors(new Attractor[] { 
						l1,
						l2,
						l3 });
				
				lorenz_Attractor.setMaxIter(500/*000*/);
				lorenz_Attractor.setSpace2d("x-z");
				lorenz_Attractor.setDebug(false);
				final JFrame frame = lorenz_Attractor;//("dejong");//("aizawa");//
				
				frame.setTitle("BawazAttractor");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
			}
		});
		
		
		
	}

	public void setAttractors(Attractor[] attrs) {
		this.attractors = attrs;
	}
	
	public void setAttractors(List<Attractor> attrList) {
		final int numAttrs = attrList.size();
		Attractor[] attractorArr = new Attractor[numAttrs];
		for (int i = 0; i < numAttrs; i++) {
			attractorArr[i] = attrList.get(i);
		}
		this.setAttractors(attractorArr);
	}

	public boolean isInstantDraw() {
		return isInstantDraw;
	}

	public void setInstantDraw(boolean isInstantDraw) {
		this.isInstantDraw = isInstantDraw;
	}

	public int getPauseTime() {
		return pauseTime;
	}

	public void setPauseTime(int pauseTime) {
		this.pauseTime = pauseTime;
	}

	public boolean isOriginUpperLeft() {
		return this.isOriginUpperLeft;
	}

	public void setOriginUpperLeft(boolean isOrigUprLft) {
		this.isOriginUpperLeft = isOrigUprLft;

		if (this.attractors != null && this.attractors.length > 0) {
			for (Attractor a : this.attractors) {
				a.setOriginUpperLeft(isOrigUprLft);
			}
		}
	}

	public boolean isDebug() {
		return this.isDebug;
	}

	public void setDebug(boolean isDbg) {
		this.isDebug = isDbg;

		if (this.attractors != null && this.attractors.length > 0) {
			for (Attractor a : this.attractors) {
				a.setDebug(isDbg);
			}
		}
	}


}
