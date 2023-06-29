package com.example.todorest.repository;

import com.example.todorest.entity.Category;
import com.example.todorest.entity.Status;
import com.example.todorest.entity.Todo;
import com.example.todorest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Integer> {

    <S extends Todo> S save(S entity);
    Optional<Todo> findById(Integer id);
    List<Todo> findByCategoryAndUser(Category category, User user);
    List<Todo> findByStatusAndUser(Status status, User user);
    List<Todo> findByUser(User user);
    void deleteById(Integer id);

}
