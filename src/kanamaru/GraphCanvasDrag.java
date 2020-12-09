package kanamaru;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;

public class GraphCanvasDrag extends GraphCanvas implements MouseMotionListener, MouseListener {
	private static final long serialVersionUID = 124L;
	
	double xminold;
    double xmaxold;
    double yminold;
    double ymaxold;
    int xstart_pxl;
    int xstop_pxl;
    int xnow_pxl;
    int ystart_pxl;
    int ystop_pxl;
    int ynow_pxl;
    Color rectColor = Color.red;
    boolean isDragging = false;

    public GraphCanvasDrag(int n, int n2, double d, double d2, double d3, double d4, String string, String string2, int n3, int n4) {
        super(n, n2, d, d2, d3, d4, string, string2, n3, n4);
        this.addMouseMotionListener((MouseMotionListener)this);
        this.addMouseListener((MouseListener)this);
    }

    public boolean inArea(int n, int n2) {
        if (n < this.yaxispos || n >= this.mywidth || n2 < 0 || n2 >= this.ypix) return false;
        return true;
    }

    public boolean inAreaPix(int n, int n2) {
        if (n < 0 || n >= this.xpix || n2 < 0 || n2 >= this.ypix) return false;
        return true;
    }

    public void myMouseUPEvent() {
        this.xminold = this.xmin;
        this.xmaxold = this.xmax;
        this.yminold = this.ymin;
        this.ymaxold = this.ymax;
        this.pix2xminmax(this.xstart_pxl, this.xstop_pxl);
        this.pix2yminmax(this.ystart_pxl, this.ystop_pxl);
        this.drawAll();
    }

    public void pix2xminmax(int n, int n2) {
        int n3;
        int n4;
        if (n2 < n) {
            n4 = n2;
            n3 = n;
        } else {
            n4 = n;
            n3 = n2;
        }
        if (n4 < this.yaxispos) {
            n4 = this.yaxispos;
        }
        if (n3 > this.mywidth - 1) {
            n3 = this.mywidth - 1;
        }
        double d = this.pix2x(n4 - this.yaxispos);
        double d2 = this.pix2x(n3 - this.yaxispos);
        this.xmin = d;
        this.xmax = d2;
    }

    public void pix2yminmax(int n, int n2) {
        int n3;
        int n4;
        if (n < n2) {
            n4 = n;
            n3 = n2;
        } else {
            n3 = n;
            n4 = n2;
        }
        if (n3 > this.myheight - 12 - 1) {
            n3 = this.myheight - 12 - 1;
        }
        if (n4 < 0) {
            n4 = 0;
        }
        double d = this.pix2y(n3);
        double d2 = this.pix2y(n4);
        this.ymin = d;
        this.ymax = d2;
    }

    void mydrawRect(Graphics graphics) {
        int n;
        int n2;
        int n3;
        int n4;
        if (this.xstart_pxl < this.xnow_pxl) {
            n = this.xstart_pxl;
            n2 = this.xnow_pxl;
        } else {
            n = this.xnow_pxl;
            n2 = this.xstart_pxl;
        }
        if (this.ystart_pxl < this.ynow_pxl) {
            n3 = this.ystart_pxl;
            n4 = this.ynow_pxl;
        } else {
            n3 = this.ynow_pxl;
            n4 = this.ystart_pxl;
        }
        if (n < this.yaxispos) {
            n = this.yaxispos;
        }
        if (n2 > this.mywidth - 1) {
            n2 = this.mywidth - 1;
        }
        if (n3 < 0) {
            n3 = 0;
        }
        if (n4 > this.myheight - 1 - 12) {
            n4 = this.myheight - 1 - 12;
        }
        graphics.drawImage(this.offimageALL, 0, 0, (ImageObserver)this);
        graphics.setColor(this.rectColor);
        graphics.drawRect(n, n3, n2 - n, n4 - n3);
    }

    public void setrectColor(Color color) {
        this.rectColor = color;
    }

    public void mouseClicked(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (!this.inArea(mouseEvent.getX(), mouseEvent.getY())) return;
        this.xstart_pxl = mouseEvent.getX();
        this.ystart_pxl = mouseEvent.getY();
        this.isDragging = true;
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        if (!this.isDragging) return;
        this.xstop_pxl = mouseEvent.getX();
        this.ystop_pxl = mouseEvent.getY();
        if (this.ystop_pxl == this.ystart_pxl || this.xstart_pxl == this.xstop_pxl) {
            this.isDragging = false;
            return;
        }
        this.myMouseUPEvent();
        this.isDragging = false;
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        if (!this.isDragging) return;
        this.xnow_pxl = mouseEvent.getX();
        this.ynow_pxl = mouseEvent.getY();
        this.mydrawRect(this.getGraphics());
    }

    public void mouseMoved(MouseEvent mouseEvent) {
    }
}
