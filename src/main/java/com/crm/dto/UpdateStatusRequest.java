package com.crm.dto;

import com.crm.entities.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
    @NotNull(message = "Status is required")
    TicketStatus status
) {}
