package com.example.todorest.endpoint;

import com.example.todorest.dto.category.CategoryDto;
import com.example.todorest.entity.Category;
import com.example.todorest.mapper.CategoryMapper;
import com.example.todorest.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryEndpoint {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody CategoryDto categoryDto) {
        Optional<Category> byName = categoryService.findByName(categoryDto.getName());
        if (byName.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Category category = categoryMapper.map(categoryDto);
        categoryService.save(category);
        return ResponseEntity.ok(categoryMapper.mapToDto(category));
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAll() {
        List<Category> all = categoryService.findAll();
        if (all.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        List<CategoryDto> categoryDtos = new ArrayList<>();
        for (Category category : all) {
            categoryDtos.add(categoryMapper.mapToDto(category));
        }
        return ResponseEntity.ok(categoryDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable("id") int id) {
        Optional<Category> byId = categoryService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
