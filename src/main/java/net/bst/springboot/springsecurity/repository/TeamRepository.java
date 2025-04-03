package net.bst.springboot.springsecurity.repository;

import net.bst.springboot.springsecurity.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // Find all teams associated with a specific user ID
    List<Team> findByUserId(Long userId);

    // We might add custom query methods here later, e.g.:
    // Optional<Team> findByName(String name);
} 