package com.pragma.usuarios.infrastructure.adapter.output.external;

import com.pragma.shared.config.ExternalApiProperties;
import com.pragma.usuarios.domain.port.output.ExternalUserRepository;
import com.pragma.usuarios.infrastructure.adapter.output.external.dto.PragmaUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PragmaUserAdapter implements ExternalUserRepository {

    private final ExternalApiProperties apiProperties;
    private final RestTemplate restTemplate;

    @Override
    public Optional<PragmaUserDto> findUserByEmail(String email) {
        try {
            String url = apiProperties.getServiceUrl() + "/prod/administration/pragmatic/" + email;
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json, text/plain, */*");
            headers.set("x-api-key-prod", apiProperties.getServiceKey());
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            log.debug("Calling external API: {}", url);
            ResponseEntity<PragmaUserDto> response = restTemplate.exchange(
                url, HttpMethod.GET, entity, PragmaUserDto.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.debug("Successfully retrieved user data for email: {}", response.getBody());
                return Optional.of(response.getBody());
            }
            
            log.warn("No user data found for email: {}", email);
            return Optional.empty();
            
        } catch (Exception e) {
            log.error("Error calling external API for email {}: {}", email, e.getMessage());
            return Optional.empty();
        }
    }
}