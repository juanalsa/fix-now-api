package com.fixnow.api.infrastructure.mappers;

import com.fixnow.api.application.dto.UserDTO;
import com.fixnow.api.domain.model.User;

import java.util.UUID;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .lastName(user.getLastName())
                .userName(user.getUsername())
                .password(user.getPassword())
                .build();
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.id() != null ? UUID.fromString(userDTO.id()) : null)
                .name(userDTO.name())
                .lastName(userDTO.lastName())
                .userName(userDTO.userName())
                .password(userDTO.password())
                .build();
    }
}
