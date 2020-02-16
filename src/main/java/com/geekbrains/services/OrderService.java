package com.geekbrains.services;

import com.geekbrains.entites.Order;
import com.geekbrains.entites.OrderItem;
import com.geekbrains.entites.Product;
import com.geekbrains.entites.User;
import com.geekbrains.repositories.OrderItemRepository;
import com.geekbrains.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> findOrdersByPhone (String phone){
        return orderRepository.findAllByPhone(phone) ;
    }

    public List<Order> findOrdersByUser(User user) {
        return orderRepository.findAllByUser(user) ;
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).get();
    }

}
