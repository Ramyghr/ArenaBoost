package tn.esprit.pidev.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.entities.AthleteProfile;
import tn.esprit.pidev.entities.Investment;
import tn.esprit.pidev.entities.Investor;
import tn.esprit.pidev.entities.Manager;
import tn.esprit.pidev.repository.InvestorRepository;

import java.util.List;


public interface IInvestorService {


    public Investor createInvestor(Investor investor);
    public Investor getInvestorById(long id);
    public List<Investor> getAllInvestor();
    public Investor updateInvestor(long id,Investor investor);
    public void deleteInvestor(long id);
    public List<AthleteProfile> searchAthletesByName(String name);
    public List<AthleteProfile> searchAthletesBySport(String sport);
    public List<Manager> searchManagersByName(String name);
    public List<Manager> searchManagersBySport(String sport);
    public List<Manager> searchManagersByFinancialStatus(String financialStatus);
    //public report

}
