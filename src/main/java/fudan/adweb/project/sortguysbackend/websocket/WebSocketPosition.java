package fudan.adweb.project.sortguysbackend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fudan.adweb.project.sortguysbackend.constant.GameConstant;
import fudan.adweb.project.sortguysbackend.entity.GameControlMsg;
import fudan.adweb.project.sortguysbackend.entity.PositionMsg;
import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import fudan.adweb.project.sortguysbackend.service.GameService;
import fudan.adweb.project.sortguysbackend.service.MessageService;
import fudan.adweb.project.sortguysbackend.service.RoomService;
import fudan.adweb.project.sortguysbackend.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import static javax.websocket.CloseReason.CloseCodes.getCloseCode;

@ServerEndpoint(value = "/websocketPosition/{roomId}/{nickname}")
@Component
public class WebSocketPosition {

    private static Map<String, String> usersMap = new HashMap<>();

    // 记录房间内的用户（及session）
    private static final Map<Integer, Set<WebSocketPosition>> roomMap = new ConcurrentHashMap<>();

    // 记录房间内用户的位置
//    private static final Map<Integer, Map<String, PositionMsg>> roomPositionMap = new ConcurrentHashMap<>();

    private Session session;

    private final AtomicInteger roomCount = new AtomicInteger(0);

    public static RoomService roomService;
    public static GameService gameService;
    public static MessageService messageService;

    /**
     * on connect
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) throws IOException {
        // todo: 1. 判断房间是否存在，如果房间不存在，则拒绝连接
        System.out.println(roomService.isExisted("1"));


        // todo: 2. 判断游戏是否已经开始，如果已经开始，则拒绝连接
        if(roomId==-2){
            session.close(new CloseReason(getCloseCode(GameConstant.GAME_ALREADY_START), "already start"));
            return;
        }

        // 存入用户session
        this.session = session;
        usersMap.put(nickname, session.getId());

        // 若房间不存在，则创建新房间；若房间存在，则将用户放入房间（并发？）
        if(!roomService.isExisted(String.valueOf(roomId))){
            roomService.createRoom(String.valueOf(roomId), nickname);
        }else{
            roomService.addPlayerIntoRoom(false, String.valueOf(roomId), nickname);
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

        Set<PlayerInfo> playerInfos = gameService.getAllPlayerInfo(String.valueOf(roomId));
        Map<String, PositionMsg> positionMap = messageService.fromAllPlayerInfo2PositionMsg(playerInfos);
        synchronized (this.session){
            this.session.getBasicRemote().sendText(asJsonString(positionMap));
        }

        // 给房间中的人广播新的人的信息
        PositionMsg positionMsg = new PositionMsg(nickname, 0d, 30d, 0d, 1);
        multicast(positionMsg, roomId);
    }

    /**
     * close
     */
    @OnClose
    public void onClose(Session session, CloseReason closeReason, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) throws IOException {
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

        roomService.leaveRoom(String.valueOf(roomId), username);

        multicast(new PositionMsg(username, -1d, -1d, -1d, 2), roomId);
    }

    /**
     * @param message message from client
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) {
        ObjectMapper objectMapper = new ObjectMapper();

        // 请求类型作为url参数进行传递，保证是同一个连接
        Integer messageType = Integer.parseInt(session.getRequestParameterMap().get("messageType").get(0));
        System.out.println(messageType);
        // 用户位置移动信息
        if (messageType == GameConstant.POSITION_MESSAGE){
            PositionMsg positionMsg;
            try {
                positionMsg = objectMapper.readValue(message, PositionMsg.class);

                // 更新位置信息
                gameService.updatePosition(String.valueOf(roomId), nickname, positionMsg.getX(), positionMsg.getY(), positionMsg.getZ());

                // 设置正常消息
                positionMsg.setType(1);

                // 广播用户位置
                multicast(positionMsg, roomId);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 游戏控制信息
        else if (messageType == GameConstant.GAME_CONTROL_MESSAGE) {
            GameControlMsg gameControlMsg;
            try {
                gameControlMsg = objectMapper.readValue(message, GameControlMsg.class);
                if (gameControlMsg.getType() == GameConstant.GAME_CONTROL_GET_READY) {
                    gameService.getReady(String.valueOf(roomId), nickname);
                }else if (gameControlMsg.getType() == GameConstant.GAME_CONTROL_START) {
                    gameService.getStart(String.valueOf(roomId), nickname);
                }else if (gameControlMsg.getType() == GameConstant.GAME_CONTROL_STOP) {
                    gameService.getStop(String.valueOf(roomId));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //todo: 捡垃圾
        else if (messageType == GameConstant.PICK_GARBAGE_MESSAGE){

        }
        //todo: 扔垃圾
        else if (messageType == GameConstant.THROW_GARBAGE_MESSAGE){

        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    // 组播位置信息（基于房间号）
    private void multicast(PositionMsg positionMsg, Integer roomId) throws IOException {
        Set<WebSocketPosition> room = roomMap.get(roomId);
        for (WebSocketPosition item : room) {
            synchronized (item.session){
                item.session.getBasicRemote().sendText(asJsonString(positionMsg));
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

}
