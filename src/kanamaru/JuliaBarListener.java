package kanamaru;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class JuliaBarListener implements AdjustmentListener {
    JuliaMain mapp;
    int BarID;

    public JuliaBarListener(JuliaMain juliaMain, int n) {
        this.mapp = juliaMain;
        this.BarID = n;
    }

    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        if (this.BarID != 0) return;
        this.mapp.npanel_j.setN(this.mapp.bar_j.getValue());
        this.mapp.npanel_j.repaint();
        this.mapp.gcd_j.setTMAX(this.mapp.bar_j.getValue());
    }
}
