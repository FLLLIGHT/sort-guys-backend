package fudan.adweb.project.sortguysbackend.entity;

public class GarbageSortResultInfo {
    private Integer gid;
    private String name;
    private String cname;
    private String url;
    private String description;
    private Integer times;
    private Integer correctTimes;
    private String unlockTime;  // 管理员获取全部的垃圾分类信息时该项为垃圾初始化的时间

    public GarbageSortResultInfo(Integer gid, String name, String cname, String description, Integer times,
                                 Integer correctTimes, String unlockTime, String url) {
        this.gid = gid;
        this.name = name;
        this.description = description;
        this.times = times;
        this.correctTimes = correctTimes;
        this.unlockTime = unlockTime;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
