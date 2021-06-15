package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.UserAppearance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAppearanceMapper {

    @Insert("insert into user_appearance(uid, color, url) values(#{uid}, #{color}, #{url})")
    void insert(@Param("uid") Integer uid, @Param("color") String color, @Param("url") String url);

    @Select("select * from user_appearance where uid = #{uid}")
    UserAppearance findByUid(Integer uid);

    @Update("update user_appearance set color = #{color}, url = #{url} where uid = #{uid}")
    void update(UserAppearance userAppearance);
}
