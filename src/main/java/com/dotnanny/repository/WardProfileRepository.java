package com.dotnanny.repository;

import com.dotnanny.model.WardProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface WardProfileRepository extends MongoRepository<WardProfile, String> {
    List<WardProfile> findByGuardianId(String guardianId);
    Optional<WardProfile> findFirstByFullNameIgnoreCase(String fullName);
}
