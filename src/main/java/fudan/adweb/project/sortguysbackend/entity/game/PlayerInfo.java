package fudan.adweb.project.sortguysbackend.entity.game;

public class PlayerInfo {
    private String username;
    private Double x;
    private Double y;
    private Double z;

    // 未准备：1；已准备：2；游戏中：3
    private int status;
    private boolean isRoomOwner;

    // 剩余可用的提示次数
    private int hintsNumLeft;
    // 当前连续分对的垃圾数
    private int correctNum;

    public boolean isRoomOwner() {
        return isRoomOwner;
    }

    public void setRoomOwner(boolean roomOwner) {
        isRoomOwner = roomOwner;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getHintsNumLeft() {
        return hintsNumLeft;
    }

    public void setHintsNumLeft(int hintsNumLeft) {
        this.hintsNumLeft = hintsNumLeft;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(int correctNum) {
        this.correctNum = correctNum;
    }
}
