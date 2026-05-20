package com.eazybytes.gatewayserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/observability", produces = MediaType.APPLICATION_JSON_VALUE)
public class ObservabilityController {

    private static final Logger log = LoggerFactory.getLogger(ObservabilityController.class);

    @GetMapping("/ping")
    public Mono<Map<String, Object>> ping(@RequestParam(defaultValue = "manual-test") String message) {
        log.info("Observability ping received message={}", message);

        return Mono.just(Map.of(
                "status", "ok",
                "service", "gatewayserver",
                "message", message,
                "timestamp", Instant.now().toString()
        ));
    }
}
