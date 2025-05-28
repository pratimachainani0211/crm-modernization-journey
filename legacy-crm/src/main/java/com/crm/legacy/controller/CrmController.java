package com.crm.legacy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * LEGACY CONTROLLER - DEMONSTRATES ANTI-PATTERNS
 * Problems:
 * - Direct SQL in controller (violates SRP)
 * - No abstraction layers
 * - Mixed concerns
 */
@RestController
@RequiredArgsConstructor
public class CrmController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/customers")
    public List<Map<String, Object>> getCustomers() {
        // BAD: Direct SQL in controller
        return jdbcTemplate.queryForList("SELECT * FROM customers");
    }

    @PostMapping("/customers")
    public String createCustomer(@RequestBody Map<String, String> customer) {
        // BAD: No validation, direct SQL
        jdbcTemplate.update(
                "INSERT INTO customers (name, email) VALUES (?, ?)",
                customer.get("name"), customer.get("email")
        );
        return "Customer created";
    }

    @GetMapping("/orders")
    public List<Map<String, Object>> getOrders() {
        // BAD: Complex joins in controller
        return jdbcTemplate.queryForList("""
            SELECT o.id, o.total, c.name as customer_name 
            FROM orders o JOIN customers c ON o.customer_id = c.id
        """);
    }
}