package tn.esprit.pidev.services;

import jakarta.mail.MessagingException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.entities.Investment;
import tn.esprit.pidev.entities.InvestmentStatus;
import tn.esprit.pidev.entities.InvestmentType;
import tn.esprit.pidev.entities.DividendPaymentFrequency;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import tn.esprit.pidev.entities.*;
import tn.esprit.pidev.repository.InvestmentRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InvestmentService implements IInvestmentService {

    private static final Logger logger = LoggerFactory.getLogger(InvestmentService.class);

    @Autowired
    InvestmentRepository investmentRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    public Investment saveInvestment(Investment investment) {
        return investmentRepository.save(investment);
    }

    @Override
    public Investment findInvestmentById(long id) {
        return investmentRepository.findById(id).orElseThrow(() -> new RuntimeException("Investment not found with ID:" + id));
    }

    @Override
    public List<Investment> findAllInvestments() {
        return investmentRepository.findAll();
    }


    @Override
    public void deleteInvestment(long id) {
        investmentRepository.deleteById(id);
    }

    @Override
    public Investment updateInvestment(long id, Investment investment) {
        Investment existingInvestment = findInvestmentById(id);
        existingInvestment.setAmount(investment.getAmount());
        existingInvestment.setStartDate(investment.getStartDate());
        existingInvestment.setEndDate(investment.getEndDate());
        existingInvestment.setInvestmentType(investment.getInvestmentType());
        existingInvestment.setRoiPercentage(investment.getRoiPercentage());
        existingInvestment.setStatus(investment.getStatus());
        return investmentRepository.save(investment);
    }
    @Override
    public String monitorInvestmentPerformance(Investment investment) {
        double actualROI = investment.getActualROI();
        double expectedROI = investment.getExpectedROI();
        if (actualROI >= expectedROI) {
            return "On Track";
        } else {
            return "Underperforming";
        }
    }
    @Override
    public String sendMail(EmailDetails details, Investment investment, Investor investor, String contractFilePath)
             {
        try {
            // Create the email body with investment and investor details
            String emailBody = "<html><body>"
                    + "<p>Dear " + investor.getName() + ",</p>"
                    + "<p>Thank you for your investment. Below are the details:</p>"
                    + "<ul>"
                    + "<li><strong>Your Budget:</strong> " + investor.getInvestment_budget() + "</li>"
                    + "<li><strong>Your Preferred Sport:</strong> " + investor.getPreferredSport() + "</li>"
                    + "<li><strong>Your Risk Tolerance:</strong> " + investor.getRiskTolerance() + "</li>"
                    + "<li><strong>Investment ID:</strong> " + investment.getInvestmentId() + "</li>"
                    + "<li><strong>Amount:</strong> " + investment.getAmount() + " TND</li>"
                    + "<li><strong>Created At:</strong> " + investment.getCreatedAt() + "</li>"
                    + "</ul>"
                    + "<p>Best regards,<br>Your Investment Team</p>"
                    + "<p><img src='cid:logo' alt='Startup Logo'></p>" // Embed logo here
                    + "</body></html>";

            // Create a MimeMessage
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true indicates multipart message

            // Set email details
            helper.setFrom(sender);
            helper.setTo(investor.getEmail());
            helper.setSubject("Investment Confirmation");
            helper.setText(emailBody, true); // true indicates HTML content

            // Add the logo as an inline attachment
            Resource logoResource = new ClassPathResource("templates/images/arenaboost.png");
            helper.addInline("logo", logoResource); // "logo" matches the cid in the HTML

            // Attach the contract PDF if the file path is provided and the file exists
            if (contractFilePath != null && Files.exists(Paths.get(contractFilePath))) {
                FileSystemResource file = new FileSystemResource(new File(contractFilePath));
                helper.addAttachment("Investment_Contract.pdf", file);
            } else {
                System.out.println("Contract file does not exist: " + contractFilePath);
            }

            // Send the email
            javaMailSender.send(mimeMessage);
            return "Mail Sent Successfully...";
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
            return "Error while Sending Mail";
        }
    }

    @Override
    public Investment calculateAndUpdateROI(long investmentId, double netProfit) {
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new RuntimeException("Investment not found"));
        double actualROI = (netProfit / investment.getAmount()) * 100; // Actual ROI calculation
        investment.setActualROI(actualROI);
        investment.setNetProfit(netProfit);
        return investmentRepository.save(investment);
    }

    @Override
    public Double getROIForInvestment(long investmentId) {
        Investment investment = investmentRepository.findById(investmentId)
                .orElseThrow(() -> new RuntimeException("Investment not found"));
        return investment.getRoiPercentage();
    }

    @Override
    public List<Investment> getInvestmentWithROIForInvestor(long investorId) {
        return investmentRepository.findByInvestor_Id(investorId);
    }

    @Override
    public Double calculateNPVForInvestment(long investmentId, List<Double> cashFlows, Double discountRate) {
        Investment investment = findInvestmentById(investmentId);
        Double npv = 0.0;
        for (int i = 0; i < cashFlows.size(); i++) {
            npv += cashFlows.get(i) / Math.pow(1 + discountRate, i);
        }
        investment.setNpv(npv); // Assuming you have an npv field in Investment entity
        investmentRepository.save(investment);
        return npv;
    }

    @Override
    public Double calculateFVForInvestment(long investmentId, Double interestRate, Integer periods) {
        Investment investment = findInvestmentById(investmentId);
        Double fv = investment.getAmount() * Math.pow(1 + interestRate, periods);
        investment.setFutureValue(fv); // Assuming you have a futureValue field
        investmentRepository.save(investment);
        return fv;
    }

    @Override
    public Double calculateDividendForInvestment(long investmentId) {
        Investment investment = findInvestmentById(investmentId);
        if (investment.getDividendRate() == null || investment.getDividendPaymentFrequency() == null) {
            throw new RuntimeException("Dividend rate or frequency not set for investment");
        }
        double annualDividend = investment.getAmount() * (investment.getDividendRate() / 100);
        Double dividend = switch (investment.getDividendPaymentFrequency()) {
            case MONTHLY -> annualDividend / 12;
            case QUARTERLY -> annualDividend / 4;
            case ANNUALLY -> annualDividend;
            case NONE -> null;
        };
        investment.setDividendPayment(dividend); // Assuming you have a dividendPayment field
        investmentRepository.save(investment);
        return dividend;
    }

    @Override
    public Double calculatePVForInvestment(long investmentId, Double interestRate, Integer periods) {
        Investment investment = findInvestmentById(investmentId);
        if (investment.getFutureValue() == null) {
            throw new RuntimeException("Future value not calculated for investment");
        }
        Double pv = investment.getFutureValue() / Math.pow(1 + interestRate, periods);
        investment.setPresentValue(pv); // Assuming you have a presentValue field
        investmentRepository.save(investment);
        return pv;
    }

    @Override
    public List<Investment> getInvestmentsByInvestorId(Long investorId){
        return investmentRepository.findByInvestor_Id(investorId);
    }

    @Override
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
    ) {
        logger.info("Filtering investments with criteria: investmentType={}, minROI={}, maxROI={}, status={}, minAmount={}, maxAmount={}, dividendPaymentFrequency={}, minDividendRate={}, maxDividendRate={}",
                investmentType, minROI, maxROI, status, minAmount, maxAmount, dividendPaymentFrequency, minDividendRate, maxDividendRate);

        Specification<Investment> spec = Specification.where(null);

        if (investmentType != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("investmentType"), investmentType));
            logger.info("Applied filter: investmentType={}", investmentType);
        }

        if (minROI != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("expectedROI"), minROI));
            logger.info("Applied filter: minROI={}", minROI);
        }

        if (maxROI != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("expectedROI"), maxROI));
            logger.info("Applied filter: maxROI={}", maxROI);
        }

        if (status != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("status"), status));
            logger.info("Applied filter: status={}", status);
        }

        if (minAmount != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minAmount));
            logger.info("Applied filter: minAmount={}", minAmount);
        }

        if (maxAmount != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxAmount));
            logger.info("Applied filter: maxAmount={}", maxAmount);
        }

        if (dividendPaymentFrequency != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("dividendPaymentFrequency"), dividendPaymentFrequency));
            logger.info("Applied filter: dividendPaymentFrequency={}", dividendPaymentFrequency);
        }

        if (minDividendRate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("dividendRate"), minDividendRate));
            logger.info("Applied filter: minDividendRate={}", minDividendRate);
        }

        if (maxDividendRate != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("dividendRate"), maxDividendRate));
            logger.info("Applied filter: maxDividendRate={}", maxDividendRate);
        }

        List<Investment> result = investmentRepository.findAll(spec);
        logger.info("Found {} investments matching criteria", result.size());
        return result;
    }




    @Override
    public List<Investment> findInvestmentsByInvestorId(Long investorId) {
        return investmentRepository.findByInvestor_Id(investorId); // Fetch investments by investor ID
    }
}
