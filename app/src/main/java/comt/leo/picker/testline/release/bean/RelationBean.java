package comt.leo.picker.testline.release.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class RelationBean implements Serializable {
    private int circle;//半径
    private int numberNex;//这层关系有几个
    private int X1;//当前的中心 点
    private int Y1;//当前的中心点
    private int trueJD;//真正的角度
    private int relation;//当前是 第几度关系
    private ArrayList<AtmanRelation> atmanRelations;

    public RelationBean(int circle, int numberNex, int x1, int y1, int trueJD, int relation, ArrayList<AtmanRelation> atmanRelations) {
        this.circle = circle;
        this.numberNex = numberNex;
        X1 = x1;
        Y1 = y1;
        this.trueJD = trueJD;
        this.relation = relation;
        this.atmanRelations = atmanRelations;
    }

    public int getCircle() {
        return circle;
    }

    public void setCircle(int circle) {
        this.circle = circle;
    }

    public int getNumberNex() {
        return numberNex;
    }

    public void setNumberNex(int numberNex) {
        this.numberNex = numberNex;
    }

    public int getX1() {
        return X1;
    }

    public void setX1(int x1) {
        X1 = x1;
    }

    public int getY1() {
        return Y1;
    }

    public void setY1(int y1) {
        Y1 = y1;
    }

    public int getTrueJD() {
        return trueJD;
    }

    public void setTrueJD(int trueJD) {
        this.trueJD = trueJD;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public ArrayList<AtmanRelation> getAtmanRelations() {
        return atmanRelations;
    }

    public void setAtmanRelations(ArrayList<AtmanRelation> atmanRelations) {
        this.atmanRelations = atmanRelations;
    }
}
