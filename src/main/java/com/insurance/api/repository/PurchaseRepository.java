package com.insurance.api.repository;

import com.insurance.api.model.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {

    List<Purchase> findByUserId(UUID userId);

    Optional<Purchase> findByUserIdAndInsuranceId(UUID userId, UUID insuranceId);

    Optional<Purchase> findByPolicyNumber(String policyNumber);
}