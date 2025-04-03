package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Match;
import net.bst.springboot.springsecurity.web.dto.MatchDto;

import java.util.List;
import java.util.Optional;

public interface MatchService {

    Match save(MatchDto matchDto);

    List<Match> findAllMatches();

    Optional<Match> findMatchById(Long id);

    Match updateMatch(Long id, MatchDto matchDto);

    void deleteMatch(Long id);

    // Potential future methods:
    // List<Match> findMatchesForTeam(Long teamId);

} 