package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import fudan.adweb.project.sortguysbackend.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RoomService {
    private RedisUtil redisUtil;

    @Autowired
    public RoomService(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    // 创建房间（指定房间号）
    // 并发控制：防止两个用户同时创建相同roomId
    public synchronized String createRoom(String roomId, String roomOwner){
        if(isExisted(roomId)){
            return "room already exists";
        }

        // 创建新房间并设置房间信息
        Map<String, Object> map = new HashMap<>();
        map.put("roomOwner", roomOwner);
        map.put("status", 1);

        String userMapKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("userMapKey", userMapKey);

        String garbageListKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("garbageListKey", garbageListKey);
        redisUtil.hmset(roomId, map);


        // 创建房间的用户集合，并将房主放入
        addPlayerIntoRoom(true, roomId, roomOwner);

        return roomId;
    }

    // 判断房间是否已经存在
    public boolean isExisted(String roomId){
        return redisUtil.hget(roomId, "roomOwner") != null;
    }

    public void addPlayerIntoRoom(boolean isRoomOwner, String roomId, String username){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setRoomOwner(isRoomOwner);
        playerInfo.setStatus(0);
        playerInfo.setUsername(username);
        playerInfo.setX(0d);
        playerInfo.setY(30d);
        playerInfo.setZ(0d);
        redisUtil.hset(userMapKey, username, playerInfo);
    }

    public void leaveRoom(String roomId, String username){
        // todo: 判断游戏是否已经开始？

        // todo: 判断是否是房主？
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        redisUtil.hdel(userMapKey, username);
    }
}
