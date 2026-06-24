package com.dotnanny.catalog;

import java.util.List;

/** Standard childcare consents a guardian signs off (port of frontend config/care.ts). */
public final class ConsentCatalog {

    private ConsentCatalog() {}

    public static final List<ConsentDefinition> CONSENTS = List.of(
            new ConsentDefinition("photos", "Photos & media", "Take photos/videos of my child for care updates and reports."),
            new ConsentDefinition("outings", "Local outings & trips", "Take my child on local walks, parks and supervised outings."),
            new ConsentDefinition("sunscreen", "Apply sun cream", "Apply sun cream / lotion to my child as needed."),
            new ConsentDefinition("medication", "Administer medication", "Give prescribed medication following my written instructions."),
            new ConsentDefinition("emergency_treatment", "Emergency medical treatment", "Seek emergency medical care if I cannot be reached."),
            new ConsentDefinition("transport", "Transport by car", "Transport my child by car using an appropriate car seat."),
            new ConsentDefinition("water_play", "Water & swimming play", "Supervised water play / swimming activities.")
    );

    public static final List<String> AGE_GROUPS = List.of(
            "Newborn (0–12 mo)", "Toddler (1–3 yrs)", "Preschool (3–5 yrs)", "School Age (5–12 yrs)", "Teen (12+ yrs)"
    );
}
