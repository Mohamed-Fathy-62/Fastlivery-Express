package com.Fastlivery_Express.shipment.repository;

import com.Fastlivery_Express.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findById(Long shipmentId);

    Optional<List<Shipment>> findByCustomerId(Long customerId);

     Optional<Shipment> findByTrackingNumber(String trackingNumber);

}
