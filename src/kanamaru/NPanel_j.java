package kanamaru;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Panel;

public class NPanel_j extends Panel {
	private static final long serialVersionUID = 1874L;
	private int N;
    double cr;
    double ci;

    public NPanel_j(int n, double d, double d2) {
        this.N = n;
        this.cr = d;
        this.ci = d2;
        this.setBackground(Color.black);
    }

    public void paint(Graphics graphics) {
        double d = (double)((int)(this.cr * 10000.0)) / 10000.0;
        double d2 = (double)((int)(this.ci * 10000.0)) / 10000.0;
        graphics.setColor(Color.white);
        graphics.drawString("TMAX", 10, 20);
        graphics.drawString("" + this.N, 10, 40);
        graphics.drawString("cr=" + d, 10, 60);
        graphics.drawString("ci=" + d2, 10, 80);
    }

    public void setN(int n) {
        this.N = n;
    }

    public void setc(double d, double d2) {
        this.cr = d;
        this.ci = d2;
    }
}
