package com.Fastlivery_Express.user.mapper;

import com.Fastlivery_Express.user.dto.DriverDto;
import com.Fastlivery_Express.user.entity.Driver;

public class DriverMapper {
    public static DriverDto mapToDriverDto(Driver driver) {
        if (driver == null) return null;
        DriverDto driverDto = new DriverDto();

        driverDto.setUserId(driver.getUserId());
        driverDto.setFirstname(driver.getFirstname());
        driverDto.setLastname(driver.getLastname());
        driverDto.setEmail(driver.getEmail());
        driverDto.setMobileNumber(driver.getMobileNumber());
        driverDto.setProfileImageUrl(driver.getProfileImageUrl());
        driverDto.setRole(driver.getRole());
        driverDto.setStatus(driver.getStatus());
        driverDto.setPreferredLanguage(driver.getPreferredLanguage());
        driverDto.setIsVerified(driver.getIsVerified());
        driverDto.setLicenseNumber(driver.getLicenseNumber());
        driverDto.setVehicleType(driver.getVehicleType());
        driverDto.setVehicleNumber(driver.getVehiclePlateNumber());
        driverDto.setIsAvailable(driver.getIsAvailable());
        driverDto.setCurrentLocation(driver.getCurrentLocation());
        driverDto.setCurrentLatitude(driver.getCurrentLatitude());
        driverDto.setCurrentLongitude(driver.getCurrentLongitude());
        driverDto.setRating(driver.getRating());
        driverDto.setTotalDeliveries(driver.getTotalDeliveries());
        driverDto.setTotalDistanceTraveled(driver.getTotalDistanceTraveled());
        driverDto.setLastActiveTime(driver.getLastActiveTime());
        return driverDto;
    }
    public static Driver mapToDriver(DriverDto driverDto) {
        if (driverDto == null) return null;
        Driver driver = new Driver();

        UserMapper.mapUserFields(driverDto, driver);
        mapDriverFields(driverDto, driver);
        return driver;
    }

    public static void mapDriverFields(DriverDto driverDto, Driver driver) {
        if (driverDto == null || driver == null) return;

        driver.setLicenseNumber(driverDto.getLicenseNumber());
        driver.setVehicleType(driverDto.getVehicleType());
        driver.setVehiclePlateNumber(driverDto.getVehicleNumber());
        driver.setIsAvailable(driverDto.getIsAvailable() != null ? driverDto.getIsAvailable() : Boolean.FALSE);
        driver.setCurrentLocation(driverDto.getCurrentLocation());
        driver.setCurrentLatitude(driverDto.getCurrentLatitude());
        driver.setCurrentLongitude(driverDto.getCurrentLongitude());
        driver.setRating(driverDto.getRating() != null ? driverDto.getRating() : 0.0);
        driver.setTotalDeliveries(driverDto.getTotalDeliveries() != null ? driverDto.getTotalDeliveries() : 0);
        driver.setTotalDistanceTraveled(driverDto.getTotalDistanceTraveled() != null ? driverDto.getTotalDistanceTraveled() : 0.0);
        driver.setLastActiveTime(driverDto.getLastActiveTime());
    }
}
