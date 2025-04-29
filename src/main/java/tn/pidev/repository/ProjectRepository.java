package tn.esprit.pidev.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidev.entities.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByNameContainingIgnoreCase(String name); // Recherche insensible à la casse par nom

}
