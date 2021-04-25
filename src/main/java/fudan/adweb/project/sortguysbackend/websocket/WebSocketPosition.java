package fudan.adweb.project.sortguysbackend.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import fudan.adweb.project.sortguysbackend.entity.PositionMsg;
import fudan.adweb.project.sortguysbackend.entity.SocketMsg;
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
    private Session session;

    /**
     * on connect
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("nickname") String nickname) {
        Map<String,Object> message= new HashMap<>();
        this.session = session;
        map.put(session.getId(), session);
        usersMap.put(nickname, session.getId());
        webSocketSet.add(this);

        System.out.println("New connection: " + nickname + ", id: " + session.getId() + ", current people:" + webSocketSet.size());
        message.put("type",0); // 0-connect success，1-message
        message.put("people",webSocketSet.size());
        message.put("name",nickname);
        message.put("aisle",session.getId());
        this.session.getAsyncRemote().sendText(new Gson().toJson(message));
    }

    /**
     * close
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        map.remove(this.session.getId());
        for (Map.Entry<String, String> entry : usersMap.entrySet()){
            if (entry.getValue().equals(this.session.getId())){
                usersMap.remove(entry.getKey());
                break;
            }
        }
        System.out.println("Close 1, current people: " + webSocketSet.size());
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
    private void broadcast(PositionMsg positionMsg) {
        for (WebSocketPosition item : webSocketSet) {
            item.session.getAsyncRemote().sendText(asJsonString(positionMsg));
        }
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
