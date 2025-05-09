package tn.esprit.pidev.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.pidev.entities.Contribution;

import java.util.List;

public interface ContributionRepository extends JpaRepository<Contribution, Integer> {
    @Query("SELECT c FROM Contribution c WHERE c.project.id_project = :projectId")
    List<Contribution> findByProjectId(@Param("projectId") Integer projectId);
    List<Contribution> findAll();
    @Query("SELECT c FROM Contribution c WHERE c.supporter_name = :supporterName")
    List<Contribution> findBySupporterName(@Param("supporterName") String supporterName);

}