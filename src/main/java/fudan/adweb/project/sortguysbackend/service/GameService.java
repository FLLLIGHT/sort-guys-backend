package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import fudan.adweb.project.sortguysbackend.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.PlainDocument;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
public class GameService {

    private RedisUtil redisUtil;

    @Autowired
    public GameService(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    public Set<PlayerInfo> getAllPlayerInfo(String roomId){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        return castFromObjectToPlayerInfo(Objects.requireNonNull(redisUtil.hmget(userMapKey)));
    }

    private Set<PlayerInfo> castFromObjectToPlayerInfo(Map<Object, Object> userMap){
        Set<PlayerInfo> set = new HashSet<>();
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            set.add((PlayerInfo) entry.getValue());
        }
        return set;
    }

    public void updatePosition(String roomId, String username, Double x, Double y, Double z){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        PlayerInfo playerInfo = (PlayerInfo) redisUtil.hget(userMapKey, username);
        playerInfo.setX(x);
        playerInfo.setY(y);
        playerInfo.setZ(z);
        redisUtil.hset(userMapKey, username, playerInfo);
    }


}
