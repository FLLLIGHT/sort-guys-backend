package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.User;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    //根据id得到用户
    @Select("select * from user where uid = #{uid}")
    @Results({
            @Result(id = true, column = "uid", property = "uid"),
            @Result(column = "username", property = "username"),
            @Result(column = "password", property = "password"),
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
}
