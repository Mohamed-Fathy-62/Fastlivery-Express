package com.Fastlivery_Express.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "drivers")
public class Driver extends User{
    @Column(name = "license_number")
    private String licenseNumber;
    @Column(name = "vehicle_plate_number")
    private String vehiclePlateNumber;
    @Column(name = "vehicle_type")
    private String vehicleType;
    @Column(name = "is_available")
    private Boolean isAvailable;
    @Column(name = "current_location")
    private String currentLocation;
    @Column(name = "current_latitude")
    private Double currentLatitude;
    @Column(name = "current_longitude")
    private Double currentLongitude;
    @Column(name = "rating")
    private Double rating;
    @Column(name = "total_deliveries")
    private Integer totalDeliveries;
    @Column(name = "total_distance_traveled")
    private Double totalDistanceTraveled;
    @Column(name = "last_active_time")
    private String lastActiveTime;
}
