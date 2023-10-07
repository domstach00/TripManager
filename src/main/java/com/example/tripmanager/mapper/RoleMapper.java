package com.example.tripmanager.mapper;

import com.example.tripmanager.model.user.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {

    default Collection<? extends GrantedAuthority> roleToGrantedAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

    default List<String> roleToString(Collection<Role> roles) {
        return roles.stream()
                .map(Enum::name)
                .toList();
    }
}
