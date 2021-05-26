package fudan.adweb.project.sortguysbackend.controller;

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

    @PutMapping("/hint/{gid}")
    public ResponseEntity<?> getGarbageTypeByGid(@PathVariable("gid") Integer gid, @RequestBody HintRequest request){
        // 是不是本人
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.getUid().equals(request.getUid())){
            Map<String, String> map = new HashMap<>();
            map.put("message", "uid 错误");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        Garbage garbage = garbageService.getByGid(gid);
        if (garbage == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "garbage 不存在");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

        String msg = hintService.checkHintNum(request.getRoomId(), user.getUsername());
        if (msg.equals("success")){
            return builder.body(garbage.getType());
        }

        // 失败的情况
        Map<String, String> map = new HashMap<>();
        map.put("message", msg);
        return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
    }
}
