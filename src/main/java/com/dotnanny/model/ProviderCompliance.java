package com.dotnanny.model;

import com.dotnanny.common.Jurisdiction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/** All compliance data for one provider (nanny). */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("compliance")
public class ProviderCompliance {
    @Id
    private String id;
    @Indexed(unique = true)
    private String providerId;
    private String providerName;
    @Builder.Default
    private Jurisdiction jurisdiction = Jurisdiction.UK;
    @Builder.Default
    private List<ProviderDocument> documents = new ArrayList<>();
}
