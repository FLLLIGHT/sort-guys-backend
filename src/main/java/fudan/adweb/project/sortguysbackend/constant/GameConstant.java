package fudan.adweb.project.sortguysbackend.constant;

public final class GameConstant {
    // websocket连接错误码
    public static final int GAME_ALREADY_START = 4000;
    public static final int ROOM_NOT_EXIST = 4001;

    // websocket传递message类型
    public static final int POSITION_MESSAGE = 1;
    public static final int GAME_CONTROL_MESSAGE = 2;
    public static final int PICK_GARBAGE_MESSAGE = 3;
    public static final int THROW_GARBAGE_MESSAGE = 4;

    // 用户游戏状态
    public static final int PLAYER_NOT_READY = 1;
    public static final int PLAYER_READY = 2;
    public static final int PLAYER_IN_GAME = 3;

    // 房间状态
    public static final int ROOM_WAITING = 1;
    public static final int ROOM_GAMING = 2;
    //暂停
    public static final int ROOM_STOPPING = 3;

    // 游戏控制信息类型
    public static final int GAME_CONTROL_GET_READY = 1;
    public static final int GAME_CONTROL_START = 2;
    public static final int GAME_CONTROL_STOP = 3;
}
