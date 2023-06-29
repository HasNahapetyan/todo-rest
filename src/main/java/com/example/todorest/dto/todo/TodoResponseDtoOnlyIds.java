package com.example.todorest.dto.todo;

import com.example.todorest.entity.Status;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TodoResponseDtoOnlyIds {
    private int id;
    private String title;
    private Status status;
    private int categoryId;
    private int userId;
}
