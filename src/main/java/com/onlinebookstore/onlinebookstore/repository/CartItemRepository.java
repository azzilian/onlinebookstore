package com.onlinebookstore.onlinebookstore.repository;

import com.onlinebookstore.onlinebookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
