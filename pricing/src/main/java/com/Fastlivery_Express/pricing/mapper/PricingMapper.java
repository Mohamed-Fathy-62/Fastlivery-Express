package com.Fastlivery_Express.pricing.mapper;

import com.Fastlivery_Express.pricing.dto.PricingDto;
import com.Fastlivery_Express.pricing.entity.Pricing;

public class PricingMapper {
    public static Pricing mapToPricing(PricingDto pricingDto) {
        if (pricingDto == null) {
            return null;
        }
        Pricing pricing = new Pricing();
        pricing.setBaseFare(pricingDto.baseFare());
        pricing.setRatePerKm(pricingDto.ratePerKm());
        pricing.setRatePerKg(pricingDto.ratePerHour());
        return pricing;
    }

    public static PricingDto mapToPricingDto(Pricing pricing) {
        if (pricing == null) {
            return null;
        }
        return new PricingDto(pricing.getBaseFare(),
                pricing.getRatePerKm(),
                pricing.getRatePerKg(),
                pricing.getIsActive());
    }

}
