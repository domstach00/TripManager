package com.example.tripmanager.account.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public enum Role {
    ROLE_USER,
    ROLE_MODERATOR,
    ROLE_ADMIN;

    public static final String ROLE_ADMIN_NAME = "ROLE_ADMIN";

    public static Collection<? extends GrantedAuthority> toGrantedAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

    public static List<String> roleToString(Collection<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .toList();
    }
}
