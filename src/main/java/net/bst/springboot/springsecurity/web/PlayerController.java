package net.bst.springboot.springsecurity.web;

import net.bst.springboot.springsecurity.model.Player;
import net.bst.springboot.springsecurity.service.PlayerService;
import net.bst.springboot.springsecurity.web.dto.PlayerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // Handler method to process the add player form submission
    @PostMapping
    public String addPlayer(@Valid @ModelAttribute("newPlayer") PlayerDto playerDto,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {

         if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newPlayer", result);
            redirectAttributes.addFlashAttribute("newPlayer", playerDto);
            if (playerDto.getTeamId() == null) {
                redirectAttributes.addFlashAttribute("playerError", "Cannot add player: Team ID missing.");
                return "redirect:/teams";
            }
            return "redirect:/teams/" + playerDto.getTeamId() + "?addPlayerError";
        }

        try {
            Player savedPlayer = playerService.save(playerDto);
            redirectAttributes.addFlashAttribute("playerSuccess", "Player '".concat(savedPlayer.getName()).concat("' added successfully!"));
            return "redirect:/teams/" + savedPlayer.getTeam().getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("playerError", e.getMessage());
            redirectAttributes.addFlashAttribute("newPlayer", playerDto);
            if (playerDto.getTeamId() != null) {
                return "redirect:/teams/" + playerDto.getTeamId() + "?addPlayerError";
            } else {
                return "redirect:/teams";
            }
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("playerError", "Error adding player: " + e.getMessage());
             redirectAttributes.addFlashAttribute("newPlayer", playerDto);
             if (playerDto.getTeamId() != null) {
                 return "redirect:/teams/" + playerDto.getTeamId() + "?addPlayerError";
             } else {
                 return "redirect:/teams";
             }
        }
    }

    // Handler method to show the player edit form
    @GetMapping("/{playerId}/edit")
    public String showEditPlayerForm(@PathVariable Long playerId, Model model) {
        Optional<Player> playerOpt = playerService.findPlayerById(playerId);
        if (!playerOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Player not found");
        }
        Player player = playerOpt.get();

        // Authorization check (redundant if service layer checks, but good practice)
        // checkUserOwnsTeam(player.getTeam()); // Requires helper or moving logic

        // Create DTO for the form
        PlayerDto playerDto = new PlayerDto(player.getTeam().getId(), player.getName(), player.getPosition());

        model.addAttribute("playerDto", playerDto);
        model.addAttribute("playerId", playerId);
        model.addAttribute("teamId", player.getTeam().getId()); // Needed for cancel button link

        return "edit_player"; // View name: edit_player.html
    }

    // Handler method to process the player edit form submission
    @PostMapping("/{playerId}")
    public String updatePlayer(@PathVariable Long playerId,
                               @Valid @ModelAttribute("playerDto") PlayerDto playerDto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {

        Long teamId = playerDto.getTeamId();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.playerDto", result);
            redirectAttributes.addFlashAttribute("playerDto", playerDto);
            return "redirect:/players/" + playerId + "/edit?error";
        }

        try {
            playerService.updatePlayer(playerId, playerDto);
            redirectAttributes.addFlashAttribute("playerSuccess", "Player updated successfully!");
            if (teamId == null) {
                 throw new IllegalStateException("Team ID missing during player update.");
            }
            return "redirect:/teams/" + teamId;
        } catch (IllegalArgumentException e) {
            // Handle custom validation errors (e.g., duplicate name)
             redirectAttributes.addFlashAttribute("playerError", e.getMessage());
             redirectAttributes.addFlashAttribute("playerDto", playerDto);
             return "redirect:/players/" + playerId + "/edit?error";
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("playerError", "Error updating player: " + e.getMessage());
             redirectAttributes.addFlashAttribute("playerDto", playerDto);
             return "redirect:/players/" + playerId + "/edit?error";
        }
    }

    // Handler method to process player deletion
    @PostMapping("/{playerId}/delete")
    public String deletePlayer(@PathVariable Long playerId, RedirectAttributes redirectAttributes) {
        Long teamId = null;
        try {
             // Find player first to get teamId for redirect
             Optional<Player> playerOpt = playerService.findPlayerById(playerId);
             if (playerOpt.isPresent()) {
                 teamId = playerOpt.get().getTeam().getId();
                 playerService.deletePlayer(playerId);
                 redirectAttributes.addFlashAttribute("playerSuccess", "Player deleted successfully!");
             } else {
                 redirectAttributes.addFlashAttribute("playerError", "Player not found.");
             }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("playerError", "Error deleting player: " + e.getMessage());
        }

        if (teamId != null) {
            return "redirect:/teams/" + teamId;
        } else {
             // Fallback if teamId couldn't be determined
            return "redirect:/teams";
        }
    }
}