package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.*;
import fudan.adweb.project.sortguysbackend.mapper.GarbageMapper;
import fudan.adweb.project.sortguysbackend.mapper.GarbageSortResultMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GarbageSortResultService {
    private final GarbageSortResultMapper garbageSortResultMapper;
    private final GarbageMapper garbageMapper;
    private final UserMapper userMapper;

    @Autowired
    public GarbageSortResultService(GarbageSortResultMapper garbageSortResultMapper, GarbageMapper garbageMapper,
                                    UserMapper userMapper) {
        this.garbageSortResultMapper = garbageSortResultMapper;
        this.garbageMapper = garbageMapper;
        this.userMapper = userMapper;
    }


    // 初始化表
    public void init(Integer uid) {
        // 获取所有的垃圾 gid
        List<Integer> gidList = garbageMapper.findGidOfAll();
        // 初始化
        for (Integer gid: gidList){
            garbageSortResultMapper.insert(gid, uid);
        }
    }

    public List<GarbageSortResultInfo> getSortResult(Integer uid) {
        if (userMapper.getUserByUid(uid) == null){
            return null;
        }

        // 查找该用户的垃圾图鉴并返回
        List<GarbageSortResultInfo> result = new ArrayList<>();

        List<GarbageSortResult> results = garbageSortResultMapper.findByUid(uid);
        for (GarbageSortResult re: results){
            Integer gid = re.getGid();
            Garbage garbage = garbageMapper.findByGid(gid);
            GarbageSortResultInfo info = new GarbageSortResultInfo(gid, garbage.getName(), garbage.getCname(), garbage.getDescription(),
                    re.getTimes(), re.getCorrectTimes(), re.getUnlockTime(), garbage.getUrl());
            result.add(info);
        }

        return result;
    }

    public List<GarbageSortResultInfo> getAllSortResult() {
        List<GarbageSortResultInfo> result = new ArrayList<>();
        List<Garbage> garbageList = garbageMapper.findAll();
        for (Garbage g: garbageList){
            GarbageSortResultInfo info = new GarbageSortResultInfo(g.getGid(), g.getName(), g.getCname(), g.getDescription(),
                    0, 0, g.getUpdateTime(), g.getUrl());
            result.add(info);
        }

        Map<Integer, GarbageSortResultInfo> map = new HashMap<>();
        // 获取所有的用户
        List<UserInfo> users = userMapper.getAll();
        List<GarbageSortResultInfo> temp;
        for (UserInfo info: users){
            temp = getSortResult(info.getUid());
            for (GarbageSortResultInfo i: temp){
                if (map.containsKey(i.getGid())){
                    Integer newTimes = map.get(i.getGid()).getTimes() + i.getTimes();
                    Integer newCorrectTimes = map.get(i.getGid()).getCorrectTimes() + i.getCorrectTimes();
                    i.setTimes(newTimes);
                    i.setCorrectTimes(newCorrectTimes);
                }
                map.put(i.getGid(), i);
            }
        }

        // 得到 result
        for (GarbageSortResultInfo info: result){
            info.setTimes(map.get(info.getGid()).getTimes());
            info.setCorrectTimes(map.get(info.getGid()).getCorrectTimes());
        }

        return result;
    }

    // 判断是否是疑难垃圾
    public boolean isGarbagePuzzle(Integer gid){
        int correctNum = 0;
        int totalNum = 0;
        // 遍历所有的 sort result
        List<GarbageSortResult> sortResults = garbageSortResultMapper.findByGid(gid);
        for(GarbageSortResult result: sortResults){
            correctNum += result.getCorrectTimes();
            totalNum += result.getTimes();
        }

        if (totalNum == 0) return false;
        if (correctNum == 0) return true;

        return (double) correctNum / (double) totalNum <= 0.5;
    }

    public void updateFalseResult(Integer gid, Integer uid){
        garbageSortResultMapper.updateFalseResult(gid, uid);
    }

    public void updateCorrectResult(Integer gid, Integer uid){
        garbageSortResultMapper.updateCorrectResult(gid, uid);
    }

    public void unlock(Integer gid, Integer uid){
        System.out.println("gid: " + gid + ", uid = " + uid);
        GarbageSortResult garbageSortResult = garbageSortResultMapper.findByGidAndUid(gid, uid);
        if(garbageSortResult.getUnlockTime()==null){
            garbageSortResultMapper.updateUnlockTime(new Date(), gid, uid);
        }
    }
}
