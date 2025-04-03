package net.bst.springboot.springsecurity.web;

import net.bst.springboot.springsecurity.model.Team;
import net.bst.springboot.springsecurity.model.Match;
import net.bst.springboot.springsecurity.service.MatchService;
import net.bst.springboot.springsecurity.service.TeamService; // Need TeamService to get teams for dropdowns
import net.bst.springboot.springsecurity.service.StatsService; // Import StatsService
import net.bst.springboot.springsecurity.web.dto.MatchDto;
import net.bst.springboot.springsecurity.web.dto.TeamStatsDto; // Import TeamStatsDto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/matches")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private TeamService teamService; // Inject TeamService

    @Autowired
    private StatsService statsService; // Inject StatsService

    // Handler to show the form for recording a new match
    @GetMapping("/new")
    public String showRecordMatchForm(Model model) {
        // Fetch all teams to populate dropdowns (consider security/filtering later)
        // For simplicity, fetching all teams now. Could filter by current user's teams if needed.
        List<Team> allTeams = teamService.findTeamsForCurrentUser(); // Or fetch all teams if desired

        model.addAttribute("matchDto", new MatchDto());
        model.addAttribute("allTeams", allTeams);

        return "record_match"; // View name: record_match.html
    }

    // Handler to process the match recording form submission
    @PostMapping
    public String recordMatch(@Valid @ModelAttribute("matchDto") MatchDto matchDto,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        // Basic validation: Check if Team A and Team B are the same
        if (matchDto.getTeamAId() != null && matchDto.getTeamAId().equals(matchDto.getTeamBId())) {
            result.rejectValue("teamBId", "match.teams.same", "Team A and Team B cannot be the same.");
        }

        if (result.hasErrors()) {
            // If validation errors, return to the form
            List<Team> allTeams = teamService.findTeamsForCurrentUser(); // Re-populate teams list
            model.addAttribute("allTeams", allTeams);
            return "record_match";
        }

        try {
            matchService.save(matchDto);
            redirectAttributes.addFlashAttribute("matchSuccess", "Match recorded successfully!");
            return "redirect:/matches/history"; // Redirect to a match history page (we'll create this next)
        } catch (Exception e) {
            // Handle potential errors during saving (e.g., EntityNotFoundException)
            List<Team> allTeams = teamService.findTeamsForCurrentUser(); // Re-populate teams list
            model.addAttribute("allTeams", allTeams);
            model.addAttribute("matchDto", matchDto); // Add DTO back to model
            model.addAttribute("errorMessage", "Error recording match: " + e.getMessage());
            return "record_match";
        }
    }

     // Handler to show match history (to be implemented)
     @GetMapping("/history")
     public String showMatchHistory(Model model) {
         // Fetch matches using MatchService
         model.addAttribute("matches", matchService.findAllMatches());
         return "match_history"; // View name: match_history.html
     }

    // Handler to show league standings
    @GetMapping("/standings")
    public String showLeagueStandings(Model model) {
        List<TeamStatsDto> standings = statsService.getLeagueStandings();
        model.addAttribute("standings", standings);
        return "league_standings"; // View name: league_standings.html
    }

    // Handler method to show the match edit form
    @GetMapping("/{id}/edit")
    public String showEditMatchForm(@PathVariable Long id, Model model) {
        Optional<Match> matchOpt = matchService.findMatchById(id);
        if (!matchOpt.isPresent()) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Match not found");
        }
        Match match = matchOpt.get();

        // Create DTO from Entity
        MatchDto matchDto = new MatchDto();
        matchDto.setTeamAId(match.getTeamA().getId());
        matchDto.setTeamBId(match.getTeamB().getId());
        matchDto.setScoreTeamA(match.getScoreTeamA());
        matchDto.setScoreTeamB(match.getScoreTeamB());
        matchDto.setTouchdownsTeamA(match.getTouchdownsTeamA());
        matchDto.setTouchdownsTeamB(match.getTouchdownsTeamB());
        matchDto.setCasualtiesTeamA(match.getCasualtiesTeamA());
        matchDto.setCasualtiesTeamB(match.getCasualtiesTeamB());

        List<Team> allTeams = teamService.findTeamsForCurrentUser(); // Or findAll depending on desired scope

        model.addAttribute("matchDto", matchDto);
        model.addAttribute("allTeams", allTeams);
        model.addAttribute("matchId", id);
        model.addAttribute("isEditMode", true); // Flag for the view

        return "record_match"; // Reuse the same template
    }

    // Handler method to process the match edit form submission
    @PostMapping("/{id}")
    public String updateMatch(@PathVariable Long id,
                             @Valid @ModelAttribute("matchDto") MatchDto matchDto,
                             BindingResult result,
                             Model model, // Keep model for error case
                             RedirectAttributes redirectAttributes) {

        if (matchDto.getTeamAId() != null && matchDto.getTeamAId().equals(matchDto.getTeamBId())) {
            result.rejectValue("teamBId", "match.teams.same", "Team A and Team B cannot be the same.");
        }

        if (result.hasErrors()) {
            List<Team> allTeams = teamService.findTeamsForCurrentUser();
            model.addAttribute("allTeams", allTeams);
            model.addAttribute("matchId", id); // Need matchId for form action
            model.addAttribute("isEditMode", true);
            return "record_match"; // Return to form view on error
        }

        try {
            matchService.updateMatch(id, matchDto);
            redirectAttributes.addFlashAttribute("matchSuccess", "Match updated successfully!");
            return "redirect:/matches/history";
        } catch (Exception e) {
            List<Team> allTeams = teamService.findTeamsForCurrentUser();
            model.addAttribute("allTeams", allTeams);
            model.addAttribute("matchId", id);
            model.addAttribute("isEditMode", true);
            model.addAttribute("errorMessage", "Error updating match: " + e.getMessage());
            return "record_match"; // Return to form view on error
        }
    }

    // Handler method to process match deletion
    @PostMapping("/{id}/delete")
    public String deleteMatch(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            matchService.deleteMatch(id);
            redirectAttributes.addFlashAttribute("matchSuccess", "Match deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("matchError", "Error deleting match: " + e.getMessage()); // Use specific error attr
        }
        return "redirect:/matches/history";
    }
} 