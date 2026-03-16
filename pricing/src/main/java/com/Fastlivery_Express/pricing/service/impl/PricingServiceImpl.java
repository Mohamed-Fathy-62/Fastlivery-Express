package com.Fastlivery_Express.pricing.service.impl;

import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.OpenRouteServiceResponse;
import com.Fastlivery_Express.pricing.repository.CostsRepository;
import com.Fastlivery_Express.pricing.service.IPricingService;
import com.Fastlivery_Express.pricing.service.clients.OrsClient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PricingServiceImpl implements IPricingService {

    private CostsRepository costsRepository;
    private OrsClient orsClient;

    @Override
    public String calculatePrice(CoordinatesDto coordinatesDto) {
        Mono<OpenRouteServiceResponse> response = orsClient.directionsDrivingCar(coordinatesDto);
        Double cost = 100.0 * response.block().getRoutes().get(0).getSummary().getDistance() / 1000; // Example: 100 currency units per km
        String res = "Cost: " + cost + " and time needed: " + response.block().getRoutes().get(0).getSummary().getDuration() / 60 + " minutes";
        return res;
    }
}

