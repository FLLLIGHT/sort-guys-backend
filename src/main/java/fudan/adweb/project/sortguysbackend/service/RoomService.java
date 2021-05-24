package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.constant.GameConstant;
import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import fudan.adweb.project.sortguysbackend.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        map.put("status", GameConstant.ROOM_WAITING);

        String userMapKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("userMapKey", userMapKey);

        String garbageMapKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("garbageMapKey", garbageMapKey);

        String scoreZSetKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("scoreZSetKey", scoreZSetKey);

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

        String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
        redisUtil.zSet(scoreZSetKey, username, 0);
    }

    public void leaveRoom(String roomId, String username){
        // todo: 判断游戏是否已经开始？

        // todo: 判断是否是房主？
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        redisUtil.hdel(userMapKey, username);

        String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
        redisUtil.zSetRemove(scoreZSetKey, username);
    }

    // 判断用户是否是房主
    public boolean checkRoomOwner(String roomId, String username){
        return redisUtil.hget(roomId, "roomOwner").equals(username);
    }

    // 判断房间内除房主外所有用户是否已经准备好了（房主随意）
    public boolean checkReadyStatus(String roomId){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        Map<Object, Object> userMap = redisUtil.hmget(userMapKey);
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            PlayerInfo playerInfo = (PlayerInfo) entry.getValue();
            if(!playerInfo.isRoomOwner() && playerInfo.getStatus() != GameConstant.PLAYER_READY){
                return false;
            }
        }
        return true;
    }
}
