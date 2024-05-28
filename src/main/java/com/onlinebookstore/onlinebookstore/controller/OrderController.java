package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.order.OrderItemResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderRequestDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderUpdateStatusDto;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.service.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Tag(name = "Order endpoints management", description = "Endpoints to manage shopping cart")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get all orders", description = " User can view all order "
            + "their details and status")
    public Set<OrderResponseDto> getOrderHistory(
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderHistory(user);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get a all items in order", description = " User can view "
            + "all items in order")
    public Set<OrderItemResponseDto> getOrderItems(
            @PathVariable Long orderId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItems(orderId, user.getId());
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get a specific item in order", description = " User can view a specific "
            + "item in  order and its details")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItem(orderId, itemId, user.getId());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create order", description = "Creating new order"
            + " based on cartItems in users cart, after order created - "
            + "all cartitems are removed from cart, status became PENDING")
    public OrderResponseDto placeOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto,
            @AuthenticationPrincipal User user) {
        return orderService.placeOrder(orderRequestDto, user);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Order status update", description = " PATCH update order status"
            + "by admin, status can be PENDING, COMPLETED, DELIVERING")
    public OrderResponseDto updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderUpdateStatusDto orderUpdateStatusDto) {
        return orderService.updateOrderStatus(orderId,
                orderUpdateStatusDto);
    }
}
