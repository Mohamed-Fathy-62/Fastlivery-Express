package com.Fastlivery_Express.shipment.service.impl;

import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.dto.StripeCheckoutSessionDto;
import com.Fastlivery_Express.shipment.entity.Shipment;
import com.Fastlivery_Express.shipment.exception.ShipmentNotFoundException;
import com.Fastlivery_Express.shipment.mapper.ShipmentMapper;
import com.Fastlivery_Express.shipment.repository.ShipmentRepository;
import com.Fastlivery_Express.shipment.service.IStripePaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Transactional
public class StripePaymentServiceImpl implements IStripePaymentService {

    private static final String PAYMENT_PENDING = "PENDING";
    private static final String PAYMENT_PAID = "PAID";
    private static final String PAYMENT_FAILED = "FAILED";

    private final ShipmentRepository shipmentRepository;

    @Value("${stripe.secret-key:}")
    private String stripeSecretKey;

    @Value("${stripe.publishable-key:}")
    private String stripePublishableKey;

    @Value("${stripe.webhook-secret:}")
    private String stripeWebhookSecret;

    @Value("${stripe.currency:egp}")
    private String currency;

    @Value("${stripe.success-url}")
    private String successUrl;

    @Value("${stripe.cancel-url}")
    private String cancelUrl;

    @PostConstruct
    public void initStripe() {
        if (stripeSecretKey != null && !stripeSecretKey.isBlank()) {
            Stripe.apiKey = stripeSecretKey;
        }
    }

    @Override
    public StripeCheckoutSessionDto createCheckoutSession(Long shipmentId) {
        ensureStripeConfigured();
        Shipment shipment = findShipment(shipmentId);

        if ("PAID".equalsIgnoreCase(shipment.getPaymentStatus())) {
            throw new IllegalStateException("Shipment with id " + shipmentId + " is already paid.");
        }
        if (shipment.getTotalPrice() == null || shipment.getTotalPrice() <= 0) {
            throw new IllegalStateException("Shipment with id " + shipmentId + " does not have a valid total price.");
        }

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setClientReferenceId(String.valueOf(shipment.getId()))
                    .setSuccessUrl(successUrl)
                    .setCancelUrl(cancelUrl)
                    .putMetadata("shipmentId", String.valueOf(shipment.getId()))
                    .putMetadata("trackingNumber", shipment.getTrackingNumber())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setQuantity(1L)
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(currency.toLowerCase())
                                                    .setUnitAmount(toMinorUnits(shipment.getTotalPrice()))
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Fastlivery shipment " + shipment.getTrackingNumber())
                                                                    .setDescription(shipment.getOriginAddress() + " to " + shipment.getDestinationAddress())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .build()
                    )
                    .build();

            Session checkoutSession = Session.create(params);
            shipment.setStripeCheckoutSessionId(checkoutSession.getId());
            shipment.setStripePaymentIntentId(checkoutSession.getPaymentIntent());
            shipment.setPaymentStatus(PAYMENT_PENDING);
            shipmentRepository.save(shipment);

            return new StripeCheckoutSessionDto(
                    shipment.getId(),
                    shipment.getTrackingNumber(),
                    checkoutSession.getId(),
                    checkoutSession.getUrl(),
                    stripePublishableKey,
                    shipment.getPaymentStatus()
            );
        } catch (StripeException exception) {
            throw new IllegalStateException("Unable to create Stripe checkout session: " + exception.getMessage(), exception);
        }
    }

    @Override
    public ShipmentDto syncCheckoutSession(String checkoutSessionId) {
        ensureStripeConfigured();
        try {
            Session checkoutSession = Session.retrieve(checkoutSessionId);
            Shipment shipment = shipmentRepository.findByStripeCheckoutSessionId(checkoutSessionId)
                    .orElseThrow(() -> new ShipmentNotFoundException("Shipment with Stripe checkout session " + checkoutSessionId + " not found."));
            updatePaymentStatusFromSession(shipment, checkoutSession);
            return ShipmentMapper.mapToShipmentDto(shipmentRepository.save(shipment));
        } catch (StripeException exception) {
            throw new IllegalStateException("Unable to retrieve Stripe checkout session: " + exception.getMessage(), exception);
        }
    }

    @Override
    public ShipmentDto handleWebhook(String payload, String signatureHeader) {
        ensureStripeConfigured();
        if (stripeWebhookSecret == null || stripeWebhookSecret.isBlank()) {
            throw new IllegalStateException("Stripe webhook secret is not configured.");
        }

        try {
            Event event = Webhook.constructEvent(payload, signatureHeader, stripeWebhookSecret);
            if (!event.getType().startsWith("checkout.session.")) {
                return null;
            }

            Session checkoutSession = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new IllegalStateException("Unable to deserialize Stripe checkout session event."));

            Shipment shipment = shipmentRepository.findByStripeCheckoutSessionId(checkoutSession.getId())
                    .orElseThrow(() -> new ShipmentNotFoundException("Shipment with Stripe checkout session " + checkoutSession.getId() + " not found."));
            updatePaymentStatusFromSession(shipment, checkoutSession);
            return ShipmentMapper.mapToShipmentDto(shipmentRepository.save(shipment));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to handle Stripe webhook: " + exception.getMessage(), exception);
        }
    }

    private Shipment findShipment(Long shipmentId) {
        return shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + shipmentId + " not found."));
    }

    private void updatePaymentStatusFromSession(Shipment shipment, Session checkoutSession) {
        shipment.setStripeCheckoutSessionId(checkoutSession.getId());
        shipment.setStripePaymentIntentId(checkoutSession.getPaymentIntent());

        if ("paid".equalsIgnoreCase(checkoutSession.getPaymentStatus())) {
            shipment.setPaymentStatus(PAYMENT_PAID);
        } else if ("expired".equalsIgnoreCase(checkoutSession.getStatus())) {
            shipment.setPaymentStatus(PAYMENT_FAILED);
        } else {
            shipment.setPaymentStatus(PAYMENT_PENDING);
        }
    }

    private Long toMinorUnits(Double amount) {
        return BigDecimal.valueOf(amount)
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();
    }

    private void ensureStripeConfigured() {
        if (stripeSecretKey == null || stripeSecretKey.isBlank()) {
            throw new IllegalStateException("Stripe secret key is not configured. Set STRIPE_SECRET_KEY.");
        }
    }
}
