/**
 * 
 */
package org.bawaweb.ui.sierpinkski;

import javax.swing.JApplet;

public class SierpinskiCompositional_Web extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SierpinskiComboPanel sierpinskiComboPanel;// = new SierpinskiComboPanel();
	static SierpinskiCompositional_Web applet;

	public void init() {
		applet = this;
		/*String media = "./audio";
		String param = null;
		if ((param = getParameter("dir")) != null) {
			media = param;
		} */
		getContentPane().add("Center", sierpinskiComboPanel = new SierpinskiComboPanel());
	}

	public void start() {
	}

	public void stop() {
	}

	public void destroy() {
		applet = null;
		sierpinskiComboPanel = null;
	}
      

}
