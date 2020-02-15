package com.geekbrains.repositories;


import com.geekbrains.entites.OrderItem;
import com.geekbrains.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT o.id FROM OrderItem o WHERE o.product.id =?1 and o.order.user = ?2")
    Long findOrderItemByProductIdAAndUser(Long prodId, User user);
}