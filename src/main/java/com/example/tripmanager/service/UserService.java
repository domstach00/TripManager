package com.example.tripmanager.service;

import com.example.tripmanager.model.user.Role;
import com.example.tripmanager.model.user.User;
import com.example.tripmanager.model.user.UserDto;

import java.util.Set;

public interface UserService {
    User getCurrentUser();
    UserDto getCurrentUserDto();
    Set<Role> getCurrentUserRoles();
}
