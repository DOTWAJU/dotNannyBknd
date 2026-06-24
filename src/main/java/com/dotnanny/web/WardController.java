package com.dotnanny.web;

import com.dotnanny.common.NotFoundException;
import com.dotnanny.model.WardProfile;
import com.dotnanny.service.WardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wards")
public class WardController {

    private final WardService wards;

    public WardController(WardService wards) {
        this.wards = wards;
    }

    @GetMapping
    public List<WardProfile> list(@RequestParam(required = false) String guardianId) {
        return guardianId == null ? wards.all() : wards.byGuardian(guardianId);
    }

    @GetMapping("/{id}")
    public WardProfile get(@PathVariable String id) {
        return wards.get(id);
    }

    @GetMapping("/by-name/{name}")
    public WardProfile byName(@PathVariable String name) {
        WardProfile w = wards.byName(name);
        if (w == null) {
            throw new NotFoundException("Ward not found: " + name);
        }
        return w;
    }

    /** Create or update (upsert) a ward care profile. */
    @PostMapping
    public WardProfile upsert(@Valid @RequestBody WardProfile ward) {
        return wards.save(ward);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        wards.delete(id);
        return ResponseEntity.noContent().build();
    }
}
