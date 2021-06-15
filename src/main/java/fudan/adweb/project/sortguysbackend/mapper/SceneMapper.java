package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.Scene;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SceneMapper {
    @Select("select * from scene where name = #{name}")
    public Scene getSceneByName(String name);

    @Select("select * from scene where sid = #{sid}")
    public Scene getSceneBySid(Integer sid);

    @Select("select * from scene")
    List<Scene> getAll();
}
