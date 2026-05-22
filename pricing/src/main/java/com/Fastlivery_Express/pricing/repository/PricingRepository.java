package com.Fastlivery_Express.pricing.repository;

import com.Fastlivery_Express.pricing.entity.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long>, JpaSpecificationExecutor<Pricing> {
    Optional<Pricing> findByIsActiveTrue();
    List<Pricing> findByIsActiveTrueOrderByIdDesc();
}
