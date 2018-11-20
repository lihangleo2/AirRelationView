package comt.leo.picker.testline.release.bean;

import java.io.Serializable;

public class RelationParent implements Serializable {
    private int group;//父类的id
    private int pictureCount;//与父类的证据
    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(int pictureCount) {
        this.pictureCount = pictureCount;
    }
}
