package com.onlinebookstore.onlinebookstore.dto.order;

import com.onlinebookstore.onlinebookstore.model.orders.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderUpdateStatusDto {
    @NotNull
    private OrderStatus orderStatus;
}
