package net.bst.springboot.springsecurity.service;

import net.bst.springboot.springsecurity.model.Player;
import net.bst.springboot.springsecurity.web.dto.PlayerDto;

import java.util.Optional;

public interface PlayerService {

    Player save(PlayerDto playerDto);

    Optional<Player> findPlayerById(Long playerId);

    Player updatePlayer(Long playerId, PlayerDto playerDto);

    void deletePlayer(Long playerId);

    // Potential future methods:
    // List<Player> findPlayersByTeamId(Long teamId);

} 