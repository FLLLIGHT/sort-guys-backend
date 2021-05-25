package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.entity.Garbage;
import fudan.adweb.project.sortguysbackend.service.GarbageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class GarbageController {
    private final GarbageService garbageService;

    @Autowired
    public GarbageController(GarbageService garbageService) {
        this.garbageService = garbageService;
    }

    @GetMapping("/garbageInfo/{gid}")
    public ResponseEntity<?> getGarbageByGid(@PathVariable("gid") Integer gid){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        Garbage garbage = garbageService.getByGid(gid);
        if (garbage == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "垃圾不存在");
            return builder.body(map);
        }
        return builder.body(garbage);
    }
}
