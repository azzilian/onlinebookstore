package com.onlinebookstore.onlinebookstore.service.interfaces;

import com.onlinebookstore.onlinebookstore.dto.order.OrderItemResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderRequestDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderUpdateStatusDto;
import com.onlinebookstore.onlinebookstore.model.User;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    OrderResponseDto placeOrder(OrderRequestDto orderRequestDto);

    Set<OrderResponseDto> getOrderHistory(User user);

    OrderResponseDto updateOrderStatus(Long orderId, OrderUpdateStatusDto orderUpdateStatusDto);

    Set<OrderItemResponseDto> getOrderItems(Long orderId, Long userId);

    OrderItemResponseDto getOrderItem(Long orderId, Long orderItemId, Long userId);
}
