package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.UserLoginInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginInfoMapper {
    @Insert("insert into user_login_info(uid, token) " +
            "values(#{uid}, #{token})")
    void insert(@Param("uid") Integer uid, @Param("token") String token);

    @Select("select * from user_login_info where uid = #{uid}")
    UserLoginInfo findByUid(Integer uid);

    @Update("update user_login_info set token = #{token} where uid = #{uid}")
    void updateTokenByUid(@Param("uid") Integer uid, @Param("token") String token);

    @Delete("delete from user_login_info where uid = #{uid}")
    void deleteByUid(Integer uid);
}
