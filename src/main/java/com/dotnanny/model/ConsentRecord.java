package com.dotnanny.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** A guardian's consent decision (with e-signature) for one consent key. */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRecord {
    private boolean granted;
    private String signedBy;
    private String signedAt;
}
