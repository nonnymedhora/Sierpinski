/**
 * 
 */
package org.bawaweb.ui.sierpinkski;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.bawaweb.ui.sierpinkski.FractalBase.ComplexNumber;

/**
 * @author Navroz
 *
 */

class SierpinskiComboPanel extends JPanel {
	
	private static final long serialVersionUID = 156478L;
	
	private static final Double[] BOUNDARIES = new Double[] { -5.0, -4.5, -4.0, -3.5, -3.0, -2.5, -2.0, -1.5, -1.0, 0.0, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private static final Integer[] EXPONENTS = new Integer[] { -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private static final Integer[] MAX_ITERATIONS = new Integer[] { 10, 50, 100, 200, 225, 255, 300, 350, 400, 450, 500, 600, 700, 800, 900, 1000, 2000, 3000, 5000, 7500, 10000 };
	private static final Integer[] AREA_SIZES = new Integer[] { 10, 50, 100, 200, 225, 255, 500, 512, 599, 800 };
	private static final Double[] CENTER_XY = new Double[] { -5.0, -4.5, -4.0, -3.5, -3.0, -2.5, -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0 };
	private static final Double[] SCALE_SIZES = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private static final String[] POLY_RCMT_TYPES = new String[] { "Reverse", "Exchange", "Single", "Duplicate", "Exponent", "Power", "Default" };
	private static final Integer[] FANNY_RATIOS = new Integer[] { 1, 2, 3, 4, 5, 6, 7 };
	private static final Integer[] FANNY_SIZE_OPTIONS = new Integer[] { 50, 60, 70, 80, 90, 100, 120, 150, 170, 200, 250, 300, 350 };
	private static final Integer[] APOLLO_MULTS = new Integer[]{ 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
	private static final Integer[] APOLLO_CURVES = new Integer[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
	
	// for Julia
	private static final String J1 = "P[2] C[0.279]";	//f(z) = z^2 + 0.279
	private static final String J2 = "P[3] C[0.4]";		//f(z) = z^3 + 0.400
	private static final String J3 = "P[4] C[0.484]";	//f(z) = z^4 + 0.484
	private static final String J4 = "P[5] C[0.544]";	//f(z) = z^5 + 0.544
	private static final String J5 = "P[6] C[0.59]";	//f(z) = z^6 + 0.590
	private static final String J6 = "P[7] C[0.626]";	//f(z) = z^7 + 0.626
	// extra for Julia
	private static final String J7 = "P[2] C1";//[-0.74543+0.11301*i]";	//f(z) = z^2 + ...
	private static final String J8 = "P[2] C2";//[-0.75+0.11*i]";	//f(z) = z^2 + ....
	private static final String J9 = "P[2] C3";//[-0.1+0.651*i]";	//f(z) = z^2 + ...
	
	
	//	for	ApollonianCircles
	// curvature & multiplier options for @see ApollonianCircles
	private static final String A6 = "C[2, 2, 3] M[1]";
	private static final String A5 = "C[23, 27, 18] M[16]";
	private static final String A4 = "C[10, 15, 19] M[6]";
	private static final String A3 = "C[5, 8, 8] M[6]";
	private static final String A2 = "C[25, 25, 28] M[20]";
	private static final String A1 = "C[1, 1, 1] M[1]";

	// Fractal Art Choices
	private static final String SIERPINSKI_SQUARES 		= "SierpinskiSquares";
	private static final String APOLLONIAN_CIRCLES 		= "ApollonianCircles";
	private static final String SIERPINSKI_TRIANGLES 	= "SierpinskiTriangles";
	private static final String CST_FRACTAL 			= "CSTFractal";
	private static final String SAMPLE 					= "Sample";
	private static final String MANDELBROT 				= "Mandelbrot";
	private static final String JULIA 					= "Julia";
	private static final String FANNY_TRIANGLES 		= "FannyTriangles";
	private static final String FANNY_CIRCLE 			= "FannyCircle";
	private static final String KOCHSNOWFLAKE 			= "KochSnowFlake";
	private static final String DIY 					= "Do_It_Yourself";
	private static final String POLY 					= "Poly_Fractals";

	
	private final String[] comboOptions = new String[]{ DIY, FANNY_CIRCLE, FANNY_TRIANGLES, SIERPINSKI_TRIANGLES, SIERPINSKI_SQUARES, APOLLONIAN_CIRCLES, CST_FRACTAL, SAMPLE, MANDELBROT, JULIA, KOCHSNOWFLAKE, POLY };
	private final JComboBox<String> combos = new JComboBox<String>(comboOptions);
	
	//	for	FannyCircle & FannyTriangles
	private final Integer[] sideOptions = FANNY_SIZE_OPTIONS;
	private final JComboBox<Integer> sideCombos = new JComboBox<Integer>(sideOptions);
	private final Integer[] ratioOptions = FANNY_RATIOS;
	private final JComboBox<Integer> ratioCombos = new JComboBox<Integer>(ratioOptions);
	
	// for SierpinskiTraingles
	private final JCheckBox sierpinskiTFillInnerCb = new JCheckBox("FillInnerTriangles", true);
	private boolean sierpinskiFillInnerT = false;
	
	protected JRadioButton 	sierpTUpRb 	= new JRadioButton("UP", false);
	protected JRadioButton 	sierpTDnRb 	= new JRadioButton("DOWN", true);	
	protected ButtonGroup 	sierpTBg 	= new ButtonGroup();
	private String 			sierpTDir = "UP";
	
	// for KochSnowFlakeFractal
	protected final JCheckBox kochFillExternalCb = new JCheckBox("FillOuterTriangles",false);
	protected final JCheckBox kochMixColorsCb = new JCheckBox("MixColors",false);
	protected final JCheckBox kochSpreadOuterCb = new JCheckBox("SpreadOuter",false);
	
	
	private boolean kochFillExternals = false;
	private boolean kochMixColors = false;
	private boolean kochSpreadOuter = false;
	
	// for Julia
	private final String[] juliaOptions = new String[] { J1, J2, J3, J4, J5, J6, J7, J8, J9 };
	private final JComboBox<String> juliaCombos = new JComboBox<String>(juliaOptions);
	private final JCheckBox juliaUseDiffCb = new JCheckBox("UseDifferencesOnly", true);
	private final Integer[] juliaMaxIterOptions = MAX_ITERATIONS;
	private final JComboBox<Integer> juliaMaxIterCombos = new JComboBox<Integer>(juliaMaxIterOptions);
	private final Integer[] juliaSizeOptions = AREA_SIZES;
	private final JComboBox<Integer> juliaSizeCombos = new JComboBox<Integer>(juliaSizeOptions);
	private final Double[] juliaBoundOptions = BOUNDARIES;
	private final JComboBox<Double> juliaBoundCombos = new JComboBox<Double>(juliaBoundOptions);
	
	private final Double[] juliaXCOptions = CENTER_XY;
	private final JComboBox<Double> juliaXCCombos = new JComboBox<Double>(juliaXCOptions);
	private final Double[] juliaYCOptions = CENTER_XY;
	private final JComboBox<Double> juliaYCCombos = new JComboBox<Double>(juliaYCOptions);
	private final Double[] juliaScaleSizeOptions = SCALE_SIZES;
	private final JComboBox<Double> juliaScaleSizeCombos = new JComboBox<Double>(juliaScaleSizeOptions);
	
	private ButtonGroup  juliaFieldLinesBG 		= new ButtonGroup();
	private JRadioButton juliaFieldNoneRB 		= new JRadioButton("None", true);
	private JRadioButton juliaFieldFatouRB 		= new JRadioButton("Fatou", false);
	private JRadioButton juliaFieldZSqRB 		= new JRadioButton("Z-Squared", false);
	private JRadioButton juliaFieldClassicRB 	= new JRadioButton("ClassicJ", false);
	

	// for Mandelbrot
	private final Integer[] mandOptions = EXPONENTS;	//TODO	-	ok for now
	private final JComboBox<Integer> mandCombos = new JComboBox<Integer>(mandOptions);
	private final Integer[] mandExpOptions = EXPONENTS;
	private final JComboBox<Integer> mandExpCombos = new JComboBox<Integer>(mandExpOptions);
	private final JCheckBox mandUseDiffCb = new JCheckBox("UseDifferencesOnly", true);
	private final Integer[] mandMaxIterOptions = MAX_ITERATIONS;
			
	private final JComboBox<Integer> mandMaxIterCombos = new JComboBox<Integer>(mandMaxIterOptions);
	private final Integer[] mandSizeOptions = AREA_SIZES;
	private final JComboBox<Integer> mandSizeCombos = new JComboBox<Integer>(mandSizeOptions);
	private final Double[] mandBoundOptions = BOUNDARIES;
	private final JComboBox<Double> mandBoundCombos = new JComboBox<Double>(mandBoundOptions);


	private final Double[] mandXCOptions = CENTER_XY;
	private final JComboBox<Double> mandXCCombos = new JComboBox<Double>(mandXCOptions);
	private final Double[] mandYCOptions = CENTER_XY;
	private final JComboBox<Double> mandYCCombos = new JComboBox<Double>(mandYCOptions);

	private final Double[] mandScaleSizeOptions = SCALE_SIZES;
	private final JComboBox<Double> mandScaleSizeCombos = new JComboBox<Double>(mandScaleSizeOptions);
	
	/*protected JRadioButton mandColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton mandColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup mandColrBg = new ButtonGroup();*/
	
	//	for	Apollo
	private final String[] curvOptions = new String[] { A1, A2, A3, A4, A5, A6 };
	private final JComboBox<String> curvCombos = new JComboBox<String>(curvOptions);

	/*protected JRadioButton apolloColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton apolloColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup apolloColrBg = new ButtonGroup();*/

	// Fractal Art Options
	private JPanel fannyOptionsPanel 	= new JPanel(new FlowLayout(),true);
	private JPanel apolloOptionsPanel 	= new JPanel(new FlowLayout(),true);
	private JPanel sierpinskiTPanel 	= new JPanel(new FlowLayout(),true);	
	private JPanel kochSnowFlakePanel 	= new JPanel(new FlowLayout(),true);	
	
	private JPanel juliaOptionsPanel 	= new JPanel(new GridLayout(/*4,8*/10,5),true);
	private JPanel mandOptionsPanel 	= new JPanel(new GridLayout(10,5),true);
	private JPanel polyOptionsPanel 	= new JPanel(new GridLayout(10,5),true);
	private JPanel diyOptionsPanel		= new JPanel(new FlowLayout(),true);//GridLayout(4,7),true);
	
	private JPanel diyMandPanel 		= new JPanel(new GridLayout(5,8),true);
	private JPanel diyJuliaPanel 		= new JPanel(new GridLayout(5,8),true);
	private JPanel diyApolloPanel 		= new JPanel(new GridLayout(4,8),true);
	
	private JTextArea formulaArea = new JTextArea(5,20);
	//private JTextPane formulaPane;
	
	private final JButton buStart = new JButton("Start |>");
	private final JButton buPause = new JButton("Pause ||");
	private final JButton buClose = new JButton("CLOSEImage");
	
	// for running sub-implementation of FractalBase
	protected String comboChoice;

	// FannyCircle & FannyTriangles	--	side Length & ratio
	protected int sideChoice;	
	protected int ratioChoice;
	
	// for ApollonianCircles -- curvatures & multiplier options
	protected double[] curvChoices;
	protected double mult;
	
	// for Julia -- power & constant
	protected int power;
	protected double compConst;
	protected String complex;
	protected String juliaSelection;
	protected boolean jUseDiff;			
	protected int juliaMaxIter;
	protected int juliaSize;			
	protected double juliaBound;
	
	protected double juliaXC;
	protected double juliaYC;
	protected double juliaScaleSize;	
	
	
	
	// for mandelbrot
	protected int magnification;
	protected int exponent;
	protected boolean mUseDiff;
	protected int mandMaxIter;
	protected int mandSize;			
	protected double mandBound;

	
	protected double mandXC;
	protected double mandYC;
	protected double mandScaleSize;	
	
	// for DIY
	//radioButton
	protected JRadioButton diyMandRb = new JRadioButton(MANDELBROT,true);
	protected JRadioButton diyJuliaRb = new JRadioButton(JULIA);
	protected JRadioButton diyApolloRb = new JRadioButton(APOLLONIAN_CIRCLES);
	
	protected ButtonGroup diyBg = new ButtonGroup();
	
	// diy fractal art option choice
	private final String diyMand = "DIY_"+MANDELBROT;
	private final String diyJulia = "DIY_"+JULIA;
	private final String diyApollo = "DIY_"+APOLLONIAN_CIRCLES;

	private String diyFractChoice;
	
	//diy Mandelbrot options
	protected int diyMandMagnification;		//v	mandOptions
	protected int diyMandExponent;			//v	mandExpOptions
	protected double diyMandConstReal;
	protected double diyMandConstImg;
	protected boolean diyMandUseDiff;		//v	mandUseDiffCb
	protected boolean diyMandKeepConst;
	protected int diyMandMaxIter;
	protected int diyMandSize;
	protected double diyMandBound;
	
	protected double diyMandXC;
	protected double diyMandYC;
	protected double diyMandScaleSize;
	
	private final Integer[] diyMandMagOptions = EXPONENTS;
	private final JComboBox<Integer> diyMandMagCombos = new JComboBox<Integer>(diyMandMagOptions);
	private final Integer[] diyMandExpOptions = EXPONENTS;
	private final JComboBox<Integer> diyMandExpCombos = new JComboBox<Integer>(diyMandExpOptions);
	private final JCheckBox diyMandUseDiffCb = new JCheckBox("UseDifferencesOnly",true);
	private JTextField diyMandRealTf = new JTextField(5);
	private JTextField diyMandImgTf = new JTextField(5);	

	private JRadioButton diyMandelbrotKeepConstRb = new JRadioButton("Dynamic Constant -- C = z", true);
	private JRadioButton diyMandelbrotCreateConstRb = new JRadioButton("Generate Constant");
	private ButtonGroup diyMandelbrotConstBg = new ButtonGroup();
	
	private final Integer[] diyMandMaxIterOptions = MAX_ITERATIONS;
	private final JComboBox<Integer> diyMandMaxIterCombos = new JComboBox<Integer>(diyMandMaxIterOptions);
	
	private final Integer[] diyMandSizeOptions = AREA_SIZES;
	private final JComboBox<Integer> diyMandSizeCombos = new JComboBox<Integer>(diyMandSizeOptions);
	
	private final Double[] diyMandBoundOptions = BOUNDARIES;
	private final JComboBox<Double> diyMandBoundCombos = new JComboBox<Double>(diyMandBoundOptions);
	
	private final Double[] diyMandXCOptions = CENTER_XY;
	private final JComboBox<Double> diyMandXCCombos = new JComboBox<Double>(diyMandXCOptions);
	private final Double[] diyMandYCOptions = CENTER_XY;
	private final JComboBox<Double> diyMandYCCombos = new JComboBox<Double>(diyMandYCOptions);

	private final Double[] diyMandScaleSizeOptions = SCALE_SIZES;
	private final JComboBox<Double> diyMandScaleSizeCombos = new JComboBox<Double>(diyMandScaleSizeOptions);
	
	/*protected JRadioButton diyMandColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton diyMandColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup diyMandColrBg = new ButtonGroup();*/
	
	//diy Julia options
	protected int diyJuliaPower;
	protected double diyJuliaConstReal;
	protected double diyJuliaConstImg;
	protected boolean diyJuliaUseDiff;
	protected boolean diyJuliaKeepConst;
	protected boolean diyJuliaUseSineCalc;
	protected int diyJuliaMaxIter;
	protected double diyJuliaBound;

	protected int diyJuliaSize;
	
	protected double diyJuliaXC;
	protected double diyJuliaYC;
	protected double diyJuliaScaleSize;
	
	private final Integer[] diyJuliaPowerOptions = EXPONENTS;
	private final JComboBox<Integer> diyJuliaPowerCombos = new JComboBox<Integer>(diyJuliaPowerOptions);
	private final JCheckBox diyJuliaUseDiffCb = new JCheckBox("UseDifferencesOnly",true);
	

	private ButtonGroup diyJuliaFieldLinesBG = new ButtonGroup();
	private JRadioButton diyJuliaFieldNoneRB = new JRadioButton("None", true);
	private JRadioButton diyJuliaFieldFatouRB = new JRadioButton("Fatou", false);
	private JRadioButton diyJuliaFieldZSqRB = new JRadioButton("Z-Squared", false);
	private JRadioButton diyJuliaFieldClassicRB = new JRadioButton("Classic", false);
	
	private JTextField diyJuliaRealTf = new JTextField(5);
	private JTextField diyJuliaImgTf = new JTextField(5);

	private JRadioButton diyJuliaKeepConstRb = new JRadioButton("Dynamic Constant -- C = z", true);
	private JRadioButton diyJuliaCreateConstRb = new JRadioButton("Generate Constant");
	private ButtonGroup diyJuliaConstBg = new ButtonGroup();

	private final Integer[] diyJuliaMaxIterOptions = MAX_ITERATIONS;
	private final JComboBox<Integer> diyJuliaMaxIterCombos = new JComboBox<Integer>(diyJuliaMaxIterOptions);	

	private final Integer[] diyJuliaSizeOptions = AREA_SIZES;
	private final JComboBox<Integer> diyJuliaSizeCombos = new JComboBox<Integer>(diyJuliaSizeOptions);	
	
	private final Double[] diyJuliaBoundOptions = BOUNDARIES;
	private final JComboBox<Double> diyJuliaBoundCombos = new JComboBox<Double>(diyJuliaBoundOptions);

	private final Double[] diyJuliaXCOptions = CENTER_XY;
	private final JComboBox<Double> diyJuliaXCCombos = new JComboBox<Double>(diyJuliaXCOptions);
	private final Double[] diyJuliaYCOptions = CENTER_XY;
	private final JComboBox<Double> diyJuliaYCCombos = new JComboBox<Double>(diyJuliaYCOptions);

	private final Double[] diyJuliaScaleSizeOptions = SCALE_SIZES;
	private final JComboBox<Double> diyJuliaScaleSizeCombos = new JComboBox<Double>(diyJuliaScaleSizeOptions);
	
	/*protected JRadioButton diyJuliaColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton diyJuliaColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup diyJuliaColrBg = new ButtonGroup();*/
	
	//diy Apollo Options
	protected int diyApolloC1;
	protected int diyApolloC2;
	protected int diyApolloC3;
	protected int diyApolloMult;
	

	private final Integer[] diyApolloC1Options = APOLLO_CURVES;
	private final JComboBox<Integer> diyApolloC1Combos = new JComboBox<Integer>(diyApolloC1Options);
	private final Integer[] diyApolloC2Options = APOLLO_CURVES;
	private final JComboBox<Integer> diyApolloC2Combos = new JComboBox<Integer>(diyApolloC1Options);	
	private final Integer[] diyApolloC3Options = APOLLO_CURVES;
	private final JComboBox<Integer> diyApolloC3Combos = new JComboBox<Integer>(diyApolloC1Options);
	
	private final Integer[] diyApolloMultOptions = APOLLO_MULTS;
	private final JComboBox<Integer> diyApolloMultCombos = new JComboBox<Integer>(diyApolloMultOptions);
	
	/*protected JRadioButton diyApolloColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton diyApolloColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup diyApolloColrBg = new ButtonGroup();*/
	
	
	//POLY
	private final Integer[] polyExpOptions = EXPONENTS;
	private final JComboBox<Integer> polyExpCombos = new JComboBox<Integer>(polyExpOptions);
	private final JCheckBox polyUseDiffCb = new JCheckBox("UseDifferencesOnly",true);
	private final Integer[] polyMaxIterOptions = MAX_ITERATIONS;
	private final JComboBox<Integer> polyMaxIterCombos = new JComboBox<Integer>(polyMaxIterOptions);
	private final Integer[] polySizeOptions = AREA_SIZES;
	private final JComboBox<Integer> polySizeCombos = new JComboBox<Integer>(polySizeOptions);
	private final Double[] polyBoundOptions = BOUNDARIES;
	private final JComboBox<Double> polyBoundCombos = new JComboBox<Double>(polyBoundOptions);
	private final Double[] polyXCOptions = CENTER_XY;
	private final JComboBox<Double> polyXCCombos = new JComboBox<Double>(polyXCOptions);
	private final Double[] polyYCOptions = CENTER_XY;
	private final JComboBox<Double> polyYCCombos = new JComboBox<Double>(polyYCOptions);

	private final Double[] polyScaleSizeOptions = SCALE_SIZES;
	private final JComboBox<Double> polyScaleSizeCombos = new JComboBox<Double>(polyScaleSizeOptions);
	private final String[] polyTypeOptions = POLY_RCMT_TYPES;
	private final JComboBox<String> polyTypeCombos = new JComboBox<String>(polyTypeOptions);
	private JTextField polyRealTf = new JTextField(5);
	private JTextField polyImgTf = new JTextField(5);

	private JRadioButton polyKeepConstRb = new JRadioButton("Dynamic Constant -- C = z", true);
	private JRadioButton polyCreateConstRb = new JRadioButton("Generate Constant");
	private ButtonGroup polyConstBg = new ButtonGroup();

	
	//variables_for_poly
	protected int polyPower;
	protected boolean polyUseDiff;
	protected int polyMaxIter;
	protected boolean polyKeepConst;
	protected int polySize;			
	protected double polyBound;
	protected String polyType = "Reverse";

	
	protected double polyXC;
	protected double polyYC;
	protected double polyScaleSize;
	
	
	/////////
	private boolean keepConst = true;
	private boolean applyFatou = false;
	private boolean applyZSq = false;
	private boolean applyClassicJulia = false;
	
	////////
	
	
	//controlPanel
	private JPanel controlPanel = new JPanel();
	private final JComboBox<Integer> expCombos = new JComboBox<Integer>(EXPONENTS);
	private final JCheckBox useDiff = new JCheckBox("UseDifferencesOnly", true);
	
	@SuppressWarnings("unused")
	private boolean useColorPalette = false;
	private boolean useBlackAndWhite = false;
	
	private double rotation = 0.0;
	
	private JLabel rotLabel = new JLabel("Rotation:");
	private Vector<Double> rotOptions = new Vector<Double>();
	protected JComboBox<Double> rotateCombo ;
	
	protected JRadioButton colrPRb = new JRadioButton("Use Color Palette", false);
	protected JRadioButton colrCRb = new JRadioButton("Compute Color", true);
	protected JRadioButton colrBwRb = new JRadioButton("Black_&_White", false);
	
	protected ButtonGroup colrBg = new ButtonGroup();	
	
	protected JRadioButton noFuncRb 	= new JRadioButton("None",true);
	protected JRadioButton sineRb 		= new JRadioButton("sine",false);
	protected JRadioButton cosineRb 	= new JRadioButton("cosine",false);
	protected JRadioButton tanRb 		= new JRadioButton("tan",false);	
	protected JRadioButton arcSineRb 	= new JRadioButton("arcsine",false);
	protected JRadioButton arcCosineRb 	= new JRadioButton("arccosine",false);
	protected JRadioButton arcTanRb 	= new JRadioButton("arctan",false);		
	protected JRadioButton sqRb 		= new JRadioButton("square",false);	
	protected JRadioButton cuRb 		= new JRadioButton("cube",false);
	protected JRadioButton expRb 		= new JRadioButton("exponent(e)",false);
	protected JRadioButton rootRb 		= new JRadioButton("root",false);
	/*protected JRadioButton cuRootRb = new JRadioButton("cube-root",false);*/
	protected JRadioButton lnRb 		= new JRadioButton("log(e)",false);	
	
	protected ButtonGroup constFnBg = new ButtonGroup();	
	private String constFuncChoice = "None";
	
	private final JCheckBox magnifyCb = new JCheckBox("Magnify",false);
	private boolean doMagnify = false;
	
	private BufferedImage fractalImage;
	private FractalBase fBase;
	
	private JButton buPrint = new JButton("Print");
	private JButton buSave = new JButton("Save");
	
	private Thread fbf;

	private String fieldLines = "None";
	
	public SierpinskiComboPanel() {
		super();
		this.add(new JLabel("Choose: "));
		this.add(this.combos);

		this.createActionControls();

		this.createPanels();
		this.setLayout(new FlowLayout());

		this.setComboSelections();		
		
		this.setUpListeners();		
	}
	
	private void createActionControls() {

		// creates-color-choice-options
		this.createColorChoiceRBs();
		
		// creates-rototation-choice-options -- does not add
		this.createRotationCombo();
		
		// creates-functional-choices-for-constants
		this.createConstantFunctionsRBs();

		this.rotLabel.setVisible(false);
		this.rotateCombo.setVisible(false);
		this.add(this.rotLabel);
		this.add(this.rotateCombo);

		this.buStart.setEnabled(false);
		this.add(this.buStart);
		// this.add(this.buPause);
		this.add(this.buPrint);
		this.add(this.buSave);
		this.buClose.setEnabled(false);
		this.add(this.buClose);

		this.formulaArea.setVisible(false);
		this.add(this.formulaArea);

	}

	private void createPanels() {
		//	fanny	--	does add
		this.createFannyPanel();
		
		//sierpisnkiT
		this.createSierpinskiTPanel();
		
		//koch
		this.createKochSnowFlakePanel();
		
		//apollo	--	does add
		this.createApolloPanel();
		
		//julia		--	does add
		this.createJuliaPanel();
		
		//mandelbrot	--	does add
		this.createMandelbrotPanel();
		
		//poly -- does add
		this.createPolyPanel();		
		
		//	diy	panel	--	does add
		this.createDIYPanel();
	}
	
	private void createConstantFunctionsRBs(){
		this.constFnBg.add(this.noFuncRb);
		this.constFnBg.add(this.sineRb);
		this.constFnBg.add(this.cosineRb);
		this.constFnBg.add(this.tanRb);
		this.constFnBg.add(this.arcSineRb);
		this.constFnBg.add(this.arcCosineRb);
		this.constFnBg.add(this.arcTanRb);
		
		this.constFnBg.add(this.sqRb);
		this.constFnBg.add(this.cuRb);
		this.constFnBg.add(this.expRb);
		this.constFnBg.add(this.rootRb);
		/*this.constFnBg.add(this.cuRootRb);*/
		this.constFnBg.add(this.lnRb);
		
		this.noFuncRb.setActionCommand("NoCalc");
		this.sineRb.setActionCommand("SineCalc");
		this.cosineRb.setActionCommand("CosineCalc");
		this.tanRb.setActionCommand("TanCalc");
		this.arcSineRb.setActionCommand("ArcSineCalc");
		this.arcCosineRb.setActionCommand("ArcCosineCalc");
		this.arcTanRb.setActionCommand("ArcTanCalc");

		this.sqRb.setActionCommand("SquareCalc");
		this.cuRb.setActionCommand("CubeCalc");
		this.expRb.setActionCommand("ExponentCalc");
		this.rootRb.setActionCommand("RootCalc");
		/*this.cuRootRb.setActionCommand("CubeRootCalc");*/
		this.lnRb.setActionCommand("LogCalc");
		
		this.noFuncRb.setName("None");
		this.sineRb.setName("Sine");
		this.cosineRb.setName("Cosine");
		this.tanRb.setName("Tan");
		this.arcSineRb.setName("ArcSine");
		this.arcCosineRb.setName("ArcCosine");
		this.arcTanRb.setName("ArcTan");
		

		this.sqRb.setName("Square");
		this.cuRb.setName("Cube");
		this.expRb.setName("Exponent");
		this.rootRb.setName("Root");
		/*this.cuRootRb.setName("CubeRoot");*/
		this.lnRb.setName("Log");
		
		this.add(this.noFuncRb);
		this.add(this.sineRb);
		this.add(this.cosineRb);
		this.add(this.tanRb);
		this.add(this.arcSineRb);
		this.add(this.arcCosineRb);
		this.add(this.arcTanRb);

		this.add(this.sqRb);
		this.add(this.cuRb);
		this.add(this.expRb);
		this.add(this.rootRb);
		/*this.add(this.cuRootRb);*/
		this.add(this.lnRb);
		
		this.noFuncRb.setVisible(false);
		this.sineRb.setVisible(false);
		this.cosineRb.setVisible(false);
		this.tanRb.setVisible(false);
		this.arcSineRb.setVisible(false);
		this.arcCosineRb.setVisible(false);
		this.arcTanRb.setVisible(false);

		this.sqRb.setVisible(false);
		this.cuRb.setVisible(false);
		this.expRb.setVisible(false);
		this.rootRb.setVisible(false);
		/*this.cuRootRb.setVisible(false);*/
		this.lnRb.setVisible(false);
	}

	private void createColorChoiceRBs() {
		this.colrBg.add(this.colrPRb);
		this.colrBg.add(this.colrCRb);
		this.colrBg.add(this.colrBwRb);
		
		this.colrPRb.setActionCommand("ColorPalette");
		this.colrCRb.setActionCommand("ComputeColor");
		this.colrBwRb.setActionCommand("BlackWhite");

		this.colrPRb.setName("ColorPalette");
		this.colrCRb.setName("ComputeColor");
		this.colrBwRb.setName("BlackWhite");

		this.add(this.colrPRb);
		this.add(this.colrCRb);
		this.add(this.colrBwRb);
		
		this.colrPRb.setVisible(false);
		this.colrCRb.setVisible(false);
		this.colrBwRb.setVisible(false);
		
	}

	private void createRotationCombo() {
		for (int i = 0; i < 1000; i+=15) {
			this.rotOptions.add((double) i);
		}
		
		this.rotateCombo=new JComboBox<Double>(this.rotOptions);
		this.rotateCombo.setVisible(false);
	}
	
	private void createPolyPanel() {
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.colrPRb.setVisible(true);
		this.colrCRb.setVisible(true);
		this.colrBwRb.setVisible(true);

		
		this.noFuncRb.setVisible(true);
		this.sineRb.setVisible(true);
		this.cosineRb.setVisible(true);
		this.tanRb.setVisible(true);
		this.arcSineRb.setVisible(true);
		this.arcCosineRb.setVisible(true);
		this.arcTanRb.setVisible(true);

		this.sqRb.setVisible(true);
		this.cuRb.setVisible(true);
		this.expRb.setVisible(true);
		this.rootRb.setVisible(true);
		/*this.cuRootRb.setVisible(true);*/
		this.lnRb.setVisible(true);
		
		this.polyOptionsPanel.add(new JLabel("Exponent(X):"));
		this.polyOptionsPanel.add(this.polyExpCombos);		
		this.polyOptionsPanel.add(this.polyUseDiffCb);
		
		this.polyConstBg.add(this.polyKeepConstRb);
		this.polyConstBg.add(this.polyCreateConstRb);
		
		this.polyOptionsPanel.add(this.polyKeepConstRb);
		this.polyOptionsPanel.add(this.polyCreateConstRb);		
		
		this.polyOptionsPanel.add(new JLabel("ComplexConstant - Real (R) "));
		this.polyRealTf.setEnabled(false);
		this.polyOptionsPanel.add(this.polyRealTf);
		this.polyOptionsPanel.add(new JLabel("Imaginary (*i)"));
		this.polyImgTf.setEnabled(false);
		this.polyOptionsPanel.add(this.polyImgTf);		
		
		this.polyOptionsPanel.add(new JLabel("Max Iterations:"));
		this.polyOptionsPanel.add(this.polyMaxIterCombos);	
		this.polyOptionsPanel.add(new JLabel("RowColumnMixType:"));
		this.polyOptionsPanel.add(this.polyTypeCombos);		
		this.polyOptionsPanel.add(new  JLabel("Area Size:"));
		this.polyOptionsPanel.add(this.polySizeCombos);
		this.polyOptionsPanel.add(new JLabel("Boundary:"));
		this.polyOptionsPanel.add(this.polyBoundCombos);
		this.polyOptionsPanel.add(new JLabel("Center: X "));
		this.polyOptionsPanel.add(this.polyXCCombos);
		this.polyOptionsPanel.add(new JLabel(" Y "));
		this.polyOptionsPanel.add(this.polyYCCombos);
		this.polyOptionsPanel.add(new JLabel("ScaleSize:"));
		this.polyOptionsPanel.add(this.polyScaleSizeCombos);
		
		this.polyOptionsPanel.setVisible(false);
		this.add(this.polyOptionsPanel);
	}

	private void createDIYPanel() {
		this.setupDIYRBs();		
		
		this.createDiyMandelbrotPanel();		
		this.createDiyJuliaPanel();		
		this.createDiyApolloPanel();		
		
		this.diyOptionsPanel.add(this.diyMandPanel);
		this.diyOptionsPanel.add(this.diyJuliaPanel);
		this.diyOptionsPanel.add(this.diyApolloPanel);
		this.diyOptionsPanel.setVisible(false);

		this.add(diyOptionsPanel);
	}

	private void setupDIYRBs() {
		this.diyMandRb.setActionCommand(this.diyMand);
		this.diyJuliaRb.setActionCommand(this.diyJulia);
		this.diyApolloRb.setActionCommand(this.diyApollo);

		this.diyBg.add(this.diyMandRb);
		this.diyBg.add(this.diyJuliaRb);
		this.diyBg.add(this.diyApolloRb);

		this.diyMandRb.setSelected(true);
		this.diyMandRb.setName(this.diyMand);
		this.diyJuliaRb.setName(this.diyJulia);
		this.diyApolloRb.setName(this.diyApollo);

		this.diyOptionsPanel.add(this.diyMandRb);
		this.diyOptionsPanel.add(this.diyJuliaRb);
		this.diyOptionsPanel.add(this.diyApolloRb);
	}

	private void createDiyApolloPanel() {
		this.rotLabel.setVisible(false);
		this.rotateCombo.setVisible(false);
		this.colrPRb.setVisible(true);
		this.colrCRb.setVisible(true);
		this.colrBwRb.setVisible(false);
		this.diyApolloPanel.add(new JLabel("C1"));
		this.diyApolloPanel.add(this.diyApolloC1Combos);
		this.diyApolloPanel.add(new JLabel("C2"));
		this.diyApolloPanel.add(this.diyApolloC2Combos);
		this.diyApolloPanel.add(new JLabel("C3"));
		this.diyApolloPanel.add(this.diyApolloC3Combos);
		this.diyApolloPanel.add(new JLabel("Multiplier"));
		this.diyApolloPanel.add(this.diyApolloMultCombos);
		this.diyApolloPanel.setVisible(false);
	}

	private void createDiyJuliaPanel() {
		this.colrPRb.setVisible(true);
		this.colrCRb.setVisible(true);
		this.colrBwRb.setVisible(true);
		this.noFuncRb.setVisible(true);
		this.sineRb.setVisible(true);
		this.cosineRb.setVisible(true);
		this.tanRb.setVisible(true);
		this.arcSineRb.setVisible(true);
		this.arcCosineRb.setVisible(true);
		this.arcTanRb.setVisible(true);
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.sqRb.setVisible(true);
		this.cuRb.setVisible(true);
		this.expRb.setVisible(true);
		this.rootRb.setVisible(true);
		/*this.cuRootRb.setVisible(true);*/
		this.lnRb.setVisible(true);
		
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		
		this.diyJuliaPanel.add(new JLabel("Power:"));
		this.diyJuliaPanel.add(this.diyJuliaPowerCombos);
		this.diyJuliaPanel.add(this.diyJuliaUseDiffCb);
		
		this.diyJuliaFieldLinesBG.add(diyJuliaFieldNoneRB);
		this.diyJuliaFieldLinesBG.add(diyJuliaFieldFatouRB);
		this.diyJuliaFieldLinesBG.add(diyJuliaFieldZSqRB);
		this.diyJuliaFieldLinesBG.add(diyJuliaFieldClassicRB);

		this.diyJuliaPanel.add(this.diyJuliaFieldNoneRB);
		this.diyJuliaPanel.add(this.diyJuliaFieldFatouRB);
		this.diyJuliaPanel.add(this.diyJuliaFieldZSqRB);
		this.diyJuliaPanel.add(this.diyJuliaFieldClassicRB);

		
		this.diyJuliaConstBg.add(this.diyJuliaKeepConstRb);
		this.diyJuliaConstBg.add(this.diyJuliaCreateConstRb);
		
		this.diyJuliaPanel.add(this.diyJuliaKeepConstRb);
		this.diyJuliaPanel.add(this.diyJuliaCreateConstRb);
		
		this.diyJuliaPanel.add(new JLabel("ComplexConstant - Real (R) "));
		this.diyJuliaRealTf.setEnabled(false);
		this.diyJuliaPanel.add(this.diyJuliaRealTf);
		this.diyJuliaPanel.add(new JLabel("Imaginary (*i)"));
		this.diyJuliaImgTf.setEnabled(false);
		this.diyJuliaPanel.add(this.diyJuliaImgTf);
		
		this.diyJuliaPanel.add(new JLabel("Max Iterations: "));
		this.diyJuliaPanel.add(this.diyJuliaMaxIterCombos);

		this.diyJuliaPanel.add(new JLabel("Area: "));
		this.diyJuliaPanel.add(this.diyJuliaSizeCombos);
		
		this.diyJuliaPanel.add(new JLabel("Boundary: "));
		this.diyJuliaPanel.add(this.diyJuliaBoundCombos);
		
		this.diyJuliaPanel.add(new JLabel("Center: X "));
		this.diyJuliaPanel.add(this.diyJuliaXCCombos);
		this.diyJuliaPanel.add(new JLabel(" Y "));
		this.diyJuliaPanel.add(this.diyJuliaYCCombos);
		this.diyJuliaPanel.add(new JLabel("ScaleSize:"));
		this.diyJuliaPanel.add(this.diyJuliaScaleSizeCombos);
		
		this.diyJuliaPanel.setVisible(false);
	}

	private void createDiyMandelbrotPanel() {
		this.colrPRb.setVisible(true);
		this.colrCRb.setVisible(true);
		this.colrBwRb.setVisible(true);
		this.noFuncRb.setVisible(true);
		this.sineRb.setVisible(true);
		this.cosineRb.setVisible(true);
		this.tanRb.setVisible(true);
		this.arcSineRb.setVisible(true);
		this.arcCosineRb.setVisible(true);
		this.arcTanRb.setVisible(true);
		this.sqRb.setVisible(true);
		this.cuRb.setVisible(true);
		this.expRb.setVisible(true);
		this.rootRb.setVisible(true);
		/*this.cuRootRb.setVisible(true);*/
		this.lnRb.setVisible(true);
		
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.diyMandPanel.add(new JLabel("Magnification:"));
		this.diyMandPanel.add(this.diyMandMagCombos);
		this.diyMandPanel.add(new JLabel("Exponent:"));
		this.diyMandPanel.add(this.diyMandExpCombos);
		this.diyMandPanel.add(this.diyMandUseDiffCb);
		
		this.diyMandelbrotConstBg.add(this.diyMandelbrotKeepConstRb);
		this.diyMandelbrotConstBg.add(this.diyMandelbrotCreateConstRb);
		
		this.diyMandPanel.add(this.diyMandelbrotKeepConstRb);
		this.diyMandPanel.add(this.diyMandelbrotCreateConstRb);		
		
		this.diyMandPanel.add(new JLabel("ComplexConstant - Real (R) "));
		this.diyMandRealTf.setEnabled(false);
		this.diyMandPanel.add(this.diyMandRealTf);
		this.diyMandPanel.add(new JLabel("Imaginary (*i)"));
		this.diyMandImgTf.setEnabled(false);
		this.diyMandPanel.add(this.diyMandImgTf);
		/*this.diyMandPanel.add(this.diyMandKeepConstantCb);*/
		this.diyMandPanel.add(new JLabel("Max Iterations: "));
		this.diyMandPanel.add(this.diyMandMaxIterCombos);

		this.diyMandPanel.add(new JLabel("Area: "));
		this.diyMandPanel.add(this.diyMandSizeCombos);
		
		this.diyMandPanel.add(new JLabel("Boundary: "));
		this.diyMandPanel.add(this.diyMandBoundCombos);
		
		this.diyMandPanel.add(new JLabel("Center: X "));
		this.diyMandPanel.add(this.diyMandXCCombos);
		this.diyMandPanel.add(new JLabel(" Y "));
		this.diyMandPanel.add(this.diyMandYCCombos);
		this.diyMandPanel.add(new JLabel("ScaleSize:"));
		this.diyMandPanel.add(this.diyMandScaleSizeCombos);
		
		this.diyMandPanel.setVisible(false);
	}

	private void createMandelbrotPanel() {
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.colrPRb.setVisible(true);
		this.colrCRb.setVisible(true);
		this.colrBwRb.setVisible(true);
		this.noFuncRb.setVisible(true);
		this.sineRb.setVisible(true);
		this.cosineRb.setVisible(true);
		this.tanRb.setVisible(true);
		this.arcSineRb.setVisible(true);
		this.arcCosineRb.setVisible(true);
		this.arcTanRb.setVisible(true);
		this.sqRb.setVisible(true);
		this.cuRb.setVisible(true);
		this.expRb.setVisible(true);
		this.rootRb.setVisible(true);
		/*this.cuRootRb.setVisible(true);*/
		this.lnRb.setVisible(true);
		
		this.mandOptionsPanel.add(new JLabel("Magnification(M):"));
		this.mandOptionsPanel.add(this.mandCombos);
		this.mandOptionsPanel.add(new JLabel("Exponent(X):"));
		this.mandOptionsPanel.add(this.mandExpCombos);		
		this.mandOptionsPanel.add(this.mandUseDiffCb);
		this.mandOptionsPanel.add(new JLabel("Max Iterations:"));
		this.mandOptionsPanel.add(this.mandMaxIterCombos);		
		this.mandOptionsPanel.add(new  JLabel("Area Size:"));
		this.mandOptionsPanel.add(this.mandSizeCombos);
		this.mandOptionsPanel.add(new JLabel("Boundary:"));
		this.mandOptionsPanel.add(this.mandBoundCombos);
		this.mandOptionsPanel.add(new JLabel("Center: X "));
		this.mandOptionsPanel.add(this.mandXCCombos);
		this.mandOptionsPanel.add(new JLabel(" Y "));
		this.mandOptionsPanel.add(this.mandYCCombos);
		this.mandOptionsPanel.add(new JLabel("ScaleSize:"));
		this.mandOptionsPanel.add(this.mandScaleSizeCombos);
		
		this.mandOptionsPanel.setVisible(false);
		this.add(this.mandOptionsPanel);
	}

	private void createJuliaPanel() {
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.colrPRb.setVisible(true);
		this.colrCRb.setVisible(true);
		this.colrBwRb.setVisible(true);
		this.noFuncRb.setVisible(true);
		this.sineRb.setVisible(true);
		this.cosineRb.setVisible(true);
		this.tanRb.setVisible(true);
		this.arcSineRb.setVisible(true);
		this.arcCosineRb.setVisible(true);
		this.arcTanRb.setVisible(true);
		this.sqRb.setVisible(true);
		this.cuRb.setVisible(true);
		this.expRb.setVisible(true);
		this.rootRb.setVisible(true);
		/*this.cuRootRb.setVisible(true);*/
		this.lnRb.setVisible(true);
		
		this.juliaOptionsPanel.add(new JLabel("Power-Constant:"));
		this.juliaOptionsPanel.add(this.juliaCombos);		
		this.juliaOptionsPanel.add(this.juliaUseDiffCb);
		
		this.juliaFieldLinesBG.add(this.juliaFieldNoneRB);
		this.juliaFieldLinesBG.add(this.juliaFieldFatouRB);
		this.juliaFieldLinesBG.add(this.juliaFieldZSqRB);
		this.juliaFieldLinesBG.add(this.juliaFieldClassicRB);
		
		this.juliaOptionsPanel.add(this.juliaFieldNoneRB);
		this.juliaOptionsPanel.add(this.juliaFieldFatouRB);
		this.juliaOptionsPanel.add(this.juliaFieldZSqRB);
		this.juliaOptionsPanel.add(this.juliaFieldClassicRB);

		
		this.juliaOptionsPanel.add(new JLabel("Max Iterations:"));
		this.juliaOptionsPanel.add(this.juliaMaxIterCombos);	
		this.juliaOptionsPanel.add(new  JLabel("Area: "));
		this.juliaOptionsPanel.add(this.juliaSizeCombos);
		this.juliaOptionsPanel.add(new JLabel("Boundary:"));
		this.juliaOptionsPanel.add(this.juliaBoundCombos);

		this.juliaOptionsPanel.add(new JLabel("Center: X "));
		this.juliaOptionsPanel.add(this.juliaXCCombos);
		this.juliaOptionsPanel.add(new JLabel(" Y "));
		this.juliaOptionsPanel.add(this.juliaYCCombos);
		this.juliaOptionsPanel.add(new JLabel("ScaleSize:"));
		this.juliaOptionsPanel.add(this.juliaScaleSizeCombos);
		
		this.juliaOptionsPanel.setVisible(false);		
		this.add(this.juliaOptionsPanel);
	}

	private void createApolloPanel() {
		this.rotLabel.setVisible(false);
		if (this.rotateCombo!=null) {
			this.rotateCombo.setVisible(false);
		}
		this.colrPRb.setVisible(true);
		this.colrCRb.setVisible(true);
		this.colrBwRb.setVisible(false);
		this.apolloOptionsPanel.add(new JLabel("CurvatureOptions:"));
		this.apolloOptionsPanel.add(this.curvCombos);
		
		/*this.apolloColrBg.add(this.apolloColrPRb);
		this.apolloColrBg.add(this.apolloColrCRb);
		
		this.apolloColrPRb.setActionCommand("ColorPalette");
		this.apolloColrCRb.setActionCommand("ComputeColor");

		this.apolloColrPRb.setName("ColorPalette");
		this.apolloColrCRb.setName("ComputeColor");
		
		this.apolloColrPRb.setVisible(true);
		this.apolloColrCRb.setVisible(true);
		this.apolloOptionsPanel.add(this.apolloColrPRb);
		this.apolloOptionsPanel.add(this.apolloColrCRb);*/
		this.apolloOptionsPanel.setVisible(false);
		this.add(this.apolloOptionsPanel);
	}

	private void createFannyPanel() {
		this.rotLabel.setVisible(false);
		if (this.rotateCombo!=null) {
			this.rotateCombo.setVisible(false);
		}
		this.colrPRb.setVisible(false);
		this.colrCRb.setVisible(false);
		this.colrBwRb.setVisible(false);
		this.fannyOptionsPanel.add(new JLabel("Dimension Size:"));
		this.fannyOptionsPanel.add(this.sideCombos);

		this.fannyOptionsPanel.add(new JLabel("Ratio:"));
		this.fannyOptionsPanel.add(this.ratioCombos);
		
		this.fannyOptionsPanel.setVisible(false);
		this.add(this.fannyOptionsPanel);
	}
	
	private void createKochSnowFlakePanel(){
		this.kochSnowFlakePanel.add(this.kochFillExternalCb);
		this.kochSnowFlakePanel.add(this.kochMixColorsCb);
		this.kochSnowFlakePanel.add(this.kochSpreadOuterCb);
		
		this.kochSnowFlakePanel.setVisible(false);
		this.add(this.kochSnowFlakePanel);
	}
	
	private void createSierpinskiTPanel() {
		this.sierpinskiTPanel.add(this.sierpinskiTFillInnerCb);
		
		this.sierpTBg.add(this.sierpTUpRb);
		this.sierpTBg.add(this.sierpTDnRb);
		
		this.sierpinskiTPanel.add(new JLabel("Direction: "));
		
		this.sierpinskiTPanel.add(this.sierpTUpRb);
		this.sierpinskiTPanel.add(this.sierpTDnRb);
		
		this.sierpinskiTPanel.setVisible(false);
		this.add(this.sierpinskiTPanel);
	}
	
	/*private void createControlPanel() {
		this.mandOptionsPanel.add(new JLabel("Exponent(X):"));
		this.mandOptionsPanel.add(this.expCombos);
		this.controlPanel.add(this.useDiff);
	}
*/	
	private void doReset() {
		/*this.sideChoice = 0;
		this.ratioChoice = 0;
		this.curvChoices = null;
		this.mult = 0.0;
		this.power = 0;
		this.compConst = 0.0;
		this.complex = null;
		this.magnification = 0;
		this.mandMaxIter = 255;
		this.juliaMaxIter = 255;
		this.mandSize = 599;
		this.juliaSize = 599;
		this.mandBound=0.0;
		this.juliaBound=0.0;
		
		this.juliaXC=0.0;
		this.juliaYC=0.0;
		this.juliaScaleSize=2.0;
		


		this.mandXC=-0.5;
		this.mandYC=0.0;
		this.mandScaleSize=2.0;
		
		
		
		//diy
		this.diyMandMagnification = 0;
		this.diyMandExponent = 0;
		this.diyMandConstReal = 0.0;
		this.diyMandConstImg = 0.0;

		this.diyMandXC=-0.5;
		this.diyMandYC=0.0;
		this.diyMandScaleSize=2.0;
		
		this.diyMandMaxIter = 255;
		this.diyJuliaMaxIter = 255;
		this.diyMandBound=0.0;
		this.diyJuliaBound=0.0;

		this.diyJuliaPower = 0;
		this.diyJuliaConstReal = 0.0;
		this.diyJuliaConstImg = 0.0;
		

		this.diyJuliaXC=0.0;
		this.diyJuliaYC=0.0;
		this.diyJuliaScaleSize=2.0;
		
		this.diyMandSize = 599;
		this.diyJuliaSize = 599;

		this.diyApolloC1 = 0;
		this.diyApolloC2 = 0;
		this.diyApolloC3 = 0;
		this.diyApolloMult = 0;
		*/
	}

	private void setComboSelections() {
		this.combos.setSelectedIndex(0);
		this.sideCombos.setSelectedIndex(0);
		this.ratioCombos.setSelectedIndex(0);
		this.curvCombos.setSelectedIndex(0);
		this.juliaCombos.setSelectedIndex(0);
		this.mandCombos.setSelectedIndex(0);
		this.juliaMaxIterCombos.setSelectedIndex(5);
		this.mandMaxIterCombos.setSelectedIndex(5);
		this.juliaBoundCombos.setSelectedIndex(2);
		this.mandBoundCombos.setSelectedIndex(2);
		
		this.juliaXCCombos.setSelectedIndex(4);
		this.mandXCCombos.setSelectedIndex(3);
		this.juliaYCCombos.setSelectedIndex(4);
		this.mandYCCombos.setSelectedIndex(4);
		this.juliaScaleSizeCombos.setSelectedIndex(8);
		this.mandScaleSizeCombos.setSelectedIndex(8);
		
		this.rotateCombo.setSelectedIndex(0);		

		this.diyJuliaXCCombos.setSelectedIndex(4);
		this.diyMandXCCombos.setSelectedIndex(3);
		this.diyJuliaYCCombos.setSelectedIndex(4);
		this.diyMandYCCombos.setSelectedIndex(4);
		this.diyJuliaScaleSizeCombos.setSelectedIndex(8);
		this.diyMandScaleSizeCombos.setSelectedIndex(8);
		
		this.diyMandMagCombos.setSelectedIndex(0);
		this.diyMandExpCombos.setSelectedIndex(0);
		
		this.diyJuliaPowerCombos.setSelectedIndex(0);
		
		this.diyApolloC1Combos.setSelectedIndex(0);
		this.diyApolloC2Combos.setSelectedIndex(0);
		this.diyApolloC3Combos.setSelectedIndex(0);
		
		this.combos.setSelectedItem(this.comboOptions[0]);		
		this.sideCombos.setSelectedItem(this.sideOptions[0]);		
		this.ratioCombos.setSelectedItem(this.ratioOptions[0]);		
		this.curvCombos.setSelectedItem(this.ratioOptions[0]);
		this.juliaCombos.setSelectedItem(this.juliaOptions[0]);
		this.mandCombos.setSelectedItem(this.mandOptions[0]);
		this.mandMaxIterCombos.setSelectedItem(this.mandMaxIterOptions[5]);
		this.juliaMaxIterCombos.setSelectedItem(this.juliaMaxIterOptions[5]);
		this.mandSizeCombos.setSelectedItem(this.mandSizeOptions[6]);
		this.juliaSizeCombos.setSelectedItem(this.juliaSizeOptions[6]);
		this.mandBoundCombos.setSelectedItem(this.mandBoundOptions[2]);
		this.juliaBoundCombos.setSelectedItem(this.juliaBoundOptions[2]);
		
		this.juliaXCCombos.setSelectedItem(this.juliaXCOptions[4]);
		this.mandXCCombos.setSelectedItem(this.mandXCOptions[3]);
		this.juliaYCCombos.setSelectedItem(this.juliaYCOptions[4]);
		this.mandYCCombos.setSelectedItem(this.mandYCOptions[4]);
		this.juliaScaleSizeCombos.setSelectedItem(this.juliaScaleSizeOptions[8]);
		this.mandScaleSizeCombos.setSelectedItem(this.mandScaleSizeOptions[8]);
		
		this.rotateCombo.setSelectedItem(this.rotOptions.get(0));
		
		this.diyJuliaXCCombos.setSelectedItem(this.diyJuliaXCOptions[4]);
		this.diyMandXCCombos.setSelectedItem(this.diyMandXCOptions[3]);
		this.diyJuliaYCCombos.setSelectedItem(this.diyJuliaYCOptions[4]);
		this.diyMandYCCombos.setSelectedItem(this.diyMandYCOptions[4]);
		this.diyJuliaScaleSizeCombos.setSelectedItem(this.diyJuliaScaleSizeOptions[8]);
		this.diyMandScaleSizeCombos.setSelectedItem(this.diyMandScaleSizeOptions[8]);
		
		
		this.diyMandMagCombos.setSelectedItem(this.diyMandMagOptions[0]);
//		this.diyMandExpCombos.setSelectedItem(this.diyMandExpOptions[0]);
		this.diyJuliaPowerCombos.setSelectedItem(this.diyJuliaPowerOptions[0]);
		this.diyMandMaxIterCombos.setSelectedItem(this.diyMandMaxIterOptions[5]);
		this.diyJuliaMaxIterCombos.setSelectedItem(this.diyJuliaMaxIterOptions[5]);
		
		this.diyMandBoundCombos.setSelectedItem(this.diyMandBoundOptions[2]);
		this.diyJuliaBoundCombos.setSelectedItem(this.diyJuliaBoundOptions[2]);		

		this.diyApolloC1Combos.setSelectedItem(this.diyApolloC1Options[0]);
		this.diyApolloC2Combos.setSelectedItem(this.diyApolloC2Options[0]);
		this.diyApolloC3Combos.setSelectedItem(this.diyApolloC3Options[0]);
		this.diyApolloMultCombos.setSelectedItem(this.diyApolloMultOptions[0]);
	}
	
	
	private void displayFractalPanel(String fractal) {
		
	}
	
	private void doSelectCombosCommand(String option) {
//		System.out.println("in doSelectCombosCommand == option is "+option);
		this.comboChoice = option;
		/*TODO	l8r*/
		/*this.displayFractalPanel(this.comboChoice);*/

		if (this.comboChoice.equals(APOLLONIAN_CIRCLES)) {
			this.rotLabel.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(true);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(false);

			if (this.curvChoices == null || this.mult == 0) {
				this.buStart.setEnabled(false);
			} else {
				this.buStart.setEnabled(true);
			}
		} else if (this.comboChoice.equals(FANNY_CIRCLE) || this.comboChoice.equals(FANNY_TRIANGLES)) {
			this.rotLabel.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.fannyOptionsPanel.setVisible(true);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(false);
			if (this.sideChoice == 0 || this.ratioChoice == 0) {
				this.buStart.setEnabled(false);
			} else {
				this.buStart.setEnabled(true);
			}
		} else if (this.comboChoice.equals(JULIA)) {
			this.rotLabel.setVisible(true);
			this.rotateCombo.setVisible(true);
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(true);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
			if (this.power == 0 && (this.compConst == 0.0 || this.complex == null)) {
				this.buStart.setEnabled(false);
			} else {
				this.buStart.setEnabled(true);
			}
		} else if (this.comboChoice.equals(MANDELBROT)) {
			this.rotLabel.setVisible(true);
			this.rotateCombo.setVisible(true);
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(true);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
		} else if (this.comboChoice.equals(POLY)) {
			this.rotLabel.setVisible(true);
			this.rotateCombo.setVisible(true);
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(true);
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
		} else if (this.comboChoice.equals(DIY)) {
			this.rotLabel.setVisible(true);
			if (!this.diyApolloRb.isSelected()) {
				this.rotLabel.setVisible(true);
				this.rotateCombo.setVisible(true);
			}else{
				this.rotLabel.setVisible(false);
				this.rotateCombo.setVisible(false);
			}
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(true);
			this.diyMandPanel.setVisible(true);
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
		} else if (this.comboChoice.equals(SIERPINSKI_TRIANGLES)) {
			this.rotLabel.setVisible(false);
			this.formulaArea.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(true);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(false);
			this.buStart.setEnabled(true);
		} else if(this.comboChoice.equals(KOCHSNOWFLAKE)){
			this.rotLabel.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(true);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(false);
			this.buStart.setEnabled(true);
		}
		else {
			this.rotLabel.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(false);
			this.buStart.setEnabled(true);
		}
	}

	private void doSelectSideCombosCommand(Integer sideOption) {
		this.sideChoice = sideOption;
		if (this.ratioChoice != 0) {
			this.buStart.setEnabled(true);
		}
	}

	private void doSelectRatioCombosCommand(Integer ratioOption) {
		this.ratioChoice = ratioOption;
		if (this.sideChoice != 0) {
			this.buStart.setEnabled(true);
		}
	}
	
	private void doSelectCurvCombosCommand(String cOption) {
		switch (cOption) {
			case A1: // A1 = "C[1, 1, 1] M[1]";
				this.curvChoices = new double[] { 1.0, 1.0, 1.0 };
				this.mult = 1.0;
				break;

			case A2: // A2 = "C[25, 25, 28] M[20]";
				this.curvChoices = new double[] { 25.0, 25.0, 28.0 };
				this.mult = 20.0;
				break;

			case A3: // A3 = "C[5, 8, 8] M[6]";
				this.curvChoices = new double[] { 5.0, 8.0, 8.0 };
				this.mult = 6.0;
				break;

			case A4: // A4 = "C[10, 15, 19] M[6]";
				this.curvChoices = new double[] { 10.0, 15.0, 19.0 };
				this.mult = 6.0;
				break;

			case A5: // A5 = "C[23, 27, 18] M[16]";
				this.curvChoices = new double[] { 23.0, 27.0, 18.0 };
				this.mult = 16.0;
				break;

			case A6: // A6 = "C[2, 2, 3] M[1]";
				this.curvChoices = new double[] { 2.0, 2.0, 3.0 };
				this.mult = 1.0;
				break;

			default:
				// use--a1
				this.curvChoices = new double[] { 1.0, 1.0, 1.0 };
				this.mult = 1.0;
				break;
		}
		this.buStart.setEnabled(true);
	}	
	
	private void doJuliaChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.power != 0 && (this.compConst != 0.0 || this.complex!=null)) {
			this.buStart.setEnabled(true);
			
			addJuliaFormulaInfo();
			
			addJuliaUseDiffInfo();
		} else {
			this.buStart.setEnabled(false);
		}
	}

	private void addJuliaFormulaInfo() {
		if (!(this.juliaSelection.equals("J7") || this.juliaSelection.equals("J8")
				|| this.juliaSelection.equals("J9"))) {
			
			this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z ^ " + this.power + " + " + this.compConst+"<br/>");
			
		} else {
			if(this.juliaSelection.equals("J7")){
				this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z ^ " + this.power + " + (-0.74543 + 0.11301 * i)<br/>");
			}else if(this.juliaSelection.equals("J8")){
				this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z ^ " + this.power + " + (-0.75 + 0.11 * i)<br/>");
			}else if(this.juliaSelection.equals("J9")){
				this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z ^ " + this.power + " + (-0.1 + 0.651 * i)<br/>");
			}
		}
		
		if(this.applyFatou){
			this.formulaArea.setText("<br/><font color='green'>Fatou Field Lines:<br/>f(z) = (1 - (z^3/6)) / ((z - (z^2/2)) ^ 2) + C</font><br/>");
		}else if(this.applyZSq){
			this.formulaArea.append("<br/><font color='green'>ZSquared Field Lines:<br/><br/>f(z) = (z ^ 2) + C</font><br/>");
		}else if(this.applyClassicJulia){
			this.formulaArea.append("<br/><font color='green'>Classic Julia:<br/><br/>f(z) = z^4 + ((z^3)/(z-1)) + ((z^2)/(z^3 + 4*z^2 + 5))  + C</font><br/>");
		}
	}
	
	private void addDiyJuliaUseDiffInfo() {
		if (this.diyJuliaUseDiff || this.diyJuliaUseDiffCb.isSelected()) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>");
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>");
		}
	}
	
	private void addDiyMandelbrotUseDiffInfo() {
		if (this.diyMandUseDiff || this.diyMandUseDiffCb.isSelected()) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>");
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>");
		}
	}

	private void addJuliaUseDiffInfo() {
		if (this.jUseDiff || this.juliaUseDiffCb.isSelected()) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>");
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>");
		}
	}
	
	private void addMandelbrotUseDiffInfo() {
		if (this.mUseDiff || this.mandUseDiffCb.isSelected()) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>");
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>");
		}
	}
	
	private void addPolyUseDiffInfo() {
		if (this.polyUseDiff || this.polyUseDiffCb.isSelected()) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>");
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>");
		}
	}
	
	private void addMandelbrotConstInfo(FractalBase fBase){
		Mandelbrot m = (Mandelbrot)fBase;
		ComplexNumber	c	=m.getComplex();
	
		String func2Apply = m.useFuncConst;
		addFuncTypeConstInfo(c, func2Apply);
	}
	
	
	private void addPolyConstInfo(FractalBase fBase) {
		PolyFract p = (PolyFract) fBase;
		ComplexNumber c = p.getCompConst();

		String func2Apply = p.useFuncConst;
		addFuncTypeConstInfo(c, func2Apply);
	}
	
	private void addJuliaConstInfo(FractalBase fBase){
		Julia j = (Julia)fBase;
		ComplexNumber c = j.getComplex();
		
		String func2Apply = j.useFuncConst;
		addFuncTypeConstInfo(c, func2Apply);
	}

	private void addFuncTypeConstInfo(ComplexNumber c, String func2Apply) {
		boolean isComplexConst = false;
		if (c == null) {
			isComplexConst = true;
		}
		switch (func2Apply) {
		case	"None":
			if (!isComplexConst) {
				//			System.out.println("NONE-<br/>ComplexConstant == "+c.toString()+"<br/>");
				this.formulaArea.append("<br/>ComplexConstant == " + c.toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = z");
			}
			break;
		case	"Sine":
			if (!isComplexConst) {
				//			System.out.println("SINE-<br/>ComplexConstant == "+c.sine().toString()+"<br/>");
				this.formulaArea.append("<br/>sin(ComplexConstant) == " + c.sine().toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = sin(z)");
			}
			break;
		case	"Cosine":
			if (!isComplexConst) {
				//			System.out.println("COSINE-<br/>ComplexConstant == "+c.cosine().toString()+"<br/>");
				this.formulaArea.append("<br/>cos(ComplexConstant) == " + c.cosine().toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = cos(z)");
			}
			break;
		case	"Tan":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>tan(ComplexConstant) == " + c.tan().toString() + "<br/>");
				
			} else {
				this.formulaArea.append("<br/>Complex Constant = tan(z)");
			}
			break;
		case	"ArcSine":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>asin2(ComplexConstant) == " + c.inverseSine().toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = arcsin(z)");
			}
			break;
		case	"ArcCosine":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>acos2(ComplexConstant) == " + c.inverseCosine().toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = arccos(z)");
			}
			break;
		case	"ArcTan":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>atan(ComplexConstant) == " + c.inverseTangent().toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = arctan(z)");
			}
			break;
		case	"Square":
			if (!isComplexConst) {
				//			System.out.println("SQUARE-<br/>ComplexConstant == "+c.power(2).toString()+"<br/>");
				this.formulaArea.append("<br/>(ComplexConstant)squared or C ^ 2 == " + c.power(2).toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = ( z ^ 2 )");
			}
			break;
		case	"Cube":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>(ComplexConstant)cubed or C ^ 3 == " + c.power(3).toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = ( z ^ 3 )");
			}
			break;
		case	"Exponent":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>exponent(ComplexConstant) or e ^ C == " + c.exp().toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = ( e ^ z )");
			}
			break;
		case	"Root":
			if (!isComplexConst) {
				//			System.out.println("ROOT-<br/>ComplexConstant == "+c.sqroot().toString()+"<br/>");
				this.formulaArea.append("<br/>sqrt(ComplexConstant) == " + c.sqroot().toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant =  sqrt( z )");
			}
			break;
		case	"Log":
			if (!isComplexConst) {
				//			System.out.println("LOG--<br/>ComplexConstant == "+c.ln().toString()+"<br/>");
				this.formulaArea.append("<br/>log(ComplexConstant) [base e] == " + c.ln().toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = ln( z )");
			}
			break;

		default:
			if (!isComplexConst) {
				this.formulaArea.append("<br/>ComplexConstant == " + c.toString() + "<br/>");
			} else {
				this.formulaArea.append("<br/>Complex Constant = Z");
			}
			break;
		}
	}

	
	private void doSetKochSpreadOuterCommand(boolean spread) {
		this.kochSpreadOuter = spread;
	}
	
	private void doSetKochFillExternalCommand(boolean fillExternal) {
		this.kochFillExternals = fillExternal;
	}
	
	private void doSetKochMixColorsCommand(boolean mix) {
		this.kochMixColors = mix;
	}
	
	private void doSetSierpinskiTFillInnerCommand(boolean fillInner) {
		this.sierpinskiFillInnerT = fillInner;
	}
	
	private ActionListener sierpTDirRbListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSierpTDirChoice();
			}
		};
	}

	private void doSierpTDirChoice() {
		if (this.sierpTUpRb.isSelected()) {
			this.sierpTDir = "UP";
		} else {
			this.sierpTDir = "DOWN";
		}
	}

	private void doSetJuliaUseDiffCommand(boolean useDiffs) {
		this.jUseDiff = useDiffs;
		this.formulaArea.setVisible(true);
		this.doJuliaChoicesCheck();
	}
	
	private void doSelectJuliaMaxIterCombosCommand(Integer maxIt) {
		this.juliaMaxIter = maxIt;
	}
	

	private void doSelectJuliaLoopLimitCombosCommand(Integer limit) {
		this.juliaSize = limit;
	}
	

	private void doSelectJuliaBoundCombosCommand(Double bound) {
		this.juliaBound = bound;
	}
	
	private void doSelectJuliaXCCombosCommand(Double x) {
		this.juliaXC = x;
	}
	private void doSelectJuliaYCCombosCommand(Double y) {
		this.juliaYC = y;
	}
	private void doSelectJuliaScaleSizeCombosCommand(Double size) {
		this.juliaScaleSize = size;
	}
	
	private void doSelectJuliaCombosCommand(String cOption) {
		switch (cOption) {
			case J1: //  J1 = "P[2] C[0.279]";	//f(z) = z^2 + 0.279
				this.power = 2;
				this.compConst = 0.279;
				this.juliaSelection="J1";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;
			case J2: //  J2 = "P[3] C[0.4]";		//f(z) = z^3 + 0.400
				this.power = 3;
				this.compConst = 0.4;
				this.juliaSelection="J2";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;	
			case J3: //  J3 = "P[4] C[0.484]";	//f(z) = z^4 + 0.484
				this.power = 4;
				this.compConst = 0.484;
				this.juliaSelection="J3";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;	
			case J4: //  J4 = "P[5] C[0.544]";	//f(z) = z^5 + 0.544
				this.power = 5;
				this.compConst = 0.544;
				this.juliaSelection="J4";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;	
			case J5: //  J5 = "P[6] C[0.59]";	//f(z) = z^6 + 0.590
				this.power = 6;
				this.compConst = 0.59;
				this.juliaSelection="J5";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;	
			case J6: //  J6 = "P[7] C[0.626]";	//f(z) = z^7 + 0.626
				this.power = 7;
				this.compConst = 0.626;
				this.juliaSelection="J6";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;	
			case J7: //  J7 = "P[2] C1[-0.74543+0.11301*i]";
				this.power = 2;				this.compConst=0.0;
				this.complex = "C1";
				this.juliaSelection="J7";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;	
			case J8: //  J8 = "P[2] C2";//[-0.75+0.11*i]";
				this.power = 2;				this.compConst=0.0;
				this.complex = "C2";
				this.juliaSelection="J8";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;
			case J9: //  J9 = "P[2] C3";//[-0.1+0.651*i]";
				this.power = 2;				this.compConst=0.0;
				this.complex = "C3";
				this.juliaSelection="J9";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;
				
			default:
				// use--J1
				this.power = 2;
				this.compConst = 0.279;
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				
				break;				
		}
		
		this.formulaArea.setVisible(true);

		this.buStart.setEnabled(true);
	}
	
	private void doSelectMandMaxIterCombosCommand(Integer maxIt) {
		this.mandMaxIter = maxIt;
	}
	
	private void doSelectMandLoopLimitCombosCommand(Integer limit) {
		this.mandSize = limit;
	}
	
	
	private void doMandelbrotChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.magnification != 0 && this.exponent != 0) {
			this.buStart.setEnabled(true);
			this.formulaArea.setText("Mandelbrot Set:<br/><br/>f(z) = z ^ " + this.exponent + " + C");
			if (this.mUseDiff || this.mandUseDiffCb.isSelected()) {
				this.formulaArea
						.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin");
			} else {
				this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin");
			}
		} else {
			this.buStart.setEnabled(false);
		}
	}
	
	private void doDiyMandelbrotChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.diyMandMagnification != 0 && this.diyMandExponent != 0
				&& ( ((this.diyMandRealTf.getText().length() > 0) && 
						(this.diyMandImgTf.getText().length() > 0))
					&& !this.keepConst)
				|| this.keepConst
			) {
			
			this.buStart.setEnabled(true);
			String formula = "Mandelbrot Set:<br/><br/>f(z) = z ^ " + this.diyMandExponent + " + C";
			if (!this.keepConst) {
				formula += "<br/>  C = " + Double.parseDouble(this.diyMandRealTf.getText()) + " + ("
						+ Double.parseDouble(this.diyMandImgTf.getText()) + ")";
			}
			if (this.mUseDiff || this.mandUseDiffCb.isSelected()) {
				formula += "<br/><br/>Calculated inverted colors based on differences in pixel values from origin";
			} else {
				formula += "<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin";
			}
			this.formulaArea.setText(formula);
		} else {
			this.buStart.setEnabled(false);
		}
	}
	
	private void doDiyJuliaChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.diyJuliaPower != 0
				&& (!this.keepConst && (this.diyJuliaRealTf.getText().length() > 0)
						&& (this.diyJuliaImgTf.getText().length() > 0) && !this.keepConst)
				|| this.keepConst) {
			this.buStart.setEnabled(true);
			String formula = "Julia Set:<br/><br/>f(z) = z ^ " + this.diyJuliaPower + " + C";
			if (!this.keepConst) {
				formula += "<br/>  C = " + Double.parseDouble(this.diyJuliaRealTf.getText()) + " + ("
						+ Double.parseDouble(this.diyJuliaImgTf.getText()) + ")";
			}
			if (this.diyJuliaUseDiff || this.diyJuliaUseDiffCb.isSelected()) {
				formula += "<br/><br/>Calculated inverted colors based on differences in pixel values from origin";
			} else {
				formula += "<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin";
			}

			this.formulaArea.setText(formula);
		} else {
			this.buStart.setEnabled(false);
		}
	}
	
	private void doDiyApolloChoicesCheck() {
		if (this.diyApolloC1 != 0 && this.diyApolloC2 != 0 && this.diyApolloC3 != 0 && this.diyApolloMult != 0) {
			this.buStart.setEnabled(true);
		} else {
			this.buStart.setEnabled(false);
		}
	}
	
	private void doSelectMandelbrotCombosCommand(Integer magOption) {
		this.magnification = magOption;
		this.formulaArea.setVisible(true);
		this.doMandelbrotChoicesCheck();
	}
	
	private void doSelectMandExponentCombosCommand(Integer expOption) {
		this.exponent = expOption;
		this.formulaArea.setVisible(true);
		this.doMandelbrotChoicesCheck();
	}
	
	private void doSetMandUseDiffCommand(boolean useDiffs) {
		this.mUseDiff = useDiffs;
		this.formulaArea.setVisible(true);
		this.doMandelbrotChoicesCheck();
	}
	

	private void doSelectMandBoundCombosCommand(Double bound) {
		this.mandBound = bound;
	}
	
	private void doSelectMandXCCombosCommand(Double x) {
		this.mandXC = x;
	}
	private void doSelectMandYCCombosCommand(Double y) {
		this.mandYC = y;
	}
	private void doSelectMandScaleSizeCombosCommand(Double size) {
		this.mandScaleSize = size;
	}
	////////ends-mand
	
	////////////poly/////////////
	private void doSelectPolyExponentCombosCommand(Integer expOption) {
		this.polyPower = expOption;
		this.formulaArea.setVisible(true);
		this.doPolyChoicesCheck();
	}
	
	private void doSetPolyUseDiffCommand(boolean useDiffs) {
		this.polyUseDiff = useDiffs;
		this.formulaArea.setVisible(true);
		this.doPolyChoicesCheck();
	}	

	private void doSelectPolyBoundCombosCommand(Double bound) {
		this.polyBound = bound;
	}
	
	private void doSelectPolyXCCombosCommand(Double x) {
		this.polyXC = x;
	}
	private void doSelectPolyYCCombosCommand(Double y) {
		this.polyYC = y;
	}
	private void doSelectPolyScaleSizeCombosCommand(Double size) {
		this.polyScaleSize = size;
	}
	private void doSelectPolyMaxIterCombosCommand(Integer max) {
		this.polyMaxIter = max;
		this.formulaArea.setVisible(true);
		this.doPolyChoicesCheck();
	}

	private void doSelectPolyTypeCombosCommand(String tp) {
		this.polyType = tp;
		this.formulaArea.setVisible(true);
		this.doPolyChoicesCheck();
	}
	private void doSelectPolyLoopLimitCombosCommand(Integer limit) {
		this.polySize = limit;
	}
	
	private void doPolyChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.polyPower != 0) {
			this.buStart.setEnabled(true);
			this.formulaArea.setText("Poynomial Set:<br/><br/>nf(z) = (x ^ " + this.polyPower + " + y ^ " + this.polyPower + ") + C"+
					"<br/>  x = Row + 0 * i , y = 0 + Column * i");
			if (this.polyUseDiff || this.polyUseDiffCb.isSelected()) {
				this.formulaArea
						.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin");
			} else {
				this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin");
			}
		} else {
			this.buStart.setEnabled(false);
		}
	}
	////////ends-poly////////////
	
	///////////////////
	private void doSelectDiyMandMagnificationCombosCommand(Integer magOption) {
		this.diyMandMagnification = magOption;
		this.formulaArea.setVisible(true);
		this.doDiyMandelbrotChoicesCheck();
	}
	
	private void doSelectDiyMandExponentCombosCommand(Integer expOption) {
		this.diyMandExponent = expOption;
		this.formulaArea.setVisible(true);
		this.doDiyMandelbrotChoicesCheck();
	}
	
	private void doSetDiyMandUseDiffCommand(boolean useDiffs) {
		this.diyMandUseDiff = useDiffs;
		this.formulaArea.setVisible(true);
		this.doDiyMandelbrotChoicesCheck();
	}
	
	private void doSelectDiyMandMaxIterCombosCommand(Integer max) {
		this.diyMandMaxIter = max;
		this.formulaArea.setVisible(true);
		this.doDiyMandelbrotChoicesCheck();
	}
	
	private void doSelectDiyMandBoundCombosCommand(Double bd) {
		this.diyMandBound = bd;
		this.formulaArea.setVisible(true);
		this.doDiyMandelbrotChoicesCheck();
	}

	private void doSelectDiyMandXCCombosCommand(Double x) {
		this.diyMandXC = x;
	}

	private void doSelectDiyMandYCCombosCommand(Double y) {
		this.diyMandYC = y;
	}

	private void doSelectDiyMandScaleSizeCombosCommand(Double size) {
		this.diyMandScaleSize = size;
	}
	
	//-julia
	private void doSelectDiyJuliaPowerCombosCommand(Integer powerOption) {
		this.diyJuliaPower = powerOption;
		this.formulaArea.setVisible(true);
		this.doDiyJuliaChoicesCheck();
	}
	
	private void doSelectDiyJuliaMaxIterCombosCommand(Integer max) {
		this.diyJuliaMaxIter = max;
		this.formulaArea.setVisible(true);
		this.doDiyJuliaChoicesCheck();
	}

	
	private void doSelectDiyJuliaBoundCombosCommand(Double bd) {
		this.diyJuliaBound = bd;
		this.formulaArea.setVisible(true);
		this.doDiyJuliaChoicesCheck();
	}
	
	private void doSetDiyJuliaUseDiffCommand(boolean useDiffs) {
		this.diyJuliaUseDiff = useDiffs;
		this.formulaArea.setVisible(true);
		this.doDiyJuliaChoicesCheck();
	}
	

	private void doSelectDiyJuliaXCCombosCommand(Double x) {
		this.diyJuliaXC = x;
	}

	private void doSelectDiyJuliaYCCombosCommand(Double y) {
		this.diyJuliaYC = y;
	}

	private void doSelectDiyJuliaScaleSizeCombosCommand(Double size) {
		this.diyJuliaScaleSize = size;
	}
	
	//-apollo
	private void doSelectDiyApolloC1CombosCommand(Integer c1Option) {
		this.diyApolloC1 = c1Option;
		this.doDiyApolloChoicesCheck();
	}
	private void doSelectDiyApolloC2CombosCommand(Integer c2Option) {
		this.diyApolloC2 = c2Option;
		this.doDiyApolloChoicesCheck();
	}
	private void doSelectDiyApolloC3CombosCommand(Integer c3Option) {
		this.diyApolloC3 = c3Option;
		this.doDiyApolloChoicesCheck();
	}
	private void doSelectDiyApolloMultCombosCommand(Integer multOption) {
		this.diyApolloMult = multOption;
		this.doDiyApolloChoicesCheck();
	}
	///////////////////
	
	private void doSetDiyFractalChoice(String diyChoice) {
		this.setDiyFractChoice(diyChoice);
//System.out.println("Here---doSetDiyFractalChoice -- "+diyChoice);
		if (diyChoice.equals(diyMand) && this.diyMandRb.isSelected()) {
			this.comboChoice=diyMand;//"DIY_" + MANDELBROT;
			this.diyMandPanel.setVisible(true);
			this.diyJuliaPanel.setVisible(false);
			this.diyApolloPanel.setVisible(false);
		} else if (diyChoice.equals(diyJulia) && this.diyJuliaRb.isSelected()) {
			this.comboChoice=diyJulia;
			this.diyMandPanel.setVisible(false);
			this.diyJuliaPanel.setVisible(true);
			this.diyApolloPanel.setVisible(false);
		} else if (diyChoice.equals(diyApollo) && this.diyApolloRb.isSelected()) {
			this.comboChoice=diyApollo;
			this.rotLabel.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.diyMandPanel.setVisible(false);
			this.diyJuliaPanel.setVisible(false);
			this.diyApolloPanel.setVisible(true);
		}
	}
	
	private ActionListener keepConstRbListener() {
		return new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				doSetKeepConstantCommand();
			}
		};
	}
	
	private void doSetKeepConstantCommand() {
		String choice = this.getComboChoice();
		this.keepConst = (this.diyJuliaKeepConstRb.isSelected()&&choice.equals(diyJulia))||
						(this.diyMandelbrotKeepConstRb.isSelected()&&choice.equals(diyMand))||
						(this.polyKeepConstRb.isSelected()&&choice.equals(POLY));
		if (this.keepConst) {
			if (choice.equals(diyJulia)) {
				this.diyJuliaRealTf.setEnabled(false);
				this.diyJuliaImgTf.setEnabled(false);
			} else if (choice.equals(diyMand)) {
				this.diyMandRealTf.setEnabled(false);
				this.diyMandImgTf.setEnabled(false);
			} else if (choice.equals(POLY)) {
				this.polyRealTf.setEnabled(false);
				this.polyImgTf.setEnabled(false);
			}
		} else {
			if (choice.equals(diyJulia)) {
				this.diyJuliaRealTf.setEnabled(true);
				this.diyJuliaImgTf.setEnabled(true);
			} else if (choice.equals(diyMand)) {
				this.diyMandRealTf.setEnabled(true);
				this.diyMandImgTf.setEnabled(true);
			} else if (choice.equals(POLY)) {
				this.polyRealTf.setEnabled(true);
				this.polyImgTf.setEnabled(true);
			}
		}
		this.formulaArea.setVisible(true);

		if (choice.equals(diyJulia)) {
			this.doDiyJuliaChoicesCheck();
		} else if (choice.equals(diyMand)) {
			this.doDiyMandelbrotChoicesCheck();
		} else if (choice.equals(POLY)) {
			this.doPolyChoicesCheck();
		}
	}
	
	private void doSetRotationCombosCommand(Double rot){
		this.setRotation(rot);
	}
	
	private void doCloseCommand(){
		this.getFractalBase().dispose();
		this.buStart.setEnabled(false);
	}
	
	private void doStartCommand() {
		this.formulaArea.setText("");
		// fractal art choice
		String choice = this.getComboChoice();
//		System.out.println("choice--startC--is="+choice);
		// for Fanny
		int length = this.getSideComboChoice();
		int ratio = this.getRatioChoice();
		// for Apollo
		double[] cChoices = this.getCurvChoice();
		double mXt = this.getMultiplier();

		FractalBase ff = null;
		
		if (choice.equals(FANNY_CIRCLE)) {
			ff = new FannyCircle(length, ratio);
		} else if (choice.equals(FANNY_TRIANGLES)) {
			ff = new FannyTriangles(length, ratio);
		} else if (choice.equals(SIERPINSKI_TRIANGLES)) {
			boolean fillInner = this.sierpinskiFillInnerT;
			String dirSierpT = this.sierpTDir;
			ff = new SierpinskiTriangle(dirSierpT, fillInner);
		} else if (choice.equals(SIERPINSKI_SQUARES)) {
			ff = new SierpinskiSquare();
		} else if (choice.equals(APOLLONIAN_CIRCLES)) {
			ff = new ApollonianCircles(cChoices, mXt);
			boolean useCP = this.colrPRb.isSelected();
			ff.setUseColorPalette(useCP);
		} else if (choice.equals(SAMPLE)) {
			ff = new FractalBaseSample();
		} else if (choice.equals(POLY)) {			
			ff = startPoly();
		} else if (choice.equals(MANDELBROT)) {
			ff = startMandelbrot();
		} else if (choice.equals(JULIA)) {
			ff = startJulia();
		} else if (choice.equals(CST_FRACTAL)) {
			ff = new CSTFractal();
		} else if (choice.equals(SAMPLE)) {
			ff = new FractalBaseSample();
		} else if (choice.equals(KOCHSNOWFLAKE)) {
			boolean fillExt = this.kochFillExternals;
			boolean mixColors = this.kochMixColors;
			boolean spreadOut = this.kochSpreadOuter;
			ff = new KochSnowFlakeFractal(fillExt, mixColors,spreadOut);
		} else if ((choice.startsWith("DIY")||choice.endsWith("Yourself"))) {
			if (this.diyMandRb.isSelected()) {
				ff = createDIYMandelbrot();				
			} else if(this.diyJuliaRb.isSelected()){
				ff = createDIYJulia();
			} else if (this.diyApolloRb.isSelected()) {
				ff = startDIYApollo();
			} else {
				ff=null;
			}
		}  else {
			ff = null;
			return;
		}

		ff.reset();
		this.startFractals(ff);
	}

	private FractalBase startDIYApollo() {
		FractalBase ff;
		boolean useCP = this.colrPRb.isSelected();
		double c1 = this.diyApolloC1;
		double c2 = this.diyApolloC2;
		double c3 = this.diyApolloC3;
		double mult = this.diyApolloMult;
		ff = new ApollonianCircles(new double[] {c1,c2,c3}, mult);
		ff.setUseColorPalette(useCP);
		return ff;
	}

	private FractalBase createDIYJulia() {
		FractalBase ff;
		//diyJulia
		boolean useCP = this.colrPRb.isSelected();
		boolean useBw = this.colrBwRb.isSelected();
		String func = this.constFuncChoice;
		double rot = this.getRotation();
		int diyJuliaLoopLt = this.juliaSize;
		int diyJuliaP = this.getDiyJuliaPower();
		boolean diyJuliaUseD = this.getDiyJuliaUseDiff();
		int diyJuliaMaxIt = this.diyJuliaMaxIter;
		double diyJuliaBd = this.diyJuliaBound;
		double diyJXc = this.diyJuliaXC;
		double diyJYc = this.diyJuliaYC;
		double diyJScale = this.diyJuliaScaleSize;
		


		String diyJApplyField = this.fieldLines;
		boolean diyJApplyFatou = this.applyFatou;
		boolean diyJApplyZSq = this.applyZSq;
		boolean diyJApplyClassic = this.applyClassicJulia;
		
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>(DIY)Julia Set:<br/>");//<br/>f(z) = z ^ " + diyJuliaP + " + C<br/>");
		
		if (diyJApplyFatou) {
			this.formulaArea.append("<br/><font color='green'>Fatou Field Lines:<br/><br/>f(z) = (1 - (z^3/6)) / ((z - (z^2/2)) ^ 2) + C</font><br/>");
		} else if (diyJApplyZSq) {
			this.formulaArea.append("<br/><font color='green'>ZSquared Field Lines:<br/><br/>f(z) = (z ^ 2) ^ " + diyJuliaP + " + C</font><br/>");
		} else if (diyJApplyClassic) {
			this.formulaArea.append("<br/><font color='green'>Classic Julia:<br/><br/>f(z) = z^4 + z^3/(z-1) + z^2/(z^3 + 4*z^2 + 5)  + C</font><br/>");
		} else {
			this.formulaArea.append("<br/><font color='green'>f(z) = z ^ " + diyJuliaP + " + C</font><br/>");
		}
		
		this.addDiyJuliaUseDiffInfo();

		boolean diyJKConst = this.keepConst;
		if (diyJKConst) {
			this.formulaArea.append("<br/>Dynamic Constant = z</font>");
			ff = new Julia(diyJuliaP, diyJuliaUseD, diyJuliaBd, diyJKConst);
		} else {
			double diyJuliaRealVal = Double.parseDouble(this.diyJuliaRealTf.getText());
			double diyJuliaImgVal = Double.parseDouble(this.diyJuliaImgTf.getText());
			this.formulaArea.append("<br/>Constant = " + diyJuliaRealVal + " + (" + diyJuliaImgVal + " * i)</font>");
			ff = new Julia(diyJuliaP, diyJuliaUseD, diyJuliaBd, diyJuliaRealVal, diyJuliaImgVal);
		}
		
		if (useCP) {
			ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setUseBlackWhite(useBw);
			} else {
				ff.setUseColorPalette(useCP);
			}
		}
		ff.setUseFuncConst(func);
		ff.setRotation(rot);
		
		if (!diyJApplyField.equals("None")) {
			if (diyJApplyFatou) {
				ff.setFatou(diyJApplyFatou);
				ff.setZSq(false);
				ff.setClassicJulia(false);
			} else if (diyJApplyZSq) {
				ff.setZSq(diyJApplyZSq);
				ff.setFatou(false);
				ff.setClassicJulia(false);
			} else if (diyJApplyClassic) {
				ff.setClassicJulia(diyJApplyClassic);
				ff.setFatou(false);
				ff.setZSq(false);
			}
		} else {
			ff.setFatou(false);
			ff.setZSq(false);
			ff.setClassicJulia(false);

		}
		
		FractalBase.setMaxIter(diyJuliaMaxIt);
		FractalBase.setAreaSize(diyJuliaLoopLt);
		FractalBase.setxC(diyJXc);
		FractalBase.setxC(diyJYc);
		FractalBase.setScaleSize(diyJScale);
		
		this.addJuliaConstInfo(ff);
		return ff;
	}

	private FractalBase createDIYMandelbrot() {
		FractalBase ff;
		// for diy mandelbrot

		boolean useCP = this.colrPRb.isSelected();
		boolean useBw = this.colrBwRb.isSelected();
		String func = this.constFuncChoice;

		double rot = this.getRotation();
		int diyMandLoopLt = this.mandSize;
		int diyMag = this.getDiyMandMagnification();
		int diyMandExp = this.getDiyMandExponent();
		boolean diyMandUseD = this.getDiyMandUseDiff();
		boolean diyMKConst = this.keepConst;
		int diyMaxIt = this.diyMandMaxIter;
		double diyMandB = this.diyMandBound;
		double diyMXc = this.diyMandXC;
		double diyMYc = this.diyMandYC;
		double diyMScale = this.diyMandScaleSize;

		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>(DIY)Mandelbrot Set:<br/><br/>f(z) = z ^ " + diyMandExp + " + C");
		this.addDiyMandelbrotUseDiffInfo();
		
		if (diyMKConst) {
			this.formulaArea.append("<br/>Dynamic Constant = z</font>");
			ff = new Mandelbrot(diyMag, diyMandExp, diyMandUseD,diyMandB, diyMKConst);			
		} else {
			double diyMRealVal = Double.parseDouble(this.diyMandRealTf.getText());
			double diyMImgVal = Double.parseDouble(this.diyMandImgTf.getText());
			this.formulaArea.append("<br/>Constant = " + diyMRealVal + " + (" + diyMImgVal + " * i)</font>");
			ff = new Mandelbrot(diyMag, diyMandExp, diyMandUseD,diyMandB, diyMRealVal, diyMImgVal);
		}
		
		if (useCP) {
			ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setUseBlackWhite(useBw);
			} else {
				ff.setUseColorPalette(useCP);
			}
		}
		ff.setUseFuncConst(func);
		ff.setRotation(rot);
		FractalBase.setMaxIter(diyMaxIt);
		FractalBase.setAreaSize(diyMandLoopLt);
		FractalBase.setxC(diyMXc);
		FractalBase.setxC(diyMYc);
		FractalBase.setScaleSize(diyMScale);

		this.addMandelbrotConstInfo(ff);
		return ff;
	}

	private FractalBase startJulia() {
		int pow = this.getPower();
		double con = this.getComplexConst();
		String comp = this.getComplex();
		boolean jUseD = this.getJUseDiff();
		int juliaMax = this.juliaMaxIter;
		int juliaLoopLt = this.juliaSize;
		double jBound = this.juliaBound;// this.getJuliaBound();
		
		double jXc = this.juliaXC;
		double jYc = this.juliaYC;
		double jScale = this.juliaScaleSize;
		
		String jApplyField = this.fieldLines;
		boolean jApplyFatou = this.applyFatou;
		boolean jApplyZSq = this.applyZSq;
		boolean jApplyClassic = this.applyClassicJulia;

		boolean useCP = this.colrPRb.isSelected();
		boolean useBw = this.colrBwRb.isSelected();
		
		String func = this.constFuncChoice;
		
		double rot = this.getRotation();
		FractalBase ff;
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.addJuliaFormulaInfo();
		this.addJuliaUseDiffInfo();
		if (!(this.juliaSelection.equals("J7") || this.juliaSelection.equals("J8")
				|| this.juliaSelection.equals("J9"))) {
			ff = new Julia(pow, con, jBound, jUseD);
			
		} else {
			ff = new Julia(pow, comp, jBound, jUseD);
		}
		
		if (useCP) {
			ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setUseBlackWhite(useBw);
			} else {
				ff.setUseColorPalette(useCP);
			}
		}
		ff.setUseFuncConst(func);
		ff.setRotation(rot);
		
		if (!jApplyField.equals("None")) {
			if (jApplyFatou) {
				ff.setFatou(jApplyFatou);
				ff.setZSq(false);
				ff.setClassicJulia(false);
			} else if (jApplyZSq) {
				ff.setZSq(jApplyZSq);
				ff.setFatou(false);
				ff.setClassicJulia(false);
			} else if (jApplyClassic) {
				ff.setClassicJulia(jApplyClassic);
				ff.setFatou(false);
				ff.setZSq(false);
			}
		} else {
			ff.setFatou(false);
			ff.setZSq(false);
			ff.setClassicJulia(false);

		}
		
		FractalBase.setxC(jXc);
		FractalBase.setxC(jYc);
		FractalBase.setScaleSize(jScale);
		FractalBase.setMaxIter(juliaMax);
		FractalBase.setAreaSize(juliaLoopLt);

		this.addJuliaConstInfo(ff);
		return ff;
	}

	private FractalBase startMandelbrot() {
		int mag = this.getMagnification();
		int exp = this.getExponent();
		boolean mUseD = this.getMUseDiff();
		int mandMax = this.mandMaxIter;
		int mandLoopLt = this.mandSize;
		double mBound=this.mandBound;
		double mXc = this.mandXC;
		double mYc = this.mandYC;
		double mScale = this.mandScaleSize;
		boolean useCP = this.colrPRb.isSelected();
		boolean useBw = this.colrBwRb.isSelected();		
		String func = this.constFuncChoice;	
		double rot = this.getRotation();
		
		FractalBase ff;
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>Mandelbrot Set:<br/><br/>f(z) = z ^ " + exp + " + C");
		this.addMandelbrotUseDiffInfo();

		ff = new Mandelbrot(mag, exp, mUseD, mBound, true);
		if (useCP) {
			ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setUseBlackWhite(useBw);
			} else {
				ff.setUseColorPalette(useCP);
			}
		}
		ff.setUseBlackWhite(useBw);
		ff.setUseFuncConst(func);
		ff.setRotation(rot);
		FractalBase.setxC(mXc);
		FractalBase.setxC(mYc);
		FractalBase.setScaleSize(mScale);
		FractalBase.setMaxIter(mandMax);
		FractalBase.setAreaSize(mandLoopLt);

		this.addMandelbrotConstInfo(ff);
		return ff;
	}

	private FractalBase startPoly() {

		boolean useCP = this.colrPRb.isSelected();
		boolean useBw = this.colrBwRb.isSelected();	
		String func = this.constFuncChoice;
		double rot = this.getRotation();
		
		FractalBase ff;
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>Poynomial Set:<br/><br/>f(z) = (x ^ " + this.polyPower + " + y ^ " + this.polyPower + ") + C"+
				"<br/>  x = Row + 0 * i , y = 0 + Column * i");
		
		if (this.polyType.equals("Reverse")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, y)<br/>");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(y, x)<br/>");
		} else if (this.polyType.equals("Exchange")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, 0.0)<br/>");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(0.0, y)<br/>");
		} else if (this.polyType.equals("Single")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, y)<br/>");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(0.0, 0.0)<br/>");
		} else if (this.polyType.equals("Duplicate")) {
			this.formulaArea.append("<br/><br/>Zx = Zy = new ComplexNumber(x, y)<br/>");
		} else if (this.polyType.equals("Exponent")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, 0.0).exp()<br/>");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(y, 0.0).exp()<br/>");
		} else if (this.polyType.equals("Power")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, y).power((int)x)<br/>");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(y, x).power((int)y)<br/>");
		} else if (this.polyType.equals("Default")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, 0.0)<br/>");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(y, 0.0)<br/>");
		}
		this.addPolyUseDiffInfo();

		if (this.keepConst) {
			this.formulaArea.append("<br/>Dynamic Constant = z</font>");
			ff = new PolyFract(this.polyPower, this.polyUseDiff, this.polyBound, this.keepConst);
		}else{
			double polyRealVal = Double.parseDouble(this.polyRealTf.getText());
			double polyImgVal = Double.parseDouble(this.polyImgTf.getText());
			this.formulaArea.append("<br/>Constant = " + polyRealVal + " + (" + polyImgVal + " * i)</font>");
			
			ff = new PolyFract(this.polyPower, this.polyUseDiff, this.polyBound,/* this.keepConst,*/ polyRealVal, polyImgVal);
		}
		
		if (useCP) {
			ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setUseBlackWhite(useBw);
			} else {
				ff.setUseColorPalette(useCP);
			}
		}
		ff.setUseFuncConst(func);
		ff.setRotation(rot);
		ff.setRowColMixType(this.polyType);
		FractalBase.setxC(this.polyXC);
		FractalBase.setxC(this.polyYC);
		FractalBase.setScaleSize(this.polyScaleSize);
		FractalBase.setMaxIter(this.polyMaxIter);
		FractalBase.setAreaSize(this.polySize);

		this.addPolyConstInfo(ff);
		return ff;
	}	

	private void startFractals(final FractalBase ff) {
//		this.startProgress();
		final FractalBase frame = ff;
		frame.setTitle(ff.getFractalShapeTitle());
		
		if (!(this.diyApolloRb.isSelected() || this.getComboChoice().equals(APOLLONIAN_CIRCLES))) {
			frame.setSize(FractalBase.getAreaSize(), FractalBase.getAreaSize());//(FractalBase.WIDTH, FractalBase.HEIGHT);
		}
		
		frame.setDefaultCloseOperation(closeIt(frame));
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth())/2, 
	            (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight())/2);
		frame.setResizable(false);
		frame.setVisible(true);

		this.setFractalImage(frame.getBufferedImage());
		this.setFractalBase(frame);
		
		this.buClose.setEnabled(true);
		
		/*
		if(this.doMagnify){
			new ZoomInBox(frame);
		}*/
		
		boolean staticFractalChoice = ( this.comboChoice.contains(MANDELBROT) || this.comboChoice.contains(JULIA) || this.comboChoice.contains(POLY) ||
				(this.comboChoice.contains("self")&&!(this.diyApolloRb.isSelected() || this.getComboChoice().equals(APOLLONIAN_CIRCLES))));
		if(!staticFractalChoice) {
			frame.setRunning(true);
//System.out.println("this.comboChoice--"+this.comboChoice+"isThread");
			/*if(this.doMagnify){
				new ZoomInBox(frame);
			}*/
			this.formulaArea.setVisible(false);
			this.fbf = new Thread(frame);
			this.fbf.start();
			
			this.setFractalImage(frame.getBufferedImage());
			this.setFractalBase(frame);
			
			this.buClose.setEnabled(true);
			return;
		}
		

		if (this.doMagnify) {
			this.setFractalImage(frame.getBufferedImage());
			new ZoomInBox(frame);
		}
		return;
		
//		this.endProgress();
	}

	private int closeIt(FractalBase frame) {
		this.buStart.setEnabled(false);
		if (!(this.comboChoice.equals(JULIA)||this.comboChoice.equals(MANDELBROT)||this.comboChoice.equals(POLY))) {
			this.formulaArea.setVisible(false);
		}
		/*frame.reset();*/
		frame.dispose();
		return JFrame.DISPOSE_ON_CLOSE;
	}
	
	private void doPauseCommand(Thread fb){
		fb.interrupt();
	}
	
	private void doSaveImageCommand() {
		BufferedImage fractalImage = this.getFractalImage();
		
		String imgBaseInfo = this.getImgBaseInfo();
		String extraInfo = this.getExtraInfo();
		
		BufferedImage baseInfoImg = this.createImageInfo(imgBaseInfo);
        
        
		String imageFilePath="images\\"+this.getComboChoice()+"["+extraInfo+"]"+" ____"+System.currentTimeMillis()+".png";
		File outputfile = new File(imageFilePath);
	    try {
			ImageIO.write(FractalBase.joinBufferedImage(fractalImage,baseInfoImg), "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage createImageInfo(final String info) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        /*int width = this.getFractalImage().getWidth();
        int height = 300;*/
        g2d.dispose();

		JLabel textLabel = new JLabel(info);
		Dimension d = textLabel.getPreferredSize();
		textLabel.setSize(d);
		
		img = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);

		g2d = img.createGraphics();
        g2d.setColor(new Color(255, 255, 255, 128));
        g2d.fillRoundRect(0, 0, this.getFractalImage().getWidth(), 300, 15, 10);
        g2d.setColor(Color.black);
        textLabel.paint(g2d);
        
        g2d.dispose();
        
		return img;
	}
	
	private String getImgBaseInfo() {
		String eol = "<br/>";
		
		String choice = this.getComboChoice();
		String baseInfo = "<html><font color='red'>"+choice + " {";
		switch(choice){
			case	FANNY_CIRCLE :	
				int lengthFc = this.getSideComboChoice();
				int ratioFc = this.getRatioChoice();
	
				baseInfo += "Length(" + lengthFc + "), ";
				baseInfo += "Ratio(" + ratioFc + ") }";
				break;
			case	FANNY_TRIANGLES :	
				int lengthFt = this.getSideComboChoice();
				int ratioFt = this.getRatioChoice();
	
				baseInfo += "Length(" + lengthFt + "), ";
				baseInfo += "Ratio(" + ratioFt + ") }";
				break;
				
			case	SIERPINSKI_TRIANGLES	:
				baseInfo += "}";
				break;
				
			case	APOLLONIAN_CIRCLES	:
				double[] cChoices = this.getCurvChoice();
				double mXt = this.getMultiplier();
				
				baseInfo += "Radii: C1(" + cChoices[0] + "), C2(" + cChoices[1] + "), C3(" + cChoices[2] + ")" + eol;
				baseInfo+=" Multiplier("+mXt+") }";
				break;
			
			case	SAMPLE	:
				baseInfo+="}";
				break;
				
			case	CST_FRACTAL	:
				baseInfo+="}";
				break;
				
			case	KOCHSNOWFLAKE	:
				baseInfo += "Filled Circumscribed Triangles: " + this.kochFillExternals + eol;
				baseInfo += "Mixed 2-colors: " + this.kochMixColors + eol;
				baseInfo += "Spread External: " + this.kochSpreadOuter + eol;
				
				baseInfo+="}";
				break;
				
			case 	POLY:
				if (this.colrPRb.isSelected()) {
					baseInfo += "ColorPalette, " + eol;
				} else if (this.colrCRb.isSelected()) {
					baseInfo += "ColorComputed, " + eol;
				} else if (this.colrBwRb.isSelected()) {
					baseInfo += "BlackWhite, " + eol;
				}
				baseInfo += "Power: " + this.polyPower + ", ";
				if (this.polyUseDiff) {
					baseInfo += "Ud, "+eol;
				}
				baseInfo += "Boundary: " + this.polyBound + ", " + eol;
				if (this.keepConst) {
					baseInfo += "Dynamic Constant	C = z"+eol;
				} else {
					baseInfo += "Real Value = " + Double.parseDouble(this.polyRealTf.getText()) + "," + eol;
					baseInfo += "Imaginary Value = " + Double.parseDouble(this.polyImgTf.getText()) + eol;
				}
				if (!(this.constFuncChoice.equals("None") || this.noFuncRb.isSelected())) {
					if (this.sineRb.isSelected()) {
						baseInfo += "Sine of Constant	sine(C)" + eol;
					} else if (this.cosineRb.isSelected()) {
						baseInfo += "Cosine of Constant		cos(C)" + eol;
					} else if (this.tanRb.isSelected()) {
						baseInfo += "Tan of Constant	tan(C)" + eol;
					} else if (this.arcSineRb.isSelected()) {
						baseInfo += "ArcSine of Constant	arcsin(C)" + eol;
					} else if (this.arcCosineRb.isSelected()) {
						baseInfo += "ArcCosine of Constant	arccos(C)" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "ArcTan of Constant		arctan(C)" + eol;
					} else if (this.sqRb.isSelected()) {
						baseInfo += "Square of Constant		(C ^ 2)" + eol;
					} else if (this.cuRb.isSelected()) {
						baseInfo += "Cube of Constant		(C ^ 3)" + eol;
					} else if (this.expRb.isSelected()) {
						baseInfo += "Exponent of Constant	(e ^ C)" + eol;
					} else if (this.rootRb.isSelected()) {
						baseInfo += "Square Root of Constant	(C ^ (1/2))" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "Cube Root of Constant		(C ^ (1/3))" + eol;
					} else if (this.lnRb.isSelected()) {
						baseInfo += "Log(e) of Constant		Ln(C)" + eol;
					}
				}
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Row-Column Mix-Type: " + this.polyType + eol;
				baseInfo += "Center: P(x,y): (" + this.polyXC + ", " + this.polyYC + ")" + eol;
				baseInfo += "Maximum Iterations: " + this.polyMaxIter + eol;
				baseInfo += " Scaled Size: " + this.polyScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
			
			case	MANDELBROT	:
				if (this.colrPRb.isSelected()) {
					baseInfo += "ColorPalette, " + eol;
				} else if (this.colrCRb.isSelected()) {
					baseInfo += "ColorComputed, " + eol;
				} else if (this.colrBwRb.isSelected()) {
					baseInfo += "BlackWhite, " + eol;
				}
				baseInfo += "ImageMagnification: " + this.magnification + eol;
				baseInfo += "Power: " + this.exponent + ", ";
				if (this.getMUseDiff()) {
					baseInfo += "Ud, ";
				}
				baseInfo += "Boundary: " + this.mandBound + eol;
				if (!(this.constFuncChoice.equals("None") || this.noFuncRb.isSelected())) {
					if (this.sineRb.isSelected()) {
						baseInfo += "Sine of Constant" + eol;
					} else if (this.cosineRb.isSelected()) {
						baseInfo += "Cosine of Constant" + eol;
					} else if (this.tanRb.isSelected()) {
						baseInfo += "Tan of Constant" + eol;
					} else if (this.arcSineRb.isSelected()) {
						baseInfo += "ArcSine of Constant" + eol;
					} else if (this.arcCosineRb.isSelected()) {
						baseInfo += "ArcCosine of Constant" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "ArcTan of Constant" + eol;
					} else if (this.sqRb.isSelected()) {
						baseInfo += "Square of Constant		(C ^ 2)" + eol;
					} else if (this.cuRb.isSelected()) {
						baseInfo += "Cube of Constant		(C ^ 3)" + eol;
					} else if (this.expRb.isSelected()) {
						baseInfo += "Exponent of Constant	(e ^ C)" + eol;
					} else if (this.rootRb.isSelected()) {
						baseInfo += "Square Root of Constant	(C ^ (1/2))" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "Cube Root of Constant		(C ^ (1/3))" + eol;
					} else if (this.lnRb.isSelected()) {
						baseInfo += "Log(e) of Constant		Ln(C)" + eol;
					}
				}
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Center: P(x,y): (" + this.mandXC + ", " + this.mandYC + ")" + eol;
				baseInfo += "Maximum Iterations: " + this.mandMaxIter + eol;
				baseInfo += " Scaled Size: " + this.mandScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
				
			case	JULIA	:	
				if (this.colrPRb.isSelected()) {
					baseInfo += "ColorPalette, " + eol;
				} else if (this.colrCRb.isSelected()) {
					baseInfo += "ColorComputed, " + eol;
				} else if (this.colrBwRb.isSelected()) {
					baseInfo += "BlackWhite, " + eol;
				}
				baseInfo += "Power: " + this.polyPower + ", ";
				if (this.getJUseDiff()) {
					baseInfo += "Ud, ";
				}
				baseInfo += "Boundary: " + this.juliaBound + eol;
				if (!(this.constFuncChoice.equals("None") || this.noFuncRb.isSelected())) {
					if (this.sineRb.isSelected()) {
						baseInfo += "Sine of Constant" + eol;
					} else if (this.cosineRb.isSelected()) {
						baseInfo += "Cosine of Constant" + eol;
					} else if (this.tanRb.isSelected()) {
						baseInfo += "Tan of Constant" + eol;
					} else if (this.arcSineRb.isSelected()) {
						baseInfo += "ArcSine of Constant" + eol;
					} else if (this.arcCosineRb.isSelected()) {
						baseInfo += "ArcCosine of Constant" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "ArcTan of Constant" + eol;
					} else if (this.sqRb.isSelected()) {
						baseInfo += "Square of Constant		(C ^ 2)" + eol;
					} else if (this.cuRb.isSelected()) {
						baseInfo += "Cube of Constant		(C ^ 3)" + eol;
					} else if (this.expRb.isSelected()) {
						baseInfo += "Exponent of Constant	(e ^ C)" + eol;
					} else if (this.rootRb.isSelected()) {
						baseInfo += "Square Root of Constant	(C ^ (1/2))" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "Cube Root of Constant		(C ^ (1/3))" + eol;
					} else if (this.lnRb.isSelected()) {
						baseInfo += "Log(e) of Constant		Ln(C)" + eol;
					}
				}
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Center: P(x,y): (" + this.juliaXC + ", " + this.juliaYC + ")" + eol;
				baseInfo += "Maximum Iterations: " + this.juliaMaxIter + eol;
				baseInfo += " Scaled Size: " + this.juliaScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
				
			case	diyMand:
				baseInfo += MANDELBROT + eol;
				if (this.colrPRb.isSelected()) {
					baseInfo += "ColorPalette, " + eol;
				} else if (this.colrCRb.isSelected()) {
					baseInfo += "ColorComputed, " + eol;
				} else if (this.colrBwRb.isSelected()) {
					baseInfo += "BlackWhite, " + eol;
				}
				baseInfo += "ImageMagnification: " + this.diyMandMagnification + eol;
				baseInfo += "Power: " + this.diyMandExponent + ", ";
				if (this.getDiyMandUseDiff()) {
					baseInfo += "Ud, ";
				}
				baseInfo += "Boundary: " + this.diyMandBound + ", " + eol;
				if (this.keepConst) {
					baseInfo += "Dynamic Constant	Z=C" + eol;
				} else {
					baseInfo += "Real Value = " + Double.parseDouble(this.diyMandRealTf.getText()) + ","+eol;
					baseInfo += "Imaginary Value = " + Double.parseDouble(this.diyMandImgTf.getText()) + eol;
				}
				if (!(this.constFuncChoice.equals("None") || this.noFuncRb.isSelected())) {
					if (this.sineRb.isSelected()) {
						baseInfo += "Sine of Constant" + eol;
					} else if (this.cosineRb.isSelected()) {
						baseInfo += "Cosine of Constant" + eol;
					} else if (this.tanRb.isSelected()) {
						baseInfo += "Tan of Constant" + eol;
					} else if (this.arcSineRb.isSelected()) {
						baseInfo += "ArcSine of Constant" + eol;
					} else if (this.arcCosineRb.isSelected()) {
						baseInfo += "ArcCosine of Constant" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "ArcTan of Constant" + eol;
					} else if (this.sqRb.isSelected()) {
						baseInfo += "Square of Constant		(C ^ 2)" + eol;
					} else if (this.cuRb.isSelected()) {
						baseInfo += "Cube of Constant		(C ^ 3)" + eol;
					} else if (this.expRb.isSelected()) {
						baseInfo += "Exponent of Constant	(e ^ C)" + eol;
					} else if (this.rootRb.isSelected()) {
						baseInfo += "Square Root of Constant	(C ^ (1/2))" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "Cube Root of Constant		(C ^ (1/3))" + eol;
					} else if (this.lnRb.isSelected()) {
						baseInfo += "Log(e) of Constant		Ln(C)" + eol;
					}
				}
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Center: P(x,y): (" + this.diyMandXC + ", " + this.diyMandYC + ")" + eol;
				baseInfo += "Maximum Iterations: " + this.diyMandMaxIter + eol;
				baseInfo += " Scaled Size: " + this.diyMandScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
				
			case diyJulia:
				baseInfo += JULIA + eol;
				if (this.colrPRb.isSelected()) {
					baseInfo += "ColorPalette, " + eol;
				} else if (this.colrCRb.isSelected()) {
					baseInfo += "ColorComputed, " + eol;
				} else if (this.colrBwRb.isSelected()) {
					baseInfo += "BlackWhite, " + eol;
				}
				baseInfo += "Power: " + this.diyJuliaPower + ", ";
				if (this.getDiyJuliaUseDiff()) {
					baseInfo += "Ud, ";
				}
				baseInfo += "Boundary: " + this.diyJuliaBound + ", "+eol;
				if (this.keepConst) {
					baseInfo += "Dynamic Constant	Z=C" + eol;
				} else {
					baseInfo += "Real Value = " + Double.parseDouble(this.diyJuliaRealTf.getText()) + "," + eol;
					baseInfo += "Imaginary Value = " + Double.parseDouble(this.diyJuliaImgTf.getText()) + eol;
				}
				if (!(this.constFuncChoice.equals("None") || this.noFuncRb.isSelected())) {
					if (this.sineRb.isSelected()) {
						baseInfo += "Sine of Constant" + eol;
					} else if (this.cosineRb.isSelected()) {
						baseInfo += "Cosine of Constant" + eol;
					} else if (this.tanRb.isSelected()) {
						baseInfo += "Tan of Constant" + eol;
					} else if (this.arcSineRb.isSelected()) {
						baseInfo += "ArcSine of Constant" + eol;
					} else if (this.arcCosineRb.isSelected()) {
						baseInfo += "ArcCosine of Constant" + eol;
					} else if (this.arcTanRb.isSelected()) {
						baseInfo += "ArcTan of Constant" + eol;
					} else if (this.sqRb.isSelected()) {
						baseInfo += "Square of Constant		(C ^ 2)" + eol;
					} else if (this.cuRb.isSelected()) {
						baseInfo += "Cube of Constant		(C ^ 3)" + eol;
					} else if (this.expRb.isSelected()) {
						baseInfo += "Exponent of Constant	(e ^ C)" + eol;
					} else if (this.rootRb.isSelected()) {
						baseInfo += "Square Root of Constant	(C ^ (1/2))" + eol;
					} else if (this.lnRb.isSelected()) {
						baseInfo += "Log(e) of Constant		Ln(C)" + eol;
					}
				}
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Center: P(x,y): (" + this.diyJuliaXC + ", " + this.diyJuliaYC + ")" + eol;
				baseInfo += "Maximum Iterations: " + this.diyJuliaMaxIter + eol;
				baseInfo += " Scaled Size: " + this.diyJuliaScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
				
			case diyApollo:
				double c1 = this.diyApolloC1;
				double c2 = this.diyApolloC2;
				double c3 = this.diyApolloC3;
				double mult = this.diyApolloMult;
				
				baseInfo += APOLLONIAN_CIRCLES + eol;
				baseInfo += "Radii: C1(" + c1 + "), C2(" + c2 + "), C3(" + c3 + ")";
				baseInfo += " Multiplier(" + mult + ") }";
				break;
				
			default:
				baseInfo+="}";
		}
		
		return baseInfo+"</font></html>";		
	}
	
	private String getExtraInfo() {
		String choice = this.getComboChoice();
		String extra = "";
//		System.out.println("Choice--to--saveimage--" + choice);
		switch (choice) {
			case APOLLONIAN_CIRCLES:
				extra+=""+APOLLONIAN_CIRCLES+"_";
				double[] cc = this.getCurvChoice();
				extra+=	"C1["+cc[0]+"],C2["+cc[1]+"],C3["+cc[2]+"],Mul("+this.getMultiplier()+")";
				break;
			case POLY:
				extra += ""+POLY+"_";
				extra += "P("+this.polyPower+"),";
				extra += "B(" + this.polyBound + "),";
				extra += "RCMT( " + this.polyType + "),";
				
				extra += "MxIt(" + this.polyMaxIter + "),";
				extra += "Cx(" + this.polyXC + "),";
				extra += "Cy(" + this.polyYC + "),";
				extra += "Sz(" + this.polyScaleSize + ")";
				break;
			case MANDELBROT:
				extra += ""+MANDELBROT+"_";
				extra += "Z(" + this.magnification + "),";
				extra += "E(" + this.exponent + "),";
				extra += "B(" + this.mandBound + "),";
				extra += "MxIt(" + this.mandMaxIter + "),";
				extra += "Cx(" + this.mandXC + "),";
				extra += "Cy(" + this.mandYC + "),";
				extra += "Sz(" + this.mandScaleSize + ")";
				break;
			case JULIA:
				extra += ""+JULIA+"_";
				extra += "P("+this.power+"),";
				if(this.compConst!=0.0)
					extra+="C("+this.compConst+"),";
				else
					extra+="C("+this.complex+"),";
				extra += "B(" + this.juliaBound + "),";
				extra += "MxIt(" + this.juliaMaxIter + "),";
				extra += "Cx(" + this.juliaXC + "),";
				extra += "Cy(" + this.juliaYC + "),";
				extra += "Sz(" + this.juliaScaleSize + ")";
				break;
			case diyMand:
				extra += "DIY_" + MANDELBROT+"_";
				extra += "Z(" + this.diyMandMagnification + "),";
				extra += "E(" + this.diyMandExponent + "),";
				extra += "B(" + this.diyMandBound + "),";
				extra += "MxIt(" + this.diyMandMaxIter + "),";
				extra += "Cx(" + this.diyMandXC + "),";
				extra += "Cy(" + this.diyMandYC + "),";
				extra += "Sz(" + this.diyMandScaleSize + "),";
				if (this.keepConst) {
					extra += "CONST";
				} else {
					extra += "Real(" + this.diyMandConstReal + "),";
					extra += "Img(" + this.diyMandConstImg + ")";
				}
				break;
			case diyJulia:
				extra += "DIY_"+JULIA+"_";
				extra += "P("+this.diyJuliaPower+"),";
				
				extra += "B(" + this.diyJuliaBound + "),";
				extra += "MxIt(" + this.diyJuliaMaxIter + "),";
				extra += "Cx(" + this.diyJuliaXC + "),";
				extra += "Cy(" + this.diyJuliaYC + "),";
				extra += "Sz(" + this.diyJuliaScaleSize + "),";
				if (this.keepConst) {
					extra += "CONST";
				}else{
					extra += "Real(" + this.diyJuliaConstReal + "),";
					extra += "Img(" + this.diyJuliaConstImg + ")";
				}
				break;
			case diyApollo:
				extra+="DIY_"+APOLLONIAN_CIRCLES+"_";
				extra+="C1("+this.diyApolloC1+"),C2("+this.diyApolloC2+"),C3("+this.diyApolloC3+"),Mx("+this.diyApolloMult+")";
				break;
			default:
				extra="";
				break;
		}
		return extra;
	}

	private void doPrintCommand(){
		PrinterJob printJob = PrinterJob.getPrinterJob();
		BufferedImage image = this.getFractalImage();
		printJob.setPrintable(new Printable() {
			public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
				if (pageIndex != 0) {
					return NO_SUCH_PAGE;
				}
				graphics.drawImage(image, 0, 0, FractalBase.WIDTH, FractalBase.HEIGHT, null);
				return PAGE_EXISTS;
			}
		});
		try {
			printJob.print();
		} catch (PrinterException e1) {
			e1.printStackTrace();
		}
	}

	private void setUpListeners() {		
		this.combos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String comboOption = (String)cb.getSelectedItem();
				doSelectCombosCommand(comboOption);				
			}});

		this.diyMandRb.addActionListener(diyFractChoiceRbListener());		
		this.diyJuliaRb.addActionListener(diyFractChoiceRbListener());
		this.diyApolloRb.addActionListener(diyFractChoiceRbListener());
		
		this.setupFannyListeners();	
		this.setupSierpisnkiTListeners();
		this.setupKochListeners();
		this.setupApolloListeners();		
		this.setupJuliaListeners();		
		this.setupMandelbrotListeners();
		this.setupPolyListeners();
		
		//DIY-Listeners
		this.setupDIYMandelbrotListeners();
		this.setupDIYJuliaListeners();		
		this.setupDIYApolloListeners();		
		
		this.colrPRb.addActionListener(this.colorChoiceRbListener());
		this.colrCRb.addActionListener(this.colorChoiceRbListener());
		this.colrBwRb.addActionListener(this.colorChoiceRbListener());
		
		
		this.noFuncRb.addActionListener(this.funcCalcRbListener());
		this.sineRb.addActionListener(this.funcCalcRbListener());
		this.cosineRb.addActionListener(this.funcCalcRbListener());
		this.tanRb.addActionListener(this.funcCalcRbListener());
		this.arcSineRb.addActionListener(this.funcCalcRbListener());
		this.arcCosineRb.addActionListener(this.funcCalcRbListener());
		this.arcTanRb.addActionListener(this.funcCalcRbListener());

		this.sqRb.addActionListener(this.funcCalcRbListener());
		this.cuRb.addActionListener(this.funcCalcRbListener());
		this.expRb.addActionListener(this.funcCalcRbListener());
		this.rootRb.addActionListener(this.funcCalcRbListener());
		/*this.cuRootRb.addActionListener(this.funcCalcRbListener());*/
		this.lnRb.addActionListener(this.funcCalcRbListener());
		
		this.magnifyCb.setActionCommand("Magnify");
		this.magnifyCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doMagnify(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doMagnify(false);
				}
			}
        });
		
		this.rotateCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double rotationComboOption = (Double)cb.getSelectedItem();
				doSetRotationCombosCommand(rotationComboOption);				
			}});

		this.buStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStartCommand();				
			}});
		
		this.buClose.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				doCloseCommand();				
			}
		});
		

		this.buPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPauseCommand(fbf);				
			}
		});	
		
		this.buPrint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPrintCommand();				
			}
		});			

		this.buSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSaveImageCommand();				
			}
		});
	}

	private void setupDIYApolloListeners() {
		//////////////////////////////////
		//	apollo-diy
		//
		this.diyApolloC1Combos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyApolloC1ComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyApolloC1CombosCommand(diyApolloC1ComboOption);				
			}});
		this.diyApolloC2Combos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyApolloC2ComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyApolloC2CombosCommand(diyApolloC2ComboOption);				
			}});
		this.diyApolloC3Combos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyApolloC3ComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyApolloC3CombosCommand(diyApolloC3ComboOption);				
			}});
		this.diyApolloMultCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyApolloMultComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyApolloMultCombosCommand(diyApolloMultComboOption);				
			}});
		
		/*this.diyApolloColrPRb.addActionListener(colorChoiceRbListener());
		this.diyApolloColrCRb.addActionListener(colorChoiceRbListener());
		*/
		//////////////	ends	apollo-duy	/////////////////
	}

	private void setupDIYJuliaListeners() {
		////////////////////////////////////////
		//		julia-diy
		//
		this.diyJuliaPowerCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyJuliaPowerComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyJuliaPowerCombosCommand(diyJuliaPowerComboOption);				
			}});
		
		this.diyJuliaUseDiffCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyJuliaUseDiffCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyJuliaUseDiffCommand(false);
				}
			}
        });/*
		this.diyJuliaApplyFatouCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyJuliaApplyFatouCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyJuliaApplyFatouCommand(false);
				}
			}
        });*/
		

		
		this.diyJuliaFieldNoneRB.addActionListener(this.fieldLinesRBListener());
		this.diyJuliaFieldFatouRB.addActionListener(this.fieldLinesRBListener());
		this.diyJuliaFieldZSqRB.addActionListener(this.fieldLinesRBListener());
		this.diyJuliaFieldClassicRB.addActionListener(this.fieldLinesRBListener());
		
		this.diyJuliaKeepConstRb.addActionListener(this.keepConstRbListener());
		this.diyJuliaCreateConstRb.addActionListener(this.keepConstRbListener());
		
		this.diyJuliaMaxIterCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyJuliaMaxIterComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyJuliaMaxIterCombosCommand(diyJuliaMaxIterComboOption);				
			}});
		
		this.diyJuliaSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyJuliaLoopLimitComboOption = (Integer)cb.getSelectedItem();
				doSelectJuliaLoopLimitCombosCommand(diyJuliaLoopLimitComboOption);				
			}});		

		this.diyJuliaBoundCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyJuliaBoundComboOption = (Double)cb.getSelectedItem();
				doSelectDiyJuliaBoundCombosCommand(diyJuliaBoundComboOption);				
			}});
		
		this.diyJuliaXCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyJuliaXCComboOption = (Double)cb.getSelectedItem();
				doSelectDiyJuliaXCCombosCommand(diyJuliaXCComboOption);				
			}});
		
		
		this.diyJuliaYCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyJuliaYCComboOption = (Double)cb.getSelectedItem();
				doSelectDiyJuliaYCCombosCommand(diyJuliaYCComboOption);				
			}});		
		
		this.diyJuliaScaleSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyJuliaScaleSizeComboOption = (Double)cb.getSelectedItem();
				doSelectDiyJuliaScaleSizeCombosCommand(diyJuliaScaleSizeComboOption);				
			}});
		
		/*this.diyJuliaColrPRb.addActionListener(colorChoiceRbListener());
		this.diyJuliaColrCRb.addActionListener(colorChoiceRbListener());*/
	}

	private void setupDIYMandelbrotListeners() {
		///////////////////////////////////////////////
		//		diy--mandelbrot
		//
		this.diyMandMagCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyMandComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyMandMagnificationCombosCommand(diyMandComboOption);				
			}});
		
		this.diyMandExpCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyMandExpComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyMandExponentCombosCommand(diyMandExpComboOption);				
			}});
		
		this.diyMandUseDiffCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyMandUseDiffCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyMandUseDiffCommand(false);
				}
			}
        });

		this.diyMandelbrotKeepConstRb.addActionListener(this.keepConstRbListener());
		this.diyMandelbrotCreateConstRb.addActionListener(this.keepConstRbListener());
		
		this.diyMandMaxIterCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyMandMaxIterComboOption = (Integer)cb.getSelectedItem();
				doSelectDiyMandMaxIterCombosCommand(diyMandMaxIterComboOption);				
			}});
		
		this.diyMandSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer diyMandLoopLimitComboOption = (Integer)cb.getSelectedItem();
				doSelectMandLoopLimitCombosCommand(diyMandLoopLimitComboOption);				
			}});
		

		this.diyMandBoundCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyMandBoundComboOption = (Double)cb.getSelectedItem();
				doSelectDiyMandBoundCombosCommand(diyMandBoundComboOption);				
			}});

		this.diyMandXCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyMandXCComboOption = (Double)cb.getSelectedItem();
				doSelectDiyMandXCCombosCommand(diyMandXCComboOption);				
			}});
		
		this.diyMandYCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyMandYCComboOption = (Double)cb.getSelectedItem();
				doSelectDiyMandYCCombosCommand(diyMandYCComboOption);				
			}});
		
		this.diyMandScaleSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyMandScaleSizeComboOption = (Double)cb.getSelectedItem();
				doSelectDiyMandScaleSizeCombosCommand(diyMandScaleSizeComboOption);				
			}});
		
		/*this.diyMandColrPRb.addActionListener(colorChoiceRbListener());
		this.diyMandColrCRb.addActionListener(colorChoiceRbListener());*/
	}
	
	private void setupPolyListeners() {
		//////////////////////////////////////////////////
		// POLY

		this.polyExpCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>) e.getSource();
				Integer polyExpComboOption = (Integer) cb.getSelectedItem();
				doSelectPolyExponentCombosCommand(polyExpComboOption);
			}
		});

		this.polyUseDiffCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetPolyUseDiffCommand(true);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSetPolyUseDiffCommand(false);
				}
			}
		});

		this.polyMaxIterCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>) e.getSource();
				Integer polyMaxIterComboOption = (Integer) cb.getSelectedItem();
				doSelectPolyMaxIterCombosCommand(polyMaxIterComboOption);
			}
		});

		this.polyKeepConstRb.addActionListener(this.keepConstRbListener());
		this.polyCreateConstRb.addActionListener(this.keepConstRbListener());
		
		this.polyTypeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String polyTypeComboOption = (String) cb.getSelectedItem();
				doSelectPolyTypeCombosCommand(polyTypeComboOption);
			}
		});
		
		this.polySizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>) e.getSource();
				Integer polyLoopLimitComboOption = (Integer) cb.getSelectedItem();
				doSelectPolyLoopLimitCombosCommand(polyLoopLimitComboOption);
			}
		});

		this.polyBoundCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double polyBoundComboOption = (Double) cb.getSelectedItem();
				doSelectPolyBoundCombosCommand(polyBoundComboOption);
			}
		});

		this.polyXCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double polyXCComboOption = (Double) cb.getSelectedItem();
				doSelectPolyXCCombosCommand(polyXCComboOption);
			}
		});

		this.polyYCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double polyYCComboOption = (Double) cb.getSelectedItem();
				doSelectPolyYCCombosCommand(polyYCComboOption);
			}
		});

		this.polyScaleSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double polyScaleSizeComboOption = (Double) cb.getSelectedItem();
				doSelectPolyScaleSizeCombosCommand(polyScaleSizeComboOption);
			}
		});
	}

	private void setupMandelbrotListeners() {
		//////////////////////////////////////////////////
		//		MANDELBROT
		
		this.mandCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer mandComboOption = (Integer)cb.getSelectedItem();
				doSelectMandelbrotCombosCommand(mandComboOption);				
			}});
		
		this.mandExpCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer mandExpComboOption = (Integer)cb.getSelectedItem();
				doSelectMandExponentCombosCommand(mandExpComboOption);				
			}});
		
		this.mandUseDiffCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetMandUseDiffCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetMandUseDiffCommand(false);
				}
			}
        });
		
		this.mandMaxIterCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer mandMaxIterComboOption = (Integer)cb.getSelectedItem();
				doSelectMandMaxIterCombosCommand(mandMaxIterComboOption);				
			}});
		

		
		this.mandSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer mandLoopLimitComboOption = (Integer)cb.getSelectedItem();
				doSelectMandLoopLimitCombosCommand(mandLoopLimitComboOption);				
			}});

		this.mandBoundCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double mandBoundComboOption = (Double)cb.getSelectedItem();
				doSelectMandBoundCombosCommand(mandBoundComboOption);				
			}});

		this.mandXCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double mandXCComboOption = (Double)cb.getSelectedItem();
				doSelectMandXCCombosCommand(mandXCComboOption);				
			}});
		
		this.mandYCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double mandYCComboOption = (Double)cb.getSelectedItem();
				doSelectMandYCCombosCommand(mandYCComboOption);				
			}});
		
		this.mandScaleSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double mandScaleSizeComboOption = (Double)cb.getSelectedItem();
				doSelectMandScaleSizeCombosCommand(mandScaleSizeComboOption);				
			}});
		
		/*this.mandColrPRb.addActionListener(colorChoiceRbListener());
		this.mandColrCRb.addActionListener(colorChoiceRbListener());*/
		///////////////////	endsmandelbrot	//////////////////////
		//////////////////////////////////////////////////////////
	}

	private void setupJuliaListeners() {
		///////////////////////////////////////
		//		JULIA
		//
		this.juliaCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String juliaComboOption = (String)cb.getSelectedItem();
				doSelectJuliaCombosCommand(juliaComboOption);				
			}});
		
		this.juliaUseDiffCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetJuliaUseDiffCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetJuliaUseDiffCommand(false);
				}
			}
        });
		
		this.juliaFieldNoneRB.addActionListener(this.fieldLinesRBListener());
		this.juliaFieldFatouRB.addActionListener(this.fieldLinesRBListener());
		this.juliaFieldZSqRB.addActionListener(this.fieldLinesRBListener());
		this.juliaFieldClassicRB.addActionListener(this.fieldLinesRBListener());
		
		/*this.juliaApplyFatouCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetJuliaApplyFatouCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetJuliaUseDiffCommand(false);
				}
			}
        });*/
		
		this.juliaMaxIterCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer juliaMaxIterComboOption = (Integer)cb.getSelectedItem();
				doSelectJuliaMaxIterCombosCommand(juliaMaxIterComboOption);				
			}});
		
		this.juliaSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer juliaLoopLimitComboOption = (Integer)cb.getSelectedItem();
				doSelectJuliaLoopLimitCombosCommand(juliaLoopLimitComboOption);				
			}});
		
		this.juliaBoundCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double juliaBoundComboOption = (Double)cb.getSelectedItem();
				doSelectJuliaBoundCombosCommand(juliaBoundComboOption);				
			}});
		
		this.juliaXCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double juliaXCComboOption = (Double)cb.getSelectedItem();
				doSelectJuliaXCCombosCommand(juliaXCComboOption);				
			}});
		
		this.juliaYCCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double juliaYCComboOption = (Double)cb.getSelectedItem();
				doSelectJuliaYCCombosCommand(juliaYCComboOption);				
			}});
		
		this.juliaScaleSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double juliaScaleSizeComboOption = (Double)cb.getSelectedItem();
				doSelectJuliaScaleSizeCombosCommand(juliaScaleSizeComboOption);				
			}});

		/*this.juliaColrPRb.addActionListener(colorChoiceRbListener());
		this.juliaColrCRb.addActionListener(colorChoiceRbListener());*/
		///////////////////	ends	julia	/////////////////////
		
	}

	private void setupApolloListeners() {
		//////////////////////////////////////////////////
		//		APOLLO
		//
		this.curvCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String curvComboOption = (String)cb.getSelectedItem();
				doSelectCurvCombosCommand(curvComboOption);				
			}}
		);		
		
	}
	
	private void setupKochListeners() {
		this.kochFillExternalCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetKochFillExternalCommand(true);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSetKochFillExternalCommand(false);
				}
			}
		});

		this.kochMixColorsCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetKochMixColorsCommand(true);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSetKochMixColorsCommand(false);
				}
			}
		});

		this.kochSpreadOuterCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetKochSpreadOuterCommand(true);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSetKochSpreadOuterCommand(false);
				}
			}
		});
	}
	
	private void setupSierpisnkiTListeners(){
		this.sierpinskiTFillInnerCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetSierpinskiTFillInnerCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetSierpinskiTFillInnerCommand(false);
				}
			}
        });
		
		this.sierpTUpRb.addActionListener(this.sierpTDirRbListener());
		this.sierpTDnRb.addActionListener(this.sierpTDirRbListener());
	}

	private void setupFannyListeners() {
		this.sideCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
		        Integer sideComboOption = (Integer)cb.getSelectedIndex();
		        int sideVal = cb.getItemAt(sideComboOption);
				doSelectSideCombosCommand(sideVal);				
			}
		});
		
		this.ratioCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
		        @SuppressWarnings("unused")
				Integer ratioComboOption = (Integer)cb.getSelectedIndex();
		        int ratioVal = cb.getItemAt(ratioComboOption);
				doSelectRatioCombosCommand(ratioVal);				
			}
		});
	}
	
	
	private ActionListener colorChoiceRbListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doFractalColorChoice();			
			}
		};
	}
	
	private ActionListener fieldLinesRBListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doApplyFieldLinesChoice();			
			}
		};
	}
	
	private void doApplyFieldLinesChoice() {
		if (this.juliaFieldFatouRB.isSelected() || this.diyJuliaFieldFatouRB.isSelected()) {
			this.fieldLines = "Fatou";
			this.applyFatou = true;
			this.applyZSq = false;
			this.applyClassicJulia = false;
		} else if (this.juliaFieldZSqRB.isSelected() || this.diyJuliaFieldZSqRB.isSelected()) {
			this.fieldLines = "ZSq";
			this.applyFatou = false;
			this.applyZSq = true;
			this.applyClassicJulia = false;
		} else if (this.juliaFieldClassicRB.isSelected() || this.diyJuliaFieldClassicRB.isSelected()) {
			this.fieldLines = "ClassicJulia";
			this.applyFatou = false;
			this.applyZSq = false;
			this.applyClassicJulia = true;
		} else if (this.juliaFieldNoneRB.isSelected() || this.diyJuliaFieldNoneRB.isSelected()) {
			this.fieldLines = "None";
			this.applyFatou = false;
			this.applyZSq = false;
			this.applyClassicJulia = false;
		} else {
			this.fieldLines = "None";
			this.applyFatou = false;
			this.applyZSq = false;
			this.applyClassicJulia = false;
		}
	}
	

	private ActionListener funcCalcRbListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doConstFunctionalSelectionChoice();
			}
		};
	}
	
	private void doFractalColorChoice() {
		if (this.colrCRb.isSelected()) {
			this.setUseColorPalette(true);
			this.setUseBlackAndWhite(false);
		} else if (this.colrCRb.isSelected()) {
			this.setUseColorPalette(false);
			this.setUseBlackAndWhite(false);
		} else if (this.colrBwRb.isSelected()) {
			this.setUseColorPalette(false);
			this.setUseBlackAndWhite(true);
		} 
	}
	
	private void doConstFunctionalSelectionChoice() {
		if (this.noFuncRb.isSelected()) {
			this.setConstFuncChoice("None");
		} else if (this.sineRb.isSelected()) {
			this.setConstFuncChoice("Sine");
		} else if (this.cosineRb.isSelected()) {
			this.setConstFuncChoice("Cosine");
		} else if (this.tanRb.isSelected()) {
			this.setConstFuncChoice("Tan");
		} else if (this.arcSineRb.isSelected()) {
			this.setConstFuncChoice("ArcSine");
		} else if (this.arcCosineRb.isSelected()) {
			this.setConstFuncChoice("ArcCosine");
		} else if (this.arcTanRb.isSelected()) {
			this.setConstFuncChoice("ArcTan");
		} else if (this.sqRb.isSelected()) {
			this.setConstFuncChoice("Square");
		} else if (this.cuRb.isSelected()) {
			this.setConstFuncChoice("Cube");
		} else if (this.expRb.isSelected()) {
			this.setConstFuncChoice("Exponent");
		} else if (this.rootRb.isSelected()) {
			this.setConstFuncChoice("Root");
		} else if (this.lnRb.isSelected()) {
			this.setConstFuncChoice("Log");
		}
	}

	private void setUseColorPalette(boolean b) {
		this.useColorPalette = b;
	}

	private void setUseBlackAndWhite(boolean b) {
		this.useBlackAndWhite = b;
	}
	
	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(double rot) {
		this.rotation = rot;
	}

	private void doMagnify(boolean mag) {
		this.doMagnify=mag;
		/*if(mag){
			new ZoomInBox(this.ff);
		}*/
		/*if (mag) {
			this.doMagnify=true;
			while (this.doMagnify) {
				Point p = MouseInfo.getPointerInfo().getLocation();
				int x = p.x;
				int y = p.y;
				System.out.println("[doMagnify] x is " + x + " and y is " + y);
				try {
					Robot r = new Robot();
					BufferedImage i = r.createScreenCapture(new Rectangle(x - 30, y - 30, 150, 150));
					Graphics gg = i.getGraphics();

					// draw the image 
					gg.drawImage(i, 0, 0, 300, 300, null);
				} catch (AWTException e) {
					e.printStackTrace();
				} 
			} 
			 
		} else {
			this.doMagnify=false;
		}*/
	}

	private ActionListener diyFractChoiceRbListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				 JRadioButton button = (JRadioButton) e.getSource();
				 doSetDiyFractalChoice(button.getName());			
			}};
	}
	
	/*	//////////////////////starts-progressbar/////////////////////
	// function to increase progress
	public void startProgress() {

		this.progressBar.setStringPainted(true);
//		this.progressBar.setBounds(40,80,160,30); 
		this.progressBar.setValue(0);

        this.progressBar.setIndeterminate(true);
		this.add(this.progressBar);
		
		this.progressBar.setVisible(true);
		int i = 0;
		try {
			while (i <= 10000) {
				// set text accoring to the level to which the bar is filled
				if (i > 30 && i < 700)
					this.progressBar.setString("Working");
				else if (i > 700)
					this.progressBar.setString("Almost finished loading");
				else
					this.progressBar.setString("Loading started");

				// fill the menu bar
				this.progressBar.setValue(i + 100);

				// delay the thread
				Thread.sleep(3000);
				i += 20;
			}
		} catch (Exception e) {
		}
	}
	
	public void endProgress() {
		
	}
	////////////////////////ends-progressbar//////////////////////*/

	protected String getComboChoice() {		
		return this.comboChoice;
	}
	
	protected int getSideComboChoice(){
		return this.sideChoice;
	}
	
	protected int getRatioChoice(){
		return this.ratioChoice;
	}
	
	protected double[] getCurvChoice(){
		return this.curvChoices;
	}
	
	protected double getMultiplier(){
		return this.mult;
	}
	
	protected int getPower(){
		return this.power;
	}
	
	protected double getComplexConst(){
		return this.compConst;
	}
	
	protected String getComplex(){
		return this.complex;
	}
	
	protected int getMagnification(){
		return this.magnification;
	}
	
	protected int getExponent(){
		return this.exponent;
	}

	protected boolean getMUseDiff(){
		return this.mUseDiff || this.mandUseDiffCb.isSelected();
	}
	protected boolean getJUseDiff(){
		return this.jUseDiff || this.juliaUseDiffCb.isSelected();
	}

	public String getDiyFractChoice() {
		return this.diyFractChoice;
	}

	public void setDiyFractChoice(String diyFractChoice) {
		this.diyFractChoice = diyFractChoice;
	}

	public int getDiyMandMagnification() {
		return diyMandMagnification;
	}

	public void setDiyMandMagnification(int diyMagnification) {
		this.diyMandMagnification = diyMagnification;
	}

	public int getDiyMandExponent() {
		return diyMandExponent;
	}

	public void setDiyMandExponent(int diyExponent) {
		this.diyMandExponent = diyExponent;
	}

	public double getDiyMandConstReal() {
		return diyMandConstReal;
	}

	public void setDiyMandConstReal(double diyMandConstReal) {
		this.diyMandConstReal = diyMandConstReal;
	}

	public double getDiyMandConstImg() {
		return diyMandConstImg;
	}

	public void setDiyMandConstImg(double diyMandConstImg) {
		this.diyMandConstImg = diyMandConstImg;
	}

	public boolean getDiyMandUseDiff() {
		return this.diyMandUseDiff || this.diyMandUseDiffCb.isSelected();
	}

	public void setDiyMandMUseDiff(boolean diyMUseDiff) {
		this.diyMandUseDiff = diyMUseDiff;
		this.diyMandUseDiffCb.setSelected(diyMUseDiff);
	}

	public int getDiyJuliaPower() {
		return this.diyJuliaPower;
	}

	public void setDiyJuliaPower(int diyJuliaPower) {
		this.diyJuliaPower = diyJuliaPower;
	}

	public double getDiyJuliaConstReal() {
		return diyJuliaConstReal;
	}

	public void setDiyJuliaConstReal(double diyJuliaConstReal) {
		this.diyJuliaConstReal = diyJuliaConstReal;
	}

	public double getDiyJuliaConstImg() {
		return diyJuliaConstImg;
	}

	public void setDiyJuliaConstImg(double diyJuliaConstImg) {
		this.diyJuliaConstImg = diyJuliaConstImg;
	}

	public boolean getDiyJuliaUseDiff() {
		return this.diyJuliaUseDiff || this.diyJuliaUseDiffCb.isSelected();
	}

	public void setDiyJuliaUseDiff(boolean diyJuliaUseDiff) {
		this.diyJuliaUseDiff = diyJuliaUseDiff;
	}

	public double getJuliaBound() {
		return juliaBound;
	}

	public void setJuliaBound(double juliaBound) {
		this.juliaBound = juliaBound;
	}

	public double getMandBound() {
		return mandBound;
	}

	public void setMandBound(double mandBound) {
		this.mandBound = mandBound;
	}

	public double getJuliaXC() {
		return juliaXC;
	}

	public void setJuliaXC(double juliaXC) {
		this.juliaXC = juliaXC;
	}

	public double getJuliaYC() {
		return juliaYC;
	}

	public void setJuliaYC(double juliaYC) {
		this.juliaYC = juliaYC;
	}

	public double getMandXC() {
		return mandXC;
	}

	public void setMandXC(double mandXC) {
		this.mandXC = mandXC;
	}

	public double getMandYC() {
		return mandYC;
	}

	public void setMandYC(double mandYC) {
		this.mandYC = mandYC;
	}

	public double getMandScaleSize() {
		return mandScaleSize;
	}

	public void setMandScaleSize(double mandScaleSize) {
		this.mandScaleSize = mandScaleSize;
	}
	
	public FractalBase getFractalBase() {
		return this.fBase;
	}

	public void setFractalBase(FractalBase fBase) {
		this.fBase = fBase;
	}

	public BufferedImage getFractalImage() {
		return this.fractalImage;
	}

	public void setFractalImage(BufferedImage fImage) {
		this.fractalImage = fImage;
	}

	public void setFractalImage(Image fImage) {
		this.fractalImage = (BufferedImage) fImage;
	}

	public String getConstFuncChoice() {
		return this.constFuncChoice;
	}

	public void setConstFuncChoice(String cFuncChoice) {
		this.constFuncChoice = cFuncChoice;
	}
	
}




public class SierpinskiCompositional extends JFrame {

	private static final long serialVersionUID = 17584L;
	private SierpinskiComboPanel topPanel = new SierpinskiComboPanel();

	/**
	 * @throws HeadlessException
	 */
	public SierpinskiCompositional() throws HeadlessException {
		super();
		this.initComponents();
	}

	private void initComponents() {
		Container cp;
		cp = this.getContentPane();
		cp.setLayout(new BorderLayout());

		cp.add(this.topPanel);
		
		this.pack();
		this.revalidate();
	    this.repaint();
	   	    
		this.setVisible(true);
	    
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		      @Override
		      public void run() {
		        final SierpinskiCompositional frame = new SierpinskiCompositional();
		        frame.setTitle("BaWaZ FractalArtz: ");
		        frame.setSize(900, 800);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setResizable(true);
		        frame.setVisible(true);
		      }
		});
	}

}
