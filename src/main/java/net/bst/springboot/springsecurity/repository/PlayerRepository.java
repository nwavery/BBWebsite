package net.bst.springboot.springsecurity.repository;

import net.bst.springboot.springsecurity.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    // Method to find players belonging to a specific team
    List<Player> findByTeamId(Long teamId);

    // Check if a player name exists for a specific team
    boolean existsByNameAndTeamId(String name, Long teamId);

    // Check if player name exists for a team, excluding a specific player ID (for updates)
    boolean existsByNameAndTeamIdAndIdNot(String name, Long teamId, Long playerIdToExclude);

    // Add other custom queries if needed later
} 