package com.Fastlivery_Express.user.controller;

import com.Fastlivery_Express.user.dto.UserDto;
import com.Fastlivery_Express.user.service.IUserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(
        name = "CRUD REST APIs for User in Fastlivery Express",
        description = "CRUD REST APIs in Fastlivery to CREATE, UPDATE, FETCH AND DELETE account details"
)
@RestController
@RequestMapping(path = "/api/users", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class UserController {

    private IUserService userService;

    //sign up new user in keycloak and create a new user profile in postgres
    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto, @AuthenticationPrincipal Jwt jwt) {
        UserDto created = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    //for debug-------------------------------------------
//    @GetMapping
//    public ResponseEntity<UserDto> getUser() {
//        return ResponseEntity.ok(null);
//    }
    @GetMapping("/me")
    public ResponseEntity<?> debug(@RequestHeader HttpHeaders headers) {
        headers.forEach((key, value) -> System.out.println(key + ": " + value));
        return ResponseEntity.ok().build();
    }
    //-----------------------------------------------------
    //get user profile by id
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@Valid @PathVariable String id) {
        Optional<UserDto> userDto = userService.getUserById(id);
//        return ResponseEntity.ok(null);
        if (userDto.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userDto.get());
    }


//    @PutMapping("/{id}")
//    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody UserDto userDto) {
//        boolean isUpdated = userService.updateUser(id, userDto);
//        if (!isUpdated) return ResponseEntity.notFound().build();
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDto);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteUser(@Valid @PathVariable Long id) {
//        boolean isDeleted = userService.deleteUser(id);
//        if (!isDeleted) return ResponseEntity.notFound().build();
//        return ResponseEntity.noContent().build();
//    }
//
//
//    @GetMapping("/contact-info")
//    public ResponseEntity<UsersContactInfoDto> getContactInfo() {
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(usersContactInfoDto);
//    }
//    @GetMapping("/shipments/getAllShipmentsByUserId")
//    public ResponseEntity<List<ShipmentDto>> getAllShipmentsByUserId(/*@RequestParam("customer_id") Long customerId*/) {
//        List<ShipmentDto> shipments = userService.getAllShipmentsByUserId(1L);
//        return ResponseEntity.ok(shipments);
//    }
}
