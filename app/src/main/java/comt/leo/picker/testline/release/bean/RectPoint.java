package comt.leo.picker.testline.release.bean;

public class RectPoint {
    //左上角的点
    private int left_top_y;
    private int left_x;



    //右下角的点
    private int rigth_x;
    private int right_bottom_y;

    public RectPoint() {
    }

    public RectPoint(int left_x, int left_top_y, int rigth_x, int right_bottom_y) {
        this.left_x = left_x;
        this.left_top_y = left_top_y;
        this.rigth_x = rigth_x;
        this.right_bottom_y = right_bottom_y;
    }


    public int getLeft_top_y() {
        return left_top_y;
    }

    public void setLeft_top_y(int left_top_y) {
        this.left_top_y = left_top_y;
    }

    public int getLeft_x() {
        return left_x;
    }

    public void setLeft_x(int left_x) {
        this.left_x = left_x;
    }

    public int getRigth_x() {
        return rigth_x;
    }

    public void setRigth_x(int rigth_x) {
        this.rigth_x = rigth_x;
    }

    public int getRight_bottom_y() {
        return right_bottom_y;
    }

    public void setRight_bottom_y(int right_bottom_y) {
        this.right_bottom_y = right_bottom_y;
    }
}
