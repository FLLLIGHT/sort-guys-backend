package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.UserAppearance;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAppearanceMapper {

    @Insert("insert into user_appearance(uid, color) values(#{uid}, #{color})")
    void insert(@Param("uid") Integer uid, @Param("color") String color);

    @Select("select * from user_appearance where uid = #{uid}")
    UserAppearance findByUid(Integer uid);
}
