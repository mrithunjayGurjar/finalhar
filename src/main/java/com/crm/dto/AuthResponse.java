package com.crm.dto;

import com.crm.entities.Role;

public record AuthResponse(
    String token,
    Long userId,
    String name,
    String email,
    Role role
) {}
