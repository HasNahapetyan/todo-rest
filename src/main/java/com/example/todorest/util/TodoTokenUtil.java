package com.example.todorest.util;

import com.example.todorest.config.properties.JwtConfigProperties;
import com.example.todorest.config.properties.TodoTokenConfigProperties;
import com.example.todorest.dto.todo.TodoResponseDto;
import com.example.todorest.entity.Todo;
import com.example.todorest.entity.User;
import com.example.todorest.mapper.TodoMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoTokenUtil {
    private final TodoMapper toDoMapper;
    private final TodoTokenConfigProperties todoTokenConfigProperties;

    public String generateToken(User user, List<Todo> todos) {
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        todos.forEach(toDo -> {
            todoResponseDtos.add(toDoMapper.mapToDto(toDo));
        });
        Date expiration = new Date(System.currentTimeMillis() + todoTokenConfigProperties.getExpiration());
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("todos", todoResponseDtos);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, todoTokenConfigProperties.getSecretKey())
                .compact();
    }}
