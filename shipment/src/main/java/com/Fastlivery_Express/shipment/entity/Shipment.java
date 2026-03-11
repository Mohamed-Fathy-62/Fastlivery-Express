package com.Fastlivery_Express.shipment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Table(name = "shipments")
public class Shipment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "tracking_number", nullable = false)
    private String trackingNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "driver_id", nullable = false)
    private Long driverId;

    @Column(name = "origin_address", nullable = false)
    private String originAddress;

    @Column(name = "destination_address", nullable = false)
    private String destinationAddress;

    @Column(name = "status", nullable = false)
    private String status; // e.g., pending, in_transit, delivered, cancelled

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "estimated_delivery_time")
    private LocalDateTime estimatedDeliveryTime; // e.g., "2024-06-30T18:30:00Z"

    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime; // e.g., "2024-06-30T18:30:00Z"

    @Column(name = "package_details")
    private String packageDetails; // e.g., "2 boxes, fragile"

    @Column(name = "payment_status")
    private String paymentStatus; // e.g., pending, paid, failed

    @Column(name = "customer_feedback")
    private String customerFeedback; // e.g., "Great service!", "Package arrived late."

    @Column(name = "customer_rating")
    private Integer customerRating; // e.g., 1 to 5 stars

    @Column(name = "driver_feedback")
    private String driverFeedback; // e.g., "Customer was not available at delivery time."

    @Column(name = "driver_rating")
    private Integer driverRating; // e.g., 1 to 5 stars

    @Column(name = "cancellation_reason")
    private String cancellationReason; // e.g., "Customer requested cancellation", "Driver unavailable"

    @Column(name = "last_known_location")
    private String lastKnownLocation; // e.g., "Warehouse A", "On the way

    @Column(name = "total_weight")
    private Double totalWeight; // e.g., 5.5 kg

    @Column(name = "package_dimensions")
    private String packageDimensions; // e.g., "30x20x15 cm"

}
