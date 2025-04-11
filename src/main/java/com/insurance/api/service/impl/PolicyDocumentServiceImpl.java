package com.insurance.api.service.impl;

import com.insurance.api.model.Purchase;
import com.insurance.api.service.PolicyDocumentService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

/**
 * Implementation of PolicyDocumentService that generates PDF policy documents
 * using iText.
 */
@Service
public class PolicyDocumentServiceImpl implements PolicyDocumentService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public byte[] generatePolicyDocument(Purchase purchase) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Add header
            Paragraph header = new Paragraph("Insurance Policy Document")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(header);
            document.add(new Paragraph("\n"));

            // Add policy details in a table
            Table table = new Table(UnitValue.createPercentArray(2)).useAllAvailableWidth();

            // Add rows with null checks
            addTableRow(table, "Policy Number", purchase.getPolicyNumber());

            // Handle null values for customer properties
            if (purchase.getCustomerName() != null) {
                addTableRow(table, "Customer Name", purchase.getCustomerName());
            } else if (purchase.getUser() != null) {
                addTableRow(table, "Customer Name", purchase.getUser().getUsername());
            } else {
                addTableRow(table, "Customer Name", "Not specified");
            }

            // Handle null values for insurance type
            if (purchase.getInsuranceType() != null) {
                addTableRow(table, "Insurance Type", purchase.getInsuranceType());
            } else if (purchase.getInsurance() != null && purchase.getInsurance().getCategory() != null) {
                addTableRow(table, "Insurance Type", purchase.getInsurance().getCategory().toString());
            } else {
                addTableRow(table, "Insurance Type", "Not specified");
            }

            // Handle null values for coverage amount
            if (purchase.getCoverageAmount() != null) {
                addTableRow(table, "Coverage Amount", String.format("$%,.2f", purchase.getCoverageAmount()));
            } else if (purchase.getInsurance() != null && purchase.getInsurance().getCoverageAmount() != null) {
                addTableRow(table, "Coverage Amount",
                        String.format("$%,.2f", purchase.getInsurance().getCoverageAmount()));
            } else {
                addTableRow(table, "Coverage Amount", "Not specified");
            }

            // Handle null values for premium amount
            if (purchase.getPremiumAmount() != null) {
                addTableRow(table, "Premium Amount", String.format("$%,.2f", purchase.getPremiumAmount()));
            } else if (purchase.getPremium() != null) {
                addTableRow(table, "Premium Amount", String.format("$%,.2f", purchase.getPremium()));
            } else {
                addTableRow(table, "Premium Amount", "Not specified");
            }

            // Handle null values for dates
            if (purchase.getStartDate() != null) {
                addTableRow(table, "Start Date", purchase.getStartDate().format(DATE_FORMATTER));
            } else if (purchase.getPurchaseDate() != null) {
                addTableRow(table, "Start Date", purchase.getPurchaseDate().format(DATE_FORMATTER));
            } else {
                addTableRow(table, "Start Date", "Not specified");
            }

            if (purchase.getEndDate() != null) {
                addTableRow(table, "End Date", purchase.getEndDate().format(DATE_FORMATTER));
            } else {
                addTableRow(table, "End Date", "Not specified");
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            // Add terms and conditions
            document.add(new Paragraph("Terms and Conditions").setBold());
            document.add(new Paragraph("1. This policy is valid for the specified coverage period."));
            document.add(new Paragraph("2. Claims must be submitted within 30 days of the incident."));
            document.add(new Paragraph("3. Coverage is subject to the terms outlined in the policy agreement."));

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            // Log the error and rethrow
            System.err.println("Error generating policy document: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to generate policy document", e);
        }
    }

    private void addTableRow(Table table, String key, String value) {
        table.addCell(new Paragraph(key).setBold());
        table.addCell(new Paragraph(value != null ? value : ""));
    }
}