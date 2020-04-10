package com.psych.game.controller;

import com.psych.game.exceptions.InvalidGameActionException;
import com.psych.game.model.Game;
import com.psych.game.model.Player;
import com.psych.game.model.PlayerAnswer;
import com.psych.game.repository.PlayerAnswerRepository;
import com.psych.game.repository.PlayerRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;
import java.util.Optional;

public class GamePlayAPI {
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    PlayerAnswerRepository playerAnswerRepository;
    private static JSONObject success;

    @GetMapping("/start-game")
    public JSONObject startGame(Authentication authentication) throws InvalidGameActionException {
        Player leader = getCurrentPlayer(authentication);
        leader.getCurrentGame().startGame(leader);
        return success;
    }

    private Player getCurrentPlayer(Authentication authentication) {

        return playerRepository.findByEmail(authentication.getName()).orElseThrow(NoSuchElementException::new);
    }

    @GetMapping("/end-game")
    public  JSONObject endGame(Authentication authentication) throws InvalidGameActionException{
         Player leader = getCurrentPlayer(authentication);
         leader.getCurrentGame().endGame(leader);
         return  success;
    }

    @GetMapping("/submit-answer")
    public JSONObject submitAnswer(Authentication authentication, @RequestParam(name = "answer") String answer) throws InvalidGameActionException {
        Player player=getCurrentPlayer(authentication);
        player.getCurrentGame().submitAnswer(player,answer);
        return  success;
    }


    @GetMapping("/select-answer")
    public JSONObject selectAnswer(Authentication authentication, @RequestParam(name = "playerAnswerId") Long playerAnswerId) throws InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        Game game = player.getCurrentGame();

        PlayerAnswer playerAnswer = playerAnswerRepository.findById(playerAnswerId)
                .orElseThrow(NoSuchElementException::new );
        game.selectAnswer(player, playerAnswer);
        return success;
    }

    @GetMapping("/player-ready")
    public JSONObject playerReady(Authentication authentication) throws InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        Game game = player.getCurrentGame();
        game.playerIsReady(player);
        return success;
    }

    @GetMapping("/player-not-ready")
    public JSONObject playerNotReady(Authentication authentication) throws InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        Game game = player.getCurrentGame();
        game.playerIsNotReady(player);
        return success;
    }
}
