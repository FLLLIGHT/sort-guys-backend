package fudan.adweb.project.sortguysbackend.controller;

import fudan.adweb.project.sortguysbackend.entity.Emoji;
import fudan.adweb.project.sortguysbackend.service.EmojiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EmojiController {
    private final EmojiService emojiService;

    @Autowired
    public EmojiController(EmojiService emojiService) {
        this.emojiService = emojiService;
    }

    @GetMapping("/emoji")
    public ResponseEntity<?> getAllEmoji(){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        List<Emoji> emojis = emojiService.findAll();
        return builder.body(emojis);
    }

    @GetMapping("/emoji/{name}")
    public ResponseEntity<?> getEmoji(@PathVariable("name") String name){
        ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
        Emoji emoji = emojiService.findByName(name);
        if (emoji == null){
            Map<String, String> map = new HashMap<>();
            map.put("message", "emoji 不存在");
            return builder.body(map);
        }
        return builder.body(emoji);
    }
}
