package com.Fastlivery_Express.pricing.service.impl;

import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.OpenRouteServiceResponse;
import com.Fastlivery_Express.pricing.service.clients.OrsClient;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class CachingService {
    private final OrsClient orsClient;
    @Cacheable(value = "GeoHashCache", key = "#coordinatesDto.geoHash")
    public OpenRouteServiceResponse getOpenRouteServiceResponse(CoordinatesDto coordinatesDto) {
        return orsClient.directionsDrivingCar(coordinatesDto).block();
    }
}
