package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.GarbageSortResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarbageSortResultMapper {

    @Insert("insert into garbage_sort_result(gid, uid) values(#{gid}, #{uid})")
    void insert(@Param("gid") Integer gid, @Param("uid") Integer uid);

    @Select("select * from garbage_sort_result where uid = #{uid}")
    List<GarbageSortResult> findByUid(Integer uid);
}
