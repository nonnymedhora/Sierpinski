/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;


/**
 * @author Navroz
 * Superclass for SierpinskiTriangle, SiepinskiSquare, KochSnowflake
 */
public abstract class FractalBase_vInternalFrameTesting extends JInternalFrame implements Runnable {
	
	private static final long serialVersionUID = 123456543L;
	protected static final int HEIGHT = 600;
	protected static final int WIDTH = 600;

    boolean isOriginUpperLeft = true;  	// location of origin
	static int OFFSET = 25; 			// pixel offset from edge

	static int depth; 					// recursion depth
	static final int MAX_DEPTH = 10;
	
	final static int maxIter = 255;		//	maximum iterations to check for Mandelbrot
	
	BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	final Point center = new Point(WIDTH / 2, HEIGHT / 2);
	
	protected boolean running = false;

	/** Constructor: an instance */
	public FractalBase_vInternalFrameTesting() {
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
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g1) {
		super.paint(g1);
		Image img = createFractalImage();
		Graphics2D g2 = (Graphics2D) g1;
		g2.drawImage(img, null, null);
		g2.dispose();
	}

	private Image createFractalImage(){
		Graphics2D g = bufferedImage.createGraphics();
		// Clear the frame
		g.setColor(getBGColor());
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.red);

		createFractalShape(g);
		return bufferedImage;
	}

	protected Color getBGColor() {
		return Color.black;
	}

	public abstract void createFractalShape(Graphics2D g);

	protected abstract String getFractalShapeTitle();
	
	protected void setPixel(int col, int row, int rgbColor) {
		validateColumnIndex(col);
		validateRowIndex(row);
		if (isOriginUpperLeft)
			bufferedImage.setRGB(col, row, rgbColor);
		else
			bufferedImage.setRGB(col, HEIGHT - row - 1, rgbColor);
	}
	
	protected Color getPixelDisplayColor(int row, int col, int colorRGB) {
		Color color;
		/* Simple */
		/* color = new Color(gray, gray, gray); */
		/* Complex */ 
		if (colorRGB % 2 == 0) {
			color = new Color(colorRGB, colorRGB, colorRGB);
		} else if (colorRGB % 3 == 0) {
			/* color = Colors.GREEN.getColor(); */
			final int start = 32;
			int num1 = correctColor(start, colorRGB, row, 3);
			int num2 = correctColor(start, colorRGB, col, 3);
			color = new Color(num1, num2, start);
		} else if (colorRGB % 5 == 0) {
			final int start = 64;
			int num1 = correctColor(start, colorRGB, row, 5);
			int num2 = correctColor(start, colorRGB, col, 5);
			color = new Color(num1, num2, start);
		}
		else if (colorRGB % 7 == 0) {
			/* color=Colors.RED.getColor(); */
			final int start = 128;
			int num1 = correctColor(start, colorRGB, row, 7);
			int num2 = correctColor(start, colorRGB, col, 7);
			color = new Color(num1, num2, start);
		} else {
			/* color=Colors.BLUE.getColor(); */

			final int start = 255;
			int num1 = correctColor(start, colorRGB, row, 1);
			int num2 = correctColor(start, colorRGB, col, 1);
			color = new Color(num1, num2, start);
		}
		return color;
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
		int corrected = start + num / div - sub;	//random transformation
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
	
	protected void drawTriangle(Graphics2D g, Point p1, Point p2, Point p3, Color color) {
		Path2D myPath = getTrianglePath(p1, p2, p3);

		g.setPaint(color);
		g.draw(myPath);
	}
	
	public static final Color[] ColorPalette = new Color[] {
			Colors.BLACK.getColor(),
			Colors.RED.getColor(),
			Colors.BLUE.getColor(),
			Colors.GREEN.getColor(),
			Colors.ORANGE.getColor(),
			Colors.LIGHT_GRAY.getColor(),
			Colors.YELLOW.getColor(),
			Colors.PINK.getColor(),
			Colors.MAGENTA.getColor(),
			Colors.CYAN.getColor(),
			Colors.DARK_GRAY.getColor(),
			Colors.WHITE.getColor()
			
			
	};


	// setup for Color palette
	enum Colors {
		RED (Color.RED), BLUE (Color.BLUE), GREEN (Color.GREEN),
		ORANGE (Color.ORANGE), YELLOW (Color.YELLOW), PINK (Color.PINK),
		BLACK (Color.WHITE), WHITE (Color.WHITE),		
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


	
	class Line {
		private double x, y, length, angle;
		
		public Line(Point p1,Point p2){
			super();
			this.x=p1.x;
			this.y=p1.y;
			
			this.length=this.length(p1,p2);
			this.angle=this.angle(p1,p2);
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
			if (imaginary == 0) return real + "";
	        if (real == 0) return imaginary + "i";
	        if (imaginary <  0) return real + " - " + (-imaginary) + "i";
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
		

	    // return a new Complex object whose value is the complex exponential of this
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
			assert (power > 0);
			int iter = 1;
			final ComplexNumber a = this;
			ComplexNumber powered = a;
			for(int i = 1; i < power; i++) {
				powered=powered.times(a);
			}
			return powered;
		}
		
	}


	public void reset() {
		depth=0;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}
	
}
