package com.user.userservice.controller;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.dto.validator.CreateUserValidationGroup;
import com.user.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "API for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get users")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID id) {
        UserResponseDTO userResponseDTO = userService.getUser(id);

        return ResponseEntity.ok().body(userResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Get users")
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        List<UserResponseDTO> userResponseDTOs = userService.getUsers();

        return ResponseEntity.ok().body(userResponseDTOs);
    }

    @PostMapping
    @Operation(summary = "Create a user")
    public ResponseEntity<UserResponseDTO> createUser(@Validated({Default.class, CreateUserValidationGroup.class}) @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);

        return ResponseEntity.ok().body(userResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a user")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @Validated({Default.class}) @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.updateUser(id, userRequestDTO);

        return ResponseEntity.ok().body(userResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
