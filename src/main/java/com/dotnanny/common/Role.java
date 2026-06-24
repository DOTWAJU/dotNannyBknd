package com.dotnanny.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    GUARDIAN, NANNY, ADMIN;

    @JsonValue
    public String json() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static Role from(String value) {
        // The frontend uses "ward" as the guardian/client account id.
        if (value != null && value.equalsIgnoreCase("ward")) {
            return GUARDIAN;
        }
        return value == null ? GUARDIAN : Role.valueOf(value.toUpperCase());
    }
}
