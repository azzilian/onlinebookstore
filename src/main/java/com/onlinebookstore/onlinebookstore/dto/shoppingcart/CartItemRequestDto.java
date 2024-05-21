package com.onlinebookstore.onlinebookstore.dto.shoppingcart;

import lombok.Data;

@Data
public class CartItemRequestDto {
    private Long bookId;
    private int quantity;
}
