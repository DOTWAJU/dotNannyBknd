package com.dotnanny.dto;

public record ComplianceHealth(
        int total,
        int ready,
        int expiring,
        int expired,
        int missing,
        int pending,
        int attention
) {}
