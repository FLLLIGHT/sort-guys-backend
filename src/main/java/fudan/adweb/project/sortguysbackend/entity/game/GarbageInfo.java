package fudan.adweb.project.sortguysbackend.entity.game;

public class GarbageInfo {
    private String garbageName;
    private Double x;
    private Double y;
    private Double z;

    // 干垃圾：1；湿垃圾：2；有害垃圾：3；可回收垃圾：4
    private int type;

    // 垃圾的分值，可以暂时先默认为1
    private int score;

    // 所属玩家名
    private String username;

    // UUID
    private String garbageId;

    // 缩放大小
    private float rate;

    // 中文名
    private String cname;

    public String getGarbageId() {
        return garbageId;
    }

    public void setGarbageId(String garbageId) {
        this.garbageId = garbageId;
    }

    public String getGarbageName() {
        return garbageName;
    }

    public void setGarbageName(String garbageName) {
        this.garbageName = garbageName;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getZ() {
        return z;
    }

    public void setZ(Double z) {
        this.z = z;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
