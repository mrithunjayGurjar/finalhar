package com.crm.repositories;

import com.crm.entities.Ticket;
import com.crm.entities.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatusNot(TicketStatus status);
    List<Ticket> findByCreatedById(Long userId);
    List<Ticket> findByAssignedToId(Long userId);
}
