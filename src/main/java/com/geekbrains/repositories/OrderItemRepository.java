package com.geekbrains.repositories;

import com.geekbrains.entites.Feedback;
import com.geekbrains.entites.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}