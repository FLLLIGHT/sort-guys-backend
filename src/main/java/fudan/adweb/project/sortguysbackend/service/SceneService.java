package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.Scene;
import fudan.adweb.project.sortguysbackend.mapper.SceneMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SceneService {
    private final SceneMapper sceneMapper;

    @Autowired
    public SceneService(SceneMapper sceneMapper){
        this.sceneMapper = sceneMapper;
    }

    public List<Scene> getAllScenes(){
        return sceneMapper.getAll();
    }

    public Scene getSceneBySid(Integer sid){
        return sceneMapper.getSceneBySid(sid);
    }
}
