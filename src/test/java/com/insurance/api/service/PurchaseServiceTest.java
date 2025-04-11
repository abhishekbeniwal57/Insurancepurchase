package com.insurance.api.service;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Purchase;
import com.insurance.api.model.User;
import com.insurance.api.repository.PurchaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private PolicyDocumentService policyDocumentService;

    @InjectMocks
    private PurchaseService purchaseService;

    private User user;
    private Insurance insurance;
    private Purchase purchase;

    @BeforeEach
    void setUp() {
        // Create test user
        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");

        // Set first and last name if these methods exist
        try {
            user.getClass().getMethod("setFirstName", String.class).invoke(user, "Test");
            user.getClass().getMethod("setLastName", String.class).invoke(user, "User");
        } catch (Exception e) {
            // Methods not available, just continue
        }

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
    }

    @Test
    void purchaseInsurance_shouldCreateNewPurchase() {
        // Arrange
        when(purchaseRepository.save(any(Purchase.class))).thenAnswer(invocation -> {
            Purchase savedPurchase = invocation.getArgument(0);
            savedPurchase.setId(UUID.randomUUID());
            return savedPurchase;
        });

        // Act
        Purchase result = purchaseService.purchaseInsurance(user, insurance);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getPolicyNumber());
        assertEquals(user, result.getUser());
        assertEquals(insurance, result.getInsurance());
        assertNotNull(result.getPurchaseDate());
        verify(purchaseRepository, times(1)).save(any(Purchase.class));
    }

    @Test
    void getPurchaseById_withValidId_shouldReturnPurchase() {
        // Arrange
        UUID id = purchase.getId();
        when(purchaseRepository.findById(id)).thenReturn(Optional.of(purchase));

        // Act
        Optional<Purchase> result = purchaseService.getPurchaseById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(purchase.getPolicyNumber(), result.get().getPolicyNumber());
        verify(purchaseRepository, times(1)).findById(id);
    }

    @Test
    void getPurchaseById_withInvalidId_shouldReturnEmpty() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(purchaseRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<Purchase> result = purchaseService.getPurchaseById(id);

        // Assert
        assertFalse(result.isPresent());
        verify(purchaseRepository, times(1)).findById(id);
    }

    @Test
    void getPurchasesByUserId_shouldReturnUserPurchases() {
        // Arrange
        UUID userId = user.getId();
        when(purchaseRepository.findByUserId(userId)).thenReturn(List.of(purchase));

        // Act
        List<Purchase> result = purchaseService.getPurchasesByUserId(userId);

        // Assert
        assertEquals(1, result.size());
        assertEquals(purchase.getPolicyNumber(), result.get(0).getPolicyNumber());
        verify(purchaseRepository, times(1)).findByUserId(userId);
    }

    @Test
    void getPurchaseByPolicyNumber_withValidNumber_shouldReturnPurchase() {
        // Arrange
        String policyNumber = purchase.getPolicyNumber();
        when(purchaseRepository.findByPolicyNumber(policyNumber)).thenReturn(Optional.of(purchase));

        // Act
        Optional<Purchase> result = purchaseService.getPurchaseByPolicyNumber(policyNumber);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(policyNumber, result.get().getPolicyNumber());
        verify(purchaseRepository, times(1)).findByPolicyNumber(policyNumber);
    }

    @Test
    void generatePolicyDocument_shouldCallPolicyDocumentService() throws Exception {
        // Arrange
        byte[] expectedDocument = "Test document content".getBytes();
        when(policyDocumentService.generatePolicyDocument(purchase)).thenReturn(expectedDocument);

        // Act
        byte[] result = purchaseService.generatePolicyDocument(purchase);

        // Assert
        assertArrayEquals(expectedDocument, result);
        verify(policyDocumentService, times(1)).generatePolicyDocument(purchase);
    }
}