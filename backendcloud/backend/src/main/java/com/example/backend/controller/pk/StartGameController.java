package com.example.backend.controller.pk;

import com.example.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class StartGameController {
    @Autowired
    private StartGameService startGameService;
    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer a_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer b_id = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        Integer aBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_bot_id")));
        Integer bBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_bot_id")));
        return startGameService.startGame(a_id, aBotId, b_id, bBotId);
    }
}
