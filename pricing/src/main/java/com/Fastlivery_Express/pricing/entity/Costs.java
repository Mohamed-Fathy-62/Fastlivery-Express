package com.Fastlivery_Express.pricing.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Table(name = "pricings")
public class Costs extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

    @Column(name = "base_price", nullable = false)
    private Double basePrice;

    @Column(name = "distance_price", nullable = false)
    private Double distancePrice;

    @Column(name = "weight_price", nullable = false)
    private Double weightPrice;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;






}
