package com.Fastlivery_Express.pricing.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pricing")
public class Pricing extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    @Column(name = "base_fare", nullable = false)
    private Double baseFare;
    @Column(name = "rate_per_km", nullable = false)
    private Double ratePerKm;
    @Column(name = "rate_per_kg", nullable = false)
    private Double ratePerKg;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}
