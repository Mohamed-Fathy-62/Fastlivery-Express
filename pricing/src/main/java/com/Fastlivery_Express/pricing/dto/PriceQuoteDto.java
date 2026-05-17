package com.Fastlivery_Express.pricing.dto;

public record PriceQuoteDto(
        Long pricingId,
        String currency,
        Double baseFare,
        Double ratePerKm,
        Double ratePerKg,
        Double distanceKm,
        Double durationMinutes,
        Double weightKg,
        Double distancePrice,
        Double weightPrice,
        Double totalPrice,
        String geoHash
) {
}
