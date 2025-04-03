package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Match;
import net.bst.springboot.springsecurity.model.Team;
import net.bst.springboot.springsecurity.repository.MatchRepository;
import net.bst.springboot.springsecurity.repository.TeamRepository;
import net.bst.springboot.springsecurity.web.dto.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    @Transactional
    public Match save(MatchDto matchDto) {
        // Fetch the teams involved
        Team teamA = teamRepository.findById(matchDto.getTeamAId())
                .orElseThrow(() -> new EntityNotFoundException("Team A not found with id: " + matchDto.getTeamAId()));

        Team teamB = teamRepository.findById(matchDto.getTeamBId())
                .orElseThrow(() -> new EntityNotFoundException("Team B not found with id: " + matchDto.getTeamBId()));

        // TODO: Add validation - ensure teamA and teamB are not the same team
        if (teamA.getId().equals(teamB.getId())) {
             throw new IllegalArgumentException("A team cannot play against itself.");
        }

        // TODO: Add authorization - ensure the current user has permission to record matches
        // (e.g., is an admin, or owns one of the teams involved, depending on rules)

        Match match = new Match();
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setScoreTeamA(matchDto.getScoreTeamA());
        match.setScoreTeamB(matchDto.getScoreTeamB());
        match.setTouchdownsTeamA(matchDto.getTouchdownsTeamA());
        match.setTouchdownsTeamB(matchDto.getTouchdownsTeamB());
        match.setCasualtiesTeamA(matchDto.getCasualtiesTeamA());
        match.setCasualtiesTeamB(matchDto.getCasualtiesTeamB());
        match.setMatchDate(LocalDateTime.now()); // Set the date/time when saved

        return matchRepository.save(match);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> findAllMatches() {
        return matchRepository.findAllByOrderByMatchDateDesc();
    }

    // Implement other methods later
} 