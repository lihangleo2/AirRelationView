package comt.leo.picker.testline.release.bean;

import java.io.Serializable;

public class ZhengjuBean implements Serializable {

    private String groupId1;
    private String groupId2;
    private int picCount;

    public String getGroupId1() {
        return groupId1;
    }

    public void setGroupId1(String groupId1) {
        this.groupId1 = groupId1;
    }

    public String getGroupId2() {
        return groupId2;
    }

    public void setGroupId2(String groupId2) {
        this.groupId2 = groupId2;
    }

    public int getPicCount() {
        return picCount;
    }

    public void setPicCount(int picCount) {
        this.picCount = picCount;
    }
}
