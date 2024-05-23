package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.order.OrderItemResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderRequestDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderUpdateStatusDto;
import com.onlinebookstore.onlinebookstore.service.interfaces.OrderService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponseDto placeOrder(@RequestBody OrderRequestDto orderRequestDto) {
        return orderService.placeOrder(orderRequestDto);
    }

    @GetMapping
    public Set<OrderResponseDto> getOrderHistory(
            @PathVariable Long id) {
        return orderService.getOrderHistory(id);
    }

    @GetMapping("/{orderId}/items")
    public Set<OrderItemResponseDto> getOrderItems(
            @PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getOrderItem(orderId, itemId);
    }

    @PatchMapping("/{id}")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderUpdateStatusDto orderUpdateStatusDto) {
        return orderService.updateOrderStatus(id, orderUpdateStatusDto);
    }
}
