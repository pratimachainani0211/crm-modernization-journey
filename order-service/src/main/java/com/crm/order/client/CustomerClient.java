package com.crm.order.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerClient {

    private final RestTemplate restTemplate;

    @Value("${customer.service.url:http://customer-service:8081}")
    private String customerServiceUrl;

    public Object getCustomer(Long customerId) {
        String url = customerServiceUrl + "/customers/" + customerId;
        log.info("Fetching customer {} from customer service", customerId);
        return restTemplate.getForEntity(url, Object.class).getBody();
    }
}
