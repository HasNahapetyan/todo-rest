package com.example.todorest.filter;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.todorest.entity.Role;
import com.example.todorest.service.impl.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationTokenFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private static final String USER_ID = "userId";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String ROLE = "role";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");
        if (jwt == null || !jwt.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = jwt.replace("Bearer ", "");
        try {
            JWTVerifier jwtVerifier = jwtService.jwtVerifier();
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            CurrentUser currentUser = createCurrentUser(decodedJWT);
            Claim authority = decodedJWT.getClaim(ROLE);
            List<String> authoritiesInString = new ArrayList<>();
            authoritiesInString.add(authority.asString());
            List<SimpleGrantedAuthority> authorities = authoritiesInString.stream().map(SimpleGrantedAuthority::new).toList();
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            currentUser, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        catch (JWTVerificationException ex) {
            logger.error("--------------token validation exception--------------");
        }
        filterChain.doFilter(request, response);
    }

    private CurrentUser createCurrentUser(DecodedJWT decodedJWT) {
        return CurrentUser.builder()
                .email(decodedJWT.getSubject())
                .id(Integer.parseInt(String.valueOf(decodedJWT.getClaim(USER_ID))))
                .name(decodedJWT.getClaim(FIRST_NAME).asString())
                .surname(decodedJWT.getClaim(LAST_NAME).asString())
                .role(Role.valueOf(decodedJWT.getClaim(ROLE).asString()))
                .build();
    }
}