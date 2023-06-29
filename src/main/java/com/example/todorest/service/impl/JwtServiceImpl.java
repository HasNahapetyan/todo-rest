package com.example.todorest.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.todorest.config.properties.JwtConfigProperties;
import com.example.todorest.entity.User;
import com.example.todorest.service.JwtService;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private static final String USER_ID = "userId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ROLE = "role";
    private final JwtConfigProperties jwtConfigProperties;

    public String generateToken(User user) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfigProperties.getSecretKey());
        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim(USER_ID, user.getId())
                .withClaim(FIRST_NAME, user.getName())
                .withClaim(LAST_NAME, user.getSurname())
                .withClaim(ROLE, user.getRole().toString())
                .withIssuer(jwtConfigProperties.getIssuer())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(jwtConfigProperties.getExpiration(), ChronoUnit.MINUTES))
                .sign(Algorithm.HMAC256(keyBytes));
    }
    public JWTVerifier jwtVerifier() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfigProperties.getSecretKey());
        return JWT.require(Algorithm.HMAC256(keyBytes)).build();
    }
}