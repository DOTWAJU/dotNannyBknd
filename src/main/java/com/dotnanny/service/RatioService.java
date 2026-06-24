package com.dotnanny.service;

import com.dotnanny.catalog.JurisdictionCatalog;
import com.dotnanny.catalog.JurisdictionConfig;
import com.dotnanny.catalog.RatioLimit;
import com.dotnanny.common.Jurisdiction;
import com.dotnanny.dto.BandResult;
import com.dotnanny.dto.ChildInCare;
import com.dotnanny.dto.RatioResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatioService {

    /** Evaluate a set of children-in-care against a jurisdiction's ratio limits. */
    public RatioResult evaluate(Jurisdiction jurisdiction, List<ChildInCare> children) {
        JurisdictionConfig cfg = JurisdictionCatalog.get(jurisdiction);
        List<ChildInCare> kids = children == null ? List.of() : children;
        int total = kids.size();
        int cap = cfg.maxChildrenPerAdult();
        boolean totalBreach = total > cap;
        boolean anyBreach = totalBreach;

        List<BandResult> bands = new ArrayList<>();
        for (RatioLimit lim : cfg.ageBandLimits()) {
            int count = (int) kids.stream()
                    .filter(c -> c.ageYears() != null && c.ageYears() < lim.maxAgeYears())
                    .count();
            boolean breach = count > lim.max();
            if (breach) {
                anyBreach = true;
            }
            bands.add(new BandResult(lim.label(), count, lim.max(), breach));
        }
        return new RatioResult(total, cap, totalBreach, bands, anyBreach);
    }

    /** Age in fractional years from an ISO date of birth (null if unknown). */
    public static Double ageInYears(String dobIso) {
        if (dobIso == null || dobIso.isBlank()) {
            return null;
        }
        try {
            LocalDate d = LocalDate.parse(dobIso.substring(0, Math.min(10, dobIso.length())));
            return ChronoUnit.DAYS.between(d, LocalDate.now()) / 365.25;
        } catch (Exception e) {
            return null;
        }
    }
}
