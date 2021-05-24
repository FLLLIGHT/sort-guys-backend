package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.controller.request.AuthRequest;
import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.security.jwt.JwtTokenUtil;
import fudan.adweb.project.sortguysbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthController(AuthService authService, JwtTokenUtil jwtTokenUtil) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("test");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        User user = authService.login(request.getUsername(), request.getPassword());
        if (user == null) return new ResponseEntity<>("账户或密码错误", HttpStatus.FORBIDDEN);
        String token = jwtTokenUtil.generateToken(user);
        // 判断该 user 账号是否已经处于登录状态
        boolean isOnline = authService.checkIsOnlineAndUpdate(user, token);
        if (isOnline){
            // 禁止该用户登录
            return new ResponseEntity<>("用户已经登录", HttpStatus.FORBIDDEN);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.header("token", token);
        return builder.body(user);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        User user = authService.login("Bob", "1234567");
        if (user == null) return new ResponseEntity<>("账户或密码错误", HttpStatus.FORBIDDEN);

        String token = jwtTokenUtil.generateToken(user);
        // 判断该 user 账号是否已经处于登录状态
        boolean isOnline = authService.checkIsOnlineAndUpdate(user, token);
        if (isOnline){
            // 禁止该用户登录
            return new ResponseEntity<>("用户已经登录", HttpStatus.FORBIDDEN);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.header("token", token);
        return builder.body(user);
    }
}
