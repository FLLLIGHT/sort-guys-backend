package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.UserAppearance;
import fudan.adweb.project.sortguysbackend.mapper.UserAppearanceMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAppearanceService {

    private final UserAppearanceMapper userAppearanceMapper;
    private final UserMapper userMapper;

    @Autowired
    public UserAppearanceService(UserAppearanceMapper userAppearanceMapper, UserMapper userMapper) {
        this.userAppearanceMapper = userAppearanceMapper;
        this.userMapper = userMapper;
    }

    public void initAppearance(Integer uid){
        // 初始化为白色
        userAppearanceMapper.insert(uid, "#FFFFFF");
    }

    public UserAppearance getAppearance(Integer uid) {
        if (uid == null) return null;
        return userAppearanceMapper.findByUid(uid);
    }

    public String updateAppearance(Integer uid, UserAppearance userAppearance) {
        if (uid == null || userAppearance == null || !uid.equals(userAppearance.getUid())){
            return "uid 错误";
        }
        userAppearanceMapper.update(userAppearance);
        return "success";
    }
}
