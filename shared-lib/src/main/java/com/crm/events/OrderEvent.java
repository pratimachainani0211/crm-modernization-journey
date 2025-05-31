package com.crm.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private String eventType; // ORDER_CREATED, ORDER_UPDATED, ORDER_CANCELLED
    private Long orderId;
    private Long customerId;
    private BigDecimal total;
    private String status;
    private LocalDateTime timestamp;

    public static OrderEvent created(Long orderId, Long customerId, BigDecimal total) {
        return new OrderEvent("ORDER_CREATED", orderId, customerId, total, "PENDING", LocalDateTime.now());
    }
}