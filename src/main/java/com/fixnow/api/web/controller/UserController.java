package com.fixnow.api.web.controller;

import com.fixnow.api.application.dto.UserDTO;
import com.fixnow.api.application.usecases.user.FindUserByIdUseCase;
import com.fixnow.api.application.usecases.user.FindUsersUseCase;
import com.fixnow.api.application.usecases.user.ModifyUserUseCase;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.infrastructure.mappers.UserMapper;
import com.fixnow.api.application.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "User Management")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindUsersUseCase findUsersUseCase;
    private final ModifyUserUseCase modifyUserUseCase;

    @Operation(summary = "Get user by ID", description = "Get a user registered in the system by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String id) throws UserNotFoundException {
        User userFound = findUserByIdUseCase.execute(UUID.fromString(id));

        return ResponseEntity.ok(UserMapper.toDTO(userFound));
    }

    @Operation(summary = "Get all users", description = "Returns paginated user list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User list successfully obtained"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping
    public ResponseEntity<List<UserDTO>> findUsers(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        List<User> users = findUsersUseCase.execute(page, size);
        List<UserDTO> usersDTO = users.stream()
                .map(UserMapper::toDTO)
                .toList();

        return ResponseEntity.ok(usersDTO);
    }

    @Operation(summary = "Modify the user info", description = "Modify user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User modified successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> modifyUser(@PathVariable String id,
                                              @Valid @RequestBody UserDTO userDTO) throws UserNotFoundException {
        User userModified = modifyUserUseCase.execute(UUID.fromString(id), UserMapper.toEntity(userDTO));

        return ResponseEntity.ok(UserMapper.toDTO(userModified));
    }
}
