package fudan.adweb.project.sortguysbackend.mapper;

import fudan.adweb.project.sortguysbackend.entity.Garbage;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GarbageMapper {

    @Select("select * from garbage where gid = #{gid}")
    Garbage findByGid(Integer gid);

    @Select("select gid from garbage")
    List<Integer> findGidOfAll();

    @Select("select * from garbage")
    List<Garbage> findAll();

    @Select("select * from garbage order by rand() limit 1")
    Garbage findRandom1();

    @Select("select * from garbage order by rand() limit 5")
    List<Garbage> findRandom5();
}
