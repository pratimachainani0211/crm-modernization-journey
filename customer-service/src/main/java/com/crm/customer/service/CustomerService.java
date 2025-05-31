package com.crm.customer.service;

import com.crm.customer.entity.Customer;
import com.crm.customer.repository.CustomerRepository;
import com.crm.events.CustomerEvent;
import com.crm.events.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final EventPublisher eventPublisher;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer createCustomer(Customer customer) {
        Customer saved = customerRepository.save(customer);
        log.info("Customer created: {} - {}", saved.getId(), saved.getName());

        // Publish event asynchronously
        eventPublisher.publishCustomerEvent(
                CustomerEvent.created(saved.getId(), saved.getName(), saved.getEmail())
        );

        return saved;
    }

    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));

        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());

        Customer updated = customerRepository.save(customer);
        log.info("Customer updated: {} - {}", updated.getId(), updated.getName());

        // Publish event asynchronously
        eventPublisher.publishCustomerEvent(
                CustomerEvent.updated(updated.getId(), updated.getName(), updated.getEmail())
        );

        return updated;
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found: " + id));
    }
}