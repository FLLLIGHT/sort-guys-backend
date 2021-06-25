package fudan.adweb.project.sortguysbackend.entity;

public class LeavePositionMsg {
    PositionMsg positionMsg;
    String newRoomOwner;

    public PositionMsg getPositionMsg() {
        return positionMsg;
    }

    public void setPositionMsg(PositionMsg positionMsg) {
        this.positionMsg = positionMsg;
    }

    public String getNewRoomOwner() {
        return newRoomOwner;
    }

    public void setNewRoomOwner(String newRoomOwner) {
        this.newRoomOwner = newRoomOwner;
    }
}
