package com.fixnow.api.domain.repository;

import com.fixnow.api.domain.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findByStatus(Ticket.Status status);

    List<Ticket> findByUserId(UUID userId);
}
