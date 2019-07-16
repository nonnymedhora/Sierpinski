package org.bawaweb.ui.sierpinkski;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * 
 */

/**
 * @author Navroz
 *
 */
public class SierpinskiFr {

	private JButton start = new JButton("Start");
	
	private JButton stop = new JButton("Stop");
	private JTextArea resA = new JTextArea();
	protected SierpinskiPanel sierpinskiPanel = new SierpinskiPanel();

	public SierpinskiFr() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}

				JFrame frame = new JFrame("Bawa's Sierpinski Triangle");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLayout(new BorderLayout());
				frame.setSize(800,800);
				frame.add(sierpinskiPanel,BorderLayout.CENTER);

				JPanel butPanel = new JPanel();
				start.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
					doStartCommand();
				}

					private void doStartCommand() {
						System.out.println("In start");
						sierpinskiPanel.started = true;
						sierpinskiPanel.stopped = false;
						new Thread(sierpinskiPanel).start();
					}});
				
				stop.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
					doStopCommand();
				}

					private void doStopCommand() {
						System.out.println("In stoppppp");
						sierpinskiPanel.started = false;
						sierpinskiPanel.stopped = true;

					}});
				
				
				butPanel.add(start);
				butPanel.add(stop);
				
				frame.add(butPanel,BorderLayout.SOUTH);

				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public static void main(String[] args) {
		new SierpinskiFr();
	}

}
