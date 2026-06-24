package com.dotnanny.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IncidentType {
    INJURY, ILLNESS, ACCIDENT, SAFEGUARDING, BEHAVIOUR, OTHER;

    @JsonValue
    public String json() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static IncidentType from(String value) {
        return IncidentType.valueOf(value.toUpperCase());
    }
}
