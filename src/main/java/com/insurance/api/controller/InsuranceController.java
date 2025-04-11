package com.insurance.api.controller;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Insurance.Category;
import com.insurance.api.model.User;
import com.insurance.api.service.InsuranceService;
import com.insurance.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/insurances")
public class InsuranceController {

    private final InsuranceService insuranceService;
    private final UserService userService;

    @Autowired
    public InsuranceController(InsuranceService insuranceService, UserService userService) {
        this.insuranceService = insuranceService;
        this.userService = userService;
    }

    /**
     * List all available insurance policies
     */
    @GetMapping
    public ResponseEntity<List<Insurance>> getAllInsurances() {
        return ResponseEntity.ok(insuranceService.getAllInsurances());
    }

    /**
     * Get an insurance policy by its ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Insurance> getInsuranceById(@PathVariable UUID id) {
        Optional<Insurance> insurance = insuranceService.getInsuranceById(id);
        return insurance.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all insurances of a specific category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Insurance>> getInsurancesByCategory(@PathVariable Category category) {
        return ResponseEntity.ok(insuranceService.getInsurancesByCategory(category));
    }

    /**
     * Get recommended insurances based on the authenticated user's profile
     */
    @GetMapping("/recommended")
    public ResponseEntity<?> getRecommendedInsurances() {
        // Get the authenticated user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> userOpt = userService.getUserByUsername(username);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<Insurance> recommendedInsurances = insuranceService.getRecommendedInsurances(user);
            return ResponseEntity.ok(recommendedInsurances);
        } else {
            return ResponseEntity.status(403).body("User not found or not authenticated properly");
        }
    }

    @GetMapping("/policy/{policyNumber}")
    public ResponseEntity<Insurance> getInsuranceByPolicyNumber(@PathVariable String policyNumber) {
        Optional<Insurance> insurance = insuranceService.getInsuranceByPolicyNumber(policyNumber);
        return insurance.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<Insurance>> getInsurancesByCustomerName(@PathVariable String customerName) {
        List<Insurance> insurances = insuranceService.getInsurancesByCustomerName(customerName);
        return ResponseEntity.ok(insurances);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Insurance>> getInsurancesByType(@PathVariable String type) {
        List<Insurance> insurances = insuranceService.getInsurancesByType(type);
        return ResponseEntity.ok(insurances);
    }

    @GetMapping("/active/{active}")
    public ResponseEntity<List<Insurance>> getActiveInsurances(@PathVariable boolean active) {
        List<Insurance> insurances = insuranceService.getActiveInsurances(active);
        return ResponseEntity.ok(insurances);
    }

    @PostMapping
    public ResponseEntity<Insurance> createInsurance(@Valid @RequestBody Insurance insurance) {
        Insurance createdInsurance = insuranceService.createInsurance(insurance);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInsurance);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Insurance> updateInsurance(@PathVariable Long id, @Valid @RequestBody Insurance insurance) {
        Insurance updatedInsurance = insuranceService.updateInsurance(id, insurance);
        return ResponseEntity.ok(updatedInsurance);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInsurance(@PathVariable Long id) {
        insuranceService.deleteInsurance(id);
        return ResponseEntity.noContent().build();
    }
}