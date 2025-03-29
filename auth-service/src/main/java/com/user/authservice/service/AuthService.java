package com.user.authservice.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.user.authservice.dto.LoginRequestDTO;
import com.user.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {
        return userService.findByEmail(loginRequestDTO.getEmail())
                .filter(user -> passwordEncoder.matches(
                        loginRequestDTO.getPassword(), user.getPassword())
                )
                .map(user -> jwtUtil.generateToken(
                        user.getEmail(), user.getRole())
                );
    }

    public boolean validateToken(String token) {
        try {
            jwtUtil.verifyToken(token);
            return true;
        } catch (JWTVerificationException e){
            return false;
        }
    }
}