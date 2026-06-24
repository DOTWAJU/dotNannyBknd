package com.dotnanny.dto;

import com.dotnanny.common.IncidentStatus;

public record StatusUpdateRequest(IncidentStatus status, String reviewedBy) {}
