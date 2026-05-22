package com.Fastlivery_Express.pricing.controller;

import com.Fastlivery_Express.pricing.dto.ApiResponse;
import com.Fastlivery_Express.pricing.dto.PriceQuoteDto;
import com.Fastlivery_Express.pricing.dto.PricingDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.service.IPricingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ApiResponse<Double>> calculatePrice(@Valid @RequestBody CoordinatesDto coordinatesDto,
                                                              @RequestParam(defaultValue = "1.0") Double weight) {
        Double price = iPricingService.calculatePrice(coordinatesDto, weight);
        return ResponseEntity.ok(ApiResponse.success("Price calculated successfully", price));
    }

    @PostMapping("/quote")
    public ResponseEntity<ApiResponse<PriceQuoteDto>> calculateQuote(@Valid @RequestBody CoordinatesDto coordinatesDto,
                                                                     @RequestParam(defaultValue = "1.0") Double weight) {
        return ResponseEntity.ok(ApiResponse.success("Price quote calculated successfully",
                iPricingService.calculateQuote(coordinatesDto, weight)));
    }

    @PostMapping("/configs")
    public ResponseEntity<ApiResponse<PricingDto>> createPricing(@Valid @RequestBody PricingDto pricingDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Pricing configuration created successfully",
                iPricingService.createPricing(pricingDto)));
    }

    @GetMapping("/configs")
    public ResponseEntity<ApiResponse<Page<PricingDto>>> getAllPricing(@RequestParam(required = false) Boolean active,
                                                                       @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Pricing configurations fetched successfully",
                iPricingService.getAllPricing(active, pageable)));
    }

    @GetMapping("/configs/active")
    public ResponseEntity<ApiResponse<PricingDto>> getActivePricing() {
        return ResponseEntity.ok(ApiResponse.success("Active pricing configuration fetched successfully",
                iPricingService.getActivePricing()));
    }

    @PutMapping("/configs/{id}")
    public ResponseEntity<ApiResponse<PricingDto>> updatePricing(@PathVariable Long id,
                                                                 @Valid @RequestBody PricingDto pricingDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success("Pricing configuration updated successfully",
                iPricingService.updatePricing(id, pricingDto)));
    }

    @PatchMapping("/configs/{id}/activate")
    public ResponseEntity<ApiResponse<PricingDto>> activatePricing(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Pricing configuration activated successfully",
                iPricingService.activatePricing(id)));
    }

    @DeleteMapping("/configs/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePricing(@PathVariable Long id) {
        iPricingService.deletePricing(id);
        return ResponseEntity.ok(ApiResponse.success("Pricing configuration deleted successfully", null));
    }


}
