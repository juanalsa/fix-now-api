package com.fixnow.api.web.controller;

import com.fixnow.api.application.dto.UserDTO;
import com.fixnow.api.application.usecases.user.FindUserByIdUseCase;
import com.fixnow.api.application.usecases.user.FindUsersUseCase;
import com.fixnow.api.application.usecases.user.ModifyUserUseCase;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.infrastructure.mappers.UserMapper;
import com.fixnow.api.web.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final FindUserByIdUseCase findUserByIdUseCase;
    private final FindUsersUseCase findUsersUseCase;
    private final ModifyUserUseCase modifyUserUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable String id) throws UserNotFoundException {
        User userFound = findUserByIdUseCase.execute(UUID.fromString(id));

        return ResponseEntity.ok(UserMapper.toDTO(userFound));
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> modifyUser(@PathVariable String id,
                                              @Valid @RequestBody UserDTO userDTO) throws UserNotFoundException {
        User userModified = modifyUserUseCase.execute(UUID.fromString(id), UserMapper.toEntity(userDTO));

        return ResponseEntity.ok(UserMapper.toDTO(userModified));
    }
}
