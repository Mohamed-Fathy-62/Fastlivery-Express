package com.Fastlivery_Express.user.service;

import com.Fastlivery_Express.user.dto.AuthResponseDto;
import com.Fastlivery_Express.user.dto.CustomerRegistrationDto;
import com.Fastlivery_Express.user.dto.LoginRequestDto;
import com.Fastlivery_Express.user.entity.Customer;
import com.Fastlivery_Express.user.exception.InvalidCredentialsException;
import com.Fastlivery_Express.user.exception.UserAlreadyExistsException;
import com.Fastlivery_Express.user.mapper.CustomerMapper;
import com.Fastlivery_Express.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.audience}")
    private String audience;

    @Value("${app.jwt.ttl-minutes:60}")
    private long tokenTtlMinutes;

    public AuthResponseDto registerCustomer(CustomerRegistrationDto request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String mobile = request.getMobileNumber().trim();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("An account with this email already exists.");
        }
        if (userRepository.findByMobileNumber(mobile).isPresent()) {
            throw new UserAlreadyExistsException("An account with this mobile number already exists.");
        }

        Customer customer = new Customer();
        customer.setFirstname(request.getFirstname().trim());
        customer.setLastname(request.getLastname().trim());
        customer.setEmail(email);
        customer.setMobileNumber(mobile);
        customer.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        customer.setRole("ROLE_CUSTOMER");
        customer.setStatus("ACTIVE");
        customer.setIsVerified(false);
        customer.setPreferredLanguage(request.getPreferredLanguage() == null ? "en" : request.getPreferredLanguage());
        customer.setDeliveryAddress(request.getDeliveryAddress());
        customer.setLoyaltyPoints(0);
        customer.setTotalOrders(0);
        customer.setTotalSpent(0.0);
        customer.setIsPremiumMember(false);

        Customer saved = userRepository.save(customer);
        return createAuthResponse(saved);
    }

    public AuthResponseDto login(LoginRequestDto request) {
        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        Customer customer = userRepository.findByEmail(email)
                .filter(Customer.class::isInstance)
                .map(Customer.class::cast)
                .filter(account -> account.getPasswordHash() != null
                        && passwordEncoder.matches(request.getPassword(), account.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);
        return createAuthResponse(customer);
    }

    private AuthResponseDto createAuthResponse(Customer customer) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(tokenTtlMinutes, ChronoUnit.MINUTES);
        String role = customer.getRole() == null ? "ROLE_CUSTOMER" : customer.getRole();
        String roleClaim = role.startsWith("ROLE_") ? role.substring("ROLE_".length()) : role;

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(customer.getUserId())
                .audience(List.of(audience))
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("email", customer.getEmail())
                .claim("realm_access", Map.of("roles", List.of(roleClaim)))
                .build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();

        return new AuthResponseDto(token, "Bearer", tokenTtlMinutes * 60,
                CustomerMapper.mapToCustomerDto(customer));
    }
}
