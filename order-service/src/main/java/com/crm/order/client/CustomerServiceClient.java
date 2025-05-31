package com.crm.order.client;

import com.crm.dto.CustomerDto;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceClient {

    private final RestTemplate restTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @Value("${customer.service.url:http://customer-service:8081}")
    private String customerServiceUrl;

    public CustomerDto getCustomer(Long customerId) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("customer-service");

        Supplier<CustomerDto> customerSupplier = CircuitBreaker
                .decorateSupplier(circuitBreaker, () -> {
                    log.info("Calling customer service for customer: {}", customerId);
                    String url = customerServiceUrl + "/customers/" + customerId;
                    return restTemplate.getForObject(url, CustomerDto.class);
                });

        try {
            return customerSupplier.get();
        } catch (Exception e) {
            log.warn("Customer service unavailable, using fallback for customer: {}", customerId);
            return getFallbackCustomer(customerId);
        }
    }

    private CustomerDto getFallbackCustomer(Long customerId) {
        // Fallback: Return basic customer info or cached data
        CustomerDto fallback = new CustomerDto();
        fallback.setId(customerId);
        fallback.setName("Customer " + customerId);
        fallback.setEmail("unknown@example.com");
        return fallback;
    }
}