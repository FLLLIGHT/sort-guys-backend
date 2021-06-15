package fudan.adweb.project.sortguysbackend.constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GameConstant {
    // websocket连接错误码
    public static final int GAME_ALREADY_START = 4000;
    public static final int ROOM_NOT_EXIST = 4001;

    // websocket传递message类型 - 1：位置信息  2：游戏控制信息（准备/开始/暂停）  3：垃圾操纵动作 4：聊天
    public static final int POSITION_MESSAGE = 1;
    public static final int GAME_CONTROL_MESSAGE = 2;
    public static final int GARBAGE_CONTROL_MESSAGE = 3;
    public static final int CHAT_CONTROL_MESSAGE = 4;

    // 用户游戏状态 - 1：未准备  2：准备  3：游戏中
    public static final int PLAYER_NOT_READY = 1;
    public static final int PLAYER_READY = 2;
    public static final int PLAYER_IN_GAME = 3;

    // 房间状态 - 1：准备中  2：游戏中  3：暂停
    public static final int ROOM_WAITING = 1;
    public static final int ROOM_GAMING = 2;
    public static final int ROOM_STOPPING = 3;

    // 游戏控制信息类型 - 1：准备  2：开始游戏  3：暂停游戏  4：结束游戏  5：取消准备
    public static final int GAME_CONTROL_GET_READY = 1;
    public static final int GAME_CONTROL_START = 2;
    public static final int GAME_CONTROL_STOP = 3;
    public static final int GAME_CONTROL_OVER = 4;
    public static final int GAME_CONTROL_GET_UNREADY = 5;

    // 位置消息类型 - 1：位置改变信息  2：位置移除信息（用户退出房间时触发）
    public static final int POSITION_CHANGE_MESSAGE = 1;
    public static final int POSITION_REMOVE_MESSAGE = 2;

    // 垃圾种类 - 1：干垃圾  2：湿垃圾  3：可回收垃圾  4：有害垃圾
    public static final int RESIDUAL_GARBAGE = 1;
    public static final int HOUSEHOLD_FOOD_GARBAGE = 2;
    public static final int RECYCLABLE_GARBAGE = 3;
    public static final int HAZARDOUS_GARBAGE = 4;

    public static final Map<String, Integer> GARBAGE_TYPE_MAP = new HashMap<String, Integer>(){{
        put("干垃圾", RESIDUAL_GARBAGE);
        put("湿垃圾", HOUSEHOLD_FOOD_GARBAGE);
        put("可回收物", RECYCLABLE_GARBAGE);
        put("有害垃圾", HAZARDOUS_GARBAGE);
    }};

    // 操纵垃圾的动作 - 1：获取  2：扔掉（到地上）  3:扔掉（到垃圾桶）
    public static final int GARBAGE_CONTROL_GET = 1;
    public static final int GARBAGE_CONTROL_THROW_GROUND = 2;
    public static final int GARBAGE_CONTROL_THROW_BIN = 3;

    // 聊天形式 1：群聊 2：单聊
    public static final int CHAT_WITH_GROUP = 1;
    public static final int CHAT_WITH_USER = 2;

    // 发送的内容 - 1：文字 2：表情包
    public static final int CHAT_CONTENT_TEXT = 1;
    public static final int CHAT_CONTENT_EMOJI = 2;

    // 查询提示次数 - 1：房间号不存在 2：房间号与用户号不匹配
    public static final int HINT_ROOM_NOT_EXIST = -100;
    public static final int HINT_ROOM_USER_NOT_MATCH = -101;

    // 垃圾分值 - 基础分 3 分，疑难额外 +2 分
    public static final int GARBAGE_BASIC_SCORE = 3;
    public static final int GARBAGE_PUZZLE_SCORE = 2;

    // 连续分对垃圾的情况
    public static final List<Integer> GARBAGE_BONUS_CORRECT_SCORE = new ArrayList<Integer>(){
        {
            add(1);
            add(2);
            add(3);
            add(5);
        }
    };

}
