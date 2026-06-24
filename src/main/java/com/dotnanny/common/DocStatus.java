package com.dotnanny.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Computed status of a single compliance requirement. */
public enum DocStatus {
    MISSING, PENDING, EXPIRED, EXPIRING, VERIFIED;

    @JsonValue
    public String json() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static DocStatus from(String value) {
        return DocStatus.valueOf(value.toUpperCase());
    }
}
