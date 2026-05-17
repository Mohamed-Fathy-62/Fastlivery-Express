package com.Fastlivery_Express.user.controller;

import com.Fastlivery_Express.user.dto.DriverDto;
import com.Fastlivery_Express.user.service.IDriverService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Driver in Fastlivery Express",
        description = "CRUD REST APIs in Fastlivery to CREATE, UPDATE, FETCH AND DELETE account details of Driver"
)
@RestController
@RequestMapping(path = "/api/drivers", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class DriverController {

    private IDriverService driverService;

    @PostMapping
    public ResponseEntity<DriverDto> createDriver(@Valid @RequestBody DriverDto driverDto) {
        DriverDto created = driverService.createDriver(driverDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DriverDto> getDriverById(@Valid @PathVariable String id) {
        return ResponseEntity.ok(driverService.getDriverById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<DriverDto> getDriverByEmail(@Valid @PathVariable String email) {
        return ResponseEntity.ok(driverService.getDriverByEmail(email));
    }

    @GetMapping("/nearest")
    public ResponseEntity<DriverDto> findNearestAvailableDriver(@RequestParam Double latitude,
                                                                @RequestParam Double longitude) {
        System.out.println("i am here");
        return ResponseEntity.ok(driverService.findNearestAvailableDriver(latitude, longitude));
    }

    @PutMapping("/{email}")
    public ResponseEntity<DriverDto> updateDriver(@Valid @PathVariable String email,
                                                  @Valid @RequestBody DriverDto driverDto) {
        driverService.updateDriver(email, driverDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(driverDto);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<DriverDto> updateDriverAvailability(@Valid @PathVariable String id,
                                                              @RequestParam Boolean available) {
        return ResponseEntity.ok(driverService.updateDriverAvailability(id, available));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteDriver(@Valid @PathVariable String email) {
        driverService.deleteDriver(email);
        return ResponseEntity.noContent().build();
    }
}
