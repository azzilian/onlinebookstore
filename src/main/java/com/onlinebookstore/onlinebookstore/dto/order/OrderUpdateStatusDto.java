package com.onlinebookstore.onlinebookstore.dto.order;

import com.onlinebookstore.onlinebookstore.model.orders.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OrderUpdateStatusDto {
    private OrderStatus orderStatus;
}
