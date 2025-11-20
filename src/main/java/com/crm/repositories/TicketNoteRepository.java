package com.crm.repositories;

import com.crm.entities.TicketNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketNoteRepository extends JpaRepository<TicketNote, Long> {
    List<TicketNote> findByTicketIdOrderByCreatedAtAsc(Long ticketId);
}
