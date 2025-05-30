package com.crm.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutingService {

    private final RestTemplate restTemplate;

    @Value("${legacy.service.url:http://legacy-app:8080}")
    private String legacyServiceUrl;

    @Value("${customer.service.url:http://customer-service:8081}")
    private String customerServiceUrl;

    // Route to Customer Service (NEW in Phase 2)
    public ResponseEntity<String> routeToCustomerService(String path) {
        String url = customerServiceUrl + path;
        log.info("Routing GET {} to customer service", path);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            log.info("Customer service response status: {}", response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        } catch (Exception e) {
            log.error("Error routing to customer service: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Customer service unavailable\"}");
        }
    }

    public ResponseEntity<String> routeToCustomerService(String path, String body) {
        String url = customerServiceUrl + path;
        log.info("Routing POST {} to customer service", path);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);
            log.info("Customer service response status: {}", response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        } catch (Exception e) {
            log.error("Error routing to customer service: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Customer service unavailable\"}");
        }
    }

    // Route to Legacy System (for orders and other unchanged endpoints)
    public ResponseEntity<String> routeToLegacy(String path) {
        String url = legacyServiceUrl + path;
        log.info("Routing GET {} to legacy", path);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            log.info("Legacy response status: {}", response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        } catch (Exception e) {
            log.error("Error routing to legacy: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Legacy service unavailable\"}");
        }
    }

    public ResponseEntity<String> routeToLegacy(String path, String body) {
        String url = legacyServiceUrl + path;
        log.info("Routing POST {} to legacy", path);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);
            log.info("Legacy response status: {}", response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        } catch (Exception e) {
            log.error("Error routing to legacy: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Legacy service unavailable\"}");
        }
    }
}