package kanamaru;

import java.awt.Component;
import java.awt.HeadlessException;

import javax.swing.JApplet;

public class JuliaApp extends JApplet {
	private static final long serialVersionUID = 4737342790012552761L;
	JuliaMain m;

    public void init() {
        this.m = new JuliaMain();
        this.getContentPane().add((Component)this.m);
    }
}
