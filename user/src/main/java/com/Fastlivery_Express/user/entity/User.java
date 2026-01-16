package com.Fastlivery_Express.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Table(name = "test_user")
public class User extends BaseEntity {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO) //ToDo: change userId generation logic don't depend on the db generation
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "role")
    private String role; // e.g., customer, admin

    @Column(name = "status")
    private String status; // e.g., active, inactive, suspended

    @Column(name = "preferred_language")
    private String preferredLanguage;

    @Column(name = "is_verified")
    private Boolean isVerified;

}
