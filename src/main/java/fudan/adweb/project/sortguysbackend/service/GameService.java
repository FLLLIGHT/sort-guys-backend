package fudan.adweb.project.sortguysbackend.service;

import com.alibaba.fastjson.JSON;
import fudan.adweb.project.sortguysbackend.constant.GameConstant;
import fudan.adweb.project.sortguysbackend.entity.Garbage;
import fudan.adweb.project.sortguysbackend.entity.game.GarbageInfo;
import fudan.adweb.project.sortguysbackend.entity.game.PlayerInfo;
import fudan.adweb.project.sortguysbackend.entity.game.ScoreInfo;
import fudan.adweb.project.sortguysbackend.mapper.GarbageMapper;
import fudan.adweb.project.sortguysbackend.util.RedisUtil;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.swing.text.PlainDocument;
import java.util.*;

@Service
public class GameService {

    private final RedisUtil redisUtil;
    private final RoomService roomService;
    private final GarbageMapper garbageMapper;

    @Autowired
    public GameService(RedisUtil redisUtil, RoomService roomService, GarbageMapper garbageMapper){
        this.redisUtil = redisUtil;
        this.roomService = roomService;
        this.garbageMapper = garbageMapper;
    }

    public void updatePosition(String roomId, String username, Double x, Double y, Double z){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        PlayerInfo playerInfo = (PlayerInfo) redisUtil.hget(userMapKey, username);
        playerInfo.setX(x);
        playerInfo.setY(y);
        playerInfo.setZ(z);
        redisUtil.hset(userMapKey, username, playerInfo);
    }

    public void updatePlayerStatus(String roomId, String username, int status){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        PlayerInfo playerInfo = (PlayerInfo) redisUtil.hget(userMapKey, username);
        playerInfo.setStatus(status);
        redisUtil.hset(userMapKey, username, playerInfo);
    }

    public void updateAllPlayerStatus(String roomId, int status){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        Map<Object, Object> userMap = redisUtil.hmget(userMapKey);
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            PlayerInfo playerInfo = (PlayerInfo) entry.getValue();
            playerInfo.setStatus(status);
            redisUtil.hset(userMapKey, playerInfo.getUsername(), playerInfo);
        }
    }

    public String getReady(String roomId, String username){
        // todo: 判断用户是不是已经准备好了

        updatePlayerStatus(roomId, username, GameConstant.PLAYER_READY);
        return "准备成功";
    }

    public String getStart(String roomId, String username){
        // 检查是否是房主
        if (!roomService.checkRoomOwner(roomId, username)) {
            return "不是房主，没有权限";
        }

        // 检查房间内用户是否都已准备
        if (!roomService.checkReadyStatus(roomId)) {
            return "房间内有用户尚未准备";
        }

        // 将房间内所有用户状态变为游戏中
        updateAllPlayerStatus(roomId, GameConstant.PLAYER_IN_GAME);

        // 更新房间状态
        redisUtil.hset(roomId, "status", GameConstant.ROOM_GAMING);

        // 在房间内随机生成垃圾
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        List<Garbage> garbageList = garbageMapper.findRandom5();
        for (Garbage garbage : garbageList) {
            generateGarbageInRoom(garbage, garbageMapKey);
        }

        // todo: 将用户位置归到中心/随机点

        return "游戏开始！";
    }

    private GarbageInfo generateGarbageInRoom(Garbage garbage, String garbageMapKey){
        String garbageId = UUID.randomUUID().toString().replaceAll("-","");
        GarbageInfo garbageInfo = new GarbageInfo();
        garbageInfo.setGarbageId(garbageId);
        garbageInfo.setScore(1);
        // todo：生成在随机位置
        garbageInfo.setX(0d);
        garbageInfo.setY(30d);
        garbageInfo.setZ(0d);
        garbageInfo.setGarbageName(garbage.getName());
        garbageInfo.setType(GameConstant.GARBAGE_TYPE_MAP.get(garbage.getType()));
        redisUtil.hset(garbageMapKey, garbageId, garbageInfo);
        return garbageInfo;
    }

    public GarbageInfo generateGarbageInRoom(String roomId){
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        Garbage garbage = garbageMapper.findRandom1();
        return generateGarbageInRoom(garbage, roomId);
    }

    public String getStop(String roomId){
        redisUtil.hset(roomId, "status", GameConstant.ROOM_STOPPING);
        return "暂停成功";
    }

    // 获取排行榜
    public List<ScoreInfo> getScoreList(String roomId){
        String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
        Set<ZSetOperations.TypedTuple<Object>> rangeWithScores = redisUtil.zReverseRangeWithScore(scoreZSetKey, 0, 4);

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = rangeWithScores.iterator();
        List<ScoreInfo> list = new LinkedList<>();
        while(iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            list.add(new ScoreInfo((String) next.getValue(), next.getScore()));
        }

        return list;
    }

    // 游戏结束，删除游戏相关信息，将游戏记录写入MySQL
    public List<ScoreInfo> getOver(String roomId, String username){
        // 删除游戏相关信息 或 归零
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        Map<Object, Object> garbageMap = redisUtil.hmget(garbageMapKey);
        for (Map.Entry<Object, Object> entry : garbageMap.entrySet()){
            redisUtil.hdel(garbageMapKey, (String) entry.getKey());
        }

        List<ScoreInfo> list = getScoreList(roomId);

        // 更新房间/用户状态
        redisUtil.hset(roomId, "status", GameConstant.ROOM_WAITING);
        updateAllPlayerStatus(roomId, GameConstant.PLAYER_NOT_READY);

        return list;
    }

    public List<GarbageInfo> getAllGarbageInfo(String roomId){
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        return castFromObjectToGarbageInfo(Objects.requireNonNull(redisUtil.hmget(garbageMapKey)));
    }

    private List<GarbageInfo> castFromObjectToGarbageInfo(Map<Object, Object> garbageMap){
        List<GarbageInfo> list = new LinkedList<>();
        for (Map.Entry<Object, Object> entry : garbageMap.entrySet()){
            list.add((GarbageInfo) entry.getValue());
        }
        return list;
    }

    public GarbageInfo pickUpGarbage(String roomId, String username, String garbageId){
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        GarbageInfo garbageInfo = (GarbageInfo) redisUtil.hget(garbageMapKey, garbageId);
        garbageInfo.setUsername(username);
        redisUtil.hset(garbageMapKey, garbageId, garbageInfo);
        return garbageInfo;
    }

    public GarbageInfo throwGarbage(String roomId, String username, String garbageId, int garbageBinType){
        // 判断垃圾扔的是否正确
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        GarbageInfo garbageInfo = (GarbageInfo) redisUtil.hget(garbageMapKey, garbageId);
        boolean correct = checkGarbageType(garbageInfo, garbageBinType);

        // 从redis中移除垃圾
        redisUtil.hdel(garbageMapKey, garbageId);

        // 若扔对了，添加分数
        if(correct){
            String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
            redisUtil.zIncr(scoreZSetKey, username, garbageInfo.getScore());
        }

        // 添加拾取记录（到MySQL）

        return garbageInfo;
    }

    private boolean checkGarbageType(GarbageInfo garbageInfo, int garbageBinType){
        return garbageInfo.getType() == garbageBinType;
    }

}
