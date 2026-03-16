package com.Fastlivery_Express.pricing.dto.ors_dtos;

import lombok.Data;
import java.util.List;

@Data
public class OpenRouteServiceResponse {
    private List<Route> routes;
}
