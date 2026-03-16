package com.Fastlivery_Express.pricing.dto.ors_dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

//Becase Data Carrier only


@JsonIgnoreProperties(ignoreUnknown = true)
public record CoordinatesDto(
        @NotEmpty(message = "coordinates must not be empty")
        List<
                @Size(min = 2, max = 2, message = "each coordinate must be [lon, lat]")
                        List<Double>
                > coordinates
) {}

