package tn.esprit.pidev.services;

import tn.esprit.pidev.entities.*;
import tn.esprit.pidev.repository.InvestmentRepository;

import java.io.IOException;
import java.util.List;

public interface IInvestmentService {

    public Investment saveInvestment(Investment investment);
    public Investment findInvestmentById(long id);
    public List<Investment> findAllInvestments();
    public void deleteInvestment(long id);
    public Investment updateInvestment(long id, Investment investment);
    public String sendMail(EmailDetails details, Investment investment, Investor investor,String contractFilePath);
    public Investment calculateAndUpdateROI(long investmentId, double netProfit);
    public Double getROIForInvestment(long investmentId);
    public List<Investment> getInvestmentWithROIForInvestor(long investorId); // Corrected return type
    public List<Investment> getInvestmentsByInvestorId(Long investorId);
    public List<Investment> findInvestmentsByInvestorId(Long investorId);
    public List<Investment> findInvestmentsByCriteria(
            InvestmentType investmentType,
            Double minROI,
            Double maxROI,
            InvestmentStatus status,
            Double minAmount,
            Double maxAmount,
            DividendPaymentFrequency dividendPaymentFrequency,
            Double minDividendRate,
            Double maxDividendRate
    );
    public Double calculateNPVForInvestment(long investmentId, List<Double> cashFlows, Double discountRate);
    public Double calculateFVForInvestment(long investmentId, Double interestRate, Integer periods);
    public Double calculatePVForInvestment(long investmentId, Double interestRate, Integer periods);
    public Double calculateDividendForInvestment(long investmentId);
    public String monitorInvestmentPerformance(Investment investment) ;

    }