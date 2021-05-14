package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.controller.request.AuthRequest;
import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.UserInfo;
import fudan.adweb.project.sortguysbackend.security.jwt.JwtTokenUtil;
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

    @Autowired
    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
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
        builder.header("token", jwtTokenUtil.generateToken(user));
        return builder.body(user);
    }

    @PutMapping("/user/{uid}")
    public ResponseEntity<?> modifyUser(@PathVariable("uid") Integer uid, @RequestBody UserInfo userInfo){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        String message = userService.update(uid, userInfo);
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return builder.body(map);
    }

}
