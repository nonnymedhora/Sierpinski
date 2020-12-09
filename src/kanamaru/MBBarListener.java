package kanamaru;

import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public class MBBarListener implements AdjustmentListener {
	
    JuliaMain mapp;
    int BarID;

    public MBBarListener(JuliaMain juliaMain, int n) {
        this.mapp = juliaMain;
        this.BarID = n;
    }

    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        if (this.BarID != 0) return;
        this.mapp.npanel.setN(this.mapp.bar.getValue());
        this.mapp.npanel.repaint();
        this.mapp.gcd.setTMAX(this.mapp.bar.getValue());
    }
}
