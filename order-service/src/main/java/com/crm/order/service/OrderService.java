package com.crm.order.service;

import com.crm.order.client.CustomerClient;
import com.crm.order.entity.Order;
import com.crm.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.crm.order.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerClient customerClient;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrderByCustomerId(Long id) {
        return orderRepository.findByCustomerId(id);
    }

    public Order createOrder(Order order) {
        // Validate customer exists
        customerClient.getCustomer(order.getCustomerId());
        return orderRepository.save(order);
    }
}
