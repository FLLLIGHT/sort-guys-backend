package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthService(UserMapper userMapper, PasswordEncoder encoder) {
        this.userMapper = userMapper;
        this.encoder = encoder;
    }

    public User login(String username, String password) throws UsernameNotFoundException, BadCredentialsException {
        Integer uid = userMapper.getUidByUsername(username);
        if (uid == null) throw new UsernameNotFoundException("User: '" + username + "' not found.");
        User user = userMapper.getUserByUid(uid);
        if (user == null) throw new UsernameNotFoundException("User: '" + username + "' not found.");

//        if (!encoder.matches(password, user.getPassword()))
        if (!password.equals(user.getPassword()))
            throw new BadCredentialsException("User: '" + username + "' got wrong password.");
        System.out.println(user);
        return user;
    }
}
