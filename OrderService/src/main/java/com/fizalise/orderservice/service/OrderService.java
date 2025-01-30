package com.fizalise.orderservice.service;

import com.fizalise.orderservice.client.InventoryClient;
import com.fizalise.orderservice.dto.OrderRequest;
import com.fizalise.orderservice.entity.Order;
import com.fizalise.orderservice.event.OrderPlacedEvent;
import com.fizalise.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public record OrderService(OrderRepository orderRepository, InventoryClient inventoryClient,
                           KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
    public Order placeOrder(OrderRequest orderRequest) {
        if (!inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Item with SKU code = %s is not in stock or stock does not have such quantity"
                            .formatted(orderRequest.skuCode()));
        }
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .price(orderRequest.price())
                .skuCode(orderRequest.skuCode())
                .quantity(orderRequest.quantity())
                .build();
        orderRepository.save(order);
        OrderPlacedEvent orderPlacedEvent = OrderPlacedEvent.builder()
                        .orderNumber(order.getOrderNumber())
                        .email(orderRequest.userDetails().email())
                        .build();
        log.info("Trying to send OrderPlacedEvent {} to Kafka topic: {}",
                orderPlacedEvent, "order-placed");
        kafkaTemplate.send("order-placed", orderPlacedEvent);
        return order;
    }
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
