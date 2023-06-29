package com.example.todorest.mapper;

import com.example.todorest.dto.category.CategoryDto;
import com.example.todorest.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category map(CategoryDto dto);
    CategoryDto mapToDto(Category entity);
}
