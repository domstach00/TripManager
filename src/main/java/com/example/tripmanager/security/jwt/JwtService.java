package com.example.tripmanager.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.tripmanager.mapper.RoleMapper;
import com.example.tripmanager.model.user.Role;
import com.example.tripmanager.model.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JwtService {
    private final RoleMapper roleMapper;

    private final String jwtSecret;

    private final int jwtExpirationMs;

    public JwtService(@Autowired RoleMapper roleMapper,
                      @Value("${sec.app.jwtSecret}") String jwtSecret,
                      @Value("${sec.app.jwtExpirationMs}") int jwtExpirationMs) {
        this.roleMapper = roleMapper;
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String createToken(UserDto userDto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationMs);

        return JWT.create()
                .withIssuer(userDto.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("id", userDto.getId())
                .withClaim("username", userDto.getUsername())
                .withClaim("email", userDto.getEmail())
                .withClaim("authorities", roleMapper.roleToString(userDto.getRoles()))
                .sign(Algorithm.HMAC256(this.jwtSecret));
    }

    public Authentication validateToken(String token) {
        UserDto user = getUserDtoFromJwt(token);
        return new UsernamePasswordAuthenticationToken(user, null, roleMapper.roleToGrantedAuthorities(user.getRoles()));
    }

    public UserDto getUserDtoFromJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        return getUserDtoFromJwt(decoded);
    }

    public UserDto getUserDtoFromJwt(DecodedJWT decoded) {
        return UserDto.builder()
                .username(decoded.getIssuer())
                .id(decoded.getClaim("id").asString())
                .email(decoded.getClaim("email").asString())
                .roles(Set.copyOf(decoded.getClaim("authorities").asList(Role.class)))
                .build();
    }
}
