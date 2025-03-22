package com.user.userservice.controller.integration;

import com.user.userservice.dto.UserRequestDTO;
import com.user.userservice.dto.UserResponseDTO;
import com.user.userservice.model.User;
import com.user.userservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.user.userservice.util.TestDataUtil.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void getUsers() {
        User user = createUserWithoutId();

        userRepository.save(user);

        ResponseEntity<UserResponseDTO[]> response = restTemplate.getForEntity("/users", UserResponseDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getName()).isEqualTo("User");
    }

    @Test
    void createUser() {
        UserRequestDTO userRequestDTO = createUserRequestDTO();

        ResponseEntity<UserResponseDTO> response = restTemplate.postForEntity("/users", userRequestDTO, UserResponseDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(userRequestDTO.getName());
    }

    @Test
    void updateUser() {
        User user = createUserWithoutId();

        userRepository.save(user);

        UserRequestDTO userRequestDTO = createUserRequestDTO();

        ResponseEntity<UserResponseDTO> response = restTemplate.exchange(
                "/users/" + user.getId(), HttpMethod.PUT, new HttpEntity<>(userRequestDTO), UserResponseDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(userRequestDTO.getName());
    }

    @Test
    void deleteUser() {
        User user = userRepository.save(createUserWithoutId());

        ResponseEntity<Void> response = restTemplate.exchange(
                "/users/" + user.getId(), HttpMethod.DELETE, null, Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}