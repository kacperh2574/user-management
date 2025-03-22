package com.user.userservice.service;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.mapper.UserMapper;
import com.user.userservice.model.User;
import com.user.userservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.user.userservice.util.TestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void getUsers_returnsAllUsers() {
        User userA = createUserA();
        User userB = createUserB();
        List<User> users = List.of(userA, userB);

        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> userDTOs = userService.getUsers();

        assertThat(users).hasSize(2);
        assertThat(userDTOs)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        UserMapper.toDTO(userA),
                        UserMapper.toDTO(userB)
                ));
    }

    @Test
    void createUser_returnsUser() {
        UserRequestDTO userRequestDTO = createUserRequestDTO();
        User user = UserMapper.toModel(userRequestDTO);
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .name(user.getName())
                .email(user.getEmail())
                .address(user.getAddress())
                .dateOfBirth(user.getDateOfBirth())
                .dateOfRegistration(user.getDateOfRegistration())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

        assertThat(userResponseDTO)
                .usingRecursiveComparison()
                .isEqualTo(UserMapper.toDTO(savedUser));
    }
}