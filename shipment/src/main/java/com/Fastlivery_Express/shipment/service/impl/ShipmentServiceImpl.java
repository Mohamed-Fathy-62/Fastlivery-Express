package com.Fastlivery_Express.shipment.service.impl;

import com.Fastlivery_Express.shipment.dto.CoordinatesDto;
import com.Fastlivery_Express.shipment.dto.DriverDto;
import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.dto.ShipmentQuoteResponseDto;
import com.Fastlivery_Express.shipment.dto.ShipmentRequestDto;
import com.Fastlivery_Express.shipment.dto.ShipmentTrackingDto;
import com.Fastlivery_Express.shipment.entity.Shipment;
import com.Fastlivery_Express.shipment.exception.ShipmentNotFoundException;
import com.Fastlivery_Express.shipment.mapper.ShipmentMapper;
import com.Fastlivery_Express.shipment.repository.ShipmentRepository;
import com.Fastlivery_Express.shipment.service.IShipmentService;
import com.Fastlivery_Express.shipment.service.clients.PricingFeignClient;
import com.Fastlivery_Express.shipment.service.clients.UsersFeignClient;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShipmentServiceImpl implements IShipmentService {

    private static final String STATUS_PENDING_CONFIRMATION = "PENDING_CONFIRMATION";
    private static final String STATUS_CONFIRMED = "CONFIRMED";
    private static final String STATUS_EXPIRED = "EXPIRED";
    private static final String PAYMENT_PAID = "PAID";

    private final ShipmentRepository shipmentRepository;
    private final PricingFeignClient pricingFeignClient;
    private final UsersFeignClient usersFeignClient;

    @Value("${shipments.quote.expiry-minutes:10}")
    private Long quoteExpiryMinutes;

    @Override
    public ShipmentDto getShipmentById(Long id) {
        Optional<Shipment> shipment = shipmentRepository.findById(id);
        if(shipment.isPresent()){
            return ShipmentMapper.mapToShipmentDto(shipment.get());
        }else{
            throw new ShipmentNotFoundException("shipment with id " + id + " not found.");
        }
    }

    @Override
    public boolean updateShipment(Long id, ShipmentDto shipmentDto) {
        boolean isUpdated = false;
        Optional<Shipment> existingShipment = shipmentRepository.findById(id);
        if(existingShipment.isPresent()){
            Shipment shipmentToUpdate = existingShipment.get();
            shipmentToUpdate.setTrackingNumber(shipmentDto.getTrackingNumber());
            shipmentToUpdate.setCustomerId(shipmentDto.getCustomerId());
            shipmentToUpdate.setDriverId(shipmentDto.getDriverId());
            shipmentToUpdate.setOriginAddress(shipmentDto.getOriginAddress());
            shipmentToUpdate.setDestinationAddress(shipmentDto.getDestinationAddress());
            shipmentToUpdate.setOriginLatitude(shipmentDto.getOriginLatitude());
            shipmentToUpdate.setOriginLongitude(shipmentDto.getOriginLongitude());
            shipmentToUpdate.setDestinationLatitude(shipmentDto.getDestinationLatitude());
            shipmentToUpdate.setDestinationLongitude(shipmentDto.getDestinationLongitude());
            shipmentToUpdate.setStatus(shipmentDto.getStatus());
            shipmentToUpdate.setTotalPrice(shipmentDto.getTotalPrice());
            shipmentToUpdate.setEstimatedDeliveryTime(shipmentDto.getEstimatedDeliveryTime());
            shipmentToUpdate.setQuoteExpiresAt(shipmentDto.getQuoteExpiresAt());
            shipmentToUpdate.setActualDeliveryTime(shipmentDto.getActualDeliveryTime());
            shipmentToUpdate.setPackageDetails(shipmentDto.getPackageDetails());
            shipmentToUpdate.setPaymentStatus(shipmentDto.getPaymentStatus());
            shipmentToUpdate.setCustomerFeedback(shipmentDto.getCustomerFeedback());
            shipmentToUpdate.setCustomerRating(shipmentDto.getCustomerRating());
            shipmentToUpdate.setDriverFeedback(shipmentDto.getDriverFeedback());
            shipmentToUpdate.setDriverRating(shipmentDto.getDriverRating());
            shipmentToUpdate.setCancellationReason(shipmentDto.getCancellationReason());
            shipmentToUpdate.setLastKnownLocation(shipmentDto.getLastKnownLocation());
            shipmentToUpdate.setTotalWeight(shipmentDto.getTotalWeight());
            shipmentToUpdate.setPackageDimensions(shipmentDto.getPackageDimensions());
            isUpdated = true;
            shipmentRepository.save(shipmentToUpdate);
        }else{
            throw new ShipmentNotFoundException("Shipment with id " + id + " not found.");
        }
        return isUpdated;
    }

    @Override
    public boolean deleteShipment(Long id) {
        return shipmentRepository.findById(id).map(shipment -> {
            shipmentRepository.delete(shipment);
            return true;
        }).orElse(false);
    }

    @Override
    public List<ShipmentDto> getAllShipmentsByUserId(String customerId) {
        return ShipmentMapper.mapToListDtos(shipmentRepository.findByCustomerId(customerId));
    }

    @Override
    public Page<ShipmentDto> searchShipments(String customerId, String driverId, String status,
                                             String paymentStatus, String trackingNumber, Pageable pageable) {
        return shipmentRepository.findAll(buildShipmentSpecification(customerId, driverId, status, paymentStatus, trackingNumber), pageable)
                .map(ShipmentMapper::mapToShipmentDto);
    }

    @Override
    public ShipmentDto createShipment(ShipmentDto shipmentDto) {
        Shipment savedShipment = shipmentRepository.save(ShipmentMapper.mapToShipment(shipmentDto));
        return ShipmentMapper.mapToShipmentDto(savedShipment);
    }

    @Override
    public ShipmentQuoteResponseDto requestShipment(ShipmentRequestDto shipmentRequestDto) {
        Double price = pricingFeignClient.calculatePrice(buildCoordinates(shipmentRequestDto), shipmentRequestDto.getTotalWeight()).getData();
        DriverDto assignedDriver = usersFeignClient.findNearestAvailableDriver(
                shipmentRequestDto.getOriginLatitude(),
                shipmentRequestDto.getOriginLongitude()
        ).getData();
        usersFeignClient.updateDriverAvailability(assignedDriver.getUserId(), false);

        Shipment shipment = new Shipment();
        shipment.setTrackingNumber(generateTrackingNumber());
        shipment.setCustomerId(shipmentRequestDto.getCustomerId());
        shipment.setDriverId(assignedDriver.getUserId());
        shipment.setOriginAddress(shipmentRequestDto.getOriginAddress());
        shipment.setDestinationAddress(shipmentRequestDto.getDestinationAddress());
        shipment.setOriginLatitude(shipmentRequestDto.getOriginLatitude());
        shipment.setOriginLongitude(shipmentRequestDto.getOriginLongitude());
        shipment.setDestinationLatitude(shipmentRequestDto.getDestinationLatitude());
        shipment.setDestinationLongitude(shipmentRequestDto.getDestinationLongitude());
        shipment.setStatus(STATUS_PENDING_CONFIRMATION);
        shipment.setTotalPrice(price);
        shipment.setPaymentStatus("PENDING");
        shipment.setPackageDetails(shipmentRequestDto.getPackageDetails());
        shipment.setTotalWeight(shipmentRequestDto.getTotalWeight());
        shipment.setPackageDimensions(shipmentRequestDto.getPackageDimensions());
        shipment.setLastKnownLocation(shipmentRequestDto.getOriginAddress());
        shipment.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(2));
        shipment.setQuoteExpiresAt(LocalDateTime.now().plusMinutes(quoteExpiryMinutes));

        Shipment savedShipment = shipmentRepository.save(shipment);
        return new ShipmentQuoteResponseDto(
                ShipmentMapper.mapToShipmentDto(savedShipment),
                assignedDriver,
                price,
                "Shipment priced and driver reserved. Confirm before " + savedShipment.getQuoteExpiresAt() + " or the quote will expire."
        );
    }

    @Override
    public ShipmentDto confirmShipment(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " not found."));

        if (!STATUS_PENDING_CONFIRMATION.equalsIgnoreCase(shipment.getStatus())) {
            throw new IllegalStateException("Shipment with id " + id + " is not waiting for confirmation.");
        }
        if (shipment.getQuoteExpiresAt() != null && shipment.getQuoteExpiresAt().isBefore(LocalDateTime.now())) {
            expireShipment(shipment);
            throw new IllegalStateException("Shipment quote with id " + id + " has expired. Please request a new quote.");
        }
        if (!PAYMENT_PAID.equalsIgnoreCase(shipment.getPaymentStatus())) {
            throw new IllegalStateException("Shipment with id " + id + " must be paid before confirmation.");
        }

        shipment.setStatus(STATUS_CONFIRMED);
        Shipment savedShipment = shipmentRepository.save(shipment);
        return ShipmentMapper.mapToShipmentDto(savedShipment);
    }

    @Override
    public ShipmentTrackingDto trackShipment(String trackingNumber) {
        Shipment shipment = shipmentRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with tracking number " + trackingNumber + " not found."));
        DriverDto driver = usersFeignClient.getDriverById(shipment.getDriverId()).getData();

        return new ShipmentTrackingDto(
                shipment.getTrackingNumber(),
                shipment.getStatus(),
                shipment.getEstimatedDeliveryTime(),
                shipment.getActualDeliveryTime(),
                shipment.getLastKnownLocation(),
                shipment.getOriginAddress(),
                shipment.getDestinationAddress(),
                shipment.getDriverId(),
                buildDriverName(driver),
                driver.getMobileNumber(),
                driver.getCurrentLocation(),
                driver.getCurrentLatitude(),
                driver.getCurrentLongitude()
        );
    }

    @Scheduled(fixedDelayString = "${shipments.quote.cleanup-fixed-delay-ms:60000}")
    public void expirePendingShipmentQuotes() {
        shipmentRepository.findByStatusAndQuoteExpiresAtBefore(STATUS_PENDING_CONFIRMATION, LocalDateTime.now())
                .forEach(this::expireShipment);
    }

    private CoordinatesDto buildCoordinates(ShipmentRequestDto shipmentRequestDto) {
        return new CoordinatesDto(List.of(
                List.of(shipmentRequestDto.getOriginLongitude(), shipmentRequestDto.getOriginLatitude()),
                List.of(shipmentRequestDto.getDestinationLongitude(), shipmentRequestDto.getDestinationLatitude())
        ));
    }

    private String generateTrackingNumber() {
        return "FLX-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private String buildDriverName(DriverDto driver) {
        return String.join(" ",
                driver.getFirstname() != null ? driver.getFirstname() : "",
                driver.getLastname() != null ? driver.getLastname() : ""
        ).trim();
    }

    private void expireShipment(Shipment shipment) {
        shipment.setStatus(STATUS_EXPIRED);
        shipment.setCancellationReason("Quote expired before customer confirmation.");
        shipmentRepository.save(shipment);
        usersFeignClient.updateDriverAvailability(shipment.getDriverId(), true);
    }

    private Specification<Shipment> buildShipmentSpecification(String customerId, String driverId, String status,
                                                               String paymentStatus, String trackingNumber) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (hasText(customerId)) {
                predicates.add(criteriaBuilder.equal(root.get("customerId"), customerId));
            }
            if (hasText(driverId)) {
                predicates.add(criteriaBuilder.equal(root.get("driverId"), driverId));
            }
            if (hasText(status)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("status")), status.toLowerCase()));
            }
            if (hasText(paymentStatus)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("paymentStatus")), paymentStatus.toLowerCase()));
            }
            if (hasText(trackingNumber)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("trackingNumber")), "%" + trackingNumber.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
