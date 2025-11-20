package com.crm.dto;

import com.crm.entities.NoteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddNoteRequest(
    @NotBlank(message = "Message is required")
    String message,
    
    @NotNull(message = "Note type is required")
    NoteType type
) {}
