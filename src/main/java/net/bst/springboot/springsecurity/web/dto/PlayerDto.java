package net.bst.springboot.springsecurity.web.dto;

import net.bst.springboot.springsecurity.model.Position;

public class PlayerDto {

    private Long teamId; // ID of the team this player belongs to
    private String name;
    private Position position;

    public PlayerDto() {
    }

    public PlayerDto(Long teamId, String name, Position position) {
        this.teamId = teamId;
        this.name = name;
        this.position = position;
    }

    // Getters and Setters
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
} 