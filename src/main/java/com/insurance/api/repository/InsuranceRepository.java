package com.insurance.api.repository;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Insurance.Category;
import com.insurance.api.model.Insurance.RecommendedGender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface InsuranceRepository extends JpaRepository<Insurance, UUID> {

        List<Insurance> findByCategory(Category category);

        @Query("SELECT i FROM Insurance i WHERE " +
                        "(:age IS NULL OR (i.ageMinimum IS NULL OR :age >= i.ageMinimum)) AND " +
                        "(:age IS NULL OR (i.ageMaximum IS NULL OR :age <= i.ageMaximum)) AND " +
                        "(:gender IS NULL OR i.recommendedGender = :gender OR i.recommendedGender = 'ANY') AND " +
                        "(:income IS NULL OR (i.recommendedIncomeMin IS NULL OR :income >= i.recommendedIncomeMin)) AND "
                        +
                        "(:income IS NULL OR (i.recommendedIncomeMax IS NULL OR :income <= i.recommendedIncomeMax))")
        List<Insurance> findRecommendedInsurances(
                        @Param("age") Integer age,
                        @Param("gender") RecommendedGender gender,
                        @Param("income") BigDecimal income);
}