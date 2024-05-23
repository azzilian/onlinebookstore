package com.onlinebookstore.onlinebookstore.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemRequestDto {
    @Min(value = 1, message = "minimal quantity is at least 1")
    private Long bookId;
    @NotNull(message = "quantity field cannot be empty")
    @Min(value = 1, message = "minimal quantity is at least 1")
    private int quantity;
}
