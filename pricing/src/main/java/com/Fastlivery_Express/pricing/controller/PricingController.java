package com.Fastlivery_Express.pricing.controller;

import com.Fastlivery_Express.pricing.dto.PriceQuoteDto;
import com.Fastlivery_Express.pricing.dto.PricingDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.service.IPricingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for Costs in Fastlivery Express",
        description = "CRUD REST APIs in Fastlivery to CREATE, UPDATE, FETCH AND DELETE pricing details"
)
@RestController
@RequestMapping(path = "/api/v1/pricing", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class PricingController {

    private final IPricingService iPricingService;


    @PostMapping("/calculate")
    public ResponseEntity<Double> calculatePrice(@Valid @RequestBody CoordinatesDto coordinatesDto,
                                                 @RequestParam(defaultValue = "1.0") Double weight) {
        Double price = iPricingService.calculatePrice(coordinatesDto, weight);
        return ResponseEntity.ok(price);
    }

    @PostMapping("/quote")
    public ResponseEntity<PriceQuoteDto> calculateQuote(@Valid @RequestBody CoordinatesDto coordinatesDto,
                                                        @RequestParam(defaultValue = "1.0") Double weight) {
        return ResponseEntity.ok(iPricingService.calculateQuote(coordinatesDto, weight));
    }

    @PostMapping("/configs")
    public ResponseEntity<PricingDto> createPricing(@Valid @RequestBody PricingDto pricingDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iPricingService.createPricing(pricingDto));
    }

    @GetMapping("/configs")
    public ResponseEntity<List<PricingDto>> getAllPricing() {
        return ResponseEntity.ok(iPricingService.getAllPricing());
    }

    @GetMapping("/configs/active")
    public ResponseEntity<PricingDto> getActivePricing() {
        return ResponseEntity.ok(iPricingService.getActivePricing());
    }

    @PutMapping("/configs/{id}")
    public ResponseEntity<PricingDto> updatePricing(@PathVariable Long id,
                                                    @Valid @RequestBody PricingDto pricingDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(iPricingService.updatePricing(id, pricingDto));
    }

    @PatchMapping("/configs/{id}/activate")
    public ResponseEntity<PricingDto> activatePricing(@PathVariable Long id) {
        return ResponseEntity.ok(iPricingService.activatePricing(id));
    }

    @DeleteMapping("/configs/{id}")
    public ResponseEntity<Void> deletePricing(@PathVariable Long id) {
        iPricingService.deletePricing(id);
        return ResponseEntity.noContent().build();
    }


}
