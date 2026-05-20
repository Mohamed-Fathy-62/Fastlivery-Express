package com.Fastlivery_Express.shipment.service.clients;

import com.Fastlivery_Express.shipment.dto.DriverDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users", url = "${clients.users.url:http://localhost:8085}")
public interface UsersFeignClient {

    @GetMapping("/api/v1/drivers/nearest")
    DriverDto findNearestAvailableDriver(@RequestParam("latitude") Double latitude,
                                         @RequestParam("longitude") Double longitude);

    @GetMapping("/api/v1/drivers/{id}")
    DriverDto getDriverById(@PathVariable("id") String id);

    @PutMapping("/api/v1/drivers/{id}/availability")
    DriverDto updateDriverAvailability(@PathVariable("id") String id,
                                       @RequestParam("available") Boolean available);
}
