package com.Fastlivery_Express.user.configs;

import com.Fastlivery_Express.user.dto.KeycloakConfigsDto;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Autowired
    private KeycloakConfigsDto keycloakConfigsDto;

    @Bean
    public Keycloak keycloak() {
//        return KeycloakBuilder.builder()
//                .serverUrl(keycloakConfigsDto.getUrl())
//                .realm(keycloakConfigsDto.getRealm())
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .clientId(keycloakConfigsDto.getClientId())
//                .clientSecret(keycloakConfigsDto.getClientSecret())
//                .build();
                return KeycloakBuilder.builder()
                .serverUrl("http://localhost:9095")
                .realm("fastlivery-express")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("fastlivery-express-user-service")
                .clientSecret("ZOgpbzbN1ML7IWrDHRTM1GFxdErx8bSS")
                .build();
    }
}