package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.User;
import fudan.adweb.project.sortguysbackend.entity.UserInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    //根据id得到用户
    @Select("select * from user where uid = #{uid}")
    @Results({
            @Result(id = true, column = "uid", property = "uid"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
            @Result(column = "score", property = "score"),
            @Result(column = "uid", property="authorities",
                    many=@Many(
                            select="fudan.adweb.project.sortguysbackend.mapper.AuthorityMapper.getAllAuthoritiesByUid",
                            fetchType= FetchType.LAZY
                    )
            )
    })
    public User getUserByUid(int uid);

    @Select("select uid from user where username = #{username}")
    public Integer getUidByUsername(String username);

    @Insert("insert into user(username, password) values(#{username}, #{password}) ")
    //获取自增主键
    @Options(useGeneratedKeys = true, keyProperty = "uid", keyColumn="uid")
    public Integer insert(User user);

    @Update("update user set username = #{username}, password = #{password} where uid = #{uid}")
    void update(UserInfo userInfo);

    @Select("select * from user")
    List<UserInfo> getAll();
}
