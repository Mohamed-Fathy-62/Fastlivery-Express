package com.Fastlivery_Express.shipment.controller;

import com.Fastlivery_Express.shipment.dto.ApiResponse;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(path = "/api/v1/shipments", produces = {MediaType.APPLICATION_JSON_VALUE})
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
    public ResponseEntity<ApiResponse<ShipmentDto>> createShipment(@Valid @RequestBody ShipmentDto shipmentDto) {
        ShipmentDto created = iShipmentService.createShipment(shipmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Shipment created successfully", created));
    }

    @PostMapping("/request")
    public ResponseEntity<ApiResponse<ShipmentQuoteResponseDto>> requestShipment(@Valid @RequestBody ShipmentRequestDto shipmentRequestDto) {
        ShipmentQuoteResponseDto quote = iShipmentService.requestShipment(shipmentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Shipment quote created successfully", quote));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<ShipmentDto>> confirmShipment(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Shipment confirmed successfully", iShipmentService.confirmShipment(id)));
    }

    @PostMapping("/{id}/payment/checkout")
    public ResponseEntity<ApiResponse<StripeCheckoutSessionDto>> createStripeCheckoutSession(@Valid @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Stripe checkout session created successfully",
                stripePaymentService.createCheckoutSession(id)));
    }

    @PostMapping("/payment/sync")
    public ResponseEntity<ApiResponse<ShipmentDto>> syncStripeCheckoutSession(@RequestParam("session_id") String checkoutSessionId) {
        return ResponseEntity.ok(ApiResponse.success("Stripe checkout session synced successfully",
                stripePaymentService.syncCheckoutSession(checkoutSessionId)));
    }

    @PostMapping("/payment/webhook")
    public ResponseEntity<ApiResponse<ShipmentDto>> handleStripeWebhook(@RequestBody String payload,
                                                                        @RequestHeader("Stripe-Signature") String signatureHeader) {
        ShipmentDto updatedShipment = stripePaymentService.handleWebhook(payload, signatureHeader);
        return ResponseEntity.ok(ApiResponse.success("Stripe webhook handled successfully", updatedShipment));
    }

    @GetMapping("/track/{trackingNumber}")
    public ResponseEntity<ApiResponse<ShipmentTrackingDto>> trackShipment(@PathVariable String trackingNumber) {
        return ResponseEntity.ok(ApiResponse.success("Shipment tracking fetched successfully",
                iShipmentService.trackShipment(trackingNumber)));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ShipmentDto>>> getAllShipments(@RequestParam("customer_id") String customerId) {
        List<ShipmentDto> shipments = iShipmentService.getAllShipmentsByUserId(customerId);
        return ResponseEntity.ok(ApiResponse.success("Shipments fetched successfully", shipments));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ShipmentDto>>> searchShipments(@RequestParam(required = false) String customerId,
                                                                          @RequestParam(required = false) String driverId,
                                                                          @RequestParam(required = false) String status,
                                                                          @RequestParam(required = false) String paymentStatus,
                                                                          @RequestParam(required = false) String trackingNumber,
                                                                          @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Shipments fetched successfully",
                iShipmentService.searchShipments(customerId, driverId, status, paymentStatus, trackingNumber, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipmentDto>> getShipment(@Valid @PathVariable Long id) {
        ShipmentDto shipmentDto = iShipmentService.getShipmentById(id);
        if (shipmentDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ApiResponse.success("Shipment fetched successfully", shipmentDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShipmentDto>> updateUser(@Valid @PathVariable Long id, @Valid @RequestBody ShipmentDto shipmentDto) {
        boolean isUpdated = iShipmentService.updateShipment(id, shipmentDto);
        if (!isUpdated) return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success("Shipment updated successfully", shipmentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShipment(@Valid @PathVariable Long id) {
        boolean isDeleted = iShipmentService.deleteShipment(id);
        if (!isDeleted) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ApiResponse.success("Shipment deleted successfully", null));
    }

    @GetMapping("/contact-info")
    public ResponseEntity<ApiResponse<ShipmentContactInfoDto>> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Shipment contact info fetched successfully", shipmentContactInfoDto));
    }

}
