package com.Fastlivery_Express.pricing.service;


import com.Fastlivery_Express.pricing.dto.PriceQuoteDto;
import com.Fastlivery_Express.pricing.dto.PricingDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;

import java.util.List;

public interface IPricingService {
    Double calculatePrice(CoordinatesDto coordinatesDto, Double weight);
    PriceQuoteDto calculateQuote(CoordinatesDto coordinatesDto, Double weight);
    PricingDto createPricing(PricingDto pricingDto);
    PricingDto getActivePricing();
    List<PricingDto> getAllPricing();
    PricingDto updatePricing(Long id, PricingDto pricingDto);
    PricingDto activatePricing(Long id);
    boolean deletePricing(Long id);
}
