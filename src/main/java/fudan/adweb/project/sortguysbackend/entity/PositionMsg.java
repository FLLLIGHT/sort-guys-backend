package fudan.adweb.project.sortguysbackend.entity;

public class PositionMsg {
    private String username;
    private Double x;
    private Double y;
    private Double z;
    // 一般的有内容有意义的位置信息的类型都是1，然后断开连接的消息类型是2（里面的位置信息无意义，都是-1）
    private int type;

    private String color;
    private Double rotation;

    public PositionMsg(){
        //empty
    }

    public PositionMsg(String username, Double x, Double y, Double z, int type, String color, Double rotation) {
        this.username = username;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.color = color;
        this.rotation = rotation;
    }

    public Double getRotation() {
        return rotation;
    }

    public void setRotation(Double rotation) {
        this.rotation = rotation;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
