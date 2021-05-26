package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.constant.GameConstant;
import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import fudan.adweb.project.sortguysbackend.entity.game.RoomInfo;
import fudan.adweb.project.sortguysbackend.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoomService {
    private RedisUtil redisUtil;

    @Autowired
    public RoomService(RedisUtil redisUtil){
        this.redisUtil = redisUtil;
    }

    // 创建房间（指定房间号）
    // 并发控制：防止两个用户同时创建相同roomId
    public String createRoom(String roomId, String roomOwner, int hintsNum){
        if(isExisted(roomId)){
            return "room already exists";
        }

        // 创建新房间并设置房间信息
        Map<String, Object> map = new HashMap<>();
        map.put("roomOwner", roomOwner);
        map.put("status", GameConstant.ROOM_WAITING);
        map.put("hintsNum", hintsNum);

        String userMapKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("userMapKey", userMapKey);

        String garbageMapKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("garbageMapKey", garbageMapKey);

        String scoreZSetKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("scoreZSetKey", scoreZSetKey);

        redisUtil.hmset(roomId, map);

        // 创建房间的用户集合，并将房主放入
        enterRoom(true, roomId, roomOwner);

        // 加入总的房间
        redisUtil.sSet("existedRoomId", roomId);

        return roomId;
    }

    // 判断房间是否已经存在
    public boolean isExisted(String roomId){
        return redisUtil.hget(roomId, "roomOwner") != null;
    }

    // 用户进入房间
    public void enterRoom(boolean isRoomOwner, String roomId, String username){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        // 获取该房间的 hints 最大值
        int hintsNum = (int)redisUtil.hget(roomId, "hintsNum");
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setRoomOwner(isRoomOwner);
        playerInfo.setStatus(0);
        playerInfo.setUsername(username);
        playerInfo.setX(0d);
        playerInfo.setY(30d);
        playerInfo.setZ(0d);
        playerInfo.setHintsNumLeft(hintsNum);
        redisUtil.hset(userMapKey, username, playerInfo);

        String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
        redisUtil.zSet(scoreZSetKey, username, 0);
    }

    // 用户离开房间
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

    // 判断游戏是否已经开始，返回true表示已经开始
    public boolean checkRoomStatus(String roomId){
        return !((Integer) redisUtil.hget(roomId, "status") == GameConstant.ROOM_WAITING);
    }

    // 获取全局可用房间号，并+1
    public long getAvailableRoomIdAndIncr(){
        return redisUtil.incr("availableRoomId", 1);
    }

    // 获取所有房间的信息
    public List<RoomInfo> getAllRoomInfo(){
        Set<Object> existedRoomIds = redisUtil.sGet("existedRoomId");
        List<RoomInfo> roomInfos = new LinkedList<>();
        for (Object existedRoomId : existedRoomIds){
            roomInfos.add(getRoomInfo((String) existedRoomId));
        }
        return roomInfos;
    }

    // 获取指定房间的信息
    public RoomInfo getRoomInfo(String roomId){
        // 若不存在，则直接返回
        if (!isExisted(roomId)) {
            return null;
        }

        // 获取房间信息
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setStatus((Integer) redisUtil.hget(roomId, "status"));
        roomInfo.setRoomOwner((String) redisUtil.hget(roomId, "roomOwner"));
        roomInfo.setPlayerInfos(getAllPlayerInfo(roomId));

        return roomInfo;
    }

    // 获取房间内所有玩家信息
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
}
