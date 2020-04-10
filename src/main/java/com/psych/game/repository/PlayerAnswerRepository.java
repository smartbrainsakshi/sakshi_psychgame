package com.psych.game.repository;

import com.psych.game.model.Player;
import com.psych.game.model.PlayerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer,Long> {

}
