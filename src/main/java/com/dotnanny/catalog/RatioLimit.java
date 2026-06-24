package com.dotnanny.catalog;

/** A computable age-band limit, e.g. "no more than 1 child under 1 year". */
public record RatioLimit(String label, double maxAgeYears, int max) {}
