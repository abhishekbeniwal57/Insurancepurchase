package com.insurance.api.service;

import com.insurance.api.model.Purchase;

/**
 * Service interface for generating policy documents.
 */
public interface PolicyDocumentService {

    /**
     * Generates a PDF policy document for the given purchase.
     *
     * @param purchase the purchase for which to generate the policy document
     * @return byte array containing the PDF document
     */
    byte[] generatePolicyDocument(Purchase purchase);
}