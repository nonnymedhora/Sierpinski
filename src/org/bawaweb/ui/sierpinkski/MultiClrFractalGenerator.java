/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 * @author Navroz
 *
 */
public abstract class MultiClrFractalGenerator {
	
	public static final int[] FRST_SIX_PRIMES 	= new int[] { 2, 3, 5, 7, 11, 13 };
	public static final int[] POW2_4_200 		= new int[] { 4, 8, 16, 32, 64, 128, 200 };
	
	
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

	private static int[] getVga() {
		String[] v = new String[] { "0", "43520", "11141120", "11184640", "2852126720", "2852170240", "2857697280",
				"2863311360", "1431655680", "1431699200", "1442796800", "1442840320", "4283782400", "4283825920",
				"4294923520", "4294967040", "0", "336860160", "538976256", "741092352", "943208448", "1162167552",
				"1364283648", "1633771776", "1903259904", "2189591040", "2459079168", "2728567296", "3065427456",
				"3419130624", "3823362816", "4294967040", "65280", "1090584320", "2097217280", "3187736320",
				"4278255360", "4278238720", "4278222080", "4278206720", "4278190080", "4282449920", "4286382080",
				"4290641920", "4294901760", "3204382720", "2113863680", "1107230720", "16711680", "16728320",
				"16743680", "16760320", "16776960", "12517120", "8257280", "4325120", "2105409280", "2659057408",
				"3195928320", "3749576448", "4286447360", "4286439168", "4286430720", "4286422528", "4286414080",
				"4288576768", "4290673920", "4292836608", "4294933760", "3758062848", "3204414720", "2667543808",
				"2113895680", "2113904128", "2113912320", "2113920768", "2113928960", "2111831808", "2109669120",
				"2107571968", "3065446144", "3350658816", "3686203136", "3954638592", "4290182912", "4290177792",
				"4290173696", "4290168576", "4290164224", "4291278336", "4292589056", "4293637632", "4294948352",
				"3959404032", "3690968576", "3355424256", "3070211584", "3070215936", "3070221056", "3070225152",
				"3070230272", "3068919552", "3067870976", "3066560256", "28928", "469790976", "939553024", "1426092288",
				"1895854336", "1895847168", "1895839744", "1895832576", "1895825408", "1897660416", "1899495424",
				"1901395968", "1903230976", "1433468928", "946929664", "477167616", "7405568", "7412736", "7419904",
				"7427328", "7434496", "5599488", "3698944", "1863936", "943223040", "1161326848", "1429762304",
				"1631088896", "1899524352", "1899520256", "1899517184", "1899513088", "1899509760", "1900361728",
				"1901410304", "1902196736", "1903245312", "1634809856", "1433483264", "1165047808", "946944000",
				"946947328", "946951424", "946954496", "946958592", "945910016", "945123584", "944075008", "1364291840",
				"1498509568", "1632727296", "1766945024", "1901162752", "1901160704", "1901158656", "1901156608",
				"1901154560", "1901678848", "1902203136", "1902727424", "1903251712", "1769033984", "1634816256",
				"1500598528", "1366380800", "1366382848", "1366384896", "1366386944", "1366388992", "1365864704",
				"1365340416", "1364816128", "16640", "268452096", "536887552", "805323008", "1090535680", "1090531328",
				"1090527232", "1090523136", "1090519040", "1091567616", "1092616192", "1093664768", "1094778880",
				"809566208", "541130752", "272695296", "4259840", "4263936", "4268032", "4272128", "4276480", "3162368",
				"2113792", "1065216", "538984704", "673202432", "807420160", "941637888", "1092632832", "1092630528",
				"1092628480", "1092626432", "1092624384", "1093148672", "1093672960", "1094197248", "1094787072",
				"943792128", "809574400", "675356672", "541138944", "541140992", "541143040", "541145088", "541147392",
				"540557568", "540033280", "539508992", "741097728", "808206592", "875315456", "1009533184",
				"1093419264", "1093417984", "1093415936", "1093414912", "1093413888", "1093676032", "1093938176",
				"1094462464", "1094790144", "1010904064", "876686336", "809577472", "742468608", "742469632",
				"742470656", "742472704", "742473984", "742146304", "741622016", "741359872", "0", "0", "0", "0", "0",
				"0", "0", "0" };
		int[] n = new int[v.length];
		for (int i = 0; i < v.length; i++) {
			n[i] = Integer.parseUnsignedInt(v[i]);
		}
		return n;
	}

	final String PIPE = "\\|";
	private static final int[] vga = getVga();
	private static final int COLORMAXRGB  	= 0xFF;

	abstract int getIterBlowOutCount(ComplexNumber z, FractalDtlInfo opt);
	
	private String colorBlowoutType;
	private String colorChoice;
	
	public MultiClrFractalGenerator(String folderPath, FractalDtlInfo fDInfo) {
		this.path = folderPath;
		this.fDtls = fDInfo;

		this.data = new int[fDInfo.getAreaSize() * fDInfo.getAreaSize()];
		
		double xc = fDInfo.getxC();
		double yc = fDInfo.getyC();
		double size = fDInfo.getName().equals("Mandelbrot") ? fDInfo.getMagnification() : fDInfo.getScaleSize();
		double bd = fDInfo.getBound();
		int max = fDInfo.getMaxIter();
		String func2Apply = fDInfo.getUseFuncConst();
		String pxFunc2Apply = fDInfo.getUseFuncPixel();
		
		int n = fDInfo.getAreaSize();
		int pow = fDInfo.getPower();
		
		this.setRangeSpace(xc, yc, size, n);
		this.data = this.processFractalDetails(this.fDtls);
		
		this.createFractalImages();
	}

	private void createFractalImages() {
		final String[] colorsChoices = this.getColorsChoices();
		final int numClrs = colorsChoices.length;
		Image[] images = new Image[numClrs];

		final String subDirName = this.fDtls.getName() + System.currentTimeMillis();
		final String subDirPath = this.path + File.separator + subDirName;
		File subDir = new File(subDirPath);
		
		if (!subDir.exists()) {
			subDir.mkdir();
			//check-again-for-create
			System.out.println("subDir-did-NOTExist.....created_using_mkdir--now-checking");
			System.out.println("subDir_exists?....."+subDir.exists());
		}
		
		for (int i = 0; i < numClrs; i++) {
			int[] pixelClrData = processPixelClrs(this.data, colorsChoices[i]);
			JPanel p = new JPanel();
			int size = this.fDtls.getAreaSize();
			images[i] = p.createImage(new MemoryImageSource(size, size, pixelClrData, 0, size));
			
			BufferedImage buffy = convertImage(images[i]);
			
			String imageFilePath = subDirPath + File.separator + colorsChoices[i].replace("|", "_") + ".jpg";
			File outputfile = new File(imageFilePath);
			try {
				ImageIO.write(buffy, "jpg", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}

	private int[] processPixelClrs(int[] d, String clrChoice) {
		int[] clrPxls = new int[d.length];
		if (clrChoice.contains(PIPE)) {
			this.setColorChoice((clrChoice.split(PIPE))[0]);
			this.setColorBlowoutType((clrChoice.split(PIPE))[1]);
		} else {
			this.setColorChoice(clrChoice);
		}
		int n = this.fDtls.getAreaSize();
		int pixPt = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				clrPxls[pixPt] = this.getPixelDisplayColor(i, j, d[pixPt],this.fDtls.isUseDiff());

				pixPt += 1;
			}
		}
		return clrPxls;
	}

	private int getPixelDisplayColor(int row, int col, int datum, boolean diff) {
		if (this.colorChoice.equals("ColorGradient6")) {
			double colG6 = datum * 6.0;
			int hI = datum;
			double hF = hI - colG6;
			hF = this.correctColorGr6(hF);
			switch (hI % 6) {
				case 0:		return new Color(1,(int) hF,0).getRGB();
				case 1:		return new Color(1,(int) hF,1,0).getRGB();
				case 2:		return new Color(0,1,(int) hF).getRGB();	
				case 3:		return new Color(0,1,(int) hF,0).getRGB();
				case 4:		return new Color((int) hF,0,1).getRGB();
				case 5:		return new Color(1,0,1,(int) hF).getRGB();				

			}

		}
		
		if(this.colorChoice.equals("ColorBlowout")){
			return new Color(this.time2Color(datum)).getRGB();
		}
		
		if(this.colorChoice.equals("ColorSuperBlowout")){
			return this.getColorSuperBlowout(datum).getRGB();
		}
		
		final int[] divs = FRST_SIX_PRIMES;				//	1st 6 primes
		final int[] colStart = POW2_4_200;
		Color color = null;
		for(int iter = 0; iter < divs.length; iter++) {
			if (datum % divs[iter] == 0) {
				if (this.colorChoice.equals("ColorPalette")) {
					if (diff) {
						color = ColorPalette[iter];
					} else {
						color = ColorPalette[ColorPalette.length - iter - 1];
					}
				} else if(this.colorChoice.equals("BlackWhite")) {
					if (diff) {
						color = BlackWhitePalette[iter];
					} else {
						color = BlackWhitePalette[BlackWhitePalette.length - iter - 1];
					}
				} else if(this.colorChoice.equals("ComputeColor")) {

					final int start = colStart[iter];
					final int end = COLORMAXRGB - start;

					int num1 = correctColor(start, datum, row, divs[iter]);
					int num2 = correctColor(start, datum, col, divs[iter]);

					if (diff) {
						color = new Color(num1, num2, start);
					} else {
						color = new Color(num1, num2, end);
					}
				}
				
				if (color != null)
					return color.getRGB();
				else
					return 0;
			}
			
		}

		if (this.colorChoice.equals("ColorPalette")) {
			if (diff) {
				color = ColorPalette[4];
			} else {
				color = ColorPalette[ColorPalette.length - 5];
			}
			if (color != null)
				return color.getRGB();
			else
				return 0;
		}

		if (this.colorChoice.equals("BlackWhite")) {
			if (diff) {
				color = BlackWhitePalette[4];
			} else {
				color = BlackWhitePalette[BlackWhitePalette.length - 5];
			}
			if (color != null)
				return color.getRGB();
			else
				return 0;
		}
		
		if (this.colorChoice.equals("ComputeColor")) {
			final int start = 200; // 255 is max.	0<=colorRGB<=255
			final int end = COLORMAXRGB - start;
			int num1 = correctColor(start, datum, row, 1);
			int num2 = correctColor(start, datum, col, 1);
			if (diff) {
				color = new Color(num1, num2, start);
			} else {
				color = new Color(num1, num2, end);
			}

			return color.getRGB();
		}
		return datum;
		
		/*if (color != null)
			return color.getRGB();
		else
			return 0;*/
	}

	private int[] processFractalDetails(FractalDtlInfo opt) {
		this.fDtls = opt;

		double xCenter = opt.getxC();
		double yCenter = opt.getyC();
		double scaleSize = opt.getScaleSize();

		int n = opt.getAreaSize(); // create n-by-n image
		int maxIter = opt.getMaxIter(); // maximum number of iterations

		this.resetFractal(n);

		int pixPt = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				double x0 = xCenter - scaleSize / 2 + scaleSize * i / n;
				double y0 = yCenter - scaleSize / 2 + scaleSize * j / n;

				ComplexNumber z0 = this.createZValue(x0, y0);

				final int iterBlowoutCount = this.getIterBlowOutCount(z0, opt);
				this.data[pixPt] = iterBlowoutCount;
				/*int clrVal = maxIter - iterBlowoutCount;

				clrVal = checkColor(clrVal);
				Color color = new Color(clrVal, clrVal, clrVal);

				this.data[pixPt] = color.getRGB();*/
				pixPt += 1;
			}
		}
		return this.data;

	}
	
	protected BufferedImage convertImage(Image original) {
        ColorModel cm = ColorModel.getRGBdefault();
        int width = original.getWidth(null);
        int height = original.getHeight(null);
        BufferedImage image = new BufferedImage (
        							cm, 
        							cm.createCompatibleWritableRaster(width, height),
        							cm.isAlphaPremultiplied(), 
        							null);
        Graphics2D bg = image.createGraphics();
        bg.drawImage(original, 0, 0, width, height, null);
        bg.dispose();
        
        return image;
    }
	

	protected int checkColor(int grey) {
		return grey = grey < 0 ? 0 : (grey > 255 ? 255 : grey);
	}
	

	private void resetFractal(int size) {
		this.data = new int[size * size];
	}

	protected String path;
	protected FractalDtlInfo fDtls;
	
	protected int[] data = new int[500 * 500];		//fDtls.getAreaSize()
	
	
	protected double x_min;
	protected double y_min;
	protected double x_max;
	protected double y_max;
	
	protected void setRangeSpace(double xc, double yc, double size, int n) {
		x_min = xc - size / 2;
		y_min = yc - size / 2;
		x_max = xc + size / 2;//xc - size / 2 + size * (n - 1) / n;
		y_max = yc + size / 2;//yc - size / 2 + size * (n - 1) / n;
	}
	
	private ComplexNumber applyIntraPixelOperation(double x, double y) {
		ComplexNumber z = null;
		switch(this.fDtls.getPixXYOperation()){
			case	"Plus":		z = new ComplexNumber(x,y);	break;
			case	"Minus":	z = new ComplexNumber(x,y*-1.0);	break;
			case	"Multiply":	z = new ComplexNumber(x,0.0).times(new	ComplexNumber(0.0,y));	break;
			case	"Divide":	z = new ComplexNumber(x,0.0).divides(new	ComplexNumber(0.0,y));	break;
			case	"PowerX^Y":	z = new ComplexNumber(x,0.0).power(/*(int)*/y);	break;
			case	"PowerY^X":	z = new ComplexNumber(y,0.0).power(/*(int)*/x);	break;
			default:	z = new ComplexNumber(x,y);	break;	
		}
		return z;
	}

	private double applyPixelXPointTransformation(double pix) {
		switch (this.fDtls.getPxXTransformation()) {
		case "none":
			break;
		case "absolute":
			pix = Math.abs(pix);
			break;
		case "absoluteSquare":
			pix = Math.pow(Math.abs(pix), 2);
			break;
		case "reciprocal":
			pix = 1.0 / (pix);
			break;
		case "reciprocalSquare":
			pix = Math.pow((1.0 / (pix)), 2);
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
			pix = 1 / Math.sin(pix);
			break;
		case "sec":
			pix = 1 / Math.cos(pix);
			break;
		case "cot":
			pix = 1 / Math.tan(pix);
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
			pix = 1 / Math.sinh(pix);
			break;
		case "arccosh":
			pix = 1 / Math.cosh(pix);
			break;
		case "arctanh":
			pix = 1 / Math.tanh(pix);
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
		switch (this.fDtls.getPxYTransformation()) {
		case "none":
			break;
		case "absolute":
			pix = Math.abs(pix);
			break;
		case "absoluteSquare":
			pix = Math.pow(Math.abs(pix), 2);
			break;
		case "reciprocal":
			pix = 1.0 / (pix);
			break;
		case "reciprocalSquare":
			pix = Math.pow((1.0 / (pix)), 2);
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
			pix = 1 / Math.sin(pix);
			break;
		case "sec":
			pix = 1 / Math.cos(pix);
			break;
		case "cot":
			pix = 1 / Math.tan(pix);
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
			pix = 1 / Math.sinh(pix);
			break;
		case "arccosh":
			pix = 1 / Math.cosh(pix);
			break;
		case "arctanh":
			pix = 1 / Math.tanh(pix);
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

	protected ComplexNumber createZValue(double x, double y) {
		x = this.applyPixelXPointTransformation(x);
		y = this.applyPixelYPointTransformation(y);

		return this.applyIntraPixelOperation(x, y);
	}

	protected ComplexNumber getZValue(ComplexNumber zConstant, ComplexNumber z, FractalDtlInfo opt) {
		double exp = opt.getPower();
		String zCOp = opt.getPxConstOperation();
		
		z = processForZExponent(z,exp);
		z = processForZCOperation(z,zCOp,zConstant);
		return z;//z.times(z).plus(zConstant);
	}

	private ComplexNumber processForZExponent(ComplexNumber z, double exp) {
		int pow = (int) exp;
		for (int i = 1; i < pow; i++)
			z = z.times(z);
		return z;
	}
	
	private ComplexNumber processForZCOperation(ComplexNumber z, String op, ComplexNumber zConstant) {
		/*private static final String[] PIX_CONST_OPRNS = new String[] 
		 * {"Plus","Minus","Multiply","Divide","PowerZ^C","PowerC^Z"};*/
		switch (op) {
			case "Plus":
				z = z.plus(zConstant);
				break;
			case "Minus":
				z = z.minus(zConstant);
				break;
			case "Multiply":
				z = z.times(zConstant);
				break;
			case "Divide":
				z = z.divides(zConstant);
				break;
			case "PowerZ^C":
				z = z.power(zConstant);
				break;
			case "PowerC^Z":
				z = zConstant.power(z);
				break;				
				
			default:	//"Plus"
				z = z.plus(zConstant);
		}
		return z;
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
			if (Math.abs(n) < COLORMAXRGB)
				return new Color(vga[Math.abs(n)]);
			return new Color(vga[Math.abs(n) % COLORMAXRGB]);
			
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
    
    private int time2Color(int n) {
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
    
    
    public int sum3colors(int n, int n2, int n3) {
        int n4 = n + n2 + n3;
        if (n4 > COLORMAXRGB) return COLORMAXRGB;
        return n4;
    }

	public String getColorBlowoutType() {
		return this.colorBlowoutType;
	}

	public void setColorBlowoutType(String type) {
		this.colorBlowoutType = type;
	}
    
	public String getColorChoice() {
		return this.colorChoice;
	}

	public void setColorChoice(String clrChoice) {
		this.colorChoice = clrChoice;
	}
	
	protected double correctColorGr6(double val) {
		double corrected = val;
		if (corrected < 0)
			corrected = -1 * corrected;
		corrected = corrected > COLORMAXRGB ? COLORMAXRGB : corrected;
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
	
	private String[] getColorsChoices() {
		String[] colr_options_no_sampl = new String[] { "BlackWhite", "ColorPalette", "ComputeColor", "ColorGradient6",
				"ColorBlowout|Default", "ColorBlowout|Snowy", "ColorBlowout|Pink", "ColorBlowout|Matrix",
				"ColorSuperBlowout|Original", "ColorSuperBlowout|Fire", "ColorSuperBlowout|Black & White",
				"ColorSuperBlowout|Electric Blue", "ColorSuperBlowout|Toon", "ColorSuperBlowout|Gold",
				"ColorSuperBlowout|Classic VGA", "ColorSuperBlowout|CGA 1", "ColorSuperBlowout|CGA 2",
				"ColorSuperBlowout|Primary (RGB)", "ColorSuperBlowout|Secondary (CMY)", "ColorSuperBlowout|Tertiary 1",
				"ColorSuperBlowout|Tertiary 2", "ColorSuperBlowout|Neon" };

		return (colr_options_no_sampl);

	}
	

}
