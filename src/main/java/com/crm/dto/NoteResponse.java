package com.crm.dto;

import com.crm.entities.NoteType;

import java.time.LocalDateTime;

public record NoteResponse(
    Long id,
    Long ticketId,
    AuthorInfo author,
    String message,
    NoteType type,
    LocalDateTime createdAt
) {
    public record AuthorInfo(Long id, String name) {}
}
