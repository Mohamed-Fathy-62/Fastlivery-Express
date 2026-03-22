package com.Fastlivery_Express.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @Column(name = "keycloak_id", unique = true, nullable = false)
    private String keycloakId;
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "national_id")
    private String nationalId;

    @Column(name = "role")
    private String role; // ROLE_CUSTOMER, ROLE_DELIVERY, ROLE_ADMIN

    @Column(name = "status")
    private String status; //active, inactive, suspended

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Column(name = "is_verified")
    private Boolean isVerified;

}
