package com.Fastlivery_Express.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private String tokenType;
    private long expiresInSeconds;
    private CustomerDto customer;
}
