package kanamaru;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MBButtonListener implements ActionListener {
    JuliaMain mb;
    int ButtonID;

    public MBButtonListener(JuliaMain juliaMain, int n) {
        this.mb = juliaMain;
        this.ButtonID = n;
    }

    public void actionPerformed(ActionEvent actionEvent) {
        if (this.ButtonID == 0) {
            if (this.mb.gcd.threadActive) return;
            this.mb.gcd.setup();
        } else if (this.ButtonID == 1) {
            if (this.mb.gcd.threadActive) return;
            this.mb.gcd.setup_partial();
        } else {
            if (!this.mb.gcd.threadActive) return;
            this.mb.gcd.cancelCalc();
            this.mb.gcd.repaint();
        }
    }
}
