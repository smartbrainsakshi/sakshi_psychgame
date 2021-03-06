package com.psych.game.repository;

import com.psych.game.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query(value = "SELECT * FROM questions WHERE game_mode_id=:gameModeId ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Question getRandomQuestion(Long id);
}
