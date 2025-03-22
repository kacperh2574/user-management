package com.user.userservice.service;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.mapper.UserMapper;
import com.user.userservice.model.User;
import com.user.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponseDTO> getUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        User newUser = userRepository.save(
                UserMapper.toModel(userRequestDTO)
        );

        return UserMapper.toDTO(newUser);
    }
}
