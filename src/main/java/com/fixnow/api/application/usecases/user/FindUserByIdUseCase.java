package com.fixnow.api.application.usecases.user;

import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.UserRepository;
import com.fixnow.api.application.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindUserByIdUseCase {

    private final UserRepository userRepository;

    public User execute(UUID id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));
    }
}
