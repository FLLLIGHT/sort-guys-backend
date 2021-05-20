package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.entity.GarbageSortResult;
import fudan.adweb.project.sortguysbackend.entity.GarbageSortResultInfo;
import fudan.adweb.project.sortguysbackend.service.GarbageSortResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GarbageSortResultController {
    private final GarbageSortResultService garbageSortResultService;

    @Autowired
    public GarbageSortResultController(GarbageSortResultService garbageSortResultService) {
        this.garbageSortResultService = garbageSortResultService;
    }

    @GetMapping("/sortResult/{uid}")
    public ResponseEntity<?> getSortResult(@PathVariable("uid") Integer uid){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        List<GarbageSortResultInfo> infos = garbageSortResultService.getSortResult(uid);
        if (infos == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "用户不存在");
            return builder.body(map);
        }

        return builder.body(infos);
    }

    @GetMapping("/sortResult")
    public ResponseEntity<?> getAllSortResult(){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        List<GarbageSortResultInfo> infos = garbageSortResultService.getAllSortResult();
        return builder.body(infos);
    }
}
