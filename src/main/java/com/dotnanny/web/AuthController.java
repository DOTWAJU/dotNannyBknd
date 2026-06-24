package com.dotnanny.web;

import com.dotnanny.dto.AuthResponse;
import com.dotnanny.dto.LoginRequest;
import com.dotnanny.dto.RegisterRequest;
import com.dotnanny.dto.UserResponse;
import com.dotnanny.model.User;
import com.dotnanny.security.JwtService;
import com.dotnanny.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService users;
    private final JwtService jwt;

    public AuthController(UserService users, JwtService jwt) {
        this.users = users;
        this.jwt = jwt;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        User user = users.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return users.login(req.email(), req.password())
                .map(u -> ResponseEntity.ok(authResponse(u)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    private AuthResponse authResponse(User user) {
        String token = jwt.generate(user.getEmail(), user.getRole().json());
        return new AuthResponse(token, UserResponse.from(user));
    }
}
