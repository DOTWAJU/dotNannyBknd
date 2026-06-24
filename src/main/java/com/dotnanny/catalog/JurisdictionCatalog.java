package com.dotnanny.catalog;

import com.dotnanny.common.ComplianceCategory;
import com.dotnanny.common.Jurisdiction;

import java.util.List;
import java.util.Map;

/**
 * Static jurisdiction rule sets (UK, Ontario, California) — a 1:1 port of the
 * frontend's config/jurisdictions.ts. Add a jurisdiction here and the rest of
 * the compliance + ratio engine picks it up.
 */
public final class JurisdictionCatalog {

    private JurisdictionCatalog() {}

    public static final Map<Jurisdiction, JurisdictionConfig> CONFIGS = Map.of(
            Jurisdiction.UK, uk(),
            Jurisdiction.CA_ON, caOntario(),
            Jurisdiction.US_CA, usCalifornia()
    );

    public static JurisdictionConfig get(Jurisdiction j) {
        return CONFIGS.getOrDefault(j, CONFIGS.get(Jurisdiction.UK));
    }

    public static List<JurisdictionConfig> all() {
        return List.of(uk(), caOntario(), usCalifornia());
    }

    private static JurisdictionConfig uk() {
        return new JurisdictionConfig(
                Jurisdiction.UK, "United Kingdom", "England", "🇬🇧",
                "Childminder",
                "Ofsted & the Local Authority Designated Officer (LADO)",
                "GBP", "£",
                List.of(
                        new RatioRule("Total (under 8)", "Max 6 children"),
                        new RatioRule("Under 5", "No more than 3"),
                        new RatioRule("Under 1", "No more than 1")
                ),
                6,
                List.of(
                        new RatioLimit("under 1", 1, 1),
                        new RatioLimit("under 5", 5, 3)
                ),
                List.of(
                        new ComplianceItem("ofsted", "Ofsted registration", "Ofsted",
                                "Registration on the Early Years and/or Childcare Register.",
                                ComplianceCategory.REGISTRATION, false, null),
                        new ComplianceItem("dbs", "Enhanced DBS + barred list check", "Disclosure and Barring Service",
                                "Enhanced check with children’s barred list.",
                                ComplianceCategory.BACKGROUND, true, 36),
                        new ComplianceItem("paed_first_aid", "Paediatric First Aid (12hr)", "Approved EYFS provider",
                                "Full 12-hour paediatric first aid certificate.",
                                ComplianceCategory.TRAINING, true, 36),
                        new ComplianceItem("eyfs", "EYFS safeguarding training", "Local authority / approved provider",
                                "Early Years Foundation Stage safeguarding & welfare.",
                                ComplianceCategory.TRAINING, true, 24),
                        new ComplianceItem("public_liability", "Public liability insurance", null,
                                "Valid public liability cover for home-based care.",
                                ComplianceCategory.INSURANCE, true, 12)
                )
        );
    }

    private static JurisdictionConfig caOntario() {
        return new JurisdictionConfig(
                Jurisdiction.CA_ON, "Canada", "Ontario", "🇨🇦",
                "Home Child Care Provider",
                "the local Children’s Aid Society & Ministry of Education",
                "CAD", "$",
                List.of(
                        new RatioRule("Home child care (under 13)", "Max 6 children, incl. own under 4"),
                        new RatioRule("Under 2 years", "Max 3 children under 2")
                ),
                6,
                List.of(new RatioLimit("under 2", 2, 3)),
                List.of(
                        new ComplianceItem("agency_registration", "Licensed agency registration (CCEYA)",
                                "Ministry of Education (Ontario)",
                                "Registration through a licensed home child care agency.",
                                ComplianceCategory.REGISTRATION, false, null),
                        new ComplianceItem("vulnerable_sector_check", "Vulnerable Sector Check", "Local police service",
                                "Criminal record + vulnerable sector screening.",
                                ComplianceCategory.BACKGROUND, true, 60),
                        new ComplianceItem("first_aid_cpr_c", "Standard First Aid + CPR-C", "Red Cross / St John Ambulance",
                                "Standard first aid with infant/child CPR (level C).",
                                ComplianceCategory.TRAINING, true, 36),
                        new ComplianceItem("immunization", "Immunization records", "Public Health",
                                "Provider and household immunization on file.",
                                ComplianceCategory.HEALTH, false, null),
                        new ComplianceItem("liability_insurance_on", "Liability insurance", null,
                                "Home child care liability cover.",
                                ComplianceCategory.INSURANCE, true, 12)
                )
        );
    }

    private static JurisdictionConfig usCalifornia() {
        return new JurisdictionConfig(
                Jurisdiction.US_CA, "United States", "California", "🇺🇸",
                "Family Child Care Provider",
                "Child Protective Services (CPS) & Community Care Licensing",
                "USD", "$",
                List.of(
                        new RatioRule("Small family home", "Up to 8 children (with limits on infants)"),
                        new RatioRule("Infants", "No more than 2 under 2")
                ),
                8,
                List.of(new RatioLimit("under 2", 2, 2)),
                List.of(
                        new ComplianceItem("state_license", "Family Child Care Home licence",
                                "CA Dept. of Social Services (CCLD)",
                                "State licence to operate a family child care home.",
                                ComplianceCategory.REGISTRATION, false, null),
                        new ComplianceItem("fbi_fingerprint", "FBI & DOJ fingerprint clearance", "Live Scan (CCDBG)",
                                "Federal + state criminal background via Live Scan.",
                                ComplianceCategory.BACKGROUND, true, 60),
                        new ComplianceItem("abuse_registry", "Child abuse & sex-offender registry check", "CACI / NCIC (CCDBG)",
                                "Child abuse central index and sex-offender registry.",
                                ComplianceCategory.BACKGROUND, true, 60),
                        new ComplianceItem("cpr_first_aid_us", "Pediatric CPR & First Aid", "EMSA-approved provider",
                                "Pediatric CPR and first aid certification.",
                                ComplianceCategory.TRAINING, true, 24),
                        new ComplianceItem("liability_insurance_us", "Liability insurance", null,
                                "Family child care liability cover.",
                                ComplianceCategory.INSURANCE, true, 12)
                )
        );
    }
}
