package com.example.todorest.service.impl;

import com.example.todorest.entity.Category;
import com.example.todorest.entity.Status;
import com.example.todorest.entity.Todo;
import com.example.todorest.entity.User;
import com.example.todorest.repository.TodoRepository;
import com.example.todorest.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {
    private final TodoRepository toDoRepository;

    @Override
    public <S extends Todo> S save(S entity){
        return toDoRepository.save(entity);
    }

    @Override
    public Optional<Todo> findById(Integer id){
        return toDoRepository.findById(id);
    }

    @Override
    public List<Todo> findByCategoryAndUser(Category category, User user) {
        return toDoRepository.findByCategoryAndUser(category,user);
    }

    public List<Todo> findByStatusAndUser(Status status, User user){
        return toDoRepository.findByStatusAndUser(status,user);
    }

    @Override
    public List<Todo> findByUser(User user) {
        return toDoRepository.findByUser(user);
    }

    @Override
    public void deleteById(Integer id) {
        toDoRepository.deleteById(id);
    }
}
