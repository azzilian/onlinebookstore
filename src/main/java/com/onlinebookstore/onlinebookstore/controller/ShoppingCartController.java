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
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class ShoppingCartController {
    private final ShoppingCartService cartService;

    @PostMapping("/{id}/items")
    public ResponseEntity<ShoppingCartResponseDto> addBookToCart(
            @PathVariable Long id,
            @RequestBody CartItemRequestDto cartItemRequestDto) {
        ShoppingCartResponseDto response = cartService.addBookToCart(id, cartItemRequestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingCartResponseDto> getCartById(@PathVariable Long id) {
        ShoppingCartResponseDto cart = cartService.getCartByUser(id);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ShoppingCartResponseDto> updateCartItem(@PathVariable Long cartItemId,
                                                                  @RequestParam int quantity) {
        ShoppingCartResponseDto response = cartService.updateCartItem(cartItemId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> removeBookFromCart(@PathVariable Long cartItemId) {
        cartService.removeBookFromCart(cartItemId);
        return ResponseEntity.ok().build();
    }
}
