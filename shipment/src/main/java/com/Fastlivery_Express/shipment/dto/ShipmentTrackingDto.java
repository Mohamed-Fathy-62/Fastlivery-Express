package com.Fastlivery_Express.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ShipmentTrackingDto {
    private String trackingNumber;
    private String status;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private String lastKnownLocation;
    private String originAddress;
    private String destinationAddress;
    private String driverId;
    private String driverName;
    private String driverMobileNumber;
    private String driverCurrentLocation;
    private Double driverCurrentLatitude;
    private Double driverCurrentLongitude;
}
