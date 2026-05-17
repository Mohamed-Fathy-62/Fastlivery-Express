package com.Fastlivery_Express.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(
        name = "Driver",
        description = "Schema to hold Driver information")
public class DriverDto extends UserDto{

    private String licenseNumber;
    private String vehicleType;
    private String vehicleNumber;
    private Boolean isAvailable;
    private String currentLocation;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double rating;
    private Integer totalDeliveries;
    private Double totalDistanceTraveled;
    private String lastActiveTime;
}
