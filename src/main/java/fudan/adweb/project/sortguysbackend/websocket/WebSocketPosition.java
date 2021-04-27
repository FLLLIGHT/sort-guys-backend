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
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/websocketPosition/{nickname}")
@Component
public class WebSocketPosition {
    // bind sessionId and session
    private static Map<String, Session> map = new HashMap<>();
    // store WebSocketService
    private static CopyOnWriteArraySet<WebSocketPosition> webSocketSet = new CopyOnWriteArraySet<>();
    private static Map<String, String> usersMap = new HashMap<>();
    private static Map<String, PositionMsg> positionMap = new HashMap<>();
    private Session session;

    /**
     * on connect
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("nickname") String nickname) throws IOException {
        Map<String,Object> message= new HashMap<>();
        this.session = session;
        map.put(session.getId(), session);
        usersMap.put(nickname, session.getId());
        PositionMsg positionMsg = new PositionMsg(nickname, 0d, 30d, 0d, 1);
        positionMap.put(nickname, positionMsg);
        webSocketSet.add(this);

        System.out.println("New connection: " + nickname + ", id: " + session.getId() + ", current people:" + webSocketSet.size());
        message.put("type",0); // 0-connect success，1-message
        message.put("people",webSocketSet.size());
        message.put("name",nickname);
        message.put("aisle",session.getId());
        synchronized (this.session){
            this.session.getBasicRemote().sendText(new Gson().toJson(message));
        }
        synchronized (this.session){
            this.session.getBasicRemote().sendText(asJsonString(positionMap));
        }
        broadcast(positionMsg);
    }

    /**
     * close
     */
    @OnClose
    public void onClose() throws IOException {
        webSocketSet.remove(this);
        map.remove(this.session.getId());
        String username = "";
        for (Map.Entry<String, String> entry : usersMap.entrySet()){
            if (entry.getValue().equals(this.session.getId())){
                username = entry.getKey();
                usersMap.remove(entry.getKey());
                break;
            }
        }
        for (Map.Entry<String, PositionMsg> entry : positionMap.entrySet()){
            if (entry.getKey().equals(username)){
                positionMap.remove(entry.getKey());
                break;
            }
        }
        System.out.println("Close 1, current people: " + webSocketSet.size());
        broadcast(new PositionMsg(username, -1d, -1d, -1d, 2));
    }

    /**
     * @param message message from client
     */
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("nickname") String nickname) {
        ObjectMapper objectMapper = new ObjectMapper();
        PositionMsg positionMsg;

        try {
            positionMsg = objectMapper.readValue(message, PositionMsg.class);
            // 设置正常消息
            positionMsg.setType(1);
            positionMap.put(nickname, positionMsg);
            broadcast(positionMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    // 广播位置信息
    private void broadcast(PositionMsg positionMsg) throws IOException {
        for (WebSocketPosition item : webSocketSet) {
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
