package com.Fastlivery_Express.user.controller;

import com.Fastlivery_Express.user.dto.ApiResponse;
import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for User in Fastlivery Express",
        description = "CRUD REST APIs in Fastlivery to CREATE, UPDATE, FETCH AND DELETE account details"
)
@RestController
@RequestMapping(path = "/api/v1/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class UserController {

    private IUserService userService;

    //sign up new user in keycloak and create a new user profile in postgres
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto created = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("User created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> searchUsers(@RequestParam(required = false) String status,
                                                                  @RequestParam(required = false) String role,
                                                                  @RequestParam(required = false) Boolean verified,
                                                                  @RequestParam(required = false) String email,
                                                                  @RequestParam(required = false) String name,
                                                                  @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully",
                userService.searchUsers(status, role, verified, email, name, pageable)));
    }

    //-----------------------------------------------------
    //get user profile by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@Valid @PathVariable String id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", userDto));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<UserDto>> getUserByEmail(@Valid @PathVariable String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", userDto));
    }



    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<UserDto>> updateUser(@Valid @PathVariable String email, @Valid @RequestBody UserDto userDto) {
        boolean isUpdated = userService.updateUser(email, userDto);
        if (!isUpdated) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success("User updated successfully", userDto));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@Valid @PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

}
