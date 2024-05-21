package com.onlinebookstore.onlinebookstore.service.impl;

import com.onlinebookstore.onlinebookstore.dto.shoppingcart.CartItemRequestDto;
import com.onlinebookstore.onlinebookstore.dto.shoppingcart.ShoppingCartResponseDto;
import com.onlinebookstore.onlinebookstore.exeption.EntityNotFoundException;
import com.onlinebookstore.onlinebookstore.mapper.CartItemMapper;
import com.onlinebookstore.onlinebookstore.mapper.ShoppingCartMapper;
import com.onlinebookstore.onlinebookstore.model.Book;
import com.onlinebookstore.onlinebookstore.model.CartItem;
import com.onlinebookstore.onlinebookstore.model.ShoppingCart;
import com.onlinebookstore.onlinebookstore.model.User;
import com.onlinebookstore.onlinebookstore.repository.BookRepository;
import com.onlinebookstore.onlinebookstore.repository.CartItemRepository;
import com.onlinebookstore.onlinebookstore.repository.ShoppingCartRepository;
import com.onlinebookstore.onlinebookstore.repository.UserRepository;
import com.onlinebookstore.onlinebookstore.service.interfaces.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getCartByUser(Long userId) {
        logger.info("Fetching cart for user with id {}", userId);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createShoppingCartForUser(userId));
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addBookToCart(Long userId,
                                                 CartItemRequestDto cartItemRequestDto) {
        logger.info("Adding book to cart for user with id {}", userId);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseGet(() -> createShoppingCartForUser(userId));

        Book book = bookRepository.findById(cartItemRequestDto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        cartItem = cartItemRepository.save(cartItem);

        shoppingCart.getCartItems().add(cartItem);
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(Long cartItemId, int quantity) {
        logger.info("Updating cart item with id {}", cartItemId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toResponseDto(cartItem.getShoppingCart());
    }

    @Override
    public void removeBookFromCart(Long cartItemId) {
        logger.info("Removing cart item with id {}", cartItemId);
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItemRepository.delete(cartItem);
    }

    @Override
    public ShoppingCart createShoppingCartForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }
}
