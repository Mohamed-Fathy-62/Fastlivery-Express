package com.Fastlivery_Express.shipment.dto;

import lombok.Data;

@Data
public class DriverDto {
    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private String mobileNumber;
    private String licenseNumber;
    private String vehicleType;
    private String vehicleNumber;
    private Boolean isAvailable;
    private String currentLocation;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double rating;
    private Integer totalDeliveries;
}
