package com.Fastlivery_Express.user.controller;

import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "/api/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class UserController {

    private final IUserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto created = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@Valid @PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        if (userDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody UserDto userDto) {
        boolean isUpdated = userService.updateUser(id, userDto);
        if (!isUpdated) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@Valid @PathVariable Long id) {
        boolean isDeleted = userService.deleteUser(id);
        if (!isDeleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

}
