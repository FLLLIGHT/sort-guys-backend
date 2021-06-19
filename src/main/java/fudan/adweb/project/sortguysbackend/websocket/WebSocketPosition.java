package fudan.adweb.project.sortguysbackend.websocket;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fudan.adweb.project.sortguysbackend.constant.GameConstant;
import fudan.adweb.project.sortguysbackend.entity.ChatMsg;
import fudan.adweb.project.sortguysbackend.entity.GameControlMsg;
import fudan.adweb.project.sortguysbackend.entity.GarbageControlMsg;
import fudan.adweb.project.sortguysbackend.entity.PositionMsg;
import fudan.adweb.project.sortguysbackend.entity.game.GarbageBinInfo;
import fudan.adweb.project.sortguysbackend.entity.game.GarbageInfo;
import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import fudan.adweb.project.sortguysbackend.entity.game.ScoreInfo;
import fudan.adweb.project.sortguysbackend.service.EmojiService;
import fudan.adweb.project.sortguysbackend.service.GameService;
import fudan.adweb.project.sortguysbackend.service.MessageService;
import fudan.adweb.project.sortguysbackend.service.RoomService;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.websocket.CloseReason.CloseCodes.getCloseCode;

@ServerEndpoint(value = "/websocketPosition/{roomId}/{nickname}")
@Component
public class WebSocketPosition {
    // session id 和 session 的对应关系，在单聊时用到
    private static Map<String, Session> sessionMap = new HashMap<>();
    private static Map<String, String> usersMap = new HashMap<>();

    // 记录房间内的用户（及session）
    private static final Map<Integer, Set<WebSocketPosition>> roomMap = new ConcurrentHashMap<>();

    private Session session;

    private final AtomicInteger roomCount = new AtomicInteger(0);

    public static RoomService roomService;
    public static GameService gameService;
    public static MessageService messageService;
    public static EmojiService emojiService;

    /**
     * on connect
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) throws IOException {
        System.out.println("========IN OPEN===========");

        // 1. 判断房间是否存在，如果房间不存在，则拒绝连接
        if (!roomService.isExisted(String.valueOf(roomId))) {
            session.close(new CloseReason(getCloseCode(GameConstant.ROOM_NOT_EXIST), "room not exist"));
            return;
        }

        // 2. 判断游戏是否已经开始，如果已经开始，则拒绝连接
        if (roomService.checkRoomStatus(String.valueOf(roomId))) {
            session.close(new CloseReason(getCloseCode(GameConstant.GAME_ALREADY_START), "already start"));
            return;
        }

        // 存入用户session
        this.session = session;
        sessionMap.put(session.getId(), session);
        usersMap.put(nickname, session.getId());

        PlayerInfo newPlayerInfo = null;
        // 房主在创建房间时就已经加入内存了，不需要重复加入
        if(!roomService.checkRoomOwner(String.valueOf(roomId), nickname)){
            newPlayerInfo = roomService.enterRoom(false, String.valueOf(roomId), nickname);
        }else{
            newPlayerInfo = roomService.enterRoom(true, String.valueOf(roomId), nickname);
        }

        Set<WebSocketPosition> room = roomMap.get(roomId);
        // 若房间不存在，则创建新房间；若房间存在，则将用户放入房间
        if (room == null){
            room = new CopyOnWriteArraySet<>();
            room.add(this);
            roomMap.put(roomId, room);
        }else{
            room.add(this);
        }

        // 创建要返回的message
        Map<String,Object> message= new HashMap<>();
        System.out.println("New connection: " + nickname + ", id: " + session.getId() + ", current people:" + roomMap.get(roomId).size());
        message.put("type",0); // 0-connect success，1-message
        message.put("people",roomMap.get(roomId).size());
        message.put("name",nickname);
        message.put("aisle",session.getId());
        message.put("roomId", roomId);

        // 给新连进的人发送信息和当前房间中其他所有人的位置
        synchronized (this.session){
            this.session.getBasicRemote().sendText(new Gson().toJson(message));
        }

        Set<PlayerInfo> playerInfos = roomService.getAllPlayerInfo(String.valueOf(roomId));
        Map<String, PositionMsg> positionMap = messageService.fromAllPlayerInfo2PositionMsg(playerInfos);
        synchronized (this.session){
            this.session.getBasicRemote().sendText(asJsonString(positionMap));
        }

        // 给房间中的人广播新的人的信息
        PositionMsg positionMsg = messageService.fromPlayerInfo2PositionMsg(newPlayerInfo);
        multicastPosition(positionMsg, roomId);
    }

    /**
     * close
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) throws IOException {
        // 房间不存在
        if(closeReason.getCloseCode().getCode() == GameConstant.ROOM_NOT_EXIST){
            return;
        }

        // 游戏已经开始
        if(closeReason.getCloseCode().getCode() == GameConstant.GAME_ALREADY_START){
            return;
        }
        if(roomMap.containsKey(roomId)){
            roomMap.get(roomId).remove(this);
        }

        String username = "";
        for (Map.Entry<String, String> entry : usersMap.entrySet()){
            if (entry.getValue().equals(this.session.getId())){
                username = entry.getKey();
                usersMap.remove(entry.getKey());
                break;
            }
        }

        sessionMap.remove(this.session.getId());

        roomService.leaveRoom(String.valueOf(roomId), username);

        multicastPosition(new PositionMsg(username, -1d, -1d, -1d, GameConstant.POSITION_REMOVE_MESSAGE, "", 0d), roomId);
    }

    /**
     * @param message message from client
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) {
        // 忽略messageType字段
        ObjectMapper objectMapper = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);;

        Integer messageType = -1;
        try{
            JsonNode jsonNode = objectMapper.readTree(message);
            JsonNode name = jsonNode.get("messageType");
            messageType = Integer.parseInt(name.asText());
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        System.out.println(message);
        // 请求类型作为url参数进行传递，保证是同一个连接
        System.out.println(messageType);
        // 用户位置移动信息
        if (messageType == GameConstant.POSITION_MESSAGE){
            PositionMsg positionMsg;
            try {
                positionMsg = objectMapper.readValue(message, PositionMsg.class);

                // 更新位置信息
                gameService.updatePosition(String.valueOf(roomId), nickname, positionMsg.getX(), positionMsg.getY(), positionMsg.getZ());

                // 设置正常消息
                positionMsg.setType(GameConstant.POSITION_CHANGE_MESSAGE);

                // 广播用户位置
                multicastPosition(positionMsg, roomId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 游戏控制信息
        else if (messageType == GameConstant.GAME_CONTROL_MESSAGE) {
            GameControlMsg gameControlMsg;
            try {
                gameControlMsg = objectMapper.readValue(message, GameControlMsg.class);
                Map<String,Object> returnMessageMap = new HashMap<>();
                String returnMessage = "";
                // 准备
                if (gameControlMsg.getType() == GameConstant.GAME_CONTROL_GET_READY) {
                    returnMessage = gameService.getReady(String.valueOf(roomId), nickname);
                }
                // 取消准备
                else if (gameControlMsg.getType() == GameConstant.GAME_CONTROL_GET_UNREADY) {
                    returnMessage = gameService.getUnready(String.valueOf(roomId), nickname);
                }
                // 开始游戏（房主）
                else if (gameControlMsg.getType() == GameConstant.GAME_CONTROL_START) {
                    returnMessage = gameService.getStart(String.valueOf(roomId), nickname);
                    if (!returnMessage.equals("游戏开始！")){
                        returnMessageMap.put("message", returnMessage);
                        returnMessageMap.put("username", nickname);
                        synchronized (this.session){
                            this.session.getBasicRemote().sendText(asJsonString(returnMessageMap));
                        }
                        return;
                    }
                    System.out.println("开始！");
                    List<GarbageInfo> garbageInfos = gameService.getAllGarbageInfo(String.valueOf(roomId));
                    multicastGarbage(garbageInfos, roomId);
                    List<GarbageBinInfo> garbageBinInfos = gameService.generateGarbageBins(String.valueOf(roomId));
                    multicastGarbageBin(garbageBinInfos, roomId);
                }
                // 暂停游戏
                else if (gameControlMsg.getType() == GameConstant.GAME_CONTROL_STOP) {
                    returnMessage = gameService.getStop(String.valueOf(roomId));
                }
                // 结束游戏
                else if (gameControlMsg.getType() == GameConstant.GAME_CONTROL_OVER) {
                    List<ScoreInfo> scoreInfoList = gameService.getOver(String.valueOf(roomId), nickname);
                    multicastScoreList(scoreInfoList, roomId);
                }

                returnMessageMap.put("message", returnMessage);
                returnMessageMap.put("username", nickname);
                multicastMessage(returnMessageMap, roomId);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 操纵垃圾
        else if (messageType == GameConstant.GARBAGE_CONTROL_MESSAGE){
            GarbageControlMsg garbageControlMsg;
            try {
                garbageControlMsg = objectMapper.readValue(message, GarbageControlMsg.class);
                Map<String,Object> returnMessageMap = new HashMap<>();
                GarbageInfo oldGarbageInfo = null;
                // 捡垃圾
                if (garbageControlMsg.getAction() == GameConstant.GARBAGE_CONTROL_GET) {
                    oldGarbageInfo = gameService.pickUpGarbage(String.valueOf(roomId), nickname, garbageControlMsg.getGarbageId());
                }
                // 扔垃圾（到地上）
                else if (garbageControlMsg.getAction() == GameConstant.GARBAGE_CONTROL_THROW_GROUND) {
                    // 等同于大地捡起了垃圾^^
                    oldGarbageInfo = gameService.pickUpGarbage(String.valueOf(roomId), "", garbageControlMsg.getGarbageId());
                }
                // 扔垃圾（到垃圾桶）
                else if (garbageControlMsg.getAction() == GameConstant.GARBAGE_CONTROL_THROW_BIN) {
                    oldGarbageInfo = gameService.throwGarbage(String.valueOf(roomId), nickname, garbageControlMsg.getGarbageId(), garbageControlMsg.getGarbageBinType());
                    GarbageInfo newGarbageInfo = gameService.generateGarbageInRoom(String.valueOf(roomId));
                    returnMessageMap.put("newGarbageInfo", newGarbageInfo);
                }
                returnMessageMap.put("oldGarbageInfo", oldGarbageInfo);
                multicastMessage(returnMessageMap, roomId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 聊天
        else if (messageType == GameConstant.CHAT_CONTROL_MESSAGE){
            ChatMsg chatMsg;
            try {
                chatMsg = objectMapper.readValue(message, ChatMsg.class);
                // 单聊
                if (chatMsg.getType() == GameConstant.CHAT_WITH_USER) {
                    Session fromSession = sessionMap.get(session.getId());
                    String toUsername = chatMsg.getToUser();
                    String toId = usersMap.get(toUsername);
                    Session toSession = sessionMap.get(toId);

                    if (toSession != null) {
                        Map<String, Object> m = new HashMap<>();
                        m.put("type", GameConstant.CHAT_WITH_USER);
                        m.put("toUser", toUsername);
                        m.put("fromUser", nickname);
                        m.put("msg", getMessage(chatMsg.getContentType(), chatMsg.getMsg()));
                        m.put("contentType", chatMsg.getContentType());
                        sendMessage(fromSession, new Gson().toJson(m));
                        sendMessage(toSession, new Gson().toJson(m));
                    } else {
                        Map<String, String> map = new HashMap<>();
                        map.put("msg", "发送失败，对方不在线");
                        fromSession.getAsyncRemote().sendText(new Gson().toJson(map));
                    }
                }
                // 群聊
                else if (chatMsg.getType() == GameConstant.CHAT_WITH_GROUP) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("type", GameConstant.CHAT_WITH_GROUP);
                    map.put("fromUser", nickname);
                    map.put("msg", getMessage(chatMsg.getContentType(), chatMsg.getMsg()));
                    map.put("contentType", chatMsg.getContentType());
                    multicastMessage(map, roomId);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 根据消息类型生成 msg
    private String getMessage(int contentType, String msg) {
        if (contentType == GameConstant.CHAT_CONTENT_TEXT){
            return msg;
        }
        else if (contentType == GameConstant.CHAT_CONTENT_EMOJI){
            // 找到 msg 对应的 url
            return emojiService.findUrlByName(msg);
        }
        return "";
    }


    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    // 组播位置信息（基于房间号）
    private void multicastPosition(PositionMsg positionMsg, Integer roomId) throws IOException {
        Set<WebSocketPosition> room = roomMap.get(roomId);
        for (WebSocketPosition item : room) {
            synchronized (item.session){
                item.session.getBasicRemote().sendText(asJsonString(positionMsg));
            }
        }
    }

    // 组播消息
    private void multicastMessage(Map<String,Object> message, Integer roomId) throws IOException {
        Set<WebSocketPosition> room = roomMap.get(roomId);
        for (WebSocketPosition item : room) {
            synchronized (item.session){
                item.session.getBasicRemote().sendText(asJsonString(message));
            }
        }
    }

    // 组播所有垃圾
    private void multicastGarbage(List<GarbageInfo> garbageInfos, Integer roomId) throws IOException {
        Set<WebSocketPosition> room = roomMap.get(roomId);
        for (WebSocketPosition item : room) {
            synchronized (item.session){
                item.session.getBasicRemote().sendText(asJsonString(garbageInfos));
            }
        }
    }

    // 组播所有垃圾桶
    private void multicastGarbageBin(List<GarbageBinInfo> garbageBinInfos, Integer roomId) throws IOException {
        Set<WebSocketPosition> room = roomMap.get(roomId);
        for (WebSocketPosition item : room) {
            synchronized (item.session){
                item.session.getBasicRemote().sendText(asJsonString(garbageBinInfos));
            }
        }
    }

    // 组播排行榜
    private void multicastScoreList(List<ScoreInfo> scoreInfoList, Integer roomId) throws IOException {
        Set<WebSocketPosition> room = roomMap.get(roomId);
        for (WebSocketPosition item : room) {
            synchronized (item.session){
                item.session.getBasicRemote().sendText(asJsonString(scoreInfoList));
            }
        }
    }

    private void position(){

    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 单播
    private void sendMessage(Session session, String message) throws IOException {
        synchronized (session) {
            session.getBasicRemote().sendText(message);
        }
    }

}
