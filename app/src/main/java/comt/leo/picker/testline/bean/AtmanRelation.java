package comt.leo.picker.testline.bean;

import java.io.Serializable;

public class AtmanRelation implements Serializable {
    private int nodePar;//父类id
    private int degree;//当前第几度关系
    private int node;//我的id
    private String avatar;//头像url
    private int sonCount;//子类关系 有几个

    private int x_center;//当前点的x中心点
    private int y_center;//当前点的y中心点
    private RectPoint rectPoint;//当前点所在的区域。用于放置图片证据的时候进行判断不覆盖

    public int getNodePar() {
        return nodePar;
    }

    public void setNodePar(int nodePar) {
        this.nodePar = nodePar;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSonCount() {
        return sonCount;
    }

    public void setSonCount(int sonCount) {
        this.sonCount = sonCount;
    }

    public int getX_center() {
        return x_center;
    }

    public void setX_center(int x_center) {
        this.x_center = x_center;
    }

    public int getY_center() {
        return y_center;
    }

    public void setY_center(int y_center) {
        this.y_center = y_center;
    }

    public RectPoint getRectPoint() {
        return rectPoint;
    }

    public void setRectPoint(RectPoint rectPoint) {
        this.rectPoint = rectPoint;
    }
}
