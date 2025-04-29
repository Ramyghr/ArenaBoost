package tn.esprit.pidev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidev.entities.Complaint;
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,Long> {
}
