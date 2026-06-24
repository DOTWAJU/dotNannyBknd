package com.dotnanny.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IncidentSeverity {
    LOW, MEDIUM, HIGH;

    @JsonValue
    public String json() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static IncidentSeverity from(String value) {
        return IncidentSeverity.valueOf(value.toUpperCase());
    }
}
