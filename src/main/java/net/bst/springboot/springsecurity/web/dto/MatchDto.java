package net.bst.springboot.springsecurity.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class MatchDto {

    @NotNull(message = "Team A ID is required")
    private Long teamAId;

    @NotNull(message = "Team B ID is required")
    private Long teamBId;

    @NotNull(message = "Team A score is required")
    @PositiveOrZero(message = "Score must be non-negative")
    private Integer scoreTeamA;

    @NotNull(message = "Team B score is required")
    @PositiveOrZero(message = "Score must be non-negative")
    private Integer scoreTeamB;

    @NotNull(message = "Team A touchdowns required")
    @PositiveOrZero(message = "Touchdowns must be non-negative")
    private Integer touchdownsTeamA;

    @NotNull(message = "Team B touchdowns required")
    @PositiveOrZero(message = "Touchdowns must be non-negative")
    private Integer touchdownsTeamB;

    @NotNull(message = "Team A casualties required")
    @PositiveOrZero(message = "Casualties must be non-negative")
    private Integer casualtiesTeamA;

    @NotNull(message = "Team B casualties required")
    @PositiveOrZero(message = "Casualties must be non-negative")
    private Integer casualtiesTeamB;

    // No need for matchDate in DTO, service can set it upon saving

    // Getters and Setters

    public Long getTeamAId() {
        return teamAId;
    }

    public void setTeamAId(Long teamAId) {
        this.teamAId = teamAId;
    }

    public Long getTeamBId() {
        return teamBId;
    }

    public void setTeamBId(Long teamBId) {
        this.teamBId = teamBId;
    }

    public Integer getScoreTeamA() {
        return scoreTeamA;
    }

    public void setScoreTeamA(Integer scoreTeamA) {
        this.scoreTeamA = scoreTeamA;
    }

    public Integer getScoreTeamB() {
        return scoreTeamB;
    }

    public void setScoreTeamB(Integer scoreTeamB) {
        this.scoreTeamB = scoreTeamB;
    }

    public Integer getTouchdownsTeamA() {
        return touchdownsTeamA;
    }

    public void setTouchdownsTeamA(Integer touchdownsTeamA) {
        this.touchdownsTeamA = touchdownsTeamA;
    }

    public Integer getTouchdownsTeamB() {
        return touchdownsTeamB;
    }

    public void setTouchdownsTeamB(Integer touchdownsTeamB) {
        this.touchdownsTeamB = touchdownsTeamB;
    }

    public Integer getCasualtiesTeamA() {
        return casualtiesTeamA;
    }

    public void setCasualtiesTeamA(Integer casualtiesTeamA) {
        this.casualtiesTeamA = casualtiesTeamA;
    }

    public Integer getCasualtiesTeamB() {
        return casualtiesTeamB;
    }

    public void setCasualtiesTeamB(Integer casualtiesTeamB) {
        this.casualtiesTeamB = casualtiesTeamB;
    }
} 