package com.dotnanny.service;

import com.dotnanny.common.Role;
import com.dotnanny.dto.RegisterRequest;
import com.dotnanny.model.User;
import com.dotnanny.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public User register(RegisterRequest r) {
        if (repo.existsByEmailIgnoreCase(r.email())) {
            throw new IllegalArgumentException("An account with this email already exists");
        }
        User user = User.builder()
                .fullName(r.fullName())
                .email(r.email().toLowerCase())
                .password(r.password()) // demo only — hash in production
                .role(r.role() == null ? Role.GUARDIAN : r.role())
                .phoneNumber(r.phoneNumber())
                .createdAt(Instant.now())
                .build();
        return repo.save(user);
    }

    public Optional<User> login(String email, String password) {
        return repo.findByEmailIgnoreCase(email).filter(u -> u.getPassword().equals(password));
    }

    public List<User> all() {
        return repo.findAll();
    }
}
