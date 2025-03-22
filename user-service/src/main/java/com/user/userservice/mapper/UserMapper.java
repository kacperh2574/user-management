package com.user.userservice.mapper;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.model.User;

import java.time.LocalDate;

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

    public static User toModel(UserRequestDTO userRequestDTO) {
        return User.builder()
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .address(userRequestDTO.getAddress())
                .dateOfBirth(LocalDate.parse(userRequestDTO.getDateOfBirth()))
                .dateOfRegistration(LocalDate.parse(userRequestDTO.getDateOfRegistration()))
                .build();
    }
}
