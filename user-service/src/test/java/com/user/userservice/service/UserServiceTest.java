package com.user.userservice.service;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.grpc.BillingServiceGrpcClient;
import com.user.userservice.kafka.KafkaProducer;
import com.user.userservice.mapper.UserMapper;
import com.user.userservice.model.User;
import com.user.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BillingServiceGrpcClient billingServiceGrpcClient;

    @Mock
    KafkaProducer kafkaProducer;

    @InjectMocks
    UserService userService;

    UUID id;
    UserRequestDTO userRequestDTO;
    User user;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        userRequestDTO = createUserRequestDTO();
        user = createUserA();
    }

    @Test
    void createUser_returnsCreatedUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO userResponse = userService.createUser(userRequestDTO);

        assertThat(userResponse)
                .usingRecursiveComparison()
                .isEqualTo(UserMapper.toDTO(user));

        verify(billingServiceGrpcClient, times(1))
                .createBillingAccount(user.getId().toString(), user.getName(), user.getEmail());
        verify(kafkaProducer, times(1))
                .sendEvent(user);
    }

    @Test
    void getUsers_returnsAllUsers() {
        List<User> users = List.of(createUserA(), createUserB());
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponseDTO> usersResponse = userService.getUsers();

        assertThat(usersResponse)
                .usingRecursiveComparison()
                .isEqualTo(users.stream().map(UserMapper::toDTO).toList());
    }

    @Test
    void updateUser_returnsUpdatedUser() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO userResponse = userService.updateUser(id, userRequestDTO);

        assertThat(userResponse)
                .usingRecursiveComparison()
                .isEqualTo(UserMapper.toDTO(user));
    }

    @Test
    void updateUser_returnsUpdatedUser_whenUserExists() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO userResponse = userService.updateUser(id, userRequestDTO);

        assertThat(userResponse)
                .usingRecursiveComparison()
                .isEqualTo(UserMapper.toDTO(user));
    }

    @Test
    void updateUser_throwsException_whenUserDoesNotExist() {
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(id, userRequestDTO));
    }

    @Test
    void deleteUser_throwsException_whenUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(id));
    }
}