package com.Fastlivery_Express.shipment.service.impl;

import com.Fastlivery_Express.shipment.dto.ShipmentDto;
import com.Fastlivery_Express.shipment.entity.Shipment;
import com.Fastlivery_Express.shipment.exception.ShipmentNotFoundException;
import com.Fastlivery_Express.shipment.mapper.ShipmentMapper;
import com.Fastlivery_Express.shipment.repository.ShipmentRepository;
import com.Fastlivery_Express.shipment.service.IShipmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ShipmentServiceImpl implements IShipmentService {

    private ShipmentRepository shipmentRepository;


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
    public List<ShipmentDto> getAllShipmentsByUserId(Long customerId) {
        return ShipmentMapper.mapToListDtos(shipmentRepository.findByCustomerId(customerId));
    }

    @Override
    public ShipmentDto createShipment(ShipmentDto shipmentDto) {
        shipmentRepository.save(ShipmentMapper.mapToShipment(shipmentDto));
        return shipmentDto;
    }
}
