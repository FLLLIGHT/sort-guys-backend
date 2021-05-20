package fudan.adweb.project.sortguysbackend.entity;

import java.io.Serializable;

public class GarbageSortResult implements Serializable {
    private static final long serialVersionUID = -6382304737636614132L;
    private Integer gid;
    private Integer uid;
    private Integer times;
    private Integer correctTimes;
    private String unlockTime;

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Integer getCorrectTimes() {
        return correctTimes;
    }

    public void setCorrectTimes(Integer correctTimes) {
        this.correctTimes = correctTimes;
    }

    public String getUnlockTime() {
        return unlockTime;
    }

    public void setUnlockTime(String unlockTime) {
        this.unlockTime = unlockTime;
    }
}
