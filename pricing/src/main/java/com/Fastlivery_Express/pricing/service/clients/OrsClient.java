package com.Fastlivery_Express.pricing.service.clients;

import com.Fastlivery_Express.pricing.dto.ors_dtos.CoordinatesDto;
import com.Fastlivery_Express.pricing.dto.ors_dtos.OpenRouteServiceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class OrsClient {

    private final WebClient webClient;
    private final String apiKey;

    public OrsClient(WebClient orsWebClient, @Value("${ors.api-key}") String apiKey) {
        this.webClient = orsWebClient;
        this.apiKey = apiKey;
    }

    public Mono<OpenRouteServiceResponse> directionsDrivingCar(CoordinatesDto request) {
        return webClient.post()
                .uri("/v2/directions/driving-car")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", apiKey)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenRouteServiceResponse.class);
    }


}