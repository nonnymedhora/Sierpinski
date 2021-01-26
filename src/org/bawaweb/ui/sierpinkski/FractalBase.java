/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;


/**
 * @author Navroz
 * Superclass for SierpinskiTriangle, SiepinskiSquare, KochSnowflake
 */
public abstract class FractalBase extends JFrame implements Runnable, MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 123456543L;
	
	private boolean smoothen = false;
	
	private boolean captureOrbit = false;
	protected LinkedHashMap<ComplexNumber, List<Double>> orbitMap = new LinkedHashMap<ComplexNumber,List<Double>>();
	//for computecolors
	//	for divisors
	public static final int[] FRST_SIX_PRIMES 	= new int[] { 2, 3, 5, 7, 11, 13 };
	public static final int[] FRST_SIX_ODDS 	= new int[] { 3, 5, 7, 9, 11, 13 };
	public static final int[] FRST_SIX_FIBS 	= new int[] { 3, 5, 8, 13, 21, 34 };

	// for startVals
	public static final int[] POW2_4_200 		= new int[] { 4, 8, 16, 32, 64, 128, 200 };
	public static final int[] POW2_2_128 		= new int[] { 2, 4, 8, 16, 32, 64, 128 };
	public static final int[] POW2_2_F4 		= new int[] { 4, 8, 16, 32, 64, 128 };
	public static final int[] POW3_3_243 		= new int[] { 1, 3, 9, 27, 81, 243 };
	public static final int[] EQUAL_PARTS_40 	= new int[] { 40, 80, 120, 160, 200, 240 };
	public static final int[] EQUAL_PARTS_50 	= new int[] { 10, 50, 100, 150, 200, 250 };
	public static final int[] EQUAL_PARTS_25 	= new int[] { 25, 65, 105, 145, 185, 225 };

	public static final int[] FRST_FOUR_PRIMES = new int[] { 2, 3, 5, 7 };
	public static final int[] POW2_4_32 		= new int[] { 4, 8, 16,32 };
	
	private int[] rgbDivisors = FRST_FOUR_PRIMES; //FRST_SIX_PRIMES;
	private int[] rgbStartVals = POW2_4_32; //POW2_4_200;
	//endsfor computecolors
	

	private static final String NEW_LINE = "\r\n";

	
	protected static final int HEIGHT 	= 800;
	protected static final int WIDTH 	= 800;

    boolean isOriginUpperLeft 	= true;  	// location of origin
	static int OFFSET 			= 25; 		// pixel offset from edge

	static int depth; 						// recursion depth
	static final int MAX_DEPTH 		= 10;
	
	static final int COLORMAXRGB	= 0xFF;
	
	//used in Mandelbrot & Julia & Poly
	static int maxIter 	= 255;		//	maximum iterations to check for Mandelbrot, Julia & Poly
	static int areaSize = 599;	//512;	(0-599)
	
	BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

	final Point center = new Point(getAreaSize() / 2, getAreaSize() / 2);

	static double xC = 0.0;
	static double yC = 0.0;
	static double scaleSize = 1.0;
	
	protected double rotation = 0.0;
	//	type-1	Reverse
	//		zx = new ComplexNumber(x0, y0);		zy = new ComplexNumber(y0, x0);
	//	type-2	Exchange
	//		zx = new ComplexNumber(x0, 0.0);	zy = new ComplexNumber(0.0, y0);
	//	type-3	Single
	//		zx = new ComplexNumber(x0, y0);		zy = new ComplexNumber(0.0, 0.0);
	//	type-4	Duplicate
	//		zx = zy = new ComplexNumber(x0, y0);
	//	type-5	Exponent	pOWER
	//		zx = new ComplexNumber(x0, y0).power((int)x0);	zy = new ComplexNumber(y0, x0).power((int)y0);
	//	type-6	Exponent	(e)
	//		zx = new ComplexNumber(x0,0.0).exp();		zy	=	new ComplexNumber(y0,0.0).exp();
	//	default	
	//		zy = new ComplexNumber(x0, 0.0);	zy = new ComplexNumber(y0, 0.0);
	
	
	protected String rowColMixType = "Reverse";
	protected int power;
	
	protected String pxConstOperation = "Plus";	// z + C	others are "Multiply","Divide","Subtract"
		
	protected String pxXTransformation="None";	
	protected String pxYTransformation="None";
	//	others are 
	//		"Absolute"		x=|x|,y=|y|
	//		"Reciprocal"	x=1/x,y=1/y
	//		"Square"		x=x^2,y=y^2
	//		"Root"			x=sqrt(x),y=sqrt(y)
	//		Exponent		e^x,e^y
	//		"sine"			sin(x),sin(y)
	//		"cosine"		cos(x),cos(y)
	//		"tan"			tan(x),tan(y)
	
	
	protected String pixXYOperation = "Plus";	
	// x=x,y=y
	//others are "Multiply",	z=x*(iy)
	//	"Divide",				z=x/(iy)
	//	"Minus"					z=x-(iy)
	//	"Power"					z=x^(iy)
	
	
	protected String useFuncPixel	=	"None";
	
	//////	applied on the constant
	protected String useFuncConst = "None";	//	others are "Sine", "Cosine", "Tangent", etc
	
	protected boolean running = false;
/*
	protected boolean useColorPalette = false;
	protected boolean useBlackWhite = false;
	protected boolean useComputeColor = !(this.isUseColorPalette() || this.isUseBlackWhite());*/
	
	//COLOR_OPTIONS = new String[]{"BlackWhite","ColorPalette","ComputeColor"/*,"Random"*/,"SampleMix"};
	
	public final String Color_Palette = "ColorPalette";
	public final String ComputeColor = "ComputeColor";
	public final String SampleMix = "SampleMix";
	public final String BlackWhite = "BlackWhite";
	public final String Random_COLOR = "Random";
	public final String Color_Palette_Gradient6 = "ColorGradient6";
	public final String Color_Palette_Blowout = "ColorBlowout";
	public final String Color_Palette_SuperBlowout = "ColorSuperBlowout";	
	public static final String[] COLOR_SUPER_BLOWOUT_TYPES = new String[] { "Original","Fire","Black & White","Electric Blue","Toon","Gold","Classic VGA","CGA 1","CGA 2","Primary (RGB)","Secondary (CMY)","Tertiary 1","Tertiary 2","Neon"};
	
	
	protected String colorChoice=Color_Palette;
	
	//	others are "Snowy", "Pink", "Matrix"
	//	for	superBlowOut	too
	protected String colorBlowoutType = "Default";
	
	protected double  bound = 2.0;
	
	protected boolean reversePixelCalculation = false;
	
	protected boolean useLyapunovExponent = false;
	
	protected boolean savePixelInfo2File = false;
	
	private boolean useMandelbrotExplorer = false;
	private boolean useJuliaExplorer = false;
	
	protected /*final */JTextField locPtTextField = new JTextField(WIDTH);
	
	/*int[] pixel; // width*height array of pixels
	MemoryImageSource source = null;
	int npixel = 0; // tracks current pixel
*/
	
	static List<BufferedImage> fbImages = new ArrayList<BufferedImage>();
	private boolean generated = false;
	
	static List<BufferedImage> rawFbImages = new ArrayList<BufferedImage>();
	static List<FractalDtlInfo> fbDTlsList = new ArrayList<FractalDtlInfo>();
	static int fbCurrCount = 0;
	
	/** Constructor: an instance */
	public FractalBase() {
		super();
		this.setSize(new java.awt.Dimension(WIDTH, HEIGHT+20));
		/*this.npixel = 0;
		this.pixel = new int[WIDTH*HEIGHT];*/
		
		this.setVisible(true);
//		fbCurrCount+=1;
		/*if (this.isUseMandelbrotExplorer()) {
			this.add(locPtTextField, java.awt.BorderLayout.SOUTH);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}*/
		/******************
		 * this.setRgbStartVals(POW3_3_243); //POW2_4_200
		 * this.setRgbDivisors(FRST_SIX_ODDS); //FRST_SIX_PRIMES
		 *******************/		
	}
	
	
	public FractalBase(Properties p){
		this();
		this.setVisible(false);
		this.setProperties(p);
	}
	
	public synchronized void setProperties(Properties p) {
		// .getClass().getName()
		//	all keys and values of p are Strings
		
		if (p.getProperty("colorChoice") != null) {//if (p.getProperty("colorChoice") != null) {
			String cChoice = p.getProperty("colorChoice");
			if(!cChoice.contains("|")) {
				this.setColorChoice(cChoice.replaceAll(WHITESPACE, EMPTY));
			} else {
//				cChoice = cChoice.replaceAll("[", EMPTY).replaceAll("]", EMPTY);
				String[] cCs = cChoice.split(PIPE);
				this.setColorChoice(cCs);
			}
			
			/*if (!cChoice.startsWith("[")) {
				this.setColorChoice(cChoice.replaceAll(WHITESPACE, EMPTY));
			} else {
				cChoice = cChoice.replaceAll("[", EMPTY).replaceAll("]", EMPTY);
				String[] cCs = cChoice.split(",");
				this.setColorChoice(cCs);
			}*/
		}
		
		if (p.getProperty("startVals") != null)
			this.setRgbStartVals(this.intiFyRgbStartVals(p.getProperty("startVals").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("divVals") != null)
			this.setRgbDivisors(this.intiFyRgbDivisors(p.getProperty("divVals").replaceAll(WHITESPACE,EMPTY)));		
		if (p.getProperty("maxIter") != null)
			this.setMaxIter(Integer.parseInt(p.getProperty("maxIter").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("areaSize") != null)
			this.setAreaSize(Integer.parseInt(p.getProperty("areaSize").replaceAll(WHITESPACE,EMPTY)));		
		if (p.getProperty("bound") != null)
			this.setBound(Double.parseDouble(p.getProperty("bound").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("pixXYOperation") != null)
			this.setPixXYOperation(p.getProperty("pixXYOperation").replaceAll(WHITESPACE,EMPTY));
		if (p.getProperty("power") != null)
				this.setPower(Integer.parseInt(p.getProperty("power").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("pxConstOperation") != null)
				this.setPxConstOperation(p.getProperty("pxConstOperation").replaceAll(WHITESPACE,EMPTY));
		if (p.getProperty("pxXTransformation") != null)
				this.setPxXTransformation(p.getProperty("pxXTransformation").replaceAll(WHITESPACE,EMPTY));
		if (p.getProperty("pxYTransformation") != null)
				this.setPxYTransformation(p.getProperty("pxYTransformation").replaceAll(WHITESPACE,EMPTY));
		if (p.getProperty("reversePixelCalculation") != null)
				this.setReversePixelCalculation(Boolean.parseBoolean(p.getProperty("reversePixelCalculation").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("rotation") != null)
				this.setRotation(Double.parseDouble(p.getProperty("rotation").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("rowColMixType") != null)
				this.setRowColMixType(p.getProperty("rowColMixType").replaceAll(WHITESPACE,EMPTY));
		if (p.getProperty("xC") != null)
				this.setxC(Double.parseDouble(p.getProperty("xC").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("yC") != null)
				this.setyC(Double.parseDouble(p.getProperty("yC").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("scaleSize") != null)
				this.setScaleSize(Double.parseDouble(p.getProperty("scaleSize").replaceAll(WHITESPACE,EMPTY)));
		if (p.getProperty("useFuncConst") != null)
				this.setUseFuncConst(p.getProperty("useFuncConst").replaceAll(WHITESPACE,EMPTY));
		if (p.getProperty("useFuncPixel") != null)
				this.setUseFuncPixel(p.getProperty("useFuncPixel").replaceAll(WHITESPACE,EMPTY));

		/*this.setZSq(Boolean.parseBoolean(p.getProperty("isZSq")));
		this.setFatou(Boolean.parseBoolean(p.getProperty("isFatou")));
		this.setClassicJulia(Boolean.parseBoolean(p.getProperty("isClassicJulia")));*/
		
		if (p.getProperty("applyCustomFormula") != null) {
			this.setApplyCustomFormula(Boolean.parseBoolean(p.getProperty("applyCustomFormula").replaceAll(WHITESPACE,EMPTY)));
		}
		
		if (p.getProperty("smoothen") != null)
			this.setSmoothen(Boolean.parseBoolean(p.getProperty("smoothen").replaceAll(WHITESPACE,EMPTY)));
	
		
		
	}


	protected void fillEquilateralTriangle(Graphics2D g, Point center, int length, String dir, Color color) {
		g.setColor(color);
		this.fillEquilateralTriangle(g,center,length,dir);
	}
	
	
	protected void fillEquilateralTriangle(Graphics2D g, Point center, int length, String dir) {
		Line vrtxDirLn;
		Line baseDirLn;
		if ("up".equals(dir)) {// -- go up 4 vertex
			vrtxDirLn = new Line(center.x, center.y, length * 2 / 3, 90.0);
			// -- go down next for base
			baseDirLn = new Line(center.x, center.y, length / 3, 270.0);
		} else {// -- go down 4 vertex
			vrtxDirLn = new Line(center.x, center.y, length * 2 / 3, 270.0);
			// -- go up next for base
			baseDirLn = new Line(center.x, center.y, length / 3, 90.0);
		}
		Point vertex = new Point((int) vrtxDirLn.getX2(), (int) vrtxDirLn.getY2());
		Point midBase = new Point((int) baseDirLn.getX2(), (int) baseDirLn.getY2());

		// --go-left
		Line leftB = new Line(midBase.x, midBase.y, length / 2, 180.0);
		Point lftB = new Point((int) leftB.getX2(), (int) leftB.getY2());
		// --go-right
		Line rightB = new Line(midBase.x, midBase.y, length / 2, 0.0);
		Point rtB = new Point((int) rightB.getX2(), (int) rightB.getY2());

		fillTriangle(g, vertex, lftB, rtB, g.getColor());
	}
	
	protected void drawEquilateralTriangle(Graphics2D g, Point center, int length, String dir, Color color) {
		g.setColor(color);
		this.drawEquilateralTriangle(g,center,length,dir);
	}
	protected void drawEquilateralTriangle(Graphics2D g, Point center, int length, String dir) {
		Line vrtxDirLn;
		Line baseDirLn;
		if("up".equals(dir)){// -- go up 4 vertex
			vrtxDirLn = new Line(center.x, center.y, length * 2 / 3, 90.0);
			// -- go down next for base
			baseDirLn = new Line(center.x, center.y, length / 3, 270.0);
		} else {// -- go down 4 vertex
			vrtxDirLn = new Line(center.x, center.y, length * 2 / 3, 270.0);
			// -- go up next for base
			baseDirLn = new Line(center.x, center.y, length / 3, 90.0);
		}
		
		Point vertex = new Point((int) vrtxDirLn.getX2(), (int) vrtxDirLn.getY2());
//		System.out.println("Vertex is P("+vertex.x+","+vertex.y+")");
		
		Point midBase = new Point((int) baseDirLn.getX2(), (int) baseDirLn.getY2());

		// --go-left
		Line leftB = new Line(midBase.x, midBase.y, length / 2, 180.0);
		Point lftB = new Point((int) leftB.getX2(), (int) leftB.getY2());
		// --go-right
		Line rightB = new Line(midBase.x, midBase.y, length / 2, 0.0);
		Point rtB = new Point((int) rightB.getX2(), (int) rightB.getY2());
//		System.out.println("lftB is P("+lftB.x+","+lftB.y+")");
//		System.out.println("rtB is P("+rtB.x+","+rtB.y+")");
		
		drawTriangle(g,vertex,lftB,rtB,g.getColor());
		
		
		/*Path2D myPath = new Path2D.Double();
		myPath.moveTo(vertex.x,vertex.y);
		myPath.lineTo(lftB.x,lftB.y);
		myPath.lineTo(rtB.x,rtB.y);
		myPath.closePath();
		
		g.draw(myPath);*/
//==============================================================//		
//		/*g.drawLine(vertex.x,vertex.y,lftB.x,lftB.y);
//		g.drawLine(vertex.x,vertex.y,rtB.x,rtB.y);
//		g.drawLine(lftB.x,lftB.y,rtB.x,rtB.y);*/
//===============================================================//
		/*Line l1 = new Line(vertex, lftB);
		Line l2 = new Line(vertex, rtB);
		Line l3 = new Line(lftB, rtB);

		l1.draw(g);
		l2.draw(g);
		l3.draw(g);*/

	}

	public void fillSquare(Graphics2D g, Point center, int length, Color color) {
		g.setColor(color);
		this.fillSquare(g, center, length);
	}

	public void fillSquare(Graphics2D g, Point center, int length) {
		Point pu1 = setupSquare(center, length);
		g.fillRect(pu1.x, pu1.y, length, length);
	}

	public void fillSquare(Graphics2D g, int x, int y, int length, Color color) {
		g.setColor(color);
		this.fillSquare(g, x, y, length);
	}

	public void fillSquare(Graphics2D g, int x, int y, int length) {
		g.fillRect(x, y, length, length);
	}

	private Point setupSquare(Point center, int length) {
		// go-left
		Line left = new Line(center.x, center.y, length / 2, 180.0);
		Point p1l = new Point((int) left.getX2(), (int) left.getY2());

		// go-up
		Line up = new Line(p1l.x, p1l.y, length / 2, 270.0);
		Point pu1 = new Point((int) up.getX2(), (int) up.getY2());
		return pu1;
	}

	public void drawSquare(Graphics2D g, Point center, int length, Color color) {
		g.setColor(color);
		this.drawSquare(g, center, length);
	}

	public void drawSquare(Graphics2D g, Point center, int length) {
		Point pu1 = setupSquare(center, length);
		g.drawRect(pu1.x, pu1.y, length, length);
	}
	
	private void startThread(){
		this.run();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (running) {
			try {
				while (depth < MAX_DEPTH) {
					// System.out.println("depth is " + depth);
					depth += 1;
					Thread.sleep(3000);
					generate(depth);
//					run();
				}
			} catch (InterruptedException ex) {
			}
		}
	}
	
	private void generate(int d) {
		depth = d;
		this.repaint();
	}
	
	private boolean isFbCreated = false;

	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g1) {
		super.paint(g1);
		Graphics2D g2 = (Graphics2D) g1;
//		this.drawAxis(g2);
		BufferedImage img;
		if (!this.refocusDraw) {
			img = this.createFractalImage();
			// reqd drawing location
			int drawLocX = (int) getxC();
			int drawLocY = (int) getyC();
			// rotation info
			double rotationReq = Math.toRadians(this.rotation);
			double locX = getWidth() / 2;
			double locY = getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(rotationReq, locX, locY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			// Drawing the rotated image at the required drawing locations
			img = op.filter(img, null);
			g2.drawImage(img, drawLocX, drawLocY, null);
//			this.drawAxis(g2);
			/*this.setImage(img);
			this.add2FbImages(img);
			g2.dispose();*/
		} else {
			this.createRefocussedFractaImage(this);
			img = this.bufferedImage;
			g2.drawImage(img, 0, 0, null);
//			this.drawAxis(g2);
			/*this.setImage(img);
			this.add2FbImages(img);
			g2.dispose();*/
		}
		
		if (!this.generated) {
			this.add2FbImages(img);
		}
		this.setImage(img);
		this.isFbCreated = true;
		g2.dispose();
		this.isFbCreated = false;
	}
	
	private void createRefocussedFractaImage(FractalBase frctl) {
//		Graphics2D g = this.getBufferedImage().createGraphics();
		Julia julia = null;
		Mandelbrot mand = null;
		if (frctl.getClass().getName().contains("Julia")) {
			julia = (Julia) frctl;
			julia.createFocalFractalShape(frctl, new ComplexNumber(x_start,y_start), new ComplexNumber(x_end,y_end));
		} else if(frctl.getClass().getName().contains("Mandelbrot")){
			mand = (Mandelbrot) frctl;
			mand.createFocalFractalShape(frctl, new ComplexNumber(x_start,y_start), new ComplexNumber(x_end,y_end));
		}

		x_min = x_start < x_end ? x_start : x_end;
		x_max = x_start > x_end ? x_start : x_end;
		y_min = y_start < y_end ? y_start : y_end;
		y_max = y_start > y_end ? y_start : y_end;
		
		//this.createFocalFractalShape(frctl, new ComplexNumber(x_start,y_start), new ComplexNumber(x_end,y_end));
/*
		this.source=null;
		this.source=new MemoryImageSource(WIDTH,HEIGHT,this.pixel,0,WIDTH);
		this.setImage(this.createImage((ImageProducer)this.source));
		
*/		
		/*this.add2FbImages(this.getBufferedImage());*/
		/*return this.getBufferedImage();*/
		
	}

	private BufferedImage createFractalImage() {
		Graphics2D g = this.getBufferedImage().createGraphics();
		// Clear the frame
		g.setColor(getBGColor());
		g.fillRect((int)getxC(), (int)getyC(), getWidth(), getHeight());//(0, 0
		g.setColor(Color.red);

/*		System.out.println("in-FB-createfractalimage");*/
		this.createFractalShape(g);
		
		if (this.running) {
			this.addDepthInfo(g);
		}
/*		this.source=null;
		this.source=new MemoryImageSource(WIDTH,HEIGHT,this.pixel,0,WIDTH);
		this.setImage(this.convertImage(this.createImage((ImageProducer)this.source)));
*/		return this.getBufferedImage();
	}
	

    
    public BufferedImage convertImage(Image original) {
        ColorModel cm = ColorModel.getRGBdefault();
        int width = original.getWidth(null);
        int height = original.getHeight(null);
        BufferedImage image = new BufferedImage (cm,cm.createCompatibleWritableRaster(width, height),cm.isAlphaPremultiplied(), null);
        Graphics2D bg = image.createGraphics();
        bg.drawImage(original, 0, 0, width, height, null);
        bg.dispose();
        
        return image;
    }

	private void addDepthInfo(Graphics2D g) {
		g.setColor(Color.red);
		g.drawString("Depth:= " + depth, OFFSET * 2, HEIGHT - OFFSET * 9 + 10);
	}
	
	public static BufferedImage joinBufferedImages(BufferedImage[] images) {
		BufferedImage startImage = images[0];
		BufferedImage newImage = startImage;
		for (int i = 1; i < images.length; i++) {
			BufferedImage nxtImage = images[i];
			newImage = joinBufferedImages(newImage, nxtImage);
		}
		return newImage;		
	}
		
	
	public static BufferedImage joinBufferedImages(BufferedImage img1, BufferedImage img2) {
		// int offset = 2;
		int width = img1.getWidth();
		int height = img1.getHeight() + img2.getHeight();
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(img1, null, 0, 0);
		g2.drawImage(img2, null, 0, img1.getHeight());
		g2.dispose();
		return newImage;
	}
	
	public static BufferedImage joinAdjacentBufferedImages(BufferedImage imgLeft, BufferedImage imgRight) {
		// int offset = 2;
		int width = imgLeft.getWidth() + imgRight.getWidth();
		int height = imgLeft.getHeight();
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(imgLeft, null, 0, 0);
		g2.drawImage(imgRight, null, imgLeft.getWidth(), 0);//img1.getHeight());
		g2.dispose();
		return newImage;
	}
	
	public static BufferedImage joinAdjacentBufferedImages(BufferedImage[] images){
//		int width = 0;
		BufferedImage startImage = images[0];
		/*int height = startImage.getHeight();*/	
		BufferedImage newImage = startImage;
		for (int i = 1; i < images.length; i++) {
			BufferedImage image = images[i];
			/* width += image.getWidth(); */
			newImage = joinAdjacentBufferedImages(newImage, image);
			
					/*new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = newImage.createGraphics();
			Color oldColor = g2.getColor();
			g2.setPaint(Color.WHITE);
			g2.fillRect(0, 0, width, height);
			g2.setColor(oldColor);
			g2.drawImage(img1, null, 0, 0);
			g2.drawImage(img2, null, img1.getWidth(), 0);*/
		}
		
		
		return newImage;
		
	}
	
	protected void doPrint(Graphics2D g) {
		// invokes local printer
		PrinterJob printJob = PrinterJob.getPrinterJob();
		BufferedImage image = this.getBufferedImage();
		final FractalBase ff = this;
		printJob.setPrintable(new Printable() {
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				if (pageIndex != 0) {
					return NO_SUCH_PAGE;
				}
				graphics.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), ff/*null*/);
				return PAGE_EXISTS;
			}
		});
		try {
			printJob.print();
		} catch (PrinterException e1) {
			e1.printStackTrace();
		}
		
		
		/*/////////////////////////
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
	    pras.add(new Copies(1));
	    PrintService pss[] = PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);
	    if (pss.length == 0)
	      throw new RuntimeException("No printer services available.");
	    PrintService ps = pss[0];
	    System.out.println("Printing to " + ps);
	    DocPrintJob job = ps.createPrintJob();
	    FileInputStream fin = new FileInputStream("YOurImageFileName.PNG");
	    Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.GIF, null);
	    job.print(doc, pras);
	    fin.close();
		//////////////////////////*/
		
	}

	protected Color getBGColor() {
		return Color.black;
	}

	public abstract void createFractalShape(Graphics2D g);
	//public abstract void createFocalFractalShape(FractalBase fb, ComplexNumber start, ComplexNumber end);

	protected abstract String getFractalShapeTitle();
	

	
	protected ComplexNumber computePixel(String fun, ComplexNumber z0) {
		if (!this.applyCustomFormula) {
			switch (fun) {
			case "sine":
				z0 = z0.sine(); // z0.sin();
				break;
			case "cosine":
				z0 = z0.cosine(); // z0.cos();
				break;
			case "tan":
				z0 = z0.tangent(); // z0.tan();
				break;
			case "arcsine":
				z0 = z0.asin();		//z0.inverseSine(); // z0.sin();
				break;
			case "arccosine":
				z0 = z0.acos();//z0.inverseCosine(); // z0.cos();
				break;
			case "arctan":
				z0 = z0.atan();//z0.inverseTangent(); // z0.tan();
				break;
			case "cosec":
				z0 = z0.cosec(); // z0.cosec();
				break;
			case "sec":
				z0 = z0.sec(); // z0.sec();
				break;
			case "cot":
				z0 = z0.cot(); // z0.cot();
				break;
			case "sinh":
				z0 = z0.sinh(); // z0.sinh();
				break;
			case "cosh":
				z0 = z0.cosh(); // z0.cosh();
				break;
			case "tanh":
				z0 = z0.tanh(); // z0.tanh();
				break;
			case "arcsinh":
				z0 = z0.asinh(); // z0.asinh();
				break;
			case "arccosh":
				z0 = z0.acosh(); // z0.acosh();
				break;
			case "arctanh":
				z0 = z0.atanh(); // z0.atanh();
				break;
			case "reciprocal":
				z0 = z0.reciprocal(); // z0.sin();
				break;
			case "reciprocalSquare":
				z0 = (z0.reciprocal()).power(2); // z0.sin();
				break;
			case "square":
				z0 = z0.power(2); // z0.sin();
				break;
			case "cube":
				z0 = z0.power(3); // z0.cos();
				break;
			case "exponent(e)":
				z0 = z0.exp(); // z0.tan();
				break;
			case "root":
				z0 = z0.sqroot(); // z0.sin();
				break;
			case "cube-root":
				z0 = z0.curoot(); // z0.cos();
				break;
			case "log(e)":
				z0 = z0.ln(); // z0.tan();
				break;

			case "None":
				z0 = z0;
				break;
			default:
				z0 = z0;
				break;
			}// ends-switch
		} else {
			z0 = new FunctionEvaluator().evaluate(fun, z0);
		}
		return z0;
	}
	

	protected ComplexNumber computePixel(String fun, ComplexNumber z0, ComplexNumber compConst) {
		if (this.applyCustomFormula) {
			fun = fun.replaceAll("cos","kos")
					.replaceAll("cot", "kot")
					.replaceAll("sec", "sek")
					.replaceAll("csc", "ksk")
					.replaceAll("arcsin", "arksin")
					.replaceAll("arccos", "arkkos")
					.replaceAll("arctan","arktan");
			
			fun = fun.replaceAll("c", "(" + compConst.toString().replaceAll("i", "*i") + ")")
					.replaceAll("C", "(" + compConst.toString().replaceAll("i", "*i") + ")");

			fun = fun.replaceAll("kos","cos")
					.replaceAll("kot", "cot")
					.replaceAll("sek", "sec")
					.replaceAll("ksk", "csc")
					.replaceAll("arksin", "arcsin")
					.replaceAll("arkkos", "arccos")
					.replaceAll("arktan","arctan");
			
			return new FunctionEvaluator().evaluate(fun, z0);//, compConst);
		}
		return null;
	}
	
	protected boolean checkForConstInAppliedFormula(String fun2Apply) {
		boolean fun2ApplyHasC = false;
		
		String fun = fun2Apply.toLowerCase();
		if (this.applyCustomFormula) {
			//	Replace 'c' with 'k' in functionNames
			fun = fun.replaceAll("cos","kos")
					.replaceAll("cot", "kot")
					.replaceAll("sec", "sek")
					.replaceAll("csc", "ksk")
					.replaceAll("arcsin", "arksin")
					.replaceAll("arccos", "arkkos")
					.replaceAll("arctan","arktan");
			// does 'c' still exist in formula2Apply
			fun2ApplyHasC = fun.indexOf('c') > -1;
		}

		return fun2ApplyHasC ;
		
	}
	
	protected ComplexNumber getPixelComplexValue(double x, double y) {
		x = this.applyPixelXPointTransformation(x);
		y = this.applyPixelYPointTransformation(y);
		
		return this.applyIntraPixelOperation(x,y);
		
	}
	
	//protected String pxConstOperation = "Plus";	// z + C	others are "Multiply","Divide","Subtract"	
	private ComplexNumber applyIntraPixelOperation(double x, double y) {
		ComplexNumber z = null;
		switch(this.getPixXYOperation()){
			case	"Plus":		z = new ComplexNumber(x,y);	break;
			case	"Minus":	z = new ComplexNumber(x,y*-1.0);	break;
			case	"Multiply":	z = new ComplexNumber(x,0.0).times(new	ComplexNumber(0.0,y));	break;
			case	"Divide":	z = new ComplexNumber(x,0.0).divides(new	ComplexNumber(0.0,y));	break;
			case	"Power":	z = new ComplexNumber(x,0.0).power(/*(int)*/y);	break;
			default:	z = new ComplexNumber(x,y);	break;	
		}
		return z;
	}

	private double applyPixelXPointTransformation(double pix) {
		switch (this.getPxXTransformation()) {
			case "none":
				break;
			case "absolute":
				pix = Math.abs(pix);
				break;
			case "absoluteSquare":
				pix = Math.pow(Math.abs(pix),2);
				break;
			case "reciprocal":
				pix = 1.0/(pix);
				break;
			case "reciprocalSquare":
				pix = Math.pow((1.0/(pix)),2);
				break;
			case "square":
				pix = Math.pow(pix, 2);
				break;
			case "cube":
				pix = Math.pow(pix, 3);
				break;
			case "root":
				pix = Math.sqrt(pix);
				break;
			case "exponent":
				pix = Math.exp(pix);
				break;	
			case "log(10)":
				pix = Math.log10(pix);
				break;
			case "log(e)":
				pix = Math.log(pix);
				break;
			case "cosec":
				pix = 1/Math.sin(pix);
				break;
			case "sec":
				pix = 1/Math.cos(pix);
				break;
			case "cot":
				pix = 1/Math.tan(pix);
				break;
			case "sinh":
				pix = Math.sinh(pix);
				break;
			case "cosh":
				pix = Math.cosh(pix);
				break;
			case "tanh":
				pix = Math.tanh(pix);
				break;
			case "sine":
				pix = Math.sin(pix);
				break;
			case "cosine":
				pix = Math.cos(pix);
				break;
			case "tangent":
				pix = Math.tan(pix);
				break;
			case "arcsinh":
				pix = 1/Math.sinh(pix);
				break;
			case "arccosh":
				pix = 1/Math.cosh(pix);
				break;
			case "arctanh":
				pix = 1/Math.tanh(pix);
				break;
			case "arcsine":
				pix = Math.asin(pix);
				break;
			case "arccosine":
				pix = Math.acos(pix);
				break;
			case "arctangent":
				pix = Math.atan(pix);
				break;
			default:
				break;
			}
		return pix;
	}

	private double applyPixelYPointTransformation(double pix) {
		switch (this.getPxYTransformation()) {
		case "none":
			break;
		case "absolute":
			pix = Math.abs(pix);
			break;
		case "absoluteSquare":
			pix = Math.pow(Math.abs(pix),2);
			break;
		case "reciprocal":
			pix = 1.0/(pix);
			break;
		case "reciprocalSquare":
			pix = Math.pow((1.0/(pix)),2);
			break;
		case "square":
			pix = Math.pow(pix, 2);
			break;
		case "cube":
			pix = Math.pow(pix, 3);
			break;
		case "root":
			pix = Math.sqrt(pix);
			break;
		case "exponent":
			pix = Math.exp(pix);
			break;	
		case "log(10)":
			pix = Math.log10(pix);
			break;
		case "log(e)":
			pix = Math.log(pix);
			break;
		case "cosec":
			pix = 1/Math.sin(pix);
			break;
		case "sec":
			pix = 1/Math.cos(pix);
			break;
		case "cot":
			pix = 1/Math.tan(pix);
			break;
		case "sinh":
			pix = Math.sinh(pix);
			break;
		case "cosh":
			pix = Math.cosh(pix);
			break;
		case "tanh":
			pix = Math.tanh(pix);
			break;
		case "sine":
			pix = Math.sin(pix);
			break;
		case "cosine":
			pix = Math.cos(pix);
			break;
		case "tangent":
			pix = Math.tan(pix);
			break;
		case "arcsinh":
			pix = 1/Math.sinh(pix);
			break;
		case "arccosh":
			pix = 1/Math.cosh(pix);
			break;
		case "arctanh":
			pix = 1/Math.tanh(pix);
			break;
		case "arcsine":
			pix = Math.asin(pix);
			break;
		case "arccosine":
			pix = Math.acos(pix);
			break;
		case "arctangent":
			pix = Math.atan(pix);
			break;
		default:
			break;
		}
		return pix;
	}
	
	protected void setPixel(int col, int row, Color color) {
		this.setPixel(col,row,color.getRGB());
	}

	protected void setPixel(int col, int row, int rgbColor) {
		if (validateColumnIndex(col) && validateRowIndex(row)) {
			if (isOriginUpperLeft)
				bufferedImage.setRGB(col, row, rgbColor);
			else
				bufferedImage.setRGB(col, HEIGHT - row - 1, rgbColor);
		}	
	}
	
	protected Color getPixelDisplayColor(final int row, final  int col, /*final*/ int colorRGB, final boolean useD) {
		final int[] divs = this.getRgbDivisors();				//	1st 6 primes
		final int[] colStart = this.getRgbStartVals();			//	pow 2s till end-200
		Color color = null;
		
		
		

		boolean toSmoothen = this.isSmoothen();
		if (toSmoothen) {
			colorRGB = this.smoothenRGB(row,col,colorRGB);
//			return new Color(colorRGB);
		}

		if (this.colorChoice.equals(Color_Palette_Gradient6)) {
			double colG6 = colorRGB * 6.0;
			int hI = colorRGB;
			double hF = hI - colG6;
			hF = this.correctColorGr6(hF);
			switch (hI % 6) {
				case 0:		return new Color(1,(int) hF,0);
				case 1:		return new Color(1,(int) hF,1,0);
				case 2:		return new Color(0,1,(int) hF);	
				case 3:		return new Color(0,1,(int) hF,0);
				case 4:		return new Color((int) hF,0,1);
				case 5:		return new Color(1,0,1,(int) hF);				

			}

		}
		
		if(this.colorChoice.equals(Color_Palette_Blowout)){
			return new Color(this.time2Color(colorRGB));
		}
		
		if(this.colorChoice.equals(Color_Palette_SuperBlowout)){
			return this.getColorSuperBlowout(colorRGB);
		}
		
		for(int iter = 0; iter < divs.length; iter++) {
			if (colorRGB % divs[iter] == 0) {
				if (this.colorChoice.equals(Color_Palette)) {
					if (useD) {
						color = ColorPalette[iter];
					} else {
						color = ColorPalette[ColorPalette.length - iter - 1];
					}
				} else if(this.colorChoice.equals(BlackWhite)) {
					if (useD) {
						color = BlackWhitePalette[iter];
					} else {
						color = BlackWhitePalette[BlackWhitePalette.length - iter - 1];
					}
				} else if(this.colorChoice.equals(ComputeColor)||this.colorChoice.equals(SampleMix)){

					final int start = colStart[iter];
					final int end = COLORMAXRGB - start;

					int num1 = correctColor(start, colorRGB, row, divs[iter]);
					int num2 = correctColor(start, colorRGB, col, divs[iter]);

					if (useD) {
						color = new Color(num1, num2, start);
					} else {
						color = new Color(num1, num2, end);
					}
				}

				return color;
			}
			
		}

		if (this.colorChoice.equals(Color_Palette)) {
			if (useD) {
				color = ColorPalette[4];
			} else {
				color = ColorPalette[ColorPalette.length - 5];
			}
			return color;
		}

		if (this.colorChoice.equals(BlackWhite)) {
			if (useD) {
				color = BlackWhitePalette[4];
			} else {
				color = BlackWhitePalette[BlackWhitePalette.length - 5];
			}
			return color;
		}
		
		if (this.colorChoice.equals(ComputeColor)||this.colorChoice.equals(SampleMix)) {
			final int start = 200; // 255 is max.	0<=colorRGB<=255
			final int end = COLORMAXRGB - start;
			int num1 = correctColor(start, colorRGB, row, 1);
			int num2 = correctColor(start, colorRGB, col, 1);
			if (useD) {
				color = new Color(num1, num2, start);
			} else {
				color = new Color(num1, num2, end);
			}
			return color;
		}
		
		return color;
	}

	private int smoothenRGB(int x, int y, int colorRGB) {
		//https://github.com/cslarsen/mandelbrot-js
		//https://stackoverflow.com/questions/369438/smooth-spectrum-for-mandelbrot-set-rendering
		//nsmooth := n + 1 - Math.log(Math.log(zn.abs()))/Math.log(2)
		//x-row,y-col
		int smoothened = 0;
		
		boolean isMandelbrot = this.getClass().getName().contains("Mandelbrot");
		boolean isJulia = this.getClass().getName().contains("Julia");
		
		if (isMandelbrot) {
			final double log2 = Math.log(2);
			smoothened = (int) (colorRGB + 1 - Math.log(Math.log(Math.abs(colorRGB))) / log2);
		} else if(isJulia) {//n+1-log(log2(abs(z)))		log2N=log10n/log102		getPixelComplexValue
			return (int) (colorRGB + 1 - Math.log10(Math.log10(Math.abs(this.getPixelComplexValue(x,y).abs()))/Math.log10(2)));
			//return 0.5 + 0.5 * Math.sin(value * frequency);
			//smoothened = (int) (0.5 + 0.5 * Math.sin(colorRGB/maxIter));
			
//			ComplexNumber z = new ComplexNumber(x, y);
//			Julia julie = (Julia) this;
//			ComplexNumber c = julie.getComplex();//computeComplexConstant();
//			double smoothColor = Math.exp(-z.abs());
//
//			for (int i = 0; i < maxIter && z.abs() < 30; i++) {
//				z = julie.processJuliaZValue(z, c);
//				smoothColor += Math.exp(-z.abs());
//			}
//			smoothened = Color.HSBtoRGB((float) (0.95f + 10 * smoothColor/maxIter),0.6f,1.0f);
		}
		
		return smoothened;
	}
	
	// alex russel : http://stackoverflow.com/users/2146829/alex-russell
	private Color mapColor(int i, double r, double c) {
		double di = (double) i;
		double zn;
		double hue;

		zn = Math.sqrt(r + c);
        // 2 is escape radius
        hue = di + 1.0 - Math.log(Math.log(Math.abs(zn))) / Math.log(2.0);  
        hue = 0.95 + 20.0 * hue; // adjust to make it prettier
        // the hsv function expects values from 0 to 360
		while (hue > 360.0)
			hue -= 360.0;
		while (hue < 0.0)
			hue += 360.0;

		return new Color((float)hue, (float)0.8*1.0f, 1.0f);
	}


	int[] getRgbStartVals() {
		return this.rgbStartVals;// POW2_4_200;
	}

	public void setRgbStartVals(int[] startVals) {
		this.rgbStartVals = startVals;
	}

	int[] getRgbDivisors() {
		return this.rgbDivisors;// FRST_SIX_PRIMES;
	}

	public void setRgbDivisors(int[] divs) {
		this.rgbDivisors = divs;
	}
	
	
	protected int corectColorRGB(int colorRGB) {
		int corrected = colorRGB > COLORMAXRGB ? COLORMAXRGB : colorRGB;
		corrected = corrected < 0 ? 0 : corrected;
		return corrected;
	}


	/**
	 * // correction for color range  0--255
	 * @param start	---	initial value
	 * @param num	---	value to add
	 * @param sub	---	value to subtract
	 * @param div	---	divisor of num
	 * @return		 color range  0--255
	 */
	protected int correctColor(int start, int num, int sub, int div) {
		int corrected = start + (num / div) - sub;	//random transformation
		corrected = corrected > COLORMAXRGB ? COLORMAXRGB : corrected;
		corrected = corrected < 0 ? 0 : corrected;
		return corrected;
	}
	
	protected double correctColorGr6(double val) {
		double corrected = val;
		if (corrected < 0)
			corrected = -1 * corrected;
		corrected = corrected > COLORMAXRGB ? COLORMAXRGB : corrected;
		corrected = corrected < 0 ? 0 : corrected;
		return corrected;
	}
	
	private boolean validateRowIndex(int row) {
        if (row < 0 || row >= HEIGHT){
        	return false;
//            throw new IllegalArgumentException("row index must be between 0 and " + (HEIGHT - 1) + ": " + row);
        }
        return true;
    }

    private boolean validateColumnIndex(int col) {
        if (col < 0 || col >= WIDTH){
//            throw new IllegalArgumentException("column index must be between 0 and " + (WIDTH - 1) + ": " + col);
            return false;
        }
        return true;
    }
    
	protected void fillCircle(Graphics2D g, Point center, int radius, Color color) {
		g.setColor(color);
		this.fillCircle(g,center,radius);
	}
    
	protected void fillCircle(Graphics2D g, Point center, int radius) {
		Point pu1= setupSquare(center,radius*2);
		g.fillOval(pu1.x, pu1.y, radius * 2, radius * 2);
	}

    
	protected void drawCircle(Graphics2D g, Point center, int radius, Color color) {
		g.setColor(color);
		this.drawCircle(g,center,radius);
	}
    
	protected void drawCircle(Graphics2D g, Point center, int radius) {		
		Point pu1= setupSquare(center,radius*2);
		g.drawOval(pu1.x, pu1.y, radius * 2, radius * 2);

	}
    protected double distance(Point p1, Point p2) {
		return Math.sqrt( Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) );
	}
	
    protected void drawLine(Point p1, Point p2, int color) {
    	Graphics2D g = (Graphics2D) this.getGraphics();
    	g.setColor(new Color(color));
    	this.drawLine(g,p1,p2);
    }

	/** Draw a line between p1 and p2 on g. */
	protected void drawLine(Graphics2D g, Point p1, Point p2) {
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}
	
	protected Point midpoint(Line l){
		return midpoint(new Point((int)l.x,(int)l.x),new Point((int)l.getX2(),(int)l.getY2()));
	}

	/** = the midpoint between p1 and p2 */
	protected Point midpoint(Point p1, Point p2) {
		return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
	}	

	protected Point thirdPtH(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 3), pLeft.y);
	}

	protected Point thirdPtV(Point pTop, Point pBottom) {
		return new Point(pTop.x, (int) (pTop.y + (pBottom.y - pTop.y) / 3));
	}

	protected Point twoThirdPtH(Point pLeft, Point pRight) {
		return new Point((int) (pLeft.x + (pRight.x - pLeft.x) * 2 / 3), pLeft.y);
	}

	protected Point twoThirdPtV(Point pTop, Point pBottom) {
		return new Point(pTop.x, (int) (pTop.y + (pBottom.y - pTop.y) * 2 / 3));
	}

	protected Point mergeXYPt(Point p1, Point p2) {
		return new Point(p1.x, p2.y);
	}

	private Path2D getTrianglePath(Point p1, Point p2, Point p3) {
		Path2D myPath = new Path2D.Double();
		myPath.moveTo(p1.x, p1.y);
		myPath.lineTo(p2.x, p2.y);
		myPath.lineTo(p3.x, p3.y);
		myPath.closePath();
		return myPath;
	}
	
	protected void fillTriangle(Graphics2D g, Point p1, Point p2, Point p3, Color color) {
		Path2D myPath = getTrianglePath(p1, p2, p3);

		g.setPaint(color);
		g.fill(myPath);
	}
	
	protected void deleteTriangle(Graphics2D g, Point p1, Point p2, Point p3) {
		Path2D tPath = getTrianglePath(p1, p2, p3);
		g.setPaint(this.getBGColor());
		g.draw(tPath);
		g.fill(tPath);
	}
	
	protected void drawTriangle(Graphics2D g, Point p1, Point p2, Point p3, Color color) {
		Path2D myPath = getTrianglePath(p1, p2, p3);

		g.setPaint(color);
		g.draw(myPath);
	}
	
	public int getMaxIter() {
		return maxIter;
	}

	public void setMaxIter(int max) {
		FractalBase.maxIter = max;
	}

	public static int getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(int ss) {
		FractalBase.areaSize = ss;
	}

	public double getxC() {
		return xC;
	}

	public void setxC(double x) {
		FractalBase.xC = x;
	}

	public double getyC() {
		return yC;
	}

	public void setyC(double y) {
		FractalBase.yC = y;
	}

	public double getScaleSize() {
		return scaleSize;
	}

	public void setScaleSize(double scaleSize) {
		FractalBase.scaleSize = scaleSize;
	}

	public /*synchronized */BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}

	public /*synchronized */void setImage(Image img) {
		this.bufferedImage = (BufferedImage) img;
		/*this.add2FbImages(this.bufferedImage);*/
	}
	
	public void reset() {
		depth=0;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return this.running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean run) {
		this.running = run;
	}
//	public boolean isUseBlackWhite() {
//		return this.useBlackWhite/*=true*/;
//	}
//
//	public void setUseBlackWhite(boolean blackWhite) {
//		this.useBlackWhite = blackWhite;
//	}
//
//	public boolean isUseColorPalette() {
//		return this.useColorPalette/*=false*/;
//	}
//
//	public void setUseColorPalette(boolean useCPalette) {
//		this.useColorPalette = useCPalette;
//	}

	/*//from-apollo
	  final Color[] colors = { 
				Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, 
				Color.MAGENTA, Color.DARK_GRAY,Color.ORANGE, 
				Color.CYAN, Color.PINK, Color.LIGHT_GRAY};*/
	
	public static final Color[] BlackWhitePalette = new Color[] {
			new Color(0,0,0),
			new Color(25,25,25),
			new Color(50,50,50),
			new Color(70,70,70),
			new Color(100,100,100),
			new Color(150,150,150),
			new Color(200,200,200),
			new Color(255,255,255)			
	};
	

	// first = seed color
	// iterations = colors.length - 1
	public static final Color[] ColorPalette = new Color[] {
			
			Colors.BLACK.getColor(),
			Colors.RED.getColor(),
			Colors.BLUE.getColor(),
			Colors.GREEN.getColor(),
			Colors.YELLOW.getColor(),		//ORANGE
			Colors.MAGENTA.getColor(),		//LIGHT_GRAY
			Colors.DARK_GRAY.getColor(),	//YELLOW
			Colors.ORANGE.getColor(),		//PINK
			Colors.CYAN.getColor(),			//MAGENTA
			Colors.PINK.getColor(),			//CYAN
			Colors.LIGHT_GRAY.getColor(),	//DARK_GRAY
			Colors.WHITE.getColor()
			
			
	};
	
	private static int[] getVga() {
		String[] v	 = new String[]{"0","43520","11141120","11184640","2852126720","2852170240","2857697280","2863311360","1431655680","1431699200","1442796800","1442840320","4283782400","4283825920","4294923520","4294967040","0","336860160","538976256","741092352","943208448","1162167552","1364283648","1633771776","1903259904","2189591040","2459079168","2728567296","3065427456","3419130624","3823362816","4294967040","65280","1090584320","2097217280","3187736320","4278255360","4278238720","4278222080","4278206720","4278190080","4282449920","4286382080","4290641920","4294901760","3204382720","2113863680","1107230720","16711680","16728320","16743680","16760320","16776960","12517120","8257280","4325120","2105409280","2659057408","3195928320","3749576448","4286447360","4286439168","4286430720","4286422528","4286414080","4288576768","4290673920","4292836608","4294933760","3758062848","3204414720","2667543808","2113895680","2113904128","2113912320","2113920768","2113928960","2111831808","2109669120","2107571968","3065446144","3350658816","3686203136","3954638592","4290182912","4290177792","4290173696","4290168576","4290164224","4291278336","4292589056","4293637632","4294948352","3959404032","3690968576","3355424256","3070211584","3070215936","3070221056","3070225152","3070230272","3068919552","3067870976","3066560256","28928","469790976","939553024","1426092288","1895854336","1895847168","1895839744","1895832576","1895825408","1897660416","1899495424","1901395968","1903230976","1433468928","946929664","477167616","7405568","7412736","7419904","7427328","7434496","5599488","3698944","1863936","943223040","1161326848","1429762304","1631088896","1899524352","1899520256","1899517184","1899513088","1899509760","1900361728","1901410304","1902196736","1903245312","1634809856","1433483264","1165047808","946944000","946947328","946951424","946954496","946958592","945910016","945123584","944075008","1364291840","1498509568","1632727296","1766945024","1901162752","1901160704","1901158656","1901156608","1901154560","1901678848","1902203136","1902727424","1903251712","1769033984","1634816256","1500598528","1366380800","1366382848","1366384896","1366386944","1366388992","1365864704","1365340416","1364816128","16640","268452096","536887552","805323008","1090535680","1090531328","1090527232","1090523136","1090519040","1091567616","1092616192","1093664768","1094778880","809566208","541130752","272695296","4259840","4263936","4268032","4272128","4276480","3162368","2113792","1065216","538984704","673202432","807420160","941637888","1092632832","1092630528","1092628480","1092626432","1092624384","1093148672","1093672960","1094197248","1094787072","943792128","809574400","675356672","541138944","541140992","541143040","541145088","541147392","540557568","540033280","539508992","741097728","808206592","875315456","1009533184","1093419264","1093417984","1093415936","1093414912","1093413888","1093676032","1093938176","1094462464","1094790144","1010904064","876686336","809577472","742468608","742469632","742470656","742472704","742473984","742146304","741622016","741359872","0","0","0","0","0","0","0","0"};
		int	[]	n	=	new	int[v.length];
		for (int i = 0; i < v.length; i++) {
			n[i] = Integer.parseUnsignedInt(v[i]);
		}
		return	n;
	}

	
	
	private static final int[] vga = getVga();//{0,43520,11141120,11184640,2852126720,2852170240,2857697280,2863311360,1431655680,1431699200,1442796800,1442840320,4283782400,4283825920,4294923520,4294967040,0,336860160,538976256,741092352,943208448,1162167552,1364283648,1633771776,1903259904,2189591040,2459079168,2728567296,3065427456,3419130624,3823362816,4294967040,65280,1090584320,2097217280,3187736320,4278255360,4278238720,4278222080,4278206720,4278190080,4282449920,4286382080,4290641920,4294901760,3204382720,2113863680,1107230720,16711680,16728320,16743680,16760320,16776960,12517120,8257280,4325120,2105409280,2659057408,3195928320,3749576448,4286447360,4286439168,4286430720,4286422528,4286414080,4288576768,4290673920,4292836608,4294933760,3758062848,3204414720,2667543808,2113895680,2113904128,2113912320,2113920768,2113928960,2111831808,2109669120,2107571968,3065446144,3350658816,3686203136,3954638592,4290182912,4290177792,4290173696,4290168576,4290164224,4291278336,4292589056,4293637632,4294948352,3959404032,3690968576,3355424256,3070211584,3070215936,3070221056,3070225152,3070230272,3068919552,3067870976,3066560256,28928,469790976,939553024,1426092288,1895854336,1895847168,1895839744,1895832576,1895825408,1897660416,1899495424,1901395968,1903230976,1433468928,946929664,477167616,7405568,7412736,7419904,7427328,7434496,5599488,3698944,1863936,943223040,1161326848,1429762304,1631088896,1899524352,1899520256,1899517184,1899513088,1899509760,1900361728,1901410304,1902196736,1903245312,1634809856,1433483264,1165047808,946944000,946947328,946951424,946954496,946958592,945910016,945123584,944075008,1364291840,1498509568,1632727296,1766945024,1901162752,1901160704,1901158656,1901156608,1901154560,1901678848,1902203136,1902727424,1903251712,1769033984,1634816256,1500598528,1366380800,1366382848,1366384896,1366386944,1366388992,1365864704,1365340416,1364816128,16640,268452096,536887552,805323008,1090535680,1090531328,1090527232,1090523136,1090519040,1091567616,1092616192,1093664768,1094778880,809566208,541130752,272695296,4259840,4263936,4268032,4272128,4276480,3162368,2113792,1065216,538984704,673202432,807420160,941637888,1092632832,1092630528,1092628480,1092626432,1092624384,1093148672,1093672960,1094197248,1094787072,943792128,809574400,675356672,541138944,541140992,541143040,541145088,541147392,540557568,540033280,539508992,741097728,808206592,875315456,1009533184,1093419264,1093417984,1093415936,1093414912,1093413888,1093676032,1093938176,1094462464,1094790144,1010904064,876686336,809577472,742468608,742469632,742470656,742472704,742473984,742146304,741622016,741359872,0,0,0,0,0,0,0,0};
/*
var vga=new Uint32Array(256);
vga=[0,43520,11141120,11184640,2852126720,2852170240,2857697280,2863311360,1431655680,1431699200,1442796800,1442840320,4283782400,4283825920,4294923520,4294967040,0,336860160,538976256,741092352,943208448,1162167552,1364283648,1633771776,1903259904,2189591040,2459079168,2728567296,3065427456,3419130624,3823362816,4294967040,65280,1090584320,2097217280,3187736320,4278255360,4278238720,4278222080,4278206720,4278190080,4282449920,4286382080,4290641920,4294901760,3204382720,2113863680,1107230720,16711680,16728320,16743680,16760320,16776960,12517120,8257280,4325120,2105409280,2659057408,3195928320,3749576448,4286447360,4286439168,4286430720,4286422528,4286414080,4288576768,4290673920,4292836608,4294933760,3758062848,3204414720,2667543808,2113895680,2113904128,2113912320,2113920768,2113928960,2111831808,2109669120,2107571968,3065446144,3350658816,3686203136,3954638592,4290182912,4290177792,4290173696,4290168576,4290164224,4291278336,4292589056,4293637632,4294948352,3959404032,3690968576,3355424256,3070211584,3070215936,3070221056,3070225152,3070230272,3068919552,3067870976,3066560256,28928,469790976,939553024,1426092288,1895854336,1895847168,1895839744,1895832576,1895825408,1897660416,1899495424,1901395968,1903230976,1433468928,946929664,477167616,7405568,7412736,7419904,7427328,7434496,5599488,3698944,1863936,943223040,1161326848,1429762304,1631088896,1899524352,1899520256,1899517184,1899513088,1899509760,1900361728,1901410304,1902196736,1903245312,1634809856,1433483264,1165047808,946944000,946947328,946951424,946954496,946958592,945910016,945123584,944075008,1364291840,1498509568,1632727296,1766945024,1901162752,1901160704,1901158656,1901156608,1901154560,1901678848,1902203136,1902727424,1903251712,1769033984,1634816256,1500598528,1366380800,1366382848,1366384896,1366386944,1366388992,1365864704,1365340416,1364816128,16640,268452096,536887552,805323008,1090535680,1090531328,1090527232,1090523136,1090519040,1091567616,1092616192,1093664768,1094778880,809566208,541130752,272695296,4259840,4263936,4268032,4272128,4276480,3162368,2113792,1065216,538984704,673202432,807420160,941637888,1092632832,1092630528,1092628480,1092626432,1092624384,1093148672,1093672960,1094197248,1094787072,943792128,809574400,675356672,541138944,541140992,541143040,541145088,541147392,540557568,540033280,539508992,741097728,808206592,875315456,1009533184,1093419264,1093417984,1093415936,1093414912,1093413888,1093676032,1093938176,1094462464,1094790144,1010904064,876686336,809577472,742468608,742469632,742470656,742472704,742473984,742146304,741622016,741359872,0,0,0,0,0,0,0,0,];

*/

	// setup for Color palette
	enum Colors {
		RED (Color.RED), BLUE (Color.BLUE), GREEN (Color.GREEN),
		ORANGE (Color.ORANGE), YELLOW (Color.YELLOW), PINK (Color.PINK),
		BLACK /*(new Color(123))*/(Color.BLACK), WHITE (Color.WHITE),		
		MAGENTA (Color.MAGENTA),CYAN (Color.CYAN),
		LIGHT_GRAY (Color.LIGHT_GRAY), DARK_GRAY (Color.DARK_GRAY)
		;
		
		
		private final Color collor;

		private Colors(Color c){
			this.collor = c;
		}
		
		public Color getColor(){
			return this.collor;
		}
	}

	protected Color[] computeColorPalette() {
		final int numColors = MAX_DEPTH;
		Color[] colors = new Color[numColors];
		for (int i = 0; i < numColors; i++) {
			final boolean isEven = i % 2 == 0;
			int rComp = isEven ? this.corectColorRGB(30 + (i * 25) + 100)
					: this.corectColorRGB(COLORMAXRGB - (30 + (i * 25) + 100));
			int gComp = isEven ? this.corectColorRGB(COLORMAXRGB / (i + 1) + 100)
					: this.corectColorRGB(COLORMAXRGB - (COLORMAXRGB / (i + 1) + 100));
			int bComp = isEven ? this.corectColorRGB(COLORMAXRGB - (i * 25) + 100)
					: this.corectColorRGB(COLORMAXRGB - (i * 25) + 100);
			int alpha = (rComp + gComp + bComp) / 3;
			colors[i] = new Color(rComp, gComp, bComp, alpha);
		}
		return colors;

	}
	
	//TODO	l8r
	protected Color[] computeRandomColorPalette() {/*Random*/
		final int numColors = MAX_DEPTH;
		Color[] colors = new Color[numColors];
		for (int i = 0; i < numColors; i++) {
			final boolean isEven = i % 2 == 0;
			Random rand = new Random();
			
			int rRand = rand.nextInt(COLORMAXRGB);
			int gRand = rand.nextInt(COLORMAXRGB);
			int bRand = rand.nextInt(COLORMAXRGB);
			
			int rComp = isEven ? this.corectColorRGB(30 + (rRand * 25) + 100)
					: this.corectColorRGB(COLORMAXRGB - (30 + (rRand * 25) + 100));
			int gComp = isEven ? this.corectColorRGB(COLORMAXRGB / (gRand + 1) + 100)
					: this.corectColorRGB(COLORMAXRGB - (COLORMAXRGB / (gRand + 1) + 100));
			int bComp = isEven ? this.corectColorRGB(COLORMAXRGB - (bRand * 25) + 100)
					: this.corectColorRGB(COLORMAXRGB - (bRand * 25) + 100);
			int alpha = (rComp + gComp + bComp) / 3;
			colors[i] = new Color(rComp, gComp, bComp, alpha);
		}
		return colors;

	}
	
	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(double rot) {
		this.rotation = rot;
	}
	

	


	public String getRowColMixType() {
		return this.rowColMixType;
	}

	public void setRowColMixType(String ty) {
		this.rowColMixType=ty;
	}

	public int getPower() {
		return this.power;
	}

	public void setPower(int pow) {
		this.power = pow;
	}

	public double getBound() {
		return this.bound;
	}

	public void setBound(double bod) {
		this.bound = bod;
	}

	public String getPxXTransformation() {
		return this.pxXTransformation;
	}

	public void setPxXTransformation(String transform) {
		this.pxXTransformation = transform;
	}

	public String getPxYTransformation() {
		return this.pxYTransformation;
	}

	public void setPxYTransformation(String transform) {
		this.pxYTransformation = transform;
	}

	public String getPixXYOperation() {
		return this.pixXYOperation;
	}

	public void setPixXYOperation(String operation) {
		this.pixXYOperation = operation;
	}

	public String getUseFuncConst() {
		return this.useFuncConst;
	}

	public void setUseFuncConst(String ufc) {
		this.useFuncConst = ufc;
	}

	public String getUseFuncPixel() {
		return this.useFuncPixel;
	}

	public void setUseFuncPixel(String ufp) {
		this.useFuncPixel = ufp;
	}

//	//https://en.wikipedia.org/wiki/Julia_set#Field_lines
//	private boolean isFatou = false;	//todo	-	rename to getFieldLines??
//	public boolean isFatou() {
//		return isFatou;
//	}
//
//	public void setFatou(boolean isFat) {
//		isFatou = isFat;
//	}
	
	protected ComplexNumber getFatouValue(ComplexNumber z) {
		double x = z.real();
		double y = z.imaginary();
		return this.getFatouValue(x,y);
	}

	protected ComplexNumber getFatouValue(double x, double y) {
		ComplexNumber z = new ComplexNumber(x, y);
		final ComplexNumber one = new ComplexNumber(1.0, 0.0);
		final ComplexNumber six = new ComplexNumber(6.0, 0.0);
		final ComplexNumber two = new ComplexNumber(2.0, 0.0);
		
	
		final ComplexNumber numerator = (one.minus(z.power(3).divides(six)));
		final ComplexNumber denom = ((z.minus(z.power(2).divides(two)))).power(2);
		z = ( numerator.divides(denom) );
		return z;
	}
	
//	/////////////////////////
//	//	z=z^2+c
//	////////////////////////
//	private boolean isZSq = false;	//todo	-	rename to getFieldLines??
//	public boolean isZSq() {
//		return isZSq;
//	}
//
//	public void setZSq(boolean iszsq) {
//		isZSq = iszsq;
//	}
//	
//
//	protected ComplexNumber getZSqValue(ComplexNumber z) {
//		double x = z.real();
//		double y = z.imaginary();
//		return this.getZSqValue(x,y);
//	}
//
//	protected ComplexNumber getZSqValue(double x, double y) {
//		return new ComplexNumber(x, y).power(2);
//	}
//	///////////
//	//endshttps://en.wikipedia.org/wiki/Julia_set#Field_lines
//	
//	
//
//	//http://paulbourke.net/fractals/juliaset/
//	/*	Julia was  interested  in  the
//	 * 	iterative properties of a more general expression, 	 * 
//	 * 	namely 
//	 * 
//	 * z^4 + z^3/(z-1) + z^2/(z^3 + 4*z^2 + 5) + c*/
//	private boolean isClassicJulia=false;
//	public boolean isClassicJulia() {
//		return isClassicJulia;
//	}
//
//	public void setClassicJulia(boolean isClassicJulia) {
//		this.isClassicJulia = isClassicJulia;
//	}
//	protected ComplexNumber getClassicJulia(ComplexNumber z) {
//		double x = z.real();
//		double y = z.imaginary();
//		return this.getClassicJulia(x,y);
//	}
//	
//	protected ComplexNumber getClassicJulia(double x, double y) {
//		ComplexNumber z = new ComplexNumber(x, y);
//		final ComplexNumber one = new ComplexNumber(1.0, 0.0);
//		final ComplexNumber four = new ComplexNumber(4.0, 0.0);
//		final ComplexNumber five = new ComplexNumber(5.0, 0.0);
//
//		final ComplexNumber first = z.power(4);
//		final ComplexNumber second = (z.power(3)).divides(z.minus(one));
//		final ComplexNumber third = (z.power(2)).divides((z.power(3)).plus(four.times(z.power(2))).plus(five));
//
//		z = first.plus(second).plus(third);
//		return z;
//	}
//	
	public boolean isReversePixelCalculation() {
		return this.reversePixelCalculation;
	}

	public void setReversePixelCalculation(boolean reverse) {
		this.reversePixelCalculation = reverse;
	}

	public boolean isUseLyapunovExponent() {
		return this.useLyapunovExponent;
	}

	public void setUseLyapunovExponent(boolean useLExp) {
		this.useLyapunovExponent = useLExp;
	}
	
	

	public String getColorChoice() {
		return this.colorChoice;
	}

	public void setColorChoice(String choice) {
		this.colorChoice = choice;
	}
	
	//	called when SampleMix (single) is selected, NOT when vary color selected
	public void setColorChoice(String[] choice) {
		this.colorChoice = choice[0].replaceAll(WHITESPACE,EMPTY);
//System.out.println("choice-length is "+choice.length);
		if (choice.length == 3) {
			this.setRgbStartVals(this.intiFyRgbStartVals(choice[1].replaceAll(WHITESPACE,EMPTY)));
			this.setRgbDivisors(this.intiFyRgbDivisors(choice[2].replaceAll(WHITESPACE,EMPTY)));
		}
		
		if(this.colorChoice.equals("ColorBlowout")||this.colorChoice.equals("ColorSuperBlowout")){
			this.setColorBlowoutType(choice[1]);
		}

	}

	private int[] intiFyRgbStartVals(String startVals) {
		switch( startVals ) {
			case "POW2_4_200" :			return POW2_4_200;
			case "POW2_2_128" :			return POW2_2_128;
			case "POW2_2_F4" :			return POW2_2_F4;
			case "POW3_3_243" :			return POW3_3_243;
			case "EQUAL_PARTS_40" :		return EQUAL_PARTS_40;
			case "EQUAL_PARTS_50" :		return EQUAL_PARTS_50;
			case "EQUAL_PARTS_25" :		return EQUAL_PARTS_25;
		}
		return POW2_4_200;
	}
	

	private int[] intiFyRgbDivisors(String divVals) {
		switch( divVals ) {
			case "FRST_SIX_PRIMES" :		return FRST_SIX_PRIMES;
			case "FRST_SIX_ODDS" :			return FRST_SIX_ODDS;
			case "FRST_SIX_FIBS" :			return FRST_SIX_FIBS;
		}
		return FRST_SIX_PRIMES;
	}

	public boolean isSavePixelInfo2File() {
		return this.savePixelInfo2File;
	}

	private FileWriter fw;

	public void setSavePixelInfo2File(boolean save2File) {
		this.savePixelInfo2File = save2File;

		if (save2File) {
			try {
				this.fw = new FileWriter("images\\" + this.getClass().getName()+"__"+System.currentTimeMillis()+".txt",true);

				fw.write("Set:\t" + this.getClass().getName().replace("org.bawaweb.ui.sierpinkski.","") + NEW_LINE);
				fw.write("x_Center:\t" + FractalBase.xC + NEW_LINE);
				fw.write("y_Center:\t" + FractalBase.yC + NEW_LINE);
				fw.write("scaleSize:\t" + FractalBase.scaleSize + NEW_LINE);
				fw.write("Maximum Iterations:\t" + maxIter + NEW_LINE);
				fw.write("Power:\t" + this.power + NEW_LINE);
				fw.write("X_Transformation:\t" + this.pxXTransformation + NEW_LINE);
				fw.write("Y_Transformation:\t" + this.pxYTransformation + NEW_LINE);
				fw.write("X_Y_Operation:\t" + this.pixXYOperation + NEW_LINE);
				fw.write("ReversePixelCalculation:\t" + this.reversePixelCalculation + NEW_LINE);
				fw.write("Z=(X,Y) PixelFunction:\t" + this.useFuncPixel + NEW_LINE);
				fw.write("f(C) ConstantFunction:\t" + this.useFuncConst + NEW_LINE);
				fw.write("Z=(X,Y) => C Pixel_Constant+Operaion:\t" + this.pxConstOperation + NEW_LINE);
				fw.write("Boundary:\t" + this.bound + NEW_LINE);

				if (this.colorChoice.equals(Color_Palette)) {
					fw.write("ColorPalette" + NEW_LINE);
				} else if (this.colorChoice.equals(BlackWhite)) {
					fw.write("BlackWhite" + NEW_LINE);
				} else if (this.colorChoice.equals(ComputeColor)) {
					fw.write("ColorComputed" + NEW_LINE);
				}

//				fw.write("Constant:\tTBD" + NEW_LINE);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void closePixelFile() {
		try {
			this.fw.flush();
			this.fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void appendPixelInfo2File(int row, int col, int rgbColor){
		try {
			this.fw.write("[" + row + "," + col + "," + rgbColor + "]" + NEW_LINE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void appendConstantInfo2File(String constant){return;
		/*try {
			this.fw.write("[" + constant + "]" + NEW_LINE);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
	
	
	class Pixel {

		int row, column;
		int colorRGB;
		
		ComplexNumber compVal;

		public Pixel() {
			super();
		}

		public Pixel(int r, int c, int clr) {
			this(r,c);
			this.setColorRGB(clr);
		}

		public Pixel(int r, int c) {
			this.setRow(r);
			this.setColumn(c);
			this.setColorRGB(0);
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public double getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public int getColorRGB() {
			return colorRGB;
		}

		public void setColorRGB(int colorRGB) {
			this.colorRGB = colorRGB;
		}

		public ComplexNumber getCompVal() {
			return compVal;
		}

		public void setCompVal(ComplexNumber comp) {
			this.compVal = comp;
		}

		@Override
		public String toString() {
			return "Pixel [row=" + row + ", column=" + column + ", colorRGB=" + colorRGB + ", compVal=" + compVal + "]";
		}

	}

	class Line {
		double x, y, length, angle;

		public Line(Point p1, Point p2) {
			super();
			this.x = p1.x;
			this.y = p1.y;

			this.length = this.length(p1, p2);
			this.angle = this.angle(p1, p2);
		}

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
		

		public Point midPt(Point pLeft, Point pRight) {
			return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 2), (int) (pLeft.y + (pRight.y - pLeft.y) / 2));
		}
		
		public Point thirdPt(Point pLeft, Point pRight) {
			return new Point((int) (pLeft.x + (pRight.x - pLeft.x) / 3), (int) (pLeft.y + (pRight.y - pLeft.y) / 3));
		}
		
		public Point twoThirdPt(Point pLeft, Point pRight) {
			return new Point((int) (pLeft.x + (pRight.x - pLeft.x) * 2 / 3), (int) (pLeft.y + (pRight.y - pLeft.y) * 2 / 3));
		}			
		
		public double length(Point p1, Point p2) {
			return this.length = Math.sqrt( Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) );
		}
		
		//https://stackoverflow.com/questions/9970281/java-calculating-the-angle-between-two-points-in-degrees
		
		/**
		 * Calculates the angle from centerPt to targetPt in degrees.
		 * The return should range from [0,360), rotating CLOCKWISE, 
		 * 0 and 360 degrees represents NORTH,
		 * 90 degrees represents EAST, etc...
		 *
		 * Assumes all points are in the same coordinate space.  If they are not, 
		 * you will need to call SwingUtilities.convertPointToScreen or equivalent 
		 * on all arguments before passing them  to this function.
		 *
		 * @param centerPt   Point we are rotating around.
		 * @param targetPt   Point we want to calcuate the angle to.  
		 * @return angle in degrees.  This is the angle from centerPt to targetPt.
		 */
		protected double angle(Point centerPt, Point targetPt)
		{
		    // calculate the angle theta from the deltaY and deltaX values
		    // (atan2 returns radians values from [-PI,PI])
		    // 0 currently points EAST.  
		    // NOTE: By preserving Y and X param order to atan2,  we are expecting 
		    // a CLOCKWISE angle direction.  
		    double theta = Math.atan2(targetPt.y - centerPt.y, targetPt.x - centerPt.x);

		    // rotate the theta angle clockwise by 90 degrees 
		    // (this makes 0 point NORTH)
		    // NOTE: adding to an angle rotates it clockwise.  
		    // subtracting would rotate it counter-clockwise
		    theta += Math.PI/2.0;

		    // convert from radians to degrees
		    // this will give you an angle from [0->270],[-180,0]
		    double angle = Math.toDegrees(theta);

		    // convert to positive range [0-360)
		    // since we want to prevent negative angles, adjust them now.
		    // we can assume that atan2 will not return a negative value
		    // greater than one partial rotation
		    if (angle < 0) {
		        angle += 360;
		    }

		    return angle;
		}
		
		@Override
		public String toString(){
			String lineInfo="";
			lineInfo+="("+(int)this.x+","+(int)this.y+") to ("+(int)this.getX2()+","+(int)this.getY2()+")  angle="+this.angle;
			return lineInfo;
			
		}


	}
	
	//	used in complex number
	final String START_BRACKET = "(";
	final String END_BRACKET = ")";
	final String WHITESPACE = " ";
	final String EMPTY = "";
	final String PIPE = "\\|";
	
	final ComplexNumber i = new ComplexNumber(0.0, 1.0);
	
	final ComplexNumber DUMMY = new ComplexNumber(Double.NaN,Double.NaN);
	ComplexNumber Z = DUMMY;


	protected boolean applyCustomFormula = false;

///////////////////////////endComplexNumberremove//////////////////
//	class ComplexNumber {
//		final String I = "i";
//		final double real;		// the real part
//		final double imaginary;	// the imaginary part
//		
//		// return the real or imaginary part
//		public double real(){
//			return real;
//		}
//		
//		public double imaginary(){
//			return imaginary;
//		}
//
//		
//		public ComplexNumber(double r) {
//			super();
//			this.real = r;
//			this.imaginary = 0.0;
//		}
//		
//		
//		/**
//		 * @param r	-	real part
//		 * @param i	-	imaginary part
//		 */
//		public ComplexNumber(double r, double i) {
//			super();
//			this.real = r;
//			this.imaginary = i;
//		}
//		
//		
//		//convert	0.2-7.5i	//	5.7	//	-1.1i	//-4.3-0.7i
//		//3.1+i	//-1.8+0.8i	//	-0.8
//		//	1.1i
//		//	NO	leading	plus	SO	+0.8+1.5i	invalid
//		//						but	0.8+1.5i	valid
//		//	No stars for i		SO	1.1*i		invalid
//		//						but	1.1i		valid
//		public ComplexNumber(String c) {
////			System.out.println("c[1] is "+c);
//			c = c.replaceAll(WHITESPACE,EMPTY);
////			System.out.println("c[2] is "+c);
//			
//			final String plus = "+";
//			final String minus = "-";
//			final String[] EMPTYARR = new String[] {};
//
//			int i_index = c.indexOf(I);					//System.out.println("i_index="+i_index);
//			final int plusIndex = c.indexOf(plus);		//System.out.println("plusIndex"+plusIndex);
//			final int minusIndex = c.indexOf(minus);	//System.out.println("minusIndex="+minusIndex);
//
//			boolean hasPlus = plusIndex > -1;			//System.out.println("hasPlus is "+hasPlus);
//			boolean hasMinus = minusIndex > -1;			//System.out.println("hasMinus is "+hasMinus);
//
//			int plusCount = 0;
//			for (int i = 0; i < c.length(); i++) {
//				if (plus.equals(String.valueOf(c.charAt(i)))) {
//					plusCount += 1;
//				}
//			}											//System.out.println("plusCount is "+plusCount);
//
//			int minusCount = 0;
//			for (int i = 0; i < c.length(); i++) {
//				if (minus.equals(String.valueOf(c.charAt(i)))) {
//					minusCount += 1;
//				}
//			}											//System.out.println("minusCount is "+minusCount);
//
//			if (i_index == -1) {
//				this.real = Double.parseDouble(c);
//				this.imaginary = 0.0;
//			} else if (minusCount == 0) {
//				if (plusCount == 1) {
//					String realPart = c.substring(0, plusIndex);
//					String imaginaryPart = c.substring(plusIndex);
//					this.real = Double.parseDouble(realPart);
//					if (plusIndex + 1 == i_index/* && imaginaryPart.equals(I)*/) {
//						this.imaginary = 1.0;
//					} else {
//						this.imaginary = Double.parseDouble(imaginaryPart.replace(I, EMPTY));
//					}
//				} else {
//System.out.println("WHYr v here?");
//this.real=Double.NaN;this.imaginary=Double.NaN;
//				}
//			} else {
//				if (plusCount == 1) {
//					String realPart = c.substring(0, plusIndex);
//					String imaginaryPart = c.substring(plusIndex);
//					this.real = Double.parseDouble(realPart);
//					if (plusIndex + 1 == i_index && imaginaryPart.equals(I)) {
//						this.imaginary = 1.0;
//					} else {
//						this.imaginary = Double.parseDouble(imaginaryPart.replace(I, EMPTY));
//					}
//				} else {
//					if (!c.startsWith(minus)) {
//						String realPart = c.substring(0, minusIndex);
//						String imaginaryPart = c.substring(minusIndex);
//						this.real = Double.parseDouble(realPart);
//						if (minusIndex + 1 == i_index && imaginaryPart.equals(I)) {
//							this.imaginary = 1.0;
//						} else {
//							this.imaginary = Double.parseDouble(imaginaryPart.replace(I, EMPTY));
//						} 
//					} else {
//						String c2 = c.substring(1,c.length()-1);	//remove leading minus
//						System.out.println("C2 us "+c2);
//						final int c2MinusIndex = c2.indexOf(minus);
//						if (c2MinusIndex>0) {
//							this.real = Double.parseDouble(c2.substring(0, c2MinusIndex)) * -1;
//							this.imaginary = Double.parseDouble(c2.substring(c2MinusIndex).replace(I, EMPTY));
//						}else{
//							this.real = Double.parseDouble(c.replace(I,EMPTY));
//							this.imaginary=0;
//						}
//					}
//				}
//			}
//		}
//		
//		
//		// return a new Complex object whose value is (this * b)
//	    public ComplexNumber times(ComplexNumber b) {
//	    	ComplexNumber a = this;
//	        double real = a.real * b.real - a.imaginary * b.imaginary;
//	        double imag = a.real * b.imaginary + a.imaginary * b.real;
//	        return new ComplexNumber(real, imag);
//	    }
//	    
//	 // return a new Complex object whose value is (this + b)
//	    public ComplexNumber plus(ComplexNumber b) {
//	    	ComplexNumber a = this;             // invoking object
//	        double real = a.real + b.real;
//	        double imag = a.imaginary + b.imaginary;
//	        return new ComplexNumber(real, imag);
//	    }
//	    
//	 // return a new Complex object whose value is (this - b)
//	    public ComplexNumber minus(ComplexNumber b) {
//	    	ComplexNumber a = this;             // invoking object
//	        double real = a.real - b.real;
//	        double imag = a.imaginary - b.imaginary;
//	        return new ComplexNumber(real, imag);
//	    }
//
//		/* (non-Javadoc)
//		 * @see java.lang.Object#toString()
//		 */
//		@Override
//		public String toString() {
//			if (imaginary == 0.0 && real != 0.0)
//				return real + "";
//			if (real == 0.0 && imaginary != 0.0)
//				return imaginary + "i";
//			if (real < 0.0) {
//				if (imaginary < 0.0)
//					return real + " - " + (-1*imaginary) + "i";
//				else
//					return real + " + " + imaginary + "i";
//			} else if (real > 0.0) {
//				if (imaginary < 0.0)
//					return real + " - " + (-1*imaginary) + "i";
//				else
//					return real + " + " + imaginary + "i";
//			}
//			return real + " + " + imaginary + "i";
////			return "ComplexNumber [real=" + real + ", imaginary=" + imaginary + "]";
//		}
//		
//		// See Section 3.3.
//	    public boolean equals(Object x) {
//	        if (x == null) return false;
//	        if (this.getClass() != x.getClass()) return false;
//	        ComplexNumber that = (ComplexNumber) x;
//	        return (this.real == that.real) && (this.imaginary == that.imaginary);
//	    }
//
//	    // See Section 3.3.
//	    public int hashCode() {
//	        return Objects.hash(real, imaginary);
//	    }
//
//		public double abs() {
//			return Math.hypot(real, imaginary);
//		}
//		
//		
//		/**
//		 * https://math.stackexchange.com/questions/44406/how-do-i-get-the-square-root-of-a-complex-number
//		 * 
//		 * z=c+d*i				c=real,	d=imaginary
//		 * 
//		 * sqroot(z)=a+b*i
//		 * 
//		 * a=root((c+root(c^2+d^2))/2) 
//		 * b=d/|d|*root((-c+root(c^2+d^2))/2)
//		 * 
//		 * d/|d|	==> b has same sign as d
//		 * 
//		 * @return
//		 */
//		public ComplexNumber sqroot() {
////			System.out.println("Real==="+real+"---Img:=="+imaginary);
//			double a = Math.sqrt(Math.abs(real) + ( Math.pow(Math.abs(real), 2.0) + Math.pow(Math.abs(imaginary), 2.0) ) / 2.0);
//			double b = Math.sqrt( Math.abs(real /** (-1.0)*/) + Math.sqrt( Math.pow(Math.abs(real), 2.0) + Math.pow(Math.abs(imaginary), 2.0) ) / 2.0);
////System.out.println("a==="+a);
////System.out.println("b==="+b);
//			if (real == 0.0 && imaginary != 0.0) {
////				System.out.println("returning___new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary)));"+new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary))));
//				return new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary)));
//			}
//			if (imaginary > 0.0) {
////				System.out.println("returning___new ComplexNumber(a, b);"+new ComplexNumber(a, b));
//				return new ComplexNumber(a, b);
//			} else if (imaginary < 0.0) {
////				System.out.println("returning___new ComplexNumber(a, b * (-1.0));"+new ComplexNumber(a,( Math.abs(b) * (-1.0))));
//			
//				return new ComplexNumber(a,( Math.abs(b) * (-1.0)));
//			}
////			System.out.println("returning___new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0);"+new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0));
//			return new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0);
//		}
//		
//		public ComplexNumber curoot(){
//			return sqroot();	//for-now
//		}
//
//		
//		/**https://en.wikipedia.org/wiki/Complex_logarithm
//		//
//		//	z=x+y*i
//		//	log(z)=ln(root(x^2+y^2))+atan2(y,x)*i
//		 * 
//		 * @return
//		 */
//		public ComplexNumber ln() {
//			double a = Math.log(Math.sqrt(Math.pow(real, 2.0) + Math.pow(imaginary, 2.0)));
//			double b = Math.atan2(imaginary, real);
//			return new ComplexNumber(a, b);
//		}
//		
//		
//		/*public final ComplexNumber one = new ComplexNumber(1.0, 0.0);*/
//
//	    // return a new Complex object whose value is the complex exponential of this
//		// return e ^ z
//	    public ComplexNumber exp() {
//	        return new ComplexNumber(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
//	    }
//
//	    // return a new Complex object whose value is the complex sine of this
//	    public ComplexNumber sin() {
//	        return new ComplexNumber(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
//	    }
//
//	    // return a new Complex object whose value is the complex cosine of this
//	    public ComplexNumber cos() {
//	        return new ComplexNumber(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
//	    }
//
//	    // return a new Complex object whose value is the complex tangent of this
//	    public ComplexNumber tan() {
//	        return sin().divides(cos());
//	    }
//	    
//
//	    // return a / b
//	    public ComplexNumber divides(ComplexNumber b) {
//	        ComplexNumber a = this;
//	        return a.times(b.reciprocal());
//	    }
//	 // return a new Complex object whose value is the reciprocal of this
//	    public ComplexNumber reciprocal() {
//	        double scale = real*real + imaginary*imaginary;
//	        return new ComplexNumber(real / scale, -imaginary / scale);
//	    }
//
//		public ComplexNumber power(int power) {
//			if (power > 0) {
//				int iter = 1;
//				final ComplexNumber a = this;
//				ComplexNumber powered = a;
//				for (int i = 1; i < power; i++) {
//					powered = powered.times(a);
//				}
//				return powered;
//			} else if (power < 0) {
//				final ComplexNumber a = this;
//				ComplexNumber powered = a.reciprocal();
//				int powAbs = Math.abs(power);
//				for (int i = 1; i < powAbs; i++) {
//					powered = powered.times(powered);
//				}
//				return powered;
//
//			}	
//			//power==0
//			return new ComplexNumber(1.0,0.0);	//	^0==1
//		}
//
//		public ComplexNumber sine() {
//			double sineR = Math.sin(this.real) * Math.cosh(this.imaginary);
//			double sineI = Math.cos(this.real) * Math.sinh(this.imaginary);
//			return new ComplexNumber(sineR,sineI);
//		}
//		
//		//arcsin
//		public ComplexNumber inverseSine(){
//			return new ComplexNumber(1.0, 0.0).divides(this.sine());
//		}
//		
//		public ComplexNumber cosine() {
//			// cos(x)*cosh(y) - isin(x)*sinh(y)
//			double cosR = Math.cos(this.real) * Math.cosh(this.imaginary);
//			double cosI = -Math.sin(this.real) * Math.sinh(this.imaginary);
//			return new ComplexNumber(cosR, cosI);
//		}
//		
//		//arccos
//		public ComplexNumber inverseCosine(){
//			return new ComplexNumber(1.0, 0.0).divides(this.cosine());
//		}
//		
//		public ComplexNumber tangent(){
//			return this.sine().divides(this.cosine());
//		}
//		
//		//arctan
//		public ComplexNumber inverseTangent(){
//			return new ComplexNumber(1.0, 0.0).divides(this.tangent());
//		}
//		
//	}
//	
//	
	/////////////////////////endComplexNumberremove//////////////////
	
//	
//	////////////////////////Expr_remove/////////////////////////////////
//	// https://math.hws.edu › javanotes › source › chapter8 › Expr
//	/*
//	 * An object belonging to the class Expr is a mathematical expression that
//	 * can involve:
//	 * 
//	 * -- real numbers such as 2.7, 3, and 12.7e-12 -- the variable Z / z --
//	 * arithmetic operators +, -, *, /, and ^ , where the last of these
//	 * represents raising to a power -- the mathematical functions sin, cos,
//	 * tan, sec, csc, cot, arcsin, arccos, arctan, exp, ln, log2, log10, abs,
//	 * and sqrt, where ln is the natural log, log2 is the log to the base 2,
//	 * log10 is the log to the base 10, abs is absolute value, and sqrt is the
//	 * square root -- parentheses
//	 * 
//	 * Some examples would be: x^2 + x + 1 sin(2.3*x-7.1) - cos(7.1*x-2.3) x - 1
//	 * 42 exp(x^2) - 1
//	 * 
//	 * The trigonometric functions work with radians, not degrees. The parameter
//	 * of a function must be enclosed in parentheses, so that "sin x" is NOT
//	 * allowed.
//	 * 
//	 * An Expr is constructed from a String that contains its definition. If an
//	 * error is found in this definition, the constructor will throw an
//	 * IllegalArgumentException with a message that describes the error and the
//	 * position of the error in the string. After an Expr has been constructed,
//	 * its defining string can be retrieved using the method getDefinition().
//	 * 
//	 * The main operation on an Expr object is to find its value, given a value
//	 * for the variable x. The value is computed by the value() method. If the
//	 * expression is undefined at the given value of x, then the number returned
//	 * by value() method will be the special "non-a-number number", Double.NaN.
//	 * (The boolean-valued function Double.isNaN(v) can be used to test whether
//	 * the double value v is the special NaN value. For technical reasons, you
//	 * can't just use the == operator to test for this value.)
//	 * 
//	 */
//
//	class Expr {
//
//		private String type = "Complex";
//
//		// ----------------- public interface
//		// ---------------------------------------
//		public Expr() {
//		}
//
//		/**
//		 * Construct an expression, given its definition as a string. This will
//		 * throw an IllegalArgumentException if the string does not contain a
//		 * legal expression.
//		 */
//		public Expr(String definition) {
//			parse(definition);
//		}
//
//		/**
//		 * 
//		 * @param definition
//		 *            ==
//		 * @param expssionType
//		 *            == "Complex","Real"
//		 * 
//		 *            Complex ==> f(Z,i) Real ==> f(x,y,z) [Real number domain
//		 *            only. tuple solvers for ODEs]
//		 */
//		public Expr(String definition, String expssionType) {
//			this.type = expssionType;
//			this.parse(definition);
//
//		}
//
//		/**
//		 * Computes the value of this expression, when the variable x has a
//		 * specified value. If the expression is undefined for the specified
//		 * value of x, then Double.NaN is returned.
//		 * 
//		 * @param x
//		 *            the value to be used for the variable x in the expression
//		 * @return the computed value of the expression
//		 */
//		public ComplexNumber value(ComplexNumber x) {
//			return eval(x);
//		}
//
//		/**
//		 * Computes the value of this expression, when the variable x has a
//		 * specified value. If the expression is undefined for the specified
//		 * value of x, then Double.NaN is returned.
//		 * 
//		 * @param x
//		 *            the value to be used for the variable x in the expression
//		 * @return the computed value of the expression
//		 */
//		public double value(final double x) {
//			return this.eval(x, 0.0, 0.0);
//		}
//
//		public double value(final double x, final double y, final double z) {
//			return eval(x, y, z);
//		}
//
//		/**
//		 * Return the original definition string of this expression. This is the
//		 * same string that was provided in the constructor.
//		 */
//		public String toString() {
//			return definition;
//		}
//
//		// ------------------- private implementation details
//		// ----------------------------------
//
//		private String definition; // The original definition of the expression,
//									// as passed to the constructor.
//
//		private byte[] code; // A translated version of the expression,
//								// containing
//								// stack operations that compute the value of
//								// the expression.
//
//		private ComplexNumber[] stack; // A stack to be used during the
//		private double[] realStack; // evaluation of
//									// the expression.
//
//		private ComplexNumber[] constants; // An array containing all the
//		private double[] realConstants; // constants
//		// found in the expression.
//
//		private static final byte // values for code array; values >= 0 are
//									// indices into constants array
//		PLUS = -1, MINUS = -2, TIMES = -3, DIVIDE = -4, POWER = -5, SIN = -6, COS = -7, TAN = -8, COT = -9, SEC = -10,
//				CSC = -11, ARCSIN = -12, ARCCOS = -13, ARCTAN = -14, EXP = -15, LN = -16, LOG10 = -17, LOG2 = -18,
//				ABS = -19, SQRT = -20, UNARYMINUS = -21,
//				// x,y,z = f(real)
//				// Z,i = f(Complex_
//				VARIABLE_X = -22, VARIABLE_Y = -23, VARIABLE_Z = -24, VARIABLE_I = -25;
//
//		private String[] functionNames = {
//				// names of standard functions, used during parsing
//				"sin", "cos", "tan", "cot", "sec", "csc", "arcsin", "arccos", "arctan", "exp", "ln", "log10", "log2",
//				"abs", "sqrt" };
//
//		private String[] mathConstants = { "PI", "E" }; // Mathematical
//														// Constants
//
//		private double eval(final double variable_x, final double variable_y, final double variable_z) { 
//			// evaluate this expression for this value of these variables
//			try {
//				int top = 0;
//				for (int i = 0; i < codeSize; i++) {
//					if (code[i] >= 0)
//						realStack[top++] = realConstants[code[i]];
//					else if (code[i] >= POWER) {
//						double y = realStack[--top];
//						double x = realStack[--top];
//						double ans = Double.NaN;
//						switch (code[i]) {
//						case PLUS:
//							ans = x + y;
//							break;
//						case MINUS:
//							ans = x - y;
//							break;
//						case TIMES:
//							ans = x * y;
//							break;
//						case DIVIDE:
//							ans = x / y;
//							break;
//						case POWER:
//							ans = Math.pow(x, y);
//							break;
//						}
//						if (Double.isNaN(ans))
//							return ans;
//						realStack[top++] = ans;
//					} else if (code[i] == VARIABLE_X || code[i] == VARIABLE_Y || code[i] == VARIABLE_Z) {
//						if (code[i] == VARIABLE_X)
//							realStack[top++] = variable_x;
//						else if (code[i] == VARIABLE_Y)
//							realStack[top++] = variable_y;
//						else if (code[i] == VARIABLE_Z)
//							realStack[top++] = variable_z;
//					} else {
//						double x = realStack[--top];
//						double ans = Double.NaN;
//						switch (code[i]) {
//						case SIN:
//							ans = Math.sin(x);
//							break;
//						case COS:
//							ans = Math.cos(x);
//							break;
//						case TAN:
//							ans = Math.tan(x);
//							break;
//						case COT:
//							ans = Math.cos(x) / Math.sin(x);
//							break;
//						case SEC:
//							ans = 1.0 / Math.cos(x);
//							break;
//						case CSC:
//							ans = 1.0 / Math.sin(x);
//							break;
//						case ARCSIN:
//							if (Math.abs(x) <= 1.0)
//								ans = Math.asin(x);
//							break;
//						case ARCCOS:
//							if (Math.abs(x) <= 1.0)
//								ans = Math.acos(x);
//							break;
//						case ARCTAN:
//							ans = Math.atan(x);
//							break;
//						case EXP:
//							ans = Math.exp(x);
//							break;
//						case LN:
//							if (x > 0.0)
//								ans = Math.log(x);
//							break;
//						case LOG2:
//							if (x > 0.0)
//								ans = Math.log(x) / Math.log(2);
//							break;
//						case LOG10:
//							if (x > 0.0)
//								ans = Math.log(x) / Math.log(10);
//							break;
//						case ABS:
//							ans = Math.abs(x);
//							break;
//						case SQRT:
//							if (x >= 0.0)
//								ans = Math.sqrt(x);
//							break;
//						case UNARYMINUS:
//							ans = -x;
//							break;
//						}
//						if (Double.isNaN(ans))
//							return ans;
//						realStack[top++] = ans;
//
//					}
//				}
//			} catch (Exception e) {
//				return Double.NaN;
//			}
//			if (Double.isInfinite(realStack[0]))
//				return Double.NaN;
//			else
//				return realStack[0];
//		}
//
//		private ComplexNumber eval(ComplexNumber x2) { 
//			// evaluate this expression for this value of the variable
//			try {
//				int top = 0;
//				for (int index = 0; index < codeSize; index++) {
//					if (code[index] >= 0)
//						stack[top++] = constants[code[index]];
//					else if (code[index] >= POWER) {
//						ComplexNumber y = stack[--top];
//						ComplexNumber x = stack[--top];
//						ComplexNumber ans = new ComplexNumber(Double.NaN);
//						switch (code[index]) {
//						case PLUS:
//							ans = x.plus(y);
//							break;
//						case MINUS:
//							ans = x.minus(y);
//							break;
//						case TIMES:
//							ans = x.times(y);
//							break;
//						case DIVIDE:
//							ans = x.divides(y);
//							break;
//						case POWER:
//							ans = x.power((int) y.real);// Math.pow(x, y);
//							break;
//						}
//						if (Double.isNaN(ans.real))
//							return ans;
//						stack[top++] = ans;
//					} else if (code[index] == VARIABLE_Z) {
//						stack[top++] = x2;
//					} else if (code[index] == VARIABLE_I) {
//						stack[top++] = i;
//					} else {
//						ComplexNumber x = stack[--top];
//						ComplexNumber ans = new ComplexNumber(Double.NaN);
//						switch (code[index]) {
//						case SIN:
//							ans = x.sine();// Math.sin(x);
//							break;
//						case COS:
//							ans = x.cosine();// Math.cos(x);
//							break;
//						case TAN:
//							ans = x.tangent();// Math.tan(x);
//							break;
//						case COT:
//							ans = x.cosine().divides(x.sine()); // Math.cos(x) /
//																// Math.sin(x);
//							break;
//						case SEC:
//							ans = x.cosine().reciprocal();// 1.0 / Math.cos(x);
//							break;
//						case CSC:
//							ans = x.sine().reciprocal(); // 1.0 / Math.sin(x);
//							break;
//						case ARCSIN:
//							// if (Math.abs(x.real) <= 1.0)
//							ans = x.inverseSine();// Math.asin(x);
//							break;
//						case ARCCOS:
//							// if (Math.abs(x.real) <= 1.0)
//							ans = x.inverseCosine();// Math.acos(x);
//							break;
//						case ARCTAN:
//							ans = x.inverseTangent();// Math.atan(x);
//							break;
//						case EXP:
//							ans = x.exp();// Math.exp(x);
//							break;
//						case LN:
//							// if (x.real > 0.0)
//							ans = x.ln();// Math.log(x);
//							break;
//						/*
//						 * case LOG2: if (x > 0.0) ans = Math.log(x) /
//						 * Math.log(2); break; case LOG10: if (x > 0.0) ans =
//						 * Math.log(x) / Math.log(10); break;
//						 */
//						case ABS:
//							ans = new ComplexNumber(x.abs());// Math.abs(x);
//							break;
//						case SQRT:
//							// if (x.real >= 0.0)
//							ans = x.sqroot();// Math.sqrt(x);
//							break;
//						case UNARYMINUS:
//							ans = new ComplexNumber(-1.0).times(x);
//							break;
//						}
//						if (Double.isNaN(ans.real) || Double.isNaN(ans.imaginary))
//							return ans;
//						stack[top++] = ans;
//
//					}
//				}
//			} catch (Exception e) {
//				return new ComplexNumber(Double.NaN);
//			}
//			if (Double.isInfinite(stack[0].real) || Double.isInfinite(stack[0].imaginary))
//				return new ComplexNumber(Double.NaN);
//			else
//				return stack[0];
//		}
//
//		private int pos = 0, constantCt = 0, codeSize = 0; // data for use
//															// during parsing
//
//		private void error(String message) {
//			// called when an error occurs during parsing
//			throw new IllegalArgumentException("Parse error:  " + message + "  (Position in data = " + pos + ".)");
//		}
//
//		private int computeStackUsage() { // call after code[] is computed
//			int s = 0; // stack size after each operation
//			int max = 0; // maximum stack size seen
//			for (int i = 0; i < codeSize; i++) {
//				if (code[i] >= 0 || code[i] == VARIABLE_Z || code[i] == VARIABLE_I || code[i] == VARIABLE_X
//						|| code[i] == VARIABLE_Y) {
//					s++;
//					if (s > max)
//						max = s;
//				} else if (code[i] >= POWER)
//					s--;
//			}
//			return max;
//		}
//
//		private void parse(String definition) {
//			if (this.type.equals("Complex")) {
//				this.parseComplex(definition);
//			} else {
//				this.parseReal(definition);
//			}
//		}
//
//		private void parseReal(String definition) {
//			// Parse the definition and produce all
//			// the data that represents the expression
//			// internally; can throw IllegalArgumentException
//			if (definition == null || definition.trim().equals(""))
//				error("No data provided to Expr constructor");
//			this.definition = definition;
//			code = new byte[definition.length()];
//			realConstants = new double[definition.length()];
//			parseExpression();
//			skip();
//			if (next() != 0)
//				error("Extra data found after the end of the expression.");
//			int stackSize = computeStackUsage();
//			realStack = new double[stackSize];
//			byte[] c = new byte[codeSize];
//			System.arraycopy(code, 0, c, 0, codeSize);
//			code = c;
//			double[] A = new double[constantCt];
//			System.arraycopy(realConstants, 0, A, 0, constantCt);
//			realConstants = A;
//		}
//
//		private void parseComplex(String definition) {
//			// Parse the definition and produce all the data that represents the
//			// expression internally; can throw IllegalArgumentException
//			if (definition == null || definition.trim().equals(""))
//				error("No data provided to Expr constructor");
//			this.definition = definition;
//			code = new byte[definition.length()];
//			constants = new ComplexNumber[definition.length()];
//			parseExpression();
//			skip();
//			if (next() != 0)
//				error("Extra data found after the end of the expression.");
//			int stackSize = computeStackUsage();
//			stack = new ComplexNumber[stackSize];
//			byte[] c = new byte[codeSize];
//			System.arraycopy(code, 0, c, 0, codeSize);
//			code = c;
//			ComplexNumber[] A = new ComplexNumber[constantCt];
//			System.arraycopy(constants, 0, A, 0, constantCt);
//			constants = A;
//		}
//
//		private char next() {
//			// return next char in data or 0 if data is all used up
//			if (pos >= definition.length())
//				return 0;
//			else
//				return definition.charAt(pos);
//		}
//
//		private void skip() { // skip over white space in data
//			while (Character.isWhitespace(next()))
//				pos++;
//		}
//
//		// remaining routines do a standard recursive parse of the expression
//		private void parseExpression() {
//			boolean neg = false;
//			skip();
//			if (next() == '+' || next() == '-') {
//				neg = (next() == '-');
//				pos++;
//				skip();
//			}
//			parseTerm();
//			if (neg)
//				code[codeSize++] = UNARYMINUS;
//			skip();
//			while (next() == '+' || next() == '-') {
//				char op = next();
//				pos++;
//				parseTerm();
//				code[codeSize++] = (op == '+') ? PLUS : MINUS;
//				skip();
//			}
//		}
//
//		private void parseTerm() {
//			parseFactor();
//			skip();
//			while (next() == '*' || next() == '/') {
//				char op = next();
//				pos++;
//				parseFactor();
//				code[codeSize++] = (op == '*') ? TIMES : DIVIDE;
//				skip();
//			}
//		}
//
//		private void parseFactor() {
//			parsePrimary();
//			skip();
//			while (next() == '^') {
//				pos++;
//				parsePrimary();
//				code[codeSize++] = POWER;
//				skip();
//			}
//		}
//
//		private void parsePrimary() {
//			skip();
//			char ch = next();
//			if (ch == 'z' || ch == 'Z' || ch == 'i' || ch == 'I' || ch == 'x' || ch == 'X' || ch == 'y' || ch == 'Z') {
//				pos++;
//
//				if (ch == 'z' || ch == 'Z') {
//					code[codeSize++] = VARIABLE_Z;
//				} else if (ch == 'i' || ch == 'I') {
//					code[codeSize++] = VARIABLE_I;
//				} else if (ch == 'x' || ch == 'X') {
//					code[codeSize++] = VARIABLE_X;
//				} else if (ch == 'y' || ch == 'Y') {
//					code[codeSize++] = VARIABLE_Y;
//				}
//			} else if (Character.isLetter(ch))
//				parseWord();
//			else if (Character.isDigit(ch) || ch == '.')
//				parseNumber();
//			else if (ch == '(') {
//				pos++;
//				parseExpression();
//				skip();
//				if (next() != ')')
//					error("Expected a right parenthesis.");
//				pos++;
//			} else if (ch == ')')
//				error("Unmatched right parenthesis.");
//			else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^')
//				error("Operator '" + ch + "' found in an unexpected position.");
//			else if (ch == 0)
//				error("Unexpected end of data in the middle of an expression.");
//			else
//				error("Illegal character '" + ch + "' found in data.");
//		}
//
//		private void parseWord() {
//			String w = "";
//			while (Character.isLetterOrDigit(next())) {
//				w += next();
//				pos++;
//			}
//			w = w.toLowerCase();
//
//			/*
//			 * for(int i = 0; i < mathConstants.length; i++) {
//			 * if(w.equals(mathConstants[i])) { skip(); parseExpression();
//			 * skip(); } }
//			 */
//
//			for (int i = 0; i < functionNames.length; i++) {
//				if (w.equals(functionNames[i])) {
//					skip();
//					if (next() != '(')
//						error("Function name '" + w + "' must be followed by its parameter in parentheses.");
//					pos++;
//					parseExpression();
//					skip();
//					if (next() != ')')
//						error("Missing right parenthesis after parameter of function '" + w + "'.");
//					pos++;
//					code[codeSize++] = (byte) (SIN - i);
//					return;
//				}
//			}
//			error("Unknown word '" + w + "' found in data.");
//		}
//
//		private void parseNumber() {
//			String w = "";
//			while (Character.isDigit(next())) {
//				w += next();
//				pos++;
//			}
//			if (next() == '.') {
//				w += next();
//				pos++;
//				while (Character.isDigit(next())) {
//					w += next();
//					pos++;
//				}
//			}
//			if (w.equals("."))
//				error("Illegal number found, consisting of decimal point only.");
//			if (next() == 'E' || next() == 'e') {
//				w += next();
//				pos++;
//				if (next() == '+' || next() == '-') {
//					w += next();
//					pos++;
//				}
//				if (!Character.isDigit(next()))
//					error("Illegal number found, with no digits in its exponent.");
//				while (Character.isDigit(next())) {
//					w += next();
//					pos++;
//				}
//			}
//
//			if (this.type.equals("Complex")) {
//				ComplexNumber d = new ComplexNumber(Double.NaN);
//				try {
//					d = new ComplexNumber(Double.valueOf(w).doubleValue());
//				} catch (Exception e) {
//				}
//				if (Double.isNaN(d.real))
//					error("Illegal number '" + w + "' found in data.");
//				code[codeSize++] = (byte) constantCt;
//				constants[constantCt++] = d;
//			} else {
//				double d = Double.NaN;
//				try {
//					d = Double.valueOf(w).doubleValue();
//				} catch (Exception e) {
//				}
//				if (Double.isNaN(d))
//					error("Illegal number '" + w + "' found in data.");
//				code[codeSize++] = (byte) constantCt;
//				realConstants[constantCt++] = d;
//			}
//		}
//
//	} // end class Expr
//////////////////////////ENDS+Expr_remove/////////////////////////////////
	
	
//////////////////////////FunctionEvaluator_remove/////////////////////////////////	
//	// https://math.hws.edu/javanotes/c8/ex4-ans.html
//	// uses_Expr_above
//	class FunctionEvaluator {
//
//		String line; // A line of input read from the user.
//		Expr expression; // The definition of the function f(z).
//
//		ComplexNumber z; // A value of z for which f(z) is to be calculated.
//		ComplexNumber val; // The value of f(z) for the specified value of x.
//
//		String type = "Complex"; // other is "Real"
//
//		/////////// for Real /////////////////
//		double xR; // A value of x for which f(x,y,z) is to be calculated.
//		double yR = 0.0; // in case its for lesser dimension
//		double zR = 0.0;
//
//		double valR; // The value of f(x,y,z) for the specified value of x,y,z.
//
//		public FunctionEvaluator() {
//		}
//		
//		public double evaluate(final String funcString, final Tuple3d t3d) {
//			return this.evaluate(funcString,t3d.x,t3d.y,t3d.z);
//		}
//			
//
//		public double evaluate(final String funcString, final double x, final double y, final double z) {
//			if (funcString.length() == 0) {
//				System.out.println("Err___line");
//				return 0.0;
//			}
//
//			try {
//				expression = new Expr(funcString, "Real");
//			} catch (IllegalArgumentException e) {
//				// An error was found in the input. Print an error message
//				expression.error("Error!  The definition of f(x) is not valid.");
//				System.out.println(e.getMessage());
//			}
//			
//			/*
//			 * If complexNumber is not a legal complex number, print an error
//			 * message. Otherwise, compute f(x) and return the result.
//			 */
//			if(x==(Double.NaN)||y==(Double.NaN)||z==(Double.NaN)){
//				System.out.println("Err___line2");
//				return Double.NaN;
//			}
//			try {
//				xR = x;
//				yR = y;
//				zR = z;
//			} catch (NumberFormatException e) {
//				e.printStackTrace();
//				System.out.println("\"" +  "\" is not a legal number.");
//			}
//			valR = expression.value(xR,yR,zR);
//			if (Double.isNaN(valR))
//				System.out.println("f("+x+","+y+"," + z + ") is undefined.");
//			else {
//				/* System.out.println("f(" + z + ") = " + val); */}
//			return valR;
//
//		}
//
//		public ComplexNumber evaluate(final String funcString, final ComplexNumber z0) {
//			// System.out.print("\nf(z) = " + funcString.trim());
//			if (funcString.length() == 0) {
//				System.out.println("Err___line");
//				return null;
//			}
//
//			try {
//				expression = new Expr(funcString);
//			} catch (IllegalArgumentException e) {
//				// An error was found in the input. Print an error message
//				expression.error("Error!  The definition of f(x) is not valid.");
//				System.out.println(e.getMessage());
//			}
//
//			/*
//			 * If complexNumber is not a legal complex number, print an error
//			 * message. Otherwise, compute f(x) and return the result.
//			 */
//			// System.out.print("\nz = ");
//			if (z0 == null) {
//				System.out.println("Err___line2");
//				return null;
//			}
//			try {
//				z = z0;
//			} catch (NumberFormatException e) {
//				System.out.println("\"" + z0 + "\" is not a legal complexnumber.");
//			}
//			val = expression.value(z);
//			if (Double.isNaN(val.real))
//				System.out.println("f(" + z + ") is undefined.");
//			else {
//				/* System.out.println("f(" + z + ") = " + val); */}
//			return val;
//		}
//	}
//	
////////////////////////////ENDSFunctionEvaluator_remove/////////////////////////////////	
	/*public FunctionEvaluator getFunctionEvaluator() {
		return new FunctionEvaluator();
	}*/
	
	interface Formula {

		
		// applies the formula
		public void apply(Formula f);
		
		// generates a formula
		// from evaluating the expression
		public Formula evaluate(String expression);
		
		// applies the formula and generates
		//va result
		public Object getResult(Formula f);

	}
	
	class FractalFormula implements Formula {
		
//		public final ComplexNumber one = new ComplexNumber(1.0, 0.0);
//		public final ComplexNumber two = new ComplexNumber(2.0, 0.0);
//		public final ComplexNumber three = new ComplexNumber(3.0, 0.0);
//		public final ComplexNumber four = new ComplexNumber(4.0, 0.0);
//		public final ComplexNumber five = new ComplexNumber(5.0, 0.0);
//		public final ComplexNumber six = new ComplexNumber(6.0, 0.0);
//		public final ComplexNumber seven = new ComplexNumber(7.0, 0.0);
//		public final ComplexNumber eight = new ComplexNumber(8.0, 0.0);
//		public final ComplexNumber nine = new ComplexNumber(9.0, 0.0);

		/*	Field lines for an iteration of the form 
			((1-z^3)/6)/(z-z^2/2)^2)+c}
			
			z = (((one.minus(z.power(3))).divides(six)).divides(((z.minus(z.power(2))).divides(two)).power(2))).plus(complexConstant);
		*/
		public FractalFormula() {
		}


		@Override
		public void apply(Formula f) {
			
		}

		@Override
		public Formula evaluate(String expression) {
			return null;
		}

		@Override
		public Object getResult(Formula f) {
			return null;
		}
	}

	public void setPxConstOperation(String pxConstOp) {
		this.pxConstOperation = pxConstOp;
	}

	public String getPxConstOperation() {
		return this.pxConstOperation;
	}
	


    private BufferedImage addFractalInfo2Image(BufferedImage image, String info) {
        int w = image.getWidth() / 3;
        int h = image.getHeight() / 3;
        BufferedImage img = new BufferedImage(
            w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(image, 0, 0, w, h, this);
        g2d.setPaint(Color.red);
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        FontMetrics fm = g2d.getFontMetrics();
        int x = img.getWidth() - fm.stringWidth(info) - 5;
        int y = fm.getHeight();
        g2d.drawString(info, x, y);
        g2d.dispose();
        return img;
    }
	
	private BufferedImage createStringImage(final String info) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 12);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(info);
        int height = fm.getHeight();
        /*int width = this.getFractalImage().getWidth();
        int height = 300;*/
        g2d.dispose();

		JLabel textLabel = new JLabel(info);
		Dimension d = textLabel.getPreferredSize();
		textLabel.setSize(d);
		
		img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);

		g2d = img.createGraphics();
        g2d.setColor(new Color(255, 255, 255, 128));

		g2d.setColor(Color.black);
		textLabel.paint(g2d);
        
        g2d.dispose();
        
		return img;
	}
	
	
	protected String fractalDtlInfo = "";
	public String getFractalDtlInfo() {
		return this.fractalDtlInfo;
		
	}
	
	public String getMinFractalDtlInfo() {
		String info = "";
		return info;
	}

	protected void createFractalDtlInfoObject() {
		return;

	}
	
	
	protected void setFractalDtlInfo() {
//		createFractalDtlInfoObject();
		
		String info = "<html><font color='black'>";
		final String eol = "<br/>";
		info += "Class: " + this.getClass().getName() + eol;
		info += "</font></html>";
		/*info += "Power (Exponent): " + this.power + eol;
		info += "[XC,YC]:	[" + this.getxC() +", "+ this.getyC() +"]" + eol;
		info += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.x_min + ", "+ this.y_min + "] to [" + this.x_max + ", " + this.y_max +"]" + eol;
		info += "Constant: " + */
		this.fractalDtlInfo = info;
	}
	

	public final /* abstract */ String getFractalDetails() {
		String info = "";
		// via reflection
		Object obj = this;
		@SuppressWarnings("unchecked")
		Class<? extends FractalBase> theClazz = (Class<? extends FractalBase>) obj.getClass();
		
		String className = theClazz.getName();
		info += className + "<br/>";
		
		Field[] fldz = theClazz.getDeclaredFields();
		for (Field aFld : fldz) {
			String fName = aFld.getName();
			String fVal = null;
			int m =aFld.getModifiers();
			try {
				if (Modifier.isPublic(m) && !(Modifier.isStatic(m) || Modifier.isFinal(m))) {
					// fVal = String.valueOf(aFld.get(fName))/*.toString()*/;
//					info += fName + " :		" + fVal + "<br/>";
					if (Modifier.isPrivate(m)) {
						aFld.setAccessible(true);
						// fVal = String.valueOf(aFld.get(fName))/*.toString()*/;
					}
					info += fName + " :		" + aFld.get(fName) + "<br/>";
				}
				
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				System.out.println("Exception in getting value for =>> " + fName);
				e.printStackTrace();
			}
		}

		return info;

	}

	public boolean isApplyCustomFormula() {
		return this.applyCustomFormula;
	}

	public void setApplyCustomFormula(boolean apply) {
		this.applyCustomFormula = apply;
	}
	
	/**
     * Pause for t milliseconds. This method is intended to support computer animations.
     * @param t number of milliseconds
     */
    public static void pause(int t) {
        try {
            Thread.sleep(t);
        }
        catch (InterruptedException e) {
            System.out.println("Error sleeping");
        }
    }
    
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!this.isDragging)
			return;
		/*this.x_click_end = e.getX();
		this.y_click_end = e.getY();

		if (this.x_click_start == this.x_click_end || this.y_click_start == this.y_click_end) {
			this.isDragging = false;
			return;
		}
		
		Graphics g = this.bufferedImage.getGraphics();
		g.setColor(Color.white);
		g.drawRect((int) this.x_click_start, (int) this.y_click_start,
				(int) Math.abs(this.x_click_end - this.x_click_start),
				(int) Math.abs(this.y_click_end - this.y_click_start));
*/
		

		int x = e.getX();
		int y = e.getY();

		x_click_end = x;
		y_click_end = y;
		
//		System.out.println("drawRect -- ("+x_click_start+","+y_click_start+") - to - ("+x_click_end+","+y_click_end+")");
//		System.out.println("[-mouseDragged-] x is " + x + " and y is " + y);
//		drawDraggedWhiteSquare();	
		drawDraggedWhiteRectangle();
	}
	
	private void drawDraggedWhiteRectangle() {
		/*Image im = this.createImage(WIDTH, HEIGHT);*/
		Graphics g = this.getGraphics();
		g.drawImage(bufferedImage, 0, 0, (ImageObserver)this);
		g.setColor(Color.white);
		
		int xRectStart;
		int yRectStart;
		
		if (x_click_start < x_click_end) {
			xRectStart = (int) x_click_start;
			if (y_click_start < y_click_end) {
				yRectStart = (int) y_click_start;
			} else {
				yRectStart = (int) y_click_end;
			}
		} else {
			xRectStart = (int) x_click_end;
			if (y_click_start < y_click_end) {
				yRectStart = (int) y_click_start;
			} else {
				yRectStart = (int) y_click_end;
			}
		}
		
		
		g.drawRect(xRectStart, yRectStart, 
				(int) Math.abs(x_click_end - x_click_start),
				(int) Math.abs(y_click_end - y_click_start));
	}


	private void drawDraggedWhiteSquare() {
		/*Image im = this.createImage(WIDTH, HEIGHT);*/
		Graphics g = this.getGraphics();
		g.drawImage(bufferedImage, 0, 0, (ImageObserver)this);
		g.setColor(Color.white);
		
		int squareEdge = Math.abs(x_click_end - x_click_start) < Math.abs(y_click_end - y_click_start)
				? (int) Math.abs(x_click_end - x_click_start) : (int) Math.abs(y_click_end - y_click_start);


		int xSqrStart;
		int ySqrStart;

		if (x_click_start < x_click_end) {
			xSqrStart = (int) x_click_start;
			if (y_click_start < y_click_end) {
				ySqrStart = (int) y_click_start;
			} else {
				ySqrStart = (int) y_click_end;
			}
		} else {
			xSqrStart = (int) x_click_end;
			if (y_click_start < y_click_end) {
				ySqrStart = (int) y_click_start;
			} else {
				ySqrStart = (int) y_click_end;
			}
		}
		
		g.drawRect(xSqrStart,ySqrStart,squareEdge,squareEdge);
		
//		g.drawRect((int) x_click_start, (int) y_click_start, squareEdge, squareEdge);
//				/*(int) Math.abs(x_click_end - x_click_start),
//				(int) Math.abs(y_click_end - y_click_start));*/
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (this.isDragging)
			return;
		jpopFr.dispose();
		//Point p = MouseInfo.getPointerInfo().getLocation();
		int x = e.getX();// p.x;	p.x & p.y refer to screen-position
		int y = e.getY();// p.y;	coordinates
		
		x_click_start = e.getX();
		y_click_start = e.getY();
		
		/*Drawing on any GUI component is performed with coordinates 
		 * that are measured from the upper-left corner (0, 0) of that GUI component, 
		 * not the upper-left corner of the screen*/
		
		//screen-coordinates
		//x_min,y_max										x_max,y_max
		//0,0	--------------width/2,0-------------------	width,0
		//|		Rl<0[x<width/2]		|	Rl>0[x>width/2]		|
		//|		Im>0[y<height/2]	|	Im>0[y<height/2]	|
		//0,height/2-------------width/2,height/2----------	width,height/2
		//|		Rl<0[x<width/2]		|	Rl>0[x>width/2]		|
		//|		Im<0[y>height/2]	|	Im<0[y>height/2]	|
		//0,height------width/2,height-------------			width,height
		//x_min,y_min										x_max,y_min
		
		//System.out.println("[-mouseMoved-] x is " + x + " and y is " + y);
				
//		locPtTextField.setText("[-mouseMoved-] x is " + x + " and y is " + y);
		double realPosn = x_min + (x * 1.0 * (x_max - x_min)) / getWidth();		
		double imgPosn = y_max + (y * 1.0 * (y_min - y_max)) / getHeight();
		//String imgSign = imgPosn > 0 ? "+" : "";//"";//y > scrnCntrY ? "-" : "+";//
		//locPtTextField.setText(realPosn + " " + imgSign + " " + imgPosn + " * i");
		
		if (this.getClass().getName().contains("Mandelbrot")) {
			createMandelbrotExplorerJuliaSet(realPosn, imgPosn);
			jpopFr.setVisible(true);
		}
	}
	


	private void createMandelbrotExplorerJuliaSet(double realPosn, double imgPosn) {
		final Mandelbrot mand = (Mandelbrot) this;

		Julia jpop = new Julia(mand.power, new ComplexNumber(realPosn, imgPosn));
		jpop.setTitle(jpop.getFractalShapeTitle() + " R: (" + realPosn + "), I: (" + imgPosn + ")");
		jpop.setColorChoice(mand.colorChoice);
		jpop.setxC(0.0);
		jpop.setyC(0.0);
		jpop.setMaxIter(1000);
		jpop.setSize(400, 400);
		jpop.setScaleSize(FractalBase.scaleSize);
		jpop.setUseDiff(mand.isUseDiff());
		///// above are hardCoded values //////
		jpop.setRotation(mand.rotation);
		jpop.setReversePixelCalculation(mand.reversePixelCalculation);
		jpop.setPxConstOperation(mand.pxConstOperation);
		jpop.setUseFuncConst(mand.useFuncConst);
		jpop.setPxXTransformation(mand.pxXTransformation);
		jpop.setPxYTransformation(mand.pxYTransformation);
		jpop.setPixXYOperation(mand.pixXYOperation);
		jpop.setUseFuncPixel(mand.useFuncPixel);
		jpop.setApplyCustomFormula(mand.applyCustomFormula);
		jpop.setBound(mand.bound);
		jpop.setRefocusDraw(false);
		
		jpop.setCreatedViaMandelbrotExplorer(true);
		
		jpop.setResizable(false);
//		BufferedImage bImage = jpop.getBufferedImage();
//		Graphics g = bImage.createGraphics();
//		g.setColor(Color.white);
//		
//		String juliaInfo="Julia R: (" + realPosn + "), I: (" + imgPosn + ")";
//		g.drawString(juliaInfo,10,380);
//		
//		g.dispose();
//		
//		jpop.add2FbImages(bImage);
		jpopFr = jpop;
	}
	
	private static JFrame jpopFr = new JFrame();

	@Override
	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		/*System.out.println("[-mouseClicked-] x is " + x + " and y is " + y);*/
		double realPosn = x_min + (x * 1.0 * (x_max - x_min)) / getWidth();		
		double imgPosn = y_max + (y * 1.0 * (y_min - y_max)) / getHeight();
		String imgSign = imgPosn > 0 ? "+" : "";
		locPtTextField.setText(realPosn + " " + imgSign + " " + imgPosn + " * i");
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.isDragging = true;
		x_click_start = e.getX();
		y_click_start = e.getY();
		
		/*int x = e.getX();
		int y = e.getY();
		System.out.println("[-mousePressed-] x is " + x + " and y is " + y);*/
	}

	
	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		if (!this.isDragging) {
			if (this.getClass().getName().contains("Mandelbrot")) {
				jpopFr.dispose();
				x_click_start = e.getX();
				y_click_start = e.getY();
				double realPosn = x_min + (x * 1.0 * (x_max - x_min)) / getWidth();
				double imgPosn = y_max + (y * 1.0 * (y_min - y_max)) / getHeight();
				createMandelbrotExplorerJuliaSet(realPosn, imgPosn);
				jpopFr.setVisible(true);
			}
			return;
		}
		
		x_click_end = x;
		y_click_end = y;
		
		double realPosnStart = x_min + (x_click_start * 1.0 * (x_max - x_min)) / getWidth();		
		double imgPosnStart = y_max + (y_click_start * 1.0 * (y_min - y_max)) / getHeight();
		double realPosnEnd = x_min + (x_click_end * 1.0 * (x_max - x_min)) / getWidth();		
		double imgPosnEnd = y_max + (y_click_end * 1.0 * (y_min - y_max)) / getHeight();
		
		if (imgPosnEnd == imgPosnStart || realPosnEnd == realPosnStart) {
			this.isDragging = false;
			return;
		}
		
		refocusDraw=true;
//		System.out.println("new rect focus range ["+realPosnStart+","+imgPosnStart+"] == to == ["+realPosnEnd+","+imgPosnEnd+"]");
        createExplorerFocussedFractalImg(realPosnStart,imgPosnStart,realPosnEnd,imgPosnEnd);
		this.isDragging=false;
		
		/*if (!this.isDragging)
			return;
		this.x_click_end = e.getX();
		this.y_click_end = e.getY();

		if (this.x_click_start == this.x_click_end || this.y_click_start == this.y_click_end) {
			this.isDragging = false;
			return;
		}
		
		Graphics g = this.bufferedImage.getGraphics();
		g.setColor(Color.white);
		g.drawRect((int) this.x_click_start, (int) this.y_click_start,
				(int) Math.abs(this.x_click_end - this.x_click_start),
				(int) Math.abs(this.y_click_end - this.y_click_start));

        this.isDragging = false;
        
        //range

        double realPosnStart = x_min + (x_click_start * 1.0 * (x_max - x_min)) / getWidth();		
		double imgPosnStart = y_max + (y_click_start * 1.0 * (y_min - y_max)) / getHeight();
		double realPosnEnd = x_min + (x_click_end * 1.0 * (x_max - x_min)) / getWidth();		
		double imgPosnEnd = y_max + (y_click_end * 1.0 * (y_min - y_max)) / getHeight();
		
		System.out.println("new rect focus range ["+realPosnStart+","+imgPosnStart+"] == to == ["+realPosnEnd+","+imgPosnEnd+"]");
        
        */
        
		/*int x = e.getX();
		int y = e.getY();
		System.out.println("[-mouseReleased-] x is " + x + " and y is " + y);*/
		//System.out.println("releasedBOUNDEDRect is ("+x_click_start+","+y_click_start+")  ==to== ["+x_click_end+","+y_click_end+"]");
//not-getting-range-correctly	^__^
/*******************************		
		int x_from = (int) ((x_click_start < x_click_end) ? x_click_start : x_click_end);
		int x_to = (int) ((x_click_start < x_click_end) ? x_click_end : x_click_start);
		int y_from = (int) ((y_click_start < y_click_end) ? y_click_start : y_click_end);
		int y_to = (int) ((y_click_start < y_click_end) ? y_click_end : y_click_start);
		
		System.out.println("releasedBOUNDEDRect is ("+x_from+","+y_from+")  ==to== ["+x_to+","+y_to+"]");
		
		double realFromPosn = x_min + (x_from * 1.0 * (x_max - x_min)) / getWidth();		
		double imgFromPosn = y_max + (y_from * 1.0 * (y_min - y_max)) / getHeight();
		double realToPosn = x_min + (x_to * 1.0 * (x_max - x_min)) / getWidth();		
		double imgToPosn = y_max + (y_to * 1.0 * (y_min - y_max)) / getHeight();
		
		System.out.println("releasedComplexRect is ("+realFromPosn+","+imgFromPosn+")  ==to== ["+realToPosn+","+imgToPosn+"]");

		this.setxC((realFromPosn + realToPosn) / 2.0);
		this.setyC((imgFromPosn + imgToPosn) / 2.0);
		
		double pxlXJump = (realFromPosn-realToPosn)/WIDTH;
		double pxlYJump = (imgFromPosn-imgToPosn)/HEIGHT;
		
		System.out.println("xC is "+ getxC()+", yC is "+getyC()+", XJump is "+ pxlXJump+", YJump is "+pxlYJump);
		
		
//		err--output--blow		dragged-left-and-down-diagonally
//		leftDrag	x-decreases,	downDrag	y-increases
// 
//	drawRect -- (397.0,152.0) - to - (348.0,191.0)
//	releasedBOUNDEDRect is (348,152)  ==to== [397,191]
//	releasedComplexRect is (-0.2160000000000002,0.784)  ==to== [0.17600000000000016,0.472]
//	xC is -0.020000000000000018, yC is 0.628, XJump is -4.900000000000004E-4, YJump is 3.9000000000000005E-4

************/
	}

	private void createExplorerFocussedFractalImg(double xStart, double yStart, double xEnd, double yEnd) {
		x_start = xStart;
		x_end = xEnd;
		y_start = yStart;
		y_end = yEnd;

		xminold = x_min;
		xmaxold = x_max;
		yminold = y_min;
		ymaxold = y_max;

		x_min = x_start < x_end ? x_start : x_end;
		x_max = x_start > x_end ? x_start : x_end;
		y_min = y_start < y_end ? y_start : y_end;
		y_max = y_start > y_end ? y_start : y_end;
		
		refocusDraw = true;
		repaint();
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		/*Point p = MouseInfo.getPointerInfo().getLocation();
		int x = p.x;
		int y = p.y;
		System.out.println("[-mouseEntered-] x is " + x + " and y is " + y);*/
	}

    @Override
    public void mouseExited(MouseEvent e){
		/*Point p = MouseInfo.getPointerInfo().getLocation();
		int x = p.x;
		int y = p.y;
		System.out.println("[-mouseExited-] x is " + x + " and y is " + y);*/
	}
    
    
    protected void setRangeMinMaxVals(double xMinVal, double yMinVal, double xMaxVal, double yMaxVal) {
    	x_start = xMinVal;
    	y_start = yMinVal;
    	x_end = xMaxVal;
    	y_end = yMaxVal;
    	
    	x_min = xMinVal;
    	y_min = yMinVal;
    	x_max = xMaxVal;
    	y_max = yMaxVal;
    }


	protected void setRangeSpace(double xc, double yc, double size, int n) {
		x_min = xc - size / 2;
		y_min = yc - size / 2;
		x_max = xc + size / 2;//xc - size / 2 + size * (n - 1) / n;
		y_max = yc + size / 2;//yc - size / 2 + size * (n - 1) / n;

		/*System.out.println("x_min is: " + x_min);
		System.out.println("y_min is: " + y_min);
		System.out.println("x_max is: " + x_max);
		System.out.println("y_max is: " + y_max);*/

		xminold = x_min;
		xmaxold = x_max;
		yminold = y_min;		
		ymaxold = y_max;
		
		xminorig = x_min;
		xmaxorig = x_max;
		yminorig = y_min;
		ymaxorig = y_max;
		
		
		
	}
	
	public boolean isUseJuliaExplorer() {
		return this.useJuliaExplorer;
	}
	
	

	public void setUseJuliaExplorer(boolean useJExplorer) {
		this.useJuliaExplorer = useJExplorer;
		
		if (useJExplorer) {
			this.add(locPtTextField, java.awt.BorderLayout.SOUTH);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}
	}
	
	public boolean isUseMandelbrotExplorer() {
		return this.useMandelbrotExplorer;
	}


	public void setUseMandelbrotExplorer(boolean useMExplorer) {
		this.useMandelbrotExplorer = useMExplorer;
		
		if (useMExplorer) {
			this.add(locPtTextField, java.awt.BorderLayout.SOUTH);
			this.addMouseListener(this);
			this.addMouseMotionListener(this);
		}
	}

	public  double x_min;
	public  double y_min;
	public  double x_max;
	public  double y_max;
	
	public  double xminold;
	public  double xmaxold;
	public  double yminold;
	public  double ymaxold;
	
	public  double xminorig;
	public  double xmaxorig;
	public  double yminorig;
	public  double ymaxorig;
	
	//captures mouse event
	public  double x_start;
	public  double x_end;
	public  double y_start;
	public  double y_end;

	public  double x_click_start;
	public  double x_click_end;
	public  double y_click_start;
	public  double y_click_end;

	boolean isDragging = false;
	boolean refocusDraw = false;
	boolean createdViaMandelbrotExplorer = false;


    public boolean isRefocusDraw() {
		return this.refocusDraw;
	}


	public void setRefocusDraw(boolean reDraw) {
		this.refocusDraw = reDraw;
	}
	public void setCreatedViaMandelbrotExplorer(boolean isViaMandelbrot) {
		this.createdViaMandelbrotExplorer = isViaMandelbrot;
	}
	
	


	public String getColorBlowoutType() {
		return this.colorBlowoutType;
	}


	public void setColorBlowoutType(String clrBlowoutType) {
		this.colorBlowoutType = clrBlowoutType;
	}
	
	/**
	 * for orbit-trap calculation
	 * @param c
	 * @return
	 */
	double iterate(ComplexNumber c, ComplexNumber zPoint) {
		double dist = 1e20f;
		ComplexNumber z = new ComplexNumber(0.0f, 0.0f);
		for (int i = 0; i < maxIter; i++) {
			z = z.times(z).plus(c);
			if (z.abs() > bound * 1.0f) {
				return 0.0f;
			}
			dist = Math.min(dist, (Math.pow(zPoint.real, 2.0) + Math.pow(zPoint.imaginary, 2.0)));
		}
		return Math.sqrt(dist) * 1.0f;
	}


	int blowout_num(double d, double d2) {
        double d3 = d;
        double d4 = d2;
        int n = 0;
        for (int i = 0; i < FractalBase.maxIter; ++i) {
            double d5 = d3 * d3 - d4 * d4 + d;
            d4 = 2.0 * d3 * d4 + d2;
            d3 = d5;
            ++n;
            if ((int)d3 > this.bound) break;
            if ((int)d3 < -1*this.bound) break;
        }
        if (n != FractalBase.maxIter) return n;
        return -1;
    }
	
	private Color getColorSuperBlowout(int n) {
		String colorType = this.getColorBlowoutType();
		Color colr = null;
		int r = 0, g = 0, b = 0;
		switch (colorType) {
		case 	"Original":
			if (n < 32) {
				r = n * 8;
				g = n * 8;
				b = 127 - n * 4;
			} else if (n < 128) {
				r = 255;
				g = 255 - (n - 32) * 8 / 3;
				b = (n - 32) * 4 / 3;
			} else if (n < 192) {
				r = 255 - (n - 128) * 4;
				g = 0 + (n - 128) * 3;
				b = 127 - (n - 128);
			} else {
				r = 0;
				g = 192 - (n - 192) * 3;
				b = 64 + (n - 192);
			}
			break;
			
		case	"Fire":
			if (n < 64) {
				r = n * 4;
				g = 0;
				b = 0;
			} else if (n < 128) {
				r = 255;
				g = (n - 64) * 2;
				b = 0;
			} else if (n < 192) {
				r = 255;
				g = 128 - ((n - 128) * 2);
				b = 0;
			} else {
				r = 255 - (n - 192) * 4;
				g = 0;
				b = 0;
			}
			break;
		
		case	"Black & White":
			if (n < 128) {
				r = 255 - n * 2;
				g = 255 - n * 2;
				b = 255 - n * 2;
			} else {
				r = (n - 128) * 2;
				g = (n - 128) * 2;
				b = (n - 128) * 2;
			}
			break;
			
		case	"Electric Blue":
			if (n < 32) {
				r = 0;
				g = 0;
				b = n * 4;
			} else if (n < 64) {
				r = (n - 32) * 8;
				g = (n - 32) * 8;
				b = 127 + (n - 32) * 4;
			} else if (n < 96) {
				r = 255 - (n - 64) * 8;
				g = 255 - (n - 64) * 8;
				b = 255 - (n - 64) * 4;
			} else if (n < 128) {
				r = 0;
				g = 0;
				b = 127 - (n - 96) * 4;
			} else if (n < 192) {
				r = 0;
				g = 0;
				b = (n - 128);
			} else {
				r = 0;
				g = 0;
				b = 63 - (n - 192);
			}
			break;
			
		case	"Toon":
			if (n % 4 == 0) {
				r = 100;
				g = 20;
				b = 200;
			} else if (n % 4 == 1) {
				r = 220;
				g = 112;
				b = 0;
			} else if (n % 4 == 2) {
				r = 230;
				g = 120;
				b = 0;
			} else {
				r = 255;
				g = 128;
				b = 0;
			}
			break;
			
		case	"Gold":
			if (n < 32) {
				r = (int) (54 + Math.floor((n) * (224 - 54) / 32));
				g = (int) (11 + Math.floor((n) * (115 - 11) / 32));
				b = (int) (2 + Math.floor((n) * (10 - 2) / 32));
			} else if (n < 64) {
				r = (int) (224 + Math.floor((n - 32) * (255 - 224) / 32));
				g = (int) (115 + Math.floor((n - 32) * (192 - 115) / 32));
				b = (int) (10 + Math.floor((n - 32) * (49 - 10) / 32));
			} else if (n < 192) {
				r = 255;
				g = (int) (192 + Math.floor((n - 64) * (255 - 192) / 128));
				b = (int) (49 + Math.floor((n - 64) * (166 - 49) / 128));
			} else if (n < 224) {
				r = 255;
				g = (int) (255 + Math.floor((n - 192) * (192 - 255) / 32));
				b = (int) (166 + Math.floor((n - 192) * (49 - 166) / 32));
			} else {
				r = (int) (255 + Math.floor((n - 224) * (54 - 255) / 32));
				g = (int) (192 + Math.floor((n - 224) * (11 - 192) / 32));
				b = (int) (49 + Math.floor((n - 224) * (2 - 49) / 32));
			}
			break;
			
		case	"Classic VGA":
			return new Color(vga[n%COLORMAXRGB]);
			
		case	"CGA 1":
			if (n % 4 == 0)
				return new Color(0);
			else if (n % 4 == 1)
				return new Color(1442840320);
			else if (n % 4 == 2)
				return new Color(Integer.parseUnsignedInt("4283825920"));
			else if (n % 4 == 3)
				return new Color(Integer.parseUnsignedInt("4294967040"));
			
		case	"CGA 2":
			if (n % 4 == 0)
				return new Color(0);
			else if (n % 4 == 1)
				return new Color(1442796800);
			else if (n % 4 == 2)
				return new Color(Integer.parseUnsignedInt("4283782400"));
			else if (n % 4 == 3)
				return new Color(Integer.parseUnsignedInt("4294923520"));
			
		case	"Primary (RGB)":
			if (n < 85) {
				r = 255 - n * 3;
				g = 0 + n * 3;
				b = 0;
			} else if (n < 170) {
				r = 0;
				g = 255 - (n - 85) * 3;
				b = 0 + (n - 85) * 3;
			} else {
				r = 0 + (n - 170) * 3;
				g = 0;
				b = 255 - (n - 170) * 3;
			}
			break;
			
		case	"Secondary (CMY)":
			if (n < 85) {
				r = 0 + n * 3;
				g = 255 - n * 3;
				b = 255;
			} else if (n < 170) {
				r = 255;
				g = 0 + (n - 85) * 3;
				b = 255 - (n - 85) * 3;
			} else {
				r = 255 - (n - 170) * 3;
				g = 255;
				b = 0 + (n - 170) * 3;
			}
			break;
			
		case	"Tertiary 1":
			if (n < 85) {
				r = 255 - n * 3 / 2;
				g = 127 - n * 3 / 2;
				b = 0 + n * 3;
			} else if (n < 170) {
				r = 127 - (n - 85) * 3 / 2;
				g = 0 + (n - 85) * 3;
				b = 255 - (n - 85) * 3 / 2;
			} else {
				r = 0 + (n - 170) * 3;
				g = 255 - (n - 170) * 3 / 2;
				b = 127 - (n - 170) * 3 / 2;
			}
			break;
		case	"Tertiary 2":
			if (n < 85) {
				r = 255 - n * 3;
				g = 0 + n * 3 / 2;
				b = 127 + n * 3 / 2;
			} else if (n < 170) {
				r = 0 + (n - 85) * 3 / 2;
				g = 127 + (n - 85) * 3 / 2;
				b = 255 - (n - 85) * 3;
			} else {
				r = 127 + (n - 170) * 3 / 2;
				g = 255 - (n - 170) * 3;
				b = 0 + (n - 170) * 3 / 2;
			}
			break;
			
		case	"Neon":
			if (n < 32) {
				r = n * 4;
				g = 0;
				b = n * 8;
			} else if (n < 64) {
				r = 124 - (n - 32) * 4;
				g = 0;
				b = 248 - (n - 32) * 8;
			} else if (n < 96) {
				r = (n - 64) * 8;
				g = (n - 64) * 4;
				b = 0;
			} else if (n < 128) {
				r = 248 - (n - 96) * 8;
				g = 124 - (n - 96) * 4;
				b = 0;
			} else if (n < 160) {
				r = 0;
				g = (n - 128) * 4;
				b = (n - 128) * 8;
			} else if (n < 192) {
				r = 0;
				g = 124 - (n - 160) * 4;
				b = 248 - (n - 160) * 8;
			} else if (n < 224) {
				r = (n - 192) * 4;
				g = (n - 192) * 8;
				b = (n - 192) * 4;
			} else {
				r = 124 - (n - 224) * 4;
				g = 248 - (n - 224) * 8;
				b = 124 - (n - 224) * 4;
			}
			break;
			
			

		default:		//"Original"
			if (n < 32) {
				r = n * 8;
				g = n * 8;
				b = 127 - n * 4;
			} else if (n < 128) {
				r = 255;
				g = 255 - (n - 32) * 8 / 3;
				b = (n - 32) * 4 / 3;
			} else if (n < 192) {
				r = 255 - (n - 128) * 4;
				g = 0 + (n - 128) * 3;
				b = 127 - (n - 128);
			} else {
				r = 0;
				g = 192 - (n - 192) * 3;
				b = 64 + (n - 192);
			}
			break;
			

		}	//ends Swicth


		r = (r << 24);
		g = (g << 16);
		b = (b << 8);
		colr = new Color(r + g + b);
	
		
		return colr;
		
	}
    
    public int time2Color(int n) {
    	//Default
        int clr1 = -16776961;
        int clr2 = -256;
        int clr3 = -65536;
        
        String colorType = this.getColorBlowoutType();
        
        if (colorType.equals("Default")) {
			clr1 = -16776961;
			clr2 = -256;
			clr3 = -65536;
		} else if (colorType.equals("Snowy")) {
			clr1 = -16776961;
            clr2 = -1;
            clr3 = -5592321;
		} else if (colorType.equals("Pink")) {
			clr1 = -65536;
            clr2 = -1;
            clr3 = -21846;
		} else if (colorType.equals("Matrix")) {
			clr1 = -16711936;
            clr2 = -16755456;
            clr3 = -16777216;
		}
        
		int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        
//        Oooooo
//        int n11 = 10;
//        int n12 = 60;
//        int n13 = 110;
//        int n14 = 160;

        boolean isJulia = this.getClass().getName().contains("Julia");
        int n11 = isJulia ? 5 : 10;
        int n12 = isJulia ? 30 :  60;
        int n13 = isJulia ? 55 : 110;
        int n14 = isJulia ? 80 : 160;
        double d = 1.0;
        
        n11 = (int)(d * (double)n11);
        n12 = (int)(d * (double)n12);
        n13 = (int)(d * (double)n13);
        n14 = (int)(d * (double)n14);
        int n15 = n;
        int n16 = -1;
        
        if (n < n11) {
            n15 = n;
            int n17 = (clr1 & 16711680) >> 16;
            int n18 = (clr1 & 65280) >> 8;
            int n19 = clr1 & 255;
            n17 = n17 * n15 / n11;
            n18 = n18 * n15 / n11;
            n19 = n19 * n15 / n11;
            n16 = -16777216 | n17 << 16 | n18 << 8 | n19;
            return n16;
        }
        if ((n15 = (n - n11) % (n14 - n11) + n11) < n12) {
            n8 = (clr1 & 16711680) >> 16;
            n7 = (clr1 & 65280) >> 8;
            n9 = clr1 & 255;
            n8 = n8 * (n12 - n15) / (n12 - n11);
            n7 = n7 * (n12 - n15) / (n12 - n11);
            n9 = n9 * (n12 - n15) / (n12 - n11);
        } else if (n15 < n13) {
            n8 = 0;
            n7 = 0;
            n9 = 0;
        } else {
            n8 = (clr1 & 16711680) >> 16;
            n7 = (clr1 & 65280) >> 8;
            n9 = clr1 & 255;
            n8 = n8 * (n15 - n13) / (n14 - n13);
            n7 = n7 * (n15 - n13) / (n14 - n13);
            n9 = n9 * (n15 - n13) / (n14 - n13);
        }
        if (n15 < n12) {
            n2 = (clr2 & 16711680) >> 16;
            n10 = (clr2 & 65280) >> 8;
            n6 = clr2 & 255;
            n2 = n2 * (n15 - n11) / (n12 - n11);
            n10 = n10 * (n15 - n11) / (n12 - n11);
            n6 = n6 * (n15 - n11) / (n12 - n11);
        } else if (n15 < n13) {
            n2 = (clr2 & 16711680) >> 16;
            n10 = (clr2 & 65280) >> 8;
            n6 = clr2 & 255;
            n2 = n2 * (n13 - n15) / (n13 - n12);
            n10 = n10 * (n13 - n15) / (n13 - n12);
            n6 = n6 * (n13 - n15) / (n13 - n12);
        } else {
            n2 = 0;
            n10 = 0;
            n6 = 0;
        }
        if (n15 < n12) {
            n5 = 0;
            n3 = 0;
            n4 = 0;
        } else if (n15 < n13) {
            n5 = (clr3 & 16711680) >> 16;
            n3 = (clr3 & 65280) >> 8;
            n4 = clr3 & 255;
            n5 = n5 * (n15 - n12) / (n13 - n12);
            n3 = n3 * (n15 - n12) / (n13 - n12);
            n4 = n4 * (n15 - n12) / (n13 - n12);
        } else {
            n5 = (clr3 & 16711680) >> 16;
            n3 = (clr3 & 65280) >> 8;
            n4 = clr3 & 255;
            n5 = n5 * (n14 - n15) / (n14 - n13);
            n3 = n3 * (n14 - n15) / (n14 - n13);
            n4 = n4 * (n14 - n15) / (n14 - n13);
        }
        n16 = -16777216 | this.sum3colors(n8, n2, n5) << 16 | this.sum3colors(n7, n10, n3) << 8 | this.sum3colors(n9, n6, n4);
        return n16;
    }
	
	
	/*
	public int time2Color(int n) {
        final int col1 = -16776961;
        final int col2 = -256;
        final int col3 = -65536;
        
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        
        int n11 = 10;
        int n12 = 60;
        int n13 = 110;
        int n14 = 160;
        double d = 1.0;
        
        n11 = (int)(d * (double)n11);
        n12 = (int)(d * (double)n12);
        n13 = (int)(d * (double)n13);
        n14 = (int)(d * (double)n14);
        int n15 = n;
        int n16 = -1;
        
        if (n < n11) {
            n15 = n;
            int n17 = (col1 & 16711680) >> 16;
            int n18 = (col1 & 65280) >> 8;
            int n19 = col1 & 255;
            n17 = n17 * n15 / n11;
            n18 = n18 * n15 / n11;
            n19 = n19 * n15 / n11;
            n16 = -16777216 | n17 << 16 | n18 << 8 | n19;
            return n16;
        }
        if ((n15 = (n - n11) % (n14 - n11) + n11) < n12) {
            n8 = (col1 & 16711680) >> 16;
            n7 = (col1 & 65280) >> 8;
            n9 = col1 & 255;
            n8 = n8 * (n12 - n15) / (n12 - n11);
            n7 = n7 * (n12 - n15) / (n12 - n11);
            n9 = n9 * (n12 - n15) / (n12 - n11);
        } else if (n15 < n13) {
            n8 = 0;
            n7 = 0;
            n9 = 0;
        } else {
            n8 = (col1 & 16711680) >> 16;
            n7 = (col1 & 65280) >> 8;
            n9 = col1 & 255;
            n8 = n8 * (n15 - n13) / (n14 - n13);
            n7 = n7 * (n15 - n13) / (n14 - n13);
            n9 = n9 * (n15 - n13) / (n14 - n13);
        }
        if (n15 < n12) {
            n2 = (col2 & 16711680) >> 16;
            n10 = (col2 & 65280) >> 8;
            n6 = col2 & 255;
            n2 = n2 * (n15 - n11) / (n12 - n11);
            n10 = n10 * (n15 - n11) / (n12 - n11);
            n6 = n6 * (n15 - n11) / (n12 - n11);
        } else if (n15 < n13) {
            n2 = (col2 & 16711680) >> 16;
            n10 = (col2 & 65280) >> 8;
            n6 = col2 & 255;
            n2 = n2 * (n13 - n15) / (n13 - n12);
            n10 = n10 * (n13 - n15) / (n13 - n12);
            n6 = n6 * (n13 - n15) / (n13 - n12);
        } else {
            n2 = 0;
            n10 = 0;
            n6 = 0;
        }
        if (n15 < n12) {
            n5 = 0;
            n3 = 0;
            n4 = 0;
        } else if (n15 < n13) {
            n5 = (col3 & 16711680) >> 16;
            n3 = (col3 & 65280) >> 8;
            n4 = col3 & 255;
            n5 = n5 * (n15 - n12) / (n13 - n12);
            n3 = n3 * (n15 - n12) / (n13 - n12);
            n4 = n4 * (n15 - n12) / (n13 - n12);
        } else {
            n5 = (col3 & 16711680) >> 16;
            n3 = (col3 & 65280) >> 8;
            n4 = col3 & 255;
            n5 = n5 * (n14 - n15) / (n14 - n13);
            n3 = n3 * (n14 - n15) / (n14 - n13);
            n4 = n4 * (n14 - n15) / (n14 - n13);
        }
        n16 = -16777216 | this.sum3colors(n8, n2, n5) << 16 | this.sum3colors(n7, n10, n3) << 8 | this.sum3colors(n9, n6, n4);
        return n16;
    }
	*/
    public int sum3colors(int n, int n2, int n3) {
        int n4 = n + n2 + n3;
        if (n4 > COLORMAXRGB) return COLORMAXRGB;
        return n4;
    }
    
    /**
     * 
     * @param n	--	n,	the	number
     * @param exponent	--	the	exponent,	power
     * @return	n	^	exponent	(for	exponent	>	0)
     */
	public int intpower(int n, int exponent) {
		int num = 1;
		for (int i = 0; i < exponent; ++i) {
			num *= n;
		}
		return num;
	}
	
	public void drawAxis(Graphics graphics) {
        int n = this.intpower(10, 6);	//	using 6-digit precision
        int n2 = this.intpower(10, 6);
        int n3 = (int)(x_min * (double)n);
        int n4 = (int)(x_max * (double)n);
        float f = (float)n3 / (float)n;
        float f2 = (float)n4 / (float)n;
        String string = "" + f;
        String string2 = "" + f2;
        graphics.setColor(Color.black);
        graphics.drawLine(0, getHeight() - 12, getWidth() - 1, getHeight() - 12);
        graphics.drawString(string, 20 + 8, getHeight() - 1);
        graphics.drawString("cr", 20 + (getWidth() - 20) / 2 - "cr".length() * 8 / 2, getHeight() - 1);
        graphics.drawString(string2, getWidth() - string2.length() * 8, getHeight() - 1);
        n3 = (int)(y_min * (double)n2);
        n4 = (int)(y_max * (double)n2);
        f = (float)n3 / (float)n2;
        f2 = (float)n4 / (float)n2;
        String string3 = "" + f;
        String string4 = "" + f2;
        graphics.drawLine(20 - 1, 0, 20 - 1, getHeight() - 1);
        graphics.drawString(string4, 0, 12);
        graphics.drawString("ci", 10, (int)((double)getHeight() / 2.0));
        graphics.drawString(string3, 0, getHeight() - 12);
    }
	
	protected synchronized void add2FbImages(BufferedImage bImage) {
		if (!this.createdViaMandelbrotExplorer && !this.isFbCreated) {
			if (!rawFbImages.add(bImage)) {
				System.out.println("Couldnt_add_raw_image___" + bImage);
			}
			this.createFractalDtlInfoObject();
			fbCurrCount += 1;
//			System.out.println("In add2FbImages fbCurrCount = " + fbCurrCount + " and size is " + FractalBase.rawFbImages.size());
		}
		//		
		this.setFractalDtlInfo();
		
		BufferedImage stringImage = this.createStringImage(this.getFractalDtlInfo());
//		/*if (!fbImages.contains(bImage)) {*/
		if(!fbImages.add(joinBufferedImages(bImage,stringImage)))
			System.out.println("Couldnt_add_Joinedimage___"+bImage);
				
//		/*}*/
			
		//BufferedImage fbInfoImage = 	
//			
	}
	
	public List<BufferedImage> getImages() {
		return fbImages;
	}


	public boolean isGenerated() {
		return this.generated;
	}


	public void setGenerated(boolean gen) {
		this.generated = gen;
	}

	public boolean isSmoothen() {
		return this.smoothen;
	}
	
	public void setSmoothen(boolean toSmoothen) {
		this.smoothen = toSmoothen;
	}


	public boolean isCaptureOrbit() {
		return this.captureOrbit;
	}


	public void setCaptureOrbit(boolean capOrbit) {
		this.captureOrbit = capOrbit;		
	}
	
	protected void printOrbitMap() {
		System.out.println("orbitMap.size()"+orbitMap.size());
		Set<ComplexNumber> set = orbitMap.keySet();
		Iterator<ComplexNumber> iter = set.iterator();
		while(iter.hasNext()) {
			ComplexNumber k = iter.next();
			List<Double> l = orbitMap.get(k);
			printKeyValueList(k,l);
		}
	}


	private void printKeyValueList(ComplexNumber k, List<Double> l) {
		String ll = "[";
		for(int i = 0; i < l.size(); i++) {
			if(i==l.size()-1) {
				ll += l.get(i)+"]";
			} else {
				ll += l.get(i) + ",";
			}
		}
		System.out.println(k + " ==> "+ll);
//		Iterator<Double> iter = l.iterator();
//		while(iter.hasNext()) {
//			ll += iter.next()+",";
//		}
		
	}

}
