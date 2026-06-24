package com.dotnanny.service;

import com.dotnanny.common.BookingStatus;
import com.dotnanny.model.Booking;
import com.dotnanny.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository repo;

    public BookingService(BookingRepository repo) {
        this.repo = repo;
    }

    public List<Booking> all() {
        return repo.findAll();
    }

    public List<Booking> byGuardian(String guardianId) {
        return repo.findByGuardianId(guardianId);
    }

    public List<Booking> byNanny(String nannyId) {
        return repo.findByNannyId(nannyId);
    }

    public Booking create(Booking booking) {
        booking.setId(null);
        booking.setCreatedAt(Instant.now());
        if (booking.getStatus() == null) {
            booking.setStatus(BookingStatus.PENDING);
        }
        return repo.save(booking);
    }
}
