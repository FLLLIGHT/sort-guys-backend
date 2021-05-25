package fudan.adweb.project.sortguysbackend.service;

import fudan.adweb.project.sortguysbackend.mapper.EmojiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
