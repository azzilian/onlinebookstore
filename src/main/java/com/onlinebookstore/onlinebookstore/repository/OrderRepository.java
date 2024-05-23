package com.onlinebookstore.onlinebookstore.repository;

import com.onlinebookstore.onlinebookstore.model.orders.Order;
import com.onlinebookstore.onlinebookstore.model.orders.OrderStatus;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Set<Order> findByUserId(Long userId);

    Optional<Order> findByUserIdAndOrderStatus(Long userId, OrderStatus orderStatus);
}
