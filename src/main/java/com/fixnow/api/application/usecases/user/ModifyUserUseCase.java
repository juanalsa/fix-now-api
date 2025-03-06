package com.fixnow.api.application.usecases.user;

import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.UserRepository;
import com.fixnow.api.web.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ModifyUserUseCase {

    private final UserRepository userRepository;

    public User execute(UUID id, User userData) throws UserNotFoundException {
        User userFound = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id.toString()));

        userFound.setName(userData.getName());
        userFound.setLastName(userData.getLastName());
        userRepository.save(userFound);

        return userFound;
    }
}
