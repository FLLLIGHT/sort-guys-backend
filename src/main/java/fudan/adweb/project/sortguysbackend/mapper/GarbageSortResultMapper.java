package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.GarbageSortResult;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface GarbageSortResultMapper {

    @Insert("insert into garbage_sort_result(gid, uid) values(#{gid}, #{uid})")
    void insert(@Param("gid") Integer gid, @Param("uid") Integer uid);

    @Select("select * from garbage_sort_result where uid = #{uid}")
    List<GarbageSortResult> findByUid(Integer uid);

    @Select("select * from garbage_sort_result where gid = #{gid}")
    List<GarbageSortResult> findByGid(Integer gid);

    @Update("update garbage_sort_result set times = times + 1 where gid = #{gid} and uid = #{uid}")
    void updateFalseResult(Integer gid, Integer uid);

    @Update("update garbage_sort_result set times = times + 1, correctTimes = correctTimes + 1 where gid = #{gid} and uid = #{uid}")
    void updateCorrectResult(Integer gid, Integer uid);

    @Select("select * from garbage_sort_result where gid = #{gid} and uid = #{uid}")
    GarbageSortResult findByGidAndUid(Integer gid, Integer uid);

    @Update("update garbage_sort_result set unlockTime = #{unlockTime} where gid = #{gid} and uid = #{uid}")
    void updateUnlockTime(Date unlockTime, Integer gid, Integer uid);
}
