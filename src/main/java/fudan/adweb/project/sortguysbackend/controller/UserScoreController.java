package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.service.UserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserScoreController {
    private final UserScoreService userScoreService;

    @Autowired
    public UserScoreController(UserScoreService userScoreService) {
        this.userScoreService = userScoreService;
    }

    @GetMapping("/score/{uid}")
    public ResponseEntity<?> getScore(@PathVariable("uid") Integer uid){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        Integer score = userScoreService.getScoreByUid(uid);
        if (score == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "uid 不存在");
            return builder.body(map);
        }
        Map<String, Integer> map = new HashMap<>();
        map.put("message", score);
        return builder.body(map);
    }

}
