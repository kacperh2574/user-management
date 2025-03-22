package com.user.userservice.controller;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> userResponseDTOs = userService.getUsers();

        return ResponseEntity.ok().body(userResponseDTOs);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

        return ResponseEntity.ok().body(userResponseDTO);
    }
}
