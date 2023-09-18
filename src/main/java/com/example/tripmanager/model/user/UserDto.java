package com.example.tripmanager.model.user;

import lombok.Builder;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class UserDto {
    private String id;

    private String username;

    private String email;

    private Set<Role> roles = new HashSet<>();
    private String token;
}
