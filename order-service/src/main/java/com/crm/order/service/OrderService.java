package com.crm.order.service;

import com.crm.order.client.CustomerServiceClient;
import com.crm.order.entity.Order;
import com.crm.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerServiceClient customerServiceClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrderByCustomerId(Long id) {
        return orderRepository.findByCustomerId(id);
    }

    public Order createOrder(Order order) {
        // Validate customer exists
        customerServiceClient.getCustomer(order.getCustomerId());
        return orderRepository.save(order);
    }
}
