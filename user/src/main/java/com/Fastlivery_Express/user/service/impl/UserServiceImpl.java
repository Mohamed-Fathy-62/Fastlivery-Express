package com.Fastlivery_Express.user.service.impl;

import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.entity.User;
import com.Fastlivery_Express.user.mapper.UserMapper;
import com.Fastlivery_Express.user.repository.UserRepository;
import com.Fastlivery_Express.user.service.IUserService;
import com.Fastlivery_Express.user.service.clients.ShipmentsFeignClient;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
//TODO : remove the hardcoded realm name and move it to application properties
    private UserRepository userRepository;
    private ShipmentsFeignClient shipmentsFeignClient;

    private Keycloak keycloak;

    @Override
    public UserDto createUser(UserDto userDto) {

        //TODO
        //implement a "Transaction" logic
        // If the Postgres save fails, you should "roll back" by deleting the user from Keycloak
        // so you don't end up with "ghost" users who have an identity but no profile.
        UserRepresentation newUser = UserMapper.mapToUserRepresentation(userDto, new UserRepresentation());
        UsersResource users = keycloak.realm("fastlivery-express").users();
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userDto.getPassword());
        credential.setTemporary(false);
        newUser.setCredentials(List.of(credential));
        newUser.setEmailVerified(true);
        newUser.setRealmRoles(Collections.singletonList("ROLE_" + userDto.getRole().toUpperCase()));

        var response = users.create(newUser);
        int status = response.getStatus();
        if(status == 201) {
            //get keycloak id from the location header of the response
            String location = response.getLocation().toString();
            String userId = location.substring(location.lastIndexOf('/') + 1);
            System.out.println("Created user with ID = " + userId);
            //save user to local database
            userDto.setKeycloakId(userId);
            User user = UserMapper.mapToUser(userDto);
            userRepository.save(user);
            return userDto;
        } else if(status == 409) {
            String body = response.readEntity(String.class);
            throw new IllegalStateException("User already exists: " + body);
        } else {
            String body = response.readEntity(String.class);
            throw new IllegalStateException("User create failed (" + status + "): " + body);
        }
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        //get user from keycloak database to check if he registered with an email
        Optional<UserRepresentation> newUser = keycloak.realm("fastlivery-express")
                .users()
                .searchByEmail(email,true)
                .stream()
                .findFirst();

        UserDto userDto;
        if(newUser.isPresent()){
            Optional<User> existedUser = userRepository.findByEmail(newUser.get().getEmail());
            if(existedUser.isPresent()) {
                userDto = UserMapper.mapToUserDto(existedUser.get());
                userDto.setKeycloakId(newUser.get().getId());
                return Optional.of(userDto);
            }else{
                return Optional.empty();
            }
        }else{
            return Optional.empty();
        }
    }
    @Override
    public Optional<UserDto> getUserById(String keycloakId) {
        UserRepresentation newUser = keycloak.realm("fastlivery-express")
                .users()
                .get(keycloakId) // Direct ID lookup
                .toRepresentation(); // This will throw a 404 if not found

        UserDto userDto;
        Optional<User> existedUser = userRepository.findByKeycloakId(newUser.getId());
        if(existedUser.isPresent()) {
            userDto = UserMapper.mapToUserDto(existedUser.get());
            userDto.setKeycloakId(newUser.getId());
            return Optional.of(userDto);
        }
        return Optional.empty();
    }



//    @Override
//    public boolean updateUser(Long id, UserDto userDto) {
//        boolean isUpdated = false;
//        Optional<User> existingUser = userRepository.findById(id);
//        if(existingUser.isPresent()){
//            User userToUpdate = existingUser.get();
//            userToUpdate.setFirstName(userDto.getFirstname());
//            userToUpdate.setLastName(userDto.getLastname());
//            userToUpdate.setEmail(userDto.getEmail());
//            userToUpdate.setMobileNumber(userDto.getMobileNumber());
//            userToUpdate.setProfileImageUrl(userDto.getProfileImageUrl());
//            userToUpdate.setRole(userDto.getRole());
//            userToUpdate.setStatus(userDto.getStatus());
//            userToUpdate.setPreferredLanguage(userDto.getPreferredLanguage());
//            userToUpdate.setIsVerified(userDto.getIsVerified());
//            userRepository.save(userToUpdate);
//            isUpdated = true;
//        }else{
//            throw new UserNotFoundException("User with id " + id + " not found.");
//        }
//        return isUpdated;
//    }
//
//    @Override
//    public boolean deleteUser(Long id) {
//        return userRepository.findById(id).map(user -> {
//            userRepository.delete(user);
//            return true;
//        }).orElse(false);
//    }
//
//    @Override
//    public List<ShipmentDto> getAllShipmentsByUserId(Long userId) {
//        return shipmentsFeignClient.getAllShipments(userId).getBody();
//    }
}
