package com.insurance.api.controller;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Purchase;
import com.insurance.api.model.User;
import com.insurance.api.service.InsuranceService;
import com.insurance.api.service.PurchaseService;
import com.insurance.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserService userService;
    private final InsuranceService insuranceService;

    @Autowired
    public PurchaseController(PurchaseService purchaseService, UserService userService,
            InsuranceService insuranceService) {
        this.purchaseService = purchaseService;
        this.userService = userService;
        this.insuranceService = insuranceService;
    }

    /**
     * Purchase an insurance policy
     */
    @PostMapping
    public ResponseEntity<?> purchaseInsurance(@RequestBody PurchaseRequest purchaseRequest) {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);
        Optional<Insurance> insuranceOpt = insuranceService.getInsuranceById(purchaseRequest.getInsuranceId());

        if (!userOpt.isPresent()) {
            return ResponseEntity.status(403).body("User not found or not authenticated properly");
        }

        if (!insuranceOpt.isPresent()) {
            return ResponseEntity.status(404).body("Insurance not found");
        }

        User user = userOpt.get();
        Insurance insurance = insuranceOpt.get();

        Purchase purchase = purchaseService.purchaseInsurance(user, insurance);
        return ResponseEntity.status(HttpStatus.CREATED).body(purchase);
    }

    /**
     * Get all purchases for the authenticated user
     */
    @GetMapping
    public ResponseEntity<?> getUserPurchases() {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);

        if (!userOpt.isPresent()) {
            return ResponseEntity.status(403).body("User not found or not authenticated properly");
        }

        User user = userOpt.get();
        List<Purchase> purchases = purchaseService.getPurchasesByUserId(user.getId());
        return ResponseEntity.ok(purchases);
    }

    /**
     * Get a specific purchase by ID or policy number
     */
    @GetMapping("/{idOrPolicyNumber}")
    public ResponseEntity<?> getPurchaseById(@PathVariable String idOrPolicyNumber) {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);

        if (!userOpt.isPresent()) {
            return ResponseEntity.status(403).body("User not found or not authenticated properly");
        }

        Optional<Purchase> purchaseOpt;

        // Try to find by policy number first
        purchaseOpt = purchaseService.getPurchaseByPolicyNumber(idOrPolicyNumber);

        // If not found, try to parse as UUID and find by ID
        if (!purchaseOpt.isPresent()) {
            try {
                UUID id = UUID.fromString(idOrPolicyNumber);
                purchaseOpt = purchaseService.getPurchaseById(id);
            } catch (IllegalArgumentException e) {
                // Not a valid UUID, ignore
            }
        }

        if (!purchaseOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(purchaseOpt.get());
    }

    /**
     * Download the policy document for a purchase
     */
    @GetMapping("/{idOrPolicyNumber}/policy")
    public ResponseEntity<?> getPolicyDocument(@PathVariable String idOrPolicyNumber) {
        // Get authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);

        if (!userOpt.isPresent()) {
            return ResponseEntity.status(403).body("User not found or not authenticated properly");
        }

        Optional<Purchase> purchaseOpt;

        // Try to find by policy number first
        purchaseOpt = purchaseService.getPurchaseByPolicyNumber(idOrPolicyNumber);

        // If not found, try to parse as UUID and find by ID
        if (!purchaseOpt.isPresent()) {
            try {
                UUID id = UUID.fromString(idOrPolicyNumber);
                purchaseOpt = purchaseService.getPurchaseById(id);
            } catch (IllegalArgumentException e) {
                // Not a valid UUID, ignore
            }
        }

        if (!purchaseOpt.isPresent()) {
            return ResponseEntity.status(404).body("Purchase not found with ID or policy number: " + idOrPolicyNumber);
        }

        Purchase purchase = purchaseOpt.get();

        try {
            byte[] documentBytes = purchaseService.generatePolicyDocument(purchase);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "policy_" + purchase.getPolicyNumber() + ".pdf");

            return new ResponseEntity<>(documentBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to generate policy document: " + e.getMessage());
        }
    }

    // Request class for purchasing insurance
    public static class PurchaseRequest {
        private UUID insuranceId;

        public UUID getInsuranceId() {
            return insuranceId;
        }

        public void setInsuranceId(UUID insuranceId) {
            this.insuranceId = insuranceId;
        }
    }
}