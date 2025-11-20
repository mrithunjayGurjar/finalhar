package com.crm.dto;

import com.crm.entities.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketRequest(
    @NotBlank(message = "Title is required")
    String title,
    
    String description,
    
    Long assignedToId,
    
    @NotNull(message = "Priority is required")
    Priority priority
) {}
