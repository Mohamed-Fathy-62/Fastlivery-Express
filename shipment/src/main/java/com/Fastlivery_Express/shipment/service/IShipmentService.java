package com.Fastlivery_Express.shipment.service;

import com.Fastlivery_Express.shipment.dto.ShipmentDto;

import java.util.List;

public interface IShipmentService {
    ShipmentDto createShipment(ShipmentDto shipmentDto);
    ShipmentDto getShipmentById(Long id);
    boolean updateShipment(Long id, ShipmentDto userDto);
    boolean deleteShipment(Long id);

    List<ShipmentDto> getAllShipmentsByUserId(Long userId);
}
