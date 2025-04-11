package com.insurance.api.service;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Insurance.Category;
import com.insurance.api.model.Insurance.RecommendedGender;
import com.insurance.api.model.User;
import com.insurance.api.repository.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;

    @Autowired
    public InsuranceServiceImpl(InsuranceRepository insuranceRepository) {
        this.insuranceRepository = insuranceRepository;
    }

    @Override
    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    @Override
    public Optional<Insurance> getInsuranceById(UUID id) {
        return insuranceRepository.findById(id);
    }

    @Override
    public List<Insurance> getInsurancesByCategory(Category category) {
        return insuranceRepository.findByCategory(category);
    }

    @Override
    public List<Insurance> getRecommendedInsurances(User user) {
        // Map User.Gender to Insurance.RecommendedGender
        RecommendedGender recommendedGender = null;
        if (user.getGender() != null) {
            switch (user.getGender()) {
                case MALE:
                    recommendedGender = RecommendedGender.MALE;
                    break;
                case FEMALE:
                    recommendedGender = RecommendedGender.FEMALE;
                    break;
                default:
                    recommendedGender = RecommendedGender.ANY;
            }
        }

        return insuranceRepository.findRecommendedInsurances(
                user.getAge(),
                recommendedGender,
                user.getIncome());
    }

    @Override
    public Optional<Insurance> getInsuranceByPolicyNumber(String policyNumber) {
        // For now, return empty as this is not implemented in repository
        return Optional.empty();
    }

    @Override
    public Insurance createInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    @Override
    public Insurance updateInsurance(Long id, Insurance insurance) {
        // For now, just save the insurance as is
        return insuranceRepository.save(insurance);
    }

    @Override
    public void deleteInsurance(Long id) {
        // Not implemented as we use UUID instead of Long
    }

    @Override
    public List<Insurance> getInsurancesByCustomerName(String customerName) {
        // For now, return all insurances as customer name search is not implemented
        return insuranceRepository.findAll();
    }

    @Override
    public List<Insurance> getInsurancesByType(String type) {
        // For now, return all insurances as type search is not implemented
        return insuranceRepository.findAll();
    }

    @Override
    public List<Insurance> getActiveInsurances(boolean active) {
        // For now, return all insurances as they are all considered active
        return insuranceRepository.findAll();
    }
}