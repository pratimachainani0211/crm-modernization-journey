package com.crm.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public void publishCustomerEvent(CustomerEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend("customer-events", eventJson);
            log.info("Published customer event: {}", event.getEventType());
        } catch (Exception e) {
            log.error("Failed to publish customer event", e);
        }
    }

    public void publishOrderEvent(OrderEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend("order-events", eventJson);
            log.info("Published order event: {}", event.getEventType());
        } catch (Exception e) {
            log.error("Failed to publish order event", e);
        }
    }
}