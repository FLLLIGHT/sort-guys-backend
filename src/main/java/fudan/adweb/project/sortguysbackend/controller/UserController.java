package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.controller.request.GetAppearanceRequest;
import fudan.adweb.project.sortguysbackend.entity.UserAppearance;
import fudan.adweb.project.sortguysbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/getAppearance")
    public ResponseEntity<?> getAppearance(@RequestBody GetAppearanceRequest request){
        String username = request.getUsername();
        UserAppearance appearance = userService.getAppearance(username);

        if (appearance == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "用户不存在");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();

        return builder.body(appearance);
    }
}
