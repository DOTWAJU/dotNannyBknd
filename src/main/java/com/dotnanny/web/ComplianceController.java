package com.dotnanny.web;

import com.dotnanny.dto.ComplianceHealth;
import com.dotnanny.dto.ComplianceView;
import com.dotnanny.dto.DocumentRequest;
import com.dotnanny.dto.JurisdictionUpdate;
import com.dotnanny.model.ProviderCompliance;
import com.dotnanny.service.ComplianceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compliance")
public class ComplianceController {

    private final ComplianceService compliance;

    public ComplianceController(ComplianceService compliance) {
        this.compliance = compliance;
    }

    /** All providers with their computed summaries (admin review). */
    @GetMapping("/providers")
    public List<ComplianceView> providers() {
        return compliance.all().stream().map(this::toView).toList();
    }

    /** Org-wide compliance health rollup. */
    @GetMapping("/health")
    public ComplianceHealth health() {
        return compliance.health();
    }

    @GetMapping("/{providerId}")
    public ComplianceView get(@PathVariable String providerId) {
        return compliance.view(providerId);
    }

    @GetMapping("/{providerId}/approvable")
    public Map<String, Boolean> approvable(@PathVariable String providerId) {
        return Map.of("approvable", compliance.isApprovable(providerId));
    }

    @PutMapping("/{providerId}/jurisdiction")
    public ComplianceView setJurisdiction(@PathVariable String providerId, @RequestBody JurisdictionUpdate body) {
        return toView(compliance.setJurisdiction(providerId, body.jurisdiction()));
    }

    @PostMapping("/{providerId}/documents")
    public ComplianceView upsertDocument(@PathVariable String providerId, @Valid @RequestBody DocumentRequest req) {
        return toView(compliance.upsertDocument(providerId, req));
    }

    @DeleteMapping("/{providerId}/documents/{itemKey}")
    public ComplianceView removeDocument(@PathVariable String providerId, @PathVariable String itemKey) {
        return toView(compliance.removeDocument(providerId, itemKey));
    }

    @PostMapping("/{providerId}/documents/{itemKey}/verify")
    public ComplianceView verify(@PathVariable String providerId, @PathVariable String itemKey,
                                 @RequestParam(defaultValue = "true") boolean verified) {
        return toView(compliance.setVerified(providerId, itemKey, verified));
    }

    private ComplianceView toView(ProviderCompliance pc) {
        return new ComplianceView(pc.getProviderId(), pc.getProviderName(), pc.getJurisdiction(),
                pc.getDocuments(), compliance.summarize(pc.getJurisdiction(), pc.getDocuments()));
    }
}
