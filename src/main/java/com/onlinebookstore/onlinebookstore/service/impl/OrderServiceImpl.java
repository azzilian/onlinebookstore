package com.onlinebookstore.onlinebookstore.service.impl;

import com.onlinebookstore.onlinebookstore.dto.order.OrderItemResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderRequestDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderResponseDto;
import com.onlinebookstore.onlinebookstore.dto.order.OrderUpdateStatusDto;
import com.onlinebookstore.onlinebookstore.exeption.EntityNotFoundException;
import com.onlinebookstore.onlinebookstore.mapper.OrderItemMapper;
import com.onlinebookstore.onlinebookstore.mapper.OrderMapper;
import com.onlinebookstore.onlinebookstore.model.ShoppingCart;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.model.orders.Order;
import com.onlinebookstore.onlinebookstore.model.orders.OrderItem;
import com.onlinebookstore.onlinebookstore.model.orders.OrderStatus;
import com.onlinebookstore.onlinebookstore.repository.OrderRepository;
import com.onlinebookstore.onlinebookstore.repository.ShoppingCartRepository;
import com.onlinebookstore.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.onlinebookstore.service.interfaces.OrderService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto) {
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: "
                        + orderRequestDto.getUserId()));

        Optional<Order> uncompletedOrder = orderRepository
                .findByUserIdAndOrderStatus(user.getId(), OrderStatus.PENDING);
        if (uncompletedOrder.isPresent()) {
            throw new IllegalStateException("User already has an uncompleted order.");
        }

        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id: " + user.getId()));

        Order order = orderMapper.toModel(orderRequestDto);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderItems(cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                }).collect(Collectors.toSet()));
        order.setTotal(cart.getCartItems().stream()
                .map(cartItem -> cartItem
                        .getBook()
                        .getPrice()
                        .multiply(BigDecimal
                                .valueOf(cartItem
                                        .getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto.getShippingAddress());

        Order savedOrder = orderRepository.save(order);

        cart.getCartItems().clear();
        shoppingCartRepository.save(cart);

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Set<OrderResponseDto> getOrderHistory(User user) {
        Set<Order> orders = orderRepository.findByUser(user);
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("No orders found for user id: " + user.getId());
        }
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId,
                                              OrderUpdateStatusDto orderUpdateStatusDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
        order.setOrderStatus(orderUpdateStatusDto.getOrderStatus());

        if (order.getOrderStatus() == OrderStatus.COMPLETED) {
            ShoppingCart cart = shoppingCartRepository
                    .findByUserId(order.getUser().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Shopping cart not found for user id: "
                                    + order.getUser().getId()));
            cart.getCartItems().clear();
            shoppingCartRepository.save(cart);
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.toDto(updatedOrder);
    }

    @Override
    public Set<OrderItemResponseDto> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
        Set<OrderItem> orderItems = order.getOrderItems();
        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long orderItemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "OrderItem not found with id: " + orderItemId));
        return orderItemMapper.toDto(orderItem);
    }
}
