package com.Fastlivery_Express.pricing.service.impl;

import ch.hsr.geohash.GeoHash;
import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.OpenRouteServiceResponse;
import com.Fastlivery_Express.pricing.entity.Pricing;
import com.Fastlivery_Express.pricing.exception.ActivePricingNotFoundException;
import com.Fastlivery_Express.pricing.repository.CostsRepository;
import com.Fastlivery_Express.pricing.repository.PricingRepository;
import com.Fastlivery_Express.pricing.service.IPricingService;
import com.Fastlivery_Express.pricing.service.clients.OrsClient;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class PricingServiceImpl implements IPricingService {

    private CostsRepository costsRepository;
    private PricingRepository pricingRepository;
    private CachingService cachingService;
    private OrsClient orsClient;

    //draft implementation, will be changed to return Object with full details about the order
    @Override
    public Double calculatePrice(CoordinatesDto coordinatesDto, Double weight) {
        coordinatesDto = hashCoordinates(coordinatesDto);

        OpenRouteServiceResponse response = cachingService.getOpenRouteServiceResponse(coordinatesDto);


        Double distanceInKm = response.getRoutes().get(0).getSummary().getDistance() / 1000.0;
        Pricing activePricing = findActivePricing();
        Double calculatedCost = activePricing.getBaseFare()
                + (activePricing.getRatePerKg() * weight)
                + (activePricing.getRatePerKm() * distanceInKm);


        //String res = "Cost: " + cost + " and time needed: " + response.block().getRoutes().get(0).getSummary().getDuration() / 60 + " minutes";
        return calculatedCost;
    }

    public Pricing findActivePricing() {
        return pricingRepository.findByIsActiveTrue()
                .orElseThrow(() -> new ActivePricingNotFoundException("No active pricing configuration found"));
    }
    //--proxy problem with caching / when caching is not working think of proxy problem first--
//    @Cacheable(value = "GeoHashCache", key = "#coordinatesDto.geoHash")
//    public OpenRouteServiceResponse getOpenRouteServiceResponse(CoordinatesDto coordinatesDto) {
//        return orsClient.directionsDrivingCar(coordinatesDto).block();
//    }
    public String geoHashCoordinates(Double lat, Double lon) {
        return GeoHash.withCharacterPrecision(lat, lon, 7).toBase32();
    }
    public CoordinatesDto hashCoordinates(CoordinatesDto coordinatesDto) {
        String geoHashStart = geoHashCoordinates(coordinatesDto.getCoordinates().get(0).get(0),
                coordinatesDto.getCoordinates().get(0).get(1));
        String geoHashEnd = geoHashCoordinates(coordinatesDto.getCoordinates().get(1).get(0),
                coordinatesDto.getCoordinates().get(1).get(1));

        coordinatesDto.setGeoHash(geoHashStart + "-" + geoHashEnd);
        return coordinatesDto;
    }
}

