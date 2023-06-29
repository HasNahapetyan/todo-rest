package com.example.todorest.dto.todo;

import com.example.todorest.dto.category.CategoryDto;
import com.example.todorest.dto.user.UserDto;
import com.example.todorest.entity.Status;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TodoResponseDto {
    private int id;
    private String title;
    private Status status;
    private CategoryDto categoryDto;
    private UserDto userDto;
}
