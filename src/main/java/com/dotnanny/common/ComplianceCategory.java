package com.dotnanny.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ComplianceCategory {
    REGISTRATION, BACKGROUND, TRAINING, INSURANCE, HEALTH;

    @JsonValue
    public String json() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static ComplianceCategory from(String value) {
        return ComplianceCategory.valueOf(value.toUpperCase());
    }
}
