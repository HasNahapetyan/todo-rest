package com.example.todorest.service;

import com.example.todorest.entity.Category;
import com.example.todorest.entity.User;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findByName(String name);
    <S extends Category> S save(S entity);
    List<Category> findAll();
    Optional<Category> findById(Integer id);
    void deleteById(Integer id);
}