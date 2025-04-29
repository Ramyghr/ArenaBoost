package tn.esprit.pidev.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.entities.AthleteProfile;
import tn.esprit.pidev.entities.AthleteProfile;
import tn.esprit.pidev.entities.Investor;
import tn.esprit.pidev.entities.Manager;
import tn.esprit.pidev.repository.AthleteProfileRepository;
import tn.esprit.pidev.repository.AthleteRepository;
import tn.esprit.pidev.repository.InvestorRepository;
import tn.esprit.pidev.repository.ManagerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class InvestorService implements IInvestorService{

    @Autowired
    private InvestorRepository investorRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private AthleteProfileRepository athleteRepository;

    @Override
    public Investor createInvestor(Investor investor) {
        return investorRepository.save(investor);
    }

    @Override
    public Investor getInvestorById(long id) {
        return investorRepository.findById(id).orElseThrow(()->new RuntimeException("Investor not found with ID :"+id));
    }

    @Override
    public List<Investor> getAllInvestor() {
        return investorRepository.findAll();
    }

    @Override
    public Investor updateInvestor(long id, Investor investor) {
        Investor existingInvestor = getInvestorById(id);
        existingInvestor.setName(investor.getName());
        existingInvestor.setEmail(investor.getEmail());
        existingInvestor.setPhone(investor.getPhone());
        existingInvestor.setInvestment_budget(investor.getInvestment_budget());
        existingInvestor.setUpdatedAt(LocalDateTime.now());
        return investorRepository.save(existingInvestor);
    }

    @Override
    public void deleteInvestor(long id) {
        investorRepository.deleteById(id);
    }

    @Override
    public List<AthleteProfile> searchAthletesByName(String name) {
        return athleteRepository.findAthleteProfileByFirstName(name);
    }

    @Override
    public List<AthleteProfile> searchAthletesBySport(String sport) {
        return athleteRepository.findAthleteProfileBySportDiscipline(sport);
    }

    @Override
    public List<Manager> searchManagersByName(String name) {
        return managerRepository.findManagerByManagerNameContaining(name);
    }

    @Override
    public List<Manager> searchManagersBySport(String sport) {
        return managerRepository.findManagerBySport(sport);
    }

    // Search managers by financial status
    public List<Manager> searchManagersByFinancialStatus(String financialStatus) {
        return managerRepository.findManagerByFinancialStatus(financialStatus);
    }

}
