/**
 * 
 */
package org.bawaweb.ui.sierpinkski;


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
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

/**
 * @author Navroz
 *
 */

class SierpinskiComboPanel extends JPanel {
	
	private static final Integer[] EXPONENTS = new Integer[] { -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 9, 10 };
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
	private static final String SIERPINSKI_SQUARES = "SierpinskiSquares";
	private static final String APOLLONIAN_CIRCLES = "ApollonianCircles";
	private static final String SIERPINSKI_TRIANGLES = "SierpinskiTriangles";
	private static final String CST_FRACTAL = "CSTFractal";
	private static final String SAMPLE = "Sample";
	private static final String MANDELBROT = "Mandelbrot";
	private static final String JULIA = "Julia";
	private static final String FANNY_TRIANGLES = "FannyTriangles";
	private static final String FANNY_CIRCLE = "FannyCircle";
	private static final String KOCHSNOWFLAKE = "KochSnowFlake";
	private static final String DIY	="Do_It_Yourself";

	private static final long serialVersionUID = 156478L;
	
	private final String[] comboOptions = new String[]{DIY,FANNY_CIRCLE,FANNY_TRIANGLES,SIERPINSKI_TRIANGLES,SIERPINSKI_SQUARES,APOLLONIAN_CIRCLES,CST_FRACTAL,SAMPLE,MANDELBROT,JULIA,KOCHSNOWFLAKE};
	private final JComboBox<String> combos = new JComboBox<String>(comboOptions);
	
	//	for	FannyCircle & FannyTriangles
	private final Integer[] sideOptions = new Integer[] { 50, 60, 70, 80, 90, 100, 120, 150, 170, 200, 250, 300, 350 };
	private final JComboBox<Integer> sideCombos = new JComboBox<Integer>(sideOptions);
	private final Integer[] ratioOptions = new Integer[] { 1, 2, 3, 4, 5, 6, 7 };
	private final JComboBox<Integer> ratioCombos = new JComboBox<Integer>(ratioOptions);
	
	// for Julia
	private final String[] juliaOptions = new String[] { J1, J2, J3, J4, J5, J6, J7, J8, J9 };
	private final JComboBox<String> juliaCombos = new JComboBox<String>(juliaOptions);
	private final JCheckBox juliaUseDiff = new JCheckBox("UseDifferencesOnly", true);
	private final Integer[] juliaMaxIterOptions = new Integer[] { 10, 50, 100, 200, 225, 255, 500, 1000 };
	private final JComboBox<Integer> juliaMaxIterCombos = new JComboBox<Integer>(juliaMaxIterOptions);
	private final Integer[] juliaSizeOptions = new Integer[] { 10, 50, 100, 200, 225, 255, 500, 512, 599, 800 };
	private final JComboBox<Integer> juliaSizeCombos = new JComboBox<Integer>(juliaSizeOptions);
	private final Double[] juliaBoundOptions = new Double[] { 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private final JComboBox<Double> juliaBoundCombos = new JComboBox<Double>(juliaBoundOptions);
	
	private final Double[] juliaXCOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
	private final JComboBox<Double> juliaXCCombos = new JComboBox<Double>(juliaXCOptions);
	private final Double[] juliaYCOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
	private final JComboBox<Double> juliaYCCombos = new JComboBox<Double>(juliaYCOptions);
	private final Double[] juliaScaleSizeOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private final JComboBox<Double> juliaScaleSizeCombos = new JComboBox<Double>(juliaScaleSizeOptions);
	
	protected JRadioButton juliaColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton juliaColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup juliaColrBg = new ButtonGroup();
	

	// for Mandelbrot
	private final Integer[] mandOptions = new Integer[] { -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private final JComboBox<Integer> mandCombos = new JComboBox<Integer>(mandOptions);
	private final Integer[] mandExpOptions = EXPONENTS;
	private final JComboBox<Integer> mandExpCombos = new JComboBox<Integer>(mandExpOptions);
	private final JCheckBox mandUseDiffCb = new JCheckBox("UseDifferencesOnly", true);
	private final Integer[] mandMaxIterOptions = new Integer[] { 10, 50, 100, 200, 225, 255, 300, 350, 400, 500, 1000 };
	private final JComboBox<Integer> mandMaxIterCombos = new JComboBox<Integer>(mandMaxIterOptions);
	private final Integer[] mandSizeOptions = new Integer[] { 10, 50, 100, 200, 225, 255, 500, 512, 599, 800 };
	private final JComboBox<Integer> mandSizeCombos = new JComboBox<Integer>(mandSizeOptions);
	private final Double[] mandBoundOptions = new Double[] { 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private final JComboBox<Double> mandBoundCombos = new JComboBox<Double>(mandBoundOptions);


	private final Double[] mandXCOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
	private final JComboBox<Double> mandXCCombos = new JComboBox<Double>(mandXCOptions);
	private final Double[] mandYCOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
	private final JComboBox<Double> mandYCCombos = new JComboBox<Double>(mandYCOptions);

	private final Double[] mandScaleSizeOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private final JComboBox<Double> mandScaleSizeCombos = new JComboBox<Double>(mandScaleSizeOptions);
	

	protected JRadioButton mandColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton mandColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup mandColrBg = new ButtonGroup();
	
	//	for	Apollo
	private final String[] curvOptions = new String[] { A1, A2, A3, A4, A5, A6 };
	private final JComboBox<String> curvCombos = new JComboBox<String>(curvOptions);

	protected JRadioButton apolloColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton apolloColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup apolloColrBg = new ButtonGroup();

	// Fractal Art Options
	private JPanel fannyOptionsPanel = new JPanel(new FlowLayout(),true);
	private JPanel apolloOptionsPanel = new JPanel(new FlowLayout(),true);
	private JPanel juliaOptionsPanel = new JPanel(new GridLayout(/*4,8*/10,5),true);
	private JPanel mandOptionsPanel = new JPanel(new GridLayout(10,5),true);
	private JPanel diyOptionsPanel	= new JPanel(new FlowLayout(),true);//GridLayout(4,7),true);
	
	private JPanel diyMandPanel = new JPanel(new GridLayout(5,8),true);
	private JPanel diyJuliaPanel = new JPanel(new GridLayout(5,8),true);
	private JPanel diyApolloPanel = new JPanel(new GridLayout(4,8),true);
	
	private JTextArea formulaArea = new JTextArea(5,20);
	//private JTextPane formulaPane;
	
	private final JButton buStart = new JButton("Start |>");
	private final JButton buPause = new JButton("Pause ||");
	
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
	private String diyMand = "DIY_"+MANDELBROT;
	private String diyJulia = "DIY_"+JULIA;
	private String diyApollo = "DIY_"+APOLLONIAN_CIRCLES;

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
	
	private final Integer[] diyMandMagOptions = new Integer[] { -3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	private final JComboBox<Integer> diyMandMagCombos = new JComboBox<Integer>(diyMandMagOptions);
	private final Integer[] diyMandExpOptions = new Integer[] { -3, -2, -1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	private final JComboBox<Integer> diyMandExpCombos = new JComboBox<Integer>(diyMandExpOptions);
	private final JCheckBox diyMandUseDiffCb = new JCheckBox("UseDifferencesOnly",true);
	private JTextField diyMandRealTf = new JTextField(5);
	private JTextField diyMandImgTf = new JTextField(5);
	private final JCheckBox diyMandKeepConstantCb = new JCheckBox("DynamicConstant",false);
	private final Integer[] diyMandMaxIterOptions = new Integer[] { 10, 50, 100, 200, 225, 255, 300, 350, 400, 500, 1000 };
	private final JComboBox<Integer> diyMandMaxIterCombos = new JComboBox<Integer>(diyMandMaxIterOptions);
	
	private final Integer[] diyMandSizeOptions = new Integer[] { 10, 50, 100, 200, 225, 255, 500, 512, 599, 800 };
	private final JComboBox<Integer> diyMandSizeCombos = new JComboBox<Integer>(diyMandSizeOptions);
	
	private final Double[] diyMandBoundOptions = new Double[] { 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private final JComboBox<Double> diyMandBoundCombos = new JComboBox<Double>(diyMandBoundOptions);

	
	private final Double[] diyMandXCOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
	private final JComboBox<Double> diyMandXCCombos = new JComboBox<Double>(diyMandXCOptions);
	private final Double[] diyMandYCOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
	private final JComboBox<Double> diyMandYCCombos = new JComboBox<Double>(diyMandYCOptions);

	private final Double[] diyMandScaleSizeOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private final JComboBox<Double> diyMandScaleSizeCombos = new JComboBox<Double>(diyMandScaleSizeOptions);
	
	protected JRadioButton diyMandColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton diyMandColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup diyMandColrBg = new ButtonGroup();
	
	//diy Julia options
	protected int diyJuliaPower;
	protected double diyJuliaConstReal;
	protected double diyJuliaConstImg;
	protected boolean diyJuliaUseDiff;
	protected boolean diyJuliaKeepConst;
	protected int diyJuliaMaxIter;
	protected double diyJuliaBound;

	protected int diyJuliaSize;
	
	protected double diyJuliaXC;
	protected double diyJuliaYC;
	protected double diyJuliaScaleSize;
	
	private final Integer[] diyJuliaPowerOptions = new Integer[] { -3, -2, -1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	private final JComboBox<Integer> diyJuliaPowerCombos = new JComboBox<Integer>(diyJuliaPowerOptions);
	private final JCheckBox diyJuliaUseDiffCb = new JCheckBox("UseDifferencesOnly",true);
	private JTextField diyJuliaRealTf = new JTextField(5);
	private JTextField diyJuliaImgTf = new JTextField(5);
	private final JCheckBox diyJuliaKeepConstantCb = new JCheckBox("DynamicConstant",false);
	
	private final Integer[] diyJuliaMaxIterOptions = new Integer[] { 10, 50, 100, 200, 225, 255, 300, 350, 400, 500, 1000 };
	private final JComboBox<Integer> diyJuliaMaxIterCombos = new JComboBox<Integer>(diyJuliaMaxIterOptions);
	

	private final Integer[] diyJuliaSizeOptions = new Integer[] { 10, 50, 100, 200, 225, 255, 500, 512, 599, 800 };
	private final JComboBox<Integer> diyJuliaSizeCombos = new JComboBox<Integer>(diyJuliaSizeOptions);
	
	
	private final Double[] diyJuliaBoundOptions = new Double[] { 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private final JComboBox<Double> diyJuliaBoundCombos = new JComboBox<Double>(diyJuliaBoundOptions);

	private final Double[] diyJuliaXCOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
	private final JComboBox<Double> diyJuliaXCCombos = new JComboBox<Double>(diyJuliaXCOptions);
	private final Double[] diyJuliaYCOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5 };
	private final JComboBox<Double> diyJuliaYCCombos = new JComboBox<Double>(diyJuliaYCOptions);

	private final Double[] diyJuliaScaleSizeOptions = new Double[] { -2.0, -1.5, -1.0, -0.5, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0 };
	private final JComboBox<Double> diyJuliaScaleSizeCombos = new JComboBox<Double>(diyJuliaScaleSizeOptions);
	
	protected JRadioButton diyJuliaColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton diyJuliaColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup diyJuliaColrBg = new ButtonGroup();
	
	//diy Apollo Options
	protected int diyApolloC1;
	protected int diyApolloC2;
	protected int diyApolloC3;
	protected int diyApolloMult;
	

	private final Integer[] diyApolloC1Options = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
	private final JComboBox<Integer> diyApolloC1Combos = new JComboBox<Integer>(diyApolloC1Options);
	private final Integer[] diyApolloC2Options = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
	private final JComboBox<Integer> diyApolloC2Combos = new JComboBox<Integer>(diyApolloC1Options);	
	private final Integer[] diyApolloC3Options = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30};
	private final JComboBox<Integer> diyApolloC3Combos = new JComboBox<Integer>(diyApolloC1Options);
	
	private final Integer[] diyApolloMultOptions = new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
	private final JComboBox<Integer> diyApolloMultCombos = new JComboBox<Integer>(diyApolloMultOptions);
	
	protected JRadioButton diyApolloColrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton diyApolloColrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup diyApolloColrBg = new ButtonGroup();
	
	
	
	//controlPanel
	private JPanel controlPanel = new JPanel();
	private final JComboBox<Integer> expCombos = new JComboBox<Integer>(EXPONENTS);
	private final JCheckBox useDiff = new JCheckBox("UseDifferencesOnly", true);
	
	@SuppressWarnings("unused")
	private boolean useColorPalette = false;
	
	private double rotation = 0.0;
	
	private JLabel rotLabel = new JLabel("Rotation:");
	private Vector<Double> rotOptions = new Vector<Double>();
	protected JComboBox<Double> rotateCombo ;//= new JComboBox<Double>(rotOptions);
	
	protected JRadioButton colrPRb = new JRadioButton("Use Color Palette",false);
	protected JRadioButton colrCRb = new JRadioButton("Compute Color",true);
	
	protected ButtonGroup colrBg = new ButtonGroup();
	
	private final JCheckBox magnifyCb = new JCheckBox("Magnify",false);
	private boolean doMagnify = false;
	
	private BufferedImage fractalImage;

	private JButton buPrint = new JButton("Print");
	private JButton buSave = new JButton("Save");
	
	private Thread fbf;
	
	
	
	public SierpinskiComboPanel() {
		super();
		this.add(new JLabel("Choose: "));//FractalArt:"));
		this.add(this.combos);
		/*// creates-color-choice-options	-- does not add 
		this.createColorChoiceRBs();
		// creates-rototation-choice-options	-- does not add
		this.createRotationCombo();
*/
		

		this.createColorChoiceRB();
		this.createRotationCombo();
		this.createPanels();
		this.createActionControls();		
		
		
		
		this.setUpListeners();
		
	}

	private void createActionControls() {

		/*//	fanny
		this.createFannyPanel();
		
		//apollo
		this.createApolloPanel();
		
		//julia
		this.createJuliaPanel();
		
		//mandelbrot
		this.createMandelbrotPanel();
		
		
		//	diy	panel
		this.createDIYPanel();*/
		
		this.add(this.rotLabel);
		this.add(this.rotateCombo);
		this.rotLabel.setVisible(false);
		this.rotateCombo.setVisible(false);

		this.buStart.setEnabled(false);
		this.add(this.buStart);
		// this.add(this.buPause);
		this.add(this.buPrint);
		this.add(this.buSave);

		this.formulaArea.setVisible(false);
		this.add(this.formulaArea);

	}

	private void createPanels() {
		//	fanny	--	does add
		this.createFannyPanel();
		
		//apollo	--	does add
		this.createApolloPanel();
		
		//julia		--	does add
		this.createJuliaPanel();
		
		//mandelbrot	--	does add
		this.createMandelbrotPanel();
		
		
		//	diy	panel	--	does add
		this.createDIYPanel();
	}

	private void createColorChoiceRBs() {
		this.colrBg.add(this.colrPRb);
		this.colrBg.add(this.colrCRb);
		
		this.colrPRb.setActionCommand("ColorPalette");
		this.colrCRb.setActionCommand("ComputeColor");

		this.colrPRb.setName("ColorPalette");
		this.colrCRb.setName("ComputeColor");
	}

	private void createColorChoiceRB() {
		this.colrBg.add(this.colrPRb);
		this.colrBg.add(this.colrCRb);
		
		this.colrPRb.setActionCommand("ColorPalette");
		this.colrCRb.setActionCommand("ComputeColor");

		this.colrPRb.setName("ColorPalette");
		this.colrCRb.setName("ComputeColor");
	}

	private void createRotationCombo() {
		for (int i = 0; i < 1000; i+=15) {
			this.rotOptions.add((double) i);
		}
		
		/*this.add(this.rotLabel);
		this.rotLabel.setVisible(false);*/
		
		this.rotateCombo=new JComboBox<Double>(this.rotOptions);
		/*this.add(this.rotateCombo);*/
		this.rotateCombo.setVisible(false);
	}

	private void createDIYPanel() {
		/*this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);*/
//		this.diyOptionsPanel.add(new JLabel("Choose Fractal Art:"));
		this.setupDIYRBs();		
		
		this.createDiyMandelbrotPanel();
		
		createDiyJuliaPanel();
		this.createDiyJuliaPanel();
		
		this.createDiyApolloPanel();
		
		
		this.diyOptionsPanel.add(this.diyMandPanel);
		this.diyOptionsPanel.add(this.diyJuliaPanel);
		this.diyOptionsPanel.add(this.diyApolloPanel);
		this.diyOptionsPanel.setVisible(false);

		/*this.colrBg.add(this.colrPRb);
		this.colrBg.add(this.colrCRb);
		
		this.colrPRb.setActionCommand("ColorPalette");
		this.colrCRb.setActionCommand("ComputeColor");

		this.colrPRb.setName("ColorPalette");
		this.colrCRb.setName("ComputeColor");
		this.diyOptionsPanel.add(this.colrPRb);
		this.diyOptionsPanel.add(this.colrCRb);*/
		
		/*this.diyOptionsPanel.add(new JLabel("Rotate:"));
		this.diyOptionsPanel.add(this.rotateCombo);*/
		
		/*if (!this.diyApolloRb.isSelected()) {
			for (int i = 0; i < 1000; i += 15) {
				this.rotOptions.add((double) i);
			}
			this.diyOptionsPanel.add(this.rotLabel);
			this.rotLabel.setVisible(false);
			this.rotateCombo = new JComboBox<Double>(this.rotOptions);
			this.rotateCombo.setVisible(false);
			this.diyOptionsPanel.add(this.rotateCombo);
		}*/
		this.add(diyOptionsPanel);
	}

	private void setupDIYRBs() {
		this.diyMandRb.setActionCommand("DIY_"+MANDELBROT);
		this.diyJuliaRb.setActionCommand("DIY_"+JULIA);
		this.diyApolloRb.setActionCommand("DIY_"+APOLLONIAN_CIRCLES);
		
		this.diyBg.add(this.diyMandRb);
		this.diyBg.add(this.diyJuliaRb);
		this.diyBg.add(this.diyApolloRb);
		
		this.diyMandRb.setSelected(true);
		this.diyMandRb.setName("DIY_"+MANDELBROT);
		this.diyJuliaRb.setName("DIY_"+JULIA);
		this.diyApolloRb.setName("DIY_"+APOLLONIAN_CIRCLES);
		
		this.diyOptionsPanel.add(this.diyMandRb);
		this.diyOptionsPanel.add(this.diyJuliaRb);
		this.diyOptionsPanel.add(this.diyApolloRb);
	}

	private void createDiyApolloPanel() {
		this.rotLabel.setVisible(false);
		this.rotateCombo.setVisible(false);
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
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.diyJuliaPanel.add(new JLabel("Power:"));
		this.diyJuliaPanel.add(this.diyJuliaPowerCombos);
		this.diyJuliaPanel.add(new JLabel("ComplexConstant - Real (R) "));
		this.diyJuliaPanel.add(this.diyJuliaRealTf);
		this.diyJuliaPanel.add(new JLabel("Imaginary (*i)"));
		this.diyJuliaPanel.add(this.diyJuliaImgTf);
		this.diyJuliaPanel.add(this.diyJuliaUseDiffCb);
		this.diyJuliaPanel.add(this.diyJuliaKeepConstantCb);
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
		
		/*this.diyJuliaColrPRb.setVisible(true);
		this.diyJuliaColrCRb.setVisible(true);		
		this.diyJuliaPanel.add(this.diyJuliaColrPRb);
		this.diyJuliaPanel.add(this.diyJuliaColrCRb);
		
		/*this.diyJuliaPanel.add(new JLabel("Rotate:"));
		this.diyJuliaPanel.add(this.rotateCombo);*/
		
		this.diyJuliaPanel.setVisible(false);
	}

	private void createDiyMandelbrotPanel() {
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.diyMandPanel.add(new JLabel("Magnification:"));
		this.diyMandPanel.add(this.diyMandMagCombos);
		this.diyMandPanel.add(new JLabel("Exponent:"));
		this.diyMandPanel.add(this.diyMandExpCombos);
		this.diyMandPanel.add(new JLabel("ComplexConstant - Real (R) "));
		this.diyMandPanel.add(this.diyMandRealTf);
		this.diyMandPanel.add(new JLabel("Imaginary (*i)"));
		this.diyMandPanel.add(this.diyMandImgTf);
		this.diyMandPanel.add(this.diyMandUseDiffCb);
		this.diyMandPanel.add(this.diyMandKeepConstantCb);
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
		
		/*this.diyMandColrPRb.setVisible(true);
		this.diyMandColrCRb.setVisible(true);
		this.diyMandPanel.add(this.diyMandColrPRb);
		this.diyMandPanel.add(this.diyMandColrCRb);
		
		/*this.diyMandPanel.add(new JLabel("Rotate:"));
		this.diyMandPanel.add(this.rotateCombo);*/
		
		this.diyMandPanel.setVisible(false);
	}

	private void createMandelbrotPanel() {
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
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
		
/*
		this.mandColrBg.add(this.mandColrPRb);
		this.mandColrBg.add(this.mandColrCRb);
		
		this.mandColrPRb.setActionCommand("ColorPalette");
		this.mandColrCRb.setActionCommand("ComputeColor");

		this.mandColrPRb.setName("ColorPalette");
		this.mandColrCRb.setName("ComputeColor");
		
		this.mandOptionsPanel.add(this.mandColrPRb);
		this.mandOptionsPanel.add(this.mandColrCRb);

		this.mandColrPRb.setVisible(true);
		this.mandColrCRb.setVisible(true);
		this.mandOptionsPanel.add(this.mandColrPRb);
		this.mandOptionsPanel.add(this.mandColrCRb);
		
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);*/
		
		
		/*
		this.juliaOptionsPanel.add(new JLabel("Rotate:"));*/
		/*this.mandOptionsPanel.add(this.rotLabel);
		this.mandOptionsPanel.add(this.rotateCombo);*/
		

		
		/*this.mandOptionsPanel.add(new JLabel("Rotate:"));
		this.mandOptionsPanel.add(this.rotateCombo);*/
		
		this.mandOptionsPanel.setVisible(false);
		this.add(this.mandOptionsPanel);
	}

	private void createJuliaPanel() {
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.juliaOptionsPanel.add(new JLabel("Power-Constant:"));
		this.juliaOptionsPanel.add(this.juliaCombos);		
		this.juliaOptionsPanel.add(this.juliaUseDiff);
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
		
		/*this.juliaColrBg.add(this.juliaColrPRb);
		this.juliaColrBg.add(this.juliaColrCRb);
		
		this.juliaColrPRb.setActionCommand("ColorPalette");
		this.juliaColrCRb.setActionCommand("ComputeColor");

		this.juliaColrPRb.setName("ColorPalette");
		this.juliaColrCRb.setName("ComputeColor");
		
		this.juliaColrPRb.setVisible(true);
		this.juliaColrCRb.setVisible(true);
		this.juliaOptionsPanel.add(this.juliaColrPRb);
		this.juliaOptionsPanel.add(this.juliaColrCRb);
		
		this.rotLabel.setVisible(true);
		this.rotateCombo.setVisible(true);
		this.juliaOptionsPanel.add(this.rotLabel);
		this.juliaOptionsPanel.add(this.rotateCombo);*/
		
		
		this.juliaOptionsPanel.setVisible(false);		
		this.add(this.juliaOptionsPanel);
	}

	private void createApolloPanel() {
		this.rotLabel.setVisible(false);
		if (this.rotateCombo!=null) {
			this.rotateCombo.setVisible(false);
		}
		this.apolloOptionsPanel.add(new JLabel("CurvatureOptions:"));
		this.apolloOptionsPanel.add(this.curvCombos);
		
		this.apolloColrBg.add(this.apolloColrPRb);
		this.apolloColrBg.add(this.apolloColrCRb);
		
		this.apolloColrPRb.setActionCommand("ColorPalette");
		this.apolloColrCRb.setActionCommand("ComputeColor");

		this.apolloColrPRb.setName("ColorPalette");
		this.apolloColrCRb.setName("ComputeColor");
		
		this.apolloColrPRb.setVisible(true);
		this.apolloColrCRb.setVisible(true);
		this.apolloOptionsPanel.add(this.apolloColrPRb);
		this.apolloOptionsPanel.add(this.apolloColrCRb);
		this.apolloOptionsPanel.setVisible(false);
		this.add(this.apolloOptionsPanel);
	}

	private void createFannyPanel() {
		this.rotLabel.setVisible(false);
		if (this.rotateCombo!=null) {
			this.rotateCombo.setVisible(false);
		}
		this.fannyOptionsPanel.add(new JLabel("Dimension Size:"));
		this.fannyOptionsPanel.add(this.sideCombos);

		this.fannyOptionsPanel.add(new JLabel("Ratio:"));
		this.fannyOptionsPanel.add(this.ratioCombos);
		
		this.fannyOptionsPanel.setVisible(false);
		this.add(this.fannyOptionsPanel);
	}
	
	/*private void createControlPanel() {
		this.mandOptionsPanel.add(new JLabel("Exponent(X):"));
		this.mandOptionsPanel.add(this.expCombos);
		this.controlPanel.add(this.useDiff);
	}
*/	
	private void doReset() {
		this.sideChoice = 0;
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
	
	private void doSelectCombosCommand(String option) {
//		System.out.println("in doSelectCombosCommand == option is "+option);
		this.comboChoice = option;

		if (this.comboChoice.equals(APOLLONIAN_CIRCLES)) {
			this.rotLabel.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.fannyOptionsPanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(true);
			this.diyOptionsPanel.setVisible(false);
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
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
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
			this.juliaOptionsPanel.setVisible(true);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
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
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(true);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
		}  else if (this.comboChoice.equals(DIY)) {
			this.rotLabel.setVisible(true);
			if (!this.diyApolloRb.isSelected()) {
				this.rotLabel.setVisible(true);
				this.rotateCombo.setVisible(true);
			}else{
				this.rotLabel.setVisible(false);
				this.rotateCombo.setVisible(false);
			}
			this.fannyOptionsPanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			
			this.diyOptionsPanel.setVisible(true);
			this.diyMandPanel.setVisible(true);
			/*if(this.diyMandRb.isSelected()){
				this.diyMandPanel.setVisible(true);
			}else if(this.diyJuliaRb.isSelected()){
				this.diyJuliaPanel.setVisible(true);
			}else if(this.diyApolloRb.isSelected()){
				this.diyApolloPanel.setVisible(true);
			}*/
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
		} else {
			this.rotLabel.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.fannyOptionsPanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(false);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
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
			
			this.formulaArea.setText("Julia Set:\n\nf(z) = z ^ " + this.power + " + " + this.compConst);
			
		} else {
			if(this.juliaSelection.equals("J7")){
				this.formulaArea.setText("Julia Set:\n\nf(z) = z ^ " + this.power + " + (-0.74543 + 0.11301 * i)");
			}else if(this.juliaSelection.equals("J8")){
				this.formulaArea.setText("Julia Set:\n\nf(z) = z ^ " + this.power + " + (-0.75 + 0.11 * i)");
			}else if(this.juliaSelection.equals("J9")){
				this.formulaArea.setText("Julia Set:\n\nf(z) = z ^ " + this.power + " + (-0.1 + 0.651 * i)");
			}
			
		}
	}

	private void addJuliaUseDiffInfo() {
		if (this.jUseDiff || this.juliaUseDiff.isSelected()) {
			this.formulaArea
					.append("\n\nCalculated inverted colors based on differences in pixel values from origin");
		} else {
			this.formulaArea.append("\n\nCalculated colors based on pixel values with a 'top-and-left' origin");
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
				this.power = 2;
				this.complex = "C1";
				this.juliaSelection="J7";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;	
			case J8: //  J8 = "P[2] C2";//[-0.75+0.11*i]";
				this.power = 2;
				this.complex = "C2";
				this.juliaSelection="J8";
				this.addJuliaFormulaInfo();
				this.addJuliaUseDiffInfo();
				break;
			case J9: //  J9 = "P[2] C3";//[-0.1+0.651*i]";
				this.power = 2;
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
			this.formulaArea.setText("Mandelbrot Set:\n\nf(z) = z ^ " + this.exponent + " + C");
			if (this.mUseDiff || this.mandUseDiffCb.isSelected()) {
				this.formulaArea
						.append("\n\nCalculated inverted colors based on differences in pixel values from origin");
			} else {
				this.formulaArea.append("\n\nCalculated colors based on pixel values with a 'top-and-left' origin");
			}
		} else {
			this.buStart.setEnabled(false);
		}
	}
	
	private void doDiyMandelbrotChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.diyMandMagnification != 0 && this.diyMandExponent != 0
				&& (((this.diyMandRealTf.getText().length() > 0) && (this.diyMandImgTf.getText().length() > 0))
						&& !this.diyMandKeepConst)
				|| this.diyMandKeepConst) {
			
			this.buStart.setEnabled(true);
			String formula = "Mandelbrot Set:\n\nf(z) = z ^ " + this.diyMandExponent + " + C";
			if (!this.diyMandKeepConst) {
				formula += "\n  C = " + Double.parseDouble(this.diyMandRealTf.getText()) + " + ("
						+ Double.parseDouble(this.diyMandImgTf.getText()) + ")";
			}
			if (this.mUseDiff || this.mandUseDiffCb.isSelected()) {
				formula += "\n\nCalculated inverted colors based on differences in pixel values from origin";
			} else {
				formula += "\n\nCalculated colors based on pixel values with a 'top-and-left' origin";
			}
			this.formulaArea.setText(formula);
		} else {
			this.buStart.setEnabled(false);
		}
	}
	
	private void doDiyJuliaChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.diyJuliaPower != 0
				&& (!this.diyJuliaKeepConst && (this.diyJuliaRealTf.getText().length() > 0)
						&& (this.diyJuliaImgTf.getText().length() > 0) && !this.diyJuliaKeepConst)
				|| this.diyJuliaKeepConst) {
			this.buStart.setEnabled(true);
			String formula = "Julia Set:\n\nf(z) = z ^ " + this.diyJuliaPower + " + C";
			if (!this.diyJuliaKeepConst) {
				formula += "\n  C = " + Double.parseDouble(this.diyJuliaRealTf.getText()) + " + ("
						+ Double.parseDouble(this.diyJuliaImgTf.getText()) + ")";
			}
			if (this.diyJuliaUseDiff || this.diyJuliaUseDiffCb.isSelected()) {
				formula += "\n\nCalculated inverted colors based on differences in pixel values from origin";
			} else {
				formula += "\n\nCalculated colors based on pixel values with a 'top-and-left' origin";
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

	private void doSetDiyMandKeepConstantCommand(boolean useConst) {
		this.diyMandKeepConst = useConst;
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
	
	private void doSetDiyJuliaKeepConstantCommand(boolean useConst) {
		this.diyJuliaKeepConst = useConst;
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
		if (diyChoice.equals("DIY_" + MANDELBROT) && this.diyMandRb.isSelected()) {
			this.comboChoice="DIY_" + MANDELBROT;
			this.diyMandPanel.setVisible(true);
			this.diyJuliaPanel.setVisible(false);
			this.diyApolloPanel.setVisible(false);
		} else if (diyChoice.equals("DIY_" + JULIA) && this.diyJuliaRb.isSelected()) {
			this.diyMandPanel.setVisible(false);
			this.diyJuliaPanel.setVisible(true);
			this.diyApolloPanel.setVisible(false);
		} else if (diyChoice.equals("DIY_" + APOLLONIAN_CIRCLES) && this.diyApolloRb.isSelected()) {
			this.rotLabel.setVisible(false);
			this.rotateCombo.setVisible(false);
			this.diyMandPanel.setVisible(false);
			this.diyJuliaPanel.setVisible(false);
			this.diyApolloPanel.setVisible(true);
		}
	}
	
	private void doSetRotationCombosCommand(Double rot){
		this.setRotation(rot);
	}
	
	private void doStartCommand() {
		this.formulaArea.setText("");
		// fractal art choice
		String choice = this.getComboChoice();
		// for Fanny
		int length = this.getSideComboChoice();
		int ratio = this.getRatioChoice();
		// for Apollo
		double[] cChoices = this.getCurvChoice();
		double mXt = this.getMultiplier();
		// for Julia
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

		// for Mandelbrot
		int mag = this.getMagnification();
		int exp = this.getExponent();
		boolean mUseD = this.getMUseDiff();
		int mandMax = this.mandMaxIter;
		int mandLoopLt = this.mandSize;
		double mBound=this.mandBound;
		double mXc = this.mandXC;
		double mYc = this.mandYC;
		double mScale = this.mandScaleSize;
		
		boolean useCP = this.juliaColrPRb.isSelected() || 
						this.mandColrPRb.isSelected() || this.colrPRb.isSelected()||
						this.diyJuliaColrPRb.isSelected()||this.diyMandColrPRb.isSelected();
		
		double rot = this.getRotation();
		
		FractalBase ff = null;;
		
		if (choice.equals(FANNY_CIRCLE)) {
			this.doReset();
			ff = new FannyCircle(length, ratio);
		} else if (choice.equals(FANNY_TRIANGLES)) {
			this.doReset();
			ff = new FannyTriangles(length, ratio);
		} else if (choice.equals(SIERPINSKI_TRIANGLES)) {
			this.doReset();
			ff = new SierpinskiTriangle();
		} else if (choice.equals(SIERPINSKI_SQUARES)) {
			this.doReset();
			ff = new SierpinskiSquare();
		} else if (choice.equals(APOLLONIAN_CIRCLES)) {
			this.doReset();
			ff = new ApollonianCircles(cChoices, mXt);
			ff.setUseColorPalette(useCP);
		} else if (choice.equals(SAMPLE)) {
			this.doReset();
			ff = new FractalBaseSample();
		} else if (choice.equals(MANDELBROT)) {
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
			this.formulaArea.setText("Mandelbrot Set:\n\nf(z) = z ^ " + exp + " + C");
			if (mUseD) {
				this.formulaArea.append("\n\nCalculated based on differences in pixel values from origin");
			} else {
				this.formulaArea.append("\n\nCalculated based on pixel values with a top-left origin");
			}
			ff = new Mandelbrot(mag, exp, mUseD, mBound, true);
			ff.setUseColorPalette(useCP);
			ff.setRotation(rot);
			FractalBase.setxC(mXc);
			FractalBase.setxC(mYc);
			FractalBase.setScaleSize(mScale);
			FractalBase.setMaxIter(mandMax);
			FractalBase.setAreaSize(mandLoopLt);
			this.doReset();
		} else if (choice.equals(JULIA)) {
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

			ff.setUseColorPalette(useCP);
			ff.setRotation(rot);
			FractalBase.setxC(jXc);
			FractalBase.setxC(jYc);
			FractalBase.setScaleSize(jScale);
			FractalBase.setMaxIter(juliaMax);
			FractalBase.setAreaSize(juliaLoopLt);
			this.doReset();

		} else if (choice.equals(CST_FRACTAL)) {
			this.doReset();
			ff = new CSTFractal();
		} else if (choice.equals(SAMPLE)) {
			this.doReset();
			ff = new FractalBaseSample();
		} else if (choice.equals(KOCHSNOWFLAKE)) {
			this.doReset();
			ff = new KochSnowFlakeFractal();
		} else if (choice.equals(DIY)) {
//			this.doReset();
//			System.out.println("A---this.diyMandRb.isSelected()==="+this.diyMandRb.isSelected());
//			System.out.println("B---this.diyJuliaRb.isSelected()==="+this.diyJuliaRb.isSelected());
//			System.out.println("C---this.diyApolloRb.isSelected()==="+this.diyApolloRb.isSelected());
			if (this.diyMandRb.isSelected()) {//System.out.println("hererererere---this.diyMandRb.isSelected()");
				// for diy mandelbrot
				int diyMag = this.getDiyMandMagnification();
				int diyMandExp = this.getDiyMandExponent();
				boolean diyMandUseD = this.getDiyMandUseDiff();
				boolean diyMKConst = this.diyMandKeepConst;
				int diyMaxIt = this.diyMandMaxIter;
				double diyMandB = this.diyMandBound;
				double diyMXc = this.diyMandXC;
				double diyMYc = this.diyMandYC;
				double diyMScale = this.diyMandScaleSize;
			
				if (diyMKConst) {
					ff = new Mandelbrot(diyMag, diyMandExp, diyMandUseD,diyMandB, diyMKConst);
				} else {

					double diyMRealVal = Double.parseDouble(this.diyMandRealTf.getText());
					double diyMImgVal = Double.parseDouble(this.diyMandImgTf.getText());
					ff = new Mandelbrot(diyMag, diyMandExp, diyMandUseD,diyMandB, diyMRealVal, diyMImgVal);
				}

				ff.setUseColorPalette(useCP);
				ff.setRotation(rot);
				FractalBase.setMaxIter(diyMaxIt);
				FractalBase.setxC(diyMXc);
				FractalBase.setxC(diyMYc);
				FractalBase.setScaleSize(diyMScale);
				
			} else if(this.diyJuliaRb.isSelected()){
				//
				int diyJuliaP = this.getDiyJuliaPower();
				boolean diyJuliaUseD = this.getDiyJuliaUseDiff();
				int diyJuliaMaxIt = this.diyJuliaMaxIter;
				double diyJuliaBd = this.diyJuliaBound;
				double diyJXc = this.diyJuliaXC;
				double diyJYc = this.diyJuliaYC;
				double diyJScale = this.diyJuliaScaleSize;

				boolean diyJKConst = this.diyJuliaKeepConst;
				if (diyJKConst) {
					ff = new Julia(diyJuliaP, diyJuliaUseD, diyJuliaBd, diyJKConst);
				} else {

					double diyJuliaRealVal = Double.parseDouble(this.diyJuliaRealTf.getText());
					double diyJuliaImgVal = Double.parseDouble(this.diyJuliaImgTf.getText());
					ff = new Julia(diyJuliaP, diyJuliaUseD, diyJuliaBd, diyJuliaRealVal, diyJuliaImgVal);
				}

				ff.setUseColorPalette(useCP);
				ff.setRotation(rot);
				FractalBase.setMaxIter(diyJuliaMaxIt);
				FractalBase.setxC(diyJXc);
				FractalBase.setxC(diyJYc);
				FractalBase.setScaleSize(diyJScale);
			} else if (this.diyApolloRb.isSelected()) {
				double c1 = this.diyApolloC1;
				double c2 = this.diyApolloC2;
				double c3 = this.diyApolloC3;
				double mult = this.diyApolloMult;
				ff = new ApollonianCircles(new double[] {c1,c2,c3}, mult);
				ff.setUseColorPalette(useCP);
			} else {
				ff=null;
			}
			this.doReset();
		}  else {
			ff = null;
			return;
		}

		ff.reset();
		this.startFractals(ff);
	}	

	private void startFractals(final FractalBase ff) {
//		this.startProgress();
		final FractalBase frame = ff;
		frame.setTitle(ff.getFractalShapeTitle());
		
		if (!(this.diyApolloRb.isSelected() || this.getComboChoice().equals(APOLLONIAN_CIRCLES))) {
			frame.setSize(FractalBase.getAreaSize(), FractalBase.getAreaSize());//(FractalBase.WIDTH, FractalBase.HEIGHT);
		}/* else {
			frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
		}*/
		frame.setDefaultCloseOperation(closeIt(frame));
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth())/2, 
	            (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight())/2);
		frame.setResizable(false);
		frame.setVisible(true);

		this.setFractalImage(frame.getBufferedImage());
//		frame.setRunning(true);
		/*
		if(this.doMagnify){
			new ZoomInBox(frame);
		}*/

		if ( !((this.comboChoice.equals(MANDELBROT) || this.comboChoice.equals(JULIA)) || 
				( this.comboChoice.equals(DIY) && !this.diyApolloRb.isSelected()) )) {
			//	Threaded -- so as the FractalBase depth increases
			// the iteration's image is rendered recursively till depth = 0
			frame.setRunning(true);

			if(this.doMagnify){
				new ZoomInBox(frame);
			}
			this.formulaArea.setVisible(false);
			this.fbf = new Thread(frame);
			this.fbf.start();
			
			this.setFractalImage(frame.getBufferedImage());
			
			return;
		}
		

		if(this.doMagnify){
			this.setFractalImage(frame.getBufferedImage());
			new ZoomInBox(frame);
		}
		return;
		
//		this.endProgress();
	}

	private int closeIt(FractalBase frame) {
		this.buStart.setEnabled(false);
		if (!(this.comboChoice.equals(JULIA)||this.comboChoice.equals(MANDELBROT))) {
			this.formulaArea.setVisible(false);
		}
		frame.reset();
		frame.dispose();
		return JFrame.DISPOSE_ON_CLOSE;
	}
	
	private void doPauseCommand(Thread fb){
		fb.interrupt();
	}
	
	private void doSaveImageCommand(){
		BufferedImage image = this.getFractalImage();
		String extraInfo = this.getExtraInfo();
		String imageFilePath="images\\"+this.getComboChoice()+"["+extraInfo+"]"+" ____"+System.currentTimeMillis()+".png";
		File outputfile = new File(imageFilePath);
	    try {
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getExtraInfo() {
		String choice = this.getComboChoice();
		String extra = "";
//		System.out.println("Choice--to--saveimage--" + choice);
		switch (choice) {
			case MANDELBROT:
				extra = "";
				extra += "Z(" + this.magnification + ")";
				extra += "E(" + this.exponent + ")";
				extra += "B(" + this.mandBound + ")";
				extra += "MxIt(" + this.mandMaxIter + ")";
				extra += "Cx(" + this.mandXC + ")";
				extra += "Cy(" + this.mandYC + ")";
				extra += "Sz(" + this.mandScaleSize + ")";
				break;
			case JULIA:
				extra = "";
				extra += "P("+this.power+")";
				if(this.compConst!=0.0)
					extra+="C("+this.compConst+")";
				else
					extra+="C("+this.complex+")";
				extra += "B(" + this.mandBound + ")";
				extra += "MxIt(" + this.juliaMaxIter + ")";
				extra += "Cx(" + this.juliaXC + ")";
				extra += "Cy(" + this.juliaYC + ")";
				extra += "Sz(" + this.juliaScaleSize + ")";
			case "DIY_"+MANDELBROT:
				extra = "";
				extra += "Z(" + this.diyMandMagnification + ")";
				extra += "E(" + this.diyMandExponent + ")";
				extra += "B(" + this.diyMandBound + ")";
				extra += "MxIt(" + this.diyMandMaxIter + ")";
				extra += "Cx(" + this.diyMandXC + ")";
				extra += "Cy(" + this.diyMandYC + ")";
				extra += "Sz(" + this.diyMandScaleSize + ")";
				if (diyMandKeepConst) {
					extra += "CONST";
				}else{
					extra += "Real(" + this.diyMandConstReal + ")";
					extra += "Img(" + this.diyMandConstImg + ")";
				}
				break;
			case "DIY_"+JULIA:
				extra = "";
				extra += "P("+this.diyJuliaPower+")";
				
				extra += "B(" + this.diyJuliaBound + ")";
				extra += "MxIt(" + this.diyJuliaMaxIter + ")";
				extra += "Cx(" + this.diyJuliaXC + ")";
				extra += "Cy(" + this.diyJuliaYC + ")";
				extra += "Sz(" + this.diyJuliaScaleSize + ")";
				if (diyJuliaKeepConst) {
					extra += "CONST";
				}else{
					extra += "Real(" + this.diyJuliaConstReal + ")";
					extra += "Img(" + this.diyJuliaConstImg + ")";
				}
			default:
				extra="";
		}
		
		
		/*if( choice.equalsIgnoreCase(MANDELBROT) ){
			extra+="Z("+this.magnification+")";
			extra+="E("+this.exponent+")";
			extra+="B("+this.mandBound+")";
			extra+="MxIt("+this.mandMaxIter+")";
			extra+="Cx("+this.mandXC+")";
			extra+="Cy("+this.mandYC+")";
			extra+="Sz("+this.mandScaleSize+")";
		} else if(choice.equalsIgnoreCase(JULIA)){}*/
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
				
//				graphics.drawImage(image, 0-FractalBase.OFFSET, 0-FractalBase.OFFSET, image.getWidth()+FractalBase.OFFSET, image.getHeight()+FractalBase.OFFSET, null);
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
		
		this.setupFannyListeners();
		
		this.setupApolloListeners();
		
		this.setupJuliaListeners();
		
		this.setupMandelbrotListeners();
		
		this.diyMandRb.addActionListener(diyFractChoiceRbListener());
		this.diyJuliaRb.addActionListener(diyFractChoiceRbListener());
		this.diyApolloRb.addActionListener(diyFractChoiceRbListener());
		
		
		this.colrPRb.addActionListener(colorChoiceRbListener());
		this.colrCRb.addActionListener(colorChoiceRbListener());
		
		
		this.diyMandColrPRb.addActionListener(colorChoiceRbListener());
		this.diyMandColrCRb.addActionListener(colorChoiceRbListener());
		this.diyJuliaColrPRb.addActionListener(colorChoiceRbListener());
		this.diyJuliaColrCRb.addActionListener(colorChoiceRbListener());
		this.diyApolloColrPRb.addActionListener(colorChoiceRbListener());
		this.diyApolloColrCRb.addActionListener(colorChoiceRbListener());
		
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
		
		this.diyMandKeepConstantCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyMandKeepConstantCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyMandKeepConstantCommand(false);
				}
			}
        });
		

		
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
		
		this.diyMandSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer mandLoopLimitComboOption = (Integer)cb.getSelectedItem();
				doSelectMandLoopLimitCombosCommand(mandLoopLimitComboOption);				
			}});
		
		this.diyMandColrPRb.addActionListener(colorChoiceRbListener());
		this.diyMandColrCRb.addActionListener(colorChoiceRbListener());
		

		//--julia-diy
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
		
		this.diyJuliaKeepConstantCb.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetDiyJuliaKeepConstantCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetDiyJuliaKeepConstantCommand(false);
				}
			}
        });
		
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
		
		this.diyJuliaSizeCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
				Integer juliaLoopLimitComboOption = (Integer)cb.getSelectedItem();
				doSelectJuliaLoopLimitCombosCommand(juliaLoopLimitComboOption);				
			}});
		

		this.diyJuliaColrPRb.addActionListener(colorChoiceRbListener());
		this.diyJuliaColrCRb.addActionListener(colorChoiceRbListener());
		
		
		
		//apollo-diy
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
		//
		
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
		
		this.mandColrPRb.addActionListener(colorChoiceRbListener());
		this.mandColrCRb.addActionListener(colorChoiceRbListener());
		///////////////////	endsmandelbrot	//////////////////////
		//////////////////////////////////////////////////////////
	}

	private void setupJuliaListeners() {
		this.juliaCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String juliaComboOption = (String)cb.getSelectedItem();
				doSelectJuliaCombosCommand(juliaComboOption);				
			}});

		
		this.juliaUseDiff.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetJuliaUseDiffCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetJuliaUseDiffCommand(false);
				}
			}
        });
		
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
		

		this.juliaColrPRb.addActionListener(colorChoiceRbListener());
		this.juliaColrCRb.addActionListener(colorChoiceRbListener());
	}

	private void setupApolloListeners() {
		this.curvCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String curvComboOption = (String)cb.getSelectedItem();
				doSelectCurvCombosCommand(curvComboOption);				
			}});
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
			}});
		
		this.ratioCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
		        @SuppressWarnings("unused")
				Integer ratioComboOption = (Integer)cb.getSelectedIndex();
		        int ratioVal = cb.getItemAt(ratioComboOption);
				doSelectRatioCombosCommand(ratioVal);				
			}});
	}
	
	
	private ActionListener colorChoiceRbListener() {
		return new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				 JRadioButton button = (JRadioButton) e.getSource();
				 //System.out.println("buttonName-----------"+button.getName());
				 doFractalColorChoice(/*button.getName()*/);			
			}};
	}
	
	
	
	protected void doFractalColorChoice(/*String colrChoice*/) {
			
	//System.out.println("Here---doSetDiyFractalChoice -- "+diyChoice);
			if (/*colrChoice.equals("ComputeColor") && */(this.juliaColrCRb.isSelected()||this.mandColrCRb.isSelected())) {
				this.setUseColorPalette(true);
			} else if (/*colrChoice.equals("ComputeColor") && */(this.juliaColrCRb.isSelected()||this.mandColrCRb.isSelected())) {
				this.setUseColorPalette(false);
			} 
		
		
	}

	private void setUseColorPalette(boolean b) {
		this.useColorPalette = b;

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
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				 JRadioButton button = (JRadioButton) e.getSource();
//				 System.out.println("buttonName"+button.getName());
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
		return this.jUseDiff || this.juliaUseDiff.isSelected();
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
		return diyJuliaPower;
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

	public BufferedImage getFractalImage() {
		return this.fractalImage;
	}

	public void setFractalImage(BufferedImage fImage) {
		this.fractalImage = fImage;
	}

	public void setFractalImage(Image fImage) {
		this.fractalImage = (BufferedImage) fImage;
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
