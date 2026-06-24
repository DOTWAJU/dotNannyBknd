package com.dotnanny.dto;

import com.dotnanny.common.IncidentSeverity;
import com.dotnanny.common.IncidentType;
import com.dotnanny.common.Jurisdiction;
import jakarta.validation.constraints.NotBlank;

public record IncidentCreateRequest(
        @NotBlank String nannyId,
        String nannyName,
        Jurisdiction jurisdiction,
        @NotBlank String wardName,
        IncidentType type,
        IncidentSeverity severity,
        String occurredAt,
        String location,
        @NotBlank String description,
        String actionTaken,
        String witnesses,
        boolean guardianNotified
) {}
