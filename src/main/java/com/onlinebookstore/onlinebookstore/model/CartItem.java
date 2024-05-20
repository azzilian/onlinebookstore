package com.onlinebookstore.onlinebookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "cartitems")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @ManyToOne
    @JoinColumn(name = "shoppingcart_id")
    private ShoppingCart shoppingCart;
    @NotBlank
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    @NotBlank
    private int quantity;
}
