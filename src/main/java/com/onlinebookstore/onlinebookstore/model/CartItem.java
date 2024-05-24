package com.onlinebookstore.onlinebookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter
@Getter
@Table(name = "cartitems")
@EqualsAndHashCode
@ToString
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shoppingcart_id",
            referencedColumnName = "id",
            nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private ShoppingCart shoppingCart;

    @ManyToOne
    @JoinColumn(name = "book_id",
            referencedColumnName = "id",
            nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Book book;

    @Column(nullable = false)
    private int quantity;
}
