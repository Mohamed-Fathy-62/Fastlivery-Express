package com.Fastlivery_Express.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatesDto {
    private List<List<Double>> coordinates;
}
