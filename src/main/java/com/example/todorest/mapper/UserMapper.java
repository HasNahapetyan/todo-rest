package com.example.todorest.mapper;

import com.example.todorest.dto.category.CreateUserRequestDto;
import com.example.todorest.dto.user.UserDto;
import com.example.todorest.entity.User;
import com.example.todorest.filter.CurrentUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User map(CreateUserRequestDto dto);
    User map(CurrentUser currentUser);
    UserDto mapToDto(User entity);

}
