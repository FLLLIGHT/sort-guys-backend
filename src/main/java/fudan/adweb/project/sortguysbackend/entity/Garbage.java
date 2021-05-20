package fudan.adweb.project.sortguysbackend.entity;

import java.io.Serializable;

public class Garbage implements Serializable {
    private static final long serialVersionUID = 6184686150867692493L;
    private Integer gid;
    private String name;
    private String type;
    private String description;
    private String updateTime;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
