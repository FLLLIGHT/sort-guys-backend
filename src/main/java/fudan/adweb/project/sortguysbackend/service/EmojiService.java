package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.entity.Emoji;
import fudan.adweb.project.sortguysbackend.mapper.EmojiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmojiService {
    private final EmojiMapper emojiMapper;

    @Autowired
    public EmojiService(EmojiMapper emojiMapper) {
        this.emojiMapper = emojiMapper;
    }

    public String findUrlByName(String name) {
        return emojiMapper.findUrlByName(name);
    }

    public List<Emoji> findAll() {
        return emojiMapper.findAll();
    }

    public Emoji findByName(String name) {
        return emojiMapper.findByName(name);
    }
}
