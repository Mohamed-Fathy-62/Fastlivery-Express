package com.Fastlivery_Express.shipment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @ToString
@NoArgsConstructor @AllArgsConstructor
@Table(name = "pricings")
public class Pricing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;





}
