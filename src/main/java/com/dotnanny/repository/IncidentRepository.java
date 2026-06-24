package com.dotnanny.repository;

import com.dotnanny.model.Incident;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IncidentRepository extends MongoRepository<Incident, String> {
    List<Incident> findAllByOrderByCreatedAtDesc();
    List<Incident> findByNannyIdOrderByCreatedAtDesc(String nannyId);
}
