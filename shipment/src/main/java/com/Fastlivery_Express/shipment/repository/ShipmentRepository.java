package com.Fastlivery_Express.shipment.repository;

import com.Fastlivery_Express.shipment.entity.Shipment;
import com.Fastlivery_Express.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    Optional<Shipment> findById(Long shipmentId);

     Optional<Shipment> findByTrackingNumber(String trackingNumber);

}
