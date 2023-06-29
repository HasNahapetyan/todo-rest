package com.example.todorest.service;

import com.example.todorest.entity.User;
import com.example.todorest.repository.UserRepository;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    <S extends User> S save(S entity);
    Optional<User> findById(Integer id);
    void deleteById(Integer id);

}
