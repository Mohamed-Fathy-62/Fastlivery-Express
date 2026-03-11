package com.Fastlivery_Express.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "users")
@Getter
@Setter
public class UsersContactInfoDto {
    private String message;
    private Map<String, String> contactDetails;
}
