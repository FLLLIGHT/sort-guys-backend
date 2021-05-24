package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.UserLoginInfo;
import fudan.adweb.project.sortguysbackend.mapper.UserLoginInfoMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
import fudan.adweb.project.sortguysbackend.security.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final UserLoginInfoMapper userLoginInfoMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthService(UserMapper userMapper, PasswordEncoder encoder,
                       UserLoginInfoMapper userLoginInfoMapper, JwtTokenUtil jwtTokenUtil) {
        this.userMapper = userMapper;
        this.encoder = encoder;
        this.userLoginInfoMapper = userLoginInfoMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public User login(String username, String password) throws UsernameNotFoundException, BadCredentialsException {
        Integer uid = userMapper.getUidByUsername(username);
//        if (uid == null) throw new UsernameNotFoundException("User: '" + username + "' not found.");
        if (uid == null) return null;

        User user = userMapper.getUserByUid(uid);
//        if (user == null) throw new UsernameNotFoundException("User: '" + username + "' not found.");
        if (user == null) return null;

        if (!encoder.matches(password, user.getPassword()))
            return null;
//        if (!password.equals(user.getPassword()))
//            throw new BadCredentialsException("User: '" + username + "' got wrong password.");
//        System.out.println(user);
        return user;
    }

    // 检查该用户是否已经登录，如已经登录则返回 false，否则更新登录信息表并返回 true
    public boolean checkIsOnlineAndUpdate(User user, String token) {
        Integer uid = user.getUid();
        // 1. 查 user login info 中是否有该用户
        UserLoginInfo userLoginInfo = userLoginInfoMapper.findByUid(uid);
        if (userLoginInfo == null){
            // 加进去
            userLoginInfoMapper.insert(uid, token);
            return false;
        }

        // 2. 查一下 token 信息，如果和现在新的 token 一致则登录失败（不太可能出现的情况）
        String oldToken = userLoginInfo.getToken();
        if (oldToken.equals(token)){
            return true;
        }

        // 3. 看之前的 token 是否过期，没有过期的话禁止当前登录
        try {
            jwtTokenUtil.validateToken(oldToken, user);
        }
        catch (ExpiredJwtException ex){
            // 4. 更新 login info
            userLoginInfoMapper.updateTokenByUid(uid, token);
            return false;
        }
        return true;
    }

    public void logout(Integer uid) {
        // 找到对应的 user
        if (uid == null){
            return;
        }
        User user = userMapper.getUserByUid(uid);
        if (user == null){
            return;
        }

        UserLoginInfo userLoginInfo = userLoginInfoMapper.findByUid(uid);
        if (userLoginInfo != null){
            userLoginInfoMapper.deleteByUid(uid);
        }
    }

    public void insertLoginInfo(Integer uid, String token) {
        userLoginInfoMapper.insert(uid, token);
    }
}
