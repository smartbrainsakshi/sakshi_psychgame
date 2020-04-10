package com.psych.game.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psych.game.Utils;
import com.psych.game.exceptions.InvalidGameActionException;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Game extends Auditable{

    @ManyToMany
    @JsonIdentityReference
    @Getter
    @Setter
    private Set<Player> players = new HashSet<>();



    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @JsonIgnore
    @Getter
    @Setter
    private List<Round> rounds = new ArrayList();

    @Getter
    @Setter
    private int numRounds = 10;

    @Getter
    @Setter
    private Boolean hasEllen = false;

    @NotNull
    @JsonIgnore
    @JsonIdentityReference
    @Getter
    @Setter
    @ManyToOne
    private Player leader;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    @Getter
    @Setter
    private Map<Player, Stat> playerStats = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private GameStatus gameStatus = GameStatus.PLAYERS_JOINING;

    @ManyToMany
    @JsonIgnore
    @Getter
    @Setter
    private Set<Player> readyPlayers = new HashSet<>();

    @NotNull
    @Getter @Setter
    @JsonIdentityReference
    @ManyToOne
    private GameMode gameMode;


    public void startGame(Player player) throws InvalidGameActionException
    {
        if(!gameStatus.equals(GameStatus.PLAYERS_JOINING))
             throw new InvalidGameActionException("The game has already started");
        if(players.size()<2)
             throw new InvalidGameActionException("Can't start a game with a single player");
        if(player!=leader)
            throw  new InvalidGameActionException("Only the leader can start the game");

        startNewRound();
    }

    private void startNewRound() {
        gameStatus=GameStatus.SUBMITTING_ANSWERS;
        Question question = Utils.getRandomQuestion(gameMode);
        Round round=new Round(this, question,rounds.size()+1);
        if (hasEllen)
            round.setEllenAnswer(Utils.getRandomEllenAnswer(question));
        rounds.add(round);
    }

    public void submitAnswer(Player player ,String answer) throws  InvalidGameActionException
    {
        if (answer.length() == 0)
            throw new InvalidGameActionException("Answer cannot be empty");
        if (!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");
        if (!gameStatus.equals(GameStatus.SUBMITTING_ANSWERS))
            throw new InvalidGameActionException("Game is not accepting answers at present");
        Round currentRound = getCurrentRound();
        currentRound.submitAnswer(player, answer);
        if (currentRound.allAnswersSubmitted(players.size()))
            gameStatus = GameStatus.SELECTING_ANSWERS;
    }

    private Round getCurrentRound() throws  InvalidGameActionException {
        if (rounds.size() == 0)
            throw new InvalidGameActionException("The game has not started");
        return rounds.get(rounds.size() - 1);
    }

    public void selectAnswer(Player player, PlayerAnswer selectedAnswer) throws InvalidGameActionException {
        if (!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");
        if (!gameStatus.equals(GameStatus.SELECTING_ANSWERS))
            throw new InvalidGameActionException("Game is not selecting answers at present");
        Round currentRound = getCurrentRound();
        currentRound.selectAnswer(player, selectedAnswer);

        if (currentRound.allAnswersSelected(players.size())) {
            if (rounds.size() < numRounds)
                gameStatus = GameStatus.WAITING_FOR_READY;
            else
                endGame();
        }
    }

    private void endGame() {
        gameStatus = GameStatus.ENDED;
        for (Player player : players) {
            if (player.getCurrentGame().equals(this))
                player.setCurrentGame(null);
            Stat oldPlayerStats = player.getStat();
            Stat deltaPlayerStats = playerStats.get(player);
            oldPlayerStats.setCorrectAnswerCount(oldPlayerStats.getCorrectAnswerCount() + deltaPlayerStats.getCorrectAnswerCount());
            oldPlayerStats.setGotPsychedCount(oldPlayerStats.getGotPsychedCount() + deltaPlayerStats.getGotPsychedCount());
            oldPlayerStats.setPsychedOtherCount(oldPlayerStats.getPsychedOtherCount() + deltaPlayerStats.getPsychedOtherCount());
        }
    }

    public void endGame(Player player) throws InvalidGameActionException {
        if (gameStatus.equals(GameStatus.ENDED))
            throw new InvalidGameActionException("The game has already ended");
        if (!player.equals(leader))
            throw new InvalidGameActionException("Only the leader can end the game");
        endGame();
    }

    public void addPlayer(Player player) throws InvalidGameActionException {
        if (!gameStatus.equals(GameStatus.PLAYERS_JOINING))
            throw new InvalidGameActionException("Can't join after the game has started");
        players.add(player);
        player.setCurrentGame(this);
    }

    public void removePlayer(Player player) throws InvalidGameActionException {
        if (!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");
        players.remove(player);
        if (player.getCurrentGame().equals(this))
            player.setCurrentGame(null);
        if (players.size() == 0 || (players.size() == 1 && !gameStatus.equals(GameStatus.PLAYERS_JOINING)))
            endGame();
    }

    public void playerIsReady(Player player) throws InvalidGameActionException {
        if (!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");
        if (!gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("Game is not waiting for players to be ready");
        readyPlayers.add(player);
        if (readyPlayers.size() == players.size())
            startNewRound();
    }

    public void playerIsNotReady(Player player) throws InvalidGameActionException {
        if (!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");
        if (!gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("Game is not waiting for players to be ready");
        readyPlayers.remove(player);
    }


}
