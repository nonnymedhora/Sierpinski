package kanamaru;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JuliaButtonListener implements ActionListener {
    JuliaMain mb;
    int ButtonID;

    public JuliaButtonListener(JuliaMain juliaMain, int n) {
        this.mb = juliaMain;
        this.ButtonID = n;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (this.ButtonID == 0) {
            if (this.mb.gcd_j.threadActive) return;
            this.mb.gcd_j.setup();
        } else if (this.ButtonID == 1) {
            if (this.mb.gcd_j.threadActive) return;
            this.mb.gcd_j.setup_partial();
        } else {
            if (!this.mb.gcd_j.threadActive) return;
            this.mb.gcd_j.cancelCalc();
            this.mb.gcd_j.repaint();
        }
    }
}
