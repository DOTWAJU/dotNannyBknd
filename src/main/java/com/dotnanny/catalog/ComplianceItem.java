package com.dotnanny.catalog;

import com.dotnanny.common.ComplianceCategory;

/** A single compliance requirement within a jurisdiction. */
public record ComplianceItem(
        String key,
        String label,
        String authority,
        String description,
        ComplianceCategory category,
        boolean requiresExpiry,
        Integer renewMonths
) {}
