package fudan.adweb.project.sortguysbackend.entity;

public class PositionMsg {
    private String username;
    private Double x;
    private Double y;
    private Double z;

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

    @Override
    public String toString() {
        return "PositionMsg{" +
                "username='" + username + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
