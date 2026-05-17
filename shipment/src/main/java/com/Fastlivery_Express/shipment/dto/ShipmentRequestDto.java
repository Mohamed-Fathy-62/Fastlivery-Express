package com.Fastlivery_Express.shipment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShipmentRequestDto {
    @NotBlank(message = "customerId can not be null or empty")
    private String customerId;
    @NotBlank(message = "originAddress can not be null or empty")
    private String originAddress;
    @NotBlank(message = "destinationAddress can not be null or empty")
    private String destinationAddress;
    @NotNull(message = "originLatitude can not be null")
    private Double originLatitude;
    @NotNull(message = "originLongitude can not be null")
    private Double originLongitude;
    @NotNull(message = "destinationLatitude can not be null")
    private Double destinationLatitude;
    @NotNull(message = "destinationLongitude can not be null")
    private Double destinationLongitude;
    @NotNull(message = "totalWeight can not be null")
    private Double totalWeight;
    private String packageDetails;
    private String packageDimensions;
}
