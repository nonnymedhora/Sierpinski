/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

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
	private final Integer[] sideOptions = new Integer[] { 50, 70, 100, 150, 200, 250, 300, 350 };
	private final JComboBox<Integer> sideCombos = new JComboBox<Integer>(sideOptions);
	private final Integer[] ratioOptions = new Integer[] { 2, 3, 4, 5, 6, 7 };
	private final JComboBox<Integer> ratioCombos = new JComboBox<Integer>(ratioOptions);
	
	// for Julia
	private final String[] juliaOptions = new String[] { J1, J2, J3, J4, J5, J6, J7, J8, J9 };
	private final JComboBox<String> juliaCombos = new JComboBox<String>(juliaOptions);
	private final JCheckBox juliaUseDiff = new JCheckBox("UseDifferencesOnly",true);

	// for Mandelbrot
	private final Integer[] mandOptions = new Integer[]{1,2,3,4,5,6,7,8,9,10};
	private final JComboBox<Integer> mandCombos = new JComboBox<Integer>(mandOptions);
	private final Integer[] mandExpOptions = new Integer[]{2,3,4,5,6,7};
	private final JComboBox<Integer> mandExpCombos = new JComboBox<Integer>(mandExpOptions);
	private final JCheckBox mandUseDiff = new JCheckBox("UseDifferencesOnly",true);

	//	for	Apollo
	private final String[] curvOptions = new String[] { A1, A2, A3, A4, A5, A6 };
	private final JComboBox<String> curvCombos = new JComboBox<String>(curvOptions);

	// Fractal Art Options
	private JPanel fannyOptionsPanel = new JPanel(new FlowLayout(),true);
	private JPanel apolloOptionsPanel = new JPanel(new FlowLayout(),true);
	private JPanel juliaOptionsPanel = new JPanel(new FlowLayout(),true);
	private JPanel mandOptionsPanel = new JPanel(new FlowLayout(),true);
	private JPanel diyOptionsPanel	= new JPanel(new FlowLayout(),true);
	
	private JPanel diyMandPanel = new JPanel(new FlowLayout(),true);
	private JPanel diyJuliaPanel = new JPanel(new FlowLayout(),true);
	private JPanel diyApolloPanel = new JPanel(new FlowLayout(),true);
	
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
	
	// for mandelbrot
	protected int magnification;
	protected int exponent;
	protected boolean mUseDiff;
	
	
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
	protected int diyMandMagnification;
	protected int diyMandExponent;
	protected double diyMandConstReal;
	protected double diyMandConstImg;
	protected boolean diyMandUseDiff;
	
	private final Integer[] diyMandMagOptions = new Integer[]{1,2,3,4,5,6,7,8,9,10};
	private final JComboBox<Integer> diyMandMagCombos = new JComboBox<Integer>(diyMandMagOptions);
	private final Integer[] diyMandExpOptions = new Integer[]{2,3,4,5,6,7};
	private final JComboBox<Integer> diyMandExpCombos = new JComboBox<Integer>(diyMandExpOptions);
	private final JCheckBox diyMandUseDiffCb = new JCheckBox("UseDifferencesOnly",true);
	private JTextField diyMandRealTf = new JTextField(5);
	private JTextField diyMandImgTf = new JTextField(5);

	
	//diy Julia options
	protected int diyJuliaPower;
	protected double diyJuliaConstReal;
	protected double diyJuliaConstImg;
	protected boolean diyJuliaUseDiff;
	
	private final Integer[] diyJuliaPowerOptions = new Integer[]{2,3,4,5,6,7};
	private final JComboBox<Integer> diyJuliaPowerCombos = new JComboBox<Integer>(diyJuliaPowerOptions);
	private final JCheckBox diyJuliaUseDiffCb = new JCheckBox("UseDifferencesOnly",true);
	private JTextField diyJuliaRealTf = new JTextField(5);
	private JTextField diyJuliaImgTf = new JTextField(5);
	
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
	
	private final JCheckBox magnifyCb = new JCheckBox("Magnify",false);
	private boolean doMagnify = false;
	
//	private FractalBase ff;
	
	
	
	
	private Thread fbf;
	
	public SierpinskiComboPanel() {
		super();
		this.setLayout(new FlowLayout());
		
		this.setComboSelections();			
		this.setUpListeners();

		this.add(new JLabel("Choose FractalArt:"));
		this.add(this.combos);

		this.fannyOptionsPanel.add(new JLabel("Dimension Size:"));
		this.fannyOptionsPanel.add(this.sideCombos);

		this.fannyOptionsPanel.add(new JLabel("Ratio:"));
		this.fannyOptionsPanel.add(this.ratioCombos);
		
		this.fannyOptionsPanel.setVisible(false);
		this.add(this.fannyOptionsPanel);
		
		this.apolloOptionsPanel.add(new JLabel("CurvatureOptions:"));
		this.apolloOptionsPanel.add(this.curvCombos);
		this.apolloOptionsPanel.setVisible(false);
		this.add(this.apolloOptionsPanel);
		
		this.juliaOptionsPanel.add(new JLabel("Power-Constant"));
		this.juliaOptionsPanel.add(this.juliaCombos);		
		this.juliaOptionsPanel.add(this.juliaUseDiff);
		this.juliaOptionsPanel.setVisible(false);
		this.add(this.juliaOptionsPanel);
		
		this.mandOptionsPanel.add(new JLabel("Magnification(M):"));
		this.mandOptionsPanel.add(this.mandCombos);
		this.mandOptionsPanel.add(new JLabel("Exponent(M):"));
		this.mandOptionsPanel.add(this.mandExpCombos);		
		this.mandOptionsPanel.add(this.mandUseDiff);		
		
		this.mandOptionsPanel.setVisible(false);
		this.add(this.mandOptionsPanel);
		
		this.diyOptionsPanel.add(new JLabel("Choose Fractal Art:"));
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
		
		this.diyMandPanel.add(new JLabel("Magnification:"));
		this.diyMandPanel.add(this.diyMandMagCombos);
		this.diyMandPanel.add(new JLabel("Exponent:"));
		this.diyMandPanel.add(this.diyMandExpCombos);
		this.diyMandPanel.add(new JLabel("ComplexConstant - Real (R) "));
		this.diyMandPanel.add(this.diyMandRealTf);
		this.diyMandPanel.add(new JLabel("Imaginary (*i)"));
		this.diyMandPanel.add(this.diyMandImgTf);
		this.diyMandPanel.add(this.diyMandUseDiffCb);
		this.diyMandPanel.setVisible(false);
		
		this.diyJuliaPanel.add(new JLabel("Power:"));
		this.diyJuliaPanel.add(this.diyJuliaPowerCombos);
		this.diyJuliaPanel.add(new JLabel("ComplexConstant - Real (R) "));
		this.diyJuliaPanel.add(this.diyJuliaRealTf);
		this.diyJuliaPanel.add(new JLabel("Imaginary (*i)"));
		this.diyJuliaPanel.add(this.diyJuliaImgTf);
		this.diyJuliaPanel.add(this.diyJuliaUseDiffCb);
		this.diyJuliaPanel.setVisible(false);
		
		this.diyApolloPanel.add(new JLabel("C1"));
		this.diyApolloPanel.add(this.diyApolloC1Combos);
		this.diyApolloPanel.add(new JLabel("C2"));
		this.diyApolloPanel.add(this.diyApolloC2Combos);
		this.diyApolloPanel.add(new JLabel("C3"));
		this.diyApolloPanel.add(this.diyApolloC3Combos);
		this.diyApolloPanel.add(new JLabel("Multiplier"));
		this.diyApolloPanel.add(this.diyApolloMultCombos);
		this.diyApolloPanel.setVisible(false);
		
		
		this.diyOptionsPanel.add(this.diyMandPanel);
		this.diyOptionsPanel.add(this.diyJuliaPanel);
		this.diyOptionsPanel.add(this.diyApolloPanel);
		this.diyOptionsPanel.setVisible(false);
		
		this.add(diyOptionsPanel);
		
		this.add(this.magnifyCb);

		this.buStart.setEnabled(false);
		this.add(this.buStart);
//		this.add(this.buPause);
		
		this.formulaArea.setVisible(false);
		this.add(this.formulaArea);
		
//		this.add(this.ff);
		
	}
	
	private void doReset() {
		this.sideChoice = 0;
		this.ratioChoice = 0;
		this.curvChoices = null;
		this.mult = 0.0;
		this.power = 0;
		this.compConst = 0.0;
		this.complex = null;
		this.magnification = 0;
		
		//diy
		this.diyMandMagnification = 0;
		this.diyMandExponent = 0;
		this.diyMandConstReal = 0.0;
		this.diyMandConstImg = 0.0;

		this.diyJuliaPower = 0;
		this.diyJuliaConstReal = 0.0;
		this.diyJuliaConstImg = 0.0;

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
		
		this.diyMandMagCombos.setSelectedItem(this.diyMandMagOptions[0]);
		this.diyMandExpCombos.setSelectedItem(this.diyMandExpOptions);
		
		this.diyJuliaPowerCombos.setSelectedItem(this.diyJuliaPowerOptions[0]);

		this.diyApolloC1Combos.setSelectedItem(this.diyApolloC1Options[0]);
		this.diyApolloC2Combos.setSelectedItem(this.diyApolloC2Options[0]);
		this.diyApolloC3Combos.setSelectedItem(this.diyApolloC3Options[0]);
		this.diyApolloMultCombos.setSelectedItem(this.diyApolloMultOptions[0]);
	}
	
	private void doSelectCombosCommand(String option) {
//		System.out.println("in doSelectCombosCommand == option is "+option);
		this.comboChoice = option;

		if (this.comboChoice.equals(APOLLONIAN_CIRCLES)) {
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
			this.fannyOptionsPanel.setVisible(false);
			this.juliaOptionsPanel.setVisible(false);
			this.mandOptionsPanel.setVisible(true);
			this.apolloOptionsPanel.setVisible(false);
			this.diyOptionsPanel.setVisible(false);
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
		}  else if (this.comboChoice.equals(DIY)) {
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
	
	
	private void doMandelbrotChoicesCheck() {
		this.formulaArea.setVisible(true);
		if (this.magnification != 0 && this.exponent != 0) {
			this.buStart.setEnabled(true);
			this.formulaArea.setText("Mandelbrot Set:\n\nf(z) = z ^ " + this.exponent + " + C");
			if (this.mUseDiff || this.mandUseDiff.isSelected()) {
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
		if (this.diyMandMagnification != 0 && this.diyMandExponent != 0 &&
				(this.diyMandRealTf.getText().length()>0)&&(this.diyMandImgTf.getText().length()>0)) {
//			System.out.println("this.diyMandRealTf.getText()=="+this.diyMandRealTf.getText()+"Dvval is "+Double.parseDouble(this.diyMandRealTf.getText()));
//			System.out.println("this.diyMandImgTf.getText()=="+this.diyMandImgTf.getText()+"Dvval is "+Double.parseDouble(this.diyMandImgTf.getText()));
			this.buStart.setEnabled(true);
			this.formulaArea.setText("Mandelbrot Set:\n\nf(z) = z ^ " + this.diyMandExponent + " + C");
			this.formulaArea.setText("\n  C = "+Double.parseDouble(this.diyMandRealTf.getText())+" + ("+Double.parseDouble(this.diyMandImgTf.getText())+")");
			if (this.mUseDiff || this.mandUseDiff.isSelected()) {
				this.formulaArea
						.append("\n\nCalculated inverted colors based on differences in pixel values from origin");
			} else {
				this.formulaArea.append("\n\nCalculated colors based on pixel values with a 'top-and-left' origin");
			}
		} else {
			this.buStart.setEnabled(false);
		}
	}
	
	private void doDiyJuliaChoicesCheck() {
		this.formulaArea.setVisible(true);
		if(this.diyJuliaPower!=0&&(this.diyJuliaRealTf.getText().length()>0)&&(this.diyJuliaImgTf.getText().length()>0)){
			this.buStart.setEnabled(true);
			this.formulaArea.setText("Julia Set:\n\nf(z) = z ^ "+this.diyJuliaPower+" + C");
			
			this.formulaArea.setText("\n  C = "+Double.parseDouble(this.diyJuliaRealTf.getText())+" + ("+Double.parseDouble(this.diyJuliaImgTf.getText())+")");
			if (this.diyJuliaUseDiff || this.diyJuliaUseDiffCb.isSelected()) {
				this.formulaArea
						.append("\n\nCalculated inverted colors based on differences in pixel values from origin");
			} else {
				this.formulaArea.append("\n\nCalculated colors based on pixel values with a 'top-and-left' origin");
			}
		}else{
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
	//-julia
	private void doSelectDiyJuliaPowerCombosCommand(Integer powerOption) {
		this.diyJuliaPower = powerOption;
		this.formulaArea.setVisible(true);
		this.doDiyJuliaChoicesCheck();
	}
	
	private void doSetDiyJuliaUseDiffCommand(boolean useDiffs) {
		this.diyJuliaUseDiff = useDiffs;
		this.formulaArea.setVisible(true);
		this.doDiyJuliaChoicesCheck();
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
			this.diyMandPanel.setVisible(true);
			this.diyJuliaPanel.setVisible(false);
			this.diyApolloPanel.setVisible(false);
		} else if (diyChoice.equals("DIY_" + JULIA) && this.diyJuliaRb.isSelected()) {
			this.diyMandPanel.setVisible(false);
			this.diyJuliaPanel.setVisible(true);
			this.diyApolloPanel.setVisible(false);
		} else if (diyChoice.equals("DIY_" + APOLLONIAN_CIRCLES) && this.diyApolloRb.isSelected()) {
			this.diyMandPanel.setVisible(false);
			this.diyJuliaPanel.setVisible(false);
			this.diyApolloPanel.setVisible(true);
		}
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
		// for Mandelbrot
		int mag = this.getMagnification();
		int exp = this.getExponent();
		boolean mUseD = this.getMUseDiff();

		/*final*/ FractalBase ff = null;;
		
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
			ff = new Mandelbrot(mag, exp, mUseD, true);
			this.doReset();
		} else if (choice.equals(JULIA)) {
			this.formulaArea.setVisible(true);
			this.formulaArea.setText("");
			this.addJuliaFormulaInfo();
			this.addJuliaUseDiffInfo();
			if (!(this.juliaSelection.equals("J7") || this.juliaSelection.equals("J8")
					|| this.juliaSelection.equals("J9"))) {
				ff = new Julia(pow, con, jUseD);
			} else {
				ff = new Julia(pow, comp, jUseD);
			}
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
			if (this.diyMandRb.isSelected()) {
				// for diy mandelbrot
				int diyMag = this.getDiyMandMagnification();
				int diyMandExp = this.getDiyMandExponent();
				boolean diyMandUseD = this.getDiyMandUseDiff();

				double diyMRealVal = Double.parseDouble(this.diyMandRealTf.getText());
				double diyMImgVal = Double.parseDouble(this.diyMandImgTf.getText());
				ff = new Mandelbrot(diyMag, diyMandExp, diyMandUseD, diyMRealVal, diyMImgVal);
			} else if(this.diyJuliaRb.isSelected()){
				//
				int diyJuliaP = this.getDiyJuliaPower();
				boolean diyJuliaUseD = this.getDiyJuliaUseDiff();

				double diyJuliaRealVal = Double.parseDouble(this.diyJuliaRealTf.getText());
				double diyJuliaImgVal = Double.parseDouble(this.diyJuliaImgTf.getText());
				ff = new Julia(diyJuliaP, diyJuliaUseD, diyJuliaRealVal, diyJuliaImgVal);
			} else if (this.diyApolloRb.isSelected()) {
				double c1 = this.diyApolloC1;
				double c2 = this.diyApolloC2;
				double c3 = this.diyApolloC3;
				double mult = this.diyApolloMult;
				ff = new ApollonianCircles(new double[] {c1,c2,c3}, mult);
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
		final FractalBase frame = ff;
		frame.setTitle(ff.getFractalShapeTitle());
		frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
		frame.setDefaultCloseOperation(closeIt(frame));
		frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width - frame.getWidth())/2, 
	            (Toolkit.getDefaultToolkit().getScreenSize().height - frame.getHeight())/2);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setRunning(true);
		
		if(this.doMagnify){
			new ZoomInBox(frame);
		}

		if ( !(this.comboChoice.equals(MANDELBROT) || this.comboChoice.equals(JULIA) || 
				( this.comboChoice.equals(DIY) && !this.diyApolloRb.isSelected()) )) {
			//	Threaded -- so as the FractalBase depth increases
			// the iteration's image is rendered recursively till depth = 0
			this.formulaArea.setVisible(false);
			this.fbf = new Thread(frame);
			this.fbf.start();
		}
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

	private void setUpListeners() {		
		this.combos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String comboOption = (String)cb.getSelectedItem();
				doSelectCombosCommand(comboOption);				
			}});
		
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
		
		this.curvCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String curvComboOption = (String)cb.getSelectedItem();
				doSelectCurvCombosCommand(curvComboOption);				
			}});
		
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
		
		this.mandUseDiff.addItemListener(new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					doSetMandUseDiffCommand(true);
				} else if(event.getStateChange()==ItemEvent.DESELECTED){
					doSetMandUseDiffCommand(false);
				}
			}
        });
		
		
		this.diyMandRb.addActionListener(diyFractChoiceRbListener());
		this.diyJuliaRb.addActionListener(diyFractChoiceRbListener());
		this.diyApolloRb.addActionListener(diyFractChoiceRbListener());
		
		
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
		return this.mUseDiff || this.mandUseDiff.isSelected();
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
	    repaint();
	   	    
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
