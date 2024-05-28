package com.onlinebookstore.onlinebookstore.service.interfaces;

import com.onlinebookstore.onlinebookstore.dto.shoppingcart.CartItemRequestDto;
import com.onlinebookstore.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.onlinebookstore.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCartByUser(Long userId);

    ShoppingCartResponseDto addBookToCart(Long userId, CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long cartItemId, int quantity, Long userId);

    void removeBookFromCart(Long cartItemId, Long userId);

    ShoppingCart createShoppingCartForUser(Long userId);

    void clearCartByUserId(Long userId);
}
