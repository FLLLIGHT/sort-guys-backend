package fudan.adweb.project.sortguysbackend.config;

import fudan.adweb.project.sortguysbackend.security.jwt.JwtTokenUtil;
import fudan.adweb.project.sortguysbackend.service.EmojiService;
import fudan.adweb.project.sortguysbackend.service.GameService;
import fudan.adweb.project.sortguysbackend.service.MessageService;
import fudan.adweb.project.sortguysbackend.service.RoomService;
import fudan.adweb.project.sortguysbackend.util.RedisUtil;
import fudan.adweb.project.sortguysbackend.websocket.WebSocketPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * 注入对象ServerEndpointExporter
 *
 */
@Configuration
public class WebSocketConfig {

    // set方法注入，websocket不支持直接注入
    @Autowired
    public void setRoomService(RoomService roomService) {
        WebSocketPosition.roomService = roomService;
    }

    @Autowired
    public void setGameService(GameService gameService) {
        WebSocketPosition.gameService = gameService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        WebSocketPosition.messageService = messageService;
    }

    @Autowired
    public void setEmojiService(EmojiService emojiService){
        WebSocketPosition.emojiService = emojiService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil){WebSocketPosition.jwtTokenUtil = jwtTokenUtil;}

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}