package fudan.adweb.project.sortguysbackend.entity;

public class GarbageControlMsg {
    // 垃圾的新的拥有者（听起来怪怪的？不过就是这个意思）
    private String toUsername;
    private String garbageId;
    // 垃圾操纵动作 - 1：获取  2：扔掉（到地上）  3:扔掉（到垃圾桶）
    private int action;
    // 干垃圾：1；湿垃圾：2；有害垃圾：3；可回收垃圾：4
    private int garbageBinType;

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getGarbageId() {
        return garbageId;
    }

    public void setGarbageId(String garbageId) {
        this.garbageId = garbageId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getGarbageBinType() {
        return garbageBinType;
    }

    public void setGarbageBinType(int garbageBinType) {
        this.garbageBinType = garbageBinType;
    }
}
