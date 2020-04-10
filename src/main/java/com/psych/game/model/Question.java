package com.psych.game.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Question extends Auditable {
    public Question() {
    }

    @NotBlank
    @Getter
    @Setter
    private String question;

    @NotBlank
    @Getter
    @Setter
    private String correctAnswer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @JsonManagedReference
    @Getter
    @Setter
    private Set<EllenAnswer> ellenAnswers = new HashSet<>();

    @ManyToOne
    @JsonIdentityReference
    @Getter
    @Setter
    @NotNull
    private GameMode gameMode;

    public Question(@NotNull String question, @NotNull String correctAnswer, @NotNull GameMode gameMode) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.gameMode = gameMode;
    }

}
