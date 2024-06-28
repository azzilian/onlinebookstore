package com.onlinebookstore.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderRequestDto {
    private Long userId;
    @NotNull(message = "shippingAddress field cannot be empty")
    @Size(min = 1, message = "shippingAddress should be at least 1 character")
    private String shippingAddress;
    private BigDecimal total;
    private LocalDateTime orderDate;
    private Set<OrderItemRequestDto> orderItemDto;
}
