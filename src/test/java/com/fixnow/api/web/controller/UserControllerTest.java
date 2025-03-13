package com.fixnow.api.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fixnow.api.application.dto.UserDTO;
import com.fixnow.api.application.exception.UserNotFoundException;
import com.fixnow.api.application.usecases.user.FindUserByIdUseCase;
import com.fixnow.api.application.usecases.user.FindUsersUseCase;
import com.fixnow.api.application.usecases.user.ModifyUserUseCase;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.infrastructure.mappers.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FindUserByIdUseCase findUserByIdUseCase;

    @MockitoBean
    private FindUsersUseCase findUsersUseCase;

    @MockitoBean
    private ModifyUserUseCase modifyUserUseCase;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .name("John")
                .lastName("Doe")
                .userName("johndoe")
                .password("123456")
                .build();
    }

    @Test
    public void should_get_user_by_id() throws Exception {
        when(findUserByIdUseCase.execute(any(UUID.class))).thenReturn(user);

        mockMvc.perform(get("/users/" + user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("John"));
    }

    @Test
    public void should_return_404_when_user_not_found_by_id() throws Exception {
        when(findUserByIdUseCase.execute(any(UUID.class))).thenThrow(new UserNotFoundException(userId.toString()));

        mockMvc.perform(get("/users/" + userId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found with id: " + userId.toString()));
    }

    @Test
    public void should_get_all_users() throws Exception {
        when(findUsersUseCase.execute(any(Integer.class), any(Integer.class))).thenReturn(Collections.singletonList(user));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userId.toString()))
                .andExpect(jsonPath("$[0].name").value("John"));
    }

    @Test
    public void should_modify_user() throws Exception {
        UserDTO userModifiedDTO = UserDTO.builder()
                .id(userId.toString())
                .name("Jane")
                .lastName("Doe")
                .userName("janedoe")
                .password("654321")
                .build();
        User userModified = UserMapper.toEntity(userModifiedDTO);

        when(modifyUserUseCase.execute(any(UUID.class), any(User.class))).thenReturn(userModified);

        mockMvc.perform(put("/users/" + user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModifiedDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.userName").value("janedoe"));
    }

    @Test
    public void should_return_400_when_invalid_data_for_modify_user() throws Exception {
        UserDTO invalidUserDTO = UserDTO.builder()
                .id(userId.toString())
                .name("")
                .lastName("Doe")
                .userName("janedoe")
                .password("654321")
                .build();

        mockMvc.perform(put("/users/" + user.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDTO)))
                .andExpect(status().isBadRequest());
    }
}