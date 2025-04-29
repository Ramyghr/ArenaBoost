package tn.esprit.pidev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;
import tn.esprit.pidev.entities.AthleteProfile;

import java.util.List;

public interface AthleteProfileRepository extends JpaRepository<AthleteProfile, Long> {
    List<AthleteProfile> findAthleteProfileBySportDiscipline(String sport);
    List<AthleteProfile> findAthleteProfileByFirstName(String name);











}