package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.UserAppearance;
import fudan.adweb.project.sortguysbackend.service.UserAppearanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserAppearanceController {
    private final UserAppearanceService userAppearanceService;

    @Autowired
    public UserAppearanceController(UserAppearanceService userAppearanceService) {
        this.userAppearanceService = userAppearanceService;
    }

    @GetMapping("/userAppearance/{uid}")
    public ResponseEntity<?> getAppearance(@PathVariable("uid") Integer uid){
        UserAppearance appearance = userAppearanceService.getAppearance(uid);

        if (appearance == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "用户不存在");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();

        return builder.body(appearance);
    }

    @PutMapping("/userAppearance/{uid}")
    public ResponseEntity<?> modifyAppearance(@PathVariable("uid") Integer uid, @RequestBody UserAppearance userAppearance){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getUid().equals(uid)){
            Map<String, String> map = new HashMap<>();
            map.put("message", "只能修改自己的虚拟形象");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        String message = userAppearanceService.updateAppearance(uid, userAppearance);
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return builder.body(map);
    }
}
