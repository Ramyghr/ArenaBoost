package tn.esprit.pidev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.pidev.entities.AthleteProfile;
import tn.esprit.pidev.entities.AthleteProfile;

import java.util.List;

public interface AthleteRepository extends JpaRepository<AthleteProfile, Long> {

//    // Search athletes by name (derived query method)
//    List<AthleteProfile> findAthletesByAthleteNameContains(String name );
//
//    // Search athletes by sport (derived query method)
//    List<AthleteProfile> findAthletesBySportType(String sport);
}
