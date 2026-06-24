package com.dotnanny.repository;

import com.dotnanny.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByGuardianId(String guardianId);
    List<Booking> findByNannyId(String nannyId);
}
