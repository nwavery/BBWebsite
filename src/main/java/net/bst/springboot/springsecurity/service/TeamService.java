package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Team;
import net.bst.springboot.springsecurity.web.dto.TeamDto;

import java.util.List;
import java.util.Optional;

public interface TeamService {

    Team save(TeamDto teamDto);

    List<Team> findTeamsForCurrentUser();

    Optional<Team> findTeamById(Long id);

    Team update(Long id, TeamDto teamDto);

    void deleteTeam(Long id);

    // Potential future methods:
    // List<Team> findTeamsByUsername(String username);
} 