package com.example.todorest.endpoint;

import com.example.todorest.dto.category.CreateUserRequestDto;
import com.example.todorest.dto.user.UpdateUserDto;
import com.example.todorest.dto.user.UserAuthRequestDto;
import com.example.todorest.dto.user.UserAuthResponseDto;
import com.example.todorest.dto.user.UserDto;
import com.example.todorest.entity.Role;
import com.example.todorest.entity.User;
import com.example.todorest.filter.CurrentUser;
import com.example.todorest.mapper.UserMapper;
import com.example.todorest.service.UserService;
import com.example.todorest.service.impl.JwtServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserEndpoint {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final UserMapper userMapper;

    @PostMapping("/auth")
    public ResponseEntity<UserAuthResponseDto> auth(@RequestBody UserAuthRequestDto userAuthRequestDto) {
        Optional<User> byEmail = userService.findByEmail(userAuthRequestDto.getEmail());
        if (byEmail.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = byEmail.get();
        if (!passwordEncoder.matches(userAuthRequestDto.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new UserAuthResponseDto("Bearer " + token));
    }

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody CreateUserRequestDto createUserRequestDto) {
        Optional<User> byEmail = userService.findByEmail(createUserRequestDto.getEmail());
        if (byEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        User user = userMapper.map(createUserRequestDto);
        user.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));
        user.setRole(Role.USER);
        userService.save(user);
        return ResponseEntity.ok(userMapper.mapToDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable("id") int id) {
        Optional<User> byId = userService.findById(id);
        if (byId.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserDto userDto = userMapper.mapToDto(byId.get());
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") int id) {
        CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (userService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserDto userDto) {
        CurrentUser principal = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> byEmail = userService.findByEmail(userDto.getEmail());
        if (byEmail.isPresent() && byEmail.get().getId() != principal.getId()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        if (userDto.getName() != null && !userDto.getName().isEmpty()) {
            principal.setName(userDto.getName());
        }
        if (userDto.getSurname() != null && !userDto.getSurname().isEmpty()) {
            principal.setName(userDto.getSurname());
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty()) {
            principal.setEmail(userDto.getEmail());
        }
        User updatedUser = userService.save(userMapper.map(principal));
        return ResponseEntity.ok(userMapper.mapToDto(updatedUser));
    }
}
