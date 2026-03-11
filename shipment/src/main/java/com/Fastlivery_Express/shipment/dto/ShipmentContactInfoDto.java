package com.Fastlivery_Express.shipment.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "shipments")
@Getter
@Setter
public class ShipmentContactInfoDto {
    private String message;
    private Map<String, String> contactDetails;
}
