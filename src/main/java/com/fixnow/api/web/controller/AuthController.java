package com.fixnow.api.web.controller;

import com.fixnow.api.application.dto.UserDTO;
import com.fixnow.api.application.dto.auth.LoginRequestDTO;
import com.fixnow.api.application.dto.auth.LoginResponseDTO;
import com.fixnow.api.application.services.AuthService;
import com.fixnow.api.web.exception.UserAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) throws UserAlreadyExistsException {

        UserDTO registeredUser = authService.register(userDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO loginResponse = authService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }
}
