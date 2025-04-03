package net.bst.springboot.springsecurity.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_result") // "match" might be a reserved word in some SQL dialects
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_a_id", nullable = false)
    private Team teamA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_b_id", nullable = false)
    private Team teamB;

    @Column(name = "score_team_a", nullable = false)
    private Integer scoreTeamA;

    @Column(name = "score_team_b", nullable = false)
    private Integer scoreTeamB;

    @Column(name = "touchdowns_team_a", nullable = false)
    private Integer touchdownsTeamA;

    @Column(name = "touchdowns_team_b", nullable = false)
    private Integer touchdownsTeamB;

    @Column(name = "casualties_team_a", nullable = false)
    private Integer casualtiesTeamA;

    @Column(name = "casualties_team_b", nullable = false)
    private Integer casualtiesTeamB;

    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    // Constructors
    public Match() {
        this.matchDate = LocalDateTime.now(); // Default to now
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Team getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = teamA;
    }

    public Team getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = teamB;
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

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    @Override
    public String toString() {
        return "Match{" +
               "id=" + id +
               ", teamA=" + (teamA != null ? teamA.getName() : null) +
               ", teamB=" + (teamB != null ? teamB.getName() : null) +
               ", scoreTeamA=" + scoreTeamA +
               ", scoreTeamB=" + scoreTeamB +
               ", matchDate=" + matchDate +
               '}';
    }
} 