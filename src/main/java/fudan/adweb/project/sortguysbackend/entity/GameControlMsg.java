package fudan.adweb.project.sortguysbackend.entity;

public class GameControlMsg {
    // 1：准备游戏 2：开始游戏 3：暂停游戏 4：....
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
