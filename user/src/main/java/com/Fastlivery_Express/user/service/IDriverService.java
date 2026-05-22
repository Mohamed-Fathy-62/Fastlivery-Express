package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.DriverDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDriverService {
    DriverDto createDriver(DriverDto driverDto);
    Page<DriverDto> searchDrivers(String status, Boolean available, String vehicleType, String email, String name, Pageable pageable);
    DriverDto getDriverById(String userId);
    DriverDto getDriverByEmail(String email);
    DriverDto findNearestAvailableDriver(Double latitude, Double longitude);
    boolean updateDriver(String email, DriverDto driverDto);
    DriverDto updateDriverAvailability(String userId, Boolean available);
    boolean deleteDriver(String email);
}
