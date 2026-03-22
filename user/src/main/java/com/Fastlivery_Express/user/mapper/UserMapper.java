package com.Fastlivery_Express.user.mapper;

import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.entity.User;
import org.keycloak.representations.idm.UserRepresentation;


public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        if (user == null) return null;
        UserDto userDto = new UserDto();

        userDto.setKeycloakId(user.getKeycloakId());
        userDto.setFirstname(userDto.getFirstname());
        userDto.setLastname(userDto.getLastname());
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

        user.setKeycloakId(userDto.getKeycloakId());
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setMobileNumber(userDto.getMobileNumber());
        user.setProfileImageUrl(userDto.getProfileImageUrl());
        user.setRole(userDto.getRole());
        user.setStatus(userDto.getStatus());
        user.setPreferredLanguage(userDto.getPreferredLanguage());
        user.setIsVerified(userDto.getIsVerified() != null ? userDto.getIsVerified() : Boolean.FALSE);
        return user;
    }

    public static UserRepresentation mapToUserRepresentation(UserDto userDto, UserRepresentation userRepresentation) {
        if (userDto == null || userRepresentation == null) return null;

        userRepresentation.setEmail(userDto.getEmail());
        userRepresentation.setFirstName(userDto.getFirstname());
        userRepresentation.setLastName(userDto.getLastname());
        userRepresentation.setUsername(userDto.getEmail());
        userRepresentation.setEnabled(true);
        return userRepresentation;
    }
}
