package com.eazybytes.gatewayserver.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        System.out.println("i am here");
        http
            .authorizeExchange(exchanges -> exchanges
                // 1. Allow public access to login and logout endpoints
                .pathMatchers("/login/**", "/logout/**", "/error").permitAll()
                // 2. Everything else requires a valid session
                .anyExchange().authenticated()
            )
            // 3. Enable OAuth2 Login (Standard OIDC flow)
            .oauth2Login(Customizer.withDefaults())
            // 4. CSRF Protection (Mandatory for Cookie-based BFF)
            .csrf(csrf -> csrf
                .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
                .csrfTokenRequestHandler(new XorServerCsrfTokenRequestAttributeHandler()::handle)
            )
            // 5. Logout handling (Clears both Gateway session and Keycloak session)
            .logout(logout -> logout.logoutSuccessHandler(oidcLogoutSuccessHandler()));

        return http.build();
    }

    // Forces the CSRF token to be generated and sent as a cookie on the first request
    @Bean
    public WebFilter csrfCookieWebFilter() {
        return (exchange, chain) -> {
            Mono<CsrfToken> csrfToken = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
            return csrfToken.doOnSuccess(token -> {}).then(chain.filter(exchange));
        };
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
        RedirectServerLogoutSuccessHandler handler = new RedirectServerLogoutSuccessHandler();
        handler.setLogoutSuccessUrl(URI.create("/"));
        return handler;
    }
}