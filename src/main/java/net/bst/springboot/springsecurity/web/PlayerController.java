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
            // If validation errors on add, redirect back to team details with errors
            // We need the teamId to redirect correctly
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newPlayer", result);
            redirectAttributes.addFlashAttribute("newPlayer", playerDto);
            // Ensure teamId is present in the DTO for redirect
            if (playerDto.getTeamId() == null) {
                // Handle error - teamId should always be present from the form
                redirectAttributes.addFlashAttribute("playerError", "Cannot add player: Team ID missing.");
                return "redirect:/teams"; // Redirect somewhere sensible
            }
            return "redirect:/teams/" + playerDto.getTeamId() + "?addPlayerError";
        }

        try {
            Player savedPlayer = playerService.save(playerDto);
            redirectAttributes.addFlashAttribute("playerSuccess", "Player added successfully!");
            // Redirect back to the team details page using the ID from the *saved* player's team
            return "redirect:/teams/" + savedPlayer.getTeam().getId();
        } catch (Exception e) {
             redirectAttributes.addFlashAttribute("playerError", "Error adding player: " + e.getMessage());
             if (playerDto.getTeamId() != null) {
                 return "redirect:/teams/" + playerDto.getTeamId() + "?addPlayerError";
             } else {
                 return "redirect:/teams"; // Fallback redirect
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

        Long teamId = playerDto.getTeamId(); // Get teamId before potential errors

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.playerDto", result);
            redirectAttributes.addFlashAttribute("playerDto", playerDto);
             // Need playerId for redirect URL
            return "redirect:/players/" + playerId + "/edit?error";
        }

        try {
            playerService.updatePlayer(playerId, playerDto);
            redirectAttributes.addFlashAttribute("playerSuccess", "Player updated successfully!");
            // Redirect back to the team details page
            if (teamId == null) {
                 // Should have teamId from the form DTO
                 throw new IllegalStateException("Team ID missing during player update.");
            }
            return "redirect:/teams/" + teamId;
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