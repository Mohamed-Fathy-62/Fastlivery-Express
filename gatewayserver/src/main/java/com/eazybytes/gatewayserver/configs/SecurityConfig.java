package com.eazybytes.gatewayserver.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http
                                    ,ReactiveClientRegistrationRepository clientRegistrationRepository) {
        System.out.println("i am here");
        http
            .authorizeExchange(exchanges -> exchanges
                    .anyExchange().permitAll() //ToDo Authorization
            )
            .oauth2Login(oauth2 -> oauth2
                    .authenticationSuccessHandler(new RedirectServerAuthenticationSuccessHandler("http://localhost:5500"))
            )
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
//            .csrf(csrf -> csrf
//                .csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse())
//                .csrfTokenRequestHandler(new XorServerCsrfTokenRequestAttributeHandler()::handle)
//            )
            .logout(logout -> logout.
                    logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository)));

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("https://google.com","http://localhost:5500", "null","http://localhost:8088", "http://localhost:8085", "http://http://127.0.0.1:5500"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    // Forces the CSRF token to be generated and sent as a cookie on the first request
    @Bean
    public WebFilter csrfCookieWebFilter() {
        return (exchange, chain) -> {
            Mono<CsrfToken> csrfToken = exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty());
            return csrfToken.doOnSuccess(token -> {}).then(chain.filter(exchange));
        };
    }
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("SESSION");
        resolver.addCookieInitializer(builder -> builder.path("/"));
        return resolver;
    }

    private ServerLogoutSuccessHandler oidcLogoutSuccessHandler(ReactiveClientRegistrationRepository repository) {
        OidcClientInitiatedServerLogoutSuccessHandler logoutHandler =
                new OidcClientInitiatedServerLogoutSuccessHandler(repository);
        logoutHandler.setPostLogoutRedirectUri("https://google.com");
        return logoutHandler;
    }
}