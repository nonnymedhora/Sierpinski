package kanamaru;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.awt.image.MemoryImageSource;

public class GraphCanvas extends Canvas {
	private static final long serialVersionUID = 187654L;
	
	double xminorg;
    double xmaxorg;
    double yminorg;
    double ymaxorg;
    double xmin;
    double xmax;
    double ymin;
    double ymax;
    String xlabel;
    String ylabel;
    int xdigits;
    int ydigits;
    int yaxispos;
    Image piximage;
    Image offimageALL;
    int xpix;
    int ypix;
    int mywidth;
    int myheight;
    boolean isFirst = true;
    int[] pixel;
    int pixelnum;
    static final int charwidth = 8;
    static final int charheight = 12;
    public static final int xaxispos = 12;
    MemoryImageSource source;

    public GraphCanvas(int n, int n2, double d, double d2, double d3, double d4, String string, String string2, int n3, int n4) {
        this.xpix = n;
        this.ypix = n2;
        this.xminorg = d;
        this.xmaxorg = d2;
        this.yminorg = d3;
        this.ymaxorg = d4;
        this.xmin = this.xminorg;
        this.xmax = this.xmaxorg;
        this.ymin = this.yminorg;
        this.ymax = this.ymaxorg;
        this.xlabel = string;
        this.ylabel = string2;
        this.xdigits = n3;
        this.ydigits = n4;
        this.yaxispos = 8 * (n4 + 3);
        this.pixelnum = this.xpix * this.ypix;
        this.mywidth = this.xpix + this.yaxispos;
        this.myheight = this.ypix + 12;
        this.setBackground(Color.white);
    }

    public void paint(Graphics graphics) {
        if (this.isFirst) {
            this.isFirst = false;
            this.setup();
        } else {
            graphics.drawImage(this.offimageALL, 0, 0, (ImageObserver)this);
        }
    }

    public void update(Graphics graphics) {
        this.paint(graphics);
    }

    public void setup() {
        this.xmin = this.xminorg;
        this.xmax = this.xmaxorg;
        this.ymin = this.yminorg;
        this.ymax = this.ymaxorg;
        this.drawAll();
    }

    public void pixeldraw() {
        if (this.piximage != null) {
            this.piximage = null;
        }
        if (this.pixel == null) {
            this.pixel = new int[this.pixelnum];
            for (int i = 0; i < this.pixelnum; ++i) {
                this.pixel[i] = -1;
            }
        }
        this.ImageSet();
        this.source = null;
        this.source = new MemoryImageSource(this.xpix, this.ypix, this.pixel, 0, this.xpix);
        this.piximage = this.createImage((ImageProducer)this.source);
    }

    public void drawAll() {
        this.pixeldraw();
        this.offimageALL = this.createImage(this.mywidth, this.myheight);
        Graphics graphics = this.offimageALL.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, this.mywidth, this.myheight);
        this.drawAxis(graphics);
        graphics.drawImage(this.piximage, this.yaxispos, 0, (ImageObserver)this);
        this.getGraphics().drawImage(this.offimageALL, 0, 0, (ImageObserver)this);
    }

    public double pix2x(int n) {
        return this.xmin + (this.xmax - this.xmin) * (double)n / (double)(this.xpix - 1);
    }

    public double pix2y(int n) {
        return (this.ymax - this.ymin) * (double)n / (double)(1 - this.ypix) + this.ymax;
    }

    public int x2pix(double d) {
        return (int)((double)(this.xpix - 1) * (d - this.xmin) / (this.xmax - this.xmin));
    }

    public int y2pix(double d) {
        return (int)((double)(1 - this.ypix) * (d - this.ymax) / (this.ymax - this.ymin));
    }

    public void drawAxis(Graphics graphics) {
        int n = this.intpower(10, this.xdigits);
        int n2 = this.intpower(10, this.ydigits);
        int n3 = (int)(this.xmin * (double)n);
        int n4 = (int)(this.xmax * (double)n);
        float f = (float)n3 / (float)n;
        float f2 = (float)n4 / (float)n;
        String string = "" + f;
        String string2 = "" + f2;
        graphics.setColor(Color.black);
        graphics.drawLine(0, this.myheight - 12, this.mywidth - 1, this.myheight - 12);
        graphics.drawString(string, this.yaxispos + 8, this.myheight - 1);
        graphics.drawString(this.xlabel, this.yaxispos + (this.mywidth - this.yaxispos) / 2 - this.xlabel.length() * 8 / 2, this.myheight - 1);
        graphics.drawString(string2, this.mywidth - string2.length() * 8, this.myheight - 1);
        n3 = (int)(this.ymin * (double)n2);
        n4 = (int)(this.ymax * (double)n2);
        f = (float)n3 / (float)n2;
        f2 = (float)n4 / (float)n2;
        String string3 = "" + f;
        String string4 = "" + f2;
        graphics.drawLine(this.yaxispos - 1, 0, this.yaxispos - 1, this.myheight - 1);
        graphics.drawString(string4, 0, 12);
        graphics.drawString(this.ylabel, 0, (int)((double)this.myheight / 2.0));
        graphics.drawString(string3, 0, this.myheight - 12);
    }

    public int intpower(int n, int n2) {
        int n3 = 1;
        for (int i = 0; i < n2; ++i) {
            n3*=n;
        }
        return n3;
    }

    public void ImageSet() {
    }

    public void pixdrawLine(int n, int n2, int n3, int n4, int n5) {
        int n6;
        int n7;
        int n8;
        int n9;
        if (n < n3) {
            n9 = n;
            n6 = n3;
        } else {
            n9 = n3;
            n6 = n;
        }
        if (n2 < n4) {
            n8 = n2;
            n7 = n4;
        } else {
            n8 = n4;
            n7 = n2;
        }
        if (n9 > this.xpix - 1 || n8 > this.ypix - 1 || n6 < 0 || n7 < 0) {
            return;
        }
        if (this.lineSign(n, n2, n3, n4, 0, 0) > 0 && this.lineSign(n, n2, n3, n4, this.xpix - 1, 0) > 0 && this.lineSign(n, n2, n3, n4, 0, this.ypix - 1) > 0 && this.lineSign(n, n2, n3, n4, this.xpix - 1, this.ypix - 1) > 0) {
            return;
        }
        if (this.lineSign(n, n2, n3, n4, 0, 0) < 0 && this.lineSign(n, n2, n3, n4, this.xpix - 1, 0) < 0 && this.lineSign(n, n2, n3, n4, 0, this.ypix - 1) < 0 && this.lineSign(n, n2, n3, n4, this.xpix - 1, this.ypix - 1) < 0) {
            return;
        }
        if (n8 < 0) {
            n8 = 0;
        }
        if (n7 > this.ypix - 1) {
            n7 = this.ypix - 1;
        }
        if (n9 < 0) {
            n9 = 0;
        }
        if (n6 > this.xpix - 1) {
            n6 = this.xpix - 1;
        }
        if (n9 == n6) {
            for (int i = n8; i <= n7; ++i) {
                this.pixel[i * this.xpix + n9] = n5;
            }
            return;
        }
        if (n8 == n7) {
            for (int i = n9; i <= n6; ++i) {
                this.pixel[n8 * this.xpix + i] = n5;
            }
            return;
        }
        for (int i = n9; i <= n6; ++i) {
            int n10;
            int n11 = (int)((double)(n4 - n2) * (double)(i - n) / (double)(n3 - n) + (double)n2);
            if ((n10 = (int)Math.abs((double)((double)(n4 - n2) / (double)(n3 - n)))) < 2) {
                n10 = 2;
            }
            for (int j = n11 - n10; j <= n11 + n10; ++j) {
                double d;
                if (j < n8) continue;
                if (j > n7) continue;
                double d2 = (double)(n4 - n2) * (double)(i - n) / (double)(n3 - n) + (double)n2;
                if (((double)i < (d = (double)(n3 - n) * (double)(j - n2) / (double)(n4 - n2) + (double)n) || (double)i - d > 0.5) && ((double)i > d || d - (double)i >= 0.5) && ((double)j < d2 || (double)j - d2 > 0.5) && ((double)j > d2 || d2 - (double)j >= 0.5)) continue;
                this.pixel[j * this.xpix + i] = n5;
            }
        }
    }

    public int lineSign(int n, int n2, int n3, int n4, int n5, int n6) {
        int n7 = (n4 - n2) * (n5 - n) - (n3 - n) * (n6 - n2);
        if (n7 > 0) {
            return 1;
        }
        if (n7 >= 0) return 0;
        return -1;
    }

    public void pixdrawDot(int n, int n2, int n3) {
        this.pixel[n2 * this.xpix + n] = n3;
    }
}
