package com.eazybytes.gatewayserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.nio.charset.StandardCharsets;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    ReactiveJwtDecoder jwtDecoder(@Value("${app.jwt.secret}") String secret,
                                  @Value("${app.jwt.issuer}") String issuer,
                                  @Value("${app.jwt.audience}") String audience) {
        var signingKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withSecretKey(signingKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        OAuth2TokenValidator<Jwt> audienceValidator = jwt -> jwt.getAudience().contains(audience)
                ? OAuth2TokenValidatorResult.success()
                : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Required audience is missing", null));
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefaultWithIssuer(issuer), audienceValidator));
        return decoder;
    }

    @Bean
    SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorize -> authorize
                        .pathMatchers("/actuator/health", "/actuator/health/**").permitAll()
                        .pathMatchers("/actuator/prometheus").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/shipments/payment/webhook").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/customers", "/api/v1/drivers").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/users/**", "/api/v1/customers/**", "/api/v1/drivers/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/users/**", "/api/v1/customers/**", "/api/v1/drivers/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/shipments/request").hasRole("CUSTOMER")
                        .pathMatchers(HttpMethod.GET, "/api/v1/shipments/all").hasRole("CUSTOMER")
                        .pathMatchers(HttpMethod.POST, "/api/v1/shipments/*/accept", "/api/v1/shipments/*/pickup",
                                "/api/v1/shipments/*/in-transit", "/api/v1/shipments/*/deliver").hasRole("DRIVER")
                        .pathMatchers(HttpMethod.GET, "/api/v1/shipments").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/shipments").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/shipments/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/shipments/**").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.POST, "/api/v1/pricing/calculate", "/api/v1/pricing/quote").authenticated()
                        .pathMatchers("/api/v1/pricing/configs", "/api/v1/pricing/configs/**").hasRole("ADMIN")
                        .anyExchange().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .build();
    }

    private Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            Collection<GrantedAuthority> scopes = new JwtGrantedAuthoritiesConverter().convert(jwt);
            if (scopes != null) {
                authorities.addAll(scopes);
            }

            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.get("roles") instanceof Collection<?> roles) {
                roles.stream()
                        .filter(String.class::isInstance)
                        .map(String.class::cast)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase(Locale.ROOT)))
                        .forEach(authorities::add);
            }
            return authorities;
        });
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
