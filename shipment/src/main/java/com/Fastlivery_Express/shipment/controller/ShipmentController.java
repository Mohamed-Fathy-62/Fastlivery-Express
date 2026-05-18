package com.Fastlivery_Express.shipment.controller;

import com.Fastlivery_Express.shipment.dto.ShipmentContactInfoDto;
import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.dto.ShipmentQuoteResponseDto;
import com.Fastlivery_Express.shipment.dto.ShipmentRequestDto;
import com.Fastlivery_Express.shipment.dto.ShipmentTrackingDto;
import com.Fastlivery_Express.shipment.dto.StripeCheckoutSessionDto;
import com.Fastlivery_Express.shipment.service.IShipmentService;
import com.Fastlivery_Express.shipment.service.IStripePaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for Shipment in Fastlivery Express",
        description = "CRUD REST APIs in Fastlivery to CREATE, UPDATE, FETCH AND DELETE shipment details"
)
@RestController
@RequestMapping(path = "/api/shipments", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class ShipmentController {

    private final IShipmentService iShipmentService;
    private final IStripePaymentService stripePaymentService;

    @Autowired
    private ShipmentContactInfoDto shipmentContactInfoDto;

    public ShipmentController(IShipmentService iShipmentService, IStripePaymentService stripePaymentService) {
        this.iShipmentService = iShipmentService;
        this.stripePaymentService = stripePaymentService;
    }


    @PostMapping
    public ResponseEntity<ShipmentDto> createShipment(@Valid @RequestBody ShipmentDto shipmentDto) {
        ShipmentDto created = iShipmentService.createShipment(shipmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/request")
    public ResponseEntity<ShipmentQuoteResponseDto> requestShipment(@Valid @RequestBody ShipmentRequestDto shipmentRequestDto) {
        ShipmentQuoteResponseDto quote = iShipmentService.requestShipment(shipmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(quote);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ShipmentDto> confirmShipment(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(iShipmentService.confirmShipment(id));
    }

    @PostMapping("/{id}/payment/checkout")
    public ResponseEntity<StripeCheckoutSessionDto> createStripeCheckoutSession(@Valid @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stripePaymentService.createCheckoutSession(id));
    }

    @PostMapping("/payment/sync")
    public ResponseEntity<ShipmentDto> syncStripeCheckoutSession(@RequestParam("session_id") String checkoutSessionId) {
        return ResponseEntity.ok(stripePaymentService.syncCheckoutSession(checkoutSessionId));
    }

    @PostMapping("/payment/webhook")
    public ResponseEntity<ShipmentDto> handleStripeWebhook(@RequestBody String payload,
                                                           @RequestHeader("Stripe-Signature") String signatureHeader) {
        ShipmentDto updatedShipment = stripePaymentService.handleWebhook(payload, signatureHeader);
        return ResponseEntity.ok(updatedShipment);
    }

    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<ShipmentTrackingDto> trackShipment(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(iShipmentService.trackShipment(trackingNumber));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ShipmentDto>> getAllShipments(@RequestParam("customer_id") String customerId) {
        List<ShipmentDto> shipments = iShipmentService.getAllShipmentsByUserId(customerId);
        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentDto> getShipment(@Valid @PathVariable Long id) {
        ShipmentDto shipmentDto = iShipmentService.getShipmentById(id);
        if (shipmentDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(shipmentDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentDto> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody ShipmentDto shipmentDto) {
        boolean isUpdated = iShipmentService.updateShipment(id, shipmentDto);
        if (!isUpdated) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shipmentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShipment(@Valid @PathVariable Long id) {
        boolean isDeleted = iShipmentService.deleteShipment(id);
        if (!isDeleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/contact-info")
    public ResponseEntity<ShipmentContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(shipmentContactInfoDto);
    }

}
