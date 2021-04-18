package fudan.adweb.project.sortguysbackend.mapper;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityMapper {
    @Insert("insert into user_authority(aid, uid) values(#{aid}, #{uid})")
    public void insert(Integer aid, Integer uid);
}
