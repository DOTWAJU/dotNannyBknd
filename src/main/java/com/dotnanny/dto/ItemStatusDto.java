package com.dotnanny.dto;

import com.dotnanny.catalog.ComplianceItem;
import com.dotnanny.common.DocStatus;
import com.dotnanny.model.ProviderDocument;

public record ItemStatusDto(
        ComplianceItem item,
        ProviderDocument document,
        DocStatus status
) {}
