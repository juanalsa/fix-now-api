package com.fixnow.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.api.application.dto.UserDTO;
import com.fixnow.api.application.dto.auth.LoginRequestDTO;
import com.fixnow.api.application.dto.auth.LoginResponseDTO;
import com.fixnow.api.application.exception.UserAlreadyExistsException;
import com.fixnow.api.application.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDTO userDTO;
    private LoginRequestDTO loginRequestDTO;
    private LoginResponseDTO loginResponseDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .name("test")
                .lastName("user")
                .userName("testuser")
                .password("password")
                .build();

        loginRequestDTO = new LoginRequestDTO("testuser", "password");
        loginResponseDTO = new LoginResponseDTO("dummy-token");
    }

    @Test
    public void should_return_201_when_register_successful() throws Exception {
        when(authService.register(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("testuser"));
    }

    @Test
    public void should_return_400_when_invalid_data_for_register() throws Exception {
        UserDTO invalidUserDTO = UserDTO.builder()
                .name("test")
                .lastName("user")
                .build();

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_409_when_user_already_exists_for_register() throws Exception {
        doThrow(new UserAlreadyExistsException(userDTO.userName())).when(authService).register(any(UserDTO.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    public void should_return_500_when_internal_server_error_for_register() throws Exception {
        doThrow(new RuntimeException("Internal Server Error")).when(authService).register(any(UserDTO.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void should_return_200_when_login_successful() throws Exception {
        when(authService.login(any(LoginRequestDTO.class))).thenReturn(loginResponseDTO);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("dummy-token"));
    }

    @Test
    public void should_return_400_when_invalid_data_for_login() throws Exception {
        LoginRequestDTO invalidLoginRequestDTO = LoginRequestDTO.builder().build();

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoginRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_return_500_when_internal_server_error_for_login() throws Exception {
        doThrow(new RuntimeException("Internal Server Error")).when(authService).login(any(LoginRequestDTO.class));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isInternalServerError());
    }
}