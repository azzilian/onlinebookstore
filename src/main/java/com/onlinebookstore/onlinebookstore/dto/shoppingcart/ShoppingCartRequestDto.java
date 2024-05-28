package com.onlinebookstore.onlinebookstore.dto.shoppingcart;

import java.util.Set;
import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    private Long userId;
    private Set<CartItemRequestDto> cartItems;
}
