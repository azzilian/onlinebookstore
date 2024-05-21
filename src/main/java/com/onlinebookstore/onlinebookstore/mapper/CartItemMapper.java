package com.onlinebookstore.onlinebookstore.mapper;

import com.onlinebookstore.onlinebookstore.dto.shoppingcart.CartItemRequestDto;
import com.onlinebookstore.onlinebookstore.dto.shoppingcart.CartItemResponseDto;
import com.onlinebookstore.onlinebookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toResponseDto(CartItem cartItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toEntity(CartItemRequestDto cartItemRequestDto);
}
