package com.Fastlivery_Express.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerRegistrationDto {
    @NotBlank
    @Size(max = 80)
    private String firstname;

    @NotBlank
    @Size(max = 80)
    private String lastname;

    @NotBlank
    @Email
    @Size(max = 254)
    private String email;

    @NotBlank
    @Pattern(regexp = "[0-9]{11}", message = "mobile number must be 11 digits")
    private String mobileNumber;

    @NotBlank
    @Size(min = 8, max = 72, message = "password must be between 8 and 72 characters")
    private String password;

    @Size(max = 500)
    private String deliveryAddress;

    @Size(max = 20)
    private String preferredLanguage;
}
