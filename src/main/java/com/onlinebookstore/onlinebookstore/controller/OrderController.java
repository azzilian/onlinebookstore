package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.order.OrderItemResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderRequestDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderUpdateStatusDto;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.service.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@RequestBody OrderRequestDto orderRequestDto,
                                                       @AuthenticationPrincipal User user) {
        orderRequestDto.setUserId(user.getId());
        OrderResponseDto orderResponse = orderService.placeOrder(orderRequestDto);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<Set<OrderResponseDto>> getOrderHistory(@AuthenticationPrincipal User user) {
        Set<OrderResponseDto> orderHistory = orderService.getOrderHistory(user);
        return ResponseEntity.ok(orderHistory);
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId,
                                                              @RequestBody OrderUpdateStatusDto orderUpdateStatusDto) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, orderUpdateStatusDto);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{orderId}/items")
    public ResponseEntity<Set<OrderItemResponseDto>> getOrderItems(@PathVariable Long orderId) {
        Set<OrderItemResponseDto> orderItems = orderService.getOrderItems(orderId);
        return ResponseEntity.ok(orderItems);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderItemResponseDto> getOrderItem(@PathVariable Long orderId,
                                                             @PathVariable Long itemId) {
        OrderItemResponseDto orderItem = orderService.getOrderItem(orderId, itemId);
        return ResponseEntity.ok(orderItem);
    }
}
