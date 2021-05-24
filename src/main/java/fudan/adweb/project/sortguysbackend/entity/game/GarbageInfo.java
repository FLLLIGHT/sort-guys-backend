package fudan.adweb.project.sortguysbackend.entity.game;

public class GarbageInfo {
    private String garbageName;
    private Double x;
    private Double y;
    private Double z;

    // 未准备：0；已准备：1；游戏中：2
    private int status;

    // 干垃圾：1；湿垃圾：2；有害垃圾：3；可回收垃圾：4
    private int type;

    // 垃圾的分值，可以暂时先默认为1
    private int score;

    // 所属玩家名
    private String username;
}
