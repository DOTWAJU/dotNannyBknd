package com.dotnanny.dto;

import jakarta.validation.constraints.NotBlank;

/** Upload/update a compliance document. */
public record DocumentRequest(
        @NotBlank String itemKey,
        @NotBlank String fileName,
        String reference,
        String issueDate,
        String expiryDate
) {}
