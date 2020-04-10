package com.psych.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.Email;

@Entity
public class Stat extends Auditable {
    @Getter
    @Setter
    private long gotPsychedCount=0;
    @Getter @Setter
    private long PsychedOtherCount=0;
    @Getter @Setter
    private long correctAnswerCount=0;
}
