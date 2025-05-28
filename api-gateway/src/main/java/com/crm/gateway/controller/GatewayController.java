package com.crm.gateway.controller;

import com.crm.gateway.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class GatewayController {

    private final RoutingService routingService;

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("{\"status\":\"UP\",\"service\":\"API Gateway\"}");
    }

    @GetMapping("/customers")
    public ResponseEntity<String> getCustomers() {
        log.info("/api/v1/customers endpoint hit");
        return routingService.routeToLegacy("/customers");
    }

    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestBody String body) {
        return routingService.routeToLegacy("/customers", body);
    }

    @GetMapping("/orders")
    public ResponseEntity<String> getOrders() {
        return routingService.routeToLegacy("/orders");
    }
}