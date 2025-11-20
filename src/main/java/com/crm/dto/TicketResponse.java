package com.crm.dto;

import com.crm.entities.Priority;
import com.crm.entities.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponse(
    Long id,
    String title,
    String description,
    UserInfo createdBy,
    UserInfo assignedTo,
    Priority priority,
    TicketStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public record UserInfo(Long id, String name, String email) {}
}
