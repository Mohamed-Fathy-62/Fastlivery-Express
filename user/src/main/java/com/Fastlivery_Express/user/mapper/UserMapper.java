package com.Fastlivery_Express.user.mapper;

import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.entity.User;


public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;
        UserDto userDto = new UserDto();

        userDto.setUserId(user.getUserId());
        userDto.setFullName(user.getFullName());
        userDto.setEmail(user.getEmail());
        userDto.setMobileNumber(user.getMobileNumber());
        userDto.setProfileImageUrl(user.getProfileImageUrl());
        userDto.setRole(user.getRole());
        userDto.setStatus(user.getStatus());
        userDto.setPreferredLanguage(user.getPreferredLanguage());
        userDto.setIsVerified(user.getIsVerified());
        return userDto;
    }

    public static User mapToUser(UserDto userDto) {
        if (userDto == null) return null;
        User user = new User();

        user.setUserId(userDto.getUserId() != null ? userDto.getUserId() : 0); //TODO:user id can't be null
        user.setFullName(userDto.getFullName());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setProfileImageUrl(userDto.getProfileImageUrl());
        user.setRole(userDto.getRole());
        user.setStatus(userDto.getStatus());
        user.setPreferredLanguage(userDto.getPreferredLanguage());
        user.setIsVerified(userDto.getIsVerified() != null ? userDto.getIsVerified() : Boolean.FALSE);
        return user;
    }
}
