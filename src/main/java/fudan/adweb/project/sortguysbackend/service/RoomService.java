package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.constant.GameConstant;
import fudan.adweb.project.sortguysbackend.entity.Scene;
import fudan.adweb.project.sortguysbackend.entity.UserAppearance;
import fudan.adweb.project.sortguysbackend.entity.game.GarbageSortResultRedisInfo;
import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import fudan.adweb.project.sortguysbackend.entity.game.RoomInfo;
import fudan.adweb.project.sortguysbackend.entity.game.ScoreInfo;
import fudan.adweb.project.sortguysbackend.mapper.SceneMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserAppearanceMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserAuthorityMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
import fudan.adweb.project.sortguysbackend.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RoomService {
    private RedisUtil redisUtil;
    private SceneService sceneService;
    private UserAppearanceService userAppearanceService;

    @Autowired
    public RoomService(RedisUtil redisUtil, SceneService sceneService, UserAppearanceService userAppearanceService){
        this.sceneService = sceneService;
        this.redisUtil = redisUtil;
        this.userAppearanceService = userAppearanceService;
    }

    // 创建房间（指定房间号）
    // 并发控制：防止两个用户同时创建相同roomId
    public String createRoom(String roomId, String roomOwner, int hintsNum, int sid){
        if(isExisted(roomId)){
            return "room already exists";
        }

        // 创建新房间并设置房间信息
        Map<String, Object> map = new HashMap<>();
        map.put("roomOwner", roomOwner);
        map.put("status", GameConstant.ROOM_WAITING);
        map.put("hintsNum", hintsNum);
        map.put("roomId", roomId);

        String userMapKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("userMapKey", userMapKey);

        String garbageMapKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("garbageMapKey", garbageMapKey);

        List<GarbageSortResultRedisInfo> garbageSortResultInfos = new LinkedList<>();
        map.put("garbageSortResultInfos", garbageSortResultInfos);

        String scoreZSetKey = UUID.randomUUID().toString().replaceAll("-","");
        map.put("scoreZSetKey", scoreZSetKey);

        map.put("scene", sceneService.getSceneBySid(sid));

        redisUtil.hmset(roomId, map);

        // 创建房间的用户集合，并将房主放入
        // 现在放到连接时一起加入
//        enterRoom(true, roomId, roomOwner);

        // 加入总的房间
        redisUtil.sSet("existedRoomId", roomId);

        return roomId;
    }

    public void delGarbageInfo(String roomId){
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        Map<Object, Object> garbageMap = redisUtil.hmget(garbageMapKey);
        for (Map.Entry<Object, Object> entry : garbageMap.entrySet()){
            redisUtil.hdel(garbageMapKey, (String) entry.getKey());
        }
    }

    // 归零
    public void rtzScoreInfo(String roomId){
        String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
        Set<ZSetOperations.TypedTuple<Object>> rangeWithScores = redisUtil.zReverseRangeWithScore(scoreZSetKey, 0, 4);

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = rangeWithScores.iterator();
        while(iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            redisUtil.zSet(scoreZSetKey, (String) next.getValue(), 0);
        }
    }

    // 判断房间是否已经存在
    public boolean isExisted(String roomId){
        return redisUtil.hget(roomId, "roomOwner") != null;
    }

    // 用户进入房间
    public PlayerInfo enterRoom(boolean isRoomOwner, String roomId, String username){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        // 获取该房间的 hints 最大值
        int hintsNum = (int)redisUtil.hget(roomId, "hintsNum");
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.setRoomOwner(isRoomOwner);
        playerInfo.setStatus(1);
        playerInfo.setUsername(username);

        // 设置用户颜色！
        UserAppearance userAppearance = userAppearanceService.getAppearance(username);
        playerInfo.setColor(userAppearance.getColor());

        Scene scene = (Scene) redisUtil.hget(roomId, "scene");
        // 生成在随机位置
        int randomX = ThreadLocalRandom.current().nextInt(8, scene.getMaxX());
        int randomZ = ThreadLocalRandom.current().nextInt(8, scene.getMaxZ());
        int randomXF = ThreadLocalRandom.current().nextBoolean() ? -1 : 1;
        int randomZF = ThreadLocalRandom.current().nextBoolean() ? -1 : 1;
        playerInfo.setX((double)randomX * randomXF);
        playerInfo.setY(30d);
        playerInfo.setZ((double)randomZ * randomZF);
        playerInfo.setRotation(0d);
        playerInfo.setHintsNumLeft(hintsNum);
        playerInfo.setCorrectNum(0);
        redisUtil.hset(userMapKey, username, playerInfo);

        String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
        redisUtil.zSet(scoreZSetKey, username, 0);
        return playerInfo;
    }

    // 用户离开房间
    public String leaveRoom(String roomId, String username){
        // todo: 判断游戏是否已经开始？

        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
        redisUtil.hdel(userMapKey, username);
        redisUtil.zSetRemove(scoreZSetKey, username);

        //判断是否是房主？
        String newRoomOwner = "";
        boolean isRoomOwner = checkRoomOwner(roomId, username);
        if (isRoomOwner) {
            Set<PlayerInfo> playerInfos = getAllPlayerInfo(roomId);
            if (playerInfos.size() == 0) {
                // 房间剩余人数为0，删除房间
                delGarbageInfo(roomId);
                redisUtil.hdel(roomId, "roomOwner", "status",
                        "hintsNum", "roomId", "userMapKey", "garbageMapKey",
                        "garbageSortResultInfos", "scoreZSetKey", "scene");
                redisUtil.setRemove("existedRoomId", roomId);
            } else {
                // 房间剩余人数不为0，随机移交房主
                PlayerInfo playerInfo = playerInfos.iterator().next();
                playerInfo.setRoomOwner(true);
                newRoomOwner = playerInfo.getUsername();

                redisUtil.hset(userMapKey, newRoomOwner, playerInfo);
                redisUtil.hset(roomId, "roomOwner", newRoomOwner);
            }
        }

        return newRoomOwner;
    }

    // 判断用户是否是房主
    public boolean checkRoomOwner(String roomId, String username){
        return redisUtil.hget(roomId, "roomOwner").equals(username);
    }

    public String getRoomOwner(String roomId){
        return (String) redisUtil.hget(roomId, "roomOwner");
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
        roomInfo.setHintsNum((int) redisUtil.hget(roomId, "hintsNum"));
        roomInfo.setRoomId(roomId);

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
