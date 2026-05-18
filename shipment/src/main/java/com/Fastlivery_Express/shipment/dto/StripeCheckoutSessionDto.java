package com.Fastlivery_Express.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeCheckoutSessionDto {
    private Long shipmentId;
    private String trackingNumber;
    private String checkoutSessionId;
    private String checkoutUrl;
    private String publishableKey;
    private String paymentStatus;
}
