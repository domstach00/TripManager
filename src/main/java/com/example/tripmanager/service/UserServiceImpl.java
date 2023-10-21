package com.example.tripmanager.service;

import com.example.tripmanager.model.user.Role;
import com.example.tripmanager.model.user.User;
import com.example.tripmanager.model.user.UserDto;
import com.example.tripmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public UserDto getCurrentUserDto() {
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Set<Role> getCurrentUserRoles() {
        return ((UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRoles();
    }


}
