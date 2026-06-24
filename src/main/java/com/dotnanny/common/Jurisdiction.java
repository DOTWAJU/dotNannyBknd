package com.dotnanny.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/** Supported childcare jurisdictions. Serialized to the codes the frontend uses. */
public enum Jurisdiction {
    UK("UK"),
    CA_ON("CA-ON"),
    US_CA("US-CA");

    private final String code;

    Jurisdiction(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }

    @JsonCreator
    public static Jurisdiction fromCode(String value) {
        if (value == null) {
            return UK;
        }
        for (Jurisdiction j : values()) {
            if (j.code.equalsIgnoreCase(value) || j.name().equalsIgnoreCase(value)) {
                return j;
            }
        }
        return UK;
    }
}
