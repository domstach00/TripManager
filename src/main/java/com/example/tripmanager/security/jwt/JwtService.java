package com.example.tripmanager.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.tripmanager.model.account.AccountDto;
import com.example.tripmanager.model.account.Role;
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

    private final String jwtSecret;

    private final int jwtExpirationMs;

    public JwtService(@Value("${sec.app.jwtSecret}") String jwtSecret,
                      @Value("${sec.app.jwtExpirationMs}") int jwtExpirationMs) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    public String createToken(AccountDto accountDto) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtExpirationMs);

        return JWT.create()
                .withIssuer(accountDto.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("id", accountDto.getId())
                .withClaim("username", accountDto.getUsername())
                .withClaim("email", accountDto.getEmail())
                .withClaim("authorities", Role.roleToString(accountDto.getRoles()))
                .sign(Algorithm.HMAC256(this.jwtSecret));
    }

    public Authentication validateToken(String token) {
        AccountDto user = getUserDtoFromJwt(token);
        return new UsernamePasswordAuthenticationToken(user, null, Role.toGrantedAuthorities(user.getRoles()));
    }

    public AccountDto getUserDtoFromJwt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(token);

        return getUserDtoFromJwt(decoded);
    }

    public AccountDto getUserDtoFromJwt(DecodedJWT decoded) {
        return AccountDto.builder()
                .username(decoded.getIssuer())
                .id(decoded.getClaim("id").asString())
                .email(decoded.getClaim("email").asString())
                .roles(Set.copyOf(decoded.getClaim("authorities").asList(Role.class)))
                .build();
    }
}
