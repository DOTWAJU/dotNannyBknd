package com.dotnanny.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BookingStatus {
    PENDING, CONFIRMED, COMPLETED, CANCELLED;

    @JsonValue
    public String json() {
        return name().toLowerCase();
    }

    @JsonCreator
    public static BookingStatus from(String value) {
        return BookingStatus.valueOf(value.toUpperCase());
    }
}
