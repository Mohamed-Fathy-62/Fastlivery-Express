package com.Fastlivery_Express.pricing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ActivePricingNotFoundException extends RuntimeException {
    public ActivePricingNotFoundException(String message) {
        super(message);
    }
}
