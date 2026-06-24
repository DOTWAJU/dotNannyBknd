package com.dotnanny.model;

import com.dotnanny.common.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class User {
    @Id
    private String id;
    private String fullName;
    @Indexed(unique = true)
    private String email;
    private String password; // demo only — store a hash in production
    private Role role;
    private String phoneNumber;
    private Instant createdAt;
}
