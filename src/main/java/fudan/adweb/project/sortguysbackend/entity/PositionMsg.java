package fudan.adweb.project.sortguysbackend.entity;

public class PositionMsg {
    private String username;
    private Double x;
    private Double y;
    private Double z;
    // type=1: 一般的消息，type=2：断开连接的消息，type=3：初始消息
    private int type;

    public PositionMsg(){
        //empty
    }

    public PositionMsg(String username, Double x, Double y, Double z, int type) {
        this.username = username;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public String toString() {
        return "PositionMsg{" +
                "username='" + username + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", type=" + type +
                '}';
    }
}
