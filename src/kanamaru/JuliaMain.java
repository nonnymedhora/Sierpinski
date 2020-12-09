package kanamaru;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class JuliaMain extends Panel {
    GCD_Mandelbrot gcd;
    Button refresh;
    Button repaint;
    Button cancel;
    Scrollbar bar;
    Panel panelall;
    Panel panel0;
    Panel panel1;
    NPanel npanel;
    Choice colchoice;
    static final int barsize = 500;
    static final int barmin = 100;
    static final int barmax = 5000;
    int defcol1;
    int defcol2;
    int defcol3;
    GCD_Julia gcd_j;
    Button refresh_j;
    Button repaint_j;
    Button cancel_j;
    Scrollbar bar_j;
    Panel panelall_j;
    Panel panel0_j;
    Panel panel1_j;
    NPanel_j npanel_j;
    Choice colchoice_j;
    static final int barsize_j = 500;
    static final int barmin_j = 100;
    static final int barmax_j = 5000;
    int defcol1_j;
    int defcol2_j;
    int defcol3_j;

    public static void main(String[] arrstring) {
        JuliaMain juliaMain = new JuliaMain();
        juliaMain.setPreferredSize(new Dimension(920, 320));
        JFrame jFrame = new JFrame("Julia");
        jFrame.getContentPane().add((Component)juliaMain);
        jFrame.addWindowListener((WindowListener)new WindowAdapter(){

            public void windowClosing(WindowEvent windowEvent) {
                System.exit((int)0);
            }
        });
        jFrame.pack();
        jFrame.setVisible(true);
    }

    public JuliaMain() {
        int n = 300;
        int n2 = 300;
        this.defcol1 = -16776961;
        this.defcol2 = -256;
        this.defcol3 = -65536;
        this.defcol1_j = -16776961;
        this.defcol2_j = -256;
        this.defcol3_j = -65536;
        this.gcd = new GCD_Mandelbrot(n, n2, -2.0, 1.0, -1.5, 1.5, "cr", "ci", 6, 6, this);
        this.gcd.setcols(this.defcol1, this.defcol2, this.defcol3);
        this.panel1 = new Panel();
        this.panel1.setLayout((LayoutManager)new BorderLayout());
        this.repaint = new Button("  Repaint  ");
        this.cancel = new Button("Cancel");
        this.repaint.addActionListener((ActionListener)new MBButtonListener(this, 1));
        this.cancel.addActionListener((ActionListener)new MBButtonListener(this, 2));
        this.bar = new Scrollbar(1, this.gcd.getTMAX(), 500, 100, 5500);
        this.bar.addAdjustmentListener((AdjustmentListener)new MBBarListener(this, 0));
        this.refresh = new Button("Resize");
        this.refresh.addActionListener((ActionListener)new MBButtonListener(this, 0));
        this.colchoice = new Choice();
        this.colchoice.addItem("Default");
        this.colchoice.addItem("Snowy");
        this.colchoice.addItem("Pink");
        this.colchoice.addItem("Matrix");
        this.colchoice.addItemListener((ItemListener)new MandelItemListener(this));
        this.npanel = new NPanel(this.gcd.getTMAX());
        this.panel1.add("North", (Component)this.repaint);
        this.panel1.add("South", (Component)this.refresh);
        this.panel1.add("East", (Component)this.bar);
        this.panel1.add("Center", (Component)this.npanel);
        this.panel0 = new Panel();
        this.panel0.setLayout((LayoutManager)new BorderLayout());
        this.panel0.add("North", (Component)this.colchoice);
        this.panel0.add("Center", (Component)this.panel1);
        this.panel0.add("South", (Component)this.cancel);
        this.panelall = new Panel();
        this.panelall.setLayout((LayoutManager)new BorderLayout());
        this.panelall.add("Center", (Component)this.gcd);
        this.panelall.add("West", (Component)this.panel0);
        this.gcd_j = new GCD_Julia(n, n2, -2.0, 2.0, -2.0, 2.0, "x", "y", 6, 6);
        this.gcd_j.setcols(this.defcol1_j, this.defcol2_j, this.defcol3_j);
        this.panel1_j = new Panel();
        this.panel1_j.setLayout((LayoutManager)new BorderLayout());
        this.repaint_j = new Button("  Repaint  ");
        this.cancel_j = new Button("Cancel");
        this.repaint_j.addActionListener((ActionListener)new JuliaButtonListener(this, 1));
        this.cancel_j.addActionListener((ActionListener)new JuliaButtonListener(this, 2));
        this.bar_j = new Scrollbar(1, this.gcd_j.getTMAX(), 500, 100, 5500);
        this.bar_j.addAdjustmentListener((AdjustmentListener)new JuliaBarListener(this, 0));
        this.refresh_j = new Button("Resize");
        this.refresh_j.addActionListener((ActionListener)new JuliaButtonListener(this, 0));
        this.colchoice_j = new Choice();
        this.colchoice_j.addItem("Default");
        this.colchoice_j.addItem("Snowy");
        this.colchoice_j.addItem("Pink");
        this.colchoice_j.addItem("Matrix");
        this.colchoice_j.addItemListener((ItemListener)new JuliaItemListener(this));
        this.npanel_j = new NPanel_j(this.gcd_j.getTMAX(), this.gcd_j.getcr(), this.gcd_j.getci());
        this.panel1_j.add("North", (Component)this.repaint_j);
        this.panel1_j.add("South", (Component)this.refresh_j);
        this.panel1_j.add("East", (Component)this.bar_j);
        this.panel1_j.add("Center", (Component)this.npanel_j);
        this.panel0_j = new Panel();
        this.panel0_j.setLayout((LayoutManager)new BorderLayout());
        this.panel0_j.add("North", (Component)this.colchoice_j);
        this.panel0_j.add("Center", (Component)this.panel1_j);
        this.panel0_j.add("South", (Component)this.cancel_j);
        this.panelall_j = new Panel();
        this.panelall_j.setLayout((LayoutManager)new BorderLayout());
        this.panelall_j.add("Center", (Component)this.gcd_j);
        this.panelall_j.add("East", (Component)this.panel0_j);
        this.setLayout((LayoutManager)new GridLayout(1, 0));
        this.add((Component)this.panelall);
        this.add((Component)this.panelall_j);
    }

    public void paint(Graphics graphics) {
    }

    public void stop() {
        if (this.gcd.threadActive) {
            this.gcd.cancelCalc();
        }
        if (!this.gcd_j.threadActive) return;
        this.gcd_j.cancelCalc();
    }

}
