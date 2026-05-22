package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.UserDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IUserService {
    UserDto createUser(UserDto userDto);
    Page<UserDto> searchUsers(String status, String role, Boolean verified, String email, String name, Pageable pageable);
    UserDto getUserById(String userId);
    UserDto getUserByEmail(String email);

    boolean updateUser(@Valid String email, @Valid UserDto userDto);

    boolean deleteUser(@Valid String email);
}
