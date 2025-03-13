package com.fixnow.api.application.usecases.user;

import com.fixnow.api.application.exception.UserNotFoundException;
import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ModifyUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ModifyUserUseCase modifyUserUseCase;

    private User existingUser;
    private User updatedUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
        existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("John");
        existingUser.setLastName("Doe");

        updatedUser = new User();
        updatedUser.setName("Jane");
        updatedUser.setLastName("Smith");
    }

    @Test
    void should_modify_user_when_user_data_is_valid() throws UserNotFoundException {
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = modifyUserUseCase.execute(userId, updatedUser);

        assertNotNull(result);
        assertEquals("Jane", result.getName());
        assertEquals("Smith", result.getLastName());
        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    void should_throws_user_not_found_exception_when_user_does_not_exists() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> modifyUserUseCase.execute(userId, updatedUser));
        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}