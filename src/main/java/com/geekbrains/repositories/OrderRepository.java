package com.geekbrains.repositories;

import com.geekbrains.entites.Order;
import com.geekbrains.entites.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByPhone(String phone);

    List<Order> findAllByUser(User user);

}
