package com.onlinebookstore.onlinebookstore.controller;

import com.onlinebookstore.onlinebookstore.dto.shoppingcart.CartItemRequestDto;
import com.onlinebookstore.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.service.interfaces.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
@Tag(name = "Shopping cart management", description = "Endpoints to manage shopping cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "get all books from users cart",
            description = "GET request, as result you will see - "
                    + " Cart Id, current user Id"
                    + "SET of cartItems with books Id, quantity in cart"
                    + " and titles")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ShoppingCartResponseDto getCartByUser(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getCartByUser(user.getId());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Add book to the shopping cart",
            description = "Add a book to SET cartItems")
    public ShoppingCartResponseDto addBookToCart(Authentication authentication,
            @Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService
                .addBookToCart(user.getId(), cartItemRequestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Change quantity of books in cart",
            description = "Put using quantity : int to change quantity")
    public ShoppingCartResponseDto updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService
                .updateCartItem(cartItemId, cartItemRequestDto.getQuantity());
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete Book from cart",
            description = "Delete cartItem by Id")
    public void removeBookFromCart(@PathVariable Long cartItemId) {
        shoppingCartService.removeBookFromCart(cartItemId);
    }
}
