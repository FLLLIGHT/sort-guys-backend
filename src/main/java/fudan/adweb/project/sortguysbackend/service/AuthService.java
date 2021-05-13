package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.UserLoginInfo;
import fudan.adweb.project.sortguysbackend.mapper.UserAuthorityMapper;
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
    private final UserAuthorityMapper userAuthorityMapper;
    private final PasswordEncoder encoder;
    private final UserLoginInfoMapper userLoginInfoMapper;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthService(UserMapper userMapper, UserAuthorityMapper userAuthorityMapper, PasswordEncoder encoder,
                       UserLoginInfoMapper userLoginInfoMapper, JwtTokenUtil jwtTokenUtil) {
        this.userMapper = userMapper;
        this.userAuthorityMapper = userAuthorityMapper;
        this.encoder = encoder;
        this.userLoginInfoMapper = userLoginInfoMapper;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public User login(String username, String password) throws UsernameNotFoundException, BadCredentialsException {
        Integer uid = userMapper.getUidByUsername(username);
        if (uid == null) throw new UsernameNotFoundException("User: '" + username + "' not found.");
        User user = userMapper.getUserByUid(uid);
        if (user == null) throw new UsernameNotFoundException("User: '" + username + "' not found.");

        if (!encoder.matches(password, user.getPassword()))
//        if (!password.equals(user.getPassword()))
            throw new BadCredentialsException("User: '" + username + "' got wrong password.");
        System.out.println(user);
        return user;
    }

    public User register(String username, String password) {
        Integer uid = userMapper.getUidByUsername(username);
        if(uid!=null){
            return null;
        }else {
            String encodedPassword = encoder.encode(password.trim());
            User newUser = new User(username, encodedPassword);
            userMapper.insert(newUser);
            //默认注册为玩家账号
            userAuthorityMapper.insert(1, newUser.getUid());
            return newUser;
        }
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
}
