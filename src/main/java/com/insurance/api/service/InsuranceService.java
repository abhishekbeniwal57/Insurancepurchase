package com.insurance.api.service;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Insurance.Category;
import com.insurance.api.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InsuranceService {

    List<Insurance> getAllInsurances();

    Optional<Insurance> getInsuranceById(UUID id);

    List<Insurance> getInsurancesByCategory(Category category);

    List<Insurance> getRecommendedInsurances(User user);

    Optional<Insurance> getInsuranceByPolicyNumber(String policyNumber);

    Insurance createInsurance(Insurance insurance);

    Insurance updateInsurance(Long id, Insurance insurance);

    void deleteInsurance(Long id);

    List<Insurance> getInsurancesByCustomerName(String customerName);

    List<Insurance> getInsurancesByType(String type);

    List<Insurance> getActiveInsurances(boolean active);
}