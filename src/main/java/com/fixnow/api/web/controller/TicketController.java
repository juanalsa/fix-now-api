package com.fixnow.api.web.controller;

import com.fixnow.api.application.dto.TicketDTO;
import com.fixnow.api.application.usecases.ticket.*;
import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.UserRepository;
import com.fixnow.api.infrastructure.mappers.TicketMapper;
import com.fixnow.api.web.exception.TicketNotFoundException;
import com.fixnow.api.web.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final CreateTicketUseCase createTicketUseCase;
    private final FindTicketByIdUseCase findTicketByIdUseCase;
    private final FindTicketByUserIdUseCase findTicketByUserIdUseCase;
    private final FindTicketsUseCase findTicketsUseCase;
    private final ModifyTicketUseCase modifyTicketUseCase;
    private final FilterTicketsUseCase filterTicketsUseCase;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) throws UserNotFoundException {
        User user = userRepository.findById(UUID.fromString(ticketDTO.userId()))
                .orElseThrow(() -> new UserNotFoundException(ticketDTO.userId()));

        Ticket ticket = Ticket.builder()
                .description(ticketDTO.description())
                .user(user)
                .status(Ticket.Status.valueOf(ticketDTO.status()))
                .build();

        Ticket createdTicket = createTicketUseCase.execute(ticket);

        return ResponseEntity.ok(TicketMapper.toDTO(createdTicket));

    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> findTicketById(@PathVariable String id) throws TicketNotFoundException {
        Ticket ticketFound = findTicketByIdUseCase.execute(UUID.fromString(id));

        return ResponseEntity.ok(TicketMapper.toDTO(ticketFound));
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> findTickets(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<Ticket> tickets = findTicketsUseCase.execute(page, size);
        List<TicketDTO> ticketsDTO = tickets.stream()
                .map(TicketMapper::toDTO)
                .toList();

        return ResponseEntity.ok(ticketsDTO);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TicketDTO>> findTicketByUserId(@PathVariable String id) {
        List<Ticket> tickets = findTicketByUserIdUseCase.execute(UUID.fromString(id));
        List<TicketDTO> ticketsDTO = tickets.stream()
                .map(TicketMapper::toDTO)
                .toList();

        return ResponseEntity.ok(ticketsDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> modifyTicket(@PathVariable String id,
                                                  @Valid @RequestBody TicketDTO ticketDTO) throws TicketNotFoundException {
        Ticket ticketModified = modifyTicketUseCase.execute(UUID.fromString(id),
                TicketMapper.toEntity(ticketDTO));

        return ResponseEntity.ok(TicketMapper.toDTO(ticketModified));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TicketDTO>> filterTickets(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String userId
    ) {
        Ticket.Status statusParam = status != null && !status.isEmpty() ?
                Ticket.Status.valueOf(status) : null;
        UUID userIdParam = userId != null && !userId.isEmpty() ?
                UUID.fromString(userId) : null;

        List<Ticket> tickets = filterTicketsUseCase.execute(statusParam, userIdParam);

        List<TicketDTO> ticketsDTO = tickets.stream()
                .map(TicketMapper::toDTO)
                .toList();

        return ResponseEntity.ok(ticketsDTO);
    }
}
