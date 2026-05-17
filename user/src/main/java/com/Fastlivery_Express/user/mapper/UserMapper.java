package com.Fastlivery_Express.user.mapper;

import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.entity.User;


public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;
        UserDto userDto = new UserDto();

        userDto.setUserId(user.getUserId());
        userDto.setFirstname(user.getFirstname());
        userDto.setLastname(user.getLastname());
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

        if (userDto.getUserId() != null) {
            user.setUserId(userDto.getUserId());
        }
        user.setFirstname(resolveFirstName(userDto));
        user.setLastname(resolveLastName(userDto));
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setProfileImageUrl(userDto.getProfileImageUrl());
        user.setRole(userDto.getRole());
        user.setStatus(userDto.getStatus());
        user.setPreferredLanguage(userDto.getPreferredLanguage());
        user.setIsVerified(userDto.getIsVerified() != null ? userDto.getIsVerified() : Boolean.FALSE);
        return user;
    }

    public static void mapUserFields(UserDto userDto, User user) {
        if (userDto == null || user == null) return;

        if (userDto.getUserId() != null) {
            user.setUserId(userDto.getUserId());
        }
        user.setFirstname(resolveFirstName(userDto));
        user.setLastname(resolveLastName(userDto));
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setProfileImageUrl(userDto.getProfileImageUrl());
        user.setRole(userDto.getRole());
        user.setStatus(userDto.getStatus());
        user.setPreferredLanguage(userDto.getPreferredLanguage());
        user.setIsVerified(userDto.getIsVerified() != null ? userDto.getIsVerified() : Boolean.FALSE);
    }

    private static String resolveFirstName(UserDto userDto) {
        return userDto.getFirstname();
    }

    private static String resolveLastName(UserDto userDto) {
        return userDto.getLastname();
    }

}
