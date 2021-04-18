package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.mapper.UserAuthorityMapper;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
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

    @Autowired
    public AuthService(UserMapper userMapper, UserAuthorityMapper userAuthorityMapper, PasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.userAuthorityMapper = userAuthorityMapper;
        this.encoder = encoder;
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
}
