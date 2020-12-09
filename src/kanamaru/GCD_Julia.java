package kanamaru;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;

public class GCD_Julia extends GraphCanvasDrag implements Runnable {
    double x;
    double y;
    int TMAX;
    int TMAXnew;
    static final int MAX = 20;
    Thread rbthread;
    boolean threadActive;
    boolean shouldStop;
    int col1;
    int col2;
    int col3;
    double cr;
    double ci;

    public GCD_Julia(int n, int n2, double d, double d2, double d3, double d4, String string, String string2, int n3, int n4) {
        super(n, n2, d, d2, d3, d4, string, string2, n3, n4);
        this.TMAXnew = this.TMAX = 500;
        this.threadActive = false;
        this.shouldStop = false;
        this.col1 = -16776961;
        this.col2 = -256;
        this.col3 = -65536;
        this.setrectColor(Color.white);
        this.cr = 0.369166666;
        this.ci = 0.339166666;
    }

    public void setup() {
        this.xminold = this.xmin;
        this.xmaxold = this.xmax;
        this.yminold = this.ymin;
        this.ymaxold = this.ymax;
        this.xmin = this.xminorg;
        this.xmax = this.xmaxorg;
        this.ymin = this.yminorg;
        this.ymax = this.ymaxorg;
        if (this.offimageALL == null) {
            this.offimageALL = this.createImage(this.mywidth, this.myheight);
            Graphics graphics = this.offimageALL.getGraphics();
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, this.mywidth, this.myheight);
            this.drawAxis(graphics);
        }
        this.setup_partial();
    }

    public void setup_partial() {
        this.rbthread = new Thread((Runnable)this);
        this.threadActive = true;
        this.rbthread.start();
    }

    public void run() {
        this.drawAll();
        this.shouldStop = false;
        this.threadActive = false;
    }

    public void drawAll() {
        this.pixeldraw();
        if (!this.threadActive) return;
        this.offimageALL = this.createImage(this.mywidth, this.myheight);
        Graphics graphics = this.offimageALL.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, this.mywidth, this.myheight);
        this.drawAxis(graphics);
        graphics.drawImage(this.piximage, this.yaxispos, 0, (ImageObserver)this);
        this.getGraphics().drawImage(this.offimageALL, 0, 0, (ImageObserver)this);
    }

    public void ImageSet() {
        this.TMAX = this.TMAXnew;
        int n = 0;
        for (int i = 0; i < this.ypix; ++i) {
            Graphics object;
            String var3_3;
            boolean bl = this.mypercent(i - 1) % 10 == 9;
            boolean bl2 = this.mypercent(i) % 10 == 0;
            if (bl & bl2) {
                this.getGraphics().drawImage(this.offimageALL, 0, 0, (ImageObserver)this);
                try {
                    Thread.sleep((long)10);
                }
                catch (InterruptedException e) {
                	e.printStackTrace();
//                    InterruptedException interruptedException;
//                    var3_3 = (reference)interruptedException;
                }
                var3_3 = "" + this.mypercent(i) + "%";
                object = (Graphics)this.getGraphics();
                object.setColor(Color.red);
                object.drawString((String)var3_3, 16, (int)((double)this.myheight / 2.0));
                try {
                    Thread.sleep((long)10);
                }
                catch (InterruptedException var5_5) {
                    // empty catch block
                }
            }
            for (int v3 = 0; v3 < this.xpix; ++v3) {
                this.x = this.pix2x((int)v3);
                this.y = this.pix2y(i);
//                this.pixel[n++] = (object = this.blowout_num(this.x, this.y)) < 0 ? -16777216 : this.time2Color((int)object);
                this.pixel[n++] = (this.blowout_num(this.x, this.y)) < 0 ? -16777216 : this.time2Color(this.blowout_num(this.x, this.y));
                if (!this.shouldStop) continue;
                this.shouldStop = false;
                this.threadActive = false;
                return;
            }
        }
    }

    int blowout_num(double d, double d2) {
        double d3 = d;
        double d4 = d2;
        int n = 0;
        for (int i = 0; i < this.TMAX; ++i) {
            double d5 = d3 * d3 - d4 * d4 + this.cr;
            d4 = 2.0 * d3 * d4 + this.ci;
            d3 = d5;
            ++n;
            if ((int)d3 > 20) break;
            if ((int)d3 < -20) break;
        }
        if (n != this.TMAX) return n;
        return -1;
    }

    public void setTMAX(int n) {
        this.TMAXnew = n;
    }

    public int getTMAX() {
        return this.TMAX;
    }

    public double getcr() {
        return this.cr;
    }

    public double getci() {
        return this.ci;
    }

    public void setc(double d, double d2) {
        this.cr = d;
        this.ci = d2;
    }

    public void setcols(int n, int n2, int n3) {
        this.col1 = n;
        this.col2 = n2;
        this.col3 = n3;
    }

    public int time2Color(int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        int n10;
        int n11 = 5;
        int n12 = 30;
        int n13 = 55;
        int n14 = 80;
        double d = 1.0;
        n11 = (int)(d * (double)n11);
        n12 = (int)(d * (double)n12);
        n13 = (int)(d * (double)n13);
        n14 = (int)(d * (double)n14);
        int n15 = n;
        int n16 = -1;
        if (n < n11) {
            n15 = n;
            int n17 = (this.col1 & 16711680) >> 16;
            int n18 = (this.col1 & 65280) >> 8;
            int n19 = this.col1 & 255;
            n17 = n17 * n15 / n11;
            n18 = n18 * n15 / n11;
            n19 = n19 * n15 / n11;
            n16 = -16777216 | n17 << 16 | n18 << 8 | n19;
            return n16;
        }
        if ((n15 = (n - n11) % (n14 - n11) + n11) < n12) {
            n10 = (this.col1 & 16711680) >> 16;
            n7 = (this.col1 & 65280) >> 8;
            n8 = this.col1 & 255;
            n10 = n10 * (n12 - n15) / (n12 - n11);
            n7 = n7 * (n12 - n15) / (n12 - n11);
            n8 = n8 * (n12 - n15) / (n12 - n11);
        } else if (n15 < n13) {
            n10 = 0;
            n7 = 0;
            n8 = 0;
        } else {
            n10 = (this.col1 & 16711680) >> 16;
            n7 = (this.col1 & 65280) >> 8;
            n8 = this.col1 & 255;
            n10 = n10 * (n15 - n13) / (n14 - n13);
            n7 = n7 * (n15 - n13) / (n14 - n13);
            n8 = n8 * (n15 - n13) / (n14 - n13);
        }
        if (n15 < n12) {
            n6 = (this.col2 & 16711680) >> 16;
            n9 = (this.col2 & 65280) >> 8;
            n5 = this.col2 & 255;
            n6 = n6 * (n15 - n11) / (n12 - n11);
            n9 = n9 * (n15 - n11) / (n12 - n11);
            n5 = n5 * (n15 - n11) / (n12 - n11);
        } else if (n15 < n13) {
            n6 = (this.col2 & 16711680) >> 16;
            n9 = (this.col2 & 65280) >> 8;
            n5 = this.col2 & 255;
            n6 = n6 * (n13 - n15) / (n13 - n12);
            n9 = n9 * (n13 - n15) / (n13 - n12);
            n5 = n5 * (n13 - n15) / (n13 - n12);
        } else {
            n6 = 0;
            n9 = 0;
            n5 = 0;
        }
        if (n15 < n12) {
            n2 = 0;
            n3 = 0;
            n4 = 0;
        } else if (n15 < n13) {
            n2 = (this.col3 & 16711680) >> 16;
            n3 = (this.col3 & 65280) >> 8;
            n4 = this.col3 & 255;
            n2 = n2 * (n15 - n12) / (n13 - n12);
            n3 = n3 * (n15 - n12) / (n13 - n12);
            n4 = n4 * (n15 - n12) / (n13 - n12);
        } else {
            n2 = (this.col3 & 16711680) >> 16;
            n3 = (this.col3 & 65280) >> 8;
            n4 = this.col3 & 255;
            n2 = n2 * (n14 - n15) / (n14 - n13);
            n3 = n3 * (n14 - n15) / (n14 - n13);
            n4 = n4 * (n14 - n15) / (n14 - n13);
        }
        n16 = -16777216 | this.sum3colors(n10, n6, n2) << 16 | this.sum3colors(n7, n9, n3) << 8 | this.sum3colors(n8, n5, n4);
        return n16;
    }

    public int sum3colors(int n, int n2, int n3) {
        int n4 = n + n2 + n3;
        if (n4 >= 256) return 255;
        return n4;
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (this.threadActive) {
            return;
        }
        super.mousePressed(mouseEvent);
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (this.threadActive) {
            return;
        }
        super.mouseReleased(mouseEvent);
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (this.threadActive) {
            return;
        }
        super.mouseDragged(mouseEvent);
    }

    public void myMouseUPEvent() {
        this.xminold = this.xmin;
        this.xmaxold = this.xmax;
        this.yminold = this.ymin;
        this.ymaxold = this.ymax;
        this.pix2xminmax(this.xstart_pxl, this.xstop_pxl);
        this.pix2yminmax(this.ystart_pxl, this.ystop_pxl);
        if (this.rbthread != null) {
            this.rbthread = null;
        }
        this.rbthread = new Thread((Runnable)this);
        this.rbthread.start();
        this.threadActive = true;
    }

    public int mypercent(int n) {
        return 100 * n / this.xpix;
    }

    public void cancelCalc() {
        this.shouldStop = true;
        this.xmin = this.xminold;
        this.xmax = this.xmaxold;
        this.ymin = this.yminold;
        this.ymax = this.ymaxold;
    }
}
