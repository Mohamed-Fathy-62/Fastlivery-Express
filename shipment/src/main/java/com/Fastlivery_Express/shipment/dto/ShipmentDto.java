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
    private Long id;
    private String trackingNumber;
    private String customerId;
    private String driverId;
    private String originAddress;
    private String destinationAddress;
    private Double originLatitude;
    private Double originLongitude;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private String status;
    private Double totalPrice;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime quoteExpiresAt;
    private LocalDateTime actualDeliveryTime;
    private String packageDetails;
    private String paymentStatus;
    private String stripeCheckoutSessionId;
    private String stripePaymentIntentId;
    private String customerFeedback;
    private Integer customerRating;
    private String driverFeedback;
    private Integer driverRating;
    private String cancellationReason;
    private String lastKnownLocation;
    private Double totalWeight;
    private String packageDimensions;

}
