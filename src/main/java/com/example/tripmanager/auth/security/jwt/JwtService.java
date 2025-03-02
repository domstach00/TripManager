package com.example.tripmanager.auth.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    private final String jwtSecret;
    private final int jwtExpirationMs;
    private final String jwtCookieName;
    private final Algorithm algorithm;

    public JwtService(@Value("${sec.app.jwtSecret}") String jwtSecret,
                      @Value("${sec.app.jwtExpirationMs}") int jwtExpirationMs,
                      @Value("${sec.app.jwtCookieName}") String jwtCookieName) {
        this.jwtSecret = jwtSecret;
        this.jwtExpirationMs = jwtExpirationMs;
        this.jwtCookieName = jwtCookieName;
        this.algorithm = Algorithm.HMAC512(this.jwtSecret);
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(algorithm);
    }

    public String getUsernameFromJWT(String jwtToken) {
        DecodedJWT decodedJWT = JWT.decode(jwtToken);
        return decodedJWT.getSubject();
    }

    public boolean validateToken(String jwtToken) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(jwtToken);
            return true;
        } catch (JWTVerificationException e) {
            log.warn("JWT token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String jwtToken, UserDetailsService userDetailsService) {
          String username = getUsernameFromJWT(jwtToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }

    public String getJwtCookieName() {
        return jwtCookieName;
    }
}
