package com.crm.controllers;

import com.crm.dto.*;
import com.crm.entities.User;
import com.crm.services.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    
    private final TicketService ticketService;
    
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(ticketService.createTicket(request, currentUser));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicket(id));
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request
    ) {
        return ResponseEntity.ok(ticketService.updateStatus(id, request));
    }
    
    @PutMapping("/{id}/escalate")
    public ResponseEntity<TicketResponse> escalateTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.escalateTicket(id));
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<ActiveTicketResponse>> getActiveTickets() {
        return ResponseEntity.ok(ticketService.getActiveTickets());
    }
    
    @PostMapping("/{id}/notes")
    public ResponseEntity<NoteResponse> addNote(
            @PathVariable Long id,
            @Valid @RequestBody AddNoteRequest request,
            @AuthenticationPrincipal User currentUser
    ) {
        return ResponseEntity.ok(ticketService.addNote(id, request, currentUser));
    }
    
    @GetMapping("/{id}/notes")
    public ResponseEntity<List<NoteResponse>> getTicketNotes(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketNotes(id));
    }
}
