package fudan.adweb.project.sortguysbackend.entity.game;

import java.util.Set;

public class RoomInfo {
    private String roomOwner;
    private int status;
    private Set<PlayerInfo> playerInfos;

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
}
