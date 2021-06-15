package fudan.adweb.project.sortguysbackend.entity.game;

import java.util.Set;

public class RoomInfo {
    private String roomOwner;
    private int status;
    private Set<PlayerInfo> playerInfos;
    private int hintsNum;
    private String roomId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Set<PlayerInfo> getPlayerInfos() {
        return playerInfos;
    }

    public void setPlayerInfos(Set<PlayerInfo> playerInfos) {
        this.playerInfos = playerInfos;
    }

    public int getHintsNum() {
        return hintsNum;
    }

    public void setHintsNum(int hintsNum) {
        this.hintsNum = hintsNum;
    }
}
