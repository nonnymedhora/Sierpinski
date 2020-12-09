package kanamaru;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class MandelItemListener implements ItemListener {
    JuliaMain man;

    public MandelItemListener(JuliaMain juliaMain) {
        this.man = juliaMain;
    }

    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() != 1) return;
        if (itemEvent.getItem().equals((Object)"Default")) {
            int n = this.man.defcol1;
            int n2 = this.man.defcol2;
            int n3 = this.man.defcol3;
            this.man.gcd.setcols(n, n2, n3);
        } else if (itemEvent.getItem().equals((Object)"Snowy")) {
            int n = -16776961;
            int n4 = -1;
            int n5 = -5592321;
            this.man.gcd.setcols(n, n4, n5);
        } else if (itemEvent.getItem().equals((Object)"Pink")) {
            int n = -65536;
            int n6 = -1;
            int n7 = -21846;
            this.man.gcd.setcols(n, n6, n7);
        } else {
            if (!itemEvent.getItem().equals((Object)"Matrix")) return;
            int n = -16711936;
            int n8 = -16755456;
            int n9 = -16777216;
            this.man.gcd.setcols(n, n8, n9);
        }
    }
}
