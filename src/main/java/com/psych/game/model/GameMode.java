package com.psych.game.model;


import lombok.Getter;
import lombok.Setter;


import javax.persistence.Column;
import javax.persistence.Entity;

import javax.validation.constraints.NotBlank;

@Entity
public class GameMode extends Auditable {
    @NotBlank
    @Getter
    @Setter
    @Column(unique = true)
    private String name;

    @Getter
    @Setter
    private String picture;

    @Getter
    @Setter
    private String description;


    public GameMode() {

    }
}
