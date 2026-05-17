package com.Fastlivery_Express.shipment.service.impl;

import com.Fastlivery_Express.shipment.dto.CoordinatesDto;
import com.Fastlivery_Express.shipment.dto.DriverDto;
import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.dto.ShipmentQuoteResponseDto;
import com.Fastlivery_Express.shipment.dto.ShipmentRequestDto;
import com.Fastlivery_Express.shipment.entity.Shipment;
import com.Fastlivery_Express.shipment.exception.ShipmentNotFoundException;
import com.Fastlivery_Express.shipment.mapper.ShipmentMapper;
import com.Fastlivery_Express.shipment.repository.ShipmentRepository;
import com.Fastlivery_Express.shipment.service.IShipmentService;
import com.Fastlivery_Express.shipment.service.clients.PricingFeignClient;
import com.Fastlivery_Express.shipment.service.clients.UsersFeignClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class ShipmentServiceImpl implements IShipmentService {

    private ShipmentRepository shipmentRepository;
    private PricingFeignClient pricingFeignClient;
    private UsersFeignClient usersFeignClient;

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
    public ShipmentDto createShipment(ShipmentDto shipmentDto) {
        Shipment savedShipment = shipmentRepository.save(ShipmentMapper.mapToShipment(shipmentDto));
        return ShipmentMapper.mapToShipmentDto(savedShipment);
    }

    @Override
    public ShipmentQuoteResponseDto requestShipment(ShipmentRequestDto shipmentRequestDto) {
        Double price = pricingFeignClient.calculatePrice(buildCoordinates(shipmentRequestDto), shipmentRequestDto.getTotalWeight());
        DriverDto assignedDriver = usersFeignClient.findNearestAvailableDriver(
                shipmentRequestDto.getOriginLatitude(),
                shipmentRequestDto.getOriginLongitude()
        );

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
        shipment.setStatus("PENDING_CONFIRMATION");
        shipment.setTotalPrice(price);
        shipment.setPaymentStatus("PENDING");
        shipment.setPackageDetails(shipmentRequestDto.getPackageDetails());
        shipment.setTotalWeight(shipmentRequestDto.getTotalWeight());
        shipment.setPackageDimensions(shipmentRequestDto.getPackageDimensions());
        shipment.setLastKnownLocation(shipmentRequestDto.getOriginAddress());
        shipment.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(2));

        Shipment savedShipment = shipmentRepository.save(shipment);
        return new ShipmentQuoteResponseDto(
                ShipmentMapper.mapToShipmentDto(savedShipment),
                assignedDriver,
                price,
                "Shipment priced and nearest driver assigned. Confirm the shipment to reserve the driver."
        );
    }

    @Override
    public ShipmentDto confirmShipment(Long id) {
        Shipment shipment = shipmentRepository.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException("Shipment with id " + id + " not found."));

        if (!"PENDING_CONFIRMATION".equalsIgnoreCase(shipment.getStatus())) {
            throw new IllegalStateException("Shipment with id " + id + " is not waiting for confirmation.");
        }

        usersFeignClient.updateDriverAvailability(shipment.getDriverId(), false);
        shipment.setStatus("CONFIRMED");
        Shipment savedShipment = shipmentRepository.save(shipment);
        return ShipmentMapper.mapToShipmentDto(savedShipment);
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
}
