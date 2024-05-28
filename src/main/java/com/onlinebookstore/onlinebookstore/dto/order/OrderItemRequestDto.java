package com.onlinebookstore.onlinebookstore.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemRequestDto {
    @Min(value = 1, message = "minimal quantity is at least 1")
    private Long bookId;
    @NotNull(message = "quantity field cannot be empty")
    @Min(value = 1, message = "quantity cannot be lover than 1")
    private int quantity;
    @NotNull(message = "price field cannot be empty")
    @Min(value = 0, message = "price cannot be lover than 0")
    private BigDecimal price;
}
