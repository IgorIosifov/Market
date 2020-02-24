package com.geekbrains.utils;

import com.geekbrains.entites.Order;
import com.geekbrains.services.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Receiver {
    private RabbitTemplate rabbitTemplate;

    private OrderService orderService;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void receiveMessage(String message) {
        String [] tokens = message.split("\\.");
        Order order = orderService.findById(Long.parseLong(tokens[1]));
        String orderAddress = order.getAddress();
        order.setAddress(orderAddress + "confirmed");
    }
}

