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
import javax.swing.SwingUtilities;

/**
 * @author Navroz
 *
 */

class SierpinskiComboPanel extends JPanel {
	//	for	ApollonianCircles
	// curvature & multiplier options for @see ApollonianCircles
	private static final String A6 = "C[2, 2, 3] M[1]";
	private static final String A5 = "C[23, 27, 18] M[16]";
	private static final String A4 = "C[10, 15, 19] M[6]";
	private static final String A3 = "C[50, 80, 8] M[6]";
	private static final String A2 = "C[25, 25, 28] M[20]";
	private static final String A1 = "C[1, 1, 1] M[1]";
	
	// class names sub to FractalBase	-	todo	-	dynamic-initialization
	private static final String SIERPINSKI_SQUARES = "SierpinskiSquares";
	private static final String APOLLONIAN_CIRCLES = "ApollonianCircles";
	private static final String SIERPINSKI_TRIANGLES = "SierpinskiTriangles";
	private static final String CST_FRACTAL = "CSTFractal";
	private static final String SAMPLE = "Sample";
	private static final String MANDELBROT = "Mandelbrot";
	private static final String FANNY_TRIANGLES = "FannyTriangles";
	private static final String FANNY_CIRCLE = "FannyCircle";
	private static final String KOCHSNOWFLAKE = "KochSnowFlake";	//not-yet

	private static final long serialVersionUID = 156478L;
	private final String[] comboOptions = new String[]{FANNY_CIRCLE,FANNY_TRIANGLES,SIERPINSKI_TRIANGLES,SIERPINSKI_SQUARES,APOLLONIAN_CIRCLES,CST_FRACTAL,SAMPLE,MANDELBROT,KOCHSNOWFLAKE};
	private final JComboBox<String> combos = new JComboBox<String>(comboOptions);
	
	//	for	FannyCircle & FannyTriangles
	private final Integer[] sideOptions = new Integer[] { 50, 70, 100, 150, 200, 250, 300, 350 };
	private final JComboBox<Integer> sideCombos = new JComboBox<Integer>(sideOptions);
	private final Integer[] ratioOptions = new Integer[] { 2, 3, 4, 5, 6, 7 };
	private final JComboBox<Integer> ratioCombos = new JComboBox<Integer>(ratioOptions);

	//	for	Apollo
	private final String[] curvOptions = new String[] { A1, A2, A3, A4, A5, A6 };
	private final JComboBox<String> curvCombos = new JComboBox<String>(curvOptions);

	private JPanel fannyOptionsPanel =new JPanel(new FlowLayout());
	private JPanel apolloOptionsPanel=new JPanel(new FlowLayout());
	
	private final JButton buStart = new JButton("Start |>");
	private final JButton buPause = new JButton("Pause ||");
	
	// for running sub-implementation of FractalBase
	protected String comboChoice;

	// FannyCircle & FannyTriangles	--	side Length & ratio
	protected int sideChoice;
	
	protected int ratioChoice;
	
	// forApollonianCircles -- curvatures & multiplier options
	protected double[] curvChoices;
	protected double mult;

	
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
		
		this.apolloOptionsPanel.add(new JLabel("CurvaturesOptions:"));
		this.apolloOptionsPanel.add(this.curvCombos);
		this.apolloOptionsPanel.setVisible(false);
		this.add(this.apolloOptionsPanel);


		this.add(this.buStart);
		this.add(this.buPause);
		
	}

	private void setComboSelections() {
		this.combos.setSelectedIndex(0);
		this.sideCombos.setSelectedIndex(0);
		this.ratioCombos.setSelectedIndex(0);
		this.curvCombos.setSelectedIndex(0);
		
		this.combos.setSelectedItem(this.comboOptions[0]);		
		this.sideCombos.setSelectedItem(this.sideOptions[0]);		
		this.ratioCombos.setSelectedItem(this.ratioOptions[0]);		
		this.curvCombos.setSelectedItem(this.ratioOptions[0]);
	}
	
	private void doSelectCombosCommand(String option) {
		comboChoice = option;

		if (comboChoice.equals(APOLLONIAN_CIRCLES)) {
			fannyOptionsPanel.setVisible(false);
			apolloOptionsPanel.setVisible(true);
		} else if (comboChoice.equals(FANNY_CIRCLE) || comboChoice.equals(FANNY_TRIANGLES)) {
			fannyOptionsPanel.setVisible(true);
			apolloOptionsPanel.setVisible(false);
		} else {
			fannyOptionsPanel.setVisible(false);
			apolloOptionsPanel.setVisible(false);
		}
	}

	private void doSelectSideCombosCommand(Integer sideOption) {
		sideChoice = sideOption;
	}

	private void doSelectRatioCombosCommand(Integer ratioOption) {
		ratioChoice = ratioOption;
	}
	
	private void doSelectCurvCombosCommand(String cOption) {
		switch (cOption) {
			case A1: // A1 = "C[1, 1, 1] M[1]";
				curvChoices = new double[] { 1.0, 1.0, 1.0 };
				mult = 1.0;
				break;

			case A2: // A2 = "C[25, 25, 28] M[20]";
				curvChoices = new double[] { 25.0, 25.0, 28.0 };
				mult = 20.0;
				break;

			case A3: // A3 = "C[50, 80, 8] M[6]";
				curvChoices = new double[] { 50.0, 80.0, 8.0 };
				mult = 6.0;
				break;

			case A4: // A4 = "C[10, 15, 19] M[6]";
				curvChoices = new double[] { 10.0, 15.0, 19.0 };
				mult = 6.0;
				break;

			case A5: // A5 = "C[23, 27, 18] M[16]";
				curvChoices = new double[] { 23.0, 27.0, 16.0 };
				mult = 16.0;
				break;

			case A6: // A6 = "C[2, 2, 3] M[1]";
				curvChoices = new double[] { 2.0, 2.0, 3.0 };
				mult = 1.0;
				break;

			default:
				// use--a1
				curvChoices = new double[] { 1.0, 1.0, 1.0 };
				mult = 1.0;
				break;
		}
	}	
	
	private void doStartCommand() {
		String choice = getComboChoice();
		int length = getSideComboChoice();
		int ratio = getRatioChoice();
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
			ff = new ApollonianCircles(curvChoices, mult);
		} else if (choice.equals(SAMPLE)) {
			ff = new FractalBaseSample();
		} else if (choice.equals(MANDELBROT)) {
			ff = new Mandelbrot();
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

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				startFractals(ff);

			}
		});
	}	


	private void startFractals(final FractalBase ff) {
		final FractalBase frame = ff;
		frame.setTitle(ff.getFractalShapeTitle());
		frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
		frame.setDefaultCloseOperation(closeIt(frame));
		frame.setResizable(false);
		frame.setVisible(true);

		this.fbf = new Thread(frame);
		this.fbf.start();
	}

	private int closeIt(FractalBase frame) {
		frame.reset();
		frame.dispose();
		return JFrame.DISPOSE_ON_CLOSE;
	}
	
	private void doPauseCommand(){
		this.fbf.interrupt();
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
		
		this.buStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStartCommand();				
			}});		

		this.buPause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doPauseCommand();				
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
		        frame.setSize(400, 400);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setResizable(false);
		        frame.setVisible(true);
		      }
		});
	}

}
