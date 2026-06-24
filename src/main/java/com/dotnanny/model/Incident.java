package com.dotnanny.model;

import com.dotnanny.common.IncidentSeverity;
import com.dotnanny.common.IncidentStatus;
import com.dotnanny.common.IncidentType;
import com.dotnanny.common.Jurisdiction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/** An incident / safeguarding report filed by a nanny. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("incidents")
public class Incident {
    @Id
    private String id;
    private String reference;
    @Indexed
    private String nannyId;
    private String nannyName;
    @Builder.Default
    private Jurisdiction jurisdiction = Jurisdiction.UK;
    private String wardName;
    private IncidentType type;
    private IncidentSeverity severity;
    private String occurredAt; // ISO datetime
    private String location;
    private String description;
    private String actionTaken;
    private String witnesses;
    private boolean guardianNotified;
    private IncidentStatus status;
    private String escalatedTo;
    private String escalationNote;
    private String reviewedBy;
    private Instant createdAt;
    private Instant updatedAt;
}
