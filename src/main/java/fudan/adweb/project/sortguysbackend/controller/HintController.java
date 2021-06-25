package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.constant.GameConstant;
import fudan.adweb.project.sortguysbackend.controller.request.HintRequest;
import fudan.adweb.project.sortguysbackend.entity.Garbage;
import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.service.GarbageService;
import fudan.adweb.project.sortguysbackend.service.HintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HintController {
    private final HintService hintService;
    private final GarbageService garbageService;

    @Autowired
    public HintController(HintService hintService, GarbageService garbageService) {
        this.hintService = hintService;
        this.garbageService = garbageService;
    }

    @PutMapping("/hint/{garbageName}")
    public ResponseEntity<?> getGarbageTypeByGid(@PathVariable("garbageName") String garbageName, @RequestBody HintRequest request){
        // 是不是本人
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getUid().equals(request.getUid())){
            Map<String, String> map = new HashMap<>();
            map.put("message", "uid 错误");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        Garbage garbage = garbageService.getByName(garbageName);
        if (garbage == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "garbage 不存在");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        Map<String, String> map = hintService.checkHintNum(request.getRoomId(), user.getUsername());
        if (map.get("message").equals("success")){
            map.put("type", garbage.getType());
            return builder.body(map);
        }

        // 失败的情况
        return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
    }

    @GetMapping("/hint/{roomId}/{uid}")
    public ResponseEntity<?> getHintsNumLeft(@PathVariable("roomId") Integer roomId, @PathVariable("uid") Integer uid){
        // 是不是本人
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getUid().equals(uid)){
            Map<String, String> map = new HashMap<>();
            map.put("message", "uid 错误");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        int hintsNumLeft = hintService.getHintsNumLeft(roomId, user.getUsername());
        if (hintsNumLeft == GameConstant.HINT_ROOM_NOT_EXIST){
            Map<String, String> map = new HashMap<>();
            map.put("message", "room not found");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }
        if (hintsNumLeft == GameConstant.HINT_ROOM_USER_NOT_MATCH){
            Map<String, String> map = new HashMap<>();
            map.put("message", "user and room not match");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        return builder.body(hintsNumLeft);
    }

}
