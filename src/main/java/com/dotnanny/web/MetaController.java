package com.dotnanny.web;

import com.dotnanny.catalog.ConsentCatalog;
import com.dotnanny.catalog.ConsentDefinition;
import com.dotnanny.catalog.JurisdictionCatalog;
import com.dotnanny.catalog.JurisdictionConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Reference data the frontend needs: jurisdictions, consents, age groups. */
@RestController
@RequestMapping("/api")
public class MetaController {

    @GetMapping("/jurisdictions")
    public List<JurisdictionConfig> jurisdictions() {
        return JurisdictionCatalog.all();
    }

    @GetMapping("/consents")
    public List<ConsentDefinition> consents() {
        return ConsentCatalog.CONSENTS;
    }

    @GetMapping("/age-groups")
    public List<String> ageGroups() {
        return ConsentCatalog.AGE_GROUPS;
    }
}
