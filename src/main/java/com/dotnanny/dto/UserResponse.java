package com.dotnanny.dto;

import com.dotnanny.common.Role;
import com.dotnanny.model.User;

public record UserResponse(
        String id,
        String fullName,
        String email,
        Role role,
        String phoneNumber
) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getId(), u.getFullName(), u.getEmail(), u.getRole(), u.getPhoneNumber());
    }
}
