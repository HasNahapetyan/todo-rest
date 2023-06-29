package com.example.todorest.service;

import com.example.todorest.entity.Category;
import com.example.todorest.entity.Status;
import com.example.todorest.entity.Todo;
import com.example.todorest.entity.User;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    <S extends Todo> S save(S entity);
    Optional<Todo> findById(Integer id);
    List<Todo> findByCategoryAndUser(Category category, User user);
    List<Todo> findByStatusAndUser(Status status, User user);
    List<Todo> findByUser(User user);
    void deleteById(Integer id);

}