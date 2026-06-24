package com.dotnanny.web;

import com.dotnanny.model.Booking;
import com.dotnanny.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookings;

    public BookingController(BookingService bookings) {
        this.bookings = bookings;
    }

    @GetMapping
    public List<Booking> list(@RequestParam(required = false) String guardianId,
                              @RequestParam(required = false) String nannyId) {
        if (guardianId != null) {
            return bookings.byGuardian(guardianId);
        }
        if (nannyId != null) {
            return bookings.byNanny(nannyId);
        }
        return bookings.all();
    }

    @PostMapping
    public ResponseEntity<Booking> create(@Valid @RequestBody Booking booking) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookings.create(booking));
    }
}
