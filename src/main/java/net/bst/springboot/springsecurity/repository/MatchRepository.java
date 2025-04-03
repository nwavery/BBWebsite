package net.bst.springboot.springsecurity.repository;

import net.bst.springboot.springsecurity.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    // Find matches involving a specific team (either as teamA or teamB)
    List<Match> findByTeamAIdOrTeamBIdOrderByMatchDateDesc(Long teamAId, Long teamBId);

    // Find all matches ordered by date
    List<Match> findAllByOrderByMatchDateDesc();

    // Check if any matches exist for a given team ID
    boolean existsByTeamAIdOrTeamBId(Long teamAId, Long teamBId);
}