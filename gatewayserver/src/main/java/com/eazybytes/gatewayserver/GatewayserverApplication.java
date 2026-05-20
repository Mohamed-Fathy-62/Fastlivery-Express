package com.eazybytes.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route("users", r -> r
						.path("/api/v1/users/**", "/api/v1/customers/**", "/api/v1/drivers/**")
						.uri("lb://USERS"))
				.route("shipments", r -> r
						.path("/api/v1/shipments/**")
						.uri("lb://SHIPMENTS"))
				.route("pricing", r -> r
						.path("/api/v1/pricing/**")
						.uri("lb://PRICING"))
				.build();
	}


}
