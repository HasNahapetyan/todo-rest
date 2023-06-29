package com.example.todorest.mapper;

import com.example.todorest.dto.todo.TodoRequestDto;
import com.example.todorest.dto.todo.TodoResponseDto;
import com.example.todorest.dto.todo.TodoResponseDtoOnlyIds;
import com.example.todorest.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TodoMapper {

    @Mapping(target = "category.id", source = "categoryId")
    Todo map(TodoRequestDto dto);

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "userId", source = "user.id")
    TodoResponseDtoOnlyIds mapToDtoWithIds(Todo entity);

    @Mapping(target = "categoryDto", source = "category")
    @Mapping(target = "userDto", source = "user")
    TodoResponseDto mapToDto(Todo entity);

}
