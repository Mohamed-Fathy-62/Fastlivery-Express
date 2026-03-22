package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.UserDto;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.Optional;

public interface IUserService {
    UserDto createUser(UserDto userDto);
    Optional<UserDto> getUserById(String id);
    Optional<UserDto> getUserByEmail(String email);
//    boolean updateUser(Long id, UserDto userDto);
//    boolean deleteUser(Long id);
//    List<ShipmentDto> getAllShipmentsByUserId(Long userId);
}
