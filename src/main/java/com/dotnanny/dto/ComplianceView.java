package com.dotnanny.dto;

import com.dotnanny.common.Jurisdiction;
import com.dotnanny.model.ProviderDocument;

import java.util.List;

/** A provider's compliance record plus its computed summary. */
public record ComplianceView(
        String providerId,
        String providerName,
        Jurisdiction jurisdiction,
        List<ProviderDocument> documents,
        ComplianceSummary summary
) {}
