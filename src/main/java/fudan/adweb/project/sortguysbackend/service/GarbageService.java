package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.Garbage;
import fudan.adweb.project.sortguysbackend.mapper.GarbageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GarbageService {
    private final GarbageMapper garbageMapper;

    @Autowired
    public GarbageService(GarbageMapper garbageMapper) {
        this.garbageMapper = garbageMapper;
    }

    public Garbage getByGid(Integer gid) {
        return garbageMapper.findByGid(gid);
    }

    public Garbage getByName(String garbageName) {
        Integer gid = garbageMapper.findGidByName(garbageName);
        return getByGid(gid);
    }
}
