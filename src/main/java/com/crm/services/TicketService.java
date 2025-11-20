package com.crm.services;

import com.crm.dto.*;
import com.crm.entities.*;
import com.crm.repositories.TicketNoteRepository;
import com.crm.repositories.TicketRepository;
import com.crm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketNoteRepository noteRepository;
    
    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request, User currentUser) {
        Ticket ticket = Ticket.builder()
                .title(request.title())
                .description(request.description())
                .createdBy(currentUser)
                .priority(request.priority())
                .status(TicketStatus.OPEN)
                .build();
        
        if (request.assignedToId() != null) {
            User assignedTo = userRepository.findById(request.assignedToId())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));
            ticket.setAssignedTo(assignedTo);
        }
        
        ticket = ticketRepository.save(ticket);
        return toTicketResponse(ticket);
    }
    
    @Transactional
    public TicketResponse updateStatus(Long ticketId, UpdateStatusRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        if (!isValidStatusTransition(ticket.getStatus(), request.status())) {
            throw new RuntimeException("Invalid status transition from " + ticket.getStatus() + " to " + request.status());
        }
        
        ticket.setStatus(request.status());
        ticket = ticketRepository.save(ticket);
        
        return toTicketResponse(ticket);
    }
    
    private boolean isValidStatusTransition(TicketStatus current, TicketStatus next) {
        return switch (current) {
            case OPEN -> next == TicketStatus.IN_PROGRESS;
            case IN_PROGRESS -> next == TicketStatus.ESCALATED;
            case ESCALATED -> next == TicketStatus.RESOLVED;
            case RESOLVED -> next == TicketStatus.CLOSED;
            case CLOSED -> false;
        };
    }
    
    @Transactional
    public TicketResponse escalateTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        if (ticket.getAssignedTo() == null) {
            throw new RuntimeException("Ticket must be assigned before escalation");
        }
        
        if (ticket.getStatus() != TicketStatus.IN_PROGRESS && ticket.getStatus() != TicketStatus.ESCALATED) {
            throw new RuntimeException("Can only escalate tickets that are in IN_PROGRESS or ESCALATED status. Current status: " + ticket.getStatus());
        }
        
        User currentAssignee = ticket.getAssignedTo();
        
        switch (currentAssignee.getRole()) {
            case USER -> {
                List<User> agents = userRepository.findByRole(Role.AGENT);
                if (agents.isEmpty()) {
                    throw new RuntimeException("No agents available for escalation");
                }
                ticket.setAssignedTo(agents.get(0));
                ticket.setStatus(TicketStatus.ESCALATED);
            }
            case AGENT -> {
                List<User> managers = userRepository.findByRole(Role.MANAGER);
                if (managers.isEmpty()) {
                    throw new RuntimeException("No managers available for escalation");
                }
                ticket.setAssignedTo(managers.get(0));
                ticket.setStatus(TicketStatus.ESCALATED);
            }
            case MANAGER -> {
                ticket.setStatus(TicketStatus.ESCALATED);
            }
        }
        
        ticket = ticketRepository.save(ticket);
        return toTicketResponse(ticket);
    }
    
    public List<ActiveTicketResponse> getActiveTickets() {
        return ticketRepository.findByStatusNot(TicketStatus.CLOSED).stream()
                .map(this::toActiveTicketResponse)
                .collect(Collectors.toList());
    }
    
    public TicketResponse getTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        return toTicketResponse(ticket);
    }
    
    @Transactional
    public NoteResponse addNote(Long ticketId, AddNoteRequest request, User currentUser) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        
        TicketNote note = TicketNote.builder()
                .ticket(ticket)
                .author(currentUser)
                .message(request.message())
                .type(request.type())
                .build();
        
        note = noteRepository.save(note);
        return toNoteResponse(note);
    }
    
    public List<NoteResponse> getTicketNotes(Long ticketId) {
        return noteRepository.findByTicketIdOrderByCreatedAtAsc(ticketId).stream()
                .map(this::toNoteResponse)
                .collect(Collectors.toList());
    }
    
    private TicketResponse toTicketResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                new TicketResponse.UserInfo(
                        ticket.getCreatedBy().getId(),
                        ticket.getCreatedBy().getName(),
                        ticket.getCreatedBy().getEmail()
                ),
                ticket.getAssignedTo() != null ? new TicketResponse.UserInfo(
                        ticket.getAssignedTo().getId(),
                        ticket.getAssignedTo().getName(),
                        ticket.getAssignedTo().getEmail()
                ) : null,
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
    
    private ActiveTicketResponse toActiveTicketResponse(Ticket ticket) {
        return new ActiveTicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getPriority(),
                ticket.getStatus(),
                ticket.getAssignedTo() != null ? ticket.getAssignedTo().getName() : "Unassigned",
                ticket.getUpdatedAt()
        );
    }
    
    private NoteResponse toNoteResponse(TicketNote note) {
        return new NoteResponse(
                note.getId(),
                note.getTicket().getId(),
                new NoteResponse.AuthorInfo(
                        note.getAuthor().getId(),
                        note.getAuthor().getName()
                ),
                note.getMessage(),
                note.getType(),
                note.getCreatedAt()
        );
    }
}
