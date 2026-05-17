package com.Fastlivery_Express.pricing.service.impl;

import ch.hsr.geohash.GeoHash;
import com.Fastlivery_Express.pricing.dto.PriceQuoteDto;
import com.Fastlivery_Express.pricing.dto.PricingDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.OpenRouteServiceResponse;
import com.Fastlivery_Express.pricing.entity.Pricing;
import com.Fastlivery_Express.pricing.exception.RouteNotFoundException;
import com.Fastlivery_Express.pricing.mapper.PricingMapper;
import com.Fastlivery_Express.pricing.repository.PricingRepository;
import com.Fastlivery_Express.pricing.service.IPricingService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class PricingServiceImpl implements IPricingService {

    private static final String DEFAULT_CURRENCY = "EGP";
    private static final double DEFAULT_BASE_FARE = 25.0;
    private static final double DEFAULT_RATE_PER_KM = 6.0;
    private static final double DEFAULT_RATE_PER_KG = 4.0;

    private PricingRepository pricingRepository;
    private CachingService cachingService;

    @Override
    public Double calculatePrice(CoordinatesDto coordinatesDto, Double weight) {
        return calculateQuote(coordinatesDto, weight).totalPrice();
    }

    @Override
    public PriceQuoteDto calculateQuote(CoordinatesDto coordinatesDto, Double weight) {
        CoordinatesDto hashedCoordinates = hashCoordinates(coordinatesDto);
        OpenRouteServiceResponse response = cachingService.getOpenRouteServiceResponse(hashedCoordinates);
        validateRoute(response);

        Double distanceInKm = round(response.getRoutes().get(0).getSummary().getDistance() / 1000.0);
        Double durationInMinutes = round(response.getRoutes().get(0).getSummary().getDuration() / 60.0);
        Double safeWeight = weight != null ? weight : 1.0;
        Pricing activePricing = findOrCreateActivePricing();

        Double distancePrice = round(activePricing.getRatePerKm() * distanceInKm);
        Double weightPrice = round(activePricing.getRatePerKg() * safeWeight);
        Double totalPrice = round(activePricing.getBaseFare() + distancePrice + weightPrice);

        return new PriceQuoteDto(
                activePricing.getId(),
                DEFAULT_CURRENCY,
                activePricing.getBaseFare(),
                activePricing.getRatePerKm(),
                activePricing.getRatePerKg(),
                distanceInKm,
                durationInMinutes,
                safeWeight,
                distancePrice,
                weightPrice,
                totalPrice,
                hashedCoordinates.getGeoHash()
        );
    }

    @Override
    public PricingDto createPricing(PricingDto pricingDto) {
        Pricing pricing = PricingMapper.mapToPricing(pricingDto);
        if (Boolean.TRUE.equals(pricing.getIsActive())) {
            deactivateAllPricing();
        }
        return PricingMapper.mapToPricingDto(pricingRepository.save(pricing));
    }

    @Override
    public PricingDto getActivePricing() {
        return PricingMapper.mapToPricingDto(findOrCreateActivePricing());
    }

    @Override
    public List<PricingDto> getAllPricing() {
        return pricingRepository.findAll()
                .stream()
                .map(PricingMapper::mapToPricingDto)
                .toList();
    }

    @Override
    public PricingDto updatePricing(Long id, PricingDto pricingDto) {
        Pricing pricing = pricingRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Pricing configuration with id " + id + " not found."));

        pricing.setBaseFare(pricingDto.baseFare());
        pricing.setRatePerKm(pricingDto.ratePerKm());
        pricing.setRatePerKg(pricingDto.ratePerKg());
        if (Boolean.TRUE.equals(pricingDto.isActive())) {
            deactivateAllPricing();
            pricing.setIsActive(true);
        } else if (pricingDto.isActive() != null) {
            pricing.setIsActive(pricingDto.isActive());
        }
        return PricingMapper.mapToPricingDto(pricingRepository.save(pricing));
    }

    @Override
    public PricingDto activatePricing(Long id) {
        Pricing pricing = pricingRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Pricing configuration with id " + id + " not found."));

        deactivateAllPricing();
        pricing.setIsActive(true);
        return PricingMapper.mapToPricingDto(pricingRepository.save(pricing));
    }

    @Override
    public boolean deletePricing(Long id) {
        Pricing pricing = pricingRepository.findById(id)
                .orElseThrow(() -> new RouteNotFoundException("Pricing configuration with id " + id + " not found."));
        pricingRepository.delete(pricing);
        return true;
    }

    public Pricing findOrCreateActivePricing() {
        return pricingRepository.findByIsActiveTrue()
                .orElseGet(this::createDefaultActivePricing);
    }

    public String geoHashCoordinates(Double lon, Double lat) {
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

    private Pricing createDefaultActivePricing() {
        Pricing pricing = new Pricing();
        pricing.setBaseFare(DEFAULT_BASE_FARE);
        pricing.setRatePerKm(DEFAULT_RATE_PER_KM);
        pricing.setRatePerKg(DEFAULT_RATE_PER_KG);
        pricing.setIsActive(true);
        return pricingRepository.save(pricing);
    }

    private void deactivateAllPricing() {
        pricingRepository.findByIsActiveTrueOrderByIdDesc()
                .forEach(pricing -> {
                    pricing.setIsActive(false);
                    pricingRepository.save(pricing);
                });
    }

    private void validateRoute(OpenRouteServiceResponse response) {
        if (response == null || response.getRoutes() == null || response.getRoutes().isEmpty()
                || response.getRoutes().get(0).getSummary() == null) {
            throw new RouteNotFoundException("Route could not be calculated for the provided coordinates.");
        }
    }

    private Double round(Double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
