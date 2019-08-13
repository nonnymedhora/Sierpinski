/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.bawaweb.ui.sierpinkski.FractalBase.ComplexNumber;

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

	private static final String J7 = "P[2] C1";//[-0.74543+0.11301*i]";	//f(z) = z^2 + ...
	private static final String J8 = "P[2] C2";//[-0.75+0.11*i]";	//f(z) = z^2 + ....
	private static final String J9 = "P[2] C3";//[-0.1+0.651*i]";	//f(z) = z^2 + ...
	
	
	//	for	ApollonianCircles
	// curvature & multiplier options for @see ApollonianCircles
	private static final String A6 = "C[2, 2, 3] M[1]";
	private static final String A5 = "C[23, 27, 18] M[16]";
	private static final String A4 = "C[10, 15, 19] M[6]";
	private static final String A3 = "C[50, 80, 8] M[6]";
	private static final String A2 = "C[25, 25, 28] M[20]";
	private static final String A1 = "C[1, 1, 1] M[1]";

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

	private static final long serialVersionUID = 156478L;
	private final String[] comboOptions = new String[]{FANNY_CIRCLE,FANNY_TRIANGLES,SIERPINSKI_TRIANGLES,SIERPINSKI_SQUARES,APOLLONIAN_CIRCLES,CST_FRACTAL,SAMPLE,MANDELBROT,JULIA,KOCHSNOWFLAKE};
	private final JComboBox<String> combos = new JComboBox<String>(comboOptions);
	
	//	for	FannyCircle & FannyTriangles
	private final Integer[] sideOptions = new Integer[] { 50, 70, 100, 150, 200, 250, 300, 350 };
	private final JComboBox<Integer> sideCombos = new JComboBox<Integer>(sideOptions);
	private final Integer[] ratioOptions = new Integer[] { 2, 3, 4, 5, 6, 7 };
	private final JComboBox<Integer> ratioCombos = new JComboBox<Integer>(ratioOptions);
	
	// for Julia
	private final String[] juliaOptions = new String[] { J1, J2, J3, J4, J5, J6, J7, J8, J9 };
	private final JComboBox<String> juliaCombos = new JComboBox<String>(juliaOptions);

	//	for	Apollo
	private final String[] curvOptions = new String[] { A1, A2, A3, A4, A5, A6 };
	private final JComboBox<String> curvCombos = new JComboBox<String>(curvOptions);

	private JPanel fannyOptionsPanel = new JPanel(new FlowLayout());
	private JPanel apolloOptionsPanel = new JPanel(new FlowLayout());
	private JPanel juliaOptionsPanel = new JPanel(new FlowLayout());
	
	private JTextArea formulaArea = new JTextArea(5,20);
	
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
		this.juliaOptionsPanel.setVisible(false);
		this.add(this.juliaOptionsPanel);

		this.add(this.buStart);
//		this.add(this.buPause);
		
		this.add(this.formulaArea);
		
	}

	private void setComboSelections() {
		this.combos.setSelectedIndex(0);
		this.sideCombos.setSelectedIndex(0);
		this.ratioCombos.setSelectedIndex(0);
		this.curvCombos.setSelectedIndex(0);
		this.juliaCombos.setSelectedIndex(0);
		
		this.combos.setSelectedItem(this.comboOptions[0]);		
		this.sideCombos.setSelectedItem(this.sideOptions[0]);		
		this.ratioCombos.setSelectedItem(this.ratioOptions[0]);		
		this.curvCombos.setSelectedItem(this.ratioOptions[0]);
		this.juliaCombos.setSelectedItem(this.juliaOptions[0]);
	}
	
	private void doSelectCombosCommand(String option) {
		this.comboChoice = option;

		if (comboChoice.equals(APOLLONIAN_CIRCLES)) {
			fannyOptionsPanel.setVisible(false);
			juliaOptionsPanel.setVisible(false);
			apolloOptionsPanel.setVisible(true);
		} else if (comboChoice.equals(FANNY_CIRCLE) || comboChoice.equals(FANNY_TRIANGLES)) {
			fannyOptionsPanel.setVisible(true);
			juliaOptionsPanel.setVisible(false);
			apolloOptionsPanel.setVisible(false);
		} else if(comboChoice.equals(JULIA)) { 
			fannyOptionsPanel.setVisible(false);
			juliaOptionsPanel.setVisible(true);
			apolloOptionsPanel.setVisible(false);
		} else {
			fannyOptionsPanel.setVisible(false);
			juliaOptionsPanel.setVisible(false);
			apolloOptionsPanel.setVisible(false);
		}
	}

	private void doSelectSideCombosCommand(Integer sideOption) {
		this.sideChoice = sideOption;
	}

	private void doSelectRatioCombosCommand(Integer ratioOption) {
		this.ratioChoice = ratioOption;
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

			case A3: // A3 = "C[50, 80, 8] M[6]";
				this.curvChoices = new double[] { 50.0, 80.0, 8.0 };
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
	}	
	
	private void doSelectJuliaCombosCommand(String cOption) {
		switch (cOption) {
			case J1: //  J1 = "P[2] C[0.279]";	//f(z) = z^2 + 0.279
				this.power = 2;
				this.compConst = 0.279;
				break;
			case J2: //  J2 = "P[3] C[0.4]";		//f(z) = z^3 + 0.400
				this.power = 3;
				this.compConst = 0.4;
				break;	
			case J3: //  J3 = "P[4] C[0.484]";	//f(z) = z^4 + 0.484
				this.power = 4;
				this.compConst = 0.484;
				break;	
			case J4: //  J4 = "P[5] C[0.544]";	//f(z) = z^5 + 0.544
				this.power = 5;
				this.compConst = 0.544;
				break;	
			case J5: //  J5 = "P[6] C[0.59]";	//f(z) = z^6 + 0.590
				this.power = 6;
				this.compConst = 0.59;
				break;	
			case J6: //  J6 = "P[7] C[0.626]";	//f(z) = z^7 + 0.626
				this.power = 7;
				this.compConst = 0.626;
				break;	
			case J7: //  J7 = "P[2] C1[-0.74543+0.11301*i]";
				this.power = 2;
				this.complex = "C1";
				break;	
			case J8: //  J8 = "P[2] C2";//[-0.75+0.11*i]";
				this.power = 2;
				this.complex = "C2";
				break;
			case J9: //  J9 = "P[2] C3";//[-0.1+0.651*i]";
				this.power = 2;
				this.complex = "C3";
				break;
				
			default:
				// use--J1
				this.power = 2;
				this.compConst = 0.279;
				break;
				
		}
	}
	
	private void doStartCommand() {
		String choice = this.getComboChoice();
		int length = this.getSideComboChoice();
		int ratio = this.getRatioChoice();
		double[] cChoices = this.getCurvChoice();
		double mXt = this.getMultiplier();
		int pow = this.getPower();
		double con = this.getComplexConst();
		String comp = this.getComplex();

//		System.out.println("choice is " + choice + " and length is " + length + " and ratio is " + ratio);
		final FractalBase ff;
		if (choice.equals(FANNY_CIRCLE)) {
			ff = new FannyCircle(length, ratio);
		} else if (choice.equals(FANNY_TRIANGLES)) {
			ff = new FannyTriangles(length, ratio);
		} else if (choice.equals(SIERPINSKI_TRIANGLES)) {
			ff = new SierpinskiTriangle();
		} else if (choice.equals(SIERPINSKI_SQUARES)) {
			ff = new SierpinskiSquare();
		} else if (choice.equals(APOLLONIAN_CIRCLES)) {
			ff = new ApollonianCircles(cChoices, mXt);
		} else if (choice.equals(SAMPLE)) {
			ff = new FractalBaseSample();
		} else if (choice.equals(MANDELBROT)) {
			ff = new Mandelbrot();
		} else if (choice.equals(JULIA)) {

			if (comp == null) {
				ff = new Julia(pow, con);

				this.formulaArea.setText("f(z) = z ^ " + pow + " + " + con);
			} else {
				ff = new Julia(pow, comp);

				if (comp.equals("C1")) {
					this.formulaArea.setText("f(z) = z ^ " + pow + " + (-0.74543 + 0.11301*i)");
				} else if (comp.equals("C2")) {
					this.formulaArea.setText("f(z) = z ^ " + pow + " + (-0.75 + 0.11*i)");
				} else if (comp.equals("C3")) {
					this.formulaArea.setText("f(z) = z ^ " + pow + " + (-0.1 + 0.651*i)");

				}
			}

		} else if (choice.equals(CST_FRACTAL)) {
			ff = new CSTFractal();
		} else if (choice.equals(SAMPLE)) {
			ff = new FractalBaseSample();
		} else if (choice.equals(KOCHSNOWFLAKE)) {
			ff = new KochSnowFlakeFractal();
		} else {
			ff = null;
			return;
		}

		ff.reset();
		startFractals(ff);
	}	


	private void startFractals(final FractalBase ff) {
		final FractalBase frame = ff;
		frame.setTitle(ff.getFractalShapeTitle());
		frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
		frame.setDefaultCloseOperation(closeIt(frame));
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setRunning(true);

		if (!(this.comboChoice.equals(MANDELBROT) || this.comboChoice.equals(JULIA))) {
			this.fbf = new Thread(frame);
			this.fbf.start();
		}
	}

	private int closeIt(FractalBase frame) {
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
//		        System.out.println("sideComboOption==="+sideComboOption);
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
//		        System.out.println("ratioComboOption==="+ratioComboOption);
		        int ratioVal = cb.getItemAt(ratioComboOption);
				doSelectRatioCombosCommand(ratioVal);				
			}});
		
		this.curvCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String curvComboOption = (String)cb.getSelectedItem();
//		        System.out.println("curvComboOption==="+curvComboOption);
				doSelectCurvCombosCommand(curvComboOption);				
			}});
		
		this.juliaCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
				String juliaComboOption = (String)cb.getSelectedItem();
//		        System.out.println("curvComboOption==="+curvComboOption);
				doSelectJuliaCombosCommand(juliaComboOption);				
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
		        frame.setSize(500, 200);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setResizable(false);
		        frame.setVisible(true);
		      }
		});
	}

}
