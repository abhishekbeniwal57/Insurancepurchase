package com.insurance.api.service;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Purchase;
import com.insurance.api.model.User;
import com.insurance.api.service.impl.PolicyDocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PolicyDocumentServiceTest {

    @InjectMocks
    private PolicyDocumentServiceImpl policyDocumentService;

    private Purchase purchase;
    private User user;
    private Insurance insurance;

    @BeforeEach
    void setUp() {
        // Create test user
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");

        // Create test insurance
        insurance = new Insurance();
        insurance.setId(UUID.randomUUID());
        insurance.setName("Life Insurance Premium");
        insurance.setDescription("Comprehensive life insurance coverage");
        insurance.setCategory(Insurance.Category.LIFE);
        insurance.setPremium(BigDecimal.valueOf(100.0));
        insurance.setCoverageAmount(BigDecimal.valueOf(100000.0));

        // Create test purchase
        purchase = new Purchase();
        purchase.setId(UUID.randomUUID());
        purchase.setUser(user);
        purchase.setInsurance(insurance);
        purchase.setPurchaseDate(LocalDateTime.now());
        purchase.setPolicyNumber("POL-123456");
        purchase.setPremium(BigDecimal.valueOf(100.0));
    }

    @Test
    void generatePolicyDocument_shouldReturnPdfBytes() {
        // Act
        byte[] result = policyDocumentService.generatePolicyDocument(purchase);

        // Assert
        assertNotNull(result);
        assertTrue(result.length > 0);

        // Verify PDF signature (%PDF-) which should be at the beginning of all PDF
        // files
        byte[] pdfSignature = { 0x25, 0x50, 0x44, 0x46, 0x2D }; // %PDF- in ASCII
        byte[] resultStart = new byte[5];
        System.arraycopy(result, 0, resultStart, 0, 5);
        assertArrayEquals(pdfSignature, resultStart, "Document should start with PDF signature");
    }
}