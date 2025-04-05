package com.user.userservice.service;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.exception.EmailAlreadyExistsException;
import com.user.userservice.exception.UserNotFoundException;
import com.user.userservice.grpc.BillingServiceGrpcClient;
import com.user.userservice.kafka.KafkaProducer;
import com.user.userservice.mapper.UserMapper;
import com.user.userservice.model.User;
import com.user.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public UserService(UserRepository userRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
        this.userRepository = userRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        validateEmailUniquenessForCreate(userRequestDTO.getEmail());

        User newUser = userRepository.save(UserMapper.toModel(userRequestDTO));

        billingServiceGrpcClient.createBillingAccount(
                newUser.getId().toString(), newUser.getName(), newUser.getEmail()
        );

        kafkaProducer.sendEvent(newUser);

        return UserMapper.toDTO(newUser);
    }

    public UserResponseDTO getUser(UUID id) {
        User user = findUserById(id);

        return UserMapper.toDTO(user);
    }

    public List<UserResponseDTO> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
        User user = findUserById(id);

        validateEmailUniquenessForUpdate(userRequestDTO.getEmail(), id);

        User updatedUser = updateUserFields(user, userRequestDTO);

        updatedUser = userRepository.save(updatedUser);

        return UserMapper.toDTO(updatedUser);
    }

    public void deleteUser(UUID id) {
        findUserById(id);

        userRepository.deleteById(id);
    }

    private User findUserById(UUID id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("A user with this ID not found: " + id));
    }

    private User updateUserFields(User user, UserRequestDTO userRequestDTO) {
        return user.toBuilder()
                .name(userRequestDTO.getName())
                .email(userRequestDTO.getEmail())
                .address(userRequestDTO.getAddress())
                .dateOfBirth(LocalDate.parse(userRequestDTO.getDateOfBirth()))
                .build();
    }

    private void validateEmailUniquenessForCreate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException("A user with this email already exists: " + email);
        }
    }

    private void validateEmailUniquenessForUpdate(String email, UUID id) {
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new EmailAlreadyExistsException("A user with this email already exists: " + email);
        }
    }
}
