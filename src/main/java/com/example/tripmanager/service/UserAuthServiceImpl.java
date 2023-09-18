package com.example.tripmanager.service;

import com.example.tripmanager.exception.AccountAlreadyExistsException;
import com.example.tripmanager.exception.WrongCredentialsException;
import com.example.tripmanager.mapper.UserMapper;
import com.example.tripmanager.model.LoginRequest;
import com.example.tripmanager.model.SignupRequest;
import com.example.tripmanager.model.user.Role;
import com.example.tripmanager.model.user.User;
import com.example.tripmanager.model.user.UserDto;
import com.example.tripmanager.repository.UserRepository;
import com.example.tripmanager.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class UserAuthServiceImpl implements UserAuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDto login(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if ( user.isEmpty() || !passwordEncoder.matches(CharBuffer.wrap(loginRequest.getPassword()), user.get().getPassword())) {
            throw new WrongCredentialsException("Wrong credentials");
        }

        UserDto userDto = userMapper.toUserDto(user.get());
        userDto.setToken(jwtService.createToken(userDto));
        return userDto;
    }

    @Override
    public User register(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new AccountAlreadyExistsException("Account with email %s already exists".formatted(signupRequest.getEmail()));
        }

        User newUser = userMapper.signUpToUser(signupRequest);
        setUserPassword(newUser, signupRequest.getPassword());
        newUser.setRoles(Set.of(Role.ROLE_USER));

        return userRepository.save(newUser);
    }

    private void setUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(password)));
    }
}
