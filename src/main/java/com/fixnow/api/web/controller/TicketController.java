package com.fixnow.api.web.controller;

import com.fixnow.api.application.dto.TicketDTO;
import com.fixnow.api.application.exception.TicketNotFoundException;
import com.fixnow.api.application.exception.UserNotFoundException;
import com.fixnow.api.application.usecases.ticket.*;
import com.fixnow.api.domain.model.Ticket;
import com.fixnow.api.infrastructure.mappers.TicketMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Tickets", description = "Ticket Management")
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


    @Operation(summary = "Create new ticket", description = "Register a new ticket in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
    })
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@Valid @RequestBody TicketDTO ticketDTO) throws UserNotFoundException {
        Ticket createdTicket = createTicketUseCase.execute(TicketMapper.toEntity(ticketDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(TicketMapper.toDTO(createdTicket));

    }

    @Operation(summary = "Get ticket by ID", description = "Get a ticket registered in the system by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket found successfully"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> findTicketById(@PathVariable String id) throws TicketNotFoundException {
        Ticket ticketFound = findTicketByIdUseCase.execute(UUID.fromString(id));

        return ResponseEntity.ok(TicketMapper.toDTO(ticketFound));
    }

    @Operation(summary = "Get all tickets", description = "Returns paginated ticket list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket list successfully obtained"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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

    @Operation(summary = "Get all user tickets", description = "Returns a user ticket list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket list successfully obtained"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<List<TicketDTO>> findTicketByUserId(@PathVariable String id) {
        List<Ticket> tickets = findTicketByUserIdUseCase.execute(UUID.fromString(id));
        List<TicketDTO> ticketsDTO = tickets.stream()
                .map(TicketMapper::toDTO)
                .toList();

        return ResponseEntity.ok(ticketsDTO);
    }

    @Operation(summary = "Modify a user ticket", description = "Modify the data of user ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket modified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> modifyTicket(@PathVariable String id,
                                                  @Valid @RequestBody TicketDTO ticketDTO) throws TicketNotFoundException {
        Ticket ticketModified = modifyTicketUseCase.execute(UUID.fromString(id),
                TicketMapper.toEntity(ticketDTO));

        return ResponseEntity.ok(TicketMapper.toDTO(ticketModified));
    }

    @Operation(summary = "Get filtered tickets", description = "Returns a ticket list filtered by status and user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket list successfully obtained"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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
