package com.dotnanny.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IncidentStatus {
    SUBMITTED, UNDER_REVIEW, ESCALATED, CLOSED;

    @JsonValue
    public String json() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static IncidentStatus from(String value) {
        return IncidentStatus.valueOf(value.toUpperCase());
    }
}
