package com.Fastlivery_Express.pricing.service;


import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.OpenRouteServiceResponse;
import reactor.core.publisher.Mono;

public interface IPricingService {
    //draft implementation, will be changed to return Object with full details about the order
    Double calculatePrice(CoordinatesDto coordinatesDto, Double weight);
}
