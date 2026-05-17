package com.Fastlivery_Express.pricing.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PricingDto(
        Long id,
        @NotNull(message = "baseFare can not be null")
        @PositiveOrZero(message = "baseFare must be zero or positive")
        Double baseFare,
        @NotNull(message = "ratePerKm can not be null")
        @PositiveOrZero(message = "ratePerKm must be zero or positive")
        Double ratePerKm,
        @NotNull(message = "ratePerKg can not be null")
        @PositiveOrZero(message = "ratePerKg must be zero or positive")
        Double ratePerKg,
        Boolean isActive
) {
}
