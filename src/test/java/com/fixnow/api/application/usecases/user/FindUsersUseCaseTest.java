package com.fixnow.api.application.usecases.user;

import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FindUsersUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUsersUseCase findUsersUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_return_users_when_search_with_pagination() {
        // Arrange
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);
        Page<User> page = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findAll(pageable)).thenReturn(page);

        // Act
        List<User> result = findUsersUseCase.execute(0, 10);

        // Assert
        assertEquals(2, result.size());
        verify(userRepository, times(1)).findAll(pageable);
    }
}