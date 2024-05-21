package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.shoppingcart.CartItemRequestDto;
import com.onlinebookstore.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.onlinebookstore.model.ShoppingCart;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.service.interfaces.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/api/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    public ResponseEntity<ShoppingCartResponseDto> getCartByUser() {
        Long userId = getCurrentUserId();
        ShoppingCartResponseDto cart = shoppingCartService.getCartByUser(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping
    @Operation(summary = "Add book to the shopping cart",
            description = "Add a book to the user's shopping cart")
    public ResponseEntity<ShoppingCartResponseDto> addBookToCart(
            @RequestBody CartItemRequestDto cartItemRequestDto) {
        Long userId = getCurrentUserId();
        ShoppingCartResponseDto response = shoppingCartService
                .addBookToCart(userId, cartItemRequestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cart-items/{cartItemId}")
    public ResponseEntity<ShoppingCartResponseDto> updateCartItem(
            @PathVariable Long cartItemId, @RequestBody CartItemRequestDto cartItemRequestDto) {
        ShoppingCartResponseDto response = shoppingCartService
                .updateCartItem(cartItemId, cartItemRequestDto.getQuantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    public ResponseEntity<Void> removeBookFromCart(@PathVariable Long cartItemId) {
        shoppingCartService.removeBookFromCart(cartItemId);
        return ResponseEntity.ok().build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ((User) userDetails).getId();
        }
        throw new RuntimeException("User not authenticated");
    }
}
