package com.dotnanny.web;

import com.dotnanny.dto.UserResponse;
import com.dotnanny.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService users;

    public UserController(UserService users) {
        this.users = users;
    }

    @GetMapping
    public List<UserResponse> all() {
        return users.all().stream().map(UserResponse::from).toList();
    }
}
