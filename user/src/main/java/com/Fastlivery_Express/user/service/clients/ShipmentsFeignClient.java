package com.Fastlivery_Express.user.service.clients;

import com.Fastlivery_Express.user.dto.ShipmentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("shipments")
public interface ShipmentsFeignClient {

    @GetMapping(value = "api/shipments/all", consumes = "application/json")
    public ResponseEntity<List<ShipmentDto>> getAllShipments(@RequestParam("customer_id") Long customerId);

}
