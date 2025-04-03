package net.bst.springboot.springsecurity.web.dto;

import net.bst.springboot.springsecurity.model.Race;

public class TeamDto {

    private String name;
    private Race race;

    public TeamDto() {
    }

    public TeamDto(String name, Race race) {
        this.name = name;
        this.race = race;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
} 