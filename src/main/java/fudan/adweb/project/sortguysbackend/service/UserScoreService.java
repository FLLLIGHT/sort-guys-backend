package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserScoreMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserScoreService {
    private final UserScoreMapper userScoreMapper;
    private final UserMapper userMapper;

    @Autowired
    public UserScoreService(UserScoreMapper userScoreMapper, UserMapper userMapper) {
        this.userScoreMapper = userScoreMapper;
        this.userMapper = userMapper;
    }

    public void init(Integer uid) {
        userScoreMapper.insert(uid, 0);
    }

    public Integer getScoreByUid(Integer uid) {
        User user = userMapper.getUserByUid(uid);
        if (user == null){
            return null;
        }
        return userScoreMapper.findScoreByUid(uid);
    }
}
