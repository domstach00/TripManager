package com.example.tripmanager.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.tripmanager.model.user.Role;
import com.example.tripmanager.model.user.UserDto;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.*;

@Service
@Slf4j
public class JwtService {
//    @Value("${sec.app.jwtSecret}")
    private String jwtSecret = "abc";

//    @Value("${sec.app.jwtExpirationMs}")
    private int jwtExpirationMs = 2550000;

//    @Value("${sec.app.jwtCookieName}")
    private String jwtCookie = "abce";

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

    public String getUsernameFromJwt(String token) {
        return getUserDtoFromJwt(token).getUsername();
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        return cookie != null ? cookie.getValue() : null;
    }
//
//    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
//        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
//        return ResponseCookie.from(jwtCookie, jwt)
//                .path("/api")
//                .maxAge(24 * 60 * 60)
//                .httpOnly(true)
//                .build();
//    }
//
    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null)
                .path("/api")
                .build();
    }
//
//    public String getUsernameFromJwtToken(String token) {
//        return Jwts.parserBuilder()
//                .setSigningKey(key())
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//
//    public boolean validateJwtToken(String authToken) {
//        try {
//            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
//            return true;
//        } catch (MalformedJwtException e) {
//            log.error("Invalid JWT token: {}", e.getMessage());
//        } catch (ExpiredJwtException e) {
//            log.error("JWT token is expired: {}", e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            log.error("JWT token is unsupported: {}", e.getMessage());
//        } catch (IllegalArgumentException e) {
//            log.error("JWT claims string is empty: {}", e.getMessage());
//        }
//
//        return false;
//    }
//
//    public String generateTokenFromUsername(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(key(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    private Key key() {
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//    }
}
