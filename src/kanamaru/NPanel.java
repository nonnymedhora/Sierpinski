package kanamaru;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Panel;

public class NPanel extends Panel {
	private static final long serialVersionUID = 1342L;
	private int N;

    public NPanel(int n) {
        this.N = n;
        this.setBackground(Color.black);
    }

    public void paint(Graphics graphics) {
        graphics.setColor(Color.white);
        graphics.drawString("TMAX", 10, 20);
        graphics.drawString("" + this.N, 10, 40);
    }

    public void setN(int n) {
        this.N = n;
    }
}
