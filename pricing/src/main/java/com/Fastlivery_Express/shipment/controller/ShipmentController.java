package com.Fastlivery_Express.shipment.controller;

import com.Fastlivery_Express.shipment.dto.PricingContactInfoDto;
import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.service.IShipmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Shipment in Fastlivery Express",
        description = "CRUD REST APIs in Fastlivery to CREATE, UPDATE, FETCH AND DELETE shipment details"
)
@RestController
@RequestMapping(path = "/api/shipments", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class ShipmentController {

    private final IShipmentService iShipmentService;

    @Autowired
    private PricingContactInfoDto shipmentContactInfoDto;

    public ShipmentController(IShipmentService iShipmentService) {
        this.iShipmentService = iShipmentService;
    }


    @PostMapping
    public ResponseEntity<ShipmentDto> createShipment(@Valid @RequestBody ShipmentDto shipmentDto) {
        ShipmentDto created = iShipmentService.createShipment(shipmentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
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
    public ResponseEntity<PricingContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(shipmentContactInfoDto);
    }

}
