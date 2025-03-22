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
import java.util.Optional;
import java.util.UUID;

import static com.user.userservice.util.TestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void createUser_returnsCreatedUser() {
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

    @Test
    void updateUser_returnsUpdatedUser() {
        UUID id = UUID.fromString("c6adfd6e-deb6-4f9b-ada9-faef8a938657");
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

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDTO userResponseDTO = userService.updateUser(id, userRequestDTO);

        assertThat(userResponseDTO)
                .usingRecursiveComparison()
                .isEqualTo(UserMapper.toDTO(savedUser));
    }

    @Test
    void deleteUser_returnsNoContent() {
        UUID id = UUID.fromString("c6adfd6e-deb6-4f9b-ada9-faef8a938657");

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}