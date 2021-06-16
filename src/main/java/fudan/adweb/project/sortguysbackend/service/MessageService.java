package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.PositionMsg;
import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import org.springframework.stereotype.Service;

import javax.swing.text.Position;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class MessageService {

    public Map<String, PositionMsg> fromAllPlayerInfo2PositionMsg(Set<PlayerInfo> playerInfoSet){
        Map<String, PositionMsg> positionMsgMap = new HashMap<>();
        for(PlayerInfo playerInfo : playerInfoSet){
            positionMsgMap.put(playerInfo.getUsername(), fromPlayerInfo2PositionMsg(playerInfo));
        }
        return positionMsgMap;
    }

    public PositionMsg fromPlayerInfo2PositionMsg(PlayerInfo playerInfo){
        return new PositionMsg(
                playerInfo.getUsername(), playerInfo.getX(), playerInfo.getY(),
                playerInfo.getZ(), 1, playerInfo.getColor(), playerInfo.getRotation());
    }

    public Map<String, Object> generatePacket(Integer messageType, Object data){
        Map<String, Object> map = new HashMap<>();
        map.put("messageType", messageType);
        map.put("data", data);
        return map;
    }

}
