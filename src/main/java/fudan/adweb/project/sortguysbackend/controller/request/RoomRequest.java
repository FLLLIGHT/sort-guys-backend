package fudan.adweb.project.sortguysbackend.controller.request;

public class RoomRequest {
    private String username;
    private int hintsNum;
    private int sid;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHintsNum() {
        return hintsNum;
    }

    public void setHintsNum(int hintsNum) {
        this.hintsNum = hintsNum;
    }
}
