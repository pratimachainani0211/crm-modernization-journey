package com.crm.order.listener;

import com.crm.events.CustomerEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerEventListener implements MessageListener {

    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String eventJson = new String(message.getBody());
            CustomerEvent event = objectMapper.readValue(eventJson, CustomerEvent.class);

            log.info("Received customer event: {} for customer: {}",
                    event.getEventType(), event.getCustomerId());

            // Handle customer events (update cache, validate orders, etc.)
            handleCustomerEvent(event);

        } catch (Exception e) {
            log.error("Error processing customer event", e);
        }
    }

    private void handleCustomerEvent(CustomerEvent event) {
        switch (event.getEventType()) {
            case "CUSTOMER_CREATED" -> {
                log.info("New customer available for orders: {}", event.getCustomerName());
                // Could update local cache, send welcome email, etc.
            }
            case "CUSTOMER_UPDATED" -> {
                log.info("Customer info updated: {}", event.getCustomerName());
                // Could update cached customer data in orders
            }
            default -> log.debug("Unhandled customer event: {}", event.getEventType());
        }
    }
}