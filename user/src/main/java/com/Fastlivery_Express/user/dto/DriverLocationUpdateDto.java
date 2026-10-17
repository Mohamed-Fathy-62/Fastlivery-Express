package com.Fastlivery_Express.user.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DriverLocationUpdateDto {

    @NotBlank(message = "currentLocation can not be null or empty")
    private String currentLocation;

    @NotNull(message = "currentLatitude can not be null")
    @DecimalMin(value = "-90.0", message = "currentLatitude must be at least -90")
    @DecimalMax(value = "90.0", message = "currentLatitude must be at most 90")
    private Double currentLatitude;

    @NotNull(message = "currentLongitude can not be null")
    @DecimalMin(value = "-180.0", message = "currentLongitude must be at least -180")
    @DecimalMax(value = "180.0", message = "currentLongitude must be at most 180")
    private Double currentLongitude;
}
