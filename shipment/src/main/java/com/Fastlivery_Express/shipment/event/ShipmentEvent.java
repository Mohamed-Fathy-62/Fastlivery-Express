package com.Fastlivery_Express.shipment.event;

import java.time.LocalDateTime;

public record ShipmentEvent(
        String eventType,
        Long shipmentId,
        String trackingNumber,
        String customerId,
        String driverId,
        String status,
        String paymentStatus,
        LocalDateTime occurredAt
) {
}
