package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.UserInfo;
import fudan.adweb.project.sortguysbackend.entity.UserLoginInfo;
import fudan.adweb.project.sortguysbackend.mapper.UserAuthorityMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserLoginInfoMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserAppearanceService userAppearanceService;
    private final UserAuthorityMapper userAuthorityMapper;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserAppearanceService userAppearanceService, UserAuthorityMapper userAuthorityMapper,
                       UserMapper userMapper, PasswordEncoder encoder) {
        this.userAppearanceService = userAppearanceService;
        this.userAuthorityMapper = userAuthorityMapper;
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    public User addUser(String username, String password) {
        Integer uid = userMapper.getUidByUsername(username);
        if(uid!=null){
            return null;
        }else {
            String encodedPassword = encoder.encode(password.trim());
            User newUser = new User(username, encodedPassword);
            userMapper.insert(newUser);
            // 默认注册为玩家账号
            userAuthorityMapper.insert(1, newUser.getUid());
            // 初始化外观
            userAppearanceService.initAppearance(newUser.getUid());
            return newUser;
        }
    }

    public String update(Integer uid, UserInfo userInfo) {
        if (uid == null || userInfo == null){
            return "uid 错误";
        }

        Integer uidOfInfo = userMapper.getUidByUsername(userInfo.getUsername());
        // 该用户名以及被使用且不是这个用户
        if (uidOfInfo != null && !uid.equals(uidOfInfo)){
            return "用户名已被注册";
        }

        String encodedPassword = encoder.encode(userInfo.getPassword().trim());
        userInfo.setPassword(encodedPassword);
        userInfo.setUid(uid);
        userMapper.update(userInfo);
        return "success";
    }
}
