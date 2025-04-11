package com.insurance.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler", "password" })
    private User user;

    @NotNull(message = "Insurance is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "insurance_id")
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    private Insurance insurance;

    @Column(name = "user_id", insertable = false, updatable = false)
    private UUID userId;

    @Column(name = "insurance_id", insertable = false, updatable = false)
    private UUID insuranceId;

    @NotNull(message = "Purchase date is required")
    private LocalDateTime purchaseDate;

    @NotBlank(message = "Policy number is required")
    @Column(unique = true)
    private String policyNumber;

    @NotNull(message = "Premium is required")
    @Positive(message = "Premium must be positive")
    private BigDecimal premium;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Status is required")
    private Status status;

    private String policyDocumentPath;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String customerName;
    private String insuranceType;
    private BigDecimal coverageAmount;
    private BigDecimal premiumAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (purchaseDate == null) {
            purchaseDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        ACTIVE, EXPIRED, CANCELLED
    }
}