package com.dotnanny.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A document a provider has uploaded against a compliance requirement. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDocument {
    private String itemKey;
    private String fileName;
    private String reference;
    private String issueDate;   // ISO date
    private String expiryDate;  // ISO date
    private boolean verified;
    private String uploadedAt;  // ISO timestamp
}
