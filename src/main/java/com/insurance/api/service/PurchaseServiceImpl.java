package com.insurance.api.service;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Purchase;
import com.insurance.api.model.Purchase.Status;
import com.insurance.api.model.User;
import com.insurance.api.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PolicyDocumentService policyDocumentService;

    @Value("${insurance.policy.document.path:policies}")
    private String policyDocumentBasePath;

    @Autowired
    public PurchaseServiceImpl(PurchaseRepository purchaseRepository, PolicyDocumentService policyDocumentService) {
        this.purchaseRepository = purchaseRepository;
        this.policyDocumentService = policyDocumentService;
    }

    @Override
    public Purchase purchaseInsurance(User user, Insurance insurance) {
        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setInsurance(insurance);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPolicyNumber(generatePolicyNumber(user, insurance));
        purchase.setPremium(insurance.getPremium());
        purchase.setStatus(Status.ACTIVE);

        // Save purchase
        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Generate policy document and update path
        try {
            generatePolicyDocument(savedPurchase);
            savedPurchase.setPolicyDocumentPath(getPolicyDocumentPath(savedPurchase));
            return purchaseRepository.save(savedPurchase);
        } catch (Exception e) {
            // Just log the error and continue without document
            System.err.println("Failed to generate policy document: " + e.getMessage());
            return savedPurchase;
        }
    }

    @Override
    public Optional<Purchase> getPurchaseByPolicyNumber(String policyNumber) {
        return purchaseRepository.findByPolicyNumber(policyNumber);
    }

    @Override
    public Optional<Purchase> getPurchaseById(UUID id) {
        return purchaseRepository.findById(id);
    }

    @Override
    public List<Purchase> getPurchasesByUserId(UUID userId) {
        return purchaseRepository.findByUserId(userId);
    }

    @Override
    public byte[] generatePolicyDocument(Purchase purchase) throws Exception {
        // Create directories if they don't exist
        Path directoryPath = Paths.get(policyDocumentBasePath);
        Files.createDirectories(directoryPath);

        // Generate PDF document using PolicyDocumentService
        byte[] pdfContent = policyDocumentService.generatePolicyDocument(purchase);

        // Save to file
        String filePath = getPolicyDocumentPath(purchase);
        Path path = Paths.get(filePath);

        // Write content to file
        try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
            fos.write(pdfContent);
        }

        // Update the purchase with the document path
        purchase.setPolicyDocumentPath(filePath);
        purchaseRepository.save(purchase);

        // Return the document as bytes
        return pdfContent;
    }

    // Helper methods
    private String generatePolicyNumber(User user, Insurance insurance) {
        // Generate a unique policy number based on user, insurance, and timestamp
        return "POL-" +
                user.getId().toString().substring(0, 8) + "-" +
                insurance.getId().toString().substring(0, 8) + "-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    private String getPolicyDocumentPath(Purchase purchase) {
        return policyDocumentBasePath + "/policy_" + purchase.getPolicyNumber() + ".pdf";
    }
}