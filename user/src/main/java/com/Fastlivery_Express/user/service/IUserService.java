package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.UserDto;
import jakarta.validation.Valid;


public interface IUserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(String userId);
    UserDto getUserByEmail(String email);

    boolean updateUser(@Valid String email, @Valid UserDto userDto);

    boolean deleteUser(@Valid String email);
}
