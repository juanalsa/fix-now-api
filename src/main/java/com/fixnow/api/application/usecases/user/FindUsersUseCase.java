package com.fixnow.api.application.usecases.user;

import com.fixnow.api.domain.model.User;
import com.fixnow.api.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindUsersUseCase {

    private final UserRepository userRepository;

    public List<User> execute(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).getContent();
    }
}
