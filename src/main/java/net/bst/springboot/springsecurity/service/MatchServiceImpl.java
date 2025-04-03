package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Match;
import net.bst.springboot.springsecurity.model.Team;
import net.bst.springboot.springsecurity.repository.MatchRepository;
import net.bst.springboot.springsecurity.repository.TeamRepository;
import net.bst.springboot.springsecurity.web.dto.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Override
    @Transactional
    public Match save(MatchDto matchDto) {
        // TODO: Refine authorization - who can record matches?
        // For now, assume any authenticated user can
        checkUserCanModifyMatches(); // Basic check

        Team teamA = teamRepository.findById(matchDto.getTeamAId())
                .orElseThrow(() -> new EntityNotFoundException("Team A not found with id: " + matchDto.getTeamAId()));
        Team teamB = teamRepository.findById(matchDto.getTeamBId())
                .orElseThrow(() -> new EntityNotFoundException("Team B not found with id: " + matchDto.getTeamBId()));

        if (teamA.getId().equals(teamB.getId())) {
             throw new IllegalArgumentException("A team cannot play against itself.");
        }

        Match match = new Match();
        mapDtoToEntity(matchDto, match, teamA, teamB);
        match.setMatchDate(LocalDateTime.now()); // Set date on creation

        return matchRepository.save(match);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Match> findAllMatches() {
        return matchRepository.findAllByOrderByMatchDateDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Match> findMatchById(Long id) {
        return matchRepository.findById(id);
    }

    @Override
    @Transactional
    public Match updateMatch(Long id, MatchDto matchDto) {
        // TODO: Refine authorization - who can edit matches?
        checkUserCanModifyMatches(); // Basic check

        Match existingMatch = matchRepository.findById(id)
             .orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));

        Team teamA = teamRepository.findById(matchDto.getTeamAId())
                .orElseThrow(() -> new EntityNotFoundException("Team A not found with id: " + matchDto.getTeamAId()));
        Team teamB = teamRepository.findById(matchDto.getTeamBId())
                .orElseThrow(() -> new EntityNotFoundException("Team B not found with id: " + matchDto.getTeamBId()));

        if (teamA.getId().equals(teamB.getId())) {
             throw new IllegalArgumentException("A team cannot play against itself.");
        }

        // Map updated fields from DTO to existing entity
        mapDtoToEntity(matchDto, existingMatch, teamA, teamB);
        // Do not update matchDate on edit, unless specifically required

        return matchRepository.save(existingMatch);
    }

    @Override
    @Transactional
    public void deleteMatch(Long id) {
        // TODO: Refine authorization - who can delete matches?
        checkUserCanModifyMatches(); // Basic check

        Match matchToDelete = matchRepository.findById(id)
             .orElseThrow(() -> new EntityNotFoundException("Match not found with id: " + id));

        matchRepository.delete(matchToDelete);
    }

    // Helper to map DTO fields to entity
    private void mapDtoToEntity(MatchDto dto, Match entity, Team teamA, Team teamB) {
        entity.setTeamA(teamA);
        entity.setTeamB(teamB);
        entity.setScoreTeamA(dto.getScoreTeamA());
        entity.setScoreTeamB(dto.getScoreTeamB());
        entity.setTouchdownsTeamA(dto.getTouchdownsTeamA());
        entity.setTouchdownsTeamB(dto.getTouchdownsTeamB());
        entity.setCasualtiesTeamA(dto.getCasualtiesTeamA());
        entity.setCasualtiesTeamB(dto.getCasualtiesTeamB());
    }

    // Basic authorization check helper (placeholder)
    private void checkUserCanModifyMatches() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
             throw new AccessDeniedException("User must be logged in to modify matches.");
        }
        // Add more specific role/permission checks here if needed
        // e.g., if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
        //     throw new AccessDeniedException("User does not have permission to modify matches.");
        // }
    }
} 