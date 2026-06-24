package com.dotnanny.model;

import com.dotnanny.common.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/** A care booking made by a guardian for a nanny. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("bookings")
public class Booking {
    @Id
    private String id;
    @Indexed
    private String guardianId;
    @Indexed
    private String nannyId;
    private String nannyName;
    private String wardName;
    private String date;     // ISO date
    private String time;     // e.g. "2:00 PM - 4:00 PM"
    private int durationHours;
    private double costPerSession;
    private double totalCost;
    private String mode;     // In-home / Live-out / etc.
    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;
    private Instant createdAt;
}
