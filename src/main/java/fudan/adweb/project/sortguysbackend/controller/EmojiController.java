package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.service.EmojiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmojiController {
    private final EmojiService emojiService;

    @Autowired
    public EmojiController(EmojiService emojiService) {
        this.emojiService = emojiService;
    }
}
