package com.Fastlivery_Express.user.dto;

import lombok.Data;


@Data
public class UserDto {

    private Long userId;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String profileImageUrl;
    private String role;
    private String status;
    private String preferredLanguage;
    private Boolean isVerified;

}
