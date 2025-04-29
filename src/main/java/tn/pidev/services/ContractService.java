package tn.esprit.pidev.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;
import tn.esprit.pidev.entities.Investment;
import tn.esprit.pidev.entities.Investor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ContractService {

    private static final float MARGIN = 50;
    private static final float LEADING = 20;

    public String generateContract(Investment investment) throws IOException {
        String fileName = "contract_" + investment.getInvestmentId() + ".pdf";
        String filePath = "C:/exports/" + fileName;

        Files.createDirectories(Paths.get("C:/exports"));

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            // Main content generation
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            float yPosition = page.getMediaBox().getHeight() - MARGIN;

            // Title
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
            writeCenteredText(contentStream, "INVESTMENT CONTRACT AGREEMENT", yPosition, page);
            yPosition -= LEADING * 2;

            // Divider line
            drawHorizontalLine(contentStream, MARGIN, yPosition, page.getMediaBox().getWidth() - MARGIN * 2);
            yPosition -= LEADING * 2;

            // Contract content
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            String[] sections = generateContractContent(investment).split("\n");

            for (String line : sections) {
                if (yPosition < MARGIN + 100) { // Need new page
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = page.getMediaBox().getHeight() - MARGIN;
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                }

                if (line.matches("^\\d+\\..*")) { // Section header
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                    writeText(contentStream, line, MARGIN, yPosition);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                } else if (line.trim().startsWith("-")) { // Bullet point
                    writeText(contentStream, "• " + line.substring(1).trim(), MARGIN + 20, yPosition);
                } else { // Regular text
                    writeText(contentStream, line, MARGIN, yPosition);
                }
                yPosition -= LEADING;
            }

            // Add signatures
            addSignatures(document, contentStream, investment, page, yPosition);
            contentStream.close();

            document.save(filePath);
        }

        return filePath;
    }

    private void addSignatures(PDDocument document, PDPageContentStream contentStream,
                               Investment investment, PDPage page, float yPosition) throws IOException {
        // If not enough space, create new page for signatures
        if (yPosition < MARGIN + 100) {
            contentStream.close();
            page = new PDPage();
            document.addPage(page);
            contentStream = new PDPageContentStream(document, page);
            yPosition = page.getMediaBox().getHeight() - MARGIN;
        }

        float signatureY = yPosition - LEADING * 2;
        float pageWidth = page.getMediaBox().getWidth();

        // Signature header
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        writeCenteredText(contentStream, "IN WITNESS WHEREOF", signatureY, page);
        writeCenteredText(contentStream, "the parties hereto have executed this Agreement", signatureY - LEADING, page);
        writeCenteredText(contentStream, "as of the date first above written.", signatureY - LEADING * 2, page);

        // Investor signature
        float investorX = MARGIN;
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        writeText(contentStream, "_________________________", investorX, signatureY - LEADING * 4);
        writeText(contentStream, "Investor Signature", investorX, signatureY - LEADING * 5);
        writeText(contentStream, "Name: " + (investment.getInvestor() != null ? investment.getInvestor().getName() : "N/A"),
                investorX, signatureY - LEADING * 6);
        writeText(contentStream, "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                investorX, signatureY - LEADING * 7);

        // Company signature (right-aligned)
        float companyX = pageWidth - MARGIN - 150;
        writeText(contentStream, "_________________________", companyX, signatureY - LEADING * 4);
        writeText(contentStream, "Company Representative Signature", companyX, signatureY - LEADING * 5);
        writeText(contentStream, "Name: [Authorized Signatory]", companyX, signatureY - LEADING * 6);
        writeText(contentStream, "Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                companyX, signatureY - LEADING * 7);

        contentStream.close();
    }

    private void writeText(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private void writeCenteredText(PDPageContentStream contentStream, String text, float y, PDPage page) throws IOException {
        float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth(text) / 1000 * 16;
        float x = (page.getMediaBox().getWidth() - titleWidth) / 2;
        writeText(contentStream, text, x, y);
    }

    private void drawHorizontalLine(PDPageContentStream contentStream, float x, float y, float length) throws IOException {
        contentStream.setLineWidth(1f);
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + length, y);
        contentStream.stroke();
    }

    private String generateContractContent(Investment investment) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder content = new StringBuilder();

        // Header
        content.append("This Investment Contract Agreement (the \"Agreement\")\n")
                .append("is made and entered into as of ")
                .append(formatter.format(investment.getStartDate()))
                .append("\nby and between:\n\n");

        // 1. PARTIES
        content.append("1. PARTIES\n");
        Investor investor = investment.getInvestor();
        content.append("Investor: ").append(investor != null ? investor.getName() : "N/A")
                .append(", ID: ").append(investor != null ? investor.getInvestorId() : "N/A").append("\n");
        content.append("Company: Sports Investment Management LLC, registered in Tunisia\n\n");

        // 2. INVESTMENT DETAILS
        content.append("2. INVESTMENT DETAILS\n");
        content.append("Investment ID: ").append(investment.getInvestmentId()).append("\n");
        content.append("Type: ").append(investment.getInvestmentType()).append("\n");
        content.append("Amount: ").append(String.format("%,.2f TND", investment.getAmount())).append("\n");
        content.append("Term: From ").append(formatter.format(investment.getStartDate()))
                .append(" to ").append(formatter.format(investment.getEndDate())).append("\n\n");

        // 3. TERMS
        content.append("3. TERMS AND CONDITIONS\n");
        content.append("The Investor agrees to provide funding and the Company agrees to:\n");

        switch (investment.getInvestmentType()) {
            case ATHLETE_SPONSORSHIP:
                content.append("- Use funds for athlete training and development\n");
                content.append("- Provide quarterly performance reports\n");
                content.append("- Organize promotional events\n");
                break;
            case CLUB_SPONSORSHIP:
                content.append("- Allocate funds for club operations\n");
                content.append("- Display Investor branding\n");
                content.append("- Provide biannual financial statements\n");
                break;
            case PROJECT_FUNDING:
                content.append("- Use funds exclusively for the project\n");
                content.append("- Provide monthly progress reports\n");
                content.append("- Return unused funds upon completion\n");
                break;
            default:
                content.append("- Use funds as mutually agreed\n");
        }
        content.append("\n");

        // 4. GENERAL PROVISIONS
        content.append("4. GENERAL PROVISIONS\n");
        content.append("- This Agreement is governed by Tunisian law\n");
        content.append("- Disputes will be resolved through arbitration\n");
        content.append("- Amendments require written consent\n");

        return content.toString();
    }
}