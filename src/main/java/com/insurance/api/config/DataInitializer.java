package com.insurance.api.config;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Insurance.Category;
import com.insurance.api.model.Insurance.RecommendedGender;
import com.insurance.api.model.User;
import com.insurance.api.model.User.Gender;
import com.insurance.api.repository.InsuranceRepository;
import com.insurance.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("!prod") // Only run in non-production environments
public class DataInitializer implements CommandLineRunner {

    private final InsuranceRepository insuranceRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(InsuranceRepository insuranceRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.insuranceRepository = insuranceRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Initialize users if none exist
        if (userRepository.count() == 0) {
            createTestUsers();
        }

        // Only initialize insurances if the database is empty
        if (insuranceRepository.count() == 0) {
            createInsurances();
        }
    }

    private void createTestUsers() {
        // Create a test user
        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setEmail("test@example.com");
        testUser.setAge(35);
        testUser.setGender(Gender.MALE);
        testUser.setIncome(new BigDecimal("80000"));

        userRepository.save(testUser);

        System.out.println("Created test user: testuser / password123");
    }

    private void createInsurances() {
        List<Insurance> sampleInsurances = Arrays.asList(
                // Health Insurance Plans
                createInsurance(
                        "Basic Health Coverage",
                        "Essential health coverage for individuals and families",
                        new BigDecimal("50000"),
                        new BigDecimal("200"),
                        Category.HEALTH,
                        18,
                        65,
                        RecommendedGender.ANY,
                        new BigDecimal("30000"),
                        new BigDecimal("100000")),
                createInsurance(
                        "Premium Health Plus",
                        "Comprehensive health coverage with extended benefits",
                        new BigDecimal("100000"),
                        new BigDecimal("400"),
                        Category.HEALTH,
                        25,
                        55,
                        RecommendedGender.ANY,
                        new BigDecimal("50000"),
                        new BigDecimal("200000")),
                createInsurance(
                        "Senior Health Care",
                        "Specialized health coverage for seniors",
                        new BigDecimal("75000"),
                        new BigDecimal("300"),
                        Category.HEALTH,
                        60,
                        85,
                        RecommendedGender.ANY,
                        new BigDecimal("20000"),
                        new BigDecimal("100000")),

                // Auto Insurance Options
                createInsurance(
                        "Basic Auto Coverage",
                        "Essential auto insurance coverage",
                        new BigDecimal("25000"),
                        new BigDecimal("150"),
                        Category.AUTO,
                        18,
                        75,
                        RecommendedGender.ANY,
                        new BigDecimal("20000"),
                        new BigDecimal("100000")),
                createInsurance(
                        "Premium Auto Protection",
                        "Comprehensive auto coverage with additional benefits",
                        new BigDecimal("50000"),
                        new BigDecimal("300"),
                        Category.AUTO,
                        21,
                        65,
                        RecommendedGender.ANY,
                        new BigDecimal("40000"),
                        new BigDecimal("150000")),
                createInsurance(
                        "Young Driver Auto",
                        "Specialized coverage for young drivers",
                        new BigDecimal("35000"),
                        new BigDecimal("250"),
                        Category.AUTO,
                        18,
                        25,
                        RecommendedGender.ANY,
                        new BigDecimal("25000"),
                        new BigDecimal("80000")),

                // Home Insurance Options
                createInsurance(
                        "Basic Home Protection",
                        "Essential home insurance coverage",
                        new BigDecimal("200000"),
                        new BigDecimal("800"),
                        Category.HOME,
                        21,
                        75,
                        RecommendedGender.ANY,
                        new BigDecimal("40000"),
                        new BigDecimal("150000")),
                createInsurance(
                        "Premium Home Security",
                        "Comprehensive home coverage with extended protection",
                        new BigDecimal("500000"),
                        new BigDecimal("1500"),
                        Category.HOME,
                        25,
                        65,
                        RecommendedGender.ANY,
                        new BigDecimal("80000"),
                        new BigDecimal("300000")),
                createInsurance(
                        "Luxury Home Coverage",
                        "High-end home insurance for luxury properties",
                        new BigDecimal("1000000"),
                        new BigDecimal("2500"),
                        Category.HOME,
                        30,
                        70,
                        RecommendedGender.ANY,
                        new BigDecimal("150000"),
                        null),

                // Life Insurance Policies
                createInsurance(
                        "Basic Life Coverage",
                        "Essential life insurance protection",
                        new BigDecimal("100000"),
                        new BigDecimal("50"),
                        Category.LIFE,
                        18,
                        65,
                        RecommendedGender.ANY,
                        new BigDecimal("20000"),
                        new BigDecimal("100000")),
                createInsurance(
                        "Family Life Protection",
                        "Comprehensive life insurance for families",
                        new BigDecimal("250000"),
                        new BigDecimal("120"),
                        Category.LIFE,
                        25,
                        55,
                        RecommendedGender.ANY,
                        new BigDecimal("40000"),
                        new BigDecimal("200000")),
                createInsurance(
                        "Senior Life Plan",
                        "Specialized life insurance for seniors",
                        new BigDecimal("75000"),
                        new BigDecimal("100"),
                        Category.LIFE,
                        50,
                        75,
                        RecommendedGender.ANY,
                        new BigDecimal("30000"),
                        new BigDecimal("150000")));

        insuranceRepository.saveAll(sampleInsurances);
    }

    private Insurance createInsurance(
            String name,
            String description,
            BigDecimal coverageAmount,
            BigDecimal premium,
            Category category,
            Integer ageMinimum,
            Integer ageMaximum,
            RecommendedGender recommendedGender,
            BigDecimal recommendedIncomeMin,
            BigDecimal recommendedIncomeMax) {

        Insurance insurance = new Insurance();
        insurance.setName(name);
        insurance.setDescription(description);
        insurance.setCoverageAmount(coverageAmount);
        insurance.setPremium(premium);
        insurance.setCategory(category);
        insurance.setAgeMinimum(ageMinimum);
        insurance.setAgeMaximum(ageMaximum);
        insurance.setRecommendedGender(recommendedGender);
        insurance.setRecommendedIncomeMin(recommendedIncomeMin);
        insurance.setRecommendedIncomeMax(recommendedIncomeMax);

        return insurance;
    }
}