package com.Fastlivery_Express.shipment.service.clients;

import com.Fastlivery_Express.shipment.dto.CoordinatesDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pricing", url = "${clients.pricing.url:http://localhost:8087}")
public interface PricingFeignClient {

    @PostMapping(value = "/api/v1/pricing/calculate", consumes = "application/json")
    Double calculatePrice(@RequestBody CoordinatesDto coordinatesDto, @RequestParam("weight") Double weight);
}
