package com.dotnanny.service;

import com.dotnanny.common.Role;
import com.dotnanny.dto.RegisterRequest;
import com.dotnanny.model.User;
import com.dotnanny.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User register(RegisterRequest r) {
        if (repo.existsByEmailIgnoreCase(r.email())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }
        User user = User.builder()
                .fullName(r.fullName())
                .email(r.email().toLowerCase())
                .password(encoder.encode(r.password()))
                .role(r.role() == null ? Role.GUARDIAN : r.role())
                .phoneNumber(r.phoneNumber())
                .createdAt(Instant.now())
                .build();
        return repo.save(user);
    }

    public Optional<User> login(String email, String password) {
        return repo.findByEmailIgnoreCase(email)
                .filter(u -> u.getPassword() != null && encoder.matches(password, u.getPassword()));
    }

    public List<User> all() {
        return repo.findAll();
    }
}
