package fudan.adweb.project.sortguysbackend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fudan.adweb.project.sortguysbackend.entity.PositionMsg;
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

@ServerEndpoint(value = "/websocketPosition/{roomId}/{nickname}")
@Component
public class WebSocketPosition {
//    private static CopyOnWriteArraySet<WebSocketPosition> webSocketSet = new CopyOnWriteArraySet<>();
    private static Map<String, String> usersMap = new HashMap<>();
//    private static Map<String, PositionMsg> positionMap = new HashMap<>();

    // 记录房间内的用户（及session）
    private static final Map<Integer, Set<WebSocketPosition>> roomMap = new ConcurrentHashMap<>();

    // 记录房间内用户的位置
    private static final Map<Integer, Map<String, PositionMsg>> roomPositionMap = new ConcurrentHashMap<>();

    private Session session;

    private final AtomicInteger roomCount = new AtomicInteger(0);

    /**
     * on connect
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) throws IOException {

        Map<String,Object> message= new HashMap<>();
        this.session = session;
        usersMap.put(nickname, session.getId());
        PositionMsg positionMsg = new PositionMsg(nickname, 0d, 30d, 0d, 1);

        // 另一种方式：roomId = -1 代表新建房间，返回房间号。但可能会冲突（和直接创建的方式），两者不能并存，要讨论
        if(roomId==-1){
            roomId = roomCount.incrementAndGet();
        }

        Map<String, PositionMsg> positionMap = roomPositionMap.get(roomId);
        // 若房间不存在，则创建新房间；若房间存在，则将用户放入房间
        if (positionMap == null){
            positionMap = new HashMap<>();
        }
        positionMap.put(nickname, positionMsg);
        roomPositionMap.put(roomId, positionMap);

//        webSocketSet.add(this);

        Set<WebSocketPosition> room = roomMap.get(roomId);
        // 若房间不存在，则创建新房间；若房间存在，则将用户放入房间
        if (room == null){
            room = new CopyOnWriteArraySet<>();
            room.add(this);
            roomMap.put(roomId, room);
        }else{
            room.add(this);
        }


        System.out.println("New connection: " + nickname + ", id: " + session.getId() + ", current people:" + roomMap.get(roomId).size());
        message.put("type",0); // 0-connect success，1-message
        message.put("people",roomMap.get(roomId).size());
        message.put("name",nickname);
        message.put("aisle",session.getId());

        message.put("roomId", roomId);
        synchronized (this.session){
            this.session.getBasicRemote().sendText(new Gson().toJson(message));
        }
        synchronized (this.session){
            this.session.getBasicRemote().sendText(asJsonString(positionMap));
        }
        multicast(positionMsg, roomId);
    }

    /**
     * close
     */
    @OnClose
    public void onClose(Session session, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) throws IOException {
//        webSocketSet.remove(this);
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
        for (Map.Entry<String, PositionMsg> entry : roomPositionMap.get(roomId).entrySet()){
            if (entry.getKey().equals(username)){
                roomPositionMap.get(roomId).remove(entry.getKey());
                break;
            }
        }
        multicast(new PositionMsg(username, -1d, -1d, -1d, 2), roomId);
    }

    /**
     * @param message message from client
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam("roomId") Integer roomId, @PathParam("nickname") String nickname) {
        ObjectMapper objectMapper = new ObjectMapper();
        PositionMsg positionMsg;

        try {
            positionMsg = objectMapper.readValue(message, PositionMsg.class);
            // 设置正常消息
            positionMsg.setType(1);
            // 更新位置信息
            roomPositionMap.get(roomId).put(nickname, positionMsg);
            multicast(positionMsg, roomId);
        } catch (IOException e) {
            e.printStackTrace();
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
