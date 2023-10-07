package com.example.tripmanager.mapper;

import com.example.tripmanager.model.SignupRequest;
import com.example.tripmanager.model.user.User;
import com.example.tripmanager.model.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User signUpToUser(SignupRequest signup);
}
