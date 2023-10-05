package com.example.tripmanager.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.tripmanager.model.user.Role;
import com.example.tripmanager.model.user.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class JwtService {
    @Value("${sec.app.jwtSecret}")
    private String jwtSecret;

    @Value("${sec.app.jwtExpirationMs}")
    private int jwtExpirationMs;

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
                .withClaim("authorities", userDto.getRoles().stream().map(Enum::name).toList())
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public Authentication validateToken(String token) {
        return new UsernamePasswordAuthenticationToken(getUserDtoFromJwt(token), null, Collections.emptyList());
    }

    public UserDto getUserDtoFromJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);

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
