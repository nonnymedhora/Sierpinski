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

class SierpinskiComboPanel extends JPanel implements ActionListener {

	private static final String SIERPINSKI_SQUARES = "SierpinskiSquares";

	private static final String SIERPINSKI_TRIANGLES = "SierpinskiTriangles";

	private static final String FANNY_TRIANGLES = "FannyTriangles";

	private static final String FANNY_CIRCLE = "FannyCircle";

	private static final long serialVersionUID = 156478L;
	private final String[] comboOptions = new String[]{FANNY_CIRCLE,FANNY_TRIANGLES,SIERPINSKI_TRIANGLES,SIERPINSKI_SQUARES/*,"KochSnowfalke"*/};
	private final JComboBox<String> combos = new JComboBox<String>(comboOptions);
	private final Integer[] sideOptions = new Integer[]{50,70,100,150,200,250,300,350};
	private final JComboBox<Integer> sideCombos	= new JComboBox<Integer>(sideOptions);
	private final Integer[] ratioOptions = new Integer[]{2,3,4,5,6,7};
	private final JComboBox<Integer> ratioCombos	= new JComboBox<Integer>(ratioOptions);
	private final JButton buStart = new JButton("Start");

	protected String comboChoice;

	protected int sideChoice;
	
	protected int ratioChoice;
	
	public SierpinskiComboPanel(){
		super();
		this.setLayout(new FlowLayout());//(new BoxLayout(this, 0)); // BoxLayout.X_AXIS==0

//		this.populateCombos(this.combos, this.sideCombos, this.ratioCombos);
		this.setComboSelections();
		
		
		this.setUpListeners();

		this.add(new JLabel("ChooseFractalArt:"));
		this.add(this.combos);

		this.add(new JLabel("DimensionSize:"));
		this.add(this.sideCombos);

		this.add(new JLabel("Ratio:"));
		this.add(this.ratioCombos);

		this.add(this.buStart);
		
	}

	private void setComboSelections() {
		this.combos.setSelectedIndex(0);
		this.sideCombos.setSelectedIndex(0);
		this.ratioCombos.setSelectedIndex(0);
		
		this.combos.setSelectedItem(this.comboOptions[0]);
		
		this.sideCombos.setSelectedItem(this.sideOptions[0]);
		
		this.ratioCombos.setSelectedItem(this.ratioOptions[0]);
	}

	private void setUpListeners() {
		/*this.combos.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e){
				
			}
		});*/
		
		this.combos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> cb = (JComboBox<String>)e.getSource();
		        String comboOption = (String)cb.getSelectedItem();
				doSelectCombosCommand(comboOption);				
			}

			private void doSelectCombosCommand(String option) {
				comboChoice=option;
			}	    	
	    });
		
		this.sideCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
		        Integer sideComboOption = (Integer)cb.getSelectedIndex();
		        System.out.println("sideComboOption==="+sideComboOption);
		        int sideVal = cb.getItemAt(sideComboOption);
				doSelectSideCombosCommand(sideVal);				
			}

			private void doSelectSideCombosCommand(Integer sideOption) {
				sideChoice=sideOption;
			}	    	
	    });
		
		this.ratioCombos.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<Integer> cb = (JComboBox<Integer>)e.getSource();
		        @SuppressWarnings("unused")
				Integer ratioComboOption = (Integer)cb.getSelectedIndex();
		        System.out.println("ratioComboOption==="+ratioComboOption);
		        int ratioVal = cb.getItemAt(ratioComboOption);
				doSelectRatioCombosCommand(ratioVal);				
			}

			private void doSelectRatioCombosCommand(Integer ratioOption) {
				ratioChoice=ratioOption;
			}	    	
	    });
		
		this.buStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doStartCommand();				
			}

			private void doStartCommand() {
				String choice = getComboChoice();
				int length = getSideComboChoice();
				int ratio = getRatioChoice();
				System.out.println("choice is "+choice+" and length is "+length+" and ratio is "+ratio);
				final FractalBase ff;
				if (choice.equals(FANNY_CIRCLE)) {
					ff = new FannyCircle(length,ratio);
				} else if (choice.equals(FANNY_TRIANGLES)) {
					ff=new FannyTriangles(length,ratio);

				} else if (choice.equals(SIERPINSKI_TRIANGLES)) {
					ff=new SierpinskiTriangle();

				} else if (choice.equals(SIERPINSKI_SQUARES)) {ff=new SierpinskiSquare();

				} /*else if (choice.equals("KochSnowfalke")) {
					KochSnowFlakeFractal kfframe = new KochSnowFlakeFractal();
					new Thread(kf).start();
				}*/
				else{
					ff=null;
				}
//				new Thread(ff).start();
				final String title = ff.getFractalShapeTitle();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						final FractalBase frame = ff;
						frame.setTitle(title);
						frame.setSize(FractalBase.WIDTH, FractalBase.HEIGHT);
						frame.setDefaultCloseOperation(closeIt(frame));//(JFrame.EXIT_ON_CLOSE);
						frame.setResizable(false);
						frame.setVisible(true);
			
						new Thread(frame).start();
			
					}

					private int closeIt(FractalBase frame) {
						frame.dispose();
						return JFrame.DISPOSE_ON_CLOSE;
					}
				});
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

//	private void populateCombos(JComboBox<String> com, JComboBox<Integer> sides, JComboBox<Integer> ratios) {
//		
////		com.addItem("FannyCircle");
////		com.addItem("FannyTriangles");
//		com = new JComboBox<String>(comboOptions);
//		this.combos=com;
//		
//		Integer[] sideLs = new Integer[]{50,70,100,150,200,250,300,350};
//		sides=new JComboBox<Integer>(sideLs);
//		this.sideCombos=sides;
//		
//		
//		Integer[] ratioOptions = new Integer[]{2,3,4,5,6,7};
//		ratios=new JComboBox<Integer>(ratioOptions);
//		this.ratioCombos=ratios;
//		
//	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
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

//	    cp.add(new JLabel("BaWaZ FractalArtz: "), BorderLayout.NORTH);
		cp.add(this.topPanel);
		
		
		
		
		this.pack();
	    repaint();
	   	    
		this.setVisible(true);
	    
	}

//	/**
//	 * @param gc
//	 */
//	public SierpinskiCompositional(GraphicsConfiguration gc) {
//		super(gc);
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * @param title
//	 * @throws HeadlessException
//	 */
//	public SierpinskiCompositional(String title) throws HeadlessException {
//		super(title);
//		// TODO Auto-generated constructor stub
//	}
//
//	/**
//	 * @param title
//	 * @param gc
//	 */
//	public SierpinskiCompositional(String title, GraphicsConfiguration gc) {
//		super(title, gc);
//		// TODO Auto-generated constructor stub
//	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
		      @Override
		      public void run() {
		        final SierpinskiCompositional frame = new SierpinskiCompositional();
		        frame.setTitle("BaWaZ FractalArtz: ");
		        frame.setSize(800, 800);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setResizable(false);
		        frame.setVisible(true);
		      }
		    });
	}

}
