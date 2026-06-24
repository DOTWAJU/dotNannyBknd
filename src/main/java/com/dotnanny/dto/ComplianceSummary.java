package com.dotnanny.dto;

import java.util.List;

public record ComplianceSummary(
        List<ItemStatusDto> items,
        int requiredTotal,
        int verifiedCount,
        int submittedCount,
        int percent,
        boolean isApprovable
) {}
