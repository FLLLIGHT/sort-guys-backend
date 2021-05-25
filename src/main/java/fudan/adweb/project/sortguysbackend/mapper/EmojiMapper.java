package fudan.adweb.project.sortguysbackend.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface EmojiMapper {
    @Select("select url from emoji where name = #{name}")
    String findUrlByName(String name);
}
