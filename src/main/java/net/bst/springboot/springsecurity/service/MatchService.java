package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Match;
import net.bst.springboot.springsecurity.web.dto.MatchDto;

import java.util.List;

public interface MatchService {

    Match save(MatchDto matchDto);

    List<Match> findAllMatches();

    // Potential future methods:
    // List<Match> findMatchesForTeam(Long teamId);

} 