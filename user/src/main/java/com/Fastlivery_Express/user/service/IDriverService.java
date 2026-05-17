package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.DriverDto;

public interface IDriverService {
    DriverDto createDriver(DriverDto driverDto);
    DriverDto getDriverById(String userId);
    DriverDto getDriverByEmail(String email);
    DriverDto findNearestAvailableDriver(Double latitude, Double longitude);
    boolean updateDriver(String email, DriverDto driverDto);
    DriverDto updateDriverAvailability(String userId, Boolean available);
    boolean deleteDriver(String email);
}
