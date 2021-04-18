package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.Authority;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface AuthorityMapper {
    @Select("select * from authority where aid in (select aid from user_authority where uid = #{uid}) ")
    public Set<Authority> getAllAuthoritiesByUid(int uid);
}
