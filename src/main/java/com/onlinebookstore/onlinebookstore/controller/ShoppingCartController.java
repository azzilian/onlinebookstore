package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.shoppingcart.CartItemRequestDto;
import com.onlinebookstore.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.onlinebookstore.service.interfaces.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @PostMapping
    public ResponseEntity<ShoppingCartResponseDto> addBookToCart(
            @RequestParam Long userId,
            @RequestBody CartItemRequestDto cartItemRequestDto) {
        ShoppingCartResponseDto response = cartService.addBookToCart(userId, cartItemRequestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ShoppingCartResponseDto> getCartByUser(@RequestParam Long userId) {
        ShoppingCartResponseDto cart = cartService.getCartByUser(userId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/cart-items/{cartItemId}")
    public ResponseEntity<ShoppingCartResponseDto> updateCartItem(@PathVariable Long cartItemId,
                                                                  @RequestParam int quantity) {
        ShoppingCartResponseDto response = cartService.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public ResponseEntity<Void> removeBookFromCart(@PathVariable Long cartItemId) {
        cartService.removeBookFromCart(cartItemId);
        return ResponseEntity.ok().build();
    }
}
