package tn.esprit.pidev.controller;


import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidev.entities.*;
import tn.esprit.pidev.services.*;
import lombok.NoArgsConstructor;
//import org.hibernate.cfg.Environment;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import tn.esprit.pidev.entities.Investment;
import tn.esprit.pidev.entities.Investor;
import tn.esprit.pidev.services.InvestorService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
//import javax.mail.MessagingException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import java.util.List;

@RestController
@RequestMapping("/investments")
public class InvestmentController {

    private static final String TEMPLATE_NAME = "investment-confirmation";
    private static final String SPRING_LOGO_IMAGE = "templates/images/spring.png";
    private static final String PNG_MIME = "image/png";
    private static final String MAIL_SUBJECT = "Investment Confirmation";

    private final Environment environment;
    private final JavaMailSender mailSender;
    private final TemplateEngine htmlTemplateEngine;

    public InvestmentController(Environment environment, JavaMailSender mailSender, TemplateEngine htmlTemplateEngine) {
        this.environment = environment;
        this.mailSender = mailSender;
        this.htmlTemplateEngine = htmlTemplateEngine;
    }

    @Autowired
    InvestmentService investmentService;

    @Autowired
    ContractService contractService;

    // Create a new investment
//    @PostMapping("/addInvestment")
//    public ResponseEntity<Investment> createInvestment(@RequestBody Investment investment) {
//        Investment createdInvestment = investmentService.saveInvestment(investment);
//        return ResponseEntity.ok(createdInvestment);
//    }

    @PostMapping("/addInvestment")
    public ResponseEntity<Investment> createInvestment(@RequestBody Investment investment)
            throws MessagingException, IOException {

        // Save the investment
        Investment createdInvestment = investmentService.saveInvestment(investment);

        try {
            // Generate the contract PDF
            String contractFilePath = contractService.generateContract(createdInvestment);

            // Send the investment confirmation email with the contract attachment
            String mailResult = investmentService.sendMail(
                    new EmailDetails(), // Replace with actual email details
                    createdInvestment,
                    createdInvestment.getInvestor(), // Ensure this method exists and returns the correct investor
                    contractFilePath
            );

            System.out.println("Email sent with result: " + mailResult);
        } catch (IOException e) {
            System.err.println("Failed to generate contract or send email: " + e.getMessage());
            // Handle the exception (e.g., log it or return an error response)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(createdInvestment);
        }

        return ResponseEntity.ok(createdInvestment);
    }

    private void sendInvestmentConfirmationEmail(Investment investment,String contractFilePath) {
        // Extract investor details
        Investor investor = investment.getInvestor();

        // Create email details
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(investor.getEmail());
        emailDetails.setSubject("Investment Confirmation");
        emailDetails.setInvestment(investment);

        // Send the email
        investmentService.sendMail(emailDetails, investment, investor,contractFilePath);
    }
    @PostMapping("/generate/{investmentId}")
    public ResponseEntity<String> generateContract(@PathVariable Long investmentId) throws IOException {
        // Fetch the investment by ID
        Investment investment = investmentService.findInvestmentById(investmentId);
        if (investment == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the investor is null
        if (investment.getInvestor() == null) {
            return ResponseEntity.badRequest().body("Investment does not have an associated investor.");
        }

        // Generate the contract PDF
        String contractFilePath = contractService.generateContract(investment);

        // Return the file path of the generated contract
        return ResponseEntity.ok("Contract generated successfully: " + contractFilePath);
    }

    // Retrieve an investment by its ID
    @GetMapping("/getInvestment/{id}")
    public ResponseEntity<Investment> getInvestmentById(@PathVariable Long id) {
        Investment investment = investmentService.findInvestmentById(id);
        return ResponseEntity.ok(investment);
    }

    // Retrieve all investments
    @GetMapping("/getAllInvestment/")
    public ResponseEntity<List<Investment>> getAllInvestments() {
        List<Investment> investments = investmentService.findAllInvestments();
        return ResponseEntity.ok(investments);
    }

    // Update an existing investment
    @PutMapping("/editInvestment/{id}")
    public ResponseEntity<Investment> updateInvestment(@PathVariable Long id, @RequestBody Investment investment) {
        Investment updatedInvestment = investmentService.updateInvestment(id, investment);
        return ResponseEntity.ok(updatedInvestment);
    }

    // Delete an investment
    @DeleteMapping("/deleteInvestment/{id}")
    public ResponseEntity<Void> deleteInvestment(@PathVariable Long id) {
        investmentService.deleteInvestment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public List<Investment> filterInvestments(
            @RequestParam(required = false) InvestmentType investmentType,
            @RequestParam(required = false) Double minROI,
            @RequestParam(required = false) Double maxROI,
            @RequestParam(required = false) InvestmentStatus status,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) DividendPaymentFrequency dividendPaymentFrequency,
            @RequestParam(required = false) Double minDividendRate,
            @RequestParam(required = false) Double maxDividendRate
    ) {
        return investmentService.findInvestmentsByCriteria(
                investmentType,
                minROI,
                maxROI,
                status,
                minAmount,
                maxAmount,
                dividendPaymentFrequency,
                minDividendRate,
                maxDividendRate
        );
    }

    @PutMapping("/{id}/calculate-roi")
    public ResponseEntity<Investment> calculateAndUpdateROI(
            @PathVariable long id,
            @RequestParam double netProfit) {
        Investment updatedInvestment = investmentService.calculateAndUpdateROI(id, netProfit);
        return ResponseEntity.ok(updatedInvestment);
    }

    @GetMapping("/{investmentId}/roi")
    public ResponseEntity<Double> getROIForInvestment(
            @PathVariable long investmentId) {
        Double roi = investmentService.getROIForInvestment(investmentId);
        return ResponseEntity.ok(roi);
    }

    @GetMapping("/investors/{investorId}/roi")
    public ResponseEntity<List<Investment>> getInvestmentWithROIForInvestor(
            @PathVariable long investorId) {
        List<Investment> investments = investmentService.getInvestmentWithROIForInvestor(investorId);
        return ResponseEntity.ok(investments);
    }

    @Autowired
    private ExcelExportService excelExportService;

    @GetMapping("/export/excel")
    public ResponseEntity<ByteArrayResource> exportInvestmentsToExcel() throws IOException {
        // Fetch all investments
        List<Investment> investments = investmentService.findAllInvestments();

        // Generate Excel file
        byte[] excelBytes = excelExportService.exportInvestmentsToExcel(investments);

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=investments.xlsx");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // Return the file as a downloadable response
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelBytes.length)
                .body(new ByteArrayResource(excelBytes));
    }

    // Add these endpoints to your InvestmentController

    @PostMapping("/{investmentId}/npv")
    public ResponseEntity<Double> calculateNPVForInvestment(
            @PathVariable long investmentId,
            @RequestBody List<Double> cashFlows,
            @RequestParam Double discountRate) {
        Double npv = investmentService.calculateNPVForInvestment(investmentId, cashFlows, discountRate);
        return ResponseEntity.ok(npv);
    }

    @PostMapping("/{investmentId}/fv")
    public ResponseEntity<Double> calculateFVForInvestment(
            @PathVariable long investmentId,
            @RequestParam Double interestRate,
            @RequestParam Integer periods) {
        Double fv = investmentService.calculateFVForInvestment(investmentId, interestRate, periods);
        return ResponseEntity.ok(fv);
    }

    @PostMapping("/{investmentId}/pv")
    public ResponseEntity<Double> calculatePVForInvestment(
            @PathVariable long investmentId,
            @RequestParam Double interestRate,
            @RequestParam Integer periods) {
        Double pv = investmentService.calculatePVForInvestment(investmentId, interestRate, periods);
        return ResponseEntity.ok(pv);
    }

    @GetMapping("/{investmentId}/dividend")
    public ResponseEntity<Double> calculateDividendForInvestment(
            @PathVariable long investmentId) {
        Double dividend = investmentService.calculateDividendForInvestment(investmentId);
        return ResponseEntity.ok(dividend);
    }





}
