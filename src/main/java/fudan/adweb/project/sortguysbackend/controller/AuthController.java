package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.controller.request.AuthRequest;
import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.security.jwt.JwtTokenUtil;
import fudan.adweb.project.sortguysbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.header("token", jwtTokenUtil.generateToken(user));
        return builder.body(user);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login() {
        User user = authService.login("Bob", "123456");
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.header("token", jwtTokenUtil.generateToken(user));
        return builder.body(user);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        User user = authService.register(request.getUsername(), request.getPassword());
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.header("token", jwtTokenUtil.generateToken(user));
        return builder.body(user);
    }

    @GetMapping("/register")
    public ResponseEntity<?> register() {
        User user = authService.register("Cathy", "123456");
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        builder.header("token", jwtTokenUtil.generateToken(user));
        return builder.body(user);
    }
}