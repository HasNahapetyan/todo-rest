package com.example.todorest.config;

import com.example.todorest.filter.JWTAuthenticationTokenFilter;
import com.example.todorest.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//aranc sessioni lini
                .and()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user/auth").permitAll()
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers(HttpMethod.GET, "/user/id").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/user/id").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/user").authenticated()
                        .requestMatchers("/categories/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/todo/**").authenticated()
                        .anyRequest().authenticated())
                .httpBasic();
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
