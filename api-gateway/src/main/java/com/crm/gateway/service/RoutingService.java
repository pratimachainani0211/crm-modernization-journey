package com.crm.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
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

    @Value("${order.service.url:http://order-service:8082}")
    private String orderServiceUrl;

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
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
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
    // Route to Order Service (NEW in Phase 3)
    public ResponseEntity<String> routeToOrderService(String path) {
        String url = orderServiceUrl + path;
        log.info("Routing GET {} to order service", path);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    String.class
            );
            log.info("Order service response status: {}", response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        } catch (Exception e) {
            log.error("Error routing to order service: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Order service unavailable\"}");
        }
    }

    public ResponseEntity<String> routeToOrderService(String path, String body) {
        String url = orderServiceUrl + path;
        log.info("Routing POST {} to order service", path);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("Order service response status: {}", response.getStatusCode());
            return ResponseEntity.status(response.getStatusCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response.getBody());
        } catch (Exception e) {
            log.error("Error routing to order service: {}", e.getMessage());
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"Order service unavailable\"}");
        }
    }
}