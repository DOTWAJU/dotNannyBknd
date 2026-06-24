package com.dotnanny;

import com.dotnanny.common.DocStatus;
import com.dotnanny.common.Jurisdiction;
import com.dotnanny.dto.ChildInCare;
import com.dotnanny.dto.ComplianceSummary;
import com.dotnanny.dto.RatioResult;
import com.dotnanny.model.ProviderDocument;
import com.dotnanny.service.ComplianceService;
import com.dotnanny.service.RatioService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pure unit tests for the ported compliance + ratio logic (no MongoDB needed). */
class DomainLogicTest {

    private final RatioService ratio = new RatioService();
    // summarize()/getItemStatus() do not touch the repository.
    private final ComplianceService compliance = new ComplianceService(null);

    @Test
    void twoInfantsBreachUkUnderOnesLimit() {
        List<ChildInCare> kids = List.of(
                new ChildInCare("John", 4.5),
                new ChildInCare("Michael", 0.6),
                new ChildInCare("Amani", 0.5)
        );
        RatioResult r = ratio.evaluate(Jurisdiction.UK, kids);
        assertTrue(r.anyBreach(), "Two under-1s should breach the UK limit of 1");
        assertTrue(r.bands().stream().anyMatch(b -> b.label().equals("under 1") && b.breach()));
        assertFalse(r.totalBreach(), "3 children is within the total cap of 6");
    }

    @Test
    void sameChildrenAreWithinRatioInCalifornia() {
        List<ChildInCare> kids = List.of(
                new ChildInCare("John", 4.5),
                new ChildInCare("Michael", 0.6),
                new ChildInCare("Amani", 0.5)
        );
        RatioResult r = ratio.evaluate(Jurisdiction.US_CA, kids);
        assertFalse(r.anyBreach(), "California allows 2 under-2s, so this is within ratio");
    }

    @Test
    void missingDocumentIsNotApprovable() {
        ComplianceSummary s = compliance.summarize(Jurisdiction.UK, List.of());
        assertEquals(0, s.verifiedCount());
        assertFalse(s.isApprovable());
        assertTrue(s.requiredTotal() > 0);
    }

    @Test
    void verifiedInDateDocumentsAreApprovable() {
        List<ProviderDocument> docs = List.of(
                doc("ofsted", true, null),
                doc("dbs", true, "2030-01-01"),
                doc("paed_first_aid", true, "2030-01-01"),
                doc("eyfs", true, "2030-01-01"),
                doc("public_liability", true, "2030-01-01")
        );
        ComplianceSummary s = compliance.summarize(Jurisdiction.UK, docs);
        assertTrue(s.isApprovable());
        assertEquals(100, s.percent());
    }

    @Test
    void unverifiedDocumentIsPending() {
        DocStatus status = compliance.getItemStatus(
                com.dotnanny.catalog.JurisdictionCatalog.get(Jurisdiction.UK).items().get(1), // dbs (requires expiry)
                doc("dbs", false, "2030-01-01"),
                java.time.LocalDate.now());
        assertEquals(DocStatus.PENDING, status);
    }

    private ProviderDocument doc(String key, boolean verified, String expiry) {
        return ProviderDocument.builder().itemKey(key).fileName(key + ".pdf").verified(verified)
                .expiryDate(expiry).uploadedAt("2026-05-01T09:00:00.000Z").build();
    }
}
