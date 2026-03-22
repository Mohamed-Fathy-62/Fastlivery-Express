package com.eazybytes.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableRedisWebSession
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()

				.route("users", r -> r
						.path("/api/users/**")
						.filters(f -> f.rewritePath("/(?<p>.*)", "/${p}")) // no-op
						.uri("lb://USERS")).build();



	}


}
