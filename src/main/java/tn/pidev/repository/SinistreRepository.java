package tn.esprit.pidev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidev.entities.Sinistre;

@Repository
public interface SinistreRepository extends JpaRepository<Sinistre, Long> {
    // Additional query methods if needed
}
