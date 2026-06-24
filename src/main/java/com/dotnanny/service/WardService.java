package com.dotnanny.service;

import com.dotnanny.common.NotFoundException;
import com.dotnanny.model.WardProfile;
import com.dotnanny.repository.WardProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardService {

    private final WardProfileRepository repo;

    public WardService(WardProfileRepository repo) {
        this.repo = repo;
    }

    public List<WardProfile> all() {
        return repo.findAll();
    }

    public List<WardProfile> byGuardian(String guardianId) {
        return repo.findByGuardianId(guardianId);
    }

    public WardProfile byName(String name) {
        return repo.findFirstByFullNameIgnoreCase(name).orElse(null);
    }

    public WardProfile get(String id) {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Ward not found: " + id));
    }

    public WardProfile save(WardProfile ward) {
        return repo.save(ward);
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
