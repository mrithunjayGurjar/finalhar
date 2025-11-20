package com.crm.dto;

import com.crm.entities.Priority;
import com.crm.entities.TicketStatus;

import java.time.LocalDateTime;

public record ActiveTicketResponse(
    Long id,
    String title,
    Priority priority,
    TicketStatus status,
    String assignedToName,
    LocalDateTime lastUpdated
) {}
