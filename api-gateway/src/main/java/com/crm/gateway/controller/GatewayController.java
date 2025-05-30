package com.crm.gateway.controller;

import com.crm.gateway.service.RoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // PHASE 3: Customer requests route to Customer Service
    @GetMapping("/customers")
    public ResponseEntity<String> getCustomers() {
        log.info("Routing customers request to Customer Service");
        return routingService.routeToCustomerService("/customers");
    }

    @PostMapping("/customers")
    public ResponseEntity<String> createCustomer(@RequestBody String body) {
        log.info("Routing create customer request to Customer Service");
        return routingService.routeToCustomerService("/customers", body);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<String> getCustomer(@PathVariable Long id) {
        log.info("Routing get customer {} request to Customer Service", id);
        return routingService.routeToCustomerService("/customers/" + id);
    }

    // PHASE 3: Order requests now route to Order Service
    @GetMapping("/orders")
    public ResponseEntity<String> getOrders() {
        log.info("Routing orders request to Order Service");
        return routingService.routeToOrderService("/orders");
    }

    @PostMapping("/orders")
    public ResponseEntity<String> createOrder(@RequestBody String body) {
        log.info("Routing create order request to Order Service");
        return routingService.routeToOrderService("/orders", body);
    }
}