package com.Fastlivery_Express.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShipmentQuoteResponseDto {
    private ShipmentDto shipment;
    private DriverDto assignedDriver;
    private Double price;
    private String message;
}
