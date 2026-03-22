package com.Fastlivery_Express.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Data
@Schema(
        name = "User",
        description = "Schema to hold User information"
)
public class UserDto {
    private Long userId;
    private String firstname;
    private String lastname;
    private String password;
    private String keycloakId;


    @NotEmpty(message = "email can not be a null or empty")
    @Pattern(regexp="(^$|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})",message = "email must be a valid email address")
    @Schema(
            description = "email for user", example = "demo.demo@example.com"
    )
    private String email;
    @NotEmpty(message = "mobile number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{11})",message = "mobile number must be 11 digits")
    @Schema(
            description = "Account Number of Eazy Bank account", example = "3454433243"
    )
    private String mobileNumber;
    private String firstName;
    private String lastName;
    private String profileImageUrl;
    private String role;
    private String status;
    private String preferredLanguage;
    private Boolean isVerified;

}
