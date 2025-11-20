package com.crm.dto;

import com.crm.entities.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
    @NotBlank(message = "Name is required")
    String name,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    String email,
    
    @NotBlank(message = "Password is required")
    String password,
    
    @NotNull(message = "Role is required")
    Role role
) {}
