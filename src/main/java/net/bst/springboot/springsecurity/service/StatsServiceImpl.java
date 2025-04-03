package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Match;
import net.bst.springboot.springsecurity.model.Team;
import net.bst.springboot.springsecurity.repository.MatchRepository;
import net.bst.springboot.springsecurity.repository.TeamRepository;
import net.bst.springboot.springsecurity.web.dto.TeamStatsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Override
    @Transactional(readOnly = true) // Read-only as we are just calculating
    public List<TeamStatsDto> getLeagueStandings() {
        // 1. Fetch all teams
        List<Team> allTeams = teamRepository.findAll();

        // 2. Initialize TeamStatsDto for each team
        Map<Long, TeamStatsDto> statsMap = allTeams.stream()
                .collect(Collectors.toMap(Team::getId, team -> new TeamStatsDto(team.getId(), team.getName())));

        // 3. Fetch all matches
        List<Match> allMatches = matchRepository.findAll(); // Consider fetching only necessary fields if performance is critical

        // 4. Process each match and update stats
        for (Match match : allMatches) {
            TeamStatsDto statsA = statsMap.get(match.getTeamA().getId());
            TeamStatsDto statsB = statsMap.get(match.getTeamB().getId());

            if (statsA == null || statsB == null) {
                // Should not happen if data is consistent, but good to handle
                System.err.println("Warning: Skipping match ID " + match.getId() + " due to missing team stats.");
                continue;
            }

            // Update scores, TDs, CAS
            statsA.addTouchdownsScored(match.getTouchdownsTeamA());
            statsA.addCasualtiesInflicted(match.getCasualtiesTeamA());
            statsA.addTouchdownsConceded(match.getTouchdownsTeamB());
            statsA.addCasualtiesSuffered(match.getCasualtiesTeamB());

            statsB.addTouchdownsScored(match.getTouchdownsTeamB());
            statsB.addCasualtiesInflicted(match.getCasualtiesTeamB());
            statsB.addTouchdownsConceded(match.getTouchdownsTeamA());
            statsB.addCasualtiesSuffered(match.getCasualtiesTeamA());

            // Update Win/Loss/Draw
            if (match.getScoreTeamA() > match.getScoreTeamB()) {
                statsA.incrementWins();
                statsB.incrementLosses();
            } else if (match.getScoreTeamB() > match.getScoreTeamA()) {
                statsB.incrementWins();
                statsA.incrementLosses();
            } else {
                statsA.incrementDraws();
                statsB.incrementDraws();
            }
        }

        // 5. Convert map values to list and sort
        return statsMap.values().stream()
                .sorted(Comparator.comparing(TeamStatsDto::getPoints).reversed() // Primary: Points (desc)
                         .thenComparing(TeamStatsDto::getTouchdownDifference).reversed() // Secondary: TD Diff (desc)
                         .thenComparing(TeamStatsDto::getTouchdownsScored).reversed() // Tertiary: TDs Scored (desc)
                         .thenComparing(TeamStatsDto::getTeamName)) // Finally: Team Name (asc)
                .collect(Collectors.toList());
    }
} 