package com.example.tripmanager.service;

import com.example.tripmanager.exception.WrongCredentialsException;
import com.example.tripmanager.model.user.Role;
import com.example.tripmanager.model.user.User;
import com.example.tripmanager.model.user.UserDto;
import com.example.tripmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
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

    @Override
    public User getUserFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new WrongCredentialsException("Principal not exists");
        }
        UserDto userDto = (UserDto) principal;
        return userRepository.findByUsername(userDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public UserDto getCurrentUserDto() {
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public UserDto getUserDtoFromPrincipal(Principal principal) {
        return (UserDto) principal;
    }

    @Override
    public Set<Role> getCurrentUserRoles() {
        return ((UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getRoles();
    }


}
