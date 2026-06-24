package com.dotnanny.dto;

/** Returned by login/register: a JWT plus the authenticated user. */
public record AuthResponse(String token, UserResponse user) {}
