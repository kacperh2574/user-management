package com.user.userservice.mapper;

import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.model.User;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth().toString())
                .build();
    }
}
