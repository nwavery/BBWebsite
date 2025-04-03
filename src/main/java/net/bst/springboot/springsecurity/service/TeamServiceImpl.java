package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Team;
import net.bst.springboot.springsecurity.model.User;
import net.bst.springboot.springsecurity.repository.TeamRepository;
import net.bst.springboot.springsecurity.repository.UserRepository;
import net.bst.springboot.springsecurity.repository.MatchRepository;
import net.bst.springboot.springsecurity.web.dto.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository; // Assuming UserRepository exists

    @Autowired
    private MatchRepository matchRepository; // Inject MatchRepository

    @Override
    @Transactional
    public Team save(TeamDto teamDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByEmail(username);

        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }

        // Validation: Check if team name is unique for this user
        if (teamRepository.existsByNameAndUserId(teamDto.getName(), user.getId())) {
            throw new IllegalArgumentException("Team name '" + teamDto.getName() + "' already exists for this user.");
        }

        Team team = new Team();
        team.setName(teamDto.getName());
        team.setRace(teamDto.getRace());
        team.setUser(user);

        return teamRepository.save(team);
    }

    @Override
    public List<Team> findTeamsForCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userRepository.findByEmail(username);

        if (user != null) {
            return teamRepository.findByUserId(user.getId());
        } else {
            // Handle case where user is not found (e.g., anonymous user)
            // Return an empty list or throw an exception
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Team> findTeamById(Long id) {
        // Note: This fetches the team eagerly, including players if accessed.
        // Consider @EntityGraph if performance becomes an issue with many players.
        return teamRepository.findById(id);
    }

    // Helper to find a team by ID ensuring it belongs to the current user
    @Transactional(readOnly = true) // Good practice for read operations
    public Optional<Team> findTeamByIdForCurrentUser(Long id) {
        Optional<Team> teamOpt = findTeamById(id);
        if (teamOpt.isPresent()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = auth.getName();
            if (!teamOpt.get().getUser().getEmail().equals(currentUsername)) {
                // If the team doesn't belong to the current user, deny access
                 throw new AccessDeniedException("User does not have permission to access this team");
                // Or return Optional.empty(); depending on desired behavior
                // return Optional.empty();
            }
            // Eagerly fetch players within the transaction
            teamOpt.get().getPlayers().isEmpty(); 
        }
        return teamOpt;
    }

    @Override
    @Transactional
    public Team update(Long id, TeamDto teamDto) {
        Team existingTeam = findTeamByIdForCurrentUser(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id + " or access denied"));

        // Validation: Check if the new name is unique for this user (excluding the current team)
        if (!existingTeam.getName().equals(teamDto.getName()) &&
             teamRepository.existsByNameAndUserIdAndIdNot(teamDto.getName(), existingTeam.getUser().getId(), id)) {
            throw new IllegalArgumentException("Team name '" + teamDto.getName() + "' already exists for this user.");
        }

        existingTeam.setName(teamDto.getName());
        existingTeam.setRace(teamDto.getRace());

        return teamRepository.save(existingTeam);
    }

    @Override
    @Transactional // Ensure transactional for delete
    public void deleteTeam(Long id) {
        // Find the existing team, ensuring the current user owns it
        Team teamToDelete = findTeamByIdForCurrentUser(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + id + " or access denied"));

        // Check if the team has participated in any matches
        if (matchRepository.existsByTeamAIdOrTeamBId(id, id)) {
            throw new DataIntegrityViolationException("Cannot delete team: It has recorded matches. Please delete the matches first or contact an admin.");
        }

        // Note: Players associated with this team will also be deleted due to
        // cascade = CascadeType.ALL, orphanRemoval = true on the Team.players mapping.
        // Also, Matches involving this team might cause issues if not handled.
        // Consider options: delete matches, set team FKs to null, or prevent deletion if matches exist.
        // For now, we proceed with deletion, assuming cascade handles players.
        // TODO: Add logic to handle related Matches before deleting a team.

        teamRepository.delete(teamToDelete);
    }

    // Implement other methods from TeamService interface later
} 