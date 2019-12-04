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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;



/**
 * @author Navroz
 *	Displays the popular Mandelbrot Set
 *	see
 *	https://en.wikipedia.org/wiki/Mandelbrot_set
 */
public class Mandelbrot extends FractalBase {
	
	private static final long serialVersionUID = 13456L;

	private static final int BUDDHA_START = 20;
	private static final int IM_BUDDHA = 123;
	private int magnification;
	private boolean useDiff = false;
//	private int size;		//0-599 4 ltr
	
	private boolean isComplexNumConst = false;
	private ComplexNumber complex;// = new ComplexNumber(-0.75, 0.11);
	private boolean isConstFuncApplied = false;
	
	/*// Buddhabrot*/
	protected boolean isBuddha = false;// true;//
	/*private Map<Pixel,Integer> buddhaMap=new HashMap<Pixel,Integer>();
	private List<Pixel> buddhaList=new ArrayList<Pixel>();*/
	
	
	
	/*/Motionbrot			*/
	private boolean isMotionBrot = false;// true;//
	
	private String motionParam = "bd";	//	others are exponent/power/magnification/scaleSize
	private double motionParamJumpVal = 0.25;
	private Image[] motionImages = new Image[MAX_DEPTH];//[this.depth];	//[10];;
	
	
	public Mandelbrot() {
		super();
		this.magnification = 2;
		this.power = 2;
	}

	/**
	 * @param mg	--	magnification
	 * mg=1 is high, mg=10 = low
	 */
	public Mandelbrot(int mg) {
		super();
		this.magnification = mg;
		this.power = 2;
		this.isComplexNumConst=true;
		this.complex=null;
	}

	/**
	 * @param mg
	 * @param ep
	 */
	public Mandelbrot(int mg, int ep) {
		this(mg);
		this.power = ep;
	}

	/**
	 * @param mg
	 * @param ep
	 * @param dif
	 */
	public Mandelbrot(int mg, int ep, boolean dif) {
		this(mg, ep);
		this.useDiff = dif;
	}

	/**
	 * @param magnification
	 * @param exp
	 * @param useDiff
	 * @param complexConst
	 */
	public Mandelbrot(int mg, int ep, boolean useD, ComplexNumber complexConst) {
		this(mg, ep, useD);
		this.complex = complexConst;
		this.isComplexNumConst = false;
	}
	
	public Mandelbrot(int mg, int ep, boolean useD, double real, double img) {
		this(mg,ep,useD);
//		System.out.println(" Mandelbrot(int mg("+mg+"), int ep("+ep+"), boolean useD, double real("+real+"), double img("+img+"))");
		this.complex = new ComplexNumber(real, img);
		this.isComplexNumConst = false;
	}
	
	public Mandelbrot(int mg, int ep, boolean useD, double bd, double real, double img) {
		this(mg,ep,useD,real,img);
//		System.out.println(" Mandelbrot(int mg("+mg+"), int ep("+ep+"), boolean useD, double real("+real+"), double img("+img+"))");
		this.setBound(bd);
	}

	public Mandelbrot(int mg, int ep, boolean useD, boolean complexNumIsConst) {
		this(mg, ep, useD);
		this.isComplexNumConst = complexNumIsConst;
	}

	public Mandelbrot(int mg, int ep, boolean useD, double bd, boolean complexNumIsConst) {
		this(mg,ep,useD,complexNumIsConst);
		this.setBound(bd);
	}

	public Mandelbrot(Properties p) {
		super(p);

		if (p.getProperty("useDiff") != null) {
			this.setUseDiff(Boolean.parseBoolean(p.getProperty("useDiff").replaceAll(WHITESPACE,EMPTY)));
		} 
		if (p.getProperty("isComplexNumConst") != null) {
			this.setComplexNumConst(Boolean.parseBoolean(p.getProperty("isComplexNumConst").replaceAll(WHITESPACE,EMPTY)));
		}
		if (p.getProperty("constReal") != null && p.getProperty("constImag") != null/* && !(p.getProperty("constReal").replaceAll(WHITESPACE,EMPTY).equals("DynamicConst") || p.getProperty("constImag").replaceAll(WHITESPACE,EMPTY).equals("DynamicConst"))*/) { 
			this.setComplex(Double.parseDouble(p.getProperty("constReal").replaceAll(WHITESPACE, EMPTY)),
					Double.parseDouble(p.getProperty("constImag").replaceAll(WHITESPACE, EMPTY)));
			this.setComplexNumConst(false);		//	redundant	=	BUT!
		}		
		if (p.getProperty("magnificationChoice") != null) {
			this.setMagnification(Integer.parseInt(p.getProperty("magnificationChoice").replaceAll(WHITESPACE,EMPTY)));
		}
		
	}


	/* (non-Javadoc)
	 * @see org.bawaweb.ui.sierpinkski.FractalBase#createFractalShape(java.awt.Graphics2D)
	 */
	@Override
	public void createFractalShape(Graphics2D g) {
		/*System.out.println("increatefractalshape");*/
		if (this.isBuddha) {
			createBuddhabrot(g, this.useDiff);
		}else if(isMotionBrot){
			this.createMotionbrot(g,depth);
		}else /*if(this.isMotionBrot)*/{
			createMandelbrot(g, this.useDiff);
		}
	}
	
	public void paint(Graphics g1) {
		// regular or classic and buddha
		// static iterative-function on complex
		super.paint(g1);
		
		// for motionbrot
		if (this.isMotionBrot) {
			
			Graphics2D g = (Graphics2D) g1;
			Image img = this.createMotionbrot(g, depth);

			int motionCount = this.getMotionImageCount();
			for (int i = 0; i <= motionCount; i++) {
				try {
					System.out.println("motionCount===" + motionCount + " and depth is " + depth);
					Thread.sleep(1000);
					g.drawImage(this.motionImages[i], null, null);// g.drawImage(img, null, null);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			g.dispose();
		}
	}

	private int getMotionImageCount() {
		for (int i = 0; i < this.motionImages.length; i++) {
			if (this.motionImages[i] == null) {
				return i;
			}
		}
		return 0;
	}

	private int addMotionImage(Image img) {
		for (int i = 0; i < this.motionImages.length; i++) {
			if (this.motionImages[i] == null) {
				this.motionImages[i] = img;
				return i;
			}
		}
		return 0;

	}

	private Image createMotionbrot(Graphics2D g, int depth) {
		System.out.println("here");
		double xc = getxC();
		double yc = getyC();
		double size = this.magnification;
		double bd = this.getBound();		
		int pow = this.power;
		int max = getMaxIter();

		String func2Apply = this.useFuncConst;
		String pxFunc2Apply = this.useFuncPixel;

		int n = getAreaSize();
		this.createMandelbrotPixels(this.useDiff, xc, yc, size, bd, pow, max, func2Apply, pxFunc2Apply, n);

		this.drawMandelMotionbrot(g, depth, bd, pow, size);
		
		this.addMotionImage(bufferedImage);
		return bufferedImage;

	}

	private void drawMandelMotionbrot(Graphics2D g, int d, double bd, int pwr, double size) {
		
		System.out.println("drawMotiondepth = "+depth+" and d is "+d+" time is "+System.currentTimeMillis());
		System.out.print("motionParam is "+this.motionParam);
		
		double xc = getxC();
		double yc = getyC();
		/*double size = this.mag;*/
		
		if (this.motionParam.equals("bd")) {
			bd+=this.getMotionParamJumpVal();	//0.25;
			this.bound=bd;
			
			System.out.println("  bd is now "+this.bound+" and d is "+d);
		}
		
		if(this.motionParam.equals("pow")){
			pwr+=this.getMotionParamJumpVal();	//1;
			this.power=pwr;
			System.out.println("  pow is now "+this.power+" and d is "+d);
		}
		

		if (this.motionParam.equals("scaleSize")) {
			size+=this.getMotionParamJumpVal();	
			this.magnification=(int) size;
			
			System.out.println("  scaleSize is now "+this.magnification+" and d is "+d);
		}

		int max = getMaxIter();

		String func2Apply = this.useFuncConst;
		String pxFunc2Apply = this.useFuncPixel;
		
		int n = getAreaSize();
		System.out.println("this.useDiff is "+this.useDiff);
		this.useDiff = !this.useDiff;
		System.out.println("this.useDiff is now "+this.useDiff);
		//this.createMandelbrotPixels(this.useDiff, xc, yc, size, bd, pwr, max, func2Apply, pxFunc2Apply, n);
		
		if (d == 0) { // depth is 0, draw the triangle
			/*g.setStroke(new BasicStroke(1));
			drawLine(g, p1, p2);
			drawLine(g, p2, p3);
			drawLine(g, p3, p1);*/ 
			System.out.println("this.useDiff is "+this.useDiff+" and d="+d+"and depth is"+depth+" and bd is "+bd);
			this.createMandelbrotPixels(this.useDiff, xc, yc, size, bd, pwr, max, func2Apply, pxFunc2Apply, n);

			if (this.motionParam.equals("bd")) {
				this.bound+=this.getMotionParamJumpVal();//0.25;
			}
			
			if(this.motionParam.equals("pow")){
				pwr+=this.getMotionParamJumpVal();//1;
				this.power=pwr;
			}
			
			if (this.motionParam.equals("scaleSize")) {
				size+=this.getMotionParamJumpVal();	
				this.magnification=(int) size;
				
				System.out.println("  scaleSize is now "+this.magnification+" and d is "+d);
			}
			
			
			return;
		}
		
//		this.createMandelbrotPixels(this.useDiff, xc, yc, size, bd, pwr, max, func2Apply, pxFunc2Apply, n);
//		
		this.drawMandelMotionbrot(g, d - 1, bd, pwr, size);

		
	}

	private void createBuddhabrot(Graphics2D g, boolean diff) {
		double xc = getxC();
		double yc = getyC();
		double size = this.magnification;
		double bd = this.getBound();
		int max = getMaxIter();
		
		String func2Apply = this.useFuncConst;
		String pxFunc2Apply = this.useFuncPixel;
		
		if (this.isSavePixelInfo2File()) {
			addConstInfo(func2Apply);
		}

		int n = getAreaSize();
		

		Map<Pixel,ComplexNumber> buddhaMap = this.createNonMandelbrotPixelsMap(diff, xc, yc, size, bd, max, func2Apply, pxFunc2Apply, n);
		
		System.out.println("budhaMap.size()=="+buddhaMap.size());
		/*
		for(Pixel buddhaPix: buddhaMap.keySet()){
			setPixel(buddhaPix.row, n - 1 - buddhaPix.column, buddhaPix.colorRGB);
		}
		*/
		List<Pixel> buddhaPixPaths = this.traceBuddhaTrajectories(buddhaMap);
		System.out.println("buddhaPixPaths.size()=="+buddhaPixPaths.size());
		
		this.drawPixelPath(g,buddhaPixPaths);
	}

	private List<Pixel> traceBuddhaTrajectories(Map<Pixel, ComplexNumber> bMap) {
		List<Pixel> randBuddhaPixList = this.getRandomPixList(bMap);
		
		boolean isNotM = this.confirmNonMFrstPass(randBuddhaPixList);
		if (isNotM) {
			return this.createBuddhaTrajectories(randBuddhaPixList);
		}
		return null;
	}
	
	private List<Pixel> createBuddhaTrajectories(List<Pixel> nonMandPix) {
		List<Pixel> allPixels = new ArrayList<Pixel>();
		int n = getAreaSize();
		int max = getMaxIter();
		System.out.println("nonMandPix.size()===="+nonMandPix.size());
		for (Pixel aPix : nonMandPix) {			
			ComplexNumber z0 = aPix.compVal;//this.getZValue(func2Apply, pxFunc2Apply, x0, y0);
			allPixels.addAll( this.drawBuddhabrot(z0, max, this.power, n,this.complex, aPix) );
		}
		
		return allPixels;
	}

	private List<Pixel> getRandomPixList(Map<Pixel, ComplexNumber> bMap) {
		final int numSize = bMap.size()/BUDDHA_START;
		Pixel[] rPixArray = new Pixel[numSize];
		rPixArray=bMap.keySet().toArray(rPixArray);
		List<Pixel> rList = new ArrayList<Pixel>(numSize);

		while (rList.size() < numSize) {
			int r = new Random().nextInt(bMap.size());

			final Pixel budhR = rPixArray[r];
			
			if (!rList.contains(budhR)) {
				rList.add(budhR);
			}
		}
		System.out.println("returning rList size=="+rList.size());
		return rList;
	}

	private Map<Pixel, ComplexNumber> createNonMandelbrotPixelsMap(boolean diff, double xc, double yc, double size,
			double bd, int max, String func2Apply, String pxFunc2Apply, int n) {
		Map<Pixel,ComplexNumber> bMap = new HashMap<Pixel,ComplexNumber>();
		
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				
				ComplexNumber z0 = this.getZValue(func2Apply, pxFunc2Apply, x0, y0);
				if (z0.isNaN()) {
					continue;
				}

				int res = this.mand(z0, max, this.power, this.complex, bd, row, col);
				if(res==IM_BUDDHA){//if (res < max) { // 
					Pixel p = new Pixel(row, col, IM_BUDDHA);
					p.setCompVal(z0);

					bMap.put(p, z0);
				}
			}
		}
		
		return bMap;
	}

	private void createMandelbrot(Graphics2D g, boolean diff) {		
//		System.out.println("getXC--- "+getxC());		
//		System.out.println("getYC--- "+getyC());
		
		double xc = getxC();//-0.5;//getxC();//-0.5;
		double yc = getyC();//0;//getyC();//0;
		double size = this.magnification;//getScaleSize();//this.mag;	//10;//4;//2;
		double bd = this.getBound();
		int max = getMaxIter();
//		System.out.println("in__Mandelbrot__createMandelbrot (isComplexNumConst || this.complex == null) = "+(isComplexNumConst || this.complex == null));		

		String func2Apply = this.useFuncConst;
		String pxFunc2Apply = this.useFuncPixel;
		
		if (this.isSavePixelInfo2File()) {
			addConstInfo(func2Apply);
		}

		int n = getAreaSize();//599;//512;	(0-599)
		int pow = this.power;
		//System.out.println("here with depth " + depth);
		this.createMandelbrotPixels(diff, xc, yc, size, bd, pow, max, func2Apply, pxFunc2Apply, n);
		
		if (this.isSavePixelInfo2File()) {
			this.closePixelFile();
		}
		/*
		if (this.isBuddha) {
			System.out.println("BuddhaSize===" + this.buddhaMap.size());
			for (Pixel p : this.buddhaMap.keySet()) {
				System.out.println(p);
			}
		}
		
		if(this.isBuddha){this.processBuddhaMap();}*/
	}

	private void addConstInfo(String func2Apply) {
		if (!this.isComplexNumConst) {
			if (func2Apply.equals("None")) {
				this.appendConstantInfo2File("Dynamic Pixel Constant   z=C");
			} else {
				this.appendConstantInfo2File("Dynamic Pixel Constant  " + func2Apply + " (z)=C");
			}
		} else {
			if (this.useFuncConst.equals("None")) {
				this.appendConstantInfo2File("Constant z=C");
			} else {
				this.appendConstantInfo2File("Constant  " + this.useFuncConst + " (z)=C");
			}
		}
	}

	private void createMandelbrotPixels(boolean diff, double xc, double yc, double size, double bd, int pow, int max,
			String func2Apply, String pxFunc2Apply, int n) {
		for (int row = 0; row < n; row++) {
			for (int col = 0; col < n; col++) {
				double x0 = xc - size / 2 + size * row / n;
				double y0 = yc - size / 2 + size * col / n;
				
				ComplexNumber z0 = this.getZValue(func2Apply, pxFunc2Apply, x0, y0);	
				if (z0.isNaN()) {
					continue;
				}


				if (this.colorChoice.equals(BlackWhite)) {
					int bOrW = 0;
					
					if (diff) {
						bOrW = this.mand(z0, max, pow, this.complex, bd, row, col);
						/*if (!this.isBuddha) {*/
							if (bOrW != max) {
								bOrW = 0;
							} else {
								bOrW = COLORMAXRGB;
							} 
						/*}*/
					} else {
						int res = this.mand(z0, max, pow, this.complex, bd, row, col);
						/*if (!this.isBuddha) {*/
							bOrW = max - res;
							if (bOrW != res) {
								bOrW = 0;
							} else {
								bOrW = COLORMAXRGB;
							} 
						/*}*/

					}
					
					/*if (!this.isBuddha) {*/
						setPixel(row, n - 1 - col, bOrW);
					/*}*/
					if (this.isSavePixelInfo2File()) {
						this.appendPixelInfo2File(row, n - 1 - col, bOrW);
					}

				} else {

					int colorRGB;
	
					if (diff) {
						colorRGB = mand(z0, max, pow, this.complex, bd, row, col);
					} else {
						colorRGB = max - mand(z0, max, pow, this.complex,bd, row, col);
					}
					
					Color color = null;
	
					/*if (!this.isBuddha) {*/
						color = this.getPixelDisplayColor(row, col, colorRGB, diff);
						setPixel(row, n - 1 - col, color.getRGB());
					/*}*/
					if (this.isSavePixelInfo2File()) {
						this.appendPixelInfo2File(row, n - 1 - col, color.getRGB());
					}
				}

			}
		}
	}

	private ComplexNumber getZValue(String func2Apply, String pxFunc2Apply, double x0, double y0) {
		ComplexNumber z0;
		if (!this.isReversePixelCalculation()) {
			z0 = new ComplexNumber(x0, y0);
			z0 = this.getPixelComplexValue(x0, y0);					
		} else {
			z0 = new ComplexNumber(y0, x0);
			z0 = this.getPixelComplexValue(y0, x0);
		}
		
		boolean pxFuncHasConst = (pxFunc2Apply.indexOf('c') > -1 || pxFunc2Apply.indexOf('C') > -1);
		
		/*z0 = this.computePixel(pxFunc2Apply, z0);*/

		if (!pxFuncHasConst) {
			z0 = this.computePixel(pxFunc2Apply, z0);
		} else {
			ComplexNumber cConst = null;

			if (this.isComplexNumConst || this.complex == null) {
				cConst = z0;
				this.complex = z0;
			} else if (!this.isComplexNumConst) {
				if(this.complex==null){System.out.println("uh--oh  this.complex="+this.complex+" avd this.isComplexNumConst="+this.isComplexNumConst);}
				cConst = this.complex;
			}

			z0 = this.computePixel(pxFunc2Apply, z0, cConst);
		}
		
		if (z0.isNaN()) {
			return z0;
		}

		z0 = this.computeComplexConstant(func2Apply, z0);
		return z0;
	}
	
	
//	private void processBuddhaMap() {
//		List<Pixel> nonMandPix = this.getRandomPixList();
//
//		boolean isNotM = this.confirmNonMFrstPass(nonMandPix);
//		System.out.println("isNotM is " + isNotM + "nonMandPix size is " + nonMandPix.size());
//
//		if (isNotM) {
//			this.createBuddhaTrajectories(nonMandPix);
//		}
//
//	}

//	private void createBuddhaTrajectories(List<Pixel> nonMandPix) {
//		double xc = getxC();
//		double yc = getyC();
//		double size = this.mag;
//		double bd = this.getBound();
//		int n = getAreaSize();
//		int max = getMaxIter();
//		String func2Apply = this.useFuncConst;
//		String pxFunc2Apply = this.useFuncPixel;
//		boolean diff = this.useDiff;
//		
//		int valGrt1 = 0;
//		for(Pixel p : this.buddhaMap.keySet()){
//			if(this.buddhaMap.get(p)!=1){
//				valGrt1+=1;
//				
//			}
//		}
//		
//		System.out.println("valGrt1==="+valGrt1);
//		
//
//		for (Pixel aPix : nonMandPix) {
//			int row = aPix.row;
//			int col = aPix.column;
//
//			double x0 = xc - size / 2 + size * row / n;
//			double y0 = yc - size / 2 + size * col / n;
//			
//			ComplexNumber z0 = this.getZValue(func2Apply, pxFunc2Apply, x0, y0);
//			this.drawBuddhabrot(z0, max, this.power, n,this.complex, /*bd, row, col*/aPix);
//		}
//	}
//
	private boolean confirmNonMFrstPass(List<Pixel> pixList) {
		double xc = getxC();
		double yc = getyC();
		double size = this.magnification;
		double bd = this.getBound();
		int n = getAreaSize();
		int max = getMaxIter();
		String func2Apply = this.useFuncConst;
		String pxFunc2Apply = this.useFuncPixel;
		boolean diff = this.useDiff;

		for (Pixel aPix : pixList) {
			int row = aPix.row;
			int col = aPix.column;

			double x0 = xc - size / 2 + size * row / n;
			double y0 = yc - size / 2 + size * col / n;
			
			ComplexNumber z0 = this.getZValue(func2Apply, pxFunc2Apply, x0, y0);
			if (z0.isNaN()) {
				continue;
			}

			if (this.mand(z0, max, this.power, this.complex, bd, row, col) != IM_BUDDHA) {
				System.out.println("In+confirm+++NOTOOOOOok");
				return false;
			}
		}
		
		System.out.println("In+confirm+++ok");
		
		return true;

	}
//
//	private List<Pixel> getRandomPixList() {
//		final int numSize = this.buddhaMap.size()/BUDDHA_START;
//		List<Pixel> rList = new ArrayList<Pixel>(numSize);
//
//		while (rList.size() < numSize) {
//			int r = new Random().nextInt(this.buddhaList.size());
//
//			final Pixel budhR = this.buddhaList.get(r);
//			if (!rList.contains(budhR)) {
//				rList.add(budhR);
//			}
//		}System.out.println("returning rList size=="+rList.size());
//		return rList;
//	}
	

	private ComplexNumber computePixel(String fun, ComplexNumber z0, ComplexNumber compConst) {
		if (this.applyCustomFormula) {
			fun = fun.replaceAll("c", "(" + compConst.toString().replaceAll("i", "*i") + ")")
					.replaceAll("C", "(" + compConst.toString().replaceAll("i", "*i") + ")");

			return new FunctionEvaluator().evaluate(fun, z0);//, compConst);
		}
		return null;
	}


	private ComplexNumber computePixel(String fun, ComplexNumber z0) {
		if (!this.applyCustomFormula) {
			switch (fun) {
				case "sine":
					z0 = z0.sine();
					break;
				case "coosine":
					z0 = z0.cosine();
					break;
				case "tan":
					z0 = z0.tangent();
					break;
				case "arcsine":
					z0 = z0.inverseSine();
					break;
				case "arccosine":
					z0 = z0.inverseCosine(); 
					break;
				case "arctan":
					z0 = z0.inverseTangent();
					break;
				case "reiprocal":
					z0 = z0.reciprocal();
					break;
				case "reciprocalSquare":
					z0 = (z0.reciprocal()).power(2);
					break;
				case "square":
					z0 = z0.power(2);
					break;
				case "cube":
					z0 = z0.power(3);
					break;
				case "exponent(e)":
					z0 = z0.exp();
					break;
				case "root":
					z0 = z0.sqroot();
					break;
				case "cube-root":
					z0 = z0.curoot();
					break;
				case "log(e)":
					z0 = z0.ln();
					break;	
				case "None":
					z0 = z0;
					break;
				default:
					z0 = z0;
					break;
			}// ends-switch
		}	else {
			z0 = new FunctionEvaluator().evaluate(fun, z0);
		}

		return z0;
	}

	private ComplexNumber computeComplexConstant(String func2Apply, ComplexNumber z0) {
		ComplexNumber cConst = null;

		if (this.isComplexNumConst || this.complex == null) {
			cConst = z0;
			this.complex = z0;
		} else if (!this.isComplexNumConst) {
			cConst = this.complex;
		}

		if (!this.isConstFuncApplied) {
			switch (func2Apply) {
			case "sine":
				cConst = cConst.sine(); //z0.sin();
				break;
			case "cosine":
				cConst = cConst.cosine(); //z0.cos();
				break;
			case "tan":
				cConst = cConst.tangent(); //z0.tan();
				break;
			case "arcsine":
				cConst = cConst.inverseSine(); //z0.sin();
				break;
			case "arccosine":
				cConst = cConst.inverseCosine(); //z0.cos();
				break;
			case "arctan":
				cConst = cConst.inverseTangent(); //z0.tan();
				break;
			case "reciprocal":
				cConst = cConst.reciprocal(); //1/z0;
				break;
			case "reciprocalSquare":
				cConst = (cConst.reciprocal()).power(2); //(1/z)^2;
				break;
			case "square":
				cConst = cConst.power(2); //z0.sin();
				break;
			case "cube":
				cConst = cConst.power(3); //z0.cos();
				break;
			case "exponent(e)":
				cConst = cConst.exp(); //z0.tan();
				break;
			case "root":
				cConst = cConst.sqroot(); //z0.sin();
				break;
			case "cube-root":
				cConst = cConst.curoot(); //z0.cos();
				break;
			case "log(e)":
				cConst = cConst.ln(); //z0.tan();
				break;

			case "None":
				cConst = cConst;
				break;
			default:
				this.complex = cConst;
				break;
			}//ends-switch

			if (!this.isComplexNumConst) {
				this.complex = cConst;
				this.isConstFuncApplied = true;
			}
		}
		
		return cConst;
	}
	
	
	private List<Pixel> drawBuddhabrot(ComplexNumber z0, int maxIterations, int pwr, int size, ComplexNumber constant, Pixel pix) {
//		System.out.println("in drawBuddhabrot - pix is "+pix);

		List<Pixel> pixelPathList = new ArrayList<Pixel>();
		pixelPathList.add(pix);
		
		int row = pix.row;
		int col = pix.column;
		int colorRGB = pix.colorRGB;

		this.setPixel(row, size - 1 - col, colorRGB);	//	start
		

		for (int t = 0; t < maxIterations; t++) {

			/*colorRGB -= 50;
			this.setPixel(row, size - 1 - col, new Color(colorRGB - 5).brighter().getRGB());*/
		
			Pixel newPix = new Pixel(row,col,colorRGB);
			newPix.setCompVal(z0);
			
			pixelPathList.add(newPix);
			
			if (this.pxConstOperation.equals("Plus")) {
				z0 = z0.power(pwr).plus(constant);
			} else if (this.pxConstOperation.equals("Minus")) {
				z0 = z0.power(pwr).minus(constant);
			} else if (this.pxConstOperation.equals("Multiply")) {
				z0 = z0.power(pwr).times(constant);
			} else if (this.pxConstOperation.equals("Divide")) {
				z0 = z0.power(pwr).divides(constant);
			}
			
			row = (int)z0.real;
			col = (int)z0.imaginary;
			colorRGB -= 50;
			
		}
		/*System.out.println("pixelPathList size is "+pixelPathList.size()
			+"\nfor pix "+pix);*/
		return pixelPathList;
		
//		this.drawPixelPath(pixelPathList);
		
	}
	
	

	private void drawPixelPath(Graphics2D g, List<Pixel> path) {
		System.out.println("in drawPixelPath -- pathSize is "+path.size());
//		for(int i = 0; i <path.size()-2; i++){
//			Pixel p1 = path.get(i);
//			Pixel p2 = path.get(i+1);
//			
//			Point pt1=new Point(p1.row,p1.column);
//			Point pt2=new Point(p2.row,p2.column);
//			int color = p2.colorRGB;
//			g.setColor(new Color(color));
//			this.drawLine(g,pt1,pt2);
//		}

		for (Pixel p : path) {
			this.setPixel(p.row, p.column, p.colorRGB);
		}
		
	}

	private int mand(ComplexNumber z0, int maxIterations, int pwr, ComplexNumber constant, double bd, int r, int c) {
		ComplexNumber z = z0;
		for (int t = 0; t < maxIterations; t++) {
			if (z.abs() > bd)
				return t;

			if (this.pxConstOperation.equals("Plus")) {
				z = z.power(pwr).plus(constant);
			} else if (this.pxConstOperation.equals("Minus")) {
				z = z.power(pwr).minus(constant);
			} else if (this.pxConstOperation.equals("Multiply")) {
				z = z.power(pwr).times(constant);
			} else if (this.pxConstOperation.equals("Divide")) {
				z = z.power(pwr).divides(constant);
			} else if (this.pxConstOperation.equals("Power")) {
				z = z.power(pwr).power(constant);
			}
		}
		if (!this.isBuddha) {
			return maxIterations;
		} else {
			return IM_BUDDHA;//this.populateBuddha(r,c);
		}
		/*return maxIterations;*/
	}

	/*private int populateBuddha(int row, int col) {
		final Pixel key = new Pixel(row, col,IM_BUDDHA);
		Integer value = this.buddhaMap.get(key);
		if (value == null) {
			this.buddhaMap.put(key, 1);
			if(!this.buddhaList.contains(key)){this.buddhaList.add(key);}
		} else {
			System.out.println("ex hit r,c = "+row+","+col);
			value += 1;
			this.buddhaMap.put(key, value);
			
			if(!this.buddhaList.contains(key)){this.buddhaList.add(key);}
		}
		return IM_BUDDHA;
	}*/

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final Mandelbrot frame = new Mandelbrot(2,2,true);//96);//,2);//,true);//(2,3,false);
//				frame.setRunning(true);
				
				/*frame.setBuddha(true);*/
//				frame.depth = 5;
/*				frame.setUseColorPalette(false);
				frame.setUseBlackWhite(true);*/
				frame.setPxXTransformation("absolute");
				frame.setPxYTransformation("absolute");

				frame.setPixXYOperation("Plus");
				
				frame.setTitle(frame.getFractalShapeTitle());
				frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);
				
				new Thread(frame).start();

			}
		});
	}
	

	/*@Override
	public String getFractalDetails() {		
		return this.getFractalShapeTitle();
	}*/

	@Override
	protected String getFractalShapeTitle() {
		return "Bawaz _ Mandelbrot";
	}

	public ComplexNumber getComplex() {
		return this.complex;
	}

	public void setComplex(ComplexNumber comp) {
		this.complex = comp;
	}
	
	/**
	 * @param complex the complex to set
	 */
	public void setComplex(double realVal,double imagVal) {
		this.complex = new ComplexNumber(realVal,imagVal);
	}

	public boolean isBuddha() {
		return this.isBuddha;
	}

	public void setBuddha(boolean isB) {
		this.isBuddha = isB;
	}

	public boolean isMotionBrot() {
		return this.isMotionBrot;
	}

	public void setMotionBrot(boolean isB) {
		this.isMotionBrot = isB;
	}

	public String getMotionParam() {
		return this.motionParam;
	}

	public void setMotionParam(String motParam) {
		this.motionParam = motParam;
	}

	public double getMotionParamJumpVal() {
		return this.motionParamJumpVal;
	}

	public void setMotionParamJumpVal(double mpJumpVal) {
		this.motionParamJumpVal = mpJumpVal;
	}
	
	/**
	 * @param useDiff the useDiff to set
	 */
	public void setUseDiff(boolean useDiff) {
		this.useDiff = useDiff;
	}


	/**
	 * @return the useDiff
	 */
	public boolean isUseDiff() {
		return useDiff;
	}

	
	public boolean isComplexNumConst() {
		return this.isComplexNumConst;
	}

	public void setComplexNumConst(boolean isComplexNumConst) {
		this.isComplexNumConst = isComplexNumConst;
	}

	public int getMagnification() {
		return this.magnification;
	}

	public void setMagnification(int mg) {
		this.magnification = mg;
	}

}
