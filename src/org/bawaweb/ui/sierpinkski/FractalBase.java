/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Objects;
import java.util.Random;

import javax.swing.JFrame;


/**
 * @author Navroz
 * Superclass for SierpinskiTriangle, SiepinskiSquare, KochSnowflake
 */
public abstract class FractalBase extends JFrame implements Runnable {
	
	private static final String NEW_LINE = "\r\n";

	private static final long serialVersionUID = 123456543L;
	
	protected static final int HEIGHT 	= 800;
	protected static final int WIDTH 	= 800;

    boolean isOriginUpperLeft 	= true;  	// location of origin
	static int OFFSET 			= 25; 			// pixel offset from edge

	static int depth; 					// recursion depth
	static final int MAX_DEPTH 		= 10;
	
	static final int COLORMAXRGB	= 255;
	
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

	protected boolean useColorPalette = false;
	protected boolean useBlackWhite = false;
	protected boolean useComputeColor = !(this.isUseColorPalette() || this.isUseBlackWhite());
	
	protected double  bound = 2.0;
	
	protected boolean reversePixelCalculation = false;
	
	protected boolean useLyapunovExponent = false;
	
	protected boolean savePixelInfo2File = false;


	/** Constructor: an instance */
	public FractalBase() {
		super();
		setSize(WIDTH, HEIGHT);
		setVisible(true);
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
					run();
				}
			} catch (InterruptedException ex) {
			}
		}
	}
	
	private void generate(int d) {
		depth = d;
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g1) {
		super.paint(g1);
		BufferedImage img = this.createFractalImage();
		Graphics2D g2 = (Graphics2D) g1;
		
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

		this.setImage(img);

		g2.dispose();
	}

	private BufferedImage createFractalImage(){
		Graphics2D g = this.getBufferedImage().createGraphics();
		// Clear the frame
		g.setColor(getBGColor());
		g.fillRect((int)getxC(), (int)getyC(), getWidth(), getHeight());//(0, 0
		g.setColor(Color.red);

		createFractalShape(g);
		
		if (running) {
			addDepthInfo(g);
		}
		
		return this.getBufferedImage();
	}

	private void addDepthInfo(Graphics2D g) {
		g.setColor(Color.red);
		g.drawString("Depth:= " + depth, OFFSET * 2, HEIGHT - OFFSET * 9 + 10);
	}
	
	public static BufferedImage joinBufferedImage(BufferedImage img1, BufferedImage img2) {
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
	
	public static BufferedImage joinAdjacentBufferedImage(BufferedImage img1, BufferedImage img2) {
		// int offset = 2;
		int width = img1.getWidth() + img2.getWidth();
		int height = img1.getHeight();
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		g2.drawImage(img1, null, 0, 0);
		g2.drawImage(img2, null, img1.getWidth(), 0);//img1.getHeight());
		g2.dispose();
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

	protected abstract String getFractalShapeTitle();
	
	
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
		case	"Power":	z = new ComplexNumber(x,0.0).power((int)y);	break;
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
			case "sine":
				pix = Math.sin(pix);
				break;
			case "cosine":
				pix = Math.cos(pix);
				break;
			case "tangent":
				pix = Math.tan(pix);
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
			case "reciprocal":
				pix = 1.0/(pix);
				break;
			case "square":
				pix = Math.pow(pix, 2);
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
			case "sine":
				pix = Math.sin(pix);
				break;
			case "cosine":
				pix = Math.cos(pix);
				break;
			case "tangent":
				pix = Math.tan(pix);
				break;
			default:
				break;
			}
		return pix;
	}
	
	

	protected void setPixel(int col, int row, int rgbColor) {
		validateColumnIndex(col);
		validateRowIndex(row);
		if (isOriginUpperLeft)
			bufferedImage.setRGB(col, row, rgbColor);
		else
			bufferedImage.setRGB(col, HEIGHT - row - 1, rgbColor);
	}
	
	protected Color getPixelDisplayColor(final int row, final  int col, final int colorRGB, final boolean useD) {
		final int[] divs = new int[] { 2, 3, 5, 7, 11, 13 };				//	1st 6 primes
		final int[] colStart = new int[] { 4, 8, 16, 32, 64, 128, 200 };	//	pow 2s till end-200
		Color color = null;
		
		for(int iter = 0; iter < divs.length; iter++) {
			if (colorRGB % divs[iter] == 0) {
				if (this.useColorPalette) {
					if (useD) {
						color = ColorPalette[iter];
					} else {
						color = ColorPalette[ColorPalette.length - iter - 1];
					}
				}/* else if(this.useBlackWhite){
					
				} */else {

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
		
		if(this.useColorPalette){
			if (useD) {
				color = ColorPalette[4];
			} else {
				color = ColorPalette[ColorPalette.length - 5];
			}
			return color;
		}
		
		if (!this.useColorPalette) {
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
	
	
	protected int corectColorRGB(int colorRGB) {
		int corrected = colorRGB > 255 ? 255 : colorRGB;
		corrected = corrected < 0 ? 0 : corrected;
		return corrected;
	}


	/**
	 * // correction for color range  0--255
	 * @param start	---	initial value
	 * @param num	---	value to add
	 * @param sub		---	value to subtract
	 * @param div	---	divisor of num
	 * @return		 color range  0--255
	 */
	protected int correctColor(int start, int num, int sub, int div) {
		int corrected = start + (num / div) - sub;	//random transformation
		corrected = corrected > 255 ? 255 : corrected;
		corrected = corrected < 0 ? 0 : corrected;
		return corrected;
	}
	
	private void validateRowIndex(int row) {
        if (row < 0 || row >= HEIGHT)
            throw new IllegalArgumentException("row index must be between 0 and " + (HEIGHT - 1) + ": " + row);
    }

    private void validateColumnIndex(int col) {
        if (col < 0 || col >= WIDTH)
            throw new IllegalArgumentException("column index must be between 0 and " + (WIDTH - 1) + ": " + col);
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
	
	public static int getMaxIter() {
		return maxIter;
	}

	public static void setMaxIter(int max) {
		FractalBase.maxIter = max;
	}

	public static int getAreaSize() {
		return areaSize;
	}

	public static void setAreaSize(int ss) {
		FractalBase.areaSize = ss;
	}

	public static double getxC() {
		return xC;
	}

	public static void setxC(double x) {
		FractalBase.xC = x;
	}

	public static double getyC() {
		return yC;
	}

	public static void setyC(double y) {
		FractalBase.yC = y;
	}

	public static double getScaleSize() {
		return scaleSize;
	}

	public static void setScaleSize(double scaleSize) {
		FractalBase.scaleSize = scaleSize;
	}

	public BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}

	public void setImage(Image img) {
		this.bufferedImage = (BufferedImage) img;
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
	public boolean isUseBlackWhite() {
		return this.useBlackWhite/*=true*/;
	}

	public void setUseBlackWhite(boolean blackWhite) {
		this.useBlackWhite = blackWhite;
	}

	public boolean isUseColorPalette() {
		return this.useColorPalette/*=false*/;
	}

	public void setUseColorPalette(boolean useCPalette) {
		this.useColorPalette = useCPalette;
	}

	/*//from-apollo
	  final Color[] colors = { 
				Color.BLACK, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, 
				Color.MAGENTA, Color.DARK_GRAY,Color.ORANGE, 
				Color.CYAN, Color.PINK, Color.LIGHT_GRAY};*/
	

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


	// setup for Color palette
	enum Colors {
		RED (Color.RED), BLUE (Color.BLUE), GREEN (Color.GREEN),
		ORANGE (Color.ORANGE), YELLOW (Color.YELLOW), PINK (Color.PINK),
		BLACK (Color.BLACK), WHITE (Color.WHITE),		
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

	//https://en.wikipedia.org/wiki/Julia_set#Field_lines
	private boolean isFatou = false;	//todo	-	rename to getFieldLines??
	public boolean isFatou() {
		return isFatou;
	}

	public void setFatou(boolean isFat) {
		isFatou = isFat;
	}
	
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
	
	/////////////////////////
	//	z=z^2+c
	////////////////////////
	private boolean isZSq = false;	//todo	-	rename to getFieldLines??
	public boolean isZSq() {
		return isZSq;
	}

	public void setZSq(boolean iszsq) {
		isZSq = iszsq;
	}
	

	protected ComplexNumber getZSqValue(ComplexNumber z) {
		double x = z.real();
		double y = z.imaginary();
		return this.getZSqValue(x,y);
	}

	protected ComplexNumber getZSqValue(double x, double y) {
		return new ComplexNumber(x, y).power(2);
	}
	///////////
	//endshttps://en.wikipedia.org/wiki/Julia_set#Field_lines
	
	

	//http://paulbourke.net/fractals/juliaset/
	/*	Julia was  interested  in  the
	 * 	iterative properties of a more general expression, 	 * 
	 * 	namely 
	 * 
	 * z^4 + z^3/(z-1) + z^2/(z^3 + 4*z^2 + 5) + c*/
	private boolean isClassicJulia=false;
	public boolean isClassicJulia() {
		return isClassicJulia;
	}

	public void setClassicJulia(boolean isClassicJulia) {
		this.isClassicJulia = isClassicJulia;
	}
	protected ComplexNumber getClassicJulia(ComplexNumber z) {
		double x = z.real();
		double y = z.imaginary();
		return this.getClassicJulia(x,y);
	}
	
	protected ComplexNumber getClassicJulia(double x, double y) {
		ComplexNumber z = new ComplexNumber(x, y);
		final ComplexNumber one = new ComplexNumber(1.0, 0.0);
		final ComplexNumber four = new ComplexNumber(4.0, 0.0);
		final ComplexNumber five = new ComplexNumber(5.0, 0.0);

		final ComplexNumber first = z.power(4);
		final ComplexNumber second = (z.power(3)).divides(z.minus(one));
		final ComplexNumber third = (z.power(2)).divides((z.power(3)).plus(four.times(z.power(2))).plus(five));

		z = first.plus(second).plus(third);
		return z;
	}
	
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

				if (this.useColorPalette) {
					fw.write("ColorPalette" + NEW_LINE);
				} else if (this.useBlackWhite) {
					fw.write("BlackWhite" + NEW_LINE);
				} else if (this.useComputeColor) {
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

		double row, column;
		int colorRGB;

		public Pixel() {
			super();
		}

		public Pixel(double r, double c, int clr) {
			this.setRow(r);
			this.setColumn(c);
			this.setColorRGB(clr);
		}

		public Pixel(int r, int c) {
			this.setRow(r);
			this.setColumn(c);
		}

		public double getRow() {
			return row;
		}

		public void setRow(double row) {
			this.row = row;
		}

		public double getColumn() {
			return column;
		}

		public void setColumn(double column) {
			this.column = column;
		}

		public int getColorRGB() {
			return colorRGB;
		}

		public void setColorRGB(int colorRGB) {
			this.colorRGB = colorRGB;
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


	class ComplexNumber {
		final double real;		// the real part
		final double imaginary;	// the imaginary part
		
		// return the real or imaginary part
		public double real(){
			return real;
		}
		
		public double imaginary(){
			return imaginary;
		}
		/**
		 * @param r	-	real part
		 * @param i	-	imaginary part
		 */
		public ComplexNumber(double r, double i) {
			super();
			this.real = r;
			this.imaginary = i;
		}
		
		// return a new Complex object whose value is (this * b)
	    public ComplexNumber times(ComplexNumber b) {
	    	ComplexNumber a = this;
	        double real = a.real * b.real - a.imaginary * b.imaginary;
	        double imag = a.real * b.imaginary + a.imaginary * b.real;
	        return new ComplexNumber(real, imag);
	    }
	    
	 // return a new Complex object whose value is (this + b)
	    public ComplexNumber plus(ComplexNumber b) {
	    	ComplexNumber a = this;             // invoking object
	        double real = a.real + b.real;
	        double imag = a.imaginary + b.imaginary;
	        return new ComplexNumber(real, imag);
	    }
	    
	 // return a new Complex object whose value is (this - b)
	    public ComplexNumber minus(ComplexNumber b) {
	    	ComplexNumber a = this;             // invoking object
	        double real = a.real - b.real;
	        double imag = a.imaginary - b.imaginary;
	        return new ComplexNumber(real, imag);
	    }

		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			if (imaginary == 0.0 && real != 0.0)
				return real + "";
			if (real == 0.0 && imaginary != 0.0)
				return imaginary + "i";
			if (real < 0.0) {
				if (imaginary < 0.0)
					return real + " - " + (-1*imaginary) + "i";
				else
					return real + " + " + imaginary + "i";
			} else if (real > 0.0) {
				if (imaginary < 0.0)
					return real + " - " + (-1*imaginary) + "i";
				else
					return real + " + " + imaginary + "i";
			}
			return real + " + " + imaginary + "i";
//			return "ComplexNumber [real=" + real + ", imaginary=" + imaginary + "]";
		}
		
		// See Section 3.3.
	    public boolean equals(Object x) {
	        if (x == null) return false;
	        if (this.getClass() != x.getClass()) return false;
	        ComplexNumber that = (ComplexNumber) x;
	        return (this.real == that.real) && (this.imaginary == that.imaginary);
	    }

	    // See Section 3.3.
	    public int hashCode() {
	        return Objects.hash(real, imaginary);
	    }

		public double abs() {
			return Math.hypot(real, imaginary);
		}
		
		
		/**
		 * https://math.stackexchange.com/questions/44406/how-do-i-get-the-square-root-of-a-complex-number
		 * 
		 * z=c+d*i				c=real,	d=imaginary
		 * 
		 * sqroot(z)=a+b*i
		 * 
		 * a=root((c+root(c^2+d^2))/2) 
		 * b=d/|d|*root((-c+root(c^2+d^2))/2)
		 * 
		 * d/|d|	==> b has same sign as d
		 * 
		 * @return
		 */
		public ComplexNumber sqroot() {
//			System.out.println("Real==="+real+"---Img:=="+imaginary);
			double a = Math.sqrt(Math.abs(real) + ( Math.pow(Math.abs(real), 2.0) + Math.pow(Math.abs(imaginary), 2.0) ) / 2.0);
			double b = Math.sqrt( Math.abs(real /** (-1.0)*/) + Math.sqrt( Math.pow(Math.abs(real), 2.0) + Math.pow(Math.abs(imaginary), 2.0) ) / 2.0);
//System.out.println("a==="+a);
//System.out.println("b==="+b);
			if (real == 0.0 && imaginary != 0.0) {
//				System.out.println("returning___new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary)));"+new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary))));
				return new ComplexNumber(0.0, Math.sqrt(Math.abs(imaginary)));
			}
			if (imaginary > 0.0) {
//				System.out.println("returning___new ComplexNumber(a, b);"+new ComplexNumber(a, b));
				return new ComplexNumber(a, b);
			} else if (imaginary < 0.0) {
//				System.out.println("returning___new ComplexNumber(a, b * (-1.0));"+new ComplexNumber(a,( Math.abs(b) * (-1.0))));
			
				return new ComplexNumber(a,( Math.abs(b) * (-1.0)));
			}
//			System.out.println("returning___new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0);"+new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0));
			return new ComplexNumber(Math.sqrt(Math.abs(real)), 0.0);
		}
		
		public ComplexNumber curoot(){
			return sqroot();	//for-now
		}

		
		/**https://en.wikipedia.org/wiki/Complex_logarithm
		//
		//	z=x+y*i
		//	log(z)=ln(root(x^2+y^2))+atan2(y,x)*i
		 * 
		 * @return
		 */
		public ComplexNumber ln() {
			double a = Math.log(Math.sqrt(Math.pow(real, 2.0) + Math.pow(imaginary, 2.0)));
			double b = Math.atan2(imaginary, real);
			return new ComplexNumber(a, b);
		}
		
		
		/*public final ComplexNumber one = new ComplexNumber(1.0, 0.0);*/

	    // return a new Complex object whose value is the complex exponential of this
		// return e ^ z
	    public ComplexNumber exp() {
	        return new ComplexNumber(Math.exp(real) * Math.cos(imaginary), Math.exp(real) * Math.sin(imaginary));
	    }

	    // return a new Complex object whose value is the complex sine of this
	    public ComplexNumber sin() {
	        return new ComplexNumber(Math.sin(real) * Math.cosh(imaginary), Math.cos(real) * Math.sinh(imaginary));
	    }

	    // return a new Complex object whose value is the complex cosine of this
	    public ComplexNumber cos() {
	        return new ComplexNumber(Math.cos(real) * Math.cosh(imaginary), -Math.sin(real) * Math.sinh(imaginary));
	    }

	    // return a new Complex object whose value is the complex tangent of this
	    public ComplexNumber tan() {
	        return sin().divides(cos());
	    }
	    

	    // return a / b
	    public ComplexNumber divides(ComplexNumber b) {
	        ComplexNumber a = this;
	        return a.times(b.reciprocal());
	    }
	 // return a new Complex object whose value is the reciprocal of this
	    public ComplexNumber reciprocal() {
	        double scale = real*real + imaginary*imaginary;
	        return new ComplexNumber(real / scale, -imaginary / scale);
	    }

		public ComplexNumber power(int power) {
			if (power > 0) {
				int iter = 1;
				final ComplexNumber a = this;
				ComplexNumber powered = a;
				for (int i = 1; i < power; i++) {
					powered = powered.times(a);
				}
				return powered;
			} else if (power < 0) {
				final ComplexNumber a = this;
				ComplexNumber powered = a.reciprocal();
				int powAbs = Math.abs(power);
				for (int i = 1; i < powAbs; i++) {
					powered = powered.times(powered);
				}
				return powered;

			}	
			//power==0
			return new ComplexNumber(1.0,0.0);	//	^0==1
		}

		public ComplexNumber sine() {
			double sineR = Math.sin(this.real) * Math.cosh(this.imaginary);
			double sineI = Math.cos(this.real) * Math.sinh(this.imaginary);
			return new ComplexNumber(sineR,sineI);
		}
		
		//arcsin
		public ComplexNumber inverseSine(){
			return new ComplexNumber(1.0, 0.0).divides(this.sine());
		}
		
		public ComplexNumber cosine() {
			// cos(x)*cosh(y) - isin(x)*sinh(y)
			double cosR = Math.cos(this.real) * Math.cosh(this.imaginary);
			double cosI = -Math.sin(this.real) * Math.sinh(this.imaginary);
			return new ComplexNumber(cosR, cosI);
		}
		
		//arccos
		public ComplexNumber inverseCosine(){
			return new ComplexNumber(1.0, 0.0).divides(this.cosine());
		}
		
		public ComplexNumber tangent(){
			return this.sine().divides(this.cosine());
		}
		
		//arctan
		public ComplexNumber inverseTangent(){
			return new ComplexNumber(1.0, 0.0).divides(this.tangent());
		}
		
	}
	
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
		this.pxConstOperation=pxConstOp;		
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
	
}
