package com.user.userservice.util;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.model.User;

import java.time.LocalDate;
import java.util.UUID;

public class TestDataUtil {

    public static User createUserWithoutId() {
        return User.builder()
                .name("User")
                .email("user@example.com")
                .address("Address")
                .dateOfBirth(LocalDate.of(2023, 10, 10))
                .dateOfRegistration(LocalDate.of(2023, 12, 10))
                .build();
    }

    public static User createUserA() {
        return User.builder()
                .id(UUID.fromString("c6adfd6e-deb6-4f9b-ada9-faef8a938657"))
                .name("User A")
                .email("user.a@example.com")
                .address("Address A")
                .dateOfBirth(LocalDate.of(2025, 10, 10))
                .dateOfRegistration(LocalDate.of(2025, 12, 10))
                .build();
    }

    public static User createUserB() {
        return User.builder()
                .id(UUID.fromString("b4d08981-0663-45cd-a47c-c567c5eec261"))
                .name("User B")
                .email("user.b@example.com")
                .address("Address B")
                .dateOfBirth(LocalDate.of(2024, 10, 10))
                .dateOfRegistration(LocalDate.of(2024, 12, 10))
                .build();
    }

    public static UserRequestDTO createUserRequestDTO() {
        return UserRequestDTO.builder()
                .name("User A")
                .email("user.A@example.com")
                .address("Address A")
                .dateOfBirth("2025-10-10")
                .dateOfRegistration("2025-12-10")
                .build();
    }
}