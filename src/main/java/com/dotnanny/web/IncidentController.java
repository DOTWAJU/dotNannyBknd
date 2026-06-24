package com.dotnanny.web;

import com.dotnanny.dto.EscalateRequest;
import com.dotnanny.dto.IncidentCreateRequest;
import com.dotnanny.dto.StatusUpdateRequest;
import com.dotnanny.model.Incident;
import com.dotnanny.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService incidents;

    public IncidentController(IncidentService incidents) {
        this.incidents = incidents;
    }

    @GetMapping
    public List<Incident> list(@RequestParam(required = false) String nannyId) {
        return nannyId == null ? incidents.all() : incidents.byNanny(nannyId);
    }

    @PostMapping
    public ResponseEntity<Incident> create(@Valid @RequestBody IncidentCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidents.create(req));
    }

    @PutMapping("/{id}/status")
    public Incident setStatus(@PathVariable String id, @RequestBody StatusUpdateRequest req) {
        return incidents.setStatus(id, req.status(), req.reviewedBy());
    }

    @PutMapping("/{id}/escalate")
    public Incident escalate(@PathVariable String id, @RequestBody EscalateRequest req) {
        return incidents.escalate(id, req.note(), req.reviewedBy());
    }
}
