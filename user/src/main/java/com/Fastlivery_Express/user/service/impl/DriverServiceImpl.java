package com.Fastlivery_Express.user.service.impl;

import com.Fastlivery_Express.user.dto.DriverLocationUpdateDto;
import com.Fastlivery_Express.user.dto.DriverDto;
import com.Fastlivery_Express.user.entity.Driver;
import com.Fastlivery_Express.user.entity.User;
import com.Fastlivery_Express.user.exception.UserAlreadyExistsException;
import com.Fastlivery_Express.user.exception.UserEmailAlreadyUsed;
import com.Fastlivery_Express.user.exception.UserNotFoundException;
import com.Fastlivery_Express.user.mapper.DriverMapper;
import com.Fastlivery_Express.user.mapper.UserMapper;
import com.Fastlivery_Express.user.repository.UserRepository;
import com.Fastlivery_Express.user.service.IDriverService;
import com.Fastlivery_Express.user.service.clients.ShipmentsFeignClient;
import feign.FeignException;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class DriverServiceImpl implements IDriverService {

    private UserRepository userRepository;
    private ShipmentsFeignClient shipmentsFeignClient;

    @Override
    public DriverDto createDriver(DriverDto driverDto) {
        validateNewUser(driverDto.getEmail(), driverDto.getMobileNumber());
        if (driverDto.getRole() == null) {
            driverDto.setRole("ROLE_DRIVER");
        }
        Driver savedDriver = userRepository.save(DriverMapper.mapToDriver(driverDto));
        return DriverMapper.mapToDriverDto(savedDriver);
    }

    @Override
    public Page<DriverDto> searchDrivers(String status, Boolean available, String vehicleType, String email, String name, Pageable pageable) {
        return userRepository.findAll(buildDriverSpecification(status, available, vehicleType, email, name), pageable)
                .map(this::requireDriver)
                .map(DriverMapper::mapToDriverDto);
    }

    @Override
    public DriverDto getDriverById(String userId) {
        return userRepository.findByUserId(userId)
                .map(this::requireDriver)
                .map(DriverMapper::mapToDriverDto)
                .orElseThrow(() -> new UserNotFoundException("Driver with id " + userId + " not found."));
    }

    @Override
    public DriverDto getDriverByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::requireDriver)
                .map(DriverMapper::mapToDriverDto)
                .orElseThrow(() -> new UserNotFoundException("Driver with email " + email + " not found."));
    }

    @Override
    public DriverDto findNearestAvailableDriver(Double latitude, Double longitude) {
        return userRepository.findAvailableDriversWithLocation()
                .stream()
                .min((first, second) -> Double.compare(
                        distanceInKm(latitude, longitude, first.getCurrentLatitude(), first.getCurrentLongitude()),
                        distanceInKm(latitude, longitude, second.getCurrentLatitude(), second.getCurrentLongitude())
                ))
                .map(DriverMapper::mapToDriverDto)
                .orElseThrow(() -> new UserNotFoundException("No available driver found near the pickup location."));
    }

    @Override
    public boolean updateDriver(String email, DriverDto driverDto) {
        Driver driver = userRepository.findByEmail(email)
                .map(this::requireDriver)
                .orElseThrow(() -> new UserNotFoundException("Driver with email " + email + " not found."));

        validateUniqueEmailForUpdate(email, driverDto.getEmail());
        validateUniqueMobileForUpdate(driver.getMobileNumber(), driverDto.getMobileNumber());
        UserMapper.mapUserFields(driverDto, driver);
        DriverMapper.mapDriverFields(driverDto, driver);
        userRepository.save(driver);
        return true;
    }

    @Override
    public DriverDto updateDriverAvailability(String userId, Boolean available) {
        Driver driver = userRepository.findByUserId(userId)
                .map(this::requireDriver)
                .orElseThrow(() -> new UserNotFoundException("Driver with id " + userId + " not found."));
        driver.setIsAvailable(available);
        return DriverMapper.mapToDriverDto(userRepository.save(driver));
    }

    @Override
    public DriverDto updateDriverLocation(String userId, DriverLocationUpdateDto locationUpdateDto) {
        Driver driver = userRepository.findByUserId(userId)
                .map(this::requireDriver)
                .orElseThrow(() -> new UserNotFoundException("Driver with id " + userId + " not found."));

        driver.setCurrentLocation(locationUpdateDto.getCurrentLocation());
        driver.setCurrentLatitude(locationUpdateDto.getCurrentLatitude());
        driver.setCurrentLongitude(locationUpdateDto.getCurrentLongitude());
        driver.setLastActiveTime(LocalDateTime.now().toString());
        DriverDto updatedDriver = DriverMapper.mapToDriverDto(userRepository.save(driver));
        publishDriverLocationUpdate(userId);
        return updatedDriver;
    }

    @Override
    public boolean deleteDriver(String email) {
        Driver driver = userRepository.findByEmail(email)
                .map(this::requireDriver)
                .orElseThrow(() -> new UserNotFoundException("Driver with email " + email + " not found."));
        userRepository.delete(driver);
        return true;
    }

    private Driver requireDriver(User user) {
        if (user instanceof Driver driver) {
            return driver;
        }
        throw new UserNotFoundException("The requested user is not a driver.");
    }

    private void validateNewUser(String email, String mobileNumber) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists.");
        }
        if (userRepository.findByMobileNumber(mobileNumber).isPresent()) {
            throw new UserAlreadyExistsException("User with mobile number " + mobileNumber + " already exists.");
        }
    }

    private void validateUniqueEmailForUpdate(String currentEmail, String newEmail) {
        if (newEmail == null || newEmail.equals(currentEmail)) return;
        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new UserEmailAlreadyUsed("User with email " + newEmail + " already exists.");
        }
    }

    private void validateUniqueMobileForUpdate(String currentMobileNumber, String newMobileNumber) {
        if (newMobileNumber == null || newMobileNumber.equals(currentMobileNumber)) return;
        if (userRepository.findByMobileNumber(newMobileNumber).isPresent()) {
            throw new UserAlreadyExistsException("User with mobile number " + newMobileNumber + " already exists.");
        }
    }

    private double distanceInKm(Double firstLatitude, Double firstLongitude, Double secondLatitude, Double secondLongitude) {
        double earthRadiusKm = 6371.0;
        double latDistance = Math.toRadians(secondLatitude - firstLatitude);
        double lonDistance = Math.toRadians(secondLongitude - firstLongitude);
        double firstLatRadians = Math.toRadians(firstLatitude);
        double secondLatRadians = Math.toRadians(secondLatitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(firstLatRadians) * Math.cos(secondLatRadians)
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }

    private Specification<User> buildDriverSpecification(String status, Boolean available, String vehicleType, String email, String name) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            var driverRoot = criteriaBuilder.treat(root, Driver.class);
            predicates.add(root.type().in(Driver.class));

            if (hasText(status)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
            }
            if (available != null) {
                predicates.add(criteriaBuilder.equal(driverRoot.get("isAvailable"), available));
            }
            if (hasText(vehicleType)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(driverRoot.get("vehicleType")), vehicleType.toLowerCase()));
            }
            if (hasText(email)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (hasText(name)) {
                String searchName = "%" + name.toLowerCase() + "%";
                predicates.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("firstname")), searchName),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("lastname")), searchName)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void publishDriverLocationUpdate(String userId) {
        try {
            shipmentsFeignClient.publishDriverLocationUpdate(userId);
        } catch (FeignException exception) {
            // Driver location is the source of truth; stream publication is best-effort.
        }
    }
}
