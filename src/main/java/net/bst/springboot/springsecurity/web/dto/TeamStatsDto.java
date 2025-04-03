package net.bst.springboot.springsecurity.web.dto;

public class TeamStatsDto {

    private Long teamId;
    private String teamName;
    private int wins;
    private int losses;
    private int draws;
    private int touchdownsScored;
    private int touchdownsConceded;
    private int casualtiesInflicted;
    private int casualtiesSuffered;
    // Could add points later (e.g., 3 for win, 1 for draw)

    public TeamStatsDto(Long teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
        // Initialize stats to 0
        this.wins = 0;
        this.losses = 0;
        this.draws = 0;
        this.touchdownsScored = 0;
        this.touchdownsConceded = 0;
        this.casualtiesInflicted = 0;
        this.casualtiesSuffered = 0;
    }

    // Getters and potentially incrementer methods (or let service handle increments)

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getDraws() {
        return draws;
    }

    public int getTouchdownsScored() {
        return touchdownsScored;
    }

    public int getTouchdownsConceded() {
        return touchdownsConceded;
    }

    public int getCasualtiesInflicted() {
        return casualtiesInflicted;
    }

    public int getCasualtiesSuffered() {
        return casualtiesSuffered;
    }

    // Increment methods (optional, could be handled in service)
    public void incrementWins() { this.wins++; }
    public void incrementLosses() { this.losses++; }
    public void incrementDraws() { this.draws++; }
    public void addTouchdownsScored(int count) { this.touchdownsScored += count; }
    public void addTouchdownsConceded(int count) { this.touchdownsConceded += count; }
    public void addCasualtiesInflicted(int count) { this.casualtiesInflicted += count; }
    public void addCasualtiesSuffered(int count) { this.casualtiesSuffered += count; }

    // Basic Points calculation example (can be customized)
    public int getPoints() {
        return (this.wins * 3) + this.draws;
    }

    // Touchdown difference calculation
    public int getTouchdownDifference() {
        return this.touchdownsScored - this.touchdownsConceded;
    }
} 