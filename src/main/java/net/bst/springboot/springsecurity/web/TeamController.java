package net.bst.springboot.springsecurity.web;

import net.bst.springboot.springsecurity.service.TeamService;
import net.bst.springboot.springsecurity.web.dto.TeamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

import net.bst.springboot.springsecurity.model.Team;
import net.bst.springboot.springsecurity.service.TeamServiceImpl;
import net.bst.springboot.springsecurity.web.dto.PlayerDto;
import javax.validation.Valid;

@Controller
@RequestMapping("/teams") // Base path for team-related requests
public class TeamController {

    @Autowired
    private TeamService teamService;

    // Handler method to show the team creation form
    @GetMapping("/new")
    public String showCreateTeamForm(Model model) {
        // Create model attribute to bind form data
        TeamDto teamDto = new TeamDto();
        model.addAttribute("team", teamDto);
        return "create_team"; // Return the view name (create_team.html)
    }

    // Handler method to process the team creation form submission
    @PostMapping
    public String createTeam(@ModelAttribute("team") TeamDto teamDto) {
        teamService.save(teamDto);
        return "redirect:/teams?teamCreatedSuccess"; // Redirect to list view
    }

    // Handler method to list teams for the current user
    @GetMapping
    public String listTeams(Model model) {
        List<Team> teams = teamService.findTeamsForCurrentUser();
        model.addAttribute("teams", teams);
        return "teams_list"; // Return the view name (teams_list.html)
    }

    // Handler method to show team details page
    @GetMapping("/{id}")
    public String viewTeam(@PathVariable Long id, Model model) {
        // Fetch team ensuring it belongs to the current user
        Optional<Team> teamOpt = ((TeamServiceImpl) teamService).findTeamByIdForCurrentUser(id);

        if (!teamOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found or access denied");
        }

        Team team = teamOpt.get();
        model.addAttribute("team", team);

        // Add an empty PlayerDto to the model for the 'Add Player' form
        PlayerDto playerDto = new PlayerDto();
        playerDto.setTeamId(id);
        model.addAttribute("newPlayer", playerDto);

        return "team_details";
    }

    // Handler method to show the team edit form
    @GetMapping("/{id}/edit")
    public String showEditTeamForm(@PathVariable Long id, Model model) {
        // Fetch team ensuring it belongs to the current user
        Optional<Team> teamOpt = ((TeamServiceImpl) teamService).findTeamByIdForCurrentUser(id);
        if (!teamOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found or access denied");
        }

        Team team = teamOpt.get();
        // Create a DTO from the entity to pre-populate the form
        TeamDto teamDto = new TeamDto(team.getName(), team.getRace());

        model.addAttribute("teamDto", teamDto);
        model.addAttribute("teamId", id);
        return "edit_team"; // View name: edit_team.html
    }

    // Handler method to process the team edit form submission
    @PostMapping("/{id}")
    public String updateTeam(@PathVariable Long id,
                             @Valid @ModelAttribute("teamDto") TeamDto teamDto,
                             BindingResult result, // Add validation if needed
                             RedirectAttributes redirectAttributes) {

        // Add validation checks if needed (e.g., @Valid, BindingResult)
         if (result.hasErrors()) {
             // Return to edit form if errors
             // Need to add teamId back to model for the form action URL
             // model.addAttribute("teamId", id); // No need if using redirectAttributes
             redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.teamDto", result);
             redirectAttributes.addFlashAttribute("teamDto", teamDto);
             return "redirect:/teams/" + id + "/edit?error";
         }

        try {
            teamService.update(id, teamDto);
            redirectAttributes.addFlashAttribute("teamUpdateSuccess", "Team updated successfully!");
            return "redirect:/teams"; // Redirect to the team list page
        } catch (Exception e) {
            // Handle errors (e.g., concurrency, validation)
            redirectAttributes.addFlashAttribute("errorMessage", "Error updating team: " + e.getMessage());
            redirectAttributes.addFlashAttribute("teamDto", teamDto);
            return "redirect:/teams/" + id + "/edit?error";
        }
    }

    // Handler method to process team deletion
    @PostMapping("/{id}/delete")
    public String deleteTeam(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            teamService.deleteTeam(id);
            redirectAttributes.addFlashAttribute("teamDeleteSuccess", "Team deleted successfully!");
        } catch (Exception e) {
            // Handle errors (e.g., team not found, access denied, related matches constraint)
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting team: " + e.getMessage());
        }
        return "redirect:/teams"; // Redirect to the team list page
    }

    // POST mapping for adding players remains in PlayerController

} 