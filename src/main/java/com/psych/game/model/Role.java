package com.psych.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Role extends Auditable{
    @Getter
    @Setter
    @NotBlank
    @Column(unique = true)
    private String name;
    @Getter @Setter @NotBlank
    private String description;
}
