package com.example.todorest.endpoint;

import com.example.todorest.dto.todo.TodoRequestDto;
import com.example.todorest.dto.todo.TodoResponseDto;
import com.example.todorest.dto.todo.TodoResponseDtoOnlyIds;
import com.example.todorest.dto.todo.TodosResponseTokenDto;
import com.example.todorest.entity.Category;
import com.example.todorest.entity.Status;
import com.example.todorest.entity.Todo;
import com.example.todorest.entity.User;
import com.example.todorest.filter.CurrentUser;
import com.example.todorest.mapper.TodoMapper;
import com.example.todorest.mapper.UserMapper;
import com.example.todorest.service.impl.CategoryServiceImpl;
import com.example.todorest.service.impl.TodoServiceImpl;
import com.example.todorest.util.TodoTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class ToDoEndpoint {

    private final TodoServiceImpl toDoService;
    private final CategoryServiceImpl categoryService;
    private final TodoMapper toDoMapper;
    private final UserMapper userMapper;
    private final TodoTokenUtil todoTokenUtil;

    @PostMapping()
    public ResponseEntity<TodoResponseDtoOnlyIds> create(@RequestBody TodoRequestDto todoDto,
                                                         @AuthenticationPrincipal CurrentUser currentUser) {
        Optional<Category> categoryById = categoryService.findById(todoDto.getCategoryId());
        if (categoryById.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Todo todo = toDoMapper.map(todoDto);
        todo.setStatus(Status.NOT_STARTED);
        User user = userMapper.map(currentUser);
        todo.setUser(user);
        toDoService.save(todo);
        return ResponseEntity.ok(toDoMapper.mapToDtoWithIds(todo));
    }

    @GetMapping()
    public ResponseEntity<TodosResponseTokenDto> getAllByUser(@AuthenticationPrincipal CurrentUser currentUser) {
        List<Todo> todos = toDoService.findByUser(userMapper.map(currentUser));
        if (todos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        User user = userMapper.map(currentUser);
        String token = todoTokenUtil.generateToken(user, todos);
        return ResponseEntity.ok(new TodosResponseTokenDto(token));
    }

    @GetMapping("/byStatus")
    public ResponseEntity<?> getAllByStatusAndUser(@RequestParam("status") String statusStr) {
        CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();//
        try {
            Status status = Status.valueOf(statusStr);
            User user = userMapper.map(principal);
            List<Todo> byStatusAndUser = toDoService.findByStatusAndUser(status, user);
            List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
            byStatusAndUser.forEach(toDo -> {
                todoResponseDtos.add(toDoMapper.mapToDto(toDo));
            });
            return ResponseEntity.ok(todoResponseDtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/byCategory")
    public ResponseEntity<?> getAllByCategoryAndUser(@RequestParam("category") String categoryName) {
        Optional<Category> category = categoryService.findByName(categoryName);
        if (category.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Category categoryGet = category.get();
        User user = userMapper.map(principal);
        List<Todo> byCategoryAndUser = toDoService.findByCategoryAndUser(categoryGet, user);
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        byCategoryAndUser.forEach(toDo -> todoResponseDtos.add(toDoMapper.mapToDto(toDo)));
        return ResponseEntity.ok(todoResponseDtos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStatusById(@PathVariable("id") int id, @RequestBody String newStatus) {
        Optional<Todo> todoById = toDoService.findById(id);
        if (todoById.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Todo toDo = todoById.get();
        try {
            toDo.setStatus(Status.valueOf(newStatus));
            return ResponseEntity.ok(toDoMapper.mapToDto(toDoService.save(toDo)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") int id) {
        Optional<Todo> todoById = toDoService.findById(id);
        if (todoById.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Todo toDo = todoById.get();
        CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (toDo.getUser().getId() != principal.getId()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        toDoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
