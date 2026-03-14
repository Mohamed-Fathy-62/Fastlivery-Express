package com.Fastlivery_Express.shipment.mapper;

import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.entity.Shipment;

import java.util.List;
import java.util.Optional;


public class ShipmentMapper {
    public static ShipmentDto mapToShipmentDto(Shipment shipment) {
        if (shipment == null) return null;
        ShipmentDto shipmentDto = new ShipmentDto();

        shipmentDto.setId(shipment.getId());
        shipmentDto.setTrackingNumber(shipment.getTrackingNumber());
        shipmentDto.setCustomerId(shipment.getCustomerId());
        shipmentDto.setDriverId(shipment.getDriverId());
        shipmentDto.setOriginAddress(shipment.getOriginAddress());
        shipmentDto.setDestinationAddress(shipment.getDestinationAddress());
        shipmentDto.setStatus(shipment.getStatus());
        shipmentDto.setTotalPrice(shipment.getTotalPrice());
        shipmentDto.setEstimatedDeliveryTime(shipment.getEstimatedDeliveryTime());
        shipmentDto.setActualDeliveryTime(shipment.getActualDeliveryTime());
        shipmentDto.setPackageDetails(shipment.getPackageDetails());
        shipmentDto.setPaymentStatus(shipment.getPaymentStatus());
        shipmentDto.setCustomerFeedback(shipment.getCustomerFeedback());
        shipmentDto.setCustomerRating(shipment.getCustomerRating());
        shipmentDto.setDriverFeedback(shipment.getDriverFeedback());
        shipmentDto.setDriverRating(shipment.getDriverRating());
        shipmentDto.setCancellationReason(shipment.getCancellationReason());
        shipmentDto.setLastKnownLocation(shipment.getLastKnownLocation());
        shipmentDto.setTotalWeight(shipment.getTotalWeight());
        shipmentDto.setPackageDimensions(shipment.getPackageDimensions());


        return shipmentDto;
    }

    public static Shipment mapToShipment(ShipmentDto shipmentDto) {
        if (shipmentDto == null) return null;
        Shipment shipment = new Shipment();

        //shipment.setShipmentId(shipmentDto.getShipmentId());
        shipment.setTrackingNumber(shipmentDto.getTrackingNumber());
        shipment.setCustomerId(shipmentDto.getCustomerId());
        shipment.setDriverId(shipmentDto.getDriverId());
        shipment.setOriginAddress(shipmentDto.getOriginAddress());
        shipment.setDestinationAddress(shipmentDto.getDestinationAddress());
        shipment.setStatus(shipmentDto.getStatus());
        shipment.setTotalPrice(shipmentDto.getTotalPrice());
        shipment.setEstimatedDeliveryTime(shipmentDto.getEstimatedDeliveryTime());
        shipment.setActualDeliveryTime(shipmentDto.getActualDeliveryTime());
        shipment.setPackageDetails(shipmentDto.getPackageDetails());
        shipment.setPaymentStatus(shipmentDto.getPaymentStatus());
        shipment.setCustomerFeedback(shipmentDto.getCustomerFeedback());
        shipment.setCustomerRating(shipmentDto.getCustomerRating());
        shipment.setDriverFeedback(shipmentDto.getDriverFeedback());
        shipment.setDriverRating(shipmentDto.getDriverRating());
        shipment.setCancellationReason(shipmentDto.getCancellationReason());
        shipment.setLastKnownLocation(shipmentDto.getLastKnownLocation());
        shipment.setTotalWeight(shipmentDto.getTotalWeight());
        shipment.setPackageDimensions(shipmentDto.getPackageDimensions());

        return shipment;
    }

    public static List<ShipmentDto> mapToListDtos(Optional<List<Shipment>> byUserId) {
        return byUserId
                .orElse(List.of())
                .stream()
                .map(ShipmentMapper::mapToShipmentDto)
                .toList();
    }
}
