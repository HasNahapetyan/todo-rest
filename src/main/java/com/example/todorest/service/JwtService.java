package com.example.todorest.service;

import com.example.todorest.entity.User;

public interface JwtService {
    String generateToken(User user);
}