package com.dotnanny.repository;

import com.dotnanny.model.ProviderCompliance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProviderComplianceRepository extends MongoRepository<ProviderCompliance, String> {
    Optional<ProviderCompliance> findByProviderId(String providerId);
}
