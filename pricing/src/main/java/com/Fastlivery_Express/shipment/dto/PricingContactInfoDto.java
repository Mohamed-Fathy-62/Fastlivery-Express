package com.Fastlivery_Express.shipment.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "pricings")
@Getter
@Setter
public class PricingContactInfoDto {
    private String message;
    private Map<String, String> contactDetails;
}
