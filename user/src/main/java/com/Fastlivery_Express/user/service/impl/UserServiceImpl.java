package com.Fastlivery_Express.user.service.impl;

import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.entity.User;
import com.Fastlivery_Express.user.exception.UserAlreadyExistsException;
import com.Fastlivery_Express.user.exception.UserEmailAlreadyUsed;
import com.Fastlivery_Express.user.exception.UserNotFoundException;
import com.Fastlivery_Express.user.mapper.UserMapper;
import com.Fastlivery_Express.user.repository.UserRepository;
import com.Fastlivery_Express.user.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {
    private UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        if(userRepository.findByEmail(userDto.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("User with email " + userDto.getEmail() + " already exists.");
        }
        if(userRepository.findByMobileNumber(userDto.getMobileNumber()).isPresent()){
            throw new UserAlreadyExistsException("User with mobile number " + userDto.getMobileNumber() + " already exists.");
        }
        User user = UserMapper.mapToUser(userDto);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
    }

    @Override
    public boolean updateUser(String email, UserDto userDto) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()){
            User userToUpdate = existingUser.get();
            validateUniqueEmailForUpdate(email, userDto.getEmail());
            validateUniqueMobileForUpdate(userToUpdate.getMobileNumber(), userDto.getMobileNumber());
            UserMapper.mapUserFields(userDto, userToUpdate);
            userRepository.save(userToUpdate);
        }else{
            throw new UserNotFoundException("User with email " + email + " not found.");
        }
        return true;
    }

    @Override
    public UserDto getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found."));
    }


    @Override
    public boolean deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found."));
        userRepository.delete(user);
        return true;
    }

    private void validateUniqueEmailForUpdate(String currentEmail, String newEmail) {
        if (newEmail == null || newEmail.equals(currentEmail)) return;
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new UserEmailAlreadyUsed("User with email " + newEmail + " already exists.");
        }
    }

    private void validateUniqueMobileForUpdate(String currentMobileNumber, String newMobileNumber) {
        if (newMobileNumber == null || newMobileNumber.equals(currentMobileNumber)) return;
        if (userRepository.findByMobileNumber(newMobileNumber).isPresent()) {
            throw new UserAlreadyExistsException("User with mobile number " + newMobileNumber + " already exists.");
        }
    }
}
