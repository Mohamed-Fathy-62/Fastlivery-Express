package com.Fastlivery_Express.shipment.service;

import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.dto.StripeCheckoutSessionDto;

public interface IStripePaymentService {
    StripeCheckoutSessionDto createCheckoutSession(Long shipmentId);
    ShipmentDto syncCheckoutSession(String checkoutSessionId);
    ShipmentDto handleWebhook(String payload, String signatureHeader);
}
