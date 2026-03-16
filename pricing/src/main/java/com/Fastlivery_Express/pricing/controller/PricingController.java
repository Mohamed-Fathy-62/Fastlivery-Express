package com.Fastlivery_Express.pricing.controller;

import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.OpenRouteServiceResponse;
import com.Fastlivery_Express.pricing.service.IPricingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(
        name = "CRUD REST APIs for Costs in Fastlivery Express",
        description = "CRUD REST APIs in Fastlivery to CREATE, UPDATE, FETCH AND DELETE pricing details"
)
@RestController
@RequestMapping(path = "/api/pricing", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class PricingController {

    private final IPricingService iPricingService;


    @GetMapping
    public ResponseEntity<String> test(@Valid @RequestBody CoordinatesDto coordinatesDto) {
        var created = iPricingService.calculatePrice(coordinatesDto);



        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


//    @GetMapping
//    public ResponseEntity<DeliveredOrderResponseDto> calculatePrice(@Valid @RequestBody DeliveredOrderRequestDto deliveredOrderRequestDto) {
//        DeliveredOrderResponseDto created = iPricingService.calculateCost(deliveredOrderRequestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(created);
//    }


}
