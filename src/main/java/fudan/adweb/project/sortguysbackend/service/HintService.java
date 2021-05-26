package fudan.adweb.project.sortguysbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class HintService {
    private final GameService gameService;

    @Autowired
    public HintService(GameService gameService) {
        this.gameService = gameService;
    }

    public Map<String, String> checkHintNum(Integer roomId, String username) {
        // 是否有足够的提示数
        return gameService.updateHints(String.valueOf(roomId), username, -1);
    }

    public int getHintsNumLeft(Integer roomId, String username) {
        return gameService.getHintsNumLeft(String.valueOf(roomId), username);
    }
}
