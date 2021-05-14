package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.controller.request.AuthRequest;
import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.UserInfo;
import fudan.adweb.project.sortguysbackend.security.jwt.JwtTokenUtil;
import fudan.adweb.project.sortguysbackend.service.AuthService;
import fudan.adweb.project.sortguysbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthService authService;

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil, AuthService authService) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authService = authService;
    }

    @PostMapping("/user")
    public ResponseEntity<?> newUser(@RequestBody UserInfo userInfo) {
        User user = userService.addUser(userInfo.getUsername(), userInfo.getPassword());
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        // 用户名重复，该用户已存在
        if (user == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "用户名已被注册");
            return builder.body(map);
        }
        String token = jwtTokenUtil.generateToken(user);
        builder.header("token", token);
        authService.insertLoginInfo(user.getUid(), token);
        return builder.body(user);
    }

    @PutMapping("/user/{uid}")
    public ResponseEntity<?> modifyUser(@PathVariable("uid") Integer uid, @RequestBody UserInfo userInfo){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        String message = userService.update(uid, userInfo);
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        // 登出
        if (message.equals("success")){
            authService.logout(uid);
        }

        return builder.body(map);
    }

}
