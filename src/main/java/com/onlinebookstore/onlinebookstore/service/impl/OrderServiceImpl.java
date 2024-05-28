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
import com.onlinebookstore.onlinebookstore.service.interfaces.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService shoppingCartService;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto orderRequestDto, User user) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user id: " + user.getId()));

        Order order = createOrderFromCart(orderRequestDto, cart, user);

        Order savedOrder = orderRepository.save(order);

        shoppingCartService.clearCartByUserId(user.getId());

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public Set<OrderResponseDto> getOrderHistory(User user) {
        Set<Order> orders = orderRepository.findByUser(user);
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("No orders found for user id: "
                    + user.getId());
        }
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId,
                                              OrderUpdateStatusDto orderUpdateStatusDto) {
        Order order = checkOrder(orderId);

        logger.info("Updating order status to: " + orderUpdateStatusDto.getOrderStatus());

        order.setOrderStatus(orderUpdateStatusDto.getOrderStatus());

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toDto(updatedOrder);
    }

    @Override
    public Set<OrderItemResponseDto> getOrderItems(Long orderId,
                                                   Long userId) {
        Order order = checkOrder(orderId);

        Set<OrderItem> orderItems = order.getOrderItems();

        if (!order.getUser().getId().equals(userId)) {
            throw new SecurityException("You do not have permission to see this order items");
        }

        return orderItems.stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long orderItemId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
        if (!order.getUser().getId().equals(userId)) {
            throw new SecurityException(
                    "You do not have permission to see this order items");
        }
        OrderItem orderItem = order.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "OrderItem not found with id: " + orderItemId));
        return orderItemMapper.toDto(orderItem);
    }

    private Order createOrderFromCart(OrderRequestDto orderRequestDto,
                                      ShoppingCart cart,
                                      User user) {
        Order order = orderMapper.toModel(orderRequestDto);
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);

        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                }).collect(Collectors.toSet());

        order.setOrderItems(orderItems);

        BigDecimal total = cart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem
                                .getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotal(total);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderRequestDto
                .getShippingAddress());

        return order;
    }

    private Order checkOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found with id: " + orderId));
    }
}
