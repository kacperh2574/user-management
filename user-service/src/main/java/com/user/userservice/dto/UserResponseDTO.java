package com.user.userservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String address;
    private String dateOfBirth;
}
