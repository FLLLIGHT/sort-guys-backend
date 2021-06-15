package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.entity.Garbage;
import fudan.adweb.project.sortguysbackend.entity.Scene;
import fudan.adweb.project.sortguysbackend.service.GarbageService;
import fudan.adweb.project.sortguysbackend.service.SceneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SceneController {
    private final SceneService sceneService;

    @Autowired
    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @GetMapping("/scene")
    public ResponseEntity<?> getAllScenes(){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        List<Scene> scenes = sceneService.getAllScenes();
        return builder.body(scenes);
    }
}
