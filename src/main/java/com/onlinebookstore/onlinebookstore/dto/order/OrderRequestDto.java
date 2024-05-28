package com.onlinebookstore.onlinebookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotNull(message = "shippingAddress field cannot be empty")
    @Size(min = 1, message = "shippingAddress should be at least 1 character")
    private String shippingAddress;
}
