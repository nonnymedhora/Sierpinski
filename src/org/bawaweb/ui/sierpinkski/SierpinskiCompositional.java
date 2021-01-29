/**
 * 
 */
package org.bawaweb.ui.sierpinkski;


import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
//import static java.util.Collections.shuffle;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;


/**
 * @author Navroz
 * 
 * todo
 * 	capture fractal info via reflection
 * 		-save image with data	(current)	
 * 		-save image with detail from 'capture fractal info'	-	adjacent image
 * 	error info for NaN
 * 	send all strings to consts / enums
 *  expand combos == scaleSize, maginification
 *  
 *  lyapunov exponent,	burning ship, buddhabrot
 *  expression evaluator for inserting custom formula
 *  
 *  save pixel info to txt file for future image regeneration 
 * 	save image to file - user choice for file-destination & file-name
 * 	save image options to jpeg
 * 
 * color selection choices
 * 
 * fractal generator
 * 	
 *
 */

class SierpinskiComboPanel extends JPanel {
	private static final long serialVersionUID = 156478L;
	private static final String eol = "<br/>";

	private static final String[] OPERATIONS = getOperations();

	private static final Double[] 	BOUNDARIES = getBoundaryOptions();
	private static final String[] 	COLOR_SAMPLE_STARTVAL_OPTIONS 	= new String[] {"POW2_4_32", "POW2_4_200", "POW2_2_128", "POW2_2_F4", "POW3_3_243", "EQUAL_PARTS_40", "EQUAL_PARTS_50", "EQUAL_PARTS_25"};
	private static final String[] 	COLOR_SAMPLE_DIV_OPTIONS 		= new String[] {"FRST_FOUR_PRIMES", "FRST_SIX_PRIMES", "FRST_SIX_ODDS", "FRST_SIX_FIBS"};
	private static final int[][] 	COLOR_SAMPLE_STARTVAL_ARRAYS 	= new int[][] {FractalBase.POW2_4_32, FractalBase.POW2_4_200, FractalBase.POW2_2_128, FractalBase.POW2_2_F4, FractalBase.POW3_3_243, FractalBase.EQUAL_PARTS_40, FractalBase.EQUAL_PARTS_50, FractalBase.EQUAL_PARTS_25};
	private static final int[][] 	COLOR_SAMPLE_DIV_ARRAYS 		= new int[][] {FractalBase.FRST_FOUR_PRIMES, FractalBase.FRST_SIX_PRIMES, FractalBase.FRST_SIX_ODDS, FractalBase.FRST_SIX_FIBS};
	
	private static final String[] COLOR_OPTIONS 		= new String[] { "ColorPalette", "ComputeColor", "ColorGradient6", "ColorBlowout","ColorSuperBlowout" };
	private static final String[] COLOR_OPTIONS_ALL 	= new String[]{"BlackWhite","ColorPalette","ComputeColor","ColorGradient6","ColorBlowout"/*,"Random"*/,"SampleMix","ColorSuperBlowout"};
	private static final String[] COLOR_BLOWOUT_TYPES = new String[] { "Default", "Snowy", "Pink", "Matrix" };
	private static final String[] COLOR_SUPER_BLOWOUT_TYPES = FractalBase.COLOR_SUPER_BLOWOUT_TYPES;///new String[] { "Original","Fire","Black & White","Electric Blue","Toon","Gold","Classic VGA","CGA 1","CGA 2","Primary (RGB)","Secondary (CMY)","Tertiary 1","Tertiary 2","Neon"};
	private static final String[] FUNCTION_OPTIONS 		= {"None","sine","cosine","tan","cosec","sec","cot","sinh","cosh","tanh","arcsine","arccosine","arctan","arcsinh","arccosh","arctanh","reciprocal", "reciprocalSquare","square","cube","exponent(e)", "root",/*"cube-root",*/ "log(e)"};
	private static final String[] PIX_TRANSFORM_OPTIONS = {"none", "absolute", "absoluteSquare","reciprocal", "reciprocalSquare", "square", "cube","root", "exponent", "log(10)", "log(e)", "sine", "cosine", "tan", "cosec", "sec", "cot", "sinh", "cosh", "tanh", "arcsine", "arccosine", "arctangent", "arcsinh", "arccosh", "arctanh"};
	private static final String[] ORBIT_POINT_OPTIONS = { "[0.0, 0.0]", "[-0.5, 0.0]", "[0.5, 0.0]", "[0.0, -0.5]", "[0.0, 0.5]", "[-0.5, -0.5]", "[-0.5, 0.5]", "[0.5, -0.5]", "[0.5, 0.5]" };
	private static final Double[] TRAP_SIZE_OPTIONS = { 0.1, 0.15, 0.2, 0.25, 0.3, 0.4, 0.5, 0.75, 1.0 };
	private static final String[] TRAP_SHAPE_OPTIONS = { "Circle", "Square", "Cross", "Diamond" };

	private boolean smoothenColor = false;
	private final JCheckBox smoothenColorCb = new JCheckBox("SmoothenColor", false);
	
	
	private static final Integer[] EXPONENTS 			= new Integer[] { -10, -9, -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private static final Integer[] MAX_ITERATIONS 		= new Integer[] { 10, 50, 100, 200, 225, 255, 300, 350, 400, 450, 500, 600, 700, 800, 900, 1000, 2000, 3000, 5000, 7500, 10000 };
	private static final Integer[] AREA_SIZES 			= new Integer[] { 10, 50, 100, 200, 225, 255, 500, 512, 599, 800, Integer.MAX_VALUE };
	private static final Double[] CENTER_XY 			= new Double[] { -5.0, -4.5, -4.0, -3.5, -3.0, -2.5, -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0 };
	private static final Double[] SCALE_SIZES 			= getScaleSizeOptions();//new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private static final String[] POLY_RCMT_TYPES 		= new String[] { "Reverse", "Exchange", "Single", "Duplicate", "Exponent", "Power", "Default" };
	private static final Integer[] FANNY_RATIOS 		= new Integer[] { 1, 2, 3, 4, 5, 6, 7 };
	private static final Integer[] FANNY_SIZE_OPTIONS 	= new Integer[] { 50, 60, 70, 80, 90, 100, 120, 150, 170, 200, 250, 300, 350 };
	private static final Integer[] APOLLO_MULTS 		= new Integer[]{ 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
	private static final Integer[] APOLLO_CURVES 		= new Integer[]{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 };
	
	//	for	z+C, z-C, z*C, z/C, z^C
	private static final String[] PIX_CONST_OPRNS = OPERATIONS;
	//	for	x+iy,	x-iy,	x*iy,	x/iy
	private static final String[] PIX_INTRA_OPRNS = OPERATIONS;
	
	// for Julia
	private static final String J1 = "P[2] C[0.279]";	//f(z) = z^2 + 0.279
	private static final String J2 = "P[3] C[0.4]";		//f(z) = z^3 + 0.400
	private static final String J3 = "P[4] C[0.484]";	//f(z) = z^4 + 0.484
	private static final String J4 = "P[5] C[0.544]";	//f(z) = z^5 + 0.544
	private static final String J5 = "P[6] C[0.59]";	//f(z) = z^6 + 0.590
	private static final String J6 = "P[7] C[0.626]";	//f(z) = z^7 + 0.626
	// extra for Julia
	private static final String J7 = "P[2] C1";			//[-0.74543+0.11301*i]";	//f(z) = z^2 + ...
	private static final String J8 = "P[2] C2";			//[-0.75+0.11*i]";	//f(z) = z^2 + ....
	private static final String J9 = "P[2] C3";			//[-0.1+0.651*i]";	//f(z) = z^2 + ...
	
	private static final String J10 = "P[2] CPI1R06i";	//[pi/2*(1.0 + 0.6i)]";	//f(z) = z^2 + ...
	private static final String J11 = "P[2] CPI1R04i";	//[pi/2*(1.0 + 0.4i)]";	//f(z) = z^2 + ...
	
	
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
	private static final String ATTRACTORS 				= "Attractors";
	
	
	//= "bd";	//	others are exponent/power/magnification/scaleSize
	//= "bd";	//	others are exponent/power/magnification/scaleSize
	private static final String[] MOTION_PARAM_OPTIONS = new String[] {"bd","pow","scaleSize"};
	private static final Double[] MOTION_PARAM_JUMP_OPTIONS = getMotionParamJumpOptions();

	private static final String EMPTY = "";
	private static final String OPEN_BRACK = "\\[";
	private static final String CLOSE_BRACK = "\\]";
	private static final String COMMA = ",";
	private static final String BACK_SLASH = "\\\\";
	private static final String FORWARD_SLASH = "/";
	private static final String PIPE = "\\|";
	private static final String[] ATTRACTOR_CHOICES = new String[]{"lorenz","aizawa","dejong","custom"};
	private static final String[] ATTRACTOR_SPACE_3D_CHOICES = new String[] { "x-y", "x-z", "y-z", "y-x", "z-x", "z-y" };
	private static final String[] ATTRACTOR_SPACE_2D_CHOICES = new String[] { "x-y", "y-x" };
	
	private final String[] comboOptions = new String[]{ DIY, FANNY_CIRCLE, FANNY_TRIANGLES, SIERPINSKI_TRIANGLES, SIERPINSKI_SQUARES, APOLLONIAN_CIRCLES, CST_FRACTAL, SAMPLE, MANDELBROT, JULIA, KOCHSNOWFLAKE, POLY, ATTRACTORS };
	private final JComboBox<String> combos = new JComboBox<String>(comboOptions);
	
	//	for	FannyCircle & FannyTriangles
	private final Integer[] sideOptions = FANNY_SIZE_OPTIONS;
	private final JComboBox<Integer> sideCombos = new JComboBox<Integer>(sideOptions);
	private final Integer[] ratioOptions = FANNY_RATIOS;
	private final JComboBox<Integer> ratioCombos = new JComboBox<Integer>(ratioOptions);
	
	// for SierpinskiTraingles
	private final JCheckBox sierpinskiTFillInnerCb = new JCheckBox("FillInnerTriangles", true);
	private final JCheckBox sierpinskiCreateGasketCb = new JCheckBox("CreateGasket", true);

	private boolean sierpinskiFillInnerT = false;
	private boolean sierpinskiCreateGasket = false;
	
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

	private boolean juliaExplore = false;
	private final JCheckBox juliaExploreCb = new JCheckBox("Explore Julia", false);

	private final String[] juliaOptions = new String[] { J1, J2, J3, J4, J5, J6, J7, J8, J9, J10, J11};
	private final JComboBox<String> juliaCombos = new JComboBox<String>(juliaOptions);
	
	// lyapunov exponent for set determination
	private final JCheckBox juliaUseLyapunovExpCb = new JCheckBox("UseLyapunovExponentOnly", false );
	private boolean juliaUseLyapunovExponent = false;
	
	//or use iterative escape algorithm
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
	
	private static final String[] JULIA_FIELD_TYPES 		= new String[] { "None", "Fatou", "Z-Squared", "ClassicJ" };
	
	
	private ButtonGroup  juliaFieldLinesBG 		= new ButtonGroup();
	private JRadioButton juliaFieldNoneRB 		= new JRadioButton("None", true);
	private JRadioButton juliaFieldFatouRB 		= new JRadioButton("Fatou", false);
	private JRadioButton juliaFieldZSqRB 		= new JRadioButton("Z-Squared", false);
	private JRadioButton juliaFieldClassicRB 	= new JRadioButton("ClassicJ", false);
	
	

	// for Mandelbrot

	private boolean mandExplore = false;
	private final JCheckBox mandExploreCb = new JCheckBox("Explore Mandelbrot", false);
	// lyapunov exponent for set determination
	private final JCheckBox mandUseLyapunovExpCb = new JCheckBox("UseLyapunovExponentOnly", false);
	private boolean mandUseLyapunovExponent = false;
	
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
	
	//	for	Apollo
	private final String[] curvOptions = new String[] { A1, A2, A3, A4, A5, A6 };
	private final JComboBox<String> curvCombos = new JComboBox<String>(curvOptions);
	
	private final JCheckBox apolloTriangleCb = new JCheckBox("Apollonian Triangle");
	private final JCheckBox apolloTriangleUseCentroidCb = new JCheckBox("Apollonian TriangleUseCentroid");
	

	// Fractal Art Options
	private JPanel fannyOptionsPanel 	= new JPanel(new FlowLayout(),true);
	private JPanel apolloOptionsPanel 	= new JPanel(new FlowLayout(),true);
	private JPanel sierpinskiTPanel 	= new JPanel(new FlowLayout(),true);	
	private JPanel kochSnowFlakePanel 	= new JPanel(new FlowLayout(),true);	
	
	private JPanel juliaOptionsPanel 	= new JPanel(new GridLayout(10,5),true);
	private JPanel mandOptionsPanel 	= new JPanel(new GridLayout(10,5),true);
	private JPanel polyOptionsPanel 	= new JPanel(new GridLayout(20,15),true);
	private JPanel diyOptionsPanel		= new JPanel(new FlowLayout(),true);
	
	private JPanel diyMandPanel 		= new JPanel(new GridLayout(20,15),true);
	private JPanel diyJuliaPanel 		= new JPanel(new GridLayout(20,15),true);
	private JPanel diyApolloPanel 		= new JPanel(new GridLayout(4,8),true);

	private JPanel attractorsPanel/* 		= new JPanel(new GridLayout(20,15))*/;
	private JPanel attractorsSeedsPanel/* = new JPanel(new GridLayout(10,10))*/;
	
	private JFrame attractorsFrame;
	
	private JTextArea formulaArea = new JTextArea(5,20);
	
	private final JButton buStart = new JButton("Start |>");
	
	private final JButton buSavePxStart = new JButton("SavePxStart |>");
	
	private final JButton buPause = new JButton("Pause ||");
	private final JButton buClose = new JButton("CLOSEImage");

	private boolean savePixelInfo2File = false;
	
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
	private JCheckBox mandIsBuddhaCb = new JCheckBox("Get Buddha", false);
	private boolean mandIsBuddhabrot = false;
	
	private JCheckBox mandIsMotionbrotCb = new JCheckBox("Create Motionbrot:", false);
	private boolean mandIsMotionbrot = false;
	
	private String mandMotionParam = "None"; // others are "= "bd"; // others
												// are
												// exponent/power/magnification/scaleSize

	private JLabel mandMotionParamLabel = new JLabel("Motion Param:");
	private JComboBox<String> mandMotionParamCombo = new JComboBox<String>(MOTION_PARAM_OPTIONS);
	private JLabel mandMotionParamJumpLabel = new JLabel("Jump:");
	private JComboBox<Double> mandMotionParamJumpCombo = new JComboBox<Double>(MOTION_PARAM_JUMP_OPTIONS);

	private double mandMotionParamJumpVal = 0.5;
	
	//////////////////////////////////////////////////////////////
	// for DIY
	//radioButton
	protected JRadioButton diyMandRb = new JRadioButton(MANDELBROT/*,true*/);
	protected JRadioButton diyJuliaRb = new JRadioButton(JULIA);
	protected JRadioButton diyApolloRb = new JRadioButton(APOLLONIAN_CIRCLES);
	
	protected ButtonGroup diyBg = new ButtonGroup();
	
	// diy fractal art option choice
	private final String diyMand = "DIY_"+MANDELBROT;
	private final String diyJulia = "DIY_"+JULIA;
	private final String diyApollo = "DIY_"+APOLLONIAN_CIRCLES;

	private String diyFractChoice;
	
	//diy Mandelbrot options
	protected int diyMandMagnification;		//		mandOptions
	protected int diyMandExponent;			//		mandExpOptions
	protected double diyMandConstReal;
	protected double diyMandConstImg;
	protected boolean diyMandUseDiff;		//		mandUseDiffCb
	protected boolean diyMandKeepConst;
	protected int diyMandMaxIter;
	protected int diyMandSize;
	protected double diyMandBound;
	
	protected double diyMandXC;
	protected double diyMandYC;
	protected double diyMandScaleSize;
	
	private boolean diyMandExplore = false;
	private final JCheckBox diyMandExploreCb = new JCheckBox("Explore Mandelbrot", false);
	private final Integer[] diyMandMagOptions = EXPONENTS;

	// lyapunov exponent for set determination
	private final JCheckBox diyMandUseLyapunovExpCb = new JCheckBox("UseLyapunovExponentOnly", false);
	private boolean diyMandUseLyapunovExponent = false;
	
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
	
	private final Integer[] diyMandAreaSizeOptions = AREA_SIZES;
	private final JComboBox<Integer> diyMandAreaSizeCombos = new JComboBox<Integer>(diyMandAreaSizeOptions);
	
	private final Double[] diyMandBoundOptions = BOUNDARIES;
	private final JComboBox<Double> diyMandBoundCombos = new JComboBox<Double>(diyMandBoundOptions);
	
	private final Double[] diyMandXCOptions = CENTER_XY;
	private final JComboBox<Double> diyMandXCCombos = new JComboBox<Double>(diyMandXCOptions);
	private final Double[] diyMandYCOptions = CENTER_XY;
	private final JComboBox<Double> diyMandYCCombos = new JComboBox<Double>(diyMandYCOptions);

	private final Double[] diyMandScaleSizeOptions = SCALE_SIZES;
	private final JComboBox<Double> diyMandScaleSizeCombos = new JComboBox<Double>(diyMandScaleSizeOptions);
	
	private JCheckBox diyMandIsBuddhaCb = new JCheckBox("Get Buddha", false);
	private boolean diyMandIsBuddhabrot = false;
	
	//diy Julia options
	protected int diyJuliaPower;
	protected double diyJuliaConstReal;
	protected double diyJuliaConstImg;
	protected boolean diyJuliaUseDiff;
	protected boolean diyJuliaKeepConst;
	/*protected boolean diyJuliaUseSineCalc;*/
	protected int diyJuliaMaxIter;
	protected double diyJuliaBound;

	protected int diyJuliaSize;
	
	protected double diyJuliaXC;
	protected double diyJuliaYC;
	protected double diyJuliaScaleSize;
	
	private boolean diyJuliaExplore = false;
	private final JCheckBox diyJuliaExploreCb = new JCheckBox("Explore Julia", false);

	//lyapunov exponent for set determination
	private final JCheckBox diyJuliaUseLyapunovExpCb = new JCheckBox("UseLyapunovExponentOnly", false);
	private boolean diyJuliaUseLyapunovExponent=false;
	
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

	private final Integer[] diyJuliaAreaSizeOptions = AREA_SIZES;
	private final JComboBox<Integer> diyJuliaAreaSizeCombos = new JComboBox<Integer>(diyJuliaAreaSizeOptions);	
	
	private final Double[] diyJuliaBoundOptions = BOUNDARIES;
	private final JComboBox<Double> diyJuliaBoundCombos = new JComboBox<Double>(diyJuliaBoundOptions);

	private final Double[] diyJuliaXCOptions = CENTER_XY;
	private final JComboBox<Double> diyJuliaXCCombos = new JComboBox<Double>(diyJuliaXCOptions);
	private final Double[] diyJuliaYCOptions = CENTER_XY;
	private final JComboBox<Double> diyJuliaYCCombos = new JComboBox<Double>(diyJuliaYCOptions);

	private final Double[] diyJuliaScaleSizeOptions = SCALE_SIZES;
	private final JComboBox<Double> diyJuliaScaleSizeCombos = new JComboBox<Double>(diyJuliaScaleSizeOptions);
	
	/////////////////////////////////////////////////////////////////////////////
	//Generator	Julia
	private final JCheckBox diyJuliaGenCb = new JCheckBox("Generate", false);

	private JCheckBox diyJuliaVaryColorCb = new JCheckBox("Vary Color", false);
	private JCheckBox diyJuliaVaryPixXTranCb = new JCheckBox("Vary Pixel_X_Trans f(x)", false);
	private JCheckBox diyJuliaVaryPixYTranCb = new JCheckBox("Vary Pixel_Y_Tran f(y)", false);
	private JCheckBox diyJuliaVaryIntraPixXYCb = new JCheckBox("Vary Intra_Pixel_Op f(x,y)", false);
	private JCheckBox diyJuliaVaryPixelZFuncCb = new JCheckBox("Vary Pixel_Complex f(Z)", false);
	private JCheckBox diyJuliaVaryConstCFuncCb = new JCheckBox("Vary Constant Function f(C)", false);
	private JCheckBox diyJuliaVaryPixelConstOpZCCb = new JCheckBox("Vary Pixel(Z)_Constant(C) Op O(Z,C)", false);
	private JCheckBox diyJuliaVaryGenConstantCb = new JCheckBox("Vary Constant Value  C", false);
	private JCheckBox diyJuliaVaryPixelPowerZCb = new JCheckBox("Vary Power Z^n", false);
	private JCheckBox diyJuliaVaryIterCb = new JCheckBox("Vary MaxIterations", false);
	private JCheckBox diyJuliaVaryBoundaryCb = new JCheckBox("Vary Boundary", false);
	private JCheckBox diyJuliaVaryPixXCentrCb = new JCheckBox("Vary PixelCenter_X", false);
	private JCheckBox diyJuliaVaryPixYCentrCb = new JCheckBox("Vary PixelCenter_Y", false);
	private JCheckBox diyJuliaVaryScaleSizeCb = new JCheckBox("Vary Scale", false);
	
	private JCheckBox diyJuliaVaryFieldTypeCb = new JCheckBox("Vary Type", false);
	
	private boolean diyJuliaVaryColor 		= false;			//diyJuliaVaryColorCb
	private boolean diyJuliaVaryPixXTran 	= false;			//diyJuliaVaryPixXTranCb
	private boolean diyJuliaVaryPixYTran 	= false;			//diyJuliaVaryPixYTranCb
	private boolean diyJuliaVaryIntraPixXY 	= false;			//diyJuliaVaryIntraPixXYCb
	private boolean diyJuliaVaryPixelZFunc 	= false;			//diyJuliaVaryPixelZFuncCb
	private boolean diyJuliaVaryConstCFunc 	= false;			//diyJuliaVaryConstCFuncCb
	private boolean diyJuliaVaryPixelConstOpZC 	= false;		//diyJuliaVaryPixelConstOpZCCb
	private boolean diyJuliaVaryPixelPowerZ = false;			//diyJuliaVaryPixelPowerZCb
	
	private boolean diyJuliaVaryIter 		= false;
	private boolean diyJuliaVaryFieldType 	= false;			//diyJuliaVaryFieldTypeCb
	private boolean diyJuliaVaryPixXCentr 	= false;			//diyJuliaVaryPixXCentrCb
	private boolean diyJuliaVaryPixYCentr 	= false;			//diyJuliaVaryPixYCentrCb
	
	// diyJuliaVaryPixelPowerZCb applies here
	private JLabel diyJuliaGenPixelPowerZFromLabel = new JLabel("PixelPowerZ [From]:");
	private final JTextField diyJuliaGenPixelPowerZFromTf = new JTextField(2);
	private JLabel diyJuliaGenPixelPowerZToLabel = new JLabel(" [To]:");
	private final JTextField diyJuliaGenPixelPowerZToTf = new JTextField(2);
	private JLabel diyJuliaGenPixelPowerZJumpLabel = new JLabel(" [Jump]");
	private JComboBox<Integer> diyJuliaGenPixelPowerZJumpCombo = new JComboBox<Integer>(getPixelPowerZJumpOptions());

	private int diyJuliaVaryPixelPowerZFromVal;
	private int diyJuliaVaryPixelPowerZToVal;
	private int diyJuliaVaryPixelPowerZJumpVal;
	// ends
	// diyJuliaVaryPixelPowerZCb applies here
	
	
	//diyJuliaVaryScaleSizeCb applies here
	private JLabel diyJuliaGenScaleSizeFromLabel = new JLabel("ScaleSize [From]:");
	private final JTextField diyJuliaGenScaleSizeFromTf = new JTextField(5);
	private JLabel diyJuliaGenScaleSizeToLabel = new JLabel(" [To]:");
	private final JTextField diyJuliaGenScaleSizeToTf = new JTextField(5);
	private JLabel diyJuliaGenScaleSizeJumpLabel = new JLabel(" [Jump]");	
	private JComboBox<Double> diyJuliaGenScaleSizeJumpCombo = new JComboBox<Double>(getScaleSizeJumpOptions());
	
	private boolean diyJuliaVaryScaleSize = false;
	private double diyJuliaVaryScaleSizeFromVal;
	private double diyJuliaVaryScaleSizeToVal;
	private double diyJuliaVaryScaleSizeJumpVal;
	//ends
	//diyJuliaVaryScaleSizeCb applies here
	
	//diyJuliaVaryBoundaryCb applies here
	private JLabel diyJuliaGenBoundaryFromLabel = new JLabel("Boundary [From]:");
	private final JTextField diyJuliaGenBoundaryFromTf = new JTextField(5);
	private JLabel diyJuliaGenBoundaryToLabel = new JLabel(" [To]:");
	private final JTextField diyJuliaGenBoundaryToTf = new JTextField(5);
	private JLabel diyJuliaGenBoundaryJumpLabel = new JLabel(" [Jump]");	
	private JComboBox<Double> diyJuliaGenBoundaryJumpCombo = new JComboBox<Double>(getBoundaryJumpOptions());
	
	private boolean diyJuliaVaryBoundary = false;
	private double diyJuliaVaryBoundaryFromVal;
	private double diyJuliaVaryBoundaryToVal;
	private double diyJuliaVaryBoundaryJumpVal;
	//ends
	//diyJuliaVaryBoundaryCCb applies here
	
	//diyJuliaVaryGenConstantCb applies here
	private JLabel diyJuliaGenRealFromLabel = new JLabel("Constant - Real [From]");
	private final JTextField diyJuliaGenRealFromTf = new JTextField(5);
	private JLabel diyJuliaGenRealToLabel = new JLabel(" Real [To]");
	private final JTextField diyJuliaGenRealToTf = new JTextField(5);
	private JLabel diyJuliaGenRealJumpLabel = new JLabel(" Real [Jump]");	
	private JComboBox<Double> diyJuliaGenRealJumpCombo = new JComboBox<Double>(getConstantJumpOptions());
	
	private JLabel diyJuliaGenImagFromLabel = new JLabel(" Img [From]");
	private final JTextField diyJuliaGenImagFromTf = new JTextField(5);
	private JLabel diyJuliaGenImagToLabel = new JLabel(" Img [To]");
	private final JTextField diyJuliaGenImagToTf = new JTextField(5);
	private JLabel diyJuliaGenImagJumpLabel = new JLabel(" Img [Jump]");
	private JComboBox<Double> diyJuliaGenImagJumpCombo = new JComboBox<Double>(getConstantJumpOptions());	
	
	private boolean diyJuliaVaryConstant = false;
	private double diyJuliaGenRealFromVal;
	private double diyJuliaGenRealToVal;
	private double diyJuliaGenRealJumpVal;
	private double diyJuliaGenImagFromVal;
	private double diyJuliaGenImagToVal;
	private double diyJuliaGenImagJumpVal;
	
	//ends
	//diyJuliaVaryGenConstantCb applies here
	
	private boolean diyJuliaGen = false;
	private JButton diyJuliaGenBu = new JButton("Generate Julia");
	private JButton diyJuliaGen2FolderBu = new JButton("Generate Julia 2Folder");
	private String diyJuliaGen2FolderPath = null;
	private boolean diyJuliaGen2Folder = false;
	///////////////////////////////////////////////////////////////////////////
	//	Generator	Mandelbrot

	private final JCheckBox diyMandGenCb = new JCheckBox("Generate", false);

	private JCheckBox diyMandVaryColorCb = new JCheckBox("Vary Color", false);
	private JCheckBox diyMandVaryPixXTranCb = new JCheckBox("Vary Pixel_X_Trans f(x)", false);
	private JCheckBox diyMandVaryPixYTranCb = new JCheckBox("Vary Pixel_Y_Tran f(y)", false);
	private JCheckBox diyMandVaryIntraPixXYCb = new JCheckBox("Vary Intra_Pixel_Op f(x,y)", false);
	private JCheckBox diyMandVaryPixelZFuncCb = new JCheckBox("Vary Pixel_Complex f(Z)", false);
	private JCheckBox diyMandVaryConstCFuncCb = new JCheckBox("Vary Constant Function f(C)", false);
	private JCheckBox diyMandVaryPixelConstOpZCCb = new JCheckBox("Vary Pixel(Z)_Constant(C) Op O(Z,C)", false);
	private JCheckBox diyMandVaryGenConstantCb = new JCheckBox("Vary Constant Value  C", false);
	private JCheckBox diyMandVaryPixelPowerZCb = new JCheckBox("Vary Power Z^n", false);
	private JCheckBox diyMandVaryIterCb = new JCheckBox("Vary MaxIterations", false);
	private JCheckBox diyMandVaryBoundaryCb = new JCheckBox("Vary Boundary", false);
	private JCheckBox diyMandVaryPixXCentrCb = new JCheckBox("Vary PixelCenter_X", false);
	private JCheckBox diyMandVaryPixYCentrCb = new JCheckBox("Vary PixelCenter_Y", false);
	private JCheckBox diyMandVaryScaleSizeCb = new JCheckBox("Vary Scale", false);
	
	private boolean diyMandVaryColor 		= false;			//diyMandVaryColorCb
	private boolean diyMandVaryPixXTran 	= false;			//diyMandVaryPixXTranCb
	private boolean diyMandVaryPixYTran 	= false;			//diyMandVaryPixYTranCb
	private boolean diyMandVaryIntraPixXY 	= false;			//diyMandVaryIntraPixXYCb
	private boolean diyMandVaryPixelZFunc 	= false;			//diyMandVaryPixelZFuncCb
	private boolean diyMandVaryConstCFunc 	= false;			//diyMandVaryConstCFuncCb
	private boolean diyMandVaryPixelConstOpZC 	= false;		//diyMandVaryPixelConstOpZCCb
	private boolean diyMandVaryPixelPowerZ = false;			//diyMandVaryPixelPowerZCb
	

	private boolean diyMandVaryIter 		= false;			//diyMandVaryIterCb
	private boolean diyMandVaryPixXCentr 	= false;			//diyMandVaryPixXCentrCb
	private boolean diyMandVaryPixYCentr 	= false;			//diyMandVaryPixYCentrCb
	
	// diyMandVaryPixelPowerZCb applies here
	private JLabel diyMandGenPixelPowerZFromLabel = new JLabel("PixelPowerZ [From]:");
	private final JTextField diyMandGenPixelPowerZFromTf = new JTextField(2);
	private JLabel diyMandGenPixelPowerZToLabel = new JLabel(" [To]:");
	private final JTextField diyMandGenPixelPowerZToTf = new JTextField(2);
	private JLabel diyMandGenPixelPowerZJumpLabel = new JLabel(" [Jump]");
	private JComboBox<Integer> diyMandGenPixelPowerZJumpCombo = new JComboBox<Integer>(getPixelPowerZJumpOptions());

	private int diyMandVaryPixelPowerZFromVal;
	private int diyMandVaryPixelPowerZToVal;
	private int diyMandVaryPixelPowerZJumpVal;
	// ends
	// diyMandVaryPixelPowerZCb applies here
	
	
	//diyMandVaryScaleSizeCb applies here
	private JLabel diyMandGenScaleSizeFromLabel = new JLabel("ScaleSize [From]:");
	private final JTextField diyMandGenScaleSizeFromTf = new JTextField(5);
	private JLabel diyMandGenScaleSizeToLabel = new JLabel(" [To]:");
	private final JTextField diyMandGenScaleSizeToTf = new JTextField(5);
	private JLabel diyMandGenScaleSizeJumpLabel = new JLabel(" [Jump]");	
	private JComboBox<Double> diyMandGenScaleSizeJumpCombo = new JComboBox<Double>(getScaleSizeJumpOptions());
	
	private boolean diyMandVaryScaleSize = false;
	private double diyMandVaryScaleSizeFromVal;
	private double diyMandVaryScaleSizeToVal;
	private double diyMandVaryScaleSizeJumpVal;
	//ends
	//diyMandVaryScaleSizeCb applies here
	
	//diyMandVaryBoundaryCb applies here
	private JLabel diyMandGenBoundaryFromLabel = new JLabel("Boundary [From]:");
	private final JTextField diyMandGenBoundaryFromTf = new JTextField(5);
	private JLabel diyMandGenBoundaryToLabel = new JLabel(" [To]:");
	private final JTextField diyMandGenBoundaryToTf = new JTextField(5);
	private JLabel diyMandGenBoundaryJumpLabel = new JLabel(" [Jump]");	
	private JComboBox<Double> diyMandGenBoundaryJumpCombo = new JComboBox<Double>(getBoundaryJumpOptions());
	
	private boolean diyMandVaryBoundary = false;
	private double diyMandVaryBoundaryFromVal;
	private double diyMandVaryBoundaryToVal;
	private double diyMandVaryBoundaryJumpVal;
	//ends
	//diyMandVaryBoundaryCCb applies here
	
	//diyMandVaryGenConstantCb applies here
	private JLabel diyMandGenRealFromLabel = new JLabel("Constant - Real [From]");
	private final JTextField diyMandGenRealFromTf = new JTextField(5);
	private JLabel diyMandGenRealToLabel = new JLabel(" Real [To]");
	private final JTextField diyMandGenRealToTf = new JTextField(5);
	private JLabel diyMandGenRealJumpLabel = new JLabel(" Real [Jump]");	
	private JComboBox<Double> diyMandGenRealJumpCombo = new JComboBox<Double>(getConstantJumpOptions());
	
	private JLabel diyMandGenImagFromLabel = new JLabel(" Img [From]");
	private final JTextField diyMandGenImagFromTf = new JTextField(5);
	private JLabel diyMandGenImagToLabel = new JLabel(" Img [To]");
	private final JTextField diyMandGenImagToTf = new JTextField(5);
	private JLabel diyMandGenImagJumpLabel = new JLabel(" Img [Jump]");
	private JComboBox<Double> diyMandGenImagJumpCombo = new JComboBox<Double>(getConstantJumpOptions());	
	
	private boolean diyMandVaryConstant = false;
	private double diyMandGenRealFromVal;
	private double diyMandGenRealToVal;
	private double diyMandGenRealJumpVal;
	private double diyMandGenImagFromVal;
	private double diyMandGenImagToVal;
	private double diyMandGenImagJumpVal;
	
	//ends
	//diyMandVaryGenConstantCb applies here
	
	private boolean diyMandGen = false;
	private JButton diyMandGenBu = new JButton("Generate Mandelbrot");
	private JButton diyMandGen2FolderBu = new JButton("Generate Mandelbrot 2Folder");
	private String diyMandGen2FolderPath = null;
	private boolean diyMandGen2Folder = false;
	///////////////////////////////////////////////////////////////////////////
//	Generator	PolyFract

	private final JCheckBox polyGenCb = new JCheckBox("Generate", false);

	private JCheckBox polyVaryColorCb = new JCheckBox("Vary Color", false);
	private JCheckBox polyVaryPixXTranCb = new JCheckBox("Vary Pixel_X_Trans f(x)", false);
	private JCheckBox polyVaryPixYTranCb = new JCheckBox("Vary Pixel_Y_Tran f(y)", false);
	private JCheckBox polyVaryIntraPixXYCb = new JCheckBox("Vary Intra_Pixel_Op f(x,y)", false);
	private JCheckBox polyVaryPixelZFuncCb = new JCheckBox("Vary Pixel_Complex f(Z)", false);
	private JCheckBox polyVaryConstCFuncCb = new JCheckBox("Vary Constant Function f(C)", false);
	private JCheckBox polyVaryPixelConstOpZCCb = new JCheckBox("Vary Pixel(Z)_Constant(C) Op O(Z,C)", false);
	private JCheckBox polyVaryGenConstantCb = new JCheckBox("Vary Constant Value  C", false);
	private JCheckBox polyVaryPixelPowerZCb = new JCheckBox("Vary Power Z^n", false);
	private JCheckBox polyVaryIterCb = new JCheckBox("Vary MaxIterations", false);
	private JCheckBox polyVaryRCMTCb = new JCheckBox("Vary RowColMix", false);
	private JCheckBox polyVaryBoundaryCb = new JCheckBox("Vary Boundary", false);
	private JCheckBox polyVaryPixXCentrCb = new JCheckBox("Vary PixelCenter_X", false);
	private JCheckBox polyVaryPixYCentrCb = new JCheckBox("Vary PixelCenter_Y", false);
	private JCheckBox polyVaryScaleSizeCb = new JCheckBox("Vary Scale", false);
	
	private boolean polyVaryColor 		= false;			//polyVaryColorCb
	private boolean polyVaryPixXTran 	= false;			//polyVaryPixXTranCb
	private boolean polyVaryPixYTran 	= false;			//polyVaryPixYTranCb
	private boolean polyVaryIntraPixXY 	= false;			//polyVaryIntraPixXYCb
	private boolean polyVaryPixelZFunc 	= false;			//polyVaryPixelZFuncCb
	private boolean polyVaryConstCFunc 	= false;			//polyVaryConstCFuncCb
	private boolean polyVaryPixelConstOpZC 	= false;		//polyVaryPixelConstOpZCCb
	private boolean polyVaryPixelPowerZ = false;			//polyVaryPixelPowerZCb
	
	private boolean polyVaryPolyType 	= false;			//polyVaryRCMTCb
	private boolean polyVaryIter 		= false;			//polyVaryIterCb
	private boolean polyVaryPixXCentr 	= false;			//polyVaryPixXCentrCb
	private boolean polyVaryPixYCentr 	= false;			//polyVaryPixYCentrCb
	
	// polyVaryPixelPowerZCb applies here
	private JLabel polyGenPixelPowerZFromLabel = new JLabel("PixelPowerZ [From]:");
	private final JTextField polyGenPixelPowerZFromTf = new JTextField(2);
	private JLabel polyGenPixelPowerZToLabel = new JLabel(" [To]:");
	private final JTextField polyGenPixelPowerZToTf = new JTextField(2);
	private JLabel polyGenPixelPowerZJumpLabel = new JLabel(" [Jump]");
	private JComboBox<Integer> polyGenPixelPowerZJumpCombo = new JComboBox<Integer>(getPixelPowerZJumpOptions());

	private int polyVaryPixelPowerZFromVal;
	private int polyVaryPixelPowerZToVal;
	private int polyVaryPixelPowerZJumpVal;
	// ends
	// polyVaryPixelPowerZCb applies here
	
	
	//polyVaryScaleSizeCb applies here
	private JLabel polyGenScaleSizeFromLabel = new JLabel("ScaleSize [From]:");
	private final JTextField polyGenScaleSizeFromTf = new JTextField(5);
	private JLabel polyGenScaleSizeToLabel = new JLabel(" [To]:");
	private final JTextField polyGenScaleSizeToTf = new JTextField(5);
	private JLabel polyGenScaleSizeJumpLabel = new JLabel(" [Jump]");	
	private JComboBox<Double> polyGenScaleSizeJumpCombo = new JComboBox<Double>(getScaleSizeJumpOptions());
	
	private boolean polyVaryScaleSize = false;
	private double polyVaryScaleSizeFromVal;
	private double polyVaryScaleSizeToVal;
	private double polyVaryScaleSizeJumpVal;
	//ends
	//polyVaryScaleSizeCb applies here
	
	//polyVaryBoundaryCb applies here
	private JLabel polyGenBoundaryFromLabel = new JLabel("Boundary [From]:");
	private final JTextField polyGenBoundaryFromTf = new JTextField(5);
	private JLabel polyGenBoundaryToLabel = new JLabel(" [To]:");
	private final JTextField polyGenBoundaryToTf = new JTextField(5);
	private JLabel polyGenBoundaryJumpLabel = new JLabel(" [Jump]");	
	private JComboBox<Double> polyGenBoundaryJumpCombo = new JComboBox<Double>(getBoundaryJumpOptions());
	
	private boolean polyVaryBoundary = false;
	private double polyVaryBoundaryFromVal;
	private double polyVaryBoundaryToVal;
	private double polyVaryBoundaryJumpVal;
	//ends
	//polyVaryBoundaryCCb applies here
	
	//polyVaryGenConstantCb applies here
	private JLabel polyGenRealFromLabel = new JLabel("Constant - Real [From]");
	private final JTextField polyGenRealFromTf = new JTextField(5);
	private JLabel polyGenRealToLabel = new JLabel(" Real [To]");
	private final JTextField polyGenRealToTf = new JTextField(5);
	private JLabel polyGenRealJumpLabel = new JLabel(" Real [Jump]");	
	private JComboBox<Double> polyGenRealJumpCombo = new JComboBox<Double>(getConstantJumpOptions());
	
	private JLabel polyGenImagFromLabel = new JLabel(" Img [From]");
	private final JTextField polyGenImagFromTf = new JTextField(5);
	private JLabel polyGenImagToLabel = new JLabel(" Img [To]");
	private final JTextField polyGenImagToTf = new JTextField(5);
	private JLabel polyGenImagJumpLabel = new JLabel(" Img [Jump]");
	private JComboBox<Double> polyGenImagJumpCombo = new JComboBox<Double>(getConstantJumpOptions());	
	
	private boolean polyVaryConstant = false;
	private double polyGenRealFromVal;
	private double polyGenRealToVal;
	private double polyGenRealJumpVal;
	private double polyGenImagFromVal;
	private double polyGenImagToVal;
	private double polyGenImagJumpVal;
	
	//ends
	//polyVaryGenConstantCb applies here
	
	private boolean polyGen = false;
	private JButton polyGenBu = new JButton("Generate PolyFractal");
	private JButton diyPolyGen2FolderBu = new JButton("Generate PolyelFract 2Folder");
	private String diyPolyGen2FolderPath = null;
	private boolean diyPolyGen2Folder = false;
	///////////////////////////////////////////////////////////////////////////
	
	///////////ends	Generator//////////////////////////////////////////////////
	
	
	/////////////////apply function julia///////////////////////////////////
	private boolean diyJApplyFormulaZ = false;
	private JCheckBox diyJApplyFormulaZCb = new JCheckBox("Apply Formula f(Z) = ",false);
	private JTextField diyJApplyFormulaTf = new JTextField(10);
	private JTextArea diyJuliaGenVaryApplyFormulaTxtArea = new JTextArea(1, 10);
	private JScrollPane diyJuliaGenVaryApplyFormulaTxtAreaSpane = new JScrollPane(this.diyJuliaGenVaryApplyFormulaTxtArea);
	private String diyJApplyFormulaStr = "NONE";
	
	/////////////////ends apply function julia////////////////////////////////////

	///////////////// apply function
	///////////////// Mandelbrot///////////////////////////////////
	private boolean diyMandApplyFormulaZ = false;
	private JCheckBox diyMandApplyFormulaZCb = new JCheckBox("Apply Formula f(Z) = ", false);
	private JTextField diyMandApplyFormulaTf = new JTextField(10);
	private JTextArea diyMandGenVaryApplyFormulaTxtArea = new JTextArea(1, 10);
	private JScrollPane diyMandGenVaryApplyFormulaTxtAreaSpane = new JScrollPane(this.diyMandGenVaryApplyFormulaTxtArea);
	private String diyMandApplyFormulaStr = "NONE";

	///////////////// ends apply function
	///////////////// Mandelbrot////////////////////////////////////

	///////////////// PolyFract///////////////////////////////////
	private boolean polyApplyFormulaZ = false;
	private JCheckBox polyApplyFormulaZCb = new JCheckBox("Apply Formula f(Z) = ", false);
	private JTextField polyApplyFormulaTf = new JTextField(10);
	private JTextArea polyGenVaryApplyFormulaTxtArea = new JTextArea(1, 10);
	private JScrollPane polyGenVaryApplyFormulaTxtAreaSpane = new JScrollPane(this.polyGenVaryApplyFormulaTxtArea);
	private String polyApplyFormulaStr = "NONE";

	///////////////// ends apply function
	///////////////// PolyFract////////////////////////////////////
	
	
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
		
	//POLY
	private final Integer[] polyExpOptions = EXPONENTS;
//	private final JCheckBox polyUseLyapunovExpCb = new JCheckBox("UseLyapunovExponentOnly", false );
//	private boolean polyUseLyapunovExponent = false;
	
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
	protected double polyConstReal;	
	protected double polyConstImg;
	
	
	protected double polyXC;
	protected double polyYC;
	protected double polyScaleSize;
	
	
	/////////
	private boolean keepConst/* = true*/;
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
	private boolean useColorGradient6 = false;
	
	
	private double rotation = 0.0;
	
	private JLabel rotLabel = new JLabel("Rotation:");
	private Vector<Double> rotOptions = new Vector<Double>();
	protected JComboBox<Double> rotateCombo;

	private JComboBox<String> colorChoiceCombo = new JComboBox<String>(COLOR_OPTIONS);
	private String colorChoice = "ColorPalette";
	private final JCheckBox showAllColorsCb = new JCheckBox("Show All Colors", false);
	private JButton buColorChooser = new JButton("ColorChooser");
	

	private final JLabel colorBlowoutChoiceLabel = new JLabel("Color Blowout Types:");	
	private JComboBox<String> colorBlowoutChoiceCombo = new JComboBox<String>(COLOR_BLOWOUT_TYPES);
	private String colorBlowoutChoice = "Default";
	
	private final JLabel colorSuperBlowoutChoiceLabel = new JLabel("Color SuperBlowout Types:");	
	private JComboBox<String> colorSuperBlowoutChoiceCombo = new JComboBox<String>(COLOR_SUPER_BLOWOUT_TYPES);
	private String colorSuperBlowoutChoice = "Original";
	
	private final JLabel colorSampleMixStartValsLabel = new JLabel("Color Start Vals:");
	private final JComboBox<String> colorSampleMixStartValsCombo = new JComboBox<String>(COLOR_SAMPLE_STARTVAL_OPTIONS);
	private String colorSampleMixStartVals = "POW2_4_200";
	private final JLabel colorSampleDivValsLabel = new JLabel("Divisor Vals:");
	private final JComboBox<String> colorSampleDivValsCombo = new JComboBox<String>(COLOR_SAMPLE_DIV_OPTIONS);
	private String colorSampleDivVals = "FRST_SIX_PRIMES";
	
	
	//for complexNumber z	= xtranformed operation ytrasnform
	private JComboBox<String>	pxXTransformCombo = new JComboBox<String>(PIX_TRANSFORM_OPTIONS);
	private JComboBox<String>	pxYTransformCombo = new JComboBox<String>(PIX_TRANSFORM_OPTIONS);
	
	private String pixXTransform = "none";
	private String pixYTransform = "none";

//	Pix_XY_Operation
	private JComboBox<String>	intraPixOperationCombo = new JComboBox<String>(PIX_INTRA_OPRNS);
	private String pixIntraXYOperation = "Plus";
	
	/////////////////////	z=tx(x)..operation...ty(y)
	
	// for Constants
	private JComboBox<String> constFuncCombo = new JComboBox<String>(FUNCTION_OPTIONS);	
	private String constFuncChoice = "None";
	
	// for pixels
	private JCheckBox invertPixelsCb = new JCheckBox("Invert Pixel Calculation", false);
	private boolean invertPixelCalculation = false;
	
	private JComboBox<String> pxFuncCombo = new JComboBox<String>(FUNCTION_OPTIONS);
	private String pxFuncChoice = "None";

	private JComboBox<String> pxConstOprnCombo = new JComboBox<String>(PIX_CONST_OPRNS);
	private String pxConstOprnChoice = "Plus";
	
	private final JCheckBox magnifyCb = new JCheckBox("Magnify",false);
	private boolean doMagnify = false;
	
	private BufferedImage fractalImage;
	private FractalBase fBase;
	
	private JButton buPrint = new JButton("Print");
	private JButton buSave = new JButton("Save");
	private JButton buSaveWithData = new JButton("SaveWithData");
	private JButton buSaveWithDetailData = new JButton("SaveWithDetailData");
	private JButton buSave2File = new JButton("Save2File");
	private JButton buSaveWithData2File = new JButton("SaveWithData2File");
	private String save2File;
	private String save2FileWithData;
	
	private Thread fbf;

	private String fieldLines = "None";
	
	//for---explore--zoom-focus
//	Image[] fbImages = new Image[10];
	//private JPanel fbImagesPanel = new JPanel(new GridLayout(1,0),true);
	private JButton buShowFbImages = new JButton("Show all Images");
	private JButton buShowFbInfo = new JButton("Show Info");
	private JButton buFlushFbImages = new JButton("Flush all Images");
	
	private JCheckBox useRangeCb = new JCheckBox("Use Range Estimates: ",false);
	private boolean useRangeEstimation = false;
	
	private JTextField xMinTf = new JTextField(5);
	private double xMinVal;	
	private JTextField yMinTf = new JTextField(5);
	private double yMinVal;
	
	private JTextField xMaxTf = new JTextField(5);
	private double xMaxVal;	
	private JTextField yMaxTf = new JTextField(5);
	private double yMaxVal;
	
	private JCheckBox captureOrbitCb = new JCheckBox("Capture Orbit",false);
	private boolean captureOrbit = false;
	private JComboBox<String> orbitPointsCombo = new JComboBox<String>(ORBIT_POINT_OPTIONS);
	private ComplexNumber orbitTrapPointChoice;
	private JLabel trapSizeLbl = new JLabel("Trap Size");
	private JComboBox<Double> trapSizeCombo = new JComboBox<Double>(TRAP_SIZE_OPTIONS);
	private Double trapSizeChoice;
	private JLabel trapShapeLbl = new JLabel("Trap Shape");
	private JComboBox<String> trapShapeCombo = new JComboBox<String>(TRAP_SHAPE_OPTIONS);
	private String trapShapeChoice;

	
	//	navigation controls

	private JButton buFirst = new JButton("||<| First");
	private JButton buBack = new JButton("<|| Back");
	private JButton buStep = new JButton("Step ||>");
	private JButton buLast = new JButton("Last |>||");
	private JTextField tfCount = new JTextField(3);
	
	/*J10static List<BufferedImage> rawFbZoomedImages = new ArrayList<BufferedImage>();
	static List<FractalDtlInfo> fbDTlsList = new ArrayList<FractalDtlInfo>();
	static int currentIndex = 0;*/
	
	
	
//////////////////////////////ATTRACTOR////////////////////////////
	private final String[] attractorChoiceOptions = ATTRACTOR_CHOICES;
	private final JComboBox<String> attractorChoiceCombos = new JComboBox<String>(attractorChoiceOptions);
	
	private final JCheckBox attractorDimChoiceCb = new JCheckBox("3D", true);
	private boolean isAttractorDimSpace3D = true;

	private final JCheckBox attractorPixellateChoiceCb = new JCheckBox("Pixellate", true);
	private boolean isAttractorPixellated = true;

	private final JCheckBox attractorSingularChoiceCb = new JCheckBox("Plot Single Attractor", false);
	private boolean isSingularAttractor = false;

	private final JCheckBox attractorInstantDrawCb = new JCheckBox("InstantDraw Attractor", false);
	private boolean isAttractorInstantDraw = false;

	private final JComboBox<Integer> attractorPauseJumpCombo = new JComboBox<Integer>(new Integer[] { 10, 1, 5, 15 });
	private int attractorPauseJump;
	
	private final JCheckBox attractorTimeInvariantCb = new JCheckBox("TimeInvariant Attractor",false);
	private boolean isAttractorTimeInvariant = false;
	
	private final JCheckBox attractorTimeIterDependantCb = new JCheckBox("TimeIterDependant Attractor(t)",false);
	private boolean isAttractorTimeIterDependant = false;
	
	
	private String attractorSelectionChoice;
	
	private JTextField attr1SeedX_tf = new JTextField(2);
	private JTextField attr1SeedY_tf = new JTextField(2);
	private JTextField attr1SeedZ_tf = new JTextField(2);
	private JButton attr1ColorChooserBu = new JButton();
	private Color attractor1Color;
	
	/*private double attr1SeedXVal;	
	private double attr1SeedYVal;	
	private double attr1SeedZVal;*/
	
	private JTextField attr2SeedX_tf = new JTextField(2);
	private JTextField attr2SeedY_tf = new JTextField(2);
	private JTextField attr2SeedZ_tf = new JTextField(2);
	private JButton attr2ColorChooserBu = new JButton();
	private Color attractor2Color;
	
	/*private double attr2SeedXVal;	
	private double attr2SeedYVal;	
	private double attr2SeedZVal;*/
	private List<JLabel> attrSeed_X_lbList = new ArrayList<JLabel>();
	private List<JLabel> attrSeed_Y_lbList = new ArrayList<JLabel>();
	private List<JLabel> attrSeed_Z_lbList = new ArrayList<JLabel>();
	private List<JTextField> attrSeed_X_tfList = new ArrayList<JTextField>();
	private List<JTextField> attrSeed_Y_tfList = new ArrayList<JTextField>();
	private List<JTextField> attrSeed_Z_tfList = new ArrayList<JTextField>();
	private List<JButton> attrSeed_Clr_buList = new ArrayList<JButton>();
	private List<Color>	attrSeed_ClrChList = new ArrayList<Color>();
	
	JButton addAttrBu = new JButton("Add", new ImageIcon("res/add.gif"));
	JButton removeAttrBu = new JButton("Remove", new ImageIcon("res/remove.gif"));

	private JTextField attrMaxIter_tf = new JTextField(2);
	private JTextField attrDeltaTime_tf = new JTextField(2);	
	/*private double attrMaxIterVal;	*/
	
	private final String[] attractorSpace2DChoiceOptions = ATTRACTOR_SPACE_3D_CHOICES;
	/*private final JComboBox<String> attractorSpace2DChoiceCombos = new JComboBox<String>(attractorSpace2DChoiceOptions);
	private String attractorSpace2DSelectionChoice;*/
	private final JList<String> attractorSpace2DChoiceList = new JList<String>(attractorSpace2DChoiceOptions);
	private List<String> attractorSpace2DList;
	
	/*private JTextField attrCustomSeedX_tf = new JTextField(2);
	private JTextField attrCustomSeedY_tf = new JTextField(2);
	private JTextField attrCustomSeedZ_tf = new JTextField(2);
	
	private double attrCustomSeedXVal;	
	private double attrCustomSeedYVal;	
	private double attrCustomSeedZVal;*/
	
	//lorenz
	private JLabel attrLorenzSigmaLabel = new JLabel("Set sigma: ");
	private JTextField attrLorenzSigma_tf = new JTextField(2);
	private JLabel attrLorenzRhoLabel = new JLabel("  rho: ");
	private JTextField attrLorenzRho_tf = new JTextField(2);
	private JLabel attrLorenzBetaLabel = new JLabel("  beta: ");
	private JTextField attrLorenzBeta_tf = new JTextField(2);
	
	//aizawa
	private JLabel attrAizawaALabel = new JLabel("Set a: ");
	private JTextField attrAizawaA_tf = new JTextField(2);
	private JLabel attrAizawaBLabel = new JLabel("  b: ");
	private JTextField attrAizawaB_tf = new JTextField(2);
	private JLabel attrAizawaCLabel = new JLabel("  c: ");
	private JTextField attrAizawaC_tf = new JTextField(2);
	private JLabel attrAizawaDLabel = new JLabel("  d: ");
	private JTextField attrAizawaD_tf = new JTextField(2);
	private JLabel attrAizawaELabel = new JLabel("  e: ");
	private JTextField attrAizawaE_tf = new JTextField(2);
	private JLabel attrAizawaFLabel = new JLabel("  f: ");
	private JTextField attrAizawaF_tf = new JTextField(2);
	
	
	//dejong
	private JLabel attrDeJongALabel = new JLabel("Set a: ");
	private JTextField attrDeJongA_tf = new JTextField(2);
	private JLabel attrDeJongBLabel = new JLabel("  b: ");
	private JTextField attrDeJongB_tf = new JTextField(2);
	private JLabel attrDeJongCLabel = new JLabel("  c: ");
	private JTextField attrDeJongC_tf = new JTextField(2);
	private JLabel attrDeJongDLabel = new JLabel("  d: ");
	private JTextField attrDeJongD_tf = new JTextField(2);
	

	private JLabel attrCustomFormulaStrLabel = new JLabel("Custom Formula: ");
	
	private JLabel attrCustomFormulaStrDeltaXLabel = new JLabel("Delta X or dx: ");
	private JLabel attrCustomFormulaStrDeltaYLabel = new JLabel("Delta Y or dy: ");
	private JLabel attrCustomFormulaStrDeltaZLabel = new JLabel("Delta Z or dz: ");
	private JTextField attrCustom_DeltaXFormula_tf = new JTextField(10);
	private JTextField attrCustom_DeltaYFormula_tf = new JTextField(10);
	private JTextField attrCustom_DeltaZFormula_tf = new JTextField(10);
	
	/*private String attrCustom_DeltaXFormula;	
	private String attrCustom_DeltaYFormula;	
	private String attrCustom_DeltaZFormula;*/
	private AttractorsGenerator[] generators;
	
	////////////////////////////////ENDS	ATTRACTOR////////////////////////////
	
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
		this.add(new JLabel("Choose Color:"));
		this.add(this.colorChoiceCombo);
		this.add(this.showAllColorsCb);
//		this.add(this.buColorChooser);
		this.add(this.smoothenColorCb);
		
		this.add(this.colorSampleMixStartValsLabel);
		this.colorSampleMixStartValsLabel.setVisible(false);
		this.add(this.colorSampleMixStartValsCombo);
		this.colorSampleMixStartValsCombo.setVisible(false);

		this.add(this.colorSampleDivValsLabel);
		this.colorSampleDivValsLabel.setVisible(false);
		this.add(this.colorSampleDivValsCombo);
		this.colorSampleDivValsCombo.setVisible(false);			

		this.add(this.colorBlowoutChoiceLabel);
		this.colorBlowoutChoiceLabel.setVisible(false);
		this.add(this.colorBlowoutChoiceCombo);
		this.colorBlowoutChoiceCombo.setVisible(false);
		
		this.add(this.colorSuperBlowoutChoiceLabel);
		this.colorSuperBlowoutChoiceLabel.setVisible(false);
		this.add(this.colorSuperBlowoutChoiceCombo);
		this.colorSuperBlowoutChoiceCombo.setVisible(false);
		
		this.add(new JLabel("PixelTransformation:  X"));
		this.add(this.pxXTransformCombo);
		this.add(new JLabel(",  Y"));
		this.add(this.pxYTransformCombo);
		
		this.add(new JLabel("Intra Pixel Operation:"));
		this.add(this.intraPixOperationCombo);
		
		this.add(this.invertPixelsCb);
		
		this.add(new JLabel("Pixel z(x,y) Functions:"));
		this.add(this.pxFuncCombo);
		
		// creates-functional-choices-for-constants
		this.add(new JLabel("Constant (C) Functions:"));
		this.add(this.constFuncCombo);
		
		this.add(new JLabel("Pixel-Constant Operation:"));
		this.add(this.pxConstOprnCombo);
		
		// creates-rototation-choice-options -- does not add
		this.createRotationCombo();

		this.rotLabel.setVisible(false);
		this.rotateCombo.setVisible(false);
		this.add(this.rotLabel);
		this.add(this.rotateCombo);

		this.formulaArea.setVisible(false);
		this.add(this.formulaArea);

		this.buStart.setEnabled(false);
		this.add(this.buStart);	
		
		this.buSavePxStart.setVisible(false);

		this.buSavePxStart.setEnabled(false);
		this.add(this.buSavePxStart);
		// this.add(this.buPause);
		this.add(this.buPrint);
		
		this.add(this.buSave);
		this.add(this.buSaveWithData);
//		this.add(this.buSaveWithDetailData);
		this.add(this.buSave2File);
		this.add(this.buSaveWithData2File);
		
		this.buClose.setEnabled(false);
		this.add(this.buClose);

		this.add(this.buShowFbImages);
		this.add(this.buShowFbInfo);
		this.add(this.buFlushFbImages);
		
		this.add(this.useRangeCb);

		this.add(new JLabel("Minimum (x,y) :  X:"));
		this.add(this.xMinTf);
		this.add(new JLabel("   Y:"));
		this.add(this.yMinTf);


		this.add(new JLabel("Maximum (x,y) :  X:"));
		this.add(this.xMaxTf);
		this.add(new JLabel("   Y:"));
		this.add(this.yMaxTf);
		
		this.xMinTf.setEnabled(false);
		this.yMinTf.setEnabled(false);
		this.xMaxTf.setEnabled(false);
		this.yMaxTf.setEnabled(false);
		
		
		this.add(this.captureOrbitCb);
		this.add(this.orbitPointsCombo);
		this.orbitPointsCombo.setVisible(false);
		this.add(this.trapSizeLbl);
		this.trapSizeLbl.setVisible(false);
		this.add(this.trapSizeCombo);
		this.trapSizeCombo.setVisible(false);
		this.trapShapeLbl.setVisible(false);
		this.add(this.trapShapeCombo);
		this.trapShapeCombo.setVisible(false);
		
		
		///
		/*this.add(this.buFirst);
		this.add(this.buBack);
		this.add(this.buStep);
		this.add(this.buLast);
		this.add(this.tfCount);
		
		this.buFirst.setEnabled(true);
		this.buBack.setEnabled(true);
		this.buStep.setEnabled(false);
		this.buLast.setEnabled(true);
		this.tfCount.setEnabled(false);*/
		
		

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
		
		//Creating separate JFrame for Attractor choices, etc
		/*this.createAttractorsPanel();*/
		
		//	diy	panel	--	does add
		this.createDIYPanel();
		
		
		/*this.createFBImagesPanel();*/
	}

	private void createRotationCombo() {
		for (int i = 0; i < 1000; i+=15) {
			this.rotOptions.add((double) i);
		}
		
		this.rotateCombo=new JComboBox<Double>(this.rotOptions);
		this.rotateCombo.setVisible(false);
	}
	
	private static Double[] getScaleSizeOptions() {
		final double start = -50.0;
		final double end = 50.0;
		double tempVal = start;
		Vector<Double> theSzVec = new Vector<Double>();
		while(tempVal<=end){
			theSzVec.add(tempVal);
			tempVal+=0.5;
		}
		Double[] bdArr = new Double[theSzVec.size()];
		for(int i = 0; i < theSzVec.size();i++){
			bdArr[i]=theSzVec.get(i);
		}
		return bdArr;
	}

	private static String[] getOperations() {
		return new String[]{
			OpsEnum.Plus.getOperation(),
			OpsEnum.Minus.getOperation(),
			OpsEnum.Multiply.getOperation(),
			OpsEnum.Divide.getOperation(),
			OpsEnum.Power.getOperation()
		};
	}
	
	
	private enum OpsEnum {
		Plus("Plus"),Minus("Minus"),Multiply("Multiply"),Divide("Divide"),Power("Power");
		
		private String operation;

		private OpsEnum(String op){
			this.setOperation(op);
		}

		public final String getOperation() {
			return this.operation;
		}

		public void setOperation(String oprn) {
			this.operation = oprn;
		}
	}
	
	private static Double[] getMotionParamJumpOptions() {
		final double start = -10.0;
		final double end = 10.0;
		double tempVal = start;
		Vector<Double> theBdVec = new Vector<Double>();
		while(tempVal<=end){
			theBdVec.add(tempVal);
			tempVal+=0.25;
		}
		Double[] bdArr = new Double[theBdVec.size()];
		for(int i = 0; i < theBdVec.size();i++){
			bdArr[i]=theBdVec.get(i);
		}
		return bdArr;
	}

	private static Integer[] getPixelPowerZJumpOptions() {
		return new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	}
	
	private static Double[] getConstantJumpOptions() {
		return new Double[] { 0.0001, 0.001, 0.01, 0.025, 0.05, 0.075, 0.1, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, 3.5, 3.75, 4.0, 4.5, 5.0 };
	}
	
	private static Double[] getBoundaryJumpOptions(){
		return new Double[] { 0.0001, 0.001, 0.01, 0.025, 0.05, 0.075, 0.1, 0.25, 0.5, 0.75, 1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, 3.5, 3.75, 4.0, 4.5, 5.0 };
	}
	
	private static Double[] getScaleSizeJumpOptions(){
		return new Double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0, 3.5, 3.75, 4.0, 4.5, 5.0 };
	}

	private static Double[] getBoundaryOptions() {
		final double start = -100.0;
		final double end = 100.0;
		double tempVal = start;
		Vector<Double> theBdVec = new Vector<Double>();
		while(tempVal<=end){
			theBdVec.add(tempVal);
			tempVal+=0.5;
		}
		Double[] bdArr = new Double[theBdVec.size()];
		for(int i = 0; i < theBdVec.size();i++){
			bdArr[i]=theBdVec.get(i);
		}
		return bdArr;
	}
	
	
	private JPanel createAttractorsPanel() {
		/*this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		
		this.colorChoiceCombo.setVisible(false);
		this.showAllColorsCb.setVisible(false);

		this.constFuncCombo.setVisible(false);

		this.pxFuncCombo.setVisible(false);*/
		this.attractorsPanel = new JPanel(/*new GridLayout(20, 15)*/);
		
		this.attrSeed_X_tfList.add(this.attr1SeedX_tf);
		this.attrSeed_Y_tfList.add(this.attr1SeedY_tf);
		this.attrSeed_Z_tfList.add(this.attr1SeedZ_tf);
		this.attrSeed_Clr_buList.add(this.attr1ColorChooserBu);
		
		this.attrSeed_X_tfList.add(this.attr2SeedX_tf);
		this.attrSeed_Y_tfList.add(this.attr2SeedY_tf);
		this.attrSeed_Z_tfList.add(this.attr2SeedZ_tf);
		this.attrSeed_Clr_buList.add(this.attr2ColorChooserBu);

		this.attractorsPanel.add(new JLabel("Choose Atractor:"));
		this.attractorsPanel.add(this.attractorChoiceCombos);	
		
		this.attractorsPanel.add(this.attractorDimChoiceCb);
		this.attractorsPanel.add(this.attractorPixellateChoiceCb);

		this.attractorsPanel.add(this.attractorSingularChoiceCb);
		this.attractorsPanel.add(this.attractorInstantDrawCb);
		this.attractorsPanel.add(new JLabel("AtractorPause:"));
		this.attractorsPanel.add(this.attractorPauseJumpCombo);
		this.attractorsPanel.add(this.attractorTimeInvariantCb);
		
		//lorenz parameters	
		this.attrLorenzSigmaLabel.setVisible(false);
		this.attractorsPanel.add(this.attrLorenzSigmaLabel);
		this.attrLorenzSigma_tf.setVisible(false);
		this.attractorsPanel.add(this.attrLorenzSigma_tf);
		this.attrLorenzRhoLabel.setVisible(false);
		this.attractorsPanel.add(this.attrLorenzRhoLabel);
		this.attrLorenzRho_tf.setVisible(false);
		this.attractorsPanel.add(this.attrLorenzRho_tf);
		this.attrLorenzBetaLabel.setVisible(false);
		this.attractorsPanel.add(this.attrLorenzBetaLabel);
		this.attrLorenzBeta_tf.setVisible(false);
		this.attractorsPanel.add(this.attrLorenzBeta_tf);
		
		//aizawa
		this.attrAizawaALabel.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaALabel);
		this.attrAizawaA_tf.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaA_tf);
		this.attrAizawaBLabel.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaBLabel);
		this.attrAizawaB_tf.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaB_tf);
		this.attrAizawaCLabel.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaCLabel);
		this.attrAizawaC_tf.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaC_tf);
		this.attrAizawaDLabel.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaDLabel);
		this.attrAizawaD_tf.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaD_tf);
		this.attrAizawaELabel.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaELabel);
		this.attrAizawaE_tf.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaE_tf);
		this.attrAizawaFLabel.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaFLabel);
		this.attrAizawaF_tf.setVisible(false);
		this.attractorsPanel.add(this.attrAizawaF_tf);
		
		//dejong
		this.attrDeJongALabel.setVisible(false);
		this.attractorsPanel.add(this.attrDeJongALabel);
		this.attrDeJongA_tf.setVisible(false);
		this.attractorsPanel.add(this.attrDeJongA_tf);
		this.attrDeJongBLabel.setVisible(false);
		this.attractorsPanel.add(this.attrDeJongBLabel);
		this.attrDeJongB_tf.setVisible(false);
		this.attractorsPanel.add(this.attrDeJongB_tf);
		this.attrDeJongCLabel.setVisible(false);
		this.attractorsPanel.add(this.attrDeJongCLabel);
		this.attrDeJongC_tf.setVisible(false);
		this.attractorsPanel.add(this.attrDeJongC_tf);
		this.attrDeJongDLabel.setVisible(false);
		this.attractorsPanel.add(this.attrDeJongDLabel);
		this.attrDeJongD_tf.setVisible(false);
		this.attractorsPanel.add(this.attrDeJongD_tf);
		
		this.attractorsPanel.add(new JLabel("Attractor1  Seed   X:"));
		this.attractorsPanel.add(this.attr1SeedX_tf);
		this.attractorsPanel.add(new JLabel("   Y:"));
		this.attractorsPanel.add(this.attr1SeedY_tf);
		this.attractorsPanel.add(new JLabel("   Z:"));
		this.attractorsPanel.add(this.attr1SeedZ_tf);
		this.attr1ColorChooserBu./*createToolTip().*/setToolTipText("Choose Attractor1 color");
		this.attractorsPanel.add(this.attr1ColorChooserBu);
		
		this.attractorsPanel.add(new JLabel("Attractor2  Seed   X:"));
		this.attractorsPanel.add(this.attr2SeedX_tf);
		this.attractorsPanel.add(new JLabel("   Y:"));
		this.attractorsPanel.add(this.attr2SeedY_tf);
		this.attractorsPanel.add(new JLabel("   Z:"));
		this.attractorsPanel.add(this.attr2SeedZ_tf);
		this.attr2ColorChooserBu/*.createToolTip()*/.setToolTipText("Choose Attractor2 color");
		this.attractorsPanel.add(this.attr2ColorChooserBu);

		this.addAttrBu.setSize(20, 20);
		this.attractorsPanel.add(this.addAttrBu);
		
		
		/////CUSTOM	--	staysHiden
		this.attractorsPanel.add(this.attrCustomFormulaStrLabel );
		
		this.attractorsPanel.add(this.attractorTimeIterDependantCb);
		
		this.attractorsPanel.add(this.attrCustomFormulaStrDeltaXLabel );
		this.attractorsPanel.add(this.attrCustom_DeltaXFormula_tf );
		this.attractorsPanel.add(this.attrCustomFormulaStrDeltaYLabel );
		this.attractorsPanel.add(this.attrCustom_DeltaYFormula_tf );
		this.attractorsPanel.add(this.attrCustomFormulaStrDeltaZLabel );
		this.attractorsPanel.add(this.attrCustom_DeltaZFormula_tf );
		
		this.attrCustomFormulaStrLabel.setVisible(false);
		this.attrCustomFormulaStrDeltaXLabel.setVisible(false);
		this.attrCustom_DeltaXFormula_tf.setVisible(false);
		this.attrCustomFormulaStrDeltaYLabel.setVisible(false);
		this.attrCustom_DeltaYFormula_tf.setVisible(false);
		this.attrCustomFormulaStrDeltaZLabel.setVisible(false);
		this.attrCustom_DeltaZFormula_tf.setVisible(false);
		
		this.attractorsSeedsPanel = this.createAttractorSeedsPanel();
		this.attractorsPanel.add(this.attractorsSeedsPanel);

		this.attractorsPanel.add(new JLabel(" Delta Time dt :"));
		this.attractorsPanel.add(this.attrDeltaTime_tf);

		this.attractorsPanel.add(new JLabel(" Max Iterations  :"));
		this.attractorsPanel.add(this.attrMaxIter_tf);

		this.attractorsPanel.add(new JLabel(" Space(2D)  :"));
		/*this.attractorsPanel.add(this.attractorSpace2DChoiceCombos);*/
		this.attractorsPanel.add(this.attractorSpace2DChoiceList);
		
		this.buStart.setEnabled(true);
		this.attractorsPanel.add(this.buStart);
		
		this.buSave.setEnabled(true);
		this.buSaveWithData.setEnabled(true);
		this.attractorsPanel.add(this.buSave);
		this.attractorsPanel.add(this.buSaveWithData);

		this.buSave2File.setEnabled(true);
		this.buSaveWithData2File.setEnabled(true);
		this.attractorsPanel.add(this.buSave2File);
		this.attractorsPanel.add(this.buSaveWithData2File);
		
		return this.attractorsPanel;
		
		/*this.attractorsPanel.setVisible(false);
		this.add(this.attractorsPanel);*/
	}
	
	private JPanel createAttractorSeedsPanel() {
		return this.attractorsSeedsPanel = new JPanel(/*new GridLayout(10,10)*/);
	}

	private void createPolyPanel() {
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		
		this.colorChoiceCombo.setVisible(true);
		this.showAllColorsCb.setVisible(true);

		this.constFuncCombo.setVisible(true);

		this.pxFuncCombo.setVisible(true);
		
		this.polyOptionsPanel.add(new JLabel("Exponent(X):"));
		this.polyOptionsPanel.add(this.polyExpCombos);		
		this.polyOptionsPanel.add(this.polyUseDiffCb);
		
		
//		this.polyOptionsPanel.add(this.polyUseLyapunovExpCb);
		
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
		
		this.setupPolyGeneratorPanel();
		
		this.polyOptionsPanel.setVisible(false);
		this.add(this.polyOptionsPanel);
	}
	
	private void setupPolyGeneratorPanel() {
		this.polyOptionsPanel.add(this.polyGenCb);
		
		this.polyVaryColorCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryColorCb);
		this.polyVaryPixXTranCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryPixXTranCb);
		this.polyVaryPixYTranCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryPixYTranCb);
		this.polyVaryIntraPixXYCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryIntraPixXYCb);
		
		this.polyVaryPixelZFuncCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryPixelZFuncCb);

		this.polyVaryPixelPowerZCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryPixelPowerZCb);

		this.polyGenPixelPowerZFromLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenPixelPowerZFromLabel);
		this.polyGenPixelPowerZFromTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenPixelPowerZFromTf);
		this.polyGenPixelPowerZToLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenPixelPowerZToLabel);
		this.polyGenPixelPowerZToTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenPixelPowerZToTf);
		this.polyGenPixelPowerZJumpLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenPixelPowerZJumpLabel);
		this.polyGenPixelPowerZJumpCombo.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenPixelPowerZJumpCombo);

		this.polyVaryConstCFuncCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryConstCFuncCb);
		this.polyVaryPixelConstOpZCCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryPixelConstOpZCCb);
		
		this.polyVaryGenConstantCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryGenConstantCb);		
		
		this.polyGenRealFromLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenRealFromLabel);
		this.polyGenRealFromTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenRealFromTf);
		this.polyGenRealToLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenRealToLabel);
		this.polyGenRealToTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenRealToTf);
		
		this.polyGenRealJumpLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenRealJumpLabel);
		this.polyGenRealJumpCombo.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenRealJumpCombo);
		

		this.polyGenImagFromLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenImagFromLabel);
		this.polyGenImagFromTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenImagFromTf);
		this.polyGenImagToLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenImagToLabel);
		this.polyGenImagToTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenImagToTf);
		
		this.polyGenImagJumpLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenImagJumpLabel);
		this.polyGenImagJumpCombo.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenImagJumpCombo);	

		this.polyVaryIterCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryIterCb);
		
		this.polyVaryRCMTCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryRCMTCb);
		
		this.polyVaryBoundaryCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryBoundaryCb);
		this.polyGenBoundaryFromLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenBoundaryFromLabel);
		this.polyGenBoundaryFromTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenBoundaryFromTf);
		this.polyGenBoundaryToLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenBoundaryToLabel);
		this.polyGenBoundaryToTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenBoundaryToTf);
		this.polyGenBoundaryJumpLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenBoundaryJumpLabel);	
		this.polyGenBoundaryJumpCombo.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenBoundaryJumpCombo);
		

		this.polyVaryPixXCentrCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryPixXCentrCb);

		this.polyVaryPixYCentrCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryPixYCentrCb);

		this.polyVaryScaleSizeCb.setVisible(false);
		this.polyOptionsPanel.add(this.polyVaryScaleSizeCb);
		
		this.polyGenScaleSizeFromLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenScaleSizeFromLabel);
		this.polyGenScaleSizeFromTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenScaleSizeFromTf);
		this.polyGenScaleSizeToLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenScaleSizeToLabel);
		this.polyGenScaleSizeToTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenScaleSizeToTf);
		this.polyGenScaleSizeJumpLabel.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenScaleSizeJumpLabel);	
		this.polyGenScaleSizeJumpCombo.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenScaleSizeJumpCombo);
		
		this.polyApplyFormulaZCb.setVisible(true);
		this.polyOptionsPanel.add(this.polyApplyFormulaZCb);
		this.polyApplyFormulaTf.setVisible(false);
		this.polyOptionsPanel.add(this.polyApplyFormulaTf);
		this.polyGenVaryApplyFormulaTxtArea.setVisible(false);
		this.polyGenVaryApplyFormulaTxtAreaSpane.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenVaryApplyFormulaTxtAreaSpane);	
		
		
		this.polyGenBu.setVisible(false);
		this.diyPolyGen2FolderBu.setVisible(false);
		this.polyOptionsPanel.add(this.polyGenBu);
		this.polyOptionsPanel.add(this.diyPolyGen2FolderBu);
	}
	/*
	private void createFBImagesPanel() {
		this.fbImagesPanel.add(this.buShowFbImages);
		this.add(this.fbImagesPanel);
	}*/

	

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

		/*this.diyMandRb.setSelected(true);*/
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

		this.colorChoiceCombo.setVisible(true);
		this.showAllColorsCb.setVisible(true);

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
		this.colorChoiceCombo.setVisible(true);
		this.showAllColorsCb.setVisible(true);

		this.constFuncCombo.setVisible(true);
		
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);

		this.pxFuncCombo.setVisible(true);
		
		this.diyJuliaPanel.add(new JLabel("Power:"));
		this.diyJuliaPanel.add(this.diyJuliaPowerCombos);
		this.diyJuliaPanel.add(this.diyJuliaUseDiffCb);
		
		this.diyJuliaPanel.add(this.diyJuliaExploreCb);
		


//		this.diyJuliaPanel.add(this.diyJuliaUseLyapunovExpCb);
		
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
		this.diyJuliaPanel.add(this.diyJuliaAreaSizeCombos);
		
		this.diyJuliaPanel.add(new JLabel("Boundary: "));
		this.diyJuliaPanel.add(this.diyJuliaBoundCombos);
		
		this.diyJuliaPanel.add(new JLabel("Center: X "));
		this.diyJuliaPanel.add(this.diyJuliaXCCombos);
		this.diyJuliaPanel.add(new JLabel(" Y "));
		this.diyJuliaPanel.add(this.diyJuliaYCCombos);
		this.diyJuliaPanel.add(new JLabel("ScaleSize:"));
		this.diyJuliaPanel.add(this.diyJuliaScaleSizeCombos);
		
		this.setupDiyJuliaGeneratorPanel();
		
		this.diyJuliaPanel.setVisible(false);
	}

	private void setupDiyJuliaGeneratorPanel() {
		this.diyJuliaPanel.add(this.diyJuliaGenCb);
		
		this.diyJuliaVaryColorCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryColorCb);
		this.diyJuliaVaryPixXTranCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryPixXTranCb);
		this.diyJuliaVaryPixYTranCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryPixYTranCb);
		this.diyJuliaVaryIntraPixXYCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryIntraPixXYCb);
		
		this.diyJuliaVaryPixelZFuncCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryPixelZFuncCb);

		this.diyJuliaVaryPixelPowerZCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryPixelPowerZCb);

		this.diyJuliaGenPixelPowerZFromLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenPixelPowerZFromLabel);
		this.diyJuliaGenPixelPowerZFromTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenPixelPowerZFromTf);
		this.diyJuliaGenPixelPowerZToLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenPixelPowerZToLabel);
		this.diyJuliaGenPixelPowerZToTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenPixelPowerZToTf);
		this.diyJuliaGenPixelPowerZJumpLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenPixelPowerZJumpLabel);
		this.diyJuliaGenPixelPowerZJumpCombo.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenPixelPowerZJumpCombo);

		this.diyJuliaVaryConstCFuncCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryConstCFuncCb);
		this.diyJuliaVaryPixelConstOpZCCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryPixelConstOpZCCb);
		
		this.diyJuliaVaryGenConstantCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryGenConstantCb);		
		
		this.diyJuliaGenRealFromLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenRealFromLabel);
		this.diyJuliaGenRealFromTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenRealFromTf);
		this.diyJuliaGenRealToLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenRealToLabel);
		this.diyJuliaGenRealToTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenRealToTf);
		
		this.diyJuliaGenRealJumpLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenRealJumpLabel);
		this.diyJuliaGenRealJumpCombo.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenRealJumpCombo);
		

		this.diyJuliaGenImagFromLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenImagFromLabel);
		this.diyJuliaGenImagFromTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenImagFromTf);
		this.diyJuliaGenImagToLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenImagToLabel);
		this.diyJuliaGenImagToTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenImagToTf);
		
		this.diyJuliaGenImagJumpLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenImagJumpLabel);
		this.diyJuliaGenImagJumpCombo.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenImagJumpCombo);	
		
		this.diyJuliaVaryFieldTypeCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryFieldTypeCb);

		this.diyJuliaVaryIterCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryIterCb);
		
		this.diyJuliaVaryBoundaryCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryBoundaryCb);
		this.diyJuliaGenBoundaryFromLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenBoundaryFromLabel);
		this.diyJuliaGenBoundaryFromTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenBoundaryFromTf);
		this.diyJuliaGenBoundaryToLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenBoundaryToLabel);
		this.diyJuliaGenBoundaryToTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenBoundaryToTf);
		this.diyJuliaGenBoundaryJumpLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenBoundaryJumpLabel);	
		this.diyJuliaGenBoundaryJumpCombo.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenBoundaryJumpCombo);
		

		this.diyJuliaVaryPixXCentrCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryPixXCentrCb);

		this.diyJuliaVaryPixYCentrCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryPixYCentrCb);

		this.diyJuliaVaryScaleSizeCb.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaVaryScaleSizeCb);
		
		this.diyJuliaGenScaleSizeFromLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenScaleSizeFromLabel);
		this.diyJuliaGenScaleSizeFromTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenScaleSizeFromTf);
		this.diyJuliaGenScaleSizeToLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenScaleSizeToLabel);
		this.diyJuliaGenScaleSizeToTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenScaleSizeToTf);
		this.diyJuliaGenScaleSizeJumpLabel.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenScaleSizeJumpLabel);	
		this.diyJuliaGenScaleSizeJumpCombo.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenScaleSizeJumpCombo);
		
		this.diyJApplyFormulaZCb.setVisible(true);
		this.diyJuliaPanel.add(this.diyJApplyFormulaZCb);
		this.diyJApplyFormulaTf.setVisible(false);
		this.diyJuliaPanel.add(this.diyJApplyFormulaTf);
		this.diyJuliaGenVaryApplyFormulaTxtArea.setVisible(false);
		this.diyJuliaGenVaryApplyFormulaTxtAreaSpane.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenVaryApplyFormulaTxtAreaSpane);		
		
		this.diyJuliaGenBu.setVisible(false);
		this.diyJuliaGen2FolderBu.setVisible(false);
		this.diyJuliaPanel.add(this.diyJuliaGenBu);
		this.diyJuliaPanel.add(this.diyJuliaGen2FolderBu);
	}

	private void createDiyMandelbrotPanel() {
		this.colorChoiceCombo.setVisible(true);
		this.showAllColorsCb.setVisible(true);
		
		this.constFuncCombo.setVisible(true);
		
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);

		this.pxFuncCombo.setVisible(true);
		
		this.diyMandPanel.add(new JLabel("Magnification:"));
		this.diyMandPanel.add(this.diyMandMagCombos);
		this.diyMandPanel.add(new JLabel("Exponent:"));
		this.diyMandPanel.add(this.diyMandExpCombos);
		this.diyMandPanel.add(this.diyMandUseDiffCb);
		this.diyMandPanel.add(this.diyMandIsBuddhaCb);
		this.diyMandPanel.add(this.diyMandExploreCb);

//		this.diyMandPanel.add(this.diyMandUseLyapunovExpCb);
		
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
		this.diyMandPanel.add(new JLabel("Max Iterations: "));
		this.diyMandPanel.add(this.diyMandMaxIterCombos);

		this.diyMandPanel.add(new JLabel("Area: "));
		this.diyMandPanel.add(this.diyMandAreaSizeCombos);
		
		this.diyMandPanel.add(new JLabel("Boundary: "));
		this.diyMandPanel.add(this.diyMandBoundCombos);
		
		this.diyMandPanel.add(new JLabel("Center: X "));
		this.diyMandPanel.add(this.diyMandXCCombos);
		this.diyMandPanel.add(new JLabel(" Y "));
		this.diyMandPanel.add(this.diyMandYCCombos);
		this.diyMandPanel.add(new JLabel("ScaleSize:"));
		this.diyMandPanel.add(this.diyMandScaleSizeCombos);
		
		this.setupDiyMandGeneratorPanel();
		
		this.diyMandPanel.setVisible(false);
	}

	private void setupDiyMandGeneratorPanel() {
		this.diyMandPanel.add(this.diyMandGenCb);
		
		this.diyMandVaryColorCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryColorCb);
		this.diyMandVaryPixXTranCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryPixXTranCb);
		this.diyMandVaryPixYTranCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryPixYTranCb);
		this.diyMandVaryIntraPixXYCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryIntraPixXYCb);
		
		this.diyMandVaryPixelZFuncCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryPixelZFuncCb);

		this.diyMandVaryPixelPowerZCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryPixelPowerZCb);

		this.diyMandGenPixelPowerZFromLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenPixelPowerZFromLabel);
		this.diyMandGenPixelPowerZFromTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenPixelPowerZFromTf);
		this.diyMandGenPixelPowerZToLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenPixelPowerZToLabel);
		this.diyMandGenPixelPowerZToTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenPixelPowerZToTf);
		this.diyMandGenPixelPowerZJumpLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenPixelPowerZJumpLabel);
		this.diyMandGenPixelPowerZJumpCombo.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenPixelPowerZJumpCombo);

		this.diyMandVaryConstCFuncCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryConstCFuncCb);
		this.diyMandVaryPixelConstOpZCCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryPixelConstOpZCCb);
		
		this.diyMandVaryGenConstantCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryGenConstantCb);		
		
		this.diyMandGenRealFromLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenRealFromLabel);
		this.diyMandGenRealFromTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenRealFromTf);
		this.diyMandGenRealToLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenRealToLabel);
		this.diyMandGenRealToTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenRealToTf);
		
		this.diyMandGenRealJumpLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenRealJumpLabel);
		this.diyMandGenRealJumpCombo.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenRealJumpCombo);
		

		this.diyMandGenImagFromLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenImagFromLabel);
		this.diyMandGenImagFromTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenImagFromTf);
		this.diyMandGenImagToLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenImagToLabel);
		this.diyMandGenImagToTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenImagToTf);
		
		this.diyMandGenImagJumpLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenImagJumpLabel);
		this.diyMandGenImagJumpCombo.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenImagJumpCombo);	

		this.diyMandVaryIterCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryIterCb);
		
		this.diyMandVaryBoundaryCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryBoundaryCb);
		this.diyMandGenBoundaryFromLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenBoundaryFromLabel);
		this.diyMandGenBoundaryFromTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenBoundaryFromTf);
		this.diyMandGenBoundaryToLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenBoundaryToLabel);
		this.diyMandGenBoundaryToTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenBoundaryToTf);
		this.diyMandGenBoundaryJumpLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenBoundaryJumpLabel);	
		this.diyMandGenBoundaryJumpCombo.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenBoundaryJumpCombo);
		

		this.diyMandVaryPixXCentrCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryPixXCentrCb);

		this.diyMandVaryPixYCentrCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryPixYCentrCb);

		this.diyMandVaryScaleSizeCb.setVisible(false);
		this.diyMandPanel.add(this.diyMandVaryScaleSizeCb);
		
		this.diyMandGenScaleSizeFromLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenScaleSizeFromLabel);
		this.diyMandGenScaleSizeFromTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenScaleSizeFromTf);
		this.diyMandGenScaleSizeToLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenScaleSizeToLabel);
		this.diyMandGenScaleSizeToTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenScaleSizeToTf);
		this.diyMandGenScaleSizeJumpLabel.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenScaleSizeJumpLabel);	
		this.diyMandGenScaleSizeJumpCombo.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenScaleSizeJumpCombo);
		
		this.diyMandApplyFormulaZCb.setVisible(true);
		this.diyMandPanel.add(this.diyMandApplyFormulaZCb);
		this.diyMandApplyFormulaTf.setVisible(false);
		this.diyMandPanel.add(this.diyMandApplyFormulaTf);
		this.diyMandGenVaryApplyFormulaTxtArea.setVisible(false);
		this.diyMandGenVaryApplyFormulaTxtAreaSpane.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenVaryApplyFormulaTxtAreaSpane);	
		
		this.diyMandGenBu.setVisible(false);
		this.diyMandGen2FolderBu.setVisible(false);
		this.diyMandPanel.add(this.diyMandGenBu);
		this.diyMandPanel.add(this.diyMandGen2FolderBu);
	}

	private void createMandelbrotPanel() {
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.colorChoiceCombo.setVisible(true);
		this.showAllColorsCb.setVisible(true);

		this.constFuncCombo.setVisible(true);

		this.pxFuncCombo.setVisible(true);
		
		this.mandOptionsPanel.add(new JLabel("Magnification(M):"));
		this.mandOptionsPanel.add(this.mandCombos);
		this.mandOptionsPanel.add(new JLabel("Exponent(X):"));
		this.mandOptionsPanel.add(this.mandExpCombos);		
		this.mandOptionsPanel.add(this.mandUseDiffCb);
		this.mandOptionsPanel.add(this.mandIsBuddhaCb);
		this.mandOptionsPanel.add(this.mandIsMotionbrotCb);
		this.mandOptionsPanel.add(this.mandExploreCb);
		
		this.mandOptionsPanel.add(this.mandMotionParamLabel);
		this.mandMotionParamLabel.setVisible(false);
		
		this.mandOptionsPanel.add(this.mandMotionParamCombo);
		this.mandMotionParamCombo.setVisible(false);
		
		this.mandOptionsPanel.add(this.mandMotionParamJumpLabel);
		this.mandMotionParamJumpLabel.setVisible(false);
		
		this.mandOptionsPanel.add(this.mandMotionParamJumpCombo);
		this.mandMotionParamJumpCombo.setVisible(false);
		
//		this.mandOptionsPanel.add(this.mandUseLyapunovExpCb);
		
		
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
		this.colorChoiceCombo.setVisible(true);
		this.showAllColorsCb.setVisible(true);
		
		this.constFuncCombo.setVisible(true);

		this.pxFuncCombo.setVisible(true);
		
		this.juliaOptionsPanel.add(new JLabel("Power-Constant:"));
		this.juliaOptionsPanel.add(this.juliaCombos);		
		this.juliaOptionsPanel.add(this.juliaUseDiffCb);
		
		this.juliaOptionsPanel.add(this.juliaExploreCb);

//		this.juliaOptionsPanel.add(this.juliaUseLyapunovExpCb);
		
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
		this.colorChoiceCombo.setVisible(false);
		this.showAllColorsCb.setVisible(false);

		this.apolloOptionsPanel.add(new JLabel("CurvatureOptions:"));
		this.apolloOptionsPanel.add(this.curvCombos);
		
		this.apolloOptionsPanel.add(this.apolloTriangleCb);
		this.apolloOptionsPanel.add(this.apolloTriangleUseCentroidCb);

		this.apolloOptionsPanel.setVisible(false);
		this.add(this.apolloOptionsPanel);
	}

	private void createFannyPanel() {
		this.rotLabel.setVisible(false);
		if (this.rotateCombo!=null) {
			this.rotateCombo.setVisible(false);
		}
		this.colorChoiceCombo.setVisible(false);
		this.showAllColorsCb.setVisible(false);

		this.fannyOptionsPanel.add(new JLabel("Dimension Size:"));
		this.fannyOptionsPanel.add(this.sideCombos);

		this.fannyOptionsPanel.add(new JLabel("Ratio:"));
		this.fannyOptionsPanel.add(this.ratioCombos);
		
		this.fannyOptionsPanel.setVisible(false);
		this.add(this.fannyOptionsPanel);
	}
	
	private void createKochSnowFlakePanel() {
		this.colorChoiceCombo.setVisible(false);
		this.showAllColorsCb.setVisible(false);
		
		this.kochSnowFlakePanel.add(this.kochFillExternalCb);
		this.kochSnowFlakePanel.add(this.kochMixColorsCb);
		this.kochSnowFlakePanel.add(this.kochSpreadOuterCb);
		
		this.kochSnowFlakePanel.setVisible(false);
		this.add(this.kochSnowFlakePanel);
	}
	
	private void createSierpinskiTPanel() {
		this.colorChoiceCombo.setVisible(false);
		this.showAllColorsCb.setVisible(false);
		
		this.sierpinskiTPanel.add(this.sierpinskiTFillInnerCb);
		
		this.sierpinskiTPanel.add(this.sierpinskiCreateGasketCb);
		
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
	
	private int doCloseAttractorsFrame(JFrame aFrame) {
		this.setEnabled(true);
		this.buSave.setEnabled(true);
		this.buSave2File.setEnabled(true);
		aFrame.dispose();
		/*aFrame = null;*/
		return JFrame.DISPOSE_ON_CLOSE;
	}
	
	//	switching to frame
	private /*JFrame */void createAttractorsDisplayFr() {
		this.setEnabled(false);
		
		SwingUtilities.invokeLater(new Runnable() {
		      @Override
		      public void run() {
		        final JFrame frame = new JFrame();
		        frame.setTitle("BaWaZ Attractorz: ");
		        frame.setSize(900, 800);
		        
		        frame.setDefaultCloseOperation(doCloseAttractorsFrame(frame));
		        /*frame.setLayout(new GridLayout(25, 0));*/
		        frame.add(createAttractorsPanel());
		        frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2,
						(Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2);
				
		        frame.setResizable(true);
		        frame.setVisible(true);
		      }
		});
		/*
		this.attractorsFrame = new JFrame();
		this.attractorsFrame.setSize(700,700);
		this.attractorsFrame.setVisible(true);
		this.attractorsFrame.setLayout(new GridLayout(25, 0));
		this.attractorsFrame.setDefaultCloseOperation(this.doCloseAttractorsFrame(this.attractorsFrame));
		
		this.attractorsFrame.add(this.createAttractorsPanel());
*/
		return /*this.attractorsFrame*/;
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
			/*this.attractorsPanel.setVisible(false);*/
			this.formulaArea.setVisible(false);

			if (this.curvChoices == null || this.mult == 0) {
				this.buStart.setEnabled(false);
				this.buSavePxStart.setEnabled(false);
			} else {
				this.buStart.setEnabled(true);
				this.buSavePxStart.setEnabled(true);
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
			/*this.attractorsPanel.setVisible(false);*/
			this.formulaArea.setVisible(false);
			if (this.sideChoice == 0 || this.ratioChoice == 0) {
				this.buStart.setEnabled(false);
				this.buSavePxStart.setEnabled(false);
			} else {
				this.buStart.setEnabled(true);
				this.buSavePxStart.setEnabled(true);
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
			/*this.attractorsPanel.setVisible(false);*/
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
			if (this.power == 0 && (this.compConst == 0.0 || this.complex == null)) {
				this.buStart.setEnabled(false);
				this.buSavePxStart.setEnabled(false);
			} else {
				this.buStart.setEnabled(true);
				this.buSavePxStart.setEnabled(true);
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
			/*this.attractorsPanel.setVisible(false);*/
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
			/*this.attractorsPanel.setVisible(false);*/
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
		}  else if (this.comboChoice.equals(ATTRACTORS)) {
			this.rotLabel.setVisible(true);
			this.rotateCombo.setVisible(true);
			this.fannyOptionsPanel.setVisible(false);
			this.sierpinskiTPanel.setVisible(false);
			this.kochSnowFlakePanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.polyOptionsPanel.setVisible(false);
			
			/*this.attractorsFrame =*/ this.createAttractorsDisplayFr();
			/*this.attractorsPanel.setVisible(true);*/
			this.buStart.setEnabled(true);
			this.buSavePxStart.setEnabled(true);
			
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
			/*this.attractorsPanel.setVisible(false);*/
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
			/*this.attractorsPanel.setVisible(false);*/
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
			/*this.attractorsPanel.setVisible(false);*/
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
			/*this.attractorsPanel.setVisible(false);*/
			this.formulaArea.setVisible(false);
			this.buStart.setEnabled(true);
		}
	}

	private void doSelectSideCombosCommand(Integer sideOption) {
		this.sideChoice = sideOption;
		if (this.ratioChoice != 0) {
			this.buStart.setEnabled(true);
			this.buSavePxStart.setEnabled(true);
		}
	}

	private void doSelectRatioCombosCommand(Integer ratioOption) {
		this.ratioChoice = ratioOption;
		if (this.sideChoice != 0) {
			this.buStart.setEnabled(true);
			this.buSavePxStart.setEnabled(true);
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
		this.buSavePxStart.setEnabled(true);
	}	
	
	private void doJuliaChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.power != 0 && (this.compConst != 0.0 || this.complex!=null)) {
			this.buStart.setEnabled(true);
			this.buSavePxStart.setEnabled(true);
			
			addJuliaFormulaInfo();
			
			addJuliaUseDiffInfo();
		} else {
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
		}
	}

	private void addJuliaFormulaInfo() {
		if (! (this.juliaSelection.equals("J7") || this.juliaSelection.equals("J8")
				|| this.juliaSelection.equals("J9")|| this.juliaSelection.equals("J10")|| this.juliaSelection.equals("J11")) )
		{
			this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z <sup>" + this.power + "</sup> + " + this.compConst+"<br/>");
			
		} else {
			if(this.juliaSelection.equals("J7")){
				this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z <sup>" + this.power + "</sup> + (-0.74543 + 0.11301 * i)<br/>");
			}else if(this.juliaSelection.equals("J8")){
				this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z <sup>" + this.power + "</sup> + (-0.75 + 0.11 * i)<br/>");
			}else if(this.juliaSelection.equals("J9")){
				this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z <sup>" + this.power + "</sup> + (-0.1 + 0.651 * i)<br/>");
			}else if(this.juliaSelection.equals("J10")){
				this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z <sup>" + this.power + "</sup> + ( pi/2 * (1.0 + 0.6 * i) )<br/>");
			}else if(this.juliaSelection.equals("J11")){
				this.formulaArea.setText("<font color='blue'>Julia Set:<br/><br/>f(z) = z <sup>" + this.power + "</sup> + ( pi/2 * (1.0 + 0.4 * i) )<br/>");
			}
		}

		if (this.juliaUseLyapunovExponent) {
			this.addUseLyapunovInfo();
		}
		
		this.addXtrYtrXYInfroInfo(this.pixXTransform,this.pixYTransform,this.pixIntraXYOperation);
		
		this.addInvertPixelCalcInfo();

		this.addPixelConstantOperationInfo(this.pxConstOprnChoice);
		
		this.addPixelFuncInfo(this.pxFuncChoice);
		
		if (this.applyFatou) {
			this.formulaArea.setText(
					"<br/><font color='green'>Fatou Field Lines:<br/>f(z) = (1 - (z<sup>3</sup>/6)) / ((z - (z<sup>2</sup>/2)) <sup>2</sup>)");
		} else if (this.applyZSq) {
			this.formulaArea.append("<br/><font color='green'>ZSquared Field Lines:<br/><br/>f(z) = (z <sup>2</sup>)");
		} else if (this.applyClassicJulia) {
			this.formulaArea.append(
					"<br/><font color='green'>Classic Julia:<br/><br/>f(z) = z<sup>4</sup> + ((z<sup>3</sup>)/(z-1)) + ((z<sup>2</sup>)/(z<sup>3</sup> + 4*z<sup>2</sup> + 5))");
		}
		
		switch(this.pxConstOprnChoice) {
			case	"Plus"	:	
				this.formulaArea.append(" + C</font><br/>");
				break;
			case	"Minus"	:	
				this.formulaArea.append(" - C</font><br/>");
				break;
			case	"Multiply"	:	
				this.formulaArea.append(" * C</font><br/>");
				break;
			case	"Divide"	:	
				this.formulaArea.append(" / C</font><br/>");
				break;
			default:
				this.formulaArea.append(" + C</font><br/>");
				break;			
		
		}
	}

	
	
	private void addDiyJuliaUseDiffInfo() {
		if (this.diyJuliaUseDiff
				|| (this.diyJuliaUseDiffCb.isSelected() && !(this.diyJuliaGenCb.isSelected() || this.diyJuliaGen))) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>"+eol);
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>"+eol);
		}
	}
	
	private void addDiyMandelbrotUseDiffInfo() {
		if (this.diyMandUseDiff
				|| (this.diyMandUseDiffCb.isSelected() && !(this.diyMandGenCb.isSelected() || this.diyMandGen))) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>"+eol);
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>"+eol);
		}
	}

	private void addJuliaUseDiffInfo() {
		if (this.jUseDiff || this.juliaUseDiffCb.isSelected()) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>"+eol);
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>"+eol);
		}
	}
	
	private void addMandelbrotUseDiffInfo() {
		if (this.mUseDiff || this.mandUseDiffCb.isSelected()) {
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>"+eol);
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>"+eol);
		}
	}
	
	private void addPolyUseDiffInfo() {
		if (this.polyUseDiff
				|| (this.polyUseDiffCb.isSelected() && !(this.polyGenCb.isSelected() || this.polyGen))) {			
			this.formulaArea
					.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin</font>"+eol);
		} else {
			this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin</font>"+eol);
		}
	}
	
	private void addMandelbrotConstInfo(FractalBase fBase) {
		Mandelbrot m = (Mandelbrot) fBase;
		ComplexNumber c = null;
		if ( !m.isComplexNumConst() ) {
			c = m.getComplex();
		}
		String func2Apply = m.getUseFuncConst();
		addFuncTypeConstInfo(c, func2Apply);
	}
	
	
	private void addPolyConstInfo(FractalBase fBase) {
		PolyFract p = (PolyFract) fBase;
		ComplexNumber c = null;
		if (!p.isComplexNumConst()) {
			c = p.getCompConst();
		}
		String func2Apply = p.getUseFuncConst();
		addFuncTypeConstInfo(c, func2Apply);
	}

	private void addJuliaConstInfo(Julia j) {
		ComplexNumber c = null;
		if (!j.isComplexNumConst()) {
			c = j.getComplex();
		}
		String func2Apply = j.getUseFuncConst();
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
				this.formulaArea.append("<br/>ComplexConstant == " + c.toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = z");
			}
			break;
		case	"sine":
			if (!isComplexConst) {
				//			System.out.println("SINE-<br/>ComplexConstant == "+c.sine().toString()+"<br/>");
				this.formulaArea.append("<br/>sin(ComplexConstant) == " + c.sine().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = sin(z)");
			}
			break;
		case	"cosine":
			if (!isComplexConst) {
				//			System.out.println("COSINE-<br/>ComplexConstant == "+c.cosine().toString()+"<br/>");
				this.formulaArea.append("<br/>cos(ComplexConstant) == " + c.cosine().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = cos(z)");
			}
			break;
		case	"tan":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>tan(ComplexConstant) == " + c.tan().toString() + eol);
				
			} else {
				this.formulaArea.append("<br/>Complex Constant = tan(z)");
			}
			break;

		case	"cosec":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>cosec(ComplexConstant) == " + c.cosec().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = cosec(z)");
			}
			break;
		case	"sec":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>sec(ComplexConstant) == " + c.sec().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = sec(z)");
			}
			break;
		case	"cot":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>cot(ComplexConstant) == " + c.cot().toString() + eol);
				
			} else {
				this.formulaArea.append("<br/>Complex Constant = cot(z)");
			}
			break;
		case	"sinh":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>sinh(ComplexConstant) == " + c.sinh().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = sin(z)");
			}
			break;
		case	"cosh":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>cosh(ComplexConstant) == " + c.cosh().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = cosh(z)");
			}
			break;
		case	"tanh":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>tanh(ComplexConstant) == " + c.tanh().toString() + eol);
				
			} else {
				this.formulaArea.append("<br/>Complex Constant = tanh(z)");
			}
			break;
		case	"arcsine":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>asin2(ComplexConstant) == " + c.asin().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = arcsin(z)");
			}
			break;
		case	"arccosine":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>acos2(ComplexConstant) == " + c.acos().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = arccos(z)");
			}
			break;
		case	"arctan":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>atan(ComplexConstant) == " + c.atan().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = arctan(z)");
			}
			break;
		case	"arcsinh":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>arcsinh(ComplexConstant) == " + c.asinh().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = arcsinh(z)");
			}
			break;
		case	"arccosh":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>arccosh(ComplexConstant) == " + c.acosh().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = arccosh(z)");
			}
			break;
		case	"arctanh":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>atanh(ComplexConstant) == " + c.atanh().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = atanh(z)");
			}
			break;
			
			
			case	"reciprocal":
				if (!isComplexConst) {
					this.formulaArea.append("<br/>(1 / ComplexConstant) == " + c.reciprocal().toString() + eol);
				} else {
					this.formulaArea.append("<br/>Complex Constant = (1/z)");
				}
				break;
			case	"reciprocalSquare":
				if (!isComplexConst) {
					this.formulaArea.append("<br/>(1 / ComplexConstant) <sup>2</sup> == " + (c.reciprocal()).power(2).toString() + eol);
				} else {
					this.formulaArea.append("<br/>Complex Constant = (1 / ComplexConstant) <sup>2</sup>");
				}
				break;
		case	"square":
			if (!isComplexConst) {
				//			System.out.println("SQUARE-<br/>ComplexConstant == "+c.power(2).toString()+"<br/>");
				this.formulaArea.append("<br/>(ComplexConstant)squared or C <sup>2</sup> == " + c.power(2).toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = ( z <sup>2</sup> )");
			}
			break;
		case	"cube":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>(ComplexConstant)cubed or C <sup>3</sup> == " + c.power(3).toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = ( z <sup>3</sup> )");
			}
			break;
		case	"exponent(e)":
			if (!isComplexConst) {
				this.formulaArea.append("<br/>exponent(ComplexConstant) or e <sup>C</sup> == " + c.exp().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = ( e <sup>z</sup> )");
			}
			break;
		case	"root":
			if (!isComplexConst) {
				//			System.out.println("ROOT-<br/>ComplexConstant == "+c.sqroot().toString()+"<br/>");
				this.formulaArea.append("<br/>sqrt(ComplexConstant) == " + c.sqroot().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant =  sqrt( z )");
			}
			break;
		case	"log(e)":
			if (!isComplexConst) {
				//			System.out.println("LOG--<br/>ComplexConstant == "+c.ln().toString()+"<br/>");
				this.formulaArea.append("<br/>log(ComplexConstant) [base e] == " + c.ln().toString() + eol);
			} else {
				this.formulaArea.append("<br/>Complex Constant = ln( z )");
			}
			break;

		default:
			if (!isComplexConst) {
				this.formulaArea.append("<br/>ComplexConstant == " + c.toString() + eol);
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
	
	private void doSetSierpinskiCreateGasketCommand(boolean createGasket) {
		this.sierpinskiCreateGasket = createGasket;
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
	
	/*private void doSetPolyUseLyapunovExpCommand(boolean useLyapunovExpC) {
		this.polyUseLyapunovExponent = useLyapunovExpC;
		this.formulaArea.setVisible(true);
		this.doPolyChoicesCheck();
	}*/
	


	private void doSetDiyJuliaExploreCommand(boolean explore) {
		this.diyJuliaExplore = explore;
	}

	private void doSetDiyMandelbrotExploreCommand(boolean explore) {
		this.diyMandExplore = explore;
	}
	
	private void doSetDiyMandUseLyapunovExpCommand(boolean useLyapunovExpC) {
		this.diyMandUseLyapunovExponent = useLyapunovExpC;
		this.formulaArea.setVisible(true);
		this.doDiyMandelbrotChoicesCheck();
	}
	

	

	private void doSetJuliaExploreCommand(boolean explore) {
		this.juliaExplore = explore;
	}
	

	private void doSetMandelbrotExploreCommand(boolean explore) {
		this.mandExplore = explore;
	}
	

	private void doSetMandUseLyapunovExpCommand(boolean useLyapunovExpC) {
		this.mandUseLyapunovExponent = useLyapunovExpC;
		this.formulaArea.setVisible(true);
		this.doMandelbrotChoicesCheck();
	}
	
	private void doSetDiyJuliaUseLyapunovExpCommand(boolean useLyapunovExpC) {
		this.diyJuliaUseLyapunovExponent = useLyapunovExpC;
		this.formulaArea.setVisible(true);
		this.doDiyJuliaChoicesCheck();
	}
	
	private void doSetJuliaUseLyapunovExpCommand(boolean useLyapunovExpC) {
		this.juliaUseLyapunovExponent = useLyapunovExpC;
		this.formulaArea.setVisible(true);
		this.doJuliaChoicesCheck();
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
			case J10: //J10 = "P[2] CPI1R06i";//[pi/2*(1.0 + 0.6i)]";	//f(z) = z^2 + ...
				this.power = 2;				this.compConst=0.0;
				this.complex = "M1";
				this.juliaSelection="J10";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;
			case J11: //  J9 = "P[2] C3";//[-0.1+0.651*i]";
				this.power = 2;				this.compConst=0.0;
				this.complex = "M2";
				this.juliaSelection="J11";
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
		this.buSavePxStart.setEnabled(true);
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
			this.buSavePxStart.setEnabled(true);
			this.formulaArea.setText("Mandelbrot Set:<br/><br/>f(z) = z <sup>" + this.exponent + "</sup> + C");
			if (this.mUseDiff || this.mandUseDiffCb.isSelected()) {
				this.formulaArea
						.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin");
			} else {
				this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin");
			}
		} else {
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
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
			this.buSavePxStart.setEnabled(true);
			String formula = "Mandelbrot Set:<br/><br/>f(z) = z <sup>" + this.diyMandExponent + "</sup> + C";
			if (!this.keepConst) {
				try {
					formula += "<br/>  C = " + Double.parseDouble(this.diyMandRealTf.getText()) + " + ("
							+ Double.parseDouble(this.diyMandImgTf.getText()) + ")";
				} catch (NumberFormatException  | NullPointerException e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if (this.mUseDiff || this.mandUseDiffCb.isSelected()) {
				formula += "<br/><br/>Calculated inverted colors based on differences in pixel values from origin";
			} else {
				formula += "<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin";
			}
			this.formulaArea.setText(formula);
		} else {
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
		}
	}
	
	private void doDiyJuliaChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.diyJuliaPower != 0
				&& (!this.keepConst && (this.diyJuliaRealTf.getText().length() > 0)
						&& (this.diyJuliaImgTf.getText().length() > 0) && !this.keepConst)
				|| this.keepConst) {
			this.buStart.setEnabled(true);
			this.buSavePxStart.setEnabled(true);
			String formula = "Julia Set:<br/><br/>f(z) = z <sup>" + this.diyJuliaPower + "</sup> + C";
			if (!this.keepConst) {
				try {
					formula += "<br/>  C = " + Double.parseDouble(this.diyJuliaRealTf.getText()) + " + ("
							+ Double.parseDouble(this.diyJuliaImgTf.getText()) + ")";
				} catch (NumberFormatException  | NullPointerException e2) {
					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
			if (this.diyJuliaUseDiff || this.diyJuliaUseDiffCb.isSelected()) {
				formula += "<br/><br/>Calculated inverted colors based on differences in pixel values from origin";
			} else {
				formula += "<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin";
			}

			this.formulaArea.setText(formula);
		} else {
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
		}
	}
	
	private void doDiyApolloChoicesCheck() {
		if (this.diyApolloC1 != 0 && this.diyApolloC2 != 0 && this.diyApolloC3 != 0 && this.diyApolloMult != 0) {
			this.buStart.setEnabled(true);
			this.buSavePxStart.setEnabled(true);
		} else {
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
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
	
	private void doSetMandIsBuddhaCommand(boolean isB) {
		this.mandIsBuddhabrot = isB;
		this.doMandelbrotChoicesCheck();
	}
	

	private void doSetMandIsMotionCommand(boolean isM) {
		this.mandIsMotionbrot = isM;
		this.doMandelbrotChoicesCheck();
	}


	private void doSelectMandMotionParamCombosCommand(String motParam) {
		this.mandMotionParam = motParam;
	}


	private void doSelectMandMotionParamJumpCombosCommand(Double motParamJumpVal) {
		this.mandMotionParamJumpVal = motParamJumpVal;
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
			this.buSavePxStart.setEnabled(true);
			this.formulaArea.setText("Poynomial Set:<br/><br/>nf(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) + C"+
					"<br/>  x = Row + 0 * i , y = 0 + Column * i");
			if (this.polyUseDiff || this.polyUseDiffCb.isSelected()) {
				this.formulaArea
						.append("<br/><br/>Calculated inverted colors based on differences in pixel values from origin");
			} else {
				this.formulaArea.append("<br/><br/>Calculated colors based on pixel values with a 'top-and-left' origin");
			}
		} else {
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
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
	
	
	private void doSetDiyMandIsBuddhaCommand(boolean isB) {
		this.diyMandIsBuddhabrot = isB;
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
	///////////////////////////////////generator-methods///////////////////
	//////////////	generator	Mandelbrot
	private void doSelectDiyMandVaryColorCommand(boolean varyColor) {
		this.diyMandVaryColor = varyColor;
	}	

	private void doSelectDiyMandVaryIntraPixXYCommand(boolean varyaryIntraPixXY) {
		this.diyMandVaryIntraPixXY = varyaryIntraPixXY;
	}
	
	private void doSelectDiyMandVaryPixYTranCommand(boolean varyPixYTran) {
		this.diyMandVaryPixYTran = varyPixYTran;
	}	

	private void doSelectDiyMandVaryPixXTranCommand(boolean varyPixXTran) {
		this.diyMandVaryPixXTran = varyPixXTran;
	}	

	private void doSelectDiyMandVaryPixelZFuncCommand(boolean varyPixelZFunc) {
		this.diyMandVaryPixelZFunc = varyPixelZFunc;
	}

	private void doSelectDiyMandVaryConstCFuncCommand(boolean varyConstCFunc) {
		this.diyMandVaryConstCFunc = varyConstCFunc;
	}

	private void doSelectDiyMandVaryPixelConstOpZCCommand(boolean varyPixelConstOpZC) {
		this.diyMandVaryPixelConstOpZC = varyPixelConstOpZC;
	}

	private void doSelectDiyMandVaryGenConstantCommand(boolean varyGenConstant) {
		this.diyMandVaryConstant = varyGenConstant;
	}

	private void doSelectDiyMandVaryPixelPowerZCommand(boolean varyPixelPowerZ) {
		this.diyMandVaryPixelPowerZ = varyPixelPowerZ;
	}
	
	private void doSelectDiyMandVaryIterCommand(boolean varyIter) {
		this.diyMandVaryIter = varyIter;
	}
	
	private void doSelectDiyMandVaryBoundaryCommand(boolean varyBoundary) {
		this.diyMandVaryBoundary = varyBoundary;
	}
	
	private void doSelectDiyMandVaryPixXCentrCommand(boolean varyPixXCentr) {
		this.diyMandVaryPixXCentr = varyPixXCentr;
	}
	private void doSelectDiyMandVaryPixYCentrCommand(boolean varyPixYCentr) {
		this.diyMandVaryPixYCentr = varyPixYCentr;
	}
	
	private void doSelectDiyMandVaryScaleSizeCommand(boolean varyScaleSize) {
		this.diyMandVaryScaleSize = varyScaleSize;
	}
	
	private void doSelectDiyMandVaryPixelPowerZCombosCommand(Integer varyPixelPowerZJumpVal) {
		this.diyMandVaryPixelPowerZJumpVal = varyPixelPowerZJumpVal;
	}
	
	private void doSelectDiyMandVaryBoundaryJumpCombosCommand(Double varyBoundaryJumpVal) {
		this.diyMandVaryBoundaryJumpVal = varyBoundaryJumpVal;
	}
	private void doSelectDiyMandVaryScaleSizeCombosCommand(Double varyScaleSizeJumpVal) {
		this.diyMandVaryScaleSizeJumpVal = varyScaleSizeJumpVal;
	}
	
	private void doSelectDiyMandApplyFormulaCommand(boolean apply) {
		this.diyMandApplyFormulaZ = apply;
	}

	
	private void doSetDiyMandGenCommand(boolean gen) {
		this.diyMandGen = gen;
		if (gen) {
			this.keepConst = false;
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
		}
		this.formulaArea.setVisible(true);
		this.doMandelbrotChoicesCheck();
	}
	
	private void doSelectDiyMandGenRealJumpCombosCommand(Double realJump) {
		this.diyMandGenRealJumpVal = realJump;
	}
	private void doSelectDiyMandGenImagJumpCombosCommand(Double imagJump) {
		this.diyMandGenImagJumpVal = imagJump;
	}
	
	private void doMandelbrotGenerateCommand() {
		
		if (this.diyMandVaryConstant && this.diyMandVaryGenConstantCb.isSelected()) {
			System.out.println("in_doMandelbrotGenerateCommand_this.diyMandVaryConstant=true");
			try {
				this.diyMandGenRealFromVal = Double.parseDouble(this.diyMandGenRealFromTf.getText());
				this.diyMandGenImagFromVal = Double.parseDouble(this.diyMandGenImagFromTf.getText());
				this.diyMandGenRealToVal = Double.parseDouble(this.diyMandGenRealToTf.getText());
				this.diyMandGenImagToVal = Double.parseDouble(this.diyMandGenImagToTf.getText());
				
				if(this.diyMandGenRealFromVal>this.diyMandGenRealToVal||this.diyMandGenImagFromVal>this.diyMandGenImagToVal){
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for Const-Real",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number \n"+e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
		}
		
		boolean varyConst = this.diyMandVaryConstant && this.diyMandVaryGenConstantCb.isSelected();
		boolean varyKeepConst = (this.keepConst && this.diyMandelbrotKeepConstRb.isSelected()) && !varyConst;
		
		/*if (	!(varyConst && varyKeepConst) && 
				!(this.diyMandVaryConstant && this.diyMandVaryGenConstantCb.isSelected())) {*/
			if (!(this.keepConst && this.diyMandelbrotKeepConstRb.isSelected())
					&& !(this.diyMandVaryConstant && this.diyMandVaryGenConstantCb.isSelected())) {			
			try {
				this.diyMandConstReal = Double.parseDouble(this.diyMandRealTf.getText());
				this.diyMandConstImg = Double.parseDouble(this.diyMandImgTf.getText());
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		if (this.diyMandVaryBoundary) {
			System.out.println("in_doMandGenerateCommand_this.diyMandVaryBoundary=true");
			try {
				this.diyMandVaryBoundaryFromVal = Double.parseDouble(this.diyMandGenBoundaryFromTf.getText());
				this.diyMandVaryBoundaryToVal = Double.parseDouble(this.diyMandGenBoundaryToTf.getText());
				
				if (this.diyMandVaryBoundaryFromVal > this.diyMandVaryBoundaryToVal) {
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for Boundary",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number for Boundary\n"+e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
		}
		
		if (this.diyMandVaryScaleSize) {
			System.out.println("in_doMandGenerateCommand_this.diyMandVaryScaleSize=true");
			try {
				this.diyMandVaryScaleSizeFromVal = Double.parseDouble(this.diyMandGenScaleSizeFromTf.getText());
				this.diyMandVaryScaleSizeToVal = Double.parseDouble(this.diyMandGenScaleSizeToTf.getText());
				if(this.diyMandVaryScaleSizeFromVal>this.diyMandVaryScaleSizeToVal){
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for ScaleSize",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number for ScaleSize\n"+e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
		}

		if (this.diyMandVaryPixelPowerZ) {
			System.out.println("in_doMandGenerateCommand_this.diyMandVaryPixelPowerZ=true");
			try {
				this.diyMandVaryPixelPowerZFromVal = Integer.parseInt(this.diyMandGenPixelPowerZFromTf.getText());
				this.diyMandVaryPixelPowerZToVal = Integer.parseInt(this.diyMandGenPixelPowerZToTf.getText());
				if(this.diyMandVaryPixelPowerZFromVal>this.diyMandVaryPixelPowerZToVal){
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for PixelPowerZ",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Integer for PixelPowerZ\n"+e2.getMessage(), "Error - Not an Integer",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
		}
		
		final int varyColorCount = this.getVaryMandColorCount();
		final List<?> varyColorList = this.getVaryMandColorsList();
		
		final int varyPixXTransCount = this.getVaryMandPixXTransCount();
		final List<?> varyPixXTrList = this.getVaryMandPixXTransList();
		
		final int varyPixYTransCount = this.getVaryMandPixYTransCount();
		final List<?> varyPixYTrList = this.getVaryMandPixYTransList();
		
		final int varyIntraPixXYOpCount = this.getVaryMandIntraPixXYOpCount();
		final List<?> varyIntraPixXYOpList = this.getVaryMandIntraPixXYOpList();		
		
		final int varyZFuncCount = this.getVaryMandZFuncCount();
		final List<?> varyZFuncList = this.getVaryMandZFuncList();
		
		final int varyConstCFuncCount = this.getVaryMandConstCFuncCount();
		final List<?> varyConstCFuncList = this.getVaryMandConstCFuncList();
		
		final int varyPixelConstOpZCCount = this.getVaryMandPixelConstOpZCCount();
		final List<?> varyPixelConstOpZCList = this.getVaryMandPixelConstOpZCList();
		
		final int powerFrom = this.diyMandVaryPixelPowerZFromVal;
		final int powerTo = this.diyMandVaryPixelPowerZToVal;
		final int powerJump = this.diyMandVaryPixelPowerZJumpVal;
		final int varyPowerCount = this.getVaryMandPowerCount();		
		final List<?> varyPowerList = this.getVaryMandPowerList(powerFrom, powerTo, powerJump, varyPowerCount);
		
		final int varyPixXCenterCount = this.getVaryMandPixXCenterCount();
		final List<?> varyPixXCenterList = this.getVaryMandPixXCenterList();
		
		final int varyPixYCenterCount = this.getVaryMandPixYCenterCount();
		final List<?> varyPixYCenterList = this.getVaryMandPixYCenterList();
		
		final int varyIterCount = this.getVaryMandIterCount();
		final List<?> varyIterList = this.getVaryMandIterList();
		
		final int varyUseDiffCount = 2;
		final List<?> varyUseDiffList = this.getAppendStr2List("useDiffChoice=",this.getTrueFalseList());
		
		final int varyInvertPixelCount = 2;
		final List<?> varyInvertPixelList = this.getAppendStr2List("invertPixelCalcChoice=", this.getTrueFalseList());

		final double scaleSizeFrom = this.diyMandVaryScaleSizeFromVal;
		final double scaleSizeTo = this.diyMandVaryScaleSizeToVal;
		final double scaleSizeJump = this.diyMandVaryScaleSizeJumpVal;
		final int varyScaleSizeCount = this.getVaryMandScaleSizeCount();		
		final List<?> varyScaleSizeList = this.getVaryMandScaleSizeList(scaleSizeFrom, scaleSizeTo, scaleSizeJump, varyScaleSizeCount);
		
		
		final double boundaryFrom = this.diyMandVaryBoundaryFromVal;
		final double boundaryTo = this.diyMandVaryBoundaryToVal;
		final double boundaryJump = this.diyMandVaryBoundaryJumpVal;
		final int varyBoundaryCount = this.getVaryMandBoundaryCount();
		final List<?> varyBoundaryList = this.getVaryMandBoundaryList(boundaryFrom, boundaryTo, boundaryJump, varyBoundaryCount);
		
		
		final double jRealFrom = this.diyMandGenRealFromVal;
		final double jImagFrom = this.diyMandGenImagFromVal;
		final double jRealTO = this.diyMandGenRealToVal;
		final double jImagTO = this.diyMandGenImagToVal;		
		final double realJumpVal = this.diyMandGenRealJumpVal;
		final double imagJumpVal = this.diyMandGenImagJumpVal;
		
		final int varyRealConstantCount = this.getVaryMandRealConstantCount();
		final List<?> varyRealConstList = this.getVaryMandRealConstantList(jRealFrom,jRealTO,realJumpVal,varyRealConstantCount);
		
		final int varyImagConstantCount = this.getVaryMandImagConstantCount();
		final List<?> varyImagConstList = this.getVaryMandImagConstantList(jImagFrom,jImagTO,imagJumpVal,varyImagConstantCount);
		
		final List<?> varyConstList = this.getAppendStr2List("constantChoice=", this.product(varyRealConstList,varyImagConstList));

//		System.out.println("getTotalVaryCount=" + this.getTotalVaryCount());
//		System.out.println("getTotalVaryCount=HARDED-2-To5== " + 5);
//		int totalVaryCount = this.getTotalVaryCount();//5;
		
		int totalVaryCount = varyColorCount * varyPixXTransCount * varyPixYTransCount * varyIntraPixXYOpCount * 
				varyZFuncCount * varyConstCFuncCount * varyPixelConstOpZCCount  * 
				varyPowerCount  * varyPixXCenterCount * varyPixYCenterCount  * 
				varyScaleSizeCount * varyBoundaryCount  * varyIterCount * 
				varyUseDiffCount * varyInvertPixelCount *
				varyRealConstantCount * varyImagConstantCount;
				
		System.out.println("getTotalVaryCount=" + totalVaryCount);

		if (totalVaryCount > 150) {
			int ans = JOptionPane.showConfirmDialog(null, "Are You Sure needs to generate\n"+totalVaryCount+"images", "Are_You_Sure",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if(ans!=JOptionPane.YES_OPTION){
				return;
			}
		}
		
		final List<?> allCombinationsList = this.product(
				varyColorList, varyPixXTrList, varyPixYTrList, varyIntraPixXYOpList,
				varyZFuncList, varyConstCFuncList, varyPixelConstOpZCList,
				varyPowerList, varyPixXCenterList, varyPixYCenterList,
				varyScaleSizeList, varyBoundaryList, varyIterList,
				varyUseDiffList, varyInvertPixelList,
				varyConstList);
		
		System.out.println("allCombinationsListSize==totalVaryCount  "+(allCombinationsList.size()==totalVaryCount ));
		
		Properties[] props = new Properties[allCombinationsList.size()];
		props = shuffle(this.getAllCombinationProperties(allCombinationsList));
//		ps = this.getAllCombinationProperties(allCombinationsList);

		
		/*final*/ String subDirName;
		/*if(!varyConst && !varyKeepConst) {
			subDirName = "Mand_{(R)[" + String.format("%.2f", this.diyMandGenRealFromVal) + "_to_"
					+ String.format("%.2f", this.diyMandGenRealToVal) + "],(I)["
					+ String.format("%.2f", this.diyMandGenImagFromVal) + "_to_"
					+ String.format("%.2f", this.diyMandGenImagToVal) + "]" + System.currentTimeMillis() + "}";
		} else {
			subDirName = "Mand_" + System.currentTimeMillis();
		}*/

		subDirName = MANDELBROT + "__Vary[";
		
		if (this.diyMandVaryColor)					subDirName += "Colr,";
		if (this.diyMandVaryPixXTran)				subDirName += "F(x),";
		if (this.diyMandVaryPixYTran)				subDirName += "F(y),";
		if (this.diyMandVaryIntraPixXY)				subDirName += "I(xy),";
		if (this.diyMandVaryPixelZFunc)				subDirName += "F(Z),";
		if (this.diyMandApplyFormulaZ)				subDirName += "CustFormula,";
		if (this.diyMandVaryConstant)				subDirName += "Const,";
		if (this.diyMandVaryConstCFunc)				subDirName += "F(C),";
		if (this.diyMandVaryPixelConstOpZC)			subDirName += "O(ZC),";
		if (this.diyMandVaryPixelPowerZ)			subDirName += "X(Z),";
		if (this.diyMandVaryIter)					subDirName += "M(It),";
		if (this.diyMandVaryPixXCentr)				subDirName += "Cx,";
		if (this.diyMandVaryPixYCentr)				subDirName += "Cy,";
		if (this.diyMandVaryBoundary)				subDirName += "Bd,";
		if (this.diyMandVaryScaleSize)				subDirName += "Sz,";
		if (this.useRangeEstimation)				subDirName += "__RngEstSet";
		
		subDirName = subDirName.substring(0, subDirName.length() - 1) + "]_" + System.currentTimeMillis();
		
		/*File subDir = new File("images_gen\\" + subDirName);
		if (!subDir.exists()) {
			subDir.mkdir();
		}*/
		File subDir = null;
		if (!diyMandGen2Folder && diyMandGen2FolderPath == null) {
			subDir = new File("images_gen\\" + subDirName);

		} else {
			subDir = new File(diyMandGen2FolderPath+"\\"+subDirName);
		}
		
		if (!subDir.exists()) {
			subDir.mkdir();
			//check-again-for-create
			System.out.println("subDir-did-NOTExist.....created_using_mkdir--now-checking");
			System.out.println("subDir_exists?....."+subDir.exists());
		}

		File subDirDetail = new File(subDir, "Detail");
		if (!subDirDetail.exists()) {
			subDirDetail.mkdir();
		}
		
		Mandelbrot[] mands = new Mandelbrot[totalVaryCount];
		boolean hasOutOfMemoryError = false;

		for (int i = 0; i < totalVaryCount; i++) {
			Runtime.getRuntime().gc();
			if (hasOutOfMemoryError) {
				i -= 1;
			}

			try {
				mands[i] = new Mandelbrot(this.correctProperties(props[i]));
				mands[i].setGenerated(true);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage());
				JOptionPane.showMessageDialog(null, "BAaaAD Properties\n"+e1.getMessage(), "One key has NULL value",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			mands[i].setVisible(false); // make invisible

			/**
			 * 
			 * //need-to-set-4-booleans,as-properties-sends-strings-only
			mands[i].setApplyCustomFormula(this.diyMApplyFormulaZ);*/

			/////// reassign 2 UI - 4 formulaArea
			this.constFuncChoice = mands[i].getUseFuncConst();

			// 4_getExtraInfo
			this.diyMandExponent = mands[i].getPower();
			this.diyMandBound = mands[i].getBound();
			this.diyMandMaxIter = mands[i].getMaxIter();
			this.diyMandXC = mands[i].getxC();
			this.diyMandYC = mands[i].getyC();
			this.diyMandScaleSize = mands[i].getScaleSize();

			// 4_img_Base_Info
			this.colorChoice = mands[i].getColorChoice();
			if (this.colorChoice.equals("SampleMix")) {
				/* this.setSampleColorMix(julies[i]); */

				this.setSampleStart_DivVals(mands[i]);
			}

			if (this.colorChoice.equals("ColorBlowout")) {
				this.setColorBlowoutTypeVals(mands[i]);
			}
			
			if (this.colorChoice.equals("ColorSuperBlowout")) {
				this.setColorSuperBlowoutTypeVals(mands[i]);
			}


			String pxFunc = mands[i].getUseFuncPixel();
			this.pxFuncChoice = pxFunc;

			String pixXTr = mands[i].getPxXTransformation();
			this.pixXTransform = pixXTr;
			
			String pixYTr = mands[i].getPxYTransformation();
			this.pixYTransform = pixYTr;
			
			String pixIntraXYOp = mands[i].getPixXYOperation();
			this.pixIntraXYOperation=pixIntraXYOp;
			
			String pixConstOp = mands[i].getPxConstOperation();
			this.pxConstOprnChoice=pixConstOp;
			

			this.setDiyMandUseDiff(mands[i].isUseDiff());
			this.invertPixelCalculation = mands[i].isReversePixelCalculation();
			
			if (!(mands[i].isComplexNumConst() || this.diyMandKeepConst || this.keepConst)
					&&( this.diyMandelbrotKeepConstRb.isSelected() || this.diyMandVaryConstant) ) {
				this.diyMandConstReal = mands[i].getComplex().real;
				this.diyMandConstImg = mands[i].getComplex().imaginary;
			} else {
				this.keepConst = true;
			}
			
			String extraInfo = this.getExtraInfo();
			
			this.setDiyMandGenFormulaArea(pxFunc, this.diyMandExponent,pixXTr, pixYTr, pixIntraXYOp, pixConstOp);
			
			if ( !this.keepConst ) {
				this.formulaArea.append("<br/>Constant = " + this.diyMandConstReal + " + (" + this.diyMandConstImg
						+ " * i)</font>" + eol);
			}/* else {*/
			//
				this.addMandelbrotConstInfo(mands[i]);
			/*}*/
			Runtime.getRuntime().gc();
			/// done1---now-to-imaging

			Graphics2D g = mands[i].getBufferedImage().createGraphics();
//			System
			try {
				
				
				boolean useRngEst = this.useRangeEstimation;
				if (useRngEst) { //
					try {
						double xMinRealVal = Double.parseDouble(this.xMinTf.getText());
						double yMinImgVal = Double.parseDouble(this.yMinTf.getText());
						double xMaxRealVal = Double.parseDouble(this.xMaxTf.getText());
						double yMaxImgVal = Double.parseDouble(this.yMaxTf.getText());

						if (xMinRealVal >= xMaxRealVal || yMinImgVal >= yMaxImgVal) {
							String err = (xMinRealVal < xMaxRealVal) ? "xMin > xMax" : "yMin > yMax";
							JOptionPane.showMessageDialog(null, "Please enter a valid Range value", err, JOptionPane.ERROR_MESSAGE);
							return;
						}

						mands[i].setRefocusDraw(true);
						mands[i].setRangeMinMaxVals(xMinRealVal, yMinImgVal, xMaxRealVal, yMaxImgVal);
						//mands[i].createFocalFractalShape(ff, new ComplexNumber(xMinRealVal,yMinImgVal), new ComplexNumber(xMaxRealVal,yMaxImgVal));
					} catch (NumberFormatException  | NullPointerException e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				mands[i].paint(g);
			} catch (NullPointerException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage() + "+this.pxFuncChoiceIs\n" + this.pxFuncChoice);

				if (varyZFuncCount != 1) {
					JOptionPane.showMessageDialog(null, "BAaaAD Formula - continuing\n"
							+ this.pxFuncChoice, "IncorrectFormula",
							JOptionPane.INFORMATION_MESSAGE);

					this.formulaArea.setText("");

					g.dispose();
					mands[i].dispose();
					mands[i] = null;

					Runtime.getRuntime().gc();

					continue;
				} else {
					JOptionPane.showMessageDialog(null, "BAaaAD Formula\n"
							+ this.pxFuncChoice, "IncorrectFormula",
							JOptionPane.ERROR_MESSAGE);

					return;
				}
			}
			
			this.setFractalBase(mands[i]);

			this.setFractalImage(mands[i].getBufferedImage());

			String imgBaseInfo = this.getImgBaseInfo();
			BufferedImage dataInfoImg = this.createStringImage(imgBaseInfo);

			/*String imageFilePath = "images_gen\\" + subDirName + "\\" + "[" + extraInfo + "]_" + System.currentTimeMillis() + ".png";
			File outputfile = new File(imageFilePath);

			String imageDetailFilePath = "images_gen\\" + subDirName + "\\Detail\\" + "[" + extraInfo + "]_Detail_" + System.currentTimeMillis() + ".png";
			File outputDetailfile = new File(imageDetailFilePath);*/
			
			String imageFilePath = subDir+File.separator/*+"images_gen\\" + subDirName + "\\"*/ + "[" + extraInfo + "]_" + System.currentTimeMillis() + ".png";
			File outputfile = new File(imageFilePath);

			String imageDetailFilePath = subDir+File.separator/*+"images_gen\\" + subDirName + "\\"*/ + "\\Detail\\" + "[" + extraInfo + "]_Detail_" + System.currentTimeMillis() + ".png";
			File outputDetailfile = new File(imageDetailFilePath);

			try {
				ImageIO.write(mands[i].getBufferedImage(), "png", outputfile);
				System.out.println("Generated File: " + imageFilePath);

				final BufferedImage joinedFractalDataImage = FractalBase.joinBufferedImages(mands[i].getBufferedImage(), dataInfoImg);
				ImageIO.write(joinedFractalDataImage, "png", outputDetailfile);
				System.out.println("Generated FileDetail: " + imageDetailFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError oome) {
				oome.printStackTrace();
				if (!hasOutOfMemoryError) {
					hasOutOfMemoryError = true;
				} else {
					System.out.println("Error --- OuttaMemory ");
					System.exit(1);
				}
			}

			this.formulaArea.setText("");

			g.dispose();
			mands[i].dispose();
			mands[i] = null;
			
			Runtime.getRuntime().gc();//System.gc();
		}
		

		System.out.println("Done MandelbrotGeneration");
		JOptionPane.showMessageDialog(null, "Dir created: "+subDirName, "Mand Generated", JOptionPane.INFORMATION_MESSAGE);
		
		return;

	}

//////////////generator	PolyFract
	private void doSelectPolyVaryColorCommand(boolean varyColor) {
		this.polyVaryColor = varyColor;
	}

	private void doSelectPolyVaryIntraPixXYCommand(boolean varyaryIntraPixXY) {
		this.polyVaryIntraPixXY = varyaryIntraPixXY;
	}

	private void doSelectPolyVaryPixYTranCommand(boolean varyPixYTran) {
		this.polyVaryPixYTran = varyPixYTran;
	}

	private void doSelectPolyVaryPixXTranCommand(boolean varyPixXTran) {
		this.polyVaryPixXTran = varyPixXTran;
	}

	private void doSelectPolyVaryPixelZFuncCommand(boolean varyPixelZFunc) {
		this.polyVaryPixelZFunc = varyPixelZFunc;
	}

	private void doSelectPolyVaryConstCFuncCommand(boolean varyConstCFunc) {
		this.polyVaryConstCFunc = varyConstCFunc;
	}

	private void doSelectPolyVaryPixelConstOpZCCommand(boolean varyPixelConstOpZC) {
		this.polyVaryPixelConstOpZC = varyPixelConstOpZC;
	}

	private void doSelectPolyVaryGenConstantCommand(boolean varyGenConstant) {
		this.polyVaryConstant = varyGenConstant;
	}

	private void doSelectPolyVaryPixelPowerZCommand(boolean varyPixelPowerZ) {
		this.polyVaryPixelPowerZ = varyPixelPowerZ;
	}

	private void doSelectPolyVaryTypeCommand	(boolean varyPolyType) {
		this.polyVaryPolyType = varyPolyType;
	}

	private void doSelectPolyVaryIterCommand(boolean varyIter) {
		this.polyVaryIter = varyIter;
	}

	private void doSelectPolyVaryBoundaryCommand(boolean varyBoundary) {
		this.polyVaryBoundary = varyBoundary;
	}

	private void doSelectPolyVaryPixXCentrCommand(boolean varyPixXCentr) {
		this.polyVaryPixXCentr = varyPixXCentr;
	}

	private void doSelectPolyVaryPixYCentrCommand(boolean varyPixYCentr) {
		this.polyVaryPixYCentr = varyPixYCentr;
	}

	private void doSelectPolyVaryScaleSizeCommand(boolean varyScaleSize) {
		this.polyVaryScaleSize = varyScaleSize;
	}

	private void doSelectPolyVaryPixelPowerZCombosCommand(Integer varyPixelPowerZJumpVal) {
		this.polyVaryPixelPowerZJumpVal = varyPixelPowerZJumpVal;
	}

	private void doSelectPolyVaryBoundaryJumpCombosCommand(Double varyBoundaryJumpVal) {
		this.polyVaryBoundaryJumpVal = varyBoundaryJumpVal;
	}

	private void doSelectPolyVaryScaleSizeCombosCommand(Double varyScaleSizeJumpVal) {
		this.polyVaryScaleSizeJumpVal = varyScaleSizeJumpVal;
	}

	private void doSelectPolyApplyFormulaCommand(boolean apply) {
		this.polyApplyFormulaZ = apply;
	}

	private void doSetPolyGenCommand(boolean gen) {
		this.polyGen = gen;
		if (gen) {
			this.keepConst = false;
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
		}
		this.formulaArea.setVisible(true);
		this.doPolyChoicesCheck();
	}

	private void doSelectPolyGenRealJumpCombosCommand(Double realJump) {
		this.polyGenRealJumpVal = realJump;
	}

	private void doSelectPolyGenImagJumpCombosCommand(Double imagJump) {
		this.polyGenImagJumpVal = imagJump;
	}

	private void doPolyGenerateCommand() {

		if (this.polyVaryConstant && this.polyVaryGenConstantCb.isSelected()) {
			System.out.println("in_doPolyGenerateCommand_this.polyVaryConstant=true");
			try {
				this.polyGenRealFromVal = Double.parseDouble(this.polyGenRealFromTf.getText());
				this.polyGenImagFromVal = Double.parseDouble(this.polyGenImagFromTf.getText());
				this.polyGenRealToVal = Double.parseDouble(this.polyGenRealToTf.getText());
				this.polyGenImagToVal = Double.parseDouble(this.polyGenImagToTf.getText());

				if (this.polyGenRealFromVal > this.polyGenRealToVal
						|| this.polyGenImagFromVal > this.polyGenImagToVal) {
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for Const-Real",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number \n" + e2.getMessage(),
						"Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		boolean varyConst = this.polyVaryConstant && this.polyVaryGenConstantCb.isSelected();
		boolean varyKeepConst = (this.keepConst && this.polyKeepConstRb.isSelected()) && !varyConst;

		/*
		 * if ( !(varyConst && varyKeepConst) && !(this.polyVaryConstant &&
		 * this.polyVaryGenConstantCb.isSelected())) {
		 */
		if (!(this.keepConst && this.polyKeepConstRb.isSelected())
				&& !(this.polyVaryConstant && this.polyVaryGenConstantCb.isSelected())) {
			try {
				this.polyConstReal = Double.parseDouble(this.polyRealTf.getText());
				this.polyConstImg = Double.parseDouble(this.polyImgTf.getText());
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n" + e2.getMessage(),
						"Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		if (this.polyVaryBoundary) {
			System.out.println("in_doPolyGenerateCommand_this.polyVaryBoundary=true");
			try {
				this.polyVaryBoundaryFromVal = Double.parseDouble(this.polyGenBoundaryFromTf.getText());
				this.polyVaryBoundaryToVal = Double.parseDouble(this.polyGenBoundaryToTf.getText());

				if (this.polyVaryBoundaryFromVal > this.polyVaryBoundaryToVal) {
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for Boundary",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Please enter a valid Decimal Number for Boundary\n" + e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		if (this.polyVaryScaleSize) {
			System.out.println("in_doPolyGenerateCommand_this.polyVaryScaleSize=true");
			try {
				this.polyVaryScaleSizeFromVal = Double.parseDouble(this.polyGenScaleSizeFromTf.getText());
				this.polyVaryScaleSizeToVal = Double.parseDouble(this.polyGenScaleSizeToTf.getText());
				if (this.polyVaryScaleSizeFromVal > this.polyVaryScaleSizeToVal) {
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for ScaleSize",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null,
						"Please enter a valid Decimal Number for ScaleSize\n" + e2.getMessage(),
						"Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		if (this.polyVaryPixelPowerZ) {
			System.out.println("in_doPolyGenerateCommand_this.polyVaryPixelPowerZ=true");
			try {
				this.polyVaryPixelPowerZFromVal = Integer.parseInt(this.polyGenPixelPowerZFromTf.getText());
				this.polyVaryPixelPowerZToVal = Integer.parseInt(this.polyGenPixelPowerZToTf.getText());
				if (this.polyVaryPixelPowerZFromVal > this.polyVaryPixelPowerZToVal) {
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for PixelPowerZ",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Integer for PixelPowerZ\n" + e2.getMessage(),
						"Error - Not an Integer", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		final int varyColorCount = this.getVaryPolyColorCount();
		final List<?> varyColorList = this.getVaryPolyColorsList();

		final int varyPixXTransCount = this.getVaryPolyPixXTransCount();
		final List<?> varyPixXTrList = this.getVaryPolyPixXTransList();

		final int varyPixYTransCount = this.getVaryPolyPixYTransCount();
		final List<?> varyPixYTrList = this.getVaryPolyPixYTransList();

		final int varyIntraPixXYOpCount = this.getVaryPolyIntraPixXYOpCount();
		final List<?> varyIntraPixXYOpList = this.getVaryPolyIntraPixXYOpList();

		final int varyZFuncCount = this.getVaryPolyZFuncCount();
		final List<?> varyZFuncList = this.getVaryPolyZFuncList();

		final int varyConstCFuncCount = this.getVaryPolyConstCFuncCount();
		final List<?> varyConstCFuncList = this.getVaryPolyConstCFuncList();

		final int varyPixelConstOpZCCount = this.getVaryPolyPixelConstOpZCCount();
		final List<?> varyPixelConstOpZCList = this.getVaryPolyPixelConstOpZCList();

		final int powerFrom = this.polyVaryPixelPowerZFromVal;
		final int powerTo = this.polyVaryPixelPowerZToVal;
		final int powerJump = this.polyVaryPixelPowerZJumpVal;
		final int varyPowerCount = this.getVaryPolyPowerCount();
		final List<?> varyPowerList = this.getVaryPolyPowerList(powerFrom, powerTo, powerJump, varyPowerCount);

		final int varyPixXCenterCount = this.getVaryPolyPixXCenterCount();
		final List<?> varyPixXCenterList = this.getVaryPolyPixXCenterList();

		final int varyPixYCenterCount = this.getVaryPolyPixYCenterCount();
		final List<?> varyPixYCenterList = this.getVaryPolyPixYCenterList();

		final int varyPolyTypeCount = this.getVaryPolyTypeCount();
		final List<?> varyPolyTypeList = this.getVaryPolyTypeList();
		
		final int varyIterCount = this.getVaryPolyIterCount();
		final List<?> varyIterList = this.getVaryPolyIterList();

		final int varyUseDiffCount = 2;
		final List<?> varyUseDiffList = this.getAppendStr2List("useDiffChoice=", this.getTrueFalseList());

		final int varyInvertPixelCount = 2;
		final List<?> varyInvertPixelList = this.getAppendStr2List("invertPixelCalcChoice=", this.getTrueFalseList());

		final double scaleSizeFrom = this.polyVaryScaleSizeFromVal;
		final double scaleSizeTo = this.polyVaryScaleSizeToVal;
		final double scaleSizeJump = this.polyVaryScaleSizeJumpVal;
		final int varyScaleSizeCount = this.getVaryPolyScaleSizeCount();
		final List<?> varyScaleSizeList = this.getVaryPolyScaleSizeList(scaleSizeFrom, scaleSizeTo, scaleSizeJump,
				varyScaleSizeCount);

		final double boundaryFrom = this.polyVaryBoundaryFromVal;
		final double boundaryTo = this.polyVaryBoundaryToVal;
		final double boundaryJump = this.polyVaryBoundaryJumpVal;
		final int varyBoundaryCount = this.getVaryPolyBoundaryCount();
		final List<?> varyBoundaryList = this.getVaryPolyBoundaryList(boundaryFrom, boundaryTo, boundaryJump,
				varyBoundaryCount);
		
		

		final double jRealFrom = this.polyGenRealFromVal;
		final double jImagFrom = this.polyGenImagFromVal;
		final double jRealTO = this.polyGenRealToVal;
		final double jImagTO = this.polyGenImagToVal;
		final double realJumpVal = this.polyGenRealJumpVal;
		final double imagJumpVal = this.polyGenImagJumpVal;

		final int varyRealConstantCount = this.getVaryPolyRealConstantCount();
		final List<?> varyRealConstList = this.getVaryPolyRealConstantList(jRealFrom, jRealTO, realJumpVal,
				varyRealConstantCount);

		final int varyImagConstantCount = this.getVaryPolyImagConstantCount();
		final List<?> varyImagConstList = this.getVaryPolyImagConstantList(jImagFrom, jImagTO, imagJumpVal,
				varyImagConstantCount);

		final List<?> varyConstList = this.getAppendStr2List("constantChoice=",
				this.product(varyRealConstList, varyImagConstList));

		// System.out.println("getTotalVaryCount=" + this.getTotalVaryCount());
		// System.out.println("getTotalVaryCount=HARDED-2-To5== " + 5);
		// int totalVaryCount = this.getTotalVaryCount();//5;

		int totalVaryCount = varyColorCount * varyPixXTransCount * varyPixYTransCount * varyIntraPixXYOpCount
				* varyZFuncCount * varyConstCFuncCount * varyPixelConstOpZCCount * varyPowerCount * varyPixXCenterCount
				* varyPixYCenterCount * varyScaleSizeCount * varyBoundaryCount * varyIterCount * varyPolyTypeCount
				* varyUseDiffCount * varyInvertPixelCount * varyRealConstantCount * varyImagConstantCount;

		System.out.println("getTotalVaryCount=" + totalVaryCount);

		if (totalVaryCount > 150) {
			int ans = JOptionPane.showConfirmDialog(null,
					"Are You Sure needs to generate\n" + totalVaryCount + "images", "Are_You_Sure",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if (ans != JOptionPane.YES_OPTION) {
				return;
			}
		}

		final List<?> allCombinationsList = this.product(varyColorList, varyPixXTrList, varyPixYTrList,
				varyIntraPixXYOpList, varyZFuncList, varyConstCFuncList, varyPixelConstOpZCList, varyPowerList,
				varyPixXCenterList, varyPixYCenterList, varyScaleSizeList, varyBoundaryList, varyIterList,
				varyPolyTypeList, varyUseDiffList, varyInvertPixelList, varyConstList);

		System.out
				.println("allCombinationsListSize_is_"+allCombinationsList.size()+"________allCombinationsListSize==totalVaryCount  " + (allCombinationsList.size() == totalVaryCount));

		Properties[] props = new Properties[allCombinationsList.size()];
		props = shuffle(this.getAllCombinationProperties(allCombinationsList));
		// ps = this.getAllCombinationProperties(allCombinationsList);

		/* final */ String subDirName;
		/*
		 * if(!varyConst && !varyKeepConst) { subDirName = "Poly_{(R)[" +
		 * String.format("%.2f", this.polyGenRealFromVal) + "_to_" +
		 * String.format("%.2f", this.polyGenRealToVal) + "],(I)[" +
		 * String.format("%.2f", this.polyGenImagFromVal) + "_to_" +
		 * String.format("%.2f", this.polyGenImagToVal) + "]" +
		 * System.currentTimeMillis() + "}"; } else { subDirName = "Poly_" +
		 * System.currentTimeMillis(); }
		 */

		subDirName = POLY + "__Vary[";

		if (this.polyVaryColor)
			subDirName += "Colr,";
		if (this.polyVaryPixXTran)
			subDirName += "F(x),";
		if (this.polyVaryPixYTran)
			subDirName += "F(y),";
		if (this.polyVaryIntraPixXY)
			subDirName += "I(xy),";
		if (this.polyVaryPixelZFunc)
			subDirName += "F(Z),";
		if (this.polyApplyFormulaZ)
			subDirName += "CustFormula,";
		if (this.polyVaryConstant)
			subDirName += "Const,";
		if (this.polyVaryConstCFunc)
			subDirName += "F(C),";
		if (this.polyVaryPixelConstOpZC)
			subDirName += "O(ZC),";
		if (this.polyVaryPixelPowerZ)
			subDirName += "X(Z),";
		if (this.polyVaryIter)
			subDirName += "M(It),";
		if (this.polyVaryPixXCentr)
			subDirName += "Cx,";
		if (this.polyVaryPixYCentr)
			subDirName += "Cy,";
		if (this.polyVaryBoundary)
			subDirName += "Bd,";
		if (this.polyVaryPolyType)
		subDirName += "Type,";
		if (this.polyVaryScaleSize)
		subDirName += "Sz,";

		subDirName = subDirName.substring(0, subDirName.length() - 1) + "]_" + System.currentTimeMillis();

/*		File subDir = new File("images_gen\\" + subDirName);
		if (!subDir.exists()) {
			subDir.mkdir();
		}*/
		
		File subDir = null;
		if (!diyPolyGen2Folder && diyPolyGen2FolderPath == null) {
			subDir = new File("images_gen\\" + subDirName);

		} else {
			subDir = new File(diyPolyGen2FolderPath+"\\"+subDirName);
		}
		
		if (!subDir.exists()) {
			subDir.mkdir();
			//check-again-for-create
			System.out.println("subDir-did-NOTExist.....created_using_mkdir--now-checking");
			System.out.println("subDir_exists?....."+subDir.exists());
		}

		File subDirDetail = new File(subDir, "Detail");
		if (!subDirDetail.exists()) {
			subDirDetail.mkdir();
		}

		PolyFract[] polys = new PolyFract[totalVaryCount];
		boolean hasOutOfMemoryError = false;

		for (int i = 0; i < totalVaryCount; i++) {
			Runtime.getRuntime().gc();
			if (hasOutOfMemoryError) {
				i -= 1;
			}

			try {
				polys[i] = new PolyFract(this.correctProperties(props[i]));
				polys[i].setGenerated(true);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage());
				JOptionPane.showMessageDialog(null, "BAaaAD Properties\n" + e1.getMessage(), "One key has NULL value",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			polys[i].setVisible(false); // make invisible

			/**
			 * 
			 * //need-to-set-4-booleans,as-properties-sends-strings-only
			 * Polys[i].setApplyCustomFormula(this.diyMApplyFormulaZ);
			 */

			/////// reassign 2 UI - 4 formulaArea
			this.constFuncChoice = polys[i].getUseFuncConst();

			// 4_getExtraInfo
			this.polyPower = polys[i].getPower();
			this.polyUseDiff = polys[i].isUseDiff();
			this.polyBound = polys[i].getBound();
			this.polyType = polys[i].getRowColMixType();
			this.polyMaxIter = polys[i].getMaxIter();
			this.polyXC = polys[i].getxC();
			this.polyYC = polys[i].getyC();
			this.polyScaleSize = polys[i].getScaleSize();

			// 4_img_Base_Info
			this.colorChoice = polys[i].getColorChoice();
			if (this.colorChoice.equals("SampleMix")) {
				this.setSampleStart_DivVals(polys[i]);
			}

			if (this.colorChoice.equals("ColorBlowout")) {
				this.setColorBlowoutTypeVals(polys[i]);
			}
			
			if (this.colorChoice.equals("ColorSuperBlowout")) {
				this.setColorSuperBlowoutTypeVals(polys[i]);
			}
			
			String pxFunc = polys[i].getUseFuncPixel();
			this.pxFuncChoice = pxFunc;

			String pixXTr = polys[i].getPxXTransformation();
			this.pixXTransform = pixXTr;

			String pixYTr = polys[i].getPxYTransformation();
			this.pixYTransform = pixYTr;

			String pixIntraXYOp = polys[i].getPixXYOperation();
			this.pixIntraXYOperation = pixIntraXYOp;

			String pixConstOp = polys[i].getPxConstOperation();
			this.pxConstOprnChoice = pixConstOp;

			this.setPolyUseDiff(polys[i].isUseDiff());
			this.invertPixelCalculation = polys[i].isReversePixelCalculation();

			if (!(polys[i].isComplexNumConst() || this.polyKeepConst || this.keepConst)
					&& (this.polyKeepConstRb.isSelected() || this.polyVaryConstant)) {
				this.polyConstReal = polys[i].getCompConst().real;
				this.polyConstImg = polys[i].getCompConst().imaginary;
			} else {
				this.keepConst = true;
			}
			
			String extraInfo = this.getExtraInfo();

			this.setPolyGenFormulaArea(pxFunc, this.polyPower, pixXTr, pixYTr, pixIntraXYOp, pixConstOp);
			if (!this.keepConst) {
				this.formulaArea.append("<br/>Constant = " + this.polyConstReal + " + (" + this.polyConstImg
						+ " * i)</font>" + eol);
			} /* else { */
			//
			this.addPolyConstInfo(polys[i]);
			/* } */
			Runtime.getRuntime().gc();
			/// done1---now-to-imaging

			Graphics2D g = polys[i].getBufferedImage().createGraphics();
			// System
			try {
				polys[i].paint(g);
			} catch (NullPointerException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage() + "+this.pxFuncChoiceIs\n" + this.pxFuncChoice);

				if (varyZFuncCount != 1) {
					JOptionPane.showMessageDialog(null, "BAaaAD Formula - continuing\n" + this.pxFuncChoice,
							"IncorrectFormula", JOptionPane.INFORMATION_MESSAGE);

					this.formulaArea.setText("");

					g.dispose();
					polys[i].dispose();
					polys[i] = null;

					Runtime.getRuntime().gc();

					continue;
				} else {
					JOptionPane.showMessageDialog(null, "BAaaAD Formula\n" + this.pxFuncChoice, "IncorrectFormula",
							JOptionPane.ERROR_MESSAGE);

					return;
				}
			}
			
			this.setFractalBase(polys[i]);

			this.setFractalImage(polys[i].getBufferedImage());

			String imgBaseInfo = this.getImgBaseInfo();
			BufferedImage dataInfoImg = this.createStringImage(imgBaseInfo);

			/*String imageFilePath = "images_gen\\" + subDirName + "\\" + "[" + extraInfo + "]_"
					+ System.currentTimeMillis() + ".png";
			File outputfile = new File(imageFilePath);

			String imageDetailFilePath = "images_gen\\" + subDirName + "\\Detail\\" + "[" + extraInfo + "]_Detail_"
					+ System.currentTimeMillis() + ".png";
			File outputDetailfile = new File(imageDetailFilePath);*/
			
			String imageFilePath = subDir+File.separator/*+"images_gen\\" + subDirName + "\\"*/ + "[" + extraInfo + "]_" + System.currentTimeMillis() + ".png";
			File outputfile = new File(imageFilePath);

			String imageDetailFilePath = subDir+File.separator/*+"images_gen\\" + subDirName + "\\"*/ + "\\Detail\\" + "[" + extraInfo + "]_Detail_" + System.currentTimeMillis() + ".png";
			File outputDetailfile = new File(imageDetailFilePath);


			try {
				ImageIO.write(polys[i].getBufferedImage(), "png", outputfile);
				System.out.println("Generated File: " + imageFilePath);

				final BufferedImage joinedFractalDataImage = FractalBase.joinBufferedImages(polys[i].getBufferedImage(),
						dataInfoImg);
				ImageIO.write(joinedFractalDataImage, "png", outputDetailfile);
				System.out.println("Generated FileDetail: " + imageDetailFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError oome) {
				oome.printStackTrace();
				if (!hasOutOfMemoryError) {
					hasOutOfMemoryError = true;
				} else {
					System.out.println("Error --- OuttaMemory ");
					System.exit(1);
				}
			}

			this.formulaArea.setText("");

			g.dispose();
			polys[i].dispose();
			polys[i] = null;

			Runtime.getRuntime().gc();// System.gc();
		}

		System.out.println("Done PolyFractGeneration");
		JOptionPane.showMessageDialog(null, "Dir created: " + subDirName, "Poly Generated",
				JOptionPane.INFORMATION_MESSAGE);

		return;

	}

	/////////////////////////////////////////////// ensPolyFractMethods////////////////////////////////////////////////////////
	
	///////////////generator	Julia
	private void doSelectDiyJuliaVaryColorCommand(boolean varyColor) {
		this.diyJuliaVaryColor = varyColor;
	}	

	private void doSelectDiyJuliaVaryIntraPixXYCommand(boolean varyaryIntraPixXY) {
		this.diyJuliaVaryIntraPixXY = varyaryIntraPixXY;
	}
	
	private void doSelectDiyJuliaVaryPixYTranCommand(boolean varyPixYTran) {
		this.diyJuliaVaryPixYTran = varyPixYTran;
	}	

	private void doSelectDiyJuliaVaryPixXTranCommand(boolean varyPixXTran) {
		this.diyJuliaVaryPixXTran = varyPixXTran;
	}	

	private void doSelectDiyJuliaVaryPixelZFuncCommand(boolean varyPixelZFunc) {
		this.diyJuliaVaryPixelZFunc = varyPixelZFunc;
	}

	private void doSelectDiyJuliaVaryConstCFuncCommand(boolean varyConstCFunc) {
		this.diyJuliaVaryConstCFunc = varyConstCFunc;
	}

	private void doSelectDiyJuliaVaryPixelConstOpZCCommand(boolean varyPixelConstOpZC) {
		this.diyJuliaVaryPixelConstOpZC = varyPixelConstOpZC;
	}

	private void doSelectDiyJuliaVaryGenConstantCommand(boolean varyGenConstant) {
		this.diyJuliaVaryConstant = varyGenConstant;
	}

	private void doSelectDiyJuliaVaryPixelPowerZCommand(boolean varyPixelPowerZ) {
		this.diyJuliaVaryPixelPowerZ = varyPixelPowerZ;
	}
	
	private void doSelectDiyJuliaVaryFieldTypesCommand(boolean varyFieldType) {
		this.diyJuliaVaryFieldType = varyFieldType;
	}
	
	private void doSelectDiyJuliaVaryIterCommand(boolean varyIter) {
		this.diyJuliaVaryIter = varyIter;
	}
	
	private void doSelectDiyJuliaVaryBoundaryCommand(boolean varyBoundary) {
		this.diyJuliaVaryBoundary = varyBoundary;
	}
	
	private void doSelectDiyJuliaVaryPixXCentrCommand(boolean varyPixXCentr) {
		this.diyJuliaVaryPixXCentr = varyPixXCentr;
	}
	private void doSelectDiyJuliaVaryPixYCentrCommand(boolean varyPixYCentr) {
		this.diyJuliaVaryPixYCentr = varyPixYCentr;
	}
	
	private void doSelectDiyJuliaVaryScaleSizeCommand(boolean varyScaleSize) {
		this.diyJuliaVaryScaleSize = varyScaleSize;
	}
	
	private void doSelectDiyJuliaVaryPixelPowerZCombosCommand(Integer varyPixelPowerZJumpVal) {
		this.diyJuliaVaryPixelPowerZJumpVal = varyPixelPowerZJumpVal;
	}
	
	private void doSelectDiyJuliaVaryBoundaryJumpCombosCommand(Double varyBoundaryJumpVal) {
		this.diyJuliaVaryBoundaryJumpVal = varyBoundaryJumpVal;
	}
	private void doSelectDiyJuliaVaryScaleSizeCombosCommand(Double varyScaleSizeJumpVal) {
		this.diyJuliaVaryScaleSizeJumpVal = varyScaleSizeJumpVal;
	}
	

	private void doSelectDiyJuliaApplyFormulaCommand(boolean apply) {
		this.diyJApplyFormulaZ = apply;
	}
	
	
	
	
	private void doSetDiyJuliaGenCommand(boolean gen) {
		this.diyJuliaGen = gen;
		if (gen) {
			this.keepConst = false;
			this.buStart.setEnabled(false);
			this.buSavePxStart.setEnabled(false);
		}
		this.formulaArea.setVisible(true);
		this.doDiyJuliaChoicesCheck();
	}
	
	private void doSelectDiyJuliaGenRealJumpCombosCommand(Double realJump) {
		this.diyJuliaGenRealJumpVal = realJump;
	}
	private void doSelectDiyJuliaGenImagJumpCombosCommand(Double imagJump) {
		this.diyJuliaGenImagJumpVal = imagJump;
	}
	
	private void doJuliaGenerateCommand() {
		
		if (this.diyJuliaVaryConstant && this.diyJuliaVaryGenConstantCb.isSelected()) {
			System.out.println("in_doJuliaGenerateCommand_this.diyJuliaVaryConstant=true");
			try {
				this.diyJuliaGenRealFromVal = Double.parseDouble(this.diyJuliaGenRealFromTf.getText());
				this.diyJuliaGenImagFromVal = Double.parseDouble(this.diyJuliaGenImagFromTf.getText());
				this.diyJuliaGenRealToVal = Double.parseDouble(this.diyJuliaGenRealToTf.getText());
				this.diyJuliaGenImagToVal = Double.parseDouble(this.diyJuliaGenImagToTf.getText());
				
				if(this.diyJuliaGenRealFromVal>this.diyJuliaGenRealToVal||this.diyJuliaGenImagFromVal>this.diyJuliaGenImagToVal){
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for Const-Real",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number \n"+e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
		}
		
		boolean varyConst = this.diyJuliaVaryConstant && this.diyJuliaVaryGenConstantCb.isSelected();
		boolean varyKeepConst = (this.keepConst && this.diyJuliaKeepConstRb.isSelected()) && !varyConst;
		
//		if (	/*!*/(varyConst && varyKeepConst) && 
//				/*!*/(this.diyJuliaVaryConstant && this.diyJuliaVaryGenConstantCb.isSelected()) && !this.keepConst) {
		if (!(this.keepConst && this.diyJuliaKeepConstRb.isSelected())
				&& !(this.diyJuliaVaryConstant && this.diyJuliaVaryGenConstantCb.isSelected())) {
			try {
				this.diyJuliaConstReal = Double.parseDouble(this.diyJuliaRealTf.getText());
				this.diyJuliaConstImg = Double.parseDouble(this.diyJuliaImgTf.getText());
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		
		if (this.diyJuliaVaryBoundary) {
			System.out.println("in_doJuliaGenerateCommand_this.diyJuliaVaryBoundary=true");
			try {
				this.diyJuliaVaryBoundaryFromVal = Double.parseDouble(this.diyJuliaGenBoundaryFromTf.getText());
				this.diyJuliaVaryBoundaryToVal = Double.parseDouble(this.diyJuliaGenBoundaryToTf.getText());
				
				if (this.diyJuliaVaryBoundaryFromVal > this.diyJuliaVaryBoundaryToVal) {
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for Boundary",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number for Boundary\n"+e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
		}
		
		if (this.diyJuliaVaryScaleSize) {
			System.out.println("in_doJuliaGenerateCommand_this.diyJuliaVaryScaleSize=true");
			try {
				this.diyJuliaVaryScaleSizeFromVal = Double.parseDouble(this.diyJuliaGenScaleSizeFromTf.getText());
				this.diyJuliaVaryScaleSizeToVal = Double.parseDouble(this.diyJuliaGenScaleSizeToTf.getText());
				if(this.diyJuliaVaryScaleSizeFromVal>this.diyJuliaVaryScaleSizeToVal){
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for ScaleSize",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number for ScaleSize\n"+e2.getMessage(), "Error - Not a Decimal",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
		}

		if (this.diyJuliaVaryPixelPowerZ) {
			System.out.println("in_doJuliaGenerateCommand_this.diyJuliaVaryPixelPowerZ=true");
			try {
				this.diyJuliaVaryPixelPowerZFromVal = Integer.parseInt(this.diyJuliaGenPixelPowerZFromTf.getText());
				this.diyJuliaVaryPixelPowerZToVal = Integer.parseInt(this.diyJuliaGenPixelPowerZToTf.getText());
				if(this.diyJuliaVaryPixelPowerZFromVal>this.diyJuliaVaryPixelPowerZToVal){
					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range for PixelPowerZ",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Integer for PixelPowerZ\n"+e2.getMessage(), "Error - Not an Integer",
						JOptionPane.ERROR_MESSAGE);
				return;
			} 
		}

		/*String diyJApplyField = this.fieldLines;
		boolean diyJApplyFatou = this.applyFatou;
		boolean diyJApplyZSq = this.applyZSq;
		boolean diyJApplyClassic = this.applyClassicJulia;
		boolean diyJApplyLyapunovExponent = this.diyJuliaUseLyapunovExponent;*/
		
		final int varyColorCount = this.getVaryJuliaColorCount();
		final List<?> varyColorList = this.getVaryJuliaColorsList();
		
		final int varyJuliaFieldTypesCount = this.getVaryJuliaFieldTypesCount();
		final List<?> varyJuliaFieldTypesList = this.getVaryJuliaFieldTypesList();
		
		final int varyPixXTransCount = this.getVaryJuliaPixXTransCount();
		final List<?> varyPixXTrList = this.getVaryJuliaPixXTransList();
		
		final int varyPixYTransCount = this.getVaryJuliaPixYTransCount();
		final List<?> varyPixYTrList = this.getVaryJuliaPixYTransList();
		
		final int varyIntraPixXYOpCount = this.getVaryJuliaIntraPixXYOpCount();
		final List<?> varyIntraPixXYOpList = this.getVaryJuliaIntraPixXYOpList();		
		
		final int varyZFuncCount = this.getVaryJuliaZFuncCount();
		final List<?> varyZFuncList = this.getVaryJuliaZFuncList();
		
		final int varyConstCFuncCount = this.getVaryJuliaConstCFuncCount();
		final List<?> varyConstCFuncList = this.getVaryJuliaConstCFuncList();
		
		final int varyPixelConstOpZCCount = this.getVaryJuliaPixelConstOpZCCount();
		final List<?> varyPixelConstOpZCList = this.getVaryJuliaPixelConstOpZCList();
		
		final int powerFrom = this.diyJuliaVaryPixelPowerZFromVal;
		final int powerTo = this.diyJuliaVaryPixelPowerZToVal;
		final int powerJump = this.diyJuliaVaryPixelPowerZJumpVal;
		final int varyPowerCount = this.getVaryJuliaPowerCount();		
		final List<?> varyPowerList = this.getVaryJuliaPowerList(powerFrom, powerTo, powerJump, varyPowerCount);
		
		final int varyPixXCenterCount = this.getVaryJuliaPixXCenterCount();
		final List<?> varyPixXCenterList = this.getVaryJuliaPixXCenterList();
		
		final int varyPixYCenterCount = this.getVaryJuliaPixYCenterCount();
		final List<?> varyPixYCenterList = this.getVaryJuliaPixYCenterList();
		
		final int varyIterCount = this.getVaryJuliaIterCount();
		final List<?> varyIterList = this.getVaryJuliaIterList();
		
		final int varyUseDiffCount = 2;
		final List<?> varyUseDiffList = this.getAppendStr2List("useDiffChoice=",this.getTrueFalseList());
		
		final int varyInvertPixelCount = 2;
		final List<?> varyInvertPixelList = this.getAppendStr2List("invertPixelCalcChoice=", this.getTrueFalseList());

		final double scaleSizeFrom = this.diyJuliaVaryScaleSizeFromVal;
		final double scaleSizeTo = this.diyJuliaVaryScaleSizeToVal;
		final double scaleSizeJump = this.diyJuliaVaryScaleSizeJumpVal;
		final int varyScaleSizeCount = this.getVaryJuliaScaleSizeCount();		
		final List<?> varyScaleSizeList = this.getVaryJuliaScaleSizeList(scaleSizeFrom, scaleSizeTo, scaleSizeJump, varyScaleSizeCount);
		
		
		final double boundaryFrom = this.diyJuliaVaryBoundaryFromVal;
		final double boundaryTo = this.diyJuliaVaryBoundaryToVal;
		final double boundaryJump = this.diyJuliaVaryBoundaryJumpVal;
		final int varyBoundaryCount = this.getVaryJuliaBoundaryCount();
		final List<?> varyBoundaryList = this.getVaryJuliaBoundaryList(boundaryFrom, boundaryTo, boundaryJump, varyBoundaryCount);
		
		
		final double jRealFrom = this.diyJuliaGenRealFromVal;
		final double jImagFrom = this.diyJuliaGenImagFromVal;
		final double jRealTO = this.diyJuliaGenRealToVal;
		final double jImagTO = this.diyJuliaGenImagToVal;		
		final double realJumpVal = this.diyJuliaGenRealJumpVal;
		final double imagJumpVal = this.diyJuliaGenImagJumpVal;
		
		final int varyRealConstantCount = this.getVaryJuliaRealConstantCount();
		final List<?> varyRealConstList = this.getVaryJuliaRealConstantList(jRealFrom,jRealTO,realJumpVal,varyRealConstantCount);
		
		final int varyImagConstantCount = this.getVaryJuliaImagConstantCount();
		final List<?> varyImagConstList = this.getVaryJuliaImagConstantList(jImagFrom,jImagTO,imagJumpVal,varyImagConstantCount);
		
		final List<?> varyConstList = this.getAppendStr2List("constantChoice=", this.product(varyRealConstList,varyImagConstList));

//		System.out.println("getTotalVaryCount=" + this.getTotalVaryCount());
//		System.out.println("getTotalVaryCount=HARDED-2-To5== " + 5);
//		int totalVaryCount = this.getTotalVaryCount();//5;
		
		int totalVaryCount = varyColorCount * varyJuliaFieldTypesCount * varyPixXTransCount * varyPixYTransCount * varyIntraPixXYOpCount * 
				varyZFuncCount * varyConstCFuncCount * varyPixelConstOpZCCount  * 
				varyPowerCount  * varyPixXCenterCount * varyPixYCenterCount  * 
				varyScaleSizeCount * varyBoundaryCount  * varyIterCount * 
				varyUseDiffCount * varyInvertPixelCount *
				varyRealConstantCount * varyImagConstantCount;
				
		System.out.println("getTotalVaryCount=" + totalVaryCount);

		if (totalVaryCount > 150) {
			int ans = JOptionPane.showConfirmDialog(null, "Are You Sure needs to generate\n"+totalVaryCount+"images", "Are_You_Sure",
					JOptionPane.YES_NO_CANCEL_OPTION);
			if(ans!=JOptionPane.YES_OPTION){
				return;
			}
		}
		
		final List<?> allCombinationsList = this.product(
				varyColorList, varyJuliaFieldTypesList, varyPixXTrList, varyPixYTrList, varyIntraPixXYOpList,
				varyZFuncList, varyConstCFuncList, varyPixelConstOpZCList,
				varyPowerList, varyPixXCenterList, varyPixYCenterList,
				varyScaleSizeList, varyBoundaryList, varyIterList,
				varyUseDiffList, varyInvertPixelList,
				varyConstList);
		
		System.out.println("allCombinationsListSize==totalVaryCount  "+(allCombinationsList.size()==totalVaryCount ));
		
		if(allCombinationsList.size()!=totalVaryCount){
			JOptionPane.showMessageDialog(null, "List & Size Mimatch", "Check applyFormula", JOptionPane.ERROR_MESSAGE);
			
			return;
		}
		
		Properties[] props = new Properties[allCombinationsList.size()];
		props = shuffle(this.getAllCombinationProperties(allCombinationsList));
//		ps = this.getAllCombinationProperties(allCombinationsList);

		
		/*final*/ String subDirName;
		/*if(!varyConst && !varyKeepConst) {
			subDirName = "Julia_{(R)[" + String.format("%.2f", this.diyJuliaGenRealFromVal) + "_to_"
					+ String.format("%.2f", this.diyJuliaGenRealToVal) + "],(I)["
					+ String.format("%.2f", this.diyJuliaGenImagFromVal) + "_to_"
					+ String.format("%.2f", this.diyJuliaGenImagToVal) + "]" + System.currentTimeMillis() + "}";
		} else {
			subDirName = "Julia_" + System.currentTimeMillis();
		}*/

		subDirName = JULIA + "__Vary[";
		
		if (this.diyJuliaVaryColor)					subDirName += "Colr,";
		if (this.diyJuliaVaryPixXTran)				subDirName += "F(x),";
		if (this.diyJuliaVaryPixYTran)				subDirName += "F(y),";
		if (this.diyJuliaVaryIntraPixXY)			subDirName += "I(xy),";
		if (this.diyJuliaVaryPixelZFunc)			subDirName += "F(Z),";
		if (this.diyJApplyFormulaZ)					subDirName += "CustFormula,";
		if (this.diyJuliaVaryConstant)				subDirName += "Const,";
		if (this.diyJuliaVaryConstCFunc)			subDirName += "F(C),";
		if (this.diyJuliaVaryPixelConstOpZC)		subDirName += "O(ZC),";
		if (this.diyJuliaVaryPixelPowerZ)			subDirName += "X(Z),";
		if (this.diyJuliaVaryIter)					subDirName += "M(It),";
		if (this.diyJuliaVaryPixXCentr)				subDirName += "Cx,";
		if (this.diyJuliaVaryPixYCentr)				subDirName += "Cy,";
		if (this.diyJuliaVaryBoundary)				subDirName += "Bd,";
		if (this.diyJuliaVaryScaleSize)				subDirName += "Sz,";
		if (this.useRangeEstimation)				subDirName += "__RngEstSet";
		
		subDirName = subDirName.substring(0, subDirName.length() - 1) + "]_" + System.currentTimeMillis();
		
		File subDir = null;
		if (!diyJuliaGen2Folder && diyJuliaGen2FolderPath == null) {
			subDir = new File("images_gen\\" + subDirName);

		} else {
			subDir = new File(diyJuliaGen2FolderPath+"\\"+subDirName);
		}
		
		if (!subDir.exists()) {
			subDir.mkdir();
			//check-again-for-create
			System.out.println("subDir-did-NOTExist.....created_using_mkdir--now-checking");
			System.out.println("subDir_exists?....."+subDir.exists());
		}
		
		File subDirDetail = new File(subDir, "Detail");
		if (!subDirDetail.exists()) {
			subDirDetail.mkdir();
		}
		
		Julia[] julies = new Julia[totalVaryCount];
		boolean hasOutOfMemoryError = false;

		for (int i = 0; i < totalVaryCount; i++) {
			Runtime.getRuntime().gc();
			if (hasOutOfMemoryError) {
				i -= 1;
			}
			try {
				julies[i] = new Julia(this.correctProperties(props[i]));
				julies[i].setGenerated(true);
			} catch (IllegalArgumentException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage());
				JOptionPane.showMessageDialog(null, "BAaaAD Properties\n"+e1.getMessage(), "One key has NULL value",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			julies[i].setVisible(false); // make invisible
			/*julies[i].setAreaSize(this.juliaSize);
			julies[i].setRotation(this.rotation);*/
			/**
			 * 
			 * //need-to-set-4-booleans,as-properties-sends-strings-only
			julies[i].setApplyCustomFormula(this.diyJApplyFormulaZ);*/

			/////// reassign 2 UI - 4 formulaArea
			this.constFuncChoice = julies[i].getUseFuncConst();

			// 4_getExtraInfo
			this.diyJuliaPower = julies[i].getPower();
			this.diyJuliaBound = julies[i].getBound();
			this.diyJuliaMaxIter = julies[i].getMaxIter();
			this.diyJuliaXC = julies[i].getxC();
			this.diyJuliaYC = julies[i].getyC();
			this.diyJuliaScaleSize = julies[i].getScaleSize();

			// 4_img_Base_Info
			this.colorChoice = julies[i].getColorChoice();
			if (this.colorChoice.equals("SampleMix")) {
				/* this.setSampleColorMix(julies[i]); */

				this.setSampleStart_DivVals(julies[i]);
			}

			if (this.colorChoice.equals("ColorBlowout")) {
				this.setColorBlowoutTypeVals(julies[i]);
			}

			if (this.colorChoice.equals("ColorSuperBlowout")) {
				this.setColorSuperBlowoutTypeVals(julies[i]);
			}
			
			

			String pxFunc = julies[i].getUseFuncPixel();
			this.pxFuncChoice = pxFunc;

			String pixXTr = julies[i].getPxXTransformation();
			this.pixXTransform = pixXTr;
			
			String pixYTr = julies[i].getPxYTransformation();
			this.pixYTransform = pixYTr;
			
			String pixIntraXYOp = julies[i].getPixXYOperation();
			this.pixIntraXYOperation=pixIntraXYOp;
			
			String pixConstOp = julies[i].getPxConstOperation();
			this.pxConstOprnChoice=pixConstOp;
			

			this.setDiyJuliaUseDiff(julies[i].isUseDiff());
			this.invertPixelCalculation = julies[i].isReversePixelCalculation();
			
			if (!(julies[i].isComplexNumConst() || this.diyJuliaKeepConst || this.keepConst)
					&&( this.diyJuliaKeepConstRb.isSelected() || this.diyJuliaVaryConstant) ) {
				this.diyJuliaConstReal = julies[i].getComplex().real;
				this.diyJuliaConstImg = julies[i].getComplex().imaginary;
			} else {
				this.keepConst = true;
			}

			this.fieldLines = julies[i].getFieldType();

			String extraInfo = this.getExtraInfo();
			
			this.setDiyJuliaGenFormulaArea(pxFunc, this.diyJuliaPower, this.fieldLines,/*diyJApplyFatou, diyJApplyZSq, diyJApplyClassic,
					*/pixXTr, pixYTr, pixIntraXYOp, pixConstOp);
			this.formulaArea.append("<br/>Constant = " + this.diyJuliaConstReal + " + (" + this.diyJuliaConstImg
					+ " * i)</font>" + eol);
			//
			this.addJuliaConstInfo(julies[i]);

//			julies[i].reset();			----DOWENEEDTHIS-4-julia

			Runtime.getRuntime().gc();
			/// done1---now-to-imaging

			Graphics2D g = julies[i].getBufferedImage().createGraphics();
//			System
			try {

				boolean useRngEst = this.useRangeEstimation;
				if (useRngEst) { //
					try {
						double xMinRealVal = Double.parseDouble(this.xMinTf.getText());
						double yMinImgVal = Double.parseDouble(this.yMinTf.getText());
						double xMaxRealVal = Double.parseDouble(this.xMaxTf.getText());
						double yMaxImgVal = Double.parseDouble(this.yMaxTf.getText());

						if (xMinRealVal >= xMaxRealVal || yMinImgVal >= yMaxImgVal) {
							String err = (xMinRealVal < xMaxRealVal) ? "xMin > xMax" : "yMin > yMax";
							JOptionPane.showMessageDialog(null, "Please enter a valid Range value", err, JOptionPane.ERROR_MESSAGE);
							return;
						}

						julies[i].setRefocusDraw(true);
						julies[i].setRangeMinMaxVals(xMinRealVal, yMinImgVal, xMaxRealVal, yMaxImgVal);
						//julies[i].createFocalFractalShape(ff, new ComplexNumber(xMinRealVal,yMinImgVal), new ComplexNumber(xMaxRealVal,yMaxImgVal));
					} catch (NumberFormatException  | NullPointerException e2) {
						e2.printStackTrace();
						JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				julies[i].paint(g);
				
			} catch (NullPointerException e1) {
				e1.printStackTrace();
				System.out.println(e1.getMessage() + "+this.pxFuncChoiceIs\n" + this.pxFuncChoice);

				if (varyZFuncCount != 1) {
					JOptionPane.showMessageDialog(null, "BAaaAD Formula - continuing\n"
							+ this.pxFuncChoice, "IncorrectFormula",
							JOptionPane.INFORMATION_MESSAGE);

					this.formulaArea.setText("");

					g.dispose();
					julies[i].dispose();
					julies[i] = null;

					Runtime.getRuntime().gc();

					continue;
				} else {
					JOptionPane.showMessageDialog(null, "BAaaAD Formula\n"
							+ this.pxFuncChoice, "IncorrectFormula",
							JOptionPane.ERROR_MESSAGE);

					return;
				}
			}
			
			this.setFractalBase(julies[i]);

			this.setFractalImage(julies[i].getBufferedImage());

			String imgBaseInfo = this.getImgBaseInfo();
			BufferedImage dataInfoImg = this.createStringImage(imgBaseInfo);

			String imageFilePath = subDir+File.separator/*+"images_gen\\" + subDirName + "\\"*/ + "[" + extraInfo + "]_" + System.currentTimeMillis() + ".png";
			File outputfile = new File(imageFilePath);

			String imageDetailFilePath = subDir+File.separator/*+"images_gen\\" + subDirName + "\\"*/ + "\\Detail\\" + "[" + extraInfo + "]_Detail_" + System.currentTimeMillis() + ".png";
			File outputDetailfile = new File(imageDetailFilePath);

			try {
				ImageIO.write(julies[i].getBufferedImage(), "png", outputfile);
				System.out.println("Generated File: " + imageFilePath);

				final BufferedImage joinedFractalDataImage = FractalBase.joinBufferedImages(julies[i].getBufferedImage(), dataInfoImg);
				ImageIO.write(joinedFractalDataImage, "png", outputDetailfile);
				System.out.println("Generated FileDetail: " + imageDetailFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (OutOfMemoryError oome) {
				oome.printStackTrace();
				if (!hasOutOfMemoryError) {
					hasOutOfMemoryError = true;
				} else {
					System.out.println("Error --- OuttaMemory ");
					System.exit(1);
				}
			}

			this.formulaArea.setText("");

			g.dispose();
			julies[i].dispose();
			julies[i] = null;
			
			Runtime.getRuntime().gc();//System.gc();
		}
		

		System.out.println("Done JuliaGeneration");
		JOptionPane.showMessageDialog(null, "Dir created: "+subDirName, "Julia Generated", JOptionPane.INFORMATION_MESSAGE);
		
		return;

	}
	
	
	
	private Properties correctProperties(Properties props) {
		for (Object key : props.keySet()) {
			Object val = props.get(key);

			if (val == null) {
				throw new IllegalArgumentException("Error, null value for key: "+key);
			} else if (!(val instanceof String)) {
				if (val instanceof Boolean) {
					props.put(key, String.valueOf(val));
				}
			}

		}
		return props;
	}

	private List<String> getTrueFalseList(){
		return asList( new String[]{"true","false"});
	}
	
	private synchronized Properties[] getAllCombinationProperties(List<?> comboList) {
		boolean varyConst = false;
		if (this.diyJuliaGen) {
			varyConst = this.diyJuliaVaryConstant;
		} else if (this.diyMandGen) {
			varyConst = this.diyMandVaryConstant;
		}
		boolean varyKeepConst = this.keepConst && !varyConst;
		
		Properties[] ps = new Properties[comboList.size()];
		//SampleMix, EQUAL_PARTS_25, FRST_SIX_FIBS, none, none, Plus, None, None, Plus, 3, none, none, 4.0, 2.75, 500, DynamicConst, DynamicConst
		//ComputeColor, none, none, Plus, None, None, Plus, 8, none, none, 4.0, 5.0, 500, DynamicConst, DynamicConst
/*final List<?> allCombinationsList = this.product(
 * 				varyColorList,varyPixXTrList,varyPixYTrList,varyIntraPixXYOpList,
				varyZFuncList,varyConstCFuncList,varyPixelConstOpZCList,
				varyPowerList,varyPixXCenterList,varyPixYCenterList,
				varyScaleSizeList,varyBoundaryList,varyIterList,
				varyUseDiffList, varyInvertPixelList,
				varyConstList);
		


		this.setZSq(Boolean.parseBoolean(p.getProperty("isZSq")));
		this.setFatou(Boolean.parseBoolean(p.getProperty("isFatou")));
		this.setClassicJulia(Boolean.parseBoolean(p.getProperty("isClassicJulia")));

		this.setUseDiff(Boolean.parseBoolean(p.getProperty("useDiff")));
		this.setComplexNumConst(Boolean.parseBoolean(p.getProperty("isComplexNumConst")));
		this.setComplex(Double.parseDouble(p.getProperty("constReal")),Double.parseDouble(p.getProperty("constImag")));				*/
		for (int i = 0; i < comboList.size(); i++) {
			ps[i] = new Properties();
			final String line = comboList.get(i).toString();
			/*System.out.println("i is "+i);
			System.out.println("line is "+line);*/
			String[] iStr = new StringBuilder(line).toString()
					.replaceAll(OPEN_BRACK, EMPTY)
					.replaceAll(CLOSE_BRACK, EMPTY)
					.replaceAll(BACK_SLASH, EMPTY)	//TODO	maybeRemove
					.split(COMMA);//\\[\\]
			
			for(String prop : iStr) {		//startsWith replaced with java.lang.String.contains() implementation
				if (prop.indexOf("colorChoice=") > -1) {
					ps[i].setProperty("colorChoice",prop.replace("colorChoice=",EMPTY));	// 		prop.substring("colorChoice=".length()));
				} else if (prop.indexOf("startVals=") > -1) {
					ps[i].setProperty("startVals",prop.replace("startVals=",EMPTY));		
				} else if (prop.indexOf("divVals=") > -1) {
					ps[i].setProperty("divVals",prop.replace("divVals=",EMPTY));	
				} else if (prop.indexOf("pixXTransform=") > -1) {
					ps[i].setProperty("pxXTransformation", prop.replace("pixXTransform=",EMPTY));
				} else if (prop.indexOf("pixYTransform=") > -1) {
					ps[i].setProperty("pxYTransformation", prop.replace("pixYTransform=",EMPTY));
				} else if (prop.indexOf("pixIntraXYOperation=") > -1) {
					ps[i].setProperty("pixXYOperation", prop.replace("pixIntraXYOperation=",EMPTY));
				} else if (prop.indexOf("pxFuncChoice=") > -1) {
					ps[i].setProperty("useFuncPixel", prop.replace("pxFuncChoice=",EMPTY));
				} else if (prop.indexOf("constFuncChoice=") > -1) {
					ps[i].setProperty("useFuncConst", prop.replace("constFuncChoice=",EMPTY));
				} else if (prop.indexOf("pxConstOprnChoice=") > -1) {
					ps[i].setProperty("pxConstOperation", prop.replace("pxConstOprnChoice=",EMPTY));
				} else if (prop.indexOf("power=") > -1) {
					ps[i].setProperty("power", prop.replace("power=",EMPTY));
				} else if (prop.indexOf("xCentrChoice=") > -1) {
					ps[i].setProperty("xC", prop.replace("xCentrChoice=",EMPTY));
				} else if (prop.indexOf("yCentrChoice=") > -1) {
					ps[i].setProperty("yC", prop.replace("yCentrChoice=",EMPTY));
				} else if (prop.indexOf("scaleSizeChoice=") > -1) {
					ps[i].setProperty("scaleSize", prop.replace("scaleSizeChoice=",EMPTY));
				} else if (prop.indexOf("boundary=") > -1) {
					ps[i].setProperty("bound", prop.replace("boundary=",EMPTY));
				} else if (prop.indexOf("maxIter=") > -1) {
					ps[i].setProperty("maxIter", prop.replace("maxIter=",EMPTY));
				} else if (prop.indexOf("useDiffChoice=") > -1) {
					ps[i].setProperty("useDiff", prop.replace("useDiffChoice=",EMPTY));
				} else if (prop.indexOf("invertPixelCalcChoice=") > -1) {
					ps[i].setProperty("reversePixelCalculation", prop.replace("invertPixelCalcChoice=",EMPTY));
				} else if (prop.indexOf("constantChoice=realConstChoice=") > -1) {
					String realConstChoice = prop.replace("constantChoice=realConstChoice=",EMPTY);

					if (realConstChoice.contains("DynamicConst")) {
						ps[i].setProperty("isComplexNumConst", "true");
					} else {
						ps[i].setProperty("isComplexNumConst", "false");
						ps[i].setProperty("constReal", realConstChoice);
					}

				} else if (prop.indexOf("imagConstChoice=") > -1) {
					String imagConstChoice = prop.replace("imagConstChoice=",EMPTY);

					if (imagConstChoice.contains("DynamicConst")) {
						ps[i].setProperty("isComplexNumConst", "true");
					} else {
						ps[i].setProperty("isComplexNumConst", "false");
						ps[i].setProperty("constImag", imagConstChoice);
					}
				} else if (prop.indexOf("fieldType=") > -1) {
					ps[i].setProperty("fieldType",prop.replace("fieldType=",EMPTY));		
				}
				
				if (prop.indexOf("rowColumnMixType=") > -1) {
					ps[i].setProperty("rowColumnMixType",prop.replace("rowColumnMixType=",EMPTY));	// 		prop.substring("colorChoice=".length()));
				} 
			}

			if (this.diyMandGen) {
				ps[i].setProperty("magnificationChoice", String.valueOf(this.diyMandMagnification));
			}
			
			
			if (this.diyJuliaGen) {
				ps[i].setProperty("applyCustomFormula", String.valueOf(this.diyJApplyFormulaZ));
				ps[i].setProperty("areaSize",String.valueOf(this.juliaSize));
			} else if (this.diyMandGen) {
				ps[i].setProperty("applyCustomFormula", String.valueOf(this.diyMandApplyFormulaZ));
				ps[i].setProperty("areaSize",String.valueOf(this.mandSize));
			} else if (this.polyGen) {
				ps[i].setProperty("applyCustomFormula", String.valueOf(this.polyApplyFormulaZ));
				ps[i].setProperty("areaSize",String.valueOf(this.polySize));
			}
			/*ps[i].put("isZSq", String.valueOf(this.applyZSq));
			ps[i].put("isFatou", String.valueOf(this.applyFatou));
			ps[i].put("isClassicJulia", String.valueOf(this.applyClassicJulia));*/
			
			ps[i].setProperty("rotation",String.valueOf(this.rotation));

			boolean doSmoothen = this.smoothenColor;
			if(doSmoothen) {
				ps[i].setProperty("smoothen","true");
			} else {
				ps[i].setProperty("smoothen","false");
			}
			
			boolean capOrb = this.captureOrbit;
			if (capOrb) {
				ps[i].setProperty("captureOrbit", "true");

				ps[i].setProperty("orbitTrapPoint", this.orbitTrapPointChoice.toString());
				ps[i].setProperty("orbitTrapSize", String.valueOf(this.trapSizeChoice));
				ps[i].setProperty("orbitTrapShape", String.valueOf(this.trapShapeChoice));
			} else {
				ps[i].setProperty("captureOrbit", "false");
			}
			
		}
			
		return ps;
			
////////////////////////nxtIndex-removd/////////////////////////////			
/////////////////////////////////////////////////////////////////////////			
//			
//			/*String[] iStr = this.cleanString(comboList.get(i).toString());*/
//			int nxtIndex = 0;
//System.out.println("iStr[0] is "+ iStr[0]);
//ps[i].put("colorChoice", iStr[0]);
//
//
////			final boolean isSampleMixColor = iStr[0].contains("SampleMix");		//	\SampleMix
////			String colorChoice = null;
////			if (isSampleMixColor) {
//////				String[] colorChoices = new String[]{iStr[0],iStr[1],iStr[2]};			//OPEN_BRACK + iStr[0] + COMMA + iStr[1] + COMMA + iStr[2] + CLOSE_BRACK;
////				colorChoice = iStr[0]+PIPE+iStr[1]+iStr[2];
////				ps[i].put("colorChoice", colorChoice);
////				nxtIndex = 3;
////			} else {
////				colorChoice = iStr[0];
////				ps[i].put("colorChoice", colorChoice);
////				nxtIndex = 1;
////			}
//
//
//			ps[i].put("pxXTransformation", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("pxYTransformation", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("pixXYOperation", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("useFuncPixel", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("useFuncConst", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("pxConstOperation", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("power", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("xC", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("yC", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("scaleSize", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("bound", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("maxIter", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("useDiff", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			ps[i].put("reversePixelCalculation", iStr[nxtIndex]);
//			nxtIndex += 1;
//
//			if (iStr[nxtIndex].contains("DynamicConst")) {
//				ps[i].put("isComplexNumConst", "true");
//			} else {
//				ps[i].put("isComplexNumConst", "false");
//				ps[i].put("constReal", iStr[nxtIndex]);
//				nxtIndex += 1;
//				ps[i].put("constImag", iStr[nxtIndex]);
//			}
//			
//			ps[i].put("applyCustomFormula",diyJApplyFormulaZ);
//			
//			ps[i].put("isZSq", this.applyZSq);
//			ps[i].put("isFatou", this.applyFatou);
//			ps[i].put("isClassicJulia", this.applyClassicJulia);
//			
//			ps[i].put("areaSize",this.juliaSize);
//			ps[i].put("rotation",this.rotation);
//		}
//		
//		return ps;
////////////////////////nxtIndex-removd/////////////////////////////
	}

	/**
	 * Returns a cleaned string array
	 * [[[[[[[[[[[[[ColorPalette, absoluteSquare], reciprocalSquare], Plus], None], None], Plus], 3], absoluteSquare], reciprocalSquare], 1.0], 5.0], 1000], [-1.1, -1.1]]
	 * to
	 * ColorPalette absoluteSquare reciprocalSquare Plus None None Plus 3 absoluteSquare reciprocalSquare 1.0 5.0 1000 -1.1 -1.1
	 * as an String[]
	 * @param rawString	=	raw	string
	 * @return	clean string[]
	 */
	private String[] cleanString(final String rawString) {
		for(int i = 0; i < rawString.length();i++){
			
		}
		return null;
	}
	
	private String charRemoveAt(String aStr, int index) {
		return aStr.substring(0,index)+aStr.substring(index+1);
	}
/////////////////////////////////////////////////////////////////////	
/////////////////////////////////////////////////////////////////////
//	private void doJuliaGenerateCommand_0() {
//		
//		if (this.diyJuliaVaryConstant) {
//			System.out.println("in_doJuliaGenerateCommand_this.diyJuliaVaryConstant=true");
//			try {
//				this.diyJuliaGenRealFromVal = Double.parseDouble(this.diyJuliaGenRealFromTf.getText());
//				this.diyJuliaGenImagFromVal = Double.parseDouble(this.diyJuliaGenImagFromTf.getText());
//				this.diyJuliaGenRealToVal = Double.parseDouble(this.diyJuliaGenRealToTf.getText());
//				this.diyJuliaGenImagToVal = Double.parseDouble(this.diyJuliaGenImagToTf.getText());
//				
//				if(this.diyJuliaGenRealFromVal>this.diyJuliaGenRealToVal||this.diyJuliaGenImagFromVal>this.diyJuliaGenImagToVal){
//					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range",
//							JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//				
//				
//			} catch (NumberFormatException | NullPointerException e2) {
//				e2.printStackTrace();
//				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//			} 
//		}
//		
//		boolean varyConst = this.diyJuliaVaryConstant;
//		boolean varyKeepConst = this.keepConst && !varyConst;
//		
//		if(!varyConst && !varyKeepConst) {
//			try {
//				this.diyJuliaConstReal = Double.parseDouble(this.diyJuliaRealTf.getText());
//				this.diyJuliaConstImg = Double.parseDouble(this.diyJuliaImgTf.getText());
//			} catch (NumberFormatException | NullPointerException e2) {
//				e2.printStackTrace();
//				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//			}
//		}
//		
//		if (this.diyJuliaVaryBoundary) {
//			System.out.println("in_doJuliaGenerateCommand_this.diyJuliaVaryBoundary=true");
//			try {
//				this.diyJuliaVaryBoundaryFromVal = Double.parseDouble(this.diyJuliaGenBoundaryFromTf.getText());
//				this.diyJuliaVaryBoundaryToVal = Double.parseDouble(this.diyJuliaGenBoundaryToTf.getText());
//				
//				if (this.diyJuliaVaryBoundaryFromVal > this.diyJuliaVaryBoundaryToVal) {
//					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range",
//							JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//			} catch (NumberFormatException | NullPointerException e2) {
//				e2.printStackTrace();
//				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//			} 
//		}
//		
//		if (this.diyJuliaVaryScaleSize) {
//			System.out.println("in_doJuliaGenerateCommand_this.diyJuliaVaryScaleSize=true");
//			try {
//				this.diyJuliaVaryScaleSizeFromVal = Double.parseDouble(this.diyJuliaGenScaleSizeFromTf.getText());
//				this.diyJuliaVaryScaleSizeToVal = Double.parseDouble(this.diyJuliaGenScaleSizeToTf.getText());
//				if(this.diyJuliaVaryScaleSizeFromVal>this.diyJuliaVaryScaleSizeToVal){
//					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range",
//							JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//			} catch (NumberFormatException | NullPointerException e2) {
//				e2.printStackTrace();
//				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//			} 
//		}
//
//		if (this.diyJuliaVaryPixelPowerZ) {
//			System.out.println("in_doJuliaGenerateCommand_this.diyJuliaVaryPixelPowerZ=true");
//			try {
//				this.diyJuliaVaryPixelPowerZFromVal = Integer.parseInt(this.diyJuliaGenPixelPowerZFromTf.getText());
//				this.diyJuliaVaryPixelPowerZToVal = Integer.parseInt(this.diyJuliaGenPixelPowerZToTf.getText());
//				if(this.diyJuliaVaryPixelPowerZFromVal>this.diyJuliaVaryPixelPowerZToVal){
//					JOptionPane.showMessageDialog(null, "From > To ???", "Please enter a valid range",
//							JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//			} catch (NumberFormatException | NullPointerException e2) {
//				e2.printStackTrace();
//				JOptionPane.showMessageDialog(null, "Please enter a valid Integer\n"+e2.getMessage(), "Error - Not an Integer",
//						JOptionPane.ERROR_MESSAGE);
//				return;
//			} 
//		}
//		
//
//		
//
//		String diyJApplyField = this.fieldLines;
//		boolean diyJApplyFatou = this.applyFatou;
//		boolean diyJApplyZSq = this.applyZSq;
//		boolean diyJApplyClassic = this.applyClassicJulia;
//		boolean diyJApplyLyapunovExponent = this.diyJuliaUseLyapunovExponent;
//		
//		final int varyColorCount = this.getVaryJuliaColorCount();
//		final int varyPixXTransCount = this.getVaryJuliaPixXTransCount();
//		final int varyPixYTransCount = this.getVaryJuliaPixYTransCount();
//		final int varyIntraPixXYOpCount = this.getVaryJuliaIntraPixXYOpCount();
//		final int varyZFuncCount = this.getVaryJuliaZFuncCount();
//		final int varyConstCFuncCount = this.getVaryJuliaConstCFuncCount();
//		final int varyPixelConstOpZCCount = this.getVaryJuliaPixelConstOpZCCount();
//		final int varyPowerCount = this.getVaryJuliaPowerCount();
//		final int varyPixXCenterCount = this.getVaryJuliaPixXCenterCount();
//		final int varyPixYCenterCount = this.getVaryJuliaPixYCenterCount();
//		
//		final int varyIterCount = this.getVaryJuliaIterCount();
//		
//		final int powerFrom = this.diyJuliaVaryPixelPowerZFromVal;
//		final int powerTo = this.diyJuliaVaryPixelPowerZToVal;
//		final int powerJump = this.diyJuliaVaryPixelPowerZJumpVal;
//
//		
//		final double scaleSizeFrom = this.diyJuliaVaryScaleSizeFromVal;
//		final double scaleSizeTo = this.diyJuliaVaryScaleSizeToVal;
//		final double scaleSizeJump = this.diyJuliaVaryScaleSizeJumpVal;
//		final int varyScaleSizeCount = this.getVaryJuliaScaleSizeCount();
//		
//		final double boundaryFrom = this.diyJuliaVaryBoundaryFromVal;
//		final double boundaryTo = this.diyJuliaVaryBoundaryToVal;
//		final double boundaryJump = this.diyJuliaVaryBoundaryJumpVal;
//		final int varyBoundaryCount = this.getVaryJuliaBoundaryCount();
//		
//		final double jRealFrom = this.diyJuliaGenRealFromVal;
//		final double jImagFrom = this.diyJuliaGenImagFromVal;
//		final double jRealTO = this.diyJuliaGenRealToVal;
//		final double jImagTO = this.diyJuliaGenImagToVal;		
//		final double realJumpVal = this.diyJuliaGenRealJumpVal;
//		final double imagJumpVal = this.diyJuliaGenImagJumpVal;
//		final int varyRealConstantCount = this.getVaryJuliaRealConstantCount();
//		final int varyImagConstantCount = this.getVaryJuliaImagConstantCount();
//		
//
////		System.out.println("getTotalVaryCount=" + this.getTotalVaryCount());
////		System.out.println("getTotalVaryCount=HARDED-2-To5== " + 5);
////		int totalVaryCount = this.getTotalVaryCount();//5;
//		
//		int totalVaryCount = varyColorCount * varyPixXTransCount * varyPixYTransCount * varyIntraPixXYOpCount * 
//				varyZFuncCount * varyConstCFuncCount * varyPixelConstOpZCCount  * 
//				varyPowerCount  * varyPixXCenterCount * varyPixYCenterCount  * 
//				varyScaleSizeCount * varyBoundaryCount  * varyIterCount * 
//				varyRealConstantCount * varyImagConstantCount;
//				
//		System.out.println("getTotalVaryCount=" + totalVaryCount);
//
//		if (totalVaryCount > 150) {
//			int ans = JOptionPane.showConfirmDialog(null, "Are You Sure needs to generate\n"+totalVaryCount+"images", "Are_You_Sure",
//					JOptionPane.YES_NO_CANCEL_OPTION);
//			if(ans!=JOptionPane.YES_OPTION){
//				return;
//			}
//		}
//
//		
//		/*final*/ String subDirName;
//		/*if(!varyConst && !varyKeepConst) {
//			subDirName = "Julia_{(R)[" + String.format("%.2f", this.diyJuliaGenRealFromVal) + "_to_"
//					+ String.format("%.2f", this.diyJuliaGenRealToVal) + "],(I)["
//					+ String.format("%.2f", this.diyJuliaGenImagFromVal) + "_to_"
//					+ String.format("%.2f", this.diyJuliaGenImagToVal) + "]" + System.currentTimeMillis() + "}";
//		} else {
//			subDirName = "Julia_" + System.currentTimeMillis();
//		}*/
//
//		subDirName = JULIA + "__Vary[";
//		
//		if (this.diyJuliaVaryColor)					subDirName += "Colr,";
//		if (this.diyJuliaVaryPixXTran)				subDirName += "F(x),";
//		if (this.diyJuliaVaryPixYTran)				subDirName += "F(y),";
//		if (this.diyJuliaVaryIntraPixXY)			subDirName += "I(xy),";
//		if (this.diyJuliaVaryPixelZFunc)			subDirName += "F(Z),";
//		if (this.diyJApplyFormulaZ)					subDirName += "CustFormula,";
//		if (this.diyJuliaVaryConstant)				subDirName += "Const,";
//		if (this.diyJuliaVaryConstCFunc)			subDirName += "F(C),";
//		if (this.diyJuliaVaryPixelConstOpZC)		subDirName += "O(ZC),";
//		if (this.diyJuliaVaryPixelPowerZ)			subDirName += "X(Z),";
//		if (this.diyJuliaVaryIter)					subDirName += "M(It),";
//		if (this.diyJuliaVaryPixXCentr)				subDirName += "Cx,";
//		if (this.diyJuliaVaryPixYCentr)				subDirName += "Cy,";
//		if (this.diyJuliaVaryBoundary)				subDirName += "Bd,";
//		if (this.diyJuliaVaryScaleSize)				subDirName += "Sz,";
//		
//		subDirName = subDirName.substring(0, subDirName.length() - 1) + "]_" + System.currentTimeMillis();
//		
//		/*subDirName += "DIY_"+JULIA+"_";
//		subDirName += "P("+this.diyJuliaPower+"),";
//		
//		subDirName += "B(" + this.diyJuliaBound + "),";
//		subDirName += "MxIt(" + this.diyJuliaMaxIter + "),";
//		subDirName += "Cx(" + this.diyJuliaXC + "),";
//		subDirName += "Cy(" + this.diyJuliaYC + "),";
//		subDirName += "Sz(" + this.diyJuliaScaleSize + "),";
//		if (this.keepConst ) {
//			subDirName += "CONST";
//		}else{
//				subDirName += "Real(" + String.format("%.2f", this.diyJuliaConstReal) + "),";
//				subDirName += "Img(" + String.format("%.2f", this.diyJuliaConstImg) + ")";
//		}*/
//		
//		File subDir = new File("images_gen\\"+subDirName);
//		if (!subDir.exists()) {
//			subDir.mkdir();
//		}
//		
//		File subDirDetail = new File(subDir,"Detail");
//		if (!subDirDetail.exists()) {
//			subDirDetail.mkdir();
//		}
//		
//		Julia[] julies = new Julia[totalVaryCount];
//		
//		for (int i = 0; i < totalVaryCount; i++) {
//			// initialize
//			julies[i] = new Julia();
//			julies[i].setVisible(false); // make invisible
//
//			this.setColors(julies[i], totalVaryCount, i, varyColorCount);
//			this.setUseDiff_InvertPixels(julies[i], totalVaryCount, i);
//
//			String pixXTr = this.setPxXTransformations(julies[i], totalVaryCount, i, varyPixXTransCount);
//			String pixYTr = this.setPxYTransformations(julies[i], totalVaryCount, i, varyPixYTransCount);
//			String pixIntraXYOp = this.setPixXYOperations(julies[i], totalVaryCount, i, varyIntraPixXYOpCount);
//			String pxFunc = this.setUseFuncPixels(julies[i], totalVaryCount, i, varyZFuncCount);
//			this.setUseConstFunctions(julies[i], totalVaryCount, i, varyConstCFuncCount);
//			String pixConstOp = this.setPxConstOperations(julies[i], totalVaryCount, i, varyPixelConstOpZCCount);
//
//			if (varyConst) {
//				double realVal = this.setRealConstant(totalVaryCount, i, jRealFrom, jRealTO, realJumpVal, varyRealConstantCount);
//				double imagVal = this.setImagConstant(totalVaryCount, i, jImagFrom, jImagTO, imagJumpVal, varyImagConstantCount);
//				julies[i].setComplex(realVal, imagVal);
//				julies[i].setComplexNumConst(false);
//
//				this.diyJuliaConstReal = realVal;
//				this.diyJuliaConstImg = imagVal;
//			} else if (varyKeepConst) {
//				julies[i].setComplexNumConst(true);
//				this.keepConst=true;
//			} else {
//				// grabit_4__GenerateConstant
//				julies[i].setComplex(this.diyJuliaConstReal, this.diyJuliaConstImg);
//				julies[i].setComplexNumConst(false);
//			}
//
//			int diyJuliaP = this.setPixelPowerZ(julies[i], totalVaryCount, i, powerFrom, powerTo, powerJump, varyPowerCount);
//			
//			this.setLoopLimits(julies[i], totalVaryCount, i);
//
//			julies[i].setRotation(this.rotation);
//
//			if (!diyJApplyField.equals("None")) {
//				if (diyJApplyFatou) {
//					julies[i].setFatou(diyJApplyFatou);
//					julies[i].setZSq(false);
//					julies[i].setClassicJulia(false);
//				} else if (diyJApplyZSq) {
//					julies[i].setZSq(diyJApplyZSq);
//					julies[i].setFatou(false);
//					julies[i].setClassicJulia(false);
//				} else if (diyJApplyClassic) {
//					julies[i].setClassicJulia(diyJApplyClassic);
//					julies[i].setFatou(false);
//					julies[i].setZSq(false);
//				}
//			} else {
//				julies[i].setFatou(false);
//				julies[i].setZSq(false);
//				julies[i].setClassicJulia(false);
//
//			}
//
//			julies[i].setUseLyapunovExponent(this.diyJuliaUseLyapunovExponent);
//			julies[i].setAreaSize(this.juliaSize);
//
//			this.setXCs(julies[i], totalVaryCount, i, varyPixXCenterCount);
//			this.setYCs(julies[i], totalVaryCount, i, varyPixYCenterCount);
//
//			this.setScaleSizes(julies[i], totalVaryCount, i, scaleSizeFrom, scaleSizeTo, scaleSizeJump, varyScaleSizeCount);
//			this.setBoundarys(julies[i], totalVaryCount, i, boundaryFrom, boundaryTo, boundaryJump, varyBoundaryCount);
//			
//			///////	reassign 2 UI -	4 formulaArea
//			this.constFuncChoice = julies[i].getUseFuncConst();
//
//			// 4_getExtraInfo
//			this.diyJuliaPower = julies[i].getPower();
//			this.diyJuliaBound = julies[i].getBound();
//			this.diyJuliaMaxIter = julies[i].getMaxIter();
//			this.diyJuliaXC = julies[i].getxC();
//			this.diyJuliaYC = julies[i].getyC();
//			this.diyJuliaScaleSize = julies[i].getScaleSize();
//
//			// 4_img_Base_Info
//			this.colorChoice = julies[i].getColorChoice();
//			if (this.colorChoice.equals("SampleMix")) {
//				this.setSampleColorMix(julies[i]);
//			}
//			
//			String extraInfo = this.getExtraInfo();
//			
//			this.setDiyJuliaGenFormulaArea(pxFunc, diyJuliaP, diyJApplyFatou, diyJApplyZSq, diyJApplyClassic, pixXTr, pixYTr, pixIntraXYOp,  pixConstOp);			
//			this.formulaArea.append("<br/>Constant = " + this.diyJuliaConstReal + " + (" + this.diyJuliaConstImg + " * i)</font>" + eol);
////			
//			this.addJuliaConstInfo(julies[i]);
//
//			julies[i].reset();
//			
//			
//			///done1---now-to-imaging
//			
//			Graphics2D g = julies[i].getBufferedImage().createGraphics();
//			julies[i].paint(g);
//
//			this.setFractalImage(julies[i].getBufferedImage());
//			
//			String imgBaseInfo = this.getImgBaseInfo();
//			BufferedImage dataInfoImg = this.createStringImage(imgBaseInfo);
//			
//			String imageFilePath = "images_gen\\" + subDirName + "\\" + "[" + extraInfo + "]_" + System.currentTimeMillis() + ".png";
//			File outputfile = new File(imageFilePath);
//			
//			String imageDetailFilePath = "images_gen\\" + subDirName + "\\Detail\\" + "[" + extraInfo + "]_Detail_" + System.currentTimeMillis() + ".png";
//			File outputDetailfile = new File(imageDetailFilePath);
//
//			try {
//				ImageIO.write(julies[i].getBufferedImage(), "png", outputfile);
//				System.out.println("Generated File: "+imageFilePath);
//				
//				final BufferedImage joinedFractalDataImage = FractalBase.joinBufferedImage(julies[i].getBufferedImage(), dataInfoImg);
//				ImageIO.write(joinedFractalDataImage, "png", outputDetailfile);
//				System.out.println("Generated FileDetail: "+imageDetailFilePath);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			this.formulaArea.setText("");
//
//			g.dispose();
//			julies[i].dispose();
//			julies[i]=null;
//
//		}
//
//		System.out.println("Done JuliaGeneration");
//		JOptionPane.showMessageDialog(null, "Dir created: "+subDirName, "Julia Generated", JOptionPane.INFORMATION_MESSAGE);
//		
////		
////		///////////////////////removin4now///////////////////////////////
////		FractalBase ff;
////		
////		boolean useCP = this.colorChoice.equals("ColorPalette");
////		boolean useBw = this.colorChoice.equals("BlackWhite");	
////		
////		boolean useSample = this.colorChoice.equals("SampleMix");
////		
////		String pxConstOp = this.pxConstOprnChoice;
////		String func = this.constFuncChoice;
////		String pxFunc = this.pxFuncChoice;
////		double rot = this.getRotation();
////		int diyJuliaLoopLt = this.juliaSize;
////		int diyJuliaP = this.getDiyJuliaPower();
////		boolean diyJuliaUseD = this.getDiyJuliaUseDiff();
////		int diyJuliaMaxIt = this.diyJuliaMaxIter;
////		double diyJuliaBd = this.diyJuliaBound;
////		double diyJXc = this.diyJuliaXC;
////		double diyJYc = this.diyJuliaYC;
////		double diyJScale = this.diyJuliaScaleSize;
////
////		String diyJApplyField = this.fieldLines;
////		boolean diyJApplyFatou = this.applyFatou;
////		boolean diyJApplyZSq = this.applyZSq;
////		boolean diyJApplyClassic = this.applyClassicJulia;
////		
////		
////		double jReal = this.diyJuliaGenRealFromVal;
////		double jImag = this.diyJuliaGenImagFromVal;
////		final double jRealTO = this.diyJuliaGenRealToVal;
////		final double jImagTO = this.diyJuliaGenImagToVal;
////		
////		final double realJumpVal = this.diyJuliaGenRealJumpVal;
////		final double imagJumpVal = this.diyJuliaGenImagJumpVal;
////		
////		final int realJumpCount = this.getJumpCount(jReal,jRealTO,realJumpVal);
////		final int imagJumpCount = this.getJumpCount(jImag,jImagTO,imagJumpVal);
////		
////		System.out.println("RealJumpCount == "+realJumpCount);
////		System.out.println("ImagJumpCount == "+imagJumpCount);
////		System.out.println("Total = "+(realJumpCount*imagJumpCount));
////
////		//////////////////////////////////////////st
////		double real = jReal;
////		double imag = jImag;
////		
////		final String subDirName = "Julia_{(R)["+String.format("%.2f", this.diyJuliaGenRealFromVal)+"_to_"+String.format("%.2f", this.diyJuliaGenRealToVal)+"],(I)["+String.format("%.2f", this.diyJuliaGenImagFromVal)+"_to_"+String.format("%.2f", this.diyJuliaGenImagToVal)+"]"+System.currentTimeMillis()+"}";
////		File subDir = new File("images_gen\\"+subDirName);
////		if (!subDir.exists()) {
////			subDir.mkdir();
////		}
////		
////		File subDirDetail = new File(subDir,"Detail");
////		if (!subDirDetail.exists()) {
////			subDirDetail.mkdir();
////		}
////		
////		
////		
////		for (int r = 0; r < realJumpCount; r++) {
////			for (int i = 0; i < imagJumpCount; i++) {
//////		for (double real = jReal; real <= jRealTO; real += realJumpVal) {
//////			for (double imag = jImag; imag <= jImagTO; imag += imagJumpVal) {
////				this.diyJuliaConstReal = real;
////				this.diyJuliaConstImg = imag;
////				
////				this.setDiyJuliaGenFormulaArea(pxFunc, diyJuliaP, diyJApplyFatou, diyJApplyZSq, diyJApplyClassic);		
////		
////				this.formulaArea.append("<br/>Constant = " + this.diyJuliaConstReal + " + (" + this.diyJuliaConstImg + " * i)</font>" + eol);
////				
////				ff = new Julia(diyJuliaP, diyJuliaUseD, diyJuliaBd, real, imag);
////
////				ff.setUseLyapunovExponent(this.diyJuliaUseLyapunovExponent);
////				ff.setPxXTransformation(this.pixXTransform);
////				ff.setPxYTransformation(this.pixYTransform);
////				ff.setPixXYOperation(this.pixIntraXYOperation);
////				ff.setReversePixelCalculation(this.invertPixelCalculation);
////				if (useCP) {
////					ff.setUseColorPalette(useCP);
////				} else {
////					if (useBw) {
////						ff.setUseBlackWhite(useBw);
////					} else {
////						if (useSample) {
////
////							this.setSampleColorMix(ff);
////						} else {
////
////							ff.setUseColorPalette(useCP);
////						}
////					}
////				}
////				ff.setPxConstOperation(pxConstOp);
////				ff.setUseFuncConst(func);
////				ff.setUseFuncPixel(pxFunc);
////				ff.setRotation(rot);
////				if (!diyJApplyField.equals("None")) {
////					if (diyJApplyFatou) {
////						ff.setFatou(diyJApplyFatou);
////						ff.setZSq(false);
////						ff.setClassicJulia(false);
////					} else if (diyJApplyZSq) {
////						ff.setZSq(diyJApplyZSq);
////						ff.setFatou(false);
////						ff.setClassicJulia(false);
////					} else if (diyJApplyClassic) {
////						ff.setClassicJulia(diyJApplyClassic);
////						ff.setFatou(false);
////						ff.setZSq(false);
////					}
////				} else {
////					ff.setFatou(false);
////					ff.setZSq(false);
////					ff.setClassicJulia(false);
////
////				}
////				ff.reset();
////				FractalBase.setMaxIter(diyJuliaMaxIt);
////				FractalBase.setAreaSize(diyJuliaLoopLt);
////				FractalBase.setxC(diyJXc);
////				FractalBase.setxC(diyJYc);
////				FractalBase.setScaleSize(diyJScale);
////				
////
////				this.addJuliaConstInfo(ff);
////				// --endDoStart
////
////				ff.setVisible(false);
////				
////				Graphics2D g = ff.getBufferedImage().createGraphics();
////				ff.paint(g);
////
////				this.setFractalImage(ff.getBufferedImage());
////
////				String extraInfo = this.getExtraInfo();
////				String imgBaseInfo = this.getImgBaseInfo();
////				BufferedImage dataInfoImg = this.createStringImage(imgBaseInfo);
////				
////				/*final String subDirName = "Julia_{(R)["+String.format("%.2f", this.diyJuliaGenRealFromVal)+"_to_"+String.format("%.2f", this.diyJuliaGenRealToVal)+"],(I)["+String.format("%.2f", this.diyJuliaGenImagFromVal)+"_to_"+String.format("%.2f", this.diyJuliaGenImagToVal)+"]"+System.currentTimeMillis()+"}";
////				File subDir = new File("images_gen\\"+subDirName);
////				if (!subDir.exists()) {
////					subDir.mkdir();
////				}*/
////				
////				String imageFilePath = "images_gen\\" + subDirName + "\\" /*+ this.getComboChoice()*/ + "[" + extraInfo + "]_" + System.currentTimeMillis() + ".png";
////				File outputfile = new File(imageFilePath);
////				
////				String imageDetailFilePath = "images_gen\\" + subDirName + "\\Detail\\" /*+ this.getComboChoice()*/ + "[" + extraInfo + "]_Detail_" + System.currentTimeMillis() + ".png";
////				File outputDetailfile = new File(imageDetailFilePath);
////
////				try {
////					ImageIO.write(ff.getBufferedImage(), "png", outputfile);
////					System.out.println("Generated File: "+imageFilePath);
////					
////					final BufferedImage joinedFractalDataImage = FractalBase.joinBufferedImage(ff.getBufferedImage(), dataInfoImg);
////					ImageIO.write(joinedFractalDataImage, "png", outputDetailfile);
////					System.out.println("Generated FileDetail: "+imageDetailFilePath);
////				} catch (IOException e) {
////					e.printStackTrace();
////				}
////				
////				this.formulaArea.setText("");
////
////				g.dispose();
////				ff.dispose();
////
////				/*//to ensure endImagLimit is called
////				if (imag + imagJumpVal > jImagTO && !imagToReached) {
////					imag = jImagTO - imagJumpVal;
////					imagToReached = true;
////				}*/
////				imag += imagJumpVal;
////
////			} // ends forImag
////			
////			/*//to ensure endRealLimit is called
////			if (real + realJumpVal > jRealTO && !realToReached) {
////				real = jRealTO - realJumpVal;
////				realToReached = true;
////			}*/
////			
////			real += realJumpVal;
////		}	//	ends forReal
////
////		ff = null;
////
////		System.out.println("Done JuliaGeneration");
////		JOptionPane.showMessageDialog(null, "Dir created: "+subDirName, "Julia Generated", JOptionPane.INFORMATION_MESSAGE);
////		//////////////////////////////////////////////
////		
////		//////////////////////endsremovin4now/////////////////////////////
////		
//		return;
//		
//	}
/////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
	private int getTotalVaryCount() {
		return this.getVaryJuliaColorCount() * this.getVaryJuliaPixXTransCount() * this.getVaryJuliaPixYTransCount()
				* this.getVaryJuliaIntraPixXYOpCount() * this.getVaryJuliaZFuncCount() * this.getVaryJuliaConstCFuncCount()
				* this.getVaryJuliaPixelConstOpZCCount() * this.getVaryJuliaPowerCount() * this.getVaryJuliaConstantCount()
				* this.getVaryJuliaIterCount() * this.getVaryJuliaBoundaryCount() * this.getVaryJuliaPixXCenterCount()
				* this.getVaryJuliaPixYCenterCount() * this.getVaryJuliaScaleSizeCount()
//				*	2	//	for	detailData
				*	2	//	for	useDiff
				*	2;	//	for	invertPix		

	}

	//realConstant
	private double setRealConstant( final int totalJuliaCount, final int index, final double from, final double to,
			final double jump,final  int realConstantCount) {
		if (this.diyJuliaVaryConstant && this.diyJuliaVaryGenConstantCb.isSelected()) {
			double start = from;
			int indexCount = 0;
			for (int r = 0; r < totalJuliaCount; r++, indexCount++) {
				if (indexCount + 1 > realConstantCount - 1) {
					indexCount = 0;
					start = from;
				}
				if (r == index) {
					return start;
				}
				start += jump;
			}
		}
		return 0;
	}

	//imagConstant
	private double setImagConstant(final int totalJuliaCount, final int index, final double from, final double to,
			final double jump, int imagConstantCount) {
		if (this.diyJuliaVaryConstant && this.diyJuliaVaryGenConstantCb.isSelected()) {
			double start = from;
			int indexCount = 0;
			for (int r = 0; r < totalJuliaCount; r++, indexCount++) {
				if (indexCount + 1 > imagConstantCount - 1) {
					indexCount = 0;
					start = from;
				}
				if (r == index) {
					return start;
				}
				start += jump;
			}
		}
		return 0;
	}
	
	//setBoundarys
	private void setBoundarys(final FractalBase aJulia, final int totalJuliaCount, final int index, final double from,
			final double to, final double jump, final int boundarycount) {
		if (this.diyJuliaVaryBoundaryCb.isSelected() && this.diyJuliaVaryBoundary) {
			double start = from;
			int indexCount = 0;
			for (int r = 0; r < totalJuliaCount; r++, indexCount++) {
				if (indexCount + 1 > boundarycount - 1) {
					indexCount = 0;
					start = from;
				}
				if (r == index) {
					aJulia.setBound(start);
					return;
				}
				start += jump;
			}
		} else {
			aJulia.setBound(this.diyJuliaBound);
		}
	}
	
	//setScaleSizes
	private void setScaleSizes(final FractalBase aJulia, final int totalJuliaCount, final int index, final double from, final double to, final double jump, final int scaleSizecount) {
		if (this.diyJuliaVaryScaleSizeCb.isSelected() && this.diyJuliaVaryScaleSize) {
			double start = from;
			int indexCount = 0;
			for (int r = 0; r < totalJuliaCount; r++, indexCount++) {
				if (indexCount + 1 > scaleSizecount - 1) {
					indexCount = 0;
					start = from;
				}
				if (r == index) {
					aJulia.setScaleSize(start);
					return;
				}
				start += jump;
			}
//			this.diyJuliaVaryBoundaryFromVal = Double.parseDouble(this.diyJuliaGenBoundaryFromTf.getText());
//			this.diyJuliaVaryBoundaryToVal = Double.parseDouble(this.diyJuliaGenBoundaryToTf.getText());
//
//			/*int scaleSizecount = this.getVaryScaleSizeCount();*/
//			int indexCount = 0;
//			for (int i = 0; i < totalJuliaCount; i++) {
//				if (indexCount + 1 > scaleSizecount - 1) {
//					indexCount = 0;
//				}
//				if (i == index) {
//					aJulia.setScaleSize(SCALE_SIZES[indexCount]);
//					break;
//				}
//			}
		} else {
			aJulia.setScaleSize(this.diyJuliaScaleSize);
		}
	}
	
	
	//	yC
	private void setYCs(final FractalBase aJulia, final int totalJuliaCount, final int index, final int yCcount) {
		if (this.diyJuliaVaryPixYCentrCb.isSelected()) {
			//CENTER_XY
			/*int yCcount = this.getVaryPixYCenterCount();*/
			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++, indexCount++) {
				if (indexCount + 1 > yCcount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setyC(CENTER_XY[indexCount]);
					return;
				}
			}
		} else {
			aJulia.setyC(this.diyJuliaYC);
		}
	}
	
	//	xC
	private void setXCs(final FractalBase aJulia, final int totalJuliaCount, final int index, final int xCcount) {
		if (this.diyJuliaVaryPixXCentrCb.isSelected()) {
			//CENTER_XY
			/*int xCcount = this.getVaryPixXCenterCount();*/
			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++, indexCount++) {
				if (indexCount + 1 > xCcount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setxC(CENTER_XY[indexCount]);
					return;
				}
			}
		} else {
			aJulia.setxC(this.diyJuliaXC);
		}
	}
	
	
	//setLoopLimits	
	private void setLoopLimits(final FractalBase aJulia, final int totalJuliaCount, final int index) {
		if (this.diyJuliaVaryIterCb.isSelected()) {
			//EXPONENTS
			int itersCount = this.getVaryJuliaIterCount();
			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++, indexCount++) {
				if (indexCount + 1 > itersCount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setMaxIter(MAX_ITERATIONS[indexCount]);
					return;
				}
			}
		} else {
			aJulia.setMaxIter(this.diyJuliaMaxIter);
		}
	}

	//setPowers
	private int setPixelPowerZ(final FractalBase aJulia, final int totalJuliaCount, final int index, final int from, final int to, final int jump, final int powersCount) {
		if (this.diyJuliaVaryPixelPowerZCb.isSelected() && this.diyJuliaVaryPixelPowerZ) {
			int start = from;	//	TODO	CHECKTHIS
			int indexCount = 0;
			for (int r = 0; r < totalJuliaCount; r++, indexCount++) {
				if (indexCount + 1 > powersCount - 1) {
					indexCount = 0;
					start = from;
				}
				if (r == index) {
					this.diyJuliaPower=start;
					aJulia.setPower(start);
					return start;
				}
				start += jump;
			}
		} else {
			aJulia.setPower(this.diyJuliaPower);
			return this.diyJuliaPower;
		}
		return 0;
	}
	
	
	
	private void setUseConstFunctions(final FractalBase aJulia, final int totalJuliaCount, final int index, final int pixZFuncCount) {
		if (this.diyJuliaVaryConstCFuncCb.isSelected()) {
			//FUNCTION_OPTIONS
			/*int pixZFuncCount = this.getVaryConstCFuncCount();*/
			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++, indexCount++) {
				if (indexCount + 1 > pixZFuncCount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setUseFuncConst(FUNCTION_OPTIONS[indexCount]);
					return;
				}
			}
		} else {
			aJulia.setUseFuncConst(this.constFuncChoice);
		}
	}
	
	private String setPxConstOperations(final FractalBase aJulia, final int totalJuliaCount, final int index, final int pixZFuncCount) {
		if (this.diyJuliaVaryPixelConstOpZCCb.isSelected()) {
			//PIX_CONST_OPRNS
			/*int pixZFuncCount = this.getVaryConstCFuncCount();*/
			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++, indexCount++) {
				if (indexCount + 1 > pixZFuncCount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setPxConstOperation(PIX_CONST_OPRNS[indexCount]);
					return PIX_CONST_OPRNS[indexCount];
				}
			}
		} else {
			aJulia.setPxConstOperation(this.pxConstOprnChoice);
			return this.pxConstOprnChoice;
		}
		return this.pxConstOprnChoice;
	}
	
	
	private String setUseFuncPixels(final FractalBase aJulia, final int totalJuliaCount, final int index, final int pixZFuncCount) {
		if (this.diyJuliaVaryPixelZFuncCb.isSelected()) {
			//FUNCTION_OPTIONS
			/*int pixZFuncCount = this.getVaryZFuncCount();*/
			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++,indexCount++) {
				if (indexCount + 1 > pixZFuncCount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setUseFuncPixel(FUNCTION_OPTIONS[indexCount]);
					return FUNCTION_OPTIONS[indexCount];
				}
			}
		} else {
			if (this.diyJApplyFormulaZ) {
				this.pxFuncChoice = this.diyJApplyFormulaTf.getText().trim();
			}
			aJulia.setUseFuncPixel(this.pxFuncChoice);
			return this.pxFuncChoice;

		}
		return "None";
	}
/*	
//	
//	
//	private void setUseFuncPixels(final Julia[] julias) {
//		System.out.println("setUseFuncPixels__julias__lenghIs" + julias.length);
//		if (this.diyJuliaVaryPixelZFuncCb.isSelected()) {
//			//FUNCTION_OPTIONS
//			int pixZFuncCount = this.getVaryZFuncCount();
//		} else {
//			for (Julia aJulia : julias) {
//				aJulia.setUseFuncPixel(this.pxFuncChoice);
//			}
//		}
//	}
//	
//	private void setPixXYOperations(final Julia[] julias) {
//		System.out.println("setPixXYOperations__julias__lenghIs" + julias.length);
//		if (this.diyJuliaVaryIntraPixXYCb.isSelected()) {
//			//PIX_INTRA_OPRNS
//			int pixYYOpsCount = this.getVaryIntraPixXYOpCount();
//
//			int indexCount = 0;
//		}
//	}
*/	
	private String setPixXYOperations(final FractalBase aJulia, final int totalJuliaCount, final int index, final int pixYYOpsCount) {
		if (this.diyJuliaVaryIntraPixXYCb.isSelected()) {
			// PIX_INTRA_OPRNS
			/*int pixYYOpsCount = this.getVaryIntraPixXYOpCount();*/

			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++,indexCount++) {
				/*System.out.println("i_is_" + i + " indexCount us " + indexCount);*/
				if (indexCount + 1 > pixYYOpsCount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setPixXYOperation(PIX_INTRA_OPRNS[indexCount]);
					this.pixIntraXYOperation=PIX_INTRA_OPRNS[indexCount];
					return this.pixIntraXYOperation;
				}
			}
		} else {
			aJulia.setPixXYOperation(this.pixIntraXYOperation);
			return this.pixIntraXYOperation;
		}

		return this.pixIntraXYOperation;
	}
	
	private String setPxYTransformations(final FractalBase aJulia, final int totalJuliaCount, final int index, final int pixYTransCount) {
		if (this.diyJuliaVaryPixYTranCb.isSelected()) {
			// PIY_TRANSFORM_OPTIONS
			/*int pixYTransCount = this.getVaryPixYTransCount();*/

			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++) {
				if (indexCount + 1 > pixYTransCount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setPxYTransformation(PIX_TRANSFORM_OPTIONS[indexCount]);
					return PIX_TRANSFORM_OPTIONS[indexCount];
				}
				indexCount += 1;
			}
		} else {
			aJulia.setPxYTransformation(this.pixYTransform);
			return this.pixYTransform;
		}
		return this.pixYTransform;
	}
/*
//	private void setPxYTransformations(final Julia[] julias) {
//		System.out.println("setPxYTransformations__julias__lenghIs" + julias.length);
//		if (this.diyJuliaVaryPixYTranCb.isSelected()) {
//			// PIX_TRANSFORM_OPTIONS
//			int pixYTransCount = this.getVaryPixYTransCount();
//
//			int indexCount = 0;
//			for (int i = 0; i < julias.length; i++) {
//				System.out.println("i_is_" + i + " indexCount us " + indexCount);
//				Julia aJulia = julias[i];
//				if (indexCount + 1 > pixYTransCount-1) {
//					indexCount = 0;
//				}
//				aJulia.setPxYTransformation(PIX_TRANSFORM_OPTIONS[indexCount]);
//			}
//		} else {
//			for (Julia aJulia : julias) {
//				aJulia.setPxYTransformation(this.pixYTransform);
//			}
//		}
//
//	}
*/	
	private String setPxXTransformations(final FractalBase aJulia, final int totalJuliaCount, final int index,final int pixXTransCount) {
		if (this.diyJuliaVaryPixXTranCb.isSelected()) {
			// PIX_TRANSFORM_OPTIONS
			/*int pixXTransCount = this.getVaryPixXTransCount();*/

			int indexCount = 0;
			for (int i = 0; i < totalJuliaCount; i++) {
				if (indexCount + 1 > pixXTransCount - 1) {
					indexCount = 0;
				}
				if (i == index) {
					aJulia.setPxXTransformation(PIX_TRANSFORM_OPTIONS[indexCount]);
					return PIX_TRANSFORM_OPTIONS[indexCount];
				}
				indexCount += 1;
			}
		} else {
			aJulia.setPxXTransformation(this.pixXTransform);
			return this.pixXTransform;
		}
		return this.pixXTransform;
	}
	
	/*
//	private void setPxXTransformations(final Julia[] julias) {
//		System.out.println("setPxXTransformations__julias__lenghIs" + julias.length);
//		if (this.diyJuliaVaryPixXTranCb.isSelected()) {
//			// PIX_TRANSFORM_OPTIONS
//			int pixXTransCount = this.getVaryPixXTransCount();
//
//			int indexCount = 0;
//			for (int i = 0; i < julias.length; i++) {
//				System.out.println("i_is_" + i + " indexCount us " + indexCount);
//				Julia aJulia = julias[i];
//				if (indexCount + 1 > pixXTransCount-1) {
//					indexCount = 0;
//				}
//				aJulia.setPxXTransformation(PIX_TRANSFORM_OPTIONS[indexCount]);
//			}
//		} else {
//			for (Julia aJulia : julias) {
//				aJulia.setPxXTransformation(this.pixXTransform);
//			}
//		}
//
//	}
*/	
	
	/**
	 * @param aJulia
	 * @param totalJuliaCount
	 * @param index
	 */
	private void setUseDiff_InvertPixels(final Julia aJulia, final int totalJuliaCount, final int index) {
		/*
		 * 	uD		useDifference		DuD		DontUseDifference
		 * 	rPx		reversePixel		DrPx	DontReversePixel
		 * 
		 *	4
		 *	ud		Dud		uD		Dud
		 *	DrPx	DrPx	rPx		rPx
		 */
		if (index % 2 == 0) {
			aJulia.setUseDiff(true);
			this.setDiyJuliaUseDiff(true);
		} else {
			aJulia.setUseDiff(false);
			this.setDiyJuliaUseDiff(false);
		}
		
		int invertPixSwitch = 0;
		int indexCount = 0;
		boolean reversePix = false;
		for (int i = 0; i < totalJuliaCount; i++, indexCount++, invertPixSwitch++) {
			if (invertPixSwitch + 1 > 2) {
				invertPixSwitch = 0;
				reversePix = !(reversePix);
			}
			if (indexCount == index) {
				aJulia.setReversePixelCalculation(reversePix);
				this.invertPixelCalculation = reversePix;
				return;
			}
		}
	}
	
	
	private void setColors(final FractalBase aJulia, final int totalJuliaCount, final int index, final int colrCount) {
		if (this.diyJuliaVaryColorCb.isSelected()) {
			/*int colrCount = this.getVaryColorCount();*/

			int indexColor = 0;
			int sampStartVal = 0;
			int sampDivVal = 0;
			for (int i = 0; i < totalJuliaCount; i++,indexColor++) {
				if (indexColor + 1 > colrCount - 1) {
					indexColor = 0;
				}
				if (indexColor < COLOR_OPTIONS.length - 1) {
					if (i == index) {
						aJulia.setColorChoice(COLOR_OPTIONS[indexColor]);
						return;
					}
				} else if (indexColor >= COLOR_OPTIONS.length - 1) {

					if (sampStartVal >= COLOR_SAMPLE_STARTVAL_ARRAYS.length - 1) {
						sampStartVal = 0;
					}
					if (sampDivVal >= COLOR_SAMPLE_DIV_ARRAYS.length - 1) {
						sampDivVal = 0;
					}

					if (i == index) {
						aJulia.setColorChoice(COLOR_OPTIONS[COLOR_OPTIONS.length - 1]);
						aJulia.setRgbStartVals(COLOR_SAMPLE_STARTVAL_ARRAYS[sampStartVal]);
						aJulia.setRgbDivisors(COLOR_SAMPLE_DIV_ARRAYS[sampDivVal]);

						this.colorSampleMixStartVals = COLOR_SAMPLE_STARTVAL_OPTIONS[sampStartVal];
						this.colorSampleDivVals = COLOR_SAMPLE_DIV_OPTIONS[sampDivVal];
						return;
					}
					sampStartVal += 1;
					sampDivVal += 1;
				}
			}
			
		} else {
			String colorChosen = this.colorChoice;
			aJulia.setColorChoice(colorChosen);
			boolean useSample = colorChosen.equals("SampleMix");
			if (useSample) {
				this.setSampleColorMix(aJulia);
			}
		}
	}
	
//	private void setColors(final Julia[] julias) {
//		System.out.println("setColors__julias__lenghIs"+julias.length);
//		if (this.diyJuliaVaryColorCb.isSelected()) {
//			int colrCount = this.getVaryColorCount();
//
//			int indexColor = 0;			
//			int sampStartVal = 0;
//			int sampDivVal = 0;
//			
//			for (int i = 0; i < julias.length; i++) {
//				System.out.println("i_is_"+i+" indexColor us "+indexColor);
//				Julia aJulia = julias[i];
//				if (indexColor + 1 > colrCount-1) {
//					indexColor = 0;
//				}
//
//				if (indexColor >= COLOR_OPTIONS.length - 1) {
//					aJulia.setColorChoice(COLOR_OPTIONS[COLOR_OPTIONS.length - 1]);
//
//					if (sampStartVal >= COLOR_SAMPLE_STARTVAL_ARRAYS.length - 1) {
//						sampStartVal = 0;
//					}
//					if (sampDivVal >= COLOR_SAMPLE_DIV_ARRAYS.length - 1) {
//						sampDivVal = 0;
//					}
//					aJulia.setRgbStartVals(COLOR_SAMPLE_STARTVAL_ARRAYS[sampStartVal]);
//					aJulia.setRgbDivisors(COLOR_SAMPLE_DIV_ARRAYS[sampDivVal]);
//
//					sampStartVal += 1;
//					sampDivVal += 1;
//				} else {
//					aJulia.setColorChoice(COLOR_OPTIONS[indexColor]);
//				}
//				indexColor += 1;
//			}
//
//		} else {
//			// dont-vary---all-same-color
//			// COLOR_OPTIONS = new
//			// String[]{"BlackWhite","ColorPalette","ComputeColor"/*,"Random"*/,"SampleMix"};
//			String colorChosen = this.colorChoice;
//			boolean useSample = colorChosen.equals("SampleMix");
//			for (Julia aJulia : julias) {
//				aJulia.setColorChoice(colorChosen);
//				if (useSample) {
//					this.setSampleColorMix(aJulia);
//				}
//			}
//		}
////			
////	//////////////////removing--all-bleow---4--now/////////////////		
////			
////			boolean useCP = colorChosen.equals("ColorPalette");
////			boolean useBw = colorChosen.equals("BlackWhite");
//////			boolean useSample = colorChosen.equals("SampleMix");
////			/*FB
////			 * protected boolean useColorPalette = false;
////	protected boolean useBlackWhite = false;
////	protected boolean useComputeColor = !(this.isUseColorPalette() || this.isUseBlackWhite());
////	if (useCP) {
////			ff.setUseColorPalette(useCP);
////		} else {
////			if (useBw) {
////				ff.setUseBlackWhite(useBw);
////			} else {
////				if (useSample) {
////					
////					this.setSampleColorMix(ff);
////				} else {
////
////					ff.setUseColorPalette(useCP);
////				}
////			}
////		}
////		
////	*/
////			for(Julia aJulia : julias) {
////				if (useCP) {
////					aJulia.setColorChoice(colorChosen);//	aJulia.setUseColorPalette(useCP);
////				} else {
////					if (useBw) {
////						aJulia.setColorChoice(colorChosen);//	aJulia.setUseBlackWhite(useBw);
////					} else {
////						if (useSample) {
////							
////							this.setSampleColorMix(aJulia);
////						} else {
////
////							aJulia.setColorChoice(colorChosen);//	aJulia.setUseColorPalette(useCP);
////						}
////					}
////				}
////				
////			}
////			
////		}
//////////////////////removing--all-bleow---4--now/////////////////
////		
//	}
	


	private String[] getAppendStr2Array(final String appendStr, final String[] strArr) {
		String[] arr2Return = new String[strArr.length];
		for (int i = 0; i < strArr.length; i++) {
			arr2Return[i] = appendStr + strArr[i];
		}
		return arr2Return;
	}
	
	private List<String> getAppendStr2List(final String appendStr, final List<?> aList) {
		List<String> returnList = new ArrayList<String>(aList.size());
		for (int i = 0; i < aList.size(); i++) {
			returnList.add(i, appendStr + aList.get(i));
		}
		return returnList;
	}
	
	private List<?> getVaryPolyColorsList() {
//		colorChoice
		final String colorChoice = "colorChoice=";
		if (this.polyVaryColorCb.isSelected() && this.polyVaryColor) {
			/*return COLOR_OPTIONS.length;*/
			String[] colr_options_no_sampl = new String[] { colorChoice+"BlackWhite", colorChoice+"ColorPalette", colorChoice+"ComputeColor", colorChoice+"ColorGradient6", colorChoice+"ColorBlowout|Default", colorChoice+"ColorBlowout|Snowy", colorChoice+"ColorBlowout|Pink", colorChoice+"ColorBlowout|Matrix", 
					colorChoice+"ColorSuperBlowout|Original", 
					colorChoice+"ColorSuperBlowout|Fire", 
					colorChoice+"ColorSuperBlowout|Black & White", 
					colorChoice+"ColorSuperBlowout|Electric Blue", 
					colorChoice+"ColorSuperBlowout|Toon",  
					colorChoice+"ColorSuperBlowout|Gold",  
					colorChoice+"ColorSuperBlowout|Classic VGA",
					colorChoice+"ColorSuperBlowout|CGA 1",
					colorChoice+"ColorSuperBlowout|CGA 2",
					colorChoice+"ColorSuperBlowout|Primary (RGB)",
					colorChoice+"ColorSuperBlowout|Secondary (CMY)",
					colorChoice+"ColorSuperBlowout|Tertiary 1", 
					colorChoice+"ColorSuperBlowout|Tertiary 2", 
					colorChoice+"ColorSuperBlowout|Neon" };
		
			return asList(colr_options_no_sampl);

		} else {
			final String colorChosen = (String) this.colorChoiceCombo.getSelectedItem();
			if(colorChosen.equals("SampleMix")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorSampleMixStartValsCombo.getSelectedItem()+PIPE+
						(String) this.colorSampleDivValsCombo.getSelectedItem()/* +
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
			} else if(colorChosen.equals("ColorBlowout")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorBlowoutChoiceCombo.getSelectedItem() /*+
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
			} else if(colorChosen.equals("ColorSuperBlowout")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorSuperBlowoutChoiceCombo.getSelectedItem() /*+
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
			} else {
			return asList(new String[] { colorChoice+colorChosen });
		}
		}
	}

	private int getVaryPolyColorCount() {
		if (this.polyVaryColorCb.isSelected()) {
			/*return COLOR_OPTIONS.length;*/
			String[] colr_options_no_sampl = new String[] { colorChoice+"BlackWhite", colorChoice+"ColorPalette", colorChoice+"ComputeColor", colorChoice+"ColorGradient6", colorChoice+"ColorBlowout|Default", colorChoice+"ColorBlowout|Snowy", colorChoice+"ColorBlowout|Pink", colorChoice+"ColorBlowout|Matrix", 
					colorChoice+"ColorSuperBlowout|Original", 
					colorChoice+"ColorSuperBlowout|Fire", 
					colorChoice+"ColorSuperBlowout|Black & White", 
					colorChoice+"ColorSuperBlowout|Electric Blue", 
					colorChoice+"ColorSuperBlowout|Toon",  
					colorChoice+"ColorSuperBlowout|Gold",  
					colorChoice+"ColorSuperBlowout|Classic VGA",
					colorChoice+"ColorSuperBlowout|CGA 1",
					colorChoice+"ColorSuperBlowout|CGA 2",
					colorChoice+"ColorSuperBlowout|Primary (RGB)",
					colorChoice+"ColorSuperBlowout|Secondary (CMY)",
					colorChoice+"ColorSuperBlowout|Tertiary 1", 
					colorChoice+"ColorSuperBlowout|Tertiary 2", 
					colorChoice+"ColorSuperBlowout|Neon" };
			return colr_options_no_sampl.length;
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryMandColorsList() {
//		colorChoice
		final String colorChoice = "colorChoice=";
		if (this.diyMandVaryColorCb.isSelected() && this.diyMandVaryColor) {
			String[] colr_options_no_sampl = new String[] { colorChoice+"BlackWhite", colorChoice+"ColorPalette", colorChoice+"ComputeColor", colorChoice+"ColorGradient6", colorChoice+"ColorBlowout|Default", colorChoice+"ColorBlowout|Snowy", colorChoice+"ColorBlowout|Pink", colorChoice+"ColorBlowout|Matrix", 
					colorChoice+"ColorSuperBlowout|Original", 
					colorChoice+"ColorSuperBlowout|Fire", 
					colorChoice+"ColorSuperBlowout|Black & White", 
					colorChoice+"ColorSuperBlowout|Electric Blue", 
					colorChoice+"ColorSuperBlowout|Toon",  
					colorChoice+"ColorSuperBlowout|Gold",  
					colorChoice+"ColorSuperBlowout|Classic VGA",
					colorChoice+"ColorSuperBlowout|CGA 1",
					colorChoice+"ColorSuperBlowout|CGA 2",
					colorChoice+"ColorSuperBlowout|Primary (RGB)",
					colorChoice+"ColorSuperBlowout|Secondary (CMY)",
					colorChoice+"ColorSuperBlowout|Tertiary 1", 
					colorChoice+"ColorSuperBlowout|Tertiary 2", 
					colorChoice+"ColorSuperBlowout|Neon" };
			
			return asList(colr_options_no_sampl);

		} else {
			final String colorChosen = (String) this.colorChoiceCombo.getSelectedItem();
			if(colorChosen.equals("SampleMix")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorSampleMixStartValsCombo.getSelectedItem()+PIPE+
						(String) this.colorSampleDivValsCombo.getSelectedItem() /*+
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
			} else if(colorChosen.equals("ColorBlowout")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorBlowoutChoiceCombo.getSelectedItem()/* +
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
			} else if(colorChosen.equals("ColorSuperBlowout")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorSuperBlowoutChoiceCombo.getSelectedItem()/* +
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
				} else {
			return asList(new String[] { colorChoice+colorChosen });
			}
			/*
			if (!colorChosen.equals("SampleMix")) {
				return asList(new String[] { colorChoice+colorChosen });
			} else {
				return asList(new String[] {(String) ( 
						colorChoice+
						OPEN_BRACK+
						colorChosen+PIPE+ 
						(String) this.colorSampleMixStartValsCombo.getSelectedItem()+PIPE+
						(String) this.colorSampleDivValsCombo.getSelectedItem() +
						CLOSE_BRACK ).replaceAll(BACK_SLASH,EMPTY) });
			}*/
		}
	}

	private int getVaryMandColorCount() {
		if (this.diyMandVaryColorCb.isSelected()) {
			/*return COLOR_OPTIONS.length;*/
			String[] colr_options_no_sampl = new String[] { colorChoice+"BlackWhite", colorChoice+"ColorPalette", colorChoice+"ComputeColor", colorChoice+"ColorGradient6", colorChoice+"ColorBlowout|Default", colorChoice+"ColorBlowout|Snowy", colorChoice+"ColorBlowout|Pink", colorChoice+"ColorBlowout|Matrix", 
					colorChoice+"ColorSuperBlowout|Original", 
					colorChoice+"ColorSuperBlowout|Fire", 
					colorChoice+"ColorSuperBlowout|Black & White", 
					colorChoice+"ColorSuperBlowout|Electric Blue", 
					colorChoice+"ColorSuperBlowout|Toon",  
					colorChoice+"ColorSuperBlowout|Gold",  
					colorChoice+"ColorSuperBlowout|Classic VGA",
					colorChoice+"ColorSuperBlowout|CGA 1",
					colorChoice+"ColorSuperBlowout|CGA 2",
					colorChoice+"ColorSuperBlowout|Primary (RGB)",
					colorChoice+"ColorSuperBlowout|Secondary (CMY)",
					colorChoice+"ColorSuperBlowout|Tertiary 1", 
					colorChoice+"ColorSuperBlowout|Tertiary 2", 
					colorChoice+"ColorSuperBlowout|Neon" };
			return colr_options_no_sampl.length;
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryJuliaColorsList() {
//		colorChoice
		final String colorChoice = "colorChoice=";
		if (this.diyJuliaVaryColorCb.isSelected() && this.diyJuliaVaryColor) {
			String[] colr_options_no_sampl = new String[] { colorChoice+"BlackWhite", colorChoice+"ColorPalette", colorChoice+"ComputeColor", colorChoice+"ColorGradient6", colorChoice+"ColorBlowout|Default", colorChoice+"ColorBlowout|Snowy", colorChoice+"ColorBlowout|Pink", colorChoice+"ColorBlowout|Matrix", 
					colorChoice+"ColorSuperBlowout|Original", 
					colorChoice+"ColorSuperBlowout|Fire", 
					colorChoice+"ColorSuperBlowout|Black & White", 
					colorChoice+"ColorSuperBlowout|Electric Blue", 
					colorChoice+"ColorSuperBlowout|Toon",  
					colorChoice+"ColorSuperBlowout|Gold",  
					colorChoice+"ColorSuperBlowout|Classic VGA",
					colorChoice+"ColorSuperBlowout|CGA 1",
					colorChoice+"ColorSuperBlowout|CGA 2",
					colorChoice+"ColorSuperBlowout|Primary (RGB)",
					colorChoice+"ColorSuperBlowout|Secondary (CMY)",
					colorChoice+"ColorSuperBlowout|Tertiary 1", 
					colorChoice+"ColorSuperBlowout|Tertiary 2", 
					colorChoice+"ColorSuperBlowout|Neon"};
			
			/*List<String> cl1 =*/return asList(colr_options_no_sampl);

			/*List<?> cl2 = product(this.getAppendStr2List("startVals=", asList(COLOR_SAMPLE_STARTVAL_OPTIONS)), this.getAppendStr2List("divVals=",asList(COLOR_SAMPLE_DIV_OPTIONS)));
			List<?> allSampls = product(asList(new String[] { colorChoice+"SampleMix" }), cl2);

			Object[] sampsArr = allSampls.toArray();
			int s1 = sampsArr.length;
			final int s2 = colr_options_no_sampl.length;
			final int totalColrNum = s1 + s2;
			Object[] allColrsArr = new Object[totalColrNum];
			System.arraycopy(sampsArr, 0, allColrsArr, 0, s1);
			System.arraycopy(colr_options_no_sampl, 0, allColrsArr, s1, s2);

			List<?> allColrsList = asList(allColrsArr);

			return allColrsList;*/
		} else {
			final String colorChosen = (String) this.colorChoiceCombo.getSelectedItem();
			if(colorChosen.equals("SampleMix")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorSampleMixStartValsCombo.getSelectedItem()+PIPE+
						(String) this.colorSampleDivValsCombo.getSelectedItem() /*+
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
			} else if(colorChosen.equals("ColorBlowout")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorBlowoutChoiceCombo.getSelectedItem()/* +
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
			} else if(colorChosen.equals("ColorSuperBlowout")) {
				return asList(new String[] {(String) ( 
						colorChoice+
						/*OPEN_BRACK+*/
						colorChosen+PIPE+ 
						(String) this.colorSuperBlowoutChoiceCombo.getSelectedItem()/* +
						CLOSE_BRACK*/ ).replaceAll(BACK_SLASH,EMPTY) });
					} else {
				return asList(new String[] { colorChoice+colorChosen });
			}
		}
	}

	private int getVaryJuliaColorCount() {
		if (this.diyJuliaVaryColorCb.isSelected()) {
			/*int count = COLOR_OPTIONS.length - 1;
			count += (COLOR_SAMPLE_STARTVAL_OPTIONS.length * COLOR_SAMPLE_DIV_OPTIONS.length);
			System.out.println("In_getVaryColorCount count = " + count);
			return count;*/
			/*return COLOR_OPTIONS.length;*/
			String[] colr_options_no_sampl = new String[] { colorChoice+"BlackWhite", colorChoice+"ColorPalette", colorChoice+"ComputeColor", colorChoice+"ColorGradient6", colorChoice+"ColorBlowout|Default", colorChoice+"ColorBlowout|Snowy", colorChoice+"ColorBlowout|Pink", colorChoice+"ColorBlowout|Matrix", 
					colorChoice+"ColorSuperBlowout|Original", 
					colorChoice+"ColorSuperBlowout|Fire", 
					colorChoice+"ColorSuperBlowout|Black & White", 
					colorChoice+"ColorSuperBlowout|Electric Blue", 
					colorChoice+"ColorSuperBlowout|Toon",  
					colorChoice+"ColorSuperBlowout|Gold",  
					colorChoice+"ColorSuperBlowout|Classic VGA",
					colorChoice+"ColorSuperBlowout|CGA 1",
					colorChoice+"ColorSuperBlowout|CGA 2",
					colorChoice+"ColorSuperBlowout|Primary (RGB)",
					colorChoice+"ColorSuperBlowout|Secondary (CMY)",
					colorChoice+"ColorSuperBlowout|Tertiary 1", 
					colorChoice+"ColorSuperBlowout|Tertiary 2", 
					colorChoice+"ColorSuperBlowout|Neon" };
			return colr_options_no_sampl.length;
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryJuliaFieldTypesList() {
		// fieldType
		final String fieldType = "fieldType=";
		//if (this.diyJuliaGen || this.diyJuliaGenCb.isSelected()) {
		if (this.diyJuliaVaryFieldType || this.diyJuliaVaryFieldTypeCb.isSelected()) {	
			String[] fieldTypesArr = this.getAppendStr2Array(fieldType,JULIA_FIELD_TYPES);//
					//new String[] { "None", "Fatou", "Z-Sq", "ClassicJ" });
			return asList(fieldTypesArr);
		} else {		//redundant	-	for BUT!
			String diyJApplyField = this.fieldLines;
			return asList(new String[] { fieldType + diyJApplyField });
			/*boolean diyJApplyFatou = this.applyFatou;
			boolean diyJApplyZSq = this.applyZSq;
			boolean diyJApplyClassic = this.applyClassicJulia;

			if (!diyJApplyField.equals("None")) {
				if (diyJApplyFatou) {
					return asList(new String[] { fieldType + "Fatou" });
				} else if (diyJApplyZSq) {
					return asList(new String[] { fieldType + "Z-Sq" });
				} else if (diyJApplyClassic) {
					return asList(new String[] { fieldType + "ClassicJ" });
				}
			} else {
				return asList(new String[] { fieldType + "None" });
			}*/
		}
		/*return null;*/
	}
	
	private int getVaryJuliaFieldTypesCount() {// diyJuliaGenCb
		//if (this.diyJuliaGen || this.diyJuliaGenCb.isSelected()) {
		if(this.diyJuliaVaryFieldType || this.diyJuliaVaryFieldTypeCb.isSelected()){
			return JULIA_FIELD_TYPES.length;//4; // "None","Fatou","Z-Sq","ClassicJ"
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryPolyPixXTransList() {
		// pixXTransform
		final String pixXTransform = "pixXTransform=";
		if (this.polyVaryPixXTranCb.isSelected() && this.polyVaryPixXTran) {
			String[] pxXTrArr = this.getAppendStr2Array(pixXTransform, PIX_TRANSFORM_OPTIONS);
			return asList(pxXTrArr);// (PIX_TRANSFORM_OPTIONS);
		} else {
			return asList(new String[] { pixXTransform + (String) this.pxXTransformCombo.getSelectedItem() });
		}
	}

	private int getVaryPolyPixXTransCount() {
		if (this.polyVaryPixXTranCb.isSelected()) {
			System.out.println("In_getVaryPixXTransCount count = " + PIX_TRANSFORM_OPTIONS.length);
			return PIX_TRANSFORM_OPTIONS.length;
		} else {
			return 1;
		}
	}
	

	
	private List<?> getVaryMandPixXTransList() {
		// pixXTransform
		final String pixXTransform = "pixXTransform=";
		if (this.diyMandVaryPixXTranCb.isSelected() && this.diyMandVaryPixXTran) {
			String[] pxXTrArr = this.getAppendStr2Array(pixXTransform, PIX_TRANSFORM_OPTIONS);
			return asList(pxXTrArr);// (PIX_TRANSFORM_OPTIONS);
		} else {
			return asList(new String[] { pixXTransform + (String) this.pxXTransformCombo.getSelectedItem() });
		}
	}

	private int getVaryMandPixXTransCount() {
		if (this.diyMandVaryPixXTranCb.isSelected()) {
			System.out.println("In_getVaryPixXTransCount count = " + PIX_TRANSFORM_OPTIONS.length);
			return PIX_TRANSFORM_OPTIONS.length;
		} else {
			return 1;
		}
	}
	

	private List<?> getVaryJuliaPixXTransList() {
		// pixXTransform
		final String pixXTransform = "pixXTransform=";
		if (this.diyJuliaVaryPixXTranCb.isSelected() && this.diyJuliaVaryPixXTran) {
			String[] pxXTrArr = this.getAppendStr2Array(pixXTransform, PIX_TRANSFORM_OPTIONS);
			return asList(pxXTrArr);// (PIX_TRANSFORM_OPTIONS);
		} else {
			return asList(new String[] { pixXTransform + (String) this.pxXTransformCombo.getSelectedItem() });
		}
	}

	private int getVaryJuliaPixXTransCount() {
		if (this.diyJuliaVaryPixXTranCb.isSelected()) {
			System.out.println("In_getVaryPixXTransCount count = " + PIX_TRANSFORM_OPTIONS.length);
			return PIX_TRANSFORM_OPTIONS.length;
		} else {
			return 1;
		}
	}
	

	private List<?> getVaryPolyPixYTransList() {
		// pixYTransform
		final String pixYTransform = "pixYTransform=";
		if (this.polyVaryPixYTranCb.isSelected() && this.polyVaryPixYTran) {
			String[] pxYTrArr = this.getAppendStr2Array(pixYTransform, PIX_TRANSFORM_OPTIONS);
			return asList(pxYTrArr);// (PIX_TRANSFORM_OPTIONS);
		} else {
			return asList(new String[] { pixYTransform + (String) this.pxYTransformCombo.getSelectedItem() });
		}
	}

	private int getVaryPolyPixYTransCount() {
		if (this.polyVaryPixYTranCb.isSelected()) {
			System.out.println("In_getVaryPixYTransCount count = " + PIX_TRANSFORM_OPTIONS.length);
			return PIX_TRANSFORM_OPTIONS.length;
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryMandPixYTransList() {
		// pixYTransform
		final String pixYTransform = "pixYTransform=";
		if (this.diyMandVaryPixYTranCb.isSelected() && this.diyMandVaryPixYTran) {
			String[] pxYTrArr = this.getAppendStr2Array(pixYTransform, PIX_TRANSFORM_OPTIONS);
			return asList(pxYTrArr);// (PIX_TRANSFORM_OPTIONS);
		} else {
			return asList(new String[] { pixYTransform + (String) this.pxYTransformCombo.getSelectedItem() });
		}
	}

	private int getVaryMandPixYTransCount() {
		if (this.diyMandVaryPixYTranCb.isSelected()) {
			System.out.println("In_getVaryPixYTransCount count = " + PIX_TRANSFORM_OPTIONS.length);
			return PIX_TRANSFORM_OPTIONS.length;
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryJuliaPixYTransList() {
		// pixYTransform
		final String pixYTransform = "pixYTransform=";
		if (this.diyJuliaVaryPixYTranCb.isSelected() && this.diyJuliaVaryPixYTran) {
			String[] pxYTrArr = this.getAppendStr2Array(pixYTransform, PIX_TRANSFORM_OPTIONS);
			return asList(pxYTrArr);// (PIX_TRANSFORM_OPTIONS);
		} else {
			return asList(new String[] { pixYTransform + (String) this.pxYTransformCombo.getSelectedItem() });
		}
	}

	private int getVaryJuliaPixYTransCount() {
		if (this.diyJuliaVaryPixYTranCb.isSelected()) {
			System.out.println("In_getVaryPixYTransCount count = " + PIX_TRANSFORM_OPTIONS.length);
			return PIX_TRANSFORM_OPTIONS.length;
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryPolyIntraPixXYOpList() {
		// pixIntraXYOperation
		final String pixIntraXYOperation = "pixIntraXYOperation=";
		if (this.polyVaryIntraPixXYCb.isSelected() && this.polyVaryIntraPixXY) {
			String[] intraXYOpArr = this.getAppendStr2Array(pixIntraXYOperation, PIX_INTRA_OPRNS);
			return asList(intraXYOpArr);// asList(PIX_INTRA_OPRNS);
		} else {
			return asList(
					new String[] { pixIntraXYOperation + (String) this.intraPixOperationCombo.getSelectedItem() });
		}
	}

	private int getVaryPolyIntraPixXYOpCount() {
		if (this.polyVaryIntraPixXYCb.isSelected()) {
			System.out.println("In_getVaryIntraPixXYOpCount count = "+PIX_INTRA_OPRNS.length);
			return PIX_INTRA_OPRNS.length;
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryMandIntraPixXYOpList() {
		// pixIntraXYOperation
		final String pixIntraXYOperation = "pixIntraXYOperation=";
		if (this.diyMandVaryIntraPixXYCb.isSelected() && this.diyMandVaryIntraPixXY) {
			String[] intraXYOpArr = this.getAppendStr2Array(pixIntraXYOperation, PIX_INTRA_OPRNS);
			return asList(intraXYOpArr);// asList(PIX_INTRA_OPRNS);
		} else {
			return asList(
					new String[] { pixIntraXYOperation + (String) this.intraPixOperationCombo.getSelectedItem() });
		}
	}

	private int getVaryMandIntraPixXYOpCount() {
		if (this.diyMandVaryIntraPixXYCb.isSelected()) {
			System.out.println("In_getVaryIntraPixXYOpCount count = "+PIX_INTRA_OPRNS.length);
			return PIX_INTRA_OPRNS.length;
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryJuliaIntraPixXYOpList() {
		// pixIntraXYOperation
		final String pixIntraXYOperation = "pixIntraXYOperation=";
		if (this.diyJuliaVaryIntraPixXYCb.isSelected() && this.diyJuliaVaryIntraPixXY) {
			String[] intraXYOpArr = this.getAppendStr2Array(pixIntraXYOperation, PIX_INTRA_OPRNS);
			return asList(intraXYOpArr);// asList(PIX_INTRA_OPRNS);
		} else {
			return asList(
					new String[] { pixIntraXYOperation + (String) this.intraPixOperationCombo.getSelectedItem() });
		}
	}

	private int getVaryJuliaIntraPixXYOpCount() {
		if (this.diyJuliaVaryIntraPixXYCb.isSelected()) {
			System.out.println("In_getVaryIntraPixXYOpCount count = "+PIX_INTRA_OPRNS.length);
			return PIX_INTRA_OPRNS.length;
		} else {
			return 1;
		}
	}
	

	private List<?> getVaryPolyZFuncList() {
		final String pxFuncChoice = "pxFuncChoice=";
		if (this.polyVaryPixelZFuncCb.isSelected() && this.polyVaryPixelZFunc
				&& !(this.polyApplyFormulaZCb.isSelected() || this.polyApplyFormulaZ)) {
			String[] pxFuncArr = this.getAppendStr2Array(pxFuncChoice, FUNCTION_OPTIONS);
			return asList(pxFuncArr);// (FUNCTION_OPTIONS);
		} else {
			if (this.polyApplyFormulaZCb.isSelected() || this.polyApplyFormulaZ) {
				return asList(this.getPolyGenVaryApplyFormulaZ());
			} else {
				return asList(new String[] { pxFuncChoice + (String) this.pxFuncCombo.getSelectedItem() });
			}
		}
	}
	
	private String[] getPolyGenVaryApplyFormulaZ() {
		final String pxFuncChoice = "pxFuncChoice=";
		String[] applyFormulas = this.polyGenVaryApplyFormulaTxtArea.getText().split("\n");
		return this.getAppendStr2Array(pxFuncChoice, applyFormulas);
	}


	private int getVaryPolyZFuncCount() {
		if (this.polyVaryPixelZFuncCb.isSelected() && !this.polyApplyFormulaZCb.isSelected()) {
			System.out.println("In_getVaryZFuncCount count = " + FUNCTION_OPTIONS.length);
			return FUNCTION_OPTIONS.length;
		} else {
			if (this.polyApplyFormulaZCb.isSelected() || this.polyApplyFormulaZ) {
				return this.polyGenVaryApplyFormulaTxtArea.getText().split("\n").length;
			} else {
				return 1;
			}
		}
	}
	
	
	private List<?> getVaryMandZFuncList() {
		final String pxFuncChoice = "pxFuncChoice=";
		if (this.diyMandVaryPixelZFuncCb.isSelected() && this.diyMandVaryPixelZFunc
				&& !(this.diyMandApplyFormulaZCb.isSelected() || this.diyMandApplyFormulaZ)) {
			String[] pxFuncArr = this.getAppendStr2Array(pxFuncChoice, FUNCTION_OPTIONS);
			return asList(pxFuncArr);// (FUNCTION_OPTIONS);
		} else {
			if (this.diyMandApplyFormulaZCb.isSelected() || this.diyMandApplyFormulaZ) {
				return asList(this.getMandGenVaryApplyFormulaZ());
			} else {
				return asList(new String[] { pxFuncChoice + (String) this.pxFuncCombo.getSelectedItem() });
			}
		}
	}
	
	private String[] getMandGenVaryApplyFormulaZ() {
		final String pxFuncChoice = "pxFuncChoice=";
		String[] applyFormulas = this.diyMandGenVaryApplyFormulaTxtArea.getText().split("\n");
		return this.getAppendStr2Array(pxFuncChoice, applyFormulas);
	}


	private int getVaryMandZFuncCount() {
		if (this.diyMandVaryPixelZFuncCb.isSelected() && !this.diyJApplyFormulaZCb.isSelected()) {
			System.out.println("In_getVaryZFuncCount count = " + FUNCTION_OPTIONS.length);
			return FUNCTION_OPTIONS.length;
		} else {
			if (this.diyMandApplyFormulaZCb.isSelected() || this.diyMandApplyFormulaZ) {
				return this.diyMandGenVaryApplyFormulaTxtArea.getText().split("\n").length;
			} else {
				return 1;
			}
		}
	}
	
	
	private List<?> getVaryJuliaZFuncList() {
		final String pxFuncChoice = "pxFuncChoice=";
		if (this.diyJuliaVaryPixelZFuncCb.isSelected() && this.diyJuliaVaryPixelZFunc
				&& !(this.diyJApplyFormulaZCb.isSelected() || this.diyJApplyFormulaZ)) {
			String[] pxFuncArr = this.getAppendStr2Array(pxFuncChoice, FUNCTION_OPTIONS);
			return asList(pxFuncArr);// (FUNCTION_OPTIONS);
		} else {
			if (this.diyJApplyFormulaZCb.isSelected() || this.diyJApplyFormulaZ) {
				return asList(getJuliaGenVaryApplyFormulaZ());
			} else {
				return asList(new String[] { pxFuncChoice + (String) this.pxFuncCombo.getSelectedItem() });
			}
		}
	}

	private String[] getJuliaGenVaryApplyFormulaZ() {
		final String pxFuncChoice = "pxFuncChoice=";
		String[] applyFormulas = this.diyJuliaGenVaryApplyFormulaTxtArea.getText().split("\n");
		return this.getAppendStr2Array(pxFuncChoice, applyFormulas);
	}

	private int getVaryJuliaZFuncCount() {
		if (this.diyJuliaVaryPixelZFuncCb.isSelected() && !this.diyJApplyFormulaZCb.isSelected()) {
			System.out.println("In_getVaryZFuncCount count = " + FUNCTION_OPTIONS.length);
			return FUNCTION_OPTIONS.length;
		} else {
			if (this.diyJApplyFormulaZCb.isSelected() || this.diyJApplyFormulaZ) {
				return this.diyJuliaGenVaryApplyFormulaTxtArea.getText().split("\n").length;
			} else {
				return 1;
			}
		}
	}

	private List<?> getVaryPolyConstCFuncList() {
		final String constFuncChoice = "constFuncChoice=";
		if (this.polyVaryConstCFuncCb.isSelected() && this.polyVaryConstCFunc) {
			String[] constFuncArr = this.getAppendStr2Array(constFuncChoice, FUNCTION_OPTIONS);
			return asList(constFuncArr);
		} else {
			return asList(new String[] { constFuncChoice + (String) this.constFuncCombo.getSelectedItem() });
		}
	}

	private int getVaryPolyConstCFuncCount() {
		if (this.polyVaryConstCFuncCb.isSelected()) {
			System.out.println("In_getVaryConstCFuncCount count = "+FUNCTION_OPTIONS.length);
			return FUNCTION_OPTIONS.length;
		} else {
			return 1;
		}
	}

	
	private List<?> getVaryMandConstCFuncList() {
		final String constFuncChoice = "constFuncChoice=";
		if (this.diyMandVaryConstCFuncCb.isSelected() && this.diyMandVaryConstCFunc) {
			String[] constFuncArr = this.getAppendStr2Array(constFuncChoice, FUNCTION_OPTIONS);
			return asList(constFuncArr);
		} else {
			return asList(new String[] { constFuncChoice + (String) this.constFuncCombo.getSelectedItem() });
		}
	}

	private int getVaryMandConstCFuncCount() {
		if (this.diyMandVaryConstCFuncCb.isSelected()) {
			System.out.println("In_getVaryConstCFuncCount count = "+FUNCTION_OPTIONS.length);
			return FUNCTION_OPTIONS.length;
		} else {
			return 1;
		}
	}

	
	private List<?> getVaryJuliaConstCFuncList() {
		final String constFuncChoice = "constFuncChoice=";
		if (this.diyJuliaVaryConstCFuncCb.isSelected() && this.diyJuliaVaryConstCFunc) {
			String[] constFuncArr = this.getAppendStr2Array(constFuncChoice, FUNCTION_OPTIONS);
			return asList(constFuncArr);
		} else {
			return asList(new String[] { constFuncChoice + (String) this.constFuncCombo.getSelectedItem() });
		}
	}

	private int getVaryJuliaConstCFuncCount() {
		if (this.diyJuliaVaryConstCFuncCb.isSelected()) {
			System.out.println("In_getVaryConstCFuncCount count = "+FUNCTION_OPTIONS.length);
			return FUNCTION_OPTIONS.length;
		} else {
			return 1;
		}
	}

	private List<?> getVaryPolyPixelConstOpZCList() {
		//pxConstOprnChoice
		final String pxConstOprnChoice = "pxConstOprnChoice=";
		if (this.polyVaryPixelConstOpZCCb.isSelected() && this.polyVaryPixelConstOpZC) {
			String[] constFuncArr = this.getAppendStr2Array(pxConstOprnChoice, PIX_CONST_OPRNS);
			return asList(constFuncArr);
		} else {
			return asList(new String[] { pxConstOprnChoice+(String) this.pxConstOprnCombo.getSelectedItem() });
		}
	}

	private int getVaryPolyPixelConstOpZCCount() {
		if (this.polyVaryPixelConstOpZCCb.isSelected()) {
			System.out.println("In_getVaryPixelConstOpZCCount count = "+PIX_CONST_OPRNS.length);
			return PIX_CONST_OPRNS.length;
		} else {
			return 1;
		}
	}

	private List<?> getVaryMandPixelConstOpZCList() {
		//pxConstOprnChoice
		final String pxConstOprnChoice = "pxConstOprnChoice=";
		if (this.diyMandVaryPixelConstOpZCCb.isSelected() && this.diyMandVaryPixelConstOpZC) {
			String[] constFuncArr = this.getAppendStr2Array(pxConstOprnChoice, PIX_CONST_OPRNS);
			return asList(constFuncArr);
		} else {
			return asList(new String[] { pxConstOprnChoice+(String) this.pxConstOprnCombo.getSelectedItem() });
		}
	}

	private int getVaryMandPixelConstOpZCCount() {
		if (this.diyMandVaryPixelConstOpZCCb.isSelected()) {
			System.out.println("In_getVaryPixelConstOpZCCount count = "+PIX_CONST_OPRNS.length);
			return PIX_CONST_OPRNS.length;
		} else {
			return 1;
		}
	}

	private List<?> getVaryJuliaPixelConstOpZCList() {
		//pxConstOprnChoice
		final String pxConstOprnChoice = "pxConstOprnChoice=";
		if (this.diyJuliaVaryPixelConstOpZCCb.isSelected() && this.diyJuliaVaryPixelConstOpZC) {
			String[] constFuncArr = this.getAppendStr2Array(pxConstOprnChoice, PIX_CONST_OPRNS);
			return asList(constFuncArr);
		} else {
			return asList(new String[] { pxConstOprnChoice+(String) this.pxConstOprnCombo.getSelectedItem() });
		}
	}

	private int getVaryJuliaPixelConstOpZCCount() {
		if (this.diyJuliaVaryPixelConstOpZCCb.isSelected()) {
			System.out.println("In_getVaryPixelConstOpZCCount count = "+PIX_CONST_OPRNS.length);
			return PIX_CONST_OPRNS.length;
		} else {
			return 1;
		}
	}
	
	/*private List<?> getVaryJuliaScaleSizeList(double scaleSizeFrom, double scaleSizeTo, double scaleSizeJump,
			int scaleSizeCount) {
		if (this.diyJuliaVaryScaleSizeCb.isSelected() && this.diyJuliaVaryScaleSize) {
			// double scaleSizeFrom = this.diyJuliaVaryScaleSizeFromVal;
			// double scaleSizeTo = this.diyJuliaVaryScaleSizeToVal;
			// double scaleSizeJump = this.diyJuliaVaryScaleSizeJumpVal;

			boolean needsAdd = false;
			if ((scaleSizeFrom + ((scaleSizeCount - 1) * scaleSizeJump)) > scaleSizeTo) {
				needsAdd = true;
			}

			double[] scaleSizeVals = new double[scaleSizeCount];
			scaleSizeVals[0] = scaleSizeFrom;

			double start = scaleSizeFrom;

			for (int i = 1; i < scaleSizeCount - 1; i++) {
				start += scaleSizeJump;
				scaleSizeVals[i] = start;
			}

			if (needsAdd && scaleSizeVals[scaleSizeCount - 1] > scaleSizeTo) {
				scaleSizeVals[scaleSizeCount - 1] = scaleSizeTo;
			}

			List<String> list = stringListiFy(scaleSizeVals);
			return list;
		} else {
			return asList(new String[] { String.valueOf(this.diyJuliaScaleSizeCombos.getSelectedItem()) });
		}
	}*/
	private List<?> getVaryPolyPowerList(int pixelPowerZFrom,int pixelPowerZTo,int pixelPowerZJump,int powerCount) {
		final String powerChoice = "power=";
		if (this.polyVaryPixelPowerZCb.isSelected() && this.polyVaryPixelPowerZ) {
			return this.getAppendStr2List(powerChoice,	this.getVaryIntList(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump, powerCount));
		} else {
			return asList(new String[] { powerChoice+String.valueOf(this.polyExpCombos.getSelectedItem()) });
		}
	}

	private int getVaryPolyPowerCount() {
		if (this.polyVaryPixelPowerZCb.isSelected()) {
			double pixelPowerZFrom = this.polyVaryPixelPowerZFromVal;
			double pixelPowerZTo = this.polyVaryPixelPowerZToVal;
			double pixelPowerZJump = this.polyVaryPixelPowerZJumpVal;
			System.out.println("In_getVaryPowerCount count = "
					+ this.getJumpCount(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump));
			return this.getJumpCount(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump);
		} else {
			return 1;
		}
	}

	
	private List<?> getVaryMandPowerList(int pixelPowerZFrom,int pixelPowerZTo,int pixelPowerZJump,int powerCount) {
		final String powerChoice = "power=";
		if (this.diyMandVaryPixelPowerZCb.isSelected() && this.diyMandVaryPixelPowerZ) {
			return this.getAppendStr2List(powerChoice,	this.getVaryIntList(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump, powerCount));
		} else {
			return asList(new String[] { powerChoice+String.valueOf(this.diyMandExpCombos.getSelectedItem()) });
		}
	}

	private int getVaryMandPowerCount() {
		if (this.diyMandVaryPixelPowerZCb.isSelected()) {
			double pixelPowerZFrom = this.diyMandVaryPixelPowerZFromVal;
			double pixelPowerZTo = this.diyMandVaryPixelPowerZToVal;
			double pixelPowerZJump = this.diyMandVaryPixelPowerZJumpVal;
			System.out.println("In_getVaryPowerCount count = "
					+ this.getJumpCount(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump));
			return this.getJumpCount(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump);
		} else {
			return 1;
		}
	}

	
	private List<?> getVaryJuliaPowerList(int pixelPowerZFrom,int pixelPowerZTo,int pixelPowerZJump,int powerCount) {
		final String powerChoice = "power=";
		if (this.diyJuliaVaryPixelPowerZCb.isSelected() && this.diyJuliaVaryPixelPowerZ) {
			return this.getAppendStr2List(powerChoice,	this.getVaryIntList(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump, powerCount));
//			double pixelPowerZFrom = this.diyJuliaVaryPixelPowerZFromVal;
//			double pixelPowerZTo = this.diyJuliaVaryPixelPowerZToVal;
//			double pixelPowerZJump = this.diyJuliaVaryPixelPowerZJumpVal;
/*
			boolean needsAdd = false;
			if ((pixelPowerZFrom + ((powerCount - 1) * pixelPowerZJump)) > pixelPowerZTo) {
				needsAdd = true;
			}

			double[] powerZVals = new double[powerCount];
			powerZVals[0] = pixelPowerZFrom;

			double start = pixelPowerZFrom;

			for (int i = 1; i < powerCount - 1; i++) {
				start += pixelPowerZJump;
				powerZVals[i] = start;
			}
			
			if (needsAdd && powerZVals[powerCount - 1] > pixelPowerZTo) {
				powerZVals[powerCount - 1] = pixelPowerZTo;
			}
			
			List<String> list = stringListiFy(powerZVals);
			return list;*/
		} else {
			return asList(new String[] { powerChoice+String.valueOf(this.diyJuliaPowerCombos.getSelectedItem()) });
		}
	}

	private int getVaryJuliaPowerCount() {
		if (this.diyJuliaVaryPixelPowerZCb.isSelected()) {
			double pixelPowerZFrom = this.diyJuliaVaryPixelPowerZFromVal;
			double pixelPowerZTo = this.diyJuliaVaryPixelPowerZToVal;
			double pixelPowerZJump = this.diyJuliaVaryPixelPowerZJumpVal;
			System.out.println("In_getVaryPowerCount count = "
					+ this.getJumpCount(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump));
			return this.getJumpCount(pixelPowerZFrom, pixelPowerZTo, pixelPowerZJump);
		} else {
			return 1;
		}
	}
	private int getVaryPolyConstantCount() {
		if (this.polyVaryGenConstantCb.isSelected() || this.polyVaryConstant) {
			System.out.println("In_getVaryConstantCount()  realCount= "+this.getVaryPolyRealConstantCount()+" imagCount= "+this.getVaryPolyImagConstantCount());
			System.out.println("TotalConstantCount realCount*imagCount= "+(this.getVaryPolyRealConstantCount() * this.getVaryPolyImagConstantCount()));
			return (this.getVaryPolyRealConstantCount() * this.getVaryPolyImagConstantCount());
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryPolyRealConstantList(double realFrom, double realTo, double realJump, int realCount) {
		final String realConstChoice = "realConstChoice=";
		if (this.polyVaryGenConstantCb.isSelected() || this.polyVaryConstant) {
			return this.getAppendStr2List(realConstChoice,	this.getVaryDoubleList(realFrom, realTo, realJump, realCount) );
		} else {
			if (this.polyKeepConstRb.isSelected() || this.polyKeepConst) {
				return asList(new String[] { realConstChoice+"DynamicConst" });
			} else {
				return asList(new String[] { realConstChoice+this.polyRealTf.getText() });
			}
		}
	}

	private int getVaryPolyRealConstantCount() {
		if (this.polyVaryGenConstantCb.isSelected() || this.polyVaryConstant) {
			double realFrom = this.polyGenRealFromVal;
			double realTo = this.polyGenRealToVal;
			double realJump = this.polyGenRealJumpVal;
			return this.getJumpCount(realFrom, realTo, realJump);
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryPolyImagConstantList(double imagFrom, double imagTo, double imagJump, int imagCount) {
		final String imagConstChoice = "imagConstChoice=";
		if (this.polyVaryGenConstantCb.isSelected() || this.polyVaryConstant) {
			return this.getAppendStr2List(imagConstChoice,	this.getVaryDoubleList(imagFrom, imagTo, imagJump, imagCount));
		} else {
			if (this.polyKeepConstRb.isSelected() || this.polyKeepConst) {
				return asList(new String[] { imagConstChoice+"DynamicConst" });
			} else {
				return asList(new String[] { imagConstChoice+this.polyImgTf.getText() });
			}
		}
	}

	private int getVaryPolyImagConstantCount() {
		if (this.polyVaryGenConstantCb.isSelected()) {
			double imagFrom = this.polyGenImagFromVal;
			double imagTo = this.polyGenImagToVal;
			double imagJump = this.polyGenImagJumpVal;
			return this.getJumpCount(imagFrom, imagTo, imagJump);
		} else {
			return 1;
		}
	}
	

	private int getVaryMandConstantCount() {
		if (this.diyMandVaryGenConstantCb.isSelected() || this.diyMandVaryConstant) {
			System.out.println("In_getVaryConstantCount()  realCount= "+this.getVaryMandRealConstantCount()+" imagCount= "+this.getVaryMandImagConstantCount());
			System.out.println("TotalConstantCount realCount*imagCount= "+(this.getVaryMandRealConstantCount() * this.getVaryMandImagConstantCount()));
			return (this.getVaryMandRealConstantCount() * this.getVaryMandImagConstantCount());
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryMandRealConstantList(double realFrom, double realTo, double realJump, int realCount) {
		final String realConstChoice = "realConstChoice=";
		if (this.diyMandVaryGenConstantCb.isSelected() || this.diyMandVaryConstant) {
			return this.getAppendStr2List(realConstChoice,	this.getVaryDoubleList(realFrom, realTo, realJump, realCount) );
		} else {
			if (this.diyMandelbrotKeepConstRb.isSelected() || this.diyMandKeepConst) {
				return asList(new String[] { realConstChoice+"DynamicConst" });
			} else {
				return asList(new String[] { realConstChoice+this.diyMandRealTf.getText() });
			}
		}
	}

	private int getVaryMandRealConstantCount() {
		if (this.diyMandVaryGenConstantCb.isSelected() || this.diyMandVaryConstant) {
			double realFrom = this.diyMandGenRealFromVal;
			double realTo = this.diyMandGenRealToVal;
			double realJump = this.diyMandGenRealJumpVal;
			return this.getJumpCount(realFrom, realTo, realJump);
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryMandImagConstantList(double imagFrom, double imagTo, double imagJump, int imagCount) {
		final String imagConstChoice = "imagConstChoice=";
		if (this.diyMandVaryGenConstantCb.isSelected() || this.diyMandVaryConstant) {
			return this.getAppendStr2List(imagConstChoice,	this.getVaryDoubleList(imagFrom, imagTo, imagJump, imagCount));
		} else {
			if (this.diyMandelbrotKeepConstRb.isSelected() || this.diyMandKeepConst) {
				return asList(new String[] { imagConstChoice+"DynamicConst" });
			} else {
				return asList(new String[] { imagConstChoice+this.diyMandImgTf.getText() });
			}
		}
	}

	private int getVaryMandImagConstantCount() {
		if (this.diyMandVaryGenConstantCb.isSelected()) {
			double imagFrom = this.diyMandGenImagFromVal;
			double imagTo = this.diyMandGenImagToVal;
			double imagJump = this.diyMandGenImagJumpVal;
			return this.getJumpCount(imagFrom, imagTo, imagJump);
		} else {
			return 1;
		}
	}
	

	private int getVaryJuliaConstantCount() {
		if (this.diyJuliaVaryGenConstantCb.isSelected() || this.diyJuliaVaryConstant) {
			System.out.println("In_getVaryConstantCount()  realCount= "+this.getVaryJuliaRealConstantCount()+" imagCount= "+this.getVaryJuliaImagConstantCount());
			System.out.println("TotalConstantCount realCount*imagCount= "+(this.getVaryJuliaRealConstantCount() * this.getVaryJuliaImagConstantCount()));
			return (this.getVaryJuliaRealConstantCount() * this.getVaryJuliaImagConstantCount());
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryJuliaRealConstantList(double realFrom, double realTo, double realJump, int realCount) {
		final String realConstChoice = "realConstChoice=";
		if (this.diyJuliaVaryGenConstantCb.isSelected() || this.diyJuliaVaryConstant) {
			return this.getAppendStr2List(realConstChoice,	this.getVaryDoubleList(realFrom, realTo, realJump, realCount) );
		} else {
			if (this.diyJuliaKeepConstRb.isSelected() || this.diyJuliaKeepConst) {
				return asList(new String[] { realConstChoice+"DynamicConst" });
			} else {
				return asList(new String[] { realConstChoice+this.diyJuliaRealTf.getText() });
			}
		}
	}

	private int getVaryJuliaRealConstantCount() {
		if (this.diyJuliaVaryGenConstantCb.isSelected() || this.diyJuliaVaryConstant) {
			double realFrom = this.diyJuliaGenRealFromVal;
			double realTo = this.diyJuliaGenRealToVal;
			double realJump = this.diyJuliaGenRealJumpVal;
			return this.getJumpCount(realFrom, realTo, realJump);
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryJuliaImagConstantList(double imagFrom, double imagTo, double imagJump, int imagCount) {
		final String imagConstChoice = "imagConstChoice=";
		if (this.diyJuliaVaryGenConstantCb.isSelected() || this.diyJuliaVaryConstant) {
			return this.getAppendStr2List(imagConstChoice,	this.getVaryDoubleList(imagFrom, imagTo, imagJump, imagCount));
		} else {
			if (this.diyJuliaKeepConstRb.isSelected() || this.diyJuliaKeepConst) {
				return asList(new String[] { imagConstChoice+"DynamicConst" });
			} else {
				return asList(new String[] { imagConstChoice+this.diyJuliaImgTf.getText() });
			}
		}
	}

	private int getVaryJuliaImagConstantCount() {
		if (this.diyJuliaVaryGenConstantCb.isSelected()) {
			double imagFrom = this.diyJuliaGenImagFromVal;
			double imagTo = this.diyJuliaGenImagToVal;
			double imagJump = this.diyJuliaGenImagJumpVal;
			return this.getJumpCount(imagFrom, imagTo, imagJump);
		} else {
			return 1;
		}
	}
	
	


	private List<?> getVaryPolyTypeList() {
		final String rowColumnMixTypeChoice = "rowColumnMixType=";
		if (this.polyVaryRCMTCb.isSelected() && this.polyVaryPolyType) {
			List<String> polyTypeList = this.getAppendStr2List(rowColumnMixTypeChoice, asList(POLY_RCMT_TYPES));
			return (polyTypeList);
		} else {
			return asList(
					new String[] { rowColumnMixTypeChoice + String.valueOf(this.polyTypeCombos.getSelectedItem()) });
		}
	}

	private int getVaryPolyTypeCount() {
		if (this.polyVaryRCMTCb.isSelected()) {
			System.out.println("In_getVaryPolyTypeCount count = "+POLY_RCMT_TYPES.length);
			return POLY_RCMT_TYPES.length;
		} else {
			return 1;
		}
	}

	private List<?> getVaryPolyIterList() {
		final String maxIterChoice = "maxIter=";
		if (this.polyVaryIterCb.isSelected() && this.polyVaryIter) {
			List<String> maxIterList = this.getAppendStr2List(maxIterChoice, asList(MAX_ITERATIONS));
			return (maxIterList);
		} else {
			return asList(
					new String[] { maxIterChoice + String.valueOf(this.polyMaxIterCombos.getSelectedItem()) });
		}
	}

	private int getVaryPolyIterCount() {
		if (this.polyVaryIterCb.isSelected()) {
			System.out.println("In_getVaryIterCount count = "+MAX_ITERATIONS.length);
			return MAX_ITERATIONS.length;
		} else {
			return 1;
		}
	}
	

	private List<?> getVaryMandIterList() {
		final String maxIterChoice = "maxIter=";
		if (this.diyMandVaryIterCb.isSelected() && this.diyMandVaryIter) {
			List<String> maxIterList = this.getAppendStr2List(maxIterChoice, asList(MAX_ITERATIONS));
			return (maxIterList);
		} else {
			return asList(
					new String[] { maxIterChoice + String.valueOf(this.diyMandMaxIterCombos.getSelectedItem()) });
		}
	}

	private int getVaryMandIterCount() {
		if (this.diyMandVaryIterCb.isSelected()) {
			System.out.println("In_getVaryIterCount count = "+MAX_ITERATIONS.length);
			return MAX_ITERATIONS.length;
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryJuliaIterList() {
		final String maxIterChoice = "maxIter=";
		if (this.diyJuliaVaryIterCb.isSelected() && this.diyJuliaVaryIter) {
			List<String> maxIterList = this.getAppendStr2List(maxIterChoice, asList(MAX_ITERATIONS));
			return (maxIterList);
		} else {
			return asList(
					new String[] { maxIterChoice + String.valueOf(this.diyJuliaMaxIterCombos.getSelectedItem()) });
		}
	}
	

	private int getVaryJuliaIterCount() {
		if (this.diyJuliaVaryIterCb.isSelected()) {
			System.out.println("In_getVaryIterCount count = "+MAX_ITERATIONS.length);
			return MAX_ITERATIONS.length;
		} else {
			return 1;
		}
	}

	private List<?> getVaryPolyBoundaryList(double boundaryFrom, double boundaryTo, double boundaryJump, int boundaryCount) {
		final String boundaryChoice = "boundary=";
		if (this.polyVaryBoundaryCb.isSelected() && this.polyVaryBoundary) {
			return this.getAppendStr2List(boundaryChoice,
					this.getVaryDoubleList(boundaryFrom, boundaryTo, boundaryJump, boundaryCount));
		} else {
			return asList(new String[] { boundaryChoice + String.valueOf(this.polyBoundCombos.getSelectedItem()) });
		}
	}

	private int getVaryPolyBoundaryCount() {
		if (this.polyVaryBoundaryCb.isSelected()) {
			double boundaryFrom = this.polyVaryBoundaryFromVal;
			double boundaryTo = this.polyVaryBoundaryToVal;
			double boundaryJump = this.polyVaryBoundaryJumpVal;
			System.out.println("In_getVaryBoundaryCount count = "+this.getJumpCount(boundaryFrom, boundaryTo, boundaryJump));
			return this.getJumpCount(boundaryFrom, boundaryTo, boundaryJump);
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryMandBoundaryList(double boundaryFrom, double boundaryTo, double boundaryJump, int boundaryCount) {
		final String boundaryChoice = "boundary=";
		if (this.diyMandVaryBoundaryCb.isSelected() && this.diyMandVaryBoundary) {
			return this.getAppendStr2List(boundaryChoice,
					this.getVaryDoubleList(boundaryFrom, boundaryTo, boundaryJump, boundaryCount));
		} else {
			return asList(new String[] { boundaryChoice + String.valueOf(this.diyMandBoundCombos.getSelectedItem()) });
		}
	}

	private int getVaryMandBoundaryCount() {
		if (this.diyMandVaryBoundaryCb.isSelected()) {
			double boundaryFrom = this.diyMandVaryBoundaryFromVal;
			double boundaryTo = this.diyMandVaryBoundaryToVal;
			double boundaryJump = this.diyMandVaryBoundaryJumpVal;
			System.out.println("In_getVaryBoundaryCount count = "+this.getJumpCount(boundaryFrom, boundaryTo, boundaryJump));
			return this.getJumpCount(boundaryFrom, boundaryTo, boundaryJump);
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryJuliaBoundaryList(double boundaryFrom, double boundaryTo, double boundaryJump, int boundaryCount) {
		final String boundaryChoice = "boundary=";
		if (this.diyJuliaVaryBoundaryCb.isSelected() && this.diyJuliaVaryBoundary) {
			return this.getAppendStr2List(boundaryChoice,
					this.getVaryDoubleList(boundaryFrom, boundaryTo, boundaryJump, boundaryCount));
		} else {
			return asList(new String[] { boundaryChoice + String.valueOf(this.diyJuliaBoundCombos.getSelectedItem()) });
		}
	}

	private int getVaryJuliaBoundaryCount() {
		if (this.diyJuliaVaryBoundaryCb.isSelected()) {
			double boundaryFrom = this.diyJuliaVaryBoundaryFromVal;
			double boundaryTo = this.diyJuliaVaryBoundaryToVal;
			double boundaryJump = this.diyJuliaVaryBoundaryJumpVal;
			System.out.println("In_getVaryBoundaryCount count = "+this.getJumpCount(boundaryFrom, boundaryTo, boundaryJump));
			return this.getJumpCount(boundaryFrom, boundaryTo, boundaryJump);
		} else {
			return 1;
		}
	}
	

	private List<?> getVaryPolyPixXCenterList() {
		final String xCentrChoice = "xCentrChoice=";
		if (this.polyVaryPixXCentrCb.isSelected() && this.polyVaryPixXCentr) {
			return this.getAppendStr2List(xCentrChoice, asList(CENTER_XY));
		} else {
			return asList(new String[] { xCentrChoice + String.valueOf(this.polyXCCombos.getSelectedItem()) });
		}
	}


	private int getVaryPolyPixXCenterCount() {
		if (this.polyVaryPixXCentrCb.isSelected()) {
			System.out.println("In_getVaryPixXCenterCount count = "+CENTER_XY.length);
			return CENTER_XY.length;
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryPolyPixYCenterList() {
		final String yCentrChoice = "yCentrChoice=";
		if (this.polyVaryPixYCentrCb.isSelected() && this.polyVaryPixYCentr) {
			return this.getAppendStr2List(yCentrChoice, asList(CENTER_XY));
		} else {
			return asList(new String[] { yCentrChoice+String.valueOf(this.polyYCCombos.getSelectedItem()) });
		}
	}

	private int getVaryPolyPixYCenterCount() {
		if (this.polyVaryPixYCentrCb.isSelected()) {
			System.out.println("In_getVaryPixYCenterCount count = "+CENTER_XY.length);
			return CENTER_XY.length;
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryMandPixXCenterList() {
		final String xCentrChoice = "xCentrChoice=";
		if (this.diyMandVaryPixXCentrCb.isSelected() && this.diyMandVaryPixXCentr) {
			return this.getAppendStr2List(xCentrChoice, asList(CENTER_XY));
		} else {
			return asList(new String[] { xCentrChoice + String.valueOf(this.diyMandXCCombos.getSelectedItem()) });
		}
	}


	private int getVaryMandPixXCenterCount() {
		if (this.diyMandVaryPixXCentrCb.isSelected()) {
			System.out.println("In_getVaryPixXCenterCount count = "+CENTER_XY.length);
			return CENTER_XY.length;
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryMandPixYCenterList() {
		final String yCentrChoice = "yCentrChoice=";
		if (this.diyMandVaryPixYCentrCb.isSelected() && this.diyMandVaryPixYCentr) {
			return this.getAppendStr2List(yCentrChoice, asList(CENTER_XY));
		} else {
			return asList(new String[] { yCentrChoice+String.valueOf(this.diyMandYCCombos.getSelectedItem()) });
		}
	}

	private int getVaryMandPixYCenterCount() {
		if (this.diyMandVaryPixYCentrCb.isSelected()) {
			System.out.println("In_getVaryPixYCenterCount count = "+CENTER_XY.length);
			return CENTER_XY.length;
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryJuliaPixXCenterList() {
		final String xCentrChoice = "xCentrChoice=";
		if (this.diyJuliaVaryPixXCentrCb.isSelected() && this.diyJuliaVaryPixXCentr) {
			return this.getAppendStr2List(xCentrChoice, asList(CENTER_XY));
		} else {
			return asList(new String[] { xCentrChoice + String.valueOf(this.diyJuliaXCCombos.getSelectedItem()) });
		}
	}


	private int getVaryJuliaPixXCenterCount() {
		if (this.diyJuliaVaryPixXCentrCb.isSelected()) {
			System.out.println("In_getVaryPixXCenterCount count = "+CENTER_XY.length);
			return CENTER_XY.length;
		} else {
			return 1;
		}
	}
	
	private List<?> getVaryJuliaPixYCenterList() {
		final String yCentrChoice = "yCentrChoice=";
		if (this.diyJuliaVaryPixYCentrCb.isSelected() && this.diyJuliaVaryPixYCentr) {
			return this.getAppendStr2List(yCentrChoice, asList(CENTER_XY));
		} else {
			return asList(new String[] { yCentrChoice+String.valueOf(this.diyJuliaYCCombos.getSelectedItem()) });
		}
	}

	private int getVaryJuliaPixYCenterCount() {
		if (this.diyJuliaVaryPixYCentrCb.isSelected()) {
			System.out.println("In_getVaryPixYCenterCount count = "+CENTER_XY.length);
			return CENTER_XY.length;
		} else {
			return 1;
		}
	}
	


	private List<?> getVaryPolyScaleSizeList(double scaleSizeFrom,double scaleSizeTo,double scaleSizeJump,int scaleSizeCount) {
		final String scaleSizeChoice = "scaleSizeChoice=";
		if (this.polyVaryScaleSizeCb.isSelected() && this.polyVaryScaleSize) {
			return this.getAppendStr2List(scaleSizeChoice,
					this.getVaryDoubleList(scaleSizeFrom,scaleSizeTo,scaleSizeJump,scaleSizeCount));
		} else {
			return asList(new String[] {scaleSizeChoice + String.valueOf(this.polyScaleSizeCombos.getSelectedItem()) });
		}
	}

	private int getVaryPolyScaleSizeCount() {
		if (this.polyVaryScaleSizeCb.isSelected()) {
			double scaleSizeFrom = this.polyVaryScaleSizeFromVal;
			double scaleSizeTo = this.polyVaryScaleSizeToVal;
			double scaleSizeJump = this.polyVaryScaleSizeJumpVal;
			System.out.println("In_getVaryScaleSizeCount count = "+this.getJumpCount(scaleSizeFrom, scaleSizeTo, scaleSizeJump));
			return this.getJumpCount(scaleSizeFrom, scaleSizeTo, scaleSizeJump);
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryMandScaleSizeList(double scaleSizeFrom,double scaleSizeTo,double scaleSizeJump,int scaleSizeCount) {
		final String scaleSizeChoice = "scaleSizeChoice=";
		if (this.diyMandVaryScaleSizeCb.isSelected() && this.diyMandVaryScaleSize) {
			return this.getAppendStr2List(scaleSizeChoice,
					this.getVaryDoubleList(scaleSizeFrom,scaleSizeTo,scaleSizeJump,scaleSizeCount));
		} else {
			return asList(new String[] {scaleSizeChoice + String.valueOf(this.diyMandScaleSizeCombos.getSelectedItem()) });
		}
	}

	private int getVaryMandScaleSizeCount() {
		if (this.diyMandVaryScaleSizeCb.isSelected()) {
			double scaleSizeFrom = this.diyMandVaryScaleSizeFromVal;
			double scaleSizeTo = this.diyMandVaryScaleSizeToVal;
			double scaleSizeJump = this.diyMandVaryScaleSizeJumpVal;
			System.out.println("In_getVaryScaleSizeCount count = "+this.getJumpCount(scaleSizeFrom, scaleSizeTo, scaleSizeJump));
			return this.getJumpCount(scaleSizeFrom, scaleSizeTo, scaleSizeJump);
		} else {
			return 1;
		}
	}
	
	
	private List<?> getVaryJuliaScaleSizeList(double scaleSizeFrom,double scaleSizeTo,double scaleSizeJump,int scaleSizeCount) {
		final String scaleSizeChoice = "scaleSizeChoice=";
		if (this.diyJuliaVaryScaleSizeCb.isSelected() && this.diyJuliaVaryScaleSize) {
			return this.getAppendStr2List(scaleSizeChoice,
					this.getVaryDoubleList(scaleSizeFrom,scaleSizeTo,scaleSizeJump,scaleSizeCount));
		} else {
			return asList(new String[] {scaleSizeChoice + String.valueOf(this.diyJuliaScaleSizeCombos.getSelectedItem()) });
		}
	}

	private int getVaryJuliaScaleSizeCount() {
		if (this.diyJuliaVaryScaleSizeCb.isSelected()) {
			double scaleSizeFrom = this.diyJuliaVaryScaleSizeFromVal;
			double scaleSizeTo = this.diyJuliaVaryScaleSizeToVal;
			double scaleSizeJump = this.diyJuliaVaryScaleSizeJumpVal;
			System.out.println("In_getVaryScaleSizeCount count = "+this.getJumpCount(scaleSizeFrom, scaleSizeTo, scaleSizeJump));
			return this.getJumpCount(scaleSizeFrom, scaleSizeTo, scaleSizeJump);
		} else {
			return 1;
		}
	}
	
	
	private int getJumpCount(final double from, final double to, final double jumpVal) {
		// (int)((bT-bF)/jump)+14FromStart+NotReachCheck
		assert (to > from);
		int count = 1; // from

		int numJump = (int) ((to - from) / jumpVal);

		count += numJump;

		double calc = from + (numJump * jumpVal);
		if (to > calc) {
			count += 1;
		}

		return count;
	}
	
	private List<?> getVaryDoubleList(double from, double to, double jump, int count) {

		boolean needsAdd = false;
		if ((from + ((count - 1) * jump)) > to) {
			needsAdd = true;
		}

		double[] vals = new double[count];
		vals[0] = from;

		double start = from;

		for (int i = 1; i < count /*- 1*/; i++) {
			start += jump;
			vals[i] = start;
		}

		if (needsAdd && vals[count - 1] > to) {
			vals[count - 1] = to;
		}

		List<String> list = stringListiFy(vals);
		return list;
	}
	
	private List<?> getVaryIntList(int from, int to, int jump, int count) {

		boolean needsAdd = false;
		if ((from + ((count - 1) * jump)) > to) {
			needsAdd = true;
		}

		int[] vals = new int[count];
		vals[0] = from;

		int start = from;

		for (int i = 1; i < count /*- 1*/; i++) {
			start += jump;
			vals[i] = start;
		}

		if (needsAdd && vals[count - 1] > to) {
			vals[count - 1] = to;
		}

		List<String> list = stringListiFy(vals);
		return list;
	}

	/*private int getJumpCount(final double from, final double to, final double jumpVal) {
		assert (to > from);
		int count = 1;	//TODO	CHECKTHIS
		boolean reached = false;

		double start = from;

		while (start <= to) {
			if (start + jumpVal > to && !reached) {
				reached = true;
				start = to - jumpVal;
			}
			start += jumpVal;
			count += 1;
		}

		return count;
	}*/
	
	public boolean getPolyUseDiff() {
		return this.polyUseDiff;
	}

	public void setPolyUseDiff(boolean useDiff) {
		this.polyUseDiff = useDiff;
	}
	
	private void setPolyGenFormulaArea(String pixelFunction, int PolyPower, String pixXTransform,String pixYTransform,String pixIntraXYOperation,String pxConstOprnChoice) {
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>(DIY)Poly Set:" + eol);
		/*if (this.diyPolyUseLyapunovExponent) {
			this.addUseLyapunovInfo();
		}*/
		if (pxConstOprnChoice.equals("Plus")) {
			this.formulaArea.append("<br/>f(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) + C");
		} else if (pxConstOprnChoice.equals("Multiply")) {
			this.formulaArea.append("<br/>f(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) * C");
		} else if (pxConstOprnChoice.equals("Minus")) {
			this.formulaArea.append("<br/>f(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) - C");
		} else if (pxConstOprnChoice.equals("Divide")) {
			this.formulaArea.append("<br/>f(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) / C");
		}/* else if (pxConstOp.equals("Power")) {
			this.formulaArea.setText("<br/>f(z) = (x ^ " + this.polyPower + " + y ^ " + this.polyPower + ") ^ C");
		} */

		boolean invertPix = this.invertPixelCalculation;
		
		if (!invertPix) {
			this.formulaArea.append("<br/>  x = Row + 0 * i , y = 0 + Column * i<br/>");
		}else{
			this.formulaArea.append("<br/>  x = Column + 0 * i , y = 0 + Row * i<br/>");
		}
		this.addXtrYtrXYInfroInfo(pixXTransform,pixYTransform,pixIntraXYOperation);		
		/*this.addInvertPixelCalcInfo();*/
		this.addPixelConstantOperationInfo(pxConstOprnChoice);
		this.addPixelFuncInfo(pixelFunction);

		if (this.polyType.equals("Reverse")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, y)");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(y, x)");
		} else if (this.polyType.equals("Exchange")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, 0.0)");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(0.0, y)");
		} else if (this.polyType.equals("Single")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, y)");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(0.0, 0.0)");
		} else if (this.polyType.equals("Duplicate")) {
			this.formulaArea.append("<br/><br/>Zx = Zy = new ComplexNumber(x, y)");
		} else if (this.polyType.equals("Exponent")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, 0.0).exp()");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(y, 0.0).exp()");
		} else if (this.polyType.equals("Power")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, y).power((int)x)");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(y, x).power((int)y)");
		} else if (this.polyType.equals("Default")) {
			this.formulaArea.append("<br/><br/>Zx = new ComplexNumber(x, 0.0)");
			this.formulaArea.append("<br/>Zy = new ComplexNumber(y, 0.0)");
		}
		/*
		this.addPolyUseDiffInfo();*/

		switch(pxConstOprnChoice){
			case	"Plus"	:	
				this.formulaArea.append(" + C</font>" + eol);
				break;
			case	"Minus"	:	
				this.formulaArea.append(" - C</font>" + eol);
				break;
			case	"Multiply"	:	
				this.formulaArea.append(" * C</font>" + eol);
				break;
			case	"Divide"	:	
				this.formulaArea.append(" / C</font>" + eol);
				break;
			case	"Power"	:	
				this.formulaArea.append(" ^ C</font>" + eol);
				break;
			default:
				this.formulaArea.append(" + C</font>" + eol);
				break;
		}
		
		this.addPolyUseDiffInfo();
	}
	
	

	private void setDiyMandGenFormulaArea(String pixelFunction, int mandPower, String pixXTransform,String pixYTransform,String pixIntraXYOperation,String pxConstOprnChoice) {
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>(DIY)Mand Set:" + eol);
		if (this.diyMandUseLyapunovExponent) {
			this.addUseLyapunovInfo();
		}
		
		this.addXtrYtrXYInfroInfo(pixXTransform,pixYTransform,pixIntraXYOperation);		
		this.addInvertPixelCalcInfo();
		this.addPixelConstantOperationInfo(pxConstOprnChoice);
		this.addPixelFuncInfo(pixelFunction);

		switch(pxConstOprnChoice){
			case	"Plus"	:	
				this.formulaArea.append(" + C</font>" + eol);
				break;
			case	"Minus"	:	
				this.formulaArea.append(" - C</font>" + eol);
				break;
			case	"Multiply"	:	
				this.formulaArea.append(" * C</font>" + eol);
				break;
			case	"Divide"	:	
				this.formulaArea.append(" / C</font>" + eol);
				break;
			case	"Power"	:	
				this.formulaArea.append(" ^ C</font>" + eol);
				break;
			default:
				this.formulaArea.append(" + C</font>" + eol);
				break;
		}
		
		this.addDiyMandelbrotUseDiffInfo();
	}
	
	
	private void setDiyJuliaGenFormulaArea(String pixelFunction, int juliaPower, String fieldLineTypes,/*boolean applyFatou, boolean applyZSq,
			boolean applyClassic,*/ String pixXTransform,String pixYTransform,String pixIntraXYOperation,String pxConstOprnChoice) {
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>(DIY)Julia Set:" + eol);
		if (this.diyJuliaUseLyapunovExponent) {
			this.addUseLyapunovInfo();
		}
		
		this.addXtrYtrXYInfroInfo(pixXTransform,pixYTransform,pixIntraXYOperation);		
		this.addInvertPixelCalcInfo();
		this.addPixelConstantOperationInfo(pxConstOprnChoice);
		this.addPixelFuncInfo(pixelFunction);
		
		if (fieldLineTypes.equals("Fatou")) {
			this.formulaArea.append("<br/><font color='green'>Fatou Field Lines:<br/><br/>f(z) = (1 - (z<sup>3</sup>/6)) / ((z - (z<sup>2</sup>/2)) <sup>2</sup>)");// + eol);// + C</font><br/>");			
		} else if (fieldLineTypes.equals("Z-Sq")) {
			this.formulaArea.append("<br/><font color='green'>ZSquared Field Lines:<br/><br/>f(z) = (z <sup>2</sup>) <sup> " + juliaPower+"</sup>");// + eol);// + " + C</font><br/>");
		} else if (fieldLineTypes.equals("ClassicJ")) {
			this.formulaArea.append("<br/><font color='green'>Classic Julia:<br/><br/>f(z) = z<sup>4</sup> + z<sup>3</sup>/(z-1) + z<sup>2</sup>/(z<sup>3</sup> + 4*z<sup>2</sup> + 5)");// + eol);//  + C</font><br/>");
		} else {	//NONE
			this.formulaArea.append("<br/><font color='green'>f(z) = z <sup>" + juliaPower+"</sup>");// + eol);// + " + C</font><br/>");
		}
		
		switch(pxConstOprnChoice){
			case	"Plus"	:	
				this.formulaArea.append(" + C</font>" + eol);
				break;
			case	"Minus"	:	
				this.formulaArea.append(" - C</font>" + eol);
				break;
			case	"Multiply"	:	
				this.formulaArea.append(" * C</font>" + eol);
				break;
			case	"Divide"	:	
				this.formulaArea.append(" / C</font>" + eol);
				break;
			case	"Power"	:	
				this.formulaArea.append(" ^ C</font>" + eol);
				break;
			default:
				this.formulaArea.append(" + C</font>" + eol);
				break;
		}
		
		this.addDiyJuliaUseDiffInfo();
	}
	///////////////////////////genertor-end///////////////////////////////////////
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

	private void doSetTrapSizeCombosCommand(Double size) {
		this.trapSizeChoice = size;
	}

	private void doSetTrapShapeCombosCommand(String shape) {
		this.trapShapeChoice = shape;
	}

	private void doSetOrbitPointCombosCommand(String orb){
		orb = orb.replaceAll(OPEN_BRACK,EMPTY).replaceAll(CLOSE_BRACK,EMPTY);
		String[] orbs = orb.split(",");
		this.orbitTrapPointChoice = new ComplexNumber(Double.parseDouble(orbs[0]),Double.parseDouble(orbs[1]));
		//System.out.println(" doSetOrbitPointCombosCommand==="+this.orbitTrapPointChoice);
	}
	
	private void doCloseCommand(){
		this.getFractalBase().dispose();
		this.buStart.setEnabled(false);
		this.buSavePxStart.setEnabled(false);
	}
	
	private void doSavePxStartCommand(boolean savPx) {
		this.savePixelInfo2File = savPx;

		this.doStartCommand();
	}
	
	private void doStartCommand() {
		this.formulaArea.setText("");
		// fractal art choice
		String choice = this.getComboChoice();
		System.out.println("choice--startC--is="+choice);
		
		if (choice.equals(ATTRACTORS)) {		
			this.doStartAttractorsCommand();
			return;
		}
		
		
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
			boolean createGasket = this.sierpinskiCreateGasket;
			String dirSierpT = this.sierpTDir;
			if (createGasket) {
				SierpinskiTriangle st = new SierpinskiTriangle();
				st.createGasket();
				ff = st;
			} else {
				ff = new SierpinskiTriangle(dirSierpT, fillInner);
			}
		} else if (choice.equals(SIERPINSKI_SQUARES)) {
			ff = new SierpinskiSquare();
		} else if (choice.equals(APOLLONIAN_CIRCLES)) {
			
			if (!this.apolloTriangleCb.isSelected()) {
				ff = new ApollonianCircles(cChoices, mXt);
			} else {
				 
				if (!this.apolloTriangleUseCentroidCb.isSelected()) {
					ff = new ApollonianTriangleNetwork("useRandom");
				}else{
					ff = new ApollonianTriangleNetwork("useCentroid");
				}
			}
			//			boolean useCP = this.colorChoice.equals("ColorPalette");			
			ff.setColorChoice(this.colorChoice);//		ff.setUseColorPalette(useCP);
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

	private void doStartAttractorsCommand() {
		String attractorSelected = this.attractorSelectionChoice;
		boolean is3D = this.isAttractorDimSpace3D;
		boolean isSingular = this.isSingularAttractor;
		boolean isPixellated = this.isAttractorPixellated;
		boolean isTimeInvariant = this.isAttractorTimeInvariant;
		boolean isTimeIterDependant = this.isAttractorTimeIterDependant;
		
		/*////////removeblow
		double x1S = Double.NaN;
		double y1S = Double.NaN;
		double z1S = Double.NaN;
		Color color1 = this.attractor1Color;
		Color color2 = this.attractor2Color;

		double x2S = Double.NaN;
		double y2S = Double.NaN;
		double z2S = Double.NaN;
		////////////////////////////
*/		List<Double> 	attr_XSeedVals 	= new ArrayList<Double>();
		List<Double> 	attr_YSeedVals 	= new ArrayList<Double>();
		List<Double> 	attr_ZSeedVals 	= new ArrayList<Double>();
		List<Color> 	attrColorVals 	= new ArrayList<Color>();

		double dt = Double.NaN;
		int maxIter = Integer.MIN_VALUE;
		List<String> space2d = null;
		
		boolean isDeJong = (attractorSelected.equals("dejong"));
		
		try {
			if (!isSingular) {
				for (JTextField xField : attrSeed_X_tfList) {
					attr_XSeedVals.add(Double.parseDouble(xField.getText()));
				}
				for (JTextField yField : attrSeed_Y_tfList) {
					attr_YSeedVals.add(Double.parseDouble(yField.getText()));
				}
				if (!isDeJong && is3D) {
					for (JTextField zField : attrSeed_Z_tfList) {
						attr_ZSeedVals.add(Double.parseDouble(zField.getText()));
					}
				} else if (!is3D) {
					for (JTextField zField : attrSeed_Z_tfList) {
						attr_ZSeedVals.add(0.0);
					}
				}
				
				for(Color aColor:attrSeed_ClrChList){
					attrColorVals.add(aColor);
				}
			} else {
				attr_XSeedVals.add(Double.parseDouble(this.attr1SeedX_tf.getText()));
				attr_YSeedVals.add(Double.parseDouble(this.attr1SeedY_tf.getText()));
				if (!isDeJong && is3D) {
					attr_ZSeedVals.add(Double.parseDouble(this.attr1SeedZ_tf.getText()));
				} else if (!is3D) {
					attr_ZSeedVals.add(0.0);
				}
				attrColorVals.add(this.attractor1Color);
			}
			
			
			/*x1S = Double.parseDouble(this.attr1SeedX_tf.getText());
			y1S = Double.parseDouble(this.attr1SeedY_tf.getText());
			z1S = !(isDeJong || is3D) ? 0 : Double.parseDouble(this.attr1SeedZ_tf.getText());

			if (!isSingular) {
				x2S = Double.parseDouble(this.attr2SeedX_tf.getText());
				y2S = Double.parseDouble(this.attr2SeedY_tf.getText());
				z2S = !(isDeJong || is3D) ? 0 : Double.parseDouble(this.attr2SeedZ_tf.getText());
			}*/
			
			dt = !isTimeInvariant ? Double.parseDouble(this.attrDeltaTime_tf.getText()) : 0;
			maxIter = Integer.parseInt(this.attrMaxIter_tf.getText());
			/*space2d = this.attractorSpace2DList;//attractorSpace2DSelectionChoice;
*/			
		} catch (NumberFormatException | NullPointerException e2) {
			e2.printStackTrace();
			JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n" + e2.getMessage(),
					"Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		int numAttractors = attr_XSeedVals.size();
		List<Attractor> attractors = new ArrayList<Attractor>(numAttractors);
		/*Attractor attractor1 = null, attractor2 = null;*/
		
		if (attractorSelected.equals("lorenz")) {
			List<LorenzAttractor> lorenzAttractors = new ArrayList<LorenzAttractor>(numAttractors);
			/*LorenzAttractor l1 = null, l2 = null;*/
			
			try {
				
				double sigma = Double.parseDouble(this.attrLorenzSigma_tf.getText());
				double beta = Double.parseDouble(this.attrLorenzBeta_tf.getText());
				double rho = Double.parseDouble(this.attrLorenzRho_tf.getText());
				
				for (int i = 0; i < numAttractors; i++) {
					
					final LorenzAttractor aLorenzAttractor = 
							new LorenzAttractor(
									attr_XSeedVals.get(i), 
									attr_YSeedVals.get(i), 
									attr_ZSeedVals.get(i), 
									attrColorVals.get(i)/*, 
									space2d*/);
					
					aLorenzAttractor.setMaxIter(maxIter);

					aLorenzAttractor.setSigma(sigma);
					aLorenzAttractor.setBeta(beta);
					aLorenzAttractor.setRho(rho);
					
					aLorenzAttractor.setIs3D(is3D);
					aLorenzAttractor.setPixellated(isPixellated);
					/*aLorenzAttractor.setInstantDraw(isInstantDraw);*/
					aLorenzAttractor.setTimeInvariant(isTimeInvariant);
					/*aLorenzAttractor.setSpace2dAxes(space2d);*/ 
					
					lorenzAttractors.add( aLorenzAttractor );
				}

				/*l1 = new LorenzAttractor(x1S, y1S, z1S, color1, space2d);
				l1.setMaxIter(maxIter);

				l1.setSigma(sigma);
				l1.setBeta(beta);
				l1.setRho(rho);
				
				l1.setIs3D(is3D);
				l1.setPixellated(isPixellated);
				
				l1.setSpace2dAxes(space2d);

				if (!isSingular) {
					l2 = new LorenzAttractor(x2S, y2S, z2S, color2, space2d);
					l2.setMaxIter(maxIter);

					l2.setSigma(sigma);
					l2.setBeta(beta);
					l2.setRho(rho);
					l2.setIs3D(is3D);
					l2.setPixellated(isPixellated);

					l2.setSpace2dAxes(space2d);
					
				}*/

			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n" + e2.getMessage(),
						"Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			for (int i = 0; i < numAttractors; i++)
				attractors.add( lorenzAttractors.get(i));
			/*attractor1 = l1;
			attractor2 = l2;*/
			
		} else if (attractorSelected.equals("aizawa")) {
			List<AizawaAttractor> aizawaAttractors = new ArrayList<AizawaAttractor>(numAttractors);
		
			/*AizawaAttractor az1 = null, az2 = null;*/
			
			try {
				double aVal = Double.parseDouble(this.attrAizawaA_tf.getText().trim());
				double bVal = Double.parseDouble(this.attrAizawaB_tf.getText().trim());
				double cVal = Double.parseDouble(this.attrAizawaC_tf.getText().trim());
				double dVal = Double.parseDouble(this.attrAizawaD_tf.getText().trim());
				double eVal = Double.parseDouble(this.attrAizawaE_tf.getText().trim());
				double fVal = Double.parseDouble(this.attrAizawaF_tf.getText().trim());
				
				for (int i = 0; i < numAttractors; i++) {
					final AizawaAttractor anAizawaAttractor  = new AizawaAttractor(
							attr_XSeedVals.get(i), attr_YSeedVals.get(i), attr_ZSeedVals.get(i), 
							attrColorVals.get(i)/*, space2d*/);
					
					anAizawaAttractor.setMaxIter(maxIter);
					
					anAizawaAttractor.setA(aVal);
					anAizawaAttractor.setB(bVal);
					anAizawaAttractor.setC(cVal);
					anAizawaAttractor.setD(dVal);
					anAizawaAttractor.setE(eVal);
					anAizawaAttractor.setF(fVal);
					
					anAizawaAttractor.setIs3D(is3D);
					anAizawaAttractor.setPixellated(isPixellated);
					/*anAizawaAttractor.setInstantDraw(isInstantDraw);*/
					anAizawaAttractor.setTimeInvariant(isTimeInvariant);
					
					/*anAizawaAttractor.setSpace2dAxes(space2d);*/
					
					aizawaAttractors.add(anAizawaAttractor);
				}
				
				/*az1 = new AizawaAttractor(x1S, y1S, z1S, color1, space2d);
				az1.setMaxIter(maxIter);
				
				az1.setA(aVal);
				az1.setB(bVal);
				az1.setC(cVal);
				az1.setD(dVal);
				az1.setE(eVal);
				az1.setF(fVal);
				
				az1.setIs3D(is3D);
				az1.setPixellated(isPixellated);
				
				az1.setSpace2dAxes(space2d);
				
				if (!isSingular) {
					az2 = new AizawaAttractor(x2S, y2S, z2S, color2, space2d);
					
					az2.setMaxIter(maxIter);
					
					az2.setA(aVal);
					az2.setB(bVal);
					az2.setC(cVal);
					az2.setD(dVal);
					az2.setE(eVal);
					az2.setF(fVal);
					
					az2.setIs3D(is3D);
					az2.setPixellated(isPixellated);
					
					az2.setSpace2dAxes(space2d);
				}*/
				
				/*attractor1 = az1;
				attractor2 = az2;*/
				
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n" + e2.getMessage(),
						"Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return;
			}

			for (int i = 0; i < numAttractors; i++)
				attractors.add( aizawaAttractors.get(i));
			
		} else if (attractorSelected.equals("dejong")) {
			List<DeJongAttractor> deJongAttractors = new ArrayList<DeJongAttractor>(numAttractors);
			
			/*DeJongAttractor d1 = null, d2 = null;*/
			
			try {
				double aVal = Double.parseDouble(this.attrDeJongA_tf.getText().trim());
				double bVal = Double.parseDouble(this.attrDeJongB_tf.getText().trim());
				double cVal = Double.parseDouble(this.attrDeJongC_tf.getText().trim());
				double dVal = Double.parseDouble(this.attrDeJongD_tf.getText().trim());
				
				for (int i = 0; i < numAttractors; i++) {									
					final DeJongAttractor aDeJongAttractor = 
							new DeJongAttractor(
									attr_XSeedVals.get(i), attr_YSeedVals.get(i), 0,//attr_ZSeedVals.get(i), 
									attrColorVals.get(i)/*, space2d*/);
					
					aDeJongAttractor.setMaxIter(maxIter);

					aDeJongAttractor.setA(aVal);
					aDeJongAttractor.setB(bVal);
					aDeJongAttractor.setC(cVal);
					aDeJongAttractor.setD(dVal);

					aDeJongAttractor.setIs3D(is3D);
					aDeJongAttractor.setPixellated(isPixellated);
					/*aDeJongAttractor.setInstantDraw(isInstantDraw);*/
					aDeJongAttractor.setTimeInvariant(isTimeInvariant);

					/*aDeJongAttractor.setSpace2dAxes(space2d);*/

					deJongAttractors.add( aDeJongAttractor);
				}
				
				/*d1 = new DeJongAttractor(x1S, y1S, z1S, color1, space2d);
				
				d1.setMaxIter(maxIter);
				
				d1.setA(aVal);
				d1.setB(bVal);
				d1.setC(cVal);
				d1.setD(dVal);
				
				d1.setIs3D(is3D);
				d1.setPixellated(isPixellated);
				
				d1.setSpace2dAxes(space2d);
				
				if (!isSingular) {
					d2 = new DeJongAttractor(x2S, y2S, z2S, color2, space2d);
					
					d2.setMaxIter(maxIter);
					
					d2.setA(aVal);
					d2.setB(bVal);
					d2.setC(cVal);
					d2.setD(dVal);
					
					d2.setIs3D(is3D);
					d2.setPixellated(isPixellated);
					
					d2.setSpace2dAxes(space2d);
					
				}
				
				attractor1 = d1;
				attractor2 = d2;*/
				
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n" + e2.getMessage(),
						"Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			for (int i = 0; i < numAttractors; i++)
				attractors.add( deJongAttractors.get(i));

		} else if (attractorSelected.equals("custom")) {
			List<CustomAttractor> customAttractors = new ArrayList<CustomAttractor>(numAttractors);
			/*CustomAttractor cust1 = null, cust2 = null;*/
			try {

				String deltaXFormula = this.attrCustom_DeltaXFormula_tf.getText().trim();
				String deltaYFormula = this.attrCustom_DeltaYFormula_tf.getText().trim();
				String deltaZFormula = is3D?this.attrCustom_DeltaZFormula_tf.getText().trim():null;
				
				for (int i = 0; i < numAttractors; i++) {
					final CustomAttractor aCustomAttractor;
					if (is3D) {
						aCustomAttractor = new CustomAttractor(
							attr_XSeedVals.get(i), attr_YSeedVals.get(i), attr_ZSeedVals.get(i), 
							attrColorVals.get(i));
					}else {
						aCustomAttractor = new CustomAttractor(
							attr_XSeedVals.get(i), attr_YSeedVals.get(i),0, 
							attrColorVals.get(i));
					}
					aCustomAttractor.setMaxIter(maxIter);
					
					aCustomAttractor.setDeltaXFormula(deltaXFormula);
					aCustomAttractor.setDeltaYFormula(deltaYFormula);
					if (is3D) {
						aCustomAttractor.setDeltaZFormula(deltaZFormula);
					}
					
					aCustomAttractor.setIs3D(is3D);
					aCustomAttractor.setPixellated(isPixellated);
					/*aCustomAttractor.setInstantDraw(isInstantDraw);*/

					aCustomAttractor.setTimeInvariant(isTimeInvariant);
					/*aCustomAttractor.setSpace2dAxes(space2d);*/
					
					aCustomAttractor.setTimeIterDependant(isTimeIterDependant);

					customAttractors.add( aCustomAttractor);
				}
				/*cust1 = new CustomAttractor(x1S, y1S, z1S, color1, space2d);
				
				cust1.setMaxIter(maxIter);
				
				cust1.setDeltaXFormula(deltaXFormula);
				cust1.setDeltaYFormula(deltaYFormula);
				if (is3D) {
					cust1.setDeltaZFormula(deltaZFormula);
				}
				
				cust1.setIs3D(is3D);
				cust1.setPixellated(isPixellated);

				cust1.setSpace2dAxes(space2d);
				
				if (!isSingular) {
					cust2 = new CustomAttractor(x2S, y2S, z2S, color2, space2d);
					
					cust2.setMaxIter(maxIter);
					
					cust2.setDeltaXFormula(deltaXFormula);
					cust2.setDeltaYFormula(deltaYFormula);
					if (is3D) {
						cust2.setDeltaZFormula(deltaZFormula);
					}
					
					cust1.setIs3D(is3D);
					cust2.setPixellated(isPixellated);

					cust2.setSpace2dAxes(space2d);
				}
				
				attractor1 = cust1;
				attractor2 = cust2;*/
				
				
			} catch (NumberFormatException | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n" + e2.getMessage(),
						"Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			for (int i = 0; i < numAttractors; i++)
				attractors.add( customAttractors.get(i));
		}
		
		this.generateAttractors(attractorSelected, attractors, this.attractorSpace2DList);//attractor1, attractor2);

		return;
	}
	
	

	private void generateAttractors(String attractorName, List<Attractor> attractors, List<String> space2DList) {
		this.generators = new AttractorsGenerator[space2DList.size()];
		/*BufferedImage[] attrImages = new BufferedImage[generators.length];
		BufferedImage combinedAttrImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		*/
		for (int i = 0; i < space2DList.size(); i++) {
			List<Attractor> attractorClones = attractors;
			String space2D = space2DList.get(i);
			generators[i] = new AttractorsGenerator(attractorName);
			generators[i].setInstantDraw(this.isAttractorInstantDraw);
			if (!this.isAttractorInstantDraw) {
				generators[i].setPauseTime(this.attractorPauseJump);
			}
			else{
				generators[i].setPauseTime(0);
				
			}
			generators[i].setAttractors(attractorClones);
			generators[i].setSpace2d(space2D);/*
System.out.println("space2DIs __ "+space2D);*/			
			SwingUtilities.invokeLater(generateAndRunAttractors(generators[i], attractorName));
			/*this.generateAttractors(attractorName, attractors);*/
			/*attrImages[i] = generators[i].getBufferedImage();*/
//			
			/*combinedAttrImage = FractalBase.joinBufferedImages(attrImages[i], combinedAttrImage);*/
		}/*
		for (int i = 0; i < space2DList.size(); i++) {
			attrImages[i] = generators[i].getBufferedImage();
		}*/
		
		//this.setFractalImage(combinedAttrImage);
		/*this.setFractalImage(FractalBase.joinBufferedImages(attrImages));*/
	}

	private Runnable generateAndRunAttractors(/*final */AttractorsGenerator generator, String attractorName) {
//		System.out.println("In generate&Run space2D is -- "+generator.getSpace2d());
		return new Runnable() {
			@Override
			public void run() {

				final JFrame frame = generator;
				frame.setTitle("Bawaz___" + attractorName.toUpperCase() + "Attractor  {"+generator.getSpace2d()+"}");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				frame.setDefaultCloseOperation(closeIt(frame));
				frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2,
						(Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2);
				frame.setResizable(false);
				frame.setVisible(true);

				/*if (fractalImage == null) {
					setFractalImage(generator.getBufferedImage());
				} else {
					setFractalImage(FractalBase.joinBufferedImages(fractalImage, generator.getBufferedImage()));
				}*/
				buClose.setEnabled(true);
				buSave.setEnabled(true);
				buSave2File.setEnabled(true);
			}
		};
	}

	private void generateAttractors(String name, List<Attractor> attractorsList) {

		final AttractorsGenerator generator = new AttractorsGenerator(name);
		generator.setInstantDraw(this.isAttractorInstantDraw);
		if (!this.isAttractorInstantDraw) {
			generator.setPauseTime(this.attractorPauseJump);
		}
		

		/*final boolean isSingular = this.isSingularAttractor;*/

		generator.setAttractors(attractorsList);
		/*if (!isSingular) {
		}else{
			
		}*/
			

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {

					final JFrame frame = generator;
					frame.setTitle("Bawaz___" + name.toUpperCase() + "Attractor");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setResizable(false);
					frame.setVisible(true);

					frame.setDefaultCloseOperation(closeIt(frame));
					frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2,
							(Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2);
					frame.setResizable(false);
					frame.setVisible(true);

					setFractalImage(generator.getBufferedImage());
					// this.setFractalBase(frame);

					buClose.setEnabled(true);
				}
			});
		
	}

	//TODO	-	pass	array
	private void generateAttractors(String name, Attractor a1, Attractor a2) {
		final AttractorsGenerator generator = new AttractorsGenerator(name);

		final boolean isSingular = this.isSingularAttractor;

		if (!isSingular) {
			generator.setAttractors(new Attractor[] { a1, a2 });
			/*
			 * generator.setMaxIter(maxIter); 
			 * generator.setSpace2d(space2d);
			 */
		} else {
			generator.setAttractors(new Attractor[] { a1 });
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				final JFrame frame = generator;
				frame.setTitle("Bawaz___" + name.toUpperCase() + "Attractor");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.setVisible(true);

				frame.setDefaultCloseOperation(closeIt(frame));
				frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth()) / 2,
						(Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight()) / 2);
				frame.setResizable(false);
				frame.setVisible(true);

				setFractalImage(generator.getBufferedImage());
				// this.setFractalBase(frame);

				buClose.setEnabled(true);
			}
		});
	}

	private FractalBase startDIYApollo() {
		FractalBase ff;
		boolean useCP = this.colorChoice.equals("ColorPalette");
		
		double c1 = this.diyApolloC1;
		double c2 = this.diyApolloC2;
		double c3 = this.diyApolloC3;
		double mult = this.diyApolloMult;
		ff = new ApollonianCircles(new double[] {c1,c2,c3}, mult);
		ff.setColorChoice(this.colorChoice);	//ff.setUseColorPalette(useCP);
		return ff;
	}
	
	private void addUseLyapunovInfo(){
		this.formulaArea.append(eol+"<font color='red'>Lyapunov Exponent used:</font>"+eol);
	}

	
	private void addPixelConstantOperationInfo(String pxConstOprnChoice) {		
//		 PIX_CONST_OPRNS = new String[]{"Plus","Minus","Multiply","Divide","Power"};
		switch(pxConstOprnChoice){
			case	"Plus"	:	
				this.formulaArea.append("<br/><font color='blue'>Pixel Constant Operation "+
						this.pxConstOprnChoice+
						"<br/>   f(Z) = z + C</font><br/>");
				break;
			case	"Minus"	:	
				this.formulaArea.append("<br/><font color='blue'>Pixel Constant Operation "+
						this.pxConstOprnChoice+
						"<br/>   f(Z) = z - C</font><br/>");
				break;
			case	"Multiply"	:	
				this.formulaArea.append("<br/><font color='blue'>Pixel Constant Operation "+
						this.pxConstOprnChoice+
						"<br/>   f(Z) = z * C</font><br/>");
				break;
			case	"Divide"	:	
				this.formulaArea.append("<br/><font color='blue'>Pixel Constant Operation "+
						this.pxConstOprnChoice+
						"<br/>   f(Z) = z / C</font><br/>");
				break;
			default:
				this.formulaArea.append("<br/><font color='blue'>Pixel Constant Operation "+
						this.pxConstOprnChoice+
						"<br/>   f(Z) = z + C</font><br/>");
				break;
		}
		
	}
	
	//String[] PIX_TRANSFORM_OPTIONS = {"none", "absolute", "reciprocal", "square", "root", "exponent", "log(10)", "log(e)", 
	//"sine", "cosine", "tangent", "arcsine", "arccosine", "arctangent"};
	
	
	private void addXtrYtrXYInfroInfo(String xTrans, String yTrans, String xyOp) {
		String xVal = "x";
		String yVal = "y";
		String opVal = " + ";
		
//	case "none": this.formulaArea.append(eol+"<font color='black'>x = x</font>"+eol); break;
		switch( xTrans ) {
			case "none": break;
			case "absolute": 	xVal =" | "+xVal+" | "; break;
			case "absoluteSquare": 	xVal =" | "+xVal+" | <sup>2</sup> "; break;
			case "reciprocal": 	xVal =" 1 / "+xVal+" "; break;
			case "reciprocalSquare": 	xVal =" (1 / "+xVal+") <sup>2</sup>  "; break;
			case "square": 		xVal = " "+xVal+" <sup>2</sup> "; break;
			case "cube": 		xVal = " "+xVal+" <sup>3</sup> "; break;
			case "root": 		xVal = " sqrt( "+xVal+" ) "; break;
			case "exponent": 	xVal = " e <sup>"+xVal+"</sup> "; break;
			case "log(10)": 	xVal = " log <sub>10</sub>( "+xVal+" ) "; break;
			case "log(e)": 		xVal = " log <sub>e</sub>( "+xVal+" ) "; break;
			case "sine": 		xVal = " sin( "+xVal+" ) "; break;
			case "cosine": 		xVal = " cos( "+xVal+" ) "; break;			
			case "tangent": 	xVal = " tan( "+xVal+" ) "; break;
			case "cosec": 		xVal = " cosec( "+xVal+" ) "; break;
			case "sec": 		xVal = " sec( "+xVal+" ) "; break;			
			case "cot": 		xVal = " cot( "+xVal+" ) "; break;
			case "sinh": 		xVal = " sinh( "+xVal+" ) "; break;
			case "cosh": 		xVal = " cosh( "+xVal+" ) "; break;			
			case "tanh": 		xVal = " tanh( "+xVal+" ) "; break;
			case "arcsine": 	xVal = " asin( "+xVal+" ) "; break;
			case "arccosine": 	xVal = " acos( "+xVal+" ) "; break;
			case "arctangent": 	xVal = " atan2( "+xVal+" ) "; break;
			case "arcsinh": 	xVal = " asinh( "+xVal+" ) "; break;
			case "arccosh": 	xVal = " acosh( "+xVal+" ) "; break;
			case "arctanh": 	xVal = " atanh( "+xVal+" ) "; break;
			
			default:  break;
		}
		
		switch( yTrans ) {
			case "none": break;
			case "absolute": 	yVal =" | "+yVal+" | i "; break;
			case "absoluteSquare": 	yVal =" | "+yVal+" | <sup>2</sup> i "; break;
			case "reciprocal": 	yVal =" (1 / "+yVal+") i "; break;
			case "reciprocalSquare": 	yVal =" (1 / "+yVal+") <sup>2</sup> i "; break;
			case "square": 		yVal = " "+yVal+" <sup>2</sup> i "; break;
			case "cube": 		yVal = " "+yVal+" <sup>3</sup> i "; break;
			case "root": 		yVal = " sqrt( "+yVal+" ) i "; break;
			case "exponent": 	yVal = " e <sup>"+yVal+"</sup> i "; break;
			case "log(10)": 	yVal = " log <sub>10</sub>( "+yVal+" ) i "; break;
			case "log(e)": 		yVal = " log <sub>e</sub>( "+yVal+" ) i "; break;
			case "sine": 		yVal = " sin( "+yVal+" ) i "; break;
			case "cosine": 		yVal = " cos( "+yVal+" ) i "; break;			
			case "tangent": 	yVal = " tan( "+yVal+" ) i "; break;
			
			case "cosec": 		yVal = " cosec( "+yVal+" ) i "; break;
			case "sec": 		yVal = " sec( "+yVal+" ) i "; break;			
			case "cot": 		yVal = " cot( "+yVal+" ) i "; break;
			case "sinh": 		yVal = " sinh( "+yVal+" ) i "; break;
			case "cosh": 		yVal = " cosh( "+yVal+" ) i "; break;			
			case "tanh": 		yVal = " tanh( "+yVal+" ) i "; break;

			case "arcsine": 	yVal = " asin( "+yVal+" ) i "; break;
			case "arccosine": 	yVal = " acos( "+yVal+" ) i "; break;
			case "arctangent": 	yVal = " atan2( "+yVal+" ) i "; break;
			
			case "arcsinh": 	yVal = " asinh( "+yVal+" ) i "; break;
			case "arccosh": 	yVal = " acosh( "+yVal+" ) i "; break;
			case "arctanh": 	yVal = " atanh( "+yVal+" ) i "; break;
			
			default:  break;
		}
		//String[] OPERATIONS = new String[]{"Plus","Minus","Multiply","Divide","Power"};
		switch( xyOp ) {
			case "Plus":	break;
			case "Minus":		opVal = " - "; break;
			case "Multiply":	opVal = " * "; break;
			case "Divide":		opVal = " / "; break;
			case "Power":		opVal = " ^ "; yVal="<sup> "+yVal+"</sup> ";break;
		}
			
		this.formulaArea.append(eol+"<font color='black'>z = "+xVal+" "+opVal+" "+yVal+"</font>"+eol);	
	}
	
	private void addInvertPixelCalcInfo() {
		boolean invertPix = this.invertPixelCalculation;
		String opVal = " + ";
		switch( this.pixIntraXYOperation ) {
			case "Plus":	break;
			case "Minus":		opVal = " - "; break;
			case "Multiply":	opVal = " * "; break;
			case "Divide":		opVal = " / "; break;
			case "Power":		opVal = " ^ "; ;break;
		}
			
		if (invertPix) {
			if (!this.pixIntraXYOperation.equals("Power")) {
				this.formulaArea.append(eol + "<font color='red'>Pixel Calculation reversed  z = y " + opVal + " i*x + C</font>" + eol);
			} else {
				this.formulaArea.append(eol + "<font color='red'>Pixel Calculation reversed  z = y " + opVal + "<sup> i*x</sup> + C</font>" + eol);
			}
		} else {
			if (!this.pixIntraXYOperation.equals("Power")) {
				this.formulaArea.append(eol + "<font color='red'>Pixel Calculation z = x " + opVal + " i*y + C</font>" + eol);
			} else {
				this.formulaArea.append(eol + "<font color='red'>Pixel Calculation z = x " + opVal + "<sup> i*y</sup> + C</font>" + eol);
			}
		}
	}

	private void addPixelFuncInfo(String pxFunc) {
		if ((!this.diyJApplyFormulaZ && this.diyJuliaRb.isSelected())
				|| (!this.diyMandApplyFormulaZ && this.diyMandRb.isSelected())
				||(!this.polyApplyFormulaZ && this.comboChoice.equals(POLY))) {
			switch (pxFunc) {
				case 	"sine":				this.formulaArea.append("<br/><font color='black'><b>z = sin(Z)</font></b><br/>");break;
				case 	"cosine":			this.formulaArea.append("<br/><font color='black'><b>z = cos(Z)</font></b><br/>");break;
				case	"tan":				this.formulaArea.append("<br/><font color='black'><b>z = tan(Z)</font></b><br/>");break;

				case 	"cosec":			this.formulaArea.append("<br/><font color='black'><b>z = cosec(Z)</font></b><br/>");break;
				case 	"sec":				this.formulaArea.append("<br/><font color='black'><b>z = sec(Z)</font></b><br/>");break;
				case	"cot":				this.formulaArea.append("<br/><font color='black'><b>z = cot(Z)</font></b><br/>");break;

				case 	"sinh":				this.formulaArea.append("<br/><font color='black'><b>z = sinh(Z)</font></b><br/>");break;
				case 	"cosh":				this.formulaArea.append("<br/><font color='black'><b>z = cosh(Z)</font></b><br/>");break;
				case	"tanh":				this.formulaArea.append("<br/><font color='black'><b>z = tanh(Z)</font></b><br/>");break;
				
				case	"arcsine":			this.formulaArea.append("<br/><font color='black'><b>z = arcsin(Z)</font></b><br/>");break;
				case	"arccosine":		this.formulaArea.append("<br/><font color='black'><b>z = arccos(Z)</font></b><br/>");break;
				case	"arctan":			this.formulaArea.append("<br/><font color='black'><b>z = arctan(Z)</font></b><br/>");break;

				case	"arcsinh":			this.formulaArea.append("<br/><font color='black'><b>z = arcsinh(Z)</font></b><br/>");break;
				case	"arccosh":			this.formulaArea.append("<br/><font color='black'><b>z = arccosh(Z)</font></b><br/>");break;
				case	"arctanh":			this.formulaArea.append("<br/><font color='black'><b>z = arctanh(Z)</font></b><br/>");break;
				
				
				case	"reciprocal":		this.formulaArea.append("<br/><font color='black'><b>z = (1 /Z)</font></b><br/>");break;
				case	"reciprocalSquare":	this.formulaArea.append("<br/><font color='black'><b>z = (1 /Z) <sup>2</sup></font></b><br/>");break;
				case	"square":			this.formulaArea.append("<br/><font color='black'><b>z = (Z <sup>2</sup>)</font></b><br/>");break;
				case	"cube":				this.formulaArea.append("<br/><font color='black'><b>z = (Z <sup>3</sup>)</font></b><br/>");break;
				case	"exponent(e)":		this.formulaArea.append("<br/><font color='black'><b>z = e<sup>(Z)</sup></font></b><br/>");break;
				case	"root":				this.formulaArea.append("<br/><font color='black'><b>z = root(Z)</font></b><br/>");break;
				case	"log(e)":			this.formulaArea.append("<br/><font color='black'><b>z = log(Z)</font></b><br/>");break;
				default	:					this.formulaArea.append("<br/>");break;			
			}
		} else {
			this.formulaArea.append("<br/><font color='black'><b>f(Z) = " + pxFunc + "</font></b><br/>");
		}
	}
	private FractalBase createDIYJulia() {
		FractalBase ff;
		//diyJulia
		boolean doExplore = this.diyJuliaExplore;
		boolean useRngEst = this.useRangeEstimation;
		boolean useCP = this.colorChoice.equals("ColorPalette");
		boolean useBw = this.colorChoice.equals("BlackWhite");	
		
		boolean useSample = this.colorChoice.equals("SampleMix");
		boolean useColorBlowout = this.colorChoice.equals("ColorBlowout");
		boolean useColorSuperBlowout = this.colorChoice.equals("ColorSuperBlowout");
		
		boolean doSmoothen = this.smoothenColor;
		
		String pxConstOp = this.pxConstOprnChoice;
		String func = this.constFuncChoice;
		String pxFunc = !this.diyJApplyFormulaZ ? this.pxFuncChoice : this.diyJApplyFormulaTf.getText().trim();
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
		
		boolean capOrb = this.captureOrbit;
		
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>(DIY)Julia Set:<br/>");
		if (this.diyJuliaUseLyapunovExponent) {
			this.addUseLyapunovInfo();
		}
		
		this.addXtrYtrXYInfroInfo(this.pixXTransform,this.pixYTransform,this.pixIntraXYOperation);
		
		this.addInvertPixelCalcInfo();
		this.addPixelConstantOperationInfo(this.pxConstOprnChoice);

		this.addPixelFuncInfo(pxFunc);
		
		if (diyJApplyFatou) {
			this.formulaArea.append("<br/><font color='green'>Fatou Field Lines:<br/><br/>f(z) = (1 - (z<sup>3</sup>/6)) / ((z - (z<sup>2</sup>/2)) <sup>2</sup>)");// + C</font><br/>");			
		} else if (diyJApplyZSq) {
			this.formulaArea.append("<br/><font color='green'>ZSquared Field Lines:<br/><br/>f(z) = (z <sup>2</sup>) <sup> " + diyJuliaP+"</sup>");// + " + C</font><br/>");
		} else if (diyJApplyClassic) {
			this.formulaArea.append("<br/><font color='green'>Classic Julia:<br/><br/>f(z) = z<sup>4</sup> + z<sup>3</sup>/(z-1) + z<sup>2</sup>/(z<sup>3</sup> + 4*z<sup>2</sup> + 5)");//  + C</font><br/>");
		} else {
			this.formulaArea.append("<br/><font color='green'>f(z) = z <sup>" + diyJuliaP+"</sup>");// + " + C</font><br/>");
		}
		
		switch(this.pxConstOprnChoice){
			case	"Plus"	:	
				this.formulaArea.append(" + C</font><br/>");
				break;
			case	"Minus"	:	
				this.formulaArea.append(" - C</font><br/>");
				break;
			case	"Multiply"	:	
				this.formulaArea.append(" * C</font><br/>");
				break;
			case	"Divide"	:	
				this.formulaArea.append(" / C</font><br/>");
				break;
			default:
				this.formulaArea.append(" + C</font><br/>");
				break;
		}
		
		
		
		this.addDiyJuliaUseDiffInfo();

		boolean diyJKConst = this.keepConst;
		if (diyJKConst) {
			this.formulaArea.append("<br/>Dynamic Constant = z</font>");
			ff = new Julia(diyJuliaP, diyJuliaUseD, diyJuliaBd, diyJKConst);
		} else {
			double diyJuliaRealVal = 0;
			double diyJuliaImgVal = 0;
			try {
				diyJuliaRealVal = Double.parseDouble(this.diyJuliaRealTf.getText());
				diyJuliaImgVal = Double.parseDouble(this.diyJuliaImgTf.getText());
			} catch (NumberFormatException  | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			this.formulaArea.append("<br/>Constant = " + diyJuliaRealVal + " + (" + diyJuliaImgVal + " * i)</font>");
			ff = new Julia(diyJuliaP, diyJuliaUseD, diyJuliaBd, diyJuliaRealVal, diyJuliaImgVal);
		}
		
		ff.setCaptureOrbit(capOrb);
		if(capOrb){
			ff.setOrbitTrapPoint(this.orbitTrapPointChoice);
			ff.setTrapSize(this.trapSizeChoice);
			ff.setTrapShape(this.trapShapeChoice);
		}
		
		ff.setSmoothen(doSmoothen);

		if (doExplore) {
			ff.setUseJuliaExplorer(true);
		}

		ff.setUseLyapunovExponent(this.diyJuliaUseLyapunovExponent);
		ff.setPxXTransformation(this.pixXTransform);
		ff.setPxYTransformation(this.pixYTransform);
		ff.setPixXYOperation(this.pixIntraXYOperation);
		
		
		ff.setReversePixelCalculation(this.invertPixelCalculation);
		
		ff.setColorChoice(this.colorChoice);
		if (useSample) {
//			ff.setColorChoice(this.colorChoice);	//	
			this.setSampleColorMix(ff);
		}
		if(useColorBlowout){
			ff.setColorBlowoutType(this.colorBlowoutChoice);
		}
		if(useColorSuperBlowout){
			ff.setColorBlowoutType(this.colorSuperBlowoutChoice);
		}
		
		/*
		if (useCP) {
			ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setColorChoice(this.colorChoice);	//	ff.setUseBlackWhite(useBw);
			} else {
				if (useSample) {
					ff.setColorChoice(this.colorChoice);	//	
					this.setSampleColorMix(ff);
				} else {

					ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
				}
			}
		}
		*/
		ff.setPxConstOperation(pxConstOp);
		ff.setUseFuncConst(func);
		ff.setApplyCustomFormula(this.diyJApplyFormulaZ);
		ff.setUseFuncPixel(pxFunc);
		ff.setRotation(rot);
		
		ff.setMaxIter(diyJuliaMaxIt);
		ff.setAreaSize(diyJuliaLoopLt);
		ff.setxC(diyJXc);
		ff.setyC(diyJYc);
		ff.setScaleSize(diyJScale);
		
		Julia jj = (Julia)ff;
		
		if (!diyJApplyField.equals("None")) {
			if (diyJApplyFatou) {
				jj.setFatou(diyJApplyFatou);
				jj.setZSq(false);
				jj.setClassicJulia(false);
			} else if (diyJApplyZSq) {
				jj.setZSq(diyJApplyZSq);
				jj.setFatou(false);
				jj.setClassicJulia(false);
			} else if (diyJApplyClassic) {
				jj.setClassicJulia(diyJApplyClassic);
				jj.setFatou(false);
				jj.setZSq(false);
			}
		} else {
			jj.setFatou(false);
			jj.setZSq(false);
			jj.setClassicJulia(false);

		}
		
		this.addJuliaConstInfo(jj);
		ff.setSavePixelInfo2File(this.savePixelInfo2File);

		
		if (useRngEst) {
			try {
				double xMinRealVal = Double.parseDouble(this.xMinTf.getText());
				double yMinImgVal = Double.parseDouble(this.yMinTf.getText());
				double xMaxRealVal = Double.parseDouble(this.xMaxTf.getText());
				double yMaxImgVal = Double.parseDouble(this.yMaxTf.getText());

				if (xMinRealVal >= xMaxRealVal || yMinImgVal >= yMaxImgVal) {
					String err = (xMinRealVal < xMaxRealVal) ? "xMin > xMax" : "yMin > yMax";
					JOptionPane.showMessageDialog(null, "Please enter a valid Range value", err, JOptionPane.ERROR_MESSAGE);
					return null;
				}

				jj.setRefocusDraw(true);
				jj.setRangeMinMaxVals(xMinRealVal, yMinImgVal, xMaxRealVal, yMaxImgVal);
				jj.createFocalFractalShape(ff, new ComplexNumber(xMinRealVal,yMinImgVal), new ComplexNumber(xMaxRealVal,yMaxImgVal));
			} catch (NumberFormatException  | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		ff = null;

		jj.setVisible(true);
		
		return jj;
	}
	

	
	private void setColorBlowoutTypeVals(FractalBase ff) {
		this.colorBlowoutChoice=ff.getColorBlowoutType();
	}
	
	private void setColorSuperBlowoutTypeVals(FractalBase ff) {
		this.colorSuperBlowoutChoice=ff.getColorBlowoutType();
	}
	
	private void setSampleStart_DivVals(FractalBase ff) {
		final int[] rgbStartVals = ff.getRgbStartVals();
		if (rgbStartVals == FractalBase.POW2_4_200) {
			this.colorSampleMixStartVals = "POW2_4_200";
		} else if (rgbStartVals == FractalBase.POW2_2_128) {
			this.colorSampleMixStartVals = "POW2_2_128";
		} else if (rgbStartVals == FractalBase.POW2_2_F4) {
			this.colorSampleMixStartVals = "POW2_2_F4";
		} else if (rgbStartVals == FractalBase.POW3_3_243) {
			this.colorSampleMixStartVals = "POW3_3_243";
		} else if (rgbStartVals == FractalBase.EQUAL_PARTS_40) {
			this.colorSampleMixStartVals = "EQUAL_PARTS_40";
		} else if (rgbStartVals == FractalBase.EQUAL_PARTS_40) {
			this.colorSampleMixStartVals = "EQUAL_PARTS_40";
		} else if (rgbStartVals == FractalBase.EQUAL_PARTS_25) {
			this.colorSampleMixStartVals = "EQUAL_PARTS_25";
		}

		final int[] divVals = ff.getRgbDivisors();
		if (divVals == FractalBase.FRST_SIX_PRIMES) {
			this.colorSampleDivVals = "FRST_SIX_PRIMES";
		} else if (divVals == FractalBase.FRST_SIX_ODDS) {
			this.colorSampleDivVals = "FRST_SIX_ODDS";
		} else if (divVals == FractalBase.FRST_SIX_FIBS) {
			this.colorSampleDivVals = "FRST_SIX_FIBS";
		} 
	}

	private void setSampleColorMix(FractalBase ff) {
		int[] startVals = null;
		switch(this.colorSampleMixStartVals) {
			case "POW2_4_200":	startVals = FractalBase.POW2_4_200; break;
			case "POW2_2_128":  startVals = FractalBase.POW2_2_128; break;
			case "POW2_2_F4":   startVals = FractalBase.POW2_2_F4; break;
			case "POW3_3_243":	startVals = FractalBase.POW3_3_243; break;
			case "EQUAL_PARTS_40":	startVals = FractalBase.EQUAL_PARTS_40; break;
			case "EQUAL_PARTS_50":			startVals = FractalBase.EQUAL_PARTS_50; break;
			case "EQUAL_PARTS_25":			startVals = FractalBase.EQUAL_PARTS_25; break;
			default: startVals = FractalBase.POW2_4_200; break; 
		}
		
		int[] divVals = null;
		switch(this.colorSampleDivVals) {
			case "FRST_SIX_PRIMES": divVals = FractalBase.FRST_SIX_PRIMES; break;
			case "FRST_SIX_ODDS": divVals = FractalBase.FRST_SIX_ODDS; break;
			case "FRST_SIX_FIBS": divVals = FractalBase.FRST_SIX_FIBS; break;
			default: divVals = FractalBase.FRST_SIX_PRIMES; break;
		}					
		
		ff.setRgbStartVals(startVals);
		ff.setRgbDivisors(divVals);
		
	}
	
	private FractalBase createDIYMandelbrot() {
		FractalBase ff = null;
		// for diy mandelbrot
		boolean doExplore = this.diyMandExplore;
		boolean useRngEst = this.useRangeEstimation;
		boolean useCP = this.colorChoice.equals("ColorPalette");
		boolean useBw = this.colorChoice.equals("BlackWhite");
		
		boolean useSample = this.colorChoice.equals("SampleMix");
		boolean useColorBlowout = this.colorChoice.equals("ColorBlowout");
		boolean useColorSuperBlowout = this.colorChoice.equals("ColorSuperBlowout");

		boolean doSmoothen = this.smoothenColor;

		String pxConstOp = this.pxConstOprnChoice;
		String func = this.constFuncChoice;
		String pxFunc = !this.diyMandApplyFormulaZ ? this.pxFuncChoice : this.diyMandApplyFormulaTf.getText().trim();

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
		
		boolean isBuddha = this.diyMandIsBuddhabrot;
		
		boolean capOrb = this.captureOrbit;

		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>(DIY)Mandelbrot Set:<br/><br/>f(z) = z <sup>" + diyMandExp + "</sup> + C");
		

		if(isBuddha){
			this.formulaArea.append(eol+"<font color='green'>BUDDHA<i>brot</i></font>"+eol);
		}
		
		if (this.diyMandUseLyapunovExponent) {
			this.addUseLyapunovInfo();
		}
		
		this.addXtrYtrXYInfroInfo(this.pixXTransform,this.pixYTransform,this.pixIntraXYOperation);
		
		this.addInvertPixelCalcInfo();

		this.addPixelConstantOperationInfo(this.pxConstOprnChoice);
		this.addPixelFuncInfo(pxFunc);
		
		this.addDiyMandelbrotUseDiffInfo();
		
		if (diyMKConst) {
			this.formulaArea.append("<br/>Dynamic Constant = z</font>");
			ff = new Mandelbrot(diyMag, diyMandExp, diyMandUseD,diyMandB, diyMKConst);			
		} else {
			try {
				double diyMRealVal = Double.parseDouble(this.diyMandRealTf.getText());
				double diyMImgVal = Double.parseDouble(this.diyMandImgTf.getText());
				this.formulaArea.append("<br/>Constant = " + diyMRealVal + " + (" + diyMImgVal + " * i)</font>");
				ff = new Mandelbrot(diyMag, diyMandExp, diyMandUseD,diyMandB, diyMRealVal, diyMImgVal);
			} catch (NumberFormatException  | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		ff.setCaptureOrbit(capOrb);
		if(capOrb){
			ff.setOrbitTrapPoint(this.orbitTrapPointChoice);
			ff.setTrapSize(this.trapSizeChoice);
			ff.setTrapShape(this.trapShapeChoice);
		}

		ff.setSmoothen(doSmoothen);
		
		if (doExplore) {
			ff.setUseMandelbrotExplorer(true);
		}
		
		if (isBuddha) {
			Mandelbrot m = (Mandelbrot) ff;
			m.setBuddha(isBuddha);
			ff = m;
		}
		
		ff.setUseLyapunovExponent(this.diyMandUseLyapunovExponent);
		ff.setPxXTransformation(this.pixXTransform);
		ff.setPxYTransformation(this.pixYTransform);
		ff.setPixXYOperation(this.pixIntraXYOperation);
		
		ff.setPxConstOperation(pxConstOp);
		ff.setReversePixelCalculation(this.invertPixelCalculation);
		
		ff.setColorChoice(this.colorChoice);
		if (useSample) {
//			ff.setColorChoice(this.colorChoice);	//	
			this.setSampleColorMix(ff);
		}
		if(useColorBlowout){
			ff.setColorBlowoutType(this.colorBlowoutChoice);
		}
		if(useColorSuperBlowout){
			ff.setColorBlowoutType(this.colorSuperBlowoutChoice);
		}
		/*
		if (useCP) {
			ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setColorChoice(this.colorChoice);	//	ff.setUseBlackWhite(useBw);
			} else {
				if (useSample) {
					ff.setColorChoice(this.colorChoice);	//	
					this.setSampleColorMix(ff);
				} else {

					ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
				}
			}
		}
		*/
		ff.setUseFuncConst(func);
		ff.setApplyCustomFormula(this.diyMandApplyFormulaZ);
		ff.setUseFuncPixel(pxFunc);
		ff.setRotation(rot);
		ff.setMaxIter(diyMaxIt);
		ff.setAreaSize(diyMandLoopLt);
		ff.setxC(diyMXc);
		ff.setyC(diyMYc);
		ff.setScaleSize(diyMScale);

		this.addMandelbrotConstInfo(ff);
		ff.setSavePixelInfo2File(this.savePixelInfo2File);

		
		if (useRngEst) {
			try {
				double xMinRealVal = Double.parseDouble(this.xMinTf.getText());
				double yMinImgVal = Double.parseDouble(this.yMinTf.getText());
				double xMaxRealVal = Double.parseDouble(this.xMaxTf.getText());
				double yMaxImgVal = Double.parseDouble(this.yMaxTf.getText());

				if (xMinRealVal >= xMaxRealVal || yMinImgVal >= yMaxImgVal) {
					String err = (xMinRealVal < xMaxRealVal) ? "xMin > xMax" : "yMin > yMax";
					JOptionPane.showMessageDialog(null, "Please enter a valid Range value", err, JOptionPane.ERROR_MESSAGE);
					return null;
				}
				Mandelbrot mand = (Mandelbrot)ff;
				mand.setRefocusDraw(true);
				mand.setRangeMinMaxVals(xMinRealVal, yMinImgVal, xMaxRealVal, yMaxImgVal);
				mand.createFocalFractalShape(ff, new ComplexNumber(xMinRealVal,yMinImgVal), new ComplexNumber(xMaxRealVal,yMaxImgVal));
				
				//ff = new Mandelbrot(diyMag, diyMandExp, diyMandUseD,diyMandB, diyMRealVal, diyMImgVal);
			} catch (NumberFormatException  | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		return ff;
	}

	private FractalBase startJulia() {
		boolean doExplore = this.juliaExplore;
		boolean doSmoothen = this.smoothenColor;
		
		int pow = this.getPower();
		double con = this.getComplexConst();
		String comp = this.getComplex();
		boolean jUseD = this.getJUseDiff();
		int juliaMax = this.juliaMaxIter;
		int juliaLoopLt = this.juliaSize;
		double jBound = this.juliaBound;
		
		double jXc = this.juliaXC;
		double jYc = this.juliaYC;
		double jScale = this.juliaScaleSize;
		
		String jApplyField = this.fieldLines;
		boolean jApplyFatou = this.applyFatou;
		boolean jApplyZSq = this.applyZSq;
		boolean jApplyClassic = this.applyClassicJulia;

		boolean useCP = this.colorChoice.equals("ColorPalette");
		boolean useBw = this.colorChoice.equals("BlackWhite");
		boolean useSample = this.colorChoice.equals("SampleMix");
		boolean useColorBlowout = this.colorChoice.equals("ColorBlowout");
		boolean useColorSuperBlowout = this.colorChoice.equals("ColorSuperBlowout");
		
		String pxConstOp = this.pxConstOprnChoice;
		String func = this.constFuncChoice;
		String pxFunc = this.pxFuncChoice;
		
		String pxXTrans = this.pixXTransform;
		String pxYTrans = this.pixYTransform;
		String pxIntraOp = this.pixIntraXYOperation;
		
		double rot = this.getRotation();
		boolean capOrb = this.captureOrbit;
		
		
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
		
		ff.setCaptureOrbit(capOrb);
		if(capOrb){
			ff.setOrbitTrapPoint(this.orbitTrapPointChoice);
			ff.setTrapSize(this.trapSizeChoice);
			ff.setTrapShape(this.trapShapeChoice);
		}

		ff.setSmoothen(doSmoothen);

		if (doExplore) {
			ff.setUseJuliaExplorer(true);
		}
		
		
		ff.setUseLyapunovExponent(this.juliaUseLyapunovExponent);
		ff.setPxXTransformation(this.pixXTransform);
		ff.setPxYTransformation(this.pixYTransform);
		ff.setPixXYOperation(this.pixIntraXYOperation);
		
		ff.setPxConstOperation(pxConstOp);
		
		boolean invertPix = this.invertPixelCalculation;
		ff.setReversePixelCalculation(invertPix);
		
		ff.setColorChoice(this.colorChoice);
		if (useSample) {
//			ff.setColorChoice(this.colorChoice);	//	
			this.setSampleColorMix(ff);
		}
		if(useColorBlowout){
			ff.setColorBlowoutType(this.colorBlowoutChoice);
		}
		if(useColorSuperBlowout){
			ff.setColorBlowoutType(this.colorSuperBlowoutChoice);
		}
		/*
		if (useCP) {
			ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setColorChoice(this.colorChoice);	//	ff.setUseBlackWhite(useBw);
			} else {
				if (useSample) {
					ff.setColorChoice(this.colorChoice);	//	
					this.setSampleColorMix(ff);
				} else {

					ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
				}
			}
		}
		*/
		ff.setUseFuncConst(func);
		ff.setUseFuncPixel(pxFunc);
		ff.setRotation(rot);
		
		ff.setUseLyapunovExponent(this.juliaUseLyapunovExponent);
		
		ff.setxC(jXc);
		ff.setyC(jYc);
		ff.setScaleSize(jScale);
		ff.setMaxIter(juliaMax);
		ff.setAreaSize(juliaLoopLt);
		
		Julia jj = (Julia)ff;
		
		if (!jApplyField.equals("None")) {
			if (jApplyFatou) {
				jj.setFatou(jApplyFatou);
				jj.setZSq(false);
				jj.setClassicJulia(false);
			} else if (jApplyZSq) {
				jj.setZSq(jApplyZSq);
				jj.setFatou(false);
				jj.setClassicJulia(false);
			} else if (jApplyClassic) {
				jj.setClassicJulia(jApplyClassic);
				jj.setFatou(false);
				jj.setZSq(false);
			}
		} else {
			jj.setFatou(false);
			jj.setZSq(false);
			jj.setClassicJulia(false);

		}

		this.addJuliaConstInfo(jj);
		ff.setSavePixelInfo2File(this.savePixelInfo2File);
		
		ff = null;
		
		jj.setVisible(true);
		return jj;
	}

	private FractalBase startMandelbrot() {
		boolean doExplore = this.mandExplore;
		boolean doSmoothen = this.smoothenColor;
		
		int mag = this.getMagnification();
		int exp = this.getExponent();
		boolean mUseD = this.getMUseDiff();
		int mandMax = this.mandMaxIter;
		int mandLoopLt = this.mandSize;
		double mBound=this.mandBound;
		double mXc = this.mandXC;
		double mYc = this.mandYC;
		double mScale = this.mandScaleSize;
		boolean useCP = this.colorChoice.equals("ColorPalette");
		boolean useBw = this.colorChoice.equals("BlackWhite");	
		boolean useSample = this.colorChoice.equals("SampleMix");
		boolean useColorBlowout = this.colorChoice.equals("ColorBlowout");
		boolean useColorSuperBlowout = this.colorChoice.equals("ColorSuperBlowout");
		
		String pxXTrans = this.pixXTransform;
		String pxYTrans = this.pixYTransform;
		String pxIntraOp = this.pixIntraXYOperation;
			
		String pxConstOp = this.pxConstOprnChoice;
		String func = this.constFuncChoice;	
		String pxFunc = this.pxFuncChoice;
		double rot = this.getRotation();
		
		boolean isBuddha = this.mandIsBuddhabrot();
		boolean isInMotion = this.isMandIsMotionbrot();
		String motParam = this.getMandMotionParam();
		double motParamJumpVal = this.getMandMotionParamJumpVal();

		boolean capOrb = this.captureOrbit;
		
		FractalBase ff;
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		
		if (pxConstOp.equals("Plus")) {
			this.formulaArea.setText("<font color='blue'>Mandelbrot Set:<br/><br/>f(z) = z <sup>" + exp + "</sup> + C");
		} else if (pxConstOp.equals("Minus")) {
			this.formulaArea.setText("<font color='blue'>Mandelbrot Set:<br/><br/>f(z) = z <sup>" + exp + "</sup> - C");
		} else if (pxConstOp.equals("Multiply")) {
			this.formulaArea.setText("<font color='blue'>Mandelbrot Set:<br/><br/>f(z) = z  <sup>" + exp + "</sup> * C");
		} else if (pxConstOp.equals("Divide")) {
			this.formulaArea.setText("<font color='blue'>Mandelbrot Set:<br/><br/>f(z) = z  <sup>" + exp + "</sup> / C");
		}
		
		if(isBuddha){
			this.formulaArea.append(eol+"<font color='green'>BUDDHAbrot</font>"+eol);
		}
		

		if (this.mandUseLyapunovExponent) {
			this.addUseLyapunovInfo();
		}
		
		this.addXtrYtrXYInfroInfo(pxXTrans,pxYTrans,pxIntraOp);
		
		this.addInvertPixelCalcInfo();

		this.addPixelConstantOperationInfo(pxConstOp);
		this.addPixelFuncInfo(pxFunc);
		
		this.addMandelbrotUseDiffInfo();

		ff = new Mandelbrot(mag, exp, mUseD, mBound, true);
		
		ff.setCaptureOrbit(capOrb);
		if(capOrb){
			ff.setOrbitTrapPoint(this.orbitTrapPointChoice);
			ff.setTrapSize(this.trapSizeChoice);
			ff.setTrapShape(this.trapShapeChoice);
		}

		ff.setSmoothen(doSmoothen);
		
		if (isBuddha) {
			Mandelbrot m = (Mandelbrot) ff;
			m.setBuddha(isBuddha);
			ff = m;
		}
		
		if(isInMotion) {
			Mandelbrot m = (Mandelbrot) ff;
			m.setMotionBrot(isInMotion);
			m.setMotionParam(motParam);
			m.setMotionParamJumpVal(motParamJumpVal);
			ff = m;			
		}
		
		if (doExplore) {
			ff.setUseMandelbrotExplorer(true);
		}
		
		ff.setUseLyapunovExponent(this.mandUseLyapunovExponent);

		ff.setPxXTransformation(this.pixXTransform);
		ff.setPxYTransformation(this.pixYTransform);
		ff.setPixXYOperation(this.pixIntraXYOperation);
		
		ff.setPxConstOperation(pxConstOp);

		ff.setReversePixelCalculation(this.invertPixelCalculation);
		
		
		ff.setColorChoice(this.colorChoice);
		if (useSample) {
//			ff.setColorChoice(this.colorChoice);	//	
			this.setSampleColorMix(ff);
		}
		if(useColorBlowout){
			ff.setColorBlowoutType(this.colorBlowoutChoice);
		}
		if(useColorSuperBlowout){
			ff.setColorBlowoutType(this.colorSuperBlowoutChoice);
		}
		/*
		if (useCP) {
			ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setColorChoice(this.colorChoice);	//	ff.setUseBlackWhite(useBw);
			} else {
				if (useSample) {
					ff.setColorChoice(this.colorChoice);	//	
					this.setSampleColorMix(ff);
				} else {

					ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
				}
			}
		}
		*/
		/*ff.setUseBlackWhite(useBw);		removing_4_now*/
		ff.setUseFuncConst(func);
		ff.setUseFuncPixel(pxFunc);
		ff.setRotation(rot);
		ff.setxC(mXc);
		ff.setyC(mYc);
		ff.setScaleSize(mScale);
		ff.setMaxIter(mandMax);
		ff.setAreaSize(mandLoopLt);

		this.addMandelbrotConstInfo(ff);
		ff.setSavePixelInfo2File(this.savePixelInfo2File);
		return ff;
	}

	private FractalBase startPoly() {
		boolean useCP = this.colorChoice.equals("ColorPalette");
		boolean useBw = this.colorChoice.equals("BlackWhite");
		boolean useSample = this.colorChoice.equals("sampleMix");
		boolean useColorBlowout = this.colorChoice.equals("ColorBlowout");
		boolean useColorSuperBlowout = this.colorChoice.equals("ColorSuperBlowout");

		boolean doSmoothen = this.smoothenColor;
		/*String pxXTrans = this.pixXTransform;
		String pxYTrans = this.pixYTransform;
		String pxIntraOp = this.pixIntraXYOperation;*/
		
		String pxConstOp = this.pxConstOprnChoice;
		String func = this.constFuncChoice;
		String pxFunc = !this.polyApplyFormulaZ ? this.pxFuncChoice : this.polyApplyFormulaTf.getText().trim();
		
		double rot = this.getRotation();
		boolean invertPix = this.invertPixelCalculation;
		
		boolean capOrb = this.captureOrbit;
		
		FractalBase ff = null;
		this.setUpPolyDetailInfo(pxConstOp, pxFunc, invertPix);

		if (this.keepConst) {
			this.formulaArea.append("<br/>Dynamic Constant = z</font>");
			ff = new PolyFract(this.polyPower, this.polyUseDiff, this.polyBound, this.keepConst);
		} else {
			try {
				double polyRealVal = Double.parseDouble(this.polyRealTf.getText());
				double polyImgVal = Double.parseDouble(this.polyImgTf.getText());
				this.formulaArea.append("<br/>Constant = " + polyRealVal + " + (" + polyImgVal + " * i)</font>");
				
				ff = new PolyFract(this.polyPower, this.polyUseDiff, this.polyBound, polyRealVal, polyImgVal);
			} catch (NumberFormatException  | NullPointerException e2) {
				e2.printStackTrace();
				JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		ff.setCaptureOrbit(capOrb);
		if(capOrb){
			ff.setOrbitTrapPoint(this.orbitTrapPointChoice);
			ff.setTrapSize(this.trapSizeChoice);
			ff.setTrapShape(this.trapShapeChoice);
		}

		ff.setSmoothen(doSmoothen);

		/*ff.setUseLyapunovExponent(this.polyUseLyapunovExponent)*/;

		ff.setPxXTransformation(this.pixXTransform);
		ff.setPxYTransformation(this.pixYTransform);
		ff.setPixXYOperation(this.pixIntraXYOperation);
		
		ff.setPxConstOperation(pxConstOp);
		
		ff.setReversePixelCalculation(invertPix);
		
		ff.setColorChoice(this.colorChoice);
		if (useSample) {
//			ff.setColorChoice(this.colorChoice);	//	
			this.setSampleColorMix(ff);
		}
		if(useColorBlowout){
			ff.setColorBlowoutType(this.colorBlowoutChoice);
		}
		if(useColorSuperBlowout){
			ff.setColorBlowoutType(this.colorSuperBlowoutChoice);
		}
		
		/*
		if (useCP) {
			ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
		} else {
			if (useBw) {
				ff.setColorChoice(this.colorChoice);	//	ff.setUseBlackWhite(useBw);
			} else {
				if (useSample) {
					ff.setColorChoice(this.colorChoice);	//	
					this.setSampleColorMix(ff);
				} else {

					ff.setColorChoice(this.colorChoice);	//	ff.setUseColorPalette(useCP);
				}
			}
		}
		*/
		ff.setUseFuncConst(func);
		ff.setUseFuncPixel(pxFunc);
		ff.setRotation(rot);
		ff.setRowColMixType(this.polyType);
		ff.setxC(this.polyXC);
		ff.setyC(this.polyYC);
		ff.setScaleSize(this.polyScaleSize);
		ff.setMaxIter(this.polyMaxIter);
		ff.setAreaSize(this.polySize);

		this.addPolyConstInfo(ff);
		ff.setSavePixelInfo2File(this.savePixelInfo2File);
		
		return ff;
	}

	private void setUpPolyDetailInfo(String pxConstOp, String pxFunc, boolean invertPix) {
		this.formulaArea.setVisible(true);
		this.formulaArea.setText("");
		this.formulaArea.setText("<font color='blue'>Poynomial Set:<br/>");
	
		if (pxConstOp.equals("Plus")) {
			this.formulaArea.append("<br/>f(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) + C");
		} else if (pxConstOp.equals("Multiply")) {
			this.formulaArea.append("<br/>f(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) * C");
		} else if (pxConstOp.equals("Minus")) {
			this.formulaArea.append("<br/>f(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) - C");
		} else if (pxConstOp.equals("Divide")) {
			this.formulaArea.append("<br/>f(z) = (x <sup>" + this.polyPower + "</sup> + y <sup>" + this.polyPower + "</sup>) / C");
		}/* else if (pxConstOp.equals("Power")) {
			this.formulaArea.setText("<br/>f(z) = (x ^ " + this.polyPower + " + y ^ " + this.polyPower + ") ^ C");
		} */
		
/*
		if (this.polyUseLyapunovExponent) {
			this.addUseLyapunovInfo();
		}*/
			
		if (!invertPix) {
			this.formulaArea.append("<br/>  x = Row + 0 * i , y = 0 + Column * i<br/>");
		}else{
			this.formulaArea.append("<br/>  x = Column + 0 * i , y = 0 + Row * i<br/>");
		}

		this.addXtrYtrXYInfroInfo(this.pixXTransform, this.pixYTransform, this.pixIntraXYOperation);
		this.addPixelConstantOperationInfo(this.pxConstOprnChoice);
		
		this.addPixelFuncInfo(pxFunc);
		
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
	}	

	private void startFractals(final FractalBase ff) {
//		this.startProgress();
		final FractalBase frame = ff;

//		ff.setVisible(true);
		
		frame.setTitle(ff.getFractalShapeTitle());
		
		if (!(this.diyApolloRb.isSelected() || this.getComboChoice().equals(APOLLONIAN_CIRCLES))) {
			frame.setSize(FractalBase.getAreaSize(), FractalBase.getAreaSize());//(FractalBase.WIDTH, FractalBase.HEIGHT);
		}
		
		frame.setDefaultCloseOperation(closeIt(frame));
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth())/2, 
	            (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight())/2);
		frame.setResizable(false);
		frame.setVisible(true);

		final BufferedImage bImage = frame.getBufferedImage();
		this.setFractalImage(bImage);
		this.setFractalBase(frame);
//		/*J10
//		rawFbZoomedImages.add(bImage);
//		fbDTlsList.add(frame.createFractalDtlInfoObject());
//		currentIndex+=1;*/
//		
//		this.tfCount.setText(String.valueOf(1/*J9FractalBase.fbDTlsList.size()*/));
//		/*J9if (FractalBase.fbDTlsList.size() > 1) {*/
//			this.buBack.setEnabled(true);
//		/*J9}*/
//
		this.buClose.setEnabled(true);
		
		/*
		if(this.doMagnify){
			new ZoomInBox(frame);
		}*/
		
		boolean staticIterativeFractalChoice = ( (!this.mandIsMotionbrot && this.comboChoice.contains(MANDELBROT)) || this.comboChoice.contains(JULIA) || this.comboChoice.contains(POLY) ||
				(this.comboChoice.contains("self") && !(this.diyApolloRb.isSelected() || this.getComboChoice().equals(APOLLONIAN_CIRCLES))));
	
		staticIterativeFractalChoice = !this.comboChoice.equals(ATTRACTORS) ? staticIterativeFractalChoice: true;

		if(!staticIterativeFractalChoice) {
			frame.setRunning(true);
//System.out.println("this.comboChoice--"+this.comboChoice+"isThread");
			/*if(this.doMagnify){
				new ZoomInBox(frame);
			}*/
			this.formulaArea.setVisible(false);
			this.fbf = new Thread(frame);
			this.fbf.start();
			
			this.setFractalImage(bImage);
			this.setFractalBase(frame);
			
			this.buClose.setEnabled(true);
			return;
		}
		

		if (this.doMagnify) {
			this.setFractalImage(bImage);
			new ZoomInBox(frame);
		}
		

		/*
		boolean explore = (this.juliaExplore&&this.juliaExploreCb.isSelected())||
							(this.mandExplore&&this.mandExploreCb.isSelected())||
							(this.diyJuliaExplore&&this.diyJuliaExploreCb.isSelected())||
							(this.diyMandExplore&&this.diyMandExploreCb.isSelected());
		if(explore){
			this.add2FbImages(bImage);
			this.addImage2FBImagesPanel(frame.getImages());
		}
		*/
		return;
		
//		this.endProgress();
	}

	/*private void addImage2FBImagesPanel(BufferedImage[] images) {
		//this.fbImagesPanel.removeAll();// = new JPanel(new GridLayout(1,0),true);
		
		for (int i = 0; i < images.length; i++) {
			BufferedImage anImage = images[i];
			try {
				anImage = this.resizeImage(anImage, 150, 150);

			} catch (IOException e) {
				e.printStackTrace();
			}
			JLabel label = new JLabel(new ImageIcon(anImage));
			this.fbImagesPanel.add(label);
		}
		
		this.fbImagesPanel.revalidate();
		this.fbImagesPanel.repaint();
	}*/

	/*private void add2FbImages(BufferedImage bImage) {
		int i = 0;
		for (; i < this.fbImages.length; i++) {
			if (this.fbImages[i] == null)
				break;
		}
		this.fbImages[i] = bImage;
	}*/

	/*
	private void addImage2FBImagesPanel(BufferedImage bImage) {
		BufferedImage resized = null;
		try {
			resized = this.resizeImage(bImage,150,150);
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel label = new JLabel(new ImageIcon(resized));
		this.fbImagesPanel.add(label);
	}*/
	
	public BufferedImage resizedImage(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
	
	BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
	    BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = resizedImage.createGraphics();
	    graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
	    graphics2D.dispose();
	    return resizedImage;
	}
	private int closeIt(/*FractalBase*/ JFrame frame) {
		this.buStart.setEnabled(false);
		this.buSavePxStart.setEnabled(false);
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
	
	private void doSaveImageCommand(String saveCommand) {
		/*	todo
		 * for rotation	-	fix center
		// reqd drawing location
		int drawLocX = (int) this.fBase(int) FractalBase.getxC();
		int drawLocY = (int) this.fBase(int) FractalBase.getyC();

		// rotation info
		double rotationReq = Math.toRadians(this.rotation);

		double locX = getWidth() / 2;
		double locY = getHeight() / 2;

		AffineTransform tx = AffineTransform.getRotateInstance(rotationReq, locX, locY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		BufferedImage fractalImage = this.getFractalImage();
		// Drawing the rotated image at the required drawing locations
		fractalImage = op.filter(fractalImage, null);
		
		//
//		g2.drawImage(img, drawLocX, drawLocY, null);
		
		*/
		String choice = this.getComboChoice();
		if (choice.equals(ATTRACTORS)) {
			BufferedImage[] images = new BufferedImage[this.generators.length];
			
			for(int i = 0; i < this.generators.length;i++) {
				
				images[i] = //this.generators[i].getBufferedImage();
						FractalBase.joinBufferedImages(this.generators[i].getBufferedImage(),this.createStringImage(this.generators[i].getSpace2d()));
			}
			
			this.setFractalImage(FractalBase.joinAdjacentBufferedImages(images));
		}
		
		String extraInfo = this.getExtraInfo();
		
		String imgBaseInfo;
		BufferedImage dataInfoImg = null;
//		BufferedImage dataInfoImg = null;
		BufferedImage detailDataImg = null;
		if (saveCommand.equals("saveWithData") || saveCommand.equals("save2FileWithData") || saveCommand.equals("saveWithDetailData")) {
			imgBaseInfo = this.getImgBaseInfo();
			dataInfoImg = this.createStringImage(imgBaseInfo);
		}

		if (saveCommand.equals("saveWithDetailData")) {
			String detailDataInfo = this.fBase.getFractalDetails();
			detailDataImg = this.createStringImage(detailDataInfo);
		}
        
		File outputfile;
		String imageFilePath = null;
		if (saveCommand.equals("save") || saveCommand.equals("saveWithData")) {
			imageFilePath = "images_dump\\" + this.getComboChoice() + "[" + extraInfo + "]" + " ____" + System.currentTimeMillis() + ".png";
		} else if (saveCommand.equals("save2File") || saveCommand.equals("save2FileWithData")) {
			if (saveCommand.equals("save2File")) {
				imageFilePath = this.save2File;
			}
			if (saveCommand.equals("save2FileWithData")) {
				imageFilePath = this.save2FileWithData;
			}

		} else if (saveCommand.equals("saveWithDetailData")) {
			imageFilePath = null;	//NOTE	-	AintDoingThis4 now
		}
		
		outputfile = new File(imageFilePath);
		
		fractalImage = (this.fBase.isRefocusDraw()&&!this.fBase.isGenerated())?this.fBase.getBufferedImage():fractalImage;
		
	    try {
			if (saveCommand.equals("saveWithData") || saveCommand.equals("save2FileWithData")) {
				final BufferedImage joinedFractalDataImage = FractalBase.joinBufferedImages(fractalImage, dataInfoImg);
				ImageIO.write(joinedFractalDataImage, "png", outputfile);
			} else if(saveCommand.equals("saveWithDetailData")){
				final BufferedImage joinedFractalDetailDataImage = FractalBase.joinAdjacentBufferedImages(fractalImage, detailDataImg);
				ImageIO.write(joinedFractalDetailDataImage, "png", outputfile);
			} else {
				ImageIO.write(fractalImage, "png", outputfile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private BufferedImage createStringImage(final String info) {
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
		if (this.getFractalImage() != null) {
			g2d.fillRoundRect(0, 0, this.getFractalImage().getWidth(), 300, 15, 10);
		} else {
			g2d.fillRoundRect(0, 0, 100, 300, 15, 10);
		}
		g2d.setColor(Color.black);
		textLabel.paint(g2d);
        
        g2d.dispose();
        
		return img;
	}
	
	private String getImgBaseInfo() {
		
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
				baseInfo += this.colorChoice + "," + eol;
				if(this.colorChoice.equals("SampleMix")) { baseInfo += "startVals["+this.colorSampleMixStartVals+"] divVals["+this.colorSampleDivVals+"]" + eol; }
				if(this.colorChoice.equals("ColorBlowout")) { baseInfo += "["+this.colorBlowoutChoice+"]" + eol; }
				if(this.colorChoice.equals("ColorSuperBlowout")) { baseInfo += "["+this.colorSuperBlowoutChoice+"]" + eol; }
				baseInfo += "Power: " + this.polyPower + ", ";
				if (this.getPUseDiff()) {
					baseInfo += "Ud, "+eol;
				}
				baseInfo += "Boundary: " + this.polyBound + ", " + eol;
//				if (this.keepConst) {
//					baseInfo += "Dynamic Constant	C = z"+eol;
//				} else {
//					try {
//						baseInfo += "Real Value = " + Double.parseDouble(this.polyRealTf.getText()) + "," + eol;
//						baseInfo += "Imaginary Value = " + Double.parseDouble(this.polyImgTf.getText()) + eol;
//					} catch (NumberFormatException  | NullPointerException e2) {
//						e2.printStackTrace();
//						JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
//						return null;
//					}
//				}
				if (((this.keepConst && this.polyKeepConstRb.isSelected()))
						&& !(this.polyVaryConstant || this.polyVaryGenConstantCb.isSelected())) {
					baseInfo += "Dynamic Constant	Z=C" + eol;
				} else {
						if (!polyGen) {
							try {
								baseInfo += "Real Value = " + Double.parseDouble(this.polyRealTf.getText()) + "," + eol;
								baseInfo += "Imaginary Value = " + Double.parseDouble(this.polyImgTf.getText()) + eol;
							} catch (NumberFormatException  | NullPointerException e2) {
								e2.printStackTrace();
								JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
								return null;
							}
						} else {
							baseInfo += "Real Value = " + this.polyConstReal + "," + eol;
							baseInfo += "Imaginary Value = " + this.polyConstImg + eol;
						}
					}

				baseInfo = addConstFuncInfo(baseInfo);
				
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Row-Column Mix-Type: " + this.polyType + eol;
				baseInfo += "Center: P(x,y): (" + this.polyXC + ", " + this.polyYC + ")" + eol;

				/*if(this.useRangeEstimation) {
					baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.xMinTf.getText() + ", "+ this.yMinTf.getText() + "] to [" + this.xMaxTf.getText() + ", " + this.yMaxTf.getText() +"]" + eol;
				}*/
				baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.fBase.x_min + ", "+ this.fBase.y_min + "] to [" + this.fBase.x_max + ", " + this.fBase.y_max +"]" + eol;
				
				
				baseInfo += "Maximum Iterations: " + this.polyMaxIter + eol;
				baseInfo += " Scaled Size: " + this.polyScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
			
			case	MANDELBROT	:
				baseInfo += this.colorChoice + "," + eol;
				if(this.colorChoice.equals("SampleMix")) { baseInfo += "startVals["+this.colorSampleMixStartVals+"] divVals["+this.colorSampleDivVals+"]" + eol; }
				if(this.colorChoice.equals("ColorBlowout")) { baseInfo += "["+this.colorBlowoutChoice+"]" + eol; }
				if(this.colorChoice.equals("ColorSuperBlowout")) { baseInfo += "["+this.colorSuperBlowoutChoice+"]" + eol; }
				baseInfo += "ImageMagnification: " + this.magnification + eol;
				baseInfo += "Power: " + this.exponent + ", ";
				if (this.getMUseDiff()) {
					baseInfo += "Ud, ";
				}
				baseInfo += "Boundary: " + this.mandBound + eol;

				baseInfo = addConstFuncInfo(baseInfo);
				
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Center: P(x,y): (" + this.mandXC + ", " + this.mandYC + ")" + eol;
				
				/*if(this.useRangeEstimation) {
					baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.xMinTf.getText() + ", "+ this.yMinTf.getText() + "] to [" + this.xMaxTf.getText() + ", " + this.yMaxTf.getText() +"]" + eol;
				}*/
				baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.fBase.x_min + ", "+ this.fBase.y_min + "] to [" + this.fBase.x_max + ", " + this.fBase.y_max +"]" + eol;
				
				baseInfo += "Maximum Iterations: " + this.mandMaxIter + eol;
				baseInfo += " Scaled Size: " + this.mandScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
				
			case	JULIA	:
				baseInfo += this.colorChoice + "," + eol;
				if(this.colorChoice.equals("SampleMix")) { baseInfo += "startVals["+this.colorSampleMixStartVals+"] divVals["+this.colorSampleDivVals+"]" + eol; }
				if(this.colorChoice.equals("ColorBlowout")) { baseInfo += "["+this.colorBlowoutChoice+"]" + eol; }
				if(this.colorChoice.equals("ColorSuperBlowout")) { baseInfo += "["+this.colorSuperBlowoutChoice+"]" + eol; }
				baseInfo += "Power: " + this.power + ", ";
				if (this.getJUseDiff()) {
					baseInfo += "Ud, ";
				}
				baseInfo += "Boundary: " + this.juliaBound + eol;

				baseInfo = addConstFuncInfo(baseInfo);
				
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Center: P(x,y): (" + this.juliaXC + ", " + this.juliaYC + ")" + eol;
				
				/*if(this.useRangeEstimation) {
					baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.xMinTf.getText() + ", "+ this.yMinTf.getText() + "] to [" + this.xMaxTf.getText() + ", " + this.yMaxTf.getText() +"]" + eol;
				}*/
				baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.fBase.x_min + ", "+ this.fBase.y_min + "] to [" + this.fBase.x_max + ", " + this.fBase.y_max +"]" + eol;
				
				baseInfo += "Maximum Iterations: " + this.juliaMaxIter + eol;
				baseInfo += " Scaled Size: " + this.juliaScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
				
			case	diyMand:
				baseInfo += MANDELBROT + eol;
				baseInfo += this.colorChoice + "," + eol;
				if(this.colorChoice.equals("SampleMix")) { baseInfo += "startVals["+this.colorSampleMixStartVals+"] divVals["+this.colorSampleDivVals+"]" + eol; }
				if(this.colorChoice.equals("ColorBlowout")) { baseInfo += "["+this.colorBlowoutChoice+"]" + eol; }
				if(this.colorChoice.equals("ColorSuperBlowout")) { baseInfo += "["+this.colorSuperBlowoutChoice+"]" + eol; }
				baseInfo += "ImageMagnification: " + this.diyMandMagnification + eol;
				baseInfo += "Power: " + this.diyMandExponent + ", ";
				if (this.getDiyMandUseDiff()) {
					baseInfo += "Ud, ";
				}
				baseInfo += "Boundary: " + this.diyMandBound + ", " + eol;
//				if (this.keepConst) {
//					baseInfo += "Dynamic Constant	Z=C" + eol;
//				} else {
//					try {
//						baseInfo += "Real Value = " + Double.parseDouble(this.diyMandRealTf.getText()) + ","+eol;
//						baseInfo += "Imaginary Value = " + Double.parseDouble(this.diyMandImgTf.getText()) + eol;
//					} catch (NumberFormatException  | NullPointerException e2) {
//						e2.printStackTrace();
//						JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
//						return null;
//					}
//				}
				if (((this.keepConst && this.diyMandelbrotKeepConstRb.isSelected()))
						&& !(this.diyMandVaryConstant || this.diyMandVaryGenConstantCb.isSelected())) {
					baseInfo += "Dynamic Constant	Z=C" + eol;
				} else {
						if (!diyMandGen) {
							try {
								baseInfo += "Real Value = " + Double.parseDouble(this.diyMandRealTf.getText()) + "," + eol;
								baseInfo += "Imaginary Value = " + Double.parseDouble(this.diyMandImgTf.getText()) + eol;
							} catch (NumberFormatException  | NullPointerException e2) {
								e2.printStackTrace();
								JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
								return null;
							}
						} else {
							baseInfo += "Real Value = " + this.diyMandConstReal + "," + eol;
							baseInfo += "Imaginary Value = " + this.diyMandConstImg + eol;
						}
					}

				baseInfo = addConstFuncInfo(baseInfo);
				
				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Center: P(x,y): (" + this.diyMandXC + ", " + this.diyMandYC + ")" + eol;

				/*if(this.useRangeEstimation) {
					baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.xMinTf.getText() + ", "+ this.yMinTf.getText() + "] to [" + this.xMaxTf.getText() + ", " + this.yMaxTf.getText() +"]" + eol;
				}*/
				baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.fBase.x_min + ", "+ this.fBase.y_min + "] to [" + this.fBase.x_max + ", " + this.fBase.y_max +"]" + eol;
				
				baseInfo += "Maximum Iterations: " + this.diyMandMaxIter + eol;
				baseInfo += " Scaled Size: " + this.diyMandScaleSize + "}" + eol + eol;
	
				baseInfo += this.formulaArea.getText();
				break;
				
			case diyJulia:
				baseInfo += JULIA + eol;
				baseInfo += this.colorChoice + "," + eol;
				if(this.colorChoice.equals("SampleMix")) { baseInfo += "startVals["+this.colorSampleMixStartVals+"] divVals["+this.colorSampleDivVals+"]" + eol; }
				if(this.colorChoice.equals("ColorBlowout")) { baseInfo += "["+this.colorBlowoutChoice+"]" + eol; }
				if(this.colorChoice.equals("ColorSuperBlowout")) { baseInfo += "["+this.colorSuperBlowoutChoice+"]" + eol; }
				baseInfo += "Power: " + this.diyJuliaPower + ", ";
				if (this.getDiyJuliaUseDiff()) {
					baseInfo += "Ud, ";
				}
				baseInfo += "Boundary: " + this.diyJuliaBound + ", "+eol;
				if (((this.keepConst && this.diyJuliaKeepConstRb.isSelected()))
						&& !(this.diyJuliaVaryConstant || this.diyJuliaVaryGenConstantCb.isSelected())) {
					baseInfo += "Dynamic Constant	Z=C" + eol;
				} else {
						if (!diyJuliaGen) {
							try {
								baseInfo += "Real Value = " + Double.parseDouble(this.diyJuliaRealTf.getText()) + "," + eol;
								baseInfo += "Imaginary Value = " + Double.parseDouble(this.diyJuliaImgTf.getText()) + eol;
							} catch (NumberFormatException  | NullPointerException e2) {
								e2.printStackTrace();
								JOptionPane.showMessageDialog(null, "Please enter a valid Decimal Number\n"+e2.getMessage(), "Error - Not a Decimal", JOptionPane.ERROR_MESSAGE);
								return null;
							}
						} else {
							baseInfo += "Real Value = " + this.diyJuliaConstReal + "," + eol;
							baseInfo += "Imaginary Value = " + this.diyJuliaConstImg + eol;
						}
					}

				baseInfo = addConstFuncInfo(baseInfo);

				baseInfo += "Rotation: " + this.getRotation() + eol;
				baseInfo += "Center: P(x,y): (" + this.diyJuliaXC + ", " + this.diyJuliaYC + ")" + eol;

				/*if(this.useRangeEstimation) {
					baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.xMinTf.getText() + ", "+ this.yMinTf.getText() + "] to [" + this.xMaxTf.getText() + ", " + this.yMaxTf.getText() +"]" + eol;
				}*/
				baseInfo += "Range: (xmin,ymin) to (xmax, ymax) is ["+ this.fBase.x_min + ", "+ this.fBase.y_min + "] to [" + this.fBase.x_max + ", " + this.fBase.y_max +"]" + eol;
				
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
			case	ATTRACTORS :	//= new String[]{"lorenz","aizawa","dejong","custom"};
				if (this.attractorSelectionChoice.equals("lorenz")) {
					baseInfo += "<b>LorenzAttractor</b>" + eol;
					baseInfo += "<font color='red'>Sigma: "+this.attrLorenzSigma_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='blue'>Rho: "+this.attrLorenzRho_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='green'>Beta: "+this.attrLorenzBeta_tf.getText()+"</font>" + eol;
					
					String lorenzFormulaStr = "<b>ΔX   = -σ * (X - Y)" + eol;
					lorenzFormulaStr += "ΔY   = (-X * Z) + (ρ * X) - Y;" + eol;
					lorenzFormulaStr += "ΔZ   = (X * Y) - (β * Z)</b>"	+ eol;
					
					baseInfo += lorenzFormulaStr;
					
				} else if(this.attractorSelectionChoice.equals("aizawa")) {
					baseInfo += "<b>Aizawa Attractor</b>" + eol;
					baseInfo += "<font color='red'>A: "+this.attrAizawaA_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='blue'>B: "+this.attrAizawaB_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='red'>C: "+this.attrAizawaC_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='blue'>D: "+this.attrAizawaD_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='red'>E: "+this.attrAizawaE_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='blue'>F: "+this.attrAizawaF_tf.getText()+"</font>" + eol;
					
					String aizawaFormulaStr = "<b>ΔX   =   (Z - B) * X - (D * Y)" + eol;
					aizawaFormulaStr += "ΔY   =   D * X + (Z - B) * Y " + eol;
					aizawaFormulaStr += "ΔZ   = C + (A * Z) - ((Z <sup>3</sup>) /3) - (X <sup>2</sup>)   + (F * Z * X <sup>3</sup>)</b>" + eol;
					
					baseInfo += aizawaFormulaStr;
					
				} else if(this.attractorSelectionChoice.equals("dejong")) {
					baseInfo += "<b>DeJong Attractor</b>" + eol;
					baseInfo += "<font color='red'>A: "+this.attrDeJongA_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='blue'>B: "+this.attrDeJongB_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='red'>C: "+this.attrDeJongC_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='blue'>D: "+this.attrDeJongD_tf.getText()+"</font>" + eol;
										
					String deJongFormulaStr = "<b>ΔX   =   sin(A * Y) - cos(B * X)" + eol;
					deJongFormulaStr += "ΔY   =   sin(C * X) - cos(D * Y) " + eol;
					
					baseInfo += deJongFormulaStr;
					
				} else if(this.attractorSelectionChoice.equals("custom")) {
					baseInfo += "<b>Custom Attractor</b>" + eol;
					baseInfo += "<font color='red'>ΔX: "+this.attrCustom_DeltaXFormula_tf.getText()+"</font>" + eol;
					baseInfo += "<font color='blue'>ΔY: "+this.attrCustom_DeltaYFormula_tf.getText()+"</font>" + eol;
					if (this.isAttractorDimSpace3D) {
						baseInfo += "<font color='red'>ΔZ: "+this.attrCustom_DeltaZFormula_tf.getText()+"</font>" + eol;
					}
				}
				
				if (this.isAttractorTimeInvariant) {
					baseInfo += "<font color='red'>Time Invariant Attractor</font>" + eol;
				} else {
					baseInfo += "<font color='red'>Delta_Time: " + this.attrDeltaTime_tf.getText() + "</font>" + eol;
				}
				
				if (this.isAttractorTimeIterDependant) {
					baseInfo += "<font color='red'>TimeIterator Dependant Attractor</font>" + eol;
				}
				
				if (this.isAttractorDimSpace3D) {
					baseInfo += "<font color='green'>3D Attractor</font>" + eol;
				} else {
					baseInfo += "<font color='green'>2D Attractor</font>" + eol;
				}
				
				baseInfo += "<font color='blue'>NumIterations: "+this.attrMaxIter_tf.getText()+"</font>" + eol;
				

				String seedInfo = "Seeds:" + eol;
				int numAttr = this.attrSeed_ClrChList.size();
				for(int i = 0; i < numAttr; i++) {
					Color seedColor = this.attrSeed_ClrChList.get(i);	System.out.println("colorATTRIs___"+seedColor);
					String red = Integer.toHexString(seedColor.getRed());
					if(red.equals("0")){red="00";}
					/*System.out.println("redIs___"+red);*/
					String green = Integer.toHexString(seedColor.getGreen());
					if(green.equals("0")){green="00";}
					/*System.out.println("greenIs___"+green);*/
					String blue =  Integer.toHexString(seedColor.getBlue());	
					if(blue.equals("0")){blue="00";}	
					/*System.out.println("blueIs___"+blue);*/
					String seedColorStr = "'#" + red + EMPTY + green + EMPTY + blue + "'";
					/*System.out.println("seedColorStrIs___"+seedColorStr);*/
					
					seedInfo += "<font color="+seedColorStr+">Seed "+i+": X["+this.attrSeed_X_tfList.get(i).getText()+"],"+
								" Y["+this.attrSeed_Y_tfList.get(i).getText()+"]";
					
					if (this.isAttractorDimSpace3D) {
						seedInfo += " Z[" + this.attrSeed_Z_tfList.get(i).getText() + "]";
					}
	
					seedInfo += "</font>"+eol;
				}
	
				baseInfo += seedInfo;
				break;
			default:
				baseInfo+="}";
		}
		
		return baseInfo+"</font></html>";		
	}

	private String addConstFuncInfo(String baseInfo) {
		if (!(this.constFuncChoice.equals("None"))) {
			if (this.constFuncChoice.equals("sine")) { 
				baseInfo += "Sine of Constant	sine(C)" + eol;
			} else if (this.constFuncChoice.equals("cosine")) { 
				baseInfo += "Cosine of Constant		cos(C)" + eol;
			} else if (this.constFuncChoice.equals("tan")) { 
				baseInfo += "Tan of Constant	tan(C)" + eol;
			} else if (this.constFuncChoice.equals("arcsine")) { 
				baseInfo += "ArcSine of Constant	arcsin(C)" + eol;
			} if (this.constFuncChoice.equals("arccosine")) { 
				baseInfo += "ArcCosine of Constant	arccos(C)" + eol;
			} else if (this.constFuncChoice.equals("arctan")) { 
				baseInfo += "ArcTan of Constant		arctan(C)" + eol;
			} else if (this.constFuncChoice.equals("square")) { 
				baseInfo += "Square of Constant		(C <sup>2</sup>)" + eol;
			} else if (this.constFuncChoice.equals("cube")) { 
				baseInfo += "Cube of Constant		(C <sup>3</sup>)" + eol;
			} else if (this.constFuncChoice.equals("exponent(e)")) { 
				baseInfo += "Exponent of Constant	(e <sup>C</sup>)" + eol;
			} else if (this.constFuncChoice.equals("root")) { 
				baseInfo += "Square Root of Constant	(C <sup>(1/2)</sup>)" + eol;
			} else if (this.constFuncChoice.equals("cube-root")) { 
				baseInfo += "Cube Root of Constant		(C <sup>(1/3)</sup>)" + eol;
			} else if (this.constFuncChoice.equals("log(e)")) { 
				baseInfo += "Log(e) of Constant		Ln(C)" + eol;
			}
		}
		return baseInfo;
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
				extra += "P(" + this.power + "),";
				if (this.compConst != 0.0)
					extra += "C(" + this.compConst + "),";
				else
					extra += "C(" + this.complex + "),";
				if (!this.fieldLines.equals("None")) 
					extra += this.fieldLines + ",";			
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
				if(this.diyMandApplyFormulaZ){extra+="CustFormula,";}
				if (this.keepConst) {
					extra += "CONST";
				} else {
					extra += "Real(" + String.format("%.2f", this.diyMandConstReal) + "),";
					extra += "Img(" + String.format("%.2f", this.diyMandConstImg) + ")";
				}
				break;
			case diyJulia:
				extra += "DIY_"+JULIA+"_";
				extra += "P("+this.diyJuliaPower+"),";
				if (!this.fieldLines.equals("None")) 
					extra += this.fieldLines + ",";			
				
				extra += "B(" + this.diyJuliaBound + "),";
				extra += "MxIt(" + this.diyJuliaMaxIter + "),";
				extra += "Cx(" + this.diyJuliaXC + "),";
				extra += "Cy(" + this.diyJuliaYC + "),";
				extra += "Sz(" + this.diyJuliaScaleSize + "),";
				if(this.diyJApplyFormulaZ){extra+="CustFormula,";}
				if (this.keepConst) {
					extra += "CONST";
				}else{
						extra += "Real(" + String.format("%.2f", this.diyJuliaConstReal) + "),";
						extra += "Img(" + String.format("%.2f", this.diyJuliaConstImg) + ")";
				}
				break;
			case diyApollo:
				extra+="DIY_"+APOLLONIAN_CIRCLES+"_";
				extra+="C1("+this.diyApolloC1+"),C2("+this.diyApolloC2+"),C3("+this.diyApolloC3+"),Mx("+this.diyApolloMult+")";
				break;
			case ATTRACTORS://= new String[]{"lorenz","aizawa","dejong","custom"};
				if (this.attractorSelectionChoice.equals("custom")) {
					extra+="CustomAttractor_";
				} else if (this.attractorSelectionChoice.equals("lorenz")) {
					extra+="LorenzAttractor_";					
				} else if (this.attractorSelectionChoice.equals("dejong")) {
					extra+="DeJongAttractor_";					
				} else if (this.attractorSelectionChoice.equals("aizawa")) {
					extra+="Aizawttractor_";
				}
				break;
			default:
				extra="";
				break;
		}
		return extra;
	}

	private void doPrintCommand() {
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
		
		this.colorChoiceCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String comboOption = (String)cb.getSelectedItem();
				doSelectFractalColorChoiceCommand(comboOption);		
				
				if(comboOption.equals("SampleMix")) {
					colorSampleMixStartValsLabel.setVisible(true);
					colorSampleMixStartValsCombo.setVisible(true);
					colorSampleDivValsLabel.setVisible(true);
					colorSampleDivValsCombo.setVisible(true);
					colorBlowoutChoiceLabel.setVisible(false);
					colorBlowoutChoiceCombo.setVisible(false);
					colorSuperBlowoutChoiceLabel.setVisible(false);
					colorSuperBlowoutChoiceCombo.setVisible(false); 
				} else if (comboOption.equals("ColorBlowout")) {
					colorSampleMixStartValsLabel.setVisible(false);
					colorSampleMixStartValsCombo.setVisible(false);
					colorSampleDivValsLabel.setVisible(false);
					colorSampleDivValsCombo.setVisible(false);
					colorBlowoutChoiceLabel.setVisible(true);
					colorBlowoutChoiceCombo.setVisible(true);
					colorSuperBlowoutChoiceLabel.setVisible(false);
					colorSuperBlowoutChoiceCombo.setVisible(false); 
				} else if (comboOption.equals("ColorSuperBlowout")) {
					colorSampleMixStartValsLabel.setVisible(false);
					colorSampleMixStartValsCombo.setVisible(false);
					colorSampleDivValsLabel.setVisible(false);
					colorSampleDivValsCombo.setVisible(false);
					colorBlowoutChoiceLabel.setVisible(false);
					colorBlowoutChoiceCombo.setVisible(false);
					colorSuperBlowoutChoiceLabel.setVisible(true);
					colorSuperBlowoutChoiceCombo.setVisible(true);
				} else {
					colorSampleMixStartValsLabel.setVisible(false);
					colorSampleMixStartValsCombo.setVisible(false);
					colorSampleDivValsLabel.setVisible(false);
					colorSampleDivValsCombo.setVisible(false);
					colorBlowoutChoiceLabel.setVisible(false);
					colorBlowoutChoiceCombo.setVisible(false);
					colorSuperBlowoutChoiceLabel.setVisible(false);
					colorSuperBlowoutChoiceCombo.setVisible(false);  
				}
			}});
		
		this.showAllColorsCb.setActionCommand("ShowAllColors");
		this.showAllColorsCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					colorChoiceCombo.setModel(new DefaultComboBoxModel<String>(COLOR_OPTIONS_ALL));
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					colorChoiceCombo.setModel(new DefaultComboBoxModel<String>(COLOR_OPTIONS));
				}
			}
        });
		
		this.smoothenColorCb.setActionCommand("SmoothenColors");
		this.smoothenColorCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetColorSmoothening(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetColorSmoothening(false);
				}
			}
        });
		
		this.colorSampleMixStartValsCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String comboOption = (String)cb.getSelectedItem();
		        doSelectColorSampleMixStartValsComboCommand(comboOption);				
			}});
		
		this.colorSampleDivValsCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String comboOption = (String)cb.getSelectedItem();
		        doSelectColorSampleDivValsComboCommand(comboOption);				
			}
		});

		this.colorBlowoutChoiceCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String comboOption = (String) cb.getSelectedItem();
				doSelectColorBlowoutChoiceComboCommand(comboOption);
			}
		});

		this.colorSuperBlowoutChoiceCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String comboOption = (String) cb.getSelectedItem();
				doSelectColorSuperBlowoutChoiceComboCommand(comboOption);
			}
		});
		
		
//		this.buColorChooser.addActionListener(new ActionListener() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				 Color ch = JColorChooser.showDialog(null,"ChooseYerColor",new Color(13));
//			}});
		
		this.setupFannyListeners();	
		this.setupSierpisnkiTListeners();
		this.setupKochListeners();
		this.setupApolloListeners();		
		this.setupJuliaListeners();		
		this.setupMandelbrotListeners();
		this.setupPolyListeners();
		
		this.setupAttractorsListeners();
		
		//DIY-Listeners
		this.setupDIYMandelbrotListeners();
		this.setupDIYJuliaListeners();		
		this.setupDIYApolloListeners();	
		
		
		this.pxXTransformCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String comboOption = (String) cb.getSelectedItem();
				doSelectPixXTransformComboChoice(comboOption);
			}
		});
		
		this.pxYTransformCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String comboOption = (String) cb.getSelectedItem();
				doSelectPixYTransformComboChoice(comboOption);
			}
		});
		
		this.intraPixOperationCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String comboOption = (String) cb.getSelectedItem();
				doSelectIntraPxOperationComboChoice(comboOption);
			}
		});
		
		this.constFuncCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String comboOption = (String) cb.getSelectedItem();
				doConstFunctionalSelectionChoice(comboOption);
			}
		});
		
		this.invertPixelsCb.setActionCommand("InvertPixels");
		this.invertPixelsCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetInvertPixelsCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetInvertPixelsCommand(false);
				}
			}
        });
		
		this.pxFuncCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>) e.getSource();
				String comboOption = (String) cb.getSelectedItem();
				doPxFunctionalSelectionChoice(comboOption);
			}
		});
				
		this.pxConstOprnCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String comboOption = (String)cb.getSelectedItem();
				doSelectPxConstOprnCommand(comboOption);				
			}});
		
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
		
		/*this.buSavePxStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSavePxStartCommand(true);				
			}});*/
		
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
				doSaveImageCommand("save");				
			}
		});
		
		this.buSaveWithData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSaveImageCommand("saveWithData");				
			}
		});
		
		this.buSaveWithDetailData.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSaveImageCommand("saveWithDetailData");				
			}
		});
		
		this.buSave2File.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// create an object of JFileChooser class 
				JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 

				// invoke the showsSaveDialog function to show the save dialog
				int r = chooser.showSaveDialog(null);

				// if the user selects a file
				if (r == JFileChooser.APPROVE_OPTION) {
					save2File = chooser.getSelectedFile().getAbsolutePath();
					doSaveImageCommand("save2File");
				}			
			}
		});
		
		this.buSaveWithData2File.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// create an object of JFileChooser class 
				JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); 

				// invoke the showsSaveDialog function to show the save dialog
				int r = chooser.showSaveDialog(null);

				// if the user selects a file
				if (r == JFileChooser.APPROVE_OPTION) {
					save2FileWithData = chooser.getSelectedFile().getAbsolutePath();
					doSaveImageCommand("save2FileWithData");
				}			
			}
		});
		
		this.buShowFbImages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFBImages();
			}
		});
		
		this.buShowFbInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFBInfo();
			}
		});
		
		this.buFlushFbImages.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				flushFBImages();
			}
		});
		
		this.useRangeCb.setActionCommand("UseRange");
		this.useRangeCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doUseRangeExtimates(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doUseRangeExtimates(false);
				}
			}
        });
		

		this.captureOrbitCb.setActionCommand("CaptureOrbits");
		this.captureOrbitCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetCaptueOrbitCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetCaptueOrbitCommand(false);
				}
			}
        });

		this.orbitPointsCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String orbitPointOption = (String)cb.getSelectedItem();
				doSetOrbitPointCombosCommand(orbitPointOption);				
			}});

		this.trapSizeCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double trapSizeOption = (Double)cb.getSelectedItem();
				doSetTrapSizeCombosCommand(trapSizeOption);				
			}});

		this.trapShapeCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String trapShapeOption = (String)cb.getSelectedItem();
				doSetTrapShapeCombosCommand(trapShapeOption);				
			}});
		
		this.buFirst.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doGoFirstCommand();
			}
		});
		
		this.buBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doBackCommand();
			}
		});

		this.buStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStepCommand();
			}
		});
		
		this.buLast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doGoLastCommand();
			}
		});

	}

	private void doGoLastCommand() {
		/*J9int last = FractalBase.rawFbImages.size()-1;
		FractalBase.fbCurrCount=last;
		System.out.println("inGoLast  fbCurrCount = "+FractalBase.fbCurrCount+ " and size is "+FractalBase.rawFbImages.size());
		BufferedImage bI = FractalBase.rawFbImages.get(last);
		FractalDtlInfo fD = FractalBase.fbDTlsList.get(last);
		FractalBase fB = fD.getfBase();

		this.doSetup(fB, bI);*/
		this.buStep.setEnabled(false);
		this.buBack.setEnabled(true);
		this.buFirst.setEnabled(true);
	}

	private void doGoFirstCommand() {
		FractalBase.fbCurrCount=0;
		System.out.println("inGoFirst  fbCurrCount = "+FractalBase.fbCurrCount+ " and size is "+FractalBase.rawFbImages.size());
		BufferedImage bI = FractalBase.rawFbImages.get(0);
		FractalDtlInfo fD = FractalBase.fbDTlsList.get(0);
		FractalBase fB = fD.getfBase();

		this.doSetup(fB, bI);
		this.buBack.setEnabled(false);
		this.buStep.setEnabled(true);
		this.buLast.setEnabled(true);
	}

	private void doStepCommand() {
		int numF = FractalBase.fbCurrCount;
		if (numF < FractalBase.rawFbImages.size()) {
			int curr = numF + 1;
			FractalBase.fbCurrCount = curr;
			System.out.println("inDoStep n<size  fbCurrCount = "+FractalBase.fbCurrCount+ " and size is "+FractalBase.rawFbImages.size());
			BufferedImage bI = FractalBase.rawFbImages.get(curr);
			FractalDtlInfo fD = FractalBase.fbDTlsList.get(curr);
			FractalBase fB = fD.getfBase();

			this.doSetup(fB, bI);
		} else {
			int curr = FractalBase.rawFbImages.size() - 1;
			System.out.println("inDoStep n>=size  fbCurrCount = "+FractalBase.fbCurrCount+ " and size is "+FractalBase.rawFbImages.size());
			FractalBase.fbCurrCount = curr;
			BufferedImage bI = FractalBase.rawFbImages.get(curr);
			FractalDtlInfo fD = FractalBase.fbDTlsList.get(curr);
			FractalBase fB = fD.getfBase();

			this.doSetup(fB, bI);
		}
		this.buBack.setEnabled(true);
		this.buFirst.setEnabled(true);
	}

	private void doBackCommand() {
		int numF = FractalBase.fbCurrCount;// FractalBase.fbDTlsList.size();

		if (numF > 1) {
			int curr = numF - 1;
			FractalBase.fbCurrCount = curr;
			System.out.println("inDoBACK n>1  fbCurrCount = "+FractalBase.fbCurrCount+ " and size is "+FractalBase.rawFbImages.size());
			BufferedImage bI = FractalBase.rawFbImages.get(curr);
			FractalDtlInfo fD = FractalBase.fbDTlsList.get(curr);
			FractalBase fB = fD.getfBase();

			this.doSetup(fB, bI);
		} else {
			FractalBase.fbCurrCount = 0;
			System.out.println("inDoBACK n<=1  fbCurrCount = "+FractalBase.fbCurrCount+ " and size is "+FractalBase.rawFbImages.size());
			BufferedImage bI = FractalBase.rawFbImages.get(0);
			FractalDtlInfo fD = FractalBase.fbDTlsList.get(0);
			FractalBase fB = fD.getfBase();

			this.doSetup(fB, bI);
		}
		
		this.buStep.setEnabled(true);
		this.buLast.setEnabled(true);
	}

	private void doSetup(FractalBase fB, BufferedImage bI) {
		this.fBase.dispose();
		this.fractalImage=bI;
		this.fBase = fB;
		//System.out.println(this.fBase.bufferedImage.equals(this.fractalImage));
		this.fBase.setVisible(true);
	}

	private void doUseRangeExtimates(boolean useRangeE) {
		this.useRangeEstimation = useRangeE;

		if (this.useRangeEstimation) {
			this.xMinTf.setEnabled(true);
			this.yMinTf.setEnabled(true);
			this.xMaxTf.setEnabled(true);
			this.yMaxTf.setEnabled(true);
		} else {
			this.xMinTf.setEnabled(false);
			this.yMinTf.setEnabled(false);
			this.xMaxTf.setEnabled(false);
			this.yMaxTf.setEnabled(false);
		}
	}
	
	private void showFBInfo() {
		this.formulaArea.setText("");		
		this.formulaArea.setEditable(true);		
		this.formulaArea.setText(this.fBase.getFractalDtlInfo());
		this.formulaArea.setVisible(true);
	}
	
	private void flushFBImages() {
		this.fBase.flushImages();
	}

	private void showFBImages() {
		List<BufferedImage> images = this.removeDuplicates(this.fBase.getImages());//this.fBase.getImages();//
		
		List<BufferedImage> resizedImages = new ArrayList<BufferedImage>();
		JFrame imagesFr = new JFrame();
//		imagesFr.setMinimumSize(new java.awt.Dimension(300,300));
		JPanel imgsPanel = new JPanel();
		
		JButton buSaveAllImages = new JButton("SaveAllImages");
		imgsPanel.add(buSaveAllImages);
		
		JButton buSaveAllIcons = new JButton("SaveAllIcons");
		imgsPanel.add(buSaveAllIcons);
		
		for (int i = 0; i < images.size(); i++) {
			try {
				BufferedImage resizedImage = this.resizeImage(images.get(i), 250, 250);
				resizedImages.add(resizedImage);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (int j = 0; j < resizedImages.size(); j++) {
			JLabel label = new JLabel(new ImageIcon(resizedImages.get(j)));
			imgsPanel.add(label);
		}
		
		buSaveAllImages.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				doSaveAllFbImagesCommand();				
			}

			private void doSaveAllFbImagesCommand() {
				BufferedImage[] imagesArr=new BufferedImage[images.size()];
				for(int i = 0; i < images.size(); i++) {
					imagesArr[i] = images.get(i);
				}
				BufferedImage joinedImage = FractalBase.joinAdjacentBufferedImages(imagesArr);
				
				File outputfile = new File("C:\\Users\\Navroz\\Desktop\\"+System.currentTimeMillis() + ".png");
				try {
					ImageIO.write(joinedImage, "png", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}});
		
		buSaveAllIcons.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				doSaveAllFbIconsCommand();				
			}

			private void doSaveAllFbIconsCommand() {
				BufferedImage[] resizedImagesArr=new BufferedImage[resizedImages.size()];
				for(int i = 0; i < resizedImages.size(); i++) {
					resizedImagesArr[i] = resizedImages.get(i);
				}
				BufferedImage joinedImage = FractalBase.joinAdjacentBufferedImages(resizedImagesArr);
				
				File outputfile = new File("C:\\Users\\Navroz\\Desktop\\"+System.currentTimeMillis() + ".png");
				try {
					ImageIO.write(joinedImage, "png", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}});
		
		
		
		imagesFr.getContentPane().add(new JScrollPane(imgsPanel));
		imagesFr.setVisible(true);
		imagesFr.pack();
		
	}

	private List<BufferedImage> removeDuplicates(List<BufferedImage> imgs) {
		List<BufferedImage> imgsList = new ArrayList<BufferedImage>();
		for(int i = 0; i < imgs.size(); i++) {
			BufferedImage bI = imgs.get(i);
			if(!imgsList.contains(bI)) {
				imgsList.add(bI);
			}
		}
		return imgsList;
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
        });
		
		this.diyJuliaUseLyapunovExpCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyJuliaUseLyapunovExpCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyJuliaUseLyapunovExpCommand(false);
				}
			}
        });
		
		this.diyJuliaExploreCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyJuliaExploreCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyJuliaExploreCommand(false);
				}
			}
        });
		
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
		
		this.diyJuliaAreaSizeCombos.addActionListener(new ActionListener() {
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
		
		this.setupDiyJuliaGeneratorListeners();
		
	}

	private void setupDiyJuliaGeneratorListeners() {
		///////////////////////////////////////////////////////////
		//generatorListeners
		this.diyJuliaGenCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyJuliaGenCommand(true);
					
					diyJuliaVaryColorCb.setVisible(true);
					diyJuliaVaryPixXTranCb.setVisible(true);
					diyJuliaVaryPixYTranCb.setVisible(true);
					diyJuliaVaryIntraPixXYCb.setVisible(true);
					diyJuliaVaryPixelZFuncCb.setVisible(true);
					diyJuliaVaryConstCFuncCb.setVisible(true);
					diyJuliaVaryPixelConstOpZCCb.setVisible(true);
					
					diyJuliaVaryGenConstantCb.setVisible(true);					
					diyJuliaGenRealFromLabel.setVisible(true);
					diyJuliaGenRealFromTf.setVisible(true);
					diyJuliaGenRealToLabel.setVisible(true);
					diyJuliaGenRealToTf.setVisible(true);
					diyJuliaGenRealJumpLabel.setVisible(true);
					diyJuliaGenRealJumpCombo.setVisible(true);
					diyJuliaGenImagFromLabel.setVisible(true);
					diyJuliaGenImagFromTf.setVisible(true);
					diyJuliaGenImagToLabel.setVisible(true);
					diyJuliaGenImagToTf.setVisible(true);
					diyJuliaGenImagJumpLabel.setVisible(true);
					diyJuliaGenImagJumpCombo.setVisible(true);
					
					diyJuliaGenRealFromTf.setEnabled(false);
					diyJuliaGenRealToTf.setEnabled(false);
					diyJuliaGenRealJumpCombo.setEnabled(false);
					diyJuliaGenImagFromTf.setEnabled(false);
					diyJuliaGenImagToTf.setEnabled(false);
					diyJuliaGenImagJumpCombo.setEnabled(false);
					
					diyJuliaVaryPixelPowerZCb.setVisible(true);

					diyJuliaGenPixelPowerZFromLabel.setVisible(true);
					diyJuliaGenPixelPowerZFromTf.setVisible(true);
					diyJuliaGenPixelPowerZToLabel.setVisible(true);
					diyJuliaGenPixelPowerZToTf.setVisible(true);
					diyJuliaGenPixelPowerZJumpLabel.setVisible(true);
					diyJuliaGenPixelPowerZJumpCombo.setVisible(true);

					diyJuliaGenPixelPowerZFromTf.setEnabled(false);
					diyJuliaGenPixelPowerZToTf.setEnabled(false);
					diyJuliaGenPixelPowerZJumpCombo.setEnabled(false);
					
					diyJuliaVaryFieldTypeCb.setVisible(true);
					diyJuliaVaryIterCb.setVisible(true);
					
					diyJuliaVaryBoundaryCb.setVisible(true);
					diyJuliaGenBoundaryFromLabel.setVisible(true);
					diyJuliaGenBoundaryFromTf.setVisible(true);
					diyJuliaGenBoundaryToLabel.setVisible(true);
					diyJuliaGenBoundaryToTf.setVisible(true);
					diyJuliaGenBoundaryJumpLabel.setVisible(true);	
					diyJuliaGenBoundaryJumpCombo.setVisible(true);
					
					diyJuliaGenBoundaryFromTf.setEnabled(false);
					diyJuliaGenBoundaryToTf.setEnabled(false);
					diyJuliaGenBoundaryJumpCombo.setEnabled(false);
					
					diyJuliaVaryPixXCentrCb.setVisible(true);
					diyJuliaVaryPixYCentrCb.setVisible(true);
					
					diyJuliaVaryScaleSizeCb.setVisible(true);
					diyJuliaGenScaleSizeFromLabel.setVisible(true);
					diyJuliaGenScaleSizeFromTf.setVisible(true);
					diyJuliaGenScaleSizeToLabel.setVisible(true);
					diyJuliaGenScaleSizeToTf.setVisible(true);
					diyJuliaGenScaleSizeJumpLabel.setVisible(true);	
					diyJuliaGenScaleSizeJumpCombo.setVisible(true);
					
					diyJuliaGenScaleSizeFromTf.setEnabled(false);
					diyJuliaGenScaleSizeToTf.setEnabled(false);
					diyJuliaGenScaleSizeJumpCombo.setEnabled(false);	
					
					diyJuliaUseDiffCb.setEnabled(false);
					invertPixelsCb.setEnabled(false);
					
					//diyJuliaFieldNoneRB.setEnabled(false);
					//diyJuliaFieldFatouRB.setEnabled(false);
					//diyJuliaFieldZSqRB.setEnabled(false);
					//diyJuliaFieldClassicRB.setEnabled(false);
					
					diyJuliaGenBu.setVisible(true);	
					diyJuliaGen2FolderBu.setVisible(true);

					/*diyJuliaRealTf.setEnabled(false);
					diyJuliaImgTf.setEnabled(false);
					diyJuliaKeepConstRb.setEnabled(false);
					diyJuliaCreateConstRb.setEnabled(false);*/

				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSetDiyJuliaGenCommand(false);

					diyJuliaVaryColorCb.setVisible(false);
					diyJuliaVaryPixXTranCb.setVisible(false);
					diyJuliaVaryPixYTranCb.setVisible(false);
					diyJuliaVaryIntraPixXYCb.setVisible(false);
					diyJuliaVaryPixelZFuncCb.setVisible(false);
					diyJuliaVaryConstCFuncCb.setVisible(false);
					diyJuliaVaryPixelConstOpZCCb.setVisible(false);
					
					diyJuliaVaryGenConstantCb.setVisible(false);					
					diyJuliaGenRealFromLabel.setVisible(false);
					diyJuliaGenRealFromTf.setVisible(false);
					diyJuliaGenRealToLabel.setVisible(false);
					diyJuliaGenRealToTf.setVisible(false);
					diyJuliaGenRealJumpLabel.setVisible(false);
					diyJuliaGenRealJumpCombo.setVisible(false);
					diyJuliaGenImagFromLabel.setVisible(false);
					diyJuliaGenImagFromTf.setVisible(false);
					diyJuliaGenImagToLabel.setVisible(false);
					diyJuliaGenImagToTf.setVisible(false);
					diyJuliaGenImagJumpLabel.setVisible(false);
					diyJuliaGenImagJumpCombo.setVisible(false);
					
					diyJuliaVaryPixelPowerZCb.setVisible(false);

					diyJuliaGenPixelPowerZFromLabel.setVisible(false);
					diyJuliaGenPixelPowerZFromTf.setVisible(false);
					diyJuliaGenPixelPowerZToLabel.setVisible(false);
					diyJuliaGenPixelPowerZToTf.setVisible(false);
					diyJuliaGenPixelPowerZJumpLabel.setVisible(false);
					diyJuliaGenPixelPowerZJumpCombo.setVisible(false);
					diyJuliaVaryFieldTypeCb.setVisible(false);
					diyJuliaVaryIterCb.setVisible(false);
					
					diyJuliaVaryBoundaryCb.setVisible(false);
					diyJuliaGenBoundaryFromLabel.setVisible(false);
					diyJuliaGenBoundaryFromTf.setVisible(false);
					diyJuliaGenBoundaryToLabel.setVisible(false);
					diyJuliaGenBoundaryToTf.setVisible(false);
					diyJuliaGenBoundaryJumpLabel.setVisible(false);	
					diyJuliaGenBoundaryJumpCombo.setVisible(false);					
					
					diyJuliaVaryPixXCentrCb.setVisible(false);
					diyJuliaVaryPixYCentrCb.setVisible(false);
					
					diyJuliaVaryScaleSizeCb.setVisible(false);
					diyJuliaGenScaleSizeFromLabel.setVisible(false);
					diyJuliaGenScaleSizeFromTf.setVisible(false);
					diyJuliaGenScaleSizeToLabel.setVisible(false);
					diyJuliaGenScaleSizeToTf.setVisible(false);
					diyJuliaGenScaleSizeJumpLabel.setVisible(false);	
					diyJuliaGenScaleSizeJumpCombo.setVisible(false);
					
					diyJuliaUseDiffCb.setEnabled(true);
					invertPixelsCb.setEnabled(true);
					
					diyJuliaFieldNoneRB.setEnabled(true);
					diyJuliaFieldFatouRB.setEnabled(true);
					diyJuliaFieldZSqRB.setEnabled(true);
					diyJuliaFieldClassicRB.setEnabled(true);
					
					diyJuliaGenBu.setVisible(false);
					diyJuliaGen2FolderBu.setVisible(false);
					

					/*diyJuliaRealTf.setEnabled(true);
					diyJuliaImgTf.setEnabled(true);
					diyJuliaKeepConstRb.setEnabled(true);
					diyJuliaCreateConstRb.setEnabled(true);*/
					
				}
			}
        });
		
		this.diyJuliaVaryColorCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryColorCommand(true);
					colorChoiceCombo.setEnabled(false);
					showAllColorsCb.setEnabled(false);
					colorSampleMixStartValsCombo.setEnabled(false);
					colorSampleDivValsCombo.setEnabled(false);
					colorBlowoutChoiceCombo.setEnabled(false);
					colorSuperBlowoutChoiceCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryColorCommand(false);
					colorChoiceCombo.setEnabled(true);
					showAllColorsCb.setEnabled(true);

					if (colorChoiceCombo.getSelectedItem().equals("SampleMix")) {
						colorSampleMixStartValsCombo.setVisible(true);
						colorSampleDivValsCombo.setVisible(true);
						colorSampleMixStartValsCombo.setEnabled(true);
						colorSampleDivValsCombo.setEnabled(true);
						colorBlowoutChoiceCombo.setVisible(false);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
					} else if(colorChoiceCombo.getSelectedItem().equals("ColorBlowout")) {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(true);
						colorBlowoutChoiceCombo.setEnabled(true);
						colorSuperBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
					} else if (colorChoiceCombo.getSelectedItem().equals("ColorSuperBlowout")) {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(false);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(true);
						colorSuperBlowoutChoiceCombo.setEnabled(true);
					} else {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(false);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
					}
				}
			}
		});
		
		this.diyJuliaVaryPixXTranCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryPixXTranCommand(true);					
					pxXTransformCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryPixXTranCommand(false);
					pxXTransformCombo.setEnabled(true);
				}
			}
		});

		
		this.diyJuliaVaryPixYTranCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryPixYTranCommand(true);
					pxYTransformCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryPixYTranCommand(false);
					pxYTransformCombo.setEnabled(true);
				}
			}
		});

		
		this.diyJuliaVaryIntraPixXYCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryIntraPixXYCommand(true);
					intraPixOperationCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryIntraPixXYCommand(false);
					intraPixOperationCombo.setEnabled(true);
				}
			}
		});
		

		this.diyJuliaVaryPixelZFuncCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryPixelZFuncCommand(true);
					pxFuncCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryPixelZFuncCommand(false);
					pxFuncCombo.setEnabled(true);
				}
			}
		});
		

		this.diyJuliaVaryConstCFuncCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryConstCFuncCommand(true);
					constFuncCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryConstCFuncCommand(false);
					constFuncCombo.setEnabled(true);
				}
			}
		});

		this.diyJuliaVaryPixelConstOpZCCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryPixelConstOpZCCommand(true);
					pxConstOprnCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryPixelConstOpZCCommand(false);
					pxConstOprnCombo.setEnabled(true);
				}
			}
		});
		

		this.diyJuliaVaryGenConstantCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryGenConstantCommand(true);
					diyJuliaGenRealFromLabel.setEnabled(true);
					diyJuliaGenRealFromTf.setEnabled(true);
					diyJuliaGenRealToLabel.setEnabled(true);
					diyJuliaGenRealToTf.setEnabled(true);
					diyJuliaGenRealJumpLabel.setEnabled(true);
					diyJuliaGenRealJumpCombo.setEnabled(true);
					diyJuliaGenImagFromLabel.setEnabled(true);
					diyJuliaGenImagFromTf.setEnabled(true);
					diyJuliaGenImagToLabel.setEnabled(true);
					diyJuliaGenImagToTf.setEnabled(true);
					diyJuliaGenImagJumpLabel.setEnabled(true);
					diyJuliaGenImagJumpCombo.setEnabled(true);
					
					diyJuliaKeepConstRb.setEnabled(false);
					diyJuliaCreateConstRb.setEnabled(false);
					diyJuliaRealTf.setEnabled(false);
					diyJuliaImgTf.setEnabled(false);
					
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryGenConstantCommand(false);
					diyJuliaGenRealFromLabel.setEnabled(false);
					diyJuliaGenRealFromTf.setEnabled(false);
					diyJuliaGenRealToLabel.setEnabled(false);
					diyJuliaGenRealToTf.setEnabled(false);
					diyJuliaGenRealJumpLabel.setEnabled(false);
					diyJuliaGenRealJumpCombo.setEnabled(false);
					diyJuliaGenImagFromLabel.setEnabled(false);
					diyJuliaGenImagFromTf.setEnabled(false);
					diyJuliaGenImagToLabel.setEnabled(false);
					diyJuliaGenImagToTf.setEnabled(false);
					diyJuliaGenImagJumpLabel.setEnabled(false);
					diyJuliaGenImagJumpCombo.setEnabled(false);
					
					diyJuliaKeepConstRb.setEnabled(true);
					diyJuliaCreateConstRb.setEnabled(true);
					if (diyJuliaKeepConstRb.isSelected()) {
						diyJuliaRealTf.setEnabled(false);
						diyJuliaImgTf.setEnabled(false);
					}
					if (diyJuliaCreateConstRb.isSelected()) {
						diyJuliaRealTf.setEnabled(true);
						diyJuliaImgTf.setEnabled(true);
					}
				}
			}
		});
		
//		this.diyJuliaVaryPixelPowerZCb.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent event) {
//				if (event.getStateChange() == ItemEvent.SELECTED) {
//					doSelectDiyJuliaVaryPixelPowerZCommand(true);
//					diyJuliaPowerCombos.setEnabled(false);
//				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
//					doSelectDiyJuliaVaryPixelPowerZCommand(false);
//					diyJuliaPowerCombos.setEnabled(true);
//				}
//			}
//		});
//		

		this.diyJuliaVaryPixelPowerZCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryPixelPowerZCommand(true);
					diyJuliaGenPixelPowerZFromTf.setEnabled(true);
					diyJuliaGenPixelPowerZToTf.setEnabled(true);
					diyJuliaGenPixelPowerZJumpCombo.setEnabled(true);
					diyJuliaPowerCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryPixelPowerZCommand(false);
					diyJuliaGenPixelPowerZFromTf.setEnabled(false);
					diyJuliaGenPixelPowerZToTf.setEnabled(false);
					diyJuliaGenPixelPowerZJumpCombo.setEnabled(false);
					diyJuliaPowerCombos.setEnabled(true);
				}
			}
		});


		this.diyJuliaGenPixelPowerZJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>) e.getSource();
				Integer diyJuliaVaryPixelPowerZComboOption = (Integer) cb.getSelectedItem();
				doSelectDiyJuliaVaryPixelPowerZCombosCommand(diyJuliaVaryPixelPowerZComboOption);
			}
		});
		
		this.diyJuliaVaryFieldTypeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryFieldTypesCommand(true);					

					diyJuliaFieldNoneRB.setEnabled(false);
					diyJuliaFieldFatouRB.setEnabled(false);
					diyJuliaFieldZSqRB.setEnabled(false);
					diyJuliaFieldClassicRB.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryFieldTypesCommand(false);

					diyJuliaFieldNoneRB.setEnabled(true);
					diyJuliaFieldFatouRB.setEnabled(true);
					diyJuliaFieldZSqRB.setEnabled(true);
					diyJuliaFieldClassicRB.setEnabled(true);
				}
			}
		});


		this.diyJuliaVaryIterCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryIterCommand(true);
					diyJuliaMaxIterCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryIterCommand(false);
					diyJuliaMaxIterCombos.setEnabled(true);
				}
			}
		});

		this.diyJuliaVaryBoundaryCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryBoundaryCommand(true);
					diyJuliaGenBoundaryFromTf.setEnabled(true);
					diyJuliaGenBoundaryToTf.setEnabled(true);
					diyJuliaGenBoundaryJumpCombo.setEnabled(true);
					diyJuliaBoundCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryBoundaryCommand(false);
					diyJuliaGenBoundaryFromTf.setEnabled(false);
					diyJuliaGenBoundaryToTf.setEnabled(false);
					diyJuliaGenBoundaryJumpCombo.setEnabled(false);
					diyJuliaBoundCombos.setEnabled(true);
				}
			}
		});
		
		this.diyJuliaVaryPixXCentrCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryPixXCentrCommand(true);
					diyJuliaXCCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryPixXCentrCommand(false);
					diyJuliaXCCombos.setEnabled(true);
				}
			}
		});
		
		this.diyJuliaVaryPixYCentrCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryPixYCentrCommand(true);
					diyJuliaYCCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryPixYCentrCommand(false);
					diyJuliaYCCombos.setEnabled(true);
				}
			}
		});
		

		this.diyJuliaVaryScaleSizeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaVaryScaleSizeCommand(true);
					diyJuliaGenScaleSizeFromTf.setEnabled(true);
					diyJuliaGenScaleSizeToTf.setEnabled(true);
					diyJuliaGenScaleSizeJumpCombo.setEnabled(true);
					diyJuliaScaleSizeCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaVaryScaleSizeCommand(false);
					diyJuliaGenScaleSizeFromTf.setEnabled(false);
					diyJuliaGenScaleSizeToTf.setEnabled(false);
					diyJuliaGenScaleSizeJumpCombo.setEnabled(false);
					diyJuliaScaleSizeCombos.setEnabled(true);
				}
			}
		});
		

		this.diyJuliaGenScaleSizeJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double diyJuliaVaryScaleSizeComboOption = (Double) cb.getSelectedItem();
				doSelectDiyJuliaVaryScaleSizeCombosCommand(diyJuliaVaryScaleSizeComboOption);
			}
		});
		
		this.diyJuliaGenBoundaryJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double diyJuliaVaryBoundaryComboOption = (Double) cb.getSelectedItem();
				doSelectDiyJuliaVaryBoundaryJumpCombosCommand(diyJuliaVaryBoundaryComboOption);
			}
		});
		
		
		this.diyJuliaGenRealJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyJuliaRealJumpComboOption = (Double)cb.getSelectedItem();
				doSelectDiyJuliaGenRealJumpCombosCommand(diyJuliaRealJumpComboOption);				
			}});
		
		
		this.diyJuliaGenImagJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyJuliaImagJumpComboOption = (Double)cb.getSelectedItem();
				doSelectDiyJuliaGenImagJumpCombosCommand(diyJuliaImagJumpComboOption);				
			}});
		
		
		this.diyJApplyFormulaZCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyJuliaApplyFormulaCommand(true);
					if (!diyJuliaGen) {
						diyJApplyFormulaTf.setVisible(true);
						diyJuliaGenVaryApplyFormulaTxtAreaSpane.setVisible(false);
						diyJuliaGenVaryApplyFormulaTxtArea.setVisible(false);
					} else {
						diyJApplyFormulaTf.setVisible(false);
						diyJuliaGenVaryApplyFormulaTxtAreaSpane.setVisible(true);
						diyJuliaGenVaryApplyFormulaTxtArea.setVisible(true);
					}
					//					diyJuliaPowerCombos.setEnabled(false);
					pxFuncCombo.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyJuliaApplyFormulaCommand(false);
					diyJApplyFormulaTf.setVisible(false);
//					diyJuliaPowerCombos.setEnabled(true);
					pxFuncCombo.setEnabled(true);
				}
			}
		});
		
		
		
		this.diyJuliaGenBu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().gc();
				doJuliaGenerateCommand();				
			}});
		
		this.diyJuliaGen2FolderBu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().gc();
				diyJuliaGen2Folder = true;
				
				// create an object of JFileChooser class 
				JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
				
				chooser.setFileFilter( new FileFilter(){
	                @Override
	                public boolean accept(File f) {
	                    return f.isDirectory();
	                }
	                @Override
	                public String getDescription() {
	                    return "Any folder";
	                }
	            });

				
				// invoke the showsSaveDialog function to show the save dialog
				int r = chooser.showSaveDialog(null);
				
				// if the user selects a file
				if (r == JFileChooser.APPROVE_OPTION) {
					diyJuliaGen2FolderPath  = chooser.getSelectedFile().getAbsolutePath();
//					doSaveImageCommand("save2File");
				}
				
				doJuliaGenerateCommand();				
			}});
		//////////////////////////endsGeneratorListeners/////////////////////////
		/////////////////////////////////////////////////////////////////////////
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
		
		this.diyMandIsBuddhaCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyMandIsBuddhaCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyMandIsBuddhaCommand(false);
				}
			}
        });
		
		this.diyMandUseLyapunovExpCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyMandUseLyapunovExpCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyMandUseLyapunovExpCommand(false);
				}
			}
        });
		
		this.diyMandExploreCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyMandelbrotExploreCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyMandelbrotExploreCommand(false);
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
		
		this.diyMandAreaSizeCombos.addActionListener(new ActionListener() {
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
		
		this.setupDiyMandGeneratorListeners();
	}
	


	private void setupDiyMandGeneratorListeners() {
		///////////////////////////////////////////////////////////
		//generatorListeners
		this.diyMandGenCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyMandGenCommand(true);
					
					diyMandVaryColorCb.setVisible(true);
					diyMandVaryPixXTranCb.setVisible(true);
					diyMandVaryPixYTranCb.setVisible(true);
					diyMandVaryIntraPixXYCb.setVisible(true);
					diyMandVaryPixelZFuncCb.setVisible(true);
					diyMandVaryConstCFuncCb.setVisible(true);
					diyMandVaryPixelConstOpZCCb.setVisible(true);
					
					diyMandVaryGenConstantCb.setVisible(true);					
					diyMandGenRealFromLabel.setVisible(true);
					diyMandGenRealFromTf.setVisible(true);
					diyMandGenRealToLabel.setVisible(true);
					diyMandGenRealToTf.setVisible(true);
					diyMandGenRealJumpLabel.setVisible(true);
					diyMandGenRealJumpCombo.setVisible(true);
					diyMandGenImagFromLabel.setVisible(true);
					diyMandGenImagFromTf.setVisible(true);
					diyMandGenImagToLabel.setVisible(true);
					diyMandGenImagToTf.setVisible(true);
					diyMandGenImagJumpLabel.setVisible(true);
					diyMandGenImagJumpCombo.setVisible(true);
					
					diyMandGenRealFromTf.setEnabled(false);
					diyMandGenRealToTf.setEnabled(false);
					diyMandGenRealJumpCombo.setEnabled(false);
					diyMandGenImagFromTf.setEnabled(false);
					diyMandGenImagToTf.setEnabled(false);
					diyMandGenImagJumpCombo.setEnabled(false);
					
					diyMandVaryPixelPowerZCb.setVisible(true);

					diyMandGenPixelPowerZFromLabel.setVisible(true);
					diyMandGenPixelPowerZFromTf.setVisible(true);
					diyMandGenPixelPowerZToLabel.setVisible(true);
					diyMandGenPixelPowerZToTf.setVisible(true);
					diyMandGenPixelPowerZJumpLabel.setVisible(true);
					diyMandGenPixelPowerZJumpCombo.setVisible(true);

					diyMandGenPixelPowerZFromTf.setEnabled(false);
					diyMandGenPixelPowerZToTf.setEnabled(false);
					diyMandGenPixelPowerZJumpCombo.setEnabled(false);
					
					
					diyMandVaryIterCb.setVisible(true);
					
					diyMandVaryBoundaryCb.setVisible(true);
					diyMandGenBoundaryFromLabel.setVisible(true);
					diyMandGenBoundaryFromTf.setVisible(true);
					diyMandGenBoundaryToLabel.setVisible(true);
					diyMandGenBoundaryToTf.setVisible(true);
					diyMandGenBoundaryJumpLabel.setVisible(true);	
					diyMandGenBoundaryJumpCombo.setVisible(true);
					
					diyMandGenBoundaryFromTf.setEnabled(false);
					diyMandGenBoundaryToTf.setEnabled(false);
					diyMandGenBoundaryJumpCombo.setEnabled(false);
					
					diyMandVaryPixXCentrCb.setVisible(true);
					diyMandVaryPixYCentrCb.setVisible(true);
					
					diyMandVaryScaleSizeCb.setVisible(true);
					diyMandGenScaleSizeFromLabel.setVisible(true);
					diyMandGenScaleSizeFromTf.setVisible(true);
					diyMandGenScaleSizeToLabel.setVisible(true);
					diyMandGenScaleSizeToTf.setVisible(true);
					diyMandGenScaleSizeJumpLabel.setVisible(true);	
					diyMandGenScaleSizeJumpCombo.setVisible(true);
					
					diyMandGenScaleSizeFromTf.setEnabled(false);
					diyMandGenScaleSizeToTf.setEnabled(false);
					diyMandGenScaleSizeJumpCombo.setEnabled(false);	
					
					diyMandUseDiffCb.setEnabled(false);
					invertPixelsCb.setEnabled(false);
					
					diyMandGenBu.setVisible(true);	
					diyMandGen2FolderBu.setVisible(true);

					/*diyMandRealTf.setEnabled(false);
					diyMandImgTf.setEnabled(false);
					diyMandKeepConstRb.setEnabled(false);
					diyMandCreateConstRb.setEnabled(false);*/

				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSetDiyMandGenCommand(false);

					diyMandVaryColorCb.setVisible(false);
					diyMandVaryPixXTranCb.setVisible(false);
					diyMandVaryPixYTranCb.setVisible(false);
					diyMandVaryIntraPixXYCb.setVisible(false);
					diyMandVaryPixelZFuncCb.setVisible(false);
					diyMandVaryConstCFuncCb.setVisible(false);
					diyMandVaryPixelConstOpZCCb.setVisible(false);
					
					diyMandVaryGenConstantCb.setVisible(false);					
					diyMandGenRealFromLabel.setVisible(false);
					diyMandGenRealFromTf.setVisible(false);
					diyMandGenRealToLabel.setVisible(false);
					diyMandGenRealToTf.setVisible(false);
					diyMandGenRealJumpLabel.setVisible(false);
					diyMandGenRealJumpCombo.setVisible(false);
					diyMandGenImagFromLabel.setVisible(false);
					diyMandGenImagFromTf.setVisible(false);
					diyMandGenImagToLabel.setVisible(false);
					diyMandGenImagToTf.setVisible(false);
					diyMandGenImagJumpLabel.setVisible(false);
					diyMandGenImagJumpCombo.setVisible(false);
					
					diyMandVaryPixelPowerZCb.setVisible(false);

					diyMandGenPixelPowerZFromLabel.setVisible(false);
					diyMandGenPixelPowerZFromTf.setVisible(false);
					diyMandGenPixelPowerZToLabel.setVisible(false);
					diyMandGenPixelPowerZToTf.setVisible(false);
					diyMandGenPixelPowerZJumpLabel.setVisible(false);
					diyMandGenPixelPowerZJumpCombo.setVisible(false);
					diyMandVaryIterCb.setVisible(false);
					
					diyMandVaryBoundaryCb.setVisible(false);
					diyMandGenBoundaryFromLabel.setVisible(false);
					diyMandGenBoundaryFromTf.setVisible(false);
					diyMandGenBoundaryToLabel.setVisible(false);
					diyMandGenBoundaryToTf.setVisible(false);
					diyMandGenBoundaryJumpLabel.setVisible(false);	
					diyMandGenBoundaryJumpCombo.setVisible(false);					
					
					diyMandVaryPixXCentrCb.setVisible(false);
					diyMandVaryPixYCentrCb.setVisible(false);
					
					diyMandVaryScaleSizeCb.setVisible(false);
					diyMandGenScaleSizeFromLabel.setVisible(false);
					diyMandGenScaleSizeFromTf.setVisible(false);
					diyMandGenScaleSizeToLabel.setVisible(false);
					diyMandGenScaleSizeToTf.setVisible(false);
					diyMandGenScaleSizeJumpLabel.setVisible(false);	
					diyMandGenScaleSizeJumpCombo.setVisible(false);
					
					diyMandUseDiffCb.setEnabled(true);
					invertPixelsCb.setEnabled(true);
					
					diyMandGenBu.setVisible(false);	
					diyMandGen2FolderBu.setVisible(false);
					

					/*diyMandRealTf.setEnabled(true);
					diyMandImgTf.setEnabled(true);
					diyMandKeepConstRb.setEnabled(true);
					diyMandCreateConstRb.setEnabled(true);*/
					
				}
			}
        });
		
		this.diyMandVaryColorCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryColorCommand(true);
					colorChoiceCombo.setEnabled(false);
					showAllColorsCb.setEnabled(false);
					colorSampleMixStartValsCombo.setEnabled(false);
					colorSampleDivValsCombo.setEnabled(false);
					colorBlowoutChoiceCombo.setEnabled(false);
					colorSuperBlowoutChoiceCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryColorCommand(false);
					colorChoiceCombo.setEnabled(true);
					showAllColorsCb.setEnabled(true);

					if (colorChoiceCombo.getSelectedItem().equals("SampleMix")) {
						colorSampleMixStartValsCombo.setVisible(true);
						colorSampleDivValsCombo.setVisible(true);
						colorSampleMixStartValsCombo.setEnabled(true);
						colorSampleDivValsCombo.setEnabled(true);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(false);
					} else if(colorChoiceCombo.getSelectedItem().equals("ColorBlowout")) {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setEnabled(true);
						colorBlowoutChoiceCombo.setVisible(true);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(false);
					} else if(colorChoiceCombo.getSelectedItem().equals("ColorSuperBlowout")) {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(true);
						colorSuperBlowoutChoiceCombo.setVisible(true);
					} else {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(false);
					}
				}
			}
		});
		
		this.diyMandVaryPixXTranCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryPixXTranCommand(true);					
					pxXTransformCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryPixXTranCommand(false);
					pxXTransformCombo.setEnabled(true);
				}
			}
		});

		
		this.diyMandVaryPixYTranCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryPixYTranCommand(true);
					pxYTransformCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryPixYTranCommand(false);
					pxYTransformCombo.setEnabled(true);
				}
			}
		});

		
		this.diyMandVaryIntraPixXYCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryIntraPixXYCommand(true);
					intraPixOperationCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryIntraPixXYCommand(false);
					intraPixOperationCombo.setEnabled(true);
				}
			}
		});
		

		this.diyMandVaryPixelZFuncCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryPixelZFuncCommand(true);
					pxFuncCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryPixelZFuncCommand(false);
					pxFuncCombo.setEnabled(true);
				}
			}
		});
		

		this.diyMandVaryConstCFuncCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryConstCFuncCommand(true);
					constFuncCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryConstCFuncCommand(false);
					constFuncCombo.setEnabled(true);
				}
			}
		});

		this.diyMandVaryPixelConstOpZCCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryPixelConstOpZCCommand(true);
					pxConstOprnCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryPixelConstOpZCCommand(false);
					pxConstOprnCombo.setEnabled(true);
				}
			}
		});
		

		this.diyMandVaryGenConstantCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryGenConstantCommand(true);
					diyMandGenRealFromLabel.setEnabled(true);
					diyMandGenRealFromTf.setEnabled(true);
					diyMandGenRealToLabel.setEnabled(true);
					diyMandGenRealToTf.setEnabled(true);
					diyMandGenRealJumpLabel.setEnabled(true);
					diyMandGenRealJumpCombo.setEnabled(true);
					diyMandGenImagFromLabel.setEnabled(true);
					diyMandGenImagFromTf.setEnabled(true);
					diyMandGenImagToLabel.setEnabled(true);
					diyMandGenImagToTf.setEnabled(true);
					diyMandGenImagJumpLabel.setEnabled(true);
					diyMandGenImagJumpCombo.setEnabled(true);
					
					diyMandelbrotKeepConstRb.setEnabled(false);
					diyMandelbrotCreateConstRb.setEnabled(false);
					diyMandRealTf.setEnabled(false);
					diyMandImgTf.setEnabled(false);
					
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryGenConstantCommand(false);
					diyMandGenRealFromLabel.setEnabled(false);
					diyMandGenRealFromTf.setEnabled(false);
					diyMandGenRealToLabel.setEnabled(false);
					diyMandGenRealToTf.setEnabled(false);
					diyMandGenRealJumpLabel.setEnabled(false);
					diyMandGenRealJumpCombo.setEnabled(false);
					diyMandGenImagFromLabel.setEnabled(false);
					diyMandGenImagFromTf.setEnabled(false);
					diyMandGenImagToLabel.setEnabled(false);
					diyMandGenImagToTf.setEnabled(false);
					diyMandGenImagJumpLabel.setEnabled(false);
					diyMandGenImagJumpCombo.setEnabled(false);
					
					diyMandelbrotKeepConstRb.setEnabled(true);
					diyMandelbrotCreateConstRb.setEnabled(true);
					if (diyMandelbrotKeepConstRb.isSelected()) {
						diyMandRealTf.setEnabled(false);
						diyMandImgTf.setEnabled(false);
					}
					if (diyMandelbrotCreateConstRb.isSelected()) {
						diyMandRealTf.setEnabled(true);
						diyMandImgTf.setEnabled(true);
					}
				}
			}
		});
		
//		this.diyMandVaryPixelPowerZCb.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent event) {
//				if (event.getStateChange() == ItemEvent.SELECTED) {
//					doSelectDiyMandVaryPixelPowerZCommand(true);
//					diyMandPowerCombos.setEnabled(false);
//				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
//					doSelectDiyMandVaryPixelPowerZCommand(false);
//					diyMandPowerCombos.setEnabled(true);
//				}
//			}
//		});
//		

		this.diyMandVaryPixelPowerZCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryPixelPowerZCommand(true);
					diyMandGenPixelPowerZFromTf.setEnabled(true);
					diyMandGenPixelPowerZToTf.setEnabled(true);
					diyMandGenPixelPowerZJumpCombo.setEnabled(true);
					diyMandExpCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryPixelPowerZCommand(false);
					diyMandGenPixelPowerZFromTf.setEnabled(false);
					diyMandGenPixelPowerZToTf.setEnabled(false);
					diyMandGenPixelPowerZJumpCombo.setEnabled(false);
					diyMandExpCombos.setEnabled(true);
				}
			}
		});


		this.diyMandGenPixelPowerZJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>) e.getSource();
				Integer diyMandVaryPixelPowerZComboOption = (Integer) cb.getSelectedItem();
				doSelectDiyMandVaryPixelPowerZCombosCommand(diyMandVaryPixelPowerZComboOption);
			}
		});


		this.diyMandVaryIterCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryIterCommand(true);
					diyMandMaxIterCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryIterCommand(false);
					diyMandMaxIterCombos.setEnabled(true);
				}
			}
		});

		this.diyMandVaryBoundaryCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryBoundaryCommand(true);
					diyMandGenBoundaryFromTf.setEnabled(true);
					diyMandGenBoundaryToTf.setEnabled(true);
					diyMandGenBoundaryJumpCombo.setEnabled(true);
					diyMandBoundCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryBoundaryCommand(false);
					diyMandGenBoundaryFromTf.setEnabled(false);
					diyMandGenBoundaryToTf.setEnabled(false);
					diyMandGenBoundaryJumpCombo.setEnabled(false);
					diyMandBoundCombos.setEnabled(true);
				}
			}
		});
		
		this.diyMandVaryPixXCentrCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryPixXCentrCommand(true);
					diyMandXCCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryPixXCentrCommand(false);
					diyMandXCCombos.setEnabled(true);
				}
			}
		});
		
		this.diyMandVaryPixYCentrCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryPixYCentrCommand(true);
					diyMandYCCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryPixYCentrCommand(false);
					diyMandYCCombos.setEnabled(true);
				}
			}
		});
		

		this.diyMandVaryScaleSizeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandVaryScaleSizeCommand(true);
					diyMandGenScaleSizeFromTf.setEnabled(true);
					diyMandGenScaleSizeToTf.setEnabled(true);
					diyMandGenScaleSizeJumpCombo.setEnabled(true);
					diyMandScaleSizeCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandVaryScaleSizeCommand(false);
					diyMandGenScaleSizeFromTf.setEnabled(false);
					diyMandGenScaleSizeToTf.setEnabled(false);
					diyMandGenScaleSizeJumpCombo.setEnabled(false);
					diyMandScaleSizeCombos.setEnabled(true);
				}
			}
		});
		

		this.diyMandGenScaleSizeJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double diyMandVaryScaleSizeComboOption = (Double) cb.getSelectedItem();
				doSelectDiyMandVaryScaleSizeCombosCommand(diyMandVaryScaleSizeComboOption);
			}
		});
		
		this.diyMandGenBoundaryJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double diyMandVaryBoundaryComboOption = (Double) cb.getSelectedItem();
				doSelectDiyMandVaryBoundaryJumpCombosCommand(diyMandVaryBoundaryComboOption);
			}
		});
		
		
		this.diyMandGenRealJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyMandRealJumpComboOption = (Double)cb.getSelectedItem();
				doSelectDiyMandGenRealJumpCombosCommand(diyMandRealJumpComboOption);				
			}});
		
		
		this.diyMandGenImagJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double diyMandImagJumpComboOption = (Double)cb.getSelectedItem();
				doSelectDiyMandGenImagJumpCombosCommand(diyMandImagJumpComboOption);				
			}});
		
		
		this.diyMandApplyFormulaZCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectDiyMandApplyFormulaCommand(true);
					if (!diyMandGen) {
						diyMandApplyFormulaTf.setVisible(true);
						diyMandGenVaryApplyFormulaTxtAreaSpane.setVisible(false);
						diyMandGenVaryApplyFormulaTxtArea.setVisible(false);
					} else {
						diyMandApplyFormulaTf.setVisible(false);
						diyMandGenVaryApplyFormulaTxtAreaSpane.setVisible(true);
						diyMandGenVaryApplyFormulaTxtArea.setVisible(true);
					}
					//					diyMandPowerCombos.setEnabled(false);
					pxFuncCombo.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectDiyMandApplyFormulaCommand(false);
					diyMandApplyFormulaTf.setVisible(false);
//					diyMandPowerCombos.setEnabled(true);
					pxFuncCombo.setEnabled(true);
				}
			}
		});
		
		
		
		this.diyMandGenBu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().gc();
				doMandelbrotGenerateCommand();				
			}});
		
		this.diyMandGen2FolderBu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().gc();
				diyMandGen2Folder = true;
				
				// create an object of JFileChooser class 
				JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
				
				chooser.setFileFilter( new FileFilter(){
	                @Override
	                public boolean accept(File f) {
	                    return f.isDirectory();
	                }
	                @Override
	                public String getDescription() {
	                    return "Any folder";
	                }
	            });

				
				// invoke the showsSaveDialog function to show the save dialog
				int r = chooser.showSaveDialog(null);
				
				// if the user selects a file
				if (r == JFileChooser.APPROVE_OPTION) {
					diyMandGen2FolderPath  = chooser.getSelectedFile().getAbsolutePath();
//					doSaveImageCommand("save2File");
				}
				
				doMandelbrotGenerateCommand();				
			}});
		
		//////////////////////////endsGeneratorListeners/////////////////////////
		/////////////////////////////////////////////////////////////////////////
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
		/*
		this.polyUseLyapunovExpCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetPolyUseLyapunovExpCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetPolyUseLyapunovExpCommand(false);
				}
			}
        });*/

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
		
		this.setupPolyGeneratorListeners();
	}
	

private void setupPolyGeneratorListeners() {
		///////////////////////////////////////////////////////////
		//generatorListeners
		this.polyGenCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetPolyGenCommand(true);
					
					polyVaryColorCb.setVisible(true);
					polyVaryPixXTranCb.setVisible(true);
					polyVaryPixYTranCb.setVisible(true);
					polyVaryIntraPixXYCb.setVisible(true);
					polyVaryPixelZFuncCb.setVisible(true);
					polyVaryConstCFuncCb.setVisible(true);
					polyVaryPixelConstOpZCCb.setVisible(true);
					
					polyVaryGenConstantCb.setVisible(true);					
					polyGenRealFromLabel.setVisible(true);
					polyGenRealFromTf.setVisible(true);
					polyGenRealToLabel.setVisible(true);
					polyGenRealToTf.setVisible(true);
					polyGenRealJumpLabel.setVisible(true);
					polyGenRealJumpCombo.setVisible(true);
					polyGenImagFromLabel.setVisible(true);
					polyGenImagFromTf.setVisible(true);
					polyGenImagToLabel.setVisible(true);
					polyGenImagToTf.setVisible(true);
					polyGenImagJumpLabel.setVisible(true);
					polyGenImagJumpCombo.setVisible(true);
					
					polyGenRealFromTf.setEnabled(false);
					polyGenRealToTf.setEnabled(false);
					polyGenRealJumpCombo.setEnabled(false);
					polyGenImagFromTf.setEnabled(false);
					polyGenImagToTf.setEnabled(false);
					polyGenImagJumpCombo.setEnabled(false);
					
					polyVaryPixelPowerZCb.setVisible(true);

					polyGenPixelPowerZFromLabel.setVisible(true);
					polyGenPixelPowerZFromTf.setVisible(true);
					polyGenPixelPowerZToLabel.setVisible(true);
					polyGenPixelPowerZToTf.setVisible(true);
					polyGenPixelPowerZJumpLabel.setVisible(true);
					polyGenPixelPowerZJumpCombo.setVisible(true);

					polyGenPixelPowerZFromTf.setEnabled(false);
					polyGenPixelPowerZToTf.setEnabled(false);
					polyGenPixelPowerZJumpCombo.setEnabled(false);
					

					polyVaryRCMTCb.setVisible(true);
					polyVaryIterCb.setVisible(true);
					
					polyVaryBoundaryCb.setVisible(true);
					polyGenBoundaryFromLabel.setVisible(true);
					polyGenBoundaryFromTf.setVisible(true);
					polyGenBoundaryToLabel.setVisible(true);
					polyGenBoundaryToTf.setVisible(true);
					polyGenBoundaryJumpLabel.setVisible(true);	
					polyGenBoundaryJumpCombo.setVisible(true);
					
					polyGenBoundaryFromTf.setEnabled(false);
					polyGenBoundaryToTf.setEnabled(false);
					polyGenBoundaryJumpCombo.setEnabled(false);
					
					polyVaryPixXCentrCb.setVisible(true);
					polyVaryPixYCentrCb.setVisible(true);
					
					polyVaryScaleSizeCb.setVisible(true);
					polyGenScaleSizeFromLabel.setVisible(true);
					polyGenScaleSizeFromTf.setVisible(true);
					polyGenScaleSizeToLabel.setVisible(true);
					polyGenScaleSizeToTf.setVisible(true);
					polyGenScaleSizeJumpLabel.setVisible(true);	
					polyGenScaleSizeJumpCombo.setVisible(true);
					
					polyGenScaleSizeFromTf.setEnabled(false);
					polyGenScaleSizeToTf.setEnabled(false);
					polyGenScaleSizeJumpCombo.setEnabled(false);	
					
					polyUseDiffCb.setEnabled(false);
					invertPixelsCb.setEnabled(false);
					
					polyGenBu.setVisible(true);	
					diyPolyGen2FolderBu.setVisible(true);

					/*polyRealTf.setEnabled(false);
					polyImgTf.setEnabled(false);
					polyKeepConstRb.setEnabled(false);
					polyCreateConstRb.setEnabled(false);*/

				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSetPolyGenCommand(false);

					polyVaryColorCb.setVisible(false);
					polyVaryPixXTranCb.setVisible(false);
					polyVaryPixYTranCb.setVisible(false);
					polyVaryIntraPixXYCb.setVisible(false);
					polyVaryPixelZFuncCb.setVisible(false);
					polyVaryConstCFuncCb.setVisible(false);
					polyVaryPixelConstOpZCCb.setVisible(false);
					
					polyVaryGenConstantCb.setVisible(false);					
					polyGenRealFromLabel.setVisible(false);
					polyGenRealFromTf.setVisible(false);
					polyGenRealToLabel.setVisible(false);
					polyGenRealToTf.setVisible(false);
					polyGenRealJumpLabel.setVisible(false);
					polyGenRealJumpCombo.setVisible(false);
					polyGenImagFromLabel.setVisible(false);
					polyGenImagFromTf.setVisible(false);
					polyGenImagToLabel.setVisible(false);
					polyGenImagToTf.setVisible(false);
					polyGenImagJumpLabel.setVisible(false);
					polyGenImagJumpCombo.setVisible(false);
					
					polyVaryPixelPowerZCb.setVisible(false);

					polyGenPixelPowerZFromLabel.setVisible(false);
					polyGenPixelPowerZFromTf.setVisible(false);
					polyGenPixelPowerZToLabel.setVisible(false);
					polyGenPixelPowerZToTf.setVisible(false);
					polyGenPixelPowerZJumpLabel.setVisible(false);
					polyGenPixelPowerZJumpCombo.setVisible(false);
					polyVaryRCMTCb.setVisible(false);
					polyVaryIterCb.setVisible(false);
					
					polyVaryBoundaryCb.setVisible(false);
					polyGenBoundaryFromLabel.setVisible(false);
					polyGenBoundaryFromTf.setVisible(false);
					polyGenBoundaryToLabel.setVisible(false);
					polyGenBoundaryToTf.setVisible(false);
					polyGenBoundaryJumpLabel.setVisible(false);	
					polyGenBoundaryJumpCombo.setVisible(false);					
					
					polyVaryPixXCentrCb.setVisible(false);
					polyVaryPixYCentrCb.setVisible(false);
					
					polyVaryScaleSizeCb.setVisible(false);
					polyGenScaleSizeFromLabel.setVisible(false);
					polyGenScaleSizeFromTf.setVisible(false);
					polyGenScaleSizeToLabel.setVisible(false);
					polyGenScaleSizeToTf.setVisible(false);
					polyGenScaleSizeJumpLabel.setVisible(false);	
					polyGenScaleSizeJumpCombo.setVisible(false);
					
					polyUseDiffCb.setEnabled(true);
					invertPixelsCb.setEnabled(true);
					
					polyGenBu.setVisible(false);
					diyPolyGen2FolderBu.setVisible(false);
					

					/*polyRealTf.setEnabled(true);
					polyImgTf.setEnabled(true);
					polyKeepConstRb.setEnabled(true);
					polyCreateConstRb.setEnabled(true);*/
					
				}
			}
        });
		
		this.polyVaryColorCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryColorCommand(true);
					colorChoiceCombo.setEnabled(false);
					showAllColorsCb.setEnabled(false);
					colorSampleMixStartValsCombo.setEnabled(false);
					colorSampleDivValsCombo.setEnabled(false);
					colorBlowoutChoiceCombo.setVisible(false);
					colorBlowoutChoiceCombo.setEnabled(false);
					colorSuperBlowoutChoiceCombo.setVisible(false);
					colorSuperBlowoutChoiceCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryColorCommand(false);
					colorChoiceCombo.setEnabled(true);
					showAllColorsCb.setEnabled(true);

					if (colorChoiceCombo.getSelectedItem().equals("SampleMix")) {
						colorSampleMixStartValsCombo.setVisible(true);
						colorSampleDivValsCombo.setVisible(true);
						colorSampleMixStartValsCombo.setEnabled(true);
						colorSampleDivValsCombo.setEnabled(true);
						colorBlowoutChoiceCombo.setVisible(false);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
					} else if(colorChoiceCombo.getSelectedItem().equals("ColorBlowout")) {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(true);
						colorBlowoutChoiceCombo.setEnabled(true);
						colorSuperBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
					} else if(colorChoiceCombo.getSelectedItem().equals("ColorSuperBlowout")) {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(false);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(true);
						colorSuperBlowoutChoiceCombo.setEnabled(true);
					} else {
						colorSampleMixStartValsCombo.setVisible(false);
						colorSampleDivValsCombo.setVisible(false);
						colorSampleMixStartValsCombo.setEnabled(false);
						colorSampleDivValsCombo.setEnabled(false);
						colorBlowoutChoiceCombo.setVisible(false);
						colorBlowoutChoiceCombo.setEnabled(false);
						colorSuperBlowoutChoiceCombo.setVisible(false);
						colorSuperBlowoutChoiceCombo.setEnabled(false);
					}
				}
			}
		});
		
		this.polyVaryPixXTranCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryPixXTranCommand(true);					
					pxXTransformCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryPixXTranCommand(false);
					pxXTransformCombo.setEnabled(true);
				}
			}
		});

		
		this.polyVaryPixYTranCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryPixYTranCommand(true);
					pxYTransformCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryPixYTranCommand(false);
					pxYTransformCombo.setEnabled(true);
				}
			}
		});

		
		this.polyVaryIntraPixXYCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryIntraPixXYCommand(true);
					intraPixOperationCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryIntraPixXYCommand(false);
					intraPixOperationCombo.setEnabled(true);
				}
			}
		});
		

		this.polyVaryPixelZFuncCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryPixelZFuncCommand(true);
					pxFuncCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryPixelZFuncCommand(false);
					pxFuncCombo.setEnabled(true);
				}
			}
		});
		

		this.polyVaryConstCFuncCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryConstCFuncCommand(true);
					constFuncCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryConstCFuncCommand(false);
					constFuncCombo.setEnabled(true);
				}
			}
		});

		this.polyVaryPixelConstOpZCCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryPixelConstOpZCCommand(true);
					pxConstOprnCombo.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryPixelConstOpZCCommand(false);
					pxConstOprnCombo.setEnabled(true);
				}
			}
		});
		

		this.polyVaryGenConstantCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryGenConstantCommand(true);
					polyGenRealFromLabel.setEnabled(true);
					polyGenRealFromTf.setEnabled(true);
					polyGenRealToLabel.setEnabled(true);
					polyGenRealToTf.setEnabled(true);
					polyGenRealJumpLabel.setEnabled(true);
					polyGenRealJumpCombo.setEnabled(true);
					polyGenImagFromLabel.setEnabled(true);
					polyGenImagFromTf.setEnabled(true);
					polyGenImagToLabel.setEnabled(true);
					polyGenImagToTf.setEnabled(true);
					polyGenImagJumpLabel.setEnabled(true);
					polyGenImagJumpCombo.setEnabled(true);
					
					polyKeepConstRb.setEnabled(false);
					polyCreateConstRb.setEnabled(false);
					polyRealTf.setEnabled(false);
					polyImgTf.setEnabled(false);
					
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryGenConstantCommand(false);
					polyGenRealFromLabel.setEnabled(false);
					polyGenRealFromTf.setEnabled(false);
					polyGenRealToLabel.setEnabled(false);
					polyGenRealToTf.setEnabled(false);
					polyGenRealJumpLabel.setEnabled(false);
					polyGenRealJumpCombo.setEnabled(false);
					polyGenImagFromLabel.setEnabled(false);
					polyGenImagFromTf.setEnabled(false);
					polyGenImagToLabel.setEnabled(false);
					polyGenImagToTf.setEnabled(false);
					polyGenImagJumpLabel.setEnabled(false);
					polyGenImagJumpCombo.setEnabled(false);
					
					polyKeepConstRb.setEnabled(true);
					polyCreateConstRb.setEnabled(true);
					if (polyKeepConstRb.isSelected()) {
						polyRealTf.setEnabled(false);
						polyImgTf.setEnabled(false);
					}
					if (polyCreateConstRb.isSelected()) {
						polyRealTf.setEnabled(true);
						polyImgTf.setEnabled(true);
					}
				}
			}
		});
		
//		this.polyVaryPixelPowerZCb.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent event) {
//				if (event.getStateChange() == ItemEvent.SELECTED) {
//					doSelectPolyVaryPixelPowerZCommand(true);
//					polyPowerCombos.setEnabled(false);
//				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
//					doSelectPolyVaryPixelPowerZCommand(false);
//					polyPowerCombos.setEnabled(true);
//				}
//			}
//		});
//		

		this.polyVaryPixelPowerZCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryPixelPowerZCommand(true);
					polyGenPixelPowerZFromTf.setEnabled(true);
					polyGenPixelPowerZToTf.setEnabled(true);
					polyGenPixelPowerZJumpCombo.setEnabled(true);
					polyExpCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryPixelPowerZCommand(false);
					polyGenPixelPowerZFromTf.setEnabled(false);
					polyGenPixelPowerZToTf.setEnabled(false);
					polyGenPixelPowerZJumpCombo.setEnabled(false);
					polyExpCombos.setEnabled(true);
				}
			}
		});


		this.polyGenPixelPowerZJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>) e.getSource();
				Integer polyVaryPixelPowerZComboOption = (Integer) cb.getSelectedItem();
				doSelectPolyVaryPixelPowerZCombosCommand(polyVaryPixelPowerZComboOption);
			}
		});
		this.polyVaryRCMTCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryTypeCommand(true);
					polyTypeCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryTypeCommand(false);
					polyTypeCombos.setEnabled(true);
				}
			}
		});


		this.polyVaryIterCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryIterCommand(true);
					polyMaxIterCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryIterCommand(false);
					polyMaxIterCombos.setEnabled(true);
				}
			}
		});

		this.polyVaryBoundaryCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryBoundaryCommand(true);
					polyGenBoundaryFromTf.setEnabled(true);
					polyGenBoundaryToTf.setEnabled(true);
					polyGenBoundaryJumpCombo.setEnabled(true);
					polyBoundCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryBoundaryCommand(false);
					polyGenBoundaryFromTf.setEnabled(false);
					polyGenBoundaryToTf.setEnabled(false);
					polyGenBoundaryJumpCombo.setEnabled(false);
					polyBoundCombos.setEnabled(true);
				}
			}
		});
		
		this.polyVaryPixXCentrCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryPixXCentrCommand(true);
					polyXCCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryPixXCentrCommand(false);
					polyXCCombos.setEnabled(true);
				}
			}
		});
		
		this.polyVaryPixYCentrCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryPixYCentrCommand(true);
					polyYCCombos.setEnabled(false);
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryPixYCentrCommand(false);
					polyYCCombos.setEnabled(true);
				}
			}
		});
		

		this.polyVaryScaleSizeCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyVaryScaleSizeCommand(true);
					polyGenScaleSizeFromTf.setEnabled(true);
					polyGenScaleSizeToTf.setEnabled(true);
					polyGenScaleSizeJumpCombo.setEnabled(true);
					polyScaleSizeCombos.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyVaryScaleSizeCommand(false);
					polyGenScaleSizeFromTf.setEnabled(false);
					polyGenScaleSizeToTf.setEnabled(false);
					polyGenScaleSizeJumpCombo.setEnabled(false);
					polyScaleSizeCombos.setEnabled(true);
				}
			}
		});
		

		this.polyGenScaleSizeJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double polyVaryScaleSizeComboOption = (Double) cb.getSelectedItem();
				doSelectPolyVaryScaleSizeCombosCommand(polyVaryScaleSizeComboOption);
			}
		});
		
		this.polyGenBoundaryJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>) e.getSource();
				Double polyVaryBoundaryComboOption = (Double) cb.getSelectedItem();
				doSelectPolyVaryBoundaryJumpCombosCommand(polyVaryBoundaryComboOption);
			}
		});
		
		
		this.polyGenRealJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double polyRealJumpComboOption = (Double)cb.getSelectedItem();
				doSelectPolyGenRealJumpCombosCommand(polyRealJumpComboOption);				
			}});
		
		
		this.polyGenImagJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double polyImagJumpComboOption = (Double)cb.getSelectedItem();
				doSelectPolyGenImagJumpCombosCommand(polyImagJumpComboOption);				
			}});
		
		
		this.polyApplyFormulaZCb.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectPolyApplyFormulaCommand(true);
					if (!polyGen) {
						polyApplyFormulaTf.setVisible(true);
						polyGenVaryApplyFormulaTxtAreaSpane.setVisible(false);
						polyGenVaryApplyFormulaTxtArea.setVisible(false);
					} else {
						polyApplyFormulaTf.setVisible(false);
						polyGenVaryApplyFormulaTxtAreaSpane.setVisible(true);
						polyGenVaryApplyFormulaTxtArea.setVisible(true);
					}
					//					polyPowerCombos.setEnabled(false);
					pxFuncCombo.setEnabled(false);
					
				} else if (event.getStateChange() == ItemEvent.DESELECTED) {
					doSelectPolyApplyFormulaCommand(false);
					polyApplyFormulaTf.setVisible(false);
//					polyPowerCombos.setEnabled(true);
					pxFuncCombo.setEnabled(true);
				}
			}
		});
		
		
		
		this.polyGenBu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().gc();
				doPolyGenerateCommand();				
			}});
		
		this.diyPolyGen2FolderBu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runtime.getRuntime().gc();
				diyPolyGen2Folder = true;
				
				// create an object of JFileChooser class 
				JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
				
				chooser.setFileFilter( new FileFilter(){
	                @Override
	                public boolean accept(File f) {
	                    return f.isDirectory();
	                }
	                @Override
	                public String getDescription() {
	                    return "Any folder";
	                }
	            });

				
				// invoke the showsSaveDialog function to show the save dialog
				int r = chooser.showSaveDialog(null);
				
				// if the user selects a file
				if (r == JFileChooser.APPROVE_OPTION) {
					diyPolyGen2FolderPath  = chooser.getSelectedFile().getAbsolutePath();
//					doSaveImageCommand("save2File");
				}
				
				doPolyGenerateCommand();				
			}});
		
		//////////////////////////endsGeneratorListeners/////////////////////////
		/////////////////////////////////////////////////////////////////////////
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
		
		this.mandIsBuddhaCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetMandIsBuddhaCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetMandIsBuddhaCommand(false);
				}
			}
        });
		
		this.mandIsMotionbrotCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetMandIsMotionCommand(true);
					mandMotionParamLabel.setVisible(true);
					mandMotionParamCombo.setVisible(true);
					mandMotionParamJumpLabel.setVisible(true);
					mandMotionParamJumpCombo.setVisible(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetMandIsMotionCommand(false);
					mandMotionParamLabel.setVisible(false);
					mandMotionParamCombo.setVisible(false);
					mandMotionParamJumpLabel.setVisible(false);
					mandMotionParamJumpCombo.setVisible(false);
				}
			}
        });
		

		this.mandMotionParamCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String mandMotionParamComboOption = (String)cb.getSelectedItem();
				doSelectMandMotionParamCombosCommand(mandMotionParamComboOption);				
			}});
		
		this.mandMotionParamJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Double> cb = (JComboBox<Double>)e.getSource();
				Double mandMotionParamJumpOption = (Double)cb.getSelectedItem();
				doSelectMandMotionParamJumpCombosCommand(mandMotionParamJumpOption);				
			}});

		
		this.mandUseLyapunovExpCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetMandUseLyapunovExpCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetMandUseLyapunovExpCommand(false);
				}
			}
        });
		

		
		this.mandExploreCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetMandelbrotExploreCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetMandelbrotExploreCommand(false);
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
		
		this.juliaExploreCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetJuliaExploreCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetJuliaExploreCommand(false);
				}
			}
        });
		
		this.juliaUseLyapunovExpCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetJuliaUseLyapunovExpCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetJuliaUseLyapunovExpCommand(false);
				}
			}
        });
		
		this.juliaFieldNoneRB.addActionListener(this.fieldLinesRBListener());
		this.juliaFieldFatouRB.addActionListener(this.fieldLinesRBListener());
		this.juliaFieldZSqRB.addActionListener(this.fieldLinesRBListener());
		this.juliaFieldClassicRB.addActionListener(this.fieldLinesRBListener());
		
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
				if(!apolloTriangleCb.isSelected()){
					JComboBox<String> cb = (JComboBox<String>)e.getSource();
					String curvComboOption = (String)cb.getSelectedItem();
					doSelectCurvCombosCommand(curvComboOption);
				}
			}
			});	
		
		
		this.apolloTriangleCb.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				if(apolloTriangleCb.isSelected()){
					curvCombos.setEnabled(false);
				}else{
					curvCombos.setEnabled(true);
				}
			}
			});
		
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
		this.sierpinskiCreateGasketCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetSierpinskiCreateGasketCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetSierpinskiCreateGasketCommand(false);
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
		        Integer ratioComboOption = (Integer)cb.getSelectedIndex();
		        int ratioVal = cb.getItemAt(ratioComboOption);
				doSelectRatioCombosCommand(ratioVal);				
			}
		});
	}
	
	
	/////////////////////////4Attractors//////////////////////////////////////////

	private void setupAttractorsListeners() {
		this.attractorChoiceCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String attractorChoiceVal = (String)cb.getSelectedItem();
				doSelectAttractorChoiceCombosCommand(attractorChoiceVal);				
			}
		});

		this.attractorDimChoiceCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectAttractorDimChoiceCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSelectAttractorDimChoiceCommand(false);
				}
			}
        });

		this.attractorPixellateChoiceCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectAttractorPixellateChoiceCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSelectAttractorPixellateChoiceCommand(false);
				}
			}
        });
		
		this.attractorSingularChoiceCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectSingularAttractorChoiceCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSelectSingularAttractorChoiceCommand(false);
				}
			}
        });
		
		this.attractorInstantDrawCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectAttractorInstantDrawCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSelectAttractorInstantDrawCommand(false);
				}
			}
        });
		
		
		this.attractorPauseJumpCombo.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
		        Integer attractorPauseJumpVal = (Integer)cb.getSelectedItem();
		        doSelectAttractorPauseJumpComboChoice(attractorPauseJumpVal);				
			}
		});

		
		this.attractorTimeInvariantCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectAttractorTimeInvariantCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSelectAttractorTimeInvariantCommand(false);
				}
			}
        });
		

		this.attractorTimeIterDependantCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSelectAttractorTimeIterDependantCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSelectAttractorTimeIterDependantCommand(false);
				}
			}
        });
		
		/*this.attractorSpace2DChoiceCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String attractorSpace2DChoiceVal = (String)cb.getSelectedItem();
				doSelectAttractorSpace2DChoiceCombosCommand(attractorSpace2DChoiceVal);				
			}
		});*/
		
		this.attractorSpace2DChoiceList.addListSelectionListener(new ListSelectionListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent e) {
				JList<String> jl = (JList<String>)e.getSource();
				List<String> sp2dChoices = (List<String>)jl.getSelectedValuesList();
				doSelectAttractorSpace2DChoicesListCommand(sp2dChoices);
			}
		});
		
		this.attr1ColorChooserBu.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				attractor1Color = JColorChooser.showDialog(null, "Choose Attractor1 Color", Color.black);
				attr1ColorChooserBu.setForeground(attractor1Color);
				attr1ColorChooserBu.setBackground(attractor1Color);
				if (attrSeed_ClrChList.size() >= 1) {
					attrSeed_ClrChList.set(0, attractor1Color);
				} else {
					attrSeed_ClrChList.add(attractor1Color);
				}
			}
		});

		this.attr2ColorChooserBu.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				attractor2Color = JColorChooser.showDialog(null, "Choose Attractor2 Color", Color.black);
				attr2ColorChooserBu.setForeground(attractor2Color);
				attr2ColorChooserBu.setBackground(attractor2Color);
				if (attrSeed_ClrChList.size() >= 2) {
					attrSeed_ClrChList.set(1, attractor2Color);
				} else {
					attrSeed_ClrChList.add(attractor2Color);
				}
			}
		});
		
		this.addAttrBu.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				/*JPanel anAttrSeedPanel = new JPanel(new GridLayout(1, 8));
				anAttrSeedPanel.setVisible(true);
				anAttrSeedPanel.setEnabled(true);*/

				int size = attrSeed_X_tfList.size() + 1;
				final JLabel nxtAttr_X_Label = new JLabel("Attractor " + size + "  Seed   X:");
				attrSeed_X_lbList.add(nxtAttr_X_Label);
				attractorsSeedsPanel.add(nxtAttr_X_Label);
				JTextField anAttrSeedX_tf = new JTextField(2);
				attractorsSeedsPanel.add(anAttrSeedX_tf);
				attrSeed_X_tfList.add(anAttrSeedX_tf);
				final JLabel nxtAttr_Y_Label = new JLabel("   Y:");
				attrSeed_Y_lbList.add(nxtAttr_Y_Label);
				attractorsSeedsPanel.add(nxtAttr_Y_Label);
				JTextField anAttrSeedY_tf = new JTextField(2);
				attractorsSeedsPanel.add(anAttrSeedY_tf);
				attrSeed_Y_tfList.add(anAttrSeedY_tf);

				if (attractorDimChoiceCb.isSelected()) {
					final JLabel nxtAttr_Z_Label = new JLabel("   Z:");
					attrSeed_Z_lbList.add(nxtAttr_Z_Label);
					attractorsSeedsPanel.add(nxtAttr_Z_Label);
					JTextField anAttrSeedZ_tf = new JTextField(2);
					attractorsSeedsPanel.add(anAttrSeedZ_tf);
					attrSeed_Z_tfList.add(anAttrSeedZ_tf);
				}
				
				JButton nxtAttr_Clr_Bu = new JButton();
				nxtAttr_Clr_Bu.addActionListener(addAttractorColorListener(nxtAttr_Clr_Bu,size));
				attractorsSeedsPanel.add(nxtAttr_Clr_Bu);
				
				attractorsSeedsPanel.add(removeAttrBu);
				attractorsSeedsPanel.add(addAttrBu);

				/*attractorsSeedsPanel.add(anAttrSeedPanel);*/

				attractorsSeedsPanel.revalidate();
				attractorsSeedsPanel.repaint();

				attractorsPanel.revalidate();
				attractorsPanel.repaint();

				revalidate();
				repaint();
			}
		});
		

		this.removeAttrBu.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				if (attrSeed_X_lbList.size() <= 2) {
					attractorsSeedsPanel.remove(removeAttrBu);
				}
				JLabel lastAttr_X_Label = attrSeed_X_lbList.get(attrSeed_X_lbList.size() - 1);
				attrSeed_X_lbList.remove(lastAttr_X_Label);//(attrSeed_X_lbList.size() - 1);
				JLabel lastAttr_Y_Label = attrSeed_Y_lbList.get(attrSeed_Y_lbList.size() - 1);
				attrSeed_Y_lbList.remove(lastAttr_Y_Label);//(attrSeed_Y_lbList.size() - 1);

				JTextField lastAttr_X_tf = attrSeed_X_tfList.get(attrSeed_X_tfList.size()-1);
				attrSeed_X_tfList.remove(lastAttr_X_tf);

				JTextField lastAttr_Y_tf = attrSeed_Y_tfList.get(attrSeed_Y_tfList.size()-1);
				attrSeed_Y_tfList.remove(lastAttr_Y_tf);
				
				JLabel lastAttr_Z_Label = null;
				JTextField lastAttr_Z_tf = null;
				
				if (attractorDimChoiceCb.isSelected()) {
					lastAttr_Z_Label = attrSeed_Z_lbList.get(attrSeed_Z_lbList.size() - 1);
					attrSeed_Z_lbList.remove(lastAttr_Z_Label);

					lastAttr_Z_tf = attrSeed_Z_tfList.get(attrSeed_Z_tfList.size()-1);
					attrSeed_Z_tfList.remove(lastAttr_Z_tf);
				}
				
				JButton lastAttr_Clr_Bu = attrSeed_Clr_buList.get(attrSeed_Clr_buList.size() - 1);
				attrSeed_Clr_buList.remove(lastAttr_Clr_Bu);
				Color lastAttr_Color = attrSeed_ClrChList.get(attrSeed_ClrChList.size() - 1);
				attrSeed_ClrChList.remove(lastAttr_Color);

				attractorsSeedsPanel.remove(lastAttr_X_Label);
				attractorsSeedsPanel.remove(lastAttr_Y_Label);
				attractorsSeedsPanel.remove(lastAttr_X_tf);
				attractorsSeedsPanel.remove(lastAttr_Y_tf);
				
				if (attractorDimChoiceCb.isSelected()) {
					attractorsSeedsPanel.remove(lastAttr_Z_Label);
					attractorsSeedsPanel.remove(lastAttr_Z_tf);
				}
				
				attractorsSeedsPanel.remove(lastAttr_Clr_Bu);
				
				attractorsSeedsPanel.revalidate();
				attractorsSeedsPanel.repaint();

				attractorsPanel.revalidate();
				attractorsPanel.repaint();

				revalidate();
				repaint();
			}
		});
		
//		_O_
//		this.addAttrBu.addActionListener(new ActionListener() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				JPanel anAttrSeedPanel = new JPanel(new GridLayout(1, 8));
//				anAttrSeedPanel.setVisible(true);
//				anAttrSeedPanel.setEnabled(true);
//
//				int size = attrSeed_X_tfList.size() + 1;
//				anAttrSeedPanel.add(new JLabel("Attractor " + size + "  Seed   X:"));
//				JTextField anAttrSeedX_tf = new JTextField(2);
//				anAttrSeedPanel.add(anAttrSeedX_tf);
//				attrSeed_X_tfList.add(anAttrSeedX_tf);
//				anAttrSeedPanel.add(new JLabel("   Y:"));
//				JTextField anAttrSeedY_tf = new JTextField(2);
//				anAttrSeedPanel.add(anAttrSeedY_tf);
//				attrSeed_Y_tfList.add(anAttrSeedY_tf);
//
//				if (attractorDimChoiceCb.isSelected()) {
//					anAttrSeedPanel.add(new JLabel("   Z:"));
//					JTextField anAttrSeedZ_tf = new JTextField(2);
//					anAttrSeedPanel.add(anAttrSeedZ_tf);
//					attrSeed_Z_tfList.add(anAttrSeedZ_tf);
//				}
//				anAttrSeedPanel.add(removeAttrBu);
//				anAttrSeedPanel.add(addAttrBu);
//
//				attractorsSeedsPanel.add(anAttrSeedPanel);
//				/*
//				 * attractorsSeedsPanel.revalidate();
//				 * attractorsSeedsPanel.repaint();
//				 */
//
//				/*
//				 * attractorsPanel.revalidate();
//				 * attractorsPanel.repaint();
//				 */
//
//				revalidate();
//				repaint();
//			}
//		});
	}

	protected ActionListener addAttractorColorListener(JButton nxtAttrColorBu, int num) {
		return new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				setNxtAttrColor(nxtAttrColorBu, num);
			}
		};
	}

	private void setNxtAttrColor(JButton nxtAttrClrBu, int attrSize) {
		Color nxtAttr_Color = JColorChooser.showDialog(null, "Choose Attractor: " + attrSize + " Color", Color.black);
		nxtAttrClrBu.setForeground(nxtAttr_Color);
		nxtAttrClrBu.setBackground(nxtAttr_Color);
		if (attrSeed_ClrChList.size() > (attrSize - 1)) {
			attrSeed_ClrChList.set(attrSize - 1, nxtAttr_Color);
			attrSeed_Clr_buList.set(attrSize - 1, nxtAttrClrBu);
		} else {
			attrSeed_ClrChList.add(nxtAttr_Color);
			attrSeed_Clr_buList.add(nxtAttrClrBu);
		}
	}	

	private void doSelectAttractorInstantDrawCommand(boolean instantDraw) {
		this.isAttractorInstantDraw = instantDraw;

		if (!this.isAttractorInstantDraw) {
			this.attractorPauseJumpCombo.setEnabled(true);
		} else {
			this.attractorPauseJumpCombo.setEnabled(false);
		}
	}
	
	private void doSelectAttractorPauseJumpComboChoice(int jump) {
		this.attractorPauseJump = jump;
	}
	
	private void doSelectAttractorTimeInvariantCommand(boolean invariant) {
		this.isAttractorTimeInvariant = invariant;

		if (this.isAttractorTimeInvariant) {
			this.attrDeltaTime_tf.setEnabled(false);
		} else {
			this.attrDeltaTime_tf.setEnabled(true);
		}
	}
	

	private void doSelectAttractorTimeIterDependantCommand(boolean timeIterDependant) {
		this.isAttractorTimeIterDependant = timeIterDependant;
	}

	private void doSelectSingularAttractorChoiceCommand(boolean isSingular) {
		this.isSingularAttractor = isSingular;

		if (isSingular) {
			this.attr2SeedX_tf.setEnabled(false);
			this.attr2SeedY_tf.setEnabled(false);
			this.attr2SeedZ_tf.setEnabled(false);
			this.attr2ColorChooserBu.setEnabled(false);
			this.addAttrBu.setEnabled(false);

		} else {
			this.attr2SeedX_tf.setEnabled(true);
			this.attr2SeedY_tf.setEnabled(true);
			this.attr2SeedZ_tf.setEnabled(true);
			this.attr2ColorChooserBu.setEnabled(true);			
			this.addAttrBu.setEnabled(true);
		}
	}

	private void doSelectAttractorPixellateChoiceCommand(boolean pixellateChoice) {
		this.isAttractorPixellated = pixellateChoice;
	}

	private void doSelectAttractorDimChoiceCommand(boolean dimChoice) {
		this.isAttractorDimSpace3D = dimChoice;

		if (!this.isAttractorDimSpace3D) {
			/*
			 * this.attractorSpace2DChoiceCombos.setModel(new
			 * DefaultComboBoxModel<String>(ATTRACTOR_SPACE_2D_CHOICES));
			 */
			this.attractorSpace2DChoiceList.setModel(new DefaultComboBoxModel<String>(ATTRACTOR_SPACE_2D_CHOICES));

			this.attr1SeedZ_tf.setEnabled(false);
			this.attr2SeedZ_tf.setEnabled(false);

			this.attrCustom_DeltaZFormula_tf.setEnabled(false);

		} else {
			/*
			 * this.attractorSpace2DChoiceCombos.setModel(new
			 * DefaultComboBoxModel<String>(ATTRACTOR_SPACE_3D_CHOICES));
			 */
			this.attractorSpace2DChoiceList.setModel(new DefaultComboBoxModel<String>(ATTRACTOR_SPACE_3D_CHOICES));

			this.attr1SeedZ_tf.setEnabled(true);
			this.attr2SeedZ_tf.setEnabled(true);

			this.attrCustom_DeltaZFormula_tf.setEnabled(true);
		}
	}
	
	private void doSelectAttractorSpace2DChoicesListCommand(List<String> sp2dChoices) {
		this.attractorSpace2DList	=	sp2dChoices;
	}
	
	
	/*private void doSelectAttractorSpace2DChoiceCombosCommand(String attractorSpace2DChoiceVal) {
		this.attractorSpace2DSelectionChoice	=	attractorSpace2DChoiceVal;
	}*/

	private void doSelectAttractorChoiceCombosCommand(String attractorChoiceVal) {
		this.attractorSelectionChoice = attractorChoiceVal;

		if (this.attractorSelectionChoice.equals("custom")) {
			this.attrCustomFormulaStrLabel.setVisible(true);
			this.attrCustomFormulaStrDeltaXLabel.setVisible(true);
			this.attrCustom_DeltaXFormula_tf.setVisible(true);
			this.attrCustomFormulaStrDeltaYLabel.setVisible(true);
			this.attrCustom_DeltaYFormula_tf.setVisible(true);
			this.attrCustomFormulaStrDeltaZLabel.setVisible(true);
			this.attrCustom_DeltaZFormula_tf.setVisible(true);
			
			this.attractorTimeInvariantCb.setEnabled(true);
			this.attractorTimeInvariantCb.setSelected(false);
			this.isAttractorTimeInvariant = false;
			

			this.attractorTimeIterDependantCb.setEnabled(true);
			this.attractorTimeIterDependantCb.setSelected(false);
			/*this.isAttractorTimeIterDependant = false;*/
			
			this.attractorDimChoiceCb.setEnabled(true);
			this.attractorDimChoiceCb.setSelected(true);
			this.isAttractorDimSpace3D = true;
			
			this.setupAttractorParamChoices();
		} else {
			this.attrCustomFormulaStrLabel.setVisible(false);
			this.attrCustomFormulaStrDeltaXLabel.setVisible(false);
			this.attrCustom_DeltaXFormula_tf.setVisible(false);
			this.attrCustomFormulaStrDeltaYLabel.setVisible(false);
			this.attrCustom_DeltaYFormula_tf.setVisible(false);
			this.attrCustomFormulaStrDeltaZLabel.setVisible(false);
			this.attrCustom_DeltaZFormula_tf.setVisible(false);
			
			this.setupAttractorParamChoices();
		}
		
		this.buStart.setEnabled(true);
	}

	private void setupAttractorParamChoices() {

		boolean isSingular = this.isSingularAttractor;
		if (this.attractorSelectionChoice.equals("lorenz")) {
			this.attrLorenzSigmaLabel.setVisible(true);
			this.attrLorenzSigma_tf.setVisible(true);
			this.attrLorenzRhoLabel.setVisible(true);
			this.attrLorenzRho_tf.setVisible(true);
			this.attrLorenzBetaLabel.setVisible(true);
			this.attrLorenzBeta_tf.setVisible(true);

			this.attrAizawaALabel.setVisible(false);
			this.attrAizawaA_tf.setVisible(false);
			this.attrAizawaBLabel.setVisible(false);
			this.attrAizawaB_tf.setVisible(false);
			this.attrAizawaCLabel.setVisible(false);
			this.attrAizawaC_tf.setVisible(false);
			this.attrAizawaDLabel.setVisible(false);
			this.attrAizawaD_tf.setVisible(false);
			this.attrAizawaELabel.setVisible(false);
			this.attrAizawaE_tf.setVisible(false);
			this.attrAizawaFLabel.setVisible(false);
			this.attrAizawaF_tf.setVisible(false);

			this.attrDeJongALabel.setVisible(false);
			this.attrDeJongA_tf.setVisible(false);
			this.attrDeJongBLabel.setVisible(false);
			this.attrDeJongB_tf.setVisible(false);
			this.attrDeJongCLabel.setVisible(false);
			this.attrDeJongC_tf.setVisible(false);
			this.attrDeJongDLabel.setVisible(false);
			this.attrDeJongD_tf.setVisible(false);

			this.attr1SeedZ_tf.setEnabled(true);
			if (!isSingular) {
				this.attr2SeedX_tf.setEnabled(true);
				this.attr2SeedY_tf.setEnabled(true);
				this.attr2SeedZ_tf.setEnabled(true);
				this.attr2ColorChooserBu.setEnabled(true);
				this.addAttrBu.setEnabled(true);
			} else {
				this.attr2SeedX_tf.setEnabled(false);
				this.attr2SeedY_tf.setEnabled(false);
				this.attr2SeedZ_tf.setEnabled(false);
				this.attr2ColorChooserBu.setEnabled(false);
				this.addAttrBu.setEnabled(false);
			}
			
			this.attractorTimeInvariantCb.setEnabled(false);
			this.attractorTimeInvariantCb.setSelected(false);
			this.isAttractorTimeInvariant = false;

			this.attractorTimeIterDependantCb.setEnabled(false);
			this.attractorTimeIterDependantCb.setSelected(false);
			this.isAttractorTimeIterDependant = false;
			
			this.attractorDimChoiceCb.setEnabled(false);
			this.attractorDimChoiceCb.setSelected(true);
			this.isAttractorDimSpace3D = true;
			
		} else if (this.attractorSelectionChoice.equals("aizawa")) {
			this.attrAizawaALabel.setVisible(true);
			this.attrAizawaA_tf.setVisible(true);
			this.attrAizawaBLabel.setVisible(true);
			this.attrAizawaB_tf.setVisible(true);
			this.attrAizawaCLabel.setVisible(true);
			this.attrAizawaC_tf.setVisible(true);
			this.attrAizawaDLabel.setVisible(true);
			this.attrAizawaD_tf.setVisible(true);
			this.attrAizawaELabel.setVisible(true);
			this.attrAizawaE_tf.setVisible(true);
			this.attrAizawaFLabel.setVisible(true);
			this.attrAizawaF_tf.setVisible(true);

			this.attrLorenzSigmaLabel.setVisible(false);
			this.attrLorenzSigma_tf.setVisible(false);
			this.attrLorenzRhoLabel.setVisible(false);
			this.attrLorenzRho_tf.setVisible(false);
			this.attrLorenzBetaLabel.setVisible(false);
			this.attrLorenzBeta_tf.setVisible(false);

			this.attrDeJongALabel.setVisible(false);
			this.attrDeJongA_tf.setVisible(false);
			this.attrDeJongBLabel.setVisible(false);
			this.attrDeJongB_tf.setVisible(false);
			this.attrDeJongCLabel.setVisible(false);
			this.attrDeJongC_tf.setVisible(false);
			this.attrDeJongDLabel.setVisible(false);
			this.attrDeJongD_tf.setVisible(false);

			this.attr1SeedZ_tf.setEnabled(true);

			if (!isSingular) {
				this.attr2SeedX_tf.setEnabled(true);
				this.attr2SeedY_tf.setEnabled(true);
				this.attr2SeedZ_tf.setEnabled(true);
				this.attr2ColorChooserBu.setEnabled(true);
				this.addAttrBu.setEnabled(true);
			} else {
				this.attr2SeedX_tf.setEnabled(false);
				this.attr2SeedY_tf.setEnabled(false);
				this.attr2SeedZ_tf.setEnabled(false);
				this.attr2ColorChooserBu.setEnabled(false);
				this.addAttrBu.setEnabled(false);
			}
			
			

			this.attractorTimeInvariantCb.setEnabled(false);
			this.attractorTimeInvariantCb.setSelected(true);
			this.isAttractorTimeInvariant = true;
			

			this.attractorTimeIterDependantCb .setEnabled(false);
			this.attractorTimeIterDependantCb .setSelected(false);
			this.isAttractorTimeIterDependant = false;
			
			this.attractorDimChoiceCb.setEnabled(false);
			this.attractorDimChoiceCb.setSelected(true);
			this.isAttractorDimSpace3D = true;
			
		} else if (this.attractorSelectionChoice.equals("dejong")) {
			this.attrDeJongALabel.setVisible(true);
			this.attrDeJongA_tf.setVisible(true);
			this.attrDeJongBLabel.setVisible(true);
			this.attrDeJongB_tf.setVisible(true);
			this.attrDeJongCLabel.setVisible(true);
			this.attrDeJongC_tf.setVisible(true);
			this.attrDeJongDLabel.setVisible(true);
			this.attrDeJongD_tf.setVisible(true);

			this.attrLorenzSigmaLabel.setVisible(false);
			this.attrLorenzSigma_tf.setVisible(false);
			this.attrLorenzRhoLabel.setVisible(false);
			this.attrLorenzRho_tf.setVisible(false);
			this.attrLorenzBetaLabel.setVisible(false);
			this.attrLorenzBeta_tf.setVisible(false);

			this.attrAizawaALabel.setVisible(false);
			this.attrAizawaA_tf.setVisible(false);
			this.attrAizawaBLabel.setVisible(false);
			this.attrAizawaB_tf.setVisible(false);
			this.attrAizawaCLabel.setVisible(false);
			this.attrAizawaC_tf.setVisible(false);
			this.attrAizawaDLabel.setVisible(false);
			this.attrAizawaD_tf.setVisible(false);
			this.attrAizawaELabel.setVisible(false);
			this.attrAizawaE_tf.setVisible(false);
			this.attrAizawaFLabel.setVisible(false);
			this.attrAizawaF_tf.setVisible(false);

			this.attr1SeedZ_tf.setEnabled(false);

			if (!isSingular) {
				this.attr2SeedX_tf.setEnabled(true);
				this.attr2SeedY_tf.setEnabled(true);
				this.attr2SeedZ_tf.setEnabled(true);
				this.attr2ColorChooserBu.setEnabled(true);
				this.addAttrBu.setEnabled(true);
			} else {
				this.attr2SeedX_tf.setEnabled(false);
				this.attr2SeedY_tf.setEnabled(false);
				this.attr2SeedZ_tf.setEnabled(false);
				this.attr2ColorChooserBu.setEnabled(false);
				this.addAttrBu.setEnabled(false);
			}

			this.attractorTimeInvariantCb.setEnabled(false);
			this.attractorTimeInvariantCb.setSelected(true);
			this.isAttractorTimeInvariant = true;
			
			this.attractorTimeIterDependantCb.setEnabled(false);
			this.attractorTimeIterDependantCb.setSelected(false);
			this.isAttractorTimeIterDependant  = false;
			
			this.attractorDimChoiceCb.setEnabled(false);
			this.attractorDimChoiceCb.setSelected(false);
			this.isAttractorDimSpace3D = false;
			
			
			
		} else if (this.attractorSelectionChoice.equals("custom")) {
			this.attrLorenzSigmaLabel.setVisible(false);
			this.attrLorenzSigma_tf.setVisible(false);
			this.attrLorenzRhoLabel.setVisible(false);
			this.attrLorenzRho_tf.setVisible(false);
			this.attrLorenzBetaLabel.setVisible(false);
			this.attrLorenzBeta_tf.setVisible(false);

			this.attrAizawaALabel.setVisible(false);
			this.attrAizawaA_tf.setVisible(false);
			this.attrAizawaBLabel.setVisible(false);
			this.attrAizawaB_tf.setVisible(false);
			this.attrAizawaCLabel.setVisible(false);
			this.attrAizawaC_tf.setVisible(false);
			this.attrAizawaDLabel.setVisible(false);
			this.attrAizawaD_tf.setVisible(false);
			this.attrAizawaELabel.setVisible(false);
			this.attrAizawaE_tf.setVisible(false);
			this.attrAizawaFLabel.setVisible(false);
			this.attrAizawaF_tf.setVisible(false);

			this.attrDeJongALabel.setVisible(false);
			this.attrDeJongA_tf.setVisible(false);
			this.attrDeJongBLabel.setVisible(false);
			this.attrDeJongB_tf.setVisible(false);
			this.attrDeJongCLabel.setVisible(false);
			this.attrDeJongC_tf.setVisible(false);
			this.attrDeJongDLabel.setVisible(false);
			this.attrDeJongD_tf.setVisible(false);
			if (!isSingular) {
				this.attr2SeedX_tf.setEnabled(true);
				this.attr2SeedY_tf.setEnabled(true);
				this.attr2SeedZ_tf.setEnabled(true);
				this.attr2ColorChooserBu.setEnabled(true);
				this.addAttrBu.setEnabled(true);
			} else {
				this.attr2SeedX_tf.setEnabled(false);
				this.attr2SeedY_tf.setEnabled(false);
				this.attr2SeedZ_tf.setEnabled(false);
				this.attr2ColorChooserBu.setEnabled(false);
				this.addAttrBu.setEnabled(false);
			}
			
		}
	}

	///////////////////////////////////////////////////////////////////////////////
	
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
			this.fieldLines = "Z-Sq";
			this.applyFatou = false;
			this.applyZSq = true;
			this.applyClassicJulia = false;
		} else if (this.juliaFieldClassicRB.isSelected() || this.diyJuliaFieldClassicRB.isSelected()) {
			this.fieldLines = "ClassicJ";
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
	
	private void doSetColorSmoothening(boolean smoothen) {
		this.smoothenColor = smoothen;
	}
	
	private void doSelectFractalColorChoiceCommand(String choice) {
		this.colorChoice = choice;
	}
	
	private void doSelectColorSampleMixStartValsComboCommand(String startVals) {
		this.colorSampleMixStartVals = startVals;
	}
		
	private void doSelectColorSampleDivValsComboCommand(String divVals) {
		this.colorSampleDivVals = divVals;
	}
	
	private void doSelectColorBlowoutChoiceComboCommand(String clrBlowoutChoice) {
		this.colorBlowoutChoice = clrBlowoutChoice;
	}
	
	private void doSelectColorSuperBlowoutChoiceComboCommand(String clrBlowoutChoice) {
		this.colorSuperBlowoutChoice = clrBlowoutChoice;
	}
	
	private void doSelectPixXTransformComboChoice(String transform){
		this.pixXTransform = transform;
	}
	
	private void doSelectPixYTransformComboChoice(String transform){
		this.pixYTransform = transform;
	}
	
	private void doSelectIntraPxOperationComboChoice(String operation){
		this.pixIntraXYOperation = operation;
	}
	
	private void doSelectPxConstOprnCommand(String option){
		this.pxConstOprnChoice = option;
	}
	
	private void doConstFunctionalSelectionChoice(String choice) {
		this.constFuncChoice = choice;
	}
	
	private void doPxFunctionalSelectionChoice(String choice) {
		this.pxFuncChoice = choice;
	}
	
	public double getRotation() {
		return this.rotation;
	}

	public void setRotation(double rot) {
		this.rotation = rot;
	}
	
	private void doSetInvertPixelsCommand(boolean invert) {
		this.invertPixelCalculation = invert;
	}
	

	
	private void doSetCaptueOrbitCommand(boolean capture) {
		this.captureOrbit = capture;

		if (capture) {
			this.orbitPointsCombo.setVisible(true);
			this.orbitPointsCombo.setEnabled(true);
			this.trapSizeLbl.setVisible(true);
			this.trapSizeCombo.setVisible(true);
			this.trapSizeCombo.setEnabled(true);
			this.trapShapeLbl.setVisible(true);
			this.trapShapeCombo.setVisible(true);
			this.trapShapeCombo.setEnabled(true);
		} else {
			this.orbitPointsCombo.setVisible(false);
			this.orbitPointsCombo.setEnabled(false);
			this.trapSizeLbl.setVisible(false);
			this.trapSizeCombo.setVisible(false);
			this.trapSizeCombo.setEnabled(false);
			this.trapShapeLbl.setVisible(false);
			this.trapShapeCombo.setVisible(false);
			this.trapShapeCombo.setEnabled(false);
		}
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

	protected boolean getMUseDiff() {
		return this.mUseDiff || this.mandUseDiffCb.isSelected();
	}

	protected boolean getJUseDiff() {
		return this.jUseDiff || this.juliaUseDiffCb.isSelected();
	}

	protected boolean getPUseDiff() {
		return this.polyUseDiff || this.polyUseDiffCb.isSelected();
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

	public void setDiyMandUseDiff(boolean diyMUseDiff) {
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
		return this.diyJuliaUseDiff/* || (this.diyJuliaUseDiffCb.isSelected() && !this.diyJuliaGen)*/;
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
		
		
/*
		
		boolean explore = (this.juliaExplore&&this.juliaExploreCb.isSelected())||
							(this.mandExplore&&this.mandExploreCb.isSelected())||
							(this.diyJuliaExplore&&this.diyJuliaExploreCb.isSelected())||
							(this.diyMandExplore&&this.diyMandExploreCb.isSelected());
		if(explore){
			
		}*/
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

	public String getPxFuncChoice() {
		return pxFuncChoice;
	}

	public void setPxFuncChoice(String fun) {
		this.pxFuncChoice = fun;
	}

	public String getPxConstOprn() {
		return this.pxConstOprnChoice;
	}

	public void setPxConstOprn(String pxCOprn) {
		this.pxConstOprnChoice = pxCOprn;
	}

	public String getPixXTransform() {
		return this.pixXTransform;
	}

	public void setPixXTransform(String transform) {
		this.pixXTransform = transform;
	}

	public String getPixYTransform() {
		return pixYTransform;
	}

	public void setPixYTransform(String pxYTransform) {
		this.pixYTransform = pxYTransform;
	}

	public String getPixIntraXYOperation() {
		return this.pixIntraXYOperation;
	}

	public void setPixIntraXYOperation(String operation) {
		this.pixIntraXYOperation = operation;
	}

	public boolean mandIsBuddhabrot() {
		return this.mandIsBuddhabrot;
	}

	public void setMandIsBuddhabrot(boolean isB) {
		this.mandIsBuddhabrot = isB;
	}

	public boolean isMandIsMotionbrot() {
		return this.mandIsMotionbrot;
	}

	public void setMandIsMotionbrot(boolean isInMotion) {
		this.mandIsMotionbrot = isInMotion;
	}

	public String getMandMotionParam() {
		return this.mandMotionParam;
	}

	public void setMandMotionParam(String moParam) {
		this.mandMotionParam = moParam;
	}
	


	public double getMandMotionParamJumpVal() {
		return this.mandMotionParamJumpVal;
	}

	public void setMandMotionParam(double moParamJumpVal) {
		this.mandMotionParamJumpVal = moParamJumpVal;
	}
	
	///////////////////tosdos////////////////////////////////
	
	//error-handling
	
	
	//regenrtion
	
	
	//files
	
	
	//colorhandlin
	///////////////////tosdos////////////////////////////////
	
	//https://www.rosettacode.org/wiki/Cartesian_product_of_two_or_more_lists#Java
	 
	public List<?> product(List<?>... a) {
        if (a.length >= 2) {
            List<?> product = a[0];
            for (int i = 1; i < a.length; i++) {
                product = prod(product, a[i]);
            }
            return product;
        }
 
        return emptyList();
    }
 
    private <A, B> List<?> prod(List<A> a, List<B> b) {
        return of(a.stream()
                .map(e1 -> of(b.stream().map(e2 -> asList(e1, e2)).collect(toList())).orElse(emptyList()))
                .flatMap(List::stream)
                .collect(toList())).orElse(emptyList());
    }
    /////////ends-rosetta-cartesian-product////////////////
    
    private List<String> stringListiFy(final double[] v) {
		List<String> stringList = new ArrayList<String>(v.length);
		for (int i = 0; i < v.length; i++) {
			stringList.add(i,String.valueOf(v[i]));
		}
		return stringList;
	}
	
	private List<String> stringListiFy(final int[] v) {
		List<String> stringList = new ArrayList<String>(v.length);
		for (int i = 0; i < v.length; i++) {
			stringList.add(i,String.valueOf(v[i]));
		}
		return stringList;
	}
	
	
	/**
     * Swaps the two specified elements in the specified array.
     */
    private static void swap(Object[] arr, int i, int j) {
        Object tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

@SuppressWarnings({"rawtypes", "unchecked"})
	public static Properties[] shuffle(Properties[] props) {
		int size = props.length;
		Random rnd = new Random();
		// Object arr[] = props;//list.toArray();

		// Shuffle array
		for (int i = size; i > 1; i--) {
			swap(props, i - 1, rnd.nextInt(i));
		}

		return props;
/*
		// Dump array back into list
		// instead of using a raw type here, it's possible to capture
		// the wildcard but it will require a call to a supplementary
		// private method
		ListIterator it = list.listIterator();
		for (int i = 0; i < arr.length; i++) {
			it.next();
			it.set(arr[i]);
		}*/
        
    }
	
}




public class SierpinskiCompositional extends JFrame {

	private static final long serialVersionUID = 17584L;
	private SierpinskiComboPanel topPanel = new SierpinskiComboPanel();
//	private JScrollPane jsp1 = new JScrollPane(this.topPanel);

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
//		this.jsp1.add(this.topPanel);
		cp.add(this.topPanel);//(this.jsp1);// 

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
