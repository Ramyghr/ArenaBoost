package tn.esprit.pidev.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidev.entities.Investor;

import java.util.List;

@Repository
public interface InvestorRepository extends JpaRepository<Investor, Long> {
    List<Investor> findInvestorById(long investorId);
}
