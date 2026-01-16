package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    boolean updateUser(Long id, UserDto userDto);
    boolean deleteUser(Long id);
}
