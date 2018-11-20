package comt.leo.picker.testline.release.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class AtmanRelation implements Serializable {
    private int group;//我的id
    private String userId;//
    private String lable;
    private String headerUrl;
    private int degree;//当前第几度关系
    private int sonCount;
    private String nickName;
    private ArrayList<RelationParent> parentGroups;


    private int x_center;//当前点的x中心点
    private int y_center;//当前点的y中心点
    private RectPoint rectPoint;//当前点所在的区域。用于放置图片证据的时候进行判断不覆盖



    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
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

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ArrayList<RelationParent> getParentGroups() {
        return parentGroups;
    }

    public void setParentGroups(ArrayList<RelationParent> parentGroups) {
        this.parentGroups = parentGroups;
    }
}
