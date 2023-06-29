package com.example.todorest.dto.todo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TodoRequestDto {
    private String title;
    private int categoryId;
}
