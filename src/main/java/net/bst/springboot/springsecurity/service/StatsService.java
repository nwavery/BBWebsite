package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.web.dto.TeamStatsDto;

import java.util.List;

public interface StatsService {

    /**
     * Calculates the league standings based on all recorded matches.
     * @return A list of TeamStatsDto, typically sorted by points, TD difference, etc.
     */
    List<TeamStatsDto> getLeagueStandings();

} 