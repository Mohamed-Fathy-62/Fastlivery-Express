package com.Fastlivery_Express.pricing.repository;

import com.Fastlivery_Express.pricing.entity.Costs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CostsRepository extends JpaRepository<Costs, Long> {
    Optional<Costs> findById(Long shipmentId);
}
