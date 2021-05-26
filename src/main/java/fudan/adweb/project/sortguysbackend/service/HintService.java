package fudan.adweb.project.sortguysbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HintService {
    private final GameService gameService;

    @Autowired
    public HintService(GameService gameService) {
        this.gameService = gameService;
    }

    public String checkHintNum(Integer roomId, String username) {
        // 是否有足够的提示数
        return gameService.updateHints(String.valueOf(roomId), username, -1);
    }
}
