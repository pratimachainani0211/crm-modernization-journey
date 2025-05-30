package com.crm.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutingService {

    private final RestTemplate restTemplate;

    @Value("${legacy.service.url:http://legacy-app:8080}")
    private String legacyUrl;

    public ResponseEntity<String> routeToLegacy(String path) {
        String url = legacyUrl + path;
        log.info("Routing GET {} to legacy", path);
        try {
            // Use exchange instead of getForEntity for better control
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );

            log.info("Legacy response status: {}", response.getStatusCode());

            // Return response with proper headers
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());

        } catch (Exception e) {
            log.error("Error routing to legacy: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Service unavailable");
        }
    }

    public ResponseEntity<String> routeToLegacy(String path, String body) {
        String url = legacyUrl + path;
        log.info("Routing POST {} to legacy", path);
        return restTemplate.postForEntity(url, body, String.class);
    }
}