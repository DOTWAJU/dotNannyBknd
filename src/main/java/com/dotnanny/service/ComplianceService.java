package com.dotnanny.service;

import com.dotnanny.catalog.ComplianceItem;
import com.dotnanny.catalog.JurisdictionCatalog;
import com.dotnanny.catalog.JurisdictionConfig;
import com.dotnanny.common.DocStatus;
import com.dotnanny.common.Jurisdiction;
import com.dotnanny.dto.ComplianceHealth;
import com.dotnanny.dto.ComplianceSummary;
import com.dotnanny.dto.ComplianceView;
import com.dotnanny.dto.DocumentRequest;
import com.dotnanny.dto.ItemStatusDto;
import com.dotnanny.model.ProviderCompliance;
import com.dotnanny.model.ProviderDocument;
import com.dotnanny.repository.ProviderComplianceRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ComplianceService {

    private static final int EXPIRING_WINDOW_DAYS = 60;
    private final ProviderComplianceRepository repo;

    public ComplianceService(ProviderComplianceRepository repo) {
        this.repo = repo;
    }

    public List<ProviderCompliance> all() {
        return repo.findAll();
    }

    /** Get (or lazily create) a provider's record. */
    public ProviderCompliance get(String providerId, String providerName, Jurisdiction fallback) {
        return repo.findByProviderId(providerId).orElseGet(() -> repo.save(ProviderCompliance.builder()
                .providerId(providerId)
                .providerName(providerName)
                .jurisdiction(fallback == null ? Jurisdiction.UK : fallback)
                .documents(new ArrayList<>())
                .build()));
    }

    public ComplianceView view(String providerId) {
        ProviderCompliance pc = get(providerId, null, Jurisdiction.UK);
        return new ComplianceView(pc.getProviderId(), pc.getProviderName(), pc.getJurisdiction(),
                pc.getDocuments(), summarize(pc.getJurisdiction(), pc.getDocuments()));
    }

    public ComplianceSummary summarize(Jurisdiction jurisdiction, List<ProviderDocument> documents) {
        JurisdictionConfig cfg = JurisdictionCatalog.get(jurisdiction);
        Map<String, ProviderDocument> byKey = new HashMap<>();
        for (ProviderDocument d : documents) {
            byKey.putIfAbsent(d.getItemKey(), d);
        }
        LocalDate now = LocalDate.now();
        List<ItemStatusDto> items = new ArrayList<>();
        for (ComplianceItem item : cfg.items()) {
            ProviderDocument doc = byKey.get(item.key());
            items.add(new ItemStatusDto(item, doc, getItemStatus(item, doc, now)));
        }
        int requiredTotal = items.size();
        int verifiedCount = (int) items.stream()
                .filter(i -> i.status() == DocStatus.VERIFIED || i.status() == DocStatus.EXPIRING).count();
        int submitted = (int) items.stream().filter(i -> i.status() != DocStatus.MISSING).count();
        int percent = requiredTotal == 0 ? 0 : Math.round(verifiedCount * 100f / requiredTotal);
        boolean approvable = verifiedCount == requiredTotal && requiredTotal > 0;
        return new ComplianceSummary(items, requiredTotal, verifiedCount, submitted, percent, approvable);
    }

    public DocStatus getItemStatus(ComplianceItem item, ProviderDocument doc, LocalDate now) {
        if (doc == null) {
            return DocStatus.MISSING;
        }
        if (item.requiresExpiry()) {
            Long days = daysUntil(doc.getExpiryDate(), now);
            if (days != null) {
                if (days < 0) return DocStatus.EXPIRED;
                if (!doc.isVerified()) return DocStatus.PENDING;
                if (days <= EXPIRING_WINDOW_DAYS) return DocStatus.EXPIRING;
                return DocStatus.VERIFIED;
            }
        }
        return doc.isVerified() ? DocStatus.VERIFIED : DocStatus.PENDING;
    }

    private Long daysUntil(String iso, LocalDate now) {
        if (iso == null || iso.isBlank()) {
            return null;
        }
        try {
            LocalDate d = LocalDate.parse(iso.substring(0, Math.min(10, iso.length())));
            return ChronoUnit.DAYS.between(now, d);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isApprovable(String providerId) {
        return repo.findByProviderId(providerId)
                .map(pc -> summarize(pc.getJurisdiction(), pc.getDocuments()).isApprovable())
                .orElse(false);
    }

    public ProviderCompliance setJurisdiction(String providerId, Jurisdiction jurisdiction) {
        ProviderCompliance pc = get(providerId, null, jurisdiction);
        pc.setJurisdiction(jurisdiction);
        return repo.save(pc);
    }

    public ProviderCompliance upsertDocument(String providerId, DocumentRequest req) {
        ProviderCompliance pc = get(providerId, null, Jurisdiction.UK);
        ProviderDocument doc = ProviderDocument.builder()
                .itemKey(req.itemKey())
                .fileName(req.fileName())
                .reference(req.reference())
                .issueDate(req.issueDate())
                .expiryDate(req.expiryDate())
                .verified(false)
                .uploadedAt(Instant.now().toString())
                .build();
        pc.getDocuments().removeIf(d -> d.getItemKey().equals(req.itemKey()));
        pc.getDocuments().add(doc);
        return repo.save(pc);
    }

    public ProviderCompliance removeDocument(String providerId, String itemKey) {
        ProviderCompliance pc = get(providerId, null, Jurisdiction.UK);
        pc.getDocuments().removeIf(d -> d.getItemKey().equals(itemKey));
        return repo.save(pc);
    }

    public ProviderCompliance setVerified(String providerId, String itemKey, boolean verified) {
        ProviderCompliance pc = get(providerId, null, Jurisdiction.UK);
        pc.getDocuments().stream()
                .filter(d -> d.getItemKey().equals(itemKey))
                .findFirst()
                .ifPresent(d -> d.setVerified(verified));
        return repo.save(pc);
    }

    public ComplianceHealth health() {
        List<ProviderCompliance> providers = repo.findAll();
        int ready = 0, expiring = 0, expired = 0, missing = 0, pending = 0;
        for (ProviderCompliance p : providers) {
            ComplianceSummary s = summarize(p.getJurisdiction(), p.getDocuments());
            if (s.isApprovable()) {
                ready++;
            }
            for (ItemStatusDto it : s.items()) {
                switch (it.status()) {
                    case EXPIRING -> expiring++;
                    case EXPIRED -> expired++;
                    case MISSING -> missing++;
                    case PENDING -> pending++;
                    default -> { }
                }
            }
        }
        return new ComplianceHealth(providers.size(), ready, expiring, expired, missing, pending,
                expiring + expired + missing + pending);
    }
}
