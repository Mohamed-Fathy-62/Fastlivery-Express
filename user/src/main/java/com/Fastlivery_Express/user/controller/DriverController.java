package com.Fastlivery_Express.user.controller;

import com.Fastlivery_Express.user.dto.ApiResponse;
import com.Fastlivery_Express.user.dto.DriverDto;
import com.Fastlivery_Express.user.service.IDriverService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(path = "/api/v1/drivers", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
@AllArgsConstructor
public class DriverController {

    private IDriverService driverService;

    @PostMapping
    public ResponseEntity<ApiResponse<DriverDto>> createDriver(@Valid @RequestBody DriverDto driverDto) {
        DriverDto created = driverService.createDriver(driverDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Driver created successfully", created));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DriverDto>>> searchDrivers(@RequestParam(required = false) String status,
                                                                      @RequestParam(required = false) Boolean available,
                                                                      @RequestParam(required = false) String vehicleType,
                                                                      @RequestParam(required = false) String email,
                                                                      @RequestParam(required = false) String name,
                                                                      @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success("Drivers fetched successfully",
                driverService.searchDrivers(status, available, vehicleType, email, name, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverDto>> getDriverById(@Valid @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.success("Driver fetched successfully", driverService.getDriverById(id)));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<DriverDto>> getDriverByEmail(@Valid @PathVariable String email) {
        return ResponseEntity.ok(ApiResponse.success("Driver fetched successfully", driverService.getDriverByEmail(email)));
    }

    @GetMapping("/nearest")
    public ResponseEntity<ApiResponse<DriverDto>> findNearestAvailableDriver(@RequestParam Double latitude,
                                                                             @RequestParam Double longitude) {
        return ResponseEntity.ok(ApiResponse.success("Nearest available driver fetched successfully",
                driverService.findNearestAvailableDriver(latitude, longitude)));
    }

    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<DriverDto>> updateDriver(@Valid @PathVariable String email,
                                                               @Valid @RequestBody DriverDto driverDto) {
        driverService.updateDriver(email, driverDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success("Driver updated successfully", driverDto));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<ApiResponse<DriverDto>> updateDriverAvailability(@Valid @PathVariable String id,
                                                                           @RequestParam Boolean available) {
        return ResponseEntity.ok(ApiResponse.success("Driver availability updated successfully",
                driverService.updateDriverAvailability(id, available)));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteDriver(@Valid @PathVariable String email) {
        driverService.deleteDriver(email);
        return ResponseEntity.ok(ApiResponse.success("Driver deleted successfully", null));
    }
}
