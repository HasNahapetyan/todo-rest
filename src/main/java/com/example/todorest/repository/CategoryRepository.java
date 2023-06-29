package com.example.todorest.repository;

import com.example.todorest.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    <S extends Category> S save(S entity);
    Optional<Category> findById(Integer id);
    List<Category> findAll();
    void deleteById(Integer id);

}
