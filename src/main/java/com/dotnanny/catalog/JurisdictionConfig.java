package com.dotnanny.catalog;

import com.dotnanny.common.Jurisdiction;

import java.util.List;

/** The full rule set for a jurisdiction (mirrors the frontend jurisdictions config). */
public record JurisdictionConfig(
        Jurisdiction id,
        String country,
        String region,
        String flag,
        String providerTerm,
        String safeguardingAuthority,
        String currencyCode,
        String currencySymbol,
        List<RatioRule> ratios,
        int maxChildrenPerAdult,
        List<RatioLimit> ageBandLimits,
        List<ComplianceItem> items
) {}
