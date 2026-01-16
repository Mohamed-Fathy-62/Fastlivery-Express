package com.Fastlivery_Express.user.service.impl;

import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.entity.User;
import com.Fastlivery_Express.user.exception.UserAlreadyExistsException;
import com.Fastlivery_Express.user.exception.UserNotFoundException;
import com.Fastlivery_Express.user.mapper.UserMapper;
import com.Fastlivery_Express.user.repository.UserRepository;
import com.Fastlivery_Express.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;


    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if(existingUser.isPresent()) System.out.println("Existing User ID: " + existingUser.get().getUserId());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("The email " + user.getEmail() + " is already registered.");
        } else {
            user.setUserId(1000L);
            User savedUser = userRepository.save(user);
            return userDto;
        }
    }

    @Override
    public UserDto getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return UserMapper.mapToUserDto(user.get());
        }else{
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
    }


    @Override
    public boolean updateUser(Long id, UserDto userDto) {
        boolean isUpdated = false;
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isPresent()){
            User userToUpdate = existingUser.get();
            userToUpdate.setFullName(userDto.getFullName());
            userToUpdate.setEmail(userDto.getEmail());
            userToUpdate.setMobileNumber(userDto.getMobileNumber());
            userToUpdate.setProfileImageUrl(userDto.getProfileImageUrl());
            userToUpdate.setRole(userDto.getRole());
            userToUpdate.setStatus(userDto.getStatus());
            userToUpdate.setPreferredLanguage(userDto.getPreferredLanguage());
            userToUpdate.setIsVerified(userDto.getIsVerified());
            userRepository.save(userToUpdate);
            isUpdated = true;
        }else{
            throw new UserNotFoundException("User with id " + id + " not found.");
        }
        return isUpdated;
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.findById(id).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }
}
