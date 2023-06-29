package com.example.todorest.filter;

import com.example.todorest.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUser {
    private int id;
    private String name;
    private String surname;
    private String email;
    private Role role;
}
