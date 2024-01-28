package com.example.tripmanager.service;

import com.example.tripmanager.model.user.Role;
import com.example.tripmanager.model.user.User;
import com.example.tripmanager.model.user.UserDto;

import java.security.Principal;
import java.util.Set;

public interface UserService {
    User getCurrentUser();
    User getUserFromPrincipal(Principal principal);
    UserDto getCurrentUserDto();
    UserDto getUserDtoFromPrincipal(Principal principal);
    Set<Role> getCurrentUserRoles();
}
