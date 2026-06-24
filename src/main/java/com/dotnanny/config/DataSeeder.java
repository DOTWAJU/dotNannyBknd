package com.dotnanny.config;

import com.dotnanny.catalog.ConsentCatalog;
import com.dotnanny.catalog.ConsentDefinition;
import com.dotnanny.common.IncidentSeverity;
import com.dotnanny.common.IncidentStatus;
import com.dotnanny.common.IncidentType;
import com.dotnanny.common.Jurisdiction;
import com.dotnanny.common.Role;
import com.dotnanny.model.ConsentRecord;
import com.dotnanny.model.EmergencyContact;
import com.dotnanny.model.Incident;
import com.dotnanny.model.ProviderCompliance;
import com.dotnanny.model.ProviderDocument;
import com.dotnanny.model.User;
import com.dotnanny.model.WardProfile;
import com.dotnanny.repository.IncidentRepository;
import com.dotnanny.repository.ProviderComplianceRepository;
import com.dotnanny.repository.UserRepository;
import com.dotnanny.repository.WardProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Seeds demo data on first run (mirrors the frontend localStorage seeds). */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final String DEMO_NANNY_ID = "nanny@dot-nanny.com";
    private static final String DEMO_GUARDIAN_ID = "ward@dot-nanny.com";

    private final UserRepository users;
    private final WardProfileRepository wards;
    private final ProviderComplianceRepository compliance;
    private final IncidentRepository incidents;

    public DataSeeder(UserRepository users, WardProfileRepository wards,
                      ProviderComplianceRepository compliance, IncidentRepository incidents) {
        this.users = users;
        this.wards = wards;
        this.compliance = compliance;
        this.incidents = incidents;
    }

    @Override
    public void run(String... args) {
        try {
            seedUsers();
            seedWards();
            seedCompliance();
            seedIncidents();
            log.info("Demo data seeding complete.");
        } catch (Exception e) {
            log.warn("Skipping demo data seeding (is MongoDB running?): {}", e.getMessage());
        }
    }

    private void seedUsers() {
        if (users.count() > 0) {
            return;
        }
        users.save(user("Sarah Johnson", DEMO_GUARDIAN_ID, "ward123", Role.GUARDIAN));
        users.save(user("Sarah Johnson", DEMO_NANNY_ID, "nanny123", Role.NANNY));
        users.save(user("Admin User", "admin@dot-nanny.com", "admin123", Role.ADMIN));
    }

    private User user(String name, String email, String password, Role role) {
        return User.builder().fullName(name).email(email).password(password).role(role)
                .createdAt(Instant.now()).build();
    }

    private void seedWards() {
        if (wards.count() > 0) {
            return;
        }
        wards.save(ward("ward-ava", DEMO_GUARDIAN_ID, "Ava Thompson", "2023-04-12", "Toddler (1–3 yrs)",
                List.of("Peanuts", "Eggs"), List.of("Mild eczema"), List.of("Antihistamine (as needed)"),
                List.of("No nuts", "Dairy in moderation"), "O+", "Dr. Sarah Bennett", "+44 20 7946 0011",
                List.of(ec("James Thompson", "Father", "+44 7700 900111"), ec("Grandma Rose", "Grandmother", "+44 7700 900222")),
                List.of("photos", "outings", "sunscreen", "emergency_treatment"), "Emily Thompson",
                "Carries an EpiPen — kept in the front pocket of her bag."));

        wards.save(ward("ward-liam", DEMO_GUARDIAN_ID, "Liam Thompson", "2020-09-30", "Preschool (3–5 yrs)",
                List.of(), List.of("Asthma (mild)"), List.of("Blue inhaler (as needed)"), List.of("Vegetarian"),
                "A+", "Dr. Sarah Bennett", "+44 20 7946 0011",
                List.of(ec("James Thompson", "Father", "+44 7700 900111")),
                List.of("photos", "outings", "sunscreen", "transport", "water_play", "emergency_treatment"),
                "Emily Thompson", "Inhaler must travel with him to every session."));

        wards.save(ward("ward-john", "nanny-demo", "John Doe", "2022-01-08", "Toddler (1–3 yrs)",
                List.of("Penicillin"), List.of(), List.of(), List.of("Halal"), "B+", "Dr. Aisha Khan", "+44 161 496 0123",
                List.of(ec("Mary Doe", "Mother", "+44 7700 900333")),
                List.of("photos", "outings", "sunscreen", "emergency_treatment"), "Mary Doe", null));

        wards.save(ward("ward-jane", "nanny-demo", "Jane Smith", "2018-06-21", "School Age (5–12 yrs)",
                List.of("Bee stings"), List.of("Wears glasses"), List.of(), List.of("Lactose-free"), "O-",
                "Dr. Paul Greene", "+44 113 496 0456",
                List.of(ec("Robert Smith", "Father", "+44 7700 900444"), ec("Linda Smith", "Mother", "+44 7700 900555")),
                List.of("photos", "outings", "transport", "emergency_treatment"), "Linda Smith",
                "Allergic to bee stings — antihistamine in her schoolbag."));

        wards.save(ward("ward-michael", "nanny-demo", "Michael Brown", "2025-11-02", "Newborn (0–12 mo)",
                List.of(), List.of(), List.of("Vitamin D drops (daily)"), List.of("Formula every 3 hours"), "AB+",
                "Dr. Helen Carter", "+44 20 7946 0789",
                List.of(ec("Susan Brown", "Mother", "+44 7700 900666")),
                List.of("photos", "sunscreen", "emergency_treatment"), "Susan Brown",
                "Naps at 9am and 1pm. Prefers white-noise to settle."));

        wards.save(ward("ward-amani", "nanny-demo", "Amani Bello", "2025-12-15", "Newborn (0–12 mo)",
                List.of(), List.of(), List.of(), List.of("Formula every 3 hours"), "O+",
                "Dr. Helen Carter", "+44 20 7946 0789",
                List.of(ec("Tunde Bello", "Father", "+44 7700 900777")),
                List.of("photos", "sunscreen", "emergency_treatment"), "Tunde Bello",
                "Twin sibling cared for alongside Michael some afternoons."));
    }

    private WardProfile ward(String id, String guardianId, String name, String dob, String ageGroup,
                             List<String> allergies, List<String> conditions, List<String> meds, List<String> diet,
                             String blood, String doctor, String doctorPhone, List<EmergencyContact> contacts,
                             List<String> granted, String signedBy, String notes) {
        return WardProfile.builder()
                .id(id).guardianId(guardianId).fullName(name).dateOfBirth(dob).ageGroup(ageGroup)
                .allergies(new ArrayList<>(allergies)).medicalConditions(new ArrayList<>(conditions))
                .medications(new ArrayList<>(meds)).dietaryNeeds(new ArrayList<>(diet))
                .bloodGroup(blood).doctorName(doctor).doctorPhone(doctorPhone)
                .emergencyContacts(new ArrayList<>(contacts)).consents(consentsFor(granted, signedBy))
                .notes(notes).build();
    }

    private EmergencyContact ec(String name, String relationship, String phone) {
        return EmergencyContact.builder().id(UUID.randomUUID().toString())
                .name(name).relationship(relationship).phone(phone).build();
    }

    private Map<String, ConsentRecord> consentsFor(List<String> grantedKeys, String signedBy) {
        Map<String, ConsentRecord> map = new HashMap<>();
        for (ConsentDefinition def : ConsentCatalog.CONSENTS) {
            boolean granted = grantedKeys.contains(def.key());
            map.put(def.key(), ConsentRecord.builder()
                    .granted(granted)
                    .signedBy(granted ? signedBy : null)
                    .signedAt(granted ? "2026-05-02T10:00:00.000Z" : null)
                    .build());
        }
        return map;
    }

    private void seedCompliance() {
        if (compliance.count() > 0) {
            return;
        }
        compliance.save(ProviderCompliance.builder().providerId(DEMO_NANNY_ID).providerName("Sarah Johnson")
                .jurisdiction(Jurisdiction.UK).documents(new ArrayList<>(List.of(
                        doc("ofsted", "ofsted_certificate.pdf", true, "EY123456", null, null),
                        doc("dbs", "enhanced_dbs.pdf", true, "DBS-009912", "2024-09-01", "2027-09-01"),
                        doc("paed_first_aid", "paediatric_first_aid.pdf", true, null, "2024-02-10", "2027-02-10"),
                        doc("public_liability", "liability_insurance.pdf", true, null, "2025-07-20", "2026-07-15")
                ))).build());

        compliance.save(ProviderCompliance.builder().providerId("6").providerName("Amara Okafor")
                .jurisdiction(Jurisdiction.UK).documents(new ArrayList<>(List.of(
                        doc("ofsted", "ofsted_registration.pdf", true, "EY778812", null, null),
                        doc("dbs", "dbs_enhanced.pdf", true, "DBS-114002", null, "2028-01-15"),
                        doc("paed_first_aid", "first_aid_cert.pdf", true, null, null, "2027-11-30"),
                        doc("eyfs", "eyfs_safeguarding.pdf", true, null, null, "2027-06-01"),
                        doc("public_liability", "public_liability.pdf", true, null, null, "2027-03-01")
                ))).build());

        compliance.save(ProviderCompliance.builder().providerId("7").providerName("Chioma Eze")
                .jurisdiction(Jurisdiction.US_CA).documents(new ArrayList<>(List.of(
                        doc("state_license", "fcc_home_license.pdf", true, "CA-FCC-55120", null, null),
                        doc("fbi_fingerprint", "live_scan_results.pdf", false, null, null, "2029-04-01"),
                        doc("cpr_first_aid_us", "pediatric_cpr.pdf", true, null, null, "2027-08-01"),
                        doc("liability_insurance_us", "liability.pdf", true, null, null, "2027-02-01")
                ))).build());
    }

    private ProviderDocument doc(String itemKey, String fileName, boolean verified,
                                 String reference, String issueDate, String expiryDate) {
        return ProviderDocument.builder().itemKey(itemKey).fileName(fileName).verified(verified)
                .reference(reference).issueDate(issueDate).expiryDate(expiryDate)
                .uploadedAt("2026-05-01T09:00:00.000Z").build();
    }

    private void seedIncidents() {
        if (incidents.count() > 0) {
            return;
        }
        incidents.save(Incident.builder()
                .reference("INC-2026-0001").nannyId(DEMO_NANNY_ID).nannyName("Sarah Johnson")
                .jurisdiction(Jurisdiction.UK).wardName("John Doe")
                .type(IncidentType.INJURY).severity(IncidentSeverity.LOW)
                .occurredAt("2026-06-10T14:20:00.000Z").location("Back garden")
                .description("Grazed his knee tripping on the patio while playing. Cleaned and a plaster applied.")
                .actionTaken("Cleaned the graze, applied a plaster, comforted him. No further concern.")
                .witnesses("None").guardianNotified(true).status(IncidentStatus.CLOSED).reviewedBy("Admin User")
                .createdAt(Instant.parse("2026-06-10T14:35:00.000Z")).updatedAt(Instant.parse("2026-06-11T09:00:00.000Z"))
                .build());

        incidents.save(Incident.builder()
                .reference("INC-2026-0002").nannyId(DEMO_NANNY_ID).nannyName("Sarah Johnson")
                .jurisdiction(Jurisdiction.UK).wardName("Jane Smith")
                .type(IncidentType.SAFEGUARDING).severity(IncidentSeverity.HIGH)
                .occurredAt("2026-06-18T16:00:00.000Z").location("Guardian's home")
                .description("Child disclosed information that raised a welfare concern. Recorded verbatim and reported immediately.")
                .actionTaken("Recorded the disclosure, reassured the child, notified the agency safeguarding lead the same day.")
                .witnesses("None").guardianNotified(false).status(IncidentStatus.ESCALATED)
                .escalatedTo("Ofsted & the Local Authority Designated Officer (LADO)")
                .escalationNote("Referred to LADO within 24 hours per safeguarding policy. Awaiting guidance.")
                .reviewedBy("Admin User")
                .createdAt(Instant.parse("2026-06-18T16:30:00.000Z")).updatedAt(Instant.parse("2026-06-19T08:15:00.000Z"))
                .build());

        incidents.save(Incident.builder()
                .reference("INC-2026-0003").nannyId(DEMO_NANNY_ID).nannyName("Sarah Johnson")
                .jurisdiction(Jurisdiction.UK).wardName("Michael Brown")
                .type(IncidentType.ILLNESS).severity(IncidentSeverity.MEDIUM)
                .occurredAt("2026-06-20T11:00:00.000Z").location("Guardian's home")
                .description("Developed a temperature of 38.5°C and was unsettled after his late-morning nap.")
                .actionTaken("Offered fluids, removed a layer, monitored temperature every 30 minutes, contacted guardian.")
                .witnesses("None").guardianNotified(true).status(IncidentStatus.UNDER_REVIEW)
                .createdAt(Instant.parse("2026-06-20T11:25:00.000Z")).updatedAt(Instant.parse("2026-06-20T11:25:00.000Z"))
                .build());
    }
}
