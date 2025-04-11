package com.insurance.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Coverage amount is required")
    @Positive(message = "Coverage amount must be positive")
    private BigDecimal coverageAmount;

    @NotNull(message = "Premium is required")
    @Positive(message = "Premium must be positive")
    private BigDecimal premium;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category is required")
    private Category category;

    private Integer ageMinimum;

    private Integer ageMaximum;

    @Enumerated(EnumType.STRING)
    private RecommendedGender recommendedGender;

    private BigDecimal recommendedIncomeMin;

    private BigDecimal recommendedIncomeMax;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Category {
        HEALTH, AUTO, HOME, LIFE
    }

    public enum RecommendedGender {
        MALE, FEMALE, ANY
    }
}