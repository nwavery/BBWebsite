package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Player;
import net.bst.springboot.springsecurity.model.Team;
import net.bst.springboot.springsecurity.model.User;
import net.bst.springboot.springsecurity.repository.PlayerRepository;
import net.bst.springboot.springsecurity.repository.TeamRepository;
import net.bst.springboot.springsecurity.repository.UserRepository;
import net.bst.springboot.springsecurity.web.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository; // Need this for auth check

    @Override
    @Transactional // Ensure this runs within a transaction
    public Player save(PlayerDto playerDto) {
        Team team = teamRepository.findById(playerDto.getTeamId())
                .orElseThrow(() -> new EntityNotFoundException("Team not found with id: " + playerDto.getTeamId()));
        checkUserOwnsTeam(team);

        // Validation: Check if player name is unique for this team
        if (playerRepository.existsByNameAndTeamId(playerDto.getName(), team.getId())) {
            throw new IllegalArgumentException("Player name '" + playerDto.getName() + "' already exists on this team.");
        }

        Player player = new Player();
        player.setName(playerDto.getName());
        player.setPosition(playerDto.getPosition());
        player.setTeam(team);

        return playerRepository.save(player);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Player> findPlayerById(Long playerId) {
        return playerRepository.findById(playerId);
    }

    @Override
    @Transactional
    public Player updatePlayer(Long playerId, PlayerDto playerDto) {
        Player existingPlayer = playerRepository.findById(playerId)
                .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));
        checkUserOwnsTeam(existingPlayer.getTeam());

        // Validation: Check if the new name is unique for this team (excluding the current player)
        if (!existingPlayer.getName().equals(playerDto.getName()) &&
            playerRepository.existsByNameAndTeamIdAndIdNot(playerDto.getName(), existingPlayer.getTeam().getId(), playerId)) {
             throw new IllegalArgumentException("Player name '" + playerDto.getName() + "' already exists on this team.");
        }

        existingPlayer.setName(playerDto.getName());
        existingPlayer.setPosition(playerDto.getPosition());

        return playerRepository.save(existingPlayer);
    }

    @Override
    @Transactional
    public void deletePlayer(Long playerId) {
        Player playerToDelete = playerRepository.findById(playerId)
             .orElseThrow(() -> new EntityNotFoundException("Player not found with id: " + playerId));

        // Authorization check: Ensure user owns the team this player belongs to
        checkUserOwnsTeam(playerToDelete.getTeam());

        // TODO: Consider implications if this player has stats recorded elsewhere (not currently the case)

        playerRepository.delete(playerToDelete);
    }

    /**
     * Helper method to check if the currently authenticated user owns the given team.
     * Throws AccessDeniedException if not authorized.
     */
    private void checkUserOwnsTeam(Team team) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        User currentUser = userRepository.findByEmail(currentUsername);

        if (currentUser == null || team == null || team.getUser() == null || !team.getUser().getId().equals(currentUser.getId())) {
             throw new AccessDeniedException("User does not have permission to modify players for this team.");
        }
    }

    // Implement other methods later
} 