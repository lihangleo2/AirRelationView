package comt.leo.picker.testline.release.bean;

import java.io.Serializable;

public class CanScrollBean implements Serializable {
    //左上角的点
    private PointScroll left_top_y;
    private PointScroll left_x;



    //右下角的点
    private PointScroll rigth_x;
    private PointScroll right_bottom_y;

    public PointScroll getLeft_top_y() {
        return left_top_y;
    }

    public void setLeft_top_y(PointScroll left_top_y) {
        this.left_top_y = left_top_y;
    }

    public PointScroll getLeft_x() {
        return left_x;
    }

    public void setLeft_x(PointScroll left_x) {
        this.left_x = left_x;
    }

    public PointScroll getRigth_x() {
        return rigth_x;
    }

    public void setRigth_x(PointScroll rigth_x) {
        this.rigth_x = rigth_x;
    }

    public PointScroll getRight_bottom_y() {
        return right_bottom_y;
    }

    public void setRight_bottom_y(PointScroll right_bottom_y) {
        this.right_bottom_y = right_bottom_y;
    }
}
