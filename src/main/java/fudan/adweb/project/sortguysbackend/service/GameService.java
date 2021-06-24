package fudan.adweb.project.sortguysbackend.service;

import com.alibaba.fastjson.JSON;
import fudan.adweb.project.sortguysbackend.constant.GameConstant;
import fudan.adweb.project.sortguysbackend.entity.Garbage;
import fudan.adweb.project.sortguysbackend.entity.Scene;
import fudan.adweb.project.sortguysbackend.entity.game.GarbageBinInfo;
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
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GameService {

    private final RedisUtil redisUtil;
    private final RoomService roomService;
    private final GarbageMapper garbageMapper;
    private final GarbageSortResultService garbageSortResultService;
    private final  UserScoreService userScoreService;

    @Autowired
    public GameService(RedisUtil redisUtil, RoomService roomService, GarbageMapper garbageMapper,
                       GarbageSortResultService garbageSortResultService, UserScoreService userScoreService){
        this.redisUtil = redisUtil;
        this.roomService = roomService;
        this.garbageMapper = garbageMapper;
        this.garbageSortResultService = garbageSortResultService;
        this.userScoreService = userScoreService;
    }

    // 更新用户在房间内的位置信息
    public void updatePosition(String roomId, String username, Double x, Double y, Double z){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        PlayerInfo playerInfo = (PlayerInfo) redisUtil.hget(userMapKey, username);
        playerInfo.setX(x);
        playerInfo.setY(y);
        playerInfo.setZ(z);
        redisUtil.hset(userMapKey, username, playerInfo);
    }

    // 更新用户的准备状态（1：未准备  2：准备  3：游戏中）
    public void updatePlayerStatus(String roomId, String username, int status){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        PlayerInfo playerInfo = (PlayerInfo) redisUtil.hget(userMapKey, username);
        playerInfo.setStatus(status);
        redisUtil.hset(userMapKey, username, playerInfo);
    }

    // 统一更新所有用户的准备状态（1：未准备  2：准备  3：游戏中）
    public void updateAllPlayerStatus(String roomId, int status){
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        Map<Object, Object> userMap = redisUtil.hmget(userMapKey);
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            PlayerInfo playerInfo = (PlayerInfo) entry.getValue();
            playerInfo.setStatus(status);
            redisUtil.hset(userMapKey, playerInfo.getUsername(), playerInfo);
        }
    }

    // 准备
    public String getReady(String roomId, String username){
        updatePlayerStatus(roomId, username, GameConstant.PLAYER_READY);
        return "准备成功";
    }

    // 取消准备
    public String getUnready(String roomId, String username){
        updatePlayerStatus(roomId, username, GameConstant.PLAYER_NOT_READY);
        return "取消准备成功";
    }

    // 开始游戏
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
            generateGarbageInRoom(garbage, garbageMapKey, getSceneInfo(roomId));
        }

        // todo: 将用户位置归到中心/随机点

        return "游戏开始！";
    }

    // 在房间内随机生成垃圾（添加到redis）——内部方法
    private GarbageInfo generateGarbageInRoom(Garbage garbage, String garbageMapKey, Scene scene){
        String garbageId = UUID.randomUUID().toString().replaceAll("-","");
        GarbageInfo garbageInfo = new GarbageInfo();
        garbageInfo.setGarbageId(garbageId);
        // 是否是疑难垃圾
        garbageInfo.setScore(GameConstant.GARBAGE_BASIC_SCORE + (garbageSortResultService.isGarbagePuzzle(garbage.getGid()) ? GameConstant.GARBAGE_PUZZLE_SCORE : 0));

        // 生成在随机位置
        int randomX = ThreadLocalRandom.current().nextInt(scene.getMinX(), scene.getMaxX() + 1);
        int randomZ = ThreadLocalRandom.current().nextInt(scene.getMinZ(), scene.getMaxZ() + 1);
        garbageInfo.setX((double) randomX);
        garbageInfo.setY(30d);
        garbageInfo.setZ((double) randomZ);

        garbageInfo.setGarbageName(garbage.getName());
        garbageInfo.setType(GameConstant.GARBAGE_TYPE_MAP.get(garbage.getType()));
        redisUtil.hset(garbageMapKey, garbageId, garbageInfo);
        return garbageInfo;
    }

    // 在房间内随机生成垃圾（添加到redis）——对外暴露
    public GarbageInfo generateGarbageInRoom(String roomId){
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        Garbage garbage = garbageMapper.findRandom1();
//        return generateGarbageInRoom(garbage, roomId);
        return generateGarbageInRoom(garbage, garbageMapKey, getSceneInfo(roomId));
    }

    // 暂停游戏
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
        updateAllPlayerHintsNum(roomId, (int) redisUtil.hget(roomId, "hintsNum"));
        updateAllPlayerCorrectNum(roomId, 0);

        // TODO: 更新垃圾分类结果信息，用户总得分信息（MySQL）
        updateUserScoreToSQL(roomId);

        return list;
    }

    private void updateUserScoreToSQL(String roomId) {
        List<ScoreInfo> scoreList = getScoreList(roomId);
        for (ScoreInfo scoreInfo: scoreList){
            userScoreService.updateUserScore(scoreInfo.getUsername(), scoreInfo.getScore());
        }
    }

    private void updateAllPlayerCorrectNum(String roomId, int num) {
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        Map<Object, Object> userMap = redisUtil.hmget(userMapKey);
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            PlayerInfo playerInfo = (PlayerInfo) entry.getValue();
            playerInfo.setCorrectNum(num);
            redisUtil.hset(userMapKey, playerInfo.getUsername(), playerInfo);
        }
    }

    // 更新房间中所有玩家的提示次数信息
    private void updateAllPlayerHintsNum(String roomId, int hintsNum) {
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        Map<Object, Object> userMap = redisUtil.hmget(userMapKey);
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            PlayerInfo playerInfo = (PlayerInfo) entry.getValue();
            playerInfo.setHintsNumLeft(hintsNum);
            redisUtil.hset(userMapKey, playerInfo.getUsername(), playerInfo);
        }
    }

    // 获取房间内所有垃圾信息
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

    // 拾取垃圾
    public GarbageInfo pickUpGarbage(String roomId, String username, String garbageId){
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        GarbageInfo garbageInfo = (GarbageInfo) redisUtil.hget(garbageMapKey, garbageId);
        garbageInfo.setUsername(username);
        redisUtil.hset(garbageMapKey, garbageId, garbageInfo);
        return garbageInfo;
    }

    // 扔垃圾
    public GarbageInfo throwGarbage(String roomId, String username, String garbageId, int garbageBinType){
        // 判断垃圾扔的是否正确
        String garbageMapKey = (String) redisUtil.hget(roomId, "garbageMapKey");
        GarbageInfo garbageInfo = (GarbageInfo) redisUtil.hget(garbageMapKey, garbageId);
        boolean correct = checkGarbageType(garbageInfo, garbageBinType);

        int correctNum = updateCorrectNum(roomId, username, correct);

        // 从redis中移除垃圾
        redisUtil.hdel(garbageMapKey, garbageId);

        // 若扔对了，添加分数
        if(correct){
            int bonusScore = 0;
            if (correctNum > 1){
                correctNum = Math.min(5, correctNum);
                // 连续分对垃圾的附加分
                bonusScore = GameConstant.GARBAGE_BONUS_CORRECT_SCORE.get(correctNum - 1);
            }
            String scoreZSetKey = (String) redisUtil.hget(roomId, "scoreZSetKey");
            redisUtil.zIncr(scoreZSetKey, username, garbageInfo.getScore() + bonusScore);

            // 在回传信息中设置本次实际得分
            garbageInfo.setScore(garbageInfo.getScore() + bonusScore);
        } else {
            // 若扔错，则设为0分（实际得分为0分）
            garbageInfo.setScore(0);
        }

        // todo: 添加拾取记录（到MySQL）

        return garbageInfo;
    }

    // 验证垃圾分类是否正确
    private boolean checkGarbageType(GarbageInfo garbageInfo, int garbageBinType){
        return garbageInfo.getType() == garbageBinType;
    }

    // 获取当前用户的剩余提示数并更新（如果可以更新的话）
    public Map<String, String> updateHints(String roomId, String username, int updateValue) {
        Map<String, String> map = new HashMap<>();
        if (!roomService.isExisted(roomId)) {
            map.put("message", "room not found");
            return map;
        }
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        Map<Object, Object> userMap = redisUtil.hmget(userMapKey);
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            PlayerInfo playerInfo = (PlayerInfo) entry.getValue();
            if (playerInfo.getUsername().equals(username)){
                int hintsNumLeft = playerInfo.getHintsNumLeft();
                if (hintsNumLeft >= 1){
                    playerInfo.setHintsNumLeft(hintsNumLeft + updateValue);
                    redisUtil.hset(userMapKey, playerInfo.getUsername(), playerInfo);
                    map.put("message", "success");
                    map.put("hintsNumLeft", String.valueOf(playerInfo.getHintsNumLeft()));
                }
                else {
                    map.put("message", "hint num not enough");
                }
                return map;
            }
        }
        map.put("message", "user and room not match");
        return map;
    }

    public int getHintsNumLeft(String roomId, String username) {
        if (!roomService.isExisted(roomId)) {
            return GameConstant.HINT_ROOM_NOT_EXIST;
        }
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        Map<Object, Object> userMap = redisUtil.hmget(userMapKey);
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            PlayerInfo playerInfo = (PlayerInfo) entry.getValue();
            if (playerInfo.getUsername().equals(username)){
                return playerInfo.getHintsNumLeft();
            }
        }
        return GameConstant.HINT_ROOM_USER_NOT_MATCH;
    }

    // 更新用户的连续正确数，并返回更新后的正确数，如出现问题则返回 -1
    private int updateCorrectNum(String roomId, String username, boolean correct) {
        if (!roomService.isExisted(roomId)) {
            return -1;
        }
        String userMapKey = (String) redisUtil.hget(roomId, "userMapKey");
        Map<Object, Object> userMap = redisUtil.hmget(userMapKey);
        for (Map.Entry<Object, Object> entry : userMap.entrySet()){
            PlayerInfo playerInfo = (PlayerInfo) entry.getValue();
            if (playerInfo.getUsername().equals(username)){
                int oldCorrectNum = playerInfo.getCorrectNum();
                playerInfo.setCorrectNum(correct ? oldCorrectNum + 1 : 0);
                redisUtil.hset(userMapKey, username, playerInfo);
                return playerInfo.getCorrectNum();
            }
        }
        return -1;
    }

    private Scene getSceneInfo(String roomId){
        return (Scene) redisUtil.hget(roomId, "scene");
    }

    public List<GarbageBinInfo> generateGarbageBins(String roomId){
        Scene scene = getSceneInfo(roomId);
        int maxX = scene.getMaxX();
        int minX = scene.getMinX();
        int maxZ = scene.getMaxZ();
        int minZ = scene.getMinZ();
        // 取整
        int midX = minX + (maxX - minX) / 2;
        int midZ = minZ + (maxZ - minZ) / 2;

        // 生成在随机位置
        List<GarbageBinInfo> garbageBinInfos = new ArrayList<>();
        garbageBinInfos.add(generateGarbageBin(minX, midX-2, minZ, midZ-2));
        garbageBinInfos.add(generateGarbageBin(minX, midX-2, midZ, maxZ-2));
        garbageBinInfos.add(generateGarbageBin(midX, maxX-2, minZ, midZ-2));
        garbageBinInfos.add(generateGarbageBin(midX, maxX-2, midZ, maxZ-2));
        return garbageBinInfos;
    }

    public GarbageBinInfo generateGarbageBin(int minX, int maxX, int minZ, int maxZ){
        // 生成在随机位置
        int randomX = ThreadLocalRandom.current().nextInt(minX, maxX + 1);
        int randomZ = ThreadLocalRandom.current().nextInt(minZ, maxZ + 1);
        return new GarbageBinInfo((double)randomX, (double)randomX+2, (double)randomZ, (double)randomZ+2);
    }
}
