package com.dotnanny.dto;

import java.util.List;

public record RatioResult(
        int total,
        int cap,
        boolean totalBreach,
        List<BandResult> bands,
        boolean anyBreach
) {}
