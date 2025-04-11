package com.insurance.api.service;

import com.insurance.api.model.Insurance;
import com.insurance.api.model.Purchase;
import com.insurance.api.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PurchaseService {

    Purchase purchaseInsurance(User user, Insurance insurance);

    Optional<Purchase> getPurchaseByPolicyNumber(String policyNumber);

    Optional<Purchase> getPurchaseById(UUID id);

    List<Purchase> getPurchasesByUserId(UUID userId);

    byte[] generatePolicyDocument(Purchase purchase) throws Exception;
}