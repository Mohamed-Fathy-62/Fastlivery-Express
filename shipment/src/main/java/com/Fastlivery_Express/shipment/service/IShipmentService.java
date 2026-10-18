package com.Fastlivery_Express.shipment.service;

import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.dto.ShipmentQuoteResponseDto;
import com.Fastlivery_Express.shipment.dto.ShipmentRequestDto;
import com.Fastlivery_Express.shipment.dto.ShipmentTrackingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IShipmentService {
    ShipmentDto createShipment(ShipmentDto shipmentDto);
    ShipmentDto getShipmentById(Long id);
    boolean updateShipment(Long id, ShipmentDto userDto);
    boolean deleteShipment(Long id);

    List<ShipmentDto> getAllShipmentsByUserId(String userId);
    Page<ShipmentDto> searchShipments(String customerId, String driverId, String status,
                                      String paymentStatus, String trackingNumber, Pageable pageable);
    ShipmentQuoteResponseDto requestShipment(ShipmentRequestDto shipmentRequestDto, String customerId);
    ShipmentDto confirmShipment(Long id);
    ShipmentDto acceptShipment(Long id, String driverId);
    ShipmentDto pickupShipment(Long id, String driverId);
    ShipmentDto markShipmentInTransit(Long id, String driverId);
    ShipmentDto deliverShipment(Long id, String driverId);
    ShipmentTrackingDto trackShipment(String trackingNumber);
}
