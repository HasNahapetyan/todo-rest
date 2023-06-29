package com.example.todorest.repository;

import com.example.todorest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    <S extends User> S save(S entity);
    Optional<User> findById(Integer id);
    void deleteById(Integer id);
}
