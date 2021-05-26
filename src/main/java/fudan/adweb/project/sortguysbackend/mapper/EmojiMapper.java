package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.Emoji;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmojiMapper {
    @Select("select url from emoji where name = #{name}")
    String findUrlByName(String name);

    @Select("select * from emoji")
    List<Emoji> findAll();

    @Select("select * from emoji where name = #{name}")
    Emoji findByName(String name);
}
