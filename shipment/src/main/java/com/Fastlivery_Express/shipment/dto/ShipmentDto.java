package com.Fastlivery_Express.shipment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Schema(
        name = "Shipment",
        description = "Schema to hold Shipment information"
)
public class ShipmentDto {
    private Long shipmentId;
    private String trackingNumber;
    private Long customerId;
    private Long driverId;
    private String originAddress;
    private String destinationAddress;
    private String status;
    private Double totalPrice;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime actualDeliveryTime;
    private String packageDetails;
    private String paymentStatus;
    private String customerFeedback;
    private Integer customerRating;
    private String driverFeedback;
    private Integer driverRating;
    private String cancellationReason;
    private String lastKnownLocation;
    private Double totalWeight;
    private String packageDimensions;

}
