package fudan.adweb.project.sortguysbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserScoreMapper {

    @Insert("insert into user_score(uid, score) values(#{uid}, #{score})")
    void insert(@Param("uid") Integer uid, @Param("score") int score);

    @Select("select score from user_score where uid = #{uid}")
    Integer findScoreByUid(Integer uid);

    @Update("update user_score set score = #{score} where uid = #{uid}")
    void updateScoreByUid(@Param("uid") Integer uid, @Param("score") Double score);
}
