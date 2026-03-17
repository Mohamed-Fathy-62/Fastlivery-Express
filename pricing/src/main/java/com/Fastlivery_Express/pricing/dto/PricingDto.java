package com.Fastlivery_Express.pricing.dto;

public record PricingDto(Double baseFare, Double ratePerKm, Double ratePerHour, Boolean isActive) {
}
