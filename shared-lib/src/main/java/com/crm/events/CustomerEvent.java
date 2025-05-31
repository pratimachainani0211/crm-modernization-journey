package com.crm.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEvent {
    private String eventType; // CREATED, UPDATED, DELETED
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private LocalDateTime timestamp;

    public static CustomerEvent created(Long customerId, String name, String email) {
        return new CustomerEvent("CUSTOMER_CREATED", customerId, name, email, LocalDateTime.now());
    }

    public static CustomerEvent updated(Long customerId, String name, String email) {
        return new CustomerEvent("CUSTOMER_UPDATED", customerId, name, email, LocalDateTime.now());
    }
}