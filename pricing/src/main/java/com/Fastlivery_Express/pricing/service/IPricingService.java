package com.Fastlivery_Express.pricing.service;


import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.OpenRouteServiceResponse;
import reactor.core.publisher.Mono;

public interface IPricingService {
    String calculatePrice(CoordinatesDto deliveredOrderRequestDto);
}
