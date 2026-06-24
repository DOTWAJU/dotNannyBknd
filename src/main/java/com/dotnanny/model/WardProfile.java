package com.dotnanny.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A ward (child) care profile, owned by a guardian. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("wards")
public class WardProfile {
    @Id
    private String id;
    @Indexed
    private String guardianId;
    private String fullName;
    private String dateOfBirth; // ISO date
    private String ageGroup;

    @Builder.Default
    private List<String> allergies = new ArrayList<>();
    @Builder.Default
    private List<String> medicalConditions = new ArrayList<>();
    @Builder.Default
    private List<String> medications = new ArrayList<>();
    @Builder.Default
    private List<String> dietaryNeeds = new ArrayList<>();

    private String bloodGroup;
    private String doctorName;
    private String doctorPhone;

    @Builder.Default
    private List<EmergencyContact> emergencyContacts = new ArrayList<>();
    @Builder.Default
    private Map<String, ConsentRecord> consents = new HashMap<>();

    private String notes;
}
