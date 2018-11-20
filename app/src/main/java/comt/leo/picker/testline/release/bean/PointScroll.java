package comt.leo.picker.testline.release.bean;

import java.io.Serializable;

public class PointScroll implements Serializable {
    private int X;
    private int Y;

    public PointScroll(int x, int y) {
        X = x;
        Y = y;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }
}
