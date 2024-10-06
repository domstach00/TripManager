package com.example.tripmanager.model.account;

import com.example.tripmanager.model.AbstractEntityDto;

import java.util.HashSet;
import java.util.Set;

public class AccountDto extends AbstractEntityDto {

    private String username;

    private String email;

    private Set<Role> roles = new HashSet<>();
    private String token;


    public AccountDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
