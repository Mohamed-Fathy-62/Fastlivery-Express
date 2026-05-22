package com.Fastlivery_Express.shipment.repository;

import com.Fastlivery_Express.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long>, JpaSpecificationExecutor<Shipment> {

    Optional<Shipment> findById(Long shipmentId);

    Optional<List<Shipment>> findByCustomerId(String customerId);

     Optional<Shipment> findByTrackingNumber(String trackingNumber);

    Optional<Shipment> findByStripeCheckoutSessionId(String stripeCheckoutSessionId);

    List<Shipment> findByStatusAndQuoteExpiresAtBefore(String status, LocalDateTime quoteExpiresAt);

}
