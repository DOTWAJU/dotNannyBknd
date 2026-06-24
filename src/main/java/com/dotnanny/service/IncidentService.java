package com.dotnanny.service;

import com.dotnanny.catalog.JurisdictionCatalog;
import com.dotnanny.common.IncidentStatus;
import com.dotnanny.common.Jurisdiction;
import com.dotnanny.common.NotFoundException;
import com.dotnanny.dto.IncidentCreateRequest;
import com.dotnanny.model.Incident;
import com.dotnanny.repository.IncidentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Year;
import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository repo;

    public IncidentService(IncidentRepository repo) {
        this.repo = repo;
    }

    public List<Incident> all() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public List<Incident> byNanny(String nannyId) {
        return repo.findByNannyIdOrderByCreatedAtDesc(nannyId);
    }

    public Incident create(IncidentCreateRequest r) {
        Instant now = Instant.now();
        Incident inc = Incident.builder()
                .reference(nextReference())
                .nannyId(r.nannyId())
                .nannyName(r.nannyName())
                .jurisdiction(r.jurisdiction() == null ? Jurisdiction.UK : r.jurisdiction())
                .wardName(r.wardName())
                .type(r.type())
                .severity(r.severity())
                .occurredAt(r.occurredAt() == null || r.occurredAt().isBlank() ? now.toString() : r.occurredAt())
                .location(r.location())
                .description(r.description())
                .actionTaken(r.actionTaken())
                .witnesses(r.witnesses())
                .guardianNotified(r.guardianNotified())
                .status(IncidentStatus.SUBMITTED)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return repo.save(inc);
    }

    private String nextReference() {
        long n = repo.count() + 1;
        return "INC-" + Year.now() + "-" + String.format("%04d", n);
    }

    public Incident setStatus(String id, IncidentStatus status, String reviewedBy) {
        Incident inc = get(id);
        inc.setStatus(status);
        if (reviewedBy != null) {
            inc.setReviewedBy(reviewedBy);
        }
        inc.setUpdatedAt(Instant.now());
        return repo.save(inc);
    }

    public Incident escalate(String id, String note, String reviewedBy) {
        Incident inc = get(id);
        String authority = JurisdictionCatalog.get(inc.getJurisdiction()).safeguardingAuthority();
        inc.setStatus(IncidentStatus.ESCALATED);
        inc.setEscalatedTo(authority);
        inc.setEscalationNote(note == null || note.isBlank() ? "Referred per safeguarding policy." : note);
        if (reviewedBy != null) {
            inc.setReviewedBy(reviewedBy);
        }
        inc.setUpdatedAt(Instant.now());
        return repo.save(inc);
    }

    private Incident get(String id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Incident not found: " + id));
    }
}
